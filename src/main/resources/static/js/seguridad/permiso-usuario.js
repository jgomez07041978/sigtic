const  headers= {
    "X-CSRF-Token":  $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

var id = "";
var idEntePublico = null;
var id2 = "";

$(document).ready(function() {

    jQuery.event.special.mousewheel = {
        setup: function( _, ns, handle ){
            this.addEventListener("mousewheel", handle, { passive: true });
        }
    };

    $("#idEntePublicoSearch").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--large",
        selectionCssClass: "select2--large",
        dropdownCssClass: "select2--large"
    });
    $("#idEntePublicoSearch2").select2({
        width: '100%',
        theme: 'bootstrap-5',
        containerCssClass: "select2--small",
        selectionCssClass: "select2--small",
        dropdownCssClass: "select2--small",
        dropdownParent: $("#dlgModalAsignarInstitucion")
    });

    inicializarDependencias();

    if ($("#idEntePublicoSession").val()!=""){
        getTabla($("#idEntePublicoSession").val());
        obtienearbolPorInstitucion(function (data) {
            $("#idEntePublico").val(data.idEntePublico);
            $("#descEntePublico").val(data.descEntePublico);
            $("#idUnidadAdmtva").val(data.id);
            $("#descUnidadAdmtva").val(data.text);
            limpiarBuscarArbol();
            $('#modal_arbol_unidad').modal('hide');
            $("#descEntePublico").removeClass( "is-valid" );
            $("#descEntePublico").removeClass( "is-invalid" );
            $("#descUnidadAdmtva").removeClass( "is-valid" );
            $("#descUnidadAdmtva").removeClass( "is-invalid" );
        },$("#idEntePublicoSession").val());

        obtienearbolPorInstitucion2(function (data) {},$("#idEntePublicoSession").val());

    }
    else
        getTabla(0);

    $('#idEntePublicoSearch').on('change', function() {
        if ($("#idEntePublicoSearch").val()!="")
            getTabla($("#idEntePublicoSearch").val());


        obtienearbolPorInstitucion(function (data) {
            $("#idEntePublico").val(data.idEntePublico);
            $("#descEntePublico").val(data.descEntePublico);
            $("#idUnidadAdmtva").val(data.id);
            $("#descUnidadAdmtva").val(data.text);
            limpiarBuscarArbol();
            $('#modal_arbol_unidad').modal('hide');
            $("#descEntePublico").removeClass( "is-valid" );
            $("#descEntePublico").removeClass( "is-invalid" );
            $("#descUnidadAdmtva").removeClass( "is-valid" );
            $("#descUnidadAdmtva").removeClass( "is-invalid" );
        },$("#idEntePublicoSearch").val());
    });

    $('#idEntePublicoSearch2').on('change', function() {
        if ($("#idEntePublicoSearch2").val()!="")

        obtienearbolPorInstitucion2(function (data) {},$("#idEntePublicoSearch2").val());
    });

    $("#idNuevoUsuario").click(function () {

        $("#opcion").val("Nuevo");
        $("#changePassword").prop('checked',false);
        $("#idChangePassword").hide();
        $("#contraseniaSistema").removeClass("bg-secondary-subtle");
        $("#contraseniaRepite").removeClass("bg-secondary-subtle");
        $("#contraseniaSistema").prop('readonly', false);
        $("#contraseniaRepite").prop('readonly', false);

        limpiarFormularioUsuario();

        $('#titleFormUsuario').text("Nuevo Usuario");
        $("#idEntePublico").val($("#idEntePublicoSearch").val())
        $("#descEntePublico").val($("#idEntePublicoSearch option:selected").html())
        $('#dlgModalFormUsuario').modal('show')
    });

    $("#btnOpenArbol").click(function () {
        $('#modal_arbol_unidad').modal('show');
    });

    $("#txtBuscar").keyup(function () {
        busqueda($(this).val())
    });

    $("#txtBuscar2").keyup(function () {
        busqueda2($(this).val())
    });

    $("#txtBuscar3").keyup(function () {
        busqueda3($(this).val())
    });

    $.validator.addMethod("formValContrasenia", function (value, element) {
        if ($('#idUsuario').val()!="" && $("#changePassword").prop('checked')==false && $('#contraseniaSistema').val()=="")
            return true;
        else
        if ($('#idUsuario').val()!="" && $("#changePassword").prop('checked') && $('#contraseniaSistema').val()=="") {
            $.validator.messages["formValContrasenia"] = "Teclee la contraseña";
            return false;
        }
        else
        if ($('#idUsuario').val()!="" && $("#changePassword").prop('checked') && $('#contraseniaSistema').val()!=$('#contraseniaRepite').val())
        {
            $.validator.messages["formValContrasenia"] = "Las contraseñas son diferentes";
            return false;
        }else
            if ($('#idUsuario').val()=="" && $('#contraseniaSistema').val()=="") {
                $.validator.messages["formValContrasenia"] = "Contraseña Requerida";
                return false;

            }else
            if ($('#idUsuario').val()=="" && $('#contraseniaSistema').val()!= $('#contraseniaRepite').val()) {
                $.validator.messages["formValContrasenia"] = "Las contraseñas son diferentes";
                return false;
            }else
                return true


    }, "");


    $.validator.addMethod('regexpp', function (value) {
        /*Para validar espacios*/

        if ($("#opcion").val()=="Nuevo"){
            let noValido = /\s/;

            if (noValido.test(value))
                return false;
            else
                return /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$^&*()_-]).{12,18}$/.test(value);
        }else {
            if ($('#idUsuario').val() != "" && $("#changePassword").prop('checked') == false)
                return true
            else {

                let noValido = /\s/;

                if (noValido.test(value))
                    return false;
                else
                    return /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$^&*()_-]).{12,18}$/.test(value);
            }
        }
    }, 'Contraseña no valida');


    $( "#formUsuario" ).validate( {
        rules: {
            descEntePublico:"required",
            descUnidadAdmtva:"required",
            nombreUsuario: "required",
            cveEstatus: "required",
                        contraseniaSistema:{regexpp:true},
                        contraseniaRepite: {regexpp:true}
        },
        messages: {
            descEntePublico: "Selecciona la institución",
            descUnidadAdmtva: "Selecciona la unidad administrativa",
            nombreUsuario: "Selecciona el usuario",
            cveEstatus: "Seleccione el estatus",
                        contraseniaSistema: {},
                        contraseniaRepite:{}
        },
        errorElement: "em",
        errorPlacement: function (error, element) {
            error.insertAfter(element);
            error.addClass( "invalid-feedback" );;
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

    $("#formUsuario").submit(function(e) {
        e.preventDefault();
        if($('#formUsuario').valid()) {
            if ($("#contraseniaSistema").val()==$("#contraseniaRepite").val()) {
                guardar();
                return true;
            }else
                {
                    Toast.fire({
                        icon: "error",
                        title: "El password nuevo y repetir password no coninciden"
                    });
                }

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
        llenarCombo('#idEntePublicoSearch2', 'idEntePublico','nombreEntePublicoConCve', respJSON);
        $("#idEntePublicoSearch").val($("#idEntePublicoSession").val());
        $("#idEntePublicoSearch2").val($("#idEntePublicoSession").val());

        offLoading(null);
    }catch (e) {
        offLoading(null);
        Toast.fire({
            icon: 'error',
            title: "Error al cargar los datos"
        });
    }
};


const getUsuarioId = async (idUsuarioEntePublico) => {

    const options={
        method: "POST",
        body:  JSON.stringify({idUsuarioEntePublico:idUsuarioEntePublico}),
        headers: headers
    };

    try {
        onLoading("dlgModalFormUsuario");
        const res = await fetch(contexto+'/api/seguridad/usuario', options);
        const respJSON = await res.json();
        if (res.ok) {

            $("#idUsuario").val(respJSON.idUsuario);
            $("#idUsuarioEntePublico").val(respJSON.idUsuarioEntePublico);
            $("#cveEstatus").val(respJSON.cveEstatus);

            if (respJSON.catUsuarioEntePublico != null) {
                $("#idEntePublico").val(respJSON.catUsuarioEntePublico.entePublico.idEntePublico);
                $("#descEntePublico").val(respJSON.catUsuarioEntePublico.entePublico.nombreEntePublico);

                $("#nombreUsuario").val(respJSON.catUsuarioEntePublico.catUsuario.rfc + ") " + respJSON.catUsuarioEntePublico.catUsuario.nombreCompleto);
            }

            if (respJSON.catUsuarioEntePublico.unidadAdministrativa != null) {
                $("#idUnidadAdmtva").val(respJSON.catUsuarioEntePublico.unidadAdministrativa.idUnidadAdministrativa);
                $("#descUnidadAdmtva").val(respJSON.catUsuarioEntePublico.unidadAdministrativa.nombreUnidadAdministrativa);
            }

            offLoading("dlgModalFormUsuario");
        }else{
            offLoading("dlgModalFormUsuario");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }
    }catch (e) {
        offLoading("dlgModalFormUsuario");
        Toast.fire({
            icon: "error",
            title: e
        });
    }

}

const guardarUsuarioAsync = async () =>{
    const options={
        method: "POST",
        body: JSON.stringify($("#formUsuario").serializeToJSON()),
        headers: headers
    };

    try {
        onLoading("dlgModalFormUsuario");
        const res = await fetch(contexto+'/api/seguridad/saveUsuario', options);
        const respJSON = await res.json();

        if (res.ok) {
        onChangeEditarPassword();
        Toast.fire({
            icon: "success",
            title: "El Usuario se guardo correctamente"
        });
        getTabla($("#idEntePublicoSearch").val());
        offLoading("dlgModalFormUsuario");
            $('#dlgModalFormUsuario').modal("hide");

        }else{
            offLoading("dlgModalFormUsuario");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }
    }catch (e) {
        var error =  JSON.parse(e.responseText);
        offLoading("dlgModalFormUsuario");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
};

const eliminarUsuarioAsync = async (idUsuarioEntePublico) =>{
    const options={
        method: "DELETE",
        body: JSON.stringify({idUsuarioEntePublico: idUsuarioEntePublico}),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/seguridad/deleteUsuario', options);
        const respJSON = await res.json();
        if (res.ok) {

        Toast.fire({
            icon: "success",
            title: respJSON.message
        });

        getTabla($("#idEntePublicoSearch").val());
        offLoading("dlgModalFormUsuario");

        }else{
            offLoading("dlgModalFormUsuario");
            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
        }
    }catch (e) {
        var error =  JSON.parse(e.responseText);
        offLoading("dlgModalFormUsuario");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }
};

function guardar(){

    let titleSave='¿Guardar Usuario?';
    let msgSave='¿Estas seguro que deseas guardar el usuario?';
    if ($('#opcion').val() == "Editar"){
        titleSave='¿Actualizar Usuario?';
        msgSave='¿Estas seguro que deseas actualizar el usuario?';
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
            guardarUsuarioAsync();
        }
    });
}

function eliminarUsuario(idUsuarioEntePublico){

    Swal.fire({
        title: 'Eliminar Usuario',
        text: '¿Estas seguro que deseas eliminar el usuario del sistema?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function(result) {
        if (result.isConfirmed) {
            eliminarUsuarioAsync(idUsuarioEntePublico);
        }
    })

}

function modalUsuarioEditar(idUsuarioEntePublico){
    $("#opcion").val("Editar");
    $("#idChangePassword").show();
    $("#contraseniaSistema").addClass("bg-secondary-subtle");
    $("#contraseniaRepite").addClass("bg-secondary-subtle");
    $("#contraseniaSistema").prop('readonly', true);
    $("#contraseniaRepite").prop('readonly', true);

    limpiarFormularioUsuario();

    $("#btnOpenArbol").hide();
    $('#button-buscar').hide();
    $('#idUsuarioEntePublico').val(idUsuarioEntePublico);
    //$('#idUsuario').val(idUsuario);

    getUsuarioId($("#idUsuarioEntePublico").val());

    $('#titleFormUsuario').text("Editar Usuario");
    $('#dlgModalFormUsuario').modal('show')
}

function onChangeEditarPassword() {
    if ($("#changePassword").prop('checked')){
        $("#contraseniaSistema").removeClass("bg-secondary-subtle");
        $("#contraseniaRepite").removeClass("bg-secondary-subtle");
        $("#contraseniaSistema").prop('readonly', false);
        $("#contraseniaRepite").prop('readonly', false);
    }else{
        $("#contraseniaSistema").val("");
        $("#contraseniaRepite").val("");
        $("#contraseniaSistema").addClass("bg-secondary-subtle");
        $("#contraseniaRepite").addClass("bg-secondary-subtle");
        $("#contraseniaSistema").prop('readonly', true);
        $("#contraseniaRepite").prop('readonly', true);
    }
}

function limpiarFormularioUsuario() {
    $("#descEntePublico").removeClass( "is-valid" );
    $("#descEntePublico").removeClass( "is-invalid" );
    $("#descUnidadAdmtva").removeClass( "is-valid" );
    $("#descUnidadAdmtva").removeClass( "is-invalid" );
    $("#nombreUsuario").removeClass( "is-valid" );
    $("#nombreUsuario").removeClass( "is-invalid" );
    $("#contraseniaSistema").removeClass( "is-valid" );
    $("#contraseniaSistema").removeClass( "is-invalid" );
    $("#contraseniaRepite").removeClass( "is-valid" );
    $("#contraseniaRepite").removeClass( "is-invalid" );

    $("#changePassword").prop('checked',false);

    $('#idUsuario').val("");
    $('#idEntePublico').val("");
    $('#descEntePublico').val("");
    $('#idUnidadAdmtva').val("");
    $('#descUnidadAdmtva').val("");
    $('#nombreUsuario').val("");
    $('#contraseniaSistema').val("");
    $('#contraseniaRepite').val("");
    $('#cveEstatus').val("VIG");
    $("#btnOpenArbol").show();
    $('#button-buscar').show();
}

function modalCatUsuarios(){
    if ($("#idUnidadAdmtva").val()=="")
        Swal.fire({
            title: 'Mensaje',
            text: "Seleccione una unidad administrativa",
            icon: 'warning',
        });
    else {
        getTablaCatUsuarios();
        $("#idEnteseleccionado").text($("#descEntePublico").val());
        $('#dlgModalCatUsuario').modal('show');
    }
}

function getTablaCatUsuarios(){
    const tableCu = new DataTable('#tableCatUsuario', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering: false,
        bSort: false,
        ajax: {
            url: contexto+'/api/seguridad/tablaUsuariosDisponibles',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    idUnidadAdministrativa: $("#idUnidadAdmtva").val()
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

function getTabla(idEntePublico){
    //var groupColumn=2;

    const table = new DataTable('#tableSegUsuario', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering: false,
        bSort: false,
        ajax: {
            url: contexto+'/api/seguridad/tabla',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    idEntePublico:idEntePublico
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
                "width": "50px",
                "targets": 0,
                "className": "text-center",
            },
            {
                "targets": 3,
                "width": "50px",
                "className": "text-center",
            },
            {
                "targets": 4,
                "width": "70",
                "className": "text-center",
            },
            {
                "targets": 5,
                "width": "170",
                "className": "text-center",
            }],
        lengthChange: false,
        select: false
    });
}

function modalRolConsulta(rfcUsuario, nombreCompletoUsuario){
    $('#rfcRolConsulta').text(rfcUsuario);
    $('#usuarioRolConsulta').text(nombreCompletoUsuario);

    getTablaPerfileConsulta(rfcUsuario);
    $('#dlgModalRolConsulta').modal('show');
}

function getTablaPerfileConsulta(rfcUsuario){

    const tablePc = new DataTable('#tablaRolConsulta', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering: false,
        bSort: false,
        ajax: {
            url: contexto+'/api/rolUsuario/tablaPerfilConsulta',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    segUsuarioPk: {usuario:rfcUsuario}

                }));
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [    {
            "targets": 1,
            "width": "50px",
            "className": "text-left",
        }],
        lengthChange: false,
        select: false
    });
}

function modalPerfil(rfcUsuario, nombreCompletoUsuario){
    $('#rfcRol').text(rfcUsuario);
    $('#usuarioRol').text(nombreCompletoUsuario);

    getTablaPerfilesDisponibles(rfcUsuario);
    getTablaPerfilesAsignados(rfcUsuario);

    $('#dlgModalForRol').modal('show');
}

function getTablaPerfilesDisponibles(rfcUsuario){

    const tablePd = new DataTable('#tablaRolesDisponible', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering: false,
        bSort: false,
        ajax: {
            url: contexto+'/api/rolUsuario/tablaRolesDisponibles',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    segUsuarioPk: {usuario:rfcUsuario}

                }));
            }
        },
        bProcessing: false,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [     {
            "targets": 1,
            "width": "40px",
            "className": "text-center",
        }],
        lengthChange: false,
        select: false
    });
}

