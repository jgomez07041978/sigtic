const test = require("node:test");
const assert = require("node:assert/strict");
const fs = require("node:fs");
const path = require("node:path");
const vm = require("node:vm");
const source = fs.readFileSync(path.join(__dirname, "../../main/resources/static/js/directorio/directorio-user-menu.js"), "utf8");

test("vista pública no inicializa el menú ni formularios privados", () => {
    let initialized = false;
    vm.runInNewContext(source, {
        document: { getElementById: () => null },
        llamarEye() { initialized = true; }
    });
    assert.equal(initialized, false);
});

test("menú de sesión inicializa ojos y limpia campos de contraseña al abrir y cerrar", () => {
    const elements = {};
    const events = {};
    let initialized = 0;
    let resets = 0;
    let validationResets = 0;
    const fields = ["contraseniaActual", "contraseniaNueva", "repetirContraseniaNueva"];
    ["directoryUserMenu", "idModalCambiarPassword", "idFormPassword", ...fields,
        "verPassword10", "verPassword11", "verPassword12", "id-fa-eye10", "id-fa-eye11", "id-fa-eye12"]
        .forEach(id => {
            const classes = new Set(["is-valid", "is-invalid", "fa-eye"]);
            elements[id] = { value: "temporal", type: "text", classes,
                classList: { remove: (...values) => values.forEach(value => classes.delete(value)), add: value => classes.add(value) } };
        });
    elements.idModalCambiarPassword.addEventListener = (event, callback) => { events[event] = callback; };
    elements.idFormPassword.reset = () => { resets++; };
    vm.runInNewContext(source, {
        document: { getElementById: id => elements[id] },
        jQuery: () => ({ data: () => ({ resetForm: () => { validationResets++; } }) }),
        llamarEye() { initialized++; }
    });
    assert.equal(initialized, 1);
    for (const event of ["show.bs.modal", "hidden.bs.modal"]) {
        fields.forEach(id => { elements[id].value = "temporal"; elements[id].type = "text"; });
        events[event]();
        fields.forEach(id => {
            assert.equal(elements[id].value, "");
            assert.equal(elements[id].type, "password");
            assert.equal(elements[id].classes.has("is-invalid"), false);
        });
        ["10", "11", "12"].forEach(number => {
            assert.equal(elements["verPassword" + number].value, "0");
            assert.equal(elements["id-fa-eye" + number].classes.has("fa-eye"), false);
            assert.equal(elements["id-fa-eye" + number].classes.has("fa-eye-slash"), true);
        });
    }
    assert.equal(resets, 2);
    assert.equal(validationResets, 2);
});

test("Directorio reutiliza menú y modales compartidos con dependencias solo de sesión", () => {
    const html = fs.readFileSync(path.join(__dirname, "../../main/resources/templates/directorio/directorio.html"), "utf8");
    const layout = fs.readFileSync(path.join(__dirname, "../../main/resources/templates/layout/mainLayout.html"), "utf8");
    for (const fragment of ["userMenu", "userLoadingModal", "userProfileModal", "userPasswordModal"]) {
        assert.ok(html.includes("layout/mainLayout :: " + fragment));
        assert.ok(layout.includes('th:fragment="' + fragment + '"'));
    }
    assert.match(html, /<nav th:if="\$\{directorioAuthenticated\}" id="directoryUserMenu"/);
    assert.match(html, /directorioAuthenticated and _csrf != null/);
    assert.ok(html.indexOf("sweetalert2.all.min.js") < html.indexOf("/js/utils.js"));
    assert.ok(html.indexOf("/js/utils.js") < html.indexOf("/js/directorio/directorio-user-menu.js"));
    assert.equal((html.match(/jquery-3\.7\.0\.min\.js/g) || []).length, 1);
});
