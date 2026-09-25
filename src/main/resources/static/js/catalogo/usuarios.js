const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

let tableUSer=null;
let tableEnteUSer=null;

$(document).ready(function(){

    jQuery.event.special.mousewheel = {
        setup: function( _, ns, handle ){
            this.addEventListener("mousewheel", handle, { passive: true });
        }
    };

    $("#btnExportar").click(function () {
        exportarUsuarios();
    });


    $("#idBtnSearchTodo").click(function () {

        $("#rfcSearch").val("");
        $("#curpSearch").val("");
        $("#nombreSearch").val("");
        $("#segundoNombreSearch").val("");
        $("#apellidoPaternoSearch").val("");
        $("#apellidoMaternoSearch").val("");

        if ( $('#idVigente').prop('checked')==false && $('#idHistorico').prop('checked')==false && $('#idNoVigente').prop('checked')==false){
            $('#idVigente').prop('checked',true);
            $('#idHistorico').prop('checked',true);
        }

            obtieneUsuarios();
    });

    $("#formBusquedaUsuario").submit(function(e) {
        e.preventDefault();
        $("#idCardTablaUsuario").hide();
        $("#rfcSearch").val($("#rfcSearch").val().trim());
        $("#curpSearch").val($("#curpSearch").val().trim());
        $("#nombreSearch").val($("#nombreSearch").val().trim());
        $("#segundoNombreSearch").val($("#segundoNombreSearch").val().trim());
        $("#apellidoPaternoSearch").val($("#apellidoPaternoSearch").val().trim());
        $("#apellidoMaternoSearch").val($("#apellidoMaternoSearch").val().trim());

        if ($("#rfcSearch").val().trim()=="" && $("#curpSearch").val().trim()=="" && $("#nombreSearch").val().trim()=="" && $("#segundoNombreSearch").val().trim()==""
            && $("#apellidoPaternoSearch").val().trim()=="" && $("#apellidoMaternoSearch").val().trim()=="")
            Toast.fire({
                icon: 'warning',
                //title: 'Validar',
                html: "Se requiere por lo menos un campo de búsqueda"
            });
        else
            if ( $('#idVigente').prop('checked')==false && $('#idHistorico').prop('checked')==false && $('#idNoVigente').prop('checked')==false)
                Toast.fire({
                    icon: 'warning',
                    //title: 'Validar',
                    html: "Se requiere por lo menos un estatus de búsqueda"
                });
            else
                obtieneUsuarios();
        return true;
    });

    $("#idNuevoUsuario").click(function () {
        limpiarForm();
        $('#dlgModalFormNuevoCatUsuario').modal('show');
    });

    $("#btnNewUsuario").click(function () {
        limpiarForm();
    });

    $("#idBtnLimpiar").click(function () {
        $("#rfcSearch").val("");
        $("#curpSearch").val("");
        $("#nombreSearch").val("");
        $("#segundoNombreSearch").val("");
        $("#apellidoPaternoSearch").val("");
        $("#apellidoMaternoSearch").val("");
        $("#idVigente").val("VIG");
        $('#idVigente').prop('checked',true);
        $('#idHistorico').prop('checked',true);

    });


    $("#formCatUsuario").validate( {
        ignore: "",
        rules: {
            rfcCat: { required:true, minlength: 10, maxlength: 13},
            curpCat: { required:true, minlength: 18, maxlength: 18},
            nombreCat:"required",
            apellidoPaternoCat:{
                required: function () {
                    if ($("#apellidoPaternoCat").val().trim()=="" && $("#apellidoMaternoCat").val().trim()=="")
                        return true;
                    else{
                        $("#apellidoPaternoCat").removeClass( "is-invalid" )
                        $("#apellidoPaternoCat").addClass( "is-valid" );
                        $("#apellidoMaternoCat").removeClass( "is-invalid" )
                        $("#apellidoMaternoCat").addClass( "is-valid" );
                        return false;
                    }
                }
            },
            apellidoMaternoCat:{
                required: function () {
                    if ($("#apellidoPaternoCat").val().trim()=="" && $("#apellidoMaternoCat").val().trim()=="")
                        return true;
                    else{
                        return false;
                        $("#apellidoPaternoCat").removeClass( "is-invalid" )
                        $("#apellidoPaternoCat").addClass( "is-valid" );
                        $("#apellidoMaternoCat").removeClass( "is-invalid" )
                        $("#apellidoMaternoCat").addClass( "is-valid" );
                    }
                }
            },
            fechaNacimientoCat:"required",
            numCelularCat: {minlength: 10, maxlength: 13},
            numTelefonoPersonalCat: {minlength: 10, maxlength: 13},
            correoPersonalCat: {email: true}
        },
        messages: {
            rfcCat:"RFC Valido requerido",
            curpCat:"CURP Valido requerido",
            nombreCat:"Nombre Requerido",
            apellidoPaternoCat:"Apellido Paterno o Materno Requerido",
            apellidoMaternoCat:"Apellido Paterno o Materno Requerido",
            fechaNacimientoCat:"Fecha de nacimiento requerida",
            numCelularCat:"Número de Celular Invalido",
            numTelefonoPersonalCat:"Número de Teléfono Invalido",
            correoPersonalCat:"Correo Invalido"
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
    } )

    $("#formCatUsuario").submit(function(e) {
        e.preventDefault();
        if ($('#formCatUsuario').valid()) {
            guardarCatUsuario();
            return true;
        }
    });

});


function limpiarForm(){
    $("#titleFormHeaderCatUsuario").text("Nuevo Usuario");
    $("#idUSuarioCat").val("");
    $("#rfcCat").val("");
    $("#curpCat").val("");
    $("#nombreCat").val("");
    $("#segundoNombreCat").val("");
    $("#apellidoPaternoCat").val("");
    $("#apellidoMaternoCat").val("");
    $("#cveSexoFemeninoCat").prop("checked", true);
    $("#fechaNacimientoCat").val("");
    $("#numCelularCat").val("");
    $("#numTelefonoPersonalCat").val("");
    $("#correoPersonalCat").val("");
    $("#cveEstatus").val("VIG");

    $("#rfcCat").removeClass( "is-valid" );
    $("#rfcCat").removeClass( "is-invalid" );
    $("#curpCat").removeClass( "is-valid" );
    $("#curpCat").removeClass( "is-invalid" );
    $("#nombreCat").removeClass( "is-valid" );
    $("#nombreCat").removeClass( "is-invalid" );
    $("#apellidoPaternoCat").removeClass( "is-valid" );
    $("#apellidoPaternoCat").removeClass( "is-invalid" );
    $("#apellidoMaternoCat").removeClass( "is-valid" );
    $("#apellidoMaternoCat").removeClass( "is-invalid" );
    $("#fechaNacimientoCat").removeClass( "is-valid" );
    $("#fechaNacimientoCat").removeClass( "is-invalid" );

    $("#numCelularCat").removeClass( "is-valid" );
    $("#numCelularCat").removeClass( "is-invalid" );
    $("#numTelefonoPersonalCat").removeClass( "is-valid" );
    $("#numTelefonoPersonalCat").removeClass( "is-invalid" );
    $("#correoPersonalCat").removeClass( "is-valid" );
    $("#correoPersonalCat").removeClass( "is-invalid" );
}

function obtieneUsuarios() {

    var formulario={
        "isVigente":$("#idVigente").is(':checked'),
        "isHistorico":$("#idHistorico").is(':checked'),
        "isNoVigente":$("#idNoVigente").is(':checked'),
        "rfc": $("#rfcSearch").val(),
        "curp": $("#curpSearch").val(),
        "nombre": $("#nombreSearch").val(),
        "segundoNombre": $("#segundoNombreSearch").val(),
        "apellidoPaterno": $("#apellidoPaternoSearch").val(),
        "apellidoMaterno": $("#apellidoMaternoSearch").val()
    };

    if (tableUSer!=null)
        tableUSer.clear();

    onLoading(null);
    tableUSer = new DataTable('#tableUsuario', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: true,
        ajax: {
            url: contexto+'/api/catalogoCatUsuario/tablaUsuario',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend(formulario, d, {}));
            },
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
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
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
            }],
        lengthChange: false,
        select: false
    });

}


