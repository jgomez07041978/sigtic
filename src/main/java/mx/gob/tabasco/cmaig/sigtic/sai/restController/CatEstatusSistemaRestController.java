package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatEstatusSistema;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatEstatusSistemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo/tic")
public class CatEstatusSistemaRestController {

    @Autowired
    private CatEstatusSistemaService catEstatusSistemaService;

    @GetMapping("/getEstatusSistema")
    public List<CatEstatusSistema> obtenEstatusSistema(){
        return catEstatusSistemaService.getEstatusSistema();
    }
}
