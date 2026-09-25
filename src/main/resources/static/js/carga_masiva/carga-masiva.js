const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let tableCargaMasiva=null;
let tableCargaMasivaDetalle=null;
let tableCargaMasivaError=null;
let listArchivo=[];
let listaPlantilla=[];

$(document).ready(function(){

    jQuery.event.special.mousewheel = {
        setup: function( _, ns, handle ){
            this.addEventListener("mousewheel", handle, { passive: true });
        }
    };

    getTabla();
    plantillasList("/api/plantillas")
    getPlantillas(null);

    $("#plantilla").change(function(){
        if($(this).val()!="" && $(this).val()!="Seleccione"){
            clearSubirPlantilla();
            getTabla();

            var encontre=false;
            for (var d = 0; d < listaPlantilla.length; d++) {
                if (listaPlantilla[d]["idPlantilla"]==$(this).val() && listaPlantilla[d]["fileNameOriginal"]!=null) {
                    encontre = true;
                    break;
                }
            }

            if (encontre){
                $("#descargaPl").prop('href', '#');
                $("#descargaPl").hide();
                $("#descargaPersonalizada").show();
                $("#descargaPersonalizada").prop('href',contexto+"/api/plantillas/exportPlantilla/"+$(this).val());
            }else{
                $("#descargaPl").show();
                $("#descargaPl").prop('href',contexto+"/api/carga_masiva/exportPlantilla/"+$(this).val());
                $("#descargaPersonalizada").prop('href', '#');
                $("#descargaPersonalizada").hide();
            }
        }
        else {
            $("#descargaPl").prop('href', '#');
            $("#descargaPl").hide();
            $("#descargaPersonalizada").prop('href', '#');
            $("#descargaPersonalizada").hide();
        }
    });

    $("#archivo").change(function(){
        listArchivo=[];
        var file = $(this).get(0).files[0];
        //console.log(file)
        getBase64(file).then(
            data => {
                var archivo = data.split(",");
                //console.log("aqui")
                listArchivo.push({
                    archivo: archivo[1],
                    nombre: file.name.split('.').shift(),
                    extension: file.name.split('.').pop(),
                    tipoMime: file.type
                });
            });
    });

    $('#formCarga').validate({
        rules: {
            plantilla: "required",
            fechaCorte: "required",
            archivo: "required",
        },
        messages: {
            plantilla: "Seleccione la plantilla",
            fechaCorte: "Seleccione la fecha de corte",
            archivo: "Seleccione el archivo"
        },
        errorElement: 'span',
        errorPlacement: function (error, element) {
            //error.insertAfter(element);
            error.addClass('invalid-feedback');
            element.closest('.form-group').append(error);
        },
        highlight: function (element) {
            $(element).addClass('is-invalid');
        },
        unhighlight: function (element) {
            $(element).removeClass('is-invalid');
        }
    });

    $("#formCarga").submit(function(e) {
        e.preventDefault();
        if($('#formCarga').valid()) {
            cargarArchivo()
            return false;
        }
        else return false;
    });



});

function getPlantillas(callback){
    fillSelectGet('/api/plantillas', '#plantilla','idPlantilla','nombrePlantilla',null, callback);
}

async function plantillasList(relativeUrl){

    const options={
        method: "GET",
        headers: {
            "X-CSRF-Token":  $('input[name="_csrf"]').val(),
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    url = contexto+relativeUrl
    const res = await fetch(url, options);
    const respJSON = await res.json();
    if (res.ok){
        listaPlantilla=respJSON;
    }
}

function getTabla(){

    //var groupColumn1 = 3;
    //var groupColumn2 = 4;

    if (tableCargaMasiva!=null)
        tableCargaMasiva.clear();

    tableCargaMasiva = new DataTable('#tableCargaMasiva', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        autoWidth: false,
        ordering: false,
        initComplete: function(settings, json) {
            $('[data-bs-toggle="tooltip"]').tooltip();
        },
        ajax: {
            url: contexto+'/api/carga_masiva/all',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    plantilla:{idPlantilla:$("#plantilla").val()}
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
                targets: 1,
                "width": "100px",
                "className": "text-center"
            },
            {
                targets: 2,
                "width": "100px",
                "className": "text-center"
            },
            {
                targets: 3,
                "width": "120px",
                "className": "text-center"
            },
            {
                targets: 4,
                "width": "120px",
                "className": "text-center"
            },
            {
                targets: 5,
                "width": "90px",
                "className": "text-center"
            },  {
                targets: 6,
                "width": "120px",
                "className": "text-center"
            },{
                targets: 7,
                "width": "200px",
                "className": "text-center"
            }],
        lengthChange: false,
        select: false
    });

}


function cargarArchivo(){

    onLoading(null);
    $.ajax({ url: contexto+'/api/carga_masiva',
        type: 'POST',
        dataType: 'HTML',
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        data: JSON.stringify({
            idCargaDatos:$("#idCargaDatosPlantilla").val(),
            idPlantilla:$("#plantilla").val(),
            nombreArchivo: listArchivo[0].nombre,
            extension: listArchivo[0].extension,
            archivo: listArchivo[0].archivo,
            fechaCorte: $("#fechaCorte").val()
        }),
        success: function(response) {
            offLoading(null);
            Toast.fire({
                icon: "success",
                title: "La plantilla se cargo correctamente"
            });

            getTabla();
            clearSubirPlantilla();
        },
        error: function (response) {
            clearSubirPlantilla();
            offLoading(null);
            var error = JSON.parse(response.responseText);

            Toast.fire({
                icon: "error",
                title: error.message
            });

        }
    });

}

