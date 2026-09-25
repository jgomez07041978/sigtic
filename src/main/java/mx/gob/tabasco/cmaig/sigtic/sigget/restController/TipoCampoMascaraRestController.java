package mx.gob.tabasco.cmaig.sigtic.sigget.restController;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.TipoCampoMascara;
import mx.gob.tabasco.cmaig.sigtic.sigget.service.TipoCampoMascaraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tipoCampoMascara")
public class TipoCampoMascaraRestController {

    @Autowired
    private TipoCampoMascaraService tipoCampoMascaraService;

/*    @PostMapping("/getTipoCampoMascaraPorTipoCampo")
    public JSONObject getTipoCampoMascaraPorTipoCampo(@RequestBody TipoCampoMascara tipoCampoMascara){
        // List<CamposBean> camposAll = camposService.getCamposAll(camposBean)

        List<TipoCampoMascara> listaTipoCampoMascara = tipoCampoMascaraService.getTipoCamposMascaraPorTipoCampo(tipoCampoMascara.getCveTipoCampo());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

            for (TipoCampoMascara tipoCampoMascara1 : listaTipoCampoMascara)
            {

                JSONArray json = new JSONArray();
                json.add(tipoCampoMascara1.getNombreMascara());
                json.add(tipoCampoMascara1.getMascara());

                String acciones = "";

                acciones +="<a href='#' class='btn btn-primary mr-1'  onclick='seleccionar("+ tipoCampoMascara1.getIdTipoCampoMascara()+")' data-toggle='tooltip' data-placement='top' title='Seleccionar Máscara'><i class='fas fa-plus-circle'></i></a>";
                json.add(acciones);

                array.add(json);

            }

        jsonObject.put("aaData", array);
        return jsonObject;
    }*/

    @GetMapping("/getTipoCampoMascaraPorTipoCampo/{cveTipoCampo}")
    public List<TipoCampoMascara> getTipoCampoMascaraPorTipoCampo(@PathVariable(value = "cveTipoCampo") String cveTipoCampo){

        List<TipoCampoMascara> listaTipoCampoMascara=null;
        if (cveTipoCampo==null)
            listaTipoCampoMascara=new ArrayList<>();
        else
            listaTipoCampoMascara = tipoCampoMascaraService.getTipoCamposMascaraPorTipoCampo(cveTipoCampo);

        return listaTipoCampoMascara;
    }

}