const guardarCatUsuarioAsync = async () =>{
/*    alert($("#fechaNacimientoCat").val());
    let dateNacimiento = $("#fechaNacimientoCat").val().split("-");
    let fechaNac=dateNacimiento[2] + "/" + dateNacimiento[1] + "/" + dateNacimiento[0];
    alert(fechaNac);*/

    const options={
        method: "POST",
        body: JSON.stringify({
            idUsuario: $("#idUSuarioCat").val(),
            rfc: $("#rfcCat").val(),
            curp: $("#curpCat").val(),
            nombre: $("#nombreCat").val(),
            segundoNombre: $("#segundoNombreCat").val(),
            apellidoPaterno: $("#apellidoPaternoCat").val(),
            apellidoMaterno: $("#apellidoMaternoCat").val(),
            //fechaNacimiento: fechaNac,
            fechaNacimiento: $("#fechaNacimientoCat").val(),
            cveSexo: $("#cveSexoFemeninoCat").is(":checked")?"F":"M",
            numCelular: $("#numCelularCat").val(),
            numTelefonoPersonal: $("#numTelefonoPersonalCat").val(),
            correoPersonal: $("#correoPersonalCat").val(),
            cveEstatus: $("#cveEstatus").val()
        }),
        headers: headers
    };

    try {
        onLoading("dlgModalFormNuevoCatUsuario");
        const res = await fetch(contexto+'/api/catalogoCatUsuario/guardarCatUsuario', options);
        const respJSON = await res.json();
        if (res.ok) {

            offLoading("dlgModalFormNuevoCatUsuario");
            //$('#dlgModalFormNuevoCatUsuario').modal('hide');

            Toast.fire({
                icon: "success",
                title: "Los datos del usuario se guardaron correctamente"
            });

          /*  $("#titleFormHeaderCatUsuario").text("Editar Usuario");
            $("#idUSuarioCat").val(respJSON.idUsuario);*/
            $('#dlgModalFormNuevoCatUsuario').modal('hide');

            $("#rfcSearch").val(respJSON.rfc);
            $("#curpSearch").val(respJSON.curp);
            $('#idVigente').prop('checked',true);
            $('#idHistorico').prop('checked',true);
            obtieneUsuarios();


        }else{
            offLoading("dlgModalFormNuevoCatUsuario");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }

    }catch (e) {
        offLoading("dlgModalFormNuevoCatUsuario");
        Toast.fire({
            icon: "error",
            title: "Error al guardar"
        });
    }
};


