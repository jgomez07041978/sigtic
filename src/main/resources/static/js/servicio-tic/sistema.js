
const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let tiposSistema=[];
let requiereSistemaPadre="N";

let tableAdmin=null;
let tableSistemaGit=null;

$(document).ready(function(){

    $('[data-bs-toggle="tooltip"]').tooltip();

    jQuery.event.special.mousewheel = {
        setup: function( _, ns, handle ){
            this.addEventListener("mousewheel", handle, { passive: true });
        }
    };
/*
    $("#nav-data-git").on("shown.bs.tab", function () {
        $("#tableSistemaGit").DataTable()
            .columns.adjust()
            .responsive.recalc();
    });*/

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

    inicializarDependencias();

    obtenerTipoSistema();

    fillSelectGet("/api/catalogo/tic/getEstatusSistema","#estatusSistema", "cveEstatusSistema", "nombreEstatusSistema", null, null);


    $("#idEntePublicoAdministra").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormSistema")
    });

    $("#idEntePublicoOpera").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormSistema")
    });

    $("#idSistemaPadre").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormSistema")
    });

    if ($("#idEntePublicoSession").val()!=""){
        $('#idNuevoSistema').prop('disabled', false);
        obtienSistemas($("#idEntePublicoSession").val());
        obtenerSistemasPadre($("#idEntePublicoSession").val());
    }
    else {
        obtienSistemas(0);
    }

    $('#idEntePublicoSearch').on('change', function() {
        if ($("#idEntePublicoSearch").val()!="")
            $('#idNuevoSistema').prop('disabled', false);
        else
            $('#idNuevoSistema').prop('disabled', true);

        obtienearbolPorInstitucion(function (data) {
            $("#idUnidadAdministrativaSearch").val(data.id);
            $("#descUnidadAdministrativaSearch").val(data.text);
            limpiarBuscarArbol();
            $('#modal_arbol_unidad').modal('hide');
            $("#descUnidadAdministrativaSearch").removeClass("is-valid");
            $("#descUnidadAdministrativaSearch").removeClass("is-invalid");

            obtienSistemas($("#idEntePublicoSearch").val(), data.id);

        }, $("#idEntePublicoSearch").val());

            obtienSistemas($("#idEntePublicoSearch").val());
            obtenerSistemasPadre($("#idEntePublicoSearch").val());
    });

    $("#btnOpenArbolSearch").click(function () {
        $('#modal_arbol_unidad').modal('show');
    });

    $("#txtBuscar").keyup(function () {
        busqueda($(this).val())
    });

    $("#btnLimpiarOpenArbolSearch").click(function () {
        obtienSistemas($("#idEntePublicoSearch").val(), null);
        $("#idUnidadAdministrativaSearch").val("");
        $("#descUnidadAdministrativaSearch").val("");
    });


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



    $('#idNuevoSistema').click(function () {
        limpiarFormulario();
        $("#opcion").val("Nuevo");
        $('#titleFormSistema').text("Nuevo Sistema");
        $("#idEntePublicoAdministra").val($("#idEntePublicoSearch").val())
        $("#idEntePublicoAdministra").trigger('change');
        $("#idEntePublicoOpera").val($("#idEntePublicoSearch").val())
        $("#idEntePublicoOpera").trigger('change');
        $("#descEntePublicoAdministra").val($("#idEntePublicoSearch option:selected").html());
        $("#codigoGit").prop('disabled', true);
        $("#descripcionGit").prop('disabled', true);
        $("#idBtnGit").prop('disabled', true);
        obtienCodigosGit();
        $('#dlgModalFormSistema').modal('show');

    });

    $('#tipoSistema').on('change', function() {
        requiereSistemaPadre = requierePadre(tiposSistema, $('#tipoSistema').val());
        actiualizarDefinicioTipoSistema(tiposSistema, $('#tipoSistema').val());
        if (requiereSistemaPadre=='S'){
            $("#idSistemaPadre").val("");
            $("#idDivSistemaPadre").show();
        }else{
            $("#idSistemaPadre").val("");
            $("#idDivSistemaPadre").hide();
        }

    });

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

    $.validator.addMethod("formValSistemaPadre", function (value, element) {
        if (requiereSistemaPadre=="S" &&  $('#idSistemaPadre').val()=="") {
            $.validator.messages["formValSistemaPadre"] = "Campo requerido";
            return false;
        }
        else
            return true;

    }, "");


    $( "#idFormSistema" ).validate( {
        ignore: "",
        rules: {
            tipoSistema:"required",
            codigoSistema:"required",
            idSistemaPadre:{formValSistemaPadre:true},
            descSistemaAbrev: {
                required:true,
                maxlength: 100
            },
            descSistema: {
                required:true,
                maxlength: 100
            },
            //cveMejoraReg:"required",
            alcanceSistema: {
                required:true,
                maxlength: 4000
            },
            descEntePublicoAdministra:"required",
            descUnidadAdminAdministra:"required",
            estatusSistema:"required",
            orden:"required",
        },
        messages: {
            tipoSistema: "Campo requerido",
            codigoSistema: "Campo requerido",
            idSistemaPadre: {},
            descSistemaAbrev: "Campo requerido",
            descSistema: "Campo requerido",
            //cveMejoraReg: "Campo requerido",
            alcanceSistema: "Campo requerido",
            descEntePublicoAdministra: "Campo requerido",
            descUnidadAdminAdministra: "Campo requerido",
            estatusSistema: "Campo requerido",
            orden: "Campo requerido",
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
    } );


    $("#idFormSistema").submit(function(e) {
        e.preventDefault();
        if($('#idFormSistema').valid()) {
            guardar();
            return true;
        }
        else {
            return false;
        }
    });

    $('#administra').change(function() {
        obtienSistemas($("#idEntePublicoSearch").val(), $("#idUnidadAdministrativaSearch").val());
    });

    $('#opera').change(function() {
        obtienSistemas($("#idEntePublicoSearch").val(), $("#idUnidadAdministrativaSearch").val());
    });

});


