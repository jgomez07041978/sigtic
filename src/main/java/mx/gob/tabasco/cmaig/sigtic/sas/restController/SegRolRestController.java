package mx.gob.tabasco.cmaig.sigtic.sas.restController;

import mx.gob.tabasco.cmaig.sigtic.sas.model.*;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.SegPerfilUsuarioCount;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilUsuarioService;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegRolService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rol")
public class SegRolRestController {

    @Autowired
    private SegRolService segRolService;

    @Autowired
    private SegPerfilUsuarioService segPerfilUsuarioService;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @GetMapping("/{id}")
    public SegPerfil getPerfil(@PathVariable("id") Integer idRol){
        return segRolService.getPerfilId(idRol);
    }

    @GetMapping("/listaPerfil")
    public List<SegPerfil> getListaPerfil(){
        return segRolService.getRolesBySistemaAndEstatus("SIGTIC","VIG");
    }

    @PostMapping("/tabla")
    public JSONObject getRoles(){
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("GEST_ROLES")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());


        List<SegPerfilBean> listaRoles = segRolService.getRolesBySistemaAndEstatusSql("SIGTIC","VIG");

        List<SegPerfilUsuarioCount> listaPerfilUsuario = segPerfilUsuarioService.getNumUsuarioByPerfil();

        Map<Integer, Long> mapPerfilUsuario = new HashMap<>();

        for (SegPerfilUsuarioCount segUsu: listaPerfilUsuario)
            mapPerfilUsuario.put(segUsu.getIdPerfil(), segUsu.getNumUsuarios());


        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

            //for (SegPerfil rol : listaRoles)
        listaRoles.forEach(rol->{
                //List<SegPerfilPrivilegioAsignado> listaPrivilegioAsignado= segPerfilPrivilegioService.getPrivilegiosPorRol(rol.getIdPerfil());

                JSONArray json = new JSONArray();
                json.add(rol.getNombrePerfil());
                json.add(rol.getDescPerfil());
                if (mapPerfilUsuario.containsKey(rol.getIdPerfil()))
                    json.add("<a href='#' onclick='modalUsuariosRol("+rol.getIdPerfil()+")'>"+mapPerfilUsuario.get(rol.getIdPerfil())+"<a/>");
                else
                    json.add("0");
                json.add(rol.getCveEstatus());

                    String enlaces = "<div class='d-grid gap-3 d-md-block'>";
            if (segPerfilPrivilegio.getEditar())
                enlaces += "<a href=#' class='btn btn-success btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Editar Rol' onclick='editarRol(" + rol.getIdPerfil() + ")'><i class='bi bi-pencil-square'></i></a>";
            if (segPerfilPrivilegio.getEliminar())
                enlaces += "<a href='javascript:void(0);' class='btn btn-danger btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Rol' onclick='eliminarRol(" + rol.getIdPerfil() + ")'><i class='bi bi-trash3'></i></a>";
                    enlaces += "<a href='javascript:void(0);' class='btn btn-info btn-sm  position-relative' role='button' data-toggle='tooltip' data-placement='top' title='Permisos asignados' onclick='modalPermisosAsignados(" + rol.getIdPerfil() + ")'><i class='bi bi-plus-circle'></i>  <span class='position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger'>" +
                            +rol.getNumPerfil()+
                            "    <span class=\"visually-hidden\">unread messages</span>" +
                            "  </span></a>";
                    enlaces+="</div>";
                    json.add(enlaces);

                array.add(json);
        });


        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping
    public SegPerfil saveRol(@RequestBody SegPerfil rol){
        SegPerfil segPerfil = segRolService.saveRol(rol);
        return segPerfil;
    }


    @PutMapping
    public SegPerfil updateRol(@RequestBody SegPerfil rol){
        SegPerfil segPerfil = segRolService.saveRol(rol);
        return segPerfil;
    }

    @DeleteMapping
    public JSONObject deletePerfil(@RequestBody SegPerfil rol){

       return segRolService.deleteRol(rol);

    }





}
