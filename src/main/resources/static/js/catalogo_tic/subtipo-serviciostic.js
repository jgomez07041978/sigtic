const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let msgSave= "";

$(document).ready(function(){

    $('.select2Se').select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormSubtipoServiciosFirma")
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




    fillSelectGet("/api/catalogo/tic/getCatTipoServiciosTic","#idTipoServicioTic", "idTipoServicioTic", "nombreTipoServicioTic", null, null);

    $('[data-bs-toggle="tooltip"]').tooltip();

    jQuery.event.special.mousewheel = {
        setup: function( _, ns, handle ){
            this.addEventListener("mousewheel", handle, { passive: true });
        }
    };

    $('#idNuevoSubtipoServicio').click(function () {
        limpiarFormularioSub();
        $('#titleFormSubtipoServiciosTic').text("Nuevo Subtipo de Servicio");
        $('#dlgModalFormSubtipoServiciosTic').modal('show');
    });

    inicializarDependencias();

    obtienSubtiposervicios();

    $("#idFormSubtipoServiciosTic").validate( {
        ignore: "",
        rules: {
            idTipoServicioTic:"required",
            cveSubtipo:"required",
            nombreSubtipoAbr:"required",
            nombreCveServicioTic:"required",
            numeroOrden:"required",
            cveEstatus:"required"
        },
        messages: {
            idTipoServicioTic:"Campo requerido",
            cveSubtipo:"Campo requerido",
            nombreSubtipoAbr:"Campo Requerido",
            nombreCveServicioTic:"Campo Requerido",
            numeroOrden:"Campo Requerido",
            cveEstatus:"Campo Requerido"
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
    });

    $("#idFormSubtipoServiciosTic").submit(function(e) {
        e.preventDefault();

        if ($('#idFormSubtipoServiciosTic').valid()) {
            guardarSubTipoServicio();
        }
    });

    $("#genera-cr-si-firmada").on("change", function(){
        if ($("#genera-cr-si-firmada").is(":checked")){
            if ($("#genera-cr-no").is(":checked")){
                $("#genera-cr-no-firmada").prop("checked", true);
            }
        }
    });

    $("#genera-cr-no").on("change", function(){
        if ($("#genera-cr-no").is(":checked")){
                $("#genera-cr-no-firmada").prop("checked", true);
        }
    });

});

const inicializarDependencias = async () => {
    const options = {
        method: "POST",
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/entePublico/lista', options);
        const respJSON = await res.json();

        llenarCombo('#idEntePublico', 'idEntePublico', 'nombreEntePublicoConCve', respJSON);

        offLoading(null);
    } catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
};

function obtienSubtiposervicios(){

    var groupColumn=1;

    const table = new DataTable('#tableSubtipoServicio', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        paging: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/catalogo/tic/tablaSubtipoServiciosTic',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
        },
        bProcessing: true,
        bServerSide: false,
        columnDefs: [{visible: false, targets: groupColumn},
            {
                "targets": 2,
                "width": "60",
                "className": "text-center",
            },
            {
                "targets": 3,
                "width": "60",
                "className": "text-center",
            },
            {
                "targets": 4,
                "width": "60",
                "className": "text-center",
            },
            {
                "targets": 5,
                "width": "60",
                "className": "text-center",
            },
            {
                "targets": 6,
                "width": "60",
                "className": "text-center",
            },
            {
                "targets": 7,
                "width": "60",
                "className": "text-center",
            },
            {
                "targets": 8,
                "width": "120",
                "className": "text-center",
            }],
        drawCallback: function (settings) {
        var api = this.api();
        var rows = api.rows({ page: 'current' }).nodes();
        var last = null;

        api.column(groupColumn, { page: 'current' })
            .data()
            .each(function (group, i) {
                if (last !== group) {
                    $(rows)
                        .eq(i)
                        .before('<tr class="group fw-bold"><td colspan="9" class="bg-secondary-subtle text-dark">' + group + '</td></tr>');

                    last = group;
                }
            });
    },
        language:{
            "url": contexto+'/include/datatables/spanish.json'
        },
        lengthChange: false,
        select: false
    });

}

function limpiarFormularioSub(){
    $("#cveSubtipo").removeClass("is-valid");
    $("#cveSubtipo").removeClass("is-invalid");
    $("#idTipoServicioTic").removeClass("is-valid");
    $("#idTipoServicioTic").removeClass("is-invalid");
    $("#nombreCveServicioTic").removeClass("is-valid");
    $("#nombreCveServicioTic").removeClass("is-invalid");
    $("#nombreSubtipoAbr").removeClass("is-valid");
    $("#nombreSubtipoAbr").removeClass("is-invalid");
    $("#numeroOrden").removeClass("is-valid");
    $("#numeroOrden").removeClass("is-invalid");
    $("#cveEstatus").removeClass("is-valid");
    $("#cveEstatus").removeClass("is-invalid");

    $("#control-inv-no").prop("checked", false);
    $("#control-ip-no").prop("checked", false);
    $("#control-puerto-no").prop("checked", false);
    $("#control-usuario-no").prop("checked", false);

    $("#idSubtipoServiciosTic").val("");
    $("#cveSubtipo").val("");
    $("#idTipoServicioTic").val("");
    $("#nombreCveServicioTic").val("");
    $("#nombreSubtipoAbr").val("");
    $("#numeroOrden").val("");
    $("#cveEstatus").val("VIG");

}

