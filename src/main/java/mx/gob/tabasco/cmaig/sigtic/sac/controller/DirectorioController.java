package mx.gob.tabasco.cmaig.sigtic.sac.controller;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletContext;

@Controller
public class DirectorioController {

    @Autowired
    private ServletContext servletContext;

    @GetMapping("/directorio")
    public String usuarios(Model model) {

        model.addAttribute("titlePage","Directorio Institucional");
        model.addAttribute("contextSigtic",servletContext.getContextPath());

        return "directorio/directorio";
    }
}
