const headers = {
    "X-CSRF-Token": $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let idCategoriaTemp = "";

let tableUsuarioSearch = null;

let ocultar = false;

let table=null;

var coloresText = ['text-primary', 'text-success', 'text-warning', 'text-info', 'text-dark', 'text-light'];
var colores=['#0d6efd','#ffc107','#198754','#dc3545', '#6f42c1','#0dcaf0', '#BC955C','#235B4E','#1F618D','#fd7e14','#6F7271','#27AE60','#dc3545','#d63384','#6610f2','#198754','#0dcaf0','#2E4053'];


let chartCorreoEstatus = null;
let chartCorreoPorTipo = null;

let tableCorreo;
$(document).ready(function () {

    //document.addEventListener("DOMContentLoaded",obtienCorreosElectronicos);
    pageInput.addEventListener("change",paginate);

    $('.tree').treegrid();
    $('[data-bs-toggle="tooltip"]').tooltip();


    if ($("#rolEntePublico").val() == "1") {
        $("#correoElectronico").prop('disabled', true);
        $("#correoElectronicoEnvio").prop('disabled', true);
        $("#correoElectronicoAlias").prop('disabled', true);
    } else {
        $("#correoElectronico").prop('disabled', false);
        $("#correoElectronicoEnvio").prop('disabled', false);
        $("#correoElectronicoAlias").prop('disabled', false);
    }

    jQuery.event.special.mousewheel = {
        setup: function (_, ns, handle) {
            this.addEventListener("mousewheel", handle, {passive: true});
        }
    };

    inicializarDependencias();

    $('.select2Pe').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
    });

    $('.select2Pe')
        .parent('div')
        .children('span')
        .children('span')
        .children('span')
        .css('height', ' calc(3.5rem + 2px)');
    $('.select2Pe')
        .parent('div')
        .children('span')
        .children('span')
        .children('span')
        .children('span')
        .css('margin-top', '18px');
    $('.select2Pe')
        .parent('div')
        .find('label')
        .css('z-index', '1');

    $('.select2me').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormCorreoElectronicoForm")
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

    if ($("#idEntePublicoSession").val() != "") {

        obtienearbolPorInstitucion(function (data) {
            $("#idUnidadAdministrativa").val(data.id);
            $("#descUnidadAdministrativa").val(data.text);
            limpiarBuscarArbol();
            $('#modal_arbol_unidad').modal('hide');
            $("#descUnidadAdministrativa").removeClass("is-valid");
            $("#descUnidadAdministrativa").removeClass("is-invalid");
        }, $("#idEntePublicoSession").val());

    }

    $('#idEntePublicoSearch').on('change', function () {
        if ($("#idEntePublicoSearch").val() != ""){
            $('#idNuevoCorreo').prop('disabled', false);
            $("#descargaPersonalizada").prop('href',contexto+"/api/catalogo/tic/exportReporteCorreos/"+$("#idEntePublicoSearch").val());
            $("#descargaPersonalizada").show();
        }
        else {
            $('#idNuevoCorreo').prop('disabled', true);
            $("#descargaPersonalizada").hide();
        }

        obtienearbolPorInstitucion(function (data) {

            $("#descargaPersonalizada").prop('href',contexto+"/api/catalogo/tic/exportReporteCorreosUnidad/"+$("#idEntePublicoSearch").val()+'/'+data.id);
            obtienCorreosElectronicos($("#idEntePublicoSearch").val(), data.id,null);
            obtenTablaCorreoGroup($("#idEntePublicoSearch").val(),data.id);
            $("#descargaPersonalizada").show();
            $("#idUnidadAdministrativaSearch").val(data.id);
            $("#descUnidadAdministrativaSearch").val(data.text);
            limpiarBuscarArbol();
            $('#modal_arbol_unidad').modal('hide');
            $("#descUnidadAdministrativaSearch").removeClass("is-valid");
            $("#descUnidadAdministrativaSearch").removeClass("is-invalid");


        }, $("#idEntePublicoSearch").val());

        getConcentradoTipoCorreo($("#idEntePublicoSearch").val());
        if ($("#isAdministradorSigtic").val() == "true") {
            getConcentradoTipoCorreoAll();
        }

        obtienCorreosElectronicos($("#idEntePublicoSearch").val(), $("#idUnidadAdministrativaSearch").val(), null);
        obtenTablaCorreoGroup($("#idEntePublicoSearch").val(),null);
    });

    $("#btnOpenArbolSearch").click(function () {
        $('#modal_arbol_unidad').modal('show');
    });

    $("#txtBuscar").keyup(function () {
        busqueda($(this).val())
    });

    $("#btnLimpiarOpenArbolSearch").click(function () {
        obtienCorreosElectronicos($("#idEntePublicoSearch").val(), null, $("#correoElectronicoSearch").val());
        obtenTablaCorreoGroup($("#idEntePublicoSearch").val(),null);
        $("#descargaPersonalizada").prop('href',contexto+"/api/catalogo/tic/exportReporteCorreos/"+$("#idEntePublicoSearch").val());
        $("#descargaPersonalizada").show();
        $("#idUnidadAdministrativaSearch").val("");
        $("#descUnidadAdministrativaSearch").val("");
    });


    $("#idRefresh").click(function () {
        obtienCorreosElectronicos($("#idEntePublicoSearch").val(), $("#idUnidadAdministrativaSearch").val(), null);
        obtenTablaCorreoGroup($("#idEntePublicoSearch").val(),$("#idUnidadAdministrativaSearch").val());
        seleccionaHoja(2);
    });

    $('#idEntePublico').on('change', function () {
        $("#idUnidadAdministrativa").val("");
        $("#descUnidadAdministrativa").val("");

        obtenerCategoria($("#idEntePublico").val());

        obtienearbolPorInstitucion2(function (data) {

            $("#idUnidadAdministrativa").val(data.id);
            $("#descUnidadAdministrativa").val(data.text);
            limpiarBuscarArbol2();
            $('#modal_arbol_unidad2').modal('hide');
            $("#descUnidadAdministrativa").removeClass("is-valid");
            $("#descUnidadAdministrativa").removeClass("is-invalid");
        }, $("#idEntePublico").val());

    });

    $("#btnOpenArbol").click(function () {
        $('#modal_arbol_unidad2').modal('show');
    });

    $("#txtBuscar2").keyup(function () {
        busqueda2($(this).val())
    });

    $("#btnLimpiarOpenArbol").click(function () {
        $("#idUnidadAdministrativa").val("");
        $("#descUnidadAdministrativa").val("");
    });

    $("#idNuevoCorreo").click(function () {
        limpiarFormCorreo();
        $('#dlgModalFormCorreoElectronicoForm').modal('show');
    });

    $("#idBtnSearchUser").click(function () {
        limpiarFormularioSearch();
        $('#dlgModalFormCorreoElectronicoForm').modal('hide');
        $('#dlgModalFormSearchCorreoElectronico').modal('show');

    });

    $("#idBtnEditUser").click(function () {
        limpiarFormCatUsuario();
        $("#titleFormHeaderCatUsuario").text("Editar Usuario");
        if ($("#idTitularCorreoElectronico").val() != "")
            editarUsuario($("#idTitularCorreoElectronico").val());
        else {
            Toast.fire({
                icon: 'warning',
                title: "No hay usuario asignado"
            });
        }

    });

    $("#btnCerrarSearch").click(function () {
        limpiarFormularioSearch();
        $('#dlgModalFormSearchCorreoElectronico').modal('hide');
        $('#dlgModalFormCorreoElectronicoForm').modal('show');

    });


    $("#formUsuarioSearch").validate({
        ignore: "",
        rules: {
            rfcSearch: {minlength: 4, maxlength: 13},
            curpSearch: {minlength: 4, maxlength: 18},
            nombreSearch: {minlength: 3},
            segundoNombreSearch: {minlength: 3},
            apellitoPaternoSearch: {minlength: 3},
            apellidoMaternoSearch: {minlength: 3}
        },
        messages: {
            rfcSearch: "RFC mínimo 4 caracteres y máximo 13",
            curpSearch: "CURP mínimo 4 caracteres y máximo 18",
            nombreSearch: "Nombre mínimo 3 caracteres",
            segundoNombreSearch: "Segundo Nombre mínimo 3 caracteres",
            apellitoPaternoSearch: "Apellido Paterno mínimo 3 caracteres",
            apellidoMaternoSearch: "Apellido Materno mínimo 3 caracteres"
        },
        errorElement: "em",
        errorPlacement: function (error, element) {
            error.insertAfter(element);
            error.addClass("invalid-feedback");
        },
        highlight: function (element, errorClass) {
            $(element).addClass("is-invalid").removeClass("is-valid");
        },
        unhighlight: function (element, errorClass) {
            $(element).addClass("is-valid").removeClass("is-invalid");
        },
        submitHandler: function (form, e) {
            e.preventDefault();
        }
    });

    $("#formUsuarioSearch").submit(function (e) {
        e.preventDefault();
        $("#idCardTablaUsuarioSearch").hide();
        $("#rfcSearch").val($("#rfcSearch").val().trim());
        $("#curpSearch").val($("#curpSearch").val().trim());
        $("#nombreSearch").val($("#nombreSearch").val().trim());
        $("#segundoNombreSearch").val($("#segundoNombreSearch").val().trim());
        $("#apellitoPaternoSearch").val($("#apellitoPaternoSearch").val().trim());
        $("#apellidoMaternoSearch").val($("#apellidoMaternoSearch").val().trim());

        if ($('#formUsuarioSearch').valid()) {
            if ($("#rfcSearch").val().trim() == "" && $("#nombreSearch").val().trim() == "" && $("#segundoNombreSearch").val().trim() == "" && $("#curpSearch").val().trim() == "" && $("#apellitoPaternoSearch").val().trim() == ""
                && $("#apellidoMaternoSearch").val().trim() == "") {
                Swal.fire({
                    title: 'Mensaje',
                    text: "La búsqueda requiere datos en al menos uno de los campos",
                    icon: 'warning',
                });
                return false;
            } else {
                obtenUsuariosSearch();
                return true;
            }

        } else return false;
    });

    obtenerServidorCorreoElectronico()
    obtenerTipoCorreoElectronico();
    obtenerEstatusCorreoElectronico()

    $("#idFormCorreo").validate({
        ignore: "",
        rules: {
            rfc: {required: true, minlength: 10, maxlength: 13},
            curp: {required: true, minlength: 18, maxlength: 18},
            idServidorCorreoElectronico: "required",
            tipoCorreo: "required",
            idEstatusCorreoElectronico: "required",
            cveEstatus: "required",
            fechaAltaCorreoElectronico: "required",
            fechaBajaCorreoElectronico: {
                required: function () {
                    if ($("#cveEstatus").val() == "HIS") {
                        return true;
                    } else {
                        $("#fechaBajaCorreoElectronico").removeClass("is-invalid")
                        $("#fechaBajaCorreoElectronico").removeClass("is-valid")
                        $("#fechaBajaCorreoElectronico").addClass("is-invalid");
                        return false;
                    }
                }
            },
            idEntePublico: "required",
            idUnidadAdministrativa: "required",
            correoElectronico: "required",
            correoElectronicoEnvio: "required",
            puesto: "required",
            idCategoria: "required"
        },
        messages: {
            rfc: "RFC Valido requerido",
            curp: "CURP Valido requerido",
            idServidorCorreoElectronico: "Servidor Requerido",
            tipoCorreo: "Tipo de Correo Requerido",
            idEstatusCorreoElectronico: "Estatus del Correo Requerido",
            cveEstatus: "Estatus Requerido",
            fechaAltaCorreoElectronico: "Fecha de Alta Requerido",
            fechaBajaCorreoElectronico: "Fecha de Baja Requerido",
            idEntePublico: "Ente Público Requerido",
            idUnidadAdministrativa: "Unidad Administrativa Requerida",
            correoElectronico: "Correo Requerido",
            correoElectronicoEnvio: "Correo Requerido",
            puesto: "Cargo Requerido",
            idCategoria: "Categoría Requerido"
        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            //error.insertAfter(element);
            error.addClass("invalid-feedback");
            element.closest('.form-floating').append(error);
        },
        highlight: function (element, errorClass) {
            $(element).addClass("is-invalid").removeClass("is-valid");
        },
        unhighlight: function (element, errorClass) {
            $(element).addClass("is-valid").removeClass("is-invalid");
        },
        submitHandler: function (form, e) {
            e.preventDefault();
        }
    })

    $("#idFormCorreo").submit(function (e) {
        e.preventDefault();
        if ($('#idFormCorreo').valid()) {
            guardarCorreoElectronico();
            return true;
        }
    });

    $("#idBtnNewUser").click(function () {
        limpiarFormCatUsuario();
        $('#dlgModalFormNuevoCatUsuario').modal('show');
    });

    $("#formCatUsuario").validate({
        ignore: "",
        rules: {
            rfcCat: {required: true, minlength: 10, maxlength: 13},
            curpCat: {required: true, minlength: 18, maxlength: 18},
            nombreCat: "required",
            apellidoPaternoCat: {
                required: function () {
                    if ($("#apellidoPaternoCat").val().trim() == "" && $("#apellidoMaternoCat").val().trim() == "")
                        return true;
                    else {
                        $("#apellidoPaternoCat").removeClass("is-invalid")
                        $("#apellidoPaternoCat").addClass("is-valid");
                        $("#apellidoMaternoCat").removeClass("is-invalid")
                        $("#apellidoMaternoCat").addClass("is-valid");
                        return false;
                    }
                }
            },
            apellidoMaternoCat: {
                required: function () {
                    if ($("#apellidoPaternoCat").val().trim() == "" && $("#apellidoMaternoCat").val().trim() == "")
                        return true;
                    else {
                        return false;
                        $("#apellidoPaternoCat").removeClass("is-invalid")
                        $("#apellidoPaternoCat").addClass("is-valid");
                        $("#apellidoMaternoCat").removeClass("is-invalid")
                        $("#apellidoMaternoCat").addClass("is-valid");
                    }
                }
            },
            fechaNacimientoCat: "required",
            numCelularCat: {minlength: 10, maxlength: 10},
            numTelefonoPersonalCat: {minlength: 10, maxlength: 10},
            correoPersonalCat: {email: true}
        },
        messages: {
            rfcCat: "RFC Valido requerido",
            curpCat: "CURP Valido requerido",
            nombreCat: "Nombre Requerido",
            apellidoPaternoCat: "Apellido Paterno o Materno Requerido",
            apellidoMaternoCat: "Apellido Paterno o Materno Requerido",
            fechaNacimientoCat: "Fecha de nacimiento requerida",
            numCelularCat: "Número de Celular Invalido",
            numTelefonoPersonalCat: "Número de Teléfono Invalido",
            correoPersonalCat: "Correo Invalido"
        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            //error.insertAfter(element);
            error.addClass("invalid-feedback");
            element.closest('.form-floating').append(error);
        },
        highlight: function (element, errorClass) {
            $(element).addClass("is-invalid").removeClass("is-valid");
        },
        unhighlight: function (element, errorClass) {
            $(element).addClass("is-valid").removeClass("is-invalid");
        },
        submitHandler: function (form, e) {
            e.preventDefault();
        }
    })

    $("#formCatUsuario").submit(function (e) {
        e.preventDefault();
        if ($('#formCatUsuario').valid()) {
            guardarCatUsuario();
            return true;
        }
    });

    $("#idFormCorreoSearch").validate({
        ignore: "",
        rules: {
            correoElectronicoSearch: {minlength: 4}
        },
        messages: {
            correoElectronicoSearch: "Falta de datos"
        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            //error.insertAfter(element);
            error.addClass("invalid-feedback");
            element.closest('.form-floating').append(error);
        },
        highlight: function (element, errorClass) {
            $(element).addClass("is-invalid").removeClass("is-valid");
        },
        unhighlight: function (element, errorClass) {
            $(element).addClass("is-valid").removeClass("is-invalid");
        },
        submitHandler: function (form, e) {
            e.preventDefault();
        }
    })

    $("#idFormCorreoSearch").submit(function (e) {
        e.preventDefault();
        if ($('#idFormCorreoSearch').valid()) {
            obtienCorreosElectronicos($("#idEntePublicoSearch").val(), $("#idUnidadAdministrativaSearch").val(), $("#correoElectronicoSearch").val());
            seleccionaHoja(2);
            $("#correoElectronicoSearch").val("");
            return true;
        }
    });

});


