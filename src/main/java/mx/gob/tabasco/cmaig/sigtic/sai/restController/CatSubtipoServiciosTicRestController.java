package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatSubtipoServiciosTicService;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo/tic")
public class CatSubtipoServiciosTicRestController {

    @Autowired
    private CatSubtipoServiciosTicService catSubtipoServiciosTicService;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @GetMapping("/getCatSubtipoServiciosTic/{idTipoServicio}")
    public List<CatSubtipoServiciosTic> obtenCatSubtipoServiciosTic(@PathVariable("idTipoServicio") Long idTipoServicio){
        return catSubtipoServiciosTicService.getCatSubtipoServiciosTic(idTipoServicio);
    }

    @GetMapping("/getCatSubtipoServiciosTicCr/{idTipoServicio}")
    public List<CatSubtipoServiciosTic> obtenCatSubtipoServiciosTicCr(@PathVariable("idTipoServicio") Long idTipoServicio){
        return catSubtipoServiciosTicService.getCatSubtipoServiciosTicCr(idTipoServicio);
    }

    @PostMapping("/tablaSubtipoServiciosTic")
    public JSONObject getSubtipoServiciosTic(){
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("Subtipo_Servicios")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        boolean existeControlFirma = authUser.getListaPerfil().stream().filter(p->p.getNombrePerfil().equals("CONTROL_FIRMA_SUBTIPO")).findFirst().isPresent();

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        List<CatSubtipoServiciosTic> listaSubtipoServicios = catSubtipoServiciosTicService.getCatSubtipoServiciosTicAll();

        listaSubtipoServicios.forEach(subtipoServiciosTic -> {
            JSONArray json = new JSONArray();
            json.add(subtipoServiciosTic.getNombreSubtipoServicioTic());
            json.add(subtipoServiciosTic.getCatTipoServiciosTic().getNombreTipoServicioTic());
            json.add(subtipoServiciosTic.getControlInventario());
            json.add(subtipoServiciosTic.getControlPuerto());
            json.add(subtipoServiciosTic.getControlIp());
            json.add(subtipoServiciosTic.getControlUsuarios());
            json.add(subtipoServiciosTic.getGeneraCr() + " (" + subtipoServiciosTic.getCrFirmada() + ")");
            json.add(subtipoServiciosTic.getCveEstatus());
            String enlaces = "<div class=\"btn-group\"> ";
            if (segPerfilPrivilegio.getEditar())
                enlaces += "<a href=#' class='btn btn-success btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Editar Subtipo Servicio' onclick='editarSubtipoServiciosTic(" + subtipoServiciosTic.getIdSubtipoServicioTic() + ")'><i class='bi bi-pencil-square'></i></a>";
            enlaces += "</br>";
            if (segPerfilPrivilegio.getEliminar())
                enlaces += "<a href='javascript:void(0);' class='btn btn-danger btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Subtipo Servicio' onclick='eliminarSubTipoServicio(" + subtipoServiciosTic.getIdSubtipoServicioTic() + ")'><i class='bi bi-trash3'></i></a>";
            if (existeControlFirma)
                enlaces += "<a href='javascript:void(0);' class='btn btn-primary btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Configurar Número Firmas' onclick='configurarNumeroFirmas(" + subtipoServiciosTic.getIdSubtipoServicioTic() + ")'><i class=\"fa-solid fa-gear\"></i></a>";
            enlaces += "</div>";
            json.add(enlaces);
            array.add(json);
        });

        jsonObject.put("isTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/saveSubtipoServiciosTic")
    public CatSubtipoServiciosTic saveSubtipoServiciosTic(@RequestBody CatSubtipoServiciosTic catSubtipoServiciosTic){
        return catSubtipoServiciosTicService.guardarSubtipoServiciosTic(catSubtipoServiciosTic);
    }

    @GetMapping("/getSubtipoServiciosTic/{id}")
    public CatSubtipoServiciosTic getSubtipoServiciosTic(@PathVariable("id") Long idSubtipoServicioTic){
        return catSubtipoServiciosTicService.getSubtipoServiciosTic(idSubtipoServicioTic);
    }

    @DeleteMapping("/deleteSubTipoServicio")
    public CatSubtipoServiciosTic deleteSubTipoServicio(@RequestBody CatSubtipoServiciosTic catSubtipoServiciosTic){
        return catSubtipoServiciosTicService.deleteSubTipoServicio(catSubtipoServiciosTic);
    }

    @GetMapping("/exportarSubtipoServicio")
    public void exportarSubtipoServicio(HttpServletResponse response) throws IOException {

        ClassPathResource resource = new ClassPathResource("static/plantillas/cat_subtipo_servicios_tic.xlsx");
        Workbook workbook = new XSSFWorkbook(resource.getInputStream());

        Sheet sheet = workbook.getSheetAt(0);

        Row filaFecha = sheet.getRow(2);
        if (filaFecha == null) filaFecha = sheet.createRow(2);
        Cell labelCell = filaFecha.getCell(0);
        if (labelCell == null) labelCell = filaFecha.createCell(0);
        labelCell.setCellValue("Fecha de Descarga:");
        int lastCol = sheet.getRow(3).getLastCellNum() - 1;
        Cell fechaCell = filaFecha.getCell(lastCol);
        if (fechaCell == null) fechaCell = filaFecha.createCell(lastCol);
        fechaCell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));


        List<CatSubtipoServiciosTic> lista = catSubtipoServiciosTicService.getCatSubtipoServiciosTicAll();

        int i = 4;
        for (CatSubtipoServiciosTic st : lista) {
            Row row = sheet.createRow(i++);
            row.createCell(0).setCellValue(st.getIdSubtipoServicioTic()     != null ? st.getIdSubtipoServicioTic()     : 0L);
            row.createCell(1).setCellValue(st.getCveSubtipoServicioTic()    != null ? st.getCveSubtipoServicioTic()    : "");
            row.createCell(2).setCellValue(st.getCatTipoServiciosTic()      != null ? st.getCatTipoServiciosTic().getNombreTipoServicioTic() : "");
            row.createCell(3).setCellValue(st.getNombreSubtipoServicioTic() != null ? st.getNombreSubtipoServicioTic() : "");
            row.createCell(4).setCellValue(st.getControlInventario()        != null ? st.getControlInventario()        : "");
            row.createCell(5).setCellValue(st.getControlPuerto()            != null ? st.getControlPuerto()            : "");
            row.createCell(6).setCellValue(st.getControlIp()                != null ? st.getControlIp()                : "");
            row.createCell(7).setCellValue(st.getControlUsuarios()          != null ? st.getControlUsuarios()          : "");
            String generaCr = (st.getGeneraCr()  != null ? st.getGeneraCr()  : "")
                            + (st.getCrFirmada() != null ? " (" + st.getCrFirmada() + ")" : "");
            row.createCell(8).setCellValue(generaCr);
            row.createCell(9).setCellValue(st.getCveEstatus()               != null ? st.getCveEstatus()               : "");
        }

        for (int col = 0; col < 10; col++) { sheet.autoSizeColumn(col); }

        String filename = "subtipo_servicios_tic_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
