package mx.gob.tabasco.cmaig.sigtic.sas.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilUsuarioService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rolUsuario")
public class SegRolUsuarioRestController {

    @Autowired
    private SegPerfilUsuarioService segPerfilUsuarioService;

    @PostMapping("/tablaUsuariosRol")
    public JSONObject getUsuariosRol(@RequestBody SegPerfil segPerfil){

        List<CatUsuarioEntePublico> lista = segPerfilUsuarioService.getUsuariosRol(segPerfil);

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        lista.forEach(catUsuario->{

           JSONArray json = new JSONArray();
            json.add(catUsuario.getEntePublico().getNombreEntePublico());
            json.add(catUsuario.getCatUsuario().getRfc());
            json.add(catUsuario.getCatUsuario().getNombreCompleto());
            if (catUsuario.getUnidadAdministrativa()!= null)
                json.add(catUsuario.getUnidadAdministrativa().getNombreUnidadAdministrativa());
            else
                json.add("");
            json.add(catUsuario.getCveEstatus());

            array.add(json);

        });

        jsonObject.put("aaData", array);
        return jsonObject;
    }


    @PostMapping("/tablaPerfilConsulta")
    public JSONObject tablaPerfilConsulta(@RequestBody SegUsuario segUsuario){

        List<SegPerfilUsuario> lista = segPerfilUsuarioService.getRolesUsuarioAsignados(segUsuario.getSegUsuarioPk().getUsuario());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        lista.forEach(segPerfilUsuario->{

               JSONArray json = new JSONArray();
                json.add(segPerfilUsuario.getSegPerfil().getNombrePerfil());
                json.add(segPerfilUsuario.getSegPerfil().getCveEstatus());
                array.add(json);
                });

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/tablaRolesDisponibles")
    public JSONObject tablaPerfilesDisponibles(@RequestBody SegUsuario segUsuario){

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SegPerfil> listaPerfil = segPerfilUsuarioService.getRolesDisponibles(segUsuario.getSegUsuarioPk().getUsuario(), authUser.getUsername(),authUser.isAdministradorSigtic());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        listaPerfil.forEach(perfil->{
            JSONArray json = new JSONArray();
            json.add(perfil.getNombrePerfil());
            json.add("<a href='#'  onclick='addRolUSuario("+perfil.getIdPerfil()+",\""+segUsuario.getSegUsuarioPk().getUsuario()+"\")' class='btn btn-success mr-1'><i class=\"bi bi-box-arrow-right\"></i></a>");
            array.add(json);

        });

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/tablaRolesAsignados")
    public JSONObject tablaPerfilesAsignados(@RequestBody SegUsuario segUsuario){

        List<SegPerfilUsuario> listaRolesAsignados = segPerfilUsuarioService.getRolesUsuarioAsignados(segUsuario.getSegUsuarioPk().getUsuario());
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        listaRolesAsignados.forEach(segPerfilUsuario->{
            JSONArray json = new JSONArray();
            json.add("<a href='#' onclick='removeRolUsuario("+segPerfilUsuario.getIdSegPerfilUsuario()+",\""+segUsuario.getSegUsuarioPk().getUsuario()+"\")' class='btn btn-danger mr-1'><i class=\"bi bi-box-arrow-left\"></i></a>");
            json.add(segPerfilUsuario.getSegPerfil().getNombrePerfil());
            json.add(segPerfilUsuario.getSegPerfil().getCveEstatus());
            array.add(json);
        });

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/asignarRolUsuario")
    public SegPerfilUsuario asignarRolUsuario(@RequestBody SegUsuario segUsuario){
        return segPerfilUsuarioService.asignarRolUsuario(segUsuario);
    }

    @PostMapping("/removerRolUsuario")
    public JSONObject removerRolUsuario(@RequestBody SegPerfilUsuario segPerfilUsuario){

        segPerfilUsuarioService.removerRolUsuario(segPerfilUsuario);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message","El rol se removió correctamente");
        return jsonObject;
    }


}
