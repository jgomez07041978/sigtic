var jstree3 = null;

function obtienearbol3(callback) {
   onLoading(null);

    if (jstree3!=null)
        $('#arbol3').jstree('destroy');

    jstree3 = $('#arbol3').jstree({
        'core': {
            'data': {
                'url': function (node) {
                    return node.id === '#' ? contexto + '/api/tree/0' : contexto + '/api/tree/' + node.id
                }
            },
            "check_callback": true,
            "dblclick_toggle": false
        },
        "types": {
            "default": {
                "icon": "fa fa-home"
            }
        },
        "plugins": ["types", "search"],
        "search": {
            "case_sensitive": false,
            "show_only_matches": true
        }
    }).on('ready.jstree', function () {
            offLoading(null);
    }).on('dblclick', '.jstree-anchor', function (e) {
        if (typeof callback === 'function') {
            callback(getDatosUnidad3());
        }
    });

}
function obtienearbolPorInstitucion3(callback, id) {
    if (jstree3!=null)
        $('#arbol3').jstree('destroy');

    if(id!="") {
        onLoading(null);
        jstree3 = $('#arbol3').jstree({
            'core': {
                'data': {
                    'url': function (node) {
                        return node.id === '#' ? contexto + '/api/tree/getPorInstitucion/' + id : contexto + '/api/tree/' + node.id;
                    }
                },
                "check_callback": true,
                "dblclick_toggle": false
            },
            "types": {
                "default": {
                    "icon": "fa fa-home"
                }
            },
            "plugins": ["types", "search"],
            "search": {
                "case_sensitive": false,
                "show_only_matches": true
            }
        }).bind('loaded.jstree', function (event, data) {
            offLoading(null);
            //$(this).jstree('open_all');
        }).on('ready.jstree', function () {

        }).on('dblclick', '.jstree-anchor', function (e) {
            if (typeof callback === 'function') {
                callback(getDatosUnidad3());
            }
        });
    }
}

function obtienearbolPorIdUsuario3(callback, id) {
    onLoading(null);

    if (jstree3!=null) {
        $('#arbol3').jstree('destroy');
    }

    if(id!=null && id!="") {
        jstree3 = $('#arbol3').jstree({
            'core': {
                'data': {
                    'url': function (node) {
                        return node.id === '#' ? contexto + '/api/tree/getPorIdUsuario/' + id : contexto + '/api/tree/' + node.id;
                    }
                },
                "check_callback": true,
                "dblclick_toggle": false
            },
            "types": {
                "default": {
                    "icon": "fa fa-home"
                }
            },
            "plugins": ["types", "search"],
            "search": {
                "case_sensitive": false,
                "show_only_matches": true
            }
        }).bind('loaded.jstree', function (event, data) {
            offLoading(null);
            //$(this).jstree('open_all');
        }).on('dblclick', '.jstree-anchor', function (e) {
            if (typeof callback === 'function') {
                callback(getDatosUnidad3());
            }
        });
    }

}


function obtieneIdArbol3(){
    var id="";
    if($("#arbol3").jstree("get_selected").length > 0){
        id= $("#arbol3").jstree("get_selected")[0]
    }
    return id;
}

function getDatosUnidad3(){

    return $("#arbol3").jstree("get_selected",true)[0].original;
}

/*function getDatosNodo(){
    return $("#arbol3").jstree("get_selected",true)[0].original;
}

function crearNodoArbol(idArbol, json) {
    $("#arbol3").jstree(true).create_node(idArbol,json , 'last');
}

function actualizaNodo(idArbol, text){
    $("#arbol3").jstree(true).rename_node (idArbol, text);
}

function deleteNodo(idArbol){
    $("#arbol3").jstree(true).delete_node (idArbol)
}

function tieneHijos(idArbol){
    return $("#arbol3").jstree(true).is_parent(idArbol)
}*/

function recargarArbol3(){
    $('#arbol3').jstree("refresh");
}

function deSelectAll3(){
    $('#arbol3').jstree("deselect_all");
}


function busqueda3(term){
    $('#arbol3').jstree(true).search(term);
}

function limpiarBuscarArbol3(){
    $("#txtBuscar3").val("");
    busqueda3("");
}