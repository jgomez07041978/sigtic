package mx.gob.tabasco.cmaig.sigtic.sai.controller;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.SegUsuarioRepository;
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
public class CatTipoSistemasController {
    @Autowired
    private SegUsuarioService segUsuarioService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @GetMapping("/tipo-sistemas")
    public  String catTipoSistemas(Model model){
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("Tipos_Sistemas")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        model.addAttribute("perfil",segUsuarioService.getPerfil());
        model.addAttribute("listaMenu",authUser.getListaMenuBean());
        model.addAttribute("activarMenuPadre","CATALOGOS TICS");
        model.addAttribute("activarMenu","Tipos_Sistemas");
        model.addAttribute("titlePage","Tipos de Sistemas");
        model.addAttribute("contextSigtic",servletContext.getContextPath());
        model.addAttribute("activeAgregar",segPerfilPrivilegio.getAgregar());
        model.addAttribute("activeConsultar",segPerfilPrivilegio.getConsultar());

        return "catalogo_tic/tipos_sistemas";
    }
}
