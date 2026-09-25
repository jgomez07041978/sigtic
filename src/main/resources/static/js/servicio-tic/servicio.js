const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let ocultar = false;

let table = null;
let tableSearch = null;
let tableServicioDisponibles = null;
let tableServiciosRelacionados = null;
let tableServiciosRelacionadosPuerto = null;
let tableServiciosRelacionadosUsuarios = null;
let idEntePublicoDetalleTemp = null;
let idTipoServicioDetalleTemp = null;
let idSubTipoServicioDetalleTemp = null;

let idEntePublicoSeleccionado=0;
let idSubtipoServicioTemp="";

let chartServiciosAdministra = null;
let chartServiciosOpera = null;
var colores=['#0d6efd','#ffc107','#198754','#dc3545', '#6f42c1','#0dcaf0', '#BC955C','#235B4E','#1F618D','#fd7e14','#6F7271','#27AE60','#dc3545','#d63384','#6610f2','#198754','#0dcaf0','#2E4053'];
var estatusTemp="";
var tipoTemp="";
let listaTreeHijos=null;
let elimino=false;

let paginaActual=1;
let sizePagina=10;
let totalPaginas=0;
let totalElementos=0;
let lastPagina=false;
let firstPagina=false;
let emptyPagina=false;

let paginaActualSearch=1;
let sizePaginaSearch=10;
let totalPaginasSearch=0;
let totalElementosSearch=0;
let lastPaginaSearch=false;
let firstPaginaSearch=false;
let emptyPaginaSearch=false;

//let tableUSerSearch=null;
let tableDirectorio = null;

let rfcTemp="";
let curpTemp="";
let nombreTemp="";
let segundoNombreTemp="";
let apellidoPaternoTemp="";
let apellidoMaternoTemp="";

let idDomicilioEnteTemp=-1;
let idUbicacionTemp=-1;

let idDomicilioEnteOperaTemp=-1;
let idUbicacionOperaTemp=-1;

let opServicio=0;

let modalSearchUser = 1;

$(document).ready(function(){

    $('.tree').treegrid();
    $('[data-bs-toggle="tooltip"]').tooltip();

    jQuery.event.special.mousewheel = {
        setup: function( _, ns, handle ){
            this.addEventListener("mousewheel", handle, { passive: true });
        }
    };

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

    $('#idDomicilioEnte').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormServicio")
    });

    $('#idDomicilioEnteOpera').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormServicio")
    });

    $("#idEntePublicoAdministra").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormServicio")
    });

    $("#idTipoServicioTic").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormServicio")
    });

    $("#idTipoServicioTicPlantilla").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalDescargarPlantilla")
    });


    $('.select2me').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormServicio")
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

    $('.select3me').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalDescargarPlantilla")
    });
    $('.select3me')
        .parent('div')
        .children('span')
        .children('span')
        .children('span')
        .css('height', ' calc(3.5rem + 2px)');
    $('.select3me')
        .parent('div')
        .children('span')
        .children('span')
        .children('span')
        .children('span')
        .css('margin-top', '18px');
    $('.select3me')
        .parent('div')
        .find('label')
        .css('z-index', '1');

    $("#idControlInventario").hide();
    $("#nav-data-ips-tab").hide();
    $("#nav-data-puertos-tab").hide();
    $("#nav-data-usuarios-tab").hide();

    if ($("#idEntePublicoSession").val()!="") {
        $('#idNuevoServicio').prop('disabled', false);
        idEntePublicoSeleccionado=$("#idEntePublicoSession").val();

        if ($("#isAdministradorSigtic").val()=="true") {
            idEntePublicoSeleccionado=null
            obtenTablaServicioGroup(idEntePublicoSeleccionado, null);
        }
    }

    $('#idEntePublicoSearch').on('change', function() {
        if ($("#idEntePublicoSearch").val()!="")
            $('#idNuevoServicio').prop('disabled', false);
        else
            $('#idNuevoServicio').prop('disabled', true);

        obtienearbolPorInstitucion(function (data) {
            $("#idUnidadAdministrativaSearch").val(data.id);
            $("#descUnidadAdministrativaSearch").val(data.text);
            limpiarBuscarArbol();
            $('#modal_arbol_unidad').modal('hide');
            $("#descUnidadAdministrativaSearch").removeClass("is-valid");
            $("#descUnidadAdministrativaSearch").removeClass("is-invalid");
            obtenTablaServicioGroup($("#idEntePublicoSearch").val(), data.id);
        }, $("#idEntePublicoSearch").val());

        $("#idUnidadAdministrativaSearch").val("");
        $("#descUnidadAdministrativaSearch").val("");

        idEntePublicoSeleccionado=$("#idEntePublicoSearch").val();
        obtenTablaServicioGroup($("#idEntePublicoSearch").val(), null);
    });

   /* $('#idEntePublicoSearch2').on('change', function() {
        searchService();
    });*/

    $("#btnOpenArbolSearch").click(function () {
        $('#modal_arbol_unidad').modal('show');
    });

    $("#btnLimpiarOpenArbolSearch").click(function () {
        $("#idUnidadAdministrativaSearch").val("");
        $("#descUnidadAdministrativaSearch").val("");
        obtenTablaServicioGroup($("#idEntePublicoSearch").val(), $("#idUnidadAdministrativaSearch").val());
    });


    $('#idEntePublicoAdministra').on('change', function() {
        getDomicilioEntePublico($('#idEntePublicoAdministra').val());

        $('#idUnidadAdminAdministra').val("");
        $('#descUnidadAdminAdministra').val("");

        obtienearbolPorInstitucion2(function (data) {
            $("#idUnidadAdminAdministra").val(data.id);
            $("#descUnidadAdminAdministra").val(data.text);
            limpiarBuscarArbol2();
            $('#modal_arbol_unidad2').modal('hide');
            $("#descUnidadAdminAdministra").removeClass( "is-valid" );
            $("#descUnidadAdminAdministra").removeClass( "is-invalid" );
        },$("#idEntePublicoAdministra").val());
    });

    $('#idEntePublicoOpera').on('change', function() {
        getDomicilioEntePublicoOpera($('#idEntePublicoOpera').val());

        $('#descUnidadAdminOpera').val("");
        $('#idUnidadAdminOpera').val("");
        obtienearbolPorInstitucion3(function (data) {
            $("#idUnidadAdminOpera").val(data.id);
            $("#descUnidadAdminOpera").val(data.text);
            limpiarBuscarArbol3();
            $('#modal_arbol_unidad3').modal('hide');
            $("#descUnidadAdminOpera").removeClass( "is-valid" );
            $("#descUnidadAdminOpera").removeClass( "is-invalid" );
        },$("#idEntePublicoOpera").val());
    });

    $('#idNuevoServicio').click(function () {
        limpiarServiciosTic();
        $('#titleFormServicio').text("Nuevo servicio");
        $('#dlgModalFormServicio').modal('show');
    });

    $('#idDescargarPlantilla').click(function () {
        $('#dlgModalDescargarPlantilla').modal('show');
    });


    inicializarDependencias();


    fillSelectGet("/api/catalogo/tic/getEstatusServiciosTic","#idEstatusServicio", "cveEstatusServicioTic", "descEstatusServicioTic", null, null);
    fillSelectGet("/api/catalogo/tic/getCatTipoServiciosTic","#idTipoServicioTic", "idTipoServicioTic", "nombreTipoServicioTic", null, null);
    fillSelectGet("/api/catalogo/tic/getCatTipoServiciosTic","#idTipoServicioTicPlantilla", "idTipoServicioTic", "nombreTipoServicioTic", null, null);
    fillSelectGet("/api/catalogo/tic/getCatTipoImpactoServiciosTic","#idTipoImpactoServicio","cveTipoImpactoServicioTic", "descTipoImpactoServicioTic", null, null);
    fillSelectGet("/api/catalogo/tic/getEstadosServiciosTic","#idEstadoServicio","cveEstadoServicioTic", "descEstadoServicioTic", null, null);
    fillSelectPost("/api/tipoUsuarioServicio/getAll","#idTipoUsuarioServicio","idTipoUsuario", "tipoUsuarioServicio", null);


    $("#idBtnUnidadAdmin").click(function () {
        $('#modal_arbol_unidad2').modal('show');
    });

    $("#idBtnUnidadOperativa").click(function () {
        $('#modal_arbol_unidad3').modal('show');
    });

    $("#txtBuscar").keyup(function () {
        busqueda($(this).val())
    });

    $("#txtBuscar2").keyup(function () {
        busqueda2($(this).val())
    });

    $("#txtBuscar3").keyup(function () {
        busqueda3($(this).val())
    });

    $("#idTipoServicioTic").on("change", function(){
        obtenerSubtipoServicio($("#idTipoServicioTic").val());
    })

    $("#idTipoServicioTicPlantilla").on("change", function(){
        obtenerSubtipoServicioPlantilla($("#idTipoServicioTicPlantilla").val());
    })

    $("#idFormServicioTic").validate({
        ignore:"",
        rules: {
            idTipoServicioTic:"required",
            idSubtipoServicioTic:"required",
            idCveServicioTic:"required",
            idNombreServicio: "required",
            idDescServicioTicAbr: "required",
            idDescServicioTic: "required",
            idEstatusServicio: "required",
            idEstadoServicio: "required",
            idTipoImpactoServicio: "required",
            idNumOrdenServicio: "required",
            idAlcanceServicioTic: "required",
            idObsServicioTic: "required",
            idEntePublicoAdministra: "required",
            descUnidadAdminAdministra: "required",
            idEntePublicoOpera:"required",
            descUnidadAdminOpera: "required",
            idDomicilioEnte: "required",
            idUbicacionUnidadAdmtva: "required",
            idDomicilioEnteOpera: "required",
            idUbicacionUnidadOpera: "required",
        },
        messages:{
            idTipoServicioTic:"Campo requerido",
            idSubtipoServicioTic:"Campo requerido",
            idCveServicioTic:"Campo requerido",
            idNombreServicio: "Campo requerido",
            idDescServicioTicAbr: "Campo requerido",
            idDescServicioTic: "Campo requerido",
            idEstatusServicio: "Campo requerido",
            idEstadoServicio: "Campo requerido",
            idTipoImpactoServicio: "Campo requerido",
            idNumOrdenServicio: "Campo requerido",
            idAlcanceServicioTic: "Campo requerido",
            idObsServicioTic: "Campo requerido",
            idEntePublicoAdministra: "Campo requerido",
            descUnidadAdminAdministra: "Campo requerido",
            idEntePublicoOpera:"Campo requerido",
            descUnidadAdminOpera: "Campo requerido",
            idDomicilioEnte: "Campo requerido",
            idUbicacionUnidadAdmtva: "Campo requerido",
            idDomicilioEnteOpera: "Campo requerido",
            idUbicacionUnidadOpera: "Campo requerido",

        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            var n = element.attr("id");
            if (n=="descUnidadAdminAdministra" || n=="descUnidadAdminOpera")
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
    });

    $("#idFormServicioTic").submit(function(e) {
        e.preventDefault();
        if($('#idFormServicioTic').valid()) {
            guardarServiciosTic();
            return true;
        }
        else
            return false;
    });

    $('#cargadoTree').on('change', function() {
       if ($('#cargadoTree').val()!=""){
           setTimeout(() => {
               mostrarTreeHijos();
           }, 700);

       }
    });


    $("#idBtnBuscar").click(function () {
        modalSearchUser=1;
        limpiarSearch();
        $('#dlgModalSearchUsuario').modal('show');
    });

    $("#idBtnBuscar2").click(function () {
        modalSearchUser=2;
        limpiarSearch();
        $('#dlgModalSearchUsuario').modal('show');
    });


    $("#idBtnLimpiar").click(function () {
        limpiarSearch();
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
            rfcTemp=$("#rfcSearch").val();
            curpTemp=$("#curpSearch").val();
            nombreTemp=$("#nombreSearch").val();
            segundoNombreTemp=$("#segundoNombreSearch").val();
            apellidoPaternoTemp=$("#apellidoPaternoSearch").val();
            apellidoMaternoTemp=$("#apellidoMaternoSearch").val();

            searchUsuario();
            return true;
        }
    });


    $('#actualPage').blur(function(){
        if ($("#actualPage").val()==""){
            $("#actualPage").val(paginaActual);
        }

        if (parseInt($("#actualPage").val())<1 || parseInt($("#actualPage").val())>totalPaginas){
            $("#actualPage").val(paginaActual);
        }

        if (parseInt($("#actualPage").val())!=paginaActual)
        {
            paginaActual=$("#actualPage").val();
            buscarNav();
        }
    });

    $("#actualPage").on('keyup', function (e) {
        if (e.key === 'Enter' || e.keyCode === 13) {
            $('#actualPage').blur();
        }
    });

    $('#actualPageSearch').blur(function(){
        if ($("#actualPageSearch").val()==""){
            $("#actualPageSearch").val(paginaActualSearch);
        }

        if (parseInt($("#actualPageSearch").val())<1 || parseInt($("#actualPageSearch").val())>totalPaginasSearch){
            $("#actualPageSearch").val(paginaActualSearch);
        }

        if (parseInt($("#actualPageSearch").val())!=paginaActualSearch)
        {
            paginaActualSearch=$("#actualPageSearch").val();
            buscarNav();
        }
    });

    $("#actualPageSearch").on('keyup', function (e) {
        if (e.key === 'Enter' || e.keyCode === 13) {
            $('#actualPageSearch').blur();
        }
    });


    $("#formUsuarioSearch").validate({
        ignore:"",
        rules: {
            idTipoUsuarioServicio:"required",
            rfc:"required",
            curp: "required",
            fechaAltaServicio: "required",
            fechaBajaServicio: "required"
        },
        messages:{
            idTipoUsuarioServicio:"Campo requerido",
            rfc:"Campo requerido",
            curp: "Campo requerido",
            fechaAltaServicio: "Campo requerido",
            fechaBajaServicio: "Campo requerido"

        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            var n = element.attr("id");
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
    });

    $("#formUsuarioSearch").submit(function(e) {
        e.preventDefault();
        if($('#formUsuarioSearch').valid()) {
            guardarUSuarioServicio();
            return true;
        }
        else
            return false;
    });

});


