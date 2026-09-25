package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoSistema;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatTipoSistemaService;
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
public class CatTipoSistemaRestController {

    @Autowired
    private CatTipoSistemaService catTipoSistemaService;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @GetMapping("/getSistema")
    public List<CatTipoSistema> obtenTipoSistema(){
        return catTipoSistemaService.getListaTipoSistema();
    }

    @PostMapping("/tablaTipoSistema")
    public JSONObject getTablaTipoSistema(){
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("Tipos_Sistemas")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        List<CatTipoSistema> listaTiposistema = catTipoSistemaService.getListaTipoSistema();

        listaTiposistema.forEach(tipoSistema -> {
            JSONArray json = new JSONArray();
            json.add(tipoSistema.getNombreTipoSistema());
            json.add(tipoSistema.getNombreTipoSistemaAbreviado());
            json.add(tipoSistema.getAlcanceSistema() != null ? tipoSistema.getAlcanceSistema() : "");
            json.add(tipoSistema.getRequiereSistemaPadre());
            json.add(tipoSistema.getNumeroOrdenTipoSistema());
            json.add(tipoSistema.getCveEstatus());

            String enlaces = "<div class=\"btn-group\"> ";
            if (segPerfilPrivilegio.getEditar())
                enlaces += "<a href=#' class='btn btn-success btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Editar Tipo Sistema' onclick='editarTipoSistema(\"" + tipoSistema.getCveTipoSistema() + "\")'><i class='bi bi-pencil-square'></i></a>";
            if (segPerfilPrivilegio.getEliminar())
                enlaces += "<a href='javascript:void(0);' class='btn btn-danger btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Tipo Sistema' onclick='eliminarTipoSistema(\"" + tipoSistema.getCveTipoSistema() + "\")'><i class='bi bi-trash3'></i></a>";
            enlaces += "</div>";
            json.add(enlaces);
            array.add(json);
        });

        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/guardarTipoSistemas")
    public CatTipoSistema getTipoSistemas(@RequestBody CatTipoSistema catTipoSistema){
        return catTipoSistemaService.guardarTipoSistemas(catTipoSistema);
    }

    @GetMapping("/getTipoSistema/{cveTipoSistema}")
    public CatTipoSistema getTipoSistemas(@PathVariable(name = "cveTipoSistema") String cveTipoSistema){
        return catTipoSistemaService.getTipoSistemaCve(cveTipoSistema);
    }

    @DeleteMapping("/deleteTipoSistema")
    public CatTipoSistema deleteTipoSistema(@RequestBody CatTipoSistema catTipoSistema){
        return catTipoSistemaService.deleteTipoSistema(catTipoSistema);
    }

    @GetMapping("/exportarTipoSistema")
    public void exportarTipoSistema(HttpServletResponse response) throws IOException {

        ClassPathResource resource = new ClassPathResource("static/plantillas/cat_tipo_sistemas.xlsx");
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

        List<CatTipoSistema> lista = catTipoSistemaService.getListaTipoSistema();

        CellStyle wrapStyle = workbook.createCellStyle();
        wrapStyle.setWrapText(true);
        wrapStyle.setVerticalAlignment(VerticalAlignment.TOP);

        sheet.setColumnWidth(2, 80 * 256);

        int i = 4;
        for (CatTipoSistema ts : lista) {
            Row row = sheet.createRow(i++);
            row.createCell(0).setCellValue(ts.getNombreTipoSistema()          != null ? ts.getNombreTipoSistema()          : "");
            row.createCell(1).setCellValue(ts.getNombreTipoSistemaAbreviado() != null ? ts.getNombreTipoSistemaAbreviado() : "");
            Cell alcanceCell = row.createCell(2);
            alcanceCell.setCellValue(ts.getAlcanceSistema()                   != null ? ts.getAlcanceSistema()             : "");
            alcanceCell.setCellStyle(wrapStyle);
            row.createCell(3).setCellValue(ts.getRequiereSistemaPadre()       != null ? ts.getRequiereSistemaPadre()       : "");
            row.createCell(4).setCellValue(ts.getNumeroOrdenTipoSistema()     != null ? ts.getNumeroOrdenTipoSistema()     : 0);
            row.createCell(5).setCellValue(ts.getCveEstatus()                 != null ? ts.getCveEstatus()                 : "");
        }

        for (int col = 0; col < 6; col++) {
            if (col != 2) sheet.autoSizeColumn(col);
        }

        String filename = "tipo_sistemas_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
