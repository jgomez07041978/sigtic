const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let tablePlantillas=null;
let tableCampos=null;
let respuestaSave="";
let metodoSave="";
let listaMascara=[];

$(document).ready(function(){

    $('[data-bs-toggle="tooltip"]').tooltip();

    //$("#idImgPK").attr("src",contexto+"/include/imagen/premary_key.jpg");

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
        dropdownParent: $("#dlgModalFormPlantilla")
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


    $('.select2Campo').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormCampos")
    });
    $('.select2Campo')
        .parent('div')
        .children('span')
        .children('span')
        .children('span')
        .css('height', ' calc(3.5rem + 2px)');
    $('.select2Campo')
        .parent('div')
        .children('span')
        .children('span')
        .children('span')
        .children('span')
        .css('margin-top', '18px');
    $('.select2Campo')
        .parent('div')
        .find('label')
        .css('z-index', '1');

    getUnidadesAdmin();
    getSistemas(null);
    getSeparador(null);

    $('#cveTipoCampo').on('change', function(){
        getTipoCamposMascara( $('#cveTipoCampo').val());
        validarTipoCampo();
    });

    $('#idTipoCampoMascara').on('change', function(){
        if ($('#idTipoCampoMascara').val()=="")
            $('#mascara').val("");
        else
            actiualizarTipoMascara(listaMascara,$('#idTipoCampoMascara').val())

    });

    $('#idNuevaPlantilla').click(function () {
        limpiarPlantilla();

        $('#titleFormPlantilla').text("Nueva Plantilla");
        $('#dlgModalFormPlantilla').modal('show');
    });

    $("#btnOpenArbol").click(function () {
        $('#modal_arbol_unidad').modal('show');
    });

    $.validator.addMethod("formValArchivo", function (value, element, param) {
        if (value.lastIndexOf(".")==-1) {
            $('#idSeparador').val("");
            return false;
        }
        else {
            var extension = $('#nombreArchivo').val().substr(($('#nombreArchivo').val().lastIndexOf(".") + 1 - $('#nombreArchivo').val().length));

            if (extension.toLowerCase() == "txt"){
                //$('#idSeparador').val("");
                $('#idSeparador').removeAttr("disabled");

            }else{
                $('#idSeparador').val("");
                $('#idSeparador').attr("disabled", true);
            }

            if (extension.toLowerCase() == "txt" || extension.toLowerCase() == "xlsx" || extension.toLowerCase() == "xls")
                return true;
            else{
                $.validator.messages["formValArchivo"] = "Escriba nombre del archivo con extensión valida";
                return false;
            }
        }
    });

    $("#idFormPlantilla" ).validate( {
        ignore: "",
        rules: {
            descUnidadAdmtva: "required"
            ,idSistema: "required"
            ,baseDatos: "required"
            ,esquema: "required"
            ,nombrePlantillaAbrev: "required"
            ,nombrePlantilla: "required"
            ,nombreArchivo:{formValArchivo:true}
            ,numCampos: { required:true, min:1, digits:true}
            ,numLinea: { required:true, min:1, digits:true}
            ,cveEstatus: "required"
            ,idSeparador: {
                required: {
                    depends: function(elem) {
                        var extension = $('#nombreArchivo').val().replace(/^.*\./, '');

                        if (extension.toLowerCase()=="txt")
                            return true;
                        else
                            return false;
                    }
                }
            }
        },
        messages: {
            descUnidadAdmtva: "Selecciona la unidad administrativa"
            ,idSistema: "Selecciona el sistema"
            ,baseDatos: "Nombre de la base de datos"
            ,esquema: "Escriba el esquema"
            ,nombrePlantillaAbrev: "Escriba la descripción abreviada"
            ,nombrePlantilla: "Escriba la descripción"
            ,nombreArchivo:{}
            ,numCampos: "Escriba el número de campos"
            ,numLinea: "Escriba el número de filas"
            ,cveEstatus: "Seleccione el estatus"
            ,idSeparador: "Seleccione el separador"
        },
        errorElement: "em",
        errorPlacement: function (error, element) {
            error.addClass( "invalid-feedback" );
            element.closest('.form-group').append(error);
        },
        highlight: function (element, errorClass) {
            $( element ).addClass( "is-invalid" ).removeClass( "is-valid" );
        },
        unhighlight: function (element, errorClass) {
            $( element ).addClass( "is-valid" ).removeClass( "is-invalid" );
        }
    } );

    $("#idFormPlantilla").submit(function(e) {
        e.preventDefault();

        if($('#idFormPlantilla').valid()) {

            let confirmText = "";
            respuestaSave = "";
            metodoSave = "";

            if ($('#idPlantilla').val() == "") {
                confirmText = "¿Está seguro que desea guardar la plantilla?";
                respuestaSave = "La plantilla se guardó correctamente";
                metodoSave = "POST";
            } else {
                confirmText = "¿Está seguro que desea actualizar la plantilla?";
                respuestaSave = "La plantilla se actualizó correctamente";
                metodoSave = "PUT";
            }


            Swal.fire({
                title: "Guardar",
                text: confirmText,
                icon: 'question',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si',
                cancelButtonText: "No"
            }).then(function (result) {
                if (result.isConfirmed)
                    guardarPlantillaAsync();

                return false;
            });

            }else
            return false;


    });

    $('#btnNuevaPlantilla').click(function () {
        $('#idNuevaPlantilla').click();
    });

    getTabla();

    $('#idNuevoCampo').click(function () {
        limpiarCampo();
        $('#idPlantillaCampoTemp').val($('#idPantillaTemp').val());
        getUbicaciones($('#idPantillaTemp').val(),-1, null);
        getUbicacionesBd($('#idPantillaTemp').val(),-1, null);
        getNumCampos(null);
        $('#titleFormCampos').text("Nuevo Campo");
        $('#dlgModalCampos').modal('hide');
        $('#dlgModalFormCampos').modal('show');
    });

    getTipoCampos(null);
    getTipoCamposMascara(null,null, null);

    $("#idFormCampo" ).validate( {
        ignore: "",
        rules: {
            numCampo: "required"
            ,nombreCampoAbr: "required"
            ,nombreCampo: "required"
            ,ubicacionColumna: "required"
            ,etiquetaColumna: "required"
            //,numOrdenCampo: "required"
            ,campoBaseDatos:"required"
            ,cveTipoCampo: "required"
            ,idTipoCampoMascara:{
                required:{
                    depends: function(elem) {
                        if ($('#cveTipoCampo').val()=="D")
                            return true;
                        else
                            return false;
                    }
                }

            }
            ,obligatorio: "required"
            ,pkFkTabla: {
                required: {
                    depends: function(elem) {

                        if ($('#pkFkColumna').val().trim()!="" || $('#pkNumPosicionCampo').val().trim()!="")
                            return true;
                        else
                            return false;
                    }
                }
            }
            ,pkFkColumna: {
                required: {
                    depends: function(elem) {

                        if ($('#pkFkTabla').val().trim()!="" || $('#pkNumPosicionCampo').val().trim()!="")
                            return true;
                        else
                            return false;
                    }
                }
            }
            ,fkTabla: {
                required: {
                    depends: function(elem) {

                        if ($('#fkColumna').val().trim()!="" || $('#fkNumPosicionColumna').val().trim()!="")
                            return true;
                        else
                            return false;
                    }
                }
            }
            ,fkColumna: {
                required: {
                    depends: function(elem) {

                        if ($('#fkTabla').val().trim()!="" || $('#fkNumPosicionColumna').val().trim()!="")
                            return true;
                        else
                            return false;
                    }
                }
            }
            ,longitud: {
                required: {
                    depends: function(elem) {

                        if ($('#longitud').val().trim()!="")
                            return true;
                        else
                            return false;
                    }
                },
                number:true,
                max:4000
            }
            ,longitudDecimal: {
                required: {
                    depends: function(elem) {
                        if ($('#longitudDecimal').val().trim()!="")
                            return true;
                        else
                            return false;
                    }
                },
                number:true,
                max:99
            }
        },
        messages: {
            numCampo: "Selecciona el número de campo"
            ,nombreCampoAbr: "Escriba la descripción abreviada"
            ,nombreCampo: "Escriba la descripción"
            ,ubicacionColumna: "Escriba la ubicación de la columna"
            ,etiquetaColumna: "Escriba la etiqueta de la columna"
            //,numOrdenCampo: "Escriba el número de orden"
            ,campoBaseDatos:"Escriba el nombre del campo"
            ,cveTipoCampo: "Seleccione el tipo de campo"
            ,idTipoCampoMascara: "Seleccione el tipo de máscara"
            ,obligatorio: "Seleccione si es obligatorio"
            ,pkFkTabla: "Escriba el nombre de la tabla destino"
            ,pkFkColumna: "Escriba el nombre de la columna destino"
            ,fkTabla: "Escriba el nombre de la tabla foreana"
            ,fkColumna: "Escriba el nombre de la columna foreana"
            ,longitud: "Longitud menor a 4000 caracteres"
            ,longitudDecimal: "Longitud en caracteres del número menor a 99"
        },
        errorElement: "em",
        errorPlacement: function (error, element) {
            error.addClass( "invalid-feedback" );
            element.closest('.form-group').append(error);
        },
        highlight: function (element, errorClass) {
            $( element ).addClass( "is-invalid" ).removeClass( "is-valid" );
        },
        unhighlight: function (element, errorClass) {
            $( element ).addClass( "is-valid" ).removeClass( "is-invalid" );
        }
    });

    $("#idFormCampo").submit(function(e) {
        e.preventDefault();

        if($('#idFormCampo').valid()) {
            let confirmText = "";
            respuestaSave = "";
            metodoSave = "";

            if ($('#titleFormCampos').text() == "Editar Campo"){
                confirmText = "¿Está seguro que desea actualizar el campo?";
                respuestaSave = "El campo se actualizó correctamente";
                metodoSave = "PUT";
            }else{
                confirmText = "¿Está seguro que desea guardar el campo?";
                respuestaSave = "El campo se guardó correctamente";
                metodoSave = "POST";
            }

            Swal.fire({
                title: "Guardar",
                text: confirmText,
                icon: 'question',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si',
                cancelButtonText: "No"
            }).then(function (result) {
                if (result.isConfirmed) {
                    guardarCamposAsync();
                }
            });


            return true;
        }else
            return false;
    });

    $('#btnNuevoCampo').click(function () {
        $('#idNuevoCampo').click();
    });

});

