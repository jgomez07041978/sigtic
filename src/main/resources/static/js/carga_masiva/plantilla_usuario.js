const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let idEntePublicoTemp="";
let rfcTemp="";
let curpTemp="";
let nombreTemp="";
let apellidoPaternoTemp="";
let apellidoMaternoTemp="";


let paginaActual=1;
let sizePagina=10;
let totalPaginas=0;
let totalElementos=0;
let lastPagina=false;
let firstPagina=false;
let emptyPagina=false;

let tableUsuarioPlantilla=null;
let tablePlantillaAsignada=null;
let tablePlantillaDisponible=null;

$(document).ready(function(){

    $('[data-bs-toggle="tooltip"]').tooltip();

    //$("#idImgPK").attr("src",contexto+"/include/imagen/premary_key.jpg");

    jQuery.event.special.mousewheel = {
        setup: function( _, ns, handle ){
            this.addEventListener("mousewheel", handle, { passive: true });
        }
    };

    $("#idEntePublico").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large"
    });

    inicializarDependencias();

    $("#formBusquedaUsuarioPlantilla").submit(function(e) {
        e.preventDefault();
        $("#idCardTablaPlantillaUsuario").hide();
        $("#rfc").val($("#rfc").val().trim());
        $("#curp").val($("#curp").val().trim());
        $("#nombre").val($("#nombre").val().trim());
        $("#apellidoPaterno").val($("#apellidoPaterno").val().trim());
        $("#apellidoMaterno").val($("#apellidoMaterno").val().trim());

        if ($("#idEntePublico").val().trim()=="" && $("#rfc").val().trim()=="" && $("#curp").val().trim()=="" && $("#nombre").val().trim()=="" &&
            $("#apellidoPaterno").val().trim()=="" && $("#apellidoMaterno").val().trim()=="")
            Toast.fire({
                icon: 'warning',
                //title: 'Validar',
                html: "Se requiere por lo menos un campo de búsqueda"
            });
        else{
            idEntePublicoTemp=$("#idEntePublico").val();
            rfcTemp=$("#rfc").val();
            curpTemp=$("#curp").val();
            nombreTemp=$("#nombre").val();
            apellidoPaternoTemp=$("#apellidoPaterno").val();
            apellidoMaternoTemp=$("#apellidoMaterno").val();

            searchPlantillaUsuario();
            return true;
        }
    });

    $("#idBtnLimpiar").click(function () {
        limpiarBusqueda();
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

        offLoading(null);
    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
};

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
    searchPlantillaUsuario();
}

const searchPlantillaUsuario = async () => {
    let parametros = {};

    if (idEntePublicoTemp!="")
        parametros.idEntePublico=idEntePublicoTemp;

    if (rfcTemp!="")
        parametros.rfcUsuario=rfcTemp;

    if (curpTemp!="")
        parametros.curpUsuario=curpTemp;

    if (nombreTemp!="")
        parametros.nombreUsuario=nombreTemp;

    if (apellidoPaternoTemp!="")
        parametros.apellidoPaterno=apellidoPaternoTemp;

    if (apellidoMaternoTemp!="")
        parametros.apellidoMaterno=apellidoMaternoTemp;

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
    const res = await fetch(contexto + '/api/usuario-plantilla/filtrarUsariosPlantilla', options);
    const respJSON = await res.json();
    if (res.ok){
        $("#idCardTablaPlantillaUsuario").show();
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
                $("#primeraPag").addClass("disabled");
                $("#previousPag").addClass("disabled");
            }else{
                $("#primeraPag").removeClass("disabled");
                $("#previousPag").removeClass("disabled");
            }
            if (lastPagina){
                $("#nextPag").addClass("disabled");
                $("#lastPag").addClass("disabled");
            }else{
                $("#nextPag").removeClass("disabled");
                $("#lastPag").removeClass("disabled");
            }

            $("#actualPage").val(paginaActual);

            $("#infoPag").text("Pag. "+paginaActual+" de "+totalPaginas);

            if (lastPagina)
                $("#totalRegistro").text("Mostrando "+((paginaActual*sizePagina)-sizePagina+1)+" - "+totalElementos+" de "+ totalElementos);
            else
                $("#totalRegistro").text("Mostrando "+((paginaActual*sizePagina)-sizePagina+1)+" - "+(paginaActual*sizePagina)+" de "+ totalElementos);
        }

        getTablaUsuariosPlantilla(respJSON.content);


        offLoading(null);
    }else {
        Toast.fire({
            icon: "error",
            title: respJSON.message
        });
        offLoading(null);
    }
};


