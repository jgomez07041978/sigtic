
const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let idLogoTemp=null;
let idSubtipoServicioTemp="";

let paginaActual=1;
let sizePagina=10;
let totalPaginas=0;
let totalElementos=0;
let lastPagina=false;
let firstPagina=false;
let emptyPagina=false;

let rfcTemp="";
let curpTemp="";
let nombreTemp="";
let segundoNombreTemp="";
let apellidoPaternoTemp="";
let apellidoMaternoTemp="";

let tableDirectorio = null;
let tipoUsuario=0;



$(document).ready(function() {

    obtenConfigReporte();

    $("#idBtnBuscarLogo").click(function () {
        $('#dlgModalLogo').modal('show');
        obtenLogos();
    });

    $("#idBtnBuscarUserEntrega").click(function () {
        limpiarSearch();
        tipoUsuario=1;
        $('#dlgModalSearchUsuario').modal('show');
    });

    $("#idBtnBuscarUserAutoriza").click(function () {
        limpiarSearch();
        tipoUsuario=2;
        $('#dlgModalSearchUsuario').modal('show');
    });


    $("#idBtnLimpiar").click(function () {
        limpiarSearch();
    });

    $("#idNuevoConfig").click(function(){
        limpiar();
        $('#dlgModalFormConfigReporte').modal('show');
    });

    fillSelectGet("/api/catalogo/tic/getCatTipoServiciosTic","#idTipoServicioTic", "idTipoServicioTic", "nombreTipoServicioTic", null, null);


    $("#idTipoServicioTic").on("change", function(){
        obtenerSubtipoServicio($("#idTipoServicioTic").val());
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

    $("#idFormConfigReporte").validate( {
        ignore: "",
        rules: {
            idTipoServicioTic:"required",
            idSubtipoServicioTic:"required",
            idNombreReporte:"required",
            idTituloReporte:"required",
            descFirma1:"required",
            curpEntrega:"required",
            nombreEntrega:"required",
            cargoEntrega:"required",
            descFirmaEntrega:"required",
            curpAutoriza:"required",
            nombreAutoriza:"required",
            cargoAutoriza:"required",
            descFirmaAutoriza:"required",

        },
        messages: {
            idTipoServicioTic:"Campo Requerido",
            idSubtipoServicioTic:"Campo Requerido",
            idNombreReporte:"Campo Requerido",
            idTituloReporte:"Campo Requerido",
            descFirma1:"Campo Requerido",
            curpEntrega:"Campo Requerido",
            nombreEntrega:"Campo Requerido",
            cargoEntrega:"Campo Requerido",
            descFirmaEntrega:"Campo Requerido",
            curpAutoriza:"Campo Requerido",
            nombreAutoriza:"Campo Requerido",
            cargoAutoriza:"Campo Requerido",
            descFirmaAutoriza:"Campo Requerido",
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
    } );

    $("#idFormConfigReporte").submit(function(e) {
        e.preventDefault();
        if ($('#idFormConfigReporte').valid()) {
            if ($("#idLogo").val() != "") {
                guardarConfiguracion();
                return true;
            }else {
                return false;
            }
        }

    });

    $("#idFormAddParrafo").validate( {
        ignore: "",
        rules: {
            idParrafo:"required",
            numOrdenPa:"required",
        },
        messages: {
            idParrafo:"Campo Requerido",
            numOrdenPa:"Campo Requerido",
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
    } );

    $("#idFormAddParrafo").submit(function(e) {
        e.preventDefault();
        if ($('#idFormAddParrafo').valid()) {
            guardarParrafo();
                return true;
        }

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

function obtenConfigReporte(){

    const table = new DataTable('#tableLogos', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        paging: false,
        autoWidth: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/config/reporte',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
        },
        bProcessing: true,
        bServerSide:false,
        columnDefs: [{
                "targets": 4,
                "width":'70',
                "className": "text-center",
            },{
                "targets": 5,
                "width":'90px',
                "className": "text-center",
            }],
        language:{
            "url": contexto+'/include/datatables/spanish.json'
        },
        lengthChange: false,
        select: false
    });
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
function selectedSubtipoServicio(){
    if (idSubtipoServicioTemp!="")
        $("#idSubtipoServicioTic").val(idSubtipoServicioTemp);
}

function obtenLogos(){

    const tableLogo = new DataTable('#tableLogosEnte', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        paging: false,
        autoWidth: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/logo/listaLogosEnte',
            type: 'GET',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
        },
        bProcessing: true,
        bServerSide:false,
        columnDefs: [{
                "targets": 3,
                "width":'70px',
                "className": "text-center",
            }],
        language:{
            "url": contexto+'/include/datatables/spanish.json'
        },
        lengthChange: false,
        select: false
    });
}


function seleccionarLogo(idLogo, rutaLogo){
    $("#idLogoAlert").hide();
    $("#idLogo").val(idLogo);
    $("#idImg").attr("src",rutaLogo);
    $('#dlgModalLogo').modal('hide');
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
    sizePagina=10;
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
            if (response.idUsuarioEntePublico!=null){

                if (response.catUsuario!=null) {
                    if (tipoUsuario==1){
                        $("#idUsuarioEntePublicoEntrega").val(response.idUsuarioEntePublico);
                        $("#curpEntrega").val(response.catUsuario.curp);
                        $("#nombreEntrega").val(response.catUsuario.nombreCompleto);

                        if (response.puesto!=null)
                            $("#cargoEntrega").val(response.puesto);

                    }else
                    if (tipoUsuario==2){
                        $("#idUsuarioEntePublicoAutoriza").val(response.idUsuarioEntePublico);
                        $("#curpAutoriza").val(response.catUsuario.curp);
                        $("#nombreAutoriza").val(response.catUsuario.nombreCompleto);

                        if (response.puesto!=null)
                            $("#cargoAutoriza").val(response.puesto);
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

function limpiar(){
    $("#idNombreReporte").removeClass("is-valid");
    $("#idNombreReporte").removeClass("is-invalid");
    $("#idTituloReporte").removeClass("is-valid");
    $("#idTituloReporte").removeClass("is-invalid");
    $("#idSubtipoServicioTic").removeClass("is-valid");
    $("#idSubtipoServicioTic").removeClass("is-invalid");
    $("#idTipoServicioTic").removeClass("is-valid");
    $("#idTipoServicioTic").removeClass("is-invalid");
    $("#descFirma1").removeClass("is-valid");
    $("#descFirma1").removeClass("is-invalid");
    $("#curpEntrega").removeClass("is-valid");
    $("#curpEntrega").removeClass("is-invalid");
    $("#nombreEntrega").removeClass("is-valid");
    $("#nombreEntrega").removeClass("is-invalid");
    $("#cargoEntrega").removeClass("is-valid");
    $("#cargoEntrega").removeClass("is-invalid");
    $("#descFirmaEntrega").removeClass("is-valid");
    $("#descFirmaEntrega").removeClass("is-invalid");
    $("#curpAutoriza").removeClass("is-valid");
    $("#curpAutoriza").removeClass("is-invalid");
    $("#nombreAutoriza").removeClass("is-valid");
    $("#nombreAutoriza").removeClass("is-invalid");
    $("#cargoAutoriza").removeClass("is-valid");
    $("#cargoAutoriza").removeClass("is-invalid");
    $("#descFirmaAutoriza").removeClass("is-valid");
    $("#descFirmaAutoriza").removeClass("is-invalid");

    $("#idPlantillaReporte").val("");
    $("#idNombreReporte").val("");
    $("#idTituloReporte").val("");
    $("#idUsuarioEntePublicoEntrega").val("");
    $("#idUsuarioEntePublicoAutoriza").val("");
    $("#idLogoAlert").show();
    $("#idLogo").val("");
    $("#idImg").attr("src","");
    $("#idSubtipoServicioTic").val("");
    $("#idTipoServicioTic").val("");
    $("#idTipoServicioTic").trigger('change');
    $("#descFirma1").val("Persona Servidora Pública que Acepta y Recibe");
    $("#curpEntrega").val("");
    $("#nombreEntrega").val("");
    $("#cargoEntrega").val("");
    $("#descFirmaEntrega").val("Persona Servidora Pública que Entrega");
    $("#curpAutoriza").val("");
    $("#nombreAutoriza").val("");
    $("#cargoAutoriza").val("");
    $("#descFirmaAutoriza").val("Persona Servidora Pública que Autoriza");
}

function guardarConfiguracion(){

    let titleSave='Configuración de reporte';
    let msgSave='¿Estas seguro que deseas guardar la configuración del reporte?';

    if ($("#idPlantillaReporte").val()!="")
        msgSave='¿Estas seguro que deseas actualizar la configuración del reporte?';


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
            guardarConfiguracionAsync();

        }
    });
}

const guardarConfiguracionAsync = async () =>{

    let msg="Configuración de reporte guardada correctamente correctamente";

    if ($("#idPlantillaReporte").val()!="")
        msg="Configuración de reporte actualizada correctamente correctamente"

    const options={
        method: "POST",
        body: JSON.stringify({
            idPlantillaReporte:$("#idPlantillaReporte").val(),
            catUsuarioEntePublicoEntrega:{idUsuarioEntePublico:$("#idUsuarioEntePublicoEntrega").val()},
            catUsuarioEntePublicoAutoriza:{idUsuarioEntePublico:$("#idUsuarioEntePublicoAutoriza").val()},
            idSubtipoServicio:$("#idSubtipoServicioTic").val(),
            nombreReporte:$("#idNombreReporte").val(),
            tituloReporte:$("#idTituloReporte").val(),
            decFirma1:$("#descFirma1").val(),
            decFirma2:$("#descFirmaEntrega").val(),
            decFirma3:$("#descFirmaAutoriza").val(),
            logo:{idLogo:$("#idLogo").val()}
        }),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/config/guardarConfiguracionReporte', options);
        const respJSON = await res.json();
        if (res.ok) {

            offLoading(null);

            Toast.fire({
                icon: "success",
                title: msg
            });
            $('#dlgModalFormConfigReporte').modal('hide');

            obtenConfigReporte();

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
            title: "Error al guardar la configuración del reporte"
        });
    }
};

const editarPlantilla = async (idPlantillaReporte) =>{

    limpiar();

    const options={
        method: "GET",
        header:headers
    };

    try {
        onLoading(null);
        const res= await fetch(contexto+'/api/config/editarConfiguracionReporte/'+idPlantillaReporte, options);
        const respJSON = await res.json();

        if (res.ok){
            $("#idPlantillaReporte").val(respJSON.idPlantillaReporte);
            $("#idNombreReporte").val(respJSON.nombreReporte);
            $("#idTituloReporte").val(respJSON.tituloReporte);
            $("#descFirma1").val(respJSON.decFirma1);

            $("#curpEntrega").val(respJSON.curpEntrega);
            $("#nombreEntrega").val(respJSON.firma2);
            $("#cargoEntrega").val(respJSON.cargo2);
            $("#descFirmaEntrega").val(respJSON.decFirma2);

            $("#curpAutoriza").val(respJSON.curpAutoriza);
            $("#nombreAutoriza").val(respJSON.firma3);
            $("#cargoAutoriza").val(respJSON.cargo3);
            $("#descFirmaAutoriza").val(respJSON.decFirma3);

            $("#idUsuarioEntePublicoEntrega").val(respJSON.catUsuarioEntePublicoEntrega.idUsuarioEntePublico);


            $("#idUsuarioEntePublicoAutoriza").val(respJSON.catUsuarioEntePublicoAutoriza.idUsuarioEntePublico);

            $("#idLogo").val(respJSON.logo.idLogo);
            $("#idLogoAlert").hide();
            $("#idImg").attr("src",respJSON.rutaLogo);

            if (respJSON.catSubtipoServiciosTic!=null){
                idSubtipoServicioTemp=respJSON.catSubtipoServiciosTic.idSubtipoServicioTic
                $("#idTipoServicioTic").val(respJSON.catSubtipoServiciosTic.catTipoServiciosTic.idTipoServicioTic);
                $("#idTipoServicioTic").trigger('change');
            }

            $('#dlgModalFormConfigReporte').modal('show');

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
const eliminarPlantillaAsync = async (idPlantillaReporte) =>{
    const options={
        method: "DELETE",
        body: JSON.stringify({idPlantillaReporte: idPlantillaReporte}),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/config/deleteConfiguracionReporte', options);
        const respJSON = await res.json();

        if (res.ok) {

            obtenConfigReporte();

            Toast.fire({
                icon: "success",
                title: "Plantilla eliminada correctamente"
            });


            offLoading(null);

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
            title: "Error al eliminar la plantilla"
        });
    }
};

function eliminarPlantilla(idPlantillaReporte){
    Swal.fire({
        title: 'Eliminar Palntilla',
        text: '¿Estas seguro que deseas eliminar la plantilla?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminarPlantillaAsync(idPlantillaReporte);
        }
    })

}

function modalParrafo(idPlantillaReporte){
    cancelarParrafo();
    obtenConfigParrafo(idPlantillaReporte);
    $("#idPlantillaReporte2").val(idPlantillaReporte);
    $("#dlgModalAddParrafo").modal("show");
}

function limpiarFormParrafo(){
    $("#idParrafo").removeClass("is-valid");
    $("#idParrafo").removeClass("is-invalid");
    $("#numOrdenPa").removeClass("is-valid");
    $("#numOrdenPa").removeClass("is-invalid");
    $("#idParrafo").val("");
    $("#numOrdenPa").val("");
}

function newParrafo(){
    limpiarFormParrafo();
    $("#idNewParrado").hide();
    $("#idAddDiv").show();
}

function cancelarParrafo(){
    limpiarFormParrafo();
    $("#idAddDiv").hide();
    $("#idNewParrado").show();
}

function obtenConfigParrafo(idPlantillaReporte){


    const tablePa = new DataTable('#tableParrafos', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        paging: false,
        autoWidth: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/config/parrafos',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    idPlantillaReporte:idPlantillaReporte
                }));
            },
        },
        bProcessing: true,
        bServerSide:false,
        columnDefs: [{
            "targets": 1,
            "width":'70',
            "className": "text-center",
        },{
            "targets": 2,
            "width":'80px',
            "className": "text-center",
        }],
        language:{
            "url": contexto+'/include/datatables/spanish.json'
        },
        lengthChange: false,
        select: false
    });
}