function closeModalCampo(){
    $('#dlgModalFormCampos').modal('hide');
    $('#dlgModalCampos').modal('show');
}

function getUnidadesAdmin(){
    obtienearbol(function (data) {
        $("#idUnidadAdmtva").val(data.id);
        $("#descUnidadAdmtva").val(data.text);
        limpiarBuscarArbol();
        $('#modal_arbol_unidad').modal('hide');
    });
}

function getSistemas(callback){
    fillSelectGet('/api/catalogo/tic/getSistemasVigente', '#idSistema','idSistema','nombreSistema',null, callback);
}

function getSeparador(callback){
    fillSelectGet('/api/plantillas/getSeparadorVigente', '#idSeparador','idSeparador','nombreSeparador',null, callback);
}

const getUbicaciones = async (idPlantilla, numCampo, selecciona) =>{

    if (idPlantilla==null || numCampo=="")
        $("#ubicacionColumna").empty();
    else {

        const options = {
            method: "GET",
            headers: headers
        };

        const res = await fetch(contexto + "/api/campos-plantilla/lista-campos/" + idPlantilla+'/'+ numCampo, options);
        const respJSON = await res.json();

        if (res.ok) {
            llenarCombo('#ubicacionColumna', 'nombreCampo', 'nombreCampo', respJSON);
            if (selecciona != null && selecciona != ""){
                $("#ubicacionColumna").val(selecciona);
                $('#ubicacionColumna').trigger('change');
            }

        } else {

            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }
    }

};