function seleccionaHoja(hoja){

    if (hoja==1){
        $("#hoja1").addClass("active");
        $("#hoja2").removeClass("active");
        $("#idHoja1").show();
        $("#idHoja2").hide();
    }else
    if (hoja==2){
        if (ocultar==false)
            ocultarBarra();
        $("#hoja1").removeClass("active");
        $("#hoja2").addClass("active");
        $("#idHoja1").hide();
        $("#idHoja2").show();

    }
}

function primeraPagina(){
    if (!firstPagina && totalPaginas>0){
        paginaActual=1;
        $("#actualPage").val(paginaActual);
        buscarNav();
    }
}

function anteriorPagina(){
    if (!firstPagina && totalPaginas>0){
        paginaActual=parseInt(paginaActual)-1;
        $("#actualPage").val(paginaActual);
        buscarNav();
    }
}

function ultimaPagina(){

    if (!lastPagina && totalPaginas>0){
        paginaActual=totalPaginas;
        $("#actualPage").val(paginaActual);
        buscarNav();
    }
}
function siguientePagina(){
    if (!lastPagina && totalPaginas>0){
        paginaActual=parseInt(paginaActual)+1;
        $("#actualPage").val(paginaActual);
        buscarNav();
    }
}

function buscarNav(){
    searchUsuario();
}

