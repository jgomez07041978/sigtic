package mx.gob.tabasco.cmaig.sigtic.sac.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.model.OrganigramaBean;
import mx.gob.tabasco.cmaig.sigtic.sac.model.OrganigramaSql;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sac.service.OrganigramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo")
public class OrganigramaRestController {

    @Autowired
    private OrganigramaService organigramaService;


    @GetMapping(path = "/organigrama/{id}")
    public OrganigramaBean getOrganigrama(@PathVariable(value = "id") Long idEntePublico){
        return organigramaService.getOrganigramaEntePublico(idEntePublico);
    }

    @GetMapping(path = "/getOrganigramaId/{id}")
    public List<OrganigramaSql> getOrganigramaId(@PathVariable(value = "id") Long id){
        return organigramaService.getOrganigramaId(id);
    }

    @GetMapping(path = "/getOrganigramaIdNivel2/{id}/{id2}")
    public List<OrganigramaSql> getOrganigramaIdNivel2(@PathVariable(value = "id") Long id, @PathVariable(value = "id2") Long id2){
        return organigramaService.getOrganigramaIdNivel2(id, id2);
    }

    @GetMapping(path = "/getOrganigramaIdNivel3/{id}/{id2}/{id3}")
    public List<OrganigramaSql> getOrganigramaIdNivel3(@PathVariable(value = "id") Long id, @PathVariable(value = "id2") Long id2, @PathVariable(value = "id3") Long id3){
        return organigramaService.getOrganigramaIdNivel3(id, id2, id3);
    }
    @PostMapping("/organigrama/guardarUnidadAdministrativaOrg")
    public UnidadAdministrativa saveUsuario(@RequestBody UnidadAdministrativa unidadAdministrativa){
        return organigramaService.guardarUnidadAdministrativaOrg(unidadAdministrativa);
    }

}