/*--------------------------------------------------------*/

function seleccionaHoja(hoja){

    if (hoja==1){
        $("#hoja1").addClass("active");
        $("#hoja2").removeClass("active");
        $("#idHoja1").show();
        $("#idHoja2").hide();
    }else
        if (hoja==2){
            $("#hoja1").removeClass("active");
            $("#hoja2").addClass("active");
            $("#idHoja1").hide();
            $("#idHoja2").show();

        }
}

function limpiarFormCatUsuario() {
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

    $("#rfcCat").removeClass("is-valid");
    $("#rfcCat").removeClass("is-invalid");
    $("#curpCat").removeClass("is-valid");
    $("#curpCat").removeClass("is-invalid");
    $("#nombreCat").removeClass("is-valid");
    $("#nombreCat").removeClass("is-invalid");
    $("#apellidoPaternoCat").removeClass("is-valid");
    $("#apellidoPaternoCat").removeClass("is-invalid");
    $("#apellidoMaternoCat").removeClass("is-valid");
    $("#apellidoMaternoCat").removeClass("is-invalid");
    $("#fechaNacimientoCat").removeClass("is-valid");
    $("#fechaNacimientoCat").removeClass("is-invalid");

    $("#numCelularCat").removeClass("is-valid");
    $("#numCelularCat").removeClass("is-invalid");
    $("#numTelefonoPersonalCat").removeClass("is-valid");
    $("#numTelefonoPersonalCat").removeClass("is-invalid");
    $("#correoPersonalCat").removeClass("is-valid");
    $("#correoPersonalCat").removeClass("is-invalid");
}