function guardarCatUsuario(){

    let titleSave='¿Guardar Usuario?';
    let msgSave='¿Estas seguro que deseas guardar los datos del usuario?';


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
            guardarCatUsuarioAsync();
        }
    });
}

function editarUsuario(idUsuario){

    $('#preloader').modal('show');
    $.ajax({
        url: contexto + '/api/catalogoCatUsuario/getUsuario/' + idUsuario,
        type: 'GET',
        dataType: 'JSON',
        contentType: 'application/json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function (response) {

           $("#titleFormHeaderCatUsuario").text("Editar Usuario");

            var catUsuario = response;

            $("#idUSuarioCat").val(catUsuario.idUsuario);
            $("#rfcCat").val(catUsuario.rfc);
            $("#curpCat").val(catUsuario.curp);
            $("#nombreCat").val(catUsuario.nombre);
            $("#segundoNombreCat").val(catUsuario.segundoNombre);
            $("#apellidoPaternoCat").val(catUsuario.apellidoPaterno);
            $("#apellidoMaternoCat").val(catUsuario.apellidoMaterno);

            if (catUsuario.cveSexo=="F")
                $("#cveSexoFemeninoCat").prop("checked", true);
            else
                $("#cveSexoMasculinoCat").prop("checked", true);

            if (catUsuario.fechaNacimientoText!=null) {
                var date1 = catUsuario.fechaNacimientoText.split("/");
                $("#fechaNacimientoCat").val(date1[2] + "-" + date1[1] + "-" + date1[0]);
            }

            $("#correoPersonalCat").val(catUsuario.correoPersonal);
            $("#numCelularCat").val(catUsuario.numCelular);
            $("#numTelefonoPersonalCat").val(catUsuario.numTelefonoPersonal);
            $("#cveEstatus").val(catUsuario.cveEstatus);
            $('#preloader').modal('hide');
            $('#dlgModalFormNuevoCatUsuario').modal('show');

        },
        error: function (response) {
            var error = JSON.parse(response.responseText);
            $('#preloader').modal('hide');
            Swal.fire({
                title: 'Error',
                text: error.message,
                icon: 'error',
            });
        }
    });

}

