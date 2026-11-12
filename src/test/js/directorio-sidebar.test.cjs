const test = require("node:test");
const assert = require("node:assert/strict");
const fs = require("node:fs");
const path = require("node:path");
const vm = require("node:vm");
const source = fs.readFileSync(path.join(__dirname, "../../main/resources/static/js/directorio/directorio-sidebar.js"), "utf8");

function fixture(large) {
    const classes = new Set();
    const elements = {};
    function node() {
        return { attributes: {}, events: {}, classList: {
            contains: name => classes.has(name),
            remove: name => classes.delete(name),
            toggle(name) { if (classes.has(name)) classes.delete(name); else classes.add(name); }
        },
        setAttribute(key, value) { this.attributes[key] = value; },
        addEventListener(key, callback) { this.events[key] = callback; },
        focus() { this.focused = true; } };
    }
    ["directorySidebarToggle", "sidebar", "directorySidebarBackdrop", "sidebarDirectorio"].forEach(id => { elements[id] = node(); });
    const media = { matches: large };
    const windowEvents = {};
    const documentEvents = {};
    const styles = {};
    let headerHeight = 73;
    let modalVisible = false;
    vm.runInNewContext(source, {
        document: {
            body: { classList: node().classList, style: { setProperty(key, value) { styles[key] = value; } } },
            getElementById: id => elements[id],
            querySelector: selector => selector === ".directory-header" ? { getBoundingClientRect: () => ({ height: headerHeight }) }
                : selector === ".modal.show" && modalVisible ? {} : null,
            addEventListener: (event, callback) => { documentEvents[event] = callback; }
        },
        window: { matchMedia: () => media, addEventListener: (event, callback) => { windowEvents[event] = callback; } }
    });
    return { elements, classes, styles, media, windowEvents, documentEvents,
        setHeight(value) { headerHeight = value; }, setModal(value) { modalVisible = value; } };
}

test("menú de escritorio inicia abierto, marca Directorio y permite ocultarse", () => {
    const ui = fixture(true);
    const toggle = ui.elements.directorySidebarToggle;
    assert.equal(toggle.attributes["aria-expanded"], "true");
    assert.equal(ui.elements.sidebar.inert, false);
    assert.equal(ui.elements.sidebarDirectorio.attributes["aria-current"], "page");
    assert.equal(ui.styles["--directory-header-height"], "73px");
    toggle.events.click();
    assert.equal(toggle.attributes["aria-expanded"], "false");
    assert.equal(ui.elements.sidebar.inert, true);
    assert.equal(ui.elements.directorySidebarBackdrop.hidden, true);
    toggle.events.click();
    assert.equal(toggle.attributes["aria-expanded"], "true");
});

test("menú móvil inicia cerrado y se cierra con fondo o Escape, sin cerrar modales", () => {
    const ui = fixture(false);
    const toggle = ui.elements.directorySidebarToggle;
    assert.equal(toggle.attributes["aria-expanded"], "false");
    assert.equal(ui.elements.sidebar.inert, true);
    toggle.events.click();
    assert.equal(ui.elements.directorySidebarBackdrop.hidden, false);
    ui.elements.directorySidebarBackdrop.events.click();
    assert.equal(toggle.attributes["aria-expanded"], "false");
    assert.equal(toggle.focused, true);
    toggle.events.click();
    ui.setModal(true);
    ui.documentEvents.keydown({ key: "Escape" });
    assert.equal(toggle.attributes["aria-expanded"], "true");
    ui.setModal(false);
    ui.documentEvents.keydown({ key: "Escape" });
    assert.equal(toggle.attributes["aria-expanded"], "false");
});

test("grupos desplegables no cierran el menú móvil y navegar sí lo cierra", () => {
    const ui = fixture(false);
    ui.elements.directorySidebarToggle.events.click();
    ui.elements.sidebar.events.click({ target: { closest: () => ({ getAttribute: () => "#" }) } });
    assert.equal(ui.elements.directorySidebarToggle.attributes["aria-expanded"], "true");
    ui.elements.sidebar.events.click({ target: { closest: () => ({ getAttribute: () => "/sigtic/inicio" }) } });
    assert.equal(ui.elements.directorySidebarToggle.attributes["aria-expanded"], "false");
});

test("redimensionar ajusta el alto del encabezado y restablece visibilidad al cambiar de tamaño", () => {
    const ui = fixture(true);
    ui.elements.directorySidebarToggle.events.click();
    ui.media.matches = false;
    ui.setHeight(149);
    ui.windowEvents.resize();
    assert.equal(ui.styles["--directory-header-height"], "149px");
    assert.equal(ui.elements.directorySidebarToggle.attributes["aria-expanded"], "false");
    ui.elements.directorySidebarToggle.events.click();
    ui.media.matches = true;
    ui.windowEvents.resize();
    assert.equal(ui.elements.directorySidebarToggle.attributes["aria-expanded"], "true");
    assert.equal(ui.elements.directorySidebarBackdrop.hidden, true);
});

test("sin controles autenticados no modifica la vista pública", () => {
    assert.doesNotThrow(() => vm.runInNewContext(source, {
        document: { getElementById: () => null }
    }));
});

test("encabezado reserva el regreso al público y coloca el menú después del escudo", () => {
    const html = fs.readFileSync(path.join(__dirname, "../../main/resources/templates/directorio/directorio.html"), "utf8");
    assert.match(html, /<a th:if="\$\{!directorioAuthenticated\}"[^>]*directory-back/);
    assert.ok(html.indexOf('class="directory-government"') < html.indexOf('id="directorySidebarToggle"'));
    assert.match(html, /<i class="bi bi-list" aria-hidden="true"><\/i>/);
    assert.doesNotMatch(html, /id="directorySidebarToggle"[^>]*btn-outline/);
});