const inicializarDependencias = async () => {
    const options = {
        method: "POST",
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/entePublico/lista', options);
        const respJSON = await res.json();

        llenarCombo('#idEntePublicoSearch', 'idEntePublico', 'nombreEntePublicoConCve', respJSON);
        llenarCombo('#idEntePublico', 'idEntePublico', 'nombreEntePublicoConCve', respJSON);
        $("#idEntePublicoSearch").val($("#idEntePublicoSession").val());
        $("#descargaPersonalizada").prop('href',contexto+"/api/catalogo/tic/exportReporteCorreos/"+$("#idEntePublicoSession").val());
        $("#descargaPersonalizada").show();
        $("#idEntePublicoSearch").trigger('change');
        $("#idEntePublico").val($("#idEntePublicoSession").val());
        $("#idEntePublico").trigger('change');

        offLoading(null);
    } catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
};


function obtienCorreosElectronicos(idEntePublicoSearch, idUnidadAdministrativaSearch, correoSearch) {

    tableCorreo = new DataTable('#tableCorreoElectronicos', {
        responsive: true,
        //sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: false,
        ajax: {
            url: contexto + '/api/catalogo/tic/tablaCorreosElectronicos',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function (xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    idEntePublico: idEntePublicoSearch,
                    idUnidadAdministrativa: idUnidadAdministrativaSearch,
                    correoSearch: correoSearch
                }));
            }
        },
        bProcessing: true,
        bServerSide: false,
        language: {
            "url": contexto + '/include/datatables/spanish.json'
        },
        lengthChange: false,
        select: false
    });

    /*
    tableCorreo.on('initComplete', function () {
        $("#pageInput").trigger("change");
    });
    */

  /*  tableCorreo.on('draw.dt', function () {
        $("#pageInput").trigger("change");
    })*/

    tableCorreo.on("page", () =>{
        if(pageInput.valueAsNumber !== tableCorreo.page.info().page+1)
            pageInput.valueAsNumber = tableCorreo.page.info().page+1;
    });

}

function paginate(e){
    if(e.target.checkValidity()){
        tableCorreo.page(e.target.valueAsNumber-1).draw('page');
    }
}

function limpiarFormularioSearch() {
    $("#rfcSearch").val("");
    $("#curpSearch").val("");
    $("#nombreSearch").val("");
    $("#segundoNombreSearch").val("");
    $("#apellitoPaternoSearch").val("");
    $("#apellidoMaternoSearch").val("")
    $("#rfcSearch").removeClass("is-valid");
    $("#rfcSearch").removeClass("is-invalid");
    $("#curpSearch").removeClass("is-valid");
    $("#curpSearch").removeClass("is-invalid");
    $("#nombreSearch").removeClass("is-valid");
    $("#nombreSearch").removeClass("is-invalid");
    $("#segundoNombreSearch").removeClass("is-valid");
    $("#segundoNombreSearch").removeClass("is-invalid");
    $("#apellitoPaternoSearch").removeClass("is-valid");
    $("#apellitoPaternoSearch").removeClass("is-invalid");
    $("#apellidoMaternoSearch").removeClass("is-valid");
    $("#apellidoMaternoSearch").removeClass("is-invalid");

    $("#idCardUsuSearch").hide();
    if (tableUsuarioSearch != null)
        tableUsuarioSearch.clear();
}

function obtenUsuariosSearch() {

    $("#idCardUsuSearch").show();

    var parametro = {
        rfc: $("#rfcSearch").val().trim(),
        curp: $("#curpSearch").val().trim(),
        nombre: $("#nombreSearch").val().trim(),
        segundoNombre: $("#segundoNombreSearch").val().trim(),
        apellidoPaterno: $("#apellitoPaternoSearch").val().trim(),
        apellidoMaterno: $("#apellidoMaternoSearch").val().trim(),
        isVigente: true,
        isHistorico: true,
        isNoVigente: false
    };

    if (tableUsuarioSearch != null)
        tableUsuarioSearch.clear();

    tableUsuarioSearch = new DataTable('#tableUsuarioSearch', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering: false,
        bSort: false,
        ajax: {
            url: contexto + '/api/catalogoCatUsuario/tablaUsuarioSearch',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function (xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d,
                    parametro

                    /*{
                    entePublico:{idEntePublico:$("#idEntePublico").val()},
                    unidadAdministrativa: {idUnidadAdministrativa: $("#idUnidadAdmtva").val()}
                }*/));
            }
        },
        bProcessing: true,
        bServerSide: false,
        language: {
            "url": contexto + '/include/datatables/spanish.json',
        },
        /* columnDefs: [
             {
                 "targets": 0,
                 "width": "200px",
                 "className": "text-left",
             },
             /!*{
                 "targets": 1,
                 "width": "250px",
                 "className": "text-center",
             },*!/
             {
                 "targets": 2,
                 "width": "120px",
                 "className": "text-center",
             },
             {
                 "targets": 3,
                 "width": "40px",
                 "className": "text-center",
             }],*/
        lengthChange: false,
        select: false
    });

}

