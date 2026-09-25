package mx.gob.tabasco.cmaig.sigtic.sac.restController;


import mx.gob.tabasco.cmaig.sigtic.sac.model.Municipio;
import mx.gob.tabasco.cmaig.sigtic.sac.service.MunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/municipios")
public class MunicipioRestController {

    @Autowired
    private MunicipioService municipioService;

    @GetMapping("{idEntidadFederativa}")
    public List<Municipio> getMunicipios(@PathVariable("idEntidadFederativa") Integer idEntidadFederativa){
        if(idEntidadFederativa == 0)
            return new ArrayList<>();
        else
            return municipioService.getMunicipios(idEntidadFederativa);
    }
}
