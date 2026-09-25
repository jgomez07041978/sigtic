var contexto=$("#contextSigtic").val();


const myModal = new bootstrap.Modal('#idModalLoading', {
    focus: false
});

function onLoading(modalPadre) {
    /*Se agrega opacidad cuando el load se llama desde un modal*/
    if (modalPadre!=null && modalPadre!="")
        $("#"+modalPadre).addClass("opacity-25");

    myModal.show();
}

function offLoading(modalPadre) {
    /*Se agrega un tiempo por que el modal tiene un fade de 0.15 segundos.*/
    setTimeout(() => {
        myModal.hide();

        if (modalPadre!=null && modalPadre!="")
            $("#"+modalPadre).removeClass("opacity-25");
    }, 500);
}

function offLoadingWithModalPadreHide(modalPadre) {
    /*Se agrega un tiempo por que el modal tiene un fade de 0.15 segundos.*/
    setTimeout(() => {
        myModal.hide();
        if (modalPadre!=null && modalPadre!="") {
            $("#" + modalPadre).removeClass("opacity-25");
        }
    }, 500);
}



const Toast = Swal.mixin({
    toast: true,
    position: 'top',
    showConfirmButton: false,
    timer: 4000,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
});

function modalPerfil() {
    $('#nav-data-general-tab').tab('show');
    cancelarPerfil();
    $("#idModalPerfil").modal("show")

}

function modalPassword() {
    $("#contraseniaActual").val("");
    $("#contraseniaNueva").val("");
    $("#repetirContraseniaNueva").val("");
    $("#contraseniaActual").removeClass( "is-valid" );
    $("#contraseniaActual").removeClass( "is-invalid" );
    $("#contraseniaNueva").removeClass( "is-valid" );
    $("#contraseniaNueva").removeClass( "is-invalid" );
    $("#repetirContraseniaNueva").removeClass( "is-valid" );
    $("#repetirContraseniaNueva").removeClass( "is-invalid" );
    $("#idModalCambiarPassword").modal("show");

}


