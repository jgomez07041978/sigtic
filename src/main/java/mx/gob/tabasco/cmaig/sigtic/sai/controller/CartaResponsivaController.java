package mx.gob.tabasco.cmaig.sigtic.sai.controller;

import mx.gob.tabasco.cmaig.sigtic.Utileria.TokenUtils;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CartaResponsiva;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CartaResponsivaService;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuarioTemporal;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletContext;
import java.util.Optional;

@Controller
public class CartaResponsivaController {
    @Autowired
    private SegUsuarioService segUsuarioService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @Autowired
    private CartaResponsivaService cartaResponsivaService;

    @Autowired
    private Environment env;

    @GetMapping("/carta-responsiva")
    public String portales(Model model) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("CARTA_RESPONSIVA")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        boolean existeRolEnlaceCartaResponsiva = authUser.getListaPerfil().stream().filter(p->p.getNombrePerfil().equals("ENLACE-TIC-CARTA-RESPONSIVA") ).findFirst().isPresent();
        boolean existeControlFirma = authUser.getListaPerfil().stream().filter(p->p.getNombrePerfil().equals("CONTROL_FIRMA_SUBTIPO") ).findFirst().isPresent();
        boolean adminCr = authUser.getListaPerfil().stream().filter(p-> p.getNombrePerfil().equals("ADMINISTRA_CR")).findFirst().isPresent();

        model.addAttribute("isRolEnlaceCartaResponsiva",existeRolEnlaceCartaResponsiva);
        model.addAttribute("isControlFirma",existeControlFirma);
        model.addAttribute("perfil",segUsuarioService.getPerfil());
        model.addAttribute("listaMenu",authUser.getListaMenuBean());
        model.addAttribute("activarMenuPadre","SERVICIOS_TICS");
        model.addAttribute("activarMenu","CARTA_RESPONSIVA");
        model.addAttribute("titlePage","Carta Responsiva");
        model.addAttribute("activeConsultar",segPerfilPrivilegio.getConsultar());
        model.addAttribute("activeAgregar",segPerfilPrivilegio.getAgregar());
        model.addAttribute("contextSigtic",servletContext.getContextPath());
        model.addAttribute("activeAgregar",segPerfilPrivilegio.getAgregar());
        model.addAttribute("generaToken", env.getProperty("sigtic.generaToken"));
        model.addAttribute("usuarioToken", env.getProperty("sigtic.usuarioToken"));
        model.addAttribute("secretToken", env.getProperty("sigtic.secretToken"));
        model.addAttribute("enviaCorreo", env.getProperty("sigtic.enviaCorreo"));
        model.addAttribute("generaQr", env.getProperty("sigtic.qr"));

        model.addAttribute("idEntePublicoSession", authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico());
        model.addAttribute("isAdministradorSigtic", authUser.isAdministradorSigtic());
        model.addAttribute("isAdministradorCr", adminCr);

        return "servicio-tic/carta_responsiva";
    }

    @GetMapping("/getCr/{id}")
    public String getCartaResponsiva(Model model, @PathVariable("id") Long idCartaResponsiva) {
        model.addAttribute("contextSigtic",servletContext.getContextPath());

        CartaResponsiva cartaResponsiva = cartaResponsivaService.getCartaResponsivaId(idCartaResponsiva);
        model.addAttribute("cartaResponsiva", cartaResponsiva);

        return "servicio-tic/carta_responsiva_get";
    }

    @GetMapping("/carta-responsiva-aceptar")
    public String getCartaResponsivaAceptar(Model model, @RequestParam(name="token") String token) {

        model.addAttribute("contextSigtic",servletContext.getContextPath());

        String idCartaResponsivaToken=TokenUtils.getCorreoToken(token);

        if (idCartaResponsivaToken==null)
            model.addAttribute("error","El token no es valido");
        else
        if (idCartaResponsivaToken.equals("Expired")){
            model.addAttribute("error","El token ya expiroó");
        }
        else{
            CartaResponsiva cartaResponsiva = cartaResponsivaService.getCartaResponsivaId(Long.valueOf(idCartaResponsivaToken));
            model.addAttribute("cartaResponsiva", cartaResponsiva);
            model.addAttribute("tokenUrl", token);
        }


        return "servicio-tic/carta_responsiva_aceptar";
    }


    @GetMapping("/misCartaResponsiva")
    public String miCartaResponsiva(Model model) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("MI_CARTA_RESPONSIVA")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        model.addAttribute("perfil",segUsuarioService.getPerfil());
        model.addAttribute("listaMenu",authUser.getListaMenuBean());
        model.addAttribute("activarMenu","CARTA_RESPONSIVA");
        model.addAttribute("titlePage","Carta Responsiva");
        model.addAttribute("activeConsultar",segPerfilPrivilegio.getConsultar());
        model.addAttribute("activeAgregar",segPerfilPrivilegio.getAgregar());
        model.addAttribute("contextSigtic",servletContext.getContextPath());
        model.addAttribute("activeAgregar",segPerfilPrivilegio.getAgregar());
        model.addAttribute("generaToken", env.getProperty("sigtic.generaToken"));
        model.addAttribute("usuarioToken", env.getProperty("sigtic.usuarioToken"));
        model.addAttribute("secretToken", env.getProperty("sigtic.secretToken"));
        model.addAttribute("enviaCorreo", env.getProperty("sigtic.enviaCorreo"));

        model.addAttribute("idEntePublicoSession", authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico());

        return "servicio-tic/mi-carta-responsiva";
    }


}
