package mx.gob.tabasco.cmaig.sigtic.sigget.restController;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.*;
import mx.gob.tabasco.cmaig.sigtic.sigget.service.CargaMasivaService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.*;

@RestController
@RequestMapping("/api/carga_masiva")
public class CargaMasivaRestController {

    @Autowired
    private CargaMasivaService cargaMasivaService;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @PostMapping("/all")
    public JSONObject getPlantillasAll(@RequestBody CargaDatosPlantillaHead cargaDatosPlantillaHead) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("CARGA_MASIVA")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        if (cargaDatosPlantillaHead.getPlantilla()!=null && cargaDatosPlantillaHead.getPlantilla().getIdPlantilla()!=null){
            cargaDatosPlantillaHead.setUsuarioCargaDato(authUser.getRfc());
            List<CargaDatosPlantillaHead> datosCarga = cargaMasivaService.getCargaDatosPlantillaHead(cargaDatosPlantillaHead);
            Integer total=0;

            if (datosCarga.size() > 0) {
                for (CargaDatosPlantillaHead carga : datosCarga)
                {   JSONArray json = new JSONArray();
                    json.add(carga.getPlantilla().getNombrePlantilla());
                    json.add(carga.getFechaCorteText());
                    json.add(carga.getFechaRegistroText());
                    json.add(carga.getUsuarioCargaDato());
                    json.add(carga.getTotalRegistros());
                    if (carga.getTotalError()==0)
                        json.add(carga.getTotalError());
                    else
                        json.add("<a href='javascript:void(0);' class='btn btn-outline-danger' data-toggle='tooltip' data-placement='top' title='Lista de Errores' onclick='verErrores("+ carga.getIdCargaDatosPlantilla()  +","+ carga.getPlantilla().getIdPlantilla() +")'>"+carga.getTotalError()+"</a>");

                    json.add(carga.getEstatusText());
                    String acciones="";
                    acciones+="<div class='btn-group' role='group' aria-label='Default button group'>";
                    if (segPerfilPrivilegio.getEditar() && (carga.getEstatus().equals("VA") || carga.getEstatus().equals("CE")))
                        acciones+="<a href='javascript:void(0);' class='btn btn-outline-success' data-toggle='tooltip' data-placement='top' title='Ejecutar Script' onclick='ejecutarScript("+ carga.getIdCargaDatosPlantilla() +")'><i class='fas fa-play'></i></a>";
                    if (segPerfilPrivilegio.getEditar() &&  (carga.getEstatus().equals("PE") ||  carga.getEstatus().equals("VE")))
                        acciones+="<a href='javascript:void(0);' class='btn btn-outline-success' data-toggle='tooltip' data-placement='top' title='Validar datos de Plantilla' onclick='validarCarga("+ carga.getIdCargaDatosPlantilla() +","+ carga.getPlantilla().getIdPlantilla() +")'><i class='fas fa-spell-check'></i></a>";

                    acciones+="<a href='javascript:void(0);' class='btn btn-outline-primary' data-toggle='tooltip' data-placement='top' title='Detalles de la Plantilla' onclick='detallesCarga("+ carga.getIdCargaDatosPlantilla() +","+ carga.getPlantilla().getIdPlantilla() +")'><i class='far fa-list-alt'></i></a>";

                    if (segPerfilPrivilegio.getEditar() && !carga.getEstatus().equals("CA"))
                        acciones+="<a href='javascript:void(0);' class='btn btn-outline-primary' data-toggle='tooltip' data-placement='top' title='Subir Plantilla' onclick='subirPlantilla("+ carga.getIdCargaDatosPlantilla() +",\""+ carga.getFechaCorteText() +"\")'><i class='fas fa-edit'></i></a>";
                    if (segPerfilPrivilegio.getEliminar() && !carga.getEstatus().equals("CA"))
                    acciones+="<a href='javascript:void(0);' class='btn btn-outline-danger' data-toggle='tooltip' data-placement='top' title='Eliminar Plantillas' onclick='eliminarCarga("+ carga.getIdCargaDatosPlantilla() +","+ carga.getPlantilla().getIdPlantilla() +")'><i class='fas fa-trash'></i></a>";
                    acciones+="</div>";

                    json.add(acciones);
                    array.add(json);
                }
            }
        }

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping
    public String cargaMasiva(@RequestBody CargaMasivaBean cargaMasiva){
        return cargaMasivaService.procesarCargaMasiva(cargaMasiva);
    }

