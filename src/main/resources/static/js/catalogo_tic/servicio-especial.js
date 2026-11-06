// Nombres con sufijo ServEsp para no chocar con las const/ids de cuenta-maestra.js
const headersServEsp = {
    "Accept": "application/json",
    "X-CSRF-Token": $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

const baseServEsp = '/api/catalogo/tic/servicioEspecial';

$(document).ready(function () {

    jQuery.validator.addMethod("altibajo", function (value) {
        const v = (value || '').trim();
        if (!/[A-Za-zÁÉÍÓÚÑáéíóúñ]/.test(v)) return true;
        return v !== v.toUpperCase();
    }, "Escribe en altibajo (no todo en mayúsculas)");

    $('#idNuevoServicioEspecial').click(function (e) {
        e.preventDefault();
        abrirNuevoServicioEspecial();
    });

    // arbol dependencia-iunidad administrativa ---
    $('#idEntePublicoServEsp').on('change', function () {
        const idEnte = $(this).val();

        $('#idUnidadAdministrativaServEsp').val('');
        $('#descUnidadAdministrativaServEsp').val('').removeClass('is-valid is-invalid');

        // En edición la Cuenta Maestra es parte de la llave y no se recarga
        if ($('#opcionServEsp').val() === 'Nuevo') {
            cargarCuentasMaestrasServEsp(idEnte);
        }

        if (idEnte !== '' && idEnte !== null) {
            obtienearbolPorInstitucion(function (data) {
                $("#idUnidadAdministrativaServEsp").val(data.id);
                $("#descUnidadAdministrativaServEsp").val(data.text);
                limpiarBuscarArbol();
                $('#modal_arbol_unidad').modal('hide');
                $("#descUnidadAdministrativaServEsp").removeClass("is-valid is-invalid");
            }, idEnte);
        }
    });

    $('#idBtnUnidadAdminServEsp').click(function () {
        if (!$('#idEntePublicoServEsp').val()) {
            Toast.fire({icon: "warning", title: "Seleccione una Dependencia primero"});
            return;
        }
        $('#modal_arbol_unidad').modal('show');
    });

    $('#cvePartidaServEsp').on('change', function () {
        const cvePartida = $(this).val();
        $('#codigoEbsServEsp').empty().append($('<option></option>').attr('value', '').text('Seleccione'));
        $('#nombreArticuloServEsp').val('');
        if (cvePartida !== '' && cvePartida !== null) {
            llenarSelectServEsp(baseServEsp + '/getArticulosPorPartida/' + encodeURIComponent(cvePartida), '#codigoEbsServEsp');
        }
    });

    $('#codigoEbsServEsp').on('change', function () {
        const texto = $('#codigoEbsServEsp option:selected').text();
        const idx = texto.indexOf(' - ');
        $('#nombreArticuloServEsp').val($(this).val() && idx !== -1 ? texto.substring(idx + 3) : '');
    });

    $('#cantidadServEsp, #precioConIvaServEsp').on('input', recalcularTotalServEsp);

    $('#descripcionAbreviadaServEsp').on('input', function () {
        $('#contDescAbrevServEsp').text($(this).val().length);
    });

    // --- Validación ---
    $("#idFormServEsp").validate({
        ignore: "",
        rules: {
            idEntePublicoServEsp: "required",
            descUnidadAdministrativaServEsp: "required",
            idCuentaMaestraServEsp: "required",
            periodoServEsp: "required",
            cveTipoConexionServEsp: "required",
            cvePartidaServEsp: "required",
            codigoEbsServEsp: "required",
            cantidadServEsp: {required: true, min: 1},
            cveUnidadMedidaServEsp: "required",
            precioConIvaServEsp: {required: true, min: 0.01},
            descripcionAbreviadaServEsp: {required: true, minlength: 100, maxlength: 240, altibajo: true},
            descripcionServEsp: {altibajo: true}
        },
        messages: {
            idEntePublicoServEsp: "Campo requerido",
            descUnidadAdministrativaServEsp: "Campo requerido",
            idCuentaMaestraServEsp: "Campo requerido",
            periodoServEsp: "Campo requerido",
            cveTipoConexionServEsp: "Campo requerido",
            cvePartidaServEsp: "Campo requerido",
            codigoEbsServEsp: "Campo requerido",
            cantidadServEsp: {required: "Campo requerido", min: "Mínimo 1"},
            cveUnidadMedidaServEsp: "Campo requerido",
            precioConIvaServEsp: {required: "Campo requerido", min: "Debe ser mayor a 0"},
            descripcionAbreviadaServEsp: {
                required: "Campo requerido",
                minlength: "Mínimo 100 caracteres",
                maxlength: "Máximo 240 caracteres"
            }
        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            error.addClass("invalid-feedback");
            element.closest('.form-floating, .input-group').append(error);
        },
        highlight: function (element) {
            $(element).addClass("is-invalid").removeClass("is-valid");
        },
        unhighlight: function (element) {
            $(element).addClass("is-valid").removeClass("is-invalid");
        },
        submitHandler: function (form, e) {
            e.preventDefault();
        }
    });

    $("#idFormServEsp").submit(function (e) {
        e.preventDefault();
        if ($('#idFormServEsp').valid()) {
            guardarServicioEspecial();
        }
    });
});

function recalcularTotalServEsp() {
    const cant = parseFloat($('#cantidadServEsp').val());
    const precio = parseFloat($('#precioConIvaServEsp').val());
    if (isNaN(cant) || isNaN(precio)) {
        $('#totalServEsp').val('');
        return;
    }
    $('#totalServEsp').val((cant * precio).toLocaleString('es-MX', {minimumFractionDigits: 2, maximumFractionDigits: 2}));
}

function limpiarFormularioServEsp() {
    const validator = $("#idFormServEsp").data("validator");
    if (validator) {
        validator.resetForm();
        validator.hideErrors();
    }
    $("#idFormServEsp").find(".is-invalid, .is-valid").removeClass("is-invalid is-valid");

    $("#opcionServEsp").val("Nuevo");
    $("#noLoteOriginalServEsp").val("");
    $("#numCuentaMaestraOriginalServEsp").val("");
    $("#idEntePublicoServEsp").val("");
    $("#idUnidadAdministrativaServEsp").val("");
    $("#descUnidadAdministrativaServEsp").val("");
    $("#idCuentaMaestraServEsp").prop("disabled", false).empty()
        .append($('<option></option>').attr('value', '').text('Seleccione una Dependencia primero'));
    $("#noLoteServEsp").val("");
    $("#periodoServEsp").val("");
    $("#cveTipoConexionServEsp").val("");
    $("#cvePartidaServEsp").val("");
    $("#codigoEbsServEsp").empty().append($('<option></option>').attr('value', '').text('Seleccione una Partida primero'));
    $("#nombreArticuloServEsp").val("");
    $("#cantidadServEsp").val("");
    $("#cveUnidadMedidaServEsp").val("");
    $("#precioConIvaServEsp").val("");
    $("#totalServEsp").val("");
    $("#numOficioServEsp").val("");
    $("#descripcionAbreviadaServEsp").val("");
    $("#descripcionServEsp").val("");
    $("#contDescAbrevServEsp").text("0");
}

function llenarSelectServEsp(url, selector, textoInicial) {
    const $sel = $(selector);
    $sel.empty().append($('<option></option>').attr('value', '').text(textoInicial || 'Seleccione'));
    return fetch(contexto + url, {method: "GET", headers: headersServEsp})
        .then(res => {
            if (!res.ok) throw new Error('HTTP ' + res.status);
            return res.json();
        })
        .then(lista => {
            lista.forEach(item => {
                $sel.append($('<option></option>').attr('value', item.clave).text(item.descripcion));
            });
        })
        .catch(() => {
            Toast.fire({icon: "error", title: "No se pudo cargar el catálogo"});
        });
}

function cargarCuentasMaestrasServEsp(idEnte, valorSeleccionado) {
    const $sel = $('#idCuentaMaestraServEsp');
    $sel.empty().append($('<option></option>').attr('value', '').text('Seleccione'));
    if (!idEnte) {
        return Promise.resolve();
    }
    return fetch(contexto + '/api/catalogo/tic/cuentaMaestra/getCuentasMaestras/' + idEnte,
        {method: "GET", headers: headersServEsp})
        .then(res => res.json())
        .then(lista => {
            lista.forEach(c => {
                const texto = c.numCuentaMaestra +
                    (c.nombreClienteCuentaMaestra ? (' - ' + c.nombreClienteCuentaMaestra) : '');
                $sel.append($('<option></option>').attr('value', c.numCuentaMaestra).text(texto));
            });
            if (valorSeleccionado) {
                $sel.val(valorSeleccionado);
            }
        });
}

// Carga los catálogos
function cargarCatalogosServEsp() {
    return Promise.all([
        fillSelectGet('/api/entePublico/usuario', '#idEntePublicoServEsp', 'idEntePublico', 'nombreEntePublico', null, null),
        fillSelectGet('/api/catalogo/tic/cuentaMaestra/getTiposConexion', '#cveTipoConexionServEsp', 'cveTipoConexionLineaTelef', 'nombreTipoConexionLineaTelef', null, null),
        llenarSelectServEsp(baseServEsp + '/getPartidas', '#cvePartidaServEsp'),
        llenarSelectServEsp(baseServEsp + '/getUnidadesMedida', '#cveUnidadMedidaServEsp')
    ]);
}

// Se llama desde la opción "Nuevo servicio especial" del botón desplegable
function abrirNuevoServicioEspecial() {
    limpiarFormularioServEsp();
    $('#titleFormServEsp').text("Nuevo Servicio Especial");
    $("#noLoteServEsp").val("Se asigna al guardar");
    cargarCatalogosServEsp();
    $('#dlgModalFormServEsp').modal('show');
}

function guardarServicioEspecial() {
    let titleSave = '¿Guardar Servicio Especial?';
    let msgSave = '¿Estás seguro que deseas guardar los datos del servicio especial?';
    if ($('#opcionServEsp').val() === "Editar") {
        titleSave = '¿Actualizar Servicio Especial?';
        msgSave = '¿Estás seguro que deseas actualizar los datos del servicio especial?';
    }

    Swal.fire({
        title: titleSave,
        text: msgSave,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: 'No'
    }).then(function (result) {
        if (result.isConfirmed) {
            guardarServicioEspecialAsync();
        }
    });
}

const guardarServicioEspecialAsync = async () => {

    const body = {
        opcion: $("#opcionServEsp").val(),
        noLoteOriginal: $("#noLoteOriginalServEsp").val() || null,
        numCuentaMaestraOriginal: $("#numCuentaMaestraOriginalServEsp").val() || null,
        servicioEspecial: {
            entePublico: {idEntePublico: $("#idEntePublicoServEsp").val()},
            unidadAdministrativa: {idUnidadAdministrativa: $("#idUnidadAdministrativaServEsp").val()},
            catCuentaMaestra: {numCuentaMaestra: $("#idCuentaMaestraServEsp").val()},
            catTipoConexionLineaTelef: {cveTipoConexionLineaTelef: $("#cveTipoConexionServEsp").val()},
            periodo: $("#periodoServEsp").val() + "-01",
            cvePartida: $("#cvePartidaServEsp").val(),
            codigoEbs: $("#codigoEbsServEsp").val(),
            cantidad: parseInt($("#cantidadServEsp").val(), 10),
            cveUnidadMedida: $("#cveUnidadMedidaServEsp").val(),
            precioConIva: parseFloat($("#precioConIvaServEsp").val()),
            numOficio: $("#numOficioServEsp").val() || null,
            descripcionAbreviada: $("#descripcionAbreviadaServEsp").val().trim(),
            descripcion: $("#descripcionServEsp").val().trim() || null
        }
    };

    const options = {method: "POST", body: JSON.stringify(body), headers: headersServEsp};

    try {
        onLoading("dlgModalFormServEsp");
        const res = await fetch(contexto + baseServEsp + '/guardar', options);
        const respJSON = await res.json();

        if (res.ok) {
            Toast.fire({
                icon: "success",
                title: $("#opcionServEsp").val() === "Nuevo"
                    ? "El servicio especial se ha guardado"
                    : "El servicio especial se ha actualizado"
            });
            // Hook: define esta función en el JS donde pintes la tabla de servicios
            if (typeof refrescarServiciosEspeciales === 'function') {
                refrescarServiciosEspeciales();
            }
            offLoading("dlgModalFormServEsp");
            $('#dlgModalFormServEsp').modal('hide');
        } else {
            Toast.fire({icon: "error", title: respJSON.message});
            offLoading("dlgModalFormServEsp");
        }
    } catch (e) {
        offLoading("dlgModalFormServEsp");
        Toast.fire({icon: "error", title: "Error al guardar"});
    }
};

function editarServicioEspecial(numCuentaMaestra, noLote) {
    limpiarFormularioServEsp();
    $("#opcionServEsp").val("Editar");
    $("#noLoteOriginalServEsp").val(noLote);
    $("#numCuentaMaestraOriginalServEsp").val(numCuentaMaestra);
    $('#titleFormServEsp').text("Editar Servicio Especial");

    onLoading(null);

    fetch(contexto + baseServEsp + '/get/' + encodeURIComponent(numCuentaMaestra) + '/' + encodeURIComponent(noLote),
        {method: "GET", headers: headersServEsp})
        .then(res => res.json())
        .then(async reg => {

            await cargarCatalogosServEsp();

            $("#noLoteServEsp").val(reg.noLote);
            if (reg.catCuentaMaestra && reg.entePublico) {
                await cargarCuentasMaestrasServEsp(reg.entePublico.idEntePublico, reg.catCuentaMaestra.numCuentaMaestra);
                $("#idCuentaMaestraServEsp").prop("disabled", true);
            }

            $("#periodoServEsp").val(reg.periodo ? reg.periodo.substring(0, 7) : "");
            $("#cveTipoConexionServEsp").val(reg.catTipoConexionLineaTelef ? reg.catTipoConexionLineaTelef.cveTipoConexionLineaTelef : "");
            $("#cantidadServEsp").val(reg.cantidad);
            $("#cveUnidadMedidaServEsp").val(reg.cveUnidadMedida);
            $("#precioConIvaServEsp").val(reg.precioConIva);
            recalcularTotalServEsp();
            $("#numOficioServEsp").val(reg.numOficio || "");
            $("#descripcionAbreviadaServEsp").val(reg.descripcionAbreviada || "");
            $("#contDescAbrevServEsp").text((reg.descripcionAbreviada || "").length);
            $("#descripcionServEsp").val(reg.descripcion || "");

            // Partida - Artículo
            $("#cvePartidaServEsp").val(reg.cvePartida);
            await llenarSelectServEsp(baseServEsp + '/getArticulosPorPartida/' + encodeURIComponent(reg.cvePartida), '#codigoEbsServEsp');
            $("#codigoEbsServEsp").val(reg.codigoEbs).trigger('change');

            // Dependencia y Unidad (mismo que cuenta-maestra.js)
            if (reg.unidadAdministrativa) {
                $("#idUnidadAdministrativaServEsp").val(reg.unidadAdministrativa.idUnidadAdministrativa);
                $("#descUnidadAdministrativaServEsp").val(reg.unidadAdministrativa.nombreUnidadAdministrativa);
            }
            if (reg.entePublico) {
                $("#idEntePublicoServEsp").val(reg.entePublico.idEntePublico);
                obtienearbolPorInstitucion(function (data) {
                    $("#idUnidadAdministrativaServEsp").val(data.id);
                    $("#descUnidadAdministrativaServEsp").val(data.text);
                    limpiarBuscarArbol();
                    $('#modal_arbol_unidad').modal('hide');
                    $("#descUnidadAdministrativaServEsp").removeClass("is-valid is-invalid");
                }, reg.entePublico.idEntePublico);
            }

            offLoading(null);
            $('#dlgModalFormServEsp').modal('show');
        })
        .catch(() => {
            offLoading(null);
            Toast.fire({icon: "error", title: "Error al cargar el servicio especial"});
        });
}