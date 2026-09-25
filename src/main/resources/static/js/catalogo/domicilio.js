const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let tableDomicilio=null;
let tableUbicaciones=null;

var open=true;
var idEntePublicoTemporal=0;
var idDomicilioTemporal=0;
var listaEntesPublicos=[];

let idMunicipioTemp=-1;
let idLocalidadTemp=-1;

$(document).ready(function(){

    jQuery.event.special.mousewheel = {
        setup: function( _, ns, handle ){
            this.addEventListener("mousewheel", handle, { passive: true });
        }
    };


    $("#idEntePublicoSearch").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large"
    });

    $('.select2Pe').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormDomicilio")
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


    inicializarDependencias();
    
    $('#idEntePublicoSearch').on('change', function() {
        obtenerDomicilios($("#idEntePublicoSearch").val());
    });

    $('#idNuevoDomicilio').click(function () {
        limpiarFormDomicilio();

        $("#idEntidadFederativaEntePub").val(27);
        $("#idEntidadFederativaEntePub").trigger('change');
        getMunicipios($("#idEntidadFederativaEntePub").val(), null)
        getLocalidades(0, 0, null);
        $('#titleFormDomicilio').text("Nuevo Domicilio");

        if ($('#idEntePublicoSearch').val()!="") {
            $('#idEntePublicoEditarDom').val($('#idEntePublicoSearch').val());
            $("#idEntePublicoEditarDom").trigger('change');
        }
        else
        if ($('#idEntePublicoAsignado').val()!="") {
            $('#idEntePublicoEditarDom').val($('#idEntePublicoAsignado').val());
            $("#idEntePublicoEditarDom").trigger('change');
        }
        else {
            $('#idEntePublicoEditarDom').val("");
            $("#idEntePublicoEditarDom").trigger('change');
        }

        $('#dlgModalFormDomicilio').modal('show');

    });


    getTipoVialidad(null);
    getTipoAsentamientos(null);
    getEntidadesFederativas(null);

    $("#idEntidadFederativaEntePub").on('change',function(){
        getMunicipios($("#idEntidadFederativaEntePub").val(),null);
        getLocalidades($("#idEntidadFederativaEntePub").val(), 0, null);
    });

    $("#idMunicipioEntePublico").on('change',function(){
        getLocalidades($("#idEntidadFederativaEntePub").val(),$("#idMunicipioEntePublico").val(),null)
    });

    $( "#idFormDomicilio" ).validate( {
        ignore: "",
        rules: {
            idTipoVialidadEntePublico: "required"
            ,calleEntePublico: "required"
            ,numExteriorEntePublico: "required"
            ,idTipoAsentamientoEntePub: "required"
            ,coloniaEntePublico: "required"
            ,idEntidadFederativaEntePub: "required"
            ,idMunicipioEntePublico: "required"
            ,idLocalidadEntePublico: "required"
            ,codigoPostalLocalidadEP: "required"
            ,cveEstatusDom: "required"
            ,numOrdenDomicilio: "required"
            ,idEntePublicoEditarDom: "required"
        },
        messages: {
            idTipoVialidadEntePublico: "Selecciona el tipo de vialidad"
            ,calleEntePublico: "Escriba el nombre de la vialidad"
            ,numExteriorEntePublico: "Escriba el número exterior"
            ,idTipoAsentamientoEntePub: "Seleccione el tipo de asentamiento"
            ,coloniaEntePublico: "Escriba el nombre del asentamiento"
            ,idEntidadFederativaEntePub: "Seleccione el estado"
            ,idMunicipioEntePublico: "Seleccione el municipio"
            ,idLocalidadEntePublico: "Seleccione la localidad"
            ,codigoPostalLocalidadEP: "Escriba el código postal"
            ,cveEstatusDom: "Seleccione el estatus"
            ,numOrdenDomicilio: "Campo Requerido"
            ,idEntePublicoEditarDom: "Seleccione el ente público"
        },
        errorElement: "em",
        errorPlacement: function (error, element) {
            var n = element.attr("id");
           if (n=="idEntePublicoEditarDom" || n=="idTipoVialidadEntePublico" || n=="idTipoAsentamientoEntePub" || n=="idEntidadFederativaEntePub"
               || n=="idMunicipioEntePublico" || n=="idLocalidadEntePublico")
                element.closest('.form-floating').append(error);
            else
                error.insertAfter(element);

            error.addClass( "invalid-feedback" );
        },
        highlight: function (element, errorClass) {
            $( element ).addClass( "is-invalid" ).removeClass( "is-valid" );
        },
        unhighlight: function (element, errorClass) {
            $( element ).addClass( "is-valid" ).removeClass( "is-invalid" );
        }
    } );

    $("#idFormDomicilio").submit(function(e) {
        e.preventDefault();
        if($('#idFormDomicilio').valid()) {
            guardarDomicilio();
            return true;
        }
        else {
            $('#datos-generales-nav').tab('show');
            return false;
        }

    });


    $( "#idFormUbicacion" ).validate( {
        ignore: "",
        rules: {
            descUbicacionAbrev: "required"
            ,descUbicacion: "required"
            ,referenciaUbicacion: "required"
            ,numOrden: "required"
            ,correoUbicacion:{
                email:function (){
                    if ($("#correoUbicacion").val().trim()!="")
                        return true;
                    else return false;
                }
            },
            telefonoUbicacion:{
                required: function () {
                    if ($("#telefonoUbicacion").val().trim()!="")
                        return true;
                    else
                        return false;
                }
                ,minlength: 10, maxlength: 13
            },



        },
        messages: {
            descUbicacionAbrev: "Campo Requerido"
            ,descUbicacion: "Campo Requerido"
            ,referenciaUbicacion: "Campo Requerido"
            ,numOrden: "Campo Requerido"
            ,correoUbicacion: "Correo Invalido"
            ,telefonoUbicacion: "Número Incorrecto"

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
        }
    } );

    $("#idFormUbicacion").submit(function(e) {
        e.preventDefault();
        if($('#idFormUbicacion').valid()) {
            actualizarUbicacion();
            return true;
        }else
            return false;

    });


    $('#btnNuevaUbicacion').click(function () {
        limpiarFormUbicacion();
        $('#titleFormUbicacion').text("Nueva Ubicación");
        $('#dlgModalFormUbicacion').modal('show');

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

        llenarCombo('#idEntePublicoSearch', 'idEntePublico','nombreEntePublicoConCve', respJSON);
        $("#idEntePublicoSearch").val($("#idEntePublicoSession").val());
        $("#idEntePublicoSearch").trigger('change');
        llenarCombo('#idEntePublicoEditarDom','idEntePublico','nombreEntePublicoConCve', respJSON)

        offLoading(null);
    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
}

function obtenerDomicilios(idEntePublicoSearch){

    if (tableDomicilio!=null)
        tableDomicilio.clear();

    tableDomicilio = new DataTable('#tableDomicilios', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        autoWidth: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/catalogo/domicilioentePublico/tablaDomicilio',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    idEntePublico:idEntePublicoSearch
                }));
            }
        },
        columnDefs: [{
            targets: 0,
            "className": "text-center",
            width:"100px"
        },{
            targets: 2,
            "className": "text-center",
            width:"150px"
        },{
            targets: 3,
            "className": "text-center",
            width:"120px"
        },{
            targets: 4,
            "className": "text-center",
            width:"150px"
        }],
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json'
        },
        lengthChange: false,
        select: false
    });

}

