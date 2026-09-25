package mx.gob.tabasco.cmaig.sigtic.sas.controller;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilUsuario;
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
public class GestionAccesoController {

    @Autowired
    private SegUsuarioService segUsuarioService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @GetMapping("/gestion-perfil")
    public String seguridadRole(Model model) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("GEST_ROLES")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());


        model.addAttribute("perfil",segUsuarioService.getPerfil());
        model.addAttribute("listaMenu",authUser.getListaMenuBean());
        model.addAttribute("activarMenuPadre","GESTION_ACCESO");
        model.addAttribute("activarMenu","GEST_ROLES");
        model.addAttribute("titlePage","Gestión de Roles");
        model.addAttribute("activeAgregar",segPerfilPrivilegio.getAgregar());
        model.addAttribute("activeConsultar",segPerfilPrivilegio.getConsultar());
        model.addAttribute("contextSigtic",servletContext.getContextPath());

        return "seguridad/role";
    }

    @GetMapping("/permiso-usuario")
    public String seguridadUsuario(Model model) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("perfil",segUsuarioService.getPerfil());
        model.addAttribute("listaMenu", authUser.getListaMenuBean());
        model.addAttribute("activarMenuPadre","GESTION_ACCESO");
        model.addAttribute("activarMenu","GEST_PERMISO");
        model.addAttribute("titlePage","Permisos del Usuario");
        model.addAttribute("contextSigtic",servletContext.getContextPath());

        model.addAttribute("idEntePublicoSession", authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico());

        return "seguridad/permiso-usuario";
    }

}
