const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

const Toast = Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 4000,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
});

let permitirEnviar=false;



var contexto=$("#contextSigtic").val();

$(function () {

    $("#id-btn-eye").click(function(){
        if ($("#verPassword").val()==0){
            $("#verPassword").val(1);
            $("#password").attr('type','text');
            $("#id-fa-eye").removeClass("fa-solid fa-eye-slash");
            $("#id-fa-eye").addClass("fa-solid fa-eye");
        }else{
            $("#verPassword").val(0);
            $("#password").attr('type','password');
            $("#id-fa-eye").removeClass("fa-solid fa-eye");
            $("#id-fa-eye").addClass("fa-solid fa-eye-slash");
        }
    });


    $("#formLogin").submit(function (e) {
        //if ($("#g-recaptcha-response").val()=="") {
            e.preventDefault();
            onLoading();
                grecaptcha.ready(function () {
                    grecaptcha.execute($("#recatchaKeyPublic").val(), {action: "/inicio"})
                        .then(function (respuestaToken) {
                            $("#g-recaptcha-response").val(respuestaToken);
                            login();
                        }).catch((e) => {
                        offLoading();
                    })
                });
    });


});


function resetCaptcha() {
    location.reload();
}
function login() {
    //console.log( $("#formLogin").serialize());
    $.ajax({
        url: contexto+'/login',
        type: 'POST',
        dataType: 'JSON',
        //'contentType': "application/json; charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        data: $("#formLogin").serialize(),
        success: function (response) {
            offLoading();
            window.location = contexto+response.redirect;
        },
        error: function(e)  {
            offLoading();
            var error = JSON.parse(e.responseText);
            Toast.fire({
                icon: "error",
                title: error.message
            });

            /*setTimeout(function () {
                resetCaptcha();
            }, 1300);*/


        }
    });
}

const myModal = new bootstrap.Modal('#idModalLoading', {
    focus: false
});

function onLoading() {
    myModal.show();
}

function offLoading() {
    /*Se agrega un tiempo por que el modal tiene un fade de 0.15 segundos.*/
    setTimeout(() => {
        myModal.hide();
    }, 500);
}

