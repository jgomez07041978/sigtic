var contexto=$("#contextSigtic").val();

const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let tableCartaResponsivaDetalle=null;

$(document).ready(function () {
    $("#idAceptarCr").prop('checked', false);
    aceptarCartaResponsiva($("#idCartaResponsiva").val());
    obtieneCartasResponsivaDetalle($("#idCartaResponsiva").val());

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

const aceptarCartaResponsiva = async (idCartaResponsiva) => {

    const options={
        method: "GET",
        headers: headers
    };

    try {
        onLoading(null);
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

function obtieneCartasResponsivaDetalle(idCartaResponsiva) {

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
        ordering: true,
        ajax: {
            url: contexto+'/api/cartaResponsiva/getCartaResponsivaLotes',
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

    let funcionConfirmar = "aceptarCrFree";
    let mensaje = "Se realizó la aceptación correctamente";

    if ($("#idRechazarCr").is(":checked")) {
        funcionConfirmar = "rechazarCrFree";
        mensaje = "Se realizó el rechazo correctamente";
    }


    const options = {
        method: "PUT",
        body: JSON.stringify({
            idCartaResponsiva: $("#idCartaResponsiva").val(),
            notaRechazo: $("#motivoRechazo").val()
        }),
        headers: headers
    };
    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/cartaResponsiva/' + funcionConfirmar, options);
        const respJSON = await res.json();

        if (res.ok) {
            offLoading(null);

            $("#btnAceptar").hide();
            $("#rechazoText").hide();
            $("#aceptarText").hide();
            $("#motivoText").hide();

            Toast.fire({
                icon: "success",
                title: mensaje
            });

            setTimeout(() => {
                location.href =getAbsolutePath();
            }, 500);


        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading(null);
        }
    } catch (e) {
        var error = JSON.parse(e.responseText);
        offLoading(null);
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
}

function getAbsolutePath() {
    var loc = window.location;
    var pathName = loc.pathname.substring(0, loc.pathname.lastIndexOf('/') + 1);
    return loc.href.substring(0, loc.href.length - ((loc.pathname + loc.search + loc.hash).length - pathName.length));
}