// Reporte de Servicios Especiales (modal de filtros).
// Nombres con sufijo Rep para no chocar con cuenta-maestra.js ni servicio-especial.js.
const headersRep = {
    "Accept": "application/json",
    "X-CSRF-Token": $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

const headersRepDescarga = {
    "X-CSRF-Token": $('input[name="_csrf"]').val(),
    "Content-type": "application/json; charset=UTF-8"
};

const baseRepServEsp = '/api/catalogo/tic/servicioEspecial';
const baseRepCtaMaestra = '/api/catalogo/tic/cuentaMaestra';

$(document).ready(function () {

    $('#idBtnReportes').click(function (e) {
        e.preventDefault();
        abrirModalReportes();
    });

    $('#btnRepGenerar').click(function () {
        generarReporte();
    });

    // Dependencia - Unidades y Cuentas Maestras de esa dependencia
    $('#idEntePublicoRep').on('change', function () {
        const idEnte = $(this).val();

        llenarSelectRep(baseRepCtaMaestra + '/getUnidadAdministrativaPorEnte/' + idEnte,
            '#idUnidadAdministrativaRep', 'idUnidadAdministrativa', 'nombreUnidadAdministrativa', 'Todas', idEnte);

        llenarSelectRep(baseRepCtaMaestra + '/getCuentasMaestras/' + idEnte,
            '#numCuentaMaestraRep', 'numCuentaMaestra',
            c => c.numCuentaMaestra + (c.nombreClienteCuentaMaestra ? ' - ' + c.nombreClienteCuentaMaestra : ''),
            'Todas', idEnte);
    });

    // Partida - Artículos de esa partida (si no hay partida se muestran toodos los articulos)
    $('#cvePartidaRep').on('change', function () {
        cargarArticulosRep($(this).val());
    });
});

function cargarArticulosRep(cvePartida) {
    const url = cvePartida
        ? baseRepServEsp + '/getArticulosPorPartida/' + encodeURIComponent(cvePartida)
        : baseRepServEsp + '/getArticulos';
    return llenarSelectRep(url, '#codigoEbsRep', 'clave', 'descripcion', 'Todos');
}

function llenarSelectRep(url, selector, idField, nameField, textoInicial, requiereValor) {
    const $sel = $(selector);
    $sel.empty().append($('<option></option>').attr('value', '').text(textoInicial || 'Seleccione'));

    if (requiereValor === '' || requiereValor === null) {
        return Promise.resolve();
    }

    return fetch(contexto + url, {method: "GET", headers: headersRep})
        .then(res => {
            if (!res.ok) throw new Error('HTTP ' + res.status);
            return res.json();
        })
        .then(lista => {
            lista.forEach(item => {
                const texto = (typeof nameField === 'function') ? nameField(item) : item[nameField];
                $sel.append($('<option></option>').attr('value', item[idField]).text(texto));
            });
        })
        .catch(() => {
            Toast.fire({icon: "error", title: "No se pudo cargar un catálogo del reporte"});
        });
}

function abrirModalReportes() {
    $('#periodoInicioRep, #periodoFinRep').val('');
    $('#idVigenteRep').prop('checked', true);
    $('#idHistoricoRep').prop('checked', false);
    $('#repFormatoXlsx').prop('checked', true);

    // Dependencia: mismo endpoint que usa el formulario de servicio especial
    llenarSelectRep('/api/entePublico/usuario', '#idEntePublicoRep', 'idEntePublico', 'nombreEntePublico', 'Todas');

    // Unidad y cuenta dependen de la dependencia elegida
    llenarSelectRep('', '#idUnidadAdministrativaRep', 'idUnidadAdministrativa', 'nombreUnidadAdministrativa', 'Todas', '');
    llenarSelectRep('', '#numCuentaMaestraRep', 'numCuentaMaestra', 'numCuentaMaestra', 'Todas', '');

    // Partida y artículo (muestra todos los artículos al inicio)
    llenarSelectRep(baseRepServEsp + '/getPartidas', '#cvePartidaRep', 'clave', 'descripcion', 'Todas');
    cargarArticulosRep('');

    llenarSelectRep(baseRepCtaMaestra + '/getTiposConexion', '#cveTipoConexionRep',
        'cveTipoConexionLineaTelef', 'nombreTipoConexionLineaTelef', 'Todos');

    $('#dlgModalReportes').modal('show');
}

function periodoADia1(valorMes) {
    return valorMes ? valorMes + '-01' : null;
}

async function generarReporte() {
    const formato = $('input[name="repFormato"]:checked').val();

    if (formato !== 'XLSX' && formato !== 'PDF') {
        Toast.fire({
            icon: "info",
            title: "Ese formato aún no está disponible"
        });
        return;
    }

    const desde = $('#periodoInicioRep').val();
    const hasta = $('#periodoFinRep').val();

    if (desde && hasta && desde > hasta) {
        Toast.fire({
            icon: "warning",
            title: "El periodo inicial no puede ser mayor al final"
        });
        return;
    }

    const ruta = formato === 'PDF' ? '/exportarPdf' : '/exportar';

    const body = {
        idEntePublico: $('#idEntePublicoRep').val() || null,
        idUnidadAdministrativa: $('#idUnidadAdministrativaRep').val() || null,
        numCuentaMaestra: $('#numCuentaMaestraRep').val() || null,
        cveTipoConexion: $('#cveTipoConexionRep').val() || null,
        cvePartida: $('#cvePartidaRep').val() || null,
        codigoEbs: $('#codigoEbsRep').val() || null,
        periodoInicio: periodoADia1(desde),
        periodoFin: periodoADia1(hasta),
        isVigente: $('#idVigenteRep').is(':checked'),
        isHistorico: $('#idHistoricoRep').is(':checked')
    };

    try {
        onLoading("dlgModalReportes");

        const res = await fetch(contexto + baseRepServEsp + ruta, {
            method: "POST",
            body: JSON.stringify(body),
            headers: headersRepDescarga
        });

        if (!res.ok) {
            let mensaje = "No se pudo generar el reporte";

            try {
                const err = await res.json();
                if (err && err.message) {
                    mensaje = err.message;
                }
            } catch (e) {
            }

            Toast.fire({icon: "error", title: mensaje});
            return;
        }

        const blob = await res.blob();
        const disposicion = res.headers.get('Content-Disposition') || '';
        const coincidencia = /filename="?([^";]+)"?/i.exec(disposicion);

        const nombreArchivo = coincidencia
            ? coincidencia[1].trim()
            : (formato === 'PDF'
                ? 'servicios_especiales.pdf'
                : 'servicios_especiales.xlsx');

        const url = URL.createObjectURL(blob);
        const enlace = document.createElement('a');

        enlace.href = url;
        enlace.download = nombreArchivo;
        document.body.appendChild(enlace);
        enlace.click();
        enlace.remove();

        setTimeout(() => URL.revokeObjectURL(url), 1000);

        Toast.fire({
            icon: "success",
            title: "El reporte se ha generado"
        });
    } catch (e) {
        console.error("Error al generar el reporte:", e);

        Toast.fire({
            icon: "error",
            title: "Error al generar el reporte"
        });
    } finally {
        offLoading("dlgModalReportes");
    }
}
