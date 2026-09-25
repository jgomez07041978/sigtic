package mx.gob.tabasco.cmaig.sigtic.sai.controller;

import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletContext;

@Controller
public class PortalController {
    @Autowired
    private SegUsuarioService segUsuarioService;

    @Autowired
    private ServletContext servletContext;

    @GetMapping("/portales")
    public String portales(Model model) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("perfil",segUsuarioService.getPerfil());
        model.addAttribute("listaMenu",authUser.getListaMenuBean());
        model.addAttribute("activarMenuPadre","SERVICIOS_TICS");
        model.addAttribute("activarMenu","PORTALES");
        model.addAttribute("titlePage","Portales");
        model.addAttribute("contextSigtic",servletContext.getContextPath());

        model.addAttribute("idEntePublicoSession", authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico());

        return "servicio-tic/portal";
    }
}
