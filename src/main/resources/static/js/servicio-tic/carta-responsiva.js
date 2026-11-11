var isAdministradorCr=$("#isAdministradorCr").val();

const headers = {
    "X-CSRF-Token": $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let tableCartaResponsiva=null;
let tableCartaResponsivaDetalle=null;
let tableCartaResponsivaSeguimiento=null;
let idCartaResponsivaTemp=null;
let idEstatusCartaResponsivaTemp=null;
let tableServicioDisponible = null;
let idEntePublicoSeleccionado = null;
let idEstatusTemp=null;

let idSubTipoServicioTicTemp=null;
let idTipoServicioTicTemp=null;

let refrescarDatosPlantilla=false;

$(document).ready(function () {

   //generarQr("https://sigtic.tabasco.gob.mx/");

    jQuery.event.special.mousewheel = {
        setup: function (_, ns, handle) {
            this.addEventListener("mousewheel", handle, {passive: true});
        }
    };

    $('#idEntePublicoSearch').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
    });

    $('.select2Se').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalCatUsuario")
    });

    $('.select2Se')
        .parent('div')
        .children('span')
        .children('span')
        .children('span')
        .css('height', ' calc(3.5rem + 2px)');

    $('.select2Se')
        .parent('div')
        .children('span')
        .children('span')
        .children('span')
        .children('span')
        .css('margin-top', '18px');

    $('.select2Se')
        .parent('div')
        .find('label')
        .css('z-index', '1');

    $('#idEntePublicoSearch').on('change', function () {
        $("#idAlertReport").hide();
        obtieneCartasResponsivas();
    });

    $('#idEnteseleccionado').on('change', function () {
        idEntePublicoSeleccionado=$('#idEnteseleccionado').val();
        getTablaCatUsuarios();
    });


    //inicializarDependenciasSaig();
    inicializarDependencias();

    if (isAdministradorCr) {

        fillSelectGet("/api/catalogo/tic/getCatTipoServiciosTicCr", "#idTipoServicioTic", "idTipoServicioTic", "nombreTipoServicioTic", null, null);

        $("#idTipoServicioTic").on("change", function () {
            $("#idSubtipoServicioTic").val("");
            obtenerSubtipoServicio($("#idTipoServicioTic").val());
        });

        $("#idSubtipoServicioTic").on("change", function () {
            $("#idAlertReport").hide();
            plantillasConfiguradas();
        });

        $("#idFormGenerar").validate({
            ignore: "",
            rules: {
                idEntePublicoSearch: "required",
                idTipoServicioTic: "required",
                idSubtipoServicioTic: "required",
                firmada: "required"
            },
            messages: {
                idEntePublicoSearch: "Campo requerido",
                idTipoServicioTic: "Campo requerido",
                idSubtipoServicioTic: "Campo requerido",
                firmada: "Campo requerido"
            },
            errorElement: "span",
            errorPlacement: function (error, element) {
                /*var n = element.attr("id");
                if (n=="descUnidadAdminAdministra" || n=="descUnidadAdminOpera")
                    element.closest('.input-group').append(error);
                else*/
                element.closest('.form-floating').append(error);
                error.addClass("invalid-feedback");
                //error.insertAfter(element);
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


        $("#idFormGenerar").submit(function (e) {
            e.preventDefault();
            if ($('#idFormGenerar').valid()) {
                generarCartaResponsiva();
                return true;
            } else {
                return false;
            }
        });
    }

    $('#estatusVig').change(function() {
        obtieneCartasResponsivas();
    });

    $('#estatusHis').change(function() {
        obtieneCartasResponsivas();
    });

});



function obtenerSubtipoServicio(idTipoServicio){
    if (idTipoServicio!="")
        fillSelectGet("/api/catalogo/tic/getCatSubtipoServiciosTicCr","#idSubtipoServicioTic", "idSubtipoServicioTic", "nombreSubtipoServicioTic", idTipoServicio, null);
    else {
        $("#idSubtipoServicioTic").empty();
        var dropdown =$("#idSubtipoServicioTic");
        dropdown.append($('<option></option>').attr('value', '').text('Seleccione'));
    }
}

const inicializarDependencias = async () => {
    const options = {
        method: "POST",
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/entePublico/listaEnteCr', options);
        const respJSON = await res.json();

        llenarCombo('#idEntePublicoSearch', 'idEntePublico', 'nombreEntePublicoConCve', respJSON);
        llenarCombo('#idEnteseleccionado', 'idEntePublico', 'nombreEntePublicoConCve', respJSON);
        $("#idEntePublicoSearch").val($("#idEntePublicoSession").val());
        $("#idEntePublicoSearch").trigger('change');
        $("#idEnteseleccionado").val($("#idEntePublicoSession").val());
        getTablaCatUsuarios();


        offLoading(null);
    } catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
};

function obtieneCartasResponsivas() {

    let idEntePublico=$("#idEntePublicoSession").val();

    if ($("#idEntePublicoSearch").val()!=null && $("#idEntePublicoSearch").val()!=undefined)
        idEntePublico=$("#idEntePublicoSearch").val();


    var formulario={
        entePublico:{idEntePublico:idEntePublico},
        "isEstatusVig":$("#estatusVig").is(':checked'),
        "isEstatusHis":$("#estatusHis").is(':checked')
    };

    if (tableCartaResponsiva!=null)
        tableCartaResponsiva.clear();

    tableCartaResponsiva = new DataTable('#tableCartaResponsiva', {
        responsive: true,
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: false,
        autoWidth: false,
        columnDefs: [
            {
                targets: 0,
                "width": "150px",
                "className": "text-center"
            },
            {
                targets: 1,
                "width": "180px",
                "className": "text-left"
            },
            {
                targets: 2,
                "width": "180px",
                "className": "text-left"
            },
            /*{
                targets: 3,
                "width": "90px",
                "className": "text-center"
            },*/{
                targets: 4,
                "width": "100px",
                "className": "text-center"
            },{
                targets: 5,
                "width": "90px",
                "className": "text-center"
            },{
                targets: 6,
                "width": "90px",
                "className": "text-center"
            },{
                targets: 7,
                "width": "70px",
                "className": "text-center"
            },{
                targets: 8,
                "width": "130px",
                "className": "text-center"
            }],
        initComplete: function(settings, json) {
            $('[data-bs-toggle="tooltip"]').tooltip();
        },

        ajax: {
            url: contexto+'/api/cartaResponsiva/getCartaResponsiva',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend(formulario, d, {}));
            }
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


const generarCartaResponsivaAsync = async () =>{

    const options={
        method: "POST",
        body: JSON.stringify({
            entePublico:{idEntePublico: $("#idEntePublicoSearch").val()},
            entePublicoResponsable:{idEntePublico: $("#idEntePublicoResponsable").val()},
            catSubtipoServiciosTic:{idSubtipoServicioTic:$("#idSubtipoServicioTic").val()},
            usuario:{idUsuario:$("#idUsuario").val()},
            firmada: $("#firmada").val(),
            notificacionEntregaServicio:$("#notificacionEntrega").val().trim()
        }),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/cartaResponsiva/guardarCartaResponsiva', options);
        const respJSON = await res.json();
        if (res.ok) {

            offLoading(null);

            Toast.fire({
                icon: "success",
                title: "Carta Responsiva generada correctamente"
            });
            $('#dlgModalFormUsuarioCr').modal('hide');
            $("#firmada").val("").removeClass("is-valid is-invalid");
            obtieneCartasResponsivas();

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
            title: "Error al generar la carta responsiva"
        });
    }
};

function generarCartaResponsiva(){

        if ($("#isAdministradorSigtic").val()=="true" || $("#isRolEnlaceCartaResponsiva").val()=="true"){
            modalCatUsuarios();
        }else {

            let titleSave = 'Carta Responsiva';
            let msgSave = '¿Estás seguro que deseas generar una carta responsiva?';


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
                    generarCartaResponsivaAsync();
                }
            });
        }
}

