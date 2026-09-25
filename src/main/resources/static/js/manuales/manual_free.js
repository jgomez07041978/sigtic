var contexto=$("#contextSigtic").val();

const Toast = Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 4000,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
});

$(document).ready(function () {
    obtenManualesFree();
});

const obtenManualesFree = async() => {
    const options={
        method: "GET",
        header:{
            "X-CSRF-Token": $('input[name="_csrf"]').val(),
            "Content-type": "application/json; charset=UTF-8"
        }
    };

    let lista="";
    try {

        const res = await fetch(contexto + "/api/manual/listaManualesFree", options);
        const respJSON = await res.json();

        if (res.ok) {
            respJSON.listaGrupo.forEach((elemento) => {
                console.log("===========================");
                console.log(elemento);
                lista += "<div class=\"card\">";
                lista += "<div class=\"card-body\">";
                lista += "<h5 class=\"card-title fw-bold\">"+elemento.nombreGrupo+"</h5>";
                let listaHijo=  respJSON.listaManual.filter(manual=>{
                    return manual.grupoManual.idGrupoManual===elemento.idGrupoManual
                });

                lista+="<ol class=\"list-group list-group-numbered list-group-flush\">";

                listaHijo.forEach(hijo=>{
                    lista += "<li class=\"list-group-item d-flex justify-content-between align-items-start\">";
                    lista += " <div class=\"ms-2 me-auto\">";
                    lista += " <div class=\"fw-bold\">"+hijo.nombreManual+"</div>"+hijo.descripcionManual;
                    lista += "<p><a href=\""+contexto+"/api/manual/exportManual/"+hijo.idManual+"\" target='_blank' class=\"card-link fw-bold\"><i class=\"fa-solid fa-file-pdf text-danger\"></i> Descarga</a></p>";
                    lista += " </div>";
                    lista += " </li>";
                });
                lista+="</ol>";


                lista += "</div>";
                lista += "</div>";
            });

            $("#idManuales").append(lista);



            /* respJSON.listaConcentrado.forEach({

             });*/


        } else {
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });

        }
    } catch (e) {

        Toast.fire({
            icon: "error",
            title: "Error al obtener los manuales"
        });
    }





}