const getUbicacionesBd = async (idPlantilla, numCampo, selecciona) =>{

    if (idPlantilla==null || numCampo=="")
        $("#campoBaseDatos").empty();
    else {

        const options = {
            method: "GET",
            headers: headers
        };

        const res = await fetch(contexto + "/api/campos-plantilla/lista-camposBd/" + idPlantilla+'/'+ numCampo, options);
        const respJSON = await res.json();

        if (res.ok) {
            llenarCombo('#campoBaseDatos', 'nombreCampo', 'nombreCampo', respJSON);
            if (selecciona != null && selecciona != ""){
                $("#campoBaseDatos").val(selecciona);
                $('#campoBaseDatos').trigger('change');
            }

        } else {

            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }
    }

};

/*function getUbicaciones(idPadre1, idPadre2, callback){
    fillSelectGet_2('/api/campos-plantilla/lista-campos', '#ubicacionColumna','nombreCampo','nombreCampo',idPadre1, idPadre2, callback);
}*/

function limpiarPlantilla() {
    $('#nav-data-general-tab').tab('show');
    $("#descUnidadAdmtva").removeClass( "is-valid" );
    $("#descUnidadAdmtva").removeClass( "is-invalid" );
    $("#descUnidadAdmtva-error").removeAttr( "style" );

    $("#idSistema").removeClass( "is-valid" );
    $("#idSistema").removeClass( "is-invalid" );

    $("#baseDatos").removeClass( "is-valid" );
    $("#baseDatos").removeClass( "is-invalid" );

    $("#esquema").removeClass( "is-valid" );
    $("#esquema").removeClass( "is-invalid" );

    $("#nombrePlantillaAbrev").removeClass( "is-valid" );
    $("#nombrePlantillaAbrev").removeClass( "is-invalid" );

    $("#nombrePlantilla").removeClass( "is-valid" );
    $("#nombrePlantilla").removeClass( "is-invalid" );

    $("#nombreArchivo").removeClass( "is-valid" );
    $("#nombreArchivo").removeClass( "is-invalid" );

    $("#numCampos").removeClass( "is-valid" );
    $("#numCampos").removeClass( "is-invalid" );

    $("#numLinea").removeClass( "is-valid" );
    $("#numLinea").removeClass( "is-invalid" );

    $("#idSeparador").removeClass( "is-valid" );
    $("#idSeparador").removeClass( "is-invalid" );

    $("#cveEstatus").removeClass( "is-valid" );
    $("#cveEstatus").removeClass( "is-invalid" );


    $('#idPlantilla').val("");
    $('#idUnidadAdmtva').val("");
    $('#descUnidadAdmtva').val("");
    $('#idSistema').val("");
    $("#idSistema").trigger('change');
    $('#baseDatos').val("");
    $('#esquema').val("");
    $('#nombrePlantillaAbrev').val("");
    $('#nombrePlantilla').val("");
    $('#nombreArchivo').val("");
    $('#numCampos').val("");
    $('#numLinea').val("");
    $('#idSeparador').val("");
    $('#idSeparador').attr("disabled", true);
    $('#cveEstatus').val("VIG");
    $('#sqlSelect').val("");
    $('#sqlSelect2').val("");
    $('#sqlSelect3').val("");
    $('#sqlSelect4').val("");
    $('#sqlSelect5').val("");
    $('#sqlInsert').val("");
    $('#sqlInsert2').val("");
    $('#sqlInsert3').val("");
    $('#sqlInsert4').val("");
    $('#sqlInsert5').val("");
    $('#sqlUpdate').val("");
    $('#sqlUpdate2').val("");
    $('#sqlUpdate3').val("");
    $('#sqlUpdate4').val("");
    $('#sqlUpdate5').val("");
    $('#sqlMerge').val("");
    $('#sqlMerge2').val("");
    $('#sqlMerge3').val("");
    $('#sqlMerge4').val("");
    $('#sqlMerge5').val("");
    $('#sqlDelete').val("");
    $('#sqlDelete2').val("");
    $('#sqlDelete3').val("");
    $('#sqlDelete4').val("");
    $('#sqlDelete5').val("");
    $('#sqlValidacion').val("");
    $('#sqlValidacion2').val("");
    $('#sqlValidacion3').val("");
    $('#sqlValidacion4').val("");
    $('#sqlValidacion5').val("");
}


