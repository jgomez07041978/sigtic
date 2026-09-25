package mx.gob.tabasco.cmaig.sigtic.sac.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.model.Arbol;
import mx.gob.tabasco.cmaig.sigtic.sac.service.UnidadesAdministrativaService;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegAccesoUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.SegPermisosBean;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegAccesoUsuarioService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tree")
public class TreeRestController {

        @Autowired
        private UnidadesAdministrativaService unidadesAdministrativaService;

        @Autowired
        private SegAccesoUsuarioService segAccesoUsuarioService;

        @GetMapping(path = "/{id}")
        public List<Arbol> getArbol(@PathVariable(value = "id") Long id){
            return unidadesAdministrativaService.getUnidadAdministrativaAll(id);
        }

        @GetMapping(path = "/free/{id}")
        public List<Arbol> getArbolFree(@PathVariable(value = "id") Long id){
                return unidadesAdministrativaService.getUnidadAdministrativaAllFree(id);
        }


        @GetMapping(path = "/getPorInstitucionFree/{id}")
        public List<Arbol> getArbolInstitucionFree(@PathVariable(value = "id") Long id){
                return unidadesAdministrativaService.getUnidadAdministrativaAllPorInstitucionFree(id);
        }

        @GetMapping(path = "/getPorInstitucion/{id}")
        public List<Arbol> getArbolInstitucion(@PathVariable(value = "id") Long id){
                return unidadesAdministrativaService.getUnidadAdministrativaAllPorInstitucion(id);
        }

        @GetMapping(path = "/getAllUnidadAdminPresupuestal/{id}")
        public List<Arbol> getArbolUnidadAdminPresupuestal(@PathVariable(value = "id") Long id){
                return unidadesAdministrativaService.getUnidadAdministrativaPresupuestal(id);
        }

        @GetMapping(path = "/getPorIdUsuario/{id}")
        public List<Arbol> getPorIdUsuario(@PathVariable(value = "id") Long id){
                return unidadesAdministrativaService.getUnidadAdministrativaAllPorUsuario(id);
        }

        @PostMapping("/addPermiso")
        public SegAccesoUsuario addPermiso(@RequestBody SegPermisosBean segPermisosBean){
                return segAccesoUsuarioService.addPermisosTreeAll(segPermisosBean);
        }

        @PostMapping("/deletePermiso")
        public JSONObject deletePermiso(@RequestBody SegPermisosBean segPermisosBean){
                return segAccesoUsuarioService.deletePermisosTreeAll(segPermisosBean);
        }

}
