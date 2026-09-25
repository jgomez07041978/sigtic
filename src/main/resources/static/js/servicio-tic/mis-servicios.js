
const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let isRolEnlaceTicServicio = $("#isRolEnlaceTicServicio").val();

let idEntePublicoSeleccionado=0;
let idSubtipoServicioTemp="";
let tableUsuarioSearch = null;
let tableMisServicios = null;


$(document).ready(function(){

    $('[data-bs-toggle="tooltip"]').tooltip();

    jQuery.event.special.mousewheel = {
        setup: function( _, ns, handle ){
            this.addEventListener("mousewheel", handle, { passive: true });
        }
    };

    $('.select2me').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormMisServicios")
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

    $('.select2Pe').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormSearchUser")
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


    $('#idNuevoServicio').click(function () {
        limpiarFormulario();
        $('#dlgModalFormMisServicios').modal('show');

    });

    inicializarDependenciasFree();
    inicializarDependencias();

    obtenMisServicios();

    $('#idEntePublicoDirige').on('change', function() {

        obtienearbolPorInstitucion(function (data) {
            $("#idUnidadAdministrativaDirige").val(data.id);
            $("#descUnidadAdministrativaDirige").val(data.text);
            limpiarBuscarArbol();
            $('#modal_arbol_unidad').modal('hide');
            $("#descUnidadAdministrativaDirige").removeClass("is-valid");
            $("#descUnidadAdministrativaDirige").removeClass("is-invalid");
        }, $("#idEntePublicoDirige").val());

        $("#idUnidadAdministrativaDirige").val("");
        $("#descUnidadAdministrativaDirige").val("");

        idEntePublicoSeleccionado=$("#idEntePublicoDirige").val();
    });

    fillSelectGet("/api/catalogo/tic/getCatTipoServiciosTic","#idTipoServicioTic", "idTipoServicioTic", "nombreTipoServicioTic", null, null);

    $("#idTipoServicioTic").on("change", function(){
        obtenerSubtipoServicio($("#idTipoServicioTic").val());
    })

    $("#btnOpenArbolDirige").click(function () {
        $('#modal_arbol_unidad').modal('show');
    });


    $("#idFormMisServicios").validate({
        ignore:"",
        rules: {
            idTipoServicioTic:"required",
            idSubtipoServicioTic:"required",
            idDescServicioTicAbr: "required",
            idDescServicioTic: "required",
            idEntePublicoDirige: "required",
            descUnidadAdministrativaDirige: "required",
            curp: "required"
        },
        messages:{
            idTipoServicioTic:"Campo requerido",
            idSubtipoServicioTic:"Campo requerido",
            idDescServicioTicAbr: "Campo requerido",
            idDescServicioTic: "Campo requerido",
            idEntePublicoDirige: "Campo requerido",
            descUnidadAdministrativaDirige: "Campo requerido",
            curp: "CURP requerida"
        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            var n = element.attr("id");
            if (n=="descUnidadAdministrativaDirige")
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

    $("#idFormMisServicios").submit(function(e) {
        e.preventDefault();
        if($('#idFormMisServicios').valid()) {
            guardarServiciosTic();
            return true;
        }
        else
            return false;
    });


    $("#idBtnSearchUser").click(function () {
        limpiarFormularioSearch();
        $('#dlgModalFormMisServicios').modal('hide');
        $('#dlgModalFormSearchUser').modal('show');

    });

    $('#idAceptarCr').on('change', function () {
        if ($("#idAceptarCr").is(":checked")){
            $("#motivoRechazo").val("")
            $("#motivoRechazo").hide();
            $("#motivoRechazoLabel").hide();
        }
    });
    $('#idRechazarCr').on('change', function () {
        if ($("#idRechazarCr").is(":checked")){
            $("#motivoRechazo").show();
            $("#motivoRechazoLabel").show();
        }
    });


});


function limpiarFormulario() {
    $("#idEntePublicoDirige").removeClass( "is-valid" );
    $("#idEntePublicoDirige").removeClass( "is-invalid" );

    $("#descUnidadAdministrativaDirige").removeClass( "is-valid" );
    $("#descUnidadAdministrativaDirige").removeClass( "is-invalid" );


    idSubtipoServicioTemp="";
    $("#idTipoServicioTic").removeClass("is-valid");
    $("#idTipoServicioTic").removeClass("is-invalid");
    $("#idSubtipoServicioTic").removeClass("is-valid");
    $("#idSubtipoServicioTic").removeClass("is-invalid");
    $("#idSubtipoServicioTic").removeClass("is-valid");

    $("#idDescServicioTicAbr").removeClass("is-valid");
    $("#idDescServicioTicAbr").removeClass("is-invalid");
    $("#idDescServicioTic").removeClass("is-valid");
    $("#idDescServicioTic").removeClass("is-invalid");

    $("#titleFormMisServicios").text("Nuevo Servivio");
    $("#idUsuarioEntePublico").val("");

    if (isRolEnlaceTicServicio=='S'){
        $("#rfc").val("");
        $("#curp").val("");
        $("#nombreSolicitante").val("");
    }

    $("#idServicioTic").val("");
    $("#idTipoServicioTic").val("");
    $("#idTipoServicioTic").trigger('change');
    $("#idSubtipoServicioTic").val("");
    $("#idUnidadAdministrativaDirige").val("");
    $("#descUnidadAdministrativaDirige").val("");
    $("#idDescServicioTicAbr").val("");
    $("#idDescServicioTic").val("");

}


const inicializarDependenciasFree = async () =>{
    const options={
        method: "POST",
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/entePublico/listaFree', options);
        const respJSON = await res.json();

        llenarCombo('#idEntePublicoDirige', 'idEntePublico','nombreEntePublicoConCve', respJSON);
        $("#idEntePublicoDirige").val($("#idEntePublicoSession").val());
        $("#idEntePublicoDirige").trigger('change');

        offLoading(null);
    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
};

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

function guardarServiciosTic(){
    let titleSave='¿Guardar solicitud de servicio?';
    let msgSave='¿Estas seguro que deseas guardar la solicitud de servicio?';
    if ($('#idServicioTic').val() != ""){
        titleSave='¿Actualizar solicitud de servicio?';
        msgSave='¿Estas seguro que deseas actualizar los datos de la solicitud servicio?';
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

const guardarServiciosTicAsync = async() => {

    const options={
        method: "POST",
        body: JSON.stringify({
            idServicioTic:$("#idServicioTic").val(),
            tipoServicio:{idTipoServicioTic:$("#idTipoServicioTic").val()},
            subTipoServicio:{idSubtipoServicioTic:$("#idSubtipoServicioTic").val()},
            servicioTicAbreviadoDescripcion: $("#idDescServicioTicAbr").val(),
            servicioTicDescripcion: $("#idDescServicioTic").val(),
            entePublicoAdmin:{idEntePublico:$("#idEntePublicoDirige").val()},
            unidadAdministrativaAdmin:{idUnidadAdministrativa:$("#idUnidadAdministrativaDirige").val()},
            curp:$("#curp").val(),
            idUsuarioEntePublico:$("#idUsuarioEntePublico").val()
        }),
        headers:headers
    };

    try {
        onLoading("dlgModalFormMisServicios");
        const res = await fetch(contexto+'/api/catalogo/tic/saveMiServicio', options);
        const respJSON = await res.json();

        if(res.ok){

            offLoading("dlgModalFormMisServicios");
            $('#dlgModalFormMisServicios').modal('hide');


            if ($("#idServicioTic").val()!=""){
                Toast.fire({
                    icon: "success",
                    title: "Los datos del servicio se actualizaron correctamente"
                });
                obtenMisServicios();

            }else{
                Toast.fire({
                    icon: "success",
                    title: "Los datos del servicio se guardaron correctamente"
                });

                obtenMisServicios();
            }

        } else{

            offLoading("dlgModalFormMisServicios");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }
    } catch (e) {
        offLoading("dlgModalFormMisServicios");
        Toast.fire({
            icon: "error",
            title: "Error al guardar el servicio"
        });
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

$("#btnCerrarSearch").click(function () {
    limpiarFormularioSearch();
    $('#dlgModalFormSearchUser').modal('hide');
    $('#dlgModalFormMisServicios').modal('show');

});

$("#formUsuarioSearch").validate({
    ignore: "",
    rules: {
        idEntePublico: "required",
        rfcSearch: {minlength: 4, maxlength: 13},
        curpSearch: {minlength: 4, maxlength: 18},
        nombreSearch: {minlength: 3},
        segundoNombreSearch: {minlength: 3},
        apellitoPaternoSearch: {minlength: 3},
        apellidoMaternoSearch: {minlength: 3}
    },
    messages: {
        idEntePublico: "Campo requerido",
        rfcSearch: "RFC mínimo 4 caracteres y máximo 13",
        curpSearch: "CURP mínimo 4 caracteres y máximo 18",
        nombreSearch: "Nombre mínimo 3 caracteres",
        segundoNombreSearch: "Segundo Nombre mínimo 3 caracteres",
        apellitoPaternoSearch: "Apellido Paterno mínimo 3 caracteres",
        apellidoMaternoSearch: "Apellido Materno mínimo 3 caracteres"
    },
    errorElement: "em",
    errorPlacement: function (error, element) {
        var n = element.attr("id");
        if (n=="idEntePublico")
            element.closest('.form-floating').append(error);
        else
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

function obtenUsuariosSearch() {

    $("#idCardUsuSearch").show();

    var parametro = {
        catUsuario:{
        rfc: $("#rfcSearch").val().trim(),
        curp: $("#curpSearch").val().trim(),
        nombre: $("#nombreSearch").val().trim(),
        segundoNombre: $("#segundoNombreSearch").val().trim(),
        apellidoPaterno: $("#apellitoPaternoSearch").val().trim(),
        apellidoMaterno: $("#apellidoMaternoSearch").val().trim(),
        isVigente: true,
        isHistorico: false,
        isNoVigente: false
        },
        entePublico:{
            idEntePublico:$("#idEntePublico").val()
        }
    };

    if (tableUsuarioSearch != null)
        tableUsuarioSearch.clear();

    tableUsuarioSearch = new DataTable('#tableUsuarioSearch', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering: false,
        bSort: false,
        ajax: {
            url: contexto + '/api/catalogoCatUsuario/tablaUsuarioPorDependenciaSearch',
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
         columnDefs: [
             /*{
                 "targets": 0,
                 "width": "200px",
                 "className": "text-left",
             },
             /!*{
                 "targets": 1,
                 "width": "250px",
                 "className": "text-center",
             },*/
             {
                 "targets": 3,
                 "width": "100px",
                 "className": "text-center",
             },
             {
                 "targets": 4,
                 "width": "80px",
                 "className": "text-center",
             }],
        lengthChange: false,
        select: false
    });

}

function seleccionarUsuario(idUsuarioEntePublico, rfc, curp, nombreCompleto){
    $("#idUsuarioEntePublico").val(idUsuarioEntePublico);
    $("#rfc").val(rfc);
    $("#curp").val(curp);
    $("#nombreSolicitante").val(nombreCompleto);

    $("#dlgModalFormSearchUser").modal("hide");
    $("#dlgModalFormMisServicios").modal("show");

}

function obtenMisServicios() {

    tableMisServicios = new DataTable('#tableMisServicios', {
        responsive: true,
        //sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: false,
        initComplete: function(settings, json) {
        $('[data-bs-toggle="tooltip"]').tooltip();
        },
        ajax: {
            url: contexto + '/api/mis-servicios/tablaMisServicios',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function (xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
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

}

function editarServicio(idServicioTic) {
    limpiarFormulario();
    $('#titleFormMisServicios').text("Editar servicio");
    $('#dlgModalFormMisServicios').modal('show');
    $("#idServicioTic").val(idServicioTic);
    getServicio(idServicioTic);
}

const getServicio = async (idServicioTic) =>{

    const options={
        method: "GET",
        header:headers
    };

    try {
        onLoading("dlgModalFormMisServicios");
        const res= await fetch(contexto+'/api/catalogo/tic/getServiciosTic/'+idServicioTic, options);
        const respJSON = await res.json();

        if (res.ok){
            $("#idServicioTic").val(respJSON.idServicioTic);

            if (respJSON.subTipoServicio!=null){
                idSubtipoServicioTemp=respJSON.subTipoServicio.idSubtipoServicioTic;
            }

            if (respJSON.tipoServicio!=null){
                $("#idTipoServicioTic").val(respJSON.tipoServicio.idTipoServicioTic);
                $("#idTipoServicioTic").trigger('change');
            }

            if (respJSON.entePublicoAdmin!=null){
                $("#idEntePublicoDirige").val(respJSON.entePublicoAdmin.idEntePublico);
                $("#idEntePublicoDirige").trigger('change');
            }

            if (respJSON.unidadAdministrativaAdmin!=null){
                $("#idUnidadAdministrativaDirige").val(respJSON.unidadAdministrativaAdmin.idUnidadAdministrativa);
                $("#descUnidadAdministrativaDirige").val(respJSON.unidadAdministrativaAdmin.nombreUnidadAdministrativa);
            }

            $("#idUsuarioEntePublico").val(respJSON.idUsuarioEntePublico);
            $("#idDescServicioTicAbr").val(respJSON.servicioTicAbreviadoDescripcion);
            $("#idDescServicioTic").val(respJSON.servicioTicDescripcion);

            if (respJSON.catUsuarioEntePublico!=null){
                $("#rfc").val(respJSON.catUsuarioEntePublico.catUsuario.rfc);
                $("#curp").val(respJSON.catUsuarioEntePublico.catUsuario.curp);
                $("#nombreSolicitante").val(respJSON.catUsuarioEntePublico.catUsuario.nombreCompleto);
            }


            offLoading("dlgModalFormMisServicios");
        }else {
            Toast.fire({
                icon:"error",
                title: respJSON.message
            });
            offLoading("dlgModalFormMisServicios");
        }
    }catch (e){
        offLoading("dlgModalFormMisServicios");
        Toast.fire({
            icon: "error",
            title: "Error al obtener los datos"
        });
    }
}

function enviarServicio(idServicio){
    let titleSave='¿Enviar servicio?';
    let msgSave='¿Estas seguro que deseas enviar el servicio?';

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
            enviarServicioAsync(idServicio);
        }
    });
}

const enviarServicioAsync = async(idServicio) => {

    const options={
        method: "POST",
        body: JSON.stringify({
            idServicioTic:idServicio
        }),
        headers:headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/mis-servicios/enviarServicio', options);
        const respJSON = await res.json();

        if(res.ok){

            offLoading(null);

            Toast.fire({
                icon: "success",
                title: "Servicio enviado correctamente"
            });
            obtenMisServicios();

        } else{

            offLoading(null);
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }
    } catch (e) {
        offLoading(null);
        Toast.fire({
            icon: "error",
            title: "Error al enviar el servicio"
        });
    }
}



function verNotaRechazo(nota){
    $("#idMotivoRechazo").val(nota);
    $("#dlgModalVerRechazoServicio").modal("show");
}

const cartaResponsivaAceptada = async (idCartaResponsiva) => {

    const options={
        method: "GET",
        headers: headers
    };

    try {
        onLoading("dlgModalCrAceptada");
        const res = await fetch(contexto+'/api/cartaResponsiva/cartaResponsivaAceptada/'+idCartaResponsiva, options);
        const respJSON = await res.json();

        if (res.ok){

            $("#idNombreReporteA").html("<h3><div class=\"text-center fw-bold\">"+respJSON.nombreReporte+"</div></h3>");
            $("#idTitleReporteA").html("<h3><div class=\"text-center fw-bold\">"+respJSON.tituloReporte+"</div></h3>");
            $("#fechaReporteA").html("<div class=\"text-end\">Villahermosa, Tabasco a "+respJSON.fechaAceptadaText+".</div>");
            $("#folioReporteA").html("<div class=\"text-start\"><span class=\"fw-bold\">Folio:</span>"+respJSON.folioCr+"</div>");
            $("#idCuerpoReporteA").html(respJSON.plantillaReporteText);


            $("#dlgModalCrAceptada").modal("show");

            offLoading("dlgModalCrAceptada");
        }else{
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalCrAceptada");
        }

    }catch (e) {
        var error =  JSON.parse(e.responseText);
        offLoading("dlgModalCrAceptada");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
}

const aceptarCartaResponsiva = async (idCartaResponsiva) => {

    const options={
        method: "GET",
        headers: headers
    };

    try {
        onLoading("dlgModalAceptarCr");
        const res = await fetch(contexto+'/api/cartaResponsiva/asignarPlantillaCartaResponsivaId/'+idCartaResponsiva, options);
        const respJSON = await res.json();

        if (res.ok){
            idCartaResponsivaTemp=idCartaResponsiva;

            $("#idNombreReporte").html("<h3><div class=\"text-center fw-bold\">"+respJSON.catPlantillaReporte.nombreReporte+"</div></h3>");
            $("#idTitleReporte").html("<h3><div class=\"text-center fw-bold\">"+respJSON.catPlantillaReporte.tituloReporte+"</div></h3>");
            $("#fechaReporte").html("<div class=\"text-end\">Villahermosa, Tabasco a "+respJSON.fechaActualText+".</div>");
            $("#folioReporte").html("<div class=\"text-start\"><span class=\"fw-bold\">Folio:</span>"+respJSON.folioCr+"</div>");
            $("#idCuerpoReporte").html(respJSON.plantillaReporteText);


            $("#idRechazarCr").prop('checked', false);
            $("#idAceptarCr").prop('checked', false);
            $("#motivoRechazo").val("");

            $("#dlgModalAceptarCr").modal("show");

            offLoading("dlgModalAceptarCr");
        }else{
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalAceptarCr");
        }

    }catch (e) {
        var error =  JSON.parse(e.responseText);
        offLoading("dlgModalAceptarCr");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
}

function confirmarCr(){

    if (!$("#idAceptarCr").is(":checked") && !$("#idRechazarCr").is(":checked")){
        Toast.fire({
            icon: "warning",
            title: "Seleccione una de las dos opciones de confirmación"
        });
    }else {

        let titleSave = 'Carta Responsiva';
        let msgSave = '¿Estás seguro que deseas aceptar la carta responsiva?';
        let confirmar = true;

        if ($("#idRechazarCr").is(":checked")) {

            if ($("#motivoRechazo").val().trim() == "") {
                confirmar = false;
                Toast.fire({
                    icon: "warning",
                    title: "Teclee el motivo de rechazo"
                });
            }
            titleSave = 'Carta Responsiva';
            msgSave = '¿Estás seguro que deseas rechazar la carta responsiva?';
        }

        if (confirmar) {
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
                    confirmarCrAsync();
                }
            });
        }
    }
}

const confirmarCrAsync = async () => {

    let funcionConfirmar = "aceptarCr";
    let mensaje = "Se realizó la aceptación correctamente";

    if ($("#idRechazarCr").is(":checked")) {
        funcionConfirmar = "rechazarCr";
        mensaje = "Se realizó el rechazo correctamente";
    }


    const options = {
        method: "PUT",
        body: JSON.stringify({
            idCartaResponsiva: idCartaResponsivaTemp,
            notaRechazo: $("#motivoRechazo").val()
        }),
        headers: headers
    };
    try {
        onLoading("dlgModalAceptarCr");
        const res = await fetch(contexto + '/api/cartaResponsiva/' + funcionConfirmar, options);
        const respJSON = await res.json();

        if (res.ok) {
            offLoading("dlgModalAceptarCr");

            $("#dlgModalAceptarCr").modal("hide");

            obtenMisServicios();

            Toast.fire({
                icon: "success",
                title: mensaje
            });

        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalAceptarCr");
        }
    } catch (e) {
        var error = JSON.parse(e.responseText);
        offLoading("dlgModalAceptarCr");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
}