function guardarParrafo(){

    let titleSave='Configuración de reporte';
    let msgSave='¿Estas seguro que deseas guardar el parrafo del reporte?';

    if ($("#idPlantillaReporteParrafo").val()!="")
        msgSave='¿Estas seguro que deseas actualizar el parrafo del reporte?';


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
            guardarParrafoAsync();

        }
    });
}

const guardarParrafoAsync = async () =>{

    let msg="Parrafo de reporte agregado correctamente correctamente";

    if ($("#idPlantillaReporteParrafo").val()!="")
        msg="Parrafo de reporte actualizada correctamente correctamente"

    const options={
        method: "POST",
        body: JSON.stringify({
            idPlantillaReporte:$("#idPlantillaReporte2").val(),
            idPlantillaReporteParrafo:$("#idPlantillaReporteParrafo").val(),
            parrafo:$("#idParrafo").val(),
            numeroOrden:$("#numOrdenPa").val()
        }),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/config/guardarParrafo', options);
        const respJSON = await res.json();
        if (res.ok) {

            offLoading(null);

            Toast.fire({
                icon: "success",
                title: msg
            });
            cancelarParrafo();
            obtenConfigParrafo($("#idPlantillaReporte2").val());

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
            title: "Error al guardar la configuración del reporte"
        });
    }
};


