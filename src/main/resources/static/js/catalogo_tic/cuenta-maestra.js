const headersCtaMaestra = {
    "X-CSRF-Token": $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

const baseCtaMaestra = '/api/catalogo/tic/cuentaMaestra';

let tablaCtaMaestra = null;
let tableUsuarioContacto = null;

$(document).ready(function () {

    $('[data-bs-toggle="tooltip"]').tooltip();

    jQuery.event.special.mousewheel = {
        setup: function (_, ns, handle) {
            this.addEventListener("mousewheel", handle, {passive: true});
        }
    };

    fillSelectGet(baseCtaMaestra + '/getTiposConexion', '#cveTipoConexion', 'cveTipoConexionLineaTelef', 'nombreTipoConexionLineaTelef', null, null);

    (async () => {
        await fillSelectGet('/api/entePublico/usuario', '#idEntePublico', 'idEntePublico', 'nombreEntePublico', null, null);

        const idEnteAsignado = $('#idEntePublicoAsignado').val();
        if (idEnteAsignado) {
            $('#idEntePublico').val(idEnteAsignado);
            $('#idEntePublico').trigger('change');
        } else {
            obtieneCuentaMaestra();
        }
    })();

    $('#idEntePublico').on('change', function () {
        const idEnte = $(this).val();

        $('#idUnidadAdministrativa').empty().append($('<option></option>').attr('value', '').text('Todas'));

        if (idEnte !== '' && idEnte !== null) {
            fillSelectGet(baseCtaMaestra + '/getUnidadAdministrativaPorEnte', '#idUnidadAdministrativa', 'idUnidadAdministrativa', 'nombreUnidadAdministrativa', idEnte, obtieneCuentaMaestra);
        } else {
            obtieneCuentaMaestra();
        }
    });

    $('#idUnidadAdministrativa, #cveTipoConexion, #idVigente, #idHistorico').on('change', function () {
        obtieneCuentaMaestra();
    });

    $('#idNuevoTelefono').click(function () {
        limpiarFormularioCuentaMaestra();
        $('#titleFormCuentaMaestra').text("Nueva Línea Telefónica");
        $('#contTipoConexionForm').show();
        fillSelectGet('/api/entePublico/usuario', '#idEntePublicoForm', 'idEntePublico', 'nombreEntePublico', null, null);
        fillSelectGet(baseCtaMaestra + '/getTiposConexion', '#cveTipoConexionForm', 'cveTipoConexionLineaTelef', 'nombreTipoConexionLineaTelef', null, null);
        $('#dlgModalFormCuentaMaestra').modal('show');
    });

    $('#idBtnUnidadAdminCtaMaestra').click(function () {
        $('#modal_arbol_unidad').modal('show');
    });

    $("#txtBuscar").keyup(function () {
        busqueda($(this).val());
    });

    $('#idEntePublicoForm').on('change', function () {
        const idEnte = $(this).val();

        $('#idUnidadAdministrativaForm').val('');
        $('#descUnidadAdministrativaForm').val('').removeClass('is-valid is-invalid');
        $('#idDomicilioEnteForm').empty().append($('<option></option>').attr('value', '').text('Seleccione'));
        $('#idUbicacionForm').empty().append($('<option></option>').attr('value', '').text('Seleccione un Domicilio primero'));
        $('#curpUsuarioContactoForm').val('');
        $('#descUsuarioContactoForm').val('').removeClass('is-valid is-invalid');

        if (idEnte !== '' && idEnte !== null) {
            obtienearbolPorInstitucion(function (data) {
                $("#idUnidadAdministrativaForm").val(data.id);
                $("#descUnidadAdministrativaForm").val(data.text);
                limpiarBuscarArbol();
                $('#modal_arbol_unidad').modal('hide');
                $("#descUnidadAdministrativaForm").removeClass("is-valid is-invalid");
            }, idEnte);

            fillSelectGet('/api/catalogo/domicilioentePublico/all', '#idDomicilioEnteForm', 'idDomicilio', 'nombreDomicilio', idEnte, null);
        }
    });

    $('#idDomicilioEnteForm').on('change', function () {
        const idDomicilio = $(this).val();
        $('#idUbicacionForm').empty().append($('<option></option>').attr('value', '').text('Seleccione'));
        if (idDomicilio !== '' && idDomicilio !== null) {
            fillSelectGet('/api/catalogo/ubicacionUnidadAministrativa/all', '#idUbicacionForm', 'idUbicacionAdaministrativa', 'nombreUbicacion', idDomicilio, null);
        }
    });

    $('#idBtnBuscarUsuarioContacto').click(function () {
        $('#dlgModalFormCuentaMaestra').modal('hide');
        $('#dlgModalBuscarUsuarioContacto').modal('show');
    });

    $('#idBtnCloseSearchContacto').click(function () {
        $('#dlgModalBuscarUsuarioContacto').modal('hide');
        $('#dlgModalFormCuentaMaestra').modal('show');
    });

    $('#idBtnLimpiarSearchContacto').click(function () {
        limpiarBusquedaUsuarioContacto();
    });

    $('#dlgModalBuscarUsuarioContacto').on('hidden.bs.modal', function () {
        limpiarBusquedaUsuarioContacto();
    });

    $('#formBusquedaUsuarioContacto').submit(function (e) {
        e.preventDefault();
        obtieneUsuariosContactoSearch();
    });

    $("#idFormCuentaMaestra").validate({
        ignore: "",
        rules: {
            idEntePublicoForm: "required",
            descUnidadAdministrativaForm: "required",
            descNumTelefono: "required",
            cveTipoConexionForm: "required",
            descCuentaMaestraForm: "required",
            idDomicilioEnteForm: "required",
            idUbicacionForm: "required",
            descUsuarioContactoForm: "required"
        },
        messages: {
            idEntePublicoForm: "Campo requerido",
            descUnidadAdministrativaForm: "Campo requerido",
            descNumTelefono: "Campo requerido",
            cveTipoConexionForm: "Campo requerido",
            descCuentaMaestraForm: "Campo requerido",
            idDomicilioEnteForm: "Campo requerido",
            idUbicacionForm: "Campo requerido",
            descUsuarioContactoForm: "Campo requerido"
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

    $("#idFormCuentaMaestra").submit(function (e) {
        e.preventDefault();
        if ($('#idFormCuentaMaestra').valid()) {
            guardarCuentaMaestra();
        }
    });

    $('#btnExportarFiltro').click(function () {
        exportarFiltroCuentaMaestra();
    });
});

function limpiarFormularioCuentaMaestra() {
    const validator = $("#idFormCuentaMaestra").data("validator");
    if (validator) {
        validator.resetForm();
        validator.hideErrors();
    }
    $("#idFormCuentaMaestra").find(".is-invalid, .is-valid").removeClass("is-invalid is-valid");

    $("#opcion").val("Nuevo");
    $("#numTelefonoOriginal").val("");
    $("#numCuentaMaestraOriginal").val("");
    $("#idEntePublicoForm").val("").removeClass("is-valid is-invalid");
    $("#idUnidadAdministrativaForm").val("");
    $("#descUnidadAdministrativaForm").val("").removeClass("is-valid is-invalid");
    $("#numTelefono").val("");
    $("#descNumTelefono").val("").removeClass("is-valid is-invalid");
    $("#cveTipoConexionForm").val("").removeClass("is-valid is-invalid");
    $("#idCuentaMaestraForm").val("");
    $("#descCuentaMaestraForm").val("").removeClass("is-valid is-invalid");
    $("#idDomicilioEnteForm").empty().append($('<option></option>').attr('value', '').text('Seleccione una Dependencia primero'));
    $("#idUbicacionForm").empty().append($('<option></option>').attr('value', '').text('Seleccione un Domicilio primero'));
    $("#curpUsuarioContactoForm").val("");
    $("#descUsuarioContactoForm").val("").removeClass("is-valid is-invalid");
    $("#anchoBandaInternetMb").val("");
    $("#fechaBaja").val("");
    $("#observaciones").val("");
}

function limpiarBusquedaUsuarioContacto() {
    $('#formBusquedaUsuarioContacto')[0].reset();
    $('#idCardTablaUsuarioContacto').hide();
    if (tableUsuarioContacto != null) {
        tableUsuarioContacto.clear().draw();
    }
}

function obtieneUsuariosContactoSearch() {

    const formulario = {
        isVigente: true,
        isHistorico: true,
        isNoVigente: false,
        rfc: $("#rfcSearchContacto").val(),
        curp: $("#curpSearchContacto").val(),
        nombre: $("#nombreSearchContacto").val(),
        apellidoPaterno: $("#apellidoPaternoSearchContacto").val()
    };

    if (tableUsuarioContacto != null)
        tableUsuarioContacto.clear();

    onLoading(null);

    tableUsuarioContacto = new DataTable('#tableUsuarioContacto', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering: false,
        ajax: {
            url: contexto + '/api/catalogoCatUsuario/tablaUsuarioSearch',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function (xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val());
            },
            data: function (d) {
                return JSON.stringify(formulario);
            },
            dataSrc: function (json) {
                return json.aaData;
            },
            complete: function () {
                $("#idCardTablaUsuarioContacto").show();
                offLoading(null);
            }
        },
        bProcessing: true,
        bServerSide: false,
        language: {
            "url": contexto + '/include/datatables/spanish.json'
        },
        rowCallback: function (row, data) {
            const match = /seleccionarUsuario\("([^"]+)"\)/.exec(data[4]);
            $(row).attr('data-id-usuario', match ? match[1] : '');
            $(row).attr('data-curp', data[1]);
            $(row).attr('data-nombre', data[2]);
        },
        lengthChange: false,
        select: false
    });
}

function seleccionarUsuario(idUsuario) {
    const $row = $('tr[data-id-usuario="' + idUsuario + '"]');
    const curp = $row.attr('data-curp');
    const nombre = $row.attr('data-nombre');

    $("#curpUsuarioContactoForm").val(curp);
    $("#descUsuarioContactoForm").val(nombre).removeClass("is-valid is-invalid");

    $('#dlgModalBuscarUsuarioContacto').modal('hide');
    $('#dlgModalFormCuentaMaestra').modal('show');
}

function obtieneCuentaMaestra() {

    const formulario = {
        idEntePublico: $("#idEntePublico").val() || null,
        idUnidadAdministrativa: $("#idUnidadAdministrativa").val() || null,
        cveTipoConexion: $("#cveTipoConexion").val() || null,
        isVigente: $("#idVigente").is(':checked'),
        isHistorico: $("#idHistorico").is(':checked')
    };

    onLoading(null);

    tablaCtaMaestra = new DataTable('#tablaCuentaMaestra', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        autoWidth: false,
        ordering: true,
        ajax: {
            url: contexto + baseCtaMaestra + '/tabla',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            data: function (d) {
                return JSON.stringify(formulario);
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val());
            },
            dataSrc: function (json) {
                return json.aaData;
            },
            complete: function () {
                offLoading(null);
            }
        },
        bProcessing: true,
        bServerSide: false,
        language: {
            "url": contexto + '/include/datatables/spanish.json'
        },
        columnDefs: [
            {targets: 9, orderable: false, className: "text-center"}
        ],
        lengthChange: true,
        select: false
    });
}

