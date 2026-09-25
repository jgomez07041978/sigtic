package mx.gob.tabasco.cmaig.sigtic.sas.controller;


import mx.gob.tabasco.cmaig.sigtic.Utileria.TokenUtils;
import mx.gob.tabasco.cmaig.sigtic.sai.model.Usuario;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuarioTemporal;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegUsuarioService;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegUsuarioTemporalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletContext;
import java.util.Optional;

@Controller
public class RecuperaAccesoController {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private SegUsuarioTemporalService segUsuarioTemporalService;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @Autowired
    private SegUsuarioService segUsuarioService;

    @Autowired
    private Environment env;

    @GetMapping("/recuperar-acceso")
    public String recuperarAcceso(Model model, @RequestParam(name="token") String token) {
        model.addAttribute("contextSigtic",servletContext.getContextPath());
        String usuario=TokenUtils.getCorreoToken(token);

        if (usuario==null)
            model.addAttribute("error","El token no es valido");
        else
        if (usuario.equals("Expired")){
            model.addAttribute("error","El token ya expiró");
        }
        else{
            Optional<SegUsuario> segUsuario = segUsuarioService.getUsuarioOptional(usuario);

            if (!segUsuario.isPresent())
                model.addAttribute("error","El usuario no existe");

            if (segUsuario.get().getTokenActivo().equals("N"))
                model.addAttribute("error","El token ya fue utilizado");


            model.addAttribute("usuario",usuario);
            if (segUsuario.get().getCatUsuarioEntePublico()!=null)
                model.addAttribute("nombreComleto",segUsuario.get().getCatUsuarioEntePublico().getCatUsuario().getNombreCompleto());
            else
                model.addAttribute("nombreComleto","");

        }

        return "seguridad/recuperar-acceso";
    }

}
