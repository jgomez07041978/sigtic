package mx.gob.tabasco.cmaig.sigtic.sac.restController;


import mx.gob.tabasco.cmaig.sigtic.sac.model.EntidadFederativa;
import mx.gob.tabasco.cmaig.sigtic.sac.service.EntidadFederativaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/entidad-federativa")
public class EntidadFederativaRestController {

    @Autowired
    private EntidadFederativaService entidadFederativaService;

    @GetMapping
    public List<EntidadFederativa> getEntidadesFederativas(){
        return entidadFederativaService.getEntdadesFederativas();
    }

}
