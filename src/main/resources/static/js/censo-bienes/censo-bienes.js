const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

/*var coloresText = ['text-primary', 'text-success', 'text-warning', 'text-info', 'text-dark', 'text-light'];*/
$(document).ready(function(){

    getConcentradoBienesPorEstatus();
    getConcentradoBienesPorEntePublico();
});

const getConcentradoBienesPorEntePublico = async () => {

    $('#tbConcentradoBienEnte tbody tr').slice().remove();
    const options = {
        method: "GET",
        header: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/censo-bien/concentradoBienesPorEntePublico', options);
        const respJSON = await res.json();

        if (res.ok) {

            for (i = 0; i < respJSON.listaConcentrado.length; i++) {
                var fila = "<tr>";
                fila = fila + "<td class='fw-bold'><i class='fas fa-circle fa-fw'></i> " + respJSON.listaConcentrado[i].nombre + "</td>";
                fila = fila + "<td class='fw-bold'><a href='#' onclick='getConcentradoBienesPorUnidadAdministrativa("+respJSON.listaConcentrado[i].id+",\""+respJSON.listaConcentrado[i].nombre+"\")'>" + number_format(respJSON.listaConcentrado[i].total, 0) + "</a></td>";
                $("#tbConcentradoBienEnte tbody").append(fila);
            }
            offLoading(null);
        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading(null);
        }
    } catch
        (e) {
        offLoading(null);
        Toast.fire({
            icon: "error",
            title: "Error al obtener los datos"
        });
    }
}

const getConcentradoBienesPorUnidadAdministrativa = async (idEntePublico, nombreEnte) => {

    $("#nombreEnteDiv").text(nombreEnte);

    $('#tbConcentradoBienUnidad tbody tr').slice().remove();
    const options = {
        method: "GET",
        header: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/censo-bien/concentradoBienesPorUnidadAdministrativa/'+idEntePublico, options);
        const respJSON = await res.json();

        if (res.ok) {

            for (i = 0; i < respJSON.listaConcentrado.length; i++) {
                var fila = "<tr>";
                fila = fila + "<td class='fw-bold'><i class='fas fa-circle fa-fw'></i> " + respJSON.listaConcentrado[i].nombre + "</td>";
                fila = fila + "<td class='fw-bold'><a href='#' onclick='getListaBienes("+ respJSON.listaConcentrado[i].id+")'>" + number_format(respJSON.listaConcentrado[i].total, 0) + "</a></td>";
                $("#tbConcentradoBienUnidad tbody").append(fila);

            }
            offLoading(null);
        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading(null);
        }
    } catch
        (e) {
        offLoading(null);
        Toast.fire({
            icon: "error",
            title: "Error al obtener los datos"
        });
    }
}

const getConcentradoBienesPorEstatus = async () => {

    $("#idCargaPe").text(0);
    $("#idCargaVe").text(0);
    $("#idCargaCe").text(0);
    $("#idCargaVa").text(0);
    $("#idCargaCa").text(0);

    const options = {
        method: "GET",
        header: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto + '/api/carga_masiva/concentradoBienesPorEstatus', options);
        const respJSON = await res.json();

        if (res.ok) {

            for (i = 0; i < respJSON.listaConcentrado.length; i++) {

                if (respJSON.listaConcentrado[i].nombre=="PE")
                    $("#idCargaPe").text(number_format(respJSON.listaConcentrado[i].total, 0));
                if (respJSON.listaConcentrado[i].nombre=="VE")
                    $("#idCargaVe").text(number_format(respJSON.listaConcentrado[i].total, 0));
                if (respJSON.listaConcentrado[i].nombre=="CE")
                    $("#idCargaCe").text(number_format(respJSON.listaConcentrado[i].total, 0));
                if (respJSON.listaConcentrado[i].nombre=="VA")
                    $("#idCargaVa").text(number_format(respJSON.listaConcentrado[i].total, 0));
                if (respJSON.listaConcentrado[i].nombre=="CA")
                    $("#idCargaCa").text(number_format(respJSON.listaConcentrado[i].total, 0));

            }
            offLoading(null);
        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading(null);
        }
    } catch
        (e) {
        offLoading(null);
        Toast.fire({
            icon: "error",
            title: "Error al obtener los datos"
        });
    }
}

function getConcentradoBienesEnteEstatus(estatus){
    $("#dlgModalcargaEnte").modal("show");

    const tableCargaBienesEnte = new DataTable('#tableCargaBienesEnte', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/carga_masiva/concentradoBienesPorEstatus/'+estatus,
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
function getListaBienes(idUnidad){
    $("#dlgModalListaBienesUnidad").modal("show");

    const tableBienesUnidad = new DataTable('#tableBienesUnidad', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/censo-bien/listaBienesPorUnidad/'+idUnidad,
            type: 'GET',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            complete: function (data) {
                $("#unidadText").text(data['responseJSON'].unidadAdministrativa);

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
