const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};
$(document).ready(function(){

     obtienPreRegistros();

    llenarComboPerfil();

    $("#idNuevoPreRegistro").click(function () {

        $('#titleFormPreRegistro').text("Nuevo Pre Registro");
        $("#idUsuarioTemporal").val("")
        $("#correoInstitucional").val("")
        $('#dlgModalFormPreRegistro').modal('show')
    });

    $("#formPreRegistro").validate( {
        ignore: "",
        rules: {
            correoInstitucional: {required:true, email: true},
            rfc: {minlength: 10, maxlength: 13},
            curp: {minlength: 18, maxlength: 18},
            dependencia:"required",
            oficio:"required",
            nombre:"required",
            apellidoPaterno:{
                required: function () {
                    if ($("#apellidoPaterno").val().trim()=="" && $("#apellidoMaterno").val().trim()=="")
                        return true;
                    else{
                        $("#apellidoPaterno").removeClass( "is-invalid" )
                        $("#apellidoPaterno").addClass( "is-valid" );
                        $("#apellidoMaterno").removeClass( "is-invalid" )
                        $("#apellidoMaterno").addClass( "is-valid" );
                        return false;
                    }
                }
            },
            apellidoMaterno:{
                required: function () {
                    if ($("#apellidoPaterno").val().trim()=="" && $("#apellidoMaterno").val().trim()=="")
                        return true;
                    else{
                        return false;
                        $("#apellidoPaterno").removeClass( "is-invalid" )
                        $("#apellidoPaterno").addClass( "is-valid" );
                        $("#apellidoMaterno").removeClass( "is-invalid" )
                        $("#apellidoMaterno").addClass( "is-valid" );
                    }
                }
            },
            numCelular: {minlength: 10, maxlength: 10},
            correoPersonal: {email: true},
            telefonoInstitucional: { minlength: 10, maxlength: 10}
        },
        messages: {
            correoInstitucional:"Correo invalido",
            rfc:"RFC Valido requerido",
            curp:"CURP Valido requerido",
            dependencia:"Ente Público Requerido",
            oficio:"Oficio de Respuesta Requerido",
            nombre:"Nombre Requerido",
            apellidoPaterno:"Apellido Paterno o Materno Requerido",
            apellidoMaterno:"Apellido Paterno o Materno Requerido",
            numCelular:"Número de Celular Invalido",
            correoPersonal:"Correo Invalido",
            telefonoInstitucional:"Número de Teléfono Invalido"
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
    } );

    $("#formPreRegistro").submit(function(e) {
        e.preventDefault();
        if ($('#formPreRegistro').valid()) {

            guardarPreRegistro();
        }
    });

    $('.select2me').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalRolPreRegistro")
    });

    $('.select2me')
        .parent('div')
        .children('span')
        .children('span')
        .children('span')
        .css('height', ' calc(3.5rem + 2px)');
    $('.select2me')
        .parent('div')
        .children('span')
        .children('span')
        .children('span')
        .children('span')
        .css('margin-top', '18px');
    $('.select2me')
        .parent('div')
        .find('label')
        .css('z-index', '1');

    $("#formPreRegistroRol").validate( {
        ignore: "",
        rules: {
            cbRol: "required",
        },
        messages: {
            cbRol:"Seleccione el Rol"
        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            var n = element.attr("id");
            if (n=="cbRol" || n=="cbRol")
                element.closest('.input-group').append(error);
            else
                element.closest('.form-floating').append(error);
            error.addClass( "invalid-feedback" );
            //error.insertAfter(element);
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
    } );

    $("#formPreRegistroRol").submit(function(e) {
        e.preventDefault();
        if ($('#formPreRegistroRol').valid()) {
            agregarRolPreRegistro();
        }
    });

});

function obtienPreRegistros(){

   const table = new DataTable('#tablePreRegistro', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        autoWidth: false,
        ajax: {
            url: contexto+'/api/preRegistroAdmin/tabla',
            type: 'POST',
             beforeSend: function(xhr) {
               xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            }
        },
        bProcessing: true,
        bServerSide:false,
       language:{
           "url": contexto+'/include/datatables/spanish.json',
       },
        columnDefs: [
            {
                "width": "150px",
                "targets": 0,
                "className": "text-center",
            },
           /* {
                "width": "200px",
                "targets": 1,
                "className": "text-center",
            },
            {
                "width": "220px",
                "targets": 2,
                "className": "text-left",
            },*/
            {
                "width": "90px",
                "targets": 3,
                "className": "text-left",
            },
            {
                "width": "60px",
                "targets": 4,
                "className": "text-left",
            },
            {
                "width": "80px",
                "targets": 5,
                "className": "text-center",
            },
            {
                "width": "160px",
                "targets": 6,
                "className": "text-center",
            }],
        lengthChange: false,
        select: false
    });
}



