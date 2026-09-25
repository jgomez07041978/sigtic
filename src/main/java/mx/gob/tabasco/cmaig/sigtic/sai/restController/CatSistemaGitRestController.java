package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSistema;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSistemaGit;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatSistemaGitService;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatSistemaService;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sistema-git")
public class CatSistemaGitRestController {

    private final CatSistemaGitService catSistemaGitService;
    private final SegPerfilPrivilegioService segPerfilPrivilegioService;

    public CatSistemaGitRestController(CatSistemaGitService catSistemaGitService, SegPerfilPrivilegioService segPerfilPrivilegioService) {
        this.catSistemaGitService = catSistemaGitService;
        this.segPerfilPrivilegioService = segPerfilPrivilegioService;
    }

    @GetMapping({"/tablaSistemasGit/","/tablaSistemasGit/{idSistema}"})
    public JSONObject getSistemas(@PathVariable(name = "idSistema", required = false) Long idSistema) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("SISTEMAS")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        if (idSistema!=null) {

            List<CatSistemaGit> listaSistemaGit = catSistemaGitService.getListaSistemaGit(idSistema);

            listaSistemaGit.forEach(sistemaGit -> {

                JSONArray json = new JSONArray();
                json.add(sistemaGit.getCodigoGit());
                json.add(sistemaGit.getDescripcion());

                String enlaces = "<div class=\"btn-group\"> ";
                if (segPerfilPrivilegio.getEditar())
                    enlaces += "<a href=#' class='btn btn-success btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Editar Sistema Git' onclick='editarSistemaGit(" + sistemaGit.getIdSistemaGit() + ")'><i class='bi bi-pencil-square'></i></a>";
                if (segPerfilPrivilegio.getEliminar())
                    enlaces += "<a href='javascript:void(0);' class='btn btn-danger btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Sistema' onclick='eliminarSistemaGit(" + sistemaGit.getIdSistemaGit() + ")'><i class='bi bi-trash3'></i></a>";
                enlaces += "</div>";
                json.add(enlaces);

                array.add(json);
            });
        }


        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/saveGit")
    public CatSistemaGit saveSistema(@RequestBody CatSistemaGit catSistemaGit){
        return catSistemaGitService.guardarGit(catSistemaGit);
    }

    @GetMapping("/getSistemaGit/{id}")
    public CatSistemaGit getSistemaGit(@PathVariable("id") Long idSistemaGit){

        return catSistemaGitService.getSistemaGitById(idSistemaGit);
    }

    @DeleteMapping("/deleteSistemaGit")
    public CatSistemaGit deleteSistemaGit(@RequestBody CatSistemaGit catSistemaGit){
        return catSistemaGitService.deleteSistemaGit(catSistemaGit);
    }


}