function guardarCuentaMaestra() {
    let titleSave = '¿Guardar Línea Telefónica?';
    let msgSave = '¿Estás seguro que deseas guardar los datos de la línea telefónica?';
    if ($('#opcion').val() === "Editar") {
        titleSave = '¿Actualizar Línea Telefónica?';
        msgSave = '¿Estás seguro que deseas actualizar los datos de la línea telefónica?';
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
            guardarCuentaMaestraAsync();
        }
    });
}

const guardarCuentaMaestraAsync = async () => {

    const body = {
        opcion: $("#opcion").val(),
        numTelefonoOriginal: $("#numTelefonoOriginal").val() || null,
        numCuentaMaestraOriginal: $("#numCuentaMaestraOriginal").val() || null,
        telefono: {
            numTelefono: $("#numTelefono").val(),
            entePublico: {idEntePublico: $("#idEntePublicoForm").val()},
            unidadAdministrativa: {idUnidadAdministrativa: $("#idUnidadAdministrativaForm").val()},
            ubicacionUnidadAdmin: $("#idUbicacionForm").val() ? {idUbicacionAdmtva: $("#idUbicacionForm").val()} : null,
            curpUsuarioContacto: $("#curpUsuarioContactoForm").val() || null,
            anchoBandaInternetMb: $("#anchoBandaInternetMb").val() ? parseInt($("#anchoBandaInternetMb").val(), 10) : null,
            catCuentaMaestra: {numCuentaMaestra: $("#idCuentaMaestraForm").val()},
            catTipoConexionLineaTelef: {cveTipoConexionLineaTelef: $("#cveTipoConexionForm").val()},
            fechaBaja: $("#fechaBaja").val() ? $("#fechaBaja").val() + "T00:00:00" : null,
            observaciones: $("#observaciones").val()
        }
    };

    const options = {method: "POST", body: JSON.stringify(body), headers: headersCtaMaestra};

    try {
        onLoading("dlgModalFormCuentaMaestra");
        const res = await fetch(contexto + baseCtaMaestra + '/guardar', options);
        const respJSON = await res.json();

        if (res.ok) {
            Toast.fire({
                icon: "success",
                title: $("#opcion").val() === "Nuevo"
                    ? "La línea telefónica se ha guardado"
                    : "La línea telefónica se ha actualizado"
            });
            obtieneCuentaMaestra();
            offLoading("dlgModalFormCuentaMaestra");
            $('#dlgModalFormCuentaMaestra').modal('hide');
        } else {
            Toast.fire({icon: "error", title: respJSON.message});
            offLoading("dlgModalFormCuentaMaestra");
        }
    } catch (e) {
        offLoading("dlgModalFormCuentaMaestra");
        Toast.fire({icon: "error", title: "Error al guardar"});
    }
};

