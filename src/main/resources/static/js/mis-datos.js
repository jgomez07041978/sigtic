
const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};


let idUbicacionTemp=-1;

$(document).ready(function() {

    $('[data-bs-toggle="tooltip"]').tooltip();

    jQuery.event.special.mousewheel = {
        setup: function (_, ns, handle) {
            this.addEventListener("mousewheel", handle, {passive: true});
        }
    };

    $('#idCategoria').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
    });

    $('#idDomicilioEnte').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
    });

    getUnidadesAdmin();
    getUnidadesAdminPptal();
    getCatagorias($("#idEntePublicoSession").val());


    $("#btnOpenArbolUsuario").click(function () {
        $('#modal_arbol_unidad').modal('show');
    });

    $("#txtBuscar").keyup(function () {
        busqueda($(this).val())
    });

    $("#btnOpenArbolPptal").click(function () {
            $('#modal_arbol_unidad2').modal('show');
    });

    $("#txtBuscar2").keyup(function () {
        busqueda2($(this).val())
    });

    $("#formUsuario").validate( {
        ignore: "",
        rules: {
            descUnidadAdmtvaUsuario:"required",
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
            numCelular: { minlength: 10, maxlength: 10},
            numTelefonoPersonal: { minlength: 10, maxlength: 10},
            correoPersonal: {email: true},
            correoInstitucional: {email: true},
            telefonoInstitucional: {minlength: 10, maxlength: 10},
            idCategoria: "required",

        },
        messages: {
            descUnidadAdmtvaUsuario:"Unidad administrativa Requerido",
            nombre:"Nombre Requerido",
            apellidoPaterno:"Apellido Paterno o Materno Requerido",
            apellidoMaterno:"Apellido Paterno o Materno Requerido",
            fechaNacimiento:"Fecha de Nacimiento Requerido",
            numCelular:"Número de Celular Invalido",
            numTelefonoPersonal:"Número de Teléfono Invalido",
            correoPersonal:"Correo Invalido",
            correoInstitucional:"Correo Invalido",
            telefonoInstitucional:"Número de Teléfono Invalido",
            idCategoria:"Categoría es Requerido"

        },
        errorElement: "em",
        errorPlacement: function (error, element) {

                //error.insertAfter(element);
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
        },
        invalidHandler: function(e, validator){
            if(validator.errorList.length>0) {
                $("#"+jQuery(validator.errorList[0].element, '#nav-tab').closest(".tab-pane").attr('id')+"-tab").tab('show');
            }
        }
    } );

    $("#formUsuario").submit(function(e) {
        e.preventDefault();
        if ($('#formUsuario').valid()) {
            if ($("#idUsuario").val()!="") {
                guardarUsuario();
                return true;
            }
            else{
                Toast.fire({
                    icon: 'warning',
                    title: "Seleccione un Usuario"
                });
                return false;
            }

        }
        else return false;
    });

});

function editarUsuario(idUsuarioEntePublico){

    $("#nav-data-general-tab-user").tab('show');

    $('#preloader').modal('show');
    $.ajax({ url: contexto+'/api/catalogo/usuario/general/getUsuario/'+idUsuarioEntePublico,
        type: 'GET',
        dataType: 'JSON',
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function(response) {

           // habilitar_elementos();

                $("#textEntePublico").text(response.entePublico.cveEntePublico+" - "+response.entePublico.nombreEntePublico);

                if (response.unidadAdministrativa!=null) {
                    $("#idUnidadAdmtvaUsuario").val(response.unidadAdministrativa.idUnidadAdministrativa);
                    $("#descUnidadAdmtvaUsuario").val(response.unidadAdministrativa.nombreUnidadAdministrativa);
                }

                $("#correoInstitucional").val(response.correoInstitucional);
                $("#telefonoInstitucional").val(response.numTelefonoInstitucional);
                $("#extensionInstitucional").val(response.numeroExtensionInstitucional);

                $("#puesto").val(response.puesto);

                if (response.categoria!=null) {
                    $("#idCategoria").val(response.categoria.idCategoriaUsuario);
                    $("#idCategoria").trigger('change');
                }


                if (response.ubicacionUnidadAdmin!=null){
                    idUbicacionTemp=response.ubicacionUnidadAdmin.idUbicacionAdaministrativa;
                    $("#idDomicilioEnte").val(response.ubicacionUnidadAdmin.domicilioEntePublico.idDomicilioEntePublico);
                    $("#idDomicilioEnte").trigger('change');
                }

                if (response.unidadAdministrativaPptal!=null) {
                    $("#idUnidadAdminPptal").val(response.unidadAdministrativaPptal.idUnidadAdministrativa);
                    $("#descUnidadAdminPptal").val(response.unidadAdministrativaPptal.nombreUnidadAdministrativa)
                }

                $("#cveEstatus").val(response.cveEstatus);

            if (response.catUsuario!=null) {
                $("#idUsuario").val(response.catUsuario.idUsuario);
                $("#rfc").val(response.catUsuario.rfc);
                $("#curp").val(response.catUsuario.curp);
                $("#nombre").val(response.catUsuario.nombre);
                $("#segundoNombre").val(response.catUsuario.segundoNombre);
                $("#apellidoPaterno").val(response.catUsuario.apellidoPaterno);
                $("#apellidoMaterno").val(response.catUsuario.apellidoMaterno);

                if (response.catUsuario.cveSexo!=null && response.catUsuario.cveSexo=="M")
                    $("#cveSexoMasculino").prop("checked", true);
                else
                    $("#cveSexoFemenino").prop("checked", true);


                if (response.catUsuario.fechaNacimiento!=null) {
                    var date1 = response.catUsuario.fechaNacimientoText.split("/");
                    $("#fechaNacimiento").val(date1[2] + "-" + date1[1] + "-" + date1[0]);
                }

                $("#numCelular").val(response.catUsuario.numCelular);
                $("#numTelefonoPersonal").val(response.catUsuario.numTelefonoPersonal);
                $("#correoPersonal").val(response.catUsuario.correoPersonal);
                $("#cveEstatusUsu").val(response.catUsuario.cveEstatus);
            }

                $("#idbtnSaveText").text("Actualizar");


            $('#preloader').modal('hide');

        },
        error: function (response) {
            var error =  JSON.parse(response.responseText);
            $('#preloader').modal('hide');
            Swal.fire({
                title: 'Error',
                text: error.message,
                icon: 'error',
            });
        }
    });
}

