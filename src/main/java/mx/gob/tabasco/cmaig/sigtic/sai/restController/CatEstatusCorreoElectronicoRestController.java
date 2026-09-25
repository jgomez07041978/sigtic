package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatEstatusCorreoElectronico;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatEstatusCorreoElectronicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo")
public class CatEstatusCorreoElectronicoRestController {

    @Autowired
    private CatEstatusCorreoElectronicoService catEstatusCorreoElectronicoService;

    @GetMapping("/getEstatusCorreo")
    public List<CatEstatusCorreoElectronico> obtenEstatusCorreo() {

        return catEstatusCorreoElectronicoService.getEstatusCorreoElectronico();
    }
}
