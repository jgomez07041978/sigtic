const test = require("node:test");
const assert = require("node:assert/strict");
const ui = require("../../main/resources/static/js/directorio/directorio-publico.js");
const fs = require("node:fs");
const path = require("node:path");
const vm = require("node:vm");

test("envía la búsqueda unificada sin enviar estado ni filtros separados", () => {
    const params = new URLSearchParams(ui.query({ nombre: " Ana ", telefonoExtension: " 993 ", estado: "HIS" }, 0));
    assert.equal(params.get("nombre"), "Ana");
    assert.equal(params.get("telefonoExtension"), "993");
    assert.equal(params.has("estado"), false);
    assert.equal(params.has("telefono"), false);
    assert.equal(params.has("extension"), false);
    assert.equal(params.get("pagina"), "0");
    assert.equal(params.get("size"), "20");
    assert.equal(params.has("idEntePublico"), false);
});

test("limpiar no envía filtros vacíos y pagina correctamente", () => {
    assert.equal(new URLSearchParams(ui.query({}, 1)).size, 2);
    assert.equal(ui.summary({ number: 1, size: 20, totalElements: 25, content: [1, 2, 3, 4, 5] }),
        "Mostrando 21–25 de 25 resultados");
    assert.equal(ui.summary({ totalElements: 0 }), "No se encontraron contactos.");
});

test("envía el tamaño seleccionado sin perder los filtros ni la página", () => {
    for (const size of [10, 20, 25, 50, 100]) {
        const params = new URLSearchParams(ui.query({ nombre: " Ana ", idEntePublico: "21",
            idUnidadAdministrativa: "11", telefonoExtension: " 993 " }, 3, size));
        assert.equal(params.get("size"), String(size));
        assert.equal(params.get("pagina"), "3");
        assert.equal(params.get("nombre"), "Ana");
        assert.equal(params.get("idEntePublico"), "21");
        assert.equal(params.get("idUnidadAdministrativa"), "11");
        assert.equal(params.get("telefonoExtension"), "993");
    }
});

test("páginas numeradas limitan los botones y conservan inicio, final y página actual", () => {
    assert.deepEqual(ui.pageNumbers(0, 0), [1]);
    assert.deepEqual(ui.pageNumbers(0, 1), [1]);
    assert.deepEqual(ui.pageNumbers(3, 7), [1, 2, 3, 4, 5, 6, 7]);
    assert.deepEqual(ui.pageNumbers(0, 20), [1, 2, 3, 4, 5, null, 20]);
    assert.deepEqual(ui.pageNumbers(9, 20), [1, null, 9, 10, 11, null, 20]);
    assert.deepEqual(ui.pageNumbers(19, 20), [1, null, 16, 17, 18, 19, 20]);
    for (let page = 0; page < 100; page++) {
        const numbers = ui.pageNumbers(page, 100);
        assert.ok(numbers.length <= 7);
        assert.ok(numbers.includes(page + 1));
        assert.equal(numbers[0], 1);
        assert.equal(numbers.at(-1), 100);
        assert.equal(new Set(numbers.filter(Number.isInteger)).size, numbers.filter(Number.isInteger).length);
    }
});

test("controles accesibles navegan y se deshabilitan en límites, carga y resultados vacíos", () => {
    function element() {
        return { children: [], attributes: {}, events: {},
            appendChild(child) { this.children.push(child); },
            replaceChildren() { this.children = []; },
            setAttribute(key, value) { this.attributes[key] = value; },
            addEventListener(key, callback) { this.events[key] = callback; } };
    }
    const doc = { createElement: element };
    const list = element();
    const visited = [];
    const render = (number, totalPages, totalElements = 200, loading = false) => {
        ui.renderPagination(doc, list, { number, totalPages, totalElements }, loading, page => visited.push(page));
        return list.children.map(item => item.children[0]).filter(control => control.events.click);
    };
    let buttons = render(0, 20);
    assert.equal(buttons[0].disabled, true);
    assert.equal(buttons[1].disabled, true);
    const current = buttons.find(button => button.attributes["aria-current"] === "page");
    assert.equal(current.textContent, "1");
    assert.equal(current.disabled, false);
    current.events.click();
    assert.equal(visited.length, 0);
    buttons.find(button => button.attributes["aria-label"] === "Página 3").events.click();
    buttons.at(-2).events.click();
    buttons.at(-1).events.click();
    assert.deepEqual(visited, [2, 1, 19]);
    buttons = render(19, 20);
    assert.equal(buttons.at(-2).disabled, true);
    assert.equal(buttons.at(-1).disabled, true);
    buttons[0].events.click();
    buttons[1].events.click();
    assert.deepEqual(visited.slice(-2), [0, 18]);
    for (const controls of [render(3, 20, 200, true), render(0, 0, 0)]) {
        assert.ok(controls.every(button => button.disabled));
        const before = visited.length;
        controls.forEach(button => button.events.click());
        assert.equal(visited.length, before);
    }
    ui.renderPagination(doc, list, null, false, () => assert.fail("No hay datos para navegar"));
    assert.ok(list.children.every(item => item.children[0].disabled));
});

test("combina teléfono y extensión sin perder datos ni mostrar valores vacíos", () => {
    assert.equal(ui.telefonoExtension({ telefono: " 9931234567 ", extension: " 123 " }), "9931234567 / Ext. 123");
    assert.equal(ui.telefonoExtension({ telefono: "9931234567" }), "9931234567");
    assert.equal(ui.telefonoExtension({ extension: "123" }), "Ext. 123");
    assert.equal(ui.telefonoExtension({ telefono: null, extension: null }), "—");
    assert.equal(ui.telefonoExtension({ telefono: " ", extension: "" }), "—");
    assert.equal(ui.telefonoExtension({}), "—");
});

