package mx.gob.tabasco.cmaig.sigtic.sac.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.model.*;
import mx.gob.tabasco.cmaig.sigtic.sac.service.UnidadesAdministrativaService;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalogo")
public class UnidadAdministrativaRestController {

    @Autowired
    private UnidadesAdministrativaService unidadesAdministrativaService;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @GetMapping("/unidadAdministrativa/{id}")
    public UnidadAdministrativa getUnidadAdministrativa(@PathVariable("id") Long id){
        return unidadesAdministrativaService.getUnidadAdministrativaId(id);
    }


    @GetMapping("/tablaUnidadAdministrativa/{id}")
    public JSONObject getTablaUnidadesAdministrativa(@PathVariable("id") Long id)
    {

        Map<Integer, String> nivel = new HashMap<>();
        nivel.put(1,"");
        nivel.put(2,"<i class=\"fa-solid fa-ellipsis\"></i>&nbsp;");
        nivel.put(3,"<i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i>&nbsp;");
        nivel.put(4,"<i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i>&nbsp;");
        nivel.put(5,"<i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i>&nbsp;");
        nivel.put(6,"<i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i>&nbsp;");
        nivel.put(7,"<i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i>&nbsp;");
        nivel.put(8,"<i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i>&nbsp;");
        nivel.put(9,"<i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i><i class=\"fa-solid fa-ellipsis\"></i>&nbsp;");


        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("UNIDAD_ADMINSTRATIVA")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        List<SacCatUnidadAdministrativaView> listaUnidad=unidadesAdministrativaService.getSacUnidadAdministrativaPorEnteId(id);

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        String color="";

        for (SacCatUnidadAdministrativaView unidad : listaUnidad)
        {   JSONArray json = new JSONArray();
            json.add(unidad.getCveUnidadAdministrativa()!=null?unidad.getCveUnidadAdministrativa():"");
            json.add(unidad.getCveUnidadAdministrativaPadre()!=null?unidad.getCveUnidadAdministrativaPadre():"");
            json.add(unidad.getNombreUnidadAdministrativaAbreviada()!=null?unidad.getNombreUnidadAdministrativaAbreviada():"");

            color="";
            if (unidad.getNivelUnidadAdministrativa()!=null && unidad.getNivelUnidadAdministrativa().getNombreNivelUnidadAdministrativa().equals("Coordinación General"))
                color="text-success fw-bold";
            if (unidad.getNivelUnidadAdministrativa()!=null && unidad.getNivelUnidadAdministrativa().getNombreNivelUnidadAdministrativa().equals("Dirección General"))
                color="text-dark fw-bold";
            if (unidad.getNivelUnidadAdministrativa()!=null && unidad.getNivelUnidadAdministrativa().getNombreNivelUnidadAdministrativa().equals("Dirección"))
                color="text-dark fw-bold";
            if (unidad.getNivelUnidadAdministrativa()!=null && unidad.getNivelUnidadAdministrativa().getNombreNivelUnidadAdministrativa().equals("Departamento"))
                color="text-primary";
            if (unidad.getNivelUnidadAdministrativa()!=null && unidad.getNivelUnidadAdministrativa().getNombreNivelUnidadAdministrativa().equals("Subdirección"))
                color="text-danger";


            json.add("<span class='"+color+"'>"+nivel.get(unidad.getNumeroNivelUnidadAdministrativa())+unidad.getNombreUnidadAdministrativa()+"</span>");
            if (unidad.getNivelUnidadAdministrativa()!=null)
                json.add(unidad.getNivelUnidadAdministrativa().getNombreNivelUnidadAdministrativa());
            else
                json.add("");
            json.add(unidad.getNumeroOrden());
            json.add(unidad.getEstatus());

            String enlaces = "<div class=\"btn-group\">";

            if (segPerfilPrivilegio.getEditar())
                enlaces+="<a href='#' class='btn btn-outline-success mr-1' data-toggle='tooltip' data-placement='top' title='Editar unidad administrativa' onclick='editarUnidadAdministrativa("+ unidad.getIdUnidadAdministrativa() +")'><i class='fas fa-edit'></i></a>";

            if (segPerfilPrivilegio.getEliminar())
                enlaces+="<a href='javascript:void(0);' class='btn btn-outline-danger' data-toggle='tooltip' data-placement='top' title='Eliminar unidad administrativa' onclick='eliminarUnidadAdministrativa("+ unidad.getIdUnidadAdministrativa() +")'><i class='fas fa-trash'></i></a>";

            enlaces+="</div>";

            json.add(enlaces);

            array.add(json);
        }
        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }

     @PostMapping("/getNivelUnidadAdministrativaList")
    public List<NivelUnidadAdministrativa> getNivelUnidadAdministrativaList(){
        return unidadesAdministrativaService.getAllNivelUnidadAdministrativa("VIG");
    }

    @PostMapping("/unidad/guardarUnidadAdministrativa")
    public UnidadAdministrativa saveUsuario(@RequestBody UnidadAdministrativa unidadAdministrativa){
        return unidadesAdministrativaService.guardarUnidadAdministrativa(unidadAdministrativa);
    }

    @DeleteMapping("/deleteUnidadAdministrativa")
    public UnidadAdministrativa deleteUnidadAdministrativa(@RequestBody UnidadAdministrativa unidadAdministrativa){
        return unidadesAdministrativaService.deleteUnidadAdministrativa(unidadAdministrativa.getIdUnidadAdministrativa());
    }

    @GetMapping("/exportUnidadesAdministrativa/{idEntePublico}")
    public void exportReporteCorreosUnidad(HttpServletResponse response, @PathVariable("idEntePublico") Long idEntePublico) {
        unidadesAdministrativaService.generaReporteUnidadesAdministrativas(idEntePublico, response);
    }



    @GetMapping("/catalogoUnidadesAdministrativa/{idEntePublico}")
    public void exportarCatalogoUnidadesAdministrativa(HttpServletResponse response, @PathVariable("idEntePublico") Long idEntePublico) {
        unidadesAdministrativaService.exportarCatalogoUnidadesAdministrativas(idEntePublico, response);
    }

}