package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sai.model.Bien;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CensoBienService;
import net.minidev.json.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.List;

@RestController
@RequestMapping("/api/censo-bien")
public class CensoBienRestController {
    @Autowired
    private CensoBienService censoBienService;

    @GetMapping("/concentradoBienesPorEntePublico")
    public JSONObject concentradoBienesPorEntePublico() {
        return censoBienService.getConcentradoBienesPorEntePublico();
    }

    @GetMapping("/concentradoBienesPorUnidadAdministrativa/{id}")
    public JSONObject concentradoBienesPorUnidadAdministrativa(@PathVariable("id") Long idEntePublico) {
        return censoBienService.getConcentradoBienesPorUnidadAdministrativa(idEntePublico);
    }

    @GetMapping("/listaBienesPorUnidad/{idUnidad}")
    public JSONObject listaBienesPorUnidad(@PathVariable("idUnidad") Long idUnidad) {
        List<Bien> listaBien = censoBienService.getListaBienesPorUnidad(idUnidad);
        DecimalFormat formatea = new DecimalFormat("###,###,###.##");

        String unidadAdministrativa="";

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (Bien reg : listaBien)
        {
            unidadAdministrativa=reg.getUnidadAdministrativa().getNombreUnidadAdministrativa();
            JSONArray json = new JSONArray();
            json.add(reg.getNumeroInventario());
            json.add(reg.getDescripcionBien());
            json.add(reg.getFechaAltaText());
            json.add("$ "+formatea.format(reg.getCosto()));
            json.add(reg.getNota());
            json.add(reg.getFechaCorteText());

            array.add(json);

        }
        jsonObject.put("aaData", array);
        jsonObject.put("unidadAdministrativa", unidadAdministrativa);
        return jsonObject;

    }

}
