$(document).ready(function(){
    obtieneRoles();

    $('#idNuevoRol').click(function () {
        limpiarFormPerfil();
        $('#titleFormRol').text("Nuevo Rol");

        $('#dlgModalFormRol').modal('show');

    });


    $( "#idFormRol" ).validate( {
        ignore: "",
        rules: {
            nombreRol: "required"
            ,descRol: "required"
        },
        messages: {
            nombreRol: "Nombre de rol Requerido"
            ,descRol: "Descripción del rol Requerido"
        },
        errorElement: "em",
        errorPlacement: function (error, element) {
            error.insertAfter(element);
            error.addClass( "invalid-feedback" );
        },
        highlight: function (element, errorClass) {
            $( element ).addClass( "is-invalid" ).removeClass( "is-valid" );
        },
        unhighlight: function (element, errorClass) {
            $( element ).addClass( "is-valid" ).removeClass( "is-invalid" );
        }
    } );


    $("#idFormRol").submit(function(e) {
        e.preventDefault();
        if($('#idFormRol').valid()) {
                guardarRol();
            return true;
        }
        else
            return false;
    });

    $('#todoMenu').change(function() {
        if ($('#todoMenu').is(":checked")) {
            $("#treeMenu").jstree().check_all(true);
        }
        else {
            $("#treeMenu").jstree().uncheck_all(true);
        }
    });

});

function limpiarFormPerfil() {
    $("#nombreRol").removeClass( "is-valid" );
    $("#nombreRol").removeClass( "is-invalid" );
    $("#descRol").removeClass( "is-valid" );
    $("#descRol").removeClass( "is-invalid" );
    $("#idRol").val("");
    $('#nombreRol').val("");
    $("#descRol").val("");
}


function obtieneRoles(){

   const table = new DataTable('#tableSegRoles', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        ajax: {
            url: contexto+'/api/rol/tabla',
            type: 'POST',
             beforeSend: function(xhr) {
               xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            }
        },
        bProcessing: true,
        bServerSide:false,
       language:{
           "url": contexto+'/include/datatables/spanish.json',
       },
        columnDefs: [
            {
                "width": "60px",
                "targets": 2,
                "className": "text-center",
            },
            {
                "width": "50px",
                "targets": 3,
                "className": "text-center",
            },
            {
                "width": "150px",
                "targets": 4,
                "className": "text-center",
            }],
        lengthChange: false,
        select: false
    });

}