const validarCurp = async () =>{

    const options={
        method:"POST",
        body: JSON.stringify({
            "usuario" : $("#usernameRecuperar").val(),
        }),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/seguridad/recuperarAcceso', options);
        const respJSON = await res.json();

        if (res.ok) {

            enviarMail(respJSON.token, respJSON.correo, respJSON.catUsuarioEntePublico.catUsuario.nombreCompleto);


            restaurar();

            offLoading(null);
        } else{
            Toast.fire({
                icon: "error",
                title: "Error al enviar el correo"
            });
            offLoading(null);
        }

    }catch (e) {
        offLoading(null);
        var error =  JSON.parse(e.responseText);
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
};

const enviarSms = async (token, celular, nombre, curp) =>{
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

    let urlSend=getAbsolutePath()+"recuperar-acceso?token="+token;

    try {
        onLoading(null);
        const res = await fetch($("#generaToken").val(), options);
        const respJSON = await res.json();
        if (res.ok) {
            offLoading(null);
            enviarUrlSms(respJSON.access_token, urlSend, celular, nombre, curp);

            Toast.fire({
                icon: "success",
                title: "Sms de recuperación enviado correctamente"
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
            title: "Error al generar Token"
        });
    }
};



const enviarMail = async (token, correo, nombre) =>{
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

    let urlSend=getAbsolutePath()+"recuperar-acceso?token="+token;

    try {
        onLoading(null);
        const res = await fetch($("#generaToken").val(), options);
        const respJSON = await res.json();
        if (res.ok) {
            offLoading(null);
            enviarUrlEmail(respJSON.access_token, urlSend, correo, nombre);

            Toast.fire({
                icon: "success",
                title: "Correo de recuperación enviado correctamente"
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
            title: "Error al generar Token"
        });
    }
};

const enviarUrlEmail = async (token, url, correo, nombre) =>{

    let textoEnviar= nombre+"<br/>"+
        "Se hace llegar el siguiente link para recuperar el acceso en el SIGTIC";

    const options={
        method: "POST",
        dataType: 'json',
        body: JSON.stringify(
            { "correo": {"cuentaCorreo":correo.trim()},
                "tipo": "NOTIFICACION", "asunto": "Recuperación de la clave de acceso al (SIGTIC)",
                "contenido": textoEnviar+"<br/><br/><a href=\""+url+"\" target=\"_blank\">Recuperar Contraseña</a>",
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

const enviarUrlSms = async (token, url, celular, nombre, curp) =>{


    //"mensaje": "SIGTIC : link de recuperación de contraseña "+url,
    const options={
        method: "POST",
        dataType: 'json',
        body: JSON.stringify({
            "id_aplicacion":"sigtic",
            "usuario":nombre,
            "numero":celular,
            "mensaje": "SIGTIC : link de recuperación de contraseña "+url,
            "curp":curp,
        }),
        headers: {
            Authorization: 'Bearer '+ token,
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    try {
        onLoading(null);
        const res = await fetch($("#enviaSms").val(), options);

        const respJSON = await res.json();
        if (res.ok) {
            offLoading(null);

            Toast.fire({
                icon: "success",
                title: "Link de recuperación enviado a celular"
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
            title: "Error al enviar la sms de recuperación"
        });
    }
};



function recuperar(){
    permitirEnviar=false;
    $("#m1").hide();
    $("#m2").hide();
    $("#m3").hide();
    $("#textError").text("");
    $("#textExito").text("");
    $("#dato").val("");
    $("#correoInstitucional").prop("checked", false);
    $("#correoPersonal").prop("checked", false);
    $("#celular").prop("checked", false);

    $("#idFormAcceso").hide();
    $("#idFormRecuperar").show();
}

function restaurar(){
    permitirEnviar=false;
    $("#m1").hide();
    $("#m2").hide();
    $("#m3").hide();
    $("#textError").text("");
    $("#textExito").text("");
    $("#dato").val("");
    $("#correoInstitucional").prop("checked", false);
    $("#correoPersonal").prop("checked", false);
    $("#celular").prop("checked", false);
    $("#usernameRecuperar").val("");
    $("#idFormAcceso").show();
    $("#idFormRecuperar").hide();
}

function getAbsolutePath() {
    var loc = window.location;
    var pathName = loc.pathname.substring(0, loc.pathname.lastIndexOf('/') + 1);
    return loc.href.substring(0, loc.href.length - ((loc.pathname + loc.search + loc.hash).length - pathName.length));
}

const validarDato = async (opcion) =>{

    permitirEnviar=false;
    $("#m1").hide();
    $("#m2").hide();
    $("#m3").hide();
    $("#textError").text("");
    $("#textExito").text("");
    $("#dato").val("");

    if ($("#usernameRecuperar").val().trim()==""){
        $("#correoInstitucional").prop("checked", false);
        $("#correoPersonal").prop("checked", false);
        $("#celular").prop("checked", false);

        Toast.fire({
            icon: "warning",
            title: "Teclee la CURP y luego seleccione una opción"
        });

    }else{

        console.log($("#usernameRecuperar").val());
        console.log(opcion);
        const options={
            method:"POST",
            body: JSON.stringify({
                "curp" : $("#usernameRecuperar").val(),
                "opcion" : opcion,
            }),
            headers: headers
        };

        try {
            onLoading(null);
            const res = await fetch(contexto+'/api/seguridad/validarOpcionRecuperacion', options);
            const respJSON = await res.json();

            //console.log(res);
            //console.log(respJSON);
            if (res.ok) {

                if (respJSON.error!=null && respJSON.error!=""){
                    $("#m3").show();
                    $("#textError").text(respJSON.error);
                }else
                if (respJSON.dato!=null && respJSON.dato!=""){
                    $("#m1").show();
                    $("#m2").show();
                    $("#dato").val(respJSON.dato);
                    $("#textExito").text(respJSON.msgExito);
                    permitirEnviar=true;
                }else{
                    $("#m3").show();
                    $("#textError").text("Error de validación");
                }

                offLoading(null);
            } else{
                $("#correoInstitucional").prop("checked", false);
                $("#correoPersonal").prop("checked", false);
                $("#celular").prop("checked", false);
                Toast.fire({
                    icon: "error",
                    title: "Error al validar la opción de recuperación"
                });
                offLoading(null);
            }

        }catch (e) {
            $("#correoInstitucional").prop("checked", false);
            $("#correoPersonal").prop("checked", false);
            $("#celular").prop("checked", false);
            offLoading(null);
            var error =  JSON.parse(e.responseText);
            Toast.fire({
                icon: "error",
                title: error.message
            });
        }

    }

}

const recuperarPassword = async () =>{

    let opcion=0;

    if ($("#correoInstitucional").is(":checked"))
        opcion= 1;
    if ($("#correoPersonal").is(":checked"))
        opcion= 2;
    if ($("#celular").is(":checked"))
        opcion= 3;


    const options={
        method:"POST",
        body: JSON.stringify({
            "curp" : $("#usernameRecuperar").val(),
            "opcion" : opcion,
        }),
        headers: headers
    };

    if (permitirEnviar) {

        try {
            onLoading(null);
            const res = await fetch(contexto + '/api/seguridad/recuperarAcceso', options);
            const respJSON = await res.json();

            if (res.ok) {

                if (opcion==1 || opcion==2)
                    enviarMail(respJSON.token, respJSON.correo, respJSON.catUsuarioEntePublico.catUsuario.nombreCompleto);
                else
                if (opcion==3)
                    enviarSms(respJSON.token, respJSON.correo, respJSON.catUsuarioEntePublico.catUsuario.nombreCompleto, respJSON.catUsuarioEntePublico.catUsuario.curp);

                restaurar();

                offLoading(null);
            } else {
                Toast.fire({
                    icon: "error",
                    title: "Error al enviar la notificación"
                });
                offLoading(null);
            }

        } catch (e) {
            offLoading(null);
            var error = JSON.parse(e.responseText);
            Toast.fire({
                icon: "error",
                title: error.message
            });
        }
    }else{
        Toast.fire({
            icon: "error",
            title: "No se ha validado la opción de recuperación"
        });
    }
};