function obtenerUbicaciones(idDomicilio, idEntePublico){

    idDomicilioTemporal=idDomicilio;
    idEntePublicoTemporal=idEntePublico;

    $("#dlgModalUbicaciones").modal("show");

    if (tableUbicaciones!=null)
        tableUbicaciones.clear();

    tableUbicaciones = new DataTable('#tableUbicaciones', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        autoWidth: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/catalogo/ubicacionUnidadAministrativa/tablaUbicaciones',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    idDomicilio:idDomicilio,
                    idEntePublico:idEntePublico
                }));
            },
            complete: function (data) {
                $('#entePublicoText').text((data['responseJSON'].nombreEntePublico));
                $('#domicilioText').text((data['responseJSON'].nombreDomicilio));
            }
        },
        columnDefs: [{
            targets: 0,
            "className": "text-center",
            width:"100px"
        },{
            targets: 2,
            "className": "text-center",
            width:"120px"
        },
            {
                targets: 3,
                "className": "text-center",
                width:"150px"
            }],
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json'
        },
        lengthChange: false,
        select: false
    });

}

function limpiarFormDomicilio() {
    $("#idTipoVialidadEntePublico").removeClass( "is-valid" );
    $("#idTipoVialidadEntePublico").removeClass( "is-invalid" );
    $("#calleEntePublico").removeClass( "is-valid" );
    $("#calleEntePublico").removeClass( "is-invalid" );
    $("#numExteriorEntePublico").removeClass( "is-valid" );
    $("#numExteriorEntePublico").removeClass( "is-invalid" );
    $("#idTipoAsentamientoEntePub").removeClass( "is-valid" );
    $("#idTipoAsentamientoEntePub").removeClass( "is-invalid" );
    $("#coloniaEntePublico").removeClass( "is-valid" );
    $("#coloniaEntePublico").removeClass( "is-invalid" );
    $("#idEntidadFederativaEntePub").removeClass( "is-valid" );
    $("#idEntidadFederativaEntePub").removeClass( "is-invalid" );
    $("#idMunicipioEntePublico").removeClass( "is-valid" );
    $("#idMunicipioEntePublico").removeClass( "is-invalid" );
    $("#idLocalidadEntePublico").removeClass( "is-valid" );
    $("#idLocalidadEntePublico").removeClass( "is-invalid" );
    $("#codigoPostalLocalidadEP").removeClass( "is-valid" );
    $("#codigoPostalLocalidadEP").removeClass( "is-invalid" );
    $("#cveEstatusDom").removeClass( "is-valid" );
    $("#cveEstatusDom").removeClass( "is-invalid" );
    $("#idEntePublicoEditarDom").removeClass( "is-valid" );
    $("#idEntePublicoEditarDom").removeClass( "is-invalid" );

    idMunicipioTemp=-1;
    idLocalidadTemp=-1;

    $("#idDomicilioEntePublico").val("");
    $('#divDomicilioCompleto').val("");
    $('#divDomicilioCompleto').hide();
    $("#idTipoVialidadEntePublico").val("");
    $("#calleEntePublico").val("");
    $("#numExteriorEntePublico").val("");
    $("#numInteriorEntePublico").val("");
    $("#numTelefonoEntePublico").val("");
    $("#numExtensionEntePublico").val("");
    $("#idTipoAsentamientoEntePub").val("");
    $("#coloniaEntePublico").val("");
    $("#idEntidadFederativaEntePub").val("");
    $("#idMunicipioEntePublico").val("");
    $("#idLocalidadEntePublico").val("");
    $("#codigoPostalLocalidadEP").val("");
    $("#referenciaEntreCalle").val("");
    $("#referenciaCallePosterior").val("");
    $("#referenciaDomicilioEntePub").val("");
    $("#cveEstatusDom").val("VIG");
    $("#idEntePublicoEditarDom").val("");
    $("#descDomicilioEntePublicoAbreviado").val("");
    $("#numOrdenDomicilio").val("");
    $("#idColorDomicilio").val("");
    $("#idImagenDomicilio").val("");
    $("#latitudDomicilio").val("");
    $("#longitudDomicilio").val("");
    $("#domicilioFull").val("");
}

