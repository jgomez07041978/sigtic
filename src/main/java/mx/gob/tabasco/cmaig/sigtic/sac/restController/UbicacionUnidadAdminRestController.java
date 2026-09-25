package mx.gob.tabasco.cmaig.sigtic.sac.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.model.DomicilioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.DomicilioEntePublicoBean;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UbicacionUnidadAdmin;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.DomicilioEntePublicoBeanRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.service.EntePublicoService;
import mx.gob.tabasco.cmaig.sigtic.sac.service.UbicacionUnidadAdminService;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/catalogo/ubicacionUnidadAministrativa")
public class UbicacionUnidadAdminRestController {

    @Autowired
    private UbicacionUnidadAdminService ubicacionUnidadAdminService;

    @Autowired
    private EntePublicoService entePublicoService;

    @Autowired
    private DomicilioEntePublicoBeanRepository domicilioEntePublicoBeanRepository;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;


    @GetMapping(path = "/all/{idDomicilioEntePublico}")
    public List<UbicacionUnidadAdmin> getUbicacion(@PathVariable(value = "idDomicilioEntePublico") Long idDomicilioEntePublico){
        if (idDomicilioEntePublico!=null)
            return ubicacionUnidadAdminService.listaUbicacionesUnidadPorDomicilio(idDomicilioEntePublico);
        else
            return new ArrayList<>();
    }

    @PostMapping("/tablaUbicaciones")
    public JSONObject getTablaUbicaciones(@RequestBody DomicilioEntePublicoBean domicilioEntePublicoBean)
    {
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<SegMenu> segMenuOp=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("CAT_UBICACIONES")).findFirst();
        if (segMenuOp.isPresent()) {
            SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC", authUser.getUsername(), segMenuOp.get().getIdMenu());

            List<UbicacionUnidadAdmin> listaUbicaciones = ubicacionUnidadAdminService.listaUbicacionesUnidadPorDomicilioVigentes(domicilioEntePublicoBean.getIdDomicilio());

            for (UbicacionUnidadAdmin ubicacionUnidadAdmin : listaUbicaciones) {
                JSONArray json = new JSONArray();
                json.add(ubicacionUnidadAdmin.getIdUbicacionAdaministrativa());
                json.add(ubicacionUnidadAdmin.getNombreUbicacion());
                json.add(ubicacionUnidadAdmin.getCveEstatus());

                String enlaces = "";

                if (segPerfilPrivilegio.getEditar())
                    enlaces += "<a href='#' class='btn btn-outline-success mr-1' data-toggle='tooltip' data-placement='top' title='Editar Ubicación' onclick='editarUbicacion(" + ubicacionUnidadAdmin.getIdUbicacionAdaministrativa() + ")'><i class='fas fa-edit'></i></a>";

                if (segPerfilPrivilegio.getEliminar())
                    enlaces += "<a href='javascript:void(0);' class='btn btn-outline-secondary' data-toggle='tooltip' data-placement='top' title='Eliminar Ubicación' onclick='eliminarUbicacion(" + ubicacionUnidadAdmin.getIdUbicacionAdaministrativa() + ")'><i class='fas fa-trash'></i></a>";
                json.add(enlaces);

                array.add(json);
            }

        }

        EntePublico entePublico = entePublicoService.getEntePublicoId(domicilioEntePublicoBean.getIdEntePublico());
        DomicilioEntePublicoBean domicilioEntePublicoBean1 = domicilioEntePublicoBeanRepository.getDomicilioId(domicilioEntePublicoBean.getIdDomicilio());

        jsonObject.put("nombreEntePublico", entePublico.getNombreEntePublicoAbreviada()+") "+entePublico.getNombreEntePublico());
        jsonObject.put("nombreDomicilio", domicilioEntePublicoBean1.getNombreDomicilio());
        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }


    @GetMapping("/get/{idUbicacion}")
    public UbicacionUnidadAdmin getUbicacionId(@PathVariable(name="idUbicacion") Long idUbicacion){
        return ubicacionUnidadAdminService.obtenerUbicacionId(idUbicacion);
    }

    @PostMapping("/save")
    public UbicacionUnidadAdmin saveUbicacion(@RequestBody UbicacionUnidadAdmin ubicacionUnidadAdmin){
        return ubicacionUnidadAdminService.saveUbicacion(ubicacionUnidadAdmin);
    }

    @DeleteMapping("/delete")
    public UbicacionUnidadAdmin deleteUbicacion(@RequestBody UbicacionUnidadAdmin ubicacionUnidadAdmin){
        return ubicacionUnidadAdminService.deleteUbicacion(ubicacionUnidadAdmin);
    }


}
