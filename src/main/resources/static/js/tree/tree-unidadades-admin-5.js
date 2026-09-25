var jstree5 = null;

function obtienearbol5(callback) {
   onLoading(null);

    if (jstree5!=null)
        $('#arbol5').jstree('destroy');

    jstree5 = $('#arbol5').jstree({
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
            callback(getDatosUnidad5());
        }
    });

}

function obtienearbolPorInstitucion5(callback, id) {
    if (jstree5!=null)
        $('#arbol5').jstree('destroy');

    if(id!="") {
        onLoading(null);
        jstree5 = $('#arbol5').jstree({
            'core': {
                'data': {
                    'url': function (node) {
                        return node.id === '#' ? contexto + '/api/tree/getPorInstitucionFree/' + id : contexto + '/api/tree/free/' + node.id;
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
                callback(getDatosUnidad5());
            }
        });
    }
}

function obtienearbolPorIdUsuario5(callback, id) {
    onLoading(null);

    if (jstree5!=null) {
        $('#arbol5').jstree('destroy');
    }

    if(id!=null && id!="") {
        jstree5 = $('#arbol5').jstree({
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
                callback(getDatosUnidad5());
            }
        });
    }

}


function obtieneIdArbol5(){
    var id="";
    if($("#arbol5").jstree("get_selected").length > 0){
        id= $("#arbol5").jstree("get_selected")[0]
    }
    return id;
}

function getDatosUnidad5(){

    return $("#arbol5").jstree("get_selected",true)[0].original;
}

function recargarArbol5(){
    $('#arbol5').jstree("refresh");
}

function deSelectAll5(){
    $('#arbol5').jstree("deselect_all");
}


function busqueda5(term){
    $('#arbol5').jstree(true).search(term);
}

function limpiarBuscarArbol5(){
    $("#txtBuscar5").val("");
    busqueda5("");
}