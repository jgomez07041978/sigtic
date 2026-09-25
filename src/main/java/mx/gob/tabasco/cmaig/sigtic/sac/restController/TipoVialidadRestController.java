package mx.gob.tabasco.cmaig.sigtic.sac.restController;


import mx.gob.tabasco.cmaig.sigtic.sac.model.TipoVialidad;
import mx.gob.tabasco.cmaig.sigtic.sac.service.TipoVialidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tipo-vialidad")
public class TipoVialidadRestController {

    @Autowired
    private TipoVialidadService tipoVialidadService;

    @GetMapping
    public List<TipoVialidad> getTipoVialidad(){
        return tipoVialidadService.getTipoVialidad();
    }

}
