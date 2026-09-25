package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sac.service.UnidadesAdministrativaService;
import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CuentaMaestraService;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo/tic/cuentaMaestra")
public class CuentaMaestraRestController {

    private final CuentaMaestraService cuentaMaestraService;
    private final SegPerfilPrivilegioService segPerfilPrivilegioService;
    private final UnidadesAdministrativaService unidadesAdministrativaService;

    public CuentaMaestraRestController(CuentaMaestraService cuentaMaestraService,
                                        SegPerfilPrivilegioService segPerfilPrivilegioService,
                                        UnidadesAdministrativaService unidadesAdministrativaService) {
        this.cuentaMaestraService = cuentaMaestraService;
        this.segPerfilPrivilegioService = segPerfilPrivilegioService;
        this.unidadesAdministrativaService = unidadesAdministrativaService;
    }

    private SegPerfilPrivilegio getPrivilegios(UserDetailsSesion authUser) {
        SegMenu segMenu = authUser.getListaMenu().stream()
                .filter(p -> p.getCodigo().equals("CUENTA_MAESTRA"))
                .findFirst().get();
        return segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC", authUser.getUsername(), segMenu.getIdMenu());
    }

    private boolean tieneRol(UserDetailsSesion authUser, String nombreRol) {
        if (authUser.getListaPerfil() == null)
            return false;
        return authUser.getListaPerfil().stream()
                .anyMatch(p -> nombreRol.equalsIgnoreCase(p.getNombrePerfil()));
    }

    private String descEstatus(String cveEstatus) {
        if (cveEstatus == null)
            return "";
        switch (cveEstatus) {
            case "VIG":
                return "Vigente";
            case "NOV":
                return "No Vigente";
            default:
                return cveEstatus;
        }
    }

    private String descEstatusValidacion(String estatusValidacion) {
        if (estatusValidacion == null)
            return "";
        switch (estatusValidacion) {
            case "C":
                return "Captura";
            case "E":
                return "Enviado";
            case "V":
                return "Validado";
            case "R":
                return "Rechazado";
            default:
                return estatusValidacion;
        }
    }

    @PostMapping("/tabla")
    public JSONObject getTabla(@RequestBody CuentaMaestraFiltro filtro) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegPerfilPrivilegio segPerfilPrivilegio = getPrivilegios(authUser);
        boolean restringidoAEnlace = tieneRol(authUser, "ENLACE_TIC_CUENTA_MAESTRA") && !authUser.isAdministradorSigtic();
        boolean esAdminSigticRol = tieneRol(authUser, "ADMIN_SIGTIC");

        List<NumTelefonoCuentaMaestra> lista = cuentaMaestraService.obtenerTelefonos(filtro);

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        lista.forEach(reg -> {
            JSONArray json = new JSONArray();

            String estatus = reg.getCveEstatus();
            String estatusValidacion = reg.getEstatusValidacion();

            json.add(reg.getEntePublico() != null ? reg.getEntePublico().getNombreEntePublico() : "");
            json.add(reg.getUnidadAdministrativa() != null ? reg.getUnidadAdministrativa().getNombreUnidadAdministrativa() : "");
            json.add(reg.getNumTelefono());
            json.add(reg.getCatCuentaMaestra() != null ? reg.getCatCuentaMaestra().getNumCuentaMaestra() : "");
            json.add(reg.getCatTipoConexionLineaTelef() != null ? reg.getCatTipoConexionLineaTelef().getNombreTipoConexionLineaTelef() : "");
            json.add(reg.getFechaAltaText());
            json.add(reg.getFechaBajaText());
            json.add(descEstatus(estatus));
            json.add(descEstatusValidacion(estatusValidacion));

            String numTelJs = reg.getNumTelefono() == null ? "" : reg.getNumTelefono().replace("'", "\\'");
            String numCtaJs = reg.getCatCuentaMaestra() == null ? "" : reg.getCatCuentaMaestra().getNumCuentaMaestra().replace("'", "\\'");

            boolean esCapturaORechazado = "C".equals(estatusValidacion) || "R".equals(estatusValidacion);
            boolean esEnviado = "E".equals(estatusValidacion);
            boolean esValidado = "V".equals(estatusValidacion);

            boolean puedeEditar = segPerfilPrivilegio.getEditar()
                    && (esCapturaORechazado || (esAdminSigticRol && (esEnviado || esValidado)));
            if (restringidoAEnlace)
                puedeEditar = puedeEditar && esCapturaORechazado;

            boolean puedeEnviar = segPerfilPrivilegio.getEditar() && esCapturaORechazado;
            boolean puedeValidarRechazar = segPerfilPrivilegio.getEditar() && esAdminSigticRol && esEnviado;

            String enlaces = "<div class=\"btn-group\"> ";
            if (puedeEditar)
                enlaces += "<a href='javascript:void(0);' class='btn btn-success btn-sm' data-toggle='tooltip' data-placement='top' title='Editar' onclick=\"editarTelefono('" + numTelJs + "','" + numCtaJs + "')\"><i class='bi bi-pencil-square'></i></a>";
            if (puedeEnviar)
                enlaces += "<a href='javascript:void(0);' class='btn btn-primary btn-sm' data-toggle='tooltip' data-placement='top' title='Enviar' onclick=\"enviarTelefono('" + numTelJs + "','" + numCtaJs + "')\"><i class='bi bi-send'></i></a>";
            if (puedeValidarRechazar) {
                enlaces += "<a href='javascript:void(0);' class='btn btn-success btn-sm' data-toggle='tooltip' data-placement='top' title='Validar' onclick=\"validarTelefono('" + numTelJs + "','" + numCtaJs + "')\"><i class='bi bi-check-circle'></i></a>";
                enlaces += "<a href='javascript:void(0);' class='btn btn-danger btn-sm' data-toggle='tooltip' data-placement='top' title='Rechazar' onclick=\"rechazarTelefono('" + numTelJs + "','" + numCtaJs + "')\"><i class='bi bi-x-circle'></i></a>";
            }
            enlaces += "</div>";
            json.add(enlaces);

            array.add(json);
        });

        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @GetMapping("/get/{numTelefono}/{numCuentaMaestra}")
    public NumTelefonoCuentaMaestra getTelefono(@PathVariable("numTelefono") String numTelefono,
                                                 @PathVariable("numCuentaMaestra") String numCuentaMaestra) {
        return cuentaMaestraService.getTelefono(numTelefono, numCuentaMaestra);
    }

