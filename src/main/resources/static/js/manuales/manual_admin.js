
const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let idGrupoManualTemp=null;
let nombreGrupoTemp=null;

$(document).ready(function(){
    obtenGrupos();

    $('#idNuevoGrupo').click(function () {
        limpiarFormulario();
        $('#dlgModalFormGrupo').modal('show');

    });

    $("#idNuevoManual").click(function(){
        limpiarFormManual();
        $('#dlgModalFormManual').modal('show');
    });


    $("#idFormGrupo").validate( {
        ignore: "",
        rules: {
            nombreGrupo:"required",
            numeroOrden:"required"
        },
        messages: {
            nombreGrupo:"Campo requerido",
            numeroOrden:"Campo requerido"
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

    $("#idFormGrupo").submit(function(e) {
        e.preventDefault();
        if ($('#idFormGrupo').valid()) {
            guardarGrupo();
        }
    });

    $("#idFormManual").validate( {
        ignore: "",
        rules: {
            nombreManual:"required",
            descripcionManual:"required",
            tipoManualUsuario:"required",
            numeroOrdenManual:"required",
            rutaManual: {
                required: function () {
                    if ($("#idManual").val() == "") {
                        return true;
                    } else {
                        $("#rutaManual").removeClass("is-invalid")
                        $("#rutaManual").removeClass("is-valid")
                        $("#rutaManual").addClass("is-invalid");
                        return false;
                    }
                }
            },
        },
        messages: {
            nombreManual:"Campo requerido",
            descripcionManual:"Campo requerido",
            tipoManualUsuario:"Campo requerido",
            numeroOrdenManual:"Campo requerido",
            rutaManual:"Campo requerido",
        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            var n = element.attr("id");
            if (n=="rutaManual")
                element.closest('.form-x').append(error);
            else
                element.closest('.form-floating').append(error);
            error.addClass( "invalid-feedback" );

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

    $("#idFormManual").submit(function(e) {
        e.preventDefault();
        if ($('#idFormManual').valid()) {
            guardarManual();
        }
    });

});

function limpiarFormManual(){
    $("#nombreManual").removeClass( "is-valid" );
    $("#nombreManual").removeClass( "is-invalid" );
    $("#descripcionManual").removeClass( "is-valid" );
    $("#descripcionManual").removeClass( "is-invalid" );
    $("#numeroOrdenManual").removeClass( "is-valid" );
    $("#numeroOrdenManual").removeClass( "is-invalid" );
    $("#rutaManual").removeClass( "is-valid" );
    $("#rutaManual").removeClass( "is-invalid" );


    $("#titleFormManual").text("Nuevo Manual de "+nombreGrupoTemp);
    $("#idManual").val("");
    $("#nombreManual").val("");
    $("#descripcionManual").val("");
    $("#numeroOrdenManual").val("");
    $("#tipoManualUsuario").prop("checked", true);
    $("#numeroOrdenManual").val("");
    $("#rutaManual").val("");
    $(".custom-file-label").text("Seleccionar");
}

function guardarGrupo(){

    let titleSave='Guardar Grupo';
    let msgSave='¿Estas seguro que deseas guardar los datos del grupo?';
    if ($('#idGrupoManual').val() != ""){
        titleSave='Actualizar Grupo';
        msgSave='¿Estas seguro que deseas actualizar los datos del grupo?';
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
            guardarGrupoAsync();
        }
    });
}



const guardarGrupoAsync = async () =>{
    const options={
        method: "POST",
        body: JSON.stringify({
            idGrupoManual:$("#idGrupoManual").val(),
            nombreGrupo:$("#nombreGrupo").val(),
            numeroOrden:$("#numeroOrden").val()
        }),
        headers: headers
    };

    try {
        onLoading("dlgModalFormGrupo");
        const res = await fetch(contexto+'/api/manual/guardarGrupo', options);
        const respJSON = await res.json();
        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Los datos del grupo se guardaron correctamente"
            });

            offLoading("dlgModalFormGrupo");
            $('#dlgModalFormGrupo').modal('hide');
            obtenGrupos();

        }else{
            offLoading("dlgModalFormGrupo");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }

    }catch (e) {
        offLoading("dlgModalFormGrupo");
        Toast.fire({
            icon: "error",
            title: "Error al guardar"
        });
    }
};



function limpiarFormulario(){
    $("#nombreGrupo").removeClass( "is-valid" );
    $("#nombreGrupo").removeClass( "is-invalid" );
    $("#numeroOrden").removeClass( "is-valid" );
    $("#numeroOrden").removeClass( "is-invalid" );

    $("#idGrupoManual").val("");
    $("#nombreGrupo").val("");
    $("#numeroOrden").val("");
    $('#titleFormGrupo').text("Nuevo Grupo para Manual");
}
function obtenGrupos(){

    const table = new DataTable('#tableGrupoManuales', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        paging: false,
        autoWidth: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/manual/grupos',
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
                "targets": 1,
                "width":'90px',
                "className": "text-center",
            },
             {
                "targets": 2,
                "width":'130px',
                "className": "text-center",
            }],
        language:{
            "url": contexto+'/include/datatables/spanish.json'
        },
        lengthChange: false,
        select: false
    });
}