const guardarPlantillaAsync = async () =>{

    const options={
        method:metodoSave,
        body: JSON.stringify($("#idFormPlantilla").serializeToJSON()),
        headers: headers
    };

    try {
        onLoading("dlgModalFormPlantilla");
        const res = await fetch(contexto+'/api/plantillas', options);
        const respJSON = await res.json();

        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: respuestaSave
            });
            $('#titleFormPlantilla').text("Editar Plantilla");
            $("#idPlantilla").val(respJSON.idPlantilla);
            getTabla();
            offLoading("dlgModalFormPlantilla");
        } else{
            Toast.fire({
                icon: "error",
                title: "Error al guardar la plantilla"
            });
            offLoading("dlgModalFormPlantilla");
        }

    }catch (e) {
        var error =  JSON.parse(e.responseText);
        offLoading("dlgModalFormPlantilla");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
};


function getTabla(){

    if (tablePlantillas!=null)
        tablePlantillas.clear();

     tablePlantillas = new DataTable('#tablePlantillas', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: true,
        ajax: {
            url: contexto+'/api/plantillas/all',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            'data': function (d) {
                return JSON.stringify($.extend( {}, d, {}));
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json'
        },
        lengthChange: false,
        select: false
    });
}


function openModalPlantilla(idPlantilla){
    $('#titleFormPlantilla').text("Editar Plantilla");
    limpiarPlantilla();

    $.ajax({ url: contexto+'/api/plantillas/'+idPlantilla,
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function(response) {
            $('#idPlantilla').val(response.idPlantilla);
            $('#idUnidadAdmtva').val(response.idUnidadAdmtva);
            $('#descUnidadAdmtva').val(response.nombreUnidadAdmtva);
            $('#idSistema').val(response.idSistema);
            $("#idSistema").trigger('change');
            $('#baseDatos').val(response.baseDatos);
            $('#esquema').val(response.esquema);
            $('#nombrePlantillaAbrev').val(response.nombrePlantillaAbrev);
            $('#nombrePlantilla').val(response.nombrePlantilla);
            $('#nombreArchivo').val(response.nombreArchivo);
            $('#numCampos').val(response.numCampos);
            $('#numLinea').val(response.numLinea);

            //$('#idSeparador').val(response.idSeparador);
            if ( $('#nombreArchivo').val().lastIndexOf(".")==-1) {
                $('#idSeparador').val("");
                $('#idSeparador').attr("disabled", true);
            }
            else {
                var extension = $('#nombreArchivo').val().substr(($('#nombreArchivo').val().lastIndexOf(".") + 1 - $('#nombreArchivo').val().length));

                if (extension.toLowerCase() == "txt"){
                    $('#idSeparador').val(response.idSeparador);
                    $('#idSeparador').removeAttr("disabled");

                }else{
                    $('#idSeparador').val("");
                    $('#idSeparador').attr("disabled", true);
                }
            }

            $('#cveEstatus').val(response.cveEstatus);

            $('#sqlSelect').val(response.sqlSelect);
            $('#sqlSelect2').val(response.sqlSelect2);
            $('#sqlSelect3').val(response.sqlSelect3);
            $('#sqlSelect4').val(response.sqlSelect4);
            $('#sqlSelect5').val(response.sqlSelect5);
            $('#sqlInsert').val(response.sqlInsert);
            $('#sqlInsert2').val(response.sqlInsert2);
            $('#sqlInsert3').val(response.sqlInsert3);
            $('#sqlInsert4').val(response.sqlInsert4);
            $('#sqlInsert5').val(response.sqlInsert5);
            $('#sqlUpdate').val(response.sqlUpdate);
            $('#sqlUpdate2').val(response.sqlUpdate2);
            $('#sqlUpdate3').val(response.sqlUpdate3);
            $('#sqlUpdate4').val(response.sqlUpdate4);
            $('#sqlUpdate5').val(response.sqlUpdate5);
            $('#sqlMerge').val(response.sqlMerge);
            $('#sqlMerge2').val(response.sqlMerge2);
            $('#sqlMerge3').val(response.sqlMerge3);
            $('#sqlMerge4').val(response.sqlMerge4);
            $('#sqlMerge5').val(response.sqlMerge5);
            $('#sqlDelete').val(response.sqlDelete);
            $('#sqlDelete2').val(response.sqlDelete2);
            $('#sqlDelete3').val(response.sqlDelete3);
            $('#sqlDelete4').val(response.sqlDelete4);
            $('#sqlDelete5').val(response.sqlDelete5);
            $('#sqlValidacion').val(response.sqlValidacion);
            $('#sqlValidacion2').val(response.sqlValidacion2);
            $('#sqlValidacion3').val(response.sqlValidacion3);
            $('#sqlValidacion4').val(response.sqlValidacion4);
            $('#sqlValidacion5').val(response.sqlValidacion5);

            $('#dlgModalFormPlantilla').modal('show');
        },
        error: function (response) {

        }
    });
}

