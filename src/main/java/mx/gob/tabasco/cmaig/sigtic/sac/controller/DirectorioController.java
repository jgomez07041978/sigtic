package mx.gob.tabasco.cmaig.sigtic.sac.controller;

import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletContext;
import java.util.Collections;

@Controller
public class DirectorioController {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private SegUsuarioService segUsuarioService;

    @GetMapping("/directorio")
    public String usuarios(Model model, Authentication authentication) {

        model.addAttribute("titlePage","Directorio Institucional");
        model.addAttribute("contextSigtic",servletContext.getContextPath());
        boolean authenticated = authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        model.addAttribute("directorioAuthenticated", authenticated);
        if (authenticated) {
            UserDetailsSesion user = authentication.getPrincipal() instanceof UserDetailsSesion
                    ? (UserDetailsSesion) authentication.getPrincipal() : null;
            model.addAttribute("listaMenu", user != null && user.getListaMenuBean() != null
                    ? user.getListaMenuBean() : Collections.emptyList());
            model.addAttribute("activarMenu", "DIRECTORIO");
            if (user != null) model.addAttribute("perfil", segUsuarioService.getPerfil());
        }
        model.addAttribute("directorioReturnUrl", authenticated ? "/inicio" : "/login");
        model.addAttribute("directorioReturnLabel", authenticated ? "Volver al inicio" : "Volver al login");

        return "directorio/directorio";
    }
}
