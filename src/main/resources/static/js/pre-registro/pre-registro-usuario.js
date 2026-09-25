const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

$(document).ready(function() {

    jQuery.event.special.mousewheel = {
        setup: function (_, ns, handle) {
            this.addEventListener("mousewheel", handle, {passive: true});
        }
    };


    $("#id-btn-eye1").click(function(){
        if ($("#verPassword1").val()==0){
            $("#verPassword1").val(1);
            $("#contrasenia").attr('type','text');
            $("#id-fa-eye1").removeClass("fa-solid fa-eye-slash");
            $("#id-fa-eye1").addClass("fa-solid fa-eye");
        }else{
            $("#verPassword1").val(0);
            $("#contrasenia").attr('type','password');
            $("#id-fa-eye1").removeClass("fa-solid fa-eye");
            $("#id-fa-eye1").addClass("fa-solid fa-eye-slash");
        }
    });

    $("#id-btn-eye2").click(function(){
        if ($("#verPassword2").val()==0){
            $("#verPassword2").val(1);
            $("#contraseniaRepite").attr('type','text');
            $("#id-fa-eye2").removeClass("fa-solid fa-eye-slash");
            $("#id-fa-eye2").addClass("fa-solid fa-eye");
        }else{
            $("#verPassword2").val(0);
            $("#contraseniaRepite").attr('type','password');
            $("#id-fa-eye2").removeClass("fa-solid fa-eye");
            $("#id-fa-eye2").addClass("fa-solid fa-eye-slash");
        }
    });

    inicializarDependencias();

    if ($("#sexoTemporal").val()=="F")
        $('#cveSexoFemenino').prop('checked',true);
    else
    if ($("#sexoTemporal").val()=="M")
        $('#cveSexoMasculino').prop('checked',true);

    if ($("#fechaNacimientoTemporal").val()!="") {
        var date1 = $("#fechaNacimientoTemporal").val().split("/");
        $("#fechaNacimiento").val(date1[2] + "-" + date1[1] + "-" + date1[0]);
    }

    if ($("#verValCodigo").val()=="SI"){
        $('#idSolCodigo').show();
        $('#idValCodigo').show();
    }

    $("#idEntePublico").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large"
    });

    $('#idEntePublico').on('change', function() {
        //getUnidadesAdmin();
    });

    $("#idBtnBuscar").click(function () {
        if ($("#curp").val().trim().length<18)
            Toast.fire({
                icon: 'warning',
                //title: 'Validar',
                html: "Se requiere CURP de 18 caracteres"
            });
        else{
            buscarUsuario($("#curp").val().trim())
        }

    });

    $("#idBtnSolCodigo").click(function () {
        let error=0;

        $("#metodoValidacion").val($("#valSms").is(":checked")?"SMS":"EMAIL");

        if ($("#curpVal").val()!=$("#curp").val()){
            Toast.fire({
                icon: 'error',
                title: "La CURP guardado previamente, es diferente a la del campo CURP"
            });
            error++;
        }else
        if ($("#metodoValidacion").val()=="SMS"){
            if ($("#destinoCelular").val()!=$("#numCelular").val()){
                Toast.fire({
                    icon: 'error',
                    title: "El número de celular guardado previamente, es diferente a la del campo del celular"
                });
                error++;
            }
        }else
        if ($("#metodoValidacion").val()=="EMAIL"){
            if ($("#destinoCorreo").val()!=$("#correoInstitucional").val()){
                Toast.fire({
                    icon: 'error',
                    title: "El correo institucional guardado previamente, es diferente a la del campo del correo institucional"
                });
                error++;
            }
        }

        if (error==0)
            generarToken();
    });

    $("#idBtnValidar").click(function () {

        let error=0;

        if ($("#accessToken").val()==""){
            Toast.fire({
                icon: 'error',
                title: "Se requiere solicitar codigo"
            });
            error++;
        }else
        if ($("#codigo").val().trim()==""){
            Toast.fire({
                icon: 'error',
                title: "Se requiere teclee el código"
            });
            error++;
        }

        if (error==0)
            validarCodigo($("#codigo").val().trim());
    });




    $.validator.addMethod('regexpp', function (value) {
            let noValido = /\s/;

            if(noValido.test(value))
                return false;
            else
                return  /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$^&*()_-]).{12,18}$/.test(value);
    }, 'Contraseña no valida');

    $.validator.addMethod('regexppTelefonoPersonal', function (value) {
        if (value.trim()=="")
            return true;
        else
            return  /^\d{10}$/.test(value);
    }, 'Teléfono no valido');

    $.validator.addMethod('regexppTelefonoInstitucional', function (value) {
            return  /^\d{10}$/.test(value);
    }, 'Teléfono no valido');


    $("#formUsuario").validate( {
        ignore: "",
        rules: {
            rfc: { required:true, minlength: 10, maxlength: 13},
            curp: { required:true, minlength: 18, maxlength: 18},
            idEntePublico:"required",
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
            fechaNacimiento:"required",
            numCelular: {regexppTelefonoPersonal:true},
            telefonoInstitucional: {regexppTelefonoInstitucional:true},
            correoInstitucional: {required:true, email: true},
            correoPersonal: {email: true},
            contrasenia:{regexpp:true},
            contraseniaRepite: {regexpp:true}
        },
        messages: {
            rfc:"RFC Valido requerido",
            curp:"CURP Valido requerido",
            nombre:"Nombre Requerido",
            apellidoPaterno:"Apellido Paterno o Materno Requerido",
            apellidoMaterno:"Apellido Paterno o Materno Requerido",
            fechaNacimiento:"Fecha de nacimiento requerida",
            numCelular:"Número de Celular Invalido",
            telefonoInstitucional:"Número de Teléfono Invalido",
            correoInstitucional:"Correo invalido",
            correoPersonal:"Correo Invalido",
            contrasenia: {},
            contraseniaRepite:{}
        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            var n = element.attr("id");
            if (n=="contrasenia" || n=="contraseniaRepite")
                element.closest('.input-group').append(error);
            else
                element.closest('.form-floating').append(error);
            //error.insertAfter(element);
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
    } )

    $("#formUsuario").submit(function(e) {
        e.preventDefault();
        if ($('#formUsuario').valid()) {

            if ($("#contrasenia").val()!=$("#contraseniaRepite").val()){
                Toast.fire({
                    icon: 'error',
                    title: "Las contraseñas no son iguales"
                });
            }else
                guardarUsuario();
        }
    });




});

