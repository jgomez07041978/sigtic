package mx.gob.tabasco.cmaig.sigtic.sac.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.bean.DomiciliosBean;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.DomicilioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.DomicilioEntePublicoBean;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.service.DomicilioEntePublicoService;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo/domicilioentePublico")
public class DomicilioEntePublicoRestController {

    @Autowired
    private DomicilioEntePublicoService domicilioEntePublicoService;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @GetMapping(path = "/all/{idEntePublico}")
    public List<DomicilioEntePublicoBean> getDomicilioEntePublico(@PathVariable(value = "idEntePublico") Long idEntePublico){

        if (idEntePublico!=null)
            return domicilioEntePublicoService.listaDomicilioEntePublicoBean(idEntePublico);
        else
            return new ArrayList<>();
    }

    @PostMapping("/tablaDomicilio")
    public JSONObject getTablaDomicilios(@RequestBody EntePublico entePublico)
    {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("CAT_DOMICILIOS")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        List<DomicilioEntePublicoBean> listaDomicilio = domicilioEntePublicoService.listaDomicilioEntePublicoBean(entePublico.getIdEntePublico());

        if (entePublico.getIdEntePublico() != null) {
            for (DomicilioEntePublicoBean domicilio : listaDomicilio)
            {   JSONArray json = new JSONArray();
                json.add(domicilio.getIdDomicilio());
                json.add(domicilio.getNombreDomicilio());
                json.add("<a href='javascript:obtenerUbicaciones("+domicilio.getIdDomicilio()+","+domicilio.getIdEntePublico()+");'><span class='badge bg-primary'>"+domicilio.getTotalUbicaciones()+"</span></a>");
                json.add(domicilio.getCveEstatus());

                String enlaces = "";

                if (segPerfilPrivilegio.getEditar())
                    enlaces+="<a href='#' class='btn btn-outline-success mr-1' data-toggle='tooltip' data-placement='top' title='Editar Domicilio' onclick='editarDomicilio("+ domicilio.getIdDomicilio() +")'><i class='fas fa-edit'></i></a>";

                if (segPerfilPrivilegio.getEliminar())
                    enlaces+="<a href='javascript:void(0);' class='btn btn-outline-secondary' data-toggle='tooltip' data-placement='top' title='Eliminar Domicilio' onclick='eliminarDomicilio("+ domicilio.getIdDomicilio() +")'><i class='fas fa-trash'></i></a>";
                json.add(enlaces);;

                array.add(json);
            }
        }
        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @GetMapping(path = "/getBean/{idEntePublico}")
    public List<DomicilioEntePublicoBean> getDomicilioEntePublicoBean(@PathVariable(value = "idEntePublico") Long idEntePublico){

        if (idEntePublico!=null)
            return domicilioEntePublicoService.listaDomicilioEntePublicoBean(idEntePublico);
        else
            return new ArrayList<>();
    }

    @DeleteMapping("/delete")
    public DomicilioEntePublico deleteDomicilio(@RequestBody DomicilioEntePublico domicilioEntePublico){
        return domicilioEntePublicoService.deleteDomicilio(domicilioEntePublico);
    }

    @PostMapping("/save")
    public DomicilioEntePublico saveDomicilio(@RequestBody DomicilioEntePublico domicilioEntePublico){
        return domicilioEntePublicoService.saveDomicilio(domicilioEntePublico);
    }



    @GetMapping("/get/{idDomicilio}")
    public DomicilioEntePublico getDomicilioId(@PathVariable("idDomicilio") Long idDomicilio){
        return domicilioEntePublicoService.getDomicilioId(idDomicilio);
    }


    @GetMapping("/exportarDomiciliosPorEnte/{idEntePublico}")
    public void exportarDomiciliosPorEnte(@PathVariable("idEntePublico") Long idEntePublico, HttpServletResponse response) {
        domicilioEntePublicoService.exportarDomiciliosPorEnte(idEntePublico, response);
    }

    @GetMapping("/exportarUbicacionesPorEnte/{idEntePublico}")
    public void exportarUbicacionesPorEnte(@PathVariable("idEntePublico") Long idEntePublico, HttpServletResponse response) {
        domicilioEntePublicoService.exportarUbicacionesPorEnte(idEntePublico, response);
    }

    @GetMapping("/exportarTodasUbicaciones")
    public void exportarTodasUbicaciones(HttpServletResponse response) {
        domicilioEntePublicoService.exportarTodasUbicaciones(response);
    }

    @GetMapping("/exportarTodosDomicilios")
    public void exportarTodosDomicilios(HttpServletResponse response) {
        domicilioEntePublicoService.exportarTodosDomicilios(response);
    }

}