function generarCartaResponsivaAdmin(){

        if ($("#idUsuario").val()==""){
            Toast.fire({
                icon: "warning",
                title: "Seleccione el usuario"
            });
        }else {

            let titleSave = 'Carta Responsiva';
            let msgSave = '¿Estas seguro que deseas generar una carta responsiva?';


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
                    generarCartaResponsivaAsync();
                }
            });
        }

}

function obtieneCartasResponsivaDetalle(idCartaResponsiva, folio, idEstatusCartaResponsiva) {

        $("#idAddServicio").hide();
    if (idEstatusCartaResponsiva==1 || idEstatusCartaResponsiva==3)
        $("#idAddServicio").show();

    getCartaResponsivaId(idCartaResponsiva);
    $("#dlgModalCartaResponsiva").modal("show");
    $("#idFolioCr").text(folio);

    idCartaResponsivaTemp=idCartaResponsiva;
    idEstatusCartaResponsivaTemp=idEstatusCartaResponsiva;

    var formulario={
        idCartaResponsiva:idCartaResponsiva
    };

    if (tableCartaResponsivaDetalle!=null)
        tableCartaResponsivaDetalle.clear();

    tableCartaResponsivaDetalle = new DataTable('#tableCartaResponsivaDetalle', {
        responsive: true,
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/cartaResponsiva/getCartaResponsivaDetalle',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend(formulario, d, {}));
            }
        },
        columnDefs: [
            {
                "targets": 2,
                "width": "90px",
                "className": "text-center",
            },
            {
                "targets": 3,
                "width": "90px",
                "className": "text-center",
            },
            {
                "targets": 4,
                "width": "70px",
                "className": "text-center",
            },
            {
                "targets": 5,
                "width": "70px",
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

function obtieneServiciosDisponibles() {
    $("#dlgModalAddServicio").modal("show");
    $("#idAddFolioCr").text($("#idFolioCr").text());


    var formulario={
        idCartaResponsiva:idCartaResponsivaTemp,
        catSubtipoServiciosTic:{idSubtipoServicioTic:idSubTipoServicioTicTemp},
        idTipoServicio:idTipoServicioTicTemp
    };

    if (tableServicioDisponible!=null)
        tableServicioDisponible.clear();

    tableServicioDisponible = new DataTable('#tableServiciosDisponibles', {
        responsive: true,
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/cartaResponsiva/getServiciosDisponibles',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend(formulario, d, {}));
            }
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

const addServicioCrAsync = async (idServicio,idCartaResponsiva) =>{
    const options={
        method: "POST",
        body: JSON.stringify({
            serviciosTic:{idServicioTic:idServicio},
            cartaResponsiva:{idCartaResponsiva:idCartaResponsiva}
        }),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/cartaResponsiva/addServicioCartaResponsiva', options);
        const respJSON = await res.json();
        if (res.ok) {

            offLoading(null);

            Toast.fire({
                icon: "success",
                title: "Servicio agregado correctamente"
            });

            obtieneServiciosDisponibles();
            obtieneCartasResponsivas();
            obtieneCartasResponsivaDetalle(idCartaResponsivaTemp,$("#idFolioCr").text(), idEstatusCartaResponsivaTemp);

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
            title: "Error al add el servicio"
        });
    }
}

function addServicioCr(idServicio, idCartaResponsiva){
    Swal.fire({
        title: "Agregar Servicios",
        text: "¿Esta seguro que desea agregar el servicio?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function (result) {
        if (result.isConfirmed) {
            addServicioCrAsync(idServicio,idCartaResponsiva);
        }
    });
}

const eliminarServicioCartaResponsivaAsync = async (idCartaResponsivaDetalle) =>{
    const options={
        method: "DELETE",
        body: JSON.stringify({
            idCartaResponsivaDetalle:idCartaResponsivaDetalle
        }),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/cartaResponsiva/deleteServicioCartaResponsiva', options);
        const respJSON = await res.json();
        if (res.ok) {

            offLoading(null);

            Toast.fire({
                icon: "success",
                title: "Servicio eliminado correctamente"
            });
            obtieneCartasResponsivas();
            obtieneCartasResponsivaDetalle(idCartaResponsivaTemp,$("#idFolioCr").text(), idEstatusCartaResponsivaTemp);

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
            title: "Error al add el servicio"
        });
    }
}

function eliminarServicioCartaResponsiva(idCartaResponsivaDetalle){
    Swal.fire({
        title: "Eliminar Servicios",
        text: "¿Esta seguro que desea elminar el servicio?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function (result) {
        if (result.isConfirmed) {
            eliminarServicioCartaResponsivaAsync(idCartaResponsivaDetalle);
        }
    });
}

function modalCatUsuarios(){
        $("#idUsuario").val("");
        $("#idEntePublicoResponsable").val("");
        $("#nombreUsuario").val("");
        $('#dlgModalCatUsuario').modal('show');
}

function getTablaCatUsuarios(){
    const tableCu = new DataTable('#tableCatUsuario', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering: false,
        bSort: false,
        ajax: {
            url: contexto+'/api/seguridad/tablaUsuariosDisponiblesPorEnte',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    idEntePublico: $("#idEnteseleccionado").val()
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
                "width": "70px",
                "className": "text-center",
            },
            {
                "targets": 3,
                "width": "40px",
                "className": "text-center",
            }],
        lengthChange: false,
        select: false
    });

}

function seleccionarUsuario(idUsuario, curp, nombreCompleto, idEntePublicoResponsable){
    $('#idUsuario').val(idUsuario);
    $('#idEntePublicoResponsable').val(idEntePublicoResponsable);
    $('#nombreUsuario').val(nombreCompleto);
    $('#dlgModalCatUsuario').modal('hide');
    $('#dlgModalFormUsuarioCr').modal('show');
    $("#nombreUsuario").removeClass( "is-valid" );
    $("#nombreUsuario").removeClass( "is-invalid" );
}

function modalCatUsuarioSinReload(){
        $('#dlgModalCatUsuario').modal('show');
}

const aceptarCartaResponsiva = async (idCartaResponsiva) => {

    const options={
        method: "GET",
        headers: headers
    };

    try {
        onLoading("dlgModalAceptarCr");
        const res = await fetch(contexto+'/api/cartaResponsiva/getCartaResponsiva/'+idCartaResponsiva, options);
        const respJSON = await res.json();

        if (res.ok){

            idCartaResponsivaTemp=idCartaResponsiva;
            $("#idAceptarCr").prop('checked', false);

            $("#dlgModalAceptarCr").modal("show");

            $("#folioCrTxt").text(respJSON.folioCr);
            $("#nombreCrTxt").text(respJSON.usuario.nombreCompleto);
            $("#nombreCrTxt2").text(respJSON.usuario.nombreCompleto);
            $("#aceptadaCrTxt").text(respJSON.aceptada);
            $("#estatusCrTxt").text(respJSON.estatusCartaResponsiva.nombreEstatusCartaResponsiva);

            if (respJSON.aceptada=="SI") {
                $("#divAceptarCr1").hide();
                $("#divAceptarCr2").hide();
            }
            else{
                $("#divAceptarCr1").show();
                $("#divAceptarCr2").show();
            }
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
        offLoading("dlgModalFormSistema");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }

}

function aceptarCartaResponsivaJv(){

    if ($("#idAceptarCr").prop('checked')===true){

    Swal.fire({
        title: "Aceptar Carta Responsiva",
        text: "¿Esta seguro que desea aceptar la carta responsiva?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function (result) {
        if (result.isConfirmed) {
            aceptarCartaResponsivaAsync(idCartaResponsivaTemp);
        }
    });
    }else{
        Toast.fire({
            icon: "warning",
            title: "Tiene que seleccionar el checkbox para aceptar la carta responsiva"
        });
    }
}

const aceptarCartaResponsivaAsync = async (idCartaResponsiva) =>{
    const options={
        method: "PUT",
        body: JSON.stringify({
            idCartaResponsiva:idCartaResponsiva
        }),
        headers: headers
    };

    try {
        onLoading("dlgModalAceptarCr");
        const res = await fetch(contexto+'/api/cartaResponsiva/aceptarCartaResponsiva', options);
        const respJSON = await res.json();
        if (res.ok) {

            offLoading("dlgModalAceptarCr");

            Toast.fire({
                icon: "success",
                title: "Carta responsiva aceptada correctamente"
            });

            $("#dlgModalAceptarCr").modal("hide");

            obtieneCartasResponsivas();

        }else{
            offLoading("dlgModalAceptarCr");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }

    }catch (e) {
        offLoading("dlgModalAceptarCr");
        Toast.fire({
            icon: "error",
            title: "Error al aceptar carta responsiva"
        });
    }
}

const actualizarCartaResponsivaAsync = async (idCartaResponsiva, idEstatusCartaResponsiva, msg) =>{
    const options={
        method: "PUT",
        body: JSON.stringify({
            idCartaResponsiva:idCartaResponsiva,
            estatusCartaResponsiva:{idEstatusCartaResponsiva:idEstatusCartaResponsiva},
            nota:$("#idNota").val()
        }),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/cartaResponsiva/actualizarCartaResponsiva', options);
        const respJSON = await res.json();
        if (res.ok) {

            offLoading(null);

            Toast.fire({
                icon: "success",
                title: msg
            });

            obtieneCartasResponsivas();
            $("#idTitleCr").text("");
            $("#idTextBtEstatus").text("");
            idCartaResponsivaTemp=null;
            idEstatusTemp=null;
            $("#dlgModalSeguimientoCartaResponsiva").modal("hide");
            $("#dlgModalCartaResponsivaEnviar").modal("hide");

        }else{
            offLoading(null);
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }

    }catch (e) {
        offLoading("dlgModalAceptarCr");
        Toast.fire({
            icon: "error",
            title: "Error al actualizar la carta responsiva"
        });
    }
}




const enviarCartaResponsiva = async (idCartaResponsiva) =>{

    $("#idNombreReporte").text("");
    $("#idTitleReporte").text("");
    $("#idFechaCrReporte").text("");
    $("#fechaReporte").text("");
    $("#folioReporte").text("");
    $("#idCuerpoReporte").text("");


    idCartaResponsivaTemp=idCartaResponsiva;

    const options={
        method: "GET",
        headers: headers
    };

    try {

        const res = await fetch(contexto+'/api/cartaResponsiva/asignarPlantillaCartaResponsivaId/'+idCartaResponsiva, options);
        const respJSON = await res.json();

        if (res.ok){

            $("#idNombreReporte").html("<h3><div class=\"text-center fw-bold\">"+respJSON.catPlantillaReporte.nombreReporte+"</div></h3>");
            $("#idTitleReporte").html("<h3><div class=\"text-center fw-bold\">"+respJSON.catPlantillaReporte.tituloReporte+"</div></h3>");
            $("#fechaReporte").html("<div class=\"text-end\">Villahermosa, Tabasco a "+respJSON.fechaActualText+".</div>");
            $("#folioReporte").html("<div class=\"text-start\"><span class=\"fw-bold\">Folio:</span>"+respJSON.folioCr+"</div>");
            $("#idCuerpoReporte").html(respJSON.plantillaReporteText);

            $("#dlgModalCartaResponsivaEnviar").modal("show");
        }else{
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }

    }catch (e) {
        var error =  JSON.parse(e.responseText);
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }

    //obtenerPlantillaPorCartaResponsiva(idCartaResponsiva);
}

function enviarCartaResponsivaParaAceptar(){
    Swal.fire({
        title: "Enviar Carta Responsiva",
        text: "¿Esta seguro que desea enviar la carta responsiva?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function (result) {
        if (result.isConfirmed) {
            actualizarCartaResponsivaAsync(idCartaResponsivaTemp,8,"Carta Responsiva enviada para aceptación correctamente");
        }
    });
}

function confirmar(){

    var mensaje_title=""
    var mensaje_text=""
    var mensaje_param=""
    var mensaje_nota=""


    if (idEstatusTemp===3){
        mensaje_title="Observar Carta Responsiva";
        mensaje_text="¿Esta seguro que desea observar la carta responsiva?";
        mensaje_param="Carta Responsiva observada correctamente";
        mensaje_nota="Teclee la observación";
    }

    if (idEstatusTemp===4){
        mensaje_title="En validación Carta Responsiva";
        mensaje_text="¿Esta seguro que desea en validación la carta responsiva?";
        mensaje_param="Carta Responsiva cambio su estatus a en validacion";
        mensaje_nota="Teclee la nota de en validación";
    }

    if (idEstatusTemp===5){
        mensaje_title="Validar Carta Responsiva";
        mensaje_text="¿Esta seguro que desea validar la carta responsiva?";
        mensaje_param="Carta Responsiva cambio su estatus a validado";
        mensaje_nota="Teclee la nota para validar";
    }

    if (idEstatusTemp===6){
        mensaje_title="Autorizar Carta Responsiva";
        mensaje_text="¿Esta seguro que desea autorizar la carta responsiva?";
        mensaje_param="Carta Responsiva cambio su estatus a autorizado";
        mensaje_nota="Teclee la nota para autorizar";
    }

    if (idEstatusTemp===7){
        mensaje_title="Finalizar Carta Responsiva";
        mensaje_text="¿Esta seguro que desea finalizar la carta responsiva?";
        mensaje_param="Carta Responsiva cambio su estatus a finalizado";
        mensaje_nota="Teclee la nota para finalizar";
    }

    if ($("#idNota").val().trim()==""){
        Toast.fire({
            icon: "warning",
            title: mensaje_nota
        });
    }else {

        Swal.fire({
            title: mensaje_title,
            text: mensaje_text,
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Si',
            cancelButtonText: "No"
        }).then(function (result) {
            if (result.isConfirmed) {
                actualizarCartaResponsivaAsync(idCartaResponsivaTemp, idEstatusTemp, mensaje_param);
            }
        });
    }

}

function rechazarCartaResponsiva(idCartaResponsiva){
    $("#idTitleCr").text("Observar Carta Responsiva");
    $("#idTextObservacion").text("Observación");
    $("#idTextBtEstatus").text("Enviar");
    idCartaResponsivaTemp=idCartaResponsiva;
    idEstatusTemp=3;
    $("#idNota").val("");
    $("#dlgModalSeguimientoCartaResponsiva").modal("show");
}

function enValidacionCartaResponsiva(idCartaResponsiva) {
    $("#idTitleCr").text("En Validación Carta Responsiva");
    $("#idTextObservacion").text("Nota");
    $("#idTextBtEstatus").text("Enviar");
    idCartaResponsivaTemp=idCartaResponsiva;
    idEstatusTemp=4;
    $("#idNota").val("");
    $("#dlgModalSeguimientoCartaResponsiva").modal("show");
}

function validarCartaResponsiva(idCartaResponsiva){
    $("#idTitleCr").text("Validar Carta Responsiva");
    $("#idTextObservacion").text("Nota");
    $("#idTextBtEstatus").text("Enviar");
    idCartaResponsivaTemp=idCartaResponsiva;
    idEstatusTemp=5;
    $("#idNota").val("");
    $("#dlgModalSeguimientoCartaResponsiva").modal("show");
}

function autorizarCartaResponsiva(idCartaResponsiva){
    $("#idTitleCr").text("Autorizar Carta Responsiva");
    $("#idTextObservacion").text("Nota");
    $("#idTextBtEstatus").text("Enviar");
    idCartaResponsivaTemp=idCartaResponsiva;
    idEstatusTemp=6;
    $("#idNota").val("");
    $("#dlgModalSeguimientoCartaResponsiva").modal("show");
}
function finalizarCartaResponsiva(idCartaResponsiva){
    $("#idTitleCr").text("Finalizar Carta Responsiva");
    $("#idTextObservacion").text("Nota");
    $("#idTextBtEstatus").text("Enviar");
    idCartaResponsivaTemp=idCartaResponsiva;
    idEstatusTemp=7;
    $("#idNota").val("");
    $("#dlgModalSeguimientoCartaResponsiva").modal("show");
}

function verPassword(pwd, fecha){
    $("#idPwdService").val(pwd);
    $("#idFechaPwd").val(fecha);
    $("#dlgModalPassword").modal("show");
}

function validaUpload() {
    var mensaje = "";

    if($("#ruta_carta_responsiva").val()==="")
        mensaje += "Seleccione el Archivo";

    return mensaje;
}

const adjuntarAsync = async (datos) =>{
    const options={
        method: "POST",
        body: JSON.stringify({
            id:$("#idCartaResponsivaUpload").val(),
            contenido:datos.contenido,
            nombre:datos.nombre
        }),
        headers: headers
    };

    try {
        onLoading("dlgModalUploadCartaResponsiva");
        const res = await fetch(contexto+'/api/cartaResponsiva/uploadCartaResponsivaJpa', options);
        const respJSON = await res.json();

        if (res.ok) {

            $("#ruta_carta_responsiva").val("");
            $(".custom-file-label").text("Seleccionar");

            Toast.fire({
                icon: "success",
                title: respJSON.message
            });
            offLoading("dlgModalUploadCartaResponsiva");
            obtieneCartasResponsivas();
            $('#dlgModalUploadCartaResponsiva').modal('hide');
        } else{
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalUploadCartaResponsiva");
        }


    }catch (e) {
        var error =  JSON.parse(e.responseText);
        offLoading("dlgModalUploadCartaResponsiva");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
}

function adjuntar(){

    var m = validaUpload();

    if(m.length >0 ){

        Toast.fire({
            icon: "error",
            title: m
        });
    }
    else{
        var archivo = readFile();

        archivo.then(function (datos) {
            adjuntarAsync(datos);

        });

    }

}

function readFile() {
    return new Promise(function (resolve) {
        var envio = {};
        var dato_archivo = $('#ruta_carta_responsiva').prop("files")[0];
        if(dato_archivo != undefined || dato_archivo.length > 0){
            var reader = new FileReader();
            reader.readAsBinaryString(dato_archivo);
            reader.onload = function () {
                envio = {
                    nombre: dato_archivo.name,
                    contenido: btoa(reader.result)
                }
                resolve(envio)
            };
        }
    });
}

function openModalUploadCartaResponsiva(idCartaResponsiva){
    $("#idCartaResponsivaUpload").val(idCartaResponsiva);
    $("#ruta_carta_responsiva").val("");
    $('#dlgModalUploadCartaResponsiva').modal('show');
}

function descargar(rutaDocumento,nombreDocumentoOriginal){
    $('#preloader').modal('show');
    var oReq = new XMLHttpRequest();
    oReq.open("POST", contexto+'/api/cartaResponsiva/descarga-archivo', true);
    oReq.responseType = "arraybuffer";
    oReq.setRequestHeader("x-csrf-token", $('input[name="_csrf"]').val());
    oReq.setRequestHeader('Content-Type', 'application/json')
    oReq.onload = function(oEvent) {
        setTimeout(function () {
            $('#preloader').modal('hide');
            if(oReq.status === 500){
                var json = String.fromCharCode.apply(null, new Uint8Array(oReq.response));
                var response = JSON.parse(json);
                swal("Error", response.message,"error");
            }
            else {
                var arrayBuffer = oReq.response;
                var blob = new Blob([arrayBuffer], {type: oReq.tipoMime});
                var a = document.createElement("a");
                a.style = "display:none";
                var url = URL.createObjectURL(blob);
                a.href =  url;
                a.download = oReq.getResponseHeader("nombre");
                a.click();
                window.URL.revokeObjectURL(url);

            }
        }, 1000);
    };
    oReq.send(JSON.stringify({
        rutaDocumento:rutaDocumento,
        nombreDocumentoOriginal:nombreDocumentoOriginal
    }));
}

function usuarioAutoriza(idCartaResponsiva, idEntePublicoResponsable){
    idCartaResponsivaTemp=idCartaResponsiva;
    getTablaUsuariosAutoriza(idEntePublicoResponsable);
    $('#dlgModalAsignaUsuario').modal('show');
}

function getTablaUsuariosAutoriza(idEntePublicoResponsable){
    const tableAutoriza = new DataTable('#tableUsuarioAutoriza', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering: false,
        bSort: false,
        ajax: {
            url: contexto+'/api/seguridad/tablaUsuariosAutoriza',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    idEntePublico: idEntePublicoResponsable
                }));
            },
            complete: function (data) {
                var ente=data['responseJSON'].entePublico;
                 $("#nameDependenciaResponsable").text(ente.cveEntePublico+" - "+ente.nombreEntePublico);
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
                "width": "70px",
                "className": "text-center",
            },
            {
                "targets": 3,
                "width": "40px",
                "className": "text-center",
            }],
        lengthChange: false,
        select: false
    });

}


