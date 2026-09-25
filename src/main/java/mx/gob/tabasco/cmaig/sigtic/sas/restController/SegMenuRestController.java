package mx.gob.tabasco.cmaig.sigtic.sas.restController;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenuArbol;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class SegMenuRestController {

    @Autowired
    private SegMenuService segMenuService;

    @PostMapping("/menuArbol")
    public List<SegMenuArbol> getMenuArbol(@RequestBody SegPerfil perfil){
        return segMenuService.getMenuArbol(perfil);
    }
}