function editarTelefono(numTelefono, numCuentaMaestra) {
    limpiarFormularioCuentaMaestra();
    $("#opcion").val("Editar");
    $("#numTelefonoOriginal").val(numTelefono);
    $("#numCuentaMaestraOriginal").val(numCuentaMaestra);
    $('#titleFormCuentaMaestra').text("Editar Línea Telefónica");
    $('#contTipoConexionForm').hide();

    onLoading(null);

    fetch(contexto + baseCtaMaestra + '/get/' + encodeURIComponent(numTelefono) + '/' + encodeURIComponent(numCuentaMaestra), {method: "GET", headers: headersCtaMaestra})
        .then(res => res.json())
        .then(async reg => {
            $("#numTelefono").val(reg.numTelefono);
            $("#descNumTelefono").val(reg.numTelefono);
            $("#fechaBaja").val(reg.fechaBaja ? reg.fechaBaja.substring(0, 10) : "");
            $("#observaciones").val(reg.observaciones || "");
            $("#anchoBandaInternetMb").val(reg.anchoBandaInternetMb != null ? reg.anchoBandaInternetMb : "");

            if (reg.catCuentaMaestra) {
                $("#idCuentaMaestraForm").val(reg.catCuentaMaestra.numCuentaMaestra);
                $("#descCuentaMaestraForm").val(reg.catCuentaMaestra.numCuentaMaestra + (reg.catCuentaMaestra.nombreClienteCuentaMaestra ? (' - ' + reg.catCuentaMaestra.nombreClienteCuentaMaestra) : ''));
            }

            if (reg.curpUsuarioContacto) {
                $("#curpUsuarioContactoForm").val(reg.curpUsuarioContacto);
                $("#descUsuarioContactoForm").val(reg.curpUsuarioContacto);
                fetch(contexto + '/api/catalogoCatUsuario/tablaUsuarioSearch', {
                    method: "POST", headers: headersCtaMaestra,
                    body: JSON.stringify({isVigente: true, isHistorico: true, isNoVigente: true, curp: reg.curpUsuarioContacto})
                })
                    .then(r => r.json())
                    .then(data => {
                        if (data.aaData && data.aaData.length > 0)
                            $("#descUsuarioContactoForm").val(data.aaData[0][2]);
                    })
                    .catch(() => {
                    });
            }

            await Promise.all([
                fillSelectGet('/api/entePublico/usuario', '#idEntePublicoForm', 'idEntePublico', 'nombreEntePublico', null, null),
                fillSelectGet(baseCtaMaestra + '/getTiposConexion', '#cveTipoConexionForm', 'cveTipoConexionLineaTelef', 'nombreTipoConexionLineaTelef', null, null)
            ]);

            $("#cveTipoConexionForm").val(reg.catTipoConexionLineaTelef ? reg.catTipoConexionLineaTelef.cveTipoConexionLineaTelef : "");

            if (reg.unidadAdministrativa) {
                $("#idUnidadAdministrativaForm").val(reg.unidadAdministrativa.idUnidadAdministrativa);
                $("#descUnidadAdministrativaForm").val(reg.unidadAdministrativa.nombreUnidadAdministrativa);
            }

            if (reg.entePublico) {
                $("#idEntePublicoForm").val(reg.entePublico.idEntePublico);
                const idEnte = reg.entePublico.idEntePublico;

                obtienearbolPorInstitucion(function (data) {
                    $("#idUnidadAdministrativaForm").val(data.id);
                    $("#descUnidadAdministrativaForm").val(data.text);
                    limpiarBuscarArbol();
                    $('#modal_arbol_unidad').modal('hide');
                    $("#descUnidadAdministrativaForm").removeClass("is-valid is-invalid");
                }, idEnte);

                await fillSelectGet('/api/catalogo/domicilioentePublico/all', '#idDomicilioEnteForm', 'idDomicilio', 'nombreDomicilio', idEnte, null);

                if (reg.ubicacionUnidadAdmin && reg.ubicacionUnidadAdmin.idDomicilioEnte) {
                    $("#idDomicilioEnteForm").val(reg.ubicacionUnidadAdmin.idDomicilioEnte);
                    await fillSelectGet('/api/catalogo/ubicacionUnidadAministrativa/all', '#idUbicacionForm', 'idUbicacionAdaministrativa', 'nombreUbicacion', reg.ubicacionUnidadAdmin.idDomicilioEnte, null);
                    $("#idUbicacionForm").val(reg.ubicacionUnidadAdmin.idUbicacionAdmtva);
                }
            }

            offLoading(null);
            $('#dlgModalFormCuentaMaestra').modal('show');
        })
        .catch(() => {
            offLoading(null);
            Toast.fire({icon: "error", title: "Error al cargar la línea telefónica"});
        });
}