function getTablaPerfilesAsignados(rfcUsuario){

    const tablePa = new DataTable('#tablaRolesAsignado', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering: false,
        bSort: false,
        ajax: {
            url: contexto+'/api/rolUsuario/tablaRolesAsignados',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d, {
                    segUsuarioPk: {usuario:rfcUsuario}

                }));
            }
        },
        bProcessing: false,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [      {
            "targets": 0,
            "width": "40px",
            "className": "text-center",
        },
            {
                "targets": 2,
                "width": "50px",
                "className": "text-left",
            }],
        lengthChange: false,
        select: false
    });
}


const addRolUSuario =  async (idPerfil, rfcUsuario) => {

    const options={
        method: "POST",
        body: JSON.stringify({segUsuarioPk: {usuario:rfcUsuario}, idPerfil:idPerfil}),
        headers: headers
    };

    try {
        onLoading("dlgModalForRol");
        const res = await fetch(contexto+'/api/rolUsuario/asignarRolUsuario', options);
        const respJSON = await res.json();

        Toast.fire({
            icon: 'success',
            title: "El Rol se asigno correctamente"
        });
        offLoading("dlgModalForRol");
        setTimeout(() => {
            getTablaPerfilesDisponibles(rfcUsuario);
            getTablaPerfilesAsignados(rfcUsuario);
            getTabla($("#idEntePublicoSearch").val());
        }, 200);

    }catch (e) {
        var error =  JSON.parse(e.responseText);
        offLoading("dlgModalForRol");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }

}