function bloquearForm(){
    $("#idEntePublico").prop('disabled', true);
    $("#curp").prop('disabled', true);
    $("#idBtnBuscar").prop('disabled', true);
    $("#rfc").prop('disabled', true);
    $("#nombre").prop('disabled', true);
    $("#segundoNombre").prop('disabled', true);
    $("#apellidoPaterno").prop('disabled', true);
    $("#apellidoMaterno").prop('disabled', true);
    $("#apellidoMaterno").prop('disabled', true);
    $("#cveSexoFemenino").prop('disabled', true);
    $("#cveSexoMasculino").prop('disabled', true);
    $("#fechaNacimiento").prop('disabled', true);
    $("#numCelular").prop('disabled', true);
    $("#correoPersonal").prop('disabled', true);
    $("#correoInstitucional").prop('disabled', true);
    $("#telefonoInstitucional").prop('disabled', true);
    $("#extensionInstitucional").prop('disabled', true);
    $("#contraseniaSistema").prop('disabled', true);
    $("#contraseniaRepite").prop('disabled', true);
    $("#btnGuardarUsuario").prop('disabled', true);

    $('#idSolCodigo').hide();
    $('#idValCodigo').hide();
}


function llenarComboSelect(idElemento, value, descripcion, respJSON, valor){
    $(idElemento).empty();
    var dropdown = $(idElemento);
    dropdown.append($('<option></option>').attr('value', '').text('Seleccione'));
    for (var d = 0; d < respJSON.length; d++) {
        dropdown.append($('<option></option>').attr('value', respJSON[d][value]).text(respJSON[d][descripcion]));
    }

    if (valor!=null && valor!="")
        $(idElemento).val(valor);
}