const seleccionarUsuarioAutoriza = async (idUsuarioAutoriza) =>{
    const options={
        method: "PUT",
        body: JSON.stringify({
            idCartaResponsiva:idCartaResponsivaTemp,
            usuarioAutoriza:{idUsuario:idUsuarioAutoriza}
        }),
        headers: headers
    };

    try {
        onLoading("dlgModalAsignaUsuario");
        const res = await fetch(contexto+'/api/cartaResponsiva/usuarioAutorizaCartaResponsiva', options);
        const respJSON = await res.json();
        if (res.ok) {

            offLoading("dlgModalAsignaUsuario");

            Toast.fire({
                icon: "success",
                title: "Usuario que autoriza asignado correctamente"
            });

            obtieneCartasResponsivas();

            idCartaResponsivaTemp=null;
            $("#dlgModalAsignaUsuario").modal("hide");

        }else{
            offLoading("dlgModalAsignaUsuario");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }

    }catch (e) {
        offLoading("dlgModalAsignaUsuario");
        Toast.fire({
            icon: "error",
            title: "Error al asignar usuario que autoriza la carta responsiva"
        });
    }
}

function modalSeguimientoCr(idCartaResponsiva, folio){

    obtieneCartasResponsivasSeguimiento(idCartaResponsiva);
    $("#idFolioCrSeg").text(folio);
    $("#dlgModalCartaResponsivaSeguimiento").modal("show");

}