const searchUsuario = async () => {
    let parametros = {};

    if (rfcTemp!="")
        parametros.rfcUsuario=rfcTemp;

    if (curpTemp!="")
        parametros.curpUsuario=curpTemp;

    if (nombreTemp!="")
        parametros.nombre=nombreTemp;

    if (segundoNombreTemp!="")
        parametros.segundoNombre=segundoNombreTemp;

    if (apellidoPaternoTemp!="")
        parametros.apellidoPaterno=apellidoPaternoTemp;

    if (apellidoMaternoTemp!="")
        parametros.apellidoMaterno=apellidoMaternoTemp;

    parametros.isVigente=true;

    parametros.pagina=paginaActual-1;
    parametros.size=sizePagina;


    const options={
        method: "POST",
        body: JSON.stringify(parametros),
        headers: {
            "X-CSRF-Token":  $('input[name="_csrf"]').val(),
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    onLoading(null);
    const res = await fetch(contexto + '/api/directorio/filtrarUser', options);
    const respJSON = await res.json();
    if (res.ok){
        $("#idCardTablaUsuarioSearch").show();
        //paginaActual=respJSON.number+1;
        totalPaginas=respJSON.totalPages;
        totalElementos=respJSON.totalElements;
        lastPagina=respJSON.last;
        firstPagina=respJSON.first;
        emptyPagina=respJSON.empty;

        if (emptyPagina)
            $("#paginationNav").hide();
        else{
            $("#paginationNav").show();

            if (firstPagina){
                $("#primeraPag").addClass("disabled")
                $("#previousPag").addClass("disabled")
            }else{
                $("#primeraPag").removeClass("disabled")
                $("#previousPag").removeClass("disabled")
            }
            if (lastPagina){
                $("#nextPag").addClass("disabled")
                $("#lastPag").addClass("disabled")
            }else{
                $("#nextPag").removeClass("disabled")
                $("#lastPag").removeClass("disabled")
            }

            $("#infoPag").text("Pag. "+paginaActual+" de "+totalPaginas);
            //$("#actualPage").val(paginaActual);

            if (lastPagina)
                $("#totalRegistro").text("Mostrando "+((paginaActual*sizePagina)-sizePagina+1)+" - "+totalElementos+" de "+ totalElementos);
            else
                $("#totalRegistro").text("Mostrando "+((paginaActual*sizePagina)-sizePagina+1)+" - "+(paginaActual*sizePagina)+" de "+ totalElementos);
        }

        obtenTablaDirectorio(respJSON.content);


        offLoading(null);
    }else {
        Toast.fire({
            icon: "error",
            title: respJSON.message
        });
        offLoading(null);
    }
};

function obtenTablaDirectorio(jsonData){

    if(tableDirectorio!=null)
        tableDirectorio.clear();

    tableDirectorio = new DataTable('#tableUsuarioSearch', {
        sPaginationType: "full_numbers",
        responsive: true,
        bSort: false,
        paging: false,
        info: false,
        bDestroy: true,
        searching: false,
        initComplete: function(settings, json) {
            $('[data-bs-toggle="tooltip"]').tooltip();
        },
        data: jsonData,
        columns: [
            { data: 'entePublico.nombreEntePublico'},
            { data: 'catUsuario.curp'},
            { data: 'catUsuario.nombreCompleto'},
            { data: 'cveEstatus',
                render: function (data, type, row, meta) {
                    return '<a href="#" class="btn btn-outline-success mr-1" data-toggle="tooltip" data-placement="top" title="Seleccionar Usuario" onclick="seleccionarUsuario('+row['idUsuarioEntePublico']+')"><i class="bi bi-plus-circle"></i></a>';
                }
            }
        ],
        bProcessing: false,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [
            {
                "width": "250px",
                "targets": 0,
                "className": "text-left",
            },
            {
                "width": "120px",
                "targets": 1,
                "className": "text-center",
            },
            {
                "width": "100px",
                "targets": 3,
                "className": "text-center",
            }],
        lengthChange: false,
        select: false
    });

}

function limpiarSearch(){

    $("#rfcSearch").val("");
    $("#curpSearch").val("");
    $("#nombreSearch").val("");
    $("#segundoNombreSearch").val("");
    $("#apellidoPaternoSearch").val("");
    $("#apellidoMaternoSearch").val("");

    paginaActual=1;
    $("#actualPage").val(paginaActual);
    sizePagina=20;
    totalPaginas=0;
    totalElementos=0;
    lastPagina=false;
    firstPagina=false;
    emptyPagina=false;

    if(tableDirectorio!=null) {
        $('#tableDirectorio').dataTable().fnClearTable();
        $('#tableDirectorio').dataTable().fnDraw();
        $('#tableDirectorio').dataTable().fnDestroy();
        tableDirectorio=null;
    }

    $("#paginationNav").hide();
    $("#infoPag").text("");

    rfcTemp="";
    curpTemp="";
    nombreTemp="";
    segundoNombreTemp="";
    apellidoPaternoTemp="";
    apellidoMaternoTemp="";

    $("#idCardTablaUsuarioSearch").hide();
}



function listAllEventListeners() {
    const allElements = Array.prototype.slice.call(document.querySelectorAll('*'));
    allElements.push(document);
    allElements.push(window);

    const types = [];

    for (let ev in window) {
        if (/^on/.test(ev)) types[types.length] = ev;
    }

    let elements = [];
    for (let i = 0; i < allElements.length; i++) {
        const currentElement = allElements[i];
        for (let j = 0; j < types.length; j++) {
            if (typeof currentElement[types[j]] === 'function') {
                elements.push({
                    "node": currentElement,
                    "type": types[j],
                    "func": currentElement[types[j]].toString(),
                });
            }
        }
    }

    return elements.sort(function(a,b) {
        return a.type.localeCompare(b.type);
    });
}

const inicializarDependencias = async () =>{
    const options={
        method: "POST",
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/entePublico/lista', options);
        const respJSON = await res.json();

        llenarCombo('#idEntePublicoSearch', 'idEntePublico','nombreEntePublicoConCve', respJSON);
        llenarCombo('#idEntePublicoSearch2', 'idEntePublico','nombreEntePublicoConCve', respJSON);
        llenarCombo('#idEntePublicoAdministra', 'idEntePublico','nombreEntePublicoConCve', respJSON);
        llenarCombo('#idEntePublicoOpera', 'idEntePublico','nombreEntePublicoConCve', respJSON);

        if ($("#isAdministradorSigtic").val()=="false") {
            $("#idEntePublicoSearch").val($("#idEntePublicoSession").val());
            $("#idEntePublicoSearch").trigger('change');
        }

        $("#idEntePublicoSearch2").val($("#idEntePublicoSession").val());
        $("#idEntePublicoSearch2").trigger('change');

        $("#idEntePublicoAdministra").val($("#idEntePublicoSession").val());
        $("#idEntePublicoAdministra").trigger('change');
        $("#idEntePublicoOpera").val($("#idEntePublicoSession").val());
        $("#idEntePublicoOpera").trigger('change');

        offLoading(null);
    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
};


function selectedSubtipoServicio(){
    if (idSubtipoServicioTemp!="")
        $("#idSubtipoServicioTic").val(idSubtipoServicioTemp);
}

function obtenerSubtipoServicio(idTipoServicio){
    if (idTipoServicio!="")
        fillSelectGet("/api/catalogo/tic/getCatSubtipoServiciosTic","#idSubtipoServicioTic", "idSubtipoServicioTic", "nombreSubtipoServicioTic", idTipoServicio, selectedSubtipoServicio);
    else {
        $("#idSubtipoServicioTic").empty();
        var dropdown =$("#idSubtipoServicioTic");
        dropdown.append($('<option></option>').attr('value', '').text('Seleccione'));
    }
}

function obtenerSubtipoServicioPlantilla(idTipoServicio){
    if (idTipoServicio!="")
        fillSelectGet("/api/catalogo/tic/getCatSubtipoServiciosTic","#idSubtipoServicioTicPlantilla", "idSubtipoServicioTic", "nombreSubtipoServicioTic", idTipoServicio, null);
    else {
        $("#idSubtipoServicioTicPlantilla").empty();
        var dropdown =$("#idSubtipoServicioTicPlantilla");
        dropdown.append($('<option></option>').attr('value', '').text('Seleccione'));
    }
}


/*Limpiar formulario*/
function limpiarServiciosTic(){
    $("#nav-data-base-tab").tab('show');
    idSubtipoServicioTemp="";
    $("#idTipoServicioTic").removeClass("is-valid");
    $("#idTipoServicioTic").removeClass("is-invalid");
    $("#idSubtipoServicioTic").removeClass("is-valid");
    $("#idSubtipoServicioTic").removeClass("is-invalid");
    $("#idSubtipoServicioTic").removeClass("is-valid");
    $("#idCveServicioTic").removeClass("is-invalid");
    $("#idCveServicioTic").removeClass("is-valid");
    $("#idNombreServicio").removeClass("is-valid");
    $("#idNombreServicio").removeClass("is-invalid");
    $("#idDescServicioTicAbr").removeClass("is-valid");
    $("#idDescServicioTicAbr").removeClass("is-invalid");
    $("#idDescServicioTic").removeClass("is-valid");
    $("#idDescServicioTic").removeClass("is-invalid");
    $("#idEstatusServicio").removeClass("is-valid");
    $("#idEstatusServicio").removeClass("is-invalid");
    $("#idEstadoServicio").removeClass("is-valid");
    $("#idEstadoServicio").removeClass("is-invalid");
    $("#idTipoImpactoServicio").removeClass("is-valid");
    $("#idTipoImpactoServicio").removeClass("is-invalid");
    $("#idNumOrdenServicio").removeClass("is-valid");
    $("#idNumOrdenServicio").removeClass("is-invalid");
    $("#idAlcanceServicioTic").removeClass("is-valid");
    $("#idAlcanceServicioTic").removeClass("is-invalid");
    $("#idObsServicioTic").removeClass("is-valid");
    $("#idObsServicioTic").removeClass("is-invalid");

    $("#idEntePublicoAdministra").removeClass( "is-valid" );
    $("#idEntePublicoAdministra").removeClass( "is-invalid" );
    $("#descUnidadAdminAdministra").removeClass( "is-valid" );
    $("#descUnidadAdminAdministra").removeClass( "is-invalid" );


    $("#idDomicilioEnte").removeClass( "is-valid" );
    $("#idDomicilioEnte").removeClass( "is-invalid" );
    $("#idUbicacionUnidadAdmtva").removeClass( "is-valid" );
    $("#idUbicacionUnidadAdmtva").removeClass( "is-invalid" );

    $("#idDomicilioEnte").removeClass( "is-valid" );
    $("#idDomicilioEnte").removeClass( "is-invalid" );
    $("#idUbicacionUnidadAdmtva").removeClass( "is-valid" );
    $("#idUbicacionUnidadAdmtva").removeClass( "is-invalid" );

    $("#idDomicilioEnteOpera").removeClass("is-valid");
    $("#idDomicilioEnteOpera").removeClass("is-invalid");
    $("#idUbicacionUnidadOpera").removeClass("is-valid");
    $("#idUbicacionUnidadOpera").removeClass("is-invalid");

    $("#idServicioTic").val("");
    $("#idTipoServicioTic").val("");
    $("#idTipoServicioTic").trigger('change');
    $("#idSubtipoServicioTic").val("");
    $("#idCveServicioTic").val("");
    $("#idNombreServicio").val("");
    $("#idDescServicioTicAbr").val("");
    $("#idDescServicioTic").val("");
    $("#idEstatusServicio").val("");
    $("#idEstadoServicio").val("");
    $("#idTipoImpactoServicio").val("");
    $('#cveEstatus').val("VIG");
    $("#idNumOrdenServicio").val("");

    $("#numInventarioGet").val("");
    $("#numInventarioCi").val("");
    $("#idAlcanceServicioTic").val("");
    $("#idObsServicioTic").val("");
    $("#descUnidadAdminAdministra").val("");
    $("#descUnidadAdminOpera").val("");
    $("#idDomicilioEnte").val("");
    $("#idDomicilioEnte").trigger('change');
    $("#idUbicacionUnidadAdmtva").val("");
    $("#idDomicilioEnteOpera").val("");
    $("#idDomicilioEnteOpera").trigger('change');
    $("#idUbicacionUnidadOpera").val("");


    $("#numeroIpServicio").val("");
    $("#idNumOrdenIpServicio").val("");
    $("#numeroIpServicio").removeClass("is-valid");
    $("#numeroIpServicio").removeClass("is-invalid");
    $("#idNumOrdenIpServicio").removeClass("is-valid");
    $("#idNumOrdenIpServicio").removeClass("is-invalid");
    $("#numeroIpServicio-errorManual").hide();
    $("#idNumOrdenIpServicio-errorManual").hide();
    //$("#idFormIpDiv").hide();
    //$("#idCardTablaIp").hide();

    /*Ocultamos las pestañas asignadas */
    $("#nav-data-ips-tab").hide();
    $("#nav-data-puertos-tab").hide();
    $("#nav-data-usuarios-tab").hide();

}

const guardarServiciosTicAsync = async() => {

    const options={
        method: "POST",
        body: JSON.stringify({
            idServicioTic:$("#idServicioTic").val(),
            tipoServicio:{idTipoServicioTic:$("#idTipoServicioTic").val()},
            subTipoServicio:{idSubtipoServicioTic:$("#idSubtipoServicioTic").val()},
            cveServicioTic: $("#idCveServicioTic").val(),
            nombreServicioTic: $("#idNombreServicio").val(),
            servicioTicAbreviadoDescripcion: $("#idDescServicioTicAbr").val(),
            servicioTicDescripcion: $("#idDescServicioTic").val(),
            estatusServicioTic: {cveEstatusServicioTic: $("#idEstatusServicio").val()},
            estadosServicioTic: {cveEstadoServicioTic: $("#idEstadoServicio").val()},
            tipoImpactoServicioTic:{cveTipoImpactoServicioTic:$("#idTipoImpactoServicio").val()},
            cveEstatus:$("#cveEstatus").val(),
            numOrdenServicioTic: $("#idNumOrdenServicio").val(),
            alcanceServicioTic: $("#idAlcanceServicioTic").val(),
            obsServicioTic: $("#idObsServicioTic").val(),
            entePublicoAdmin:{idEntePublico:$("#idEntePublicoAdministra").val()},
            unidadAdministrativaAdmin:{idUnidadAdministrativa:$("#idUnidadAdminAdministra").val()},
            entePublicoOpera:{idEntePublico:$("#idEntePublicoOpera").val()},
            unidadAdministrativaOpera:{idUnidadAdministrativa:$("#idUnidadAdminOpera").val()},
            numInventarioGet:$("#numInventarioGet").val(),
            numInventarioCi:$("#numInventarioCi").val(),
            ubicacionUnidadAdmin:{idUbicacionAdmtva:$("#idUbicacionUnidadAdmtva").val()},
            ubicacionUnidadOpera:{idUbicacionAdmtva:$("#idUbicacionUnidadOpera").val()},
        }),
        headers:headers
    };

    try {
        onLoading("dlgModalFormServicio");
        const res = await fetch(contexto+'/api/catalogo/tic/saveServicio', options);
        const respJSON = await res.json();

        if(res.ok){

            offLoading("dlgModalFormServicio");
            $('#dlgModalFormServicio').modal('hide');


             if ($("#idServicioTic").val()!=""){
                 Toast.fire({
                     icon: "success",
                     title: "Los datos del servicio se actualizaron correctamente"
                 });

                 $("#dlgModalFormServicio").modal("hide");
                 if (opServicio==1)
                    verServicio(respJSON.entePublicoAdmin.idEntePublico, respJSON.tipoServicio.idTipoServicioTic,respJSON.subTipoServicio.idSubtipoServicioTic,estatusTemp, tipoTemp);
                 else
                     searchService();

             }else{
                 Toast.fire({
                     icon: "success",
                     title: "Los datos del servicio se guardaron correctamente"
                 });

                 $("#dlgModalFormServicio").modal("hide");

                if ($("#isAdministradorSigtic").val()=="false"){
                    $("#idEntePublicoSearch").val($("#idEntePublicoAdministra").val());
                    $("#idEntePublicoSearch").trigger('change');
                }else
                    obtenTablaServicioGroup(idEntePublicoSeleccionado,$("#idUnidadAdministrativaSearch").val());
             }

        } else{

            offLoading("dlgModalFormServicio");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }
    } catch (e) {
        offLoading("dlgModalFormServicio");
        Toast.fire({
            icon: "error",
            title: "Error al guardar el servicio"
        });
    }
}

function guardarServiciosTic(){
    let titleSave='¿Guardar servicio?';
    let msgSave='¿Estas seguro que deseas guardar los datos del servicio?';
    if ($('#idServicioTic').val() != ""){
        titleSave='¿Actualizar servicio?';
        msgSave='¿Estas seguro que deseas actualizar los datos del servicio?';
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
            guardarServiciosTicAsync();
        }
    });
}

const obtenTablaServicioGroup = async(idEntePublico,idUnidadAdministrativa) => {


    $('#tableServicioGroup tbody tr').slice().remove();

    const options={
        method: "POST",
        body: JSON.stringify({
            idEntePublico: idEntePublico,
            idUnidadAdministrativa: idUnidadAdministrativa
        }),
        headers:headers
    };


    try {
        const res= await  fetch(contexto+'/api/catalogo/tic/getListaServiciosTablero', options);

        const respJSON = await res.json();

        if (res.ok){

            graficaServiciosAdministra(respJSON.graficaAdministra);
            graficaServiciosOpera(respJSON.graficaOpera);
            onLoading(null);
            $("#idAdminNumText").text(number_format(respJSON.totalAdministra,0));
            $("#idOperaNumText").text(number_format(respJSON.totalOpera,0));

            $("#totalAdminVigText").text(number_format(respJSON.totalAdministraVigente,0));
            $("#totalAdminHisText").text(number_format(respJSON.totalAdministraHistorico,0));
            $("#totalAdminText").text(number_format(respJSON.totalAdministra,0));
            $("#totalOperaVigText").text(number_format(respJSON.totalOperaVigente,0));
            $("#totalOperaHisText").text(number_format(respJSON.totalOperaHistorico,0));
            $("#totalOperaText").text(number_format(respJSON.totalOpera,0));

            $("#idAdminOn").text(number_format(respJSON.totalEncendidoAdmin,0));
            $("#idAdminOff").text(number_format(respJSON.totalApagadoAdmin,0));
            $("#idOperaOn").text(number_format(respJSON.totalEncendidoOpera,0));
            $("#idOperaOff").text(number_format(respJSON.totalApagadoOpera,0));

            listaTreeHijos=respJSON.listaTableroAdminHijos;

            let fila="";
            respJSON.listaTableroAdmin.forEach((elemento) => {
                fila = "<tr class='"+elemento.clase+"' "+elemento.estilo+">";
                fila=fila+"<td>"+ elemento.nombreServicio + "</td>";


                if (elemento.idSubTipoServicio!=null && parseInt(elemento.vigenteAdmin)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+",\"VIG\",1)'>"+number_format(elemento.vigenteAdmin,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+ number_format(elemento.vigenteAdmin,0) + "</td>";

                if (elemento.idSubTipoServicio!=null && parseInt(elemento.historicoAdmin)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+",\"HIS\",1)'>"+number_format(elemento.historicoAdmin,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+number_format(elemento.historicoAdmin,0) + "</td>";

                if (elemento.idSubTipoServicio!=null && parseInt(elemento.totalAdmin)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+","+null+",1)'>"+number_format(elemento.totalAdmin,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+ number_format(elemento.totalAdmin,0) + "</td>";

                if (elemento.idSubTipoServicio!=null && parseInt(elemento.vigenteOpera)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+",\"VIG\",2)'>"+number_format(elemento.vigenteOpera,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+ number_format(elemento.vigenteOpera,0) + "</td>";

                if (elemento.idSubTipoServicio!=null && parseInt(elemento.historicoOpera)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+",\"HIS\",2)'>"+number_format(elemento.historicoOpera,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+ number_format(elemento.historicoOpera,0) + "</td>";

                if (elemento.idSubTipoServicio!=null && parseInt(elemento.totalOpera)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+","+null+",2)'>"+number_format(elemento.totalOpera,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+ number_format(elemento.totalOpera,0) + "</td>";

                fila=fila+"</tr>";

                if (elemento.tooltip!=null){
                    fila = "<tr class='"+elemento.clase+"' "+elemento.estilo+">";
                    fila=fila+"<td><span data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='"+elemento.tooltip+"'>"+ elemento.nombreServicio + "</span></td>";
                    if (elemento.idSubTipoServicio!=null && parseInt(elemento.vigenteAdmin)>0){
                        fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+",\"VIG\",1)'>"+number_format(elemento.vigenteAdmin,0) + "</a></td>";
                    }else
                        fila=fila+"<td class='text-center'>"+ number_format(elemento.vigenteAdmin,0) + "</td>";

                    if (elemento.idSubTipoServicio!=null && parseInt(elemento.historicoAdmin)>0){
                        fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+",\"HIS\",1)'>"+number_format(elemento.historicoAdmin,0) + "</a></td>";
                    }else
                        fila=fila+"<td class='text-center'>"+ number_format(elemento.historicoAdmin,0) + "</td>";

                    if (elemento.idSubTipoServicio!=null && parseInt(elemento.totalAdmin)>0){
                        fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+","+null+",1)'>"+number_format(elemento.totalAdmin,0) + "</a></td>";
                    }else
                        fila=fila+"<td class='text-center'>"+ number_format(elemento.totalAdmin,0) + "</td>";

                    if (elemento.idSubTipoServicio!=null && parseInt(elemento.vigenteOpera)>0){
                        fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+",\"VIG\",2)'>"+number_format(elemento.vigenteOpera,0) + "</a></td>";
                    }else
                        fila=fila+"<td class='text-center'>"+ number_format(elemento.vigenteOpera,0) + "</td>";

                    if (elemento.idSubTipoServicio!=null && parseInt(elemento.historicoOpera)>0){
                        fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+",\"HIS\",2)'>"+number_format(elemento.historicoOpera,0) + "</a></td>";
                    }else
                        fila=fila+"<td class='text-center'>"+ number_format(elemento.historicoOpera,0) + "</td>";

                    if (elemento.idSubTipoServicio!=null && parseInt(elemento.totalOpera)>0){
                        fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+","+null+",2)'>"+number_format(elemento.totalOpera,0) + "</a></td>";
                    }else
                        fila=fila+"<td class='text-center'>"+ number_format(elemento.totalOpera,0) + "</td>";

                    fila=fila+"</tr>";
                }

                $('.'+elemento.clasePadre).treegrid("add",[fila]);

            });

            $("#cargadoTree").val("SI");
            $("#cargadoTree").trigger('change');

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

function mostrarTreeHijos() {

            listaTreeHijos.forEach((elemento) => {
                var fila = "<tr class='"+elemento.clase+"' "+elemento.estilo+">";
                fila=fila+"<td>"+ elemento.nombreServicio + "</td>";


                if (elemento.idSubTipoServicio!=null && parseInt(elemento.vigenteAdmin)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+",\"VIG\",1)'>"+number_format(elemento.vigenteAdmin,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+ number_format(elemento.vigenteAdmin,0) + "</td>";

                if (elemento.idSubTipoServicio!=null && parseInt(elemento.historicoAdmin)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+",\"HIS\",1)'>"+number_format(elemento.historicoAdmin,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+number_format(elemento.historicoAdmin,0) + "</td>";

                if (elemento.idSubTipoServicio!=null && parseInt(elemento.totalAdmin)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+","+null+",1)'>"+number_format(elemento.totalAdmin,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+ number_format(elemento.totalAdmin,0) + "</td>";

                if (elemento.idSubTipoServicio!=null && parseInt(elemento.vigenteOpera)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+",\"VIG\",2)'>"+number_format(elemento.vigenteOpera,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+ number_format(elemento.vigenteOpera,0) + "</td>";

                if (elemento.idSubTipoServicio!=null && parseInt(elemento.historicoOpera)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+",\"HIS\",2)'>"+number_format(elemento.historicoOpera,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+ number_format(elemento.historicoOpera,0) + "</td>";

                if (elemento.idSubTipoServicio!=null && parseInt(elemento.totalOpera)>0){
                    fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+","+null+",2)'>"+number_format(elemento.totalOpera,0) + "</a></td>";
                }else
                    fila=fila+"<td class='text-center'>"+ number_format(elemento.totalOpera,0) + "</td>";

                fila=fila+"</tr>";

                if (elemento.tooltip!=null){
                    fila = "<tr class='"+elemento.clase+"' "+elemento.estilo+">";
                    fila=fila+"<td><span data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='"+elemento.tooltip+"'>"+ elemento.nombreServicio + "</span></td>";
                    if (elemento.idSubTipoServicio!=null && parseInt(elemento.vigenteAdmin)>0){
                        fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+",\"VIG\",1)'>"+number_format(elemento.vigenteAdmin,0) + "</a></td>";
                    }else
                        fila=fila+"<td class='text-center'>"+ number_format(elemento.vigenteAdmin,0) + "</td>";

                    if (elemento.idSubTipoServicio!=null && parseInt(elemento.historicoAdmin)>0){
                        fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+",\"HIS\",1)'>"+number_format(elemento.historicoAdmin,0) + "</a></td>";
                    }else
                        fila=fila+"<td class='text-center'>"+ number_format(elemento.historicoAdmin,0) + "</td>";

                    if (elemento.idSubTipoServicio!=null && parseInt(elemento.totalAdmin)>0){
                        fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+","+null+",1)'>"+number_format(elemento.totalAdmin,0) + "</a></td>";
                    }else
                        fila=fila+"<td class='text-center'>"+ number_format(elemento.totalAdmin,0) + "</td>";

                    if (elemento.idSubTipoServicio!=null && parseInt(elemento.vigenteOpera)>0){
                        fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+",\"VIG\",2)'>"+number_format(elemento.vigenteOpera,0) + "</a></td>";
                    }else
                        fila=fila+"<td class='text-center'>"+ number_format(elemento.vigenteOpera,0) + "</td>";

                    if (elemento.idSubTipoServicio!=null && parseInt(elemento.historicoOpera)>0){
                        fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+",\"HIS\",2)'>"+number_format(elemento.historicoOpera,0) + "</a></td>";
                    }else
                        fila=fila+"<td class='text-center'>"+ number_format(elemento.historicoOpera,0) + "</td>";

                    if (elemento.idSubTipoServicio!=null && parseInt(elemento.totalOpera)>0){
                        fila=fila+"<td class='text-center fw-bold'><a href='#' onclick='verServicio("+elemento.idEntePublico+","+elemento.idTipoServicio+","+elemento.idSubTipoServicio+","+null+",2)'>"+number_format(elemento.totalOpera,0) + "</a></td>";
                    }else
                        fila=fila+"<td class='text-center'>"+ number_format(elemento.totalOpera,0) + "</td>";

                    fila=fila+"</tr>";
                }

                $("."+elemento.clasePadre).treegrid("add",[fila]);
            });
    $("#cargadoTree").val("");
    $("#cargadoTree").trigger('change');
}



function verServicio(idEntePublico, idTipoServicio, idSubTipoServicio, estatus, tipo){

    $("#dlgModalServiciosDetalle").modal("show");

    estatusTemp=estatus;
    tipoTemp=tipo;
    idEntePublicoDetalleTemp = idEntePublico;
    idTipoServicioDetalleTemp = idTipoServicio;
    idSubTipoServicioDetalleTemp = idSubTipoServicio;

    if(table!=null)
        table.clear();

    table = new DataTable('#tableServicio', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //responsive: true,
        scrollX: true,
        //pageLength: 10,
        /*fixedColumns: {
            end: 1
        },*/
        initComplete: function(settings, json) {
            $('[data-bs-toggle="tooltip"]').tooltip();
        },
        ajax: {
            url: contexto+'/api/catalogo/tic/tablaServiciosTic',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    tipoServicio:{idTipoServicioTic:idTipoServicio},
                    subTipoServicio:{idSubtipoServicioTic:idSubTipoServicio},
                    entePublicoAdmin:{idEntePublico:idEntePublico},
                    unidadAdministrativaAdmin:{idUnidadAdministrativa:$("#idUnidadAdministrativaSearch").val()},
                    tipo:tipo,
                    cveEstatus:estatus
                }));
            },
            complete: function (data) {
                var etiquetas=data['responseJSON'].etiquetas;

                $("#idTipoEnteText").text(etiquetas.tipoEntePublico);
                $("#labelEntePublico").text(etiquetas.titleEntePublico);
                $("#idEntePublicoText").text(etiquetas.entePublico);
                $("#idTipoServicioText").text(etiquetas.servicio);
                $("#idSubTipoServicioText").text(etiquetas.subServicio);
                $("#idEntePublicoTable").text(etiquetas.titleEntePublicoTabla);

                $("#idBtnDescargarServicioDetalle").remove();
                $("#idBtnDescargarServicioDetalleOpera").remove();

                var tieneDatos =
                    data['responseJSON'].aaData &&
                    data['responseJSON'].aaData.length > 0;

                let botonExportar = '';

                if (tipoTemp == 1) {

                    botonExportar =
                        '<button type="button" ' +
                        'class="btn btn-success btn-sm" ' +
                        'id="idBtnDescargarServicioDetalle" ' +
                        'onclick="descargarServiciosDetalle()"' +
                        (tieneDatos ? '' : ' disabled') +
                        '>' +
                        '<i class="fa fa-file-excel"></i> Exportar Administra' +
                        '</button>';

                } else if (tipoTemp == 2) {

                    botonExportar =
                        '<button type="button" ' +
                        'class="btn btn-warning btn-sm" ' +
                        'id="idBtnDescargarServicioDetalleOpera" ' +
                        'onclick="descargarServiciosDetalleOpera()"' +
                        (tieneDatos ? '' : ' disabled') +
                        '>' +
                        '<i class="fa fa-file-excel"></i> Exportar Opera' +
                        '</button>';
                }

                $('#tableServicio_filter')
                    .addClass('d-flex align-items-center justify-content-end gap-2')
                    .prepend(botonExportar);

                if (elimino){
                    setTimeout(() => {
                        obtenTablaServicioGroup(idEntePublicoSeleccionado,$("#idUnidadAdministrativaSearch").val());
                    }, 500);

                    elimino=false;
                }

            }
        },
        columnDefs: [
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
            }],
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        lengthChange: false,
        select: false
    });
}


const getServicio = async (idServicioTic) =>{

    $("#idControlInventario").hide();
    $("#nav-data-ips-tab").hide();
    $("#nav-data-puertos-tab").hide();
    $("#nav-data-usuarios-tab").hide();

    const options={
        method: "GET",
        header:headers
    };

    try {
        onLoading("dlgModalFormServicio");
        const res= await fetch(contexto+'/api/catalogo/tic/getServiciosTic/'+idServicioTic, options);
        const respJSON = await res.json();

        if (res.ok){
            $("#idServicioTic").val(respJSON.idServicioTic);


            if (respJSON.subTipoServicio!=null){
                idSubtipoServicioTemp=respJSON.subTipoServicio.idSubtipoServicioTic;
                $("#idSubTipoText").html("/<b>"+respJSON.subTipoServicio.nombreSubtipoServicioTic+"</b>");

                if (respJSON.subTipoServicio.controlInventario=='S'){
                    $("#idControlInventario").show();
                }

                if (respJSON.subTipoServicio.controlIp=='S'){
                    obtenServiciosRelacionadosIp(respJSON.idServicioTic);
                    $("#nav-data-ips-tab").show();
                }

                if (respJSON.subTipoServicio.controlPuerto=='S'){
                    obtenServiciosRelacionadosPuerto(respJSON.idServicioTic);
                    $("#nav-data-puertos-tab").show();
                }

                if (respJSON.subTipoServicio.controlUsuarios=='S'){
                    obtenServiciosRelacionadosUsuarios(respJSON.idServicioTic);
                    $("#nav-data-usuarios-tab").show();
                }

            }

            if (respJSON.tipoServicio!=null){
                $("#idTipoServicioTic").val(respJSON.tipoServicio.idTipoServicioTic);
                $("#idTipoServicioTic").trigger('change');
                $("#idTipoText").html("<b>"+respJSON.tipoServicio.nombreTipoServicioTic+"</b>");
            }

            $("#idClaveServicioText").html("<b>"+respJSON.cveServicioTic+"</b>");
            $("#idNombreServicioText").html("<b>"+respJSON.nombreServicioTic+"</b>");

            $("#idCveServicioTic").val(respJSON.cveServicioTic);
            $("#idNombreServicio").val(respJSON.nombreServicioTic);
            $("#idDescServicioTicAbr").val(respJSON.servicioTicAbreviadoDescripcion);
            $("#idDescServicioTic").val(respJSON.servicioTicDescripcion);

            $("#idEstatusServicio").val("");
            if (respJSON.estatusServicioTic!=null)
                $("#idEstatusServicio").val(respJSON.estatusServicioTic.cveEstatusServicioTic);

            $("#idEstadoServicio").val("");
            if (respJSON.estadosServicioTic!=null)
                $("#idEstadoServicio").val(respJSON.estadosServicioTic.cveEstadoServicioTic);

            $("#idTipoImpactoServicio").val("");
            if (respJSON.tipoImpactoServicioTic!=null)
                $("#idTipoImpactoServicio").val(respJSON.tipoImpactoServicioTic.cveTipoImpactoServicioTic);

            $("#cveEstatus").val(respJSON.cveEstatus);
            $("#idNumOrdenServicio").val(respJSON.numOrdenServicioTic);


            $("#idAlcanceServicioTic").val(respJSON.alcanceServicioTic);
            $("#numInventarioGet").val(respJSON.numInventarioGet);
            $("#numInventarioCi").val(respJSON.numInventarioCi);
            $("#idObsServicioTic").val(respJSON.obsServicioTic);

            if (respJSON.ubicacionUnidadAdmin!=null){
                idUbicacionTemp=respJSON.ubicacionUnidadAdmin.idUbicacionAdmtva
                idDomicilioEnteTemp=respJSON.ubicacionUnidadAdmin.idDomicilioEnte
            }

            if (respJSON.ubicacionUnidadOpera!=null){
                // console.log("Entre");
                // console.log(respJSON.ubicacionUnidadOpera.idUbicacionAdmtva);
                // console.log(respJSON.ubicacionUnidadOpera.idDomicilioEnte);
                idUbicacionOperaTemp=respJSON.ubicacionUnidadOpera.idUbicacionAdmtva
                idDomicilioEnteOperaTemp=respJSON.ubicacionUnidadOpera.idDomicilioEnte
            }


            if (respJSON.entePublicoAdmin!=null){
                $("#idEntePublicoAdministra").val(respJSON.entePublicoAdmin.idEntePublico);
                $("#idEntePublicoAdministra").trigger('change');
            }

            if (respJSON.unidadAdministrativaAdmin!=null){
                $("#idUnidadAdminAdministra").val(respJSON.unidadAdministrativaAdmin.idUnidadAdministrativa);
                $("#descUnidadAdminAdministra").val(respJSON.unidadAdministrativaAdmin.nombreUnidadAdministrativa);
            }
            if (respJSON.entePublicoOpera!=null && respJSON.entePublicoOpera.idEntePublico!=0){
                $("#idEntePublicoOpera").val(respJSON.entePublicoOpera.idEntePublico);
                $("#idEntePublicoOpera").trigger('change');
            }

            if (respJSON.unidadAdministrativaOpera!=null){
                $("#idUnidadAdminOpera").val(respJSON.unidadAdministrativaOpera.idUnidadAdministrativa);
                $("#descUnidadAdminOpera").val(respJSON.unidadAdministrativaOpera.nombreUnidadAdministrativa);
            }


            offLoading("dlgModalFormServicio");
        }else {
            Toast.fire({
                icon:"error",
                title: respJSON.message
            });
            offLoading("dlgModalFormServicio");
        }
    }catch (e){
        offLoading("dlgModalFormServicio");
        Toast.fire({
            icon: "error",
            title: "Error al obtener los datos"
        });
    }
}
function editarServicio(idServicioTic) {
    opServicio=1;
    limpiarServiciosTic();
    $('#titleFormServicio').text("Editar servicio");
    $('#dlgModalServiciosDetalle').modal('hide');
    $('#dlgModalFormServicio').modal('show');
    $("#idServicioTic").val(idServicioTic);
    getServicio(idServicioTic);
}

function editarServicio2(idServicioTic) {
    opServicio=2;
    limpiarServiciosTic();
    $('#titleFormServicio').text("Editar servicio");
    $('#dlgModalFormServicio').modal('show');
    $("#idServicioTic").val(idServicioTic);
    getServicio(idServicioTic);
}


const eliminarServicioAsync = async (idServicioTic) =>{
    const options={
        method: "DELETE",
        body: JSON.stringify({idServicioTic: idServicioTic}),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/catalogo/tic/deleteServiciosTic', options);
        const respJSON = await res.json();

        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Servicio eliminado correctamente"
            });

            elimino=true;
            if (opServicio==1)
                verServicio(respJSON.entePublicoAdmin.idEntePublico, respJSON.tipoServicio.idTipoServicioTic,respJSON.subTipoServicio.idSubtipoServicioTic,estatusTemp, tipoTemp);
            offLoading(null);

        }else{
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


function eliminarServicio(idServicioTic){
    opServicio=1;
    Swal.fire({
        title: 'Eliminar Servicio',
        text: '¿Estas seguro que deseas eliminar el servicio?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminarServicioAsync(idServicioTic);
        }
    })

}

function eliminarServicio2(idServicioTic){
    opServicio=2;
    Swal.fire({
        title: 'Eliminar Servicio',
        text: '¿Estas seguro que deseas eliminar el servicio?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminarServicioAsync(idServicioTic);
        }
    })

}

function graficaServiciosAdministra(datos)
{

    optionsServiciosAdmin = {
        series: datos.valores,
        labels:datos.etiquetas,
        chart: {
            //width: 600,
            type: 'donut',
            toolbar: {
                show: true
            }//,
            // events: {
            //     dataPointSelection:(event, chartContext, config) => {
            //         //console.log('seriesIndex', config.seriesIndex);
            //         //console.log('dataPointIndex', config.dataPointIndex);
            //         indexOcultar=config.dataPointIndex;
            //     },
            //     legendClick: function(chartContext, seriesIndex, config) {
            //         //indexOcultar=seriesIndex;
            //         tableroRequisiciones(chartContext.w.globals.labels[seriesIndex]);
            //     }
            // }
        },
        dataLabels: {
            enabled: false
        },
       /* title: {
            text: datos.graficaAdministra.titulo,
            align: 'center',
        },*/
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
            position: 'right',
            offsetY: 0,
            //height: 300,
        }
    };

    if (chartServiciosAdministra!=null){
        try{
            chartServiciosAdministra.destroy();
            chartServiciosAdministra=null;
        }catch(e){}
    }

    chartServiciosAdministra = new ApexCharts(document.querySelector("#graficaServiciosAdministra"), optionsServiciosAdmin);
    chartServiciosAdministra.render();

}

function graficaServiciosOpera(datos)
{

    optionsServiciosOpera = {
        series: datos.valores,
        labels:datos.etiquetas,
        chart: {
            //width: 600,
            type: 'donut',
            toolbar: {
                show: true
            }//,
            // events: {
            //     dataPointSelection:(event, chartContext, config) => {
            //         //console.log('seriesIndex', config.seriesIndex);
            //         //console.log('dataPointIndex', config.dataPointIndex);
            //         indexOcultar=config.dataPointIndex;
            //     },
            //     legendClick: function(chartContext, seriesIndex, config) {
            //         //indexOcultar=seriesIndex;
            //         tableroRequisiciones(chartContext.w.globals.labels[seriesIndex]);
            //     }
            // }
        },
        dataLabels: {
            enabled: false
        },
        /* title: {
             text: datos.graficaOpera.titulo,
             align: 'center',
         },*/
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
            position: 'right',
            offsetY: 0,
            //height: 300,
        }
    };

    if (chartServiciosOpera!=null){
        try{
            chartServiciosOpera.destroy();
            chartServiciosOpera=null;
        }catch(e){}
    }

    chartServiciosOpera = new ApexCharts(document.querySelector("#graficaServiciosOpera"), optionsServiciosOpera);
    chartServiciosOpera.render();
}

function closeModalForm(){
    $("#dlgModalFormServicio").modal("hide");

    if ($("#idServicioTic").val()!="" && opServicio==1)
        $("#dlgModalServiciosDetalle").modal("show");
}

function closeModalDescargaPlantilla(){
    $("#dlgModalDescargarPlantilla").modal("hide");

}



//region Begin sección de Guardar IP


//obtenIpServicio
function obtenServiciosRelacionadosIp(idServicio){

    if ($("#idServicioTic").val()=="")
        $("#idCardTablaIp").hide();
    else
        $("#idCardTablaIp").show();

    if(tableServiciosRelacionados!=null)
        tableServiciosRelacionados.clear();

    tableServiciosRelacionados = new DataTable('#tableServiciosRelacionados', {
        //sPaginationType: "full_numbers",
        bDestroy: true,
        ordering:false,
        bSort:false,
        ajax: {
            url: contexto+'/api/catalogo/tic/tablaServicioRelacionados',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    idServicioTic:idServicio,
                    clasificacion:'IP'
                }));
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [
            {
                "targets": 1,
                "width": "80px",
                "className": "text-center"
            },
            {
                "width": "80px",
                "targets": 2,
                "className": "text-center",
            },
            {
                "width": "60px",
                "targets": 5,
                "className": "text-center",
            }],
        lengthChange: true,
        select: false
    });
}

function obtenServiciosRelacionadosPuerto(idServicio){

    if ($("#idServicioTic").val()=="")
        $("#idCardTablaPuerto").hide();
    else
        $("#idCardTablaPuerto").show();

    if(tableServiciosRelacionadosPuerto!=null)
        tableServiciosRelacionadosPuerto.clear();

    tableServiciosRelacionadosPuerto = new DataTable('#tableServiciosRelacionadosPuerto', {
        //sPaginationType: "full_numbers",
        bDestroy: true,
        ordering:false,
        bSort:false,
        ajax: {
            url: contexto+'/api/catalogo/tic/tablaServicioRelacionados',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    idServicioTic:idServicio,
                    clasificacion:'PUERTO'
                }));
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [
            {
                "targets": 1,
                "width": "80px",
                "className": "text-center"
            },
            {
                "width": "80px",
                "targets": 2,
                "className": "text-center",
            },
            {
                "width": "60px",
                "targets": 5,
                "className": "text-center",
            }],
        lengthChange: true,
        select: false
    });
}

// correccion

function obtenServiciosRelacionadosUsuarios(idServicio){

    if ($("#idServicioTic").val()=="")
        $("#idCardTablaUsuario").hide();
    else
        $("#idCardTablaUsuario").show();

    if(tableServiciosRelacionadosUsuarios!=null)
        tableServiciosRelacionadosUsuarios.clear();

    tableServiciosRelacionadosUsuarios = new DataTable('#tableServiciosRelacionadosUsuarios', {
        //sPaginationType: "full_numbers",
        bDestroy: true,
        ordering:false,
        bSort:false,
        ajax: {
            url: contexto+'/api/catalogo/tic/tablaUsuariosServicio',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    idServicioTic:idServicio
                }));
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [
            /*{
                "targets": 0,
                "width": "90px",
                "className": "text-center"
            },*/
            {
                "targets": 1,
                "width": "70px",
                "className": "text-center"
            },
            {
                "width": "80px",
                "targets": 3,
                "className": "text-center",
            },
            {
                "width": "80px",
                "targets": 5,
                "className": "text-center",
            },
            {
                "width": "60px",
                "targets": 6,
                "className": "text-center",
            }],
        lengthChange: true,
        select: false
    });
}


function modalAddServicio(clasificacion){
    $("#servicioNameText1").text(clasificacion);
    $("#servicioNameText2").text(clasificacion);
    $("#servicioNameText3").text(clasificacion);
    $('#dlgModalFormServicio').modal('hide');
    $("#dlgModalServiciosRelacionar").modal("show");
    $("#clasificacion").val(clasificacion);
    $("#numeroServicioSearch").val("");

        if (tableServicioDisponibles!=null)
            tableServicioDisponibles.clear().draw();

}

function closeModalFormAddServicio(){
    $("#dlgModalServiciosRelacionar").modal("hide");
    $("#dlgModalFormServicio").modal("show");
}


function buscarServicio(){

    if ($("#numeroServicioSearch").val().trim().length==0){
        Toast.fire({
            icon:"error",
            title: "Teclee el valor a buscar"
        });
    }else {

        if (tableServicioDisponibles != null)
            tableServicioDisponibles.clear();

        tableServicioDisponibles = new DataTable('#tableServicioDisponibles', {
            //sPaginationType: "full_numbers",
            bDestroy: true,
            ordering: false,
            bSort: false,
            //autoWidth: false,
            ajax: {
                url: contexto + '/api/catalogo/tic/getServiciosSearch',
                type: 'POST',
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                },
                data: function (d) {
                    return JSON.stringify($.extend({}, d, {
                        idServicioTic: $("#idServicioTic").val(),
                        nombreServicioTic: $("#numeroServicioSearch").val(),
                        clasificacion: $("#clasificacion").val(),
                        sinFiltrar: $('#sinFiltrar').prop('checked')
                    }));
                }
            },
            bProcessing: true,
            bServerSide: false,
            language: {
                "url": contexto + '/include/datatables/spanish.json',
            },
            columnDefs: [
                {
                    "targets": 1,
                    "width": "80px",
                    "className": "text-center"
                },
                {
                    "width": "80px",
                    "targets": 2,
                    "className": "text-center",
                },
                {
                    "width": "60px",
                    "targets": 5,
                    "className": "text-center",
                }],
            lengthChange: true,
            select: false
        });
    }
}

const agregarServicio = async (idServicioTic) =>{

    const options={
        method: "GET",
        header:headers
    };
    try {
        onLoading("dlgModalServiciosRelacionar");
        const res= await fetch(contexto+'/api/catalogo/tic/getServiciosTic/'+idServicioTic, options);
        const respJSON = await res.json();

        if (res.ok){
            if ($("#clasificacion").val()=="IP"){

                $("#idServicioAddIp").val(respJSON.idServicioTic);
                $("#nombreServicioIp").val(respJSON.nombreServicioTic);
            }
            if ($("#clasificacion").val()=="PUERTO"){

                $("#idServicioAddPuerto").val(respJSON.idServicioTic);
                $("#nombreServicioPuerto").val(respJSON.nombreServicioTic);
            }

            if ($("#clasificacion").val()=="USUARIOS"){

                $("#idServicioAddUsuario").val(respJSON.idServicioTic);
                $("#nombreServicioUsuario").val(respJSON.nombreServicioTic);
            }

            offLoading("dlgModalServiciosRelacionar");
            $("#dlgModalServiciosRelacionar").modal("hide");
            $("#dlgModalFormServicio").modal("show");


        }else {
            Toast.fire({
                icon:"error",
                title: respJSON.message
            });
            offLoading("dlgModalServiciosRelacionar");
        }
    }catch (e){
        offLoading("dlgModalServiciosRelacionar");
        Toast.fire({
            icon: "error",
            title: "Error al obtener los datos"
        });
    }
}



function addServicio(clasificacion) {

    var errorTempo = 0;

    if (clasificacion == "IP") {
        $("#idNumOrdenIp").val($("#idNumOrdenIp").val().trim());
        $("#idDescripcionIp").val($("#idDescripcionIp").val().trim());
        $("#idNotaIp").val($("#idNotaIp").val().trim());

        $("#idNumOrdenIp").removeClass("is-valid");
        $("#idNumOrdenIp").removeClass("is-invalid");
        $("#idDescripcionIp").removeClass("is-valid");
        $("#idDescripcionIp").removeClass("is-invalid");



        if ($('#idNumOrdenIp').val() == "") {
            $("#idNumOrdenIp").addClass("is-invalid");
            $("#idNumOrdenIp-errorManual").show();
            errorTempo++;
        } else
            $("#idNumOrdenIp").addClass("is-valid");

        if ($('#idNumOrdenIp').val() != "") {
            if (parseInt($('#idNumOrdenIp').val()) < 0)
                $('#idNumOrdenIp').val("");
        }

        if ($('#idNumOrdenIp').val() == "") {
            $("#idNumOrdenIp").addClass("is-invalid");
            $("#idNumOrdenIp-errorManual").show();
            errorTempo++;
        } else
            $("#idNumOrdenIp").addClass("is-valid");

        if ($('#idDescripcionIp').val() == "") {
            $("#idDescripcionIp").addClass("is-invalid");
            $("#idDescripcionIp-errorManual").show();
            errorTempo++;
        } else
            $("#idDescripcionIp").addClass("is-valid");

        if ($("#idServicioAddIp").val()==""){
            Toast.fire({
                icon: "error",
                title: "Seleccione la ip"
            });
            errorTempo++;
        }

    }else
        if (clasificacion == "PUERTO") {
        $("#idNumOrdenPuerto").val($("#idNumOrdenPuerto").val().trim());
        $("#idDescripcionPuerto").val($("#idDescripcionPuerto").val().trim());
        $("#idNotaPuerto").val($("#idNotaPuerto").val().trim());

        $("#idNumOrdenPuerto").removeClass("is-valid");
        $("#idNumOrdenPuerto").removeClass("is-invalid");
        $("#idDescripcionPuerto").removeClass("is-valid");
        $("#idDescripcionPuerto").removeClass("is-invalid");

        if ($('#idNumOrdenPuerto').val() == "") {
            $("#idNumOrdenPuerto").addClass("is-invalid");
            $("#idNumOrdenPuerto-errorManual").show();
            errorTempo++;
        } else
            $("#idNumOrdenPuerto").addClass("is-valid");

        if ($('#idNumOrdenPuerto').val() != "") {
            if (parseInt($('#idNumOrdenPuerto').val()) < 0)
                $('#idNumOrdenPuerto').val("");
        }

        if ($('#idNumOrdenPuerto').val() == "") {
            $("#idNumOrdenPuerto").addClass("is-invalid");
            $("#idNumOrdenPuerto-errorManual").show();
            errorTempo++;
        } else
            $("#idNumOrdenPuerto").addClass("is-valid");

        if ($('#idDescripcionPuerto').val() == "") {
            $("#idDescripcionPuerto").addClass("is-invalid");
            $("#idDescripcionPuerto-errorManual").show();
            errorTempo++;
        } else
            $("#idDescripcionPuerto").addClass("is-valid");

        if ($("#idServicioAddPuerto").val()==""){
            Toast.fire({
                icon: "error",
                title: "Seleccione el puerto"
            });
            errorTempo++;
        }

    }

    if (errorTempo == 0) {
        addServicioBd();
    }

}


const addServiciosTicAsync = async() => {

    let idNumOrdenTemp = "";
    let idDescripcionTemp= "";
    let idNotaTemp = "";
    let idServicioTemp="";

    if ($("#clasificacion").val()=="IP"){
        idNumOrdenTemp = $("#idNumOrdenIp").val();
        idDescripcionTemp = $("#idDescripcionIp").val();
        idNotaTemp = $("#idNotaIp").val();
        idServicioTemp=$("#idServicioAddIp").val();
    }

    if ($("#clasificacion").val()=="PUERTO"){
        idNumOrdenTemp = $("#idNumOrdenPuerto").val();
        idDescripcionTemp = $("#idDescripcionPuerto").val();
        idNotaTemp = $("#idNotaPuerto").val();
        idServicioTemp=$("#idServicioAddPuerto").val();
    }

    if ($("#clasificacion").val()=="USUARIOS"){
        idNumOrdenTemp = $("#idNumOrdenUsuario").val();
        idDescripcionTemp = $("#idDescripcionUsuario").val();
        idNotaTemp = $("#idNotaUsuario").val();
        idServicioTemp=$("#idServicioAddUsuario").val();
    }

    const options={
        method: "POST",
        body: JSON.stringify({
            serviciosTicPadre:{idServicioTic:$("#idServicioTic").val()},
            serviciosTic:{idServicioTic:idServicioTemp},
            descripcionServicioTic:idDescripcionTemp,
            notaServicioTicRelacionado:idNotaTemp,
            numeroOrdenServicio:idNumOrdenTemp
        }),
        headers:headers
    };

    try {
        onLoading("dlgModalFormServicio");
        const res = await fetch(contexto+'/api/catalogo/tic/addServicio', options);
        const respJSON = await res.json();

        if(res.ok){
            offLoading("dlgModalFormServicio");

            if ($("#clasificacion").val()=="IP"){

            $("#idServicioAddIp").val("");
            $("#nombreServicioIp").val("");
            $("#idNumOrdenIp").val("");
            $("#idDescripcionIp").val("");
            $("#idNotaIp").val("");

                $("#idNumOrdenIp").removeClass("is-valid");
                $("#idNumOrdenIp").removeClass("is-invalid");
                $("#idDescripcionIp").removeClass("is-valid");
                $("#idDescripcionIp").removeClass("is-invalid");

                obtenServiciosRelacionadosIp($("#idServicioTic").val());

                Toast.fire({
                    icon: "success",
                    title: "IP agregada correctamente"
                });
            }else
                if ($("#clasificacion").val()=="PUERTO"){

                $("#idServicioAddPuerto").val("");
                $("#nombreServicioPuerto").val("");
                $("#idNumOrdenPuerto").val("");
                $("#idDescripcionPuerto").val("");
                $("#idNotaPuerto").val("");

                $("#idNumOrdenPuerto").removeClass("is-valid");
                $("#idNumOrdenPuerto").removeClass("is-invalid");
                $("#idDescripcionPuerto").removeClass("is-valid");
                $("#idDescripcionPuerto").removeClass("is-invalid");

                obtenServiciosRelacionadosPuerto($("#idServicioTic").val());

                Toast.fire({
                    icon: "success",
                    title: "Puerto agregado correctamente"
                });
            }else
                if ($("#clasificacion").val()=="USUARIOS"){

                    $("#idServicioAddUsuario").val("");
                    $("#nombreServicioUsuario").val("");
                    $("#idNumOrdenUsuario").val("");
                    $("#idDescripcionUsuario").val("");
                    $("#idNotaUsuario").val("");

                    $("#idNumOrdenUsuario").removeClass("is-valid");
                    $("#idNumOrdenUsuario").removeClass("is-invalid");
                    $("#idDescripcionUsuario").removeClass("is-valid");
                    $("#idDescripcionUsuario").removeClass("is-invalid");

                    obtenServiciosRelacionadosUsuarios($("#idServicioTic").val());

                    Toast.fire({
                        icon: "success",
                        title: "Usuario agregado correctamente"
                    });
                }


        } else{

            offLoading("dlgModalFormServicio");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }
    } catch (e) {
        offLoading("dlgModalFormServicio");
        Toast.fire({
            icon: "error",
            title: "Error al agregar el servicio"
        });
    }
}

function addServicioBd(){
    let titleSave='¿Agregar Servicio?';
    let msgSave='¿Estas seguro que deseas agregar el servicio?';
    /*if ($('#idServicioAddIp').val() != ""){
        titleSave='¿Actualizar Servicio?';
        msgSave='¿Estas seguro que deseas actualizar el servicio?';
    }*/

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
            addServiciosTicAsync();
        }
    });
}

const eliminarServicioRelacionadoAsync = async (idServicioRelacionado, clasificacion) =>{
    const options={
        method: "DELETE",
        body: JSON.stringify({idServicioTicRelacionado: idServicioRelacionado}),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/catalogo/tic/deleteServiciosRelacionado', options);
        const respJSON = await res.json();

        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Servicio relacionado eliminado correctamente"
            });

            if (clasificacion=="IP"){
                obtenServiciosRelacionadosIp($("#idServicioTic").val());
            }
                if (clasificacion=="PUERTO"){
                obtenServiciosRelacionadosPuerto($("#idServicioTic").val());
            }

            if (clasificacion=="USUARIOS"){
                obtenServiciosRelacionadosUsuarios($("#idServicioTic").val());
            }


            offLoading(null);

        }else{
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


function eliminarServicioRelacionado(idServicioRelacionado, clasificacion){

    Swal.fire({
        title: 'Eliminar Servicio Relacionado',
        text: '¿Estas seguro que deseas eliminar el servicio Relacionado?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminarServicioRelacionadoAsync(idServicioRelacionado,clasificacion);
        }
    })

}


function modalAddUsuario(){
    limpiarFormularioUsuaio();
    $("#dlgModalFormAddUsuario").modal("show")

}

//endregion End sección de Guardar IP


function seleccionarUsuario(idUsuarioEntePublico) {


    $('#preloader').modal('show');
    $.ajax({
        url: contexto + '/api/catalogoCatUsuarioEntePublico/getUsuarioEntePublicoId/' + idUsuarioEntePublico,
        type: 'GET',
        dataType: 'JSON',
        contentType: 'application/json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function (response) {
            if (response.idUsuarioEntePublico!=null) {

                if (modalSearchUser == 2) {
                    $("#curpSearch2").val(response.catUsuario.curp);
                }
                else{


                    if (response.catUsuario != null) {
                        $("#idUsuarioEntePublico").val(response.idUsuarioEntePublico);
                        $("#rfc").val(response.catUsuario.rfc);
                        $("#curp").val(response.catUsuario.curp);
                        $("#nombreCompleto").val(response.catUsuario.nombreCompleto);
                    }

                    if (response.entePublico != null) {
                        $("#entePublicoUsuario").val(response.entePublico.nombreEntePublico);
                    }

                    if (response.unidadAdministrativa != null) {
                        $("#unidadAdministrativaUsuario").val(response.unidadAdministrativa.nombreUnidadAdministrativa);
                    }
                }

            }
            $('#preloader').modal('hide');
            $('#dlgModalSearchUsuario').modal('hide');

        },
        error: function (response) {
            var error = JSON.parse(response.responseText);
            $('#dlgModalSearchUsuario').modal('hide');
            $('#preloader').modal('hide');
            Swal.fire({
                title: 'Error',
                text: error.message,
                icon: 'error',
            });
        }
    });
}

function limpiarFormularioUsuaio()
{
    $("#idTitleAddUsuario").text("Agregar Usuario");
    $("#idTipoUsuarioServicio").removeClass("is-valid");
    $("#idTipoUsuarioServicio").removeClass("is-invalid");
    $("#rfc").removeClass("is-valid");
    $("#rfc").removeClass("is-invalid");
    $("#curp").removeClass("is-valid");
    $("#curp").removeClass("is-invalid");
    $("#fechaAltaServicio").removeClass("is-valid");
    $("#fechaAltaServicio").removeClass("is-invalid");
    $("#fechaBajaServicio").removeClass("is-valid");
    $("#fechaBajaServicio").removeClass("is-invalid");

    $("#idUsuarioServicioTic").val("");
    $("#idTipoUsuarioServicio").val("");
    $("#idUsuarioEntePublico").val("");
    $("#rfc").val("");
    $("#curp").val("");
    $("#nombreCompleto").val("");
    $("#entePublicoUsuario").val("");
    $("#unidadAdministrativaUsuario").val("");
    $("#folioSima").val("");
    $("#fechaSolicitudSima").val("");
    $("#fechaAltaServicio").val("");
    $("#fechaBajaServicio").val("");
    $("#idAlcanceServicio").val("");
    $("#idObsServicio").val("");
    $("#correoInstitucionalUsuario").val("");
    $("#correoPersonalUsuario").val("");
}

const guardarUSuarioServicioAsync = async() => {

    const options={
        method: "POST",
        body: JSON.stringify({
            idUsuarioServicio: $("#idUsuarioServicioTic").val() || null,
            catTipoUsuarioServicio:{idTipoUsuario:$("#idTipoUsuarioServicio").val()},
            serviciosTic:{idServicioTic: $("#idServicioTic").val()},
            catUsuarioEntePublico:{idUsuarioEntePublico:$("#idUsuarioEntePublico").val()},
            curpUsuarioServicio:$("#curp").val(),
            folioSima:$("#folioSima").val(),
            fechaSolicitudSima:$("#fechaSolicitudSima").val(),
            //curpTenicoServicio:$("#curpTenico").val(),
            fechaAltaServicio:$("#fechaAltaServicio").val(),
            fechaBajaServicio:$("#fechaBajaServicio").val(),
            alcanceServicio:$("#idAlcanceServicio").val(),
            observacionServicio:$("#idObsServicio").val(),
            correoInstitucionalUsuario:$("#correoInstitucionalUsuario").val(),
            correoPersonalUsuario:$("#correoPersonalUsuario").val()
        }),
        headers:headers
    };

    try {
        onLoading("dlgModalFormAddUsuario");
        const res = await fetch(contexto+'/api/catalogo/tic/saveUsuarioServicio', options);
        const respJSON = await res.json();

        if(res.ok){

            offLoading("dlgModalFormAddUsuario");
            $('#dlgModalFormAddUsuario').modal('hide');


            const esEdicion = $("#idUsuarioServicioTic").val() !== "";

            Toast.fire({
                icon: "success",
                title: esEdicion ? "Usuario actualizado correctamente" : "Usuario agregado correctamente"
            });

            obtenServiciosRelacionadosUsuarios($("#idServicioTic").val());

                $("#dlgModalFormAddUsuario").modal("hide");

        } else{

            offLoading("dlgModalFormAddUsuario");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }
    } catch (e) {
        offLoading("dlgModalFormAddUsuario");
        Toast.fire({
            icon: "error",
            title: "Error al guardar el usuario servicio"
        });
    }
}

function guardarUSuarioServicio(){
    let titleSave='¿Guardar usuario servicio?';
    let msgSave='¿Estas seguro que deseas agregar el usuario?';

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
            guardarUSuarioServicioAsync();
        }
    });
}

function formatoFechaISO(fechaDDMMYYYY) {
    if (!fechaDDMMYYYY) return "";
    const partes = fechaDDMMYYYY.split("/");
    return partes.length === 3 ? `${partes[2]}-${partes[1]}-${partes[0]}` : "";
}

function editarUsuarioServicio(idUsuarioServicioTic) {
    limpiarFormularioUsuaio();
    $("#idTitleAddUsuario").text("Editar Usuario");
    $('#preloader').modal('show');

    $.ajax({
        url: contexto + '/api/catalogo/tic/getUsuarioServicio/' + idUsuarioServicioTic,
        type: 'GET',
        dataType: 'JSON',
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function (response) {
            $("#idUsuarioServicioTic").val(response.idUsuarioServicio);

            if (response.catTipoUsuarioServicio != null) {
                $("#idTipoUsuarioServicio").val(response.catTipoUsuarioServicio.idTipoUsuario);
            }

            if (response.catUsuarioEntePublico != null) {
                $("#idUsuarioEntePublico").val(response.catUsuarioEntePublico.idUsuarioEntePublico);

                if (response.catUsuarioEntePublico.catUsuario != null) {
                    $("#rfc").val(response.catUsuarioEntePublico.catUsuario.rfc);
                    $("#nombreCompleto").val(response.catUsuarioEntePublico.catUsuario.nombreCompleto);
                } else {
                    $("#rfc").val(response.rfcFallback || "");
                    $("#nombreCompleto").val(response.nombreCompletoFallback || "");
                }
            } else {
                $("#idUsuarioEntePublico").val("");
                $("#rfc").val(response.rfcFallback || "");
                $("#nombreCompleto").val(response.nombreCompletoFallback || "");
            }

            $("#curp").val(response.curpUsuarioServicio);

            if (response.entePublicoUsuarioServicio != null) {
                $("#entePublicoUsuario").val(response.entePublicoUsuarioServicio.nombreEntePublico);
            }
            if (response.unidadAdministrativaUsuarioServicio != null) {
                $("#unidadAdministrativaUsuario").val(response.unidadAdministrativaUsuarioServicio.nombreUnidadAdministrativa);
            }
            $("#folioSima").val(response.folioSima);
            $("#fechaSolicitudSima").val(formatoFechaISO(response.fechaSolicitudSimaText));
            $("#fechaAltaServicio").val(formatoFechaISO(response.fechaAltaServicioText));
            $("#fechaBajaServicio").val(formatoFechaISO(response.fechaBajaServicioText));
            $("#idAlcanceServicio").val(response.alcanceServicio);
            $("#idObsServicio").val(response.observacionServicio);
            $("#correoInstitucionalUsuario").val(response.correoInstitucionalUsuario);
            $("#correoPersonalUsuario").val(response.correoPersonalUsuario);

            $('#preloader').modal('hide');
            $("#dlgModalFormAddUsuario").modal("show");
        },
        error: function () {
            $('#preloader').modal('hide');
            Toast.fire({ icon: "error", title: "No se pudo cargar el usuario" });
        }
    });
}

const eliminarUsuarioServicioAsync = async (idUsuarioServicio) => {
    const options = {
        method: "DELETE",
        body: JSON.stringify({idUsuarioServicio: idUsuarioServicio}),
        headers: headers
    };

    try {
        onLoading("dlgModalFormAddUsuario");
        const res = await fetch(contexto + '/api/catalogo/tic/deleteUsuarioServicio', options);
        const respJSON = await res.json();

        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Registro eliminado correctamente"
            });

            obtenServiciosRelacionadosUsuarios($("#idServicioTic").val());

            offLoading("dlgModalFormAddUsuario");

        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalFormAddUsuario");
        }

    } catch (e) {
        var error = JSON.parse(e.responseText);
        offLoading("dlgModalFormAddUsuario");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
};

