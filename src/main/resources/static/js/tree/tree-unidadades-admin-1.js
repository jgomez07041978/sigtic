var jstree = null;

function obtienearbol(callback) {
   onLoading(null);

    if (jstree!=null)
        $('#arbol').jstree('destroy');

    jstree = $('#arbol').jstree({
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
            callback(getDatosUnidad());
        }
    });

}

function obtienearbolPorInstitucion(callback, id) {

    if(id==null) id="";

    if (jstree!=null)
        $('#arbol').jstree('destroy');

    if(id!="") {
        onLoading(null);
        jstree = $('#arbol').jstree({
            'core': {
                'data': {
                    'url': function (node) {
                        return node.id === '#' ? contexto + '/api/tree/getPorInstitucion/' + id : contexto + '/api/tree/' + node.id
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
        })
            .on('ready.jstree', function () {

            }).on('dblclick', '.jstree-anchor', function (e) {
                if (typeof callback === 'function') {
                    callback(getDatosUnidad());
                }
            });
    }
}

function obtienearbolPorIdUsuario(callback, id) {
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
                callback(getDatosUnidad());
            }
        });
    }

}

/*function obtieneIdArbol(){
    var id="";
    if($("#arbol").jstree("get_selected").length > 0){
        id= $("#arbol").jstree("get_selected")[0]
    }
    return id;
}*/

function getDatosUnidad(){
    return $("#arbol").jstree("get_selected",true)[0].original;
}

/*function getDatosNodo(){
    return $("#arbol").jstree("get_selected",true)[0].original;
}

function crearNodoArbol(idArbol, json) {
    $("#arbol").jstree(true).create_node(idArbol,json , 'last');
}

function actualizaNodo(idArbol, text){
    $("#arbol").jstree(true).rename_node (idArbol, text);
}

function deleteNodo(idArbol){
    $("#arbol").jstree(true).delete_node (idArbol)
}

function tieneHijos(idArbol){
    return $("#arbol").jstree(true).is_parent(idArbol)
}*/

function recargarArbol(){
    $('#arbol').jstree("refresh");
}

function busqueda(term){
    $('#arbol').jstree(true).search(term);
}

function limpiarBuscarArbol(){
    $("#txtBuscar").val("");
    busqueda("");
}