function obtieneCartasResponsivasSeguimiento(idCartaResponsiva) {
    var formulario={
        idCartaResponsiva:idCartaResponsiva
    };

    if (tableCartaResponsivaSeguimiento!=null)
        tableCartaResponsivaSeguimiento.clear();

    tableCartaResponsivaSeguimiento = new DataTable('#tableCartaResponsivaSeguimiento', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: false,
        autoWidth: false,
        columnDefs: [
            {
                targets: 1,
                "width": "120px",
                "className": "text-center"
            },
            {
                targets: 2,
                "width": "100px",
                "className": "text-center"
            }],
        ajax: {
            url: contexto+'/api/cartaResponsiva/getSeguimientoCartaResponsiva',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend(formulario, d, {}));
            }
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


const getCartaResponsivaId = async (idCartaResponsiva) => {

    const options={
        method: "GET",
        headers: headers
    };

    try {

        const res = await fetch(contexto+'/api/cartaResponsiva/getCartaResponsiva/'+idCartaResponsiva, options);
        const respJSON = await res.json();

        if (res.ok){
            idSubTipoServicioTicTemp=respJSON.catSubtipoServiciosTic.idSubtipoServicioTic;
            idTipoServicioTicTemp=respJSON.catSubtipoServiciosTic.catTipoServiciosTic.idTipoServicioTic;
            $("#idTipoServicioMd").val(respJSON.catSubtipoServiciosTic.catTipoServiciosTic.nombreTipoServicioTic);
            $("#idSubTipoServicioMd").val(respJSON.catSubtipoServiciosTic.nombreSubtipoServicioTic);

        }else{
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }

    }catch (e) {
        var error =  JSON.parse(e.responseText);
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }

}

function generarToken(idCartaResponsiva){

    let msg="¿Estás seguro que deseas generar el token?";
    let msgRespuesta="El token se genero correctamente";


    Swal.fire({
        title: 'Carta Responsiva',
        text: msg,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function (result) {
        if (result.isConfirmed) {

            $.ajax({
                url: contexto + '/api/cartaResponsiva/generarToken',
                type: "POST",
                dataType: 'json',
                data: JSON.stringify({
                    idCartaResponsiva: idCartaResponsiva
                }),
                contentType: 'application/json',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                },
                success: function (response) {

                    obtieneCartasResponsivas();
                    
                    Toast.fire({
                        icon: 'success',
                        title: msgRespuesta
                    });

                },
                error: function (response) {
                    var error =  JSON.parse(response.responseText);
                    Toast.fire({
                        icon: 'error',
                        title: error.message
                    });
                }
            });
        }

    });
}

function getAbsolutePath() {
    var loc = window.location;
    var pathName = loc.pathname.substring(0, loc.pathname.lastIndexOf('/') + 1);
    return loc.href.substring(0, loc.href.length - ((loc.pathname + loc.search + loc.hash).length - pathName.length));
}

const enviarMail = async (url, correo, nombre, dependencia) =>{

    const options={
        method: "POST",
        dataType: 'json',
        body: JSON.stringify({
            "clientId" : $("#usuarioToken").val(),
            "clientSecret" : $("#secretToken").val()
        }),
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    let urlSend=getAbsolutePath()+"carta-responsiva-aceptar?token="+url;
    try {
        onLoading(null);
        const res = await fetch($("#generaToken").val(), options);
        const respJSON = await res.json();
        if (res.ok) {
            offLoading(null);
            enviarUrlEmail(respJSON.access_token, urlSend, correo, nombre);
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
            title: "Error al generar Token"
        });
    }
};

const enviarUrlEmail = async (token, url, correo, nombre) =>{

    let textoEnviar= nombre+"<br/>"+
        correo+"<br/>"+
        "PRESENTE<br/><br/>"+
        "Se envía enlace para aceptar la carta responsiva ";

    const options={
        method: "POST",
        dataType: 'json',
        body: JSON.stringify(
            { "correo": {"cuentaCorreo":correo.trim()},
                "tipo": "NOTIFICACION", "asunto": "Aceptar la carta responsiva de los bienes a su cargo (SIGTIC)",
                "contenido": textoEnviar+"<br/><br/><a href='"+url+"'>"+url+"</a>",
                "idSistema": "NOTIFICACION"
            }),
        headers: {Authorization: 'Bearer '+ token,
            "Content-type": "application/json; charset=UTF-8"
        }
    };


    try {
        onLoading(null);
        const res = await fetch($("#enviaCorreo").val(), options);

        const respJSON = await res.json();
        if (res.ok) {
            offLoading(null);

            Toast.fire({
                icon: "success",
                title: "URL enviada por correo institucional"
            });

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
            title: "Error al enviar la URL para actualización"
        });
    }
};

