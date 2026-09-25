package mx.gob.tabasco.cmaig.sigtic.sigget.restController;

import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Mensage;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Plantilla;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.PlantillaUploadBean;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Separador;
import mx.gob.tabasco.cmaig.sigtic.sigget.service.PlantillasService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/plantillas")
public class PlantillasRestController {

    @Autowired
    private PlantillasService plantillasService;

    @Autowired
    private ServletContext servletContext;

    @GetMapping
    public  List<Plantilla> getPlantillas(){
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return plantillasService.getPlantillas(authUser.getRfc());
    }

    @GetMapping("/{id}")
    public Plantilla getPlantillaId(@PathVariable(value = "id") Long idPlantilla){

        return plantillasService.getPlantillaId(idPlantilla);
    }

    @GetMapping("/getSeparadorVigente")
    public List<Separador> getSeparadorVigente(){
        return plantillasService.getSeparador("VIG");
    }

    @PostMapping
    public Plantilla savePlantilla(@RequestBody Plantilla plantilla){
        return plantillasService.savePlantilla(plantilla);
    }

    @PutMapping
    public Plantilla updatePlantilla(@RequestBody Plantilla plantilla){
        return plantillasService.updatePlantilla(plantilla);
    }

    @PostMapping("/all")
    public JSONObject getPlantillasAll()
    {
        List<Plantilla> plantillasAll = plantillasService.getPlantillasAll();

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
            for (Plantilla plantilla : plantillasAll)
            {   JSONArray json = new JSONArray();
                json.add(plantilla.getIdPlantilla());
                json.add(plantilla.getNombrePlantilla());
                json.add(plantilla.getNombreArchivo());
                json.add(plantilla.getTotalCamposCapturados()+" / " +plantilla.getNumCampos());

                json.add(plantilla.getCveEstatus());
                String acciones="";

                acciones +="<div class='btn-group' role='group' aria-label='Default button group'>";
                if (plantilla.getFileNameOriginal()!=null)
                    acciones +="<a href='"+servletContext.getContextPath()+"/api/plantillas/exportPlantilla/"+plantilla.getIdPlantilla()+"' target='_blank' class='btn btn-primary mr-1' data-toggle='tooltip' data-placement='top' title='Descargar Plantilla'><i class='fas fa-download'></i></a>";
                if (plantilla.getCveEstatus().equals("VIG"))
                    acciones +="<a href='#' class='btn btn-secondary mr-1' data-toggle='tooltip' data-placement='top' title='Subir Plantilla' onclick='openModalUploadPlantilla("+ plantilla.getIdPlantilla() +")'><i class='fas fa-paperclip'></i></a>";
                acciones +="<a href='#' class='btn btn-success mr-1' data-toggle='tooltip' data-placement='top' title='Editar Plantilla' onclick='openModalPlantilla("+ plantilla.getIdPlantilla() +")'><i class='fas fa-edit'></i></a>";

                if (plantilla.getCveEstatus().equals("VIG"))
                    acciones +="<a href='#' class='btn btn-danger' data-toggle='tooltip' data-placement='top' title='Eliminar Plantillas' onclick='eliminarPlantillas("+ plantilla.getIdPlantilla() +")'><i class='fas fa-trash'></i></a>";

                acciones +=" <a href='#' class='btn btn-primary mr-1' data-toggle='tooltip' data-placement='top' title='Agregar Campos' onclick='agregarCampos("+ plantilla.getIdPlantilla() +",\""+plantilla.getNombrePlantilla()+"\","+plantilla.getNumCampos()+","+plantilla.getTotalCamposCapturados()+")'><i class='fas fa-plus'></i></a>";

                acciones +="</div>";
                json.add(acciones);

                array.add(json);

            }
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @DeleteMapping
    public Plantilla deletePlantilla(@RequestBody Plantilla plantilla){
        return plantillasService.deletePlantilla(plantilla);
    }

    @PostMapping("/uploadExpedienteJpa")
    public Mensage uploadExpedienteJpa(@RequestBody PlantillaUploadBean plantillaUploadBean){
        return plantillasService.uploadExpediente(plantillaUploadBean);
    }

    @GetMapping({"/exportPlantilla/{id}"})
    public void descargaDocumento(HttpServletResponse response, @PathVariable Long id){
        plantillasService.exportPlantilla(response, id);
    }

}