function editarGrupo(idGrupoManual) {
    limpiarFormulario();
    getGrupoId(idGrupoManual);

    $('#titleFormGrupo').text("Editar Grupo para Manuales");
    $('#dlgModalFormGrupo').modal('show')
}

const eliminarGrupoAsync = async (idGrupoManual) => {
    const options = {
        method: "DELETE",
        body: JSON.stringify({idGrupoManual: idGrupoManual}),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/manual/deleteGrupo', options);
        const respJSON = await res.json();

        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Grupo eliminado correctamente"
            });

            obtenGrupos();
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

function eliminarGrupo(idGrupoManual){

    Swal.fire({
        title: 'Eliminar Grupo',
        text: '¿Estas seguro que deseas eliminar el grupo?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminarGrupoAsync(idGrupoManual);
        }
    })

}



const getGrupoId = async (idGrupoManual) => {
    const options ={
        method: "GET",
        headers: headers
    };
    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/manual/getGrupoId/'+idGrupoManual, options);
        const respJSON = await res.json();

        if (res.ok) {
            offLoading(null);
            $("#idGrupoManual").val(respJSON.idGrupoManual);
            $("#nombreGrupo").val(respJSON.nombreGrupo);
            $("#numeroOrden").val(respJSON.numeroOrden);
        }
    }catch (e) {
        offLoading(null);

        Toast.fire({
            icon: "error",
            title: "Error generado"
        });
    }
}

function modalAddManual(idGrupoManual,  nombreGrupo){
    idGrupoManualTemp=idGrupoManual;
    nombreGrupoTemp=nombreGrupo;
    $("#idGp").text(nombreGrupo);
    obtenerManuales(idGrupoManual);
    $('#dlgModalManuales').modal('show');
}

function obtenerManuales(idGrupoManual){
    var groupColumn = 0;
    $('#tableManual').DataTable({
        responsive: true,
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering:false,
        autoWidth: false,
        ajax: {
            url: contexto+'/api/manual/getManuales/'+idGrupoManual,
            type: 'GET',
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
        },
        bProcessing: true,
        bServerSide:false,
        columnDefs: [
            {
                targets: 0,
                "width": "150px",
                "className": "text-center"
            },
            {
                targets: 2,
                "width": "90px",
                "className": "text-center"
            },
            {
                targets: 3,
                "width": "80px",
                "className": "text-center"
            },
            {
                targets: 4,
                "width": "100px",
                "className": "text-center"
            },
            {
                targets: 5,
                "width": "120px",
                "className": "text-center"
            }],
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        lengthChange: false,
        select: false
    });
}