function limpiarFormulario() {
    $("#tipoSistema").removeClass( "is-valid" );
    $("#tipoSistema").removeClass( "is-invalid" );
    $("#codigoSistema").removeClass( "is-valid" );
    $("#codigoSistema").removeClass( "is-invalid" );
    $("#idSistemaPadre").removeClass( "is-valid" );
    $("#idSistemaPadre").removeClass( "is-invalid" );
    $("#descSistemaAbrev").removeClass( "is-valid" );
    $("#descSistemaAbrev").removeClass( "is-invalid" );
    $("#descSistema").removeClass( "is-valid" );
    $("#descSistema").removeClass( "is-invalid" );
    $("#alcanceSistema").removeClass( "is-valid" );
    $("#alcanceSistema").removeClass( "is-invalid" );
    $("#numInventarioGet").removeClass( "is-valid" );
    $("#numInventarioGet").removeClass( "is-invalid" );
    $("#numInventarioEnte").removeClass( "is-valid" );
    $("#numInventarioEnte").removeClass( "is-invalid" );

    $("#idEntePublicoAdministra").removeClass( "is-valid" );
    $("#idEntePublicoAdministra").removeClass( "is-invalid" );
    $("#descUnidadAdminAdministra").removeClass( "is-valid" );
    $("#descUnidadAdminAdministra").removeClass( "is-invalid" );

    $("#idEntePublicoOpera").removeClass( "is-valid" );
    $("#idEntePublicoOpera").removeClass( "is-invalid" );
    $("#descUnidadAdminOpera").removeClass( "is-valid" );
    $("#descUnidadAdminOpera").removeClass( "is-invalid" );

    $("#estatusSistema").removeClass( "is-valid" );
    $("#estatusSistema").removeClass( "is-invalid" );
    $("#orden").removeClass( "is-valid" );
    $("#orden").removeClass( "is-invalid" );

    $('#idSistema').val("");

    $("#idEntePublicoAdministra").val("");
    $("#idEntePublicoAdministra").trigger('change');
    $("#idEntePublicoOpera").val("");
    $("#idEntePublicoOpera").trigger('change');
    $('#idUnidadAdminOpera').val("");
    $('#tipoSistema').val("");
    $('#definicionTipoSistema').val("");
    $('#cveMejoraReg').val("");
    $('#codigoSistema').val("");
    $('#idSistemaPadre').val("");
    $('#descSistemaAbrev').val("");
    $('#descSistema').val("");
    $('#alcanceSistema').val("");
    $('#numInventarioGet').val("");
    $('#numInventarioEnte').val("");
    $('#descUnidadAdminAdministra').val("");
    $('#descUnidadAdminOpera').val("");
    $('#estatusSistema').val("");
    $('#orden').val("");
    $('#cveEstatus').val("VIG");
    $("#idDivSistemaPadre").hide();
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
        llenarCombo('#idEntePublicoAdministra', 'idEntePublico','nombreEntePublicoConCve', respJSON);
        llenarCombo('#idEntePublicoOpera', 'idEntePublico','nombreEntePublicoConCve', respJSON);
        $("#idEntePublicoSearch").val($("#idEntePublicoSession").val());
        $("#idEntePublicoSearch").trigger('change');
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


const obtenerTipoSistema = async () =>{

    const options={
        method: "GET",
        headers: headers
    };

    onLoading(null);
    const res = await fetch(contexto+"/api/catalogo/tic/getSistema", options);
    const respJSON = await res.json();

        if (res.ok){
            tiposSistema=respJSON;
            llenarCombo('#tipoSistema','cveTipoSistema','nombreTipoSistema', respJSON);
        }else{

            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalForRol");
        }

};

const obtenerSistemasPadre = async (idEntePublico) =>{

    $("#idSistemaPadre").empty();
    if (idEntePublico!=""){
        const options={
            method: "GET",
            headers: headers
        };

        onLoading(null);
        const res = await fetch(contexto+"/api/catalogo/tic/getListaSistemaPadre/"+idEntePublico, options);
        const respJSON = await res.json();

        if (res.ok){
            llenarCombo('#idSistemaPadre','idSistema','codigoNombreSistema', respJSON);
            offLoading(null);
        }else{

            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading(null);
        }
    }

};


function obtienSistemas(idEntePublico, idUnidadAdministrativa){

    //var groupColumn1 = 3;
    //var groupColumn2 = 4;

    if (tableAdmin!=null)
        tableAdmin.clear();

   tableAdmin = new DataTable('#tableSistema', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: false, 
       initComplete: function(settings, json) {
           $('[data-bs-toggle="tooltip"]').tooltip();
       },
        ajax: {
            url: contexto+'/api/catalogo/tic/tablaSistemas',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
             beforeSend: function(xhr) {
               xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    idUnidadAdministrativa:idUnidadAdministrativa,
                    idEntePublico:idEntePublico,
                    "isAdministra":$("#administra").is(':checked'),
                    "isOpera":$("#opera").is(':checked')
                }));
            },
            complete: function (data) {
               /* var total=data['responseJSON'].iTotalDisplayRecords;
                $("#idTotalAdmin").text(total);*/
            }
        },
        bProcessing: true,
        bServerSide:false,
       language:{
           "url": contexto+'/include/datatables/spanish.json',
       },
       columnDefs: [
           {
               targets: 6,
               "width": "90px",
               "className": "text-center"
           }],
        lengthChange: false,
        select: false
    });

}

