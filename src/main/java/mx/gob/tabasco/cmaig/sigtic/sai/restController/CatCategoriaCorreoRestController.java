package mx.gob.tabasco.cmaig.sigtic.sai.restController;


import mx.gob.tabasco.cmaig.sigtic.sai.model.CatCategoria;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatCategoriaCorreoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo/tic")
public class CatCategoriaCorreoRestController {

    @Autowired
    private CatCategoriaCorreoService catCategoriaCorreoService;


    @GetMapping ("/getCategoria/{idEnte}")
    public List<CatCategoria> obtenCategoriaCorreo(@PathVariable("idEnte") Long idEnte) {

        return catCategoriaCorreoService.obtenCategoriaCorreo(idEnte);
    }


}
