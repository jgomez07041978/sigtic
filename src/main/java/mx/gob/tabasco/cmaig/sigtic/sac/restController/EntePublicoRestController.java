package mx.gob.tabasco.cmaig.sigtic.sac.restController;


import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.service.EntePublicoService;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/entePublico")
public class EntePublicoRestController {

    @Autowired
    private EntePublicoService entePublicoService;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @PostMapping("/listaFree")
    public List<EntePublico> getEntesPublicosFree(){
        return entePublicoService.getEntesPublicos();
    }


    @PostMapping("lista")
    public List<EntePublico> getEntesPublicosPorUsuarioLogueado(){

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();



        if (authUser.isAdministradorSigtic())
            return entePublicoService.getEntesPublicos();
        else
            return entePublicoService.getEntesPublicos(authUser.getUsername());
    }

    @PostMapping("listaEnteCr")
    public List<EntePublico> getEntesPublicosCr(){

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean adminCr = authUser.getListaPerfil().stream().filter(p-> p.getNombrePerfil().equals("ADMINISTRA_CR")).findFirst().isPresent();
        boolean enlaceTicCr = authUser.getListaPerfil().stream().filter(p-> p.getNombrePerfil().equals("ENLACE-TIC-CARTA-RESPONSIVA")).findFirst().isPresent();

        if (adminCr || authUser.isAdministradorSigtic())
            return entePublicoService.getEntesPublicos();
        else
        if (enlaceTicCr)
            return entePublicoService.getEntesPublicos(authUser.getUsername());
        else
            return new ArrayList<>();
    }



    @PostMapping("listaCartaResponsiva")
    public List<EntePublico> getEntesPublicosParaCartaResponsiva(){

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authUser.isAdministradorSigtic())
            return entePublicoService.getEntesPublicos();
        else
            return entePublicoService.getEntesPublicoSaig();
    }

    @GetMapping("usuario")
    public List<EntePublico> getEntePublicoUsuario(){

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authUser.isAdministradorSigtic())
            return entePublicoService.getEntesPublicos();
        else
            return entePublicoService.getEntePublicoUsuario(authUser.getUsername(),"SIGTIC","VIG");
    }


}
