package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoImpactoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatTipoImpactoServiciosTicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo/tic")
public class CatTipoImpactoServiciosTicRestController {
    @Autowired
    private CatTipoImpactoServiciosTicService catTipoImpactoServiciosTicService;

    @GetMapping("/getCatTipoImpactoServiciosTic")
    public List<CatTipoImpactoServiciosTic> otenCatTipoImpactoServiciosTic(){
        return catTipoImpactoServiciosTicService.getCatTipoImpactoServiciosTic();
    }
}
