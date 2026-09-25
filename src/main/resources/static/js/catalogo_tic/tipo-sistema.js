const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

$(document).ready(function(){

    $('[data-bs-toggle="tooltip"]').tooltip();

    jQuery.event.special.mousewheel = {
        setup: function( _, ns, handle ){
            this.addEventListener("mousewheel", handle, { passive: true });
        }
    };

    obtienTipoSistemas();

    $('#idNuevoTipoSistema').click(function () {
        limpiarFormularioSistem();
        $('#titleFormTipoSistemas').text("Nuevo Tipo de Sistema");
        $('#dlgModalFormTipoSistemasTic').modal('show');
    });

    $("#idFormTipoSistema").validate( {
        ignore: "",
        rules: {
            cveTipoSistemas:"required",
            nombreTipoSistemaAbr:"required",
            nombreTipoSistema:"required",
            alcanseSubtipoSistema:"required",
            numeroOrden:"required"
        },
        messages: {
            cveTipoSistemas:"Campo requerido",
            nombreTipoSistemaAbr:"Campo requerido",
            nombreTipoSistema:"Campo Requerido",
            alcanseSubtipoSistema:"Campo Requerido",
            numeroOrden:"Campo Requerido"
        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            //error.insertAfter(element);
            error.addClass( "invalid-feedback" );
            element.closest('.form-floating').append(error);
        },
        highlight: function (element, errorClass) {
            $( element ).addClass( "is-invalid" ).removeClass( "is-valid" );
        },
        unhighlight: function (element, errorClass) {
            $( element ).addClass( "is-valid" ).removeClass( "is-invalid" );
        },
        submitHandler: function(form,e){
            e.preventDefault();
        }
    });

    $("#idFormTipoSistema").submit(function(e) {
        e.preventDefault();
        if ($('#idFormTipoSistema').valid()) {
            guardar();
        }
    });

});

