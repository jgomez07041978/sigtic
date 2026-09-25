package mx.gob.tabasco.cmaig.sigtic.sai.restController;


import mx.gob.tabasco.cmaig.sigtic.sai.model.CatCargosUsuarios;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatCargoCorreoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo/tic")
public class CatCargoCorreoRestController {

    @Autowired
    private CatCargoCorreoService catCargoCorreoService;

    @GetMapping ("/getCargoCorreo/{idEnte}")
    public List<CatCargosUsuarios> obtenCargoCorreo(@PathVariable("idEnte") Long idEnte) {

        return catCargoCorreoService.obtenCargoCorreo(idEnte);
    }



}