function getTablaUsuariosPlantilla(jsonData){

    if (tableUsuarioPlantilla!=null)
        tableUsuarioPlantilla.clear();

    tableUsuarioPlantilla = new DataTable('#tableUsuarioPlantilla', {
        sPaginationType: "full_numbers",
        responsive: true,
        bSort: false,
        paging: false,
        info: false,
        bDestroy: true,
        searching: false,
        //ordering: true,
        data: jsonData,
        columns: [
            { data: 'curpUsuario'},
            { data: 'apellidoPaterno'},
            { data: 'apellidoMaterno'},
            { data: 'nombreUsuario'},
            { data: 'descEntePublico',
                render: function (data, type, row, meta) {
                return row['cveEntePublico']+" "+row['descEntePublico']
                }
            },
            { data: 'descUnidadAdministrativa'},
            { data: 'numeroPlantilla'},
            { data: 'cveEstatus'},
            { data: 'rfcUsuario',
                render: function (data, type, row, meta) {
                    return '<a href="#" class="btn btn-outline-success mr-1" data-toggle="tooltip" data-placement="top" title="Plantillas Asignadas" onclick="openListaPlantilla(\''+row['rfcUsuario']+'\',\''+row['nombreUsuario']+'\',\''+row['apellidoPaterno']+'\',\''+row['apellidoMaterno']+'\','+row['idEntePublico']+','+row['idUnidadAdministrativa']+')"><i class=\'fas fa-plus\'></i></a>';
                }
            }
        ],
        bProcessing: false,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json'
        },
        lengthChange: false,
        select: false
    });
}

function openListaPlantilla(rfcUsuario,nombreUsuario, appaterno,apmaterno, idEntePublico, idUnidadAdministrativa) {
    $('#nombreUserParam').text(rfcUsuario+" "+nombreUsuario+" "+appaterno+" "+apmaterno);
    $('#idEntePublicoTemp').val(idEntePublico);
    $('#usuario').val(rfcUsuario);
    $('#idEntePublicoTemp').val(idEntePublico);
    $('#idUnidadAdministrativaTemp').val(idUnidadAdministrativa);

    getTablaplantillaAsignadas(idEntePublico, rfcUsuario);
    getTablaplantillaDisponibles(idEntePublico, rfcUsuario);
    $('#dlgModalPlantillaUSuario').modal('show');
}

function getTablaplantillaAsignadas(idEntePublico, rfcUsuario) {

    if (tablePlantillaAsignada != null)
        tablePlantillaAsignada.clear();

    tablePlantillaAsignada = new DataTable('#tableAsigPlantilla', {
        sPaginationType: "full_numbers",
        autoWidth: false,
        bDestroy: true,
        //paging: false,
        ordering: false,
        ajax: {
            url: contexto + '/api/usuario-plantilla/plantillasAsignadas/'+idEntePublico+'/'+rfcUsuario,
            type: 'GET',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function (xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            }/*,
            'data': function (d) {
                return JSON.stringify($.extend( {}, d, {}));
            }*/
        },
        bProcessing: true,
        bServerSide: false,
        columnDefs: [
            {
                "targets": 0,
                "width":'40px',
                "className": "text-center",
            }],
        language: {
            "url": contexto + '/include/datatables/spanish.json'
        },
        lengthChange: false,
        select: false
    });

    //tablePlantillaAsignada.columns.adjust().draw();

}

