package mx.gob.tabasco.cmaig.sigtic.sac.controller;

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
public class OrganigramaController {
    @Autowired
    private SegUsuarioService segUsuarioService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @GetMapping("/organigrama")
    public String organigrama(Model model) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("ORGANIGRAMA")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        model.addAttribute("perfil",segUsuarioService.getPerfil());
        model.addAttribute("listaMenu",authUser.getListaMenuBean());
        model.addAttribute("activarMenuPadre","CATALOGOS");
        model.addAttribute("activarMenu","ORGANIGRAMA");
        model.addAttribute("titlePage","Organigrama");
        model.addAttribute("contextSigtic",servletContext.getContextPath());
        model.addAttribute("activeEditar",segPerfilPrivilegio.getEditar());
        model.addAttribute("activeConsultar",segPerfilPrivilegio.getConsultar());

        model.addAttribute("idEntePublicoSession", authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico());

        return "catalogo/organigrama";
    }

}