function guardarRol(){

    let tipo="PUT";
    let msg="¿Estás seguro que deseas actualizar el rol?";
    let msgRespuesta="El Rol se actualizó correctamente";
    if ($('#idRol').val() == "") {
        msg = "¿Estás seguro que deseas guardar el rol?";
        msgRespuesta="El Rol se guardo correctamente";
        tipo="POST";
    }


    Swal.fire({
        title: 'Rol',
        text: msg,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function (result) {
        if (result.isConfirmed) {

            $.ajax({
                url: contexto + '/api/rol',
                type: tipo,
                dataType: 'json',
                data: JSON.stringify({
                    idPerfil: $("#idRol").val(),
                    nombrePerfil: $("#nombreRol").val(),
                    descPerfil: $("#descRol").val()
                }),
                contentType: 'application/json',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                },
                success: function (response) {
                    obtieneRoles();
                    //$('#idRol').val(response.idPerfil)
                    //$('#titleFormPerfil').text("Actualizar Rol");

                    Toast.fire({
                        icon: 'success',
                        title: msgRespuesta
                    });

                    $("#dlgModalFormRol").modal("hide");

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

function editarRol(idRol){
    limpiarFormPerfil();
    $('#titleFormRol').text("Actualizar Rol");
    $.ajax({ url: contexto+'/api/rol/'+idRol,
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function(response) {
            $("#idRol").val(response.idPerfil);
            $("#nombreRol").val(response.nombrePerfil);
            $("#descRol").val(response.descPerfil);

            $('#dlgModalFormRol').modal('show');
        },
        error: function (response) {
        }
    });
}

function eliminarRol(idRol){

    Swal.fire({
        title: 'Eliminar Rol',
        text: '¿Estás seguro que deseas eliminar el rol?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then((result) => {
        if (result.isConfirmed) {

            $.ajax({
                url: contexto + '/api/rol',
                type: 'DELETE',
                dataType: 'json',
                data: JSON.stringify({
                    idPerfil: idRol
                }),
                contentType: 'application/json',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                },
                success: function (response) {
                    obtieneRoles();
                    Toast.fire({
                        icon: 'success',
                        title: "El Rol se elimino correctamente"
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

function modalUsuariosRol(idRol){
    obtieneUsuariosRol(idRol);
    $('#dlgModalUsuariosRol').modal('show');
}

function obtieneUsuariosRol(idRol){
    var groupColumn = 0;

    $('#tableUsuariosRol').DataTable({
        sPaginationType: "full_numbers",
        bDestroy: true,
        ajax: {
            url: contexto+'/api/rolUsuario/tablaUsuariosRol',
            type: 'POST',
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            data: function (d) {
                return JSON.stringify($.extend( {"idPerfil":idRol}, d, {}));
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [{ visible: false, targets: groupColumn }],
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
                            .before('<tr class="group fw-bold"><td colspan="4" class="bg-info">' + group + '</td></tr>');

                        last = group;
                    }
                });
        },
        lengthChange: false,
        select: false
    });
}

function modalPermisosAsignados(idRol){
    $('#idPerfilSeleccionado').val(idRol);
    obtienePerfilesPermisoMenu(idRol)
    $('#dlgModalPermisos').modal('show');
}

function obtienePerfilesPermisoMenu(idRol){

    $('#tableSegPermisoSistema').DataTable({
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering: false,
        bSort: false,
        paging: false,
        searching: false,
        ajax: {
            url: contexto+'/api/perfilPrivilegio/tabla',
            type: 'POST',
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            'data': function (d) {
                return JSON.stringify($.extend( {"idPerfil":idRol}, d, {}));
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
        "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [[
            {
                "targets": 1,
                "width": "50px",
                "className": "text-center"
            },
            {
                "width": "50px",
                "targets": 2,
                "className": "text-center",
            },
            {
                "width": "50px",
                "targets": 3,
                "className": "text-center",
            },
            {
                "targets": 4,
                "width": "50px",
                "className": "text-center"
            }]],
        lengthChange: false,
        select: false
    });
}

function menuPorPerfil(){

    $('#dlgModalPermisos').modal('hide');
    $('#dlgModalPermisosTodo').modal('show');


    $("#treeMenu").jstree("destroy");
    $('#treeMenu').jstree({
        'core' : {
            'data' : {
                'async': true,
                'type': "POST",
                'url' : contexto+'/api/menu/menuArbol',
                'beforeSend': function(xhr) {
                    xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
                },
                'data' :  function (node) {
                    return JSON.stringify({idPerfil :  $('#idPerfilSeleccionado').val()});
                },
                'contentType': 'application/json'
            }
        },
        "types" : {
            "default": {
                "icon": "fa fa-lock"
            }
        },
        'checkbox': {
            three_state: true,
            cascade: 'up'
        },
        'plugins': ["types","checkbox"]
    })
}

function getTreeMenu(){
    var menu = []
    var selectedElms = $('#treeMenu').jstree("get_selected", true);
    $.each(selectedElms, function() {
        menu.push(this.original);
    });
    return menu;
}

function guardarPrivilegiosMenu(){
    var menu = getTreeMenu();

    $.ajax({
        url: contexto +'/api/perfilPrivilegio',
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify({
            idPerfil :  $('#idPerfilSeleccionado').val(),
            menu: menu
        }),
        contentType: 'application/json',
        'beforeSend': function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function(response) {
                if( response !== null){
                    Toast.fire({
                        icon: 'success',
                        title: "Permisos asignados correctamente"
                    });
                    obtienePerfilesPermisoMenu( $('#idPerfilSeleccionado').val());
                    obtieneRoles();
                }

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


function checkboxPrivilegio(tipo, idPerfilPrivilegio){

    $.ajax({ url: contexto+'/api/perfilPrivilegio?idPerfilPrivilegio='+idPerfilPrivilegio+"&tipo="+tipo+"&valor="+$("#"+tipo+""+idPerfilPrivilegio).prop('checked'),
        type: 'PUT',
        dataType: 'JSON',
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        /*data: JSON.stringify({
            idPerfil :  $('#idPerfilSeleccionado').val(),
            menu: menu
        }),*/
        success: function(response) {
                if( response !== null){
                    Toast.fire({
                        icon: 'success',
                        title: "Permiso actualizado correctamente"
                    });
                }
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