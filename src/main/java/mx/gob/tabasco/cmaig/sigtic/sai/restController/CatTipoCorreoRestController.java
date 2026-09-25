package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoCorreoElectronico;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatSubtipoServiciosTicRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatTipoServiciosTicRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatTipoCorreoService;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.poi.ss.usermodel.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.core.io.ClassPathResource;


import java.util.List;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api/catalogo/tic")
public class CatTipoCorreoRestController {

    private final CatTipoCorreoService catTipoCorreoService;
    private final SegPerfilPrivilegioService segPerfilPrivilegioService;
    private final CatSubtipoServiciosTicRepository catSubtipoServiciosTicRepository;
    private final CatTipoServiciosTicRepository catTipoServiciosTicRepository;


    public CatTipoCorreoRestController(CatTipoCorreoService catTipoCorreoService, SegPerfilPrivilegioService segPerfilPrivilegioService, CatSubtipoServiciosTicRepository catSubtipoServiciosTicRepository, CatTipoServiciosTicRepository catTipoServiciosTicRepository) {
        this.catTipoCorreoService = catTipoCorreoService;
        this.segPerfilPrivilegioService = segPerfilPrivilegioService;
        this.catSubtipoServiciosTicRepository = catSubtipoServiciosTicRepository;
        this.catTipoServiciosTicRepository = catTipoServiciosTicRepository;
    }

    @GetMapping("/getTipoCorreoElectronico")
    public List<CatTipoCorreoElectronico> obtenTipoCorreoElectronico() {
        return catTipoCorreoService.getListaTipoCorreo();
    }


    @PostMapping("/tablaTipoCorreo")
    public JSONObject getTablaTipoCorreoElectronico() {
        UserDetailsSesion authUser = (UserDetailsSesion)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SegMenu segMenu = authUser.getListaMenu().stream()
                .filter(p -> p.getCodigo().equals("TIPO_CORREO"))
                .findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService
                .getPerfilPrivilegioMenu("SIGTIC", authUser.getUsername(), segMenu.getIdMenu());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        List<CatTipoCorreoElectronico> lista = catTipoCorreoService.getListaTipoCorreo();

        lista.forEach(correo -> {
            JSONArray json = new JSONArray();
            json.add(correo.getCveTipoCorreoElectronico());
            json.add(correo.getNombreTipoCorreoElectronico());
            json.add(correo.getNumeroOrden());
            json.add(correo.getCveEstatus());

            String enlaces = "<div class=\"btn-group\"> ";
            if (segPerfilPrivilegio.getEditar())
                enlaces += "<a href='javascript:void(0);' class='btn btn-success btn-sm' "
                        + "data-toggle='tooltip' data-placement='top' title='Editar Tipo Correo' "
                        + "onclick='editarTipoCorreo(\"" + correo.getCveTipoCorreoElectronico() + "\")'>"
                        + "<i class='bi bi-pencil-square'></i></a>";
            if (segPerfilPrivilegio.getEliminar())
                enlaces += "<a href='javascript:void(0);' class='btn btn-danger btn-sm' "
                        + "data-toggle='tooltip' data-placement='top' title='Eliminar Tipo Correo' "
                        + "onclick='eliminarTipoCorreo(\"" + correo.getCveTipoCorreoElectronico() + "\")'>"
                        + "<i class='bi bi-trash3'></i></a>";
            enlaces += "</div>";
            json.add(enlaces);

            array.add(json);
        });

        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }



    @PostMapping("/guardarTipoCorreo")
    public CatTipoCorreoElectronico guardarTipoCorreo(@RequestBody CatTipoCorreoElectronico dto) {
        return catTipoCorreoService.guardarTipoCorreo(dto);
    }


    @GetMapping("/getTipoCorreo/{cveTipoCorreoElectronico}")
    public CatTipoCorreoElectronico getTipoCorreoElectronico(
            @PathVariable(name = "cveTipoCorreoElectronico") String cveTipoCorreoElectronico) {
        return catTipoCorreoService.getCatTipoCorreoElectronicoCve(cveTipoCorreoElectronico);
    }

    @DeleteMapping("/deleteTipoCorreo")
    public CatTipoCorreoElectronico deleteTipoCorreo(@RequestBody CatTipoCorreoElectronico dto) {
        return catTipoCorreoService.deleteTipoCorreoElectronico(dto);
    }

    @GetMapping("/exportarTipoCorreo")
    public void exportarTipoCorreo(HttpServletResponse response) throws IOException {

        ClassPathResource resource = new ClassPathResource("static/plantillas/cat_tipo_correo.xlsx");
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

        List<CatTipoCorreoElectronico> lista = catTipoCorreoService.getListaTipoCorreo();

        CellStyle wrapStyle = workbook.createCellStyle();
        wrapStyle.setWrapText(true);
        wrapStyle.setVerticalAlignment(VerticalAlignment.TOP);

        int i = 4;
        for (CatTipoCorreoElectronico correo : lista) {
            Row row = sheet.createRow(i++);
            row.createCell(0).setCellValue(correo.getCveTipoCorreoElectronico()  != null ? correo.getCveTipoCorreoElectronico()  : "");
            row.createCell(1).setCellValue(correo.getNombreTipoCorreoElectronico()  != null ? correo.getNombreTipoCorreoElectronico()  : "");
            row.createCell(4).setCellValue(correo.getNumeroOrden()                  != null ? correo.getNumeroOrden()                  : 0);
            row.createCell(5).setCellValue(correo.getCveEstatus()                   != null ? correo.getCveEstatus()                   : "");

            if (correo.getIdSubtipoServicio() != null) {
                CatSubtipoServiciosTic subtipo = catSubtipoServiciosTicRepository
                        .findById(correo.getIdSubtipoServicio())
                        .orElse(null);
                if (subtipo != null) {
                    row.createCell(2).setCellValue(subtipo.getCatTipoServiciosTic().getNombreTipoServicioTic());
                    row.createCell(3).setCellValue(subtipo.getNombreSubtipoServicioTic());
                } else {
                    row.createCell(2).setCellValue("");
                    row.createCell(3).setCellValue("");
                }
            } else {
                row.createCell(2).setCellValue("");
                row.createCell(3).setCellValue("");
            }
        }


        String filename = "tipo_correo_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
    @GetMapping("/getCatSubtipoServiciosTicById/{id}")
    public CatSubtipoServiciosTic getSubtipoServicioTicById(@PathVariable Long id) {
        return catSubtipoServiciosTicRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Subtipo no encontrado"));
    }

}