package mx.gob.tabasco.cmaig.sigtic.sai.restController;


import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoUsuarioServicio;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatTipoUsuarioServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tipoUsuarioServicio")
public class CatTipoUsuarioServicioRestController {

    @Autowired
    private CatTipoUsuarioServicioService catTipoUsuarioServicioService;

    @PostMapping("/getAll")
    public List<CatTipoUsuarioServicio> getMisCartaResponsiva(){
        return catTipoUsuarioServicioService.getTipoUsuarioServicio();
    }
}