function eliminarTelefono(numTelefono, numCuentaMaestra) {
    Swal.fire({
        title: 'Eliminar Línea Telefónica',
        text: '¿Estás seguro que deseas eliminar la línea telefónica?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: 'No'
    }).then(function (result) {
        if (result.isConfirmed) {
            eliminarTelefonoAsync(numTelefono, numCuentaMaestra);
        }
    });
}

const eliminarTelefonoAsync = async (numTelefono, numCuentaMaestra) => {
    try {
        onLoading(null);
        const res = await fetch(
            contexto + baseCtaMaestra + '/eliminar/' + encodeURIComponent(numTelefono) + '/' + encodeURIComponent(numCuentaMaestra),
            {method: "DELETE", headers: headersCtaMaestra});

        if (res.ok) {
            Toast.fire({icon: "success", title: "Línea telefónica eliminada correctamente"});
            obtieneCuentaMaestra();
        } else {
            const respJSON = await res.json();
            Toast.fire({icon: "error", title: respJSON.message});
        }
        offLoading(null);
    } catch (e) {
        offLoading(null);
        Toast.fire({icon: "error", title: "Error al eliminar"});
    }
};

function enviarTelefono(numTelefono, numCuentaMaestra) {
    Swal.fire({
        title: 'Enviar Línea Telefónica',
        text: '¿Estás seguro que deseas enviar esta línea telefónica a validación?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: 'No'
    }).then(function (result) {
        if (result.isConfirmed) {
            enviarTelefonoAsync(numTelefono, numCuentaMaestra);
        }
    });
}