function limpiarFormularioSistem(){
    $("#cveTipoSistemas").removeClass("is-valid");
    $("#cveTipoSistemas").removeClass("is-invalid");
    $("#nombreTipoSistema").removeClass("is-valid");
    $("#nombreTipoSistema").removeClass("is-invalid");
    $("#nombreTipoSistemaAbr").removeClass("is-valid");
    $("#nombreTipoSistemaAbr").removeClass("is-invalid");
    $("#alcanseSubtipoSistema").removeClass("is-valid");
    $("#alcanseSubtipoSistema").removeClass("is-invalid");
    $("#numeroOrden").removeClass("is-valid");
    $("#numeroOrden").removeClass("is-invalid");
    $("#cveEstatus").removeClass("is-valid");
    $("#cveEstatus").removeClass("is-invalid");

    $("#opcion").val("Nuevo");
    $("#cveTipoSistemas").removeAttr("disabled");
    $("#cveTipoSistemas").val("");
    $("#nombreTipoSistema").val("");
    $("#nombreTipoSistemaAbr").val("");
    $("#alcanseSubtipoSistema").val("");
    $("#numeroOrden").val("");
    $("#cveEstatus").val("VIG");
    $("#requierePadreNo").prop("checked", true);

}
function obtienTipoSistemas(){

    const table = new DataTable('#tableTipoSistema', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        paging: false,
        autoWidth: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/catalogo/tic/tablaTipoSistema',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
        },
        bProcessing: true,
        bServerSide:false,
        columnDefs: [
            {
                "targets": 0,
                "width":'250px',
                "className": "text-left",
            },
            {
                "targets": 1,
                "width":'250px',
                "className": "text-left",
            },
            {
                "targets": 2,
                "className": "text-left",
            },
            {
                "targets": 3,
                "width":'80px',
                "className": "text-center",
            }, {
                "targets": 4,
                "width":'80px',
                "className": "text-center",
            },
            {
                "targets": 5,
                "width":'80px',
                "className": "text-center",
            },
            {
                "targets": 6,
                "width":'80px',
                "className": "text-center",
            }],
        language:{
            "url": contexto+'/include/datatables/spanish.json'
        },
        lengthChange: false,
        select: false
    });

}
const guardarTipoSistemaAsync = async () =>
{
    const options = {
        method: "POST",
        body: JSON.stringify({
            cveTipoSistema: $("#cveTipoSistemas").val(),
            nombreTipoSistema: $("#nombreTipoSistema").val(),
            nombreTipoSistemaAbreviado: $("#nombreTipoSistemaAbr").val(),
            alcanceSistema: $("#alcanseSubtipoSistema").val(),
            numeroOrdenTipoSistema: $("#numeroOrden").val(),
            requiereSistemaPadre: $("#requierePadreNo").is(":checked")?"N":"S",
            cveEstatus: $("#cveEstatus").val(),
            opcion: $("#opcion").val()
        }),
        headers: headers
    };
    try {
        onLoading("dlgModalFormTipoSistemasTic");
        const res = await fetch(contexto + '/api/catalogo/tic/guardarTipoSistemas', options);
        const respJSON = await res.json();
        if (res.ok) {

            if ($("#opcion").val()=="Nuevo"){
                Toast.fire({
                    icon: "success",
                    title: "Los datos del tipo sistems se han guardado"
                });

            }else{
                Toast.fire({
                    icon: "success",
                    title: "Los datos del tipo sistems se han actualizado"
                });
            }

            obtienTipoSistemas();

            offLoading("dlgModalFormTipoSistemasTic");
            $('#dlgModalFormTipoSistemasTic').modal('hide');

        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalFormTipoSistemasTic");
        }
    } catch (e) {
        var error = JSON.parse(e.responseText);
        offLoading("dlgModalFormTipoSistemasTic");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
}
function guardar(){
    let titleSave='¿Guardar Tipo Sistema?';
    let msgSave='¿Estas seguro que deseas guardar los datos del Tipo Sistema?';
    if ($('#opcion').val() == "Editar"){
        titleSave='¿Actualizar Tipo Sistema?';
        msgSave='¿Estas seguro que deseas actualizar los datos del Tipo Sistema?';
    }

    Swal.fire({
        title: titleSave,
        text: msgSave,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function (result) {
        if (result.isConfirmed) {
            guardarTipoSistemaAsync();
        }
    });
}

function editarTipoSistema(cveTipoSistema) {
    limpiarFormularioSistem();
    $("#cveTipoSistemas").attr('disabled','disabled');
    $("#opcion").val("Editar")
    getTipoSistema(cveTipoSistema);

    $('#titleFormTipoSistemas').text("Editar Tipo de Sistema");
    $('#dlgModalFormTipoSistemasTic').modal('show')
}

const getTipoSistema = async (cveTipoSistema) => {
    const options ={
        method: "GET",
        headers: headers
    };
    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/catalogo/tic/getTipoSistema/'+cveTipoSistema, options);
        const respJSON = await res.json();

        if (res.ok) {
            offLoading(null);
            $("#cveTipoSistemas").val(respJSON.cveTipoSistema);
            $("#nombreTipoSistema").val(respJSON.nombreTipoSistema);
            $("#nombreTipoSistemaAbr").val(respJSON.nombreTipoSistemaAbreviado);
            $("#alcanseSubtipoSistema").val(respJSON.alcanceSistema);
            $("#numeroOrden").val(respJSON.numeroOrdenTipoSistema);
            $("#cveEstatus").val(respJSON.cveEstatus);

            if (respJSON.requiereSistemaPadre=="N")
                $("#requierePadreNo").prop("checked", true);
            else
                $("#requierePadreSi").prop("checked", true);

        }
    }catch (e) {
        offLoading(null);

        Toast.fire({
            icon: "error",
            title: "Error generado"
        });
    }
}


function eliminarTipoSistema(cveTipoSistema){

    Swal.fire({
        title: 'Eliminar Tipo de Sistema',
        text: '¿Estas seguro que deseas eliminar el Tipo de Sistema?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminarTipoSistemaAsync(cveTipoSistema);
        }
    })

}

const eliminarTipoSistemaAsync = async (cveTipoSistema) => {
    const options = {
        method: "DELETE",
        body: JSON.stringify({cveTipoSistema: cveTipoSistema}),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/catalogo/tic/deleteTipoSistema', options);
        const respJSON = await res.json();

        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Tipo de sistema eliminado correctamente"
            });

            obtienTipoSistemas();
            offLoading(null);
        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading(null);
        }

    }catch (e) {
        var error = JSON.parse(e.responseText);
        offLoading(null);
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
}
