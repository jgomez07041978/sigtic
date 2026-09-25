
const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let isRolEnlaceTicServicio = $("#isRolEnlaceTicServicio").val();

let tableServicioDisponibles = null;
let tableServiciosRelacionados = null;
let tableServiciosRelacionadosPuerto = null;
let tableServiciosRelacionadosUsuarios = null;

let idEntePublicoSeleccionado=0;
let idSubtipoServicioTemp="";
let tableMisServicios = null;

let idServicioRechazo = null;


$(document).ready(function(){

    $('[data-bs-toggle="tooltip"]').tooltip();

    jQuery.event.special.mousewheel = {
        setup: function( _, ns, handle ){
            this.addEventListener("mousewheel", handle, { passive: true });
        }
    };


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

    $("#idControlInventario").hide();
    $("#nav-data-ips-tab").hide();
    $("#nav-data-puertos-tab").hide();
    $("#nav-data-usuarios-tab").hide();

    inicializarDependencias();

    obtenSeguimiento();

    $('#idEntePublicoAdministra').on('change', function() {
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


    fillSelectGet("/api/catalogo/tic/getEstatusServiciosTic","#idEstatusServicio", "cveEstatusServicioTic", "descEstatusServicioTic", null, null);
    fillSelectGet("/api/catalogo/tic/getCatTipoServiciosTic","#idTipoServicioTic", "idTipoServicioTic", "nombreTipoServicioTic", null, null);
    fillSelectGet("/api/catalogo/tic/getCatTipoImpactoServiciosTic","#idTipoImpactoServicio","cveTipoImpactoServicioTic", "descTipoImpactoServicioTic", null, null);
    fillSelectGet("/api/catalogo/tic/getEstadosServiciosTic","#idEstadoServicio","cveEstadoServicioTic", "descEstadoServicioTic", null, null);



    $("#idTipoServicioTic").on("change", function(){
        obtenerSubtipoServicio($("#idTipoServicioTic").val());
    })

    $("#idBtnUnidadAdmin").click(function () {
        $('#modal_arbol_unidad2').modal('show');
    });

    $("#idBtnUnidadOperativa").click(function () {
        $('#modal_arbol_unidad3').modal('show');
    });

    $("#txtBuscar2").keyup(function () {
        busqueda2($(this).val())
    });

    $("#txtBuscar3").keyup(function () {
        busqueda3($(this).val())
    });



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


});


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

    $("#idEntePublicoOpera").removeClass("is-valid");
    $("#idEntePublicoOpera").removeClass("is-invalid");
    $("#descUnidadAdminOpera").removeClass("is-valid");
    $("#descUnidadAdminOpera").removeClass("is-invalid");

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


const inicializarDependencias = async () =>{
    const options={
        method: "POST",
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/entePublico/listaFree', options);
        const respJSON = await res.json();

        llenarCombo('#idEntePublicoAdministra', 'idEntePublico','nombreEntePublicoConCve', respJSON);
        llenarCombo('#idEntePublicoOpera', 'idEntePublico','nombreEntePublicoConCve', respJSON);
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
            unidaddAdministrativaOpera:{idUnidadAdministrativa:$("#idUnidadAdminOpera").val()},
            numInventarioGet:$("#numInventarioGet").val(),
            numInventarioCi:$("#numInventarioCi").val(),
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

                Toast.fire({
                    icon: "success",
                    title: "Los datos del servicio se actualizaron correctamente"
                });

                $("#dlgModalFormServicio").modal("hide");
                obtenSeguimiento();

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
            title: "Error al actualizar el servicio"
        });
    }
}

function guardarServiciosTic(){

    let titleSave='¿Actualizar servicio?';
    let msgSave='¿Estas seguro que deseas actualizar los datos del servicio?';

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


function obtenSeguimiento() {

    tableMisServicios = new DataTable('#tableMisServicios', {
        responsive: true,
        //sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: false,
        ajax: {
            url: contexto + '/api/mis-servicios/tablaSeguimientos',
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
    limpiarServiciosTic();
    $('#titleFormMisServicios').text("Editar servicio");
    $('#dlgModalFormServicio').modal('show');
    $("#idServicioTic").val(idServicioTic);
    getServicio(idServicioTic);
}

const getServicio = async (idServicioTic) =>{

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
            }

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

            if (respJSON.entePublicoAdmin!=null){
                $("#idEntePublicoAdministra").val(respJSON.entePublicoAdmin.idEntePublico);
                $("#idEntePublicoAdministra").trigger('change');
            }

            if (respJSON.unidadAdministrativaAdmin!=null){
                $("#idUnidadAdminAdministra").val(respJSON.unidadAdministrativaAdmin.idUnidadAdministrativa);
                $("#descUnidadAdminAdministra").val(respJSON.unidadAdministrativaAdmin.nombreUnidadAdministrativa);
            }


            if (respJSON.entePublicoOpera!=null){
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

function rechazarServicio(idServicio){
    idServicioRechazo=idServicio
    $("#idMotivoRechazo").val("");
    $("#dlgModalRechazarServicio").modal("show");

}

function rechazarButton(){

    if ($("#idMotivoRechazo").val().trim()==""){
        Toast.fire({
            icon: "warning",
            title: "Teclee el motivo de rechazo"
        });
    }else{
        rechazarServicioAsync();
    }

}

const rechazarServicioAsync = async() => {

        const options={
            method: "POST",
            body: JSON.stringify({
                idServicioTic:idServicioRechazo,
                notaRechazo:$("#idMotivoRechazo").val()
            }),
            headers:headers
        };

        try {
            onLoading(null);
            const res = await fetch(contexto+'/api/mis-servicios/rechazarServicio', options);
            const respJSON = await res.json();

            if(res.ok){

                offLoading(null);

                Toast.fire({
                    icon: "success",
                    title: "Servicio rechazado correctamente"
                });
                $("#dlgModalRechazarServicio").modal("hide");
                obtenSeguimiento();

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
                title: "Error al rechazar el servicio"
            });
        }
}


function closeModalForm(){
    $("#dlgModalFormServicio").modal("hide");
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
                    clasificacion:'USUARIOS'
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

function closeModalFormAddServicio(){
    $("#dlgModalServiciosRelacionar").modal("hide");
    $("#dlgModalFormServicio").modal("show");
}


function autorizarServicio(idServicio){
    let titleSave='¿Autorizar servicio?';
    let msgSave='¿Estas seguro que deseas autorizar el servicio?';

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
            autorizarServicioAsync(idServicio);
        }
    });
}

const autorizarServicioAsync = async(idServicio) => {

    const options={
        method: "POST",
        body: JSON.stringify({
            idServicioTic:idServicio
        }),
        headers:headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/mis-servicios/autorizarServicio', options);
        const respJSON = await res.json();

        if(res.ok){

            offLoading(null);

            Toast.fire({
                icon: "success",
                title: "Servicio autorizado correctamente y se genera carta responsiva"
            });
            obtenSeguimiento();
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
            title: "Error al autorizar el servicio"
        });
    }
}