function obtenEntesPublicos(){
    $.ajax({ url: contexto+'/api/entePublico/usuario',
        type: 'GET',
        dataType: 'JSON',
        async: false,
        //data: [],
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function(response) {
            listaEntesPublicos=response
        },
        error: function (error) {
            Swal.fire({
                title: 'Error',
                text: 'Error al obtener los datos de los entes públicos',
                icon: 'error',
            });
        }
    });
}

function getTipoVialidad(callback){
    fillSelectDinamicoGet('/api/tipo-vialidad', '#idTipoVialidadEntePublico','idTipoVialidad','nombreTipoVialidad', null, null, callback);
}

function getTipoAsentamientos(callback){
    fillSelectDinamicoGet('/api/tipo-asentamiento', '#idTipoAsentamientoEntePub','idTipoAsentamiento','nombreTipoAsentamiento', null, null, callback);
}

function getEntidadesFederativas(callback){
    fillSelectDinamicoGet('/api/entidad-federativa', '#idEntidadFederativaEntePub','idEntidadFederatia','descripcionEntidadFederativa', null, null, callback);
}

function getMunicipios(idPadre, callback){
    fillSelectGetInterno('/api/municipios', '#idMunicipioEntePublico','idMunicipio','descripcionMunicipio',idPadre, null, callback,"municipio");
}