function configurarNumFirma(){

    var msg="";

    if ($("#idEntePublicoSearch").val()=="")
        msg="<br/>* Ente Público";

    if ($("#idTipoServicioTic").val()=="")
        msg=msg+"<br/>* Tipo de Servicio";

    if ($("#idSubtipoServicioTic").val()=="")
        msg=msg+"<br/>* SubTipo de Servicio";

    if (msg!=""){
        Toast.fire({
            icon: "warning",
            title: "Se requieren los siguientes campos <br/>"+msg
        });
    }else {

        editarSubtipoServiciosFirma($("#idEntePublicoSearch").val(), $("#idSubtipoServicioTic").val());

    }
}

function guardarNumeroFirma(){

   /* if ($("#idEntePublico").val()==""){
        Toast.fire({
            icon: "warning",
            title: "Seleccione el ente público"
        });
    }else*/
    if ($("#numeroFirma").val()==""){
        Toast.fire({
            icon: "warning",
            title: "El número de firmas debe ser 1 o 2"
        });
    }else{



        let titleSave='Guardar el número de firma';
        let msgSave='¿Estas seguro que deseas guardar el número de firmas para este ente público?';
        if ($('#idSubtipoServiciosFirma').val() != ""){
            titleSave='Actualizar el número de firma';
            msgSave='¿Estas seguro que deseas actualizar el número de firmas?';
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
                guardarNumeroFirmaFirmaAsync();
            }
        });
    }
}

