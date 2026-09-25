package mx.gob.tabasco.cmaig.sigtic.sas.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.model.*;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegUsuarioTemporalService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preRegistroAdmin")
public class PreRegistroAdminRestController {

    @Autowired
    private SegUsuarioTemporalService segUsuarioTemporalService;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @PostMapping("/tabla")
    public JSONObject getUsuariosPreRegistro(){
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("PRE-REGISTRO")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());


        List<SegUsuarioTemporal> listaUSuariosTemporales = segUsuarioTemporalService.listaUsuariosTemporalAll();



        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        listaUSuariosTemporales.forEach(reg->{

            JSONArray json = new JSONArray();
            json.add(reg.getCurp());
            json.add(reg.getNombreCompleto());
            json.add(reg.getCorreoInstitucional());
            json.add(reg.getCelular());
            Integer totalRoles = segUsuarioTemporalService.totalRoles(reg);

                json.add("<a href='#' onclick='modalRolTemporal("+reg.getIdUsuarioTemporal()+",\""+reg.getCorreoInstitucional()+"\")'>"+totalRoles+"<a/>");

            json.add(reg.getEstatus());

            String enlaces = "<div class='d-grid gap-3 d-md-block'>";

            if (reg.getEstatus().equals("PENDIENTE") && reg.getTokenUrl()!=null)
                enlaces += "<a href=#' class='btn btn-outline-primary btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Enviar URL' onclick='enviarMail(\"" + reg.getTokenUrl() +"\",\""+reg.getCorreoInstitucional()+"\",\""+reg.getNombreCompleto()+"\",\""+reg.getOficio()+"\",\""+reg.getDependencia()+"\")'><i class='far fa-envelope'></i></a>";

            if (reg.getEstatus().equals("PENDIENTE") && reg.getTokenUrl()!=null)
                enlaces += "<a href='/sigtic/pre-registro?token="+reg.getTokenUrl()+"' target='_blank' class='btn btn-outline-primary btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Abrir URL'><i class='fas fa-external-link-alt'></i></a>";

            if (segPerfilPrivilegio.getEditar() && reg.getEstatus().equals("PENDIENTE"))
                enlaces += "<a href=#' class='btn btn-outline-primary btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Generar Token' onclick='generarToken(" + reg.getIdUsuarioTemporal() +")'><i class='fas fa-user-cog'></i></a>";
            if (segPerfilPrivilegio.getEditar() && reg.getEstatus().equals("PENDIENTE"))
                enlaces += "<a href=#' class='btn btn-success btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Editar Registro' onclick='editarPreRegistro(" + reg.getIdUsuarioTemporal()+")'><i class='bi bi-pencil-square'></i></a>";
            if (segPerfilPrivilegio.getEliminar() && reg.getEstatus().equals("PENDIENTE"))
                enlaces += "<a href='javascript:void(0);' class='btn btn-danger btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Registro' onclick='eliminarPreRegistro(" + reg.getIdUsuarioTemporal() + ")'><i class='bi bi-trash3'></i></a>";
            if (segPerfilPrivilegio.getEditar() && reg.getEstatus().equals("VALIDADO"))
                enlaces += "<a href='javascript:void(0);' class='btn btn-primary btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Regresar a Pendiente' onclick='regresarPendiente(" + reg.getIdUsuarioTemporal() + ")'><i class='fas fa-undo-alt'></i></a>";

            enlaces+="</div>";
            json.add(enlaces);

            array.add(json);
        });


        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping
    public SegUsuarioTemporal savePreRegistro(@RequestBody SegUsuarioTemporal segUsuarioTemporal){
        SegUsuarioTemporal segUsuarioTemporal1 = segUsuarioTemporalService.savePreRegistro(segUsuarioTemporal);
        return segUsuarioTemporal1;
    }


    @PutMapping
    public SegUsuarioTemporal updatePreRegistro(@RequestBody SegUsuarioTemporal segUsuarioTemporal){
        SegUsuarioTemporal segUsuarioTemporal1 = segUsuarioTemporalService.savePreRegistro(segUsuarioTemporal);
        return segUsuarioTemporal1;
    }

    @DeleteMapping
    public JSONObject deletePreRegistro(@RequestBody SegUsuarioTemporal segUsuarioTemporal){
        return segUsuarioTemporalService.deletePreRegistro(segUsuarioTemporal);

    }

    @PostMapping("/tablaRolTemporal")
    public JSONObject getRolesTemporales(@RequestBody SegUsuarioTemporal segUsuarioTemporal){
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("PRE-REGISTRO")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        SegUsuarioTemporal segUsuarioTemporal1= segUsuarioTemporalService.getUsuarioTemporalId(segUsuarioTemporal.getIdUsuarioTemporal());


        List<SegUsuarioTemporalRol> listaRolTemporal = segUsuarioTemporalService.listaSegUsuarioTemporalRol(segUsuarioTemporal1);


        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        listaRolTemporal.forEach(reg->{

            JSONArray json = new JSONArray();
            json.add(reg.getSegPerfil().getNombrePerfil());

            String enlaces = "<div class='d-grid gap-3 d-md-block'>";

            //if (segPerfilPrivilegio.getEliminar() && segUsuarioTemporal1.getEstatus().equals("PENDIENTE"))
                enlaces += "<a href='javascript:void(0);' class='btn btn-danger btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Rol del Pre Registro' onclick='eliminarRolPreRegistro(" + reg.getIdUsuarioTemporalRol()+ ")'><i class='bi bi-trash3'></i></a>";

            enlaces+="</div>";
            json.add(enlaces);

            array.add(json);
        });


        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/addRolTemporal")
    public SegUsuarioTemporalRol addRolTemporal(@RequestBody SegUsuarioTemporalRol segUsuarioTemporalRol){
        return segUsuarioTemporalService.addRolTemporal(segUsuarioTemporalRol);
    }

    @DeleteMapping("/deleteRolPreRegistro")
    public JSONObject deleteRolPreRegistro(@RequestBody SegUsuarioTemporalRol segUsuarioTemporalRol){
        return segUsuarioTemporalService.deleteRolPreRegistro(segUsuarioTemporalRol);
    }

    @PostMapping("/regresarPreRegistro")
    public SegUsuarioTemporal regresarPreRegistro(@RequestBody SegUsuarioTemporal segUsuarioTemporal){
        SegUsuarioTemporal segUsuarioTemporal1 = segUsuarioTemporalService.regresarPreRegistro(segUsuarioTemporal);
        return segUsuarioTemporal1;
    }

    @PostMapping("/generarToken")
    public SegUsuarioTemporal generarToken(@RequestBody SegUsuarioTemporal segUsuarioTemporal){
        SegUsuarioTemporal segUsuarioTemporal1 = segUsuarioTemporalService.generarToken(segUsuarioTemporal);
        return segUsuarioTemporal1;
    }

    @GetMapping("/getUsuarioTemporal/{idUsuarioTemporal}")
    public SegUsuarioTemporal getUsuarioTemporal(@PathVariable("idUsuarioTemporal") Long idUsuarioTemporal){
        return segUsuarioTemporalService.getUsuarioTemporalId(idUsuarioTemporal);
    }

}
