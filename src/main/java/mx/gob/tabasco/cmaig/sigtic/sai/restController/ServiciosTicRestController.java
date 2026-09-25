package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.SacCatUsuarioEntePublicoView;
import mx.gob.tabasco.cmaig.sigtic.sac.service.CatUsuarioService;
import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.UsuarioServicioRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.service.ServiciosTicService;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

@RestController
@RequestMapping("/api/catalogo/tic")
public class ServiciosTicRestController {

    @Autowired
    private ServiciosTicService serviciosTicService;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @Autowired
    private UsuarioServicioRepository usuarioServicioRepository;

    @Autowired
    private CatUsuarioService catUsuarioService;

    @PostMapping("/tablaServiciosGroup")
    public JSONObject getServicioGroup(@RequestBody EntePublico entePublico) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("Servicio")).findFirst().get();
        //SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        List<ServicioGroupBean> listaServicio = serviciosTicService.getListaServicioGroup(authUser.getUsername(), entePublico.getIdEntePublico(),authUser.isAdministradorSigtic());

        listaServicio.forEach(servicio -> {
                JSONArray json = new JSONArray();
                String enlace = "<a href='#' onclick='verServicio("+ servicio.getIdTipoServicio() + ","+servicio.getIdSubTipoServicio()+",\""+servicio.getNombreServicio()+"\",\""+servicio.getNombreSubServicio()+"\")'>"+servicio.getNombreSubServicio()+"</a>";
                json.add(servicio.getNombreServicio());
                json.add(enlace);
                json.add(servicio.getTotal());
                array.add(json);
            });

        jsonObject.put("aaData", array);
        return jsonObject;
    }


    @PostMapping("/tablaServiciosTic")
    public JSONObject getServicio(@RequestBody ServiciosTic servicio) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("Servicio")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        List<ServiciosTic> listaServiciosTic= serviciosTicService.getListaServicioPorEntePorTipoAndSubtipo(servicio.getEntePublicoAdmin().getIdEntePublico(), servicio.getUnidadAdministrativaAdmin().getIdUnidadAdministrativa(),
                        servicio.getTipoServicio().getIdTipoServicioTic(), servicio.getSubTipoServicio().getIdSubtipoServicioTic(), servicio.getTipo(), servicio.getCveEstatus());

        Properties prop = new Properties();

        if (listaServiciosTic.size()>0){

            if (servicio.getTipo().intValue()==2) {
                if (listaServiciosTic.get(0).getEntePublicoOpera().getTipoEntePublico().getNombreGrupoTipoEntePublico().equals("Poder Ejecutivo"))
                    prop.put("tipoEntePublico",listaServiciosTic.get(0).getEntePublicoOpera().getTipoEntePublico().getNombreGrupoTipoEntePublico()+" | "+listaServiciosTic.get(0).getEntePublicoOpera().getTipoEntePublico().getNombreTipoEntePublico());
                else
                    prop.put("tipoEntePublico",listaServiciosTic.get(0).getEntePublicoOpera().getTipoEntePublico().getNombreTipoEntePublico());

                prop.put("titleEntePublico","Ente Público Opera: ");
                prop.put("titleEntePublicoTabla","Ente Público/Unidad (Administra)");
                prop.put("entePublico",listaServiciosTic.get(0).getEntePublicoOpera().getNombreEntePublicoAbreviada()+") "+listaServiciosTic.get(0).getEntePublicoOpera().getNombreEntePublico());
            }

            if (servicio.getTipo().intValue()==1) {
                if (listaServiciosTic.get(0).getEntePublicoAdmin().getTipoEntePublico().getNombreGrupoTipoEntePublico().equals("Poder Ejecutivo"))
                    prop.put("tipoEntePublico",listaServiciosTic.get(0).getEntePublicoAdmin().getTipoEntePublico().getNombreGrupoTipoEntePublico()+" | "+listaServiciosTic.get(0).getEntePublicoAdmin().getTipoEntePublico().getNombreTipoEntePublico());
                else
                    prop.put("tipoEntePublico",listaServiciosTic.get(0).getEntePublicoAdmin().getTipoEntePublico().getNombreTipoEntePublico());

                prop.put("titleEntePublico","Ente Público Administra: ");
                prop.put("titleEntePublicoTabla","Ente Público/Unidad (Opera)");
                prop.put("entePublico",listaServiciosTic.get(0).getEntePublicoAdmin().getNombreEntePublicoAbreviada()+") "+listaServiciosTic.get(0).getEntePublicoAdmin().getNombreEntePublico());
            }

            prop.put("servicio",listaServiciosTic.get(0).getTipoServicio().getNombreTipoServicioTic());
            prop.put("subServicio",listaServiciosTic.get(0).getSubTipoServicio().getNombreSubtipoServicioTic());

        }

            listaServiciosTic.forEach(serviciosTic -> {
                JSONArray json = new JSONArray();
                json.add(serviciosTic.getCveServicioTic());
                json.add(serviciosTic.getNombreServicioTic());

                if (servicio.getTipo().intValue()==2) {
                    if (serviciosTic.getEntePublicoAdmin() != null && serviciosTic.getUnidadAdministrativaAdmin() != null)
                        json.add("<span data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='" + serviciosTic.getEntePublicoAdmin().getNombreEntePublico() + "'>" + serviciosTic.getEntePublicoAdmin().getNombreEntePublicoAbreviada() + "</span>/<span data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='" + serviciosTic.getUnidadAdministrativaAdmin().getNombreUnidadAdministrativa() + "'>" + serviciosTic.getUnidadAdministrativaAdmin().getNombreUnidadAdministrativaAbreviada() + "</span>");
                    else if (serviciosTic.getEntePublicoAdmin() != null)
                        json.add("<span data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='" + serviciosTic.getEntePublicoAdmin().getNombreEntePublico() + "'>" + serviciosTic.getEntePublicoAdmin().getNombreEntePublicoAbreviada() + "</span>");
                    else
                        json.add("");
                }

                if (servicio.getTipo().intValue()==1) {
                    if (serviciosTic.getEntePublicoOpera() != null && serviciosTic.getUnidadAdministrativaOpera() != null)
                        json.add("<span data-bs-toggle='tooltip' data-bs-toggle='top' data-bs-title='" + serviciosTic.getEntePublicoOpera().getNombreEntePublico() + "'>" + serviciosTic.getEntePublicoOpera().getNombreEntePublicoAbreviada() + "</span>/<span data-bs-toggle='tooltip' data-bs-toggle='top' data-bs-title='" + serviciosTic.getUnidadAdministrativaOpera().getNombreUnidadAdministrativa() + "'>" + serviciosTic.getUnidadAdministrativaOpera().getNombreUnidadAdministrativaAbreviada() + "</span>");
                    else if (serviciosTic.getEntePublicoOpera() != null)
                        json.add("<span data-bs-toggle='tooltip' data-bs-toggle='top' data-bs-title='" + serviciosTic.getEntePublicoOpera().getNombreEntePublico() + "'>" + serviciosTic.getEntePublicoOpera().getNombreEntePublicoAbreviada() + "</span>");
                    else
                        json.add("");
                }


                if (serviciosTic.getEstadosServicioTic()!=null)
                    json.add(serviciosTic.getEstadosServicioTic().getDescEstadoServicioTic());
                else
                    json.add("");

                if (serviciosTic.getEstatusServicioTic()!=null)
                    json.add(serviciosTic.getEstatusServicioTic().getDescEstatusServicioTic());
                else
                    json.add("");
                json.add(serviciosTic.getCveEstatus());

                String enlaces = "<div class='d-grid gap-3 d-md-block'>";
                if (servicio.getTipo().intValue()==1 && segPerfilPrivilegio.getEditar())
                    enlaces += "<a href=#' class='btn btn-success btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Editar Servicio' onclick='editarServicio(" + serviciosTic.getIdServicioTic() + ")'><i class='bi bi-pencil-square'></i></a>";
                if (servicio.getTipo().intValue()==1 && segPerfilPrivilegio.getEliminar())
                    enlaces += "<a href='javascript:void(0);' class='btn btn-danger btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Servicio' onclick='eliminarServicio(" + serviciosTic.getIdServicioTic() + ")'><i class='bi bi-trash3'></i></a>";
                enlaces += "</div>";
                json.add(enlaces);
                array.add(json);
            });
       // }

        jsonObject.put("aaData", array);
        jsonObject.put("etiquetas", prop);
        return jsonObject;
    }

    @PostMapping("/saveServicio")
    public  ServiciosTic saveServicioTic(@RequestBody ServiciosTic serviciosTic){
        return serviciosTicService.guardarServicios(serviciosTic);
    }

    @GetMapping("/getServiciosTic/{id}")
    public  ServiciosTic getServiciosTic(@PathVariable("id") Long idServicioTic){
        return serviciosTicService.getServiciosTic(idServicioTic);
    }

    @DeleteMapping("/deleteServiciosTic")
    public ServiciosTic deleteServiciosTic(@RequestBody ServiciosTic serviciosTic){
        return serviciosTicService.deleteServiciosTic(serviciosTic);
    }

    @PostMapping("/getListaServiciosTablero")
    public JSONObject getListaServiciosTablero(@RequestBody UnidadAdministrativa unidadAdministrativa){
        return serviciosTicService.getListaServiciosTablero(unidadAdministrativa);
    }

    @PostMapping("/getServiciosSearch")
    public JSONObject getServiciosSearch(@RequestBody ServiciosTic serviciosTic) {

        List<ServiciosTic> lista = serviciosTicService.getServiciosSearch(serviciosTic.getIdServicioTic(), serviciosTic.getNombreServicioTic(),serviciosTic.getClasificacion(), serviciosTic.isSinFiltrar());


        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        lista.forEach(servicio -> {
            JSONArray json = new JSONArray();
            json.add(servicio.getTipoServicio().getNombreTipoServicioTic());
            json.add(servicio.getCveServicioTic());
            json.add(servicio.getNombreServicioTic());
            json.add(servicio.getEstadosServicioTic().getDescEstadoServicioTic());
            json.add(servicio.getEstatusServicioTic().getDescEstatusServicioTic());
            String enlace = "<a href='#' onclick='agregarServicio("+ servicio.getIdServicioTic()+")'><i class=\"fas fa-circle-plus fa-2x\"></i></a>";
            json.add(enlace);
            array.add(json);
        });

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/tablaServicioRelacionados")
    public JSONObject tablaServicioRelacionados(@RequestBody ServiciosTic serviciosTic) {

        List<ServiciosTicRelacionados> lista = serviciosTicService.getServiciosTicRelacionados(serviciosTic);

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        lista.forEach(servicioRel -> {
            JSONArray json = new JSONArray();
            json.add(servicioRel.getServiciosTic().getTipoServicio().getNombreTipoServicioTic());
            json.add(servicioRel.getServiciosTic().getCveServicioTic());
            json.add(servicioRel.getServiciosTic().getNombreServicioTic());
            json.add(servicioRel.getDescripcionServicioTic());
            json.add(servicioRel.getNotaServicioTicRelacionado());
            String enlace = "<a href='#'  class='btn btn-outline-danger' onclick='eliminarServicioRelacionado("+ servicioRel.getIdServicioTicRelacionado()+",\""+serviciosTic.getClasificacion()+"\")'><i class='fas fa-trash fa-2x'></i></a>";
            json.add(enlace);
            array.add(json);
        });

        jsonObject.put("aaData", array);
        return jsonObject;
    }


    @PostMapping("/addServicio")
    public ServiciosTicRelacionados addServicio(@RequestBody ServiciosTicRelacionados serviciosTicRelacionados){
        return serviciosTicService.addServicio(serviciosTicRelacionados);
    }

    @DeleteMapping("/deleteServiciosRelacionado")
    public ServiciosTicRelacionados deleteServiciosRelacionado(@RequestBody ServiciosTicRelacionados serviciosTicRelacionados){
        return serviciosTicService.deleteServicioRelacionado(serviciosTicRelacionados);
    }

    @PostMapping("/saveMiServicio")
    public  ServiciosTic saveMiServicio(@RequestBody ServiciosTic serviciosTic){
        return serviciosTicService.guardarMiServicio(serviciosTic);
    }

    @PostMapping("/saveUsuarioServicio")
    public  UsuarioServicio saveUSuarioServicioTic(@RequestBody UsuarioServicio usuarioServicio){
        return serviciosTicService.guardarUsuarioServicios(usuarioServicio);
    }

    @PostMapping("/tablaUsuariosServicio")
    public JSONObject tablaUsuariosServicios(@RequestBody ServiciosTic serviciosTic) {

        List<UsuarioServicio> lista = serviciosTicService.getUsuariosServicios(serviciosTic.getIdServicioTic());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        lista.forEach(usuarioServicio -> {
            JSONArray json = new JSONArray();
            json.add(usuarioServicio.getCatTipoUsuarioServicio().getTipoUsuarioServicio());
            json.add(usuarioServicio.getCurpUsuarioServicio());

            if (usuarioServicio.getCatUsuarioEntePublico() != null && usuarioServicio.getCatUsuarioEntePublico().getCatUsuario() != null) {
                json.add(usuarioServicio.getCatUsuarioEntePublico().getCatUsuario().getNombreCompleto());
            } else {
                String nombreFallback = "";
                if (usuarioServicio.getCurpUsuarioServicio() != null) {
                    nombreFallback = catUsuarioService.getUsuarioCurp(usuarioServicio.getCurpUsuarioServicio())
                            .map(CatUsuario::getNombreCompleto)
                            .orElse("");
                }
                json.add(nombreFallback);
            }
            if (usuarioServicio.getFechaAltaServicio()!=null && usuarioServicio.getFechaBajaServicio()!=null)
                json.add(usuarioServicio.getFechaAltaServicioText()+"<br/>"+usuarioServicio.getFechaBajaServicioText());
            else
                if (usuarioServicio.getFechaAltaServicio()!=null)
                    json.add(usuarioServicio.getFechaAltaServicioText());
                else
                    json.add("");

                if (usuarioServicio.getEntePublicoUsuarioServicio()!=null)
                    json.add(usuarioServicio.getEntePublicoUsuarioServicio().getNombreEntePublico());
                else
                    json.add("");

            json.add(usuarioServicio.getCveEstatus());

            String enlace = "<a href='#' class='btn btn-outline-danger' onclick='eliminarUsuarioServicio("+ usuarioServicio.getIdUsuarioServicio()+")'><i class='fas fa-trash fa-2x'></i></a>" +
                    " <a href='#' class='btn btn-outline-warning' onclick='editarUsuarioServicio("+ usuarioServicio.getIdUsuarioServicio()+")'><i class='fas fa-edit fa-2x'></i></a>";
            json.add(enlace);
            array.add(json);
        });

        jsonObject.put("aaData", array);
        return jsonObject;
    }

//    @GetMapping("/getUsuarioServicio/{idUsuarioServicio}")
//    public UsuarioServicio getUsuarioServicio(@PathVariable Long idUsuarioServicio) {
//        return usuarioServicioRepository.findById(idUsuarioServicio)
//                .orElseThrow(() -> new NoSuchElementException("Usuario servicio no encontrado"));
//    }

    // se módifico este RestController traer más datos de otras tablas
    @GetMapping("/getUsuarioServicio/{idUsuarioServicio}")
    public UsuarioServicio getUsuarioServicio(@PathVariable Long idUsuarioServicio) {
        UsuarioServicio usuarioServicio = usuarioServicioRepository.findById(idUsuarioServicio)
                .orElseThrow(() -> new NoSuchElementException("Usuario servicio no encontrado"));

        boolean sinDatosUsuario = usuarioServicio.getCatUsuarioEntePublico() == null
                || usuarioServicio.getCatUsuarioEntePublico().getCatUsuario() == null;

        if (sinDatosUsuario && usuarioServicio.getCurpUsuarioServicio() != null) {
            catUsuarioService.getUsuarioCurp(usuarioServicio.getCurpUsuarioServicio())
                    .ifPresent(catUsuario -> {
                        usuarioServicio.setNombreCompletoFallback(catUsuario.getNombreCompleto());
                        usuarioServicio.setRfcFallback(catUsuario.getRfc());
                    });
        }

        return usuarioServicio;
    }

    @DeleteMapping("/deleteUsuarioServicio")
    public UsuarioServicio deleteUsuarioServicio(@RequestBody UsuarioServicio usuarioServicio){
        return serviciosTicService.deleteUsuarioServicio(usuarioServicio.getIdUsuarioServicio());
    }

    @PostMapping("/serviciosSearch")
    public ResponseEntity<?> getServicioSearch(@RequestBody ServiciosTic servicio){

        if (servicio.getCurp()!=null && !servicio.getCurp().equals(""))
            return ResponseEntity.ok(serviciosTicService.getUsuarioServiciosSearch(servicio));
        else
            return ResponseEntity.ok(serviciosTicService.getServiciosSearch(servicio));
    }


    /*
    @PostMapping("/tablaServiciosSearch")
    public JSONObject getServicioSearch(@RequestBody ServiciosTic servicio) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("Servicio")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        if (servicio.getEntePublicoAdmin().getIdEntePublico()!=null) {

            List<ServiciosTic> listaServiciosTic = serviciosTicService.getServiciosSearch(servicio);

            listaServiciosTic.forEach(serviciosTic -> {
                JSONArray json = new JSONArray();

                if (serviciosTic.getSubTipoServicio() != null)
                    json.add(serviciosTic.getTipoServicio().getNombreTipoServicioTic() + "/" + serviciosTic.getSubTipoServicio().getNombreSubtipoServicioTic());
                else
                    json.add("");

                json.add(serviciosTic.getCveServicioTic());
                json.add(serviciosTic.getNombreServicioTic());

                if (serviciosTic.getEntePublicoOpera() != null && serviciosTic.getUnidadAdministrativaOpera() != null)
                    json.add("<span data-bs-toggle='tooltip' data-bs-toggle='top' data-bs-title='" + serviciosTic.getEntePublicoOpera().getNombreEntePublico() + "'>" + serviciosTic.getEntePublicoOpera().getNombreEntePublicoAbreviada() + "</span>/<span data-bs-toggle='tooltip' data-bs-toggle='top' data-bs-title='" + serviciosTic.getUnidadAdministrativaOpera().getNombreUnidadAdministrativa() + "'>" + serviciosTic.getUnidadAdministrativaOpera().getNombreUnidadAdministrativaAbreviada() + "</span>");
                else if (serviciosTic.getEntePublicoOpera() != null)
                    json.add("<span data-bs-toggle='tooltip' data-bs-toggle='top' data-bs-title='" + serviciosTic.getEntePublicoOpera().getNombreEntePublico() + "'>" + serviciosTic.getEntePublicoOpera().getNombreEntePublicoAbreviada() + "</span>");
                else
                    json.add("");

                if (serviciosTic.getEstadosServicioTic() != null)
                    json.add(serviciosTic.getEstadosServicioTic().getDescEstadoServicioTic());
                else
                    json.add("");

                if (serviciosTic.getEstatusServicioTic() != null)
                    json.add(serviciosTic.getEstatusServicioTic().getDescEstatusServicioTic());
                else
                    json.add("");
                json.add(serviciosTic.getCveEstatus());

                String enlaces = "<div class='d-grid gap-3 d-md-block'>";

                if (authUser.isAdministradorSigtic() || authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico().longValue() == serviciosTic.getEntePublicoAdmin().getIdEntePublico().longValue()) {
                    if (segPerfilPrivilegio.getEditar())
                        enlaces += "<a href='#' class='btn btn-success btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Editar Servicio' onclick='editarServicio(" + serviciosTic.getIdServicioTic() + ")'><i class='bi bi-pencil-square'></i></a>";
                    if (segPerfilPrivilegio.getEliminar())
                        enlaces += "<a href='javascript:void(0);' class='btn btn-danger btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Servicio' onclick='eliminarServicio(" + serviciosTic.getIdServicioTic() + ")'><i class='bi bi-trash3'></i></a>";
                }

                enlaces += "</div>";
                json.add(enlaces);
                array.add(json);
            });
        }

        jsonObject.put("aaData", array);
        return jsonObject;
    }*/

    @GetMapping("/exportarServicioAdministra")
    public void exportarServicioDetalle(
            @RequestParam Long idEntePublico,
            @RequestParam(required = false) Long idUnidadAdministrativa,
            @RequestParam Long idTipoServicio,
            @RequestParam Long idSubtipoServicio,
            @RequestParam Integer tipo,
            @RequestParam(required = false) String cveEstatus,
            @RequestParam(required = false) String tipoEntePublicoText,
            @RequestParam(required = false) String entePublicoText,
            @RequestParam(required = false) String tipoServicioText,
            @RequestParam(required = false) String subTipoServicioText,
            HttpServletResponse response) throws IOException {

        List<ServiciosTic> lista = serviciosTicService.getListaServicioPorEntePorTipoAndSubtipo(
                idEntePublico, idUnidadAdministrativa, idTipoServicio, idSubtipoServicio, tipo, cveEstatus);

        serviciosTicService.escribirExcelServiciosAdministra(lista, tipo, tipoEntePublicoText, entePublicoText,
                tipoServicioText, subTipoServicioText, response);
    }

    @GetMapping("/exportarServicioOpera")
    public void exportarServicioDetalleOpera(
            @RequestParam Long idEntePublico,
            @RequestParam(required = false) Long idUnidadAdministrativa,
            @RequestParam Long idTipoServicio,
            @RequestParam Long idSubtipoServicio,
            @RequestParam(required = false) String cveEstatus,
            @RequestParam(required = false) String tipoEntePublicoText,
            @RequestParam(required = false) String entePublicoText,
            @RequestParam(required = false) String tipoServicioText,
            @RequestParam(required = false) String subTipoServicioText,
            HttpServletResponse response) throws IOException {

        List<ServiciosTic> lista =
                serviciosTicService.getListaServicioPorEntePorTipoAndSubtipo(
                        idEntePublico,
                        idUnidadAdministrativa,
                        idTipoServicio,
                        idSubtipoServicio,
                        2,
                        cveEstatus);

        serviciosTicService.escribirExcelServiciosOpera(
                lista,
                tipoEntePublicoText,
                entePublicoText,
                tipoServicioText,
                subTipoServicioText,
                response);
    }

}