const enviarTelefonoAsync = async (numTelefono, numCuentaMaestra) => {
    try {
        onLoading(null);
        const res = await fetch(
            contexto + baseCtaMaestra + '/enviar/' + encodeURIComponent(numTelefono) + '/' + encodeURIComponent(numCuentaMaestra),
            {method: "POST", headers: headersCtaMaestra});

        if (res.ok) {
            Toast.fire({icon: "success", title: "Línea telefónica enviada correctamente"});
            obtieneCuentaMaestra();
        } else {
            const respJSON = await res.json();
            Toast.fire({icon: "error", title: respJSON.message});
        }
        offLoading(null);
    } catch (e) {
        offLoading(null);
        Toast.fire({icon: "error", title: "Error al enviar"});
    }
};

function validarTelefono(numTelefono, numCuentaMaestra) {
    $("#validarNumTelefono").val(numTelefono);
    $("#validarNumCuentaMaestra").val(numCuentaMaestra);
    $("#descValidacionTelefonoModal").val("").removeClass("is-valid is-invalid");
    $('#dlgModalValidarCuentaMaestra').modal('show');
}

$(document).on('submit', '#idFormValidarCuentaMaestra', function (e) {
    e.preventDefault();
    const desc = $("#descValidacionTelefonoModal").val();
    if (!desc || desc.trim() === '') {
        $("#descValidacionTelefonoModal").addClass("is-invalid");
        return;
    }
    $("#descValidacionTelefonoModal").removeClass("is-invalid").addClass("is-valid");

    Swal.fire({
        title: 'Validar Línea Telefónica',
        text: '¿Estás seguro que deseas validar esta línea telefónica?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: 'No'
    }).then(function (result) {
        if (result.isConfirmed) {
            validarTelefonoAsync();
        }
    });
});

