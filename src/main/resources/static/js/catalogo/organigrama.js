const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));


google.charts.load('current', {packages:["orgchart"]});
//google.charts.setOnLoadCallback(drawChart);


const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let idEntepublicoNivel2=null;
let idEntepublicoNivel3=null;
let nivelSeleccionado=1;


$(document).ready(function(){


    jQuery.event.special.mousewheel = {
        setup: function( _, ns, handle ){
            this.addEventListener("mousewheel", handle, { passive: true });
        }
    };


    inicializarDependencias();

    $("#idEntePublicoSearch").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large"
    });

/*
    if ($("#idEntePublicoSession").val()!=""){
        google.charts.setOnLoadCallback(obtieneOrganigrama($("#idEntePublicoSession").val(), null, nivelSeleccionado));
    }
*/

    $('#idEntePublicoSearch').on('change', function() {
        idEntepublicoNivel2=null;
        idEntepublicoNivel3=null;
        nivelSeleccionado=1;
        google.charts.setOnLoadCallback(obtieneOrganigrama($("#idEntePublicoSearch").val(), null, nivelSeleccionado));
    });

    const guardarUnidadAsync = async () =>{
        const options={
            method: "POST",
            body: JSON.stringify({
                idUnidadAdministrativa:$("#idUnidadAdministrativa").val(),
                rfcTitular:$("#rfcTitular").val(),
                reglamentoInterior:$("#reglamentoInterior").val(),
                cveUnidadAdministrativa:$("#cveUnidadAdministrativa").val(),
                nombreUnidadAdministrativaAbreviada:$("#nombreUnidadAdministrativaAbreviada").val(),
                nombreUnidadAdministrativa:$("#nombreUnidadAdministrativa").val(),
                nombreCargoUnidad:$("#nombreCargoUnidad").val(),
                nombreCategoriaUnidad:$("#nombreCategoriaUnidad").val(),
                correoUnidadAdministrativa:$("#correoUnidadAdministrativa").val(),
                telefono:$("#telefono").val(),
                numeroExtension:$("#numeroExtension").val(),
                numeroOrden:$("#numeroOrden").val()

            }),
            headers: headers
        };

        try {
            onLoading("dlgModalOrganigrama");
            const res = await fetch(contexto+'/api/catalogo/organigrama/guardarUnidadAdministrativaOrg', options);
            const respJSON = await res.json();

            if (res.ok) {

                $('#dlgModalOrganigrama').modal('hide');


                if (nivelSeleccionado==1)
                    google.charts.setOnLoadCallback(obtieneOrganigrama($("#idEntePublicoSearch").val(), null, nivelSeleccionado));

                if (nivelSeleccionado==2)
                    google.charts.setOnLoadCallback(obtieneOrganigrama($("#idEntePublicoSearch").val(), idEntepublicoNivel2, nivelSeleccionado));

                if (nivelSeleccionado==3)
                    google.charts.setOnLoadCallback(obtieneOrganigrama($("#idEntePublicoSearch").val(), idEntepublicoNivel3, nivelSeleccionado));


                Toast.fire({
                    icon: "success",
                    title: "Los datos de la unidad se actualizaron correctamente"
                });

                offLoading("dlgModalOrganigrama");
            } else{
                Toast.fire({
                    icon: "error",
                    title: respJSON.message
                });
                offLoading("dlgModalOrganigrama");
            }

        }catch (e) {
            var error =  JSON.parse(e.responseText);
            offLoading("dlgModalOrganigrama");
            Toast.fire({
                icon: "error",
                title: error.message
            });
        }
    };

    function guardar(){

        let titleSave='¿Actualizar Unidad Administrativa?';
        let msgSave='¿Estas seguro que deseas actualizar los datos de la unidad administrativa?';

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
                guardarUnidadAsync();
            }
        });
    }


    $( "#formUnidad" ).validate( {
        rules: {
            rfcTitular:"required",
            nombreTitular:"required",
            //cveUnidadAdministrativa:"required",
            nombreUnidadAdministrativaAbreviada:"required",
            nombreUnidadAdministrativa:"required",
            nombreCargoUnidad:"required",
            nombreCategoriaUnidad:"required",
            telefono:"required",
            numeroOrden:"required"
        },
        messages: {
            rfcTitular: "Campo requerido",
            nombreTitular: "Campo requerido",
            //cveUnidadAdministrativa: "Campo requerido",
            nombreUnidadAdministrativaAbreviada: "Campo requerido",
            nombreUnidadAdministrativa: "Campo requerido",
            nombreCargoUnidad: "Campo requerido",
            nombreCategoriaUnidad: "Campo requerido",
            telefono: "Campo requerido",
            numeroOrden: "Campo requerido"
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


    $("#formUnidad").submit(function(e) {
        e.preventDefault();
        if($('#formUnidad').valid()) {
            guardar();
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



async function obtieneOrganigrama(idEntePublico,idEntePublicoSeleccionado,  nivel){

    let itemsTemp=[];

    const options={
        method: "GET",
        headers: {
            "X-CSRF-Token":  $('input[name="_csrf"]').val(),
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    if (nivel==1){
        idEntepublicoNivel2=null;
        idEntepublicoNivel3=null;
    }

    if (nivel==2){
        idEntepublicoNivel2=idEntePublicoSeleccionado
        idEntepublicoNivel3=null;
    }
    if (nivel==3)
        idEntepublicoNivel3=idEntePublicoSeleccionado;


    url = contexto+"/api/catalogo/getOrganigramaId/"+idEntePublico;

    if (nivel==2)
        url = contexto+"/api/catalogo/getOrganigramaIdNivel2/"+idEntePublico+"/"+idEntepublicoNivel2;

    if (nivel==3)
        url = contexto+"/api/catalogo/getOrganigramaIdNivel3/"+idEntePublico+"/"+idEntepublicoNivel2+"/"+idEntepublicoNivel3;

    onLoading(null);
    const res = await fetch(url, options);
    const respJSON = await res.json();

    if (res.ok){


        for (var d = 0; d < respJSON.length; d++) {

            let padre=respJSON[d]["parent"];

            if (padre=="#")
                padre=null;

           /* itemsTemp.push( { id: respJSON[d]["id"], parent: padre, title: "aaas", description:  respJSON[d]["text"]/!*, image:null*!/});*/
            let nombre="";

            if (respJSON[d]["nombreCompleto"]!=null)
                nombre=respJSON[d]["nombreCompleto"]

            let botonGrupo='<div class="btn-group btn-group-sm align-bottom" role="group" aria-label="Basic example">';

            var botonEditar='';
            if ($("#activeEditar").val()=="true")
                botonEditar='<button type="button" class="btn btn-success" onclick="editarUnidad('+respJSON[d]["id"]+')"><i class="bi bi-pencil-square"></i></button>';


            var botonHijo='';
            if (respJSON[d]["tieneHijo"]!=null && respJSON[d]["tieneHijo"]==true)
                botonHijo='<button type="button" class="btn btn-dark" onclick="expanderUnidad('+respJSON[d]["id"]+','+respJSON[d]["nivel"]+')"><i class="bi bi-diagram-2"></i></button>';


            botonGrupo=botonGrupo+botonEditar;
            botonGrupo=botonGrupo+botonHijo;
            botonGrupo=botonGrupo+'</div>';


            itemsTemp.push( [{ v: respJSON[d]["id"], f:  '<span class="fw-bold tooltip_x"  style="color:#ffc107;">'+respJSON[d]["text2"]+'<span class="tooltiptext">'+respJSON[d]["text"]+'</span></span><hr class="bg-warning"/><div class="fw-bold"  style="color:white;">'+ nombre+'</div>'+botonGrupo,},padre,'']);
        }

        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Name');
        data.addColumn('string', 'Manager');
        data.addColumn('string', 'ToolTip');

        // For each orgchart box, provide the name, manager, and tooltip to show.
        data.addRows(itemsTemp);


        for (i=0; i<data.Wf.length; i++){
            data.setRowProperty(i, 'style', 'width:300px;background :#343a40');
        }

        // Create the chart.
        var chart = new google.visualization.OrgChart(document.getElementById('chart_div'));


        chart.draw(data, {'allowHtml':true, allowCollapse:true,filas_compactas:true,size:'small'});

        offLoading(null);
    }else{
        Toast.fire({
            icon: "error",
            title: respJSON.message
        });
        offLoading(null);
    }
}

function getTablaCatUsuarios(){
    const tableCu = new DataTable('#tableCatUsuario', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering: false,
        bSort: false,
        ajax: {
            url: contexto+'/api/seguridad/tablaUsuariosDisponibles2',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    idEntePublico:$("#idEntePublicoSearch").val(),
                    idUnidadAdministrativa: $("#idUnidadAdministrativa").val()
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

function seleccionarUsuario(idUsuario, rfcUsuario, nombreCompleto){
    $('#idUsuario').val(idUsuario);
    $('#rfcTitular').val(rfcUsuario);
    $('#nombreTitular').val(nombreCompleto);
    $('#dlgModalCatUsuario').modal('hide');
    $("#rfcTitular").removeClass( "is-valid" );
    $("#rfcTitular").removeClass( "is-invalid" );
    $("#nombreTitular").removeClass( "is-valid" );
    $("#nombreTitular").removeClass( "is-invalid" );
}


function modalCatUsuarios(){

        getTablaCatUsuarios();
        $('#dlgModalCatUsuario').modal('show');

}


function limpiarFormulario() {
    $("#rfcTitular").removeClass( "is-valid" );
    $("#rfcTitular").removeClass( "is-invalid" );
    $("#nombreTitular").removeClass( "is-valid" );
    $("#nombreTitular").removeClass( "is-invalid" );
    $("#cveUnidadAdministrativa").removeClass( "is-valid" );
    $("#cveUnidadAdministrativa").removeClass( "is-invalid" );
    $("#nombreUnidadAdministrativaAbreviada").removeClass( "is-valid" );
    $("#nombreUnidadAdministrativaAbreviada").removeClass( "is-invalid" );
    $("#nombreUnidadAdministrativa").removeClass( "is-valid" );
    $("#nombreUnidadAdministrativa").removeClass( "is-invalid" );
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
    $('#idUsuario').val("");
    $('#rfcTitular').val("");
    $('#nombreTitular').val("");
    $('#reglamentoInterior').val("");
    $('#cveUnidadAdministrativa').val("");
    $('#nombreUnidadAdministrativaAbreviada').val("");
    $('#nombreUnidadAdministrativa').val("");
    $('#nombreCargoUnidad').val("");
    $('#nombreCategoriaUnidad').val("");
    $('#correoUnidadAdministrativa').val("");
    $('#telefono').val("");
    $('#numeroExtension').val("");
    $('#numeroOrden').val("");
}

async function editarUnidad(id){


    limpiarFormulario();

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
        $("#rfcTitular").val(respJSON.rfcTitular);
        $("#nombreTitular").val(respJSON.nombreTitular);
        $("#reglamentoInterior").val(respJSON.reglamentoInterior);
        $("#cveUnidadAdministrativa").val(respJSON.cveUnidadAdministrativa);
        $("#nombreUnidadAdministrativaAbreviada").val(respJSON.nombreUnidadAdministrativaAbreviada);
        $("#nombreUnidadAdministrativa").val(respJSON.nombreUnidadAdministrativa);
        $("#nombreCargoUnidad").val(respJSON.nombreCargoUnidad);
        $("#nombreCategoriaUnidad").val(respJSON.nombreCategoriaUnidad);
        $("#correoUnidadAdministrativa").val(respJSON.correoUnidadAdministrativa);
        $("#telefono").val(respJSON.telefono);
        $("#numeroExtension").val(respJSON.numeroExtension);
        $("#numeroOrden").val(respJSON.numeroOrden);

        $('#dlgModalOrganigrama').modal('show');
        offLoading(null);
    }else{
        Toast.fire({
            icon: "error",
            title: respJSON.message
        });
        offLoading(null);
    }

}

function expanderUnidad(id, nivel){
    nivelSeleccionado=nivel;
    obtieneOrganigrama($("#idEntePublicoSearch").val(), id, nivel);
}