const removeRolUsuario =  async (idSegPerfilUsuario, rfcUsuario) => {

    const options={
        method: "POST",
        body: JSON.stringify({ idSegPerfilUsuario: idSegPerfilUsuario}),
        headers: headers
    };

    try {
        onLoading("dlgModalForRol");
        const res = await fetch(contexto+'/api/rolUsuario/removerRolUsuario', options);
        const respJSON = await res.json();

        if (res.ok){

        Toast.fire({
            icon: 'success',
            title: "El rol se removio correctamente"
        });
        offLoading("dlgModalForRol");
            setTimeout(() => {
                getTablaPerfilesDisponibles(rfcUsuario);
                getTablaPerfilesAsignados(rfcUsuario);
                getTabla($("#idEntePublicoSearch").val());
            }, 200);
        }else {
            Toast.fire({
                icon: 'error',
                title: "Error al eliminar rol"
            });
            offLoading("dlgModalForRol");
        }
    }catch (e) {
        var error =  JSON.parse(e.responseText);
        offLoading("dlgModalForRol");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }

}

function seleccionarUsuario(idUsuarioEntePublico, idUsuario, curp, nombreCompleto){
    $('#idUsuarioEntePublico').val(idUsuarioEntePublico);
    $('#idUsuario').val(idUsuario);
    $('#nombreUsuario').val(nombreCompleto);
    $('#dlgModalCatUsuario').modal('hide');
    $("#nombreUsuario").removeClass( "is-valid" );
    $("#nombreUsuario").removeClass( "is-invalid" );
}

