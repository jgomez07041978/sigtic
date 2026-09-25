var contexto=$("#contextSigtic").val();

const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let paginaActual=1;
let sizePagina=20;
let totalPaginas=0;
let totalElementos=0;
let lastPagina=false;
let firstPagina=false;
let emptyPagina=false;

let tableDirectorio = null;

let idEntePublicoTemp=null;
let idUnidadMedidaTemp=null;
let nombreTemp="";
let telefonoTemp="";
let extensionTemp="";


$(document).ready(function() {

    $('[data-bs-toggle="tooltip"]').tooltip();

    jQuery.event.special.mousewheel = {
        setup: function (_, ns, handle) {
            this.addEventListener("mousewheel", handle, {passive: true});
        }
    };

    $("#actualPage").val(paginaActual);

    $('#idEntePublicoSearch').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
    });

    inicializarDependencias();

    $('#idEntePublicoSearch').on('change', function() {

        $("#idUnidadAdmtva").val("");
        $("#descUnidadAdmtva").val("");
        $("#nombre").val("");
        $("#telefono").val("");
        $("#extension").val("");
        $("#idContainerSearch").hide();
        $("#tableDirectorio").hide();
        $("#paginationNav").hide();
        $("#infoPag").text("");
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

        obtienearbolPorInstitucion5(function (data) {
            $("#idUnidadAdmtva").val(data.id);
            $("#descUnidadAdmtva").val(data.text);
            limpiarBuscarArbol5();
            $('#modal_arbol_unidad5').modal('hide');
        },$("#idEntePublicoSearch").val());
    });

    $("#btnOpenArbol").click(function () {
        $('#modal_arbol_unidad5').modal('show');
    });

    $("#txtBuscar5").keyup(function () {
        busqueda5($(this).val())
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

});

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

const inicializarDependencias = async () => {
    const options = {
        method: "POST",
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/entePublico/listaFree', options);
        const respJSON = await res.json();

        llenarCombo('#idEntePublicoSearch', 'idEntePublico', 'nombreEntePublicoConCve', respJSON);
        offLoading(null);
    } catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
};


function limpiarBusqueda(){

    $("#idEntePublicoSearch").val("");
    $("#idEntePublicoSearch").trigger('change');
    $("#idUnidadAdmtva").val("");
    $("#descUnidadAdmtva").val("");
    $("#nombre").val("");
    $("#telefono").val("");
    $("#extension").val("");
    $("#idCardTablaPlantillaUsuario").hide();
    $("#tableDirectorio").hide();
    $("#paginationNav").hide();
    $("#infoPag").text("");

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

    idEntePublicoTemp=null;
    idUnidadMedidaTemp=null;
    nombreTemp="";
    telefonoTemp="";
    extensionTemp="";

}

function buscar(){

    let mensaje="";
    $("#idMsgAlertSearch").text("");
    $("#alertSearch").hide();

    if ($("#idEntePublicoSearch").val()=="" && $("#idUnidadAdmtva").val()=="" && $("#nombre").val().trim()=="" && $("#telefono").val().trim()=="" && $("#extension").val().trim()=="") {
        mensaje = "Se requiere mínimo un campo de búsqueda";
    }
   /* else {
        if ($("#nombre").val().trim() != "" && $("#nombre").val().trim().length < 3)
            mensaje += "Nombre requiere mínimo de 3 caracteres para la búsqueda";

        if ($("#telefono").val().trim() != "" && $("#telefono").val().trim().length < 4) {
            if (mensaje!="")
                mensaje+="<br/";
                    mensaje += "Teléfono requiere mínimo de 4 caracteres para la búsqueda";
        }

        if ($("#extension").val().trim() != "" && $("#extension").val().trim().length < 2) {
            if (mensaje!="")
                mensaje+="<br/";
            mensaje += "Extension requiere mínimo de 2 caracteres para la búsqueda";
        }
    }*/

    if (mensaje!=""){
        $("#idMsgAlertSearch").html(mensaje);
        $("#alertSearch").show();

    }else{
         idEntePublicoTemp=$("#idEntePublicoSearch").val();
         idUnidadMedidaTemp=$("#idUnidadAdmtva").val();
         nombreTemp=$("#nombre").val().trim();
         telefonoTemp=$("#telefono").val().trim();
         extensionTemp=$("#extension").val().trim();

        $("#idContainerSearch").show()
        $("#tableDirectorio").show()
        $("#paginationNav").hide()
        searchUsuario();
    }
}

function buscarNav(){
        searchUsuario();
}

const searchUsuario = async () => {
    let parametros = {};

    if (idEntePublicoTemp!="")
        parametros.idEntePublico=idEntePublicoTemp;

    if (idUnidadMedidaTemp!="")
        parametros.idUnidadAdministrativa=idUnidadMedidaTemp;

    if (nombreTemp!="")
        parametros.nombre=nombreTemp;

    if (telefonoTemp!="")
        parametros.telefonoInstitucinal=telefonoTemp;

    if (extensionTemp!="")
        parametros.numeroExtension=extensionTemp;

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
    const res = await fetch(contexto + '/api/directorio/filtrar', options);
    const respJSON = await res.json();
    if (res.ok){
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

            if (lastPagina)
                $("#totalRegistro").text("Mostrando "+((paginaActual*sizePagina)-sizePagina+1)+" - "+totalElementos+" de "+ totalElementos);
             else
                $("#totalRegistro").text("Mostrando "+((paginaActual*sizePagina)-sizePagina+1)+" - "+(paginaActual*sizePagina)+" de "+ totalElementos);
        }
        console.log(2222);
        console.log(respJSON.content);
        obtenTablaDirectorio(respJSON.content);


        offLoading(null);
    }else {

        console.log(111);
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

    tableDirectorio = new DataTable('#tableDirectorio', {
        sPaginationType: "full_numbers",
        responsive: true,
        bSort: false,
        paging: false,
        info: false,
        bDestroy: true,
        initComplete: function(settings, json) {
        $('[data-bs-toggle="tooltip"]').tooltip();
            offLoading(null);
        },
        data: jsonData,
        columns: [
            { data: 'descEntePublicoAbreviada',
                render: function (data, type, row, meta) {
                    return '<span class="fw-bold" data-bs-toggle="tooltip" data-bs-toggle="top" data-bs-title="'+row['nombreEntePublico']+'" >' + data + '</span>';
                }
            },
            { data: 'nombreCompleto'},
            { data: 'telefonoInstitucinal' },
            { data: 'numeroExtension' },
            { data: 'correoInstitucional' },
            { data: 'nombreUnidadAdministrativa' },
            { data: 'rutaUnidadAdministrativaAbreviada',
                render: function (data, type, row, meta) {
                    return '<span class="fw-bold" data-bs-toggle="tooltip" data-bs-toggle="top" data-bs-title="'+row['rutaUnidadAdministrativa']+'" >' + data + '</span>';
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
                "width": "70px",
                "targets": 0,
                "className": "text-left",
            },
            {
                "width": "150px",
                "targets": 2,
                "className": "text-center",
            },
            {
                "width": "70px",
                "targets": 3,
                "className": "text-center",
            },
            {
                "width": "300px",
                "targets": 4,
                "className": "text-left",
            }],
        lengthChange: false,
        select: false
    });

}

