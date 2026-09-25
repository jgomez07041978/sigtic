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
public class CuentaMaestraController {

    @Autowired
    private SegUsuarioService segUsuarioService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @GetMapping("/cuenta-maestra")
    public String cuentaMaestra(Model model) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu = authUser.getListaMenu().stream().filter(p -> p.getCodigo().equals("CUENTA_MAESTRA")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC", authUser.getUsername(), segMenu.getIdMenu());

        model.addAttribute("perfil", segUsuarioService.getPerfil());
        model.addAttribute("listaMenu", authUser.getListaMenuBean());
        model.addAttribute("activarMenuPadre", "CATALOGOS TICS");
        model.addAttribute("activarMenu", "CUENTA_MAESTRA");
        model.addAttribute("titlePage", "Cuenta Maestra");
        model.addAttribute("contextSigtic", servletContext.getContextPath());
        model.addAttribute("activeConsultar", segPerfilPrivilegio.getConsultar());
        model.addAttribute("activeAgregar", segPerfilPrivilegio.getAgregar());
        model.addAttribute("activeEditar", segPerfilPrivilegio.getEditar());
        model.addAttribute("activeEliminar", segPerfilPrivilegio.getEliminar());

        Long idEntePublicoAsignado = authUser.getIdEntePublicoAsignado();
        if (idEntePublicoAsignado == null
                && authUser.getCatUsuarioEntePublico() != null
                && authUser.getCatUsuarioEntePublico().getEntePublico() != null) {
            idEntePublicoAsignado = authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico();
        }
        model.addAttribute("idEntePublicoAsignado", idEntePublicoAsignado);

        return "catalogo_tic/cuenta-maestra";
    }

}
