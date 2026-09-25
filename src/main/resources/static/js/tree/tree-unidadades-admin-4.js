var jstree4 = null;

function obtienearbol4(callback) {
   onLoading(null);

    if (jstree4!=null)
        $('#arbol4').jstree('destroy');

    jstree4 = $('#arbol4').jstree({
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
            callback(getDatosUnidad4());
        }
    });

}
function obtienearbolPorInstitucion4(callback, id) {
    if (jstree4!=null)
        $('#arbol4').jstree('destroy');

    if(id!="") {
        onLoading(null);
        jstree4 = $('#arbol4').jstree({
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
                callback(getDatosUnidad4());
            }
        });
    }
}

function obtienearbolPorIdUsuario4(callback, id) {
    onLoading(null);

    if (jstree4!=null) {
        $('#arbol4').jstree('destroy');
    }

    if(id!=null && id!="") {
        jstree4 = $('#arbol4').jstree({
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
                callback(getDatosUnidad4());
            }
        });
    }

}


function obtieneIdArbol4(){
    var id="";
    if($("#arbol4").jstree("get_selected").length > 0){
        id= $("#arbol4").jstree("get_selected")[0]
    }
    return id;
}

function getDatosUnidad4(){

    return $("#arbol4").jstree("get_selected",true)[0].original;
}

/*function getDatosNodo(){
    return $("#arbol4").jstree("get_selected",true)[0].original;
}

function crearNodoArbol(idArbol, json) {
    $("#arbol4").jstree(true).create_node(idArbol,json , 'last');
}

function actualizaNodo(idArbol, text){
    $("#arbol4").jstree(true).rename_node (idArbol, text);
}

function deleteNodo(idArbol){
    $("#arbol4").jstree(true).delete_node (idArbol)
}

function tieneHijos(idArbol){
    return $("#arbol4").jstree(true).is_parent(idArbol)
}*/

function recargarArbol4(){
    $('#arbol4').jstree("refresh");
}

function deSelectAll4(){
    $('#arbol4').jstree("deselect_all");
}


function busqueda4(term){
    $('#arbol4').jstree(true).search(term);
}

function limpiarBuscarArbol4(){
    $("#txtBuscar4").val("");
    busqueda4("");
}