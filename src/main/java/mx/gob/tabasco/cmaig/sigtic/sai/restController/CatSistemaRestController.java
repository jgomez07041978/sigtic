package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSistema;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSistemaGit;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatSistemaGitService;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CatSistemaService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/catalogo/tic")
public class CatSistemaRestController {

    @Autowired
    private CatSistemaService catSistemaService;

    @Autowired
    private CatSistemaGitService catSistemaGitService;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @PostMapping("/tablaSistemas")
    public JSONObject getSistemas(@RequestBody UnidadAdministrativa unidadAdministrativa){

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu = authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("SISTEMAS")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC", authUser.getUsername(), segMenu.getIdMenu());

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        List<CatSistema> listaSistema;
        if (unidadAdministrativa.getIsAdministra() && unidadAdministrativa.getIsOpera())
            listaSistema = catSistemaService.getListaSistemasAll(authUser.getUsername(), unidadAdministrativa.getIdEntePublico(), unidadAdministrativa.getIdUnidadAdministrativa());
        else if (unidadAdministrativa.getIsAdministra())
            listaSistema = catSistemaService.getListaSistemasAdmin(authUser.getUsername(), unidadAdministrativa.getIdEntePublico(), authUser.isAdministradorSigtic());
        else if (unidadAdministrativa.getIsOpera())
            listaSistema = catSistemaService.getListaSistemasOpera(authUser.getUsername(), unidadAdministrativa.getIdEntePublico(), authUser.isAdministradorSigtic());
        else
            listaSistema = new ArrayList<>();

        listaSistema.forEach(sistema -> {
            JSONArray json = new JSONArray();
            json.add(sistema.getCodigoSistema());
            json.add(sistema.getNombreSistema());
            json.add(sistema.getCatTipoSistema().getNombreTipoSistema());

            if (sistema.getEntePublicoAdmin()!=null && sistema.getUnidadAdministrativaAdmin() != null)
                json.add("<span data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='"+sistema.getEntePublicoAdmin().getNombreEntePublico()+"'>"+sistema.getEntePublicoAdmin().getNombreEntePublicoAbreviada()+"</span>/<span data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='"+sistema.getUnidadAdministrativaAdmin().getNombreUnidadAdministrativa()+"'>"+sistema.getUnidadAdministrativaAdmin().getNombreUnidadAdministrativaAbreviada()+"</span>");
            else if (sistema.getEntePublicoAdmin()!=null)
                json.add("<span data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='"+sistema.getEntePublicoAdmin().getNombreEntePublico()+"'>"+sistema.getEntePublicoAdmin().getNombreEntePublicoAbreviada()+"</span>");
            else
                json.add("");

            if (sistema.getEntePublicoOpera()!=null && sistema.getUnidadAdministrativaOpera() != null)
                json.add("<span data-bs-toggle='tooltip' data-bs-toggle='top' data-bs-title='"+sistema.getEntePublicoOpera().getNombreEntePublico()+"'>"+sistema.getEntePublicoOpera().getNombreEntePublicoAbreviada()+"</span>/<span data-bs-toggle='tooltip' data-bs-toggle='top' data-bs-title='"+sistema.getUnidadAdministrativaOpera().getNombreUnidadAdministrativa()+"'>"+sistema.getUnidadAdministrativaOpera().getNombreUnidadAdministrativaAbreviada()+"</span>");
            else if (sistema.getEntePublicoOpera()!=null)
                json.add("<span data-bs-toggle='tooltip' data-bs-toggle='top' data-bs-title='"+sistema.getEntePublicoOpera().getNombreEntePublico()+"'>"+sistema.getEntePublicoOpera().getNombreEntePublicoAbreviada()+"</span>");
            else
                json.add("");

            json.add(sistema.getCveEstatus());

            String enlaces = "<div class=\"btn-group\"> ";
            if (sistema.getTotalSistemaHijos()>0)
                enlaces += "<a href=#' class='btn btn-warning btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Sistemas Hijos' onclick='verSistemasHijos(" + sistema.getIdSistema() + ",\""+sistema.getNombreSistema()+"\")'><i class='fa-solid fa-diagram-project'></i></a>";
            if (segPerfilPrivilegio.getEditar())
                enlaces += "<a href=#' class='btn btn-success btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Editar Sistema' onclick='editarSistema(" + sistema.getIdSistema() + ")'><i class='bi bi-pencil-square'></i></a>";
            if (segPerfilPrivilegio.getEliminar())
                enlaces += "<a href='javascript:void(0);' class='btn btn-danger btn-sm' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Sistema' onclick='eliminarSistema(" + sistema.getIdSistema() + ")'><i class='bi bi-trash3'></i></a>";
            enlaces += "</div>";
            json.add(enlaces);
            array.add(json);
        });

        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @GetMapping("/getListaSistemaPadre/{id}")
    public List<CatSistema> obtenListaSistemaPadre(@PathVariable("id") Long idEntePublico){
        return catSistemaService.getListaSistemaPadre(idEntePublico);
    }

    @PostMapping("/saveSistema")
    public CatSistema saveSistema(@RequestBody CatSistema catSistema){
        return catSistemaService.guardarSistema(catSistema);
    }

    @GetMapping("/getSistema/{id}")
    public CatSistema getSistema(@PathVariable("id") Long idSistema){
        return catSistemaService.getSistema(idSistema);
    }

    @DeleteMapping("/deleteSistema")
    public JSONObject deleteSistema(@RequestBody CatSistema catSistema){
        return catSistemaService.deleteSistema(catSistema);
    }

    @GetMapping("/tablaSistemaHijo/{id}")
    public JSONObject getSistemasHijos(@PathVariable("id") Long idSistema){
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        List<CatSistema> listaSistema = catSistemaService.getListaSistemasHijos(idSistema);
        listaSistema.forEach(sistema -> {
            JSONArray json = new JSONArray();
            json.add(sistema.getCodigoSistema());
            json.add(sistema.getNombreSistema());
            json.add(sistema.getCatTipoSistema().getNombreTipoSistema());
            json.add(sistema.getEntePublicoAdmin() != null ? sistema.getEntePublicoAdmin().getNombreEntePublicoConCve() : "");
            json.add(sistema.getUnidadAdministrativaAdmin() != null ? sistema.getUnidadAdministrativaAdmin().getNombreUnidadAdministrativa() : "");
            json.add(sistema.getCveEstatus());
            array.add(json);
        });
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @GetMapping("/reporte/{id}")
    public ResponseEntity<byte[]> getReporte(@PathVariable("id") Long id) {
        return catSistemaService.getReporte(id);
    }

    @GetMapping("/getSistemasVigente")
    public List<CatSistema> getSistema(){
        return catSistemaService.getListaSistemas("VIG");
    }

    @GetMapping("/exportarSistemas")
    public void exportarSistemas(@RequestParam(defaultValue = "0")  Long idEntePublico,
                                 @RequestParam(defaultValue = "0")  Long idUnidadAdministrativa,
                                 @RequestParam(defaultValue = "true")  boolean isAdministra,
                                 @RequestParam(defaultValue = "true")  boolean isOpera,
                                 HttpServletResponse response) throws IOException {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long idUnidad = (idUnidadAdministrativa == 0) ? null : idUnidadAdministrativa;

        List<CatSistema> lista;
        if (isAdministra && isOpera)
            lista = catSistemaService.getListaSistemasAll(authUser.getUsername(), idEntePublico, idUnidad);
        else if (isAdministra)
            lista = catSistemaService.getListaSistemasAdmin(authUser.getUsername(), idEntePublico, authUser.isAdministradorSigtic());
        else if (isOpera)
            lista = catSistemaService.getListaSistemasOpera(authUser.getUsername(), idEntePublico, authUser.isAdministradorSigtic());
        else
            lista = new ArrayList<>();

        escribirExcelSistemas(lista, response, "sistemas_ente_");
    }

    @GetMapping("/exportarSistemasTotal")
    public void exportarSistemasTotal(HttpServletResponse response) throws IOException {
        List<CatSistema> lista = catSistemaService.getListaSistemas("VIG");
        escribirExcelSistemas(lista, response, "sistemas_total_");
    }

    private void escribirExcelSistemas(List<CatSistema> lista, HttpServletResponse response, String prefijo) throws IOException {

        ClassPathResource resource = new ClassPathResource("static/plantillas/sistemas.xlsx");
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

        int i = 4;
        for (CatSistema s : lista) {

            List<CatSistemaGit> gits = catSistemaGitService.getListaSistemaGit(s.getIdSistema());
            String codigosGit = gits.stream()
                    .map(CatSistemaGit::getCodigoGit)
                    .collect(Collectors.joining(", "));

            Row row = sheet.createRow(i++);
            row.createCell(0) .setCellValue(s.getCatSistemaPadre() != null && s.getCatSistemaPadre().getCodigoSistema() != null  ? s.getCatSistemaPadre().getCodigoSistema()  : "");
            row.createCell(1) .setCellValue(s.getCatSistemaPadre() != null && s.getCatSistemaPadre().getNombreSistema() != null   ? s.getCatSistemaPadre().getNombreSistema()   : "");
            row.createCell(2) .setCellValue(s.getCodigoSistema()                  != null ? s.getCodigoSistema()                  : "");
            row.createCell(3) .setCellValue(s.getNombreSistemaAbreviado()          != null ? s.getNombreSistemaAbreviado()          : "");
            row.createCell(4) .setCellValue(s.getNombreSistema()                  != null ? s.getNombreSistema()                  : "");
            row.createCell(5) .setCellValue(s.getCatTipoSistema()                 != null ? s.getCatTipoSistema().getCveTipoSistema()      : "");
            row.createCell(6) .setCellValue(s.getCatTipoSistema()                 != null ? s.getCatTipoSistema().getNombreTipoSistema()    : "");
            row.createCell(7) .setCellValue(s.getEntePublicoAdmin()               != null ? s.getEntePublicoAdmin().getNombreEntePublico()  : "");
            row.createCell(8) .setCellValue(s.getUnidadAdministrativaAdmin()      != null ? s.getUnidadAdministrativaAdmin().getNombreUnidadAdministrativa() : "");
            row.createCell(9) .setCellValue(s.getEntePublicoOpera()               != null ? s.getEntePublicoOpera().getNombreEntePublico()  : "");
            row.createCell(10).setCellValue(s.getUnidadAdministrativaOpera()      != null ? s.getUnidadAdministrativaOpera().getNombreUnidadAdministrativa() : "");
            row.createCell(11).setCellValue(s.getNumeroInventarioGet()             != null ? s.getNumeroInventarioGet()             : "");
            row.createCell(12).setCellValue(s.getNumeroInventarioEntePublico()     != null ? s.getNumeroInventarioEntePublico()     : "");
            row.createCell(13).setCellValue(s.getCveMejoraRegulatoria()            != null ? s.getCveMejoraRegulatoria()            : "");
            row.createCell(14).setCellValue(s.getCatEstatusSistema()              != null ? s.getCatEstatusSistema().getCveEstatusSistema() : "");
            row.createCell(15).setCellValue(codigosGit);
            row.createCell(16).setCellValue(s.getNumeroOrden()                    != null ? s.getNumeroOrden()                    : 0);
        }

        for (int col = 0; col < 17; col++) { sheet.autoSizeColumn(col); }

        String filename = prefijo + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
