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
            $("#contraseniaNew").attr('type','text');
            $("#id-fa-eye1").removeClass("fa-solid fa-eye-slash");
            $("#id-fa-eye1").addClass("fa-solid fa-eye");
        }else{
            $("#verPassword1").val(0);
            $("#contraseniaNew").attr('type','password');
            $("#id-fa-eye1").removeClass("fa-solid fa-eye");
            $("#id-fa-eye1").addClass("fa-solid fa-eye-slash");
        }
    });

    $("#id-btn-eye2").click(function(){
        if ($("#verPassword2").val()==0){
            $("#verPassword2").val(1);
            $("#repetirContraseniaNew").attr('type','text');
            $("#id-fa-eye2").removeClass("fa-solid fa-eye-slash");
            $("#id-fa-eye2").addClass("fa-solid fa-eye");
        }else{
            $("#verPassword2").val(0);
            $("#repetirContraseniaNew").attr('type','password');
            $("#id-fa-eye2").removeClass("fa-solid fa-eye");
            $("#id-fa-eye2").addClass("fa-solid fa-eye-slash");
        }
    });

});

function recuperarPassword(){

    $.validator.addMethod('regexp', function (value) {
        /*Para validar espacios*/
        let noValido = /\s/;

        if(noValido.test(value))
            return false;
        else
            return  /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$^&*()_-]).{12,18}$/.test(value);
    }, 'Contraseña no valida');



    $( "#idFormPasswordRecuperar" ).validate( {
        rules: {
            contraseniaNew: {regexp : true},
            repetirContraseniaNew: {regexp : true}
        },
        messages: {
            contraseniaNew: {},
            repetirContraseniaNew: {},
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

    if($('#idFormPasswordRecuperar').valid()) {

        if ($("#contraseniaNew").val()==$("#repetirContraseniaNew").val()) {

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
                    recuperarPasswordAsync();
                }
            });
        }else
            Toast.fire({
                icon: "error",
                title: "El password nuevo y repetir password no coninciden"
            });
    }

}


const recuperarPasswordAsync = async () =>{
    const options={
        method: "POST",
        body: JSON.stringify(
            {
                segUsuarioPk: {usuario:$("#segusuario").val(),sistema:"SIGTIC"},
                contraseniaNueva:$("#contraseniaNew").val(),
                repetirContraseniaNueva:$("#repetirContraseniaNew").val()
            }),
        headers: {
            "X-CSRF-Token":  $('input[name="_csrf"]').val(),
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/seguridad/recuperarPassword', options);
        const respJSON = await res.json();

        if (res.ok) {
            $("#contraseniaNew").val("");
            $("#repetirContraseniaNew").val("");

            Toast.fire({
                icon: "success",
                title: "Contraseña actualizada correctamente"
            });

            $("#btnRecuperar").attr("disabled",true);

            setTimeout(() => {
                window.location.href = getAbsolutePath();
            }, 1500);


            offLoading(null);
        }else{
            offLoading(null);
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }
    }catch (e) {
        var error =  JSON.parse(e.responseText);
        offLoading(null);
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
};

function getAbsolutePath() {
    var loc = window.location;
    var pathName = loc.pathname.substring(0, loc.pathname.lastIndexOf('/') + 1);
    return loc.href.substring(0, loc.href.length - ((loc.pathname + loc.search + loc.hash).length - pathName.length));
}