function guardarPreRegistro(){

    let tipo="PUT";
    let msg="¿Estás seguro que deseas actualizar el pre registro?";
    let msgRespuesta="El pre registro se actualizó correctamente";
    if ($('#idUsuarioTemporal').val() == "") {
        msg = "¿Estás seguro que deseas guardar el pre registro?";
        msgRespuesta="El pre registro se guardo correctamente";
        tipo="POST";
    }


    Swal.fire({
        title: 'Pre Registro',
        text: msg,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function (result) {
        if (result.isConfirmed) {

            $.ajax({
                url: contexto + '/api/preRegistroAdmin',
                type: tipo,
                dataType: 'json',
                data: JSON.stringify({
                    idUsuarioTemporal: $("#idUsuarioTemporal").val(),
                    correoInstitucional: $("#correoInstitucional").val(),
                    curp: $("#curp").val(),
                    rfc: $("#rfc").val(),
                    nombre: $("#nombre").val(),
                    segundoNombre: $("#segundoNombre").val(),
                    apellidoPaterno: $("#apellidoPaterno").val(),
                    apellidoMaterno: $("#apellidoMaterno").val(),
                    celular: $("#numCelular").val(),
                    correoPersonal: $("#correoPersonal").val(),
                    telefonoInstitucional: $("#telefonoInstitucional").val(),
                    extensionInstitucional: $("#extensionInstitucional").val(),
                    oficio: $("#oficio").val(),
                    dependencia: $("#dependencia").val()
                }),
                contentType: 'application/json',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                },
                success: function (response) {
                    obtienPreRegistros();

                    Toast.fire({
                        icon: 'success',
                        title: msgRespuesta
                    });

                    $("#dlgModalFormPreRegistro").modal("hide");

                },
                error: function (response) {
                    var error =  JSON.parse(response.responseText);
                    Toast.fire({
                        icon: 'error',
                        title: error.message
                    });
                }
            });
        }

    });
}


function editarPreRegistro(idUsuarioTemporal){

    $.ajax({
        url: contexto + '/api/preRegistroAdmin/getUsuarioTemporal/' + idUsuarioTemporal,
        type: 'GET',
        dataType: 'JSON',
        contentType: 'application/json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function (response) {

            $('#titleFormPreRegistro').text("Actualizar Pre Registro");
            $("#idUsuarioTemporal").val(idUsuarioTemporal);
            $("#correoInstitucional").val(response.correoInstitucional);
            $("#rfc").val(response.rfc);
            $("#curp").val(response.curp);
            $("#nombre").val(response.nombre);
            $("#segundoNombre").val(response.segundoNombre);
            $("#apellidoPaterno").val(response.apellidoPaterno);
            $("#apellidoMaterno").val(response.apellidoMaterno);
            $("#dependencia").val(response.dependencia);
            $("#oficio").val(response.oficio);
            $("#correoPersonal").val(response.correoPersonal);
            $("#numCelular").val(response.celular);
            $("#telefonoInstitucional").val(response.telefonoInstitucional);
            $("#extensionInstitucional").val(response.extensionInstitucional);
            $("#dlgModalFormPreRegistro").modal("show");

        },
        error: function (response) {
            var error =  JSON.parse(response.responseText);
            Toast.fire({
                icon: 'error',
                title: error.message
            });
        }
    });

}

function eliminarPreRegistro(idUsuarioTemporal){

    Swal.fire({
        title: 'Eliminar Pre Registro',
        text: '¿Estás seguro que deseas eliminar el Pre Registro?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then((result) => {
        if (result.isConfirmed) {

            $.ajax({
                url: contexto + '/api/preRegistroAdmin',
                type: 'DELETE',
                dataType: 'json',
                data: JSON.stringify({
                    idUsuarioTemporal: idUsuarioTemporal
                }),
                contentType: 'application/json',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                },
                success: function (response) {
                    obtienPreRegistros();
                    Toast.fire({
                        icon: 'success',
                        title: "El pre-registro se elimino correctamente"
                    });
                },
                error: function (response) {
                    var error =  JSON.parse(response.responseText);
                    Toast.fire({
                        icon: 'error',
                        title: error.message
                    });

                }
            });
        }

    });
}

