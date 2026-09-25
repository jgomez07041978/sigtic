package mx.gob.tabasco.cmaig.sigtic.sac.restController;


import mx.gob.tabasco.cmaig.sigtic.sac.model.Localidad;
import mx.gob.tabasco.cmaig.sigtic.sac.service.LocalidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/localidades")
public class LocalidadRestController {


    @Autowired
    private LocalidadService localidadService;


    @GetMapping("{idEntidadFederativa}/{idMunicipio}")
    public List<Localidad> getLocalidades(@PathVariable("idEntidadFederativa") Integer idEntidadFederativa, @PathVariable("idMunicipio") Integer idMunicipio){
        if(idEntidadFederativa == 0 || idMunicipio==0)
            return new ArrayList<>();
        else return localidadService.getLocalidades(idEntidadFederativa, idMunicipio);
    }
}
