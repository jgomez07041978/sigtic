package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Mensage;
import net.minidev.json.JSONObject;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface CartaResponsivaService {

    List<CartaResponsiva> getCartaResponsivaAdminSigtic(Long idEntePublico, boolean isEstatusVig, boolean isEstatusHis);

    List<CartaResponsiva> getCartaResponsivaAdminCr(Long idEntePublico, boolean isEstatusVig, boolean isEstatusHis);

    List<CartaResponsiva> getCartaResponsivaEnlaceCr(Long idEntePublico, boolean isEstatusVig, boolean isEstatusHis);

    List<CartaResponsiva> getCartaResponsivaPorEnte(CartaResponsiva cartaResponsiva, boolean existeRolEnlaceCartaResponsiva, boolean existeRolUsuarioCartaResponsiva, boolean isEstatusVig, boolean isEstatusHis);

    List<CartaResponsiva> getMisCartaResponsiva();

    CartaResponsiva generarCartaResponsiva(CartaResponsiva cartaResponsiva);

    CartaResponsiva generarCartaResponsivaServicio(ServiciosTic serviciosTic);
    CartaResponsiva getCartaResponsivaId(Long idCartaResponsiva);

    CartaResponsiva asignarPlantillaCartaResponsivaId(Long idCartaResponsiva);

    List<CartaResponsivaDetalle> getServiciosAdd(CartaResponsiva cartaResponsiva);

    List<ServiciosTic>  getServiciosDisponibles(CartaResponsiva cartaResponsivaParam);

    CartaResponsivaDetalle addServicioCartaResponsiva(CartaResponsivaDetalle cartaResponsivaDetalle);
    JSONObject deleteServicioCartaResponsiva(Long idCartaResponsivaDetalle);

    ResponseEntity<byte[]> getReporteCr(Long id) throws JRException, IOException;

    Integer countDetalleCr(CartaResponsiva cartaResponsiva);

    CartaResponsiva aceptarCartaResponsiva(CartaResponsiva cartaResponsiva);
    CartaResponsiva aceptarCartaResponsivaUser(CartaResponsiva cartaResponsiva);
    CartaResponsivaSeguimiento actualizarCartaResponsiva(CartaResponsiva cartaResponsiva);

    Mensage uploadCartaResponsiva(FileUploadBean fileUploadBean);

    void exportCartaResponsiva(HttpServletResponse response, Long id);

    CartaResponsiva usuarioAutorizaCartaResponsiva(CartaResponsiva cartaResponsiva);

    List<CartaResponsivaSeguimiento>  getListaSeguimientoCartaResponsiva(CartaResponsiva cartaResponsiva);

    CartaResponsiva generarToken(Long idCartaResponsiva);

    CartaResponsiva aceptarCrId(Long idCartaResponsiva);

    CartaResponsiva rechazarCrId(Long idCartaResponsiva);

    CartaResponsiva aceptarCrFreeId(Long idCartaResponsiva);

    CartaResponsiva rechazarCrFreeId(Long idCartaResponsiva);

    CartaResponsiva cartaResponsivaAceptada(Long idCartaResponsiva);

    ResponseEntity<byte[]> getReporteCrAceptada(Long idCartaResponsiva) throws FileNotFoundException, JRException, MalformedURLException;

}
