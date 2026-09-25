package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatEstatusServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatEstatusServiciosTicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo/tic")
public class CatEstatusServiciosTicRestController {

    @Autowired
    private CatEstatusServiciosTicService catEstatusServiciosTicService;

    @GetMapping("/getEstatusServiciosTic")
    public List<CatEstatusServiciosTic> obtenEstatusServiciosTic() {
        return catEstatusServiciosTicService.getEstatusServiciosTic();
    }
}