function eliminarUsuarioServicio(idUsuarioServicio) {
    let titleDelete = '¿Eliminar usuario servicio?';
    let msgDelete = '¿Estas seguro que deseas eliminar este registro?';

    Swal.fire({
        title: titleDelete,
        text: msgDelete,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Si, eliminar',
        cancelButtonText: "Cancelar"
    }).then(function (result) {
        if (result.isConfirmed) {
            eliminarUsuarioServicioAsync(idUsuarioServicio);
        }
    });
}

function getDomicilioEntePublico(idPadre,callback){
    if (idPadre!=null)
        fillSelectGetInterno('/api/catalogo/domicilioentePublico/all', '#idDomicilioEnte','idDomicilio','nombreDomicilio',idPadre, callback, "domicilio");
    else
        $('#idDomicilioEnte').empty();
}

function getDomicilioEntePublicoOpera(idPadre,callback){
    if (idPadre!=null)
        fillSelectGetInterno('/api/catalogo/domicilioentePublico/all', '#idDomicilioEnteOpera','idDomicilio','nombreDomicilio',idPadre, callback, "domicilioOpera");
    else
        $('#idDomicilioEnteOpera').empty();
}

function actualizarUbicaciones() {
    if ($("#idDomicilioEnte").val()!="")
        getUbicacionUnidad($("#idDomicilioEnte").val());
    else
        $('#idUbicacionUnidadAdmtva').empty();
}

