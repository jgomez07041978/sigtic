package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import mx.gob.tabasco.cmaig.sigtic.sai.service.MisServicios;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import java.util.List;


@RestController
@RequestMapping("/api/mis-servicios")
public class MisServiciosRestController {

    @Autowired
    private MisServicios misServicios;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @Autowired
    private ServletContext servletContext;



    @PostMapping("/tablaMisServicios")
    public JSONObject getMisServicios() {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("MIS_SERVICIOS")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        List<UsuarioServicioBean> listaServicio = misServicios.getMisServiciosBean();

        listaServicio.forEach(userService -> {
            JSONArray json = new JSONArray();
            String colorCircle="";
            String texto="";

            texto="";
            if (userService.getFolioCr()!=null){
            if (userService.getAcepto().equals("SI") && userService.getRequiereFirma().equals("S") && userService.getIdEstatusCr()==9) {
                texto="Requiere Firma Autografa";
                colorCircle = "text-warning";
            }
            else
            if (userService.getAcepto().equals("SI"))
                colorCircle="text-success";
            else
                colorCircle="text-danger";
            }


            //json.add(userService.getCurp()+"<br/>"+userService.getNombreCompleto());
            if (userService.getUnidadAdministrativaOpera()!=null)
                json.add(userService.getNombreEntePublicoOpera()+"<br/>"+userService.getUnidadAdministrativaOpera());
            else
                json.add(userService.getNombreEntePublicoOpera());

            json.add(userService.getTipoServicio()+"<br/>"+userService.getSubtipoServicio());
            json.add(userService.getDescripcionServicio());

            if(userService.getEstatusSeguimiento()==null)
                json.add("");
            else
                json.add(userService.getEstatusSeguimiento());
           /* if(userService.getCatEstatusServicio().getIdEstatusServicio()!=4)
                json.add(userService.getCatEstatusServicio().getEstatusServicio());
            else
                json.add(userService.getCatEstatusServicio().getEstatusServicio()+" <a href='javascript:void(0);' data-toggle='tooltip' data-placement='top' title='Ver nota de Rechazo' onclick='verNotaRechazo(\""+userService.getNotaRechazo()+"\")'><i class=\"fa-solid fa-eye\"></i></a>");*/


            json.add(userService.getCveEstatus());

            if (userService.getFolioCr()!=null)
                json.add("<i class='bi bi-check-circle-fill h5 "+colorCircle+"' data-bs-toggle='tooltip' data-bs-html='true' data-bs-placement='top' data-bs-title='Aceptada = "+userService.getAcepto()+"<br/>"+texto+"'></i> "+userService.getFolioCr());
            else
                json.add("");

            String enlaces = "<div class=\"btn-group\" role=\"group\">";

            if (userService.getIdEstatusCr()!=null) {

                if (userService.getIdEstatusCr().intValue() == 8)
                    enlaces += "<a class='btn btn-success btn-sm' href='#' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Aceptar Carta Responsiva' onclick='aceptarCartaResponsiva(" + userService.getIdCartaResponsiva() + ")'><i class=\"fa-solid fa-square-envelope\"></i></a>";

                if (userService.getIdEstatusCr().intValue() == 9 || userService.getIdEstatusCr().intValue() == 11) {
                    enlaces += "<a class='btn btn-primary btn-sm' href='#' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Carta Responsiva Aceptada' onclick='cartaResponsivaAceptada(" + userService.getIdCartaResponsiva() + ")'><i class=\"fa-solid fa-envelope-circle-check\"></i></a>";
                    enlaces += "<a class='btn btn-success btn-sm' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Carta responsiva Aceptada' href='" + servletContext.getContextPath() + "/api/cartaResponsiva/cartaResponsivaReporte/" + userService.getIdCartaResponsiva() + "' target='_blank'><i class='bi bi-file-pdf-fill h5'></i></a>";
                }

                if (userService.getIdEstatusCr().intValue() == 11 && userService.getFileNameOriginal() != null) {
                    enlaces += "<a class='btn btn-outline-success btn-sm' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Carta responsiva Con firma' href='" + servletContext.getContextPath() + "/api/cartaResponsiva/exportCartaResponsiva/" + userService.getIdCartaResponsiva() + "' target='_blank' ><i class='bi bi-file-earmark-pdf-fill h5'></i></a>";
                }
            }


            enlaces += "</div>";
            json.add(enlaces);
            array.add(json);
        });

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/enviarServicio")
    public ServiciosTic addServicio(@RequestBody ServiciosTic serviciosTic){
        return misServicios.enviarServicio(serviciosTic);
    }

    @PostMapping("/tablaSeguimientos")
    public JSONObject getServicioSeguimientos() {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("MIS_SERVICIOS")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        //List<UsuarioServicio> listaServicio = misServicios.getServiciosSeguimiento();

        /*listaServicio.forEach(servicio -> {
            JSONArray json = new JSONArray();
            json.add(servicio.getCurp());
            if (servicio.getCatUsuarioEntePublico()!=null)
                json.add(servicio.getCatUsuarioEntePublico().getCatUsuario().getNombreCompleto());
            else
                json.add("");

            if (servicio.getUnidadAdministrativaOpera()==null)
                json.add(servicio.getEntePublicoOpera().getNombreEntePublico());
            else
                json.add(servicio.getEntePublicoOpera().getNombreEntePublico()+"<br/>"+servicio.getUnidadAdministrativaOpera().getNombreUnidadAdministrativa());

            json.add(servicio.getTipoServicio().getNombreTipoServicioTic());
            json.add(servicio.getSubTipoServicio().getNombreSubtipoServicioTic());
            json.add(servicio.getServicioTicDescripcion());
            json.add(servicio.getCatEstatusSeguimiento().getEstatusSeguimiento());
            json.add(servicio.getCveEstatus());

            String enlaces = "<div class=\"btn-group\" role=\"group\">";
            if (servicio.getCatEstatusSeguimiento().getIdEstatusSeguimiento().intValue()==2){
                enlaces += "<a href=#' class='btn btn-success' role='button' data-toggle='tooltip' data-placement='top' title='Editar Servicio' onclick='editarServicio(" + servicio.getIdServicioTic() + ")'><i class=\"fa-solid fa-pen-to-square\"></i></a>";
                enlaces += "<a href='javascript:void(0);' class='btn btn-danger' role='button' data-toggle='tooltip' data-placement='top' title='Rechazar Servicio' onclick='rechazarServicio(" + servicio.getIdServicioTic() + ")'><i class=\"fa-solid fa-rotate-left\"></i></a>";
                enlaces += "<a href='javascript:void(0);' class='btn btn-success' role='button' data-toggle='tooltip' data-placement='top' title='Autorizar Servicio' onclick='autorizarServicio(" + servicio.getIdServicioTic() + ")'><i class=\"fa-solid fa-circle-check\"></i></a>";
            }

            enlaces += "</div>";
            json.add(enlaces);
            array.add(json);
        });*/

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/rechazarServicio")
    public ServiciosTic rechazarServicio(@RequestBody ServiciosTic serviciosTic){
        return misServicios.rechazarServicio(serviciosTic);
    }

    @PostMapping("/autorizarServicio")
    public ServiciosTic autorizarServicio(@RequestBody ServiciosTic serviciosTic){
        return misServicios.autorizarServicio(serviciosTic);
    }


}