function eliminarUsuario(idUsuario){

    Swal.fire({
        title: 'Eliminar Usuario',
        text: '¿Estás seguro que deseas eliminar el usuario?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No",
        reverseButtons: true,
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminaUsuario(idUsuario);
        }});
}

function eliminaUsuario(idUsuario) {
    $('#preloader').modal('show');
    $.ajax({
        url: contexto+'/api/catalogoCatUsuario/deleteUsuario',
        type: 'DELETE',
        contentType: 'application/json',
        dataType: 'JSON',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        data: JSON.stringify({
            idUsuario: idUsuario,
        }),
        success: function (response) {
                $('#preloader').modal('hide');
                if( response !== null){

                    obtieneUsuarios();

                    Toast.fire({
                        icon: "success",
                        title: "El usuario se eliminó correctamente"
                    });

                }
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

function obtieneEntesPublicosPorUsuarios(idUsuario) {


    if (tableEnteUSer!=null)
        tableEnteUSer.clear();

    onLoading(null);
    tableEnteUSer = new DataTable('#tableEnteUsuario', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        //paging: false,
        ordering: false,
        ajax: {
            url: contexto+'/api/catalogoCatUsuario/tablaUsuarioEntePublico',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({
                    "idUsuario": idUsuario
                }, d, {}));
            },
            complete: function (data) {
                offLoading(null);
                var total=data['responseJSON'].iTotalDisplayRecords;

                if (total>0){
                    $('#dlgModalEntePublicoPorUsuario').modal('show');
                }
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [{
            "targets": 0,
            "width": "20%",
            "className": "text-center",
        },{
            "targets": 2,
            "width": "10%",
            "className": "text-center",
        }],
        lengthChange: false,
        select: false
    })

}
function exportarUsuarios() {

    const formulario = {
        isVigente: $("#idVigente").is(':checked'),
        isHistorico: $("#idHistorico").is(':checked'),
        isNoVigente: $("#idNoVigente").is(':checked'),
        rfc: $("#rfcSearch").val(),
        curp: $("#curpSearch").val(),
        nombre: $("#nombreSearch").val(),
        segundoNombre: $("#segundoNombreSearch").val(),
        apellidoPaterno: $("#apellidoPaternoSearch").val(),
        apellidoMaterno: $("#apellidoMaternoSearch").val()
    };

    // Deshabilita el botón y muestra el loading
    $("#btnExportar").prop("disabled", true);
    onLoading(null);

    fetch(contexto + "/api/catalogoCatUsuario/exportarUsuarios", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-Token": $('input[name="_csrf"]').val()
        },
        body: JSON.stringify(formulario)
    })
        .then(response => {

            if (!response.ok) {
                throw new Error("Error al generar el archivo.");
            }

            // Obtiene el nombre que envía Spring
            const disposition = response.headers.get("Content-Disposition");
            let fileName = "cat_usuarios.xlsx";

            if (disposition && disposition.indexOf("filename=") !== -1) {
                fileName = disposition
                    .split("filename=")[1]
                    .replace(/"/g, "");
            }

            return response.blob().then(blob => ({
                blob,
                fileName
            }));
        })
        .then(data => {

            const url = window.URL.createObjectURL(data.blob);

            const a = document.createElement("a");
            a.href = url;
            a.download = data.fileName;

            document.body.appendChild(a);
            a.click();
            a.remove();

            window.URL.revokeObjectURL(url);

            offLoading(null);
            $("#btnExportar").prop("disabled", false);

            Toast.fire({
                icon: "success",
                title: "La exportación se realizó correctamente."
            });

        })
        .catch(error => {

            offLoading(null);
            $("#btnExportar").prop("disabled", false);

            Toast.fire({
                icon: "error",
                title: error.message
            });

            console.error(error);
        });

}