const obtenerServidorCorreoElectronico = async () => {

    const options = {
        method: "GET",
        headers: headers
    };

    onLoading(null);
    const res = await fetch(contexto + "/api/catalogo/tic/getServidorCorreoElectronicoAll", options);
    const respJSON = await res.json();

    if (res.ok) {
        offLoading(null);
        llenarCombo('#idServidorCorreoElectronico', 'idServidorCorreoElectronico', 'nombreServidorCorreoElectronico', respJSON);
    } else {

        Toast.fire({
            icon: "error",
            title: respJSON.message
        });
        offLoading(null);
    }
};

const obtenerTipoCorreoElectronico = async () => {

    const options = {
        method: "GET",
        headers: headers
    };

    onLoading(null);
    const res = await fetch(contexto + "/api/catalogo/tic/getTipoCorreoElectronico", options);
    const respJSON = await res.json();

    if (res.ok) {
        offLoading(null);
        llenarCombo('#tipoCorreo', 'cveTipoCorreoElectronico', 'nombreTipoCorreoElectronico', respJSON);
        /* llenarCombo('#tipoCorreoBasesEditar','cveTipoCorreoElectronico','nombreTipoCorreoElectronico', respJSON);*/
    } else {

        Toast.fire({
            icon: "error",
            title: respJSON.message
        });
        offLoading(null);
    }

};

const obtenerEstatusCorreoElectronico = async () => {

    const options = {
        method: "GET",
        headers: headers
    };

    onLoading(null);
    const res = await fetch(contexto + "/api/catalogo/getEstatusCorreo", options);
    const respJSON = await res.json();

    if (res.ok) {
        offLoading(null);
        llenarCombo('#idEstatusCorreoElectronico', 'cveEstatusCorreoElectronico', 'nombreEstatusCorreoElectronico', respJSON);
    } else {

        Toast.fire({
            icon: "error",
            title: respJSON.message
        });
        offLoading(null);
    }

};


const obtenerCategoria = async (idEntePublico) => {

    if (idEntePublico == null || idEntePublico == "")
        $("#idCategoria").empty();
    else {

        const options = {
            method: "GET",
            headers: headers
        };

        onLoading(null);
        const res = await fetch(contexto + "/api/catalogo/tic/getCategoria/" + idEntePublico, options);
        const respJSON = await res.json();

        if (res.ok) {
            offLoading(null);
            llenarCombo('#idCategoria', 'idCategoriaUsuario', 'nombreCategoriaUsuario', respJSON);
            if (idCategoriaTemp != null && idCategoriaTemp != "") {
                $("#idCategoria").val(idCategoriaTemp);
            }

        } else {

            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading(null);
        }
    }

};


function seleccionarUsuario(idUsuario) {

    $('#preloader').modal('show');
    $.ajax({
        url: contexto + '/api/catalogoCatUsuario/getUsuario/' + idUsuario,
        type: 'GET',
        dataType: 'JSON',
        contentType: 'application/json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function (response) {

            $('#dlgModalFormSearchCorreoElectronico').modal('hide');
            var catUsuario = response;

            console.log(catUsuario);

            $("#idTitularCorreoElectronico").val(catUsuario.idUsuario);
            $("#nombreTitularText").text(catUsuario.nombreCompleto);
            $("#rfc").val(catUsuario.rfc);
            $("#curp").val(catUsuario.curp);
            $("#correoPersonal").val(catUsuario.correoPersonal);
            $("#celular").val(catUsuario.numCelular);
            $('#preloader').modal('hide');
            $('#dlgModalFormCorreoElectronicoForm').modal('show');
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


const guardarCorreoElectronicoAsync = async () => {

    const options = {
        method: "POST",
        body: JSON.stringify({
            idCorreoElectronico: $("#idCorreoElectronico").val(),
            catServidorCorreoElectronico: {idServidorCorreoElectronico: $("#idServidorCorreoElectronico").val()},
            catTipoCorreoElectronico: {cveTipoCorreoElectronico: $("#tipoCorreo").val()},
            catEstatusCorreoElectronico: {cveEstatusCorreoElectronico: $("#idEstatusCorreoElectronico").val()},
            cveEstatus: $("#cveEstatus").val(),
            fechaAltaCorreoElectronico: $("#fechaAltaCorreoElectronico").val(),
            fechaBajaCorreoElectronico: $("#fechaBajaCorreoElectronico").val(),
            entePublico: {idEntePublico: $("#idEntePublico").val()},
            unidadAdministrativa: {idUnidadAdministrativa: $("#idUnidadAdministrativa").val()},
            correoElectronico: $("#correoElectronico").val(),
            correoElectronicoEnvio: $("#correoElectronicoEnvio").val(),
            correoElectronicoAlias: $("#correoElectronicoAlias").val(),
            catUsuario: {idUsuario: $("#idTitularCorreoElectronico").val()},
            cargoTitularCorreo: $("#puesto").val(),
            catCategoria: {idCategoriaUsuario: $("#idCategoria").val()}
        }),
        headers: headers
    };

    try {
        onLoading("dlgModalFormCorreoElectronicoForm");
        const res = await fetch(contexto + '/api/catalogo/tic/saveCorreoElectronico', options);
        const respJSON = await res.json();
        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Los datos del correo electronico se guardaron correctamente"
            });

            $("#idEntePublicoSearch").val($("#idEntePublico").val());
            $("#idEntePublicoSearch").trigger('change');
            offLoading("dlgModalFormCorreoElectronicoForm");
            $('#dlgModalFormCorreoElectronicoForm').modal('hide');

            getConcentradoTipoCorreo($("#idEntePublicoSearch").val());
            if ($("#isAdministradorSigtic").val() == "true") {
                getConcentradoTipoCorreoAll();
            }

        } else {
            offLoading("dlgModalFormCorreoElectronicoForm");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }

    } catch (e) {
        offLoading("dlgModalFormCorreoElectronicoForm");
        Toast.fire({
            icon: "error",
            title: "Error al guardar"
        });
    }
};


function guardarCorreoElectronico() {

    let titleSave = '¿Guardar Correo Electrónico?';
    let msgSave = '¿Estas seguro que deseas guardar los datos del correo electrónico?';
    if ($('#idCorreoElectronico').val() == "") {
        titleSave = '¿Actualizar Correo Electrónico?';
        msgSave = '¿Estas seguro que deseas actualizar los datos del correo electrónico?';
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
            guardarCorreoElectronicoAsync();
        }
    });
}

