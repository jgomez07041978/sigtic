package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatServiciosTicIp;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatServiciosTicIpService;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/catalogo/ticIp")
public class CatServiciosTicIpRestController {

    @Autowired
    private CatServiciosTicIpService catServiciosTicIpService;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @PostMapping("/tablaIpServiciosTic")
    public JSONObject getListaIpServicio(@RequestBody ServiciosTic servicio) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("Servicio")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        List<CatServiciosTicIp> listaIpServicio =catServiciosTicIpService.getListaIpPorServicio(servicio.getIdServicioTic());


        listaIpServicio.forEach(serviciosTicIp -> {
            JSONArray json = new JSONArray();
            json.add(serviciosTicIp.getIpServicio());
            json.add(serviciosTicIp.getCveEstatus());

            String enlaces = "<div class='d-grid gap-3 d-md-block'>";
            if (segPerfilPrivilegio.getEditar())
                enlaces += "<a href=#' class='btn btn-success btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Editar IP' onclick='editarIpServicio(" + serviciosTicIp.getIdIpServicioTic() + ")'><i class='bi bi-pencil-square'></i></a>";
            if (segPerfilPrivilegio.getEliminar())
                enlaces += "<a href='javascript:void(0);' class='btn btn-danger btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar IP' onclick='eliminarIpServicio(" + serviciosTicIp.getIdIpServicioTic() + ")'><i class='bi bi-trash3'></i></a>";
            enlaces += "</div>";
            json.add(enlaces);
            array.add(json);
        });
        // }

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/saveServicioTicIp")
    public CatServiciosTicIp saveCatServiciosTicIp(@RequestBody CatServiciosTicIp catServiciosTicIp){
        return catServiciosTicIpService.guardarServiciosTicIp(catServiciosTicIp);
    }
}