function actualizarUbicacionesOpera() {
    if ($("#idDomicilioEnteOpera").val()!="")
        getUbicacionUnidadOpera($("#idDomicilioEnteOpera").val());
    else
        $('#idUbicacionUnidadOpera').empty();
}

function getUbicacionUnidad(idPadre, callback){
    if (idPadre!=null)
        fillSelectGetInterno('/api/catalogo/ubicacionUnidadAministrativa/all', '#idUbicacionUnidadAdmtva','idUbicacionAdaministrativa','nombreUbicacion',idPadre, callback, "ubicacion");
    else
        $('#idUbicacionUnidadAdmtva').empty();
}

function getUbicacionUnidadOpera(idPadre, callback){
    if (idPadre!=null)
        fillSelectGetInterno('/api/catalogo/ubicacionUnidadAministrativa/all', '#idUbicacionUnidadOpera','idUbicacionAdaministrativa','nombreUbicacion',idPadre, callback, "ubicacionOpera");
    else
        $('#idUbicacionUnidadOpera').empty();
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

       /* if (opcion=="categoria") {
            getDomicilioEntePublico($('#idEntePublicoAdministra').val());
        }

        if (opcion=="categoriaOpera") {
            getDomicilioEntePublicoOpera($('#idEntePublicoOpera').val());
        }*/

        if (opcion=="domicilio") {
            if (idDomicilioEnteTemp != null && idDomicilioEnteTemp != -1) {
                $("#idDomicilioEnte").val(idDomicilioEnteTemp);
                $("#idDomicilioEnte").trigger('change');
            }
        }
        if (opcion=="domicilioOpera") {
            if (idDomicilioEnteOperaTemp != null && idDomicilioEnteOperaTemp != -1) {
                $("#idDomicilioEnteOpera").val(idDomicilioEnteOperaTemp);
                $("#idDomicilioEnteOpera").trigger('change');
            }
        }


        if (opcion=="ubicacion") {
            if (idUbicacionTemp != null && idUbicacionTemp != -1) {
                $("#idUbicacionUnidadAdmtva").val(idUbicacionTemp);
                //$("#idUbicacionUnidadAdmtva").trigger('change');
            }
        }

        if (opcion=="ubicacionOpera") {
            if (idUbicacionOperaTemp != null && idUbicacionOperaTemp != -1) {
                $("#idUbicacionUnidadOpera").val(idUbicacionOperaTemp);
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

function searchServicio(jsonData){

    if(tableSearch!=null)
        tableSearch.clear();

    tableSearch = new DataTable('#tableServicioSearch', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        bSort: false,
        paging: false,
        info: false,
        responsive: true,
        scrollX: true,
        searching: false,
        initComplete: function(settings, json) {
            $('[data-bs-toggle="tooltip"]').tooltip();
        },
        data: jsonData,
        columns: [
            { data: 'idServicioTic'},
            { data: 'tipoServicio.nombreTipoServicioTic',
                render: function (data, type, row, meta) {
                    return row['tipoServicio'].nombreTipoServicioTic+" <b><br/>("+row['subTipoServicio'].nombreSubtipoServicioTic+")</b>";
                }
            },
            { data: 'cveServicioTic'},
            { data: 'nombreServicioTic' },
            { data: 'entePublicoOpera.nombreEntePublico'},
            { data: 'estadosServicioTic.descEstadoServicioTic' },
            { data: 'estatusServicioTic.descEstatusServicioTic' },
            { data: 'cveEstatus' },
            { data: 'nombreServicioTic',
                render: function (data, type, row, meta) {
                    let accion="<div class='btn-group' role=group' aria-label=Basic example'>";

                    if ($("#isAdministradorSigtic").val()=="true" ||  parseInt($("#idEntePublicoSession").val())==parseInt(row['entePublicoAdmin'].idEntePublico)) {
                        if ($("#activeEditar").val()=="true")
                            accion += "<a href='#' class='btn btn-success btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Editar Servicio' onclick='editarServicio2(" + row['idServicioTic'] + ")'><i class='bi bi-pencil-square'></i></a>"
                        if ($("#activeEliminar").val()=="true")
                            accion += "<a href='#' class='btn btn-danger btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Servicio' onclick='eliminarServicio2(" + row['idServicioTic'] + ")'><i class='bi bi-trash3'></i></a>"
                    }
                    accion += "</div>"
                    return accion;
                }
            }
        ],
        columnDefs: [
            {
                "targets": 0,
                "width": "100px",
                "className": "text-left",
            },
            {
                "targets": 1,
                "width": "150px",
                "className": "text-left",
            },
            {
                "targets": 2,
                "width": "150px",
                "className": "text-left",
            },  {
                "targets": 7,
                "width": "100px",
                "className": "text-center",
            },  {
                "targets": 8,
                "width": "100px",
                "className": "text-center",
            }],
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        lengthChange: false,
        select: false
    });
}

function searchServicioUser(jsonData){

    if(tableSearch!=null)
        tableSearch.clear();

    tableSearch = new DataTable('#tableServicioSearch', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        bSort: false,
        paging: false,
        info: false,
        responsive: true,
        scrollX: true,
        searching: false,
        initComplete: function(settings, json) {
            $('[data-bs-toggle="tooltip"]').tooltip();
        },
        data: jsonData,
        columns: [
            { data: 'serviciosTic.idServicioTic'},
            { data: 'serviciosTic.tipoServicio.nombreTipoServicioTic',
                render: function (data, type, row, meta) {
                    let tipo = row.serviciosTic.tipoServicio.nombreTipoServicioTic
                    let subtipo = row.serviciosTic.subTipoServicio.nombreSubtipoServicioTic

                    return tipo+" <b><br/>("+subtipo+")</b>";
                }
            },
            { data: 'serviciosTic.cveServicioTic'},
            { data: 'serviciosTic.nombreServicioTic' },
            { data: 'serviciosTic.entePublicoOpera.nombreEntePublico'},
            { data: 'serviciosTic.estadosServicioTic.descEstadoServicioTic' },
            { data: 'serviciosTic.estatusServicioTic.descEstatusServicioTic' },
            { data: 'serviciosTic.cveEstatus' },
            { data: 'serviciosTic.nombreServicioTic',
                render: function (data, type, row, meta) {
                    let accion="<div class='btn-group' role=group' aria-label=Basic example'>";

                    if ($("#isAdministradorSigtic").val()=="true" ||  parseInt($("#idEntePublicoSession").val())==parseInt(row.serviciosTic.entePublicoAdmin.idEntePublico)) {
                        if ($("#activeEditar").val()=="true")
                            accion += "<a href='#' class='btn btn-success btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Editar Servicio' onclick='editarServicio2(" + row.serviciosTic.idServicioTic + ")'><i class='bi bi-pencil-square'></i></a>"
                        if ($("#activeEliminar").val()=="true")
                            accion += "<a href='#' class='btn btn-danger btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Servicio' onclick='eliminarServicio2(" + row.serviciosTic.idServicioTic + ")'><i class='bi bi-trash3'></i></a>"
                    }
                    accion += "</div>"
                    return accion;
                }
            }
        ],
        columnDefs: [
            {
                "targets": 0,
                "width": "150px",
                "className": "text-left",
            },
            {
                "targets": 1,
                "width": "150px",
                "className": "text-left",
            },
            {
                "targets": 2,
                "width": "150px",
                "className": "text-left",
            },  {
                "targets": 7,
                "width": "100px",
                "className": "text-center",
            },  {
                "targets": 8,
                "width": "100px",
                "className": "text-center",
            }],
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        lengthChange: false,
        select: false
    });
}

function limpiarService(){
    $("#idCveServicioTicSearch").val("");
    $("#idNombreServicioSearch").val("");
    $("#curpSearch2").val("");
}

const searchService = async () => {

    $("#tableServicioSearch").show();
    let parametros = {
        entePublicoAdmin:{idEntePublico:$("#idEntePublicoSearch2").val()},
        cveServicioTic:$("#idCveServicioTicSearch").val(),
        nombreServicioTic:$("#idNombreServicioSearch").val(),
        curp:$("#curpSearch2").val()
    };


    parametros.pagina=paginaActualSearch-1;
    parametros.size=sizePaginaSearch;

    const options={
        method: "POST",
        body: JSON.stringify(parametros),
        headers: {
            "X-CSRF-Token":  $('input[name="_csrf"]').val(),
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    onLoading(null);
    const res = await fetch(contexto + '/api/catalogo/tic/serviciosSearch', options);
    const respJSON = await res.json();
    if (res.ok){

        totalPaginasSearch=respJSON.totalPages;
        totalElementosSearch=respJSON.totalElements;
        lastPaginaSearch=respJSON.last;
        firstPaginaSearch=respJSON.first;
        emptyPaginaSearch=respJSON.empty;

        if (emptyPaginaSearch)
            $("#paginationNavSearch").hide();
        else{
            $("#paginationNavSearch").show();

            if (firstPaginaSearch){
                $("#primeraPagSearch").addClass("disabled")
                $("#previousPagSearch").addClass("disabled")
            }else{
                $("#primeraPagSearch").removeClass("disabled")
                $("#previousPagSearch").removeClass("disabled")
            }
            if (lastPaginaSearch){
                $("#nextPagSearch").addClass("disabled")
                $("#lastPagSearch").addClass("disabled")
            }else{
                $("#nextPagSearch").removeClass("disabled")
                $("#lastPagSearch").removeClass("disabled")
            }

            $("#infoPagSearch").text("Pag. "+paginaActualSearch+" de "+totalPaginasSearch);

            $("#actualPageSearch").val(paginaActualSearch);

            if (lastPaginaSearch)
                $("#totalRegistroSearch").text("Mostrando "+((paginaActualSearch*sizePaginaSearch)-sizePaginaSearch+1)+" - "+totalElementosSearch+" de "+ totalElementosSearch);
            else
                $("#totalRegistroSearch").text("Mostrando "+((paginaActualSearch*sizePaginaSearch)-sizePaginaSearch+1)+" - "+(paginaActualSearch*sizePaginaSearch)+" de "+ totalElementosSearch);
        }

        // console.log(respJSON.content);
        if ($("#curpSearch2").val()!=null && $("#curpSearch2").val()!="")
            searchServicioUser(respJSON.content);
        else
            searchServicio(respJSON.content);

        offLoading(null);
    }else {
        Toast.fire({
            icon: "error",
            title: respJSON.message
        });
        offLoading(null);
    }
}

function primeraPaginaSearch(){
    if (!firstPaginaSearch && totalPaginasSearch>0){
        paginaActualSearch=1;
        $("#actualPageSearch").val(paginaActualSearch);
        searchService();
    }
}

function anteriorPaginaSearch(){
    if (!firstPaginaSearch && totalPaginasSearch>0){
        paginaActualSearch=parseInt(paginaActualSearch)-1;
        $("#actualPageSearch").val(paginaActualSearch);
        searchService();
    }
}

function ultimaPaginaSearch(){

    if (!lastPaginaSearch && totalPaginasSearch>0){
        paginaActualSearch=totalPaginasSearch;
        $("#actualPageSearch").val(paginaActualSearch);
        searchService();
    }
}
function siguientePaginaSearch(){
    if (!lastPaginaSearch && totalPaginasSearch>0){
        paginaActualSearch=parseInt(paginaActualSearch)+1;
        $("#actualPageSearch").val(paginaActualSearch);
        searchService();
    }
}

function ocultarBarra() {

    if (ocultar == false) {
        ocultar = true;
        $("#idServicioLeft").hide();
        $("#idIcoControl").removeClass("fa-angle-double-left");
        $("#idIcoControl").addClass("fa-angle-double-right");
        $("#idServicioPrincipal").removeClass("col-xs-12 col-sm-12 col-md-8 col-xxl-9 col-lg-9");
        $("#idServicioPrincipal").addClass("col-xs-12 col-sm-12 col-md-12 col-xxl-12 col-lg-12");

    } else {
        ocultar = false;
        $("#idIcoControl").removeClass("fa-angle-double-right");
        $("#idIcoControl").addClass("fa-angle-double-left");
        $("#idServicioLeft").show();

        $("#idServicioPrincipal").removeClass("col-xs-12 col-sm-12 col-md-12 col-xxl-12 col-lg-12");
        $("#idServicioPrincipal").addClass("col-xs-12 col-sm-12 col-md-8 col-xxl-9 col-lg-9");
    }

}
function descargarServiciosDetalle() {

    let params = new URLSearchParams({
        idEntePublico: idEntePublicoDetalleTemp,
        idUnidadAdministrativa: $("#idUnidadAdministrativaSearch").val() || "",
        idTipoServicio: idTipoServicioDetalleTemp,
        idSubtipoServicio: idSubTipoServicioDetalleTemp,
        tipo: tipoTemp,

        tipoEntePublicoText: $("#idTipoEnteText").text(),
        entePublicoText: $("#idEntePublicoText").text(),
        tipoServicioText: $("#idTipoServicioText").text(),
        subTipoServicioText: $("#idSubTipoServicioText").text()
    });

    // Solo enviar cveEstatus cuando exista.
    // Si es Total, estatusTemp es null y no se manda.
    if (estatusTemp != null && estatusTemp !== "") {
        params.append("cveEstatus", estatusTemp);
    }

    $("#idBtnDescargarServicioDetalle").prop("disabled", true);
    onLoading(null);

    fetch(contexto + '/api/catalogo/tic/exportarServicioAdministra?' + params.toString(), {
        method: "GET"
    })
        .then(response => {

            if (!response.ok) {
                throw new Error("Error al generar el archivo.");
            }

            const disposition = response.headers.get("Content-Disposition");
            let fileName = "servicios_detalle.xlsx";

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
            $("#idBtnDescargarServicioDetalle").prop("disabled", false);

            Toast.fire({
                icon: "success",
                title: "La exportación se realizó correctamente."
            });
        })
        .catch(error => {

            offLoading(null);
            $("#idBtnDescargarServicioDetalle").prop("disabled", false);

            Toast.fire({
                icon: "error",
                title: error.message
            });

            console.error(error);
        });
}

function descargarServiciosDetalleOpera() {

    let params = new URLSearchParams({
        idEntePublico: idEntePublicoDetalleTemp,
        idUnidadAdministrativa: $("#idUnidadAdministrativaSearch").val() || "",
        idTipoServicio: idTipoServicioDetalleTemp,
        idSubtipoServicio: idSubTipoServicioDetalleTemp,

        tipoEntePublicoText: $("#idTipoEnteText").text(),
        entePublicoText: $("#idEntePublicoText").text(),
        tipoServicioText: $("#idTipoServicioText").text(),
        subTipoServicioText: $("#idSubTipoServicioText").text()
    });

    // Vigente = VIG
    // Total = no se manda cveEstatus
    if (estatusTemp != null && estatusTemp !== "") {
        params.append("cveEstatus", estatusTemp);
    }

    $("#idBtnDescargarServicioDetalle").prop("disabled", true);
    onLoading(null);

    fetch(contexto + '/api/catalogo/tic/exportarServicioOpera?' + params.toString(), {
        method: "GET"
    })
        .then(response => {

            if (!response.ok) {
                throw new Error("Error al generar el archivo.");
            }

            const disposition = response.headers.get("Content-Disposition");
            let fileName = "servicios_detalle_opera.xlsx";

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
            $("#idBtnDescargarServicioDetalle").prop("disabled", false);

            Toast.fire({
                icon: "success",
                title: "La exportación se realizó correctamente."
            });
        })
        .catch(error => {

            offLoading(null);
            $("#idBtnDescargarServicioDetalle").prop("disabled", false);

            Toast.fire({
                icon: "error",
                title: error.message
            });

            console.error(error);
        });
}