function modalRolTemporal (idUsuarioTemporal, correo){
    $('#correoPreRegistro').text(correo);
    $("#idUsuarioTemporalPr").val(idUsuarioTemporal);
    $("#cbRol").val("");
    $("#cbRol").trigger('change');

    getTablaRolesTemporales(idUsuarioTemporal);

    $('#dlgModalRolPreRegistro').modal('show');
}

function getTablaRolesTemporales(idUsuarioTemporal){

    const tablePc = new DataTable('#tablaRolPreRegistro', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering: false,
        autoWidth: false,
        bSort: false,
        ajax: {
            url: contexto+'/api/preRegistroAdmin/tablaRolTemporal',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    idUsuarioTemporal: idUsuarioTemporal

                }));
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [    {
            "targets": 1,
            "width": "50px",
            "className": "text-left",
        }],
        lengthChange: false,
        select: false
    });
}

const llenarComboPerfil = async () =>{
    const options={
        method: "GET",
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/rol/listaPerfil', options);
        const respJSON = await res.json();

        llenarCombo('#cbRol', 'idPerfil','nombrePerfil', respJSON);

        offLoading(null);
    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
};

function agregarRolPreRegistro(){

    let tipo="POST";
    let msg="¿Estás seguro que deseas agregar el rol?";
    let msgRespuesta="El Rol se agrego correctamente";



    Swal.fire({
        title: 'Rol',
        text: msg,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function (result) {
        if (result.isConfirmed) {

            $.ajax({
                url: contexto + '/api/preRegistroAdmin/addRolTemporal',
                type: tipo,
                dataType: 'json',
                data: JSON.stringify({
                    segUsuarioTemporal:{idUsuarioTemporal: $("#idUsuarioTemporalPr").val()},
                    segPerfil:{idPerfil: $("#cbRol").val()}
                }),
                contentType: 'application/json',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                },
                success: function (response) {
                    getTablaRolesTemporales($("#idUsuarioTemporalPr").val());
                    obtienPreRegistros();
                    $("#cbRol").val("");
                    $("#cbRol").trigger('change');

                    Toast.fire({
                        icon: 'success',
                        title: msgRespuesta
                    });


                },
                error: function (response) {
                    var error =  JSON.parse(response.responseText);
                    Toast.fire({
                        icon: 'error',
                        title: error.message
                    });
                }
            });
        }

    });
}
function eliminarRolPreRegistro(idUsuarioTemporalRol){

    Swal.fire({
        title: 'Eliminar Rol del Pre Registro',
        text: '¿Estás seguro que deseas eliminar el Rol del Pre Registro?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then((result) => {
        if (result.isConfirmed) {

            $.ajax({
                url: contexto + '/api/preRegistroAdmin/deleteRolPreRegistro',
                type: 'DELETE',
                dataType: 'json',
                data: JSON.stringify({
                    idUsuarioTemporalRol: idUsuarioTemporalRol
                }),
                contentType: 'application/json',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                },
                success: function (response) {
                    getTablaRolesTemporales($("#idUsuarioTemporalPr").val());
                    obtienPreRegistros();
                    Toast.fire({
                        icon: 'success',
                        title: "El rol del pre-registro se elimino correctamente"
                    });
                },
                error: function (response) {
                    var error =  JSON.parse(response.responseText);
                    Toast.fire({
                        icon: 'error',
                        title: error.message
                    });

                }
            });
        }

    });
}

function regresarPendiente(idUsuarioTemporal){

    let msg="¿Estás seguro que deseas regresar a PENDIENTE el pre registro?";
    let msgRespuesta="El pre registro se regreso a PENDIENTE correctamente";


    Swal.fire({
        title: 'Pre Registro',
        text: msg,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function (result) {
        if (result.isConfirmed) {

            $.ajax({
                url: contexto + '/api/preRegistroAdmin/regresarPreRegistro',
                type: "POST",
                dataType: 'json',
                data: JSON.stringify({
                    idUsuarioTemporal: idUsuarioTemporal
                }),
                contentType: 'application/json',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                },
                success: function (response) {
                    obtienPreRegistros();

                    Toast.fire({
                        icon: 'success',
                        title: msgRespuesta
                    });

                },
                error: function (response) {
                    var error =  JSON.parse(response.responseText);
                    Toast.fire({
                        icon: 'error',
                        title: error.message
                    });
                }
            });
        }

    });
}