function guardarManual(){

    let titleSave='Guardar Manual';
    let msgSave='¿Estas seguro que deseas guardar el manual?';

    if ($('#idManual').val() != ""){
        titleSave='Actualizar Manual';
        msgSave='¿Estas seguro que deseas actualizar el manual?';
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

            if ($('#rutaManual').prop("files").length>0){
                var archivo = readFile();

                archivo.then(function (datos) {
                    guardarManualAsync(datos);
                });


            }else{
                guardarManualAsync(null);
            }
        }
    });
}



const guardarManualAsync = async (datos) =>{


    let nombre="";
    let contenido="";

    if (datos!=null){
        nombre=datos.nombre;
        contenido=datos.contenido
    }

    const options={
        method: "POST",
        body: JSON.stringify({
            idManual:$("#idManual").val(),
            nombreManual:$("#nombreManual").val(),
            descripcionManual:$("#descripcionManual").val(),
            tipoManual:$("#tipoManualUsuario").is(":checked")?"U":"T",
            numeroOrden:$("#numeroOrdenManual").val(),
            grupoManual:{idGrupoManual:idGrupoManualTemp},
            contenido:contenido,
            nombreArchivo:nombre

        }),
        headers: headers
    };

    try {
        onLoading("dlgModalFormManual");
        const res = await fetch(contexto+'/api/manual/guardarManual', options);
        const respJSON = await res.json();
        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Los datos del manual se guardaron correctamente"
            });

            offLoading("dlgModalFormManual");
            $('#dlgModalFormManual').modal('hide');
            obtenGrupos();
            obtenerManuales(idGrupoManualTemp);

        }else{
            offLoading("dlgModalFormGrupo");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }

    }catch (e) {
        offLoading("dlgModalFormManual");
        Toast.fire({
            icon: "error",
            title: "Error al guardar"
        });
    }
};


function readFile() {
    return new Promise(function (resolve) {
        var envio = {};
        var dato_archivo = $('#rutaManual').prop("files")[0];
        console.log(dato_archivo);
        if(dato_archivo != undefined || dato_archivo.length > 0){
            var reader = new FileReader();
            reader.readAsBinaryString(dato_archivo);
            reader.onload = function () {
                envio = {
                    nombre: dato_archivo.name,
                    contenido: btoa(reader.result)
                }
                resolve(envio)
            };
        }
    });
}


function editarManual(idManual) {
    limpiarFormManual();
    $("#titleFormManual").text("Editar Manual de "+nombreGrupoTemp);
    getManualId(idManual);
    $('#dlgModalFormManual').modal('show')
}

const eliminarManualAsync = async (idManual) => {
    const options = {
        method: "DELETE",
        body: JSON.stringify({idManual: idManual}),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/manual/deleteManual', options);
        const respJSON = await res.json();

        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Manual eliminado correctamente"
            });

            obtenGrupos();
            obtenerManuales(idGrupoManualTemp);
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

const getManualId = async (idManual) => {
    const options ={
        method: "GET",
        headers: headers
    };
    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/manual/getManualId/'+idManual, options);
        const respJSON = await res.json();

        if (res.ok) {
            offLoading(null);

            $("#idManual").val(respJSON.idManual);
            $("#nombreManual").val(respJSON.nombreManual);
            $("#descripcionManual").val(respJSON.descripcionManual);
            $("#numeroOrdenManual").val(respJSON.numeroOrden);

            if (respJSON.tipoManual=="U"){
                $("#tipoManualUsuario").prop("checked", true);
            }else
                $("#tipoManualTecnico").prop("checked", true);

        }
    }catch (e) {
        offLoading(null);

        Toast.fire({
            icon: "error",
            title: "Error generado"
        });
    }
}

function eliminarManual(idManual){

    Swal.fire({
        title: 'Eliminar Manual',
        text: '¿Estas seguro que deseas eliminar el manual?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminarManualAsync(idManual);
        }
    })

}