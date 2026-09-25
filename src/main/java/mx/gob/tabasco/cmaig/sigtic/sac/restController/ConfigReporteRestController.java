package mx.gob.tabasco.cmaig.sigtic.sac.restController;


import mx.gob.tabasco.cmaig.sigtic.sac.model.*;
import mx.gob.tabasco.cmaig.sigtic.sac.service.ConfigReporteService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/config")
public class ConfigReporteRestController {


    @Autowired
    private ConfigReporteService configReporteService;

    @Autowired
    private ServletContext servletContext;

    @PostMapping("/reporte")
    public JSONObject getPlantillas()
    {

        List<CatPlantillaReporteBean> listaConfig = configReporteService.getDatosReportes();
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

            for (CatPlantillaReporteBean plantillaReporteBean : listaConfig)
            {   JSONArray json = new JSONArray();

                    json.add(plantillaReporteBean.getNombreReporte());
                    json.add(plantillaReporteBean.getTituloReporte());
                    json.add(plantillaReporteBean.getTipoServicio());
                    json.add(plantillaReporteBean.getSubTipoServicio());
                    json.add(plantillaReporteBean.getCveEstatus());

                json.add("<div class=\"btn-group\" role=\"group\" aria-label=\"..\">" +
                        "<a href='#' class='btn btn-outline-success mr-1' data-toggle='tooltip' data-placement='top' title='Agregar Parrafo' onclick='modalParrafo("+ plantillaReporteBean.getIdPlantillaReporte() +")'><i class=\"fa-solid fa-circle-plus\"></i></a>"
                        +"<a href='#' class='btn btn-outline-success mr-1' data-toggle='tooltip' data-placement='top' title='Editar Plantilla' onclick='editarPlantilla("+ plantillaReporteBean.getIdPlantillaReporte() +")'><i class='fas fa-edit'></i></a>"
                        +"<a href='javascript:void(0);' class='btn btn-outline-danger' data-toggle='tooltip' data-placement='top' title='Eliminar Plantilla' onclick='eliminarPlantilla("+ plantillaReporteBean.getIdPlantillaReporte() +")'><i class='fas fa-trash'></i></a>" +
                        "<a href='" + servletContext.getContextPath() +"/api/config/getCrPdfPrueba/" + plantillaReporteBean.getIdPlantillaReporte() +"' target='_blank' class='btn btn-outline-danger mr-1' data-toggle='tooltip' data-placement='top' title='Ver ejemplo pdf'><i class=\"fa-solid fa-file-pdf\"></i></a>"+
                        "</div>");

                array.add(json);
            }

        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/guardarConfiguracionReporte")
    public CatPlantillaReporte guardarConfiguracionReporte(@RequestBody CatPlantillaReporte catPlantillaReporte){
        return configReporteService.guardarConfiguracionReporte(catPlantillaReporte);

    }

    @GetMapping("/editarConfiguracionReporte/{idPlantillaReporte}")
    public CatPlantillaReporte editarConfiguracionReporte(@PathVariable(name="idPlantillaReporte") Long idPlantillaReporte){
        return configReporteService.editarConfiguracionReporte(idPlantillaReporte);

    }

    @DeleteMapping("/deleteConfiguracionReporte")
    public CatPlantillaReporte deleteConfiguracionReporte(@RequestBody CatPlantillaReporte catPlantillaReporte){
        return configReporteService.deleteConfiguracionReporte(catPlantillaReporte.getIdPlantillaReporte().longValue());
    }

    @PostMapping("/parrafos")
    public JSONObject getParrafos(@RequestBody CatPlantillaReporteParrafo catPlantillaReporteParrafo)
    {

        List<CatPlantillaReporteParrafo> listaParrafo = configReporteService.getListaPlantillaParrafo(catPlantillaReporteParrafo);
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        for (CatPlantillaReporteParrafo plantillaReporteParrafo : listaParrafo)
        {   JSONArray json = new JSONArray();

            json.add(plantillaReporteParrafo.getParrafo());
            json.add(plantillaReporteParrafo.getNumeroOrden());

            json.add("<div class=\"btn-group\" role=\"group\" aria-label=\"..\">" +
                    "<a href='#' class='btn btn-outline-success mr-1' data-toggle='tooltip' data-placement='top' title='Editar Plantilla' onclick='editarParrafo("+plantillaReporteParrafo.getIdPlantillaReporteParrafo()+")'><i class='fas fa-edit'></i></a>"
                    +"<a href='javascript:void(0);' class='btn btn-outline-secondary' data-toggle='tooltip' data-placement='top' title='Eliminar Plantilla' onclick='eliminarParrafo("+ plantillaReporteParrafo.getIdPlantillaReporteParrafo() +")'><i class='fas fa-trash'></i></a>" +
                    "</div>");

            array.add(json);
        }

        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/guardarParrafo")
    public CatPlantillaReporteParrafo guardarParrafo(@RequestBody CatPlantillaReporteParrafo catPlantillaReporteParrafo){
        return configReporteService.addParrafoPlantilla(catPlantillaReporteParrafo);

    }

    @GetMapping("/editarParrafo/{idPlantillaReporteParrafo}")
    public CatPlantillaReporteParrafo editarParrafo(@PathVariable(name="idPlantillaReporteParrafo") Long idPlantillaReporteParrafo){
        return configReporteService.editarParrafo(idPlantillaReporteParrafo);

    }

    @DeleteMapping("/deleteParrafo")
    public CatPlantillaReporteParrafo deleteParrafo(@RequestBody CatPlantillaReporteParrafo catPlantillaReporteParrafo){
        return configReporteService.deleteParrafo(catPlantillaReporteParrafo.getIdPlantillaReporteParrafo().longValue());
    }


    @GetMapping("/getCrPdfPrueba/{id}")
    public ResponseEntity<byte[]> getReportePrueba(@PathVariable("id") Long id) throws JRException, FileNotFoundException {
        return  configReporteService.getReportePrueba(id);
    }

}