function limpiarFormCorreo() {
    $('#titleFormCorreoElectronico').text("Nuevo Correo Electrónico");
    $("#nav-data-bases-tab").tab('show');
    idCategoriaTemp = "";
    $("#idCorreoElectronico").val("");
    $("#nombreTitularText").text("");
    $("#rfc").val("");
    $("#curp").val("");
    $("#idServidorCorreoElectronico").val("");
    $("#idServidorCorreoElectronico").trigger('change');
    $("#tipoCorreo").val("");
    $("#idEstatusCorreoElectronico").val("");
    $("#cveEstatus").val("VIG");
    $("#fechaAltaCorreoElectronico").val("");
    $("#fechaBajaCorreoElectronico").val("");
    $("#fechaBajaCorreoElectronico").prop("disabled", true);
    $("#fechaUltimoCambioText").text("");
    //$("#idEntePublico").val("");
    //$("#idEntePublico").trigger('change');
    $("#idUnidadAdministrativa").val("");
    $("#descUnidadAdministrativa").val("");
    $("#correoElectronico").val("");
    $("#correoElectronicoEnvio").val("");
    $("#correoElectronicoAlias").val("");
    $("#puesto").val("");
    $("#idCategoria").val("");
    $("#correoPersonal").val("");
    $("#telefonoPersonal").val("");
    $("#celular").val("");

    $("#rfc").removeClass("is-valid");
    $("#rfc").removeClass("is-invalid");
    $("#curp").removeClass("is-valid");
    $("#curp").removeClass("is-invalid");
    $("#idServidorCorreoElectronico").removeClass("is-valid");
    $("#idServidorCorreoElectronico").removeClass("is-invalid");
    $("#tipoCorreo").removeClass("is-valid");
    $("#tipoCorreo").removeClass("is-invalid");
    $("#idEstatusCorreoElectronico").removeClass("is-valid");
    $("#idEstatusCorreoElectronico").removeClass("is-invalid");
    $("#cveEstatus").removeClass("is-valid");
    $("#cveEstatus").removeClass("is-invalid");
    $("#fechaAltaCorreoElectronico").removeClass("is-valid");
    $("#fechaAltaCorreoElectronico").removeClass("is-invalid");
    $("#fechaBajaCorreoElectronico").removeClass("is-valid");
    $("#fechaBajaCorreoElectronico").removeClass("is-invalid");
    $("#idEntePublico").removeClass("is-valid");
    $("#idEntePublico").removeClass("is-invalid");
    $("#idUnidadAdministrativa").removeClass("is-valid");
    $("#idUnidadAdministrativa").removeClass("is-invalid");
    $("#correoElectronico").removeClass("is-valid");
    $("#correoElectronico").removeClass("is-invalid");
    $("#correoElectronicoEnvio").removeClass("is-valid");
    $("#correoElectronicoEnvio").removeClass("is-invalid");
    $("#puesto").removeClass("is-valid");
    $("#puesto").removeClass("is-invalid");
    $("#idCategoria").removeClass("is-valid");
    $("#idCategoria").removeClass("is-invalid");
}

const getCorreo = async (idCorreoElectronico) => {
    const options = {
        method: "GET",
        headers: headers
    };


    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/catalogo/tic/getCorreo/' + idCorreoElectronico, options);
        const respJSON = await res.json();

        if (res.ok) {
            offLoading(null);
            $("#idCorreoElectronico").val(respJSON.idCorreoElectronico);
            $("#puesto").val(respJSON.cargoTitularCorreo);

            $("#idTitularCorreoElectronico").val("");
            if (respJSON.catUsuario != null) {
                $("#idTitularCorreoElectronico").val(respJSON.catUsuario.idUsuario);
                $("#nombreTitularText").text(respJSON.catUsuario.nombreCompleto);
                $("#rfc").val(respJSON.catUsuario.rfc);
                $("#curp").val(respJSON.catUsuario.curp);
                $("#correoPersonal").val(respJSON.catUsuario.correoPersonal);
                $("#telefonoPersonal").val(respJSON.catUsuario.numTelefonoPersonal);
                $("#celular").val(respJSON.catUsuario.numCelular);
            }

            if (respJSON.catServidorCorreoElectronico != null) {
                $("#idServidorCorreoElectronico").val(respJSON.catServidorCorreoElectronico.idServidorCorreoElectronico);
                $("#idServidorCorreoElectronico").trigger('change');
            }


            if (respJSON.catTipoCorreoElectronico != null)
                $("#tipoCorreo").val(respJSON.catTipoCorreoElectronico.cveTipoCorreoElectronico);

            if (respJSON.catEstatusCorreoElectronico != null)
                $("#idEstatusCorreoElectronico").val(respJSON.catEstatusCorreoElectronico.cveEstatusCorreoElectronico);

            $("#cveEstatus").val(respJSON.cveEstatus);


            if (respJSON.fechaAltaCorreoElectronicoText != null) {
                var date1 = respJSON.fechaAltaCorreoElectronicoText.split("/");
                $("#fechaAltaCorreoElectronico").val(date1[2] + "-" + date1[1] + "-" + date1[0]);
            }

            if (respJSON.cveEstatus == "HIS") {
                $("#fechaBajaCorreoElectronico").prop("disabled", false);
                if (respJSON.fechaBajaCorreoElectronicoText != null) {
                    var date1 = respJSON.fechaBajaCorreoElectronicoText.split("/");
                    $("#fechaBajaCorreoElectronico").val(date1[2] + "-" + date1[1] + "-" + date1[0]);
                }
            } else {
                $("#fechaBajaCorreoElectronico").val("");
                $("#fechaBajaCorreoElectronico").prop("disabled", true);
            }

            if (respJSON.fechaUltimoCambioText != null) {
                $("#fechaUltimoCambioText").text(respJSON.fechaUltimoCambioText);
            }

            idCategoriaTemp = "";

            if (respJSON.catCategoria != null) {
                idCategoriaTemp = respJSON.catCategoria.idCategoriaUsuario;
            }
            $("#idEntePublico").val("");
            if (respJSON.entePublico != null) {
                $("#idEntePublico").val(respJSON.entePublico.idEntePublico);
                //$("#idEntePublico").trigger('change');
            }
            $("#idEntePublico").trigger('change');

            if (respJSON.unidadAdministrativa != null) {
                $("#idUnidadAdministrativa").val(respJSON.unidadAdministrativa.idUnidadAdministrativa);
                $("#descUnidadAdministrativa").val(respJSON.unidadAdministrativa.nombreUnidadAdministrativa);
            }

            $("#correoElectronico").val(respJSON.correoElectronico);
            $("#correoElectronicoEnvio").val(respJSON.correoElectronicoEnvio);
            $("#correoElectronicoAlias").val(respJSON.correoElectronicoAlias);


            $('#titleFormCorreoElectronico').text("Editar Correo Electrónico");
            $('#dlgModalFormCorreoElectronicoForm').modal('show');
        } else {
            offLoading(null);
        }
    } catch (e) {
        offLoading(null);

        Toast.fire({
            icon: "error",
            title: "Error generado"
        });
    }
}


function editarCorreo(idCorreoElectronico, actualizar) {
    limpiarFormCorreo();
    if (actualizar == "S") {
        $("#sectionButtonForm").show();
        $("#sectionButtonAdd").show();
    } else {
        $("#sectionButtonForm").hide();
        $("#sectionButtonAdd").hide();
    }

    getCorreo(idCorreoElectronico);
}

const eliminarCorreoAsync = async (idCorreoElectronico) => {
    const options = {
        method: "DELETE",
        body: JSON.stringify({idCorreoElectronico: idCorreoElectronico}),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/catalogo/tic/deleteCorreoElectronico', options);
        const respJSON = await res.json();

        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Correo electrónico eliminado correctamente"
            });

            obtienCorreosElectronicos($("#idEntePublicoSearch").val(), $("#idUnidadAdministrativaSearch").val(), null);

            if ($("#idUnidadAdministrativaSearch").val()=="" ||  $("#idUnidadAdministrativaSearch").val()==null)
                obtenTablaCorreoGroup($("#idEntePublicoSearch").val(),null);
            else
                obtenTablaCorreoGroup($("#idEntePublicoSearch").val(),$("#idUnidadAdministrativaSearch").val());

            getConcentradoTipoCorreo($("#idEntePublicoSearch").val());
            if ($("#isAdministradorSigtic").val() == "true") {
                getConcentradoTipoCorreoAll();
            }
            offLoading(null);
        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading(null);
        }

    } catch (e) {
        var error = JSON.parse(e.responseText);
        offLoading(null);
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
};