const validarTelefonoAsync = async () => {
    const numTelefono = $("#validarNumTelefono").val();
    const numCuentaMaestra = $("#validarNumCuentaMaestra").val();
    const body = {descValidacionTelefono: $("#descValidacionTelefonoModal").val()};

    try {
        onLoading("dlgModalValidarCuentaMaestra");
        const res = await fetch(
            contexto + baseCtaMaestra + '/validar/' + encodeURIComponent(numTelefono) + '/' + encodeURIComponent(numCuentaMaestra),
            {method: "POST", headers: headersCtaMaestra, body: JSON.stringify(body)});

        if (res.ok) {
            Toast.fire({icon: "success", title: "Línea telefónica validada correctamente"});
            obtieneCuentaMaestra();
            offLoading("dlgModalValidarCuentaMaestra");
            $('#dlgModalValidarCuentaMaestra').modal('hide');
        } else {
            const respJSON = await res.json();
            Toast.fire({icon: "error", title: respJSON.message});
            offLoading("dlgModalValidarCuentaMaestra");
        }
    } catch (e) {
        offLoading("dlgModalValidarCuentaMaestra");
        Toast.fire({icon: "error", title: "Error al validar"});
    }
};

function rechazarTelefono(numTelefono, numCuentaMaestra) {
    Swal.fire({
        title: 'Rechazar Línea Telefónica',
        text: '¿Estás seguro que deseas rechazar esta línea telefónica?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: 'No'
    }).then(function (result) {
        if (result.isConfirmed) {
            rechazarTelefonoAsync(numTelefono, numCuentaMaestra);
        }
    });
}

const rechazarTelefonoAsync = async (numTelefono, numCuentaMaestra) => {
    try {
        onLoading(null);
        const res = await fetch(
            contexto + baseCtaMaestra + '/rechazar/' + encodeURIComponent(numTelefono) + '/' + encodeURIComponent(numCuentaMaestra),
            {method: "POST", headers: headersCtaMaestra});

        if (res.ok) {
            Toast.fire({icon: "success", title: "Línea telefónica rechazada correctamente"});
            obtieneCuentaMaestra();
        } else {
            const respJSON = await res.json();
            Toast.fire({icon: "error", title: respJSON.message});
        }
        offLoading(null);
    } catch (e) {
        offLoading(null);
        Toast.fire({icon: "error", title: "Error al rechazar"});
    }
};

function exportarFiltroCuentaMaestra() {

    const params = {
        idEntePublico: $("#idEntePublico").val() || null,
        idUnidadAdministrativa: $("#idUnidadAdministrativa").val() || null,
        cveTipoConexion: $("#cveTipoConexion").val() || null,
        isVigente: $("#idVigente").is(':checked'),
        isHistorico: $("#idHistorico").is(':checked')
    };

    $("#btnExportarFiltro").prop("disabled", true);
    onLoading(null);

    fetch(contexto + baseCtaMaestra + '/exportarFiltro', {
        method: 'POST',
        headers: headersCtaMaestra,
        body: JSON.stringify(params)
    })
        .then(response => {
            if (!response.ok) throw new Error("Error al generar el archivo.");

            const disposition = response.headers.get("Content-Disposition");
            let fileName = "cuenta_maestra.xlsx";
            if (disposition && disposition.indexOf("filename=") !== -1) {
                fileName = disposition.split("filename=")[1].replace(/"/g, "");
            }
            return response.blob().then(blob => ({blob, fileName}));
        })
        .then(data => {
            const url = window.URL.createObjectURL(data.blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = data.fileName;
            document.body.appendChild(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);

            offLoading(null);
            $("#btnExportarFiltro").prop("disabled", false);
            Toast.fire({icon: "success", title: "La exportación se realizó correctamente."});
        })
        .catch(error => {
            offLoading(null);
            $("#btnExportarFiltro").prop("disabled", false);
            Toast.fire({icon: "error", title: error.message});
        });
}