const guardarSubtipoServiciosTicAsync = async () =>
{
    const options = {
        method: "POST",
        body: JSON.stringify({
            idSubtipoServicioTic: $("#idSubtipoServiciosTic").val(),
            cveSubtipoServicioTic: $("#cveSubtipo").val(),
            catTipoServiciosTic: {idTipoServicioTic: $("#idTipoServicioTic").val()},
            nombreSubtipoServicioTic: $("#nombreCveServicioTic").val(),
            nombreSubtipServicioTicAbr: $("#nombreSubtipoAbr").val(),
            numeroOrdenSubtipoServicioTic: $("#numeroOrden").val(),
            cveEstatus: $("#cveEstatus").val(),
            controlInventario: $("#control-inv-no").is(":checked")?"N":"S",
            controlIp: $("#control-ip-no").is(":checked")?"N":"S",
            controlPuerto: $("#control-puerto-no").is(":checked")?"N":"S",
            controlUsuarios: $("#control-usuario-no").is(":checked")?"N":"S",
            generaCr: $("#genera-cr-no").is(":checked")?"N":"S",
            crFirmada: $("#genera-cr-no-firmada").is(":checked")?"N":"S"
        }),
        headers: headers
    };
    try {
        onLoading("dlgModalFormSubtipoServiciosTic");
        const res = await
            fetch(contexto + '/api/catalogo/tic/saveSubtipoServiciosTic', options);
        const respJSON = await
            res.json();

        if (res.ok) {
            offLoading("dlgModalFormSubtipoServiciosTic");
            $('#dlgModalFormSubtipoServiciosTic').modal('hide');

            obtienSubtiposervicios();

            Toast.fire({
                icon: "success",
                title: "Los datos del subtipo servicio se han guardado"
            });

        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalFormSubtipoServiciosTic");
        }
    } catch (e) {
        var error = JSON.parse(e.responseText);
        offLoading("dlgModalFormSubtipoServiciosTic");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
}

function guardarSubTipoServicio(){

    let titleSave='Guardar Subtipo Servicio';
    let msgSave='¿Estas seguro que deseas guardar los datos del Subtipo Servicio?';
    if ($('#idSubtipoServiciosTic').val() != ""){
        titleSave='Actualizar Subtipo Servicio';
        msgSave='¿Estas seguro que deseas actualizar los datos del Subtipo Servicio?';
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
            guardarSubtipoServiciosTicAsync();
        }
    });
}

const getSubtipoServiciosTic = async (idSubtipoServicioTic) => {
        const options={
            method: "GET",
            hearders: headers
        };

        try {
            onLoading(null);
            const res = await fetch(contexto+'/api/catalogo/tic/getSubtipoServiciosTic/'+idSubtipoServicioTic, options);
            const respJSON = await res.json();

            if(res.ok){
                $("#idSubtipoServiciosTic").val(respJSON.idSubtipoServicioTic);
                $("#cveSubtipo").val(respJSON.cveSubtipoServicioTic);
                $("#idTipoServicioTic").val(respJSON.catTipoServiciosTic.idTipoServicioTic);
                $("#nombreCveServicioTic").val(respJSON.nombreSubtipoServicioTic);
                $("#nombreSubtipoAbr").val(respJSON.nombreSubtipServicioTicAbr);
                $("#numeroOrden").val(respJSON.numeroOrdenSubtipoServicioTic);
                $("#cveEstatus").val(respJSON.cveEstatus);

                if (respJSON.controlInventario==null || respJSON.controlInventario=="S")
                    $("#control-inv-si").prop("checked", true);
                else
                    $("#control-inv-no").prop("checked", true);

                if (respJSON.controlIp==null || respJSON.controlIp=="S")
                    $("#control-ip-si").prop("checked", true);
                else
                    $("#control-ip-no").prop("checked", true);

                if (respJSON.controlPuerto==null || respJSON.controlPuerto=="S")
                    $("#control-puerto-si").prop("checked", true);
                else
                    $("#control-puerto-no").prop("checked", true);

                if (respJSON.controlUsuarios==null || respJSON.controlUsuarios=="S")
                    $("#control-usuario-si").prop("checked", true);
                else
                    $("#control-usuario-no").prop("checked", true);

                if (respJSON.generaCr==null || respJSON.generaCr=="S")
                    $("#genera-cr-si").prop("checked", true);
                else
                    $("#genera-cr-no").prop("checked", true);

                if (respJSON.crFirmada==null || respJSON.crFirmada=="S")
                    $("#genera-cr-si-firmada").prop("checked", true);
                else
                    $("#genera-cr-no-firmada").prop("checked", true);



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
}

function editarSubtipoServiciosTic(idSubtipoServicioTic) {

    limpiarFormularioSub();
    getSubtipoServiciosTic(idSubtipoServicioTic);

    $('#titleFormSubtipoServiciosTic').text("Editar Subtipo Servicios Tic");
    $('#dlgModalFormSubtipoServiciosTic').modal('show');

}


const eliminarSubTipoServicioAsync = async (idSubTipoServicioTic) => {
    const options = {
        method: "DELETE",
        body: JSON.stringify({idSubtipoServicioTic: idSubTipoServicioTic}),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/catalogo/tic/deleteSubTipoServicio', options);
        const respJSON = await res.json();

        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "SubTipo de  Servicio eliminado correctamente"
            });

            obtienSubtiposervicios();
            offLoading(null);
        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading(null);
        }

    }catch (e) {
        var error = JSON.parse(e.responseText);
        offLoading(null);
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
}