function eliminarCorreo(idCorreoElectronico) {

    Swal.fire({
        title: 'Eliminar Correo Electrónico',
        text: '¿Estas seguro que deseas eliminar el correo electrónico?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function (result) {
        if (result.isConfirmed) {
            eliminarCorreoAsync(idCorreoElectronico);
        }
    })

}

const guardarCatUsuarioAsync = async () => {
    const options = {
        method: "POST",
        body: JSON.stringify({
            idUsuario: $("#idUsuarioCat").val(),
            rfc: $("#rfcCat").val(),
            curp: $("#curpCat").val(),
            nombre: $("#nombreCat").val(),
            segundoNombre: $("#segundoNombreCat").val(),
            apellidoPaterno: $("#apellidoPaternoCat").val(),
            apellidoMaterno: $("#apellidoMaternoCat").val(),
            //fechaNacimiento: dateNacimiento[2] + "/" + dateNacimiento[1] + "/" + dateNacimiento[0],
            fechaNacimiento: $("#fechaNacimientoCat").val(),
            cveSexo: $("#cveSexoFemeninoCat").is(":checked") ? "F" : "M",
            numCelular: $("#numCelularCat").val(),
            numTelefonoPersonal: $("#numTelefonoPersonalCat").val(),
            correoPersonal: $("#correoPersonalCat").val(),
            cveEstatus: "VIG"
        }),
        headers: headers
    };

    try {
        onLoading("dlgModalFormNuevoCatUsuario");
        const res = await fetch(contexto + '/api/catalogoCatUsuario/guardarCatUsuario', options);
        const respJSON = await res.json();
        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Los datos del usuario se guardaron correctamente"
            });

            offLoading("dlgModalFormNuevoCatUsuario");
            var catUsuario = respJSON;

            $('#dlgModalFormNuevoCatUsuario').modal('hide');
            $('#dlgModalFormSearchCorreoElectronico').modal('hide');

            $("#idTitularCorreoElectronico").val(catUsuario.idUsuario);
            $("#nombreTitularText").text(catUsuario.nombreCompleto);
            $("#rfc").val(catUsuario.rfc);
            $("#curp").val(catUsuario.curp);
            $("#correoPersonal").val(catUsuario.correoPersonal);
            $("#celular").val(catUsuario.numCelular);
            $('#dlgModalFormCorreoElectronicoForm').modal('show');

        } else {
            offLoading("dlgModalFormNuevoCatUsuario");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }

    } catch (e) {
        offLoading("dlgModalFormNuevoCatUsuario");
        Toast.fire({
            icon: "error",
            title: "Error al guardar"
        });
    }
};


