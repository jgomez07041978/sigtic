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
public class CorreoElectronicoController {

    @Autowired
    private SegUsuarioService segUsuarioService;

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;


    @GetMapping("/correo-electronico")
    public String seguridadRole(Model model) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("CORREO_ELECTRONICO")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        boolean existeRolEntePublico = authUser.getListaPerfil().stream().filter(p->p.getNombrePerfil().equals("ADMIN_SIGTIC") || p.getNombrePerfil().equals("SUPER_ADMINISTRADOR") ).findFirst().isPresent();
        boolean enlaceTicCorreo = authUser.getListaPerfil().stream().filter(p->p.getNombrePerfil().equals("ENTE-TIC-CORREO") || p.getNombrePerfil().equals("ENLACE_TIC_CORREO") ).findFirst().isPresent();

        if (existeRolEntePublico){
            model.addAttribute("rolEntePublico",0);
            enlaceTicCorreo=true;
        }
        else
            model.addAttribute("rolEntePublico",1);
        model.addAttribute("perfil",segUsuarioService.getPerfil());
        model.addAttribute("listaMenu",authUser.getListaMenuBean());
        model.addAttribute("activarMenuPadre","SERVICIOS_TICS");
        model.addAttribute("activarMenu","CORREO_ELECTRONICO");
        model.addAttribute("titlePage","Correos Electrónicos");
        model.addAttribute("contextSigtic",servletContext.getContextPath());
        model.addAttribute("activeAgregar",segPerfilPrivilegio.getAgregar());
        model.addAttribute("activeConsultar",segPerfilPrivilegio.getConsultar());
        model.addAttribute("idEntePublicoSession", authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico());
        model.addAttribute("isAdministradorSigtic", authUser.isAdministradorSigtic());
        model.addAttribute("isEnlaceTicCorreo", enlaceTicCorreo);

        return "servicio-tic/correo_electronico";
    }

}
