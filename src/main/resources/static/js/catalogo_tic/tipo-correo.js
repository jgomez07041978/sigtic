const headers = {
    "X-CSRF-Token": $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

$(document).ready(function () {

    $('[data-bs-toggle="tooltip"]').tooltip();

    jQuery.event.special.mousewheel = {
        setup: function (_, ns, handle) {
            this.addEventListener("mousewheel", handle, {passive: true});
        }
    };

    obtienTipoCorreo();

    // Llenar combo de Tipo de Servicio al cargar
    fillSelectGet(
        "/api/catalogo/tic/getCatTipoServiciosTic",
        "#idTipoServicioCorreo",
        "idTipoServicioTic",
        "nombreTipoServicioTic",
        null,
        null
    );


    $(document).on("change", "#idTipoServicioCorreo", function () {
        var val = $(this).val();
        if (val !== "" && val !== null && val !== undefined) {
            obtenerSubtipoCorreo(val);
        } else {
            $("#idSubtipoServicioCorreo").empty().append(
                $('<option></option>').attr('value', '').text('Seleccione')
            );
        }
    });

    $('#idNuevoTipoCorreo').click(function () {
        limpiarFormularioCorreo();
        $('#titleFormTipoCorreo').text("Nuevo Tipo de Correo Electrónico");
        $('#dlgModalFormTipoCorreoTic').modal('show');
    });

    $("#idFormTipoCorreo").validate({
        ignore: "",
        rules: {
            cveTipoCorreo:       "required",
            nombreTipoCorreo:    "required",
            idTipoServicioCorreo: "required",
            idSubtipoServicioCorreo: "required",

    // nombreTipoCorreoAbr: "required",
 //           alcanceTipoCorreo:   "required",
            numeroOrden:         "required"
        },
        messages: {
            cveTipoCorreo:       "Campo requerido",
            nombreTipoCorreo:    "Campo requerido",
            idTipoServicioCorreo: "Campo requerido",
            idSubtipoServicioCorreo: "Campo requerido",
//            nombreTipoCorreoAbr: "Campo requerido",
//            alcanceTipoCorreo:   "Campo requerido",
            numeroOrden:         "Campo requerido"
        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            error.addClass("invalid-feedback");
            element.closest('.form-floating').append(error);
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

    $("#idFormTipoCorreo").submit(function (e) {
        e.preventDefault();
        if ($('#idFormTipoCorreo').valid()) {
            guardar();
        }
    });

});

// ─── Limpiar formulario ───────────────────────────────────────────────────────

function limpiarFormularioCorreo() {
    $("#cveTipoCorreo").removeClass("is-valid is-invalid");
    $("#nombreTipoCorreo").removeClass("is-valid is-invalid");
    $("#nombreTipoCorreoAbr").removeClass("is-valid is-invalid");
    $("#alcanceTipoCorreo").removeClass("is-valid is-invalid");
    $("#numeroOrden").removeClass("is-valid is-invalid");
    $("#cveEstatus").removeClass("is-valid is-invalid");
    $("#idTipoServicioCorreo").removeClass("is-valid is-invalid");
    $("#idSubtipoServicioCorreo").removeClass("is-valid is-invalid");

    $("#opcion").val("Nuevo");
    $("#cveTipoCorreo").removeAttr("disabled");
    $("#cveTipoCorreo").val("");
    $("#nombreTipoCorreo").val("");
    $("#nombreTipoCorreoAbr").val("");
    $("#alcanceTipoCorreo").val("");
    $("#numeroOrden").val("");
    $("#cveEstatus").val("VIG");


    $("#idTipoServicioCorreo").val("");

    // Limpiar subtipo directamente sin trigger
    $("#idSubtipoServicioCorreo").empty().append(
        $('<option></option>').attr('value', '').text('Seleccione')
    );
}


function obtienTipoCorreo() {
    const table = new DataTable('#tablaTipoCorreo', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        paging: false,
        autoWidth: false,
        ordering: false,
        ajax: {
            url: contexto + '/api/catalogo/tic/tablaTipoCorreo',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function (xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val());
            },
            dataSrc: function (json) {
                return json.aaData;
            }
        },
        bProcessing: true,
        bServerSide: false,
        columnDefs: [
            { targets: 0, width: "20%" },
            { targets: 1, width: "50%" }, // Nombre
            { targets: 2, width: "10%", className: "text-center" }, // Orden
            { targets: 3, width: "15%", className: "text-center" }, // Estatus
            { targets: 4, width: "2%", orderable: false, className: "text-center" } // Acciones
        ],
        language: {
            "url": contexto + '/include/datatables/spanish.json'
        },
        lengthChange: false,
        select: false
    });
}



function guardar() {
    let titleSave = '¿Guardar Tipo de Correo Electrónico?';
    let msgSave   = '¿Estás seguro que deseas guardar los datos del Tipo de Correo Electrónico?';
    if ($('#opcion').val() === "Editar") {
        titleSave = '¿Actualizar Tipo de Correo Electrónico?';
        msgSave   = '¿Estás seguro que deseas actualizar los datos del Tipo de Correo Electrónico?';
    }

    Swal.fire({
        title: titleSave,
        text:  msgSave,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor:  '#d33',
        confirmButtonText:  'Si',
        cancelButtonText:   'No'
    }).then(function (result) {
        if (result.isConfirmed) {
            guardarTipoCorreoAsync();
        }
    });
}

const guardarTipoCorreoAsync = async () => {
    const options = {
        method: "POST",
        body: JSON.stringify({
            cveTipoCorreoElectronico:     $("#cveTipoCorreo").val(),
            nombreTipoCorreoElectronico:  $("#nombreTipoCorreo").val(),
            nombreTipoCorreoElectronicoA: $("#nombreTipoCorreoAbr").val(),
            alcanceTipoCorreoElectronico: $("#alcanceTipoCorreo").val(),
            numeroOrden:                  $("#numeroOrden").val(),
            cveEstatus:                   $("#cveEstatus").val(),
            idSubtipoServicio:            $("#idSubtipoServicioCorreo").val() || null,
            opcion:                       $("#opcion").val()
        }),
        headers: headers
    };

    try {
        onLoading("dlgModalFormTipoCorreoTic");
        const res      = await fetch(contexto + '/api/catalogo/tic/guardarTipoCorreo', options);
        const respJSON = await res.json();

        if (res.ok) {
            Toast.fire({
                icon:  "success",
                title: $("#opcion").val() === "Nuevo"
                    ? "Los datos del tipo de correo se han guardado"
                    : "Los datos del tipo de correo se han actualizado"
            });
            obtienTipoCorreo();
            offLoading("dlgModalFormTipoCorreoTic");
            $('#dlgModalFormTipoCorreoTic').modal('hide');
        } else {
            Toast.fire({icon: "error", title: respJSON.message});
            offLoading("dlgModalFormTipoCorreoTic");
        }
    } catch (e) {
        offLoading("dlgModalFormTipoCorreoTic");
        Toast.fire({icon: "error", title: "Error al guardar"});
    }
};


function editarTipoCorreo(cveTipoCorreoElectronico) {
    limpiarFormularioCorreo();
    $("#cveTipoCorreo").attr('disabled', 'disabled');
    $("#opcion").val("Editar");
    getTipoCorreo(cveTipoCorreoElectronico);
    $('#titleFormTipoCorreo').text("Editar Tipo de Correo Electrónico");
    $('#dlgModalFormTipoCorreoTic').modal('show');
}

const getTipoCorreo = async (cveTipoCorreoElectronico) => {
    const options = {method: "GET", headers: headers};
    try {
        onLoading(null);
        const res      = await fetch(contexto + '/api/catalogo/tic/getTipoCorreo/' + cveTipoCorreoElectronico, options);
        const respJSON = await res.json();

        if (res.ok) {
            offLoading(null);
            $("#cveTipoCorreo").val(respJSON.cveTipoCorreoElectronico);
            $("#nombreTipoCorreo").val(respJSON.nombreTipoCorreoElectronico);
            $("#nombreTipoCorreoAbr").val(respJSON.nombreTipoCorreoElectronicoA);
            $("#alcanceTipoCorreo").val(respJSON.alcanceTipoCorreoElectronico);
            $("#numeroOrden").val(respJSON.numeroOrden);
            $("#cveEstatus").val(respJSON.cveEstatus);

            if (respJSON.idSubtipoServicio != null) {

                //  obtener el subtipo para conocer su tipo padre
                const resSubtipo = await fetch(
                    contexto + '/api/catalogo/tic/getCatSubtipoServiciosTicById/' + respJSON.idSubtipoServicio,
                    {method: "GET", headers: headers}
                );

                if (resSubtipo.ok) {
                    const subtipo = await resSubtipo.json();

                    //  seleccionar el Tipo en el primer combo
                    $("#idTipoServicioCorreo").val(subtipo.catTipoServiciosTic.idTipoServicioTic);

                    // Paso 3 — obtener la lista de subtipos del tipo seleccionado
                    const resLista = await fetch(
                        contexto + '/api/catalogo/tic/getCatSubtipoServiciosTic/' + subtipo.catTipoServiciosTic.idTipoServicioTic,
                        {method: "GET", headers: headers}
                    );

                    if (resLista.ok) {
                        const listaSubtipos = await resLista.json();

                        // llenar el combo de subtipo
                        $("#idSubtipoServicioCorreo").empty().append(
                            $('<option></option>').attr('value', '').text('Seleccione')
                        );
                        listaSubtipos.forEach(function (item) {
                            $("#idSubtipoServicioCorreo").append(
                                $('<option></option>')
                                    .attr('value', item.idSubtipoServicioTic)
                                    .text(item.nombreSubtipoServicioTic)
                            );
                        });

                        //  seleccionar el subtipo guardado
                        $("#idSubtipoServicioCorreo").val(respJSON.idSubtipoServicio);
                    }
                }
            }
        }
    } catch (e) {
        offLoading(null);
        Toast.fire({icon: "error", title: "Error al cargar el tipo de correo"});
    }
};



function eliminarTipoCorreo(cveTipoCorreoElectronico) {
    Swal.fire({
        title: 'Eliminar Tipo de Correo Electrónico',
        text:  '¿Estás seguro que deseas eliminar el Tipo de Correo Electrónico?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor:  '#d33',
        confirmButtonText:  'Si',
        cancelButtonText:   'No'
    }).then(function (result) {
        if (result.isConfirmed) {
            eliminarTipoCorreoAsync(cveTipoCorreoElectronico);
        }
    });
}

const eliminarTipoCorreoAsync = async (cveTipoCorreoElectronico) => {
    const options = {
        method: "DELETE",
        body:   JSON.stringify({cveTipoCorreoElectronico: cveTipoCorreoElectronico}),
        headers: headers
    };

    try {
        onLoading(null);
        const res      = await fetch(contexto + '/api/catalogo/tic/deleteTipoCorreo', options);
        const respJSON = await res.json();

        if (res.ok) {
            Toast.fire({icon: "success", title: "Tipo de correo eliminado correctamente"});
            obtienTipoCorreo();
            offLoading(null);
        } else {
            Toast.fire({icon: "error", title: respJSON.message});
            offLoading(null);
        }
    } catch (e) {
        offLoading(null);
        Toast.fire({icon: "error", title: "Error al eliminar"});
    }
};


function obtenerSubtipoCorreo(idTipoServicio) {
    if (idTipoServicio !== "" && idTipoServicio !== null && idTipoServicio !== undefined) {
        fillSelectGet(
            "/api/catalogo/tic/getCatSubtipoServiciosTic",
            "#idSubtipoServicioCorreo",
            "idSubtipoServicioTic",
            "nombreSubtipoServicioTic",
            idTipoServicio,
            null
        );
    } else {
        $("#idSubtipoServicioCorreo").empty().append(
            $('<option></option>').attr('value', '').text('Seleccione')
        );
    }
}