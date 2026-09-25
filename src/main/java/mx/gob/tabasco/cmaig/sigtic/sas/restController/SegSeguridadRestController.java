package mx.gob.tabasco.cmaig.sigtic.sas.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sas.model.RecuperarPassword;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuarioLista;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuarioPk;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegUsuarioService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/seguridad")
public class SegSeguridadRestController {

    @Autowired
    private SegUsuarioService segUsuarioService;

    @Autowired
    private ServletContext servletContext;


    @PostMapping("/tabla")
    public JSONObject tablaUsuarios(@RequestBody EntePublico entePublico){

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<SegUsuarioLista> listaUsuarios = segUsuarioService.getTablaUsuarios(authUser.getUsername(), entePublico.getIdEntePublico(), authUser.isAdministradorSigtic());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        listaUsuarios.forEach(usuario -> {
              JSONArray json = new JSONArray();
                json.add(usuario.getUsuario());
                json.add(usuario.getNombreCompleto());

                if (usuario.getNombreUnidadAdministrativa()!=null)
                    json.add(usuario.getNombreUnidadAdministrativa());
                else
                    json.add("");

                if (usuario.getNumRol()>0)
                    json.add("<a href='#' onclick='modalRolConsulta(\""+usuario.getUsuario().trim()+"\",\""+ usuario.getNombreCompleto().trim()+"\")'>"+usuario.getNumRol()+"</a>");
                else
                    json.add(usuario.getNumRol());

                json.add(usuario.getCveEstatus());
                    String accion= "<div class=\"btn-group\">" +
                            "<div class=\"dropdown\">"
                            + "<button class=\"btn btn-warning dropdown-toggle\" type=\"button\" data-bs-toggle=\"dropdown\" aria-expanded=\"false\"  title='Permisoss'> <i class=\"fa-solid fa-key\"></i></button>"
                            + "<div class=\"dropdown-menu\">"
                            + "<a class=\"dropdown-item\" href=\"#\" onclick='modalPerfil(\"" + usuario.getUsuario() + "\",\"" + usuario.getNombreCompleto() + "\")'>Asignar Rol</a>"
                            + "<a class=\"dropdown-item\"  href=\"#\" onclick='asignarAcceso(" + usuario.getIdUsuario() + ",\"" + usuario.getUsuario() + "\",\"" + usuario.getNombreCompleto() + "\")'>Asignar Acceso</a>"
                            + "<a class=\"dropdown-item\"  href=\"#\" onclick='modalSubTipoServicio(" + usuario.getIdUsuario() + ",\"" + usuario.getCurp() + "\",\"" + usuario.getNombreCompleto() + "\")'>Asignar Sub-Tipo Servicio</a>"
                            + "</div>"
                            + "</div>"
                            + "<a href='#' onclick='modalUsuarioEditar(" + usuario.getIdUsuarioEntePublico() + ")' class='btn btn-success mr-1' data-toggle='tooltip' data-placement='top' title='Editar Usuario'><i class='fas fa-edit'></i></a>"
                            + "<a href='#' class='btn btn-danger mr-1' data-toggle='tooltip' data-placement='top' title='Eliminar Usuario' onclick='eliminarUsuario(" + usuario.getIdUsuarioEntePublico() + ")'><i class='fa fa-user-times'></i></a>";

                    if (usuario.getCurp().equals("MEGO750526HCSNNS05"))
                        System.out.println(usuario.getCurp());

                    if (usuario.getIdUsuarioEntePublico().longValue()!=usuario.getIdUsuarioEntePublico2().longValue())
                        accion += "<a href='#' class='btn btn-success mr-1' data-toggle='tooltip' data-placement='top' title='Actualizar Ente Actual' onclick='actualizarEnteActual(" + usuario.getIdUsuarioEntePublico() +","+usuario.getIdUsuarioEntePublico2()+")'><i class=\"fa-solid fa-arrow-rotate-right\"></i></a>";

                    accion += "</div>";

                    json.add(accion);

                array.add(json);
        });

        jsonObject.put("aaData", array);
        return jsonObject;
    }
    @PostMapping("/tablaUsuariosDisponiblesSearch")
    public JSONObject tablaUsuariosDisponiblesSearch(@RequestBody UnidadAdministrativa unidadAdministrativa){
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        EntePublico entePublico= new EntePublico();
        entePublico.setIdEntePublico(unidadAdministrativa.getIdEntePublico());

        List<CatUsuarioEntePublico> listaUsuario =  segUsuarioService.getCatUsuariosPorEntePublicoRfcAll(entePublico,unidadAdministrativa.getRfcTitular());

        listaUsuario.forEach(usuario->{
            JSONArray json = new JSONArray();
            if(usuario.getUnidadAdministrativa()!=null)
                json.add(usuario.getUnidadAdministrativa().getNombreUnidadAdministrativa());
            else
                json.add("");
            json.add(usuario.getCatUsuario().getCurp());
            json.add(usuario.getCatUsuario().getNombreCompleto());
            json.add("<a href='#' onclick='seleccionarUsuario("+usuario.getCatUsuario().getIdUsuario()+",\""+usuario.getCatUsuario().getRfc()+"\",\""+usuario.getCatUsuario().getNombreCompleto()+"\")' class='btn btn-primary mr-1' data-toggle='tooltip' data-placement='top' title='Seleccionar Usuario'><i class=\"bi bi-person-check\"></i>\n</a>");

            array.add(json);
        });

        jsonObject.put("aaData", array);
        return jsonObject;
    }