function asignarAcceso(idUsuario, curpUsuario, nombreCompletoUsuario){
    $('#curpUsuario').text(curpUsuario);
    $('#usuarioAsignar').text(nombreCompletoUsuario);

    obtienearbolPorIdUsuario3(function (data) {},idUsuario);

    $('#dlgModalAsignarInstitucion').modal('show');
}


const agregarPermiso =  async () => {

    id=obtieneIdArbol2();

    if (id!="") {
        const options = {
            method: "POST",
            body: JSON.stringify({
                curp: $("#curpUsuario").text(),
                idEntePublico: $("#idEntePublicoSearch2").val(),
                iduUnidadAdministrativa: id,
                idSeguridadSistema: "SIGTIC"
            }),
            headers: headers
        };

            onLoading("dlgModalForRol");
            const res = await fetch(contexto + '/api/tree/addPermiso', options);
            const respJSON = await res.json();

            if (res.ok){
                Toast.fire({
                    icon: "success",
                    title: "El permiso fue agregado correctamente"
                });

                recargarArbol3();
                deSelectAll2();

                offLoading("dlgModalForRol");
            }
            else{

                Toast.fire({
                    icon: "error",
                    title: respJSON.message
                });
                offLoading("dlgModalForRol");
            }

    }
    else{
        Toast.fire({
            icon: "error",
            title: "Seleccione un elemento"
        });

        }

}



