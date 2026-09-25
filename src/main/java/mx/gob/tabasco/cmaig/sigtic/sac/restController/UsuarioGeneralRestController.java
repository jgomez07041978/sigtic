package mx.gob.tabasco.cmaig.sigtic.sac.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioSql;
import mx.gob.tabasco.cmaig.sigtic.sac.service.CatUsuarioEntePublicoService;
import mx.gob.tabasco.cmaig.sigtic.sac.service.CatUsuarioService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo/usuario/general")
public class UsuarioGeneralRestController {

    @Autowired
    private CatUsuarioService catUsuarioService;

    @Autowired
    private CatUsuarioEntePublicoService catUsuarioEntePublicoService;

    @PostMapping("/tablaUsuario")
    public JSONObject getUsuarios(@RequestBody CatUsuarioEntePublico catUsuarioEntePublico)
    {

        List<CatUsuarioSql> usuarios = catUsuarioEntePublicoService.getUsuariosPorEnte(catUsuarioEntePublico);
        //String total = 0
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        if (usuarios.size() > 0) {
            for (CatUsuarioSql usuarioR : usuarios)
            {   JSONArray json = new JSONArray();
                json.add(usuarioR.getRfc());
                json.add(usuarioR.getNombreUsuario());
                json.add(usuarioR.getDescEntePublico());
                json.add(usuarioR.getDescUnidadAdmtva());
                json.add(usuarioR.getDescEstatus());
                json.add("<a /*target='_blank'*/ href='#' class='btn btn-outline-success mr-1' data-toggle='tooltip' data-placement='top' title='Editar Usuario' onclick='editarUsuario("+ usuarioR.getIdUsuarioEntePublico() +")'><i class='fas fa-edit'></i></a>"
                        +"<a href='javascript:void(0);' class='btn btn-outline-danger' data-toggle='tooltip' data-placement='top' title='Eliminar Usuario' onclick='eliminarUsuario("+ usuarioR.getIdUsuarioEntePublico() +")'><i class='fas fa-trash'></i></a>");

                array.add(json);
            }
        }
        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @GetMapping("/getUsuario/{idUsuario}")
    public CatUsuarioEntePublico getUsuario(@PathVariable("idUsuario") Long idUsuario){
        return catUsuarioEntePublicoService.getUsuario(idUsuario);
    }

    /*-------------------------------------------------------------*/


    @PostMapping("/tablaUsuarioSinDependencia")
    public JSONObject getUsuariosSinDependencia()
    {

        List<CatUsuario> usuarios = catUsuarioService.getUsuariosSinDependencia();
        //String total = 0
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        if (usuarios.size() > 0) {
            for (CatUsuario usuarioR : usuarios)
            {   JSONArray json = new JSONArray();
                json.add(usuarioR.getRfc());
                json.add(usuarioR.getCurp());
                json.add(usuarioR.getNombreCompleto());
                json.add("<a /*target='_blank'*/ href='#' class='btn btn-outline-success mr-1' data-toggle='tooltip' data-placement='top' title='Agregar Usuario' onclick='agregarUsuario(\""+ usuarioR.getRfc().toUpperCase().trim() +"\")'><i class='bi bi-plus-circle'></i></a>");

                array.add(json);
            }
        }
        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }


    @GetMapping("/usuarioPorUnidad/{idUnidad}")
    List<CatUsuario> getUsuarioPorUnidad(@PathVariable("idUnidad") Long pIdUnidad){
        return catUsuarioEntePublicoService.getCatUsuarioPorIdUnidad(pIdUnidad);
    }

    @PostMapping("/validar-usuario")
    public CatUsuarioEntePublico validarUsuario(@RequestBody CatUsuarioEntePublico catUsuarioEntePublico)
    {
        return  catUsuarioEntePublicoService.validarUsusario(catUsuarioEntePublico);
    }

    @PostMapping("/guardarUsuario")
    public CatUsuarioEntePublico guardarUsuario(@RequestBody CatUsuarioEntePublico catUsuarioEntePublico){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.from(dateTimeFormatter.parse(catUsuarioEntePublico.getCatUsuario().getFechaNacimientoText()+" 00:00:00"));
        //localDateTime= localDateTime.plusDays(1);
        catUsuarioEntePublico.getCatUsuario().setFechaNacimiento(Timestamp.valueOf(localDateTime));

        return  catUsuarioEntePublicoService.guardarUsuario(catUsuarioEntePublico);
    }

    @GetMapping("/getUsuarioEntePublico/{idUsuarioEntePublico}")
    public CatUsuarioEntePublico getUsuarioEntePublico(@PathVariable("idUsuarioEntePublico") Long idUsuarioEntePublico){
        return catUsuarioEntePublicoService.getUsuario(idUsuarioEntePublico);
    }

    @DeleteMapping("/deleteUsuario")
    public CatUsuarioEntePublico deleteUsuario(@RequestBody CatUsuarioEntePublico catUsuarioEntePublico){
        return catUsuarioEntePublicoService.deleteUsuario(catUsuarioEntePublico);
    }


}