const editarParrafo = async (idPlantillaReporteParrafo) =>{

    limpiarFormParrafo();
    $("#idAddDiv").show();
    $("#idNewParrado").hide();

    const options={
        method: "GET",
        header:headers
    };

    try {
        onLoading(null);
        const res= await fetch(contexto+'/api/config/editarParrafo/'+idPlantillaReporteParrafo, options);
        const respJSON = await res.json();

        if (res.ok){
            $("#idPlantillaReporteParrafo").val(respJSON.idPlantillaReporteParrafo);
            $("#idParrafo").val(respJSON.parrafo);
            $("#numOrdenPa").val(respJSON.numeroOrden);
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

const eliminarParrafoAsync = async (idPlantillaReporteParrafo) =>{
    const options={
        method: "DELETE",
        body: JSON.stringify({idPlantillaReporteParrafo: idPlantillaReporteParrafo}),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/config/deleteParrafo', options);
        const respJSON = await res.json();

        if (res.ok) {

            obtenConfigParrafo($("#idPlantillaReporte2").val());

            Toast.fire({
                icon: "success",
                title: "Parrafo se elimino correctamente"
            });


            offLoading(null);

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
            title: "Error al eliminar el parrafo"
        });
    }
};

function eliminarParrafo(idPlantillaReporteParrafo){
    Swal.fire({
        title: 'Eliminar Parrafo',
        text: '¿Estas seguro que deseas eliminar el parrafo?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminarParrafoAsync(idPlantillaReporteParrafo);
        }
    })

}