const inicializarDependencias = async () =>{
    const options={
        method: "POST",
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/entePublico/listaFree', options);
        const respJSON = await res.json();

        llenarComboSelect('#idEntePublico', 'idEntePublico','nombreEntePublicoConCve', respJSON, $("#idEntePublicoTemporal").val());


        offLoading(null);
    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
};

const buscarUsuario = async (curp) => {
    const options ={
        method: "GET",
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/preRegistro/searchUser/'+curp+'/'+$("#tokenParam").val(), options);
        const respJSON = await res.json();

        if (res.ok) {
            offLoading(null);

            $("#idUsuarioTemporal").val(respJSON.idUsuarioTemporal);
            $("#curp").val(respJSON.curp);
            $("#rfc").val(respJSON.rfc);
            $("#idEntePublico").val(respJSON.idEntePublico);
            $("#idEntePublico").trigger('change');
            $("#nombre").val(respJSON.nombre);
            $("#segundoNombre").val(respJSON.segundoNombre);
            $("#apellidoPaterno").val(respJSON.apellidoPaterno);
            $("#apellidoMaterno").val(respJSON.apellidoMaterno);

            if (respJSON.sexo=="F")
                $("#cveSexoFemenino").prop("checked", true);
            else
                $("#cveSexoMasculino").prop("checked", true);

            if (respJSON.fechaNacimientoText!=null) {
                var date1 = respJSON.fechaNacimientoText.split("/");
                $("#fechaNacimiento").val(date1[2] + "-" + date1[1] + "-" + date1[0]);
            }

            $("#numCelular").val(respJSON.celular);
            $("#correoPersonal").val(respJSON.correoPersonal);
            $("#correoInstitucional").val(respJSON.correoInstitucional);
            $("#telefonoInstitucional").val(respJSON.telefonoInstitucional);
            $("#extensionInstitucional").val(respJSON.extensionInstitucional);
            $("#contraseniaSistema").val(respJSON.password);
            $("#contraseniaRepite").val(respJSON.password);
        }else{
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading(null);
        }
    }catch (e) {
        offLoading(null);

        Toast.fire({
            icon: "error",
            title: "Error generado"
        });
    }
}


const guardarUsuarioAsync = async () =>{
    const options={
        method: "POST",
        body: JSON.stringify({
            idUsuarioTemporal: $("#idUsuarioTemporal").val(),
            idEntePublico: $("#idEntePublico").val(),
            rfc: $("#rfc").val(),
            curp: $("#curp").val(),
            nombre: $("#nombre").val(),
            segundoNombre: $("#segundoNombre").val(),
            apellidoPaterno: $("#apellidoPaterno").val(),
            apellidoMaterno: $("#apellidoMaterno").val(),
            fechaNacimiento: $("#fechaNacimiento").val(),
            sexo: $("#cveSexoFemenino").is(":checked")?"F":"M",
            celular: $("#numCelular").val(),
            correoInstitucional: $("#correoInstitucional").val(),
            correoPersonal: $("#correoPersonal").val(),
            telefonoInstitucional: $("#telefonoInstitucional").val(),
            extensionInstitucional: $("#extensionInstitucional").val(),
            password: $("#contrasenia").val(),
            estatus: "PENDIENTE"
        }),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/preRegistro/guardarUsuario', options);
        const respJSON = await res.json();
        if (res.ok) {

            offLoading(null);

            Toast.fire({
                icon: "success",
                title: "Los datos del usuario se guardaron correctamente"
            });

            $("#destinoCorreo").val(respJSON.correoInstitucional);
            $("#destinoCelular").val(respJSON.celular);
            $("#nombreCompleto").val(respJSON.nombreCompleto);
            $("#curpVal").val(respJSON.curp);
            $("#uuid").val(respJSON.uuid);
            $("#metodoValidacion").val(respJSON.metodoValidacion);

            $('#idSolCodigo').show();
            $('#idValCodigo').show();


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
            title: "Error al guardar"
        });
    }
};

const actualizarUsuarioAsync = async (uuid, codigo, metodoValidacion, accessToken) =>{
    const options={
        method: "POST",
        body: JSON.stringify({
            idUsuarioTemporal: $("#idUsuarioTemporal").val(),
            uuid: uuid,
            metodoValidacion: metodoValidacion,
            codigoValidacion: codigo,
            accessToken: accessToken
        }),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/preRegistro/actualizarUsuario', options);
        const respJSON = await res.json();
        if (res.ok) {
            offLoading(null);

            if (metodoValidacion=="SMS")
                enviarCodeSms(accessToken, codigo);

            if (metodoValidacion=="EMAIL")
                enviarCodeEmail(accessToken, codigo);

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
            title: "Error al guardar"
        });
    }
};


function guardarUsuario(){

    let titleSave='¿Guardar Usuario?';
    let msgSave='¿Estas seguro que deseas guardar los datos del usuario?';


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
            guardarUsuarioAsync();
        }
    });
}

