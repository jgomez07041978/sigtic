package mx.gob.tabasco.cmaig.sigtic.sac.restController;


import mx.gob.tabasco.cmaig.sigtic.sac.model.CatPlantillaReporte;
import mx.gob.tabasco.cmaig.sigtic.sac.service.CatPlantillaReporteService;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CartaResponsiva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plantillaReporte")
public class PlantillaReporteRestController {


    @Autowired
    private CatPlantillaReporteService catPlantillaReposteService;

    @PostMapping("/getPlantillaReportePorEnteSubtipoServicio")
    public List<CatPlantillaReporte> getPlantillaReportePorEnteSubtipoServicio(@RequestBody CartaResponsiva cartaResponsiva)
    {
        List<CatPlantillaReporte> listaReporte = catPlantillaReposteService.getListaPlantillaEnteSubtipoServicio(cartaResponsiva.getIdCartaResponsiva());
        return listaReporte;
    }

    @GetMapping("/getPlantillaId/{idPlantillaReporte}/{idCartaResponsiva}")
    public CatPlantillaReporte getPlantillaId(@PathVariable("idPlantillaReporte") Long idPlantillaReporte,@PathVariable("idCartaResponsiva") Long idCartaResponsiva)
    {
        return catPlantillaReposteService.getPlantillaReporteId(idPlantillaReporte, idCartaResponsiva);
    }

    @PostMapping("/getExistePlantilla")
    public Integer getExistePlantilla(@RequestBody CatPlantillaReporte catPlantillaReporte)
    {
        List<CatPlantillaReporte> listaReporte = catPlantillaReposteService.getListaPlantillaEnteSubtipoServicio(catPlantillaReporte.getEntePublico().getIdEntePublico(),catPlantillaReporte.getIdSubtipoServicio());

        return listaReporte.size();
    }




}