function eliminarSubTipoServicio(idSubTipoServicioTic){

    Swal.fire({
        title: 'Eliminar SubTipo de Servicio',
        text: '¿Estas seguro que deseas eliminar el SubTipo de Servicio?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminarSubTipoServicioAsync(idSubTipoServicioTic);
        }
    })

}

const getSubtipoServiciosTicId = async (idSubtipoServicioTic) => {
    const options={
        method: "GET",
        hearders: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/catalogo/tic/getSubtipoServiciosTic/'+idSubtipoServicioTic, options);
        const respJSON = await res.json();

        if(res.ok){

            $("#idTipoServicioText").text(respJSON.catTipoServiciosTic.nombreTipoServicioTic);
            $("#idSubTipoServicioText").text(respJSON.nombreSubtipoServicioTic);

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
}


function configurarNumeroFirmas(idSubtipoServicioTic) {

    getSubtipoServiciosTicId(idSubtipoServicioTic);

    $("#idSubtipoServicioParam").val(idSubtipoServicioTic);
    $("#idEntePublico").val("")
    $("#idEntePublico").trigger('change');

    $("#numeroFirma").val("1");

    obtienSubtiposerviciosFirma();

    $('#titleFormSubtipoServiciosFirma').text("Configurar número Firma");
    $('#dlgModalFormSubtipoServiciosFirma').modal('show');

}

function obtienSubtiposerviciosFirma(){

    var formulario={
        catSubtipoServiciosTic:{idSubtipoServicioTic:$("#idSubtipoServicioParam").val()}
    };

    const table = new DataTable('#tableSubtipoServicioFirma', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        paging: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/subtipo_servicio_firma/tablaSubtipoServiciosFirma',
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
        bServerSide: false,
        columnDefs: [
           /* {
                "targets": 0,
                "width": "120",
                "className": "text-right",
            },*/
            {
                "targets": 2,
                "width": "100",
                "className": "text-center",
            },
            {
                "targets": 3,
                "width": "100",
                "className": "text-center",
            }],
        language:{
            "url": contexto+'/include/datatables/spanish.json'
        },
        lengthChange: false,
        select: false
    });

}

const guardarSubtipoServiciosFirmaAsync = async () =>
{
    const options = {
        method: "POST",
        body: JSON.stringify({
            idSubtipoServicioFirma: $("#idSubtipoServiciosFirma").val(),
            entePublico: {idEntePublico:$("#idEntePublico").val()},
            catSubtipoServiciosTic: {idSubtipoServicioTic:$("#idSubtipoServicioParam").val()},
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

        if (res.ok) {
            offLoading("dlgModalFormSubtipoServiciosFirma");
            $("#idEntePublico").val("");
            $("#idEntePublico").trigger('change');
            $("#numeroFirma").val("1");
            $("#idSubtipoServiciosFirma").val("");
            obtienSubtiposerviciosFirma();

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

function guardarSubTipoServicioFirma(){

    if ($("#idEntePublico").val()==""){
        Toast.fire({
            icon: "warning",
            title: "Seleccione el ente público"
        });
    }else
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
            guardarSubtipoServiciosFirmaAsync();
        }
    });
    }
}

const editarSubtipoServiciosFirma = async (idSubtipoServicioFirma) => {
    const options={
        method: "GET",
        hearders: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/subtipo_servicio_firma/getSubtipoServiciosFirma/'+idSubtipoServicioFirma, options);
        const respJSON = await res.json();

        if(res.ok){

            $("#idSubtipoServiciosFirma").val(respJSON.idSubtipoServicioFirma);
            $("#idEntePublico").val(respJSON.entePublico.idEntePublico);
            $("#idEntePublico").trigger('change');
            $("#numeroFirma").val(respJSON.numeroFirmas);


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
}