const generarToken = async () =>{
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

    try {
        onLoading(null);
        const res = await fetch($("#generaToken").val(), options);
        const respJSON = await res.json();
        if (res.ok) {

            $("#accessToken").val(respJSON.access_token);
            offLoading(null);
/*            console.log(access_token);
            console.log("--------------------------------");*/
            generarCodigo(respJSON.access_token);
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
const generarCodigo = async (token) =>{

    let destino= $("#numCelular").val();

    if ($("#metodoValidacion").val()=="EMAIL")
        destino= $("#correoInstitucional").val();


    const options={
        method: "POST",
        dataType: 'json',
        body: JSON.stringify({
            "destino" :destino.trim(),
            "metodoVerificacion": $("#metodoValidacion").val()
        }),
        headers: {Authorization: 'Bearer '+ token,
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    try {
        onLoading(null);
        const res = await fetch($("#creaCodigo").val(), options);

        const respJSON = await res.json();
        if (res.ok) {

            offLoading(null);

            if (respJSON.code=="OK"){
                //console.log(respJSON.data.uuid);
                //console.log(respJSON.data.codigo);
                $("#uuid").val(respJSON.data.uuid);
                actualizarUsuarioAsync (respJSON.data.uuid, respJSON.data.codigo, $("#metodoValidacion").val(), token);
            }else{
                offLoading(null);
                Toast.fire({
                    icon: "error",
                    title: "El código de validación no se generó"
                });

            }

            console.log(respJSON);

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

const enviarCodeSms = async (token, codigo) =>{
    const options={
        method: "POST",
        dataType: 'json',
        body: JSON.stringify({
            "id_aplicacion":"sigtic",
            "usuario":$("#nombreCompleto").val().trim(),
            "numero":$("#destinoCelular").val(),
            "mensaje": "SIGTIC : Tu código es "+codigo,
            "curp":$("#curpVal").val(),
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
                title: "Código enviado como SMS"
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
            title: "Error al enviar el código de validación"
        });
    }
};

const enviarCodeEmail = async (token, codigo) =>{
    const options={
        method: "POST",
        dataType: 'json',
        body: JSON.stringify(
            { "correo": {"cuentaCorreo":$("#destinoCorreo").val()},
                "tipo": "NOTIFICACION", "asunto": "Confirma tu correo electrónico",
                "contenido": "El Código SIGTIC para validación "+codigo, "idSistema": "NOTIFICACION"
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
                title: "Código enviado por correo institucional"
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
            title: "Error al enviar el código de validación"
        });
    }
};

const validarCodigo = async (codigo) =>{

    let destino= $("#numCelular").val();

    if ($("#metodoValidacion").val()=="EMAIL")
        destino= $("#correoInstitucional").val();

/*    console.log({"destino": destino , "metodoVerificacion":$("#metodoValidacion").val(), "uuid": $("#uuid").val(), "codigo": codigo});
    console.log($("#accessToken").val());*/


    const options={
        method: "POST",
        dataType: 'json',
        body: JSON.stringify(
            {"destino": destino , "metodoVerificacion":$("#metodoValidacion").val(), "uuid": $("#uuid").val(), "codigo": codigo}),
        headers: {Authorization: 'Bearer '+ $("#accessToken").val(),
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    try {
        onLoading(null);
        const res = await fetch($("#validaCodigo").val(), options);

        const respJSON = await res.json();
        console.log(respJSON);
        if (res.ok) {
            console.log(respJSON);


            offLoading(null);

            if (respJSON.code=="V"){

            Toast.fire({
                icon: "success",
                title: "Código validado correctamente"
            });
                generandoUsuarioAsync();

            } else
                if (respJSON.code=="FV"){
                Toast.fire({
                    icon: "success",
                    title: "El código fue validado anteriormente"
                });
            }
            else{
                Toast.fire({
                    icon: "error",
                    title: "El código no es valido"
                });
            }

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
            title: "Error al validar el código"
        });
    }
};

const generandoUsuarioAsync = async () =>{
    const options={
        method: "POST",
        body: JSON.stringify({
            idUsuarioTemporal: $("#idUsuarioTemporal").val()
        }),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/preRegistro/generandoUsuario', options);
        const respJSON = await res.json();
        if (res.ok) {
            offLoading(null);

            bloquearForm();

            Toast.fire({
                icon: "success",
                title: "Usuario generado correctamente"
            });

            setTimeout(() => {
                location.href =getAbsolutePath();
            }, 500);


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
            title: "Error al guardar"
        });
    }
};

function getAbsolutePath() {
    var loc = window.location;
    var pathName = loc.pathname.substring(0, loc.pathname.lastIndexOf('/') + 1);
    return loc.href.substring(0, loc.href.length - ((loc.pathname + loc.search + loc.hash).length - pathName.length));
}