function eliminarCarga(idCarga, idPlantilla){
    //console.log(idCarga+" "+idPlantilla);

    Swal.fire({
        title: "Eliminar",
        text: "¿Esta seguro de eliminar la Carga Masiva?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function (result) {
        if (result.isConfirmed) {
            deleteCarga(idPlantilla, idCarga);
        }
    });
}

function deleteCarga(idPlantilla, idCarga){
    onLoading(null);
    $.ajax({ url: contexto+'/api/carga_masiva',
        type: 'DELETE',
        dataType: 'HTML',
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        data: JSON.stringify({
            cargaDatosPlantillaPk:{idCargaDatosPlantilla:idCarga},
            plantilla:{ idPlantilla:idPlantilla }
        }),
        success: function(response) {
            offLoading(null);
            getTabla()
            Toast.fire({
                icon: "success",
                title: "La carga se elimino correctamente"
            });


        },
        error: function (response) {
            offLoading(null);
            var error =  JSON.parse(response.responseText);
            Toast.fire({
                icon: "error",
                title: error.message
            });
        }
    });
}

const detallesCarga = async (idCargaDatos,idPlantilla) =>{
    const options={
        method: "GET",
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/carga_masiva/tablaCargaDetalle/'+idCargaDatos+'/'+idPlantilla, options);
        const respJSON = await res.json();

        if (res.ok){
            //console.log(respJSON);
            $("#dlgModalListaCargaMasiva").modal("show");

            $("#titlePlantilla").text("Nombre Plantilla: "+respJSON.nombrePlantilla)

                if ( $.fn.dataTable.isDataTable( '#tableCargaMasivaDetalle' ) ) {
                    tableCargaMasivaDetalle.destroy();
                    $('#tableCargaMasivaDetalle').empty();

                }

            tableCargaMasivaDetalle = new DataTable('#tableCargaMasivaDetalle', {
                dom: 'frtip',
                responsive: true,
                ordering: false,
                paging: false,
                bProcessing: true,
                bServerSide:false,
                language:{
                    "url": contexto+'/include/datatables/spanish.json',
                },
                "columns": respJSON.columnas,
                "columnDefs": respJSON.titulos,
                "data": respJSON.datos
            });


        }

        offLoading(null);
    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al obtener los datos"
        });
    }
};

const validarCarga = async (idCargaDatos,idPlantilla) =>{
    const options={
        method: "GET",
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/carga_masiva/validarPlantilla/'+idCargaDatos+"/"+idPlantilla, options);
        const respJSON = await res.json();

        if (res.ok){

            if (respJSON.msg.includes("exito")) {
                Toast.fire({
                    icon: 'success',
                    title: respJSON.msg
                });

            }else{
                Toast.fire({
                    icon: 'error',
                    title: respJSON.msg
                });
            }
            getTabla();

        }

        offLoading(null);
    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al validar los datos"
        });
    }
}


function verErrores(idCargaDatos, idPlantilla){

    $("#dlgModalListaCargaMasivaError").modal("show");

    if (tableCargaMasivaError!=null)
        tableCargaMasivaError.clear();

    tableCargaMasivaError = new DataTable('#tableCargaMasivaError', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        autoWidth: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/carga_masiva/tablaErroresPlantilla/'+idCargaDatos+'/'+idPlantilla,
            type: 'GET',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            complete: function (data) {
                $("#titlePlantillaError").text(data.nombrePlantilla);
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [
            {
                targets: 0,
                "width": "90px",
                "className": "text-center"
            },
            {
                targets: 1,
                "width": "100px",
                "className": "text-center"
            },
            {
                targets: 3,
                "width": "120px",
                "className": "text-center"
            }],
        lengthChange: false,
        select: false
    });

}


function subirPlantilla(idCargaDatos, fechaCorte){
    $("#idMsgPlantillaEdit").show();
    $("#idCargaDatosPlantilla").val(idCargaDatos);
    var date1 = fechaCorte.split("/");
    $("#fechaCorte").val(date1[2] + "-" + date1[1] + "-" + date1[0]);
}

function clearSubirPlantilla(){
    $("#idMsgPlantillaEdit").hide();
    $("#idCargaDatosPlantilla").val("");
    $("#fechaCorte").val("");
    $("#archivo").val("");
}



const ejecutarScript = async (idCargaDatos) =>{
    const options={
        method: "GET",
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/carga_masiva/ejecutaSql/'+idCargaDatos, options);
        const respJSON = await res.json();

        if (res.ok){

            if (respJSON.msg)

            if (respJSON.msg.includes("exitosamente")) {
                Toast.fire({
                    icon: 'success',
                    title: respJSON.msg
                });

            }else{
                Toast.fire({
                    icon: 'error',
                    title: respJSON.msg
                });
            }
            getTabla();

        }

        offLoading(null);
    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al validar los datos"
        });
    }
}