const actualizarPasswordAsync = async () =>{
    const options={
        method: "POST",
        body: JSON.stringify($("#idFormPassword").serializeToJSON()),
        headers: {
            "X-CSRF-Token":  $('input[name="_csrf"]').val(),
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    try {
        onLoading("idModalCambiarPassword");
        const res = await fetch(contexto+'/api/seguridad/actualizarPassword', options);
        const respJSON = await res.json();

        if (res.ok) {

            $("#contraseniaActual").val("");
            $("#contraseniaNueva").val("");
            $("#repetirContraseniaNueva").val("");
            $("#idModalCambiarPassword").modal("hide");

            Toast.fire({
                icon: "success",
                title: "Contraseña actualizada correctamente"
            });
            offLoading("idModalCambiarPassword");
        }else{
            offLoading("idModalCambiarPassword");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }
    }catch (e) {
        var error =  JSON.parse(e.responseText);
        offLoading("idModalCambiarPassword");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
};

function actualizarPassword(){

    $.validator.addMethod('regexp', function (value) {
        /*Para validar espacios*/
        let noValido = /\s/;

        if(noValido.test(value))
            return false;
        else
            return  /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$^&*()_-]).{12,18}$/.test(value);
    }, 'Contraseña no valida');



    $( "#idFormPassword" ).validate( {
        rules: {
            contraseniaActual:"required",
            contraseniaNueva: {regexp : true},
            repetirContraseniaNueva: {regexp : true}
        },
        messages: {
            contraseniaActual: "Campo requerido",
            contraseniaNueva: {},
            repetirContraseniaNueva: {},
        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            error.insertAfter(element);
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
    } );

    if($('#idFormPassword').valid()) {

        if ($("#contraseniaNueva").val()==$("#repetirContraseniaNueva").val()) {

            Swal.fire({
                title: "Cambiar Contraseña",
                text: "¿Seguro que desea actualizar la contraseña?",
                icon: 'question',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si',
                cancelButtonText: "No"
            }).then(function (result) {
                if (result.isConfirmed) {
                    actualizarPasswordAsync();
                }
            });
        }else
            Toast.fire({
                icon: "error",
                title: "El password nuevo y repetir password no coninciden"
            });
    }
}




async function fillSelectGet(relativeUrl,idElemento, value, descripcion, idPadre, callback){

    const options={
        method: "GET",
        headers: {
            "X-CSRF-Token":  $('input[name="_csrf"]').val(),
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    url = contexto+relativeUrl

    if (idPadre!=null)
        url=url+"/"+idPadre


    onLoading(null);
    const res = await fetch(url, options);
    const respJSON = await res.json();

    if (res.ok){
        llenarCombo(idElemento, value, descripcion, respJSON);

        if (typeof callback === 'function') {
            callback();
        }

        offLoading(null);
    }else{
        Toast.fire({
            icon: "error",
            title: respJSON.message
        });
        offLoading(null);
    }

}

async function fillSelectGet_2(relativeUrl,idElemento, value, descripcion, idPadre, idPadre2, callback){

    const options={
        method: "GET",
        headers: {
            "X-CSRF-Token":  $('input[name="_csrf"]').val(),
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    url = contexto+relativeUrl

    if (idPadre!=null)
        url=url+"/"+idPadre

    if (idPadre2!=null)
        url=url+"/"+idPadre2

    onLoading(null);
    const res = await fetch(url, options);
    const respJSON = await res.json();

    if (res.ok){
        llenarCombo(idElemento, value, descripcion, respJSON);

        if (typeof callback === 'function') {
            callback();
        }

        offLoading(null);
    }else{
        Toast.fire({
            icon: "error",
            title: respJSON.message
        });
        offLoading(null);
    }
}

async function fillSelectPost(relativeUrl,idElemento, value, descripcion, idPadre, tabla, callback){
    var url = contexto+relativeUrl

    const options={
        method: "POST",
        body: JSON.stringify({
            idDependiente:idPadre,
            consulta: tabla
        }),
        headers: {
            "X-CSRF-Token":  $('input[name="_csrf"]').val(),
            "Content-type": "application/json; charset=UTF-8"
        }
    };

        onLoading(null);
        const res = await fetch(url, options);
        const respJSON = await res.json();
        if (res.ok){
            llenarCombo(idElemento, value, descripcion, respJSON);

            if (typeof callback === 'function') {
                callback();
            }
            offLoading(null);
        }else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading(null);
        }
}

async function fillSelectPost(relativeUrl,idElemento, value, descripcion, callback){
    var url = contexto+relativeUrl

    const options={
        method: "POST",
        headers: {
            "X-CSRF-Token":  $('input[name="_csrf"]').val(),
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    onLoading(null);
    const res = await fetch(url, options);
    const respJSON = await res.json();
    if (res.ok){
        llenarCombo(idElemento, value, descripcion, respJSON);

        if (typeof callback === 'function') {
            callback();
        }
        offLoading(null);
    }else {
        Toast.fire({
            icon: "error",
            title: respJSON.message
        });
        offLoading(null);
    }
}


function inicializarCombo(idElemento){
    $(idElemento).empty();
    var dropdown = $(idElemento);
    dropdown.append($('<option></option>').attr('value', '').text('Seleccione'));
}

function llenarCombo(idElemento, value, descripcion, respJSON){
    $(idElemento).empty();
    var dropdown = $(idElemento);
    dropdown.append($('<option></option>').attr('value', '').text('Seleccione'));
    for (var d = 0; d < respJSON.length; d++) {
        dropdown.append($('<option></option>').attr('value', respJSON[d][value]).text(respJSON[d][descripcion]));
    }
}

function editarPerfil(){
    $("#idEditarPerfil").hide();
    $("#idAccionPerfil").show();
    $("#idCancelarPerfil").show();
    $("#idGuardarPerfil").show();
    $("#idNombreTxt").hide();
    $("#idNombreFrm").show();
    $("#idCurpTxt").hide();
    $("#idCurpFrm").show();
    $("#idRfcTxt").hide();
    $("#idRfcFrm").show();
    $("#idCorreoTxt").hide();
    $("#idCorreoFrm").show();

    $("#idTelefonoTxt").hide();
    $("#idTelefonoFrm").show();
    $("#idExtensionFrm").show();

    $("#idTelefonoAlternoTxt").hide();
    $("#idTelefonoAlternoFrm").show();
    $("#idCelularTxt").hide();
    $("#idCelularFrm").show();
}

function cancelarPerfil(){
    $("#idEditarPerfil").show();
    $("#idAccionPerfil").hide();
    $("#idCancelarPerfil").hide();
    $("#idGuardarPerfil").hide();
    $("#idNombreTxt").show();
    $("#idNombreFrm").hide();
    $("#idCurpTxt").show();
    $("#idCurpFrm").hide();
    $("#idRfcTxt").show();
    $("#idRfcFrm").hide();
    $("#idCorreoTxt").show();
    $("#idCorreoFrm").hide();

    $("#idTelefonoTxt").show();
    $("#idTelefonoFrm").hide();
    $("#idExtensionFrm").hide();

    $("#idTelefonoAlternoTxt").show();
    $("#idTelefonoAlternoFrm").hide();
    $("#idCelularTxt").show();
    $("#idCelularFrm").hide();
}

function actualizarPerfil(){

    $.validator.addMethod('regexpEmail', function (value) {
        /*Para validar espacios*/
        let noValido = /\s/;

        if(noValido.test(value))
            return false;
        else
            return   /^\w+([.-_+]?\w+)*@\w+([.-]?\w+)*(\.\w{2,10})+$/ .test(value);
    }, 'Correo no valido');



    $( "#idFormPerfil" ).validate( {
        ignore: "",
        rules: {
            nombrePerfil:"required",
            apellidoPaternoPerfil:"required",
            apellidoMaternoPerfil:"required",
            curpPerfil:"required",
            rfcPerfil:"required",
            correoPerfil: {regexpEmail : true},
            telefonoPerfil:"required",
            correoAlternoPerfil: {regexpEmail : true},
            celularPerfil:"required"
        },
        messages: {
            nombrePerfil: "Campo requerido",
            apellidoPaternoPerfil: "Campo requerido",
            apellidoMaternoPerfil: "Campo requerido",
            curpPerfil: "Campo requerido",
            rfcPerfil: "Campo requerido",
            correoPerfil: {},
            telefonoPerfil: "Campo requerido",
            correoAlternoPerfil: {},
            celularPerfil: "Campo requerido"
        },
        errorElement: "span",
        errorPlacement: function (error, element) {
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
        },
        invalidHandler: function(e, validator){
            if(validator.errorList.length>0) {
                $("#"+jQuery(validator.errorList[0].element, '#nav-tab').closest(".tab-pane").attr('id')+"-tab").tab('show');
            }
        }
    } );

    if($('#idFormPerfil').valid()) {

        Swal.fire({
            title: 'Rol',
            text: "¿Estás seguro que deseas actualizar el perfil?",
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Si',
            cancelButtonText: "No"
        }).then(function (result) {
            if (result.isConfirmed) {
                $.ajax({
                    url: contexto + '/api/catalogoCatUsuarioEntePublico/actualizarPerfil',
                    type: "PUT",
                    dataType: 'json',
                    data: JSON.stringify(
                            {
                                catUsuario:{
                                    nombre: $("#nombrePerfil").val(),
                                    segundoNombre: $("#segundoNombrePerfil").val(),
                                    apellidoPaterno: $("#apellidoPaternoPerfil").val(),
                                    apellidoMaterno: $("#apellidoMaternoPerfil").val(),
                                    rfc: $("#rfcPerfil").val(),
                                    curp: $("#curpPerfil").val(),
                                    correoPersonal: $("#correoAlternoPerfil").val(),
                                    numCelular: $("#celularPerfil").val()
                                },
                                numTelefonoInstitucional: $("#telefonoPerfil").val(),
                                numeroExtensionInstitucional: $("#extensionPerfil").val(),
                                correoInstitucional: $("#correoPerfil").val()
                            }),
                    contentType: 'application/json',
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                    },
                    success: function (response) {

                        let nombreCp = $("#nombrePerfil").val();

                        if ($("#apellidoPaternoPerfil").val() != "")
                        nombreCp = nombreCp + " " + $("#segundoNombrePerfil").val();

                        if ($("#apellidoPaternoPerfil").val() != "")
                            nombreCp = nombreCp + " " + $("#apellidoPaternoPerfil").val();

                        if ($("#apellidoMaternoPerfil").val() != "")
                            nombreCp = nombreCp + " " + $("#apellidoMaternoPerfil").val();

                        $("#idNombreCompletoPf").text(nombreCp);
                        $("#curpPf").text($("#curpPerfil").val());
                        $("#rfcPf").text($("#rfcPerfil").val());
                        $("#correoPf").text($("#correoPerfil").val());
                        $("#numeroTelefonoPf").text($("#telefonoPerfil").val());
                        $("#extensionPf").text($("#extensionPerfil").val());
                        $("#correoAlternoPf").text($("#correoAlternoPerfil").val());
                        $("#celularPf").text($("#celularPerfil").val());

                        cancelarPerfil();

                        Toast.fire({
                            icon: 'success',
                            title: "Perfil Actualizado Correctamente"
                        });

                    },
                    error: function (response) {
                        console.log(response);
                        var error = JSON.parse(response.responseText);
                        Toast.fire({
                            icon: 'error',
                            title: error.message
                        });
                    }
                });
            }

        });
    }
}

function mayus(e) {
    e.value = e.value.toUpperCase();
}

function number_format(amount, decimals) {

    amount += ''; // por si pasan un numero en vez de un string
    amount = parseFloat(amount.replace(/[^0-9\.]/g, '')); // elimino cualquier cosa que no sea numero o punto

    decimals = decimals || 0; // por si la variable no fue fue pasada

    // si no es un numero o es igual a cero retorno el mismo cero
    if (isNaN(amount) || amount === 0)
        return parseFloat(0).toFixed(decimals);

    // si es mayor o menor que cero retorno el valor formateado como numero
    amount = '' + amount.toFixed(decimals);

    var amount_parts = amount.split('.'),
        regexp = /(\d+)(\d{3})/;

    while (regexp.test(amount_parts[0]))
        amount_parts[0] = amount_parts[0].replace(regexp, '$1' + ',' + '$2');

    return amount_parts.join('.');
}

function getBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
    });
}

function llamarEye(){
    $("#id-btn-eye10").click(function(){
        if ($("#verPassword10").val()==0){
            $("#verPassword10").val(1);
            $("#contraseniaActual").attr('type','text');
            $("#id-fa-eye10").removeClass("fa-solid fa-eye-slash");
            $("#id-fa-eye10").addClass("fa-solid fa-eye");
        }else{
            $("#verPassword10").val(0);
            $("#contraseniaActual").attr('type','password');
            $("#id-fa-eye10").removeClass("fa-solid fa-eye");
            $("#id-fa-eye10").addClass("fa-solid fa-eye-slash");
        }
    });

    $("#id-btn-eye11").click(function(){
        if ($("#verPassword11").val()==0){
            $("#verPassword11").val(1);
            $("#contraseniaNueva").attr('type','text');
            $("#id-fa-eye11").removeClass("fa-solid fa-eye-slash");
            $("#id-fa-eye11").addClass("fa-solid fa-eye");
        }else{
            $("#verPassword11").val(0);
            $("#contraseniaNueva").attr('type','password');
            $("#id-fa-eye11").removeClass("fa-solid fa-eye");
            $("#id-fa-eye11").addClass("fa-solid fa-eye-slash");
        }
    });

    $("#id-btn-eye12").click(function(){
        if ($("#verPassword12").val()==0){
            $("#verPassword12").val(1);
            $("#repetirContraseniaNueva").attr('type','text');
            $("#id-fa-eye12").removeClass("fa-solid fa-eye-slash");
            $("#id-fa-eye12").addClass("fa-solid fa-eye");
        }else{
            $("#verPassword12").val(0);
            $("#repetirContraseniaNueva").attr('type','password');
            $("#id-fa-eye12").removeClass("fa-solid fa-eye");
            $("#id-fa-eye12").addClass("fa-solid fa-eye-slash");
        }
    });

}