    @PostMapping("/tablaUsuariosDisponibles2")
    public JSONObject getUsuariosDisponibles2(@RequestBody UnidadAdministrativa unidadAdministrativa){
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        EntePublico entePublico= new EntePublico();
        entePublico.setIdEntePublico(unidadAdministrativa.getIdEntePublico());

        List<CatUsuarioEntePublico> listaUsuario =  segUsuarioService.getCatUsuariosPorEntePublicoAll(entePublico);

        listaUsuario.forEach(usuario->{
            JSONArray json = new JSONArray();
            if(usuario.getUnidadAdministrativa()!=null)
                json.add(usuario.getUnidadAdministrativa().getNombreUnidadAdministrativa());
            else
                json.add("");
            json.add(usuario.getCatUsuario().getCurp());
            json.add(usuario.getCatUsuario().getNombreCompleto());
            json.add("<a href='#' onclick='seleccionarUsuario("+usuario.getCatUsuario().getIdUsuario()+",\""+usuario.getCatUsuario().getRfc()+"\",\""+usuario.getCatUsuario().getNombreCompleto()+"\")' class='btn btn-primary mr-1' data-toggle='tooltip' data-placement='top' title='Seleccionar Usuario'><i class=\"bi bi-person-check\"></i>\n</a>");

            array.add(json);
        });

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/tablaUsuariosDisponibles")
    public JSONObject getUsuariosDisponibles(@RequestBody UnidadAdministrativa unidadAdministrativa){
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        List<CatUsuarioEntePublico> listaUsuario =  segUsuarioService.getCatUsuariosPorUnidadAdministrativaAll(unidadAdministrativa);

        listaUsuario.forEach(usuario->{
            JSONArray json = new JSONArray();
            json.add(usuario.getUnidadAdministrativa().getNombreUnidadAdministrativa());
            json.add(usuario.getCatUsuario().getCurp());
            json.add(usuario.getCatUsuario().getNombreCompleto());
            json.add("<a href='#' onclick='seleccionarUsuario("+usuario.getIdUsuarioEntePublico()+","+usuario.getCatUsuario().getIdUsuario()+",\""+usuario.getCatUsuario().getCurp()+"\",\""+usuario.getCatUsuario().getCurp()+") "+usuario.getCatUsuario().getNombreCompleto()+"\")' class='btn btn-primary mr-1' data-toggle='tooltip' data-placement='top' title='Seleccionar Usuario'><i class=\"bi bi-person-check\"></i>\n</a>");

            array.add(json);
        });

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/tablaUsuariosDisponiblesPorEnte")
    public JSONObject getUsuariosDisponiblesPorEnte(@RequestBody EntePublico entePublico){
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        List<CatUsuarioEntePublico> listaUsuario =  null;

        if (entePublico.getIdEntePublico()!=null)
        listaUsuario =  segUsuarioService.getCatUsuariosPorEntePublicoAll(entePublico);
        else
            listaUsuario = new ArrayList<>();

        listaUsuario.forEach(usuario->{
            JSONArray json = new JSONArray();
            if (usuario.getUnidadAdministrativa()!=null)
                json.add(usuario.getUnidadAdministrativa().getNombreUnidadAdministrativa());
            else
                json.add("");

            json.add(usuario.getCatUsuario().getCurp());
            json.add(usuario.getCatUsuario().getNombreCompleto());
            json.add("<a href='#' onclick='seleccionarUsuario("+usuario.getCatUsuario().getIdUsuario()+",\""+usuario.getCatUsuario().getCurp()+"\",\""+usuario.getCatUsuario().getNombreCompleto()+"\","+usuario.getEntePublico().getIdEntePublico()+")' class='btn btn-primary mr-1' data-toggle='tooltip' data-placement='top' title='Seleccionar Usuario'><i class=\"bi bi-person-check\"></i>\n</a>");

            array.add(json);
        });

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/tablaUsuariosAutoriza")
    public JSONObject getUsuariosAutoriza(@RequestBody EntePublico entePublico){
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        List<CatUsuarioEntePublico> listaUsuario =  null;

        EntePublico entePublico1= segUsuarioService.getEntePublico(entePublico.getIdEntePublico());

        if (entePublico.getIdEntePublico()!=null)
            listaUsuario =  segUsuarioService.getCatUsuariosPorEntePublicoAll(entePublico);
        else
            listaUsuario = new ArrayList<>();

        listaUsuario.forEach(usuario->{
            JSONArray json = new JSONArray();
            if (usuario.getUnidadAdministrativa()!=null)
                json.add(usuario.getUnidadAdministrativa().getNombreUnidadAdministrativa());
            else
                json.add("");

            json.add(usuario.getCatUsuario().getCurp());
            json.add(usuario.getCatUsuario().getNombreCompleto());

            if (usuario.getPuesto()!= null)
                json.add(usuario.getPuesto());
            else
                json.add("");

            json.add("<a href='#' onclick='seleccionarUsuarioAutoriza("+usuario.getCatUsuario().getIdUsuario()+")' class='btn btn-primary mr-1' data-toggle='tooltip' data-placement='top' title='Asignar Usuario'><i class=\"bi bi-person-check\"></i>\n</a>");

            array.add(json);
        });

        jsonObject.put("aaData", array);
        jsonObject.put("entePublico", entePublico1);
        return jsonObject;
    }


    @PostMapping("/usuario")
    public SegUsuario getUsuarioId(@RequestBody SegUsuario segUsuario){
        return segUsuarioService.getUsuarioId(segUsuario.getIdUsuarioEntePublico());
    }

    @PostMapping("/saveUsuario")
    public SegUsuario saveUsuario(@RequestBody SegUsuario segUsuario){
        return segUsuarioService.saveUsuario(segUsuario);

    }

    @DeleteMapping("/deleteUsuario")
    public JSONObject deleteUsuario(@RequestBody SegUsuario segUsuario){
        return segUsuarioService.deleteUsuario(segUsuario);
    }

    @PostMapping("/actualizarPassword")
    public SegUsuario actualizarPassword(@RequestBody SegUsuario segUsuario){
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SegUsuarioPk segUsuarioPk = new SegUsuarioPk();
        segUsuarioPk.setUsuario(authUser.getUsername());
        segUsuarioPk.setSistema("SIGTIC");
        segUsuario.setSegUsuarioPk(segUsuarioPk);

        return segUsuarioService.actualizarPassword(segUsuario);

    }

    @PostMapping("/actualizarEntePublicoActual")
    public SegUsuario actualizarEntePublicoActual(@RequestBody SegUsuario segUsuario){
        return segUsuarioService.actualizarEntePublicoActual(segUsuario);
    }

    @PostMapping("/recuperarAcceso")
    public SegUsuario getRecuperarAcceso(@RequestBody RecuperarPassword recuperarPassword){
        return segUsuarioService.recuperarAcceso(recuperarPassword);
    }
    @PostMapping("/validarOpcionRecuperacion")
    public RecuperarPassword getvalidarOpcionRecuperacion(@RequestBody RecuperarPassword recuperarPassword){
        return segUsuarioService.validarDatosRecuperacion(recuperarPassword);
    }

    @PostMapping("/recuperarPassword")
    public SegUsuario recuperarrPassword(@RequestBody SegUsuario segUsuario){

        SegUsuarioPk segUsuarioPk = new SegUsuarioPk();
        segUsuarioPk.setUsuario(segUsuario.getSegUsuarioPk().getUsuario());
        segUsuarioPk.setSistema("SIGTIC");
        segUsuario.setSegUsuarioPk(segUsuarioPk);

        return segUsuarioService.recuperarPassword(segUsuario);

    }


}