test("limita enlaces a correo", () => {
    assert.equal(ui.emailLink("javascript:alert(1)"), null);
    assert.equal(ui.emailLink("<img src=x onerror=alert(1)>"), null);
    assert.match(ui.emailLink("contacto@example.test"), /^mailto:/);
});

test("fila y detalle usan texto seguro y no ofrecen edición ni eliminación", () => {
    function element(tag) {
        return {
            tag, children: [], events: {}, attributes: {},
            appendChild(child) { this.children.push(child); },
            setAttribute(key, value) { this.attributes[key] = value; },
            addEventListener(key, handler) { this.events[key] = handler; },
            set innerHTML(value) { throw new Error("No debe inyectarse HTML de contactos"); }
        };
    }
    const contact = { nombreCompleto: "<script>alert(1)</script>", estado: "HIS", puesto: "Analista",
        telefono: "<img src=x>", extension: "123", correo: "contacto@example.test" };
    let detail = null;
    const row = ui.contactRow({ createElement: element }, contact, 1, value => { detail = value; });
    assert.equal(row.children.length, 8);
    assert.equal(row.children[1].textContent, contact.nombreCompleto);
    assert.equal(row.children[5].textContent, "<img src=x> / Ext. 123");
    assert.equal(row.children.some(cell => cell.textContent === "Histórico"), false);
    const action = row.children[7].children[0];
    assert.equal(action.className, "btn btn-primary btn-sm d-inline-flex align-items-center gap-1 text-nowrap");
    assert.equal(action.attributes["aria-label"], "Ver detalle de " + contact.nombreCompleto);
    assert.equal(action.children[0].tag, "i");
    assert.equal(action.children[0].className, "fa-solid fa-eye");
    assert.equal(action.children[0].attributes["aria-hidden"], "true");
    assert.equal(action.children[1].textContent, "Ver");
    action.events.click();
    assert.equal(detail, contact);
});

test("acceso al directorio y regreso al login usan botones azules", () => {
    const login = fs.readFileSync(path.join(__dirname, "../../main/resources/templates/index.html"), "utf8");
    const directory = fs.readFileSync(path.join(__dirname, "../../main/resources/templates/directorio/directorio.html"), "utf8");
    assert.match(login, /<a th:href="@\{\/directorio\}" class="btn btn-primary">Consultar directorio institucional<\/a>/);
    assert.match(directory, /class="btn btn-primary btn-sm directory-back"/);
    assert.match(directory, /include\/fontawesome-free\/css\/all\.min\.css/);
});

function detailFixture(contact) {
    const nodes = [];
    const root = ui.contactDetail({ createElement(tag) {
        const node = { tag, children: [], attributes: {},
            appendChild(child) { this.children.push(child); },
            setAttribute(key, value) { this.attributes[key] = value; },
            set innerHTML(value) { assert.fail("Los contactos no deben inyectarse como HTML"); } };
        nodes.push(node);
        return node;
    } }, contact);
    return { root, nodes, text: nodes.map(node => node.textContent || "").join("\n") };
}

test("iniciales admiten nombres compuestos, acentos y datos ausentes sin inventar fotografías", () => {
    assert.equal(ui.contactInitials(" Ana María López "), "AL");
    assert.equal(ui.contactInitials("  ángel \t ñúñez "), "ÁÑ");
    assert.equal(ui.contactInitials("Ana"), "A");
    assert.equal(ui.contactInitials(""), "—");
    assert.equal(ui.contactInitials(null), "—");
});

test("ficha destaca identidad y organiza solo cuatro tarjetas institucionales con correo seguro", () => {
    const contact = { nombreCompleto: "Ana López", puesto: "Analista", entePublico: "Ente A",
        unidadAdministrativa: "Unidad A", telefono: "0993", extension: "001", correo: " contacto@example.test ",
        rfc: "DATO_PRIVADO", curp: "DATO_PRIVADO", password: "DATO_PRIVADO", estado: "HIS",
        telefonoPersonal: "DATO_PRIVADO", correoPersonal: "DATO_PRIVADO" };
    const { root, nodes, text } = detailFixture(contact);
    assert.equal(root.children.length, 2);
    assert.equal(nodes.find(node => node.tag === "h3").textContent, "Ana López");
    assert.equal(nodes.find(node => node.className === "directory-contact-position").textContent, "Analista");
    assert.equal(nodes.find(node => node.className === "directory-contact-avatar").attributes["aria-hidden"], "true");
    const fields = nodes.filter(node => node.className === "directory-contact-field");
    assert.equal(fields.length, 4);
    assert.deepEqual(fields.map(field => field.children[0].children[1].textContent),
        ["Ente público", "Unidad administrativa", "Teléfono/Extensión", "Correo electrónico"]);
    assert.equal(fields[2].children[1].textContent, "0993 / Ext. 001");
    const link = nodes.find(node => node.tag === "a");
    assert.equal(link.textContent, "contacto@example.test");
    assert.equal(link.href, "mailto:contacto%40example.test");
    assert.doesNotMatch(text, /DATO_PRIVADO|HIS|RFC|CURP|Contraseña|Estado/);
    assert.equal(nodes.some(node => ["input", "button", "img"].includes(node.tag)), false);
});

