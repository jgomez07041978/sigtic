package mx.gob.tabasco.cmaig.sigtic.sigget.restController;


import mx.gob.tabasco.cmaig.sigtic.sigget.model.*;
import mx.gob.tabasco.cmaig.sigtic.sigget.service.CamposService;
import mx.gob.tabasco.cmaig.sigtic.sigget.service.PlantillasService;
import mx.gob.tabasco.cmaig.sigtic.sigget.service.TipoCamposService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/campos-plantilla")
public class CamposRestController {

    @Autowired
    private CamposService camposService;

    @Autowired
    private PlantillasService plantillasService;

    @Autowired
    private TipoCamposService tipoCamposService;

    @PostMapping("/getCampoPorPlantilla")
    public JSONObject getCamposAll(@RequestBody CamposBean camposBean){
        // List<CamposBean> camposAll = camposService.getCamposAll(camposBean)
        Plantilla plantilla = plantillasService.getPlantillaId(camposBean.getIdPlantilla());

        List<Campos> camposAll = camposService.getCamposPorPlantilla(plantilla, "VIG");

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        if (camposAll.size() > 0) {

            for (Campos campo : camposAll)
            {

                JSONArray json = new JSONArray();
                json.add(campo.getCamposPK().getNumCampo());
                json.add(campo.getNombreCampo());
                json.add(campo.getCampoBaseDatos());
                json.add(campo.getTipoCampo().getNombreTipoCampo());
                json.add(campo.getUbicacionColumna());
                json.add(campo.getEtiquetaColumna());

                String acciones = "";

                acciones +="<div class='btn-group' role='group' aria-label='Default button group'>" +
                        "<a href='#' class='btn btn-success mr-1'  onclick='editarCampo("+ campo.getCamposPK().getIdPlantilla()+","+campo.getCamposPK().getNumCampo() +")' data-toggle='tooltip' data-placement='top' title='Editar Campo'><i class='fas fa-edit'></i></a>";

                if (campo.getCveEstatus().equals("VIG"))
                    acciones +="<a href='#' class='btn btn-danger' data-toggle='tooltip' data-placement='top' title='Eliminar Campo' onclick='eliminarCampo("+ campo.getCamposPK().getIdPlantilla()+","+campo.getCamposPK().getNumCampo() +")'><i class='fas fa-trash'></i></a>";

                acciones+= "</div>";
                json.add(acciones);

                array.add(json);

            }
        }
        jsonObject.put("aaData", array);
        jsonObject.put("totalCampos", camposAll.size());
        return jsonObject;
    }

    @PostMapping("/asignados")
    public String getNumeroAsignados(@RequestBody CamposBean camposBean){
        Plantilla plantilla = plantillasService.getPlantillaId(camposBean.getIdPlantilla());
        List<Campos> camposAll = camposService.getCamposPorPlantilla(plantilla, "VIG");

        String numeros="";

        for(Campos campo:camposAll){
            if (!numeros.equals(""))
                numeros+=",";

            numeros+=campo.getCamposPK().getNumCampo();
        }

        return numeros;
    }

    @PostMapping
    public Campos saveCampos(@RequestBody Campos campo){
        //CamposPK camposPK=new CamposPK(campo.getIdPlantillaCampoTemp(), campo.getNumCampo());
        CamposPK camposPK=new CamposPK();
        camposPK.setIdPlantilla(campo.getIdPlantillaCampoTemp());
        camposPK.setNumCampo(campo.getNumCampo());
        campo.setCamposPK(camposPK);
        campo.setCveEstatus("VIG");
        campo.setNumOrdenCampo(campo.getCamposPK().getNumCampo());
        TipoCampo tipoCampo=tipoCamposService.findTipoCamposById(campo.getCveTipoCampo());
        campo.setTipoCampo(tipoCampo);
        return camposService.saveCampos(campo);
    }

    @PutMapping
    public Campos updateCampos(@RequestBody Campos campo){
        CamposPK camposPK=new CamposPK();
        camposPK.setIdPlantilla(campo.getIdPlantillaCampoTemp());
        camposPK.setNumCampo(campo.getNumCampo());
        campo.setCamposPK(camposPK);
        campo.setCveEstatus("VIG");
        campo.setNumOrdenCampo(campo.getCamposPK().getNumCampo());
        TipoCampo tipoCampo=tipoCamposService.findTipoCamposById(campo.getCveTipoCampo());
        campo.setTipoCampo(tipoCampo);

        return camposService.updateCampos(campo);
    }

    @DeleteMapping
    public void deleteCampos(@RequestBody CamposPK camposPK){
        camposService.deleteCampo(camposPK);
    }

    @GetMapping("/unico/{idPlantilla}/{numCampo}")
    public Campos getCampo(@PathVariable Long idPlantilla, @PathVariable Integer  numCampo){
        CamposPK campo=new CamposPK();
        campo.setIdPlantilla(idPlantilla);
        campo.setNumCampo(numCampo);
        return camposService.getCamposId(campo);
    }

    @GetMapping("/lista-campos/{idPlantilla}/{numCampo}")
    public List<Campo> getListaCampos(@PathVariable Long idPlantilla, @PathVariable Integer  numCampo){

        Plantilla plantilla = plantillasService.getPlantillaId(idPlantilla);
        List<Campos> listaCampos = camposService.getCamposPorPlantilla(plantilla, "VIG");
        List<String> listaAsignada = listaCampos.stream().map(Campos::getUbicacionColumna).collect(Collectors.toList());
        if (numCampo>-1){
            CamposPK camposPK = new CamposPK();
            camposPK.setIdPlantilla(idPlantilla);
            camposPK.setNumCampo(numCampo);
            Campos campos= camposService.getCamposId(camposPK);
            listaAsignada.remove(campos.getUbicacionColumna());
        }

        return camposService.getCamposDisponibles(listaAsignada);
    }

    @GetMapping("/lista-camposBd/{idPlantilla}/{numCampo}")
    public List<Campo> getListaCamposBd(@PathVariable Long idPlantilla, @PathVariable Integer  numCampo){

        Plantilla plantilla = plantillasService.getPlantillaId(idPlantilla);
        List<Campos> listaCampos = camposService.getCamposPorPlantilla(plantilla, "VIG");
        List<String> listaAsignada = listaCampos.stream().map(Campos::getCampoBaseDatos).collect(Collectors.toList());
        if (numCampo>-1){
            CamposPK camposPK = new CamposPK();
            camposPK.setIdPlantilla(idPlantilla);
            camposPK.setNumCampo(numCampo);
            Campos campos= camposService.getCamposId(camposPK);
            listaAsignada.remove(campos.getCampoBaseDatos());
        }

        return camposService.getCamposDisponibles(listaAsignada);
    }


}