const guardarNumeroFirmaFirmaAsync = async () =>
{
    const options = {
        method: "POST",
        body: JSON.stringify({
            idSubtipoServicioFirma: $("#idSubtipoServiciosFirma").val(),
            entePublico: {idEntePublico:$("#idEntePublicoSearch").val()},
            catSubtipoServiciosTic: {idSubtipoServicioTic:$("#idSubtipoServicioTic").val()},
            numeroFirmas: $("#numeroFirma").val()
        }),
        headers: headers
    };
    try {
        onLoading("dlgModalFormSubtipoServiciosFirma");
        const res = await
            fetch(contexto + '/api/subtipo_servicio_firma/saveSubtipoServiciosFirma', options);
        const respJSON = await
            res.json();

        $("#idSubtipoServiciosFirma").val(respJSON.idSubtipoServicioFirma);

        if (res.ok) {
            offLoading("dlgModalFormSubtipoServiciosFirma");

            Toast.fire({
                icon: "success",
                title: "Configuración guardada correctamente"
            });

        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalFormSubtipoServiciosFirma");
        }
    } catch (e) {
        var error = JSON.parse(e.responseText);
        offLoading("dlgModalFormSubtipoServiciosFirma");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
}

const editarSubtipoServiciosFirma = async (idEntePublico, idSubtipoServicio) => {
    const options={
        method: "GET",
        hearders: headers
    };

    $("#idSubtipoServiciosFirma").val("");
    $("#numeroFirma").val("");

    try{
        onLoading(null);
        const res = await fetch(contexto + '/api/subtipo_servicio_firma/getSubtipoServiciosFirmaEnte/' + idEntePublico + "/" + idSubtipoServicio, options);
        const respJSON = await res.json();


            if (res.ok) {

                if (respJSON.numeroFirmas != null){
                    $("#idSubtipoServiciosFirma").val(respJSON.idSubtipoServicioFirma);
                    $("#numeroFirma").val(respJSON.numeroFirmas);
                }
                offLoading(null);

                $("#dlgModalFormSubtipoServiciosFirma").modal("show");

            } else {
                Toast.fire({
                    icon: "error",
                    title: respJSON.message
                });
                offLoading(null);
            }
    }catch(e){
            var error = JSON.parse(e.responseText);
            offLoading(null);
            Toast.fire({
                icon: "error",
                title: error.message
            });
    }

}

async function  obtenerPlantillaPorCartaResponsiva(idCartaResponsiva){
     var url = contexto+"/api/plantillaReporte/getPlantillaReportePorEnteSubtipoServicio";
    idCartaResponsivaTemp=idCartaResponsiva;
    const options={
        method: "POST",
        body: JSON.stringify({
            idCartaResponsiva:idCartaResponsiva,
        }),
        headers: {
            "X-CSRF-Token":  $('input[name="_csrf"]').val(),
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    onLoading("dlgModalCartaResponsivaEnviar");

    const res = await fetch(url, options);
    const respJSON = await res.json();
    if (res.ok){
        llenarCombo("#idPlantilla","idPlantillaReporte", "tituloReporte", respJSON);

        if (typeof callback === 'function') {
            callback();
        }
        offLoading("dlgModalCartaResponsivaEnviar");
    }else {
        Toast.fire({
            icon: "error",
            title: respJSON.message
        });
        offLoading("dlgModalCartaResponsivaEnviar");
    }
}

const getDatosReporteId = async (idPlantillaReporte,idCartaResponsiva) => {
    const options={
        method: "GET",
        hearders: headers
    };

    $("#idSubtipoServiciosFirma").val("");
    $("#numeroFirma").val("");

    try{
        onLoading("dlgModalCartaResponsivaEnviar");
        const res = await fetch(contexto + '/api/plantillaReporte/getPlantillaId/' + idPlantillaReporte+"/"+idCartaResponsiva, options);
        const respJSON = await res.json();

        if (res.ok) {
            if (respJSON)

            $("#fechaReporte").val(respJSON.fechaAceptadaText);
            $("#idContenido").val(respJSON.cuerpo);
            $("#firma1").val(respJSON.firma1);
            $("#cargo1").val(respJSON.cargo1);
            $("#descFirma1").val(respJSON.decFirma1);
            $("#firma2").val(respJSON.firma2);
            $("#cargo2").val(respJSON.cargo2);
            $("#descFirma2").val(respJSON.decFirma2);
            $("#firma3").val(respJSON.firma3);
            $("#cargo3").val(respJSON.cargo3);
            $("#descFirma3").val(respJSON.decFirma3);
            $("#idNombreReporte").text(respJSON.nombreReporte);
            $("#idTitleReporte").text(respJSON.tituloReporte);

            let contenitoTemp=$("#idContenido").val();


            var replacedFecha = contenitoTemp.replace("##fechaCr##",respJSON.fechaAceptadaText);
            $("#idCuerpoReporte").html(replacedFecha);

            offLoading("dlgModalCartaResponsivaEnviar");

        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalCartaResponsivaEnviar");
        }
    }catch(e){
        var error = JSON.parse(e.responseText);
        offLoading("dlgModalCartaResponsivaEnviar");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }

}

const plantillasConfiguradas = async () => {

    const options={
        method: "POST",
        body: JSON.stringify({
            entePublico: {idEntePublico: $("#idEntePublicoSearch").val()},
            idSubtipoServicio: $("#idSubtipoServicioTic").val()
        }),
        headers: {
            "X-CSRF-Token":  $('input[name="_csrf"]').val(),
            "Content-type": "application/json; charset=UTF-8"
        }
    };


    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/plantillaReporte/getExistePlantilla', options);
        const respJSON = await res.json();

        if (res.ok) {

            if (respJSON)
                $("#idAlertReport").removeClass("alert-success")
                $("#idAlertReport").removeClass("alert-danger")
                if (respJSON>0){
                    $("#idAlertReport").addClass("alert-success")
                    $("#idMessageReport").text("Hay "+respJSON+" reporte(s) configurado(s)");
                    $("#idAlertReport").show();
                }else{

                    $("#idAlertReport").addClass("alert-danger")
                    $("#idMessageReport").text("No exiten plantillas configuradas");
                    $("#idAlertReport").show();
                }

                $("#fechaReporte").val(respJSON.fechaAceptadaText);
                $("#idContenido").val(respJSON.cuerpo);
                $("#firma1").val(respJSON.firma1);
                $("#cargo1").val(respJSON.cargo1);


            offLoading("dlgModalCartaResponsivaEnviar");

        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalCartaResponsivaEnviar");
        }


        offLoading(null);
    } catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
};

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
            $("#fechaReporteA").html("<div class=\"text-end\">Villahermosa, Tabasco a "+respJSON.fechaAceptadaFija+".</div>");
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

const generarQr = async (url) =>{

    const options={
        method: "POST",
        dataType: 'json',
        body: JSON.stringify({
            "clientId" : $("#usuarioToken").val(),
            "clientSecret" : $("#secretToken").val()
        }),
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    try {
        onLoading(null);
        const res = await fetch($("#generaToken").val(), options);
        const respJSON = await res.json();
        if (res.ok) {
            offLoading(null);
            generarQrAsync(respJSON.access_token, url);
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
            title: "Error al generar Token"
        });
    }
};

const generarQrAsync = async (token, url) =>{

    let parametros=JSON.stringify({
        contenido: url,
        dimension: 200
    });

    const options={
        method: "POST",
        dataType: 'json',
        body: parametros,
        headers: {Authorization: 'Bearer '+ token,
            "Content-type": "application/json; charset=UTF-8"
        }
    };


    try {
        onLoading(null);
        const res = await fetch($("#generaQr").val(), options);
        console.log(res);
        const respJSON = await res.json();
        if (res.ok) {
            offLoading(null);

            console.log(respJSON);
            console.log(respJSON.data.qr);

            let srcQr = 'data:image/png;base64,'+respJSON.data.qr;
            console.log(srcQr);
            $("#imgPrueba").attr("src",srcQr)

            Toast.fire({
                icon: "success",
                title: "Qr Generado"
            });

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
            title: "Error al generar el QR"
        });
    }
};
