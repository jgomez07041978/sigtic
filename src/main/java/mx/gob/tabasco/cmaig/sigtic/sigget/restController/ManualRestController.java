package mx.gob.tabasco.cmaig.sigtic.sigget.restController;


import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.GrupoManual;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Manual;
import mx.gob.tabasco.cmaig.sigtic.sigget.service.ManualService;
import net.minidev.json.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/manual")
public class ManualRestController {

    @Autowired
    private ManualService manualService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @GetMapping("/listaManualesFree")
    public JSONObject getManualesFree(){

        List<Manual> listaManual = manualService.getManualesTipo("U");

        List<GrupoManual> listaGrupo = listaManual.stream().map(Manual::getGrupoManual).distinct().collect(Collectors.toList());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("listaGrupo", listaGrupo);
        jsonObject.put("listaManual", listaManual);

        return jsonObject;
    }

    @GetMapping("/listaManualesTecnico")
    public JSONObject getManualesTecnico(){

        List<Manual> listaManual = manualService.getManualesTipo("T");

        List<GrupoManual> listaGrupo = listaManual.stream().map(Manual::getGrupoManual).distinct().collect(Collectors.toList());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("listaGrupo", listaGrupo);
        jsonObject.put("listaManual", listaManual);

        return jsonObject;
    }

    @PostMapping("/grupos")
    public JSONObject getGrupos()
    {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("MANUALES")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());
        List<GrupoManual> grupos= manualService.getGrupos();
        List<Manual> manuales= manualService.getManuales();

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (GrupoManual grupoManual : grupos)
        {
            Long countManual= manuales.stream().filter(p->p.getGrupoManual().getIdGrupoManual().intValue()==grupoManual.getIdGrupoManual().intValue()).count();

            JSONArray json = new JSONArray();
            json.add(grupoManual.getNombreGrupo());
            json.add(grupoManual.getNumeroOrden());

            String acciones = "<div class='d-grid gap-3 d-md-block'>";
            if (segPerfilPrivilegio.getEditar())
                acciones += "<a href=#' class='btn btn-success btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Editar Grupo' onclick='editarGrupo(" + grupoManual.getIdGrupoManual() + ")'><i class='bi bi-pencil-square'></i></a>";
            if (segPerfilPrivilegio.getEliminar())
                acciones += "<a href='javascript:void(0);' class='btn btn-danger btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Grupo' onclick='eliminarGrupo(" + grupoManual.getIdGrupoManual() + ")'><i class='bi bi-trash3'></i></a>";
            acciones += "<a href='javascript:void(0);' class='btn btn-info btn-sm  position-relative' role='button' data-toggle='tooltip' data-placement='top' title='Agregar Manuales' onclick='modalAddManual(" + grupoManual.getIdGrupoManual() + ",\""+grupoManual.getNombreGrupo()+"\")'><i class='bi bi-plus-circle'></i>  <span class='position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger'>" +
                    +countManual+
                    "    <span class=\"visually-hidden\">unread messages</span>" +
                    "  </span></a>";
            acciones+="</div>";
            json.add(acciones);

            array.add(json);

        }
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/guardarGrupo")
    public GrupoManual guardarGrupo(@RequestBody GrupoManual grupoManual){
        return manualService.guardarGrupo(grupoManual);

    }

    @DeleteMapping("/deleteGrupo")
    public GrupoManual deleteGrupo(@RequestBody GrupoManual grupoManual){
        return manualService.deleteGrupo(grupoManual);
    }

    @GetMapping(value = "/getGrupoId/{id}")
    public GrupoManual getGrupoId(@PathVariable("id") Integer idGrupoManual){
        return manualService.getGrupoId(idGrupoManual);
    }

    @GetMapping("/getManuales/{id}")
    public JSONObject getManuales(@PathVariable("id") Integer idGrupoManual)
    {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("MANUALES")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        List<Manual> manuales= manualService.getManualesGrupo(idGrupoManual);

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (Manual manual : manuales)
        {
            JSONArray json = new JSONArray();
            json.add(manual.getNombreManual());
            json.add(manual.getDescripcionManual());
            json.add(manual.getTipoManualText());
            json.add(manual.getNumeroOrden());

            if (manual.getNombreArchivoOriginal()!=null) {
                json.add("<a class='btn btn-outline-success btn-sm' role='button' href='"+servletContext.getContextPath()+"/api/manual/exportManual/"+manual.getIdManual()+"' target='_blank' ><i class='bi bi-file-earmark-pdf-fill h5'></i></a>");
            }else
                json.add("");

            String acciones = "<div class='d-grid gap-3 d-md-block'>";
            if (segPerfilPrivilegio.getEditar())
                acciones += "<a href=#' class='btn btn-success btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Editar Manual' onclick='editarManual(" + manual.getIdManual() + ")'><i class='bi bi-pencil-square'></i></a>";
            if (segPerfilPrivilegio.getEliminar())
                acciones += "<a href='javascript:void(0);' class='btn btn-danger btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Manual' onclick='eliminarManual(" + manual.getIdManual() + ")'><i class='bi bi-trash3'></i></a>";

            acciones+="</div>";
            json.add(acciones);

            array.add(json);

        }
        jsonObject.put("aaData", array);
        return jsonObject;
    }


    @PostMapping("/guardarManual")
    public Manual guardarManual(@RequestBody Manual manual){
        return manualService.guardarManual(manual);

    }

    @DeleteMapping("/deleteManual")
    public Manual deleteManual(@RequestBody Manual manual){
        return manualService.deleteManual(manual);
    }

    @GetMapping(value = "/getManualId/{id}")
    public Manual getManualId(@PathVariable("id") Long idManual){
        return manualService.getManuald(idManual);
    }


    @GetMapping({"/exportManual/{id}"})
    public void exportCartaResponsiva(HttpServletResponse response, @PathVariable Long id){
        manualService.exportManual(response, id);
    }
}