test("ficha presenta ausencias claramente y no convierte datos maliciosos en HTML ni enlaces", () => {
    const empty = detailFixture({ nombreCompleto: " ", puesto: null, telefono: " ", correo: " " });
    assert.match(empty.text, /Nombre no disponible/);
    assert.match(empty.text, /Puesto no registrado/);
    assert.equal(empty.nodes.filter(node => node.textContent === "Sin información registrada").length, 4);
    assert.equal(empty.nodes.some(node => node.tag === "a"), false);
    const payload = '<img src=x onerror="alert(1)">';
    const hostile = detailFixture({ nombreCompleto: payload, puesto: payload, entePublico: payload,
        unidadAdministrativa: payload, telefono: payload, extension: payload, correo: "javascript:alert(1)" });
    assert.equal(hostile.nodes.find(node => node.tag === "h3").textContent, payload);
    assert.equal(hostile.nodes.filter(node => node.textContent === payload).length, 4);
    assert.equal(hostile.nodes.some(node => ["a", "img", "script"].includes(node.tag)), false);
});

test("muestra contactos sin esperar los filtros y conserva la tabla si los filtros fallan", async () => {
    const elements = {};
    function element(tag) {
        return { tag, value: "", children: [], attributes: {}, events: {},
            appendChild(child) { this.children.push(child); },
            replaceChildren() { this.children = []; },
            setAttribute(key, value) { this.attributes[key] = value; },
            addEventListener(key, callback) { this.events[key] = callback; } };
    }
    const document = {
        getElementById(id) { return elements[id] || (elements[id] = element(id)); },
        createElement: element
    };
    document.getElementById("contextSigtic").value = "/sigtic";
    let resolveOptions;
    const options = new Promise(resolve => { resolveOptions = resolve; });
    const requests = [];
    vm.runInNewContext(fs.readFileSync(path.join(__dirname, "../../main/resources/static/js/directorio/directorio-publico.js"), "utf8"), {
        document, window: {}, URLSearchParams,
        bootstrap: { Modal: function () { this.hide = () => {}; } },
        fetch: async url => {
            requests.push(url);
            if (url.endsWith("/opciones")) return options;
            return { ok: true, json: async () => ({ number: 0, size: 20, totalElements: 1, totalPages: 1,
                content: [{ nombreCompleto: "Contacto de prueba" }], first: true, last: true }) };
        }
    });
    await new Promise(resolve => setImmediate(resolve));
    assert.equal(requests.length, 2);
    assert.ok(requests[0].includes("/contactos?"));
    assert.equal(elements.directoryRows.children.length, 1);
    assert.equal(elements.directorySummary.textContent, "Mostrando 1–1 de 1 resultados");
    assert.equal(elements.directoryLoading.hidden, true);
    resolveOptions({ ok: false });
    await new Promise(resolve => setImmediate(resolve));
    assert.equal(elements.directoryRows.children.length, 1);
    assert.equal(elements.directorySummary.textContent, "Mostrando 1–1 de 1 resultados");
    assert.equal(elements.directoryLoading.hidden, true);
    assert.equal(elements.directoryError.hidden, false);
    assert.match(elements.directoryError.textContent, /filtros del directorio/);
});

test("árbol conserva jerarquía, claves y selección solo dentro del ente", () => {
    const nodes = ui.unitTree([
        { id: 1, idEntePublico: 7, idPadre: null, nombre: "Secretaría" },
        { id: 2, idEntePublico: 7, idPadre: 1, nombre: "Dirección", clave: " 001 " },
        { id: 3, idEntePublico: 8, idPadre: 1, nombre: "Otro ente" }
    ], "7", "2");
    assert.equal(nodes.length, 2);
    assert.equal(nodes[0].parent, "#");
    assert.equal(nodes[1].parent, "1");
    assert.equal(nodes[1].text, "( 001 ) Dirección");
    assert.equal(nodes[1].state.selected, true);
    assert.equal(nodes[0].state.opened, true);
    assert.equal(ui.unitTree([], "", "").length, 0);
});

test("árbol no pierde unidades con padres externos, inexistentes o ciclos", () => {
    const nodes = ui.unitTree([
        { id: 1, idEntePublico: 7, idPadre: 1, nombre: "Raíz" },
        { id: 2, idEntePublico: 7, idPadre: 3, nombre: "Ciclo A" },
        { id: 3, idEntePublico: 7, idPadre: 2, nombre: "Ciclo B" },
        { id: 4, idEntePublico: 7, idPadre: 99, nombre: "Huérfana" },
        { id: 5, idEntePublico: 7, idPadre: 6, nombre: "<img src=x onerror=alert(1)>" },
        { id: 6, idEntePublico: 8, nombre: "Otro ente" }
    ], "7");
    assert.equal(nodes.length, 5);
    assert.ok(nodes.every(node => node.parent === "#"));
    assert.equal(nodes[4].text, "<img src=x onerror=alert(1)>");
    assert.equal(ui.normalizeSearch("Áreas de EDUCACIÓN"), "areas de educacion");
});