function getTablaplantillaDisponibles(idEntePublico, rfcUsuario) {

    if (tablePlantillaDisponible != null)
        tablePlantillaDisponible.clear();

    tablePlantillaDisponible = new DataTable('#tableDispoPlantilla', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        autoWidth: false,
        //paging: false,
        ordering: false,
        ajax: {
            url: contexto + '/api/usuario-plantilla/plantillasDisponibles/'+idEntePublico+'/'+rfcUsuario,
            type: 'GET',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function (xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            }/*,
            'data': function (d) {
                return JSON.stringify($.extend( {}, d, {}));
            }*/
        },
        bProcessing: true,
        bServerSide: false,
        columnDefs: [
            {
                "targets": 1,
                "width":'40px',
                "className": "text-center",
            }],
        language: {
            "url": contexto + '/include/datatables/spanish.json'
        },
        lengthChange: false,
        select: false
    });
}

function addPlantilla(idPlantilla){
    $('#preloader').modal('show');
    $.ajax({ url: contexto+'/api/usuario-plantilla',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        data:JSON.stringify({
            plantilla:{idPlantilla:idPlantilla},
            usuario: $("#usuario").val(),
            idEntePublico:$("#idEntePublicoTemp").val(),
            idUnidadAdministrativa:$("#idUnidadAdministrativaTemp").val()
        }),
        success: function(response) {
            if(response.idPlantillaUsuario != ""){

                getTablaplantillaAsignadas($('#idEntePublicoTemp').val(),$("#usuario").val());
                getTablaplantillaDisponibles($('#idEntePublicoTemp').val(),$("#usuario").val());
                $("#idBtnSearch").click();

                Toast.fire({
                    icon: "success",
                    title: "Plantilla agregada Correctamente"
                });
            }

        },
        error: function (response) {
            Toast.fire({
                icon: "error",
                title: "Error al añadir la plantilla"
            });
        }
    });
}

function removePlantilla(idPlantillaUsuario){
    $('#preloader').modal('show');
    $.ajax({ url: contexto+'/api/usuario-plantilla',
        type: 'DELETE',
        //dataType: 'json',
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        data:JSON.stringify({
            idPlantillaUsuario:idPlantillaUsuario
            }),
        success: function(data) {
            getTablaplantillaAsignadas($('#idEntePublicoTemp').val(),$("#usuario").val());
            getTablaplantillaDisponibles($('#idEntePublicoTemp').val(),$("#usuario").val());
            $("#idBtnSearch").click();

                Toast.fire({
                    icon: "success",
                    title: "Plantilla eliminada Correctamente"
                });

        },
        error: function (error) {
            Toast.fire({
                icon: "error",
                title: "Error al eliminar la plantilla"
            });
        }
    });
}

function limpiarBusqueda(){

    $("#idEntePublico").val("");
    $("#idEntePublico").trigger('change');
    $("#rfc").val("");
    $("#curp").val("");
    $("#nombre").val("");
    $("#apellidoPaterno").val("");
    $("#apellidoMaterno").val("");

    paginaActual=1;
    $("#actualPage").val(paginaActual);
    sizePagina=20;
    totalPaginas=0;
    totalElementos=0;
    lastPagina=false;
    firstPagina=false;
    emptyPagina=false;

    if(tableUsuarioPlantilla!=null) {
        $('#tableUsuarioPlantilla').dataTable().fnClearTable();
        $('#tableUsuarioPlantilla').dataTable().fnDraw();
        $('#tableUsuarioPlantilla').dataTable().fnDestroy();
        tableUsuarioPlantilla=null;
    }

    $("#paginationNav").hide();
    $("#infoPag").text("");

    idEntePublicoTemp
    rfcTemp="";
    curpTemp="";
    nombreTemp="";
    apellidoPaternoTemp="";
    apellidoMaternoTemp="";

    $("#idCardTablaPlantillaUsuario").hide();

}