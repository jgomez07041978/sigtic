package mx.gob.tabasco.cmaig.sigtic.sigget.controller;

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
public class CargaMasivaController {
    @Autowired
    private SegUsuarioService segUsuarioService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @GetMapping("/carga_masiva")
    public String cargaMasiva(Model model) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("CARGA_MASIVA")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());


        model.addAttribute("perfil",segUsuarioService.getPerfil());
        model.addAttribute("listaMenu",authUser.getListaMenuBean());
        model.addAttribute("activarMenuPadre","CARGA");
        model.addAttribute("activarMenu","CARGA_MASIVA");
        model.addAttribute("titlePage","Carga Masiva");
        model.addAttribute("activeAgregar",segPerfilPrivilegio.getAgregar());
        model.addAttribute("activeConsultar",segPerfilPrivilegio.getConsultar());
        model.addAttribute("contextSigtic",servletContext.getContextPath());
        model.addAttribute("idEntePublicoSession", authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico());

        return "carga_masiva/carga-masiva";
    }

}
