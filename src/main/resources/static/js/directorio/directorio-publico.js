(function () {
    "use strict";

    function query(filters, page, size) {
        var params = new URLSearchParams({ pagina: String(page), size: String(size || 20) });
        ["nombre", "idEntePublico", "idUnidadAdministrativa", "telefonoExtension"].forEach(function (key) {
            var value = String(filters[key] || "").trim();
            if (value) params.set(key, value);
        });
        return params.toString();
    }

    function telefonoExtension(contact) {
        var telefono = String(contact.telefono || "").trim();
        var extension = String(contact.extension || "").trim();
        if (telefono && extension) return telefono + " / Ext. " + extension;
        return telefono || (extension ? "Ext. " + extension : "—");
    }

    function summary(page) {
        if (!page.totalElements) return "No se encontraron contactos.";
        var first = page.number * page.size + 1;
        return "Mostrando " + first + "–" + (first + page.content.length - 1)
            + " de " + page.totalElements + " resultados";
    }

    function pageNumbers(number, totalPages) {
        var total = Math.max(1, totalPages);
        var current = Math.max(0, Math.min(number, total - 1));
        var start = total <= 7 ? 1 : current < 4 ? 2 : current >= total - 4 ? total - 4 : current;
        var end = total <= 7 ? total : current < 4 ? 5 : current >= total - 4 ? total - 1 : current + 2;
        var numbers = [];
        if (start > 1) numbers.push(1);
        if (start > 2) numbers.push(null);
        for (var page = start; page <= end; page++) numbers.push(page);
        if (end < total - 1) numbers.push(null);
        if (end < total) numbers.push(total);
        return numbers;
    }

    function renderPagination(doc, list, page, loading, navigate) {
        var number = page ? page.number : 0;
        var total = page ? Math.max(1, page.totalPages) : 1;
        var unavailable = loading || !page || !page.totalElements;
        list.replaceChildren();
        function button(text, label, target, disabled, active) {
            var item = doc.createElement("li");
            item.className = "page-item" + (active ? " active" : "") + (disabled ? " disabled" : "");
            var control = doc.createElement("button");
            control.type = "button";
            control.className = "page-link";
            control.textContent = text;
            control.disabled = disabled;
            control.setAttribute("aria-label", label);
            if (active) control.setAttribute("aria-current", "page");
            control.addEventListener("click", function () { if (!control.disabled && !active) navigate(target); });
            item.appendChild(control);
            list.appendChild(item);
        }
        button("«", "Primera página", 0, unavailable || number <= 0, false);
        button("‹", "Página anterior", number - 1, unavailable || number <= 0, false);
        pageNumbers(number, total).forEach(function (value) {
            if (value === null) {
                var item = doc.createElement("li");
                item.className = "page-item disabled";
                var gap = doc.createElement("span");
                gap.className = "page-link";
                gap.textContent = "…";
                gap.setAttribute("aria-hidden", "true");
                item.appendChild(gap);
                list.appendChild(item);
            } else button(String(value), "Página " + value, value - 1, unavailable, value - 1 === number);
        });
        button("›", "Página siguiente", number + 1, unavailable || number >= total - 1, false);
        button("»", "Última página", total - 1, unavailable || number >= total - 1, false);
    }

    function emailLink(value) {
        return /^[^@\s<>:]+@[^@\s<>:]+\.[^@\s<>:]+$/.test(value || "")
            ? "mailto:" + encodeURIComponent(value) : null;
    }

    function contactInitials(name) {
        var words = String(name || "").trim().split(/\s+/).filter(Boolean);
        if (!words.length) return "—";
        return (Array.from(words[0])[0] + (words.length > 1 ? Array.from(words[words.length - 1])[0] : ""))
            .toLocaleUpperCase("es-MX");
    }

    function contactDetail(doc, contact) {
        function text(value) { return String(value || "").trim(); }
        var container = doc.createElement("div");
        var profile = doc.createElement("div");
        profile.className = "directory-contact-profile";
        var avatar = doc.createElement("span");
        avatar.className = "directory-contact-avatar";
        avatar.textContent = contactInitials(contact.nombreCompleto);
        avatar.setAttribute("aria-hidden", "true");
        profile.appendChild(avatar);
        var identity = doc.createElement("div");
        identity.className = "directory-contact-identity";
        var caption = doc.createElement("p");
        caption.className = "directory-contact-caption";
        caption.textContent = "Contacto institucional";
        var name = doc.createElement("h3");
        name.className = "directory-contact-name";
        name.textContent = text(contact.nombreCompleto) || "Nombre no disponible";
        var position = doc.createElement("p");
        position.className = "directory-contact-position";
        position.textContent = text(contact.puesto) || "Puesto no registrado";
        identity.appendChild(caption);
        identity.appendChild(name);
        identity.appendChild(position);
        profile.appendChild(identity);
        container.appendChild(profile);

        var fields = doc.createElement("dl");
        fields.className = "directory-contact-grid";
        [
            { label: "Ente público", icon: "fa-building", value: text(contact.entePublico) },
            { label: "Unidad administrativa", icon: "fa-sitemap", value: text(contact.unidadAdministrativa) },
            { label: "Teléfono/Extensión", icon: "fa-phone", value: telefonoExtension(contact) },
            { label: "Correo electrónico", icon: "fa-envelope", value: text(contact.correo), email: true }
        ].forEach(function (field) {
            var item = doc.createElement("div");
            item.className = "directory-contact-field";
            var label = doc.createElement("dt");
            var icon = doc.createElement("i");
            icon.className = "fa-solid " + field.icon;
            icon.setAttribute("aria-hidden", "true");
            var title = doc.createElement("span");
            title.textContent = field.label;
            label.appendChild(icon);
            label.appendChild(title);
            var value = doc.createElement("dd");
            var missing = !field.value || field.value === "—";
            value.className = "directory-detail-value" + (missing ? " directory-contact-missing" : "");
            var href = field.email ? emailLink(field.value) : null;
            if (href) {
                var link = doc.createElement("a");
                link.href = href;
                link.textContent = field.value;
                value.appendChild(link);
            } else value.textContent = missing ? "Sin información registrada" : field.value;
            item.appendChild(label);
            item.appendChild(value);
            fields.appendChild(item);
        });
        container.appendChild(fields);
        return container;
    }

    function contactRow(doc, contact, number, detail) {
        var row = doc.createElement("tr");
        [number, contact.nombreCompleto, contact.puesto, contact.entePublico,
            contact.unidadAdministrativa, telefonoExtension(contact)].forEach(function (value) {
            var cell = doc.createElement("td");
            cell.textContent = value === null || value === undefined || value === "" ? "—" : String(value);
            row.appendChild(cell);
        });
        var email = doc.createElement("td");
        var link = emailLink(contact.correo);
        if (link) {
            var anchor = doc.createElement("a");
            anchor.textContent = contact.correo;
            anchor.href = link;
            email.appendChild(anchor);
        } else email.textContent = contact.correo || "—";
        row.appendChild(email);
        var action = doc.createElement("td");
        var button = doc.createElement("button");
        button.type = "button";
        button.className = "btn btn-primary btn-sm d-inline-flex align-items-center gap-1 text-nowrap";
        var icon = doc.createElement("i");
        icon.className = "fa-solid fa-eye";
        icon.setAttribute("aria-hidden", "true");
        button.appendChild(icon);
        var label = doc.createElement("span");
        label.textContent = "Ver";
        button.appendChild(label);
        button.setAttribute("aria-label", "Ver detalle de " + contact.nombreCompleto);
        button.addEventListener("click", function () { detail(contact); });
        action.appendChild(button);
        row.appendChild(action);
        return row;
    }

    function normalizeSearch(value) {
        return String(value || "").normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase();
    }

    function unitTree(items, ente, selected) {
        var scoped = items.filter(function (unit) { return ente && String(unit.idEntePublico) === String(ente); });
        var parents = new Map();
        var ids = new Set(scoped.map(function (unit) { return String(unit.id); }));
        scoped.forEach(function (unit) {
            var id = String(unit.id);
            var parent = String(unit.idPadre);
            parents.set(id, parent !== id && ids.has(parent) ? parent : "#");
        });
        return scoped.map(function (unit) {
            var id = String(unit.id);
            var parent = parents.get(id);
            var ancestor = parent;
            var visited = new Set([id]);
            while (ancestor !== "#") {
                if (visited.has(ancestor)) { parent = "#"; break; }
                visited.add(ancestor);
                ancestor = parents.get(ancestor) || "#";
            }
            var clave = String(unit.clave || "").trim();
            return { id: id, parent: parent, text: (clave ? "( " + clave + " ) " : "") + unit.nombre,
                state: { opened: true, selected: id === String(selected) } };
        });
    }

    if (typeof module !== "undefined" && module.exports) {
        module.exports = { query: query, telefonoExtension: telefonoExtension, summary: summary,
            pageNumbers: pageNumbers, renderPagination: renderPagination, emailLink: emailLink,
            contactRow: contactRow, contactInitials: contactInitials, contactDetail: contactDetail,
            unitTree: unitTree, normalizeSearch: normalizeSearch };
    }
    if (typeof document === "undefined") return;

    var form = document.getElementById("directoryFilters");
    var context = document.getElementById("contextSigtic").value;
    var rows = document.getElementById("directoryRows");
    var error = document.getElementById("directoryError");
    var pagination = document.getElementById("directoryPagination");
    var pageSizeSelect = document.getElementById("directoryPageSize");
    var pageSize = 20;
    var paginationFocused = false;
    var currentPage = null;
    var filters = {};
    var units = [];
    var requestId = 0;
    var modal = new bootstrap.Modal(document.getElementById("directoryDetail"));
    var unitModalElement = document.getElementById("directoryUnitModal");
    var unitModal = new bootstrap.Modal(unitModalElement);
    var unitSearch = document.getElementById("directoryUnitSearch");
    var unitEmpty = document.getElementById("directoryUnitEmpty");
    var treeControl = null;
    var exports = [
        { button: document.getElementById("directoryExportUnits"), endpoint: "unidades",
            filename: "unidades_administrativas", requiresUnits: true, downloading: false,
            label: "el catálogo de unidades", title: "Descargar las unidades vigentes del ente seleccionado." },
        { button: document.getElementById("directoryExportUsers"), endpoint: "usuarios",
            filename: "usuarios", requiresUnits: false, downloading: false,
            label: "los usuarios", title: "Descargar todos los usuarios del ente seleccionado, sin aplicar los demás filtros." },
        { button: document.getElementById("directoryExportUnitUsers"), endpoint: "usuarios-unidad",
            filename: "usuarios_unidad", requiresUnits: true, requiresSelectedUnit: true, downloading: false,
            label: "los usuarios de la unidad", title: "Descargar todos los usuarios de la unidad seleccionada, sin incluir subunidades ni aplicar los demás filtros." }
    ];

    function updateExport() {
        var ente = document.getElementById("idEntePublico").value;
        var unitSelect = document.getElementById("idUnidadAdministrativa");
        exports.forEach(function (config) {
            config.button.disabled = config.downloading || !ente
                || (config.requiresUnits && unitSelect.disabled)
                || (config.requiresSelectedUnit === true && !unitSelect.value);
            config.button.setAttribute("aria-busy", String(config.downloading));
            config.button.title = config.downloading ? "Descargando…"
                : config.button.disabled && config.requiresSelectedUnit
                    ? "Selecciona un ente público y una unidad administrativa para descargar sus usuarios."
                : config.button.disabled ? "Selecciona un ente público"
                    + (config.requiresUnits ? " con unidades" : "") + " para descargar."
                : config.title;
        });
    }

    function exportFile(config) {
        if (config.button.disabled) return;
        var ente = document.getElementById("idEntePublico").value;
        var unit = config.requiresSelectedUnit ? document.getElementById("idUnidadAdministrativa").value : "";
        var params = new URLSearchParams();
        params.set("idEntePublico", ente);
        if (unit) params.set("idUnidadAdministrativa", unit);
        config.downloading = true;
        updateExport();
        error.hidden = true;
        return fetch(context + "/api/directorio/" + config.endpoint + ".xlsx?" + params.toString(),
            { credentials: "same-origin" }).then(function (response) {
            if (!response.ok) throw new Error("No se pudo descargar " + config.label + ". Intenta de nuevo.");
            return response.blob();
        }).then(function (blob) {
            var url = URL.createObjectURL(blob);
            var link = document.createElement("a");
            link.href = url;
            link.download = config.filename + "_" + ente + (unit ? "_" + unit : "") + ".xlsx";
            document.body.appendChild(link);
            link.click();
            link.remove();
            setTimeout(function () { URL.revokeObjectURL(url); }, 1000);
        }).catch(function (failure) {
            error.textContent = failure.message;
            error.hidden = false;
        }).then(function () {
            config.downloading = false;
            updateExport();
        });
    }
    exports.forEach(function (config) {
        config.button.addEventListener("click", function () { return exportFile(config); });
    });

    function json(url) {
        return fetch(url, { headers: { Accept: "application/json" }, credentials: "same-origin" }).then(function (response) {
            if (!response.ok) throw new Error("No se pudo cargar el directorio. Intenta de nuevo.");
            return response.json();
        });
    }

    function busy(value) {
        if (value && document.activeElement && pagination.contains(document.activeElement)) paginationFocused = true;
        document.getElementById("directoryLoading").hidden = !value;
        document.getElementById("directoryResults").setAttribute("aria-busy", String(value));
        pageSizeSelect.disabled = value;
        renderPagination(document, pagination, currentPage, value, load);
        if (!value && paginationFocused) {
            var active = pagination.querySelector('[aria-current="page"]');
            if (active && !active.disabled && (document.activeElement === document.body
                || pagination.contains(document.activeElement))) active.focus();
            paginationFocused = false;
        }
    }

    function showDetail(contact) {
        var fields = document.getElementById("directoryDetailFields");
        fields.replaceChildren();
        fields.appendChild(contactDetail(document, contact));
        modal.show();
    }

    function load(page) {
        var id = ++requestId;
        busy(true);
        error.hidden = true;
        rows.replaceChildren();
        document.getElementById("directorySummary").textContent = "Cargando contactos…";
        return json(context + "/api/directorio/contactos?" + query(filters, page, pageSize)).then(function (data) {
            if (id !== requestId) return;
            if (data.number > 0 && data.number >= data.totalPages) return load(Math.max(0, data.totalPages - 1));
            currentPage = data;
            data.content.forEach(function (contact, index) {
                rows.appendChild(contactRow(document, contact, data.number * data.size + index + 1, showDetail));
            });
            document.getElementById("directorySummary").textContent = summary(data);
            document.getElementById("directoryPage").textContent = "Página " + (data.number + 1) + " de " + Math.max(1, data.totalPages);
        }).catch(function (failure) {
            if (id !== requestId) return;
            currentPage = null;
            error.textContent = failure.message;
            error.hidden = false;
            document.getElementById("directorySummary").textContent = "No fue posible cargar los contactos.";
            document.getElementById("directoryPage").textContent = "—";
        }).then(function () { if (id === requestId) busy(false); });
    }

    function option(select, id, name) {
        var item = document.createElement("option");
        item.value = String(id);
        item.textContent = name;
        select.appendChild(item);
    }

    function updateUnits() {
        var select = document.getElementById("idUnidadAdministrativa");
        var ente = document.getElementById("idEntePublico").value;
        var available = units.filter(function (unit) { return ente && String(unit.idEntePublico) === ente; });
        unitModal.hide();
        resetUnitTree();
        select.replaceChildren();
        option(select, "", ente && !available.length ? "Sin unidades disponibles" : "Unidad administrativa");
        available.forEach(function (unit) { option(select, unit.id, unit.nombre); });
        select.value = "";
        select.disabled = !ente || !available.length;
        var message = !ente ? "Selecciona primero un ente público."
            : !available.length ? "Este ente público no tiene unidades administrativas."
            : "Selecciona una unidad administrativa de este ente público.";
        select.title = message;
        document.getElementById("directoryUnitHint").textContent = message;
        var button = document.getElementById("directoryUnitOpen");
        button.disabled = select.disabled;
        button.title = message;
        var display = document.getElementById("directoryUnitName");
        display.value = "";
        display.placeholder = ente && !available.length ? "Sin unidades disponibles" : "Unidad administrativa";
        display.disabled = select.disabled;
        display.title = message;
        updateExport();
    }

    function resetUnitTree() {
        if (treeControl) {
            treeControl.off(".directoryUnits").jstree("destroy");
            treeControl = null;
        }
        unitSearch.value = "";
        unitEmpty.hidden = true;
    }

    function chooseUnit(node) {
        var select = document.getElementById("idUnidadAdministrativa");
        var ente = document.getElementById("idEntePublico").value;
        var unit = node && units.find(function (item) {
            return String(item.id) === node.id && String(item.idEntePublico) === ente;
        });
        if (select.disabled || !unit) return;
        select.value = String(unit.id);
        document.getElementById("directoryUnitName").value = unit.nombre;
        updateExport();
        unitModal.hide();
    }

    function openUnitPicker() {
        var select = document.getElementById("idUnidadAdministrativa");
        if (select.disabled) return;
        resetUnitTree();
        unitModal.show();
        if (typeof window === "undefined" || !window.jQuery || !window.jQuery.fn.jstree) {
            unitEmpty.textContent = "No se pudo cargar el árbol de unidades. Recarga la página.";
            unitEmpty.hidden = false;
            unitSearch.disabled = true;
            return;
        }
        unitSearch.disabled = false;
        unitEmpty.textContent = "No se encontraron unidades administrativas.";
        treeControl = window.jQuery(document.getElementById("directoryUnitTree"));
        treeControl.jstree({
            core: {
                data: unitTree(units, document.getElementById("idEntePublico").value, select.value),
                force_text: true,
                worker: false,
                dblclick_toggle: false
            },
            types: { "default": { icon: "fa-solid fa-house" } },
            plugins: ["types", "search"],
            search: {
                show_only_matches: true,
                search_callback: function (term, node) {
                    return normalizeSearch(node.text).includes(normalizeSearch(term));
                }
            }
        }).on("ready.jstree.directoryUnits", function () {
            if (unitSearch.value) treeControl.jstree(true).search(unitSearch.value);
        }).on("dblclick.directoryUnits", ".jstree-anchor", function (event) {
            chooseUnit(treeControl.jstree(true).get_node(event.currentTarget));
        }).on("keydown.directoryUnits", ".jstree-anchor", function (event) {
            if (event.key === "Enter") {
                event.preventDefault();
                chooseUnit(treeControl.jstree(true).get_node(event.currentTarget));
            }
        }).on("search.jstree.directoryUnits", function (event, data) {
            unitEmpty.hidden = !unitSearch.value.trim() || data.nodes.length > 0;
        });
    }

    function setupSelector(id, fieldId, labelId, searchLabel, noResults) {
        if (typeof window === "undefined" || !window.jQuery || !window.jQuery.fn.select2) return null;
        var selector = window.jQuery(document.getElementById(id)).select2({
            theme: "bootstrap-5",
            width: "100%",
            minimumResultsForSearch: 0,
            dropdownParent: window.jQuery("#" + fieldId),
            language: {
                noResults: function () { return noResults; },
                searching: function () { return "Buscando…"; }
            }
        });
        var selection = document.querySelector("#" + fieldId + " .select2-selection");
        if (selection) {
            var label = selection.getAttribute("aria-labelledby") || "";
            selection.setAttribute("aria-labelledby", (labelId + " " + label).trim());
            var hint = document.getElementById(id).getAttribute("aria-describedby");
            if (hint) selection.setAttribute("aria-describedby", hint);
        }
        selector.on("select2:open", function () {
            var search = document.querySelector("#" + fieldId + " .select2-search__field");
            if (search) {
                search.setAttribute("aria-label", searchLabel);
                search.setAttribute("placeholder", searchLabel + "…");
                search.focus();
            }
        });
        return selector;
    }

    var enteSelector = setupSelector("idEntePublico", "directoryEnteField", "directoryEnteLabel",
        "Buscar ente público", "No se encontraron entes públicos.");
    updateUnits();
    form.addEventListener("submit", function (event) {
        event.preventDefault();
        filters = {};
        new FormData(form).forEach(function (value, key) { filters[key] = value; });
        load(0);
    });
    if (enteSelector) enteSelector.on("change", updateUnits);
    else document.getElementById("idEntePublico").addEventListener("change", updateUnits);
    document.getElementById("idUnidadAdministrativa").addEventListener("change", updateExport);
    document.getElementById("directoryUnitOpen").addEventListener("click", openUnitPicker);
    document.getElementById("directoryUnitName").addEventListener("click", openUnitPicker);
    document.getElementById("directoryUnitName").addEventListener("keydown", function (event) {
        if (event.key === "Enter" || event.key === " ") {
            event.preventDefault();
            openUnitPicker();
        }
    });
    unitSearch.addEventListener("input", function () {
        if (treeControl) treeControl.jstree(true).search(unitSearch.value.trim());
    });
    unitModalElement.addEventListener("shown.bs.modal", function () { unitSearch.focus(); });
    unitModalElement.addEventListener("hidden.bs.modal", function () {
        unitSearch.value = "";
        unitEmpty.hidden = true;
        if (treeControl) treeControl.jstree(true).clear_search();
    });
    document.getElementById("directoryClear").addEventListener("click", function () {
        form.reset();
        if (enteSelector) {
            enteSelector.val("").trigger("change.select2");
            enteSelector.select2("close");
        }
        updateUnits();
        filters = {};
        load(0);
    });
    pageSizeSelect.addEventListener("change", function () {
        var size = Number(pageSizeSelect.value);
        if ([10, 20, 25, 50, 100].indexOf(size) === -1) return;
        pageSize = size;
        load(0);
    });

    load(0);
    json(context + "/api/directorio/opciones").then(function (data) {
        data.entes.forEach(function (ente) { option(document.getElementById("idEntePublico"), ente.id, ente.nombre); });
        units = data.unidades;
        updateUnits();
    }).catch(function (failure) {
        error.textContent = "No se pudieron cargar los filtros del directorio. Recarga la página para intentar de nuevo.";
        error.hidden = false;
    });
}());