    @PostMapping("/guardar")
    public NumTelefonoCuentaMaestra guardar(@RequestBody GuardarTelefonoCuentaMaestraRequest request) {
        boolean esNuevo = "Nuevo".equalsIgnoreCase(request.getOpcion());
        return cuentaMaestraService.guardarTelefono(
                request.getTelefono(),
                esNuevo,
                request.getNumTelefonoOriginal(),
                request.getNumCuentaMaestraOriginal());
    }

    @PostMapping("/enviar/{numTelefono}/{numCuentaMaestra}")
    public NumTelefonoCuentaMaestra enviar(@PathVariable("numTelefono") String numTelefono,
                                            @PathVariable("numCuentaMaestra") String numCuentaMaestra) {
        return cuentaMaestraService.enviarTelefono(numTelefono, numCuentaMaestra);
    }

    @PostMapping("/validar/{numTelefono}/{numCuentaMaestra}")
    public NumTelefonoCuentaMaestra validar(@PathVariable("numTelefono") String numTelefono,
                                             @PathVariable("numCuentaMaestra") String numCuentaMaestra,
                                             @RequestBody ValidarTelefonoRequest request) {
        return cuentaMaestraService.validarTelefono(numTelefono, numCuentaMaestra, request.getDescValidacionTelefono());
    }

    @PostMapping("/rechazar/{numTelefono}/{numCuentaMaestra}")
    public NumTelefonoCuentaMaestra rechazar(@PathVariable("numTelefono") String numTelefono,
                                              @PathVariable("numCuentaMaestra") String numCuentaMaestra) {
        return cuentaMaestraService.rechazarTelefono(numTelefono, numCuentaMaestra);
    }

    @DeleteMapping("/eliminar/{numTelefono}/{numCuentaMaestra}")
    public NumTelefonoCuentaMaestra eliminar(@PathVariable("numTelefono") String numTelefono,
                                              @PathVariable("numCuentaMaestra") String numCuentaMaestra) {
        return cuentaMaestraService.eliminarTelefono(numTelefono, numCuentaMaestra);
    }

    @PostMapping("/exportarFiltro")
    public void exportarFiltro(@RequestBody CuentaMaestraFiltro filtro, HttpServletResponse response) {
        cuentaMaestraService.generarReporteExcel(filtro, response);
    }

    @GetMapping("/getTiposConexion")
    public List<CatTipoConexionLineaTelef> getTiposConexion() {
        return cuentaMaestraService.getTiposConexion();
    }

    @GetMapping("/getConceptosCuentaMaestra")
    public List<CatConceptoCuentaMaestra> getConceptosCuentaMaestra() {
        return cuentaMaestraService.getConceptosCuentaMaestra();
    }

    @GetMapping("/getCuentasMaestras/{idEntePublico}")
    public List<CatCuentaMaestra> getCuentasMaestras(@PathVariable("idEntePublico") Long idEntePublico) {
        return cuentaMaestraService.getCuentasMaestras(idEntePublico);
    }

    @GetMapping("/getUnidadAdministrativaPorEnte/{idEntePublico}")
    public List<UnidadAdministrativa> getUnidadAdministrativaPorEnte(@PathVariable("idEntePublico") Long idEntePublico) {
        return unidadesAdministrativaService.getUnidadAdministrativaPorEnteId(idEntePublico);
    }

    @GetMapping("/getUsuariosContactoPorEnte/{idEntePublico}")
    public List<UsuarioContactoProjection> getUsuariosContactoPorEnte(@PathVariable("idEntePublico") Long idEntePublico) {
        return cuentaMaestraService.getUsuariosContactoPorEnte(idEntePublico);
    }
}
