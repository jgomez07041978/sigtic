const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

var habilitarArbol=true;
var idDomicilioTemp=-1;
var idUbicacionTemp=-1;


let tableUSer=null;


$(document).ready(function(){

    jQuery.event.special.mousewheel = {
        setup: function( _, ns, handle ){
            this.addEventListener("mousewheel", handle, { passive: true });
        }
    };

    inicializarDependencias();

    $('#idDomicilioEnte').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormNuevoUsuario")
    });

    $('#idCategoria').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormNuevoUsuario")
    });

    $("#idEntePublico").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large"
    });

    $('#idEntePublico').on('change', function() {
        obtieneUsuarios();
        getUnidadesAdmin();
        getUnidadesAdminPptal();
        getDomicilioEntePublico($('#idEntePublico').val());
        getCatagorias($("#idEntePublico").val());
    });

    $('#idNuevoUsuario').click(function () {
        if ($("#idEntePublico").val()==""){
            Toast.fire({
                icon: 'warning',
                title: "Seleccione un Ente Público"
            });
        }else {
            limpiarFormUsuario();
            deshabilitar_elementos
            $('#titleFormHeader').text("Nuevo Usuario");
            var texto = $("#idEntePublico").find('option:selected').text();
            $('#textEntePublico').text(texto);
            $("#nav-data-general-tab-user").tab('show');
            $('#dlgModalFormNuevoUsuario').modal('show');
        }

    });

    $("#btnOpenArbolUsuario").click(function () {
            $('#modal_arbol_unidad').modal('show');
    });

    $("#txtBuscar").keyup(function () {
        busqueda($(this).val())
    });

    $("#btnOpenArbolPptal").click(function () {
        if (habilitarArbol)
            $('#modal_arbol_unidad2').modal('show');
    });

    $("#txtBuscar2").keyup(function () {
        busqueda2($(this).val())
    });

    $("#idBtnBuscar").click(function () {
        limpiarSearch();
        $('#dlgModalFormNuevoUsuario').modal('hide');
        $('#dlgModalSearchUsuario').modal('show');
    });

    $("#idBtnCloseSearch").click(function () {
        $('#dlgModalSearchUsuario').modal('hide');
        $('#dlgModalFormNuevoUsuario').modal('show');
    });


    $("#formBusquedaUsuarioEnte").submit(function(e) {
        e.preventDefault();
        $("#idCardTablaUsuarioSearch").hide();
        $("#rfcSearch").val($("#rfcSearch").val().trim());
        $("#curpSearch").val($("#curpSearch").val().trim());
        $("#nombreSearch").val($("#nombreSearch").val().trim());
        $("#segundoNombreSearch").val($("#segundoNombreSearch").val().trim());
        $("#apellidoPaternoSearch").val($("#apellidoPaternoSearch").val().trim());
        $("#apellidoMaternoSearch").val($("#apellidoMaternoSearch").val().trim());

        if ($("#rfcSearch").val().trim()=="" && $("#curpSearch").val().trim()=="" && $("#nombreSearch").val().trim()=="" && $("#segundoNombreSearch").val().trim()==""
            && $("#apellidoPaternoSearch").val().trim()=="" && $("#apellidoMaternoSearch").val().trim()=="")
            Toast.fire({
                icon: 'warning',
                //title: 'Validar',
                html: "Se requiere por lo menos un campo de búsqueda"
            });
        else{
            obtieneUsuariosSearch();
            return true;
        }
    });

    $("#idBtnLimpiar").click(function () {
        limpiarSearch();

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
            telefonoInstitucional: { minlength: 10, maxlength: 10},
            idCategoria: "required"
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

    $("#idNuevoUsuarioSearch").click(function () {
        $("#titleFormHeaderCatUsuario").text("Nuevo Usuario");
        $("#rfcCat").val("");
        $("#curpCat").val("");
        $("#nombreCat").val("");
        $("#segundoNombreCat").val("");
        $("#apellidoPaternoCat").val("");
        $("#apellidoMaternoCat").val("");
        $("#cveSexoFemeninoCat").prop("checked", true);
        $("#fechaNacimientoCat").val("");
        $("#numCelularCat").val("");
        $("#numTelefonoPersonalCat").val("");
        $("#correoPersonalCat").val("");

        $("#rfcCat").removeClass( "is-valid" );
        $("#rfcCat").removeClass( "is-invalid" );
        $("#curpCat").removeClass( "is-valid" );
        $("#curpCat").removeClass( "is-invalid" );
        $("#nombreCat").removeClass( "is-valid" );
        $("#nombreCat").removeClass( "is-invalid" );
        $("#apellidoPaternoCat").removeClass( "is-valid" );
        $("#apellidoPaternoCat").removeClass( "is-invalid" );
        $("#apellidoMaternoCat").removeClass( "is-valid" );
        $("#apellidoMaternoCat").removeClass( "is-invalid" );
        $("#fechaNacimientoCat").removeClass( "is-valid" );
        $("#fechaNacimientoCat").removeClass( "is-invalid" );

        $("#numCelularCat").removeClass( "is-valid" );
        $("#numCelularCat").removeClass( "is-invalid" );
        $("#numTelefonoPersonalCat").removeClass( "is-valid" );
        $("#numTelefonoPersonalCat").removeClass( "is-invalid" );
        $("#correoPersonalCat").removeClass( "is-valid" );
        $("#correoPersonalCat").removeClass( "is-invalid" );

        $('#dlgModalFormNuevoCatUsuario').modal('show');
    });

    $("#formCatUsuario").validate( {
        ignore: "",
        rules: {
            rfcCat: { required:true, minlength: 10, maxlength: 13},
            curpCat: { required:true, minlength: 18, maxlength: 18},
            nombreCat:"required",
            apellidoPaternoCat:{
                required: function () {
                    if ($("#apellidoPaternoCat").val().trim()=="" && $("#apellidoMaternoCat").val().trim()=="")
                        return true;
                    else{
                        $("#apellidoPaternoCat").removeClass( "is-invalid" )
                        $("#apellidoPaternoCat").addClass( "is-valid" );
                        $("#apellidoMaternoCat").removeClass( "is-invalid" )
                        $("#apellidoMaternoCat").addClass( "is-valid" );
                        return false;
                    }
                }
            },
            apellidoMaternoCat:{
                required: function () {
                    if ($("#apellidoPaternoCat").val().trim()=="" && $("#apellidoMaternoCat").val().trim()=="")
                        return true;
                    else{
                        return false;
                        $("#apellidoPaternoCat").removeClass( "is-invalid" )
                        $("#apellidoPaternoCat").addClass( "is-valid" );
                        $("#apellidoMaternoCat").removeClass( "is-invalid" )
                        $("#apellidoMaternoCat").addClass( "is-valid" );
                    }
                }
            },
            fechaNacimientoCat:"required",
            numCelularCat: {minlength: 10, maxlength: 13},
            numTelefonoPersonalCat: {minlength: 10, maxlength: 13},
            correoPersonalCat: {email: true}
        },
        messages: {
            rfcCat:"RFC Valido requerido",
            curpCat:"CURP Valido requerido",
            nombreCat:"Nombre Requerido",
            apellidoPaternoCat:"Apellido Paterno o Materno Requerido",
            apellidoMaternoCat:"Apellido Paterno o Materno Requerido",
            fechaNacimientoCat:"Fecha de nacimiento requerida",
            numCelularCat:"Número de Celular Invalido",
            numTelefonoPersonalCat:"Número de Teléfono Invalido",
            correoPersonalCat:"Correo Invalido"
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
    } )

    $("#formCatUsuario").submit(function(e) {
        e.preventDefault();
        if ($('#formCatUsuario').valid()) {
            guardarCatUsuario();
            return true;
        }
    });

    $("#idVigente").on('change', function() {
        obtieneUsuarios();
    });

    $("#idHistorico").on('change', function() {
        obtieneUsuarios();
    });


});

const inicializarDependencias = async () =>{
    const options={
        method: "POST",
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/entePublico/lista', options);
        const respJSON = await res.json();

        llenarCombo('#idEntePublico', 'idEntePublico','nombreEntePublicoConCve', respJSON);
        $("#idEntePublico").val($("#idEntePublicoSession").val());
        $("#idEntePublico").trigger('change');

        offLoading(null);
    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
};

function obtieneUsuarios() {

    //var groupColumn=2;

    var formulario={
        "isVigente":$("#idVigente").is(':checked'),
        "isHistorico":$("#idHistorico").is(':checked'),
        "isNoVigente":false
    };

    formulario.catUsuario={"nombre": ""};

    if ($("#idEntePublico").val()!="")
        formulario.entePublico={idEntePublico:$("#idEntePublico").val()};

    onLoading(null);
    const table = new DataTable('#tableUsuario', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: true,
        ajax: {
            url: contexto+'/api/catalogoCatUsuarioEntePublico/tablaUsuario',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend(formulario, d, {}));
            },
            complete: function (data) {
                var total=data['responseJSON'].iTotalDisplayRecords;
                offLoading(null);
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [
            {
                targets: 0,
                width: "15%",
                className: "text-center",
            },
            {
                targets: 3,
                width: "15%",
                className: "text-center",
            },
            {
                targets: 4,
                width: "15%",
                className: "text-center",
            }],

        lengthChange: false,
        select: false
    });
}

function limpiarFormUsuario() {

    idUbicacionTemp=-1;
    idDomicilioTemp=-1;
    idUsuarioResguardoTemp=-1;
    $("#titleFormHeader").text("Nuevo Usuario");
    deshabilitar_elementos();
    $("#idUsuarioEntePublico").val("");
    $("#idUnidadAdmtvaUsuario").val("");
    $("#descUnidadAdmtvaUsuario").val("");
    $("#idUsuario").val("");
    $("#rfc").val("");
    $("#curp").val("");
    $("#nombre").val("");
    $("#segundoNombre").val("");
    $("#apellidoPaterno").val("");
    $("#apellidoMaterno").val("");
    $("#cveSexoFemenino").prop("checked", true);
    $("#fechaNacimiento").val("");
    $("#numCelular").val("");
    $("#numTelefonoPersonal").val("");
    $("#correoPersonal").val("");
    $("#cveEstatus").val("VIG");

    $("#correoInstitucional").val("");
    $("#telefonoInstitucional").val("");
    $("#extensionInstitucional").val("");
    $("#puesto").val("");
    $("#idCategoria").val("");
    $("#idCategoria").trigger('change');
    $("#idDomicilioEnte").val("");
    $("#idDomicilioEnte").trigger('change');
    $("#idUbicacionUnidadAdmtva").val("");
    $("#idUbicacionUnidadAdmtva").trigger('change');

    $("#idUnidadAdminPptal").val("");
    $("#descUnidadAdminPptal").val("");

    $("#idbtnSaveText").text("Guardar");
}

function deshabilitar_elementos() {
    $("#btnGuardarUsuario").removeClass("btn-primary");
    $("#btnGuardarUsuario").addClass("btn-secondary");
    $("#btnGuardarUsuario").prop('disabled', true);

  /*  $("#rfc").prop('disabled', true);
    $("#curp").prop('disabled', true);*/
    $("#nombre").prop('disabled', true);
    $("#segundoNombre").prop('disabled', true);
    $("#apellidoPaterno").prop('disabled', true);
    $("#apellidoMaterno").prop('disabled', true);
    $("#fechaNacimiento").prop('disabled', true);
    $("#numCelular").prop('disabled', true);
    $("#numTelefonoPersonal").prop('disabled', true);
    $("#correoPersonal").prop('disabled', true);
    habilitarArbol=false;
    $("#correoInstitucional").prop('disabled', true);
    $("#telefonoInstitucional").prop('disabled', true);
    $("#extensionInstitucional").prop('disabled', true);
    $("#cveEstatus").prop('disabled', true);
    $("#puesto").prop('disabled', true);
    $("#idCategoria").prop('disabled', true);
    $("#idDomicilioEnte").prop('disabled', true);
    $("#idUbicacionUnidadAdmtva").prop('disabled', true);
    $("#btnGuardarUsuario").prop('disabled', true);
}


function habilitar_elementos() {

    $("#btnGuardarUsuario").removeClass("btn-secondary");
    $("#btnGuardarUsuario").addClass("btn-primary");
    $("#btnGuardarUsuario").prop('disabled', false);

  /*  $("#rfc").prop('disabled', false);
    $("#curp").prop('disabled', false);*/
    $("#nombre").prop('disabled', false);
    $("#segundoNombre").prop('disabled', false);
    $("#apellidoPaterno").prop('disabled', false);
    $("#apellidoMaterno").prop('disabled', false);
    $("#cveSexoFemenino").prop("checked", true);
    $("#fechaNacimiento").prop('disabled', false);
    $("#numCelular").prop('disabled', false);
    $("#numTelefonoPersonal").prop('disabled', false);
    $("#correoPersonal").prop('disabled', false);
    habilitarArbol=true;
    $("#correoInstitucional").prop('disabled', false);
    $("#telefonoInstitucional").prop('disabled', false);
    $("#extensionInstitucional").prop('disabled', false);
    $("#cveEstatus").prop('disabled', false);
    $("#puesto").prop('disabled', false);
    $("#idCategoria").prop('disabled', false);
    $("#idDomicilioEnte").prop('disabled', false);
    $("#idUbicacionUnidadAdmtva").prop('disabled', false);
    $("#btnGuardarUsuario").prop('disabled', false);
}

function getUnidadesAdmin(){

    obtienearbolPorInstitucion(function (data) {
        $("#idUnidadAdmtvaUsuario").val(data.id);
        $("#descUnidadAdmtvaUsuario").val(data.text);
        limpiarBuscarArbol();
        $('#modal_arbol_unidad').modal('hide');
    },$("#idEntePublico").val());
}

function getUnidadesAdminPptal(){

    obtienearbolPorUnidadAdminPresupuestal(function (data) {
        $("#idUnidadAdminPptal").val(data.id);
        $("#descUnidadAdminPptal").val(data.text);
        limpiarBuscarArbol2();
        $('#modal_arbol_unidad2').modal('hide');
    },$("#idEntePublico").val());

}
function limpiarSearch(){

    $("#rfcSearch").val("");
    $("#curpSearch").val("");
    $("#nombreSearch").val("");
    $("#segundoNombreSearch").val("");
    $("#apellidoPaternoSearch").val("");
    $("#apellidoMaternoSearch").val("");


    if (tableUSer!=null)
        tableUSer.clear();
    $("#idCardTablaUsuarioSearch").hide();
}


function obtieneUsuariosSearch() {

    var formulario={
        "isVigente":true,
        "isHistorico":true,
        "isNoVigente":false,
        "rfc": $("#rfcSearch").val(),
        "curp": $("#curpSearch").val(),
        "nombre": $("#nombreSearch").val(),
        "segundoNombre": $("#segundoNombreSearch").val(),
        "apellidoPaterno": $("#apellidoPaternoSearch").val(),
        "apellidoMaterno": $("#apellidoMaternoSearch").val()
    };

    if (tableUSer!=null)
        tableUSer.clear();

    onLoading(null);
    tableUSer = new DataTable('#tableUsuarioSearch', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/catalogoCatUsuario/tablaUsuarioSearch',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend(formulario, d, {}));
            },
            complete: function (data) {
                //var total=data['responseJSON'].iTotalDisplayRecords;
                $("#idCardTablaUsuarioSearch").show();

                offLoading(null);
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [{
            targets: 0,
            "width": "20%",
            "className": "text-center",
        },{
            targets: 1,
            "width": "20%",
            "className": "text-center",
        },{
            targets: 3,
            "width": "10%",
            "className": "text-center",
        },
            {
                targets: 4,
                "width": "10%",
                "className": "text-center",
            }],
        lengthChange: false,
        select: false
    });

}


