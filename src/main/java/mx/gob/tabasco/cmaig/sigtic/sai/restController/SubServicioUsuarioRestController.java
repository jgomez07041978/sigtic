package mx.gob.tabasco.cmaig.sigtic.sai.restController;


import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.SubServicioUsuario;
import mx.gob.tabasco.cmaig.sigtic.sai.model.Usuario;
import mx.gob.tabasco.cmaig.sigtic.sai.service.SubservicioUsuarioService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subServicioUsuario")
public class SubServicioUsuarioRestController {

    @Autowired
    private SubservicioUsuarioService subservicioUsuarioService;


    @PostMapping("/tablaSubserviciosDisponibles")
    public JSONObject getSubserviciosDisponibles(@RequestBody Usuario usuario) {

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        List<CatSubtipoServiciosTic> listaSubServicioDisp = subservicioUsuarioService.getSubserviciosAsignarUsuario(usuario.getIdUsuario());

        listaSubServicioDisp.forEach(reg -> {
            JSONArray json = new JSONArray();
            json.add(reg.getCatTipoServiciosTic().getNombreTipoServicioTic());
            json.add(reg.getNombreSubtipoServicioTic());
            json.add("<a href='#'  onclick='addSubTipoServicio("+reg.getIdSubtipoServicioTic()+","+usuario.getIdUsuario()+")' class='btn btn-success mr-1'><i class=\"bi bi-box-arrow-right\"></i></a>");
            array.add(json);
        });

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/tablaSubServiciosAsignados")
    public JSONObject tablaSubServiciosAsignados(@RequestBody Usuario usuario){

        List<SubServicioUsuario> listaSubServicioAsignados = subservicioUsuarioService.getSubServiciosAddUsuario(usuario.getIdUsuario());
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        listaSubServicioAsignados.forEach(reg->{
            JSONArray json = new JSONArray();
            json.add("<a href='#' onclick='removeSubTipoServicio("+reg.getIdSubtipoServicioUsuario()+","+usuario.getIdUsuario()+")' class='btn btn-danger mr-1'><i class=\"bi bi-box-arrow-left\"></i></a>");
            json.add(reg.getCatSubtipoServiciosTic().getNombreSubtipoServicioTic());
            json.add(reg.getCatSubtipoServiciosTic().getCatTipoServiciosTic().getNombreTipoServicioTic());
            array.add(json);
        });

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/asignarSubTipoServicioUsuario")
    public SubServicioUsuario asignarSubTipoServicioUsuario(@RequestBody SubServicioUsuario subServicioUsuario){
        return subservicioUsuarioService.asignarSubTipoServicioUsuario(subServicioUsuario);
    }

    @PostMapping("/removerSubTipoServicioUsuario")
    public JSONObject removerSubTipoServicioUsuario(@RequestBody SubServicioUsuario subServicioUsuario){

        subservicioUsuarioService.removerSubTipoServicioUsuario(subServicioUsuario);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message","El sub-tipo servicio se removió correctamente");
        return jsonObject;
    }


}