    @GetMapping("/exportPlantilla/{idPlantilla}")
    public void exportPlantilla(HttpServletResponse response, @PathVariable("idPlantilla") Long idPlantilla) {
        cargaMasivaService.generaPlantilla(idPlantilla, response);
    }

    @Transactional(value = "siggetTransactionManager")
    @DeleteMapping
    public void deleteCarga(@RequestBody CargaDatosPlantilla cargaDatosPlantilla){
        cargaMasivaService.deleteCarga(cargaDatosPlantilla);
    }


    @GetMapping("/tablaCargaDetalle/{idCargaDatos}/{idPlantilla}")
    public JSONObject getCargaMasivaDetalle(@PathVariable("idCargaDatos") Long idCargaDatos, @PathVariable("idPlantilla") Long idPlantilla){

        return cargaMasivaService.getCargaMasivaDetalle(idCargaDatos, idPlantilla);
    }

    @Transactional(value = "siggetTransactionManager")
    @GetMapping("/validarPlantilla/{idCargaDatos}/{idPlantilla}")
    public JSONObject validarPlantilla(@PathVariable("idCargaDatos") Long idCargaDatos, @PathVariable("idPlantilla") Long idPlantilla){
        return cargaMasivaService.validarCargaMasiva(idCargaDatos, idPlantilla);
    }

    @GetMapping("/tablaErroresPlantilla/{idCargaDatos}/{idPlantilla}")
    public JSONObject getErroresPlantilla(@PathVariable("idCargaDatos") Long idCargaDatos, @PathVariable("idPlantilla") Long idPlantilla) {
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        List<CargaDatosPlantillaError> datosCargaError = cargaMasivaService.getErroresPlantilla(idCargaDatos);
        String total="0";

        for (CargaDatosPlantillaError error : datosCargaError)
        {   JSONArray json = new JSONArray();
            json.add(error.getNumLineaPlantilla());
            json.add(error.getColumna());
            json.add(error.getInfoError());
            json.add(error.getFechaRegistroText());

            array.add(json);
        }

        Optional<Plantilla> plantillaOp = cargaMasivaService.getPlantillaId(idPlantilla);

        if (plantillaOp.isPresent())
            jsonObject.put("nombrePlantilla", plantillaOp.get().getNombrePlantilla());
        else
            jsonObject.put("nombrePlantilla", "");


        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @Transactional(value = "siggetTransactionManager")
    @GetMapping("/ejecutaSql/{idCargaDatos}")
    public JSONObject ejecutaSql(@PathVariable("idCargaDatos") Long idCargaDatos){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", cargaMasivaService.ejecutarSql(idCargaDatos));
        return jsonObject;
    }


    @GetMapping("/concentradoBienesPorEstatus")
    public JSONObject concentradoBienesPorEstatus() {
        return cargaMasivaService.getConcentradoBienesEstatus();
    }

    @GetMapping("/concentradoBienesPorEstatus/{estatus}")
    public JSONObject concentradoBienesEntePublico(@PathVariable("estatus") String estatus) {
        List<ConcentradoCargaEnteBean> listaConcentrado = cargaMasivaService.getConcentradoBienesEntePublico(estatus);
        DecimalFormat formatea = new DecimalFormat("###,###,###");

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (ConcentradoCargaEnteBean reg : listaConcentrado)
        {   JSONArray json = new JSONArray();
            json.add(reg.getEntePublico());
            json.add(reg.getFechaCorte());
            json.add(reg.getRfc());

            String nombre="";

            if (reg.getNombre()!=null)
                nombre= reg.getNombre();

            if (reg.getSegundoNombre()!=null)
                nombre= nombre+" "+reg.getSegundoNombre();

            if (reg.getApellidoPa()!=null)
                nombre= nombre+" "+reg.getApellidoPa();

            if (reg.getApellidoMa()!=null)
                nombre= nombre+" "+reg.getApellidoMa();

            json.add(nombre);
            json.add(reg.getEstatus());

            json.add(formatea.format(reg.getTotalRegistro()));

            array.add(json);

        }
        jsonObject.put("aaData", array);
        return jsonObject;

    }
}
