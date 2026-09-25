
const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let idLogoTemp=null;

$(document).ready(function() {

    obtenLogos();

    $("#idEntePublico").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large",
        dropdownParent: $("#dlgModalFormEntesPublico")
    });

    $("#idBtnUnidadAdmin").click(function () {
        $('#modal_arbol_unidad').modal('show');
    });

    $('#idEntePublico').on('change', function() {

        $('#idUnidadAdminAdministra').val("");
        $('#descUnidadAdminAdministra').val("");

        obtienearbolPorInstitucion(function (data) {
            $("#idUnidadAdminAdministra").val(data.id);
            $("#descUnidadAdminAdministra").val(data.text);
            limpiarBuscarArbol();
            $('#modal_arbol_unidad').modal('hide');
            $("#descUnidadAdminAdministra").removeClass( "is-valid" );
            $("#descUnidadAdminAdministra").removeClass( "is-invalid" );
        },$("#idEntePublico").val());
    });


    $("#txtBuscar").keyup(function () {
        busqueda($(this).val())
    });


    inicializarDependencias();

    $("#idNuevoLogo").click(function(){
        $("#rutaLogo").removeClass( "is-valid" );
        $("#rutaLogo").removeClass( "is-invalid" );
        $("#rutaLogo").val("");
        $(".custom-file-label").text("Seleccionar");

        $('#dlgModalFormLogo').modal('show');
    });

    $("#idFormLogo").validate({
        ignore: "",
        rules: {
            rutaLogo: "required"
        },
        messages: {
        rutaLogo: "Campo requerido"
        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            var n = element.attr("id");
            if (n == "rutaManual")
                element.closest('.form-x').append(error);
            else
                element.closest('.form-floating').append(error);
            error.addClass("invalid-feedback");

        },
        highlight: function (element, errorClass) {
            $(element).addClass("is-invalid").removeClass("is-valid");
        },
        unhighlight: function (element, errorClass) {
            $(element).addClass("is-valid").removeClass("is-invalid");
        },
        submitHandler: function (form, e) {
            e.preventDefault();
        }
    });

    $("#idFormLogo").submit(function (e) {
        e.preventDefault();
        if ($('#idFormLogo').valid()) {
            guardarLogo();
        }
    });

});

function obtenLogos(){

    var groupColumn = 2;

    const table = new DataTable('#tableLogos', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        paging: false,
        autoWidth: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/logo/listaLogos',
            type: 'GET',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
        },
        bProcessing: true,
        bServerSide:false,
        columnDefs: [{ visible: false, targets: groupColumn },
            {
                "targets": 1,
                "width":'250px',
                "className": "text-center",
            },
            {
                "targets": 3,
                "width":'80px',
                "className": "text-center",
            },{
                "targets": 4,
                "width":'500px',
                "className": "text-center",
            },{
                "targets": 5,
                "width":'70px',
                "className": "text-center",
            }],
        "drawCallback": function (settings) {
            var api = this.api();
            var rows = api.rows({ page: 'current' }).nodes();
            var last = null;

            api.column(groupColumn, { page: 'current' })
                .data()
                .each(function (group, i) {
                    if (last !== group) {
                        $(rows)
                            .eq(i)
                            .before('<tr class="group fw-bold"><td colspan="7" class="bg-info">' + group + '</td></tr>');
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

function guardarLogo(){

    let titleSave='Importar';
    let msgSave='¿Estas seguro que deseas importar el logo?';

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
            if ($('#rutaLogo').prop("files").length>0){
                var archivo = readFile();

                archivo.then(function (datos) {
                    guardarLogoAsync(datos);
                });


            }else{
                Toast.fire({
                    icon: "error",
                    title: "Seleccione el archivo"
                });
            }

        }
    });
}



const guardarLogoAsync = async (datos) =>{

    let nombre="";
    let contenido="";
    let ancho="";
    let alto="";

    if (datos!=null){
        nombre=datos.nombre;
        contenido=datos.contenido
        ancho=datos.ancho
        alto=datos.alto
    }

    const options={
        method: "POST",
        body: JSON.stringify({
            contenido:contenido,
            nombreLogo:nombre,
            ancho:ancho,
            alto:alto

        }),
        headers: headers
    };

    try {
        onLoading("dlgModalFormLogo");
        const res = await fetch(contexto+'/api/logo/importarLogo', options);
        const respJSON = await res.json();
        if (res.ok) {

            Toast.fire({
                icon: "success",
                title: "Logo importado correctamente"
            });

            offLoading("dlgModalFormLogo");
            $('#dlgModalFormLogo').modal('hide');
            obtenLogos();

        }else{
            offLoading("dlgModalFormLogo");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }

    }catch (e) {
        offLoading("dlgModalFormLogo");
        Toast.fire({
            icon: "error",
            title: "Error al importar"
        });
    }
};

function readFile() {
    return new Promise(function (resolve) {
        var envio = {};
        var dato_archivo = $('#rutaLogo').prop("files")[0];
        //console.log(dato_archivo);
        if(dato_archivo != undefined || dato_archivo.length > 0){
            var reader = new FileReader();
            reader.readAsBinaryString(dato_archivo);
            reader.onload = function () {
                envio = {
                    nombre: dato_archivo.name,
                    contenido: btoa(reader.result),
                    ancho:dato_archivo.width,
                    alto:dato_archivo.height,
                }
                resolve(envio)
            };
        }
    });
}

function deleteLogo(idLogo){

    Swal.fire({
        title: 'Eliminar Logo',
        text: '¿Estás seguro que deseas eliminar el logo?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then((result) => {
        if (result.isConfirmed) {

            $.ajax({
                url: contexto + '/api/logo/deleteLogo',
                type: 'DELETE',
                dataType: 'json',
                data: JSON.stringify({
                    idLogo: idLogo
                }),
                contentType: 'application/json',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                },
                success: function (response) {
                    obtenLogos();
                    Toast.fire({
                        icon: 'success',
                        title: "El logo se elimino correctamente"
                    });
                },
                error: function (response) {
                    var error =  JSON.parse(response.responseText);
                    Toast.fire({
                        icon: 'error',
                        title: error.message
                    });

                }
            });
        }

    });
}

const inicializarDependencias = async () =>{
    const options={
        method: "POST",
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/entePublico/listaFree', options);
        const respJSON = await res.json();

        llenarCombo('#idEntePublico', 'idEntePublico','nombreEntePublicoConCve', respJSON);


        offLoading(null);
    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
};

function asignarLogoModal(idLogo){
    idLogoTemp=idLogo;
    $("#idEntePublico").val("");
    $("#dlgModalFormEntesPublico").modal("show");

}

function asignarLogo(){


        Swal.fire({
            title: 'Asinar Logo',
            text: '¿Estás seguro que deseas asignar el logo?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Si',
            cancelButtonText: "No"
        }).then((result) => {
            if (result.isConfirmed) {

                $.ajax({
                    url: contexto + '/api/logo/asignarLogo',
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify({
                        idUnidadAdministrativa:$("#idUnidadAdminAdministra").val(),
                        idLogo: idLogoTemp
                    }),
                    contentType: 'application/json',
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                    },
                    success: function (response) {
                        obtenLogos();
                        Toast.fire({
                            icon: 'success',
                            title: "El logo se asigno correctamente"
                        });
                        $("#dlgModalFormEntesPublico").modal("hide");
                    },
                    error: function (response) {
                        var error =  JSON.parse(response.responseText);
                        Toast.fire({
                            icon: 'error',
                            title: error.message
                        });

                    }
                });
            }

        });

}