for (const enhanced of [false, true]) {
test("página completa busca y limpia con " + (enhanced ? "Select2" : "selector nativo"), async () => {
    const elements = {};
    const requests = [];
    const ids = ["directoryFilters", "contextSigtic", "directoryRows", "directoryError", "directoryPrevious",
        "directoryNext", "directoryDetail", "directoryLoading", "directoryResults", "directorySummary",
        "directoryPage", "directoryDetailFields", "directoryClear", "nombre", "idEntePublico",
        "idUnidadAdministrativa", "telefonoExtension", "directoryEnteField",
        "directoryUnitField", "directoryUnitHint", "directoryUnitOpen", "directoryUnitName",
        "directoryUnitModal", "directoryUnitSearch", "directoryUnitTree", "directoryUnitEmpty", "directoryExportUnits",
        "directoryExportUsers", "directoryExportUnitUsers", "directoryPagination", "directoryPageSize"];
    const downloads = [];
    let exportFails = false;
    let paginationTotal = null;
    let contactsFail = false;
    function element(tag) {
        return {
            tag, value: "", children: [], events: {}, attributes: {},
            appendChild(child) { this.children.push(child); },
            replaceChildren() { this.children = []; },
            setAttribute(key, value) { this.attributes[key] = value; },
            addEventListener(key, callback) { this.events[key] = callback; },
            focus() { this.focused = true; },
            showPicker() { this.pickerOpened = true; },
            click() { if (tag === "a") downloads.push({ href: this.href, filename: this.download }); },
            remove() {},
            set innerHTML(value) { throw new Error("No debe usarse HTML para datos del contacto"); }
        };
    }
    ids.forEach(id => { elements[id] = element(id); });
    const defaultEnte = element("option");
    defaultEnte.value = "";
    defaultEnte.textContent = "Seleccione";
    elements.idEntePublico.appendChild(defaultEnte);
    elements.contextSigtic.value = "/sigtic";
    const keys = ["nombre", "idEntePublico", "idUnidadAdministrativa", "telefonoExtension"];
    elements.directoryFilters.reset = () => keys.forEach(key => { elements[key].value = ""; });
    let detailOpened = 0;
    let unitOpened = 0;
    let unitHidden = 0;
    function controlFor(select) {
        return {
            events: {}, calls: [],
            select2(options) {
                this.calls.push(options);
                if (typeof options === "object") this.options = options;
                return this;
            },
            on(event, callback) { this.events[event] = callback; return this; },
            val(value) { select.value = value; return this; },
            trigger(event) {
                this.calls.push(event);
                this.selectedLabel = select.children.find(option => option.value === select.value).textContent;
                return this;
            }
        };
    }
    const searchInput = element("input");
    const enteControl = controlFor(elements.idEntePublico);
    const tree = {
        events: {}, calls: [],
        jstree(options) {
            this.calls.push(options);
            if (options === true) return treeInstance;
            if (typeof options === "object") this.options = options;
            return this;
        },
        on(event, selector, handler) { this.events[event] = handler || selector; return this; },
        off() { this.events = {}; return this; }
    };
    const treeInstance = {
        get_node(id) { return tree.options.core.data.find(node => node.id === id); },
        search(term) {
            const nodes = tree.options.core.data.filter(node => tree.options.search.search_callback(term, node));
            tree.events["search.jstree.directoryUnits"]({}, { nodes });
        },
        clear_search() { tree.calls.push("clear_search"); }
    };
    function jquery(target) {
        if (target === elements.idEntePublico) return enteControl;
        if (target === elements.directoryUnitTree) return tree;
        return {};
    }
    jquery.fn = { select2() {}, jstree() {} };
    const contacts = [
        { nombreCompleto: "Ana", estado: "VIG", telefono: "9931112233", extension: "123", id: 1 },
        { nombreCompleto: "Zoe", estado: "HIS", telefono: "9935556677", extension: "4321", id: 2 }
    ];
    vm.runInNewContext(fs.readFileSync(path.join(__dirname, "../../main/resources/static/js/directorio/directorio-publico.js"), "utf8"), {
        document: { getElementById: id => elements[id], createElement: element, body: { appendChild() {} },
            querySelector: selector => selector === "#directoryEnteField .select2-search__field" ? searchInput : null },
        window: enhanced ? { jQuery: jquery } : {},
        bootstrap: { Modal: function (host) {
            this.show = () => { if (host === elements.directoryDetail) detailOpened++; else unitOpened++; };
            this.hide = () => { if (host === elements.directoryUnitModal) unitHidden++; };
        } },
        URLSearchParams,
        URL: { createObjectURL: () => "blob:test-catalog", revokeObjectURL() {} },
        setTimeout: callback => callback(),
        FormData: function () { this.forEach = callback => keys.filter(key => !elements[key].disabled)
            .forEach(key => callback(elements[key].value, key)); },
        fetch: async url => {
            requests.push(url);
            if (url.includes("/unidades.xlsx?") || url.includes("/usuarios.xlsx?") || url.includes("/usuarios-unidad.xlsx?"))
                return { ok: !exportFails, blob: async () => ({}) };
            const params = new URL(url, "http://localhost").searchParams;
            if (!url.endsWith("/opciones")) {
                if (contactsFail) return { ok: false };
                if (paginationTotal !== null) {
                    const number = Number(params.get("pagina"));
                    const size = Number(params.get("size"));
                    const totalPages = Math.ceil(paginationTotal / size);
                    const content = Array.from({ length: Math.max(0, Math.min(size, paginationTotal - number * size)) },
                        (_, index) => contacts[index % contacts.length]);
                    return { ok: true, json: async () => ({ number, size, totalElements: paginationTotal,
                        totalPages, content, first: number === 0, last: number >= totalPages - 1 }) };
                }
            }
            const search = params.get("telefonoExtension");
            const content = contacts.filter(contact => !search || contact.telefono.includes(search) || contact.extension.includes(search));
            return { ok: true, json: async () => url.endsWith("/opciones")
                ? { entes: [{ id: 1, nombre: "Ente A" }, { id: 2, nombre: "Ente B" }, { id: 3, nombre: "Ente sin unidades" }],
                    unidades: [{ id: 11, nombre: "Unidad A", idEntePublico: 1, idPadre: null, clave: "001" },
                        { id: 12, nombre: "Unidad Áreas de operación", idEntePublico: 1, idPadre: 11, clave: "002" },
                        { id: 22, nombre: "Unidad B", idEntePublico: 2, idPadre: null, clave: "003" }] }
                : { number: 0, size: 20, content, totalElements: content.length, totalPages: 1, first: true, last: true } };
        }
    });
    const flush = () => new Promise(resolve => setImmediate(resolve));
    await flush();
    assert.equal(elements.directoryRows.children.length, 2);
    assert.equal(elements.directoryLoading.hidden, true);
    assert.equal(elements.idUnidadAdministrativa.children.length, 1);
    assert.equal(elements.idUnidadAdministrativa.disabled, true);
    assert.equal(elements.directoryUnitOpen.disabled, true);
    assert.equal(elements.directoryExportUnits.disabled, true);
    assert.equal(elements.directoryExportUsers.disabled, true);
    assert.equal(elements.directoryExportUnitUsers.disabled, true);
    elements.directoryUnitOpen.events.click();
    assert.equal(unitOpened, 0);
    assert.equal(elements.idUnidadAdministrativa.pickerOpened, undefined);
    if (enhanced) {
        assert.equal(enteControl.options.theme, "bootstrap-5");
        assert.equal(enteControl.options.minimumResultsForSearch, 0);
        assert.equal(enteControl.options.width, "100%");
        assert.equal(enteControl.options.language.noResults(), "No se encontraron entes públicos.");
        enteControl.events["select2:open"]();
        assert.equal(searchInput.attributes["aria-label"], "Buscar ente público");
        assert.equal(searchInput.focused, true);
    }
    function chooseEnte(id) {
        elements.idEntePublico.value = id;
        if (enhanced) enteControl.events.change();
        else elements.idEntePublico.events.change();
    }
    chooseEnte("1");
    assert.equal(elements.idUnidadAdministrativa.children.length, 3);
    assert.equal(elements.idUnidadAdministrativa.children[1].value, "11");
    assert.equal(elements.idUnidadAdministrativa.disabled, false);
    assert.equal(elements.directoryUnitOpen.disabled, false);
    assert.equal(elements.directoryExportUnits.disabled, false);
    assert.equal(elements.directoryExportUsers.disabled, false);
    assert.equal(elements.directoryExportUnitUsers.disabled, true);
    const beforeUnitSelection = requests.length;
    assert.equal(elements.directoryExportUnitUsers.events.click(), undefined);
    assert.equal(requests.length, beforeUnitSelection);
    const download = elements.directoryExportUnits.events.click();
    assert.equal(elements.directoryExportUnits.disabled, true);
    assert.equal(elements.directoryExportUsers.disabled, false);
    assert.equal(elements.directoryExportUnits.events.click(), undefined);
    await download;
    assert.equal(downloads.length, 1);
    assert.equal(downloads[0].filename, "unidades_administrativas_1.xlsx");
    const exportUrl = new URL(requests.at(-1), "http://localhost");
    assert.equal(exportUrl.pathname, "/sigtic/api/directorio/unidades.xlsx");
    assert.deepEqual([...exportUrl.searchParams.keys()], ["idEntePublico"]);
    assert.equal(exportUrl.searchParams.get("idEntePublico"), "1");
    exportFails = true;
    await elements.directoryExportUnits.events.click();
    assert.equal(elements.directoryError.hidden, false);
    assert.match(elements.directoryError.textContent, /No se pudo descargar/);
    assert.equal(elements.directoryExportUnits.disabled, false);
    assert.equal(downloads.length, 1);
    exportFails = false;
    elements.nombre.value = "Filtro que no debe limitar el Excel";
    elements.telefonoExtension.value = "999";
    elements.idUnidadAdministrativa.value = "11";
    const usersDownload = elements.directoryExportUsers.events.click();
    assert.equal(elements.directoryExportUsers.disabled, true);
    assert.equal(elements.directoryExportUsers.attributes["aria-busy"], "true");
    assert.equal(elements.directoryExportUsers.events.click(), undefined);
    assert.equal(elements.directoryExportUnits.disabled, false);
    chooseEnte("2");
    assert.equal(elements.directoryExportUsers.disabled, true);
    await usersDownload;
    assert.equal(downloads.length, 2);
    assert.equal(downloads[1].filename, "usuarios_1.xlsx");
    assert.equal(elements.directoryExportUsers.disabled, false);
    assert.equal(elements.directoryExportUsers.attributes["aria-busy"], "false");
    const usersUrl = new URL(requests.at(-1), "http://localhost");
    assert.equal(usersUrl.pathname, "/sigtic/api/directorio/usuarios.xlsx");
    assert.deepEqual([...usersUrl.searchParams.entries()], [["idEntePublico", "1"]]);
    exportFails = true;
    await elements.directoryExportUsers.events.click();
    assert.equal(elements.directoryError.hidden, false);
    assert.match(elements.directoryError.textContent, /No se pudo descargar los usuarios/);
    assert.equal(elements.directoryExportUsers.disabled, false);
    assert.equal(downloads.length, 2);
    exportFails = false;
    chooseEnte("1");
    elements.nombre.value = "";
    elements.telefonoExtension.value = "";
    elements.directoryUnitOpen.events.click();
    assert.equal(unitOpened, 1);
    if (enhanced) {
        assert.equal(tree.options.core.force_text, true);
        assert.equal(tree.options.core.data.length, 2);
        assert.equal(tree.options.core.data[1].parent, "11");
        elements.directoryUnitModal.events["shown.bs.modal"]();
        assert.equal(elements.directoryUnitSearch.focused, true);
        elements.directoryUnitSearch.value = "areas";
        elements.directoryUnitSearch.events.input();
        assert.equal(elements.directoryUnitEmpty.hidden, true);
        elements.directoryUnitSearch.value = "zz_no_existe";
        elements.directoryUnitSearch.events.input();
        assert.equal(elements.directoryUnitEmpty.hidden, false);
        tree.events["dblclick.directoryUnits"]({ currentTarget: "11" });
        assert.equal(elements.idUnidadAdministrativa.value, "11");
        assert.equal(elements.directoryUnitName.value, "Unidad A");
        const hiddenBefore = unitHidden;
        elements.directoryUnitOpen.events.click();
        assert.equal(tree.options.core.data[0].state.selected, true);
        elements.directoryUnitModal.events["hidden.bs.modal"]();
        assert.equal(elements.directoryUnitSearch.value, "");
        assert.equal(elements.idUnidadAdministrativa.value, "11");
        tree.events["keydown.directoryUnits"]({ key: "Enter", currentTarget: "12", preventDefault() {} });
        assert.equal(elements.idUnidadAdministrativa.value, "12");
        assert.ok(unitHidden > hiddenBefore);
        tree.events["dblclick.directoryUnits"]({ currentTarget: "11" });
    } else {
        assert.equal(elements.directoryUnitEmpty.hidden, false);
        assert.equal(elements.directoryUnitEmpty.textContent, "No se pudo cargar el árbol de unidades. Recarga la página.");
        elements.idUnidadAdministrativa.value = "11";
        elements.idUnidadAdministrativa.events.change();
    }
    assert.equal(elements.directoryExportUnitUsers.disabled, false);
    elements.nombre.value = "No debe filtrar usuarios de unidad";
    elements.telefonoExtension.value = "999";
    const unitUsersDownload = elements.directoryExportUnitUsers.events.click();
    assert.equal(elements.directoryExportUnitUsers.disabled, true);
    assert.equal(elements.directoryExportUnitUsers.attributes["aria-busy"], "true");
    assert.equal(elements.directoryExportUnitUsers.events.click(), undefined);
    const unitUsersUrl = new URL(requests.at(-1), "http://localhost");
    assert.equal(unitUsersUrl.pathname, "/sigtic/api/directorio/usuarios-unidad.xlsx");
    assert.deepEqual([...unitUsersUrl.searchParams.entries()],
        [["idEntePublico", "1"], ["idUnidadAdministrativa", "11"]]);
    chooseEnte("2");
    await unitUsersDownload;
    assert.equal(downloads.at(-1).filename, "usuarios_unidad_1_11.xlsx");
    assert.equal(elements.directoryExportUnitUsers.disabled, true);
    assert.equal(elements.directoryExportUnitUsers.attributes["aria-busy"], "false");
    chooseEnte("1");
    elements.idUnidadAdministrativa.value = "12";
    elements.idUnidadAdministrativa.events.change();
    exportFails = true;
    const beforeFailedDownload = downloads.length;
    await elements.directoryExportUnitUsers.events.click();
    assert.equal(elements.directoryError.hidden, false);
    assert.match(elements.directoryError.textContent, /No se pudo descargar los usuarios de la unidad/);
    assert.equal(downloads.length, beforeFailedDownload);
    assert.equal(elements.directoryExportUnitUsers.disabled, false);
    exportFails = false;
    elements.idUnidadAdministrativa.value = "11";
    elements.idUnidadAdministrativa.events.change();
    elements.nombre.value = "";
    elements.telefonoExtension.value = "";
    elements.directoryFilters.events.submit({ preventDefault() {} });
    await flush();
    assert.equal(new URL(requests.at(-1), "http://localhost").searchParams.get("idUnidadAdministrativa"), "11");
    chooseEnte("2");
    assert.equal(elements.idUnidadAdministrativa.value, "");
    assert.equal(elements.directoryExportUnitUsers.disabled, true);
    assert.equal(elements.directoryUnitName.value, "");
    if (enhanced) assert.ok(tree.calls.includes("destroy"));
    assert.equal(elements.idUnidadAdministrativa.children[1].value, "22");
    assert.equal(elements.idUnidadAdministrativa.children.some(option => option.value === "11"), false);
    elements.directoryFilters.events.submit({ preventDefault() {} });
    await flush();
    assert.equal(new URL(requests.at(-1), "http://localhost").searchParams.has("idUnidadAdministrativa"), false);
    chooseEnte("3");
    assert.equal(elements.idUnidadAdministrativa.children.length, 1);
    assert.equal(elements.idUnidadAdministrativa.children[0].textContent, "Sin unidades disponibles");
    assert.equal(elements.idUnidadAdministrativa.disabled, true);
    assert.equal(elements.directoryUnitOpen.disabled, true);
    assert.equal(elements.directoryUnitName.disabled, true);
    assert.equal(elements.directoryExportUnits.disabled, true);
    assert.equal(elements.directoryExportUsers.disabled, false);
    await elements.directoryExportUsers.events.click();
    assert.equal(downloads.at(-1).filename, "usuarios_3.xlsx");
    assert.equal(elements.directoryUnitName.placeholder, "Sin unidades disponibles");
    assert.equal(elements.directoryUnitHint.textContent, "Este ente público no tiene unidades administrativas.");
    chooseEnte("");
    assert.equal(elements.idUnidadAdministrativa.disabled, true);
    assert.equal(elements.directoryExportUsers.disabled, true);
    assert.equal(elements.directoryUnitHint.textContent, "Selecciona primero un ente público.");
    chooseEnte("1");
    elements.telefonoExtension.value = " 4321 ";
    elements.directoryFilters.events.submit({ preventDefault() {} });
    await flush();
    assert.match(requests.at(-1), /telefonoExtension=4321/);
    assert.equal(new URL(requests.at(-1), "http://localhost").searchParams.get("idEntePublico"), "1");
    assert.equal(new URL(requests.at(-1), "http://localhost").searchParams.has("estado"), false);
    assert.equal(elements.directoryRows.children.length, 1);
    assert.equal(elements.directoryRows.children[0].children[5].textContent, "9935556677 / Ext. 4321");
    elements.directoryRows.children[0].children[7].children[0].events.click();
    assert.equal(detailOpened, 1);
    assert.equal(elements.directoryDetailFields.children.length, 1);
    let detail = elements.directoryDetailFields.children[0];
    assert.equal(detail.children[0].children[1].children[1].textContent, "Zoe");
    assert.equal(detail.children[1].children.length, 4);
    assert.equal(detail.children[1].children[2].children[0].children[1].textContent, "Teléfono/Extensión");
    assert.equal(detail.children[1].children[2].children[1].textContent, "9935556677 / Ext. 4321");
    elements.telefonoExtension.value = "1112233";
    elements.directoryFilters.events.submit({ preventDefault() {} });
    await flush();
    assert.equal(elements.directoryRows.children.length, 1);
    assert.equal(elements.directoryRows.children[0].children[1].textContent, "Ana");
    elements.directoryRows.children[0].children[7].children[0].events.click();
    assert.equal(detailOpened, 2);
    assert.equal(elements.directoryDetailFields.children.length, 1);
    detail = elements.directoryDetailFields.children[0];
    assert.equal(detail.children[0].children[1].children[1].textContent, "Ana");
    assert.equal(detail.children[1].children[2].children[1].textContent, "9931112233 / Ext. 123");
    elements.directoryClear.events.click();
    await flush();
    assert.equal(elements.directoryRows.children.length, 2);
    assert.equal(new URL(requests.at(-1), "http://localhost").searchParams.has("estado"), false);
    assert.equal(new URL(requests.at(-1), "http://localhost").searchParams.has("telefonoExtension"), false);
    assert.equal(elements.idUnidadAdministrativa.children.length, 1);
    assert.equal(elements.idUnidadAdministrativa.disabled, true);
    assert.equal(elements.directoryUnitOpen.disabled, true);
    assert.equal(elements.directoryExportUnits.disabled, true);
    assert.equal(elements.directoryExportUsers.disabled, true);
    assert.equal(elements.directoryExportUnitUsers.disabled, true);
    assert.equal(new URL(requests.at(-1), "http://localhost").searchParams.has("idEntePublico"), false);
    assert.equal(new URL(requests.at(-1), "http://localhost").searchParams.has("idUnidadAdministrativa"), false);
    if (enhanced) {
        assert.equal(enteControl.selectedLabel, "Seleccione");
        assert.ok(enteControl.calls.includes("change.select2"));
        assert.ok(enteControl.calls.includes("close"));
    }

    const controls = () => elements.directoryPagination.children.map(item => item.children[0])
        .filter(control => control.events.click);
    const clickPage = label => controls().find(control => control.attributes["aria-label"] === label).events.click();
    const lastParams = () => new URL(requests.at(-1), "http://localhost").searchParams;
    paginationTotal = 205;
    chooseEnte("1");
    elements.nombre.value = " Ana ";
    elements.idUnidadAdministrativa.value = "11";
    elements.telefonoExtension.value = "993";
    elements.directoryFilters.events.submit({ preventDefault() {} });
    assert.equal(elements.directoryPageSize.disabled, true);
    assert.ok(controls().every(control => control.disabled));
    await flush();
    assert.equal(elements.directoryRows.children.length, 20);
    assert.equal(elements.directoryPage.textContent, "Página 1 de 11");
    clickPage("Página 3");
    await flush();
    assert.equal(lastParams().get("pagina"), "2");
    assert.equal(elements.directoryRows.children[0].children[0].textContent, "41");
    assert.equal(lastParams().get("nombre"), "Ana");
    assert.equal(lastParams().get("idEntePublico"), "1");
    assert.equal(lastParams().get("idUnidadAdministrativa"), "11");
    assert.equal(lastParams().get("telefonoExtension"), "993");
    clickPage("Última página");
    await flush();
    assert.equal(lastParams().get("pagina"), "10");
    assert.equal(elements.directorySummary.textContent, "Mostrando 201–205 de 205 resultados");
    assert.equal(elements.directoryRows.children.length, 5);
    assert.equal(controls().at(-1).disabled, true);
    clickPage("Página anterior");
    await flush();
    assert.equal(lastParams().get("pagina"), "9");
    clickPage("Primera página");
    await flush();
    assert.equal(lastParams().get("pagina"), "0");
    clickPage("Página siguiente");
    await flush();
    assert.equal(lastParams().get("pagina"), "1");
    elements.directoryPageSize.value = "50";
    elements.directoryPageSize.events.change();
    await flush();
    assert.equal(lastParams().get("pagina"), "0");
    assert.equal(lastParams().get("size"), "50");
    assert.equal(lastParams().get("idUnidadAdministrativa"), "11");
    assert.equal(elements.directoryRows.children.length, 50);
    assert.equal(elements.directoryPage.textContent, "Página 1 de 5");
    const count = requests.length;
    elements.directoryPageSize.value = "200";
    elements.directoryPageSize.events.change();
    assert.equal(requests.length, count);
    elements.directoryPageSize.value = "50";
    contactsFail = true;
    clickPage("Página siguiente");
    await flush();
    assert.equal(elements.directoryError.hidden, false);
    assert.equal(elements.directoryPageSize.disabled, false);
    assert.ok(controls().every(control => control.disabled));
    contactsFail = false;
    elements.directoryFilters.events.submit({ preventDefault() {} });
    await flush();
    assert.equal(elements.directoryError.hidden, true);
    clickPage("Última página");
    await flush();
    paginationTotal = 3;
    clickPage("Página anterior");
    await flush();
    assert.equal(lastParams().get("pagina"), "0");
    assert.equal(elements.directoryRows.children.length, 3);
    paginationTotal = 205;
    elements.directoryFilters.events.submit({ preventDefault() {} });
    await flush();
    clickPage("Última página");
    await flush();
    paginationTotal = 0;
    clickPage("Página anterior");
    await flush();
    assert.equal(elements.directorySummary.textContent, "No se encontraron contactos.");
    assert.equal(elements.directoryPage.textContent, "Página 1 de 1");
    assert.ok(controls().every(control => control.disabled));
    paginationTotal = 205;
    elements.directoryClear.events.click();
    await flush();
    assert.equal(lastParams().get("pagina"), "0");
    assert.equal(lastParams().get("size"), "50");
    assert.equal(lastParams().has("nombre"), false);
    assert.equal(lastParams().has("idEntePublico"), false);
    assert.equal(lastParams().has("idUnidadAdministrativa"), false);
    assert.equal(lastParams().has("telefonoExtension"), false);
});
}