function seleccionarUsuario(idUsuario) {


    $('#preloader').modal('show');
    $.ajax({
        url: contexto + '/api/catalogoCatUsuarioEntePublico/getUsuarioEntePublico/' + idUsuario+'/'+$("#idEntePublico").val(),
        type: 'GET',
        dataType: 'JSON',
        contentType: 'application/json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function (response) {
            habilitar_elementos();
            if (response.idUsuarioEntePublico!=null){
                $("#idUsuarioEntePublico").val(response.idUsuarioEntePublico);

                if (response.unidadAdministrativa!=null) {
                    $("#idUnidadAdmtvaUsuario").val(response.unidadAdministrativa.idUnidadAdministrativa);
                    $("#descUnidadAdmtvaUsuario").val(response.unidadAdministrativa.nombreUnidadAdministrativa);
                }

                $("#correoInstitucional").val(response.correoInstitucional);
                $("#telefonoInstitucional").val(response.numTelefonoInstitucional);
                $("#extensionInstitucional").val(response.numeroExtensionInstitucional);

                $("#puesto").val(response.puesto);

                $("#idCategoria").val("");
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

                $("#titleFormHeader").text("Editar Usuario");
                $("#idbtnSaveText").text("Actualizar");

            }else
                $("#titleFormHeader").text("Nuevo Usuario");


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
            }
            $('#dlgModalSearchUsuario').modal('hide');
            $('#dlgModalFormNuevoUsuario').modal('show');

            $('#preloader').modal('hide');
        },
        error: function (response) {
            var error = JSON.parse(response.responseText);
            $('#preloader').modal('hide');
            Swal.fire({
                title: 'Error',
                text: error.message,
                icon: 'error',
            });
        }
    });
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

        }

        if (opcion=="domicilio") {
            if (idDomicilioTemp != null && idDomicilioTemp != -1) {
                $("#idDomicilioEnte").val(idDomicilioTemp);
                $("#idDomicilioEnte").trigger('change');
            }
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

const guardarUsuarioAsync = async () =>{

    const options={
        method: "POST",
        body: JSON.stringify({
            idUsuarioEntePublico: $("#idUsuarioEntePublico").val(),
            entePublico:{idEntePublico:$("#idEntePublico").val()},
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
        onLoading("dlgModalFormNuevoUsuario");
        const res = await fetch(contexto+'/api/catalogoCatUsuarioEntePublico/guardarUsuarioEntePublico', options);
        const respJSON = await res.json();

        if (res.ok) {

            /*
            $("#idUsuarioEntePublico").val(respJSON.idUsuarioEntePublico);
            $("#titleFormHeader").text("Editar Usuario");
            $("#idbtnSaveText").text("Actualizar");
            */

            Toast.fire({
                icon: "success",
                title: "El Usuario se guardo correctamente"
            });

            obtieneUsuarios();
            offLoading("dlgModalFormNuevoUsuario");
            $('#dlgModalFormNuevoUsuario').modal('hide');
        } else{
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalFormNuevoUsuario");
        }

    }catch (e) {
        var error =  JSON.parse(e.responseText);
        offLoading("dlgModalFormSistema");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
};

function guardarUsuario(){

    let titleSave='¿Guardar Usuario?';
    let msgSave='¿Estas seguro que deseas guardar los datos del Usuario?';
    if ($('#idUsuarioEntePublico').val() != ""){
        titleSave='¿Actualizar Usuario?';
        msgSave='¿Estas seguro que deseas actualizar los datos del Usuario?';
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
            guardarUsuarioAsync();
        }
    });
}

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

            habilitar_elementos();
            if (response.idUsuarioEntePublico!=null){
                $("#idUsuarioEntePublico").val(response.idUsuarioEntePublico);

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

                $("#titleFormHeader").text("Editar Usuario");
                $("#idbtnSaveText").text("Actualizar");

            }else
                $("#titleFormHeader").text("Nuevo Usuario");


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
            $("#textEntePublico").text($("#idEntePublico option:selected").text()); //se agrego
            $('#dlgModalFormNuevoUsuario').modal('show');

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


function eliminarUsuario(idUsuarioEntePublico){

    Swal.fire({
        title: 'Eliminar Usuario',
        text: '¿Estás seguro que deseas eliminar el usuario?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No",
        reverseButtons: true,
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminaUsuario(idUsuarioEntePublico);
        }});
}

function eliminaUsuario(idUsuarioEntePublico) {
    $('#preloader').modal('show');
    $.ajax({
        url: contexto+'/api/catalogo/usuario/general/deleteUsuario',
        type: 'DELETE',
        contentType: 'application/json',
        dataType: 'JSON',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        data: JSON.stringify({
            idUsuarioEntePublico: idUsuarioEntePublico,
        }),
        success: function (response) {
            setTimeout(function () {
                $('#preloader').modal('hide');
                if( response !== null){
                    obtieneUsuarios();
                    Swal.fire({
                        title: 'Mensaje',
                        text:  "El usuario se eliminó correctamente.",
                        icon: 'success',
                    });
                }
            }, 1000);
        },
        error: function(response) {
            var error =  JSON.parse(response.responseText);
            setTimeout(function () {
                $('#preloader').modal('hide');
                Swal.fire({
                    title: 'Mensaje',
                    text:  error.message,
                    icon: 'error',
                });
            }, 1000);
        }
    });
}

const guardarCatUsuarioAsync = async () =>{
    const options={
        method: "POST",
        body: JSON.stringify({
            rfc: $("#rfcCat").val(),
            curp: $("#curpCat").val(),
            nombre: $("#nombreCat").val(),
            segundoNombre: $("#segundoNombreCat").val(),
            apellidoPaterno: $("#apellidoPaternoCat").val(),
            apellidoMaterno: $("#apellidoMaternoCat").val(),
            //fechaNacimiento: dateNacimiento[2] + "/" + dateNacimiento[1] + "/" + dateNacimiento[0],
            fechaNacimiento: $("#fechaNacimientoCat").val(),
            cveSexo: $("#cveSexoFemeninoCat").is(":checked")?"F":"M",
            numCelular: $("#numCelularCat").val(),
            numTelefonoPersonal: $("#numTelefonoPersonalCat").val(),
            correoPersonal: $("#correoPersonalCat").val(),
            cveEstatus: "VIG"
        }),
        headers: headers
    };

    try {
        onLoading("dlgModalFormNuevoCatUsuario");
        const res = await fetch(contexto+'/api/catalogoCatUsuario/guardarCatUsuario', options);
        const respJSON = await res.json();
        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Los datos del usuario se guardaron correctamente"
            });

            offLoading("dlgModalFormNuevoCatUsuario");
            habilitar_elementos();
            $("#idUsuario").val(respJSON.idUsuario);
            $("#rfc").val(respJSON.rfc);
            $("#curp").val(respJSON.curp);
            $("#nombre").val(respJSON.nombre);
            $("#segundoNombre").val(respJSON.segundoNombre);
            $("#apellidoPaterno").val(respJSON.apellidoPaterno);
            $("#apellidoMaterno").val(respJSON.apellidoMaterno);

            if (respJSON.cveSexo!=null && respJSON.cveSexo=="M")
                $("#cveSexoMasculino").prop("checked", true);
            else
                $("#cveSexoFemenino").prop("checked", true);


            if (respJSON.fechaNacimiento!=null) {
                var date1 = respJSON.fechaNacimientoText.split("/");
                $("#fechaNacimiento").val(date1[2] + "-" + date1[1] + "-" + date1[0]);
            }

            $("#numCelular").val(respJSON.numCelular);
            $("#numTelefonoPersonal").val(respJSON.numTelefonoPersonal);
            $("#correoPersonal").val(respJSON.correoPersonal);

            $('#dlgModalSearchUsuario').modal('hide');
            $('#dlgModalFormNuevoCatUsuario').modal('hide');
            $('#dlgModalFormNuevoUsuario').modal('show');

        }else{
            offLoading("dlgModalFormNuevoCatUsuario");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }

    }catch (e) {
        offLoading("dlgModalFormNuevoCatUsuario");
        Toast.fire({
            icon: "error",
            title: "Error al guardar"
        });
    }
};


