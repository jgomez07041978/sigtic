var jstree2 = null;

function obtienearbol2(callback) {
   onLoading(null);

    if (jstree2!=null)
        $('#arbol2').jstree('destroy');

    jstree2 = $('#arbol2').jstree({
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
            callback(getDatosUnidad2());
        }
    });

}

function obtienearbolPorInstitucion2(callback, id) {
    if (jstree2!=null)
        $('#arbol2').jstree('destroy');

    if(id!=null && id!="") {
        onLoading(null);
        jstree2 = $('#arbol2').jstree({
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
                callback(getDatosUnidad2());
            }
        });
    }
}

function obtienearbolPorUnidadAdminPresupuestal(callback, id) {
    if (jstree2!=null)
        $('#arbol2').jstree('destroy');

    if(id!=null && id!="") {
        onLoading(null);
        jstree2 = $('#arbol2').jstree({
            'core': {
                'data': {
                    'url': function (node) {
                        return node.id === '#' ? contexto + '/api/tree/getAllUnidadAdminPresupuestal/' + id : contexto + '/api/tree/' + node.id;
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
                callback(getDatosUnidad2());
            }
        });
    }
}



function obtieneIdArbol2(){
    var id="";
    if($("#arbol2").jstree("get_selected").length > 0){
        id= $("#arbol2").jstree("get_selected")[0]
    }
    return id;
}

function getDatosUnidad2(){
    return $("#arbol2").jstree("get_selected",true)[0].original;
}

/*function getDatosNodo(){
    return $("#arbol2").jstree("get_selected",true)[0].original;
}

function crearNodoArbol(idArbol, json) {
    $("#arbol2").jstree(true).create_node(idArbol,json , 'last');
}

function actualizaNodo(idArbol, text){
    $("#arbol2").jstree(true).rename_node (idArbol, text);
}

function deleteNodo(idArbol){
    $("#arbol2").jstree(true).delete_node (idArbol)
}

function tieneHijos(idArbol){
    return $("#arbol2").jstree(true).is_parent(idArbol)
}*/


function busqueda2(term){
    $('#arbol2').jstree(true).search(term);
}

function recargarArbol2(){
    $('#arbol2').jstree("refresh");
}

function deSelectAll2(){
    $('#arbol2').jstree("deselect_all");
}


function limpiarBuscarArbol2(){
    $("#txtBuscar2").val("");
    busqueda2("");
}