function eliminarPlantillas(idPlantilla){

    Swal.fire({
        title: "Eliminar",
        text: "¿Esta seguro de eliminar la plantilla?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function (result) {
        if (result.isConfirmed) {
            eliminarPlantillaAsync(idPlantilla);
        }
    });
}


const eliminarPlantillaAsync = async (idPlantilla) =>{
    const options={
        method: "DELETE",
        body: JSON.stringify({idPlantilla:idPlantilla}),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/plantillas', options);
        const respJSON = await res.json();

        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "La plantilla se elimino correctamente"
            });

            getTabla();
            offLoading(null);
        } else{
            Toast.fire({
                icon: "error",
                title: "Errr al eliminar la plantilla"
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

function agregarCampos(idPlantilla,descrpcionPlantilla,totalCampos, camposCapturados){
    $('#titleListaCampos').text("Lista de Campos de : "+descrpcionPlantilla);
    $('#idPantillaTemp').val(idPlantilla);
    $('#totalCampos').val(totalCampos);
    $('#idEtiquetaTotalCampo').text(totalCampos);
    $('#idEtiquetaCampoCapturado').text(camposCapturados);
    getCampos(idPlantilla);
    $('#dlgModalCampos').modal('show');
}

function getCampos(idPlantilla){

    if (tableCampos!=null)
        tableCampos.clear();

    tableCampos = new DataTable('#tableCampos', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: true,
        ajax: {
            url: contexto+'/api/campos-plantilla/getCampoPorPlantilla',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            'data': function (d) {
                return JSON.stringify($.extend( {}, d, {idPlantilla:idPlantilla}) );
            },
            complete: function (data) {
                var total=data['responseJSON'].totalCampos;
                $("#idEtiquetaCampoCapturado").text(total);
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json'
        },
        lengthChange: false,
        select: false
    });
}

function getTipoCampos(callback){
    fillSelectGet('/api/tipo_campos/vigentes', '#cveTipoCampo','cveTipoCampo','nombreTipoCampo',null, callback);
}

const getTipoCamposMascaraAsync = async (cveTipoCampo,idElemento,valor) =>{

    const options={
        method: "GET",
        headers: headers
    };

    onLoading("dlgModalFormCampos");
    const res = await fetch(contexto+"/api/tipoCampoMascara/getTipoCampoMascaraPorTipoCampo/"+cveTipoCampo, options);
    const respJSON = await res.json();

    if (res.ok){
        listaMascara=respJSON;
        llenarCombo('#idTipoCampoMascara','idTipoCampoMascara','nombreMascara', respJSON);
        if (valor!=null)
            $(idElemento).val(valor)

        offLoading("dlgModalFormCampos");

    }else{

        Toast.fire({
            icon: "error",
            title: respJSON.message
        });
        offLoading("dlgModalFormCampos");
    }

};


function getTipoCamposMascara(cveTipoCampo, idElemento, valor){
    if (cveTipoCampo==null || cveTipoCampo=="")
        inicializarCombo('#idTipoCampoMascara')
        else
            getTipoCamposMascaraAsync(cveTipoCampo, idElemento, valor);

   // fillSelectGet('/api/tipoCampoMascara/getTipoCampoMascaraPorTipoCampo', '#idTipoCampoMascara','idTipoCampoMascara','nombreMascara',cveTipoCampo, callback);
}

function getNumCampos(numCampo){
    $.ajax({ url: contexto+'/api/campos-plantilla/asignados',
        type: 'POST',
        dataType: 'HTML',
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        data: JSON.stringify({idPlantilla:$("#idPantillaTemp").val()}),
        success: function(response) {
            var asignados=JSON.parse("["+response+"]");
            var total=$("#totalCampos").val()

            //console.log(total+" si llega");
            $("#numCampo").html("");
            if(response=="" || response==null){
                //console.log("Aqui")
                for(var i=1; i<=total; i++) {
                    $("#numCampo").append("<option value='"+i+"'>"+i+"</option>");
                }
            }else{

                for(var i=1; i<=total; i++) {
                    //console.log(i)
                    if (numCampo!=null && numCampo==i)
                        $("#numCampo").append("<option value='"+i+"' selected>"+i+"</option>");
                    else
                    if(!asignados.includes(i)){
                        //console.log("si")
                        $("#numCampo").append("<option value='"+i+"'>"+i+"</option>");
                    }



                }
            }
        },
        error: function (response) {
            var error =  JSON.parse(response.responseText);
            setTimeout(function () {
                $('#preloader').modal('hide');
                swal("Error", error.message, "error");
            }, 1000);
        }
    });
}

const guardarCamposAsync = async () =>{
    const options={
        method: metodoSave,
        body: JSON.stringify($("#idFormCampo").serializeToJSON()),
        headers: headers
    };

    try {
        onLoading("dlgModalFormCampos");
        const res = await fetch(contexto+'/api/campos-plantilla', options);
        const respJSON = await res.json();

       //console.log(respJSON);

        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: respuestaSave
            });

            $('#dlgModalFormCampos').modal('hide');
            $('#dlgModalCampos').modal('show');

            getCampos($('#idPlantillaCampoTemp').val());
            getTabla()
            //$('#titleFormCampos').text("Editar Campo");

            offLoading("dlgModalFormCampos");
        } else{
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalFormCampos");
        }

    }catch (e) {
        var error =  JSON.parse(e.responseText);
        offLoading("dlgModalFormCampos");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
};

function limpiarCampo() {

    $("#numCampo").removeClass( "is-valid" );
    $("#numCampo").removeClass( "is-invalid" );

    $("#nombreCampoAbr").removeClass( "is-valid" );
    $("#nombreCampoAbr").removeClass( "is-invalid" );

    $("#nombreCampo").removeClass( "is-valid" );
    $("#nombreCampo").removeClass( "is-invalid" );

    $("#ubicacionColumna").removeClass( "is-valid" );
    $("#ubicacionColumna").removeClass( "is-invalid" );

    $("#etiquetaColumna").removeClass( "is-valid" );
    $("#etiquetaColumna").removeClass( "is-invalid" );

/*    $("#numOrdenCampo").removeClass( "is-valid" );
    $("#numOrdenCampo").removeClass( "is-invalid" );*/

    $("#campoBaseDatos").removeClass( "is-valid" );
    $("#campoBaseDatos").removeClass( "is-invalid" );

    $("#cveTipoCampo").removeClass( "is-valid" );
    $("#cveTipoCampo").removeClass( "is-invalid" );

    $("#obligatorio").removeClass( "is-valid" );
    $("#obligatorio").removeClass( "is-invalid" );

    $("#pkFkTabla").removeClass( "is-valid" );
    $("#pkFkTabla").removeClass( "is-invalid" );

    $("#pkFkColumna").removeClass( "is-valid" );
    $("#pkFkColumna").removeClass( "is-invalid" );

    $("#pkNumPosicionCampo").removeClass( "is-valid" );
    $("#pkNumPosicionCampo").removeClass( "is-invalid" );

    $("#fkTabla").removeClass( "is-valid" );
    $("#fkTabla").removeClass( "is-invalid" );

    $("#fkColumna").removeClass( "is-valid" );
    $("#fkColumna").removeClass( "is-invalid" );

    $("#fkNumPosicionColumna").removeClass( "is-valid" );
    $("#fkNumPosicionColumna").removeClass( "is-invalid" );

    $('#numCampo').val("");
    $('#nombreCampoAbr').val("");
    $('#nombreCampo').val("");
    $('#ubicacionColumna').val("");
    $('#ubicacionColumna').trigger('change');
    $('#etiquetaColumna').val("");
   // $('#numOrdenCampo').val("");
    $('#campoBaseDatos').val("");
    $('#campoBaseDatos').trigger('change');
    $('#cveTipoCampo').val("");
    //$('#longitud').attr("readOnly",true)
    //$('#longitudDecimal').attr("readOnly",true)

    $('#obligatorio').val("S");
    $('#longitud').val("");
    $('#longitudDecimal').val("");
    $('#mascara').val("");
    inicializarCombo($('#idTipoCampoMascara'))
    $('#omitirTextValiacion').val("");

    $('#pkFkTabla').val("");
    $('#pkFkColumna').val("");
    $('#pkNumPosicionCampo').val("");
    $('#fkTabla').val("");
    $('#fkColumna').val("");
    $('#fkNumPosicionColumna').val("");
}

function editarCampo(idPlantilla,numCampo){
    limpiarCampo();
    $('#titleFormCampos').text("Editar Campo");
    getNumCampos(numCampo);
    $('#idPlantillaCampoTemp').val($('#idPantillaTemp').val());



    $.ajax({ url: contexto+'/api/campos-plantilla/unico/'+idPlantilla+'/'+numCampo,
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function(response) {
            $('#numCampo').val(response.camposPK.numCampo);
            $('#nombreCampoAbr').val(response.nombreCampoAbr);
            $('#nombreCampo').val(response.nombreCampo);
            //$('#ubicacionColumna').val(response.ubicacionColumna);
            getUbicaciones(idPlantilla,numCampo, response.ubicacionColumna);

            $('#etiquetaColumna').val(response.etiquetaColumna);
            //$('#numOrdenCampo').val(response.numOrdenCampo);

           // $('#campoBaseDatos').val(response.campoBaseDatos);
            getUbicacionesBd(idPlantilla,numCampo, response.campoBaseDatos);
            $('#cveTipoCampo').val(response.tipoCampo.cveTipoCampo);
            getTipoCamposMascara( $('#cveTipoCampo').val(), "#idTipoCampoMascara",response.idTipoCampoMascara);
            validarTipoCampo();
            $('#obligatorio').val(response.obligatorio);
            $('#longitud').val(response.longitud);
            $('#longitudDecimal').val(response.longitudDecimal);
            $('#idTipoCampoMascara').val(response.idTipoCampoMascara);
            $('#mascara').val(response.mascara);
            $('#omitirTextValiacion').val(response.omitirTextValiacion);

            $('#pkFkTabla').val(response.pkFkTabla);
            $('#pkFkColumna').val(response.pkFkColumna);
            $('#pkNumPosicionCampo').val(response.pkNumPosicionCampo);

            $('#fkTabla').val(response.fkTabla);
            $('#fkColumna').val(response.fkColumna);
            $('#fkNumPosicionColumna').val(response.fkNumPosicionColumna);

            $('#dlgModalCampos').modal('hide');
            $('#dlgModalFormCampos').modal('show');
        },
        error: function (response) {

        }
    });
}

const actiualizarTipoMascara = (respJSON, value)=>{
    console.log(respJSON);
    console.log(value);
    $("#mascara").val("");
    for (var d = 0; d < respJSON.length; d++) {
        if (respJSON[d]["idTipoCampoMascara"]==value) {
            $("#mascara").val(respJSON[d]["mascara"]);
            break;
        }
    }
};

function validarTipoCampo(){
    $('#longitud').val("");
    $('#longitudDecimal').val("");
    $('#longitud').attr("readOnly",true)
    $('#longitudDecimal').attr("readOnly",true)

    if ($('#cveTipoCampo').val()=="N"){
        $('#longitud').val("");
        $('#longitud').attr("readOnly", true)
        $('#longitudDecimal').removeAttr("readOnly")
        $('#longitud').removeAttr("readOnly");
    }

    if ($('#cveTipoCampo').val()=="V" || $('#cveTipoCampo').val()=="I"){
        $('#longitudDecimal').val("");
        $('#longitudDecimal').attr("readOnly", true)
        $('#longitud').removeAttr("readOnly");
    }
}

function eliminarCampo(idPlantilla,numCampo){

    Swal.fire({
        title: "Eliminar",
        text: "¿¿Está seguro que desea eliminar el campo?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function (result) {
        if (result.isConfirmed) {
            eliminarCampoAsync(idPlantilla,numCampo);
        }
    });
}

const eliminarCampoAsync = async (idPlantilla,numCampo) =>{
    const options={
        method: "DELETE",
        body: JSON.stringify({idPlantilla:idPlantilla, numCampo:numCampo}),
        headers: headers
    };

    try {
        onLoading("dlgModalCampos");
        const res = await fetch(contexto+'/api/campos-plantilla', options);
       // const respJSON = await res.json();

        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "El campo se elimino correctamente"
            });
            getCampos(idPlantilla);
            getTabla();
            offLoading("dlgModalCampos");
        } else{
            Toast.fire({
                icon: "error",
                title: "Errr al eliminar el campo"
            });
            offLoading("dlgModalCampos");
        }

    }catch (e) {
        offLoading("dlgModalCampos");
        Toast.fire({
            icon: "error",
            title: "Error al eliminar el campo"
        });
    }
};

