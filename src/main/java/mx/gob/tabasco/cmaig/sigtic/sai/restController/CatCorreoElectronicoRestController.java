package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioSql;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sac.service.CatUsuarioEntePublicoService;
import mx.gob.tabasco.cmaig.sigtic.sac.service.EntePublicoService;
import mx.gob.tabasco.cmaig.sigtic.sac.service.UnidadesAdministrativaService;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatCorreoElectronico;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatCorreoElectronicoBean;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatCorreoElectronicoService;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegAccesoUsuarioService;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegUsuarioService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/catalogo/tic")
public class CatCorreoElectronicoRestController {

    @Autowired
    private CatCorreoElectronicoService catCorreoElectronicoService;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @Autowired
    private CatUsuarioEntePublicoService catUsuarioEntePublicoService;

    @Autowired
    private EntePublicoService entePublicoService;

    @Autowired
    private UnidadesAdministrativaService unidadesAdministrativaService;

/*    @GetMapping("/getCorreosElectronicos")
    public List<CatCorreoElectronico> obtenCorreosElectronicos(){
        return catCorreoElectronicoService.obtenerCorreosElectronicos();
    }*/


    @PostMapping("/tablaUsuarios")
    public JSONObject getUsuarios(@RequestBody CatUsuarioEntePublico catUsuarioEntePublico){

        catUsuarioEntePublico.setIsVigente(true);
        catUsuarioEntePublico.setIsNoVigente(false);
        catUsuarioEntePublico.setIsHistorico(false);

        List<CatUsuarioSql> usuarios = catUsuarioEntePublicoService.getUsuariosPorEnte(catUsuarioEntePublico);
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
                json.add("<a /*target='_blank'*/ href='#' class='btn btn-outline-success mr-1' data-toggle='tooltip' data-placement='top' title='Editar Usuario' onclick='seleccionarUsuario("+ usuarioR.getIdUsuarioEntePublico() +")'><i class='bi bi-person-check'></i></a>");

                array.add(json);
            }
        }
        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }


    @PostMapping("/tablaCorreosElectronicos")
    public JSONObject getCorreoElectronico(@RequestBody UnidadAdministrativa unidadAdministrativa){

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean existeRolVerPassword = authUser.getListaPerfil().stream().filter(p-> p.getNombrePerfil().equals("VER-PASSWORD-CORREO") ).findFirst().isPresent();

        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("CORREO_ELECTRONICO")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        List<EntePublico> listaEntePublico=null;
        if (authUser.isAdministradorSigtic())
            listaEntePublico = entePublicoService.getEntesPublicos();
        else
            listaEntePublico = entePublicoService.getEntesPublicos(authUser.getUsername());

        Map<Long, Long> mapEntePublico = listaEntePublico.stream().collect(Collectors.toMap(EntePublico::getIdEntePublico, EntePublico::getIdEntePublico));

        List<CatCorreoElectronicoBean> listaCorreo = null;

        if (authUser.isAdministradorSigtic()){
            if ((unidadAdministrativa.getCorreoSearch()!=null && !unidadAdministrativa.getCorreoSearch().equals("")))
                listaCorreo = catCorreoElectronicoService.obtenerCorreosElectronicosSearchAdmin(unidadAdministrativa.getCorreoSearch());
            else
                listaCorreo = catCorreoElectronicoService.obtenerCorreosElectronicos(unidadAdministrativa.getIdEntePublico(), unidadAdministrativa.getIdUnidadAdministrativa());
        }else{
            if (unidadAdministrativa.getCorreoSearch()!=null && !unidadAdministrativa.getCorreoSearch().equals(""))
                listaCorreo = catCorreoElectronicoService.obtenerCorreosElectronicosSearch(unidadAdministrativa.getCorreoSearch(), unidadAdministrativa.getIdEntePublico());
            else
                    listaCorreo = catCorreoElectronicoService.obtenerCorreosElectronicos(unidadAdministrativa.getIdEntePublico(), unidadAdministrativa.getIdUnidadAdministrativa());
        }




        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        listaCorreo.forEach(correo->{
            JSONArray json = new JSONArray();

            if (correo.getCurpUsuario()!=null){
                if (correo.getCurpUsuario().length()>4)
                    json.add(correo.getCurpUsuario().subSequence(0,4));
                else
                    json.add(correo.getCurpUsuario());
            }
            else
                json.add("");

            json.add(correo.getNombreCompleto());
            /*json.add(correo.getNombreServidorCorreo());*/
            json.add(correo.getNombreTipoCorreoElectronico());
            json.add(correo.getCorreoElectronico());
            json.add(correo.getCveEstatus());


            if (correo.getPasswordCorreo()==null || existeRolVerPassword==false)
                json.add("");
            else
                json.add("<span id='pw"+correo.getIdCorreoElectronico()+"'>"+StringUtils.rightPad("",correo.getPasswordCorreo().length(),"*")+"</span><i class='fa-solid fa-eye-slash text-primary' id='idEye"+correo.getIdCorreoElectronico()+"' onclick='mostrarClave("+correo.getIdCorreoElectronico()+",\""+correo.getPasswordCorreo()+"\",\""+StringUtils.rightPad("",correo.getPasswordCorreo().length(),"*")+"\")' style='cursor:pointer'></i>");

            //json.add(correo.getPasswordCorreo());
            json.add(correo.getFechaUltimoAcceso());
            json.add(correo.getEstatusValidacion());


            String enlaces = "<div class=\"btn-group\"> ";

            String actualizar="N";
            if (mapEntePublico.containsKey(correo.getIdEntePublico()))
                actualizar="S";

            if (authUser.isAdministradorSigtic())
                actualizar="S";

            String checked="";
            if (correo.getEstatusValidacion().equals("VIG"))
                checked="checked";


            String control="<div class='form-check form-switch'>" +
                    "  <input class='form-check-input' type='checkbox' role='switch' onchange='validarCorreo(" + correo.getIdCorreoElectronico()+")' id='idValCorreo"+ correo.getIdCorreoElectronico()+"' "+checked+">" +
                    "  <label class='form-check-label' for='idValCorreo"+ correo.getIdCorreoElectronico()+"'></label>" +
                    "</div>";

            /**/

            if (segPerfilPrivilegio.getValidar() && actualizar=="S")
                enlaces+=control;

 /*               enlaces += "<a href=#' class='btn btn-success' role='button' data-toggle='tooltip' data-placement='top' title='Validar Correo Como Vigente' onclick='validarCorreo(" + correo.getIdCorreoElectronico() + ",\"VIG\")'><i class='fas fa-check-circle'></i></a>";
            if (segPerfilPrivilegio.getValidar())
                enlaces += "<a href='javascript:void(0);' class='btn btn-danger' role='button' data-toggle='tooltip' data-placement='top' title='Validar Correo como No Vigente' onclick='validarCorreo(" + correo.getIdCorreoElectronico() + ",\"NOV\")'><i class='fas fa-ban'></i></a>";
*/

            if (segPerfilPrivilegio.getEditar())
                enlaces += "<a href='#' class='btn btn-success' role='button' data-toggle='tooltip' data-placement='top' title='Editar Sistema' onclick='editarCorreo(" + correo.getIdCorreoElectronico() + ",\""+actualizar+"\")'><i class='bi bi-pencil-square'></i></a>";
            if (segPerfilPrivilegio.getEliminar() && actualizar=="S")
                enlaces += "<a href='javascript:void(0);' class='btn btn-danger' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Sistema' onclick='eliminarCorreo(" + correo.getIdCorreoElectronico() + ")'><i class='bi bi-trash3'></i></a>";
            enlaces += "</div>";

            if (correo.getCveEstatus().equals("NOV"))
                json.add("");
            else
                json.add(enlaces);

          array.add(json);

        });

        jsonObject.put("aaData", array);
        return jsonObject;
    }


    @PostMapping("/saveCorreoElectronico")
    public CatCorreoElectronico saveCorreoElectronico(@RequestBody CatCorreoElectronico catCorreoElectronico){

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.from(dateTimeFormatter.parse(catCorreoElectronico.getFechaAltaCorreoElectronicoText()+" 00:00:00"));
        //localDateTime= localDateTime.plusDays(1);
        catCorreoElectronico.setFechaAltaCorreoElectronico(Timestamp.valueOf(localDateTime));
        return catCorreoElectronicoService.saveCorreoElectronico(catCorreoElectronico);

    }
    @GetMapping("/getCorreo/{id}")
    public  CatCorreoElectronico getCorreoElectronico(@PathVariable("id") Long idCorreoElectronico){
        return catCorreoElectronicoService.getCorreoElectronico(idCorreoElectronico);
    }

    @DeleteMapping("/deleteCorreoElectronico")
    public JSONObject deleteCorreoElectronico(@RequestBody CatCorreoElectronico catCorreoElectronico){
        return catCorreoElectronicoService.deleteCorreoElectronico(catCorreoElectronico);
    }

    @GetMapping("/getConcentradoTipoCorreo/{id}")
    public JSONObject getConcentradoTipoCorreo(@PathVariable("id") Long idEntePublico){
        return  catCorreoElectronicoService.getConcentradoTipoCorreo(idEntePublico);
    }

    @GetMapping("/getConcentradoTipoCorreoAll")
    public JSONObject getConcentradoTipoCorreoAll(){
        return  catCorreoElectronicoService.getConcentradoTipoCorreoAll();
    }

    @PutMapping("/vigCorreoElectronico")
    public CatCorreoElectronico vigCorreoElectronico(@RequestBody CatCorreoElectronico catCorreoElectronico){
        return catCorreoElectronicoService.vigCorreoElectronico(catCorreoElectronico);
    }

    @PutMapping("/novCorreoElectronico")
    public CatCorreoElectronico novCorreoElectronico(@RequestBody CatCorreoElectronico catCorreoElectronico){
        return catCorreoElectronicoService.novCorreoElectronico(catCorreoElectronico);
    }

    @GetMapping("/exportReporteCorreos/{idEntePublico}")
    public void exportReporteCorreos(HttpServletResponse response, @PathVariable("idEntePublico") Long idEntePublico) {
        catCorreoElectronicoService.generaReporteCorreos(idEntePublico, null, response);
    }

    @GetMapping("/exportReporteCorreosUnidad/{idEntePublico}/{idUnidad}")
    public void exportReporteCorreosUnidad(HttpServletResponse response, @PathVariable("idEntePublico") Long idEntePublico, @PathVariable("idUnidad") Long idUnidadAdministrativa) {
        catCorreoElectronicoService.generaReporteCorreos(idEntePublico, idUnidadAdministrativa, response);
    }

    @GetMapping("/getListaCorreoGroupTablero/{id}")
    public JSONObject getListaCorreoGroupTablero(@PathVariable("id") Long idEntePublico){
        JSONObject obj =catCorreoElectronicoService.getListaCorreoGroupTablero(idEntePublico, null);

        return catCorreoElectronicoService.getListaCorreoGroupTablero(idEntePublico, null);
    }

    @GetMapping("/getListaCorreoGroupTableroUnidad/{id}/{idUnidad}")
    public JSONObject getListaCorreoGroupTableroUnidad(@PathVariable("id") Long idEntePublico, @PathVariable("idUnidad") Long idUnidadAdministrativa){
        return catCorreoElectronicoService.getListaCorreoGroupTablero(idEntePublico, idUnidadAdministrativa);
    }

    @PostMapping("/tablaCorreoUnidad")
    public JSONObject getCorreoElectronicoUnidad(@RequestBody UnidadAdministrativa unidadAdministrativa){

        List<CatCorreoElectronicoBean> listaCorreo = catCorreoElectronicoService.obtenerCorreosElectronicosModal(unidadAdministrativa.getIdEntePublico(), unidadAdministrativa.getIdUnidadAdministrativa(), unidadAdministrativa.getIdTipoCorreo(), unidadAdministrativa.getEstatusCorreo());


        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        listaCorreo.forEach(correo->{
            JSONArray json = new JSONArray();

            if (correo.getCurpUsuario()!=null){
                if (correo.getCurpUsuario().length()>4)
                    json.add(correo.getCurpUsuario().subSequence(0,4));
                else
                    json.add(correo.getCurpUsuario());
            }
            else
                json.add("");

            json.add(correo.getNombreCompleto());
            /*json.add(correo.getNombreTipoCorreoElectronico());*/
            json.add(correo.getCorreoElectronico());
            json.add(correo.getCveEstatus());
            json.add(correo.getEstatusValidacion());
            array.add(json);

        });

        String tipoCorreo="";
        EntePublico entePublico1=entePublicoService.getEntePublicoId(unidadAdministrativa.getIdEntePublico());

        if (listaCorreo.size()>0){
            tipoCorreo=listaCorreo.get(0).getNombreTipoCorreoElectronico();

        }

        jsonObject.put("aaData", array);
        if(unidadAdministrativa.getIdUnidadAdministrativa()!=null) {
            UnidadAdministrativa unidadAdministrativa1 = unidadesAdministrativaService.getUnidadAdministrativaId(unidadAdministrativa.getIdUnidadAdministrativa());
            jsonObject.put("unidadAdmin", unidadAdministrativa1.getCveUnidadAdministrativa() + " - " + unidadAdministrativa1.getNombreUnidadAdministrativa());
        }else
            jsonObject.put("unidadAdmin", "Sin Unidad Administrativa");

        jsonObject.put("entePublico", entePublico1.getCveEntePublico()+" - "+entePublico1.getNombreEntePublico());
        jsonObject.put("tipoCorreo", tipoCorreo);

        return jsonObject;
    }


}