test("plantilla elimina estado y columnas separadas y conserva teléfono/extensión", () => {
    const html = fs.readFileSync(path.join(__dirname, "../../main/resources/templates/directorio/directorio.html"), "utf8");
    assert.match(html, /<th scope="col">Teléfono\/Extensión<\/th>/);
    assert.match(html, /name="telefonoExtension"/);
    assert.match(html, /Buscar por nombre y apellidos/);
    assert.match(html, /placeholder="Nombre o apellidos…"/);
    assert.equal((html.match(/<th scope="col">/g) || []).length, 8);
    assert.doesNotMatch(html, /Vigente|Histórico|>Estado<|name="estado"|name="telefono"|name="extension"/);
    assert.match(html, /id="directoryEnteField"/);
    assert.equal((html.match(/id="directoryExportUnits"/g) || []).length, 1);
    assert.match(html, /id="directoryExportUnits"[^>]*disabled>/);
    assert.equal((html.match(/id="directoryExportUsers"/g) || []).length, 1);
    assert.match(html, /id="directoryExportUsers"[^>]*disabled>/);
    assert.equal((html.match(/id="directoryExportUnitUsers"/g) || []).length, 1);
    assert.match(html, /id="directoryExportUnitUsers"[^>]*disabled>/);
    const css = fs.readFileSync(path.join(__dirname, "../../main/resources/static/assets/css/directorio.css"), "utf8");
    assert.match(css, /\.directory-card \.table-responsive\s*\{\s*position:\s*relative/);
    assert.match(html, /value="">Seleccione<\/option>/);
    assert.ok(html.indexOf("jquery-3.7.0.min.js") < html.indexOf("select2.full.min.js"));
    assert.ok(html.indexOf("select2.full.min.js") < html.indexOf("directorio-publico.js"));
    assert.match(html, /id="directoryUnitField"/);
    assert.match(html, /<select id="idUnidadAdministrativa"[^>]*aria-hidden="true"[^>]* disabled>/);
    assert.match(html, /<button id="directoryUnitOpen"[^>]*type="button"[^>]* disabled>/);
    assert.match(html, /fa-solid fa-sitemap/);
    assert.match(html, /id="directoryUnitName"[^>]*readonly disabled/);
    assert.match(html, /id="directoryUnitModal"/);
    assert.match(html, /id="directoryUnitSearch"/);
    assert.match(html, /id="directoryUnitTree"/);
    assert.match(html, /modal-dialog-centered modal-dialog-scrollable/);
    assert.match(html, /class="modal-content directory-detail-modal"/);
    assert.match(html, /<div id="directoryDetailFields"><\/div>/);
    assert.match(html, /id="directoryPagination" class="pagination pagination-sm mb-0"/);
    assert.match(html, /id="directoryPageSize"/);
    assert.match(html, /<option value="20" selected>20<\/option>/);
    for (const size of [10, 25, 50, 100]) assert.match(html, new RegExp('<option value="' + size + '">' + size));
    assert.doesNotMatch(html, /id="directoryPrevious"|id="directoryNext"/);
    assert.match(html, /Unidades Administrativas/);
    assert.ok(html.indexOf("jstree.min.js") < html.indexOf("directorio-publico.js"));
});