function openModalUploadPlantilla(idPlantilla){
    $("#idPlantillaUpload").val(idPlantilla);
    $('#dlgModalUploadPlantilla').modal('show');
}

function validaUpload() {
    var mensaje = "";

    if($("#ruta_plantilla").val()==="")
        mensaje += "Seleccione el Archivo";

    return mensaje;
}


const adjuntarAsync = async (datos) =>{
    const options={
        method: "POST",
        body: JSON.stringify({
            idPlantilla:$("#idPlantillaUpload").val(),
            contenido:datos.contenido,
            nombre:datos.nombre
        }),
        headers: headers
    };

    try {
        onLoading("dlgModalUploadPlantilla");
        const res = await fetch(contexto+'/api/plantillas/uploadExpedienteJpa', options);
        const respJSON = await res.json();

        if (res.ok) {

            $("#ruta_plantilla").val("");
            $(".custom-file-label").text("Seleccionar");

            Toast.fire({
                icon: "success",
                title: respJSON.message
            });

            offLoading("dlgModalUploadPlantilla");
        } else{
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalUploadPlantilla");
        }


    }catch (e) {
        var error =  JSON.parse(e.responseText);
        offLoading("dlgModalUploadPlantilla");
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
        var dato_archivo = $('#ruta_plantilla').prop("files")[0];
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

$("#txtBuscar").keyup(function () {
    busqueda($(this).val())
});

/*
function descargar(rutaDocumento,nombreDocumentoOriginal){
    $('#preloader').modal('show');
    var oReq = new XMLHttpRequest();
    oReq.open("POST", contexto+'/api/solicitudes/descarga-archivo', true);
    oReq.responseType = "arraybuffer";
    oReq.setRequestHeader("x-csrf-token", $('input[name="_csrf"]').val());
    oReq.setRequestHeader('Content-Type', 'application/json')
    oReq.onload = function(oEvent) {
        setTimeout(function () {
            $('#preloader').modal('hide');
            if(oReq.status === 500){
                var json = String.fromCharCode.apply(null, new Uint8Array(oReq.response));
                var response = JSON.parse(json);
                //swal("Error", response.message,"error");
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
}*/