const eliminarPermiso =  async () => {

    id2=obtieneIdArbol3();

    if (id2!="") {
        const options = {
            method: "POST",
            body: JSON.stringify({
                curp: $("#curpUsuario").text(),
                iduUnidadAdministrativa: id2,
                idSeguridadSistema: "SIGTIC"
            }),
            headers: headers
        };


            onLoading("dlgModalAsignarInstitucion");
            const res = await fetch(contexto + '/api/tree/deletePermiso', options);
            const respJSON = await res.json();

        if (res.ok){
            Toast.fire({
                icon: "success",
                title: respJSON.message
            });

            deSelectAll3();
            recargarArbol3();

            offLoading("dlgModalAsignarInstitucion");
        }
        else{

            Toast.fire({
                icon: "error",
                title: respJSON.message
            });
            offLoading("dlgModalAsignarInstitucion");
        }


    }else{
        Toast.fire({
            icon: "error",
            title: "Seleccione un elemento"
        });
    }

}


function modalSubTipoServicio(idUsuario,curp,nombreCompletoUsuario){
    $('#curpSubTipoServicio').text(curp);
    $('#usuarioSubTipoServicio').text(nombreCompletoUsuario);

    getTablaSubtipoServicioDisponibles(idUsuario);
    getTablaSubtipoServicioAsignados(idUsuario);

    $('#dlgModalFormSubtipoServicioUsuario').modal('show');
}

