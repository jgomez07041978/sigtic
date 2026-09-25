package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CuentaMaestraService {

    List<NumTelefonoCuentaMaestra> obtenerTelefonos(CuentaMaestraFiltro filtro);

    NumTelefonoCuentaMaestra getTelefono(String numTelefono, String numCuentaMaestra);

    NumTelefonoCuentaMaestra guardarTelefono(NumTelefonoCuentaMaestra telefono, boolean esNuevo, String numTelefonoOriginal, String numCuentaMaestraOriginal);

    NumTelefonoCuentaMaestra eliminarTelefono(String numTelefono, String numCuentaMaestra);

    NumTelefonoCuentaMaestra enviarTelefono(String numTelefono, String numCuentaMaestra);

    NumTelefonoCuentaMaestra validarTelefono(String numTelefono, String numCuentaMaestra, String descValidacionTelefono);

    NumTelefonoCuentaMaestra rechazarTelefono(String numTelefono, String numCuentaMaestra);

    void generarReporteExcel(CuentaMaestraFiltro filtro, HttpServletResponse response);

    List<CatTipoConexionLineaTelef> getTiposConexion();

    List<CatConceptoCuentaMaestra> getConceptosCuentaMaestra();

    List<CatCuentaMaestra> getCuentasMaestras(Long idEntePublico);

    List<UsuarioContactoProjection> getUsuariosContactoPorEnte(Long idEntePublico);
}
