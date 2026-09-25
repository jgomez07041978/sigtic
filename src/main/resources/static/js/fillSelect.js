function fillSelectDinamicoGet(relativeUrl,idElemento, value, descripcion, idPadre, idPadre2, callback){
    var url = contexto+relativeUrl

    if (idPadre!=null)
        url=url+"/"+idPadre

    if (idPadre2!=null)
        url=url+"/"+idPadre2

    $(idElemento).empty();
    $.ajax({ url: url,
        type: 'GET',
        dataType: 'JSON',
        async: false,
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function(response) {
            var dropdown =  $(idElemento);
            dropdown.append($('<option></option>').attr('value', '').text('Seleccione'));
            for (var d = 0; d < response.length; d++) {
                dropdown.append($('<option></option>').attr('value', response[d][value]).text(
                    (typeof descripcion === 'function')?descripcion(response[d]):response[d][descripcion]
                ));
            }

            if (typeof callback === 'function') {
                callback();
            }
        },
        error: function (error) {
            Swal.fire({
                title: 'Error',
                text: 'Error la cargar los datos',
                icon: 'error',
            });
        }
    });
}

function fillSelectDinamico(relativeUrl,idElemento, value, descripcion, idPadre, tabla, callback){
    var url = contexto+relativeUrl
    $(idElemento).empty();
    $.ajax({ url: url,
        type: 'POST',
        dataType: 'JSON',
        async: false,
        data: JSON.stringify({
            idDependiente:idPadre,
            consulta: tabla
        }),
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader('X-CSRF-Token', $('input[name="_csrf"]').val())
        },
        success: function(response) {
            var dropdown =  $(idElemento);
            dropdown.append($('<option></option>').attr('value', '').text('Seleccione'));
            for (var d = 0; d < response.length; d++) {
                dropdown.append($('<option></option>').attr('value', response[d][value]).text(
                    (typeof descripcion === 'function')?descripcion(response[d]):response[d][descripcion]
                ));
            }

            if (typeof callback === 'function') {
                callback();
            }
        },
        error: function (error) {
            Swal.fire({
                title: 'Error',
                text: 'Error la cargar los datos',
                icon: 'error',
            });
        }
    });
}