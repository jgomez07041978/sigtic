package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatTipoServiciosTicService;
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
public class CatTipoServiciosTicRestController {

    @Autowired
    private CatTipoServiciosTicService catTipoServiciosTicService;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @PostMapping("/tablaTipoServicio")
    public JSONObject getTablaTipoServicio(){

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("Tipos_Servicios")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        List<CatTipoServiciosTic> listaTipoServicio=catTipoServiciosTicService.getCatTipoServiciosTicAll();

        listaTipoServicio.forEach(tipoServicio -> {

                JSONArray json = new JSONArray();
                json.add(tipoServicio.getNombreTipoServicioTicAbrev());
                json.add(tipoServicio.getNombreTipoServicioTic());
                json.add(tipoServicio.getNumOrdenTipoServicioTic());
                json.add(tipoServicio.getCveEstatus());

            String enlaces = "<div class=\"btn-group\"> ";
                if (segPerfilPrivilegio.getEditar())
                    enlaces += "<a href=#' class='btn btn-success btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Editar Tipo Servicio' onclick='editarTipoServicio(" + tipoServicio.getIdTipoServicioTic() + ")'><i class='bi bi-pencil-square'></i></a>";
                if (segPerfilPrivilegio.getEliminar())
                    enlaces += "<a href='javascript:void(0);' class='btn btn-danger btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Tipo Servicio' onclick='eliminarTipoServicio(" + tipoServicio.getIdTipoServicioTic() + ")'><i class='bi bi-trash3'></i></a>";
                enlaces += "</div>";
                json.add(enlaces);

                array.add(json);
            });

        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @GetMapping("/getCatTipoServiciosTic")
    public List<CatTipoServiciosTic> obtenCatTipoServiciosTic(){
        return catTipoServiciosTicService.getCatTipoServiciosTicAll();
    }

    @GetMapping("/getCatTipoServiciosTicCr")
    public List<CatTipoServiciosTic> obtenCatTipoServiciosTicCr(){
        return catTipoServiciosTicService.getCatTipoServiciosTicAllCr();
    }

    @GetMapping("/getTipoServicio/{id}")
    public CatTipoServiciosTic getTipoServicio (@PathVariable("id") Long idTipoServicioTic){
        return catTipoServiciosTicService.getCatTipoServiciosTic(idTipoServicioTic);
    }

    @PostMapping("/guardarTipoServicio")
    public CatTipoServiciosTic guardarTipoServicio(@RequestBody CatTipoServiciosTic catTipoServiciosTic){
        return catTipoServiciosTicService.guardarTipoServicio(catTipoServiciosTic);
    }

    @DeleteMapping("/deleteTipoServicio")
    public CatTipoServiciosTic deleteTipoServicio(@RequestBody CatTipoServiciosTic catTipoServiciosTic){
        return catTipoServiciosTicService.deleteTipoServicio(catTipoServiciosTic);
    }

    @GetMapping("/exportarTipoServicio")
    public void exportarTipoServicio(HttpServletResponse response) throws IOException {

        ClassPathResource resource = new ClassPathResource("static/plantillas/cat_tipo_servicios_tic.xlsx");
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

        List<CatTipoServiciosTic> lista = catTipoServiciosTicService.getCatTipoServiciosTicAll();

        int i = 4;
        for (CatTipoServiciosTic ts : lista) {
            Row row = sheet.createRow(i++);
            row.createCell(0).setCellValue(ts.getIdTipoServicioTic()          != null ? ts.getIdTipoServicioTic()          : 0L);
            row.createCell(1).setCellValue(ts.getNombreTipoServicioTicAbrev() != null ? ts.getNombreTipoServicioTicAbrev() : "");
            row.createCell(2).setCellValue(ts.getNombreTipoServicioTic()      != null ? ts.getNombreTipoServicioTic()      : "");
            row.createCell(3).setCellValue(ts.getNumOrdenTipoServicioTic()    != null ? ts.getNumOrdenTipoServicioTic()    : 0L);
            row.createCell(4).setCellValue(ts.getCveEstatus()                 != null ? ts.getCveEstatus()                 : "");
        }

        for (int col = 0; col < 5; col++) { sheet.autoSizeColumn(col); }

        String filename = "tipo_servicios_tic_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