function getTablaSubtipoServicioDisponibles(idUsuario){

    const tablePd = new DataTable('#tablaSubTipoServiciosDisponible', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering: false,
        bSort: false,
        ajax: {
            url: contexto+'/api/subServicioUsuario/tablaSubserviciosDisponibles',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d,
                    { idUsuario:idUsuario }));
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [     {
            "targets": 2,
            "width": "40px",
            "className": "text-center",
        }],
        lengthChange: false,
        select: false
    });
}

function getTablaSubtipoServicioAsignados(idUsuario){

    const tablePa = new DataTable('#tablaSubTipoServicioAsignado', {
        sPaginationType: "full_numbers",
        bDestroy: true,
        ordering: false,
        bSort: false,
        ajax: {
            url: contexto+'/api/subServicioUsuario/tablaSubServiciosAsignados',
            type: 'POST',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
            },
            data: function (d) {
                return JSON.stringify($.extend({}, d,
                    { idUsuario:idUsuario }));
            }
        },
        bProcessing: true,
        bServerSide:false,
        language:{
            "url": contexto+'/include/datatables/spanish.json',
        },
        columnDefs: [      {
            "targets": 0,
            "width": "40px",
            "className": "text-center",
        }],
        lengthChange: false,
        select: false
    });
}

const addSubTipoServicio =  async (idSubtipoServicioTic, idUsuario) => {

    const options={
        method: "POST",
        body: JSON.stringify({catSubtipoServiciosTic: {idSubtipoServicioTic:idSubtipoServicioTic}, usuario:{idUsuario:idUsuario}}),
        headers: headers
    };

    try {
        onLoading("dlgModalFormSubtipoServicioUsuario");
        const res = await fetch(contexto+'/api/subServicioUsuario/asignarSubTipoServicioUsuario', options);
        const respJSON = await res.json();

        Toast.fire({
            icon: 'success',
            title: "El Sub-Tipo Servicio  se asigno correctamente"
        });
        getTablaSubtipoServicioDisponibles(idUsuario);
        getTablaSubtipoServicioAsignados(idUsuario);

        offLoading("dlgModalFormSubtipoServicioUsuario");
    }catch (e) {
        var error =  JSON.parse(e.responseText);
        offLoading("dlgModalFormSubtipoServicioUsuario");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }

}

