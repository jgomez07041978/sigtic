package mx.gob.tabasco.cmaig.sigtic.sas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletContext;

@Controller
public class LoginController {

    @Autowired
    private Environment env;

    @Autowired
    private ServletContext servletContext;


    @GetMapping({"/","/login"})
    public String index(Model model) {

        model.addAttribute("contextSigtic",servletContext.getContextPath());
        model.addAttribute("recatchaKeyPublic",env.getProperty("google.recaptcha.key.site"));
        model.addAttribute("userDesa",env.getProperty("sigtic.userDesa"));
        model.addAttribute("claveDesa",env.getProperty("sigtic.claveDesa"));

        model.addAttribute("generaToken", env.getProperty("sigtic.generaToken"));
        model.addAttribute("usuarioToken", env.getProperty("sigtic.usuarioToken"));
        model.addAttribute("secretToken", env.getProperty("sigtic.secretToken"));
        model.addAttribute("enviaCorreo", env.getProperty("sigtic.enviaCorreo"));
        model.addAttribute("enviaSms", env.getProperty("sigtic.enviaSms"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "index";
        }else
            return "redirect:/logout";
    }

}
