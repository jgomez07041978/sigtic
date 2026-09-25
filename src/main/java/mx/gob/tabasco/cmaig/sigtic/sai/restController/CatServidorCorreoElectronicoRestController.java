package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatServidorCorreoElectronico;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatServidorCorreoElectronicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo/tic")
public class CatServidorCorreoElectronicoRestController {

    @Autowired
    private CatServidorCorreoElectronicoService catServidorCorreoElectronicoService;

    @GetMapping("/getServidorCorreoElectronico/{id}")
    public List<CatServidorCorreoElectronico> obtenServidorCorreosElectronico(@PathVariable("id") Long idEntePublico){

        return catServidorCorreoElectronicoService.obtenServidorCorreoElectronico(idEntePublico);
    }

    @GetMapping("/getServidorCorreoElectronicoAll")
    public List<CatServidorCorreoElectronico> obtenServidorCorreosElectronicoAll(){

        return catServidorCorreoElectronicoService.obtenServidorCorreoElectronicoAll();
    }
}