function getLocalidades(idPadre, idPadre2, callback){
    fillSelectGetInterno('/api/localidades', '#idLocalidadEntePublico','idLocalidad','descripcionLocalidad',idPadre, idPadre2, callback,"localidad");
}

async function fillSelectGetInterno(relativeUrl,idElemento, value, descripcion, idPadre, idPadre2, callback, opcion){

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

    if (idPadre2!=null)
        url=url+"/"+idPadre2


    onLoading(null);
    const res = await fetch(url, options);
    const respJSON = await res.json();

    if (res.ok){
        llenarCombo(idElemento, value, descripcion, respJSON);

        if (opcion=="municipio") {
            if (idMunicipioTemp != null && idMunicipioTemp != -1) {
                $("#idMunicipioEntePublico").val(idMunicipioTemp);
                $("#idMunicipioEntePublico").trigger('change');
            }
        }

        if (opcion=="localidad") {
            if (idLocalidadTemp != null && idLocalidadTemp != -1) {
                $("#idLocalidadEntePublico").val(idLocalidadTemp);
                $("#idLocalidadEntePublico").trigger('change');
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

function getEntesDomicilio(idEntePublicoParam,callback){
    fillSelectDinamico('/api/catalogo/entes-domicilios', '#idDomicilioUbi','id','descripcion', idEntePublicoParam, null, callback);
}

function guardarDomicilio(){

    let msgTitle= 'Guardar Domicilio'
    let msgText= '¿Estas seguro que deseas guardar el domicilio?'
    let msgRespuesta= 'Domicilio guardado correctamente'

    if ($("#idDomicilioEntePublico").val()!=""){
        msgTitle= 'Actualizar Domicilio'
        msgText= '¿Estas seguro que deseas actualizar el domicilio?'
        msgRespuesta= 'Domicilio actualizado correctamente'
    }

    Swal.fire({
        title: msgTitle,
        text: msgText,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No",
        closeOnCancel: false,
        closeOnConfirm: true,
        reverseButtons: true,
    }).then(function (result) {
        if (result.isConfirmed) {

            $.ajax({
                url: contexto + '/api/catalogo/domicilioentePublico/save',
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify({
                    idDomicilioEntePublico: $("#idDomicilioEntePublico").val(),
                    entePublico: {idEntePublico: $("#idEntePublicoEditarDom").val()},
                    idTipoVialidad: $("#idTipoVialidadEntePublico").val(),
                    calle: $("#calleEntePublico").val(),
                    numExterior: $("#numExteriorEntePublico").val(),
                    numInterior: $("#numInteriorEntePublico").val(),
                    numTelefono: $("#numTelefonoEntePublico").val(),
                    numExtension: $("#numExtensionEntePublico").val(),
                    idTipoAsentamiento: $("#idTipoAsentamientoEntePub").val(),
                    colonia: $("#coloniaEntePublico").val(),
                    idEntidadFederativa: $("#idEntidadFederativaEntePub").val(),
                    idMunicipio: $("#idMunicipioEntePublico").val(),
                    idLocalidad: $("#idLocalidadEntePublico").val(),
                    idCodigoPostal: $("#codigoPostalLocalidadEP").val(),
                    codigoPostalLocalidadEp: $("#codigoPostalLocalidadEP").val(),
                    referenciaEntreCalle: $("#referenciaEntreCalle").val(),
                    referenciaCallePosterior: $("#referenciaCallePosterior").val(),
                    referenciaDomicilio: $("#referenciaDomicilioEntePub").val(),
                    cveEstatus: $("#cveEstatusDom").val(),
                    nombreDomicilioAbrev: $("#descDomicilioEntePublicoAbreviado").val(),
                    numOrden: $("#numOrdenDomicilio").val(),
                    latitud: $("#latitudDomicilio").val(),
                    longitud: $("#longitudDomicilio").val()

                }),
                contentType: 'application/json',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                },
                success: function (response) {

                    $("#idEntePublicoSearch").val($("#idEntePublicoEditarDom").val());
                    obtenerDomicilios($("#idEntePublicoSearch").val());
                    $('#dlgModalFormDomicilio').modal('hide');

                    Toast.fire({
                        icon: "success",
                        title: msgRespuesta
                    });

                },
                error: function (response) {

                }
            });
        }

    });
}

function editarDomicilio(idDomicilio){

    $('#preloader').modal('show');
    $.ajax({
        url: contexto + '/api/catalogo/domicilioentePublico/get/' + idDomicilio,
        type: 'GET',
        dataType: 'JSON',
        contentType: 'application/json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function (response) {

            $("#titleFormDomicilio").text("Editar Domicilio");
            console.log(response);
            console.log(response.idTipoVialidad);

            $("#domicilioFull").val(response.descDomicilioEntePublico);
            $("#idDomicilioEntePublico").val(response.idDomicilioEntePublico);
            $("#idEntePublicoEditarDom").val(response.entePublico.idEntePublico);
            $("#idEntePublicoEditarDom").trigger('change');
            $("#idTipoVialidadEntePublico").val(response.idTipoVialidad);
            $("#idTipoVialidadEntePublico").trigger('change');
            $("#calleEntePublico").val(response.calle);
            $("#numExteriorEntePublico").val(response.numExterior);
            $("#numInteriorEntePublico").val(response.numInterior);
            $("#numTelefonoEntePublico").val(response.numTelefono);
            $("#numExtensionEntePublico").val(response.numExtension);
            $("#idTipoAsentamientoEntePub").val(response.idTipoAsentamiento);
            $("#idTipoAsentamientoEntePub").trigger('change');
            $("#coloniaEntePublico").val(response.colonia);
            $("#idEntidadFederativaEntePub").val(response.idEntidadFederativa);
            $("#idEntidadFederativaEntePub").trigger('change');
            idMunicipioTemp=response.idMunicipio;
            getMunicipios($("#idEntidadFederativaEntePub").val(),null);
            idLocalidadTemp=response.idLocalidad;
            getLocalidades(idMunicipioTemp, 0, null);

            $("#codigoPostalLocalidadEP").val(response.codigoPostalLocalidadEp);
            $("#referenciaEntreCalle").val(response.referenciaEntreCalle);
            $("#referenciaCallePosterior").val(response.referenciaCallePosterior);
            $("#referenciaDomicilioEntePub").val(response.referenciaDomicilio);
            $("#cveEstatusDom").val(response.cveEstatus);
            $("#descDomicilioEntePublicoAbreviado").val(response.nombreDomicilioAbrev);
            $("#numOrdenDomicilio").val(response.numOrden);
            $("#latitudDomicilio").val(response.latitud);
            $("#longitudDomicilio").val(response.longitud);

            $('#preloader').modal('hide');
            $('#dlgModalFormDomicilio').modal('show');

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

function eliminarDomicilio(idDomicilio){

    Swal.fire({
        title: 'Eliminar Domicilio',
        text: '¿Estás seguro que deseas eliminar el domicilio?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No",
        reverseButtons: true,
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminaDomicilioRest(idDomicilio);
        }});
}

function eliminaDomicilioRest(idDomicilio) {
    $('#preloader').modal('show');
    $.ajax({
        url: contexto+'/api/catalogo/domicilioentePublico/delete',
        type: 'DELETE',
        contentType: 'application/json',
        dataType: 'JSON',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        data: JSON.stringify({
            idDomicilioEntePublico: idDomicilio,
        }),
        success: function (response) {
            $('#preloader').modal('hide');
            if( response !== null){

                obtenerDomicilios($("#idEntePublicoSearch").val());

                Toast.fire({
                    icon: "success",
                    title: "El domicilio se eliminó correctamente"
                });

            }
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


function editarUbicacion(idUbicacion){

    limpiarFormUbicacion();
    $('#titleFormUbicacion').text("Editar Ubicación");

    $.ajax({ url: contexto+'/api/catalogo/ubicacionUnidadAministrativa/get/'+idUbicacion,
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function(response) {
            $("#idUbicacionAdaministrativa").val(response.idUbicacionAdaministrativa);
            $("#descUbicacion").val(response.nombreUbicacion);
            $("#descUbicacionAbrev").val(response.nombreUbicacionAbreviada);
            $("#referenciaUbicacion").val(response.referencia);
            $("#correoUbicacion").val(response.correoUnidad);
            $("#telefonoUbicacion").val(response.numTelefono);
            $("#extensionUbicacion").val(response.extension);
            $("#cveEstatus").val(response.cveEstatus);
            $("#ubicacionTitular").val(response.ubicacionTitularEntePublico);
            $("#numOrden").val(response.numOrden);
            $('#dlgModalFormUbicacion').modal('show');
        },
        error: function (response) {

        }
    });
}

function limpiarFormUbicacion() {

    $("#descUbicacionAbrev").removeClass( "is-valid" );
    $("#descUbicacionAbrev").removeClass( "is-invalid" );
    $("#descUbicacion").removeClass( "is-valid" );
    $("#descUbicacion").removeClass( "is-invalid" );
    $("#referenciaUbicacion").removeClass( "is-valid" );
    $("#referenciaUbicacion").removeClass( "is-invalid" );
    $("#cveEstatus").removeClass( "is-valid" );
    $("#cveEstatus").removeClass( "is-invalid" );
    $("#ubicacionTitular").removeClass( "is-valid" );
    $("#ubicacionTitular").removeClass( "is-invalid" );

    $("#idUbicacionAdaministrativa").val("");
    $("#descUbicacion").val("");
    $("#descUbicacionAbrev").val("");
    $("#referenciaUbicacion").val("");
    $("#correoUbicacion").val("");
    $("#telefonoUbicacion").val("");
    $("#extensionUbicacion").val("");
    $("#cveEstatus").val("VIG");
    $("#ubicacionTitular").val("N");
    $("#numOrden").val("");
}


function actualizarUbicacion(){

    let msgTitle= 'Actualizar Ubicación';
    let msgText= '¿Estas seguro que deseas actualizar la ubicación?';
    let msgRespuesta= 'Ubicación actualizado correctamente';

    if ($("#idUbicacionAdaministrativa").val()==""){
        msgTitle= 'Nueva Ubicación';
        msgText= '¿Estas seguro que deseas guardar la nueva ubicación?';
        msgRespuesta= 'Ubicación guardada correctamente';
    }


    Swal.fire({
        title: msgTitle,
        text: msgText,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No",
        closeOnCancel: false,
        closeOnConfirm: true,
        reverseButtons: true,
    }).then(function (result) {
        if (result.isConfirmed) {

            $.ajax({
                url: contexto + '/api/catalogo/ubicacionUnidadAministrativa/save',
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify({
                    idUbicacionAdaministrativa: $("#idUbicacionAdaministrativa").val(),
                    domicilioEntePublico:{idDomicilioEntePublico:idDomicilioTemporal},
                    nombreUbicacionAbreviada: $("#descUbicacionAbrev").val(),
                    nombreUbicacion: $("#descUbicacion").val(),
                    referencia: $("#referenciaUbicacion").val(),
                    correoUnidad: $("#correoUbicacion").val(),
                    numTelefono: $("#telefonoUbicacion").val(),
                    extension: $("#extensionUbicacion").val(),
                    ubicacionTitularEntePublico: $("#ubicacionTitular").val(),
                    cveEstatus: $("#cveEstatus").val(),
                    numOrden: $("#numOrden").val()
                }),
                contentType: 'application/json',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                },
                success: function (response) {
                    obtenerDomicilios($("#idEntePublicoSearch").val());
                    obtenerUbicaciones( idDomicilioTemporal, idEntePublicoTemporal);
                    $('#dlgModalFormUbicacion').modal('hide');

                    Toast.fire({
                        icon: "success",
                        title: msgRespuesta
                    });

                },
                error: function (response) {

                }
            });
        }

    });
}


function eliminarUbicacion(idUbicacion){

    Swal.fire({
        title: 'Eliminar Ubicación',
        text: '¿Estás seguro que deseas eliminar la ubicación?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No",
        reverseButtons: true,
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminaUbicacionRest(idUbicacion);
        }});
}

function eliminaUbicacionRest(idUbicacion) {
    $('#preloader').modal('show');
    $.ajax({
        url: contexto+'/api/catalogo/ubicacionUnidadAministrativa/delete',
        type: 'DELETE',
        contentType: 'application/json',
        dataType: 'JSON',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        data: JSON.stringify({
            idUbicacionAdaministrativa: idUbicacion,
        }),
        success: function (response) {
            $('#preloader').modal('hide');
            if( response !== null){

                obtenerDomicilios($("#idEntePublicoSearch").val());
                obtenerUbicaciones( idDomicilioTemporal, idEntePublicoTemporal);

                Toast.fire({
                    icon: "success",
                    title: "la ubicación se eliminó correctamente"
                });

            }
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
