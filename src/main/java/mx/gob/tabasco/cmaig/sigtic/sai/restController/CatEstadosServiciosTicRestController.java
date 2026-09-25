package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatEstadosServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatEstadosServiciosTicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo/tic")
public class CatEstadosServiciosTicRestController {

    @Autowired
    private CatEstadosServiciosTicService catEstadosServiciosTicService;

    @GetMapping("/getEstadosServiciosTic")
    public List<CatEstadosServiciosTic> obtenEstadosServiciosTics(){
        return catEstadosServiciosTicService.getEstadosServiciosTic();
    }
}
