const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

var habilitarArbol2=true;
var habilitarArbol3=true;
var validado=false;
var idPuestoTemp=-1;
var idUbicacionTemp=-1;
var idDomicilioTemp=-1;
var idUsuarioResguardoTemp=-1;

$(document).ready(function(){

    jQuery.event.special.mousewheel = {
        setup: function( _, ns, handle ){
            this.addEventListener("mousewheel", handle, { passive: true });
        }
    };

    inicializarDependencias();

    $("#idEntePublico").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large"
    });

    $("#idEntePublicoUsuario").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormNuevoUsuario")
    });

    $("#idEntePublicoResguardante").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormNuevoUsuario")
    });


    $("#idPuesto").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormNuevoUsuario")
    });

    $("#idDomicilioEnte").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormNuevoUsuario")
    });

    $("#idUsuarioTitular").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormNuevoUsuario")
    });

    $("#btnLimpiarUnidad").click(function () {
        $("#idUnidadAdmtva").val("");
        $("#descUnidadAdmtva").val("");
    });

    $('#idEntePublico').on('change', function() {
        $("#idUnidadAdmtva").val("");
        $("#descUnidadAdmtva").val("");
        getUnidadesAdmin();
    });

    $('#idEntePublicoUsuario').on('change', function() {
        $("#idUnidadAdmtvaUsuario").val("");
        $("#descUnidadAdmtvaUsuario").val("");
        getUnidadesAdminUsuario();
        getPuesto($('#idEntePublicoUsuario').val());
        getDomicilioEntePublico($('#idEntePublicoUsuario').val());
    });

    $('#idEntePublicoResguardante').on('change', function() {
        $("#idUnidadAdmtvaResguardante").val("");
        $("#descUnidadAdmtvaResguardante").val("");
        getUnidadesAdminResguarda();
    });

    $("#btnOpenArbol").click(function () {
        $('#modal_arbol_unidad').modal('show');
    });

    $("#txtBuscar").keyup(function () {
        busqueda($(this).val())
    });

    $("#btnOpenArbolUsuario").click(function () {
        if (habilitarArbol3)
            $('#modal_arbol_unidad2').modal('show');
    });

    $("#txtBuscar2").keyup(function () {
        busqueda2($(this).val())
    });

    $("#btnOpenArbolResguardo").click(function () {
        if (habilitarArbol3)
            $('#modal_arbol_unidad3').modal('show');
    });

    $("#txtBuscar3").keyup(function () {
        busqueda3($(this).val())
    });

    getUnidadesAdminPptal();

    $("#btnOpenArbolPptal").click(function () {
        if (habilitarArbol3)
            $('#modal_arbol_unidad4').modal('show');
    });


    $("#formBusquedaUsuario").submit(function(e) {
        e.preventDefault();
        $("#idCardTablaUsuario").hide();
        obtieneUsuarios();
        return true;
    });

    $('#idNuevoUsuario').click(function () {
        limpiarFormUsuario();
        $("#idEntePublicoUsuario").val($("#idEntePublico").val());
        $("#idEntePublicoUsuario").trigger('change');
       /* $("#idEntePublicoResguardante").val($("#idEntePublico").val());
        $("#idEntePublicoResguardante").trigger('change');*/
        $('#titleFormHeader').text("Nuevo Usuario");
        habilitar_rfc();
        $("#nav-data-general-tab-user").tab('show');
        $('#dlgModalFormNuevoUsuario').modal('show');

    });

    $('#idBtnValidar').click(function () {
        var msg_validar="";

        if ($("#idEntePublicoUsuario").val()=="")
            msg_validar="Institución";

        if ($("#rfc").val()==""){
            if (msg_validar=="")
                msg_validar="RFC";
            else msg_validar+="<br>RFC";
        }
        else{
              if ($("#rfc").val().trim().length<13){
                  if (msg_validar=="")
                      msg_validar="El RFC debe ser de 13 caracteres";
                  else msg_validar+="<br>El RFC debe ser de 13 caracteres";
              }
        }

        if (msg_validar!="") {
            Toast.fire({
                icon: 'warning',
                //title: 'Validar',
                html: "<b>Campos Requeridos :</b><br/>" + msg_validar
            });
        }else{
            $('#preloader').modal('show');
            $.ajax({
                url: contexto + '/api/catalogo/usuario/general/validar-usuario',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                },
                data: JSON.stringify({
                        entePublico:{ idEntePublico:$("#idEntePublicoUsuario").val() },
                        catUsuario:{
                            rfc: $("#rfc").val()
                        }
                    }
                ),
                success: function (response) {

                    validado=true;

                    var catUsuarioEntePublico=response;

                    if (catUsuarioEntePublico.idUsuarioEntePublico==null){
                        $("#titleFormHeader").text("Nuevo Usuario");
                        deshabilitar_rfc();
                        $("#idUsuarioEntePublico").val("");
                    }else{
                        $("#titleFormHeader").text("Editar Usuario");
                        deshabilitar_rfc()
                        $("#idUsuarioEntePublico").val(catUsuarioEntePublico.idUsuarioEntePublico);
                    }

                    $("#nombre").val(catUsuarioEntePublico.catUsuario.nombre);
                    $("#segundoNombre").val(catUsuarioEntePublico.catUsuario.segundoNombre);
                    $("#apellidoPaterno").val(catUsuarioEntePublico.catUsuario.apellidoPaterno);
                    $("#apellidoMaterno").val(catUsuarioEntePublico.catUsuario.apellidoMaterno);
                    $("#curp").val(catUsuarioEntePublico.catUsuario.curp);


                    if (catUsuarioEntePublico.catUsuario.cveSexo==null || catUsuarioEntePublico.catUsuario.cveSexo=="F")
                        $("#cveSexoFemenino").prop("checked", true);
                    else
                        $("#cveSexoMasculino").prop("checked", true);

                    if (catUsuarioEntePublico.catUsuario.fechaNacimientoText!=null) {
                        var date1 = catUsuarioEntePublico.catUsuario.fechaNacimientoText.split("/");
                        $("#fechaNacimiento").val(date1[2] + "-" + date1[1] + "-" + date1[0]);
                    }

                    $("#numCelular").val(catUsuarioEntePublico.catUsuario.numCelular);
                    $("#numTelefonoPersonal").val(catUsuarioEntePublico.catUsuario.numTelefonoPersonal);
                    $("#correoPersonal").val(catUsuarioEntePublico.catUsuario.correoPersonal);
                    $("#correoInstitucional").val(catUsuarioEntePublico.correoInstitucional);
                    $("#telefonoInstitucional").val(catUsuarioEntePublico.numTelefonoInstitucional);
                    $("#extensionInstitucional").val(catUsuarioEntePublico.numeroExtensionInstitucional);

                    if (catUsuarioEntePublico.cveEstatus!=null)
                        $("#cveEstatus").val(catUsuarioEntePublico.cveEstatus);


                    if (catUsuarioEntePublico.unidadAdministrativa!=null){
                        $("#idUnidadAdmtvaUsuario").val(catUsuarioEntePublico.unidadAdministrativa.idUnidadAdministrativa);
                        $("#descUnidadAdmtvaUsuario").val(catUsuarioEntePublico.unidadAdministrativa.nombreUnidadAdministrativa);
                    }

                    if (catUsuarioEntePublico.puestoUsuario!=null) {
                        $("#idPuesto").val(catUsuarioEntePublico.puestoUsuario.idPuestoUsuario);
                        $("#idPuesto").trigger('change');
                    }

                    if (catUsuarioEntePublico.ubicacionUnidadAdmin!=null && catUsuarioEntePublico.ubicacionUnidadAdmin.idUbicacionAdaministrativa!=null) {
                        idUbicacionTemp=catUsuarioEntePublico.ubicacionUnidadAdmin.idUbicacionAdaministrativa;
                        $("#idDomicilioEnte").val(catUsuarioEntePublico.ubicacionUnidadAdmin.domicilioEntePublico.idDomicilioEntePublico);
                        $("#idDomicilioEnte").trigger('change');
                    }

                    if (catUsuarioEntePublico.entePublicoResguardante!=null) {
                        $("#idEntePublicoResguardante").val(catUsuarioEntePublico.entePublicoResguardante.idEntePublico);
                        $("#idEntePublicoResguardante").trigger('change');
                        getUnidadesAdminResguarda();
                    }else{
                        $("#idEntePublicoResguardante").val("");
                        $("#idEntePublicoResguardante").trigger('change');
                        getUnidadesAdminResguarda();
                    }


                    if (catUsuarioEntePublico.unidadAdministrativaResguardante!=null) {
                        if (catUsuarioEntePublico.catUsuarioResguardante!=null)
                        idUsuarioResguardoTemp=catUsuarioEntePublico.catUsuarioResguardante.idUsuario;
                        $("#idUnidadAdmtvaResguardante").val(catUsuarioEntePublico.unidadAdministrativaResguardante.idUnidadAdministrativa);
                        $("#descUnidadAdmtvaResguardante").val(catUsuarioEntePublico.unidadAdministrativaResguardante.nombreUnidadAdministrativa);
                        getUsuarioUnidad($("#idUnidadAdmtvaResguardante").val());
                    }

                    if (catUsuarioEntePublico.unidadAdministrativaPptal!=null) {
                        $("#idUnidadAdminPptal").val(catUsuarioEntePublico.unidadAdministrativaPptal.idUnidadAdministrativa);
                        $("#descUnidadAdminPptal").val(catUsuarioEntePublico.unidadAdministrativaPptal.nombreUnidadAdministrativa);
                    }


                    $('#preloader').modal('hide');
                },
                error: function (response) {
                    var error = JSON.parse(response.responseText);
                    $('#preloader').modal('hide');
                    Swal.fire({
                        title: 'Error',
                        text: error.message,
                        icon: 'info',
                    });

                }
            });
        }

    });


    $("#formUsuario").validate( {
        ignore: "",
        rules: {
            idEntePublicoUsuario:"required",
            descUnidadAdmtvaUsuario:"required",
            apellidoMaterno:"required",
            rfc: {  required: true,
                minlength: 10,
                maxlength: 13},
            curp: {
                required: true,
                minlength: 18,
                maxlength: 18},
            nombre:"required",
            apellidoPaterno:"required",
            apellidoMaterno:"required",
            fechaNacimiento:"required",
            numCelular: { minlength: 10, maxlength: 10},
            numTelefonoPersonal: { minlength: 10, maxlength: 10},
            correoPersonal: {email: true},
            correoInstitucional: {email: true},
            telefonoInstitucional: { minlength: 10, maxlength: 10}

        },
        messages: {
            idEntePublicoUsuario:"Ente Público",
            descUnidadAdmtvaUsuario:"Seleccionar Unidad administrativa",
            rfc:"Introducir RFC valido",
            curp:"Introducir Curp valida",
            nombre:"Introducir Nombre",
            apellidoPaterno:"Introducir Apellido Paterno",
            apellidoMaterno:"Introducir Apellido Materno",
            fechaNacimiento:"Seleccionar Fecha de Nacimiento",
            numCelular:"Número de Celular Invalido",
            numTelefonoPersonal:"Número de Teléfono Invalido",
            correoPersonal:"Correo Invalido",
            correoInstitucional:"Correo Invalido",
            telefonoInstitucional:"Número de Teléfono Invalido"

        },
        errorElement: "em",
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

    $("#formUsuario").submit(function(e) {
        e.preventDefault();
        if ($('#formUsuario').valid()) {
            if (validado==true) {
                 guardarUsuario();
                return true;
            }
            else{
                Toast.fire({
                    icon: 'warning',
                    title: "Requiere validar el rfc"
                });
                return false;
            }

        }
        else return false;
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
        llenarCombo('#idEntePublicoUsuario', 'idEntePublico','nombreEntePublicoConCve', respJSON);
        llenarCombo('#idEntePublicoResguardante', 'idEntePublico','nombreEntePublicoConCve', respJSON);
        $("#idEntePublico").val($("#idEntePublicoSession").val());
        $("#idEntePublicoUsuario").val($("#idEntePublicoSession").val());
        $("#idEntePublicoResguardante").val($("#idEntePublicoSession").val());
        getUnidadesAdmin();
        getUnidadesAdminUsuario();
        getUnidadesAdminResguarda();

        offLoading(null);
    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
};

function limpiarFormUsuario() {

    idPuestoTemp=-1;
    idUbicacionTemp=-1;
    idDomicilioTemp=-1;
    idUsuarioResguardoTemp=-1;
    validado=false;
    $("#titleFormHeader").text("Nuevo Usuario");
    habilitar_rfc();
    $("#idUsuarioEntePublico").val("");
    $("#idUnidadAdmtvaUsuario").val("");
    $("#descUnidadAdmtvaUsuario").val("");
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
    $("#idPuesto").val("");
    $("#idPuesto").trigger('change');
    $("#idDomicilioEnte").val("");
    $("#idDomicilioEnte").trigger('change');
    $("#idUbicacionUnidadAdmtva").val("");
    $("#idUbicacionUnidadAdmtva").trigger('change');


    $("#idUnidadAdmtvaResguardante").val("");
    $("#descUnidadAdmtvaResguardante").val("");
    $("#idUsuarioTitular").val("");
    $("#idUsuarioTitular").trigger('change');
    $("#idUnidadAdminPptal").val("");
    $("#descUnidadAdminPptal").val("");
}


function getUnidadesAdmin(){

    if ($("#idEntePublico").val()!=""){
        $('#idBtnSearch').prop('disabled', false);
        $('#idNuevoUsuario').prop('disabled', false);
    }
    else {
        $('#idBtnSearch').prop('disabled', true);
        $('#idNuevoUsuario').prop('disabled', true);
    }

    obtienearbolPorInstitucion(function (data) {
        $("#idUnidadAdmtva").val(data.id);
        $("#descUnidadAdmtva").val(data.text);
        limpiarBuscarArbol();
        $('#modal_arbol_unidad').modal('hide');
    },$("#idEntePublico").val());
}

function getUnidadesAdminUsuario(){

    obtienearbolPorInstitucion2(function (data) {
        $("#idUnidadAdmtvaUsuario").val(data.id);
        $("#descUnidadAdmtvaUsuario").val(data.text);
        limpiarBuscarArbol2();
        $('#modal_arbol_unidad2').modal('hide');
    },$("#idEntePublicoUsuario").val());
}

function getUnidadesAdminResguarda(){

    obtienearbolPorInstitucion3(function (data) {
        $("#idUnidadAdmtvaResguardante").val(data.id);
        $("#descUnidadAdmtvaResguardante").val(data.text);
        limpiarBuscarArbol3();
        $('#modal_arbol_unidad3').modal('hide');

        getUsuarioUnidad($("#idUnidadAdmtvaResguardante").val());
    },$("#idEntePublicoResguardante").val());

}

function getUnidadesAdminPptal(){

    obtienearbol4(function (data) {
        $("#idUnidadAdminPptal").val(data.id);
        $("#descUnidadAdminPptal").val(data.text);
        limpiarBuscarArbol4();
        $('#modal_arbol_unidad4').modal('hide');
    });

}


function actualizarUbicaciones() {
    if ($("#idDomicilioEnte").val()!="")
        getUbicacionUnidad($("#idDomicilioEnte").val());
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

        if (opcion=="puesto") {
            if (idPuestoTemp != null && idPuestoTemp != -1) {
                $("#idPuesto").val(idPuestoTemp);
                $("#idPuesto").trigger('change');
            }
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

        if (opcion=="usuario_resguardo") {
            if (idUsuarioResguardoTemp != null && idUsuarioResguardoTemp != -1){
                $("#idUsuarioTitular").val(idUsuarioResguardoTemp);
                $("#idUsuarioTitular").trigger('change');
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

function getPuesto(idPadre,callback){
    if (idPadre!=null)
        fillSelectGetInterno('/api/catalogo/puesto-usuario/all', '#idPuesto','idPuestoUsuario','nombrePuestoUsuario',idPadre, callback,"puesto");
    else
        $('#idPuesto').empty();
}

function getDomicilioEntePublico(idPadre,callback){
    if (idPadre!=null)
        fillSelectGetInterno('/api/catalogo/domicilioentePublico/all', '#idDomicilioEnte','idDomicilio','nombreDomicilio',idPadre, callback, "domicilio");
    else
        $('#idDomicilioEnte').empty();
}

function getUbicacionUnidad(idPadre, callback){
    if (idPadre!=null)
        fillSelectGetInterno('/api/catalogo/ubicacionUnidadAministrativa/all', '#idUbicacionUnidadAdmtva','idUbicacionAdaministrativa','nombreUbicacion',idPadre, callback, "ubicacion");
    else
        $('#idUbicacionUnidadAdmtva').empty();
}


function obtieneUsuarios() {

    var groupColumn=2;

    var formulario={
        "isVigente":$("#idVigente").is(':checked'),
        "isHistorico":$("#idHistorico").is(':checked'),
        "isNoVigente":$("#idNoVigente").is(':checked')
    };

    formulario.catUsuario={"nombre": $("#nombreUsuario").val()};

    if ($("#idEntePublico").val()!="")
        formulario.entePublico={idEntePublico:$("#idEntePublico").val()};

    if ($("#idUnidadAdmtva").val()!="")
        formulario.unidadAdministrativa={idUnidadAdministrativa:$("#idUnidadAdmtva").val()};

    if ($("#idUnidadAdmtva").val()!="")
        formulario.entePublico={idEntePublico:$("#idEntePublico").val()};

    onLoading(null);
    const table = new DataTable('#tableUsuario', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/catalogo/usuario/general/tablaUsuario',
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

                if (total>0){
                    $("#idBoxUserNoREg").hide();
                    $("#idCardTablaUsuario").show();
                }
                else {
                    $("#idCardTablaUsuario").hide();
                    $("#idBoxUserNoREg").show();
                }

                offLoading(null);
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [{visible: false, targets: groupColumn},
            {
            "targets": 4,
            "width": "30",
            "className": "text-center",
        },
            {
                "targets": 5,
                "width": "30",
                "className": "text-center",
            }],
        drawCallback: function (settings) {
            var api = this.api();
            var rows = api.rows({ page: 'current' }).nodes();
            var last = null;

            api.column(groupColumn, { page: 'current' })
                .data()
                .each(function (group, i) {
                    if (last !== group) {
                        $(rows)
                            .eq(i)
                            .before('<tr class="group fw-bold"><td colspan="5" class="bg-primary text-white">' + group + '</td></tr>');

                        last = group;
                    }
                });
        },
        lengthChange: false,
        select: false
    });

}

function getUsuarioUnidad(idPadre, callback){
    fillSelectGetInterno('/api/catalogo/usuario/general/usuarioPorUnidad', '#idUsuarioTitular','idUsuario', 'nombreCompleto',idPadre!=''?idPadre:0, callback, "usuario_resguardo");
}

function deshabilitar_rfc() {
    $("#rfc").prop('readOnly', true);
    $("#idEntePublicoUsuario").prop('disabled', true);
    $("#idUnidadAdmtvaUsuario").prop('disabled', true);
    habilitarArbol2=false;
    $("#idBtnValidar").removeClass("btn-success");
    $("#idBtnValidar").addClass("btn-secondary");
    $("#idBtnValidar").prop('disabled', true);

    $("#btnGuardarUsuario").removeClass("btn-secondary");
    $("#btnGuardarUsuario").addClass("btn-primary");
    $("#btnGuardarUsuario").prop('disabled', false);


    btnGuardarUsuario
    habilitar_elementos();
}

function habilitar_rfc() {
    $("#rfc").prop('readOnly', false);
    $("#idEntePublicoUsuario").prop('disabled', false);
    $("#idUnidadAdmtvaUsuario").prop('disabled', false);
    habilitarArbol2=true;

    $("#idBtnValidar").removeClass("btn-secondary");
    $("#idBtnValidar").addClass("btn-success");
    $("#idBtnValidar").prop('disabled', false);

    $("#btnGuardarUsuario").removeClass("btn-primary");
    $("#btnGuardarUsuario").addClass("btn-secondary");
    $("#btnGuardarUsuario").prop('disabled', true);

    deshabilitar_elementos();
}

function deshabilitar_elementos() {

    $("#curp").prop('disabled', true);
    $("#nombre").prop('disabled', true);
    $("#segundoNombre").prop('disabled', true);
    $("#apellidoPaterno").prop('disabled', true);
    $("#apellidoMaterno").prop('disabled', true);
    $("#fechaNacimiento").prop('disabled', true);
    $("#numCelular").prop('disabled', true);
    $("#numTelefonoPersonal").prop('disabled', true);
    $("#correoPersonal").prop('disabled', true);
    habilitarArbol3=false;
    $("#correoInstitucional").prop('disabled', true);
    $("#telefonoInstitucional").prop('disabled', true);
    $("#extensionInstitucional").prop('disabled', true);
    $("#cveEstatus").prop('disabled', true);
    $("#idPuesto").prop('disabled', true);
    $("#idDomicilioEnte").prop('disabled', true);
    $("#idUbicacionUnidadAdmtva").prop('disabled', true);
    $("#idEntePublicoResguardante").prop('disabled', true);
    $("#idUsuarioTitular").prop('disabled', true);
    $("#btnGuardarUsuario").prop('disabled', true);
}


function habilitar_elementos() {

    $("#curp").prop('disabled', false);
    $("#nombre").prop('disabled', false);
    $("#segundoNombre").prop('disabled', false);
    $("#apellidoPaterno").prop('disabled', false);
    $("#apellidoMaterno").prop('disabled', false);
    $("#cveSexoFemenino").prop("checked", true);
    $("#fechaNacimiento").prop('disabled', false);
    $("#numCelular").prop('disabled', false);
    $("#numTelefonoPersonal").prop('disabled', false);
    $("#correoPersonal").prop('disabled', false);
    //$("#idEntePublicoUsuario").prop('disabled', false);
    //$("#idUnidadAdmtvaUsuario").prop('disabled', false);
    habilitarArbol3=true;
    $("#correoInstitucional").prop('disabled', false);
    $("#telefonoInstitucional").prop('disabled', false);
    $("#extensionInstitucional").prop('disabled', false);
    $("#cveEstatus").prop('disabled', false);
    $("#idPuesto").prop('disabled', false);
    $("#idDomicilioEnte").prop('disabled', false);
    $("#idUbicacionUnidadAdmtva").prop('disabled', false);
    $("#idEntePublicoResguardante").prop('disabled', false);
    $("#idUsuarioTitular").prop('disabled', false);
    $("#btnGuardarUsuario").prop('disabled', false);
}

const guardarUsuarioAsync = async () =>{


    const options={
        method: "POST",
        body: JSON.stringify({
            idUsuarioEntePublico: $("#idUsuarioEntePublico").val(),
            entePublico:{idEntePublico:$("#idEntePublicoUsuario").val()},
            unidadAdministrativa:{idUnidadAdministrativa:$("#idUnidadAdmtvaUsuario").val()},
            catUsuario:{
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
                correoPersonal: $("#correoPersonal").val()
            },
            correoInstitucional: $("#correoInstitucional").val(),
            numTelefonoInstitucional: $("#telefonoInstitucional").val(),
            numeroExtensionInstitucional: $("#extensionInstitucional").val(),
            cveEstatus: $("#cveEstatus").val(),
            puestoUsuario:{idPuestoUsuario:$("#idPuesto").val()},
            ubicacionUnidadAdmin:{idUbicacionAdaministrativa:$("#idUbicacionUnidadAdmtva").val()},
            entePublicoResguardante:{idEntePublico:$("#idEntePublicoResguardante").val()},
            unidadAdministrativaResguardante:{idUnidadAdministrativa:$("#idUnidadAdmtvaResguardante").val()},
            catUsuarioResguardante:{idUsuario:$("#idUsuarioTitular").val()},
            unidadAdministrativaPptal:{idUnidadAdministrativa:$("#idUnidadAdminPptal").val()}
        }),
        headers: headers
    };

    try {
        onLoading("dlgModalFormNuevoUsuario");
        const res = await fetch(contexto+'/api/catalogo/usuario/general/guardarUsuario', options);
        const respJSON = await res.json();

        if (res.ok) {

            $("#idUsuarioEntePublico").val(respJSON.idUsuarioEntePublico);
            $("#titleFormHeader").text("Editar Usuario");

            Toast.fire({
                icon: "success",
                title: "El Usuario se guardo correctamente"
            });

            obtieneUsuarios();

            offLoading("dlgModalFormNuevoUsuario");
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

            validado=true;

            var catUsuarioEntePublico=response;

            $("#titleFormHeader").text("Editar Usuario")
            $("#rfc").val(catUsuarioEntePublico.catUsuario.rfc);

            deshabilitar_rfc()

            $("#idUsuarioEntePublico").val(catUsuarioEntePublico.idUsuarioEntePublico);

            $("#nombre").val(catUsuarioEntePublico.catUsuario.nombre);
            $("#segundoNombre").val(catUsuarioEntePublico.catUsuario.segundoNombre);
            $("#apellidoPaterno").val(catUsuarioEntePublico.catUsuario.apellidoPaterno);
            $("#apellidoMaterno").val(catUsuarioEntePublico.catUsuario.apellidoMaterno);
            $("#curp").val(catUsuarioEntePublico.catUsuario.curp);


            if (catUsuarioEntePublico.catUsuario.cveSexo==null || catUsuarioEntePublico.catUsuario.cveSexo=="F")
                $("#cveSexoFemenino").prop("checked", true);
            else
                $("#cveSexoMasculino").prop("checked", true);

            if (catUsuarioEntePublico.catUsuario.fechaNacimientoText!=null) {
                var date1 = catUsuarioEntePublico.catUsuario.fechaNacimientoText.split("/");
                $("#fechaNacimiento").val(date1[2] + "-" + date1[1] + "-" + date1[0]);
            }

            $("#numCelular").val(catUsuarioEntePublico.catUsuario.numCelular);
            $("#numTelefonoPersonal").val(catUsuarioEntePublico.catUsuario.numTelefonoPersonal);
            $("#correoPersonal").val(catUsuarioEntePublico.catUsuario.correoPersonal);
            $("#correoInstitucional").val(catUsuarioEntePublico.correoInstitucional);
            $("#telefonoInstitucional").val(catUsuarioEntePublico.numTelefonoInstitucional);
            $("#extensionInstitucional").val(catUsuarioEntePublico.numeroExtensionInstitucional);

            if (catUsuarioEntePublico.cveEstatus!=null)
                $("#cveEstatus").val(catUsuarioEntePublico.cveEstatus);

            if (catUsuarioEntePublico.puestoUsuario!=null) {
                idPuestoTemp=catUsuarioEntePublico.puestoUsuario.idPuestoUsuario
            }

            if (catUsuarioEntePublico.ubicacionUnidadAdmin!=null && catUsuarioEntePublico.ubicacionUnidadAdmin.idUbicacionAdaministrativa!=null) {
                idUbicacionTemp=catUsuarioEntePublico.ubicacionUnidadAdmin.idUbicacionAdaministrativa;

                if (catUsuarioEntePublico.ubicacionUnidadAdmin.domicilioEntePublico!=null && catUsuarioEntePublico.ubicacionUnidadAdmin.domicilioEntePublico.idDomicilioEntePublico!=null)
                    idDomicilioTemp = catUsuarioEntePublico.ubicacionUnidadAdmin.domicilioEntePublico.idDomicilioEntePublico
            }

            if (catUsuarioEntePublico.entePublico!=null){
                $("#idEntePublicoUsuario").val(catUsuarioEntePublico.entePublico.idEntePublico);
                $("#idEntePublicoUsuario").trigger('change');
            }

            if (catUsuarioEntePublico.unidadAdministrativa!=null){
                $("#idUnidadAdmtvaUsuario").val(catUsuarioEntePublico.unidadAdministrativa.idUnidadAdministrativa);
                $("#descUnidadAdmtvaUsuario").val(catUsuarioEntePublico.unidadAdministrativa.nombreUnidadAdministrativa);
            }

            if (catUsuarioEntePublico.entePublicoResguardante!=null) {
                $("#idEntePublicoResguardante").val(catUsuarioEntePublico.entePublicoResguardante.idEntePublico);
                $("#idEntePublicoResguardante").trigger('change');
                getUnidadesAdminResguarda();
            }else{
                $("#idEntePublicoResguardante").val("");
                $("#idEntePublicoResguardante").trigger('change');
                getUnidadesAdminResguarda();
            }


            if (catUsuarioEntePublico.unidadAdministrativaResguardante!=null) {
                if (catUsuarioEntePublico.catUsuarioResguardante!=null)
                    idUsuarioResguardoTemp=catUsuarioEntePublico.catUsuarioResguardante.idUsuario;
                $("#idUnidadAdmtvaResguardante").val(catUsuarioEntePublico.unidadAdministrativaResguardante.idUnidadAdministrativa);
                $("#descUnidadAdmtvaResguardante").val(catUsuarioEntePublico.unidadAdministrativaResguardante.nombreUnidadAdministrativa);
                getUsuarioUnidad($("#idUnidadAdmtvaResguardante").val());
            }

            if (catUsuarioEntePublico.unidadAdministrativaPptal!=null) {
                $("#idUnidadAdminPptal").val(catUsuarioEntePublico.unidadAdministrativaPptal.idUnidadAdministrativa);
                $("#descUnidadAdminPptal").val(catUsuarioEntePublico.unidadAdministrativaPptal.nombreUnidadAdministrativa);
            }


            $('#preloader').modal('hide');
            $('#dlgModalFormNuevoUsuario').modal('show');

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
        closeOnCancel: false,
        closeOnConfirm: true,
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


function obtieneUsuariosSinDependencia() {
    $('#dlgModalUsuarioSinEnte').modal('show');
    //onLoading("dlgModalUsuarioSinEnte");
    const tableSinDependencia = new DataTable('#tableUsuarioSinDependencia', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/catalogo/usuario/general/tablaUsuarioSinDependencia',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            }/*,
            complete: function (data) {
               offLoading("dlgModalUsuarioSinEnte");
            }*/
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        lengthChange: false,
        select: false
    });

}

function agregarUsuario(rfc){
    limpiarFormUsuario();
    $("#rfc").val(rfc);
    $("#idEntePublicoUsuario").val($("#idEntePublico").val());
    $("#idEntePublicoUsuario").trigger('change');
    $('#titleFormHeader').text("Nuevo Usuario");
    habilitar_rfc();
    $("#nav-data-general-tab-user").tab('show');
    //$('#dlgModalUsuarioSinEnte').modal('hide');
    $('#dlgModalFormNuevoUsuario').modal('show');

}