function getUnidadesAdmin(){

    obtienearbolPorInstitucion(function (data) {
        $("#idUnidadAdmtvaUsuario").val(data.id);
        $("#descUnidadAdmtvaUsuario").val(data.text);
        limpiarBuscarArbol();
        $('#modal_arbol_unidad').modal('hide');
    },$("#idEntePublicoSession").val());
}

function getCatagorias(idPadre,callback){
    if (idPadre!=null)
        fillSelectGetInterno('/api/catalogo/tic/getCategoria', '#idCategoria','idCategoriaUsuario','nombreCategoriaUsuario',idPadre, callback, "categoria");
    else
        $('#idCategoria').empty();
}



function getDomicilioEntePublico(idPadre,callback){
    if (idPadre!=null)
        fillSelectGetInterno('/api/catalogo/domicilioentePublico/all', '#idDomicilioEnte','idDomicilio','nombreDomicilio',idPadre, callback, "domicilio");
    else
        $('#idDomicilioEnte').empty();
}

function actualizarUbicaciones() {
    if ($("#idDomicilioEnte").val()!="")
        getUbicacionUnidad($("#idDomicilioEnte").val());
    else
        $('#idUbicacionUnidadAdmtva').empty();
}


function getUbicacionUnidad(idPadre, callback){
    if (idPadre!=null)
        fillSelectGetInterno('/api/catalogo/ubicacionUnidadAministrativa/all', '#idUbicacionUnidadAdmtva','idUbicacionAdaministrativa','nombreUbicacion',idPadre, callback, "ubicacion");
    else
        $('#idUbicacionUnidadAdmtva').empty();
}

async function fillSelectGetInterno(relativeUrl,idElemento, value, descripcion, idPadre, callback, opcion){

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

        if (opcion=="categoria") {
            getDomicilioEntePublico($('#idEntePublicoSession').val());
        }

        if (opcion=="domicilio") {
            editarUsuario($("#idUsuarioEntePublico").val());
        }

        if (opcion=="ubicacion") {
            if (idUbicacionTemp != null && idUbicacionTemp != -1) {
                $("#idUbicacionUnidadAdmtva").val(idUbicacionTemp);
                //$("#idUbicacionUnidadAdmtva").trigger('change');
            }
        }


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

function getUnidadesAdminPptal(){

    obtienearbolPorUnidadAdminPresupuestal(function (data) {
        $("#idUnidadAdminPptal").val(data.id);
        $("#descUnidadAdminPptal").val(data.text);
        limpiarBuscarArbol2();
        $('#modal_arbol_unidad2').modal('hide');
    },$("#idEntePublicoSession").val());
}

function guardarUsuario(){

    let titleSave='¿Actualizar Usuario?';
    let msgSave='¿Estas seguro que deseas actualizar sus datos?';


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

const guardarUsuarioAsync = async () =>{

    const options={
        method: "POST",
        body: JSON.stringify({
            idUsuarioEntePublico: $("#idUsuarioEntePublico").val(),
            entePublico:{idEntePublico:$("#idEntePublicoSession").val()},
            unidadAdministrativa:{idUnidadAdministrativa:$("#idUnidadAdmtvaUsuario").val()},
            categoria:{idCategoriaUsuario:$("#idCategoria").val()},
            catUsuario:{
                idUsuario: $("#idUsuario").val(),
                rfc: $("#rfc").val(),
                curp: $("#curp").val(),
                nombre: $("#nombre").val(),
                segundoNombre: $("#segundoNombre").val(),
                apellidoPaterno: $("#apellidoPaterno").val(),
                apellidoMaterno: $("#apellidoMaterno").val(),
                //fechaNacimiento: dateNacimiento[2] + "/" + dateNacimiento[1] + "/" + dateNacimiento[0],
                fechaNacimiento: $("#fechaNacimiento").val(),
                cveSexo: $("#cveSexoFemenino").is(":checked")?"F":"M",
                numCelular: $("#numCelular").val(),
                numTelefonoPersonal: $("#numTelefonoPersonal").val(),
                correoPersonal: $("#correoPersonal").val(),
                cveEstatus: $("#cveEstatusUsu").val()
            },
            correoInstitucional: $("#correoInstitucional").val(),
            numTelefonoInstitucional: $("#telefonoInstitucional").val(),
            numeroExtensionInstitucional: $("#extensionInstitucional").val(),
            cveEstatus: $("#cveEstatus").val(),
            puesto:$("#puesto").val(),
            ubicacionUnidadAdmin:{idUbicacionAdaministrativa:$("#idUbicacionUnidadAdmtva").val()},
            unidadAdministrativaPptal:{idUnidadAdministrativa:$("#idUnidadAdminPptal").val()}
        }),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/catalogoCatUsuarioEntePublico/guardarUsuarioEntePublico', options);
        const respJSON = await res.json();

        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "El Usuario se guardo correctamente"
            });

            offLoading(null);
        } else{
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading(null);
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

