var contexto=$("#contextSigtic").val();


let tableCartaResponsivaDetalle=null;

$(document).ready(function () {
    obtieneCartasResponsivaDetalle($("#idCartaResponsiva").val());

});

function obtieneCartasResponsivaDetalle(idCartaResponsiva) {

    var formulario={
        idCartaResponsiva:idCartaResponsiva
    };

    if (tableCartaResponsivaDetalle!=null)
        tableCartaResponsivaDetalle.clear();

    tableCartaResponsivaDetalle = new DataTable('#tableCartaResponsivaDetalle', {
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