const requierePadre = (respJSON, value)=>{
    let reqPadre ="N";
    for (var d = 0; d < respJSON.length; d++) {
        if (respJSON[d]["cveTipoSistema"]==value) {
            reqPadre = respJSON[d]["requiereSistemaPadre"];
            break;
        }
    }

    return reqPadre;
};
const actiualizarDefinicioTipoSistema = (respJSON, value)=>{
    $("#definicionTipoSistema").val();
    for (var d = 0; d < respJSON.length; d++) {
        if (respJSON[d]["cveTipoSistema"]==value) {
            $("#definicionTipoSistema").val(respJSON[d]["alcanceSistema"]);
            break;
        }
    }
};


const guardarSistemaAsync = async () =>{
    const options={
        method: "POST",
        body: JSON.stringify({
             idSistema:$("#idSistema").val()
            ,catSistemaPadre:{ idSistema: $("#idSistemaPadre").val()}
            ,cveMejoraRegulatoria:$("#cveMejoraReg").val().trim()
            ,codigoSistema:$("#codigoSistema").val().trim()
            ,catTipoSistema:{ cveTipoSistema: $("#tipoSistema").val()}
            ,nombreSistemaAbreviado:$("#descSistemaAbrev").val()
            ,nombreSistema:$("#descSistema").val()
            ,alcanceSistema:$("#alcanceSistema").val()
            ,numeroInventarioGet:$("#numInventarioGet").val()
            ,numeroInventarioEntePublico:$("#numInventarioEnte").val()
            ,entePublicoAdmin:{idEntePublico:$("#idEntePublicoAdministra").val()}
            ,unidadAdministrativaAdmin:{idUnidadAdministrativa:$("#idUnidadAdminAdministra").val()}
            ,entePublicoOpera:{idEntePublico:$("#idEntePublicoOpera").val()}
            ,unidadAdministrativaOpera:{idUnidadAdministrativa:$("#idUnidadAdminOpera").val()}
            ,catEstatusSistema:{cveEstatusSistema:$("#estatusSistema").val()}
            ,numeroOrden:$("#orden").val()
            ,cveEstatus:$("#cveEstatus").val()
        }),
        headers: headers
    };

    try {
        onLoading("dlgModalFormSistema");
        const res = await fetch(contexto+'/api/catalogo/tic/saveSistema', options);
        const respJSON = await res.json();

        if (res.ok) {
            //$("#opcion").val("Editar");
            //$('#idSistema').val(respJSON.idSistema);
            //$('#titleFormSistema').text("Editar Sistema");

            Toast.fire({
                icon: "success",
                title: "Los datos del sistema se guardaron correctamente"
            });
            obtienSistemas($("#idEntePublicoSearch").val(), $("#idUnidadAdministrativaSearch").val());
            obtenerSistemasPadre($("#idEntePublicoSearch").val());
            offLoading("dlgModalFormSistema");
            $('#dlgModalFormSistema').modal('hide');
        } else{
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalFormSistema");
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

function guardar(){

    let titleSave='¿Guardar Sistema?';
    let msgSave='¿Estas seguro que deseas guardar los datos del sistema?';
    if ($('#opcion').val() == "Editar"){
        titleSave='¿Actualizar Sistema?';
        msgSave='¿Estas seguro que deseas actualizar los datos del sistema?';
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
            guardarSistemaAsync();
        }
    });
}

const getSistema = async (idSistema) => {

    const options={
        method: "GET",
        headers: headers
    };

    try {
        onLoading("dlgModalFormSistema");
        const res = await fetch(contexto+'/api/catalogo/tic/getSistema/'+idSistema, options);
        const respJSON = await res.json();

        if (res.ok){

            $("#idSistema").val(respJSON.idSistema);
            $("#tipoSistema").val(respJSON.catTipoSistema.cveTipoSistema);

            $("#codigoSistema").val(respJSON.codigoSistema);
            $("#cveMejoraReg").val(respJSON.cveMejoraRegulatoria);

            if (respJSON.catSistemaPadre!=null) {
                $("#idSistemaPadre").val(respJSON.catSistemaPadre.idSistema);
                $("#idSistemaPadre").trigger('change');
            }

            requiereSistemaPadre = requierePadre(tiposSistema, $('#tipoSistema').val());
            actiualizarDefinicioTipoSistema(tiposSistema, $('#tipoSistema').val());
            if (requiereSistemaPadre=='S')
                $("#idDivSistemaPadre").show();
            else{
                $("#idSistemaPadre").val("");
                $("#idDivSistemaPadre").hide();
            }

            $("#descSistemaAbrev").val(respJSON.nombreSistemaAbreviado);
            $("#descSistema").val(respJSON.nombreSistema);
            $("#alcanceSistema").val(respJSON.alcanceSistema);
            $("#numInventarioGet").val(respJSON.numeroInventarioGet);
            $("#numInventarioEnte").val(respJSON.numeroInventarioEntePublico);

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

            if (respJSON.catEstatusSistema!=null)
                $("#estatusSistema").val(respJSON.catEstatusSistema.cveEstatusSistema);

            $("#cveEstatus").val(respJSON.cveEstatus);


            if (respJSON.cveEstatus==="VIG"){
                $("#codigoGit").prop('disabled', false);
                $("#descripcionGit").prop('disabled', false);
                $("#idBtnGit").prop('disabled', false);
            }else{
                $("#codigoGit").prop('disabled', true);
                $("#descripcionGit").prop('disabled', true);
                $("#idBtnGit").prop('disabled', true);
            }


            $("#orden").val(respJSON.numeroOrden);


            offLoading("dlgModalFormSistema");
        }else{
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalFormSistema");
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

function editarSistema(idSistema){
    $("#opcion").val("Editar");
    limpiarFormulario();
    getSistema(idSistema);
    $("#tableSistemaGit").show();
    obtienCodigosGit(idSistema);
    $('#titleFormUsuario').text("Editar Sistema");

    $('#dlgModalFormSistema').modal('show')
}

const eliminarSistemaAsync = async (idSistema) =>{
    const options={
        method: "DELETE",
        body: JSON.stringify({idSistema: idSistema}),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/catalogo/tic/deleteSistema', options);
        const respJSON = await res.json();

        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Sistema eliminado correctamente"
            });

            obtienSistemas($("#idEntePublicoSearch").val());
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


function eliminarSistema(idSistema){

    Swal.fire({
        title: 'Eliminar Sistema',
        text: '¿Estas seguro que deseas eliminar el sistema?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminarSistemaAsync(idSistema);
        }
    })

}

function verSistemasHijos(idSistema, sistemaPadre){
    $("#titleSistemaHijo").text(sistemaPadre);
    obtieneSistemasHijo(idSistema);
    $("#dlgModalSistemasHijo").modal("show");
}

function obtieneSistemasHijo(idSistema){

    const tableHijo = new DataTable('#tableSistemaHijo', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/catalogo/tic/tablaSistemaHijo/'+idSistema,
            type: 'GET',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
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


function obtienCodigosGit(idSistema){


    if (tableSistemaGit!=null)
        tableSistemaGit.clear();

    let ruta='/api/v1/sistema-git/tablaSistemasGit/';


    if (idSistema!==null && idSistema!==undefined)
        ruta+=idSistema;

    tableSistemaGit = new DataTable('#tableSistemaGit', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: false,
        initComplete: function(settings, json) {
            $('[data-bs-toggle="tooltip"]').tooltip();
            $("#tableSistemaGit").DataTable()
                .columns.adjust()
                .responsive.recalc();
        },
        ajax: {
            url: contexto+ruta,
            type: 'GET',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            complete: function (data) {

            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [
            {
                targets: 2,
                "width": "90px",
                "className": "text-center"
            }],
        lengthChange: false,
        select: false
    });


}

function agregarGit(){
    if ($("#codigoGit").val().trim()===""){
        Toast.fire({
            icon: "warning",
            title: "Se requiere el código GIT"
        });
    }else{
        guardarGit();
    }

}

function guardarGit(){

    let titleSave='¿Guardar GIT?';
    let msgSave='¿Estas seguro que deseas guardar los datos del Repositorio Git?';
    console.log($("#idSistemaGit").val());
    if ($("#idSistemaGit").val()!=="" &&  $("#idSistemaGit").val()!==null){
        titleSave='¿Actualizar GIT?';
        msgSave='¿Estas seguro que deseas actualizar los datos del Repositorio Git?';
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
            guardarRepositorioGit();
        }
    });
}

const guardarRepositorioGit = async () =>{
    const options={
        method: "POST",
        body: JSON.stringify({
            idSistemaGit:$("#idSistemaGit").val(),
            catSistema: { idSistema:$("#idSistema").val()},
            codigoGit:$("#codigoGit").val(),
            descripcion:$("#descripcionGit").val()
        }),
        headers: headers
    };

    try {
        onLoading("dlgModalFormSistema");
        const res = await fetch(contexto+'/api/v1/sistema-git/saveGit', options);
        const respJSON = await res.json();

        if (res.ok) {

            if ($("#idSistemaGit").val()!=="" ||  $("#idSistemaGit").val()!==null){
                Toast.fire({
                    icon: "success",
                    title: "Los datos del repositorio Git se actualizaron correctamente"
                });
            }
            else {
                Toast.fire({
                    icon: "success",
                    title: "Los datos del repositorio Git se guardaron correctamente"
                });
            }

            $("#idSistemaGit").val("");
            $("#codigoGit").val("");
            $("#descripcionGit").val("");

            obtienCodigosGit($("#idSistema").val())
            offLoading("dlgModalFormSistema");
        } else{
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalFormSistema");
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

function eliminarSistemaGit(idSistemaGit){

    Swal.fire({
        title: 'Eliminar Registro Git',
        text: '¿Estas seguro que deseas eliminar el registro?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminarSistemaGitAsync(idSistemaGit);
        }
    })
}


const eliminarSistemaGitAsync = async (idSistemaGit) =>{
    const options={
        method: "DELETE",
        body: JSON.stringify({idSistemaGit: idSistemaGit}),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/v1/sistema-git/deleteSistemaGit', options);
        const respJSON = await res.json();

        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Datos del repositorio Git eliminado correctamente"
            });

            obtienCodigosGit($("#idSistema").val())
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

const getSistemaGit = async (idSistemaGit) => {

    const options={
        method: "GET",
        headers: headers
    };

    try {
        onLoading("dlgModalFormSistema");
        const res = await fetch(contexto+'/api/v1/sistema-git/getSistemaGit/'+idSistemaGit, options);
        const respJSON = await res.json();

        $("#idSistemaGit").val("");
        $("#codigoGit").val("");
        $("#descripcionGit").val("");

        if (res.ok){
            $("#idSistemaGit").val(respJSON.idSistemaGit);
            $("#codigoGit").val(respJSON.codigoGit);
            $("#descripcionGit").val(respJSON.descripcion);

            offLoading("dlgModalFormSistema");
        }else{
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalFormSistema");
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

function editarSistemaGit(idSistemaGit){
    $("#idSistemaGit").val("");
    $("#codigoGit").val("");
    $("#descripcionGit").val("");
    getSistemaGit(idSistemaGit);
}


