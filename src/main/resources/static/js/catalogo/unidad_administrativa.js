const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let tableUnidadAdministrativa=null;
var idDomicilioTemp=-1;
var idUbicacionTemp=-1;


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

    inicializarDependencias();

    comboNivelUnidadesAdministrativa();


    $('#idEntePublicoSearch').on('change', function() {
        getUnidadesAdministrativaPadre();
        obtieneUnidadesAdministrativa($('#idEntePublicoSearch').val());
        getDomicilioEntePublico($('#idEntePublicoSearch').val());

        $("#descargaUnidades").prop('href',contexto+"/api/catalogo/exportUnidadesAdministrativa/"+$("#idEntePublicoSearch").val());
        $("#descargaUnidades").show();
    });

    $('#idNivelUnidadAdministrativa').on('change', function() {

        $('#cveUnidadAdministrativa').removeAttr("disabled");
        $('#esPresupuestalSi').removeAttr("disabled");
        if ($('#idNivelUnidadAdministrativa').val()=="1"){
            $("#descUnidadAdmtvaPadre").attr("aria-invalid",false);
            $("#descUnidadAdmtvaPadre").removeAttr("aria-describedby");
            $("#descUnidadAdmtvaPadre").removeClass("is-invalid");
            $("#descUnidadAdmtvaPadre").removeClass("is-valid");
            $("#descUnidadAdmtvaPadre").addClass("is-valid");
            $('#descUnidadAdmtvaPadre').val("");
            $('#idUnidadAdministrativaPadre').val("");
        }else
        if ($('#idNivelUnidadAdministrativa').val()=="6"){
            $('#cveUnidadAdministrativa').val("");
            $('#cveUnidadAdministrativa').attr("disabled", true);
            $("#esPresupuestalNo").prop("checked", true);
            $("#esPresupuestalSi").attr("disabled", true);
        }
    });


    $("#idNuevoUnidad").click(function (){
        limpiarFormulario();
        $("#dlgModalFormNuevaUnidad").modal('show');
    });


    $("#btnOpenArbolPadre").click(function () {
        if ($('#idNivelUnidadAdministrativa').val()==1){

            Swal.fire({
                title: 'Mensaje',
                text:  "No se requiere unidad padre cuando el nivel es secretaría.",
                icon: 'info',
            });
        }else{
            $('#modal_arbol_unidad').modal('show');
        }
    });

    $("#txtBuscar").keyup(function () {
        busqueda($(this).val())
    });


    $( "#formSearchUser" ).validate( {
        rules: {
            rfcTitularSearch: {
                required: function () {
                    if (rfcTitularSearch.length>=4)
                        return false;
                    else
                        return true;
                }
            }
        },
        messages: {
            rfcTitularSearch: "Campo requerido mínimo 4 caracteres"
        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            var n = element.attr("id");
            if (n=="rfcTitularSearch")
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

    $("#formSearchUser").submit(function(e) {
        e.preventDefault();
        if($('#formSearchUser').valid()) {
            getTablaCatUsuarios($("#rfcTitularSearch").val());
            return  true;
        }
        else {
            return false;
        }
    });



    $( "#formUnidadAdministrativa" ).validate( {
            rules: {
                idNivelUnidadAdministrativa:"required",
                descUnidadAdmtvaPadre: {
                    required: function () {

                        if ($("#idNivelUnidadAdministrativa").val() != "1") {
                            return true;
                        } else {
                            $("#descUnidadAdmtvaPadre").attr("aria-invalid",false);
                            $("#descUnidadAdmtvaPadre").removeAttr("aria-describedby");
                            $("#descUnidadAdmtvaPadre").removeClass("is-invalid");
                            $("#descUnidadAdmtvaPadre").removeClass("is-valid");
                            $("#descUnidadAdmtvaPadre").addClass("is-valid");

                            return false;
                        }
                    }
                },
                rfcTitular:"required",
                //nombreTitular:"required",
                cveUnidadAdministrativa:{
                    required: function () {

                    if ($("#esPresupuestalSi").is(":checked") && $("#cveUnidadAdministrativa").val().trim()==""){
                        return true;
                    }else{
                        $("#cveUnidadAdministrativa").attr("aria-invalid",false);
                        $("#cveUnidadAdministrativa").removeAttr("aria-describedby");
                        $("#cveUnidadAdministrativa").removeClass("is-invalid");
                        $("#cveUnidadAdministrativa").removeClass("is-valid");
                        $("#cveUnidadAdministrativa").addClass("is-valid");

                        return false;
                    }
                }
            },
            nombreUnidadAdministrativaAbreviada:"required",
            nombreUnidadAdministrativa:"required",
            nombreCargoUnidad:"required",
            nombreCategoriaUnidad:"required",
            telefono:{minlength: 10, maxlength: 10},
            numeroOrden:"required"
        },
        messages: {
            idNivelUnidadAdministrativa: "Campo requerido",
            descUnidadAdmtvaPadre: "Campo requerido",
            rfcTitular: "Campo requerido",
            //nombreTitular: "Campo requerido",
            cveUnidadAdministrativa: "Campo requerido",
            nombreUnidadAdministrativaAbreviada: "Campo requerido",
            nombreUnidadAdministrativa: "Campo requerido",
            nombreCargoUnidad: "Campo requerido",
            nombreCategoriaUnidad: "Campo requerido",
            telefono: "Campo requerido de 10 digitos",
            numeroOrden: "Campo requerido"
        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            var n = element.attr("id");
            if (n=="descUnidadAdmtvaPadre" || n=="rfcTitular")
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


    $("#formUnidadAdministrativa").submit(function(e) {
        e.preventDefault();
        if($('#formUnidadAdministrativa').valid()) {
            guardarUnidadAdministrativa();
            return  true;
        }
        else {
            return false;
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

        llenarCombo('#idEntePublicoSearch', 'idEntePublico','nombreEntePublicoConCve', respJSON);
        $("#idEntePublicoSearch").val($("#idEntePublicoSession").val());
        $("#idEntePublicoSearch").trigger('change');

        offLoading(null);
    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
};

const comboNivelUnidadesAdministrativa = async () =>{
    const options={
        method: "POST",
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/catalogo/getNivelUnidadAdministrativaList', options);
        const respJSON = await res.json();

        llenarCombo('#idNivelUnidadAdministrativa', 'idNivelUnidadAdministrativa','nombreNivelUnidadAdministrativaAbrev', respJSON);

        offLoading(null);
    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
};

function getUnidadesAdministrativaPadre(){
    obtienearbolPorInstitucion(function (data) {
        $("#idUnidadAdministrativaPadre").val(data.id);
        $("#descUnidadAdmtvaPadre").val(data.text);
        limpiarBuscarArbol();
        $('#modal_arbol_unidad').modal('hide');
    },$("#idEntePublicoSearch").val());
}


function modalCatUsuarios(){

    //getTablaCatUsuarios();
    $('#dlgModalFormNuevaUnidad').modal('hide');
    $('#dlgModalCatUsuario').modal('show');

}

function cerrarModalCatUsuarios(){

    $('#dlgModalCatUsuario').modal('hide');
    $('#dlgModalFormNuevaUnidad').modal('show');
}

function getTablaCatUsuarios(rfc){
    const tableCu = new DataTable('#tableCatUsuario', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering: false,
        bSort: false,
        ajax: {
            url: contexto+'/api/seguridad/tablaUsuariosDisponiblesSearch',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    idEntePublico: $('#idEntePublicoSearch').val(),
                    rfcTitular:rfc
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

function obtieneUnidadesAdministrativa(idUnidadAdministrativa) {

    if (tableUnidadAdministrativa!=null)
        tableUnidadAdministrativa.clear();
    
    tableUnidadAdministrativa = new DataTable('#tableUnidadAdministrativa', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        paging: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/catalogo/tablaUnidadAdministrativa/'+idUnidadAdministrativa,
            type: 'GET',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            }/*,
            complete: function (data) {
                var total=data['responseJSON'].iTotalDisplayRecords;

                if (total>0){
                    $("#idBoxUserNoREg").hide();
                    $("#idCardTablaUsuario").show();
                }
                else {
                    $("#idCardTablaUsuario").hide();
                    $("#idBoxUserNoREg").show();
                }

                offLoading(null);
            }*/
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },/*
        columnDefs: [{
            targets: 0,
            "width": "20%",
            "className": "text-center",
        },{
            targets: 1,
            "width": "20%",
            "className": "text-center",
        },{
            targets: 3,
            "width": "10%",
            "className": "text-center",
        },
            {
                targets: 4,
                "width": "10%",
                "className": "text-center",
            },
            {
                targets: 5,
                "width": "10%",
                "className": "text-center",
            }],*/
        lengthChange: false,
        select: false
    });

}


function limpiarFormulario() {
    idUbicacionTemp=-1;
    idDomicilioTemp=-1;
    $("#titleFormHeader").text("Alta de Unidad Administrativa");
    $("#idNivelUnidadAdministrativa").removeClass( "is-valid" );
    $("#idNivelUnidadAdministrativa").removeClass( "is-invalid" );

    $("#descUnidadAdmtvaPadre").attr("aria-invalid",false);
    $("#descUnidadAdmtvaPadre").removeAttr("aria-describedby");
    $("#descUnidadAdmtvaPadre").removeClass("is-invalid");
    $("#descUnidadAdmtvaPadre").removeClass("is-valid");
    $("#cveUnidadAdministrativa").removeClass( "is-valid" );
    $("#cveUnidadAdministrativa").removeClass( "is-invalid" );
    $("#descUnidadAdmtvaPadre-error").hide()
    $("#nombreUnidadAdministrativaAbreviada").removeClass( "is-valid" );
    $("#nombreUnidadAdministrativaAbreviada").removeClass( "is-invalid" );
    $("#nombreUnidadAdministrativa").removeClass( "is-valid" );
    $("#nombreUnidadAdministrativa").removeClass( "is-invalid" );

    $("#rfcTitular").removeClass( "is-valid" );
    $("#rfcTitular").removeClass( "is-invalid" );
    $("#rfcTitular-error").hide();
    $("#nombreTitular").removeClass( "is-valid" );
    $("#nombreTitular").removeClass( "is-invalid" );

    $("#nombreCargoUnidad").removeClass( "is-valid" );
    $("#nombreCargoUnidad").removeClass( "is-invalid" );
    $("#nombreCategoriaUnidad").removeClass( "is-valid" );
    $("#nombreCategoriaUnidad").removeClass( "is-invalid" );
    $("#correoUnidadAdministrativa").removeClass( "is-valid" );
    $("#correoUnidadAdministrativa").removeClass( "is-invalid" );
    $("#telefono").removeClass( "is-valid" );
    $("#telefono").removeClass( "is-invalid" );
    $("#numeroExtension").removeClass( "is-valid" );
    $("#numeroExtension").removeClass( "is-invalid" );
    $("#numeroOrden").removeClass( "is-valid" );
    $("#numeroOrden").removeClass( "is-invalid" );

    $('#idUnidadAdministrativa').val("");
    $('#idUnidadAdministrativaPadre').val("");
    $('#idNivelUnidadAdministrativa').val("");

    $('#cveUnidadAdministrativa').removeAttr("disabled");
    $('#esPresupuestalSi').removeAttr("disabled");
    $('#descUnidadAdmtvaPadre').val("");
    $('#cveUnidadAdministrativa').val("");
    $("#esPresupuestalNo").prop("checked", true);
    $('#nombreUnidadAdministrativaAbreviada').val("");
    $('#nombreUnidadAdministrativa').val("");

    $('#idUsuario').val("");
    $('#rfcTitular').val("");
    $('#nombreTitular').val("");
    $('#nombreCargoUnidad').val("");
    $('#reglamentoInterior').val("");
    $('#nombreCategoriaUnidad').val("");
    $('#correoUnidadAdministrativa').val("");
    $('#telefono').val("");
    $('#numeroExtension').val("");
    $('#numeroOrden').val("");

    $("#idDomicilioEnte").val("");
    $("#idDomicilioEnte").trigger('change');
    $("#idUbicacionUnidadAdmtva").val("");
    $("#idUbicacionUnidadAdmtva").trigger('change');
    $("#idbtnSaveText").text("Guardar");

}

function seleccionarUsuario(idUsuario, rfcUsuario, nombreCompleto){
    $('#idUsuario').val(idUsuario);
    $('#rfcTitular').val(rfcUsuario);
    $('#nombreTitular').val(nombreCompleto);
    $('#dlgModalCatUsuario').modal('hide');
    $('#dlgModalFormNuevaUnidad').modal('show');
    $("#rfcTitular").removeClass( "is-valid" );
    $("#rfcTitular").removeClass( "is-invalid" );
    $("#rfcTitular-error").hide();
    $("#nombreTitular").removeClass( "is-valid" );
    $("#nombreTitular").removeClass( "is-invalid" );
}


function getDomicilioEntePublico(idPadre,callback){
    if (idPadre!=null)
        fillSelectGetInterno('/api/catalogo/domicilioentePublico/all', '#idDomicilioEnte','idDomicilio','nombreDomicilio',idPadre, callback, "domicilio");
    else
        $('#idDomicilioEnte').empty();
}

function getUbicacionUnidad(idPadre, callback){
    if (idPadre!=null)
        fillSelectGetInterno('/api/catalogo/ubicacionUnidadAministrativa/all', '#idUbicacionUnidadAdmtva','idUbicacionAdaministrativa','nombreUbicacion',idPadre, callback, "ubicacion");
    else
        $('#idUbicacionUnidadAdmtva').empty();
}

function actualizarUbicaciones() {
    if ($("#idDomicilioEnte").val()!="")
        getUbicacionUnidad($("#idDomicilioEnte").val());
    else
        $('#idUbicacionUnidadAdmtva').empty();
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

        if (opcion=="domicilio") {
            if (idDomicilioTemp != null && idDomicilioTemp != -1) {
                $("#idDomicilioEnte").val(idDomicilioTemp);
                $("#idDomicilioEnte").trigger('change');
            }
        }

        if (opcion=="ubicacion") {
            if (idUbicacionTemp != null && idUbicacionTemp != -1) {
                $("#idUbicacionUnidadAdmtva").val(idUbicacionTemp);
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

function guardarUnidadAdministrativa(){

    let titleSave='¿Unidad Administrativa?';
    let msgSave='¿Estas seguro que deseas actualizar los datos de la unidad administrativa?';

    if ($("#idUnidadAdministrativa").val()=="")
        msgSave='¿Estas seguro que deseas guardar los datos de la unidad administrativa?';

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
            guardarUnidadAdministrativaAsync();
        }
    });
}

const guardarUnidadAdministrativaAsync = async () =>{
    const options={
        method: "POST",
        body: JSON.stringify({
            idUnidadAdministrativa:$("#idUnidadAdministrativa").val(),
            idUnidadAdministrativaPadre:$("#idUnidadAdministrativaPadre").val(),
            idEntePublico:$("#idEntePublicoSearch").val(),
            cveUnidadAdministrativa:$("#cveUnidadAdministrativa").val(),
            nombreUnidadAdministrativaAbreviada:$("#nombreUnidadAdministrativaAbreviada").val(),
            nombreUnidadAdministrativa:$("#nombreUnidadAdministrativa").val(),
            nivelUnidadAdministrativa:{idNivelUnidadAdministrativa : $("#idNivelUnidadAdministrativa").val()},
            unidadPresupuestal: $("#esPresupuestalNo").is(":checked")?"N":"S",
            rfcTitular:$("#rfcTitular").val(),
            nombreCargoUnidad:$("#nombreCargoUnidad").val(),
            nombreCategoriaUnidad:$("#nombreCategoriaUnidad").val(),
            correoUnidadAdministrativa:$("#correoUnidadAdministrativa").val(),
            telefono:$("#telefono").val(),
            numeroExtension:$("#numeroExtension").val(),
            numeroOrden:$("#numeroOrden").val(),
            idUbicacionUnidadAdministrativa:$("#idUbicacionUnidadAdmtva").val(),
            reglamentoInterior:$("#reglamentoInterior").val(),
        }),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/catalogo/unidad/guardarUnidadAdministrativa', options);
        const respJSON = await res.json();

        if (res.ok) {

            obtieneUnidadesAdministrativa($('#idEntePublicoSearch').val());
            $("#dlgModalFormNuevaUnidad").modal('hide');

            let msgResp="Los datos de la unidad se actualizaron correctamente";
            if ($("#idUnidadAdministrativa").val()=="")
                msgResp="Los datos de la unidad se guardaron correctamente";

            Toast.fire({
                icon: "success",
                title: msgResp
            });

            offLoading(null);
        } else{
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

async function editarUnidadAdministrativa(id){

    limpiarFormulario();

    $("#titleFormHeader").text("Editando Unidad Administrativa");

    const options={
        method: "GET",
        headers: {
            "X-CSRF-Token":  $('input[name="_csrf"]').val(),
            "Content-type": "application/json; charset=UTF-8"
        }
    };
    onLoading(null);
    const res = await fetch( contexto+"/api/catalogo/unidadAdministrativa/"+id, options);
    const respJSON = await res.json();

    if (res.ok){

        $("#idUnidadAdministrativa").val(respJSON.idUnidadAdministrativa);
        if (respJSON.nivelUnidadAdministrativa!=null)
            $("#idNivelUnidadAdministrativa").val(respJSON.nivelUnidadAdministrativa.idNivelUnidadAdministrativa);

        if (respJSON.unidadPresupuestal=="N")
            $("#esPresupuestalNo").prop("checked", true);
        else
            $("#esPresupuestalSi").prop("checked", true);

        $("#idUnidadAdministrativaPadre").val(respJSON.idUnidadAdministrativaPadre);
        $("#descUnidadAdmtvaPadre").val(respJSON.nombreUnidadAdministrativaPadre);
        $("#cveUnidadAdministrativa").val(respJSON.cveUnidadAdministrativa);
        $("#nombreUnidadAdministrativaAbreviada").val(respJSON.nombreUnidadAdministrativaAbreviada);
        $("#nombreUnidadAdministrativa").val(respJSON.nombreUnidadAdministrativa);
        $("#rfcTitular").val(respJSON.rfcTitular);
        $("#nombreTitular").val(respJSON.nombreTitular);
        $("#nombreCargoUnidad").val(respJSON.nombreCargoUnidad);
        $("#reglamentoInterior").val(respJSON.reglamentoInterior);
        $("#nombreCategoriaUnidad").val(respJSON.nombreCategoriaUnidad);
        $("#correoUnidadAdministrativa").val(respJSON.correoUnidadAdministrativa);
        $("#telefono").val(respJSON.telefono);
        $("#numeroExtension").val(respJSON.numeroExtension);
        $("#numeroOrden").val(respJSON.numeroOrden);


        if (respJSON.idUbicacionUnidadAdministrativa!=null){
            idUbicacionTemp=respJSON.idUbicacionUnidadAdministrativa;

            $("#idDomicilioEnte").val(respJSON.idDomicilioEntePublico);
            $("#idDomicilioEnte").trigger('change');
        }

        $('#dlgModalFormNuevaUnidad').modal('show');
        offLoading(null);
    }else{
        Toast.fire({
            icon: "error",
            title: respJSON.message
        });
        offLoading(null);
    }

}


function eliminarUnidadAdministrativa(id){

    Swal.fire({
        title: 'Eliminar Unidad',
        text: '¿Estás seguro que deseas eliminar la unidad administrativa?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No",
        closeOnCancel: false,
        closeOnConfirm: true,
        reverseButtons: true,
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminaUnidadAdministrativaAjax(id);
        }});
}

function eliminaUnidadAdministrativaAjax(id) {
    $('#preloader').modal('show');
    $.ajax({
        url: contexto+'/api/catalogo/deleteUnidadAdministrativa',
        type: 'DELETE',
        contentType: 'application/json',
        dataType: 'JSON',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        data: JSON.stringify({
            idUnidadAdministrativa: id,
        }),
        success: function (response) {
            setTimeout(function () {
                $('#preloader').modal('hide');
                if( response !== null){
                    obtieneUnidadesAdministrativa($('#idEntePublicoSearch').val());

                    Toast.fire({
                        icon: 'info',
                        title: "La unidad administrativa se elimino correctamente"
                    });
                }
            }, 1000);
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


