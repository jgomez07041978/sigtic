
const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

$(document).ready(function(){
    obtienTipodeservicio();

    $('#idNuevoTipoServicio').click(function () {
        limpiarFormulario();
        $('#titleFormTipoServicio').text("Nuevo Tipo Servicio");
        $('#dlgModalFormTipoServicioForm').modal('show');

    });


    $("#idFormTipoServicio").validate( {
        ignore: "",
        rules: {
            tipoServicioAbrev:"required",
            tipoServicio:"required",
            ordenTipoServicio:"required",
            cveEstatus:"required"
        },
        messages: {
            tipoServicioAbrev:"Campo requerido",
            tipoServicio:"Campo requerido",
            ordenTipoServicio:"Campo Requerido",
            cveEstatus:"Campo Requerido"
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

    $("#idFormTipoServicio").submit(function(e) {
        e.preventDefault();
        if ($('#idFormTipoServicio').valid()) {
            guardarTipoServicio();
        }
    });

});

function guardarTipoServicio(){

    let titleSave='Guardar Tipo de Servicio';
    let msgSave='¿Estas seguro que deseas guardar los datos del tipo de servicio?';
    if ($('#idTipoServicioTic').val() != ""){
        titleSave='Actualizar Tipo de Servicio';
        msgSave='¿Estas seguro que deseas actualizar los datos del tipo de servicio?';
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
            guardarTipoServicioAsync();
        }
    });
}



const guardarTipoServicioAsync = async () =>{
    const options={
        method: "POST",
        body: JSON.stringify({
            idTipoServicioTic:$("#idTipoServicioTic").val(),
            nombreTipoServicioTicAbrev:$("#tipoServicioAbrev").val(),
            nombreTipoServicioTic:$("#tipoServicio").val(),
            numOrdenTipoServicioTic:$("#ordenTipoServicio").val(),
            cveEstatus:$("#cveEstatus").val()
        }),
        headers: headers
    };

    try {
        onLoading("dlgModalFormTipoServicioForm");
        const res = await fetch(contexto+'/api/catalogo/tic/guardarTipoServicio', options);
        const respJSON = await res.json();
        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Los datos del tipo de servicio se guardaron correctamente"
            });

            offLoading("dlgModalFormTipoServicioForm");
            $('#dlgModalFormTipoServicioForm').modal('hide');
            obtienTipodeservicio();

        }else{
            offLoading("dlgModalFormTipoServicioForm");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }

    }catch (e) {
        offLoading("dlgModalFormTipoServicioForm");
        Toast.fire({
            icon: "error",
            title: "Error al guardar"
        });
    }
};



function limpiarFormulario(){
    $("#tipoServicioAbrev").removeClass( "is-valid" );
    $("#tipoServicioAbrev").removeClass( "is-invalid" );
    $("#tipoServicio").removeClass( "is-valid" );
    $("#tipoServicio").removeClass( "is-invalid" );
    $("#ordenTipoServicio").removeClass( "is-valid" );
    $("#ordenTipoServicio").removeClass( "is-invalid" );

    $("#idTipoServicioTic").val("");
    $("#tipoServicioAbrev").val("");
    $("#ordenTipoServicio").val("");
    $('#cveEstatus').val("VIG");
    $('#titleFormTipoServicio').text("Nuevo Tipo Servicio");
}
function obtienTipodeservicio(){

    const table = new DataTable('#tableTipodeServicio', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        paging: false,
        autoWidth: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/catalogo/tic/tablaTipoServicio',
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
                "targets": 2,
                "width":'80px',
                "className": "text-center",
            }, {
                "targets": 3,
                "width":'70px',
                "className": "text-center",
            },
             {
                "targets": 4,
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

function editarTipoServicio(IdTipoServicioTic) {
    limpiarFormulario();
    getTipoServicio(IdTipoServicioTic);

    $('#titleFormTipoServicio').text("Editar Sistema");
    $('#dlgModalFormTipoServicioForm').modal('show')
}

const eliminarTipoServicioAsync = async (idTipoServicioTic) => {
    const options = {
        method: "DELETE",
        body: JSON.stringify({idTipoServicioTic: idTipoServicioTic}),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/catalogo/tic/deleteTipoServicio', options);
        const respJSON = await res.json();

        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Tipo de  Servicio eliminado correctamente"
            });

            obtienTipodeservicio();
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

function eliminarTipoServicio(idTipoServicioTic){

    Swal.fire({
        title: 'Eliminar Tipo de Servicio',
        text: '¿Estas seguro que deseas eliminar el Tipo de Servicio?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminarTipoServicioAsync(idTipoServicioTic);
        }
    })

}



const getTipoServicio = async (idTipoServicioTic) => {
    const options ={
        method: "GET",
        headers: headers
    };
    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/catalogo/tic/getTipoServicio/'+idTipoServicioTic, options);
        const respJSON = await res.json();

        if (res.ok) {
            offLoading(null);
            $("#idTipoServicioTic").val(respJSON.idTipoServicioTic);
            $("#tipoServicioAbrev").val(respJSON.nombreTipoServicioTicAbrev);
            $("#tipoServicio").val(respJSON.nombreTipoServicioTic);
            $("#ordenTipoServicio").val(respJSON.numOrdenTipoServicioTic);
            $("#cveEstatus").val(respJSON.cveEstatus);


            $('#titleFormTipoServicio').text("Editar Tipo de Servicio");
            $('#dlgModalFormTipoServicioForm').modal('show');
        }
    }catch (e) {
        offLoading(null);

        Toast.fire({
            icon: "error",
            title: "Error generado"
        });
    }
}