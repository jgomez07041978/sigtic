package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosFirma;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatSubtipoServiciosFirmaService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subtipo_servicio_firma")
public class CatSubtipoServiciosFirmaRestController {

    @Autowired
    private CatSubtipoServiciosFirmaService catSubtipoServiciosFirmaService;


    @PostMapping("/tablaSubtipoServiciosFirma")
    public JSONObject getSubtipoServiciosFirma(@RequestBody CatSubtipoServiciosFirma catSubtipoServiciosFirma){
        //UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        List<CatSubtipoServiciosFirma> listaSubtipoServicioFirma = catSubtipoServiciosFirmaService.getSubtipoServicioFirmas(catSubtipoServiciosFirma.getCatSubtipoServiciosTic());

        listaSubtipoServicioFirma.forEach(reg ->
        {
            JSONArray json = new JSONArray();
            json.add(reg.getEntePublico().getCveEntePublico());
            json.add(reg.getEntePublico().getNombreEntePublico());
            json.add(reg.getNumeroFirmas());
            String enlaces = "<div class=\"btn-group\"> ";
                enlaces += "<a href=#' class='btn btn-success btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Editar Configuración Firma' onclick='editarSubtipoServiciosFirma(" + reg.getIdSubtipoServicioFirma() + ")'><i class='bi bi-pencil-square'></i></a>";
                enlaces += "</br>";
                //enlaces += "<a href='javascript:void(0);' class='btn btn-danger btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Configuración Firma' onclick='eliminarSubTipoServicioFirma(" + reg.getIdSubtipoServicioFirma() + ")'><i class='bi bi-trash3'></i></a>";
            enlaces += "</div>";
            json.add(enlaces);
            array.add(json);
        });
        jsonObject.put("isTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/saveSubtipoServiciosFirma")
    public CatSubtipoServiciosFirma saveSubtipoServiciosFirma(@RequestBody CatSubtipoServiciosFirma catSubtipoServiciosFirma){
        return catSubtipoServiciosFirmaService.guardarSubtipoServicioFirma(catSubtipoServiciosFirma);
    }

    @GetMapping("/getSubtipoServiciosFirma/{id}")
    public CatSubtipoServiciosFirma getSubtipoServiciosFirmaId(@PathVariable("id") Long idSubtipoServicioFirma){
        return catSubtipoServiciosFirmaService.getSubtipoServicioFirmaId(idSubtipoServicioFirma);
    }

    @GetMapping("/getSubtipoServiciosFirmaEnte/{idEnte}/{idSubtipoServicio}")
    public CatSubtipoServiciosFirma getSubtipoServiciosFirmaEnteId(@PathVariable("idEnte") Long idEnte, @PathVariable("idSubtipoServicio") Long idSubtipoServicio){
        return catSubtipoServiciosFirmaService.getSubtipoServicioFirmaEnteId(idEnte, idSubtipoServicio);
    }

    @DeleteMapping("/deleteSubTipoServicioFirma")
    public void deleteSubTipoServicioFirma(@RequestBody CatSubtipoServiciosFirma catSubtipoServiciosFirma){
        catSubtipoServiciosFirmaService.eliminarSubtipoServicioFirma(catSubtipoServiciosFirma);
    }

}