const removeSubTipoServicio =  async (idSubtipoServicioUsuario, idUsuario) => {

    const options={
        method: "POST",
        body: JSON.stringify({ idSubtipoServicioUsuario: idSubtipoServicioUsuario}),
        headers: headers
    };

    try {
        onLoading("dlgModalFormSubtipoServicioUsuario");
        const res = await fetch(contexto+'/api/subServicioUsuario/removerSubTipoServicioUsuario', options);
        const respJSON = await res.json();

        Toast.fire({
            icon: 'success',
            title: "El Sub-Tipo Servicio se removio correctamente"
        });

        getTablaSubtipoServicioDisponibles(idUsuario);
        getTablaSubtipoServicioAsignados(idUsuario);

        offLoading("dlgModalFormSubtipoServicioUsuario");
    }catch (e) {
        var error =  JSON.parse(e.responseText);
        offLoading("dlgModalFormSubtipoServicioUsuario");
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }

}


function actualizarEnteActual(idUsuarioEntepublico, idUsuarioEntepublicoActual){

    Swal.fire({
        title: 'Actualizar Ente Público',
        text: '¿Estas seguro que deseas actualizar al ente público actual?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Si',
        cancelButtonText: "No"
    }).then(function(result) {
        if (result.isConfirmed) {
            actualizarEnteActualAsync(idUsuarioEntepublico, idUsuarioEntepublicoActual);
        }
    })

}

const actualizarEnteActualAsync =  async (idUsuarioEntepublico, idUsuarioEntepublicoActual) => {

    const options={
        method: "POST",
        body: JSON.stringify({ idUsuarioEntePublico: idUsuarioEntepublico,catUsuarioEntePublico:{idUsuarioEntePublico:idUsuarioEntepublicoActual}}),
        headers: headers
    };

    try {
        onLoading(null);
        const res = await fetch(contexto+'/api/seguridad/actualizarEntePublicoActual', options);
        const respJSON = await res.json();

        Toast.fire({
            icon: 'success',
            title: "Ente actualizado correctamente"
        });

        getTabla($("#idEntePublicoSearch").val());

        offLoading(null);
    }catch (e) {
        var error =  JSON.parse(e.responseText);
        offLoading(null);
        Toast.fire({
            icon: "error",
            title: error.message
        });
    }

}