function guardarCatUsuario(){

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
            guardarCatUsuarioAsync();
        }
    });
}

function exportarConFiltro() {
    const idEnte = $("#idEntePublico").val();

    const params = {
        entePublico: idEnte ? { idEntePublico: parseInt(idEnte, 10) } : null,
        catUsuario:  { nombre: "" },
        isVigente:   $("#idVigente").is(":checked"),
        isHistorico: $("#idHistorico").is(":checked"),
        isNoVigente: false
    };

    $("#btnExportarFiltro").prop("disabled", true);
    onLoading(null);

    fetch(contexto + '/api/catalogoCatUsuarioEntePublico/exportarUsuariosFiltro', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-Token': $('input[name="_csrf"]').val()
        },
        body: JSON.stringify(params)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al generar el archivo.");
            }

            const disposition = response.headers.get("Content-Disposition");
            let fileName = "usuarios_filtro.xlsx";

            if (disposition && disposition.indexOf("filename=") !== -1) {
                fileName = disposition.split("filename=")[1].replace(/"/g, "");
            }

            return response.blob().then(blob => ({ blob, fileName }));
        })
        .then(data => {
            const url = window.URL.createObjectURL(data.blob);

            const a = document.createElement("a");
            a.href = url;
            a.download = data.fileName;

            document.body.appendChild(a);
            a.click();
            a.remove();

            window.URL.revokeObjectURL(url);

            offLoading(null);
            $("#btnExportarFiltro").prop("disabled", false);

            Toast.fire({
                icon: "success",
                title: "La exportación se realizó correctamente."
            });
        })
        .catch(error => {
            offLoading(null);
            $("#btnExportarFiltro").prop("disabled", false);

            Toast.fire({
                icon: "error",
                title: error.message
            });

            console.error(error);
        });
}

function exportarTodos() {

    $("#btnExportarTodos").prop("disabled", true);
    onLoading(null);

    fetch(contexto + '/api/catalogoCatUsuarioEntePublico/exportarUsuariosTodos', {
        method: 'GET'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al generar el archivo.");
            }

            const disposition = response.headers.get("Content-Disposition");
            let fileName = "usuarios_todos.xlsx";

            if (disposition && disposition.indexOf("filename=") !== -1) {
                fileName = disposition.split("filename=")[1].replace(/"/g, "");
            }

            return response.blob().then(blob => ({ blob, fileName }));
        })
        .then(data => {
            const url = window.URL.createObjectURL(data.blob);

            const a = document.createElement("a");
            a.href = url;
            a.download = data.fileName;

            document.body.appendChild(a);
            a.click();
            a.remove();

            window.URL.revokeObjectURL(url);

            offLoading(null);
            $("#btnExportarTodos").prop("disabled", false);

            Toast.fire({
                icon: "success",
                title: "La exportación se realizó correctamente."
            });
        })
        .catch(error => {
            offLoading(null);
            $("#btnExportarTodos").prop("disabled", false);

            Toast.fire({
                icon: "error",
                title: error.message
            });

            console.error(error);
        });
}
