package mx.gob.tabasco.cmaig.sigtic.sas.restController;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegioAsignado;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfilPrivilegio")
public class SegPerfilPrivilegioRestController {

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @PostMapping("/tabla")
    public JSONObject getPerfilPrivilegiosAsignado(@RequestBody SegPerfil segPerfil){

        List<SegPerfilPrivilegioAsignado> lista = segPerfilPrivilegioService.getPrivilegiosPorRol(segPerfil.getIdPerfil());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        lista.forEach(perfilAs->{
            JSONArray json = new JSONArray();
            if (perfilAs.getNombreMenu().startsWith(" "))
                json.add("<div class='text-dark'> <i class='fas fa-angle-double-right'></i> "+perfilAs.getNombreMenu()+"</div>");
            else
                json.add("<div class='text-blue'>"+perfilAs.getNombreMenu()+"</div>");

            String checkedEd="";
            String checkedEl="";
            String checkedAg="";
            String checkedCo="";
            String checkedVa="";
            if (perfilAs.getEditar()!=null && perfilAs.getEditar())
                checkedEd="checked";
            if (perfilAs.getEliminar()!=null && perfilAs.getEliminar())
                checkedEl="checked";
            if (perfilAs.getAgregar()!=null && perfilAs.getAgregar())
                checkedAg="checked";
            if (perfilAs.getConsultar()!=null && perfilAs.getConsultar())
                checkedCo="checked";
            if (perfilAs.getValidar()!=null && perfilAs.getValidar())
                checkedVa="checked";

            json.add("<input type='checkbox' id='Ed_"+perfilAs.getIdSegPerfilPrivilegio()+"' "+checkedEd+" onclick='checkboxPrivilegio(\"Ed_\","+perfilAs.getIdSegPerfilPrivilegio()+")'>");
            json.add("<input type='checkbox' id='El_"+perfilAs.getIdSegPerfilPrivilegio()+"' "+checkedEl+" onclick='checkboxPrivilegio(\"El_\","+perfilAs.getIdSegPerfilPrivilegio()+")'>");
            json.add("<input type='checkbox' id='Ag_"+perfilAs.getIdSegPerfilPrivilegio()+"' "+checkedAg+" onclick='checkboxPrivilegio(\"Ag_\","+perfilAs.getIdSegPerfilPrivilegio()+")'>");
            json.add("<input type='checkbox' id='Co_"+perfilAs.getIdSegPerfilPrivilegio()+"' "+checkedCo+" onclick='checkboxPrivilegio(\"Co_\","+perfilAs.getIdSegPerfilPrivilegio()+")'>");
            json.add("<input type='checkbox' id='Va_"+perfilAs.getIdSegPerfilPrivilegio()+"' "+checkedVa+" onclick='checkboxPrivilegio(\"Va_\","+perfilAs.getIdSegPerfilPrivilegio()+")'>");
            array.add(json);
        });

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping
    public JSONObject savePrivilegiosMenu(@RequestBody SegPerfil perfil){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("datos",segPerfilPrivilegioService.savePervilegioPorPerfil(perfil));

        return jsonObject;
    }

    @PutMapping
    public SegPerfilPrivilegio actualizarPrivilegioPerfil(@RequestParam Long idPerfilPrivilegio, @RequestParam String tipo, @RequestParam Boolean valor){
        return segPerfilPrivilegioService.actualizarPerfilPrivilegio(idPerfilPrivilegio,tipo,valor);

    }

}
