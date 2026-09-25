package mx.gob.tabasco.cmaig.sigtic.sigget.restController;


import mx.gob.tabasco.cmaig.sigtic.sac.model.SacCatUsuarioEntePublicoView;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Plantilla;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.PlantillasUsuario;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.UsuarioPlantilla;
import mx.gob.tabasco.cmaig.sigtic.sigget.service.UsuarioPlantillaService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario-plantilla")
public class UsuarioPlantillaRestController {

    @Autowired
    private UsuarioPlantillaService usuarioPlantillaService;


    @PostMapping("/filtrarUsariosPlantilla")
    public ResponseEntity<?> getFiltrarUsuariosPlantilla(@RequestBody UsuarioPlantilla usuarioPlantilla){
        return ResponseEntity.ok(usuarioPlantillaService.getUsuariosPlantillas(usuarioPlantilla));
    }

   /* @GetMapping("/allUsers")
    public JSONObject getUsuariosPlantillas()
    {
        JSONObject jsonObject = new JSONObject();

        List<UsuarioPlantilla> lista =  usuarioPlantillaService.getUsuariosPlantillas();

        JSONArray array = new JSONArray();
        if (lista.size() > 0) {
            for (UsuarioPlantilla usuarioPlantilla : lista)
            {   JSONArray json = new JSONArray();
                json.add(usuarioPlantilla.getCurpUsuario());
                json.add(usuarioPlantilla.getApellidoPaterno());
                json.add(usuarioPlantilla.getApellidoMaterno());
                json.add(usuarioPlantilla.getNombreUsuario());
                json.add(usuarioPlantilla.getCveEntePublico()+" "+usuarioPlantilla.getDescEntePublico());
                json.add(usuarioPlantilla.getDescUnidadAdministrativa());
                json.add(usuarioPlantilla.getNumeroPlantilla());
                json.add(usuarioPlantilla.getCveEstatus());

                String acciones="";
                acciones +="<a href='#' class='btn btn-outline-success mr-1' data-toggle='tooltip' data-placement='top' title='Plantillas Asignadas' onclick='openListaPlantilla(\""+ usuarioPlantilla.getRfcUsuario() +"\",\""+usuarioPlantilla.getNombreUsuario()+"\",\""+usuarioPlantilla.getApellidoPaterno()+"\",\""+usuarioPlantilla.getApellidoMaterno()+"\","+usuarioPlantilla.getIdEntePublico()+","+usuarioPlantilla.getIdUnidadAdministrativa()+")'><i class='fas fa-plus'></i></a>";
                json.add(acciones);
                array.add(json);

            }
        }

        jsonObject.put("aaData", array);

        return jsonObject;
    }*/

    @GetMapping("/plantillasAsignadas/{idEntePublico}/{rfcUsuario}")
    public JSONObject getPlantillasAsignadas(@PathVariable Long idEntePublico, @PathVariable String rfcUsuario)
    {
        JSONObject jsonObject = new JSONObject();

        List<PlantillasUsuario> lista =  usuarioPlantillaService.getPlantillasAsignadas(idEntePublico, rfcUsuario);

        JSONArray array = new JSONArray();
        if (lista.size() > 0) {
            for (PlantillasUsuario plantillasUsuario : lista)
            {   JSONArray json = new JSONArray();
                if (plantillasUsuario.getPlantilla()!=null) {

                    String acciones="<button type=\"button\" class=\"btn btn-danger\" onclick=\"removePlantilla("+plantillasUsuario.getIdPlantillaUsuario()+")\"><i class=\"fas fa-angle-left\"></i></button>";
                    json.add(acciones);
                    json.add(plantillasUsuario.getPlantilla().getNombrePlantilla());
                    array.add(json);
                }

            }
        }

        jsonObject.put("aaData", array);

        return jsonObject;
    }

    @GetMapping("/plantillasDisponibles/{idEntePublico}/{rfcUsuario}")
    public JSONObject getPlantillasDisponibles(@PathVariable Long idEntePublico, @PathVariable String rfcUsuario)
    {
        JSONObject jsonObject = new JSONObject();

        List<Long> lista =  usuarioPlantillaService.getPlantillasIds(idEntePublico, rfcUsuario);

        List<Plantilla> listaPlantillas = usuarioPlantillaService.getPlantillasDisponibles(lista);

        JSONArray array = new JSONArray();
        for (Plantilla plantilla : listaPlantillas)
        {   JSONArray json = new JSONArray();
            if (plantilla!=null) {

                json.add(plantilla.getNombrePlantilla());

                String acciones="<button type=\"button\" class=\"btn btn-success\" onclick=\"addPlantilla("+plantilla.getIdPlantilla()+")\"><i class=\"fas fa-angle-right\"></i></button>";
                 json.add(acciones);
                array.add(json);
            }

        }

        jsonObject.put("aaData", array);

        return jsonObject;
    }

    @PostMapping
    public PlantillasUsuario addPlantilla(@RequestBody PlantillasUsuario plantillasUsuario){
        return usuarioPlantillaService.addPlantilla(plantillasUsuario);
    }

    @DeleteMapping
    public void deletePlantilla(@RequestBody PlantillasUsuario plantillasUsuario){
        usuarioPlantillaService.removePlantilla(plantillasUsuario);
    }

}
