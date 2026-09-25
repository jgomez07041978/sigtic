package mx.gob.tabasco.cmaig.sigtic.sai.controller;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletContext;

@Controller
public class MisServiciosController {
    @Autowired
    private SegUsuarioService segUsuarioService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @GetMapping("/misServicios")
    public String misServicios(Model model) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("MIS_SERVICIOS")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());
        boolean existeRolEnlaceTicServicio = authUser.getListaPerfil().stream().filter(p-> p.getNombrePerfil().equals("ENLACE_TIC_SERVICIO")).findFirst().isPresent();

        model.addAttribute("perfil",segUsuarioService.getPerfil());
        model.addAttribute("listaMenu",authUser.getListaMenuBean());
        model.addAttribute("activarMenu","MIS_SERVICIOS");
        model.addAttribute("titlePage","Mis Servicios");
        model.addAttribute("contextSigtic",servletContext.getContextPath());
        model.addAttribute("activeAgregar",segPerfilPrivilegio.getAgregar());
        model.addAttribute("activeConsultar",segPerfilPrivilegio.getConsultar());
        model.addAttribute("idEntePublicoSession", authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico());
        model.addAttribute("isAdministradorSigtic", authUser.isAdministradorSigtic());
        model.addAttribute("isRolEnlaceTicServicio", existeRolEnlaceTicServicio ? "S":"N");


        model.addAttribute("rfc", authUser.getRfc());
        model.addAttribute("curp", authUser.getCatUsuarioEntePublico().getCatUsuario().getCurp());
        model.addAttribute("nombreCompleto", authUser.getCatUsuarioEntePublico().getCatUsuario().getNombreCompleto());

        return "servicio-tic/mis-servicios";
    }
}