function guardarCatUsuario() {

    let titleSave = '¿Guardar Usuario?';
    let msgSave = '¿Estas seguro que deseas guardar los datos del usuario?';


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

function changeEstatus() {
    if ($("#cveEstatus").val() == "HIS") {
        $('#fechaBajaCorreoElectronico').prop('disabled', false);
    } else {
        $('#fechaBajaCorreoElectronico').val("");
        $('#fechaBajaCorreoElectronico').prop('disabled', true);
    }


}

function editarUsuario(idUsuario) {

    $('#preloader').modal('show');
    $.ajax({
        url: contexto + '/api/catalogoCatUsuario/getUsuario/' + idUsuario,
        type: 'GET',
        dataType: 'JSON',
        contentType: 'application/json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function (response) {

            $("#titleFormHeaderCatUsuario").text("Editar Usuario");

            var catUsuario = response;

            $("#idUsuarioCat").val(catUsuario.idUsuario);
            $("#rfcCat").val(catUsuario.rfc);
            $("#curpCat").val(catUsuario.curp);
            $("#nombreCat").val(catUsuario.nombre);
            $("#segundoNombreCat").val(catUsuario.segundoNombre);
            $("#apellidoPaternoCat").val(catUsuario.apellidoPaterno);
            $("#apellidoMaternoCat").val(catUsuario.apellidoMaterno);

            if (catUsuario.cveSexo == "F")
                $("#cveSexoFemeninoCat").prop("checked", true);
            else
                $("#cveSexoMasculinoCat").prop("checked", true);

            if (catUsuario.fechaNacimientoText != null) {
                var date1 = catUsuario.fechaNacimientoText.split("/");
                $("#fechaNacimientoCat").val(date1[2] + "-" + date1[1] + "-" + date1[0]);
            }

            $("#correoPersonalCat").val(catUsuario.correoPersonal);
            $("#numCelularCat").val(catUsuario.numCelular);
            $("#numTelefonoPersonalCat").val(catUsuario.numTelefonoPersonal);
            $("#cveEstatus").val(catUsuario.cveEstatus);
            $('#preloader').modal('hide');
            $('#dlgModalFormNuevoCatUsuario').modal('show');

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


const getConcentradoTipoCorreo = async (idEntePublico) => {

    $('#tbConcentradoEnte tbody tr').slice().remove();
    const options = {
        method: "GET",
        header: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/catalogo/tic/getConcentradoTipoCorreo/' + idEntePublico, options);
        const respJSON = await res.json();

        if (res.ok) {

            let tActivo=0;
            let tOtros=0;
            let tTotal=0;

            var item = 0;
            for (i = 0; i < respJSON.listaConcentrado.length; i++) {
                var fila = "<tr>";
                fila = fila + "<td class=' " + coloresText[item] + "'><i class='fas fa-circle " + coloresText[item] + " fa-fw'></i> " + respJSON.listaConcentrado[i].nombre + "</td>";
                fila = fila + "<td class=' " + coloresText[item] + "'>" + number_format(respJSON.listaConcentrado[i].totalActivo, 0) + "</td>";
                fila = fila + "<td class=' " + coloresText[item] + "'>" + number_format(respJSON.listaConcentrado[i].totalOtros, 0) + "</td>";
                fila = fila + "<td class=' " + coloresText[item] + "'>" + number_format(respJSON.listaConcentrado[i].total, 0) + "</td>";
                tActivo+=respJSON.listaConcentrado[i].totalActivo;
                tOtros+=respJSON.listaConcentrado[i].totalOtros;
                tTotal+=respJSON.listaConcentrado[i].total;
                $("#tbConcentradoEnte tbody").append(fila);
                item++;
                if (item == 6)
                    item = 0;
            }

            var fila = "<tr>";
            fila = fila + "<td class='fw-bold '> Total</td>";
            fila = fila + "<td class='fw-bold '>" + number_format(tActivo, 0) + "</td>";
            fila = fila + "<td class='fw-bold '>" + number_format(tOtros, 0) + "</td>";
            fila = fila + "<td class='fw-bold '>" + number_format(tTotal, 0) + "</td>";
            $("#tbConcentradoEnte tfoot").append(fila);


            offLoading(null);
        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading(null);
        }
    } catch
        (e) {
        offLoading(null);
        Toast.fire({
            icon: "error",
            title: "Error al obtener los datos"
        });
    }
}

const getConcentradoTipoCorreoAll = async () => {

    $('#tbConcentradoAll tbody tr').slice().remove();
    const options = {
        method: "GET",
        header: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/catalogo/tic/getConcentradoTipoCorreoAll/', options);
        const respJSON = await res.json();

        if (res.ok) {

            let tActivo=0;
            let tOtros=0;
            let tTotal=0;

            var item = 0;
            for (i = 0; i < respJSON.listaConcentrado.length; i++) {
                var fila = "<tr>";
                fila = fila + "<td class='fw-bold " + coloresText[item] + "'><i class='fas fa-circle " + coloresText[item] + " fa-fw'></i> " + respJSON.listaConcentrado[i].nombre + "</td>";
                fila = fila + "<td class='fw-bold " + coloresText[item] + "'>" + number_format(respJSON.listaConcentrado[i].totalActivo, 0) + "</td>";
                fila = fila + "<td class='fw-bold " + coloresText[item] + "'>" + number_format(respJSON.listaConcentrado[i].totalOtros, 0) + "</td>";
                fila = fila + "<td class='fw-bold " + coloresText[item] + "'>" + number_format(respJSON.listaConcentrado[i].total, 0) + "</td>";
                tActivo+=respJSON.listaConcentrado[i].totalActivo;
                tOtros+=respJSON.listaConcentrado[i].totalOtros;
                tTotal+=respJSON.listaConcentrado[i].total;

                $("#tbConcentradoAll tbody").append(fila);
                item++;
                if (item == 6)
                    item = 0;
            }

            var fila = "<tr>";
            fila = fila + "<td class='fw-bold '>Total</td>";
            fila = fila + "<td class='fw-bold '>" + number_format(tActivo, 0) + "</td>";
            fila = fila + "<td class='fw-bold '>" + number_format(tOtros, 0) + "</td>";
            fila = fila + "<td class='fw-bold '>" + number_format(tTotal, 0) + "</td>";
            $("#tbConcentradoAll tfoot").append(fila);

            offLoading(null);
        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading(null);
        }
    } catch
        (e) {
        offLoading(null);
        Toast.fire({
            icon: "error",
            title: "Error al obtener los datos"
        });
    }
}

const validarCorreoAsync = async (idCorreoElectronico, estatus) => {
    const options = {
        method: "PUT",
        body: JSON.stringify({idCorreoElectronico: idCorreoElectronico}),
        headers: headers
    };

    let rutaVal = '/api/catalogo/tic/vigCorreoElectronico'

    if (estatus == "NOV")
        rutaVal = '/api/catalogo/tic/novCorreoElectronico'

    try {
        onLoading(null);
        const res = await fetch(contexto + rutaVal, options);
        const respJSON = await res.json();

        if (res.ok) {


            if (estatus == "NOV") {
                Toast.fire({
                    icon: "success",
                    title: "Correo electrónico validado como No Vigente"
                });
            } else {
                Toast.fire({
                    icon: "success",
                    title: "Correo electrónico validado como Vigente"
                });
            }

            offLoading(null);

            setTimeout(() => {
                obtienCorreosElectronicos($("#idEntePublicoSearch").val(), $("#idUnidadAdministrativaSearch").val(), null);
            }, 550);


        } else {
            offLoading(null);

            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }

    } catch (e) {
        var error = JSON.parse(e.responseText);
        offLoading(null);

        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
};

function validarCorreo(idCorreoElectronico) {
    var estatusVal = "NOV";

    if ($("#idValCorreo" + idCorreoElectronico).prop('checked'))
        estatusVal = "VIG";

    let msgVal = "¿Esta seguro que desea validar el correo como VIGENTE?"

    if (estatusVal == 'NOV')
        msgVal = "¿Esta seguro que desea validar el correo ya como NO VIGENTE?"

    Swal.fire({
        title: 'Validar Correo Electrónico',
        text: msgVal,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function (result) {
        if (result.isConfirmed) {
            validarCorreoAsync(idCorreoElectronico, estatusVal);
        } else {
            if (estatusVal == "VIG")
                $("#idValCorreo" + idCorreoElectronico).prop('checked', false);
            else
                $("#idValCorreo" + idCorreoElectronico).prop('checked', true);
        }
    })

}


/*
function validarCorreo(idCorreoElectronico, estatus){

    let msgVal="¿Esta seguro que desea validar el correo como VIGENTE?"

    if (estatus=='NOV')
        msgVal="¿Esta seguro que desea validar el correo ya como NO VIGENTE?"

    Swal.fire({
        title: 'Validar Correo Electrónico',
        text: msgVal,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function(result) {
        if (result.isConfirmed) {
            validarCorreoAsync(idCorreoElectronico, estatus);
        }
    })

}*/

function mostrarClave(idCorreoElectronico, clave, asteriscos) {

    if ($("#idEye" + idCorreoElectronico).hasClass("fa-eye-slash")) {
        $("#idEye" + idCorreoElectronico).removeClass("fa-eye-slash");
        $("#idEye" + idCorreoElectronico).addClass("fa-eye");
        $("#pw" + idCorreoElectronico).text(clave);
    } else {
        $("#idEye" + idCorreoElectronico).removeClass("fa-eye");
        $("#idEye" + idCorreoElectronico).addClass("fa-eye-slash");
        $("#pw" + idCorreoElectronico).text(asteriscos);
    }

}

function ocultarBarra() {

    if (ocultar == false) {
        ocultar = true;
        $("#idCorreoLeft").hide();
        $("#idIcoControl").removeClass("fa-angle-double-left");
        $("#idIcoControl").addClass("fa-angle-double-right");
        $("#idCorreoPrincipal").removeClass("col-xs-12 col-sm-12 col-md-8 col-xxl-9 col-lg-9");
        $("#idCorreoPrincipal").addClass("col-xs-12 col-sm-12 col-md-12 col-xxl-12 col-lg-12");

    } else {
        ocultar = false;
        $("#idIcoControl").removeClass("fa-angle-double-right");
        $("#idIcoControl").addClass("fa-angle-double-left");
        $("#idCorreoLeft").show();

        $("#idCorreoPrincipal").removeClass("col-xs-12 col-sm-12 col-md-12 col-xxl-12 col-lg-12");
        $("#idCorreoPrincipal").addClass("col-xs-12 col-sm-12 col-md-8 col-xxl-9 col-lg-9");
    }

}

function graficaCorreosPorEstatus(datos, total)
{
    let optionsCorreoEstatus = {
        series: datos.valores,
        labels:datos.etiquetas,
        chart: {
            //width: 600,
            type: 'donut',
            toolbar: {
                show: true
            }
        },
        dataLabels: {
            enabled: false
        },
        title: {
            text: 'Correos por Estatus'
        },
        subtitle: {
            text: 'Total: '+number_format(total,0),
            align: 'right',
            margin: 10,
            floating: false,
            style: {
                fontSize:  '14px',
                fontWeight:  'bold',
                //fontFamily:  undefined,
                color:  '#000000'
            },
        },
        plotOptions: {
            pie: {
                expandOnClick: true,
                donut: {
                    labels: {
                        show: true
                    }
                }
            }
        },
        fill: {
            type: 'gradient',
        },
        responsive: [{
            //breakpoint: 600,
            options: {
                chart: {
                    //width: 200
                },
                legend: {
                    show: false
                }
            }
        }],
        colors: colores,
        tooltip: {
            enabled: true,
            y: {
                formatter: function(val) {
                    return " "+number_format(val,0)
                }
            }
        },
        legend: {
            formatter: function(val, opts) {
                return val + " - " + number_format(opts.w.globals.series[opts.seriesIndex],0)
            },
            position: 'left',
            offsetY: 0,
            //height: 300,
        }
    };

    if (chartCorreoEstatus!=null){
        try{
            chartCorreoEstatus.destroy();
            chartCorreoEstatus=null;
        }catch(e){}
    }

    chartCorreoEstatus = new ApexCharts(document.querySelector("#graficaCorreosEstatus"), optionsCorreoEstatus);
    chartCorreoEstatus.render();

}

function graficaCorreosPorTipo(datos, total)
{
    let optionsCorrePorTipo = {
        series: datos.valores,
        labels:datos.etiquetas,
        chart: {
            //width: 600,
            type: 'donut',
            toolbar: {
                show: true
            }
        },
        dataLabels: {
            enabled: false
        },
        title: {
            text: 'Correos por Tipo'
        },
        subtitle: {
            text: 'Total: '+number_format(total,0),
            align: 'right',
            margin: 10,
            floating: false,
            style: {
                fontSize:  '14px',
                fontWeight:  'bold',
                //fontFamily:  undefined,
                color:  '#000000'
            },
        },
        plotOptions: {
            pie: {
                expandOnClick: true,
                donut: {
                    labels: {
                        show: true
                    }
                }
            }
        },
        fill: {
            type: 'gradient',
        },
        responsive: [{
            //breakpoint: 600,
            options: {
                chart: {
                    //width: 200
                },
                legend: {
                    show: false
                }
            }
        }],
        colors: colores,
        tooltip: {
            enabled: true,
            y: {
                formatter: function(val) {
                    return " "+number_format(val,0)
                }
            }
        },
        legend: {
            formatter: function(val, opts) {
                return val + " - " + number_format(opts.w.globals.series[opts.seriesIndex],0)
            },
            position: 'left',
            offsetY: 0,
            //height: 300,
        }
    };

    if (chartCorreoPorTipo!=null){
        try{
            chartCorreoPorTipo.destroy();
            chartCorreoPorTipo=null;
        }catch(e){}
    }

    chartCorreoPorTipo = new ApexCharts(document.querySelector("#graficaCorreosPorTipoCorreo"), optionsCorrePorTipo);
    chartCorreoPorTipo.render();

}

const obtenTablaCorreoGroup = async(idEntePublico, idUnidadAdministrativa) => {


    $('#tableCorreoGroup tbody tr').slice().remove();

    const options={
        method: "GET",
        header:headers
    };

    try {
        let ruta=contexto+'/api/catalogo/tic/getListaCorreoGroupTablero/'+idEntePublico;

        if (idUnidadAdministrativa!=null)
            ruta=contexto+'/api/catalogo/tic/getListaCorreoGroupTableroUnidad/'+idEntePublico+'/'+idUnidadAdministrativa;

        const res= await fetch(ruta, options);
        const respJSON = await res.json();

        if (res.ok){
            graficaCorreosPorEstatus(respJSON.graficaCorreoEstatus, respJSON.total);
            graficaCorreosPorTipo(respJSON.graficaCorreoPorTipo, respJSON.totalTipo);

            onLoading(null);

            let fila="";
            respJSON.listaTablero.forEach((elemento) => {
                fila = "<tr class='"+elemento.clase+"' "+elemento.estilo+">";
                fila=fila+"<td>"+ elemento.nombre + "</td>";


                if (elemento.idTipoCorreo!=null && parseInt(elemento.totalActivo)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verCorreos("+elemento.idEntePublico+","+elemento.idUnidadAdministrativa+","+elemento.idTipoCorreo+",\"ACT\")'>"+number_format(elemento.totalActivo,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+ number_format(elemento.totalActivo,0) + "</td>";

                if (elemento.idTipoCorreo!=null && parseInt(elemento.totalBloqueado)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verCorreos("+elemento.idEntePublico+","+elemento.idUnidadAdministrativa+","+elemento.idTipoCorreo+",\"BLO\")'>"+number_format(elemento.totalBloqueado,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+ number_format(elemento.totalBloqueado,0) + "</td>";

                if (elemento.idTipoCorreo!=null && parseInt(elemento.totalBloqueadoServidor)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verCorreos("+elemento.idEntePublico+","+elemento.idUnidadAdministrativa+","+elemento.idTipoCorreo+",\"BXS\")'>"+number_format(elemento.totalBloqueadoServidor,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+ number_format(elemento.totalBloqueadoServidor,0) + "</td>";

                if (elemento.idTipoCorreo!=null && parseInt(elemento.totalMantenimiento)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verCorreos("+elemento.idEntePublico+","+elemento.idUnidadAdministrativa+","+elemento.idTipoCorreo+",\"MAN\")'>"+number_format(elemento.totalMantenimiento,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+ number_format(elemento.totalMantenimiento,0) + "</td>";

                if (elemento.idTipoCorreo!=null && parseInt(elemento.totalNoActivo)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verCorreos("+elemento.idEntePublico+","+elemento.idUnidadAdministrativa+","+elemento.idTipoCorreo+",\"NOA\")'>"+number_format(elemento.totalNoActivo,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+ number_format(elemento.totalNoActivo,0) + "</td>";

                if (elemento.idTipoCorreo!=null && parseInt(elemento.total)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verCorreos("+elemento.idEntePublico+","+elemento.idUnidadAdministrativa+","+elemento.idTipoCorreo+",\"TODO\")'>"+number_format(elemento.total,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+ number_format(elemento.total,0) + "</td>";

                fila=fila+"</tr>";

                $('.'+elemento.clasePadre).treegrid("add",[fila]);
            });

            $("#cargadoTree").val("SI");
            $("#cargadoTree").trigger('change');

            $("#totalActivoText").text(respJSON.totalActivo);
            $("#totalBloqueoText").text(respJSON.totalBloqueado);
            $("#totalBloqueoServidorText").text(respJSON.totalBloqueadoServidor);
            $("#totalMantenimientoText").text(respJSON.totalMantenimiento);
            $("#totalNoActivoText").text(respJSON.totalNoActivo);
            $("#totalEstatusText").text(respJSON.total);

            $('[data-bs-toggle="tooltip"]').tooltip();
            offLoading(null);
        }else {
            Toast.fire({
                icon:"error",
                title: respJSON.message
            });
            offLoading(null);
        }
    }catch (e){
        offLoading(null);
        Toast.fire({
            icon: "error",
            title: "Error al obtener los datos"
        });
    }

}


function verCorreos(idEntePublico, idUnidadAdministrativa, idTipoCorreo, estatusCorreo){

    $("#dlgModalCorreo").modal("show");

    if(table!=null)
        table.clear();

    table = new DataTable('#tableCorreoModal', {
        responsive: true,
        sPaginationType: "full_numbers",
        bDestroy: true,
        scrollX: true,
        ordering: false,
        ajax: {
            url: contexto+'/api/catalogo/tic/tablaCorreoUnidad',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    idEntePublico:idEntePublico,
                    idUnidadAdministrativa:idUnidadAdministrativa,
                    idTipoCorreo:idTipoCorreo,
                    estatusCorreo:estatusCorreo
                }));
            },
            complete: function (data) {
                $("#idEntePublicoText").text(data['responseJSON'].entePublico);
                $("#idUnidadAdminText").text(data['responseJSON'].unidadAdmin);
                $("#idTipoCorreoText").text(data['responseJSON'].tipoCorreo);

            }
        },
        /*columnDefs: [
            {
                "targets": 0,
                "width": "150px",
                "className": "text-left",
            },  {
                "targets": 5,
                "width": "100px",
                "className": "text-center",
            },  {
                "targets": 6,
                "width": "100px",
                "className": "text-center",
            }],*/
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        lengthChange: false,
        select: false
    });

}