function generarToken(idUsuarioTemporal){

    let msg="¿Estás seguro que deseas generar el token para la url?";
    let msgRespuesta="El token se genero correctamente";


    Swal.fire({
        title: 'Pre Registro',
        text: msg,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function (result) {
        if (result.isConfirmed) {

            $.ajax({
                url: contexto + '/api/preRegistroAdmin/generarToken',
                type: "POST",
                dataType: 'json',
                data: JSON.stringify({
                    idUsuarioTemporal: idUsuarioTemporal
                }),
                contentType: 'application/json',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                },
                success: function (response) {
                    obtienPreRegistros();

                    Toast.fire({
                        icon: 'success',
                        title: msgRespuesta
                    });

                },
                error: function (response) {
                    var error =  JSON.parse(response.responseText);
                    Toast.fire({
                        icon: 'error',
                        title: error.message
                    });
                }
            });
        }

    });
}

const copiarToken = async (token) => {
    try {
        await navigator.clipboard.writeText(getAbsolutePath()+"pre-registro?token="+token);

        Toast.fire({
            icon: 'success',
            title: "Url Copiado"
        });
        console.log('Contenido copiado al portapapeles');
    } catch (err) {
        console.error('Error al copiar: ', err);
    }
}

function getAbsolutePath() {
    var loc = window.location;
    var pathName = loc.pathname.substring(0, loc.pathname.lastIndexOf('/') + 1);
    return loc.href.substring(0, loc.href.length - ((loc.pathname + loc.search + loc.hash).length - pathName.length));
}

const enviarMail = async (url, correo, nombre, oficio, dependencia) =>{
    const options={
        method: "POST",
        dataType: 'json',
        body: JSON.stringify({
            "clientId" : $("#usuarioToken").val(),
            "clientSecret" : $("#secretToken").val()
        }),
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    let urlSend=getAbsolutePath()+"pre-registro?token="+url;

    try {
        onLoading(null);
        const res = await fetch($("#generaToken").val(), options);
        const respJSON = await res.json();
        if (res.ok) {
            offLoading(null);
            enviarUrlEmail(respJSON.access_token, urlSend, correo, nombre, oficio, dependencia);
        }else{
            offLoading(null);
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }

    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: "error",
            title: "Error al generar Token"
        });
    }
};


const enviarUrlEmail = async (token, url, correo, nombre, oficio, dependencia) =>{

   let textoEnviar= nombre+"<br/>"+
        correo+"<br/>"+
       "PRESENTE<br/><br/>"+
       "En Seguimiento a la circular No. SAIG/CGMAIG/003/2024 de fecha 25 de abril de 2024 y en respuesta al oficio "+oficio+" de la " +
       "<strong>"+dependencia+"</strong> donde fue asignado como enlace para el registro del inventario de " +
       "Software y equipo informático en el <strong>“Sistema de Inventario para Software y Equipos Informáticos de los Entes del Consejo de Gobierno Estatal”</strong>, " +
       "con fundamento en el artículo 22, fracción IX de la Ley de Gobierno Digital y firma Electrónica para el Estado de Tabasco y sus Municipios.<br/><br/>"+
       "Se hace llegar el siguiente link para generar la clave de acceso en el SIGTIC.";

    const options={
        method: "POST",
        dataType: 'json',
        body: JSON.stringify(
            { "correo": {"cuentaCorreo":correo.trim()},
                "tipo": "NOTIFICACION", "asunto": "Actualizar información de Enlace del Sistema de Inventario para Software y Equipos Informáticos (SIGTIC)",
                "contenido": textoEnviar+"<br/><br/>"+url,
                "idSistema": "NOTIFICACION"
            }),
        headers: {Authorization: 'Bearer '+ token,
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    try {
        onLoading(null);
        const res = await fetch($("#enviaCorreo").val(), options);

        const respJSON = await res.json();
        if (res.ok) {
            offLoading(null);

            Toast.fire({
                icon: "success",
                title: "URL enviada por correo institucional"
            });

        }else{
            offLoading(null);
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }

    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: "error",
            title: "Error al enviar la URL para actualización"
        });
    }
};
