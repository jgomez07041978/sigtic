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
public class ManualController {

    @Autowired
    private SegUsuarioService segUsuarioService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @GetMapping("/manuales")
    public String manuales(Model model) {

        model.addAttribute("titlePage","Manuales");
        model.addAttribute("contextSigtic",servletContext.getContextPath());

        return "manuales/manual_free";
    }

    @GetMapping("/manuales_admin")
    public String manualesAdmin(Model model) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("MANUALES")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        model.addAttribute("perfil",segUsuarioService.getPerfil());
        model.addAttribute("listaMenu",authUser.getListaMenuBean());
        model.addAttribute("activarMenuPadre","CONFIGURACION");
        model.addAttribute("activarMenu","MANUALES");
        model.addAttribute("titlePage","Manuales");
        model.addAttribute("activeAgregar",segPerfilPrivilegio.getAgregar());
        model.addAttribute("activeConsultar",segPerfilPrivilegio.getConsultar());
        model.addAttribute("contextSigtic",servletContext.getContextPath());

        return "manuales/manual_admin";
    }

    @GetMapping("/manual_tecnico")
    public String manualTecnico(Model model) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("MANUALES_TECNICO")).findFirst().get();
       // model.addAttribute("activarMenu","MANUALES_TECNICO");
        model.addAttribute("titlePage","Manuales");

        model.addAttribute("perfil",segUsuarioService.getPerfil());
        model.addAttribute("listaMenu",authUser.getListaMenuBean());
        model.addAttribute("contextSigtic",servletContext.getContextPath());

        return "manuales/manual_tecnico";
    }
}
