package mx.gob.tabasco.cmaig.sigtic.sac.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioSql;
import mx.gob.tabasco.cmaig.sigtic.sac.model.DomicilioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.service.CatUsuarioEntePublicoService;
import mx.gob.tabasco.cmaig.sigtic.sac.service.CatUsuarioService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/catalogoCatUsuarioEntePublico")
public class CatUsuarioEntePublicoRestController {

    @Autowired
    private CatUsuarioEntePublicoService catUsuarioEntePublicoService;

    @Autowired
    private CatUsuarioService catUsuarioService;

    @PutMapping("/actualizarPerfil")
    public CatUsuarioEntePublico actualizarPerfil(@RequestBody CatUsuarioEntePublico catUsuarioEntePublico){
        return catUsuarioEntePublicoService.actualizarPerfil(catUsuarioEntePublico);
    }

    @PostMapping("/tablaUsuario")
    public JSONObject getUsuarios(@RequestBody CatUsuarioEntePublico catUsuarioEntePublico)
    {

        List<CatUsuarioSql> usuarios = catUsuarioEntePublicoService.getUsuariosPorEnte(catUsuarioEntePublico);
        //String total = 0
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        if (usuarios.size() > 0) {
            for (CatUsuarioSql usuarioR : usuarios)
            {   JSONArray json = new JSONArray();
                json.add(usuarioR.getCurp());
                json.add(usuarioR.getRfc());
                json.add(usuarioR.getNombreUsuario());
                json.add(usuarioR.getDescUnidadAdmtva());
                json.add(usuarioR.getDescEstatus());
                json.add("<div class=\"btn-group\" role=\"group\" aria-label=\"Basic example\">" +
                        "<a href='#' class='btn btn-outline-success mr-1' data-toggle='tooltip' data-placement='top' title='Editar Usuario' onclick='editarUsuario("+ usuarioR.getIdUsuarioEntePublico() +")'><i class='fas fa-edit'></i></a>"
                        +"<a href='javascript:void(0);' class='btn btn-outline-danger' data-toggle='tooltip' data-placement='top' title='Eliminar Usuario' onclick='eliminarUsuario("+ usuarioR.getIdUsuarioEntePublico() +")'><i class='fas fa-trash'></i></a>" +
                        "</div>");
                array.add(json);
            }
        }
        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @GetMapping("/getUsuarioEntePublico/{idUsuario}/{idEntePublico}")
    public CatUsuarioEntePublico getUsuarioEntePublico(@PathVariable("idUsuario") Long idUsuario, @PathVariable("idEntePublico") Long idEntePublico){
        return catUsuarioEntePublicoService.getUsuarioEntePublico(idUsuario, idEntePublico);
    }

    @PostMapping("/guardarUsuarioEntePublico")
    public CatUsuarioEntePublico guardarUsuarioEntePublico(@RequestBody CatUsuarioEntePublico catUsuarioEntePublico){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.from(dateTimeFormatter.parse(catUsuarioEntePublico.getCatUsuario().getFechaNacimientoText()+" 00:00:00"));
        //localDateTime= localDateTime.plusDays(1);
        catUsuarioEntePublico.getCatUsuario().setFechaNacimiento(Timestamp.valueOf(localDateTime));

        return  catUsuarioEntePublicoService.guardarUsuarioEntePublico(catUsuarioEntePublico);
    }

    @GetMapping("/getUsuarioEntePublicoId/{idUsuarioEntePublico}")
    public CatUsuarioEntePublico getUsuarioEntePublicoId(@PathVariable("idUsuarioEntePublico") Long idUsuarioEntePublico){
        return catUsuarioEntePublicoService.getUsuario(idUsuarioEntePublico);
    }

    @PostMapping("/exportarUsuariosFiltro")
    public void exportarUsuariosFiltro(
            @RequestBody CatUsuarioEntePublico filtro,
            HttpServletResponse response) throws IOException {

        List<CatUsuarioEntePublico> lista = catUsuarioEntePublicoService.getUsuariosParaExportar(filtro);
        exportarExcelCompleto(lista, response, "usuarios_filtro_");
    }

    @GetMapping("/exportarUsuariosTodos")
    public void exportarUsuariosTodos(HttpServletResponse response) throws IOException {

        List<CatUsuarioEntePublico> listaCompleta =
                catUsuarioEntePublicoService.getListaCompletaUsuarios();
        exportarExcelCompleto(listaCompleta, response, "usuarios_todos_");
    }

    private void exportarExcelCompleto(List<CatUsuarioEntePublico> lista,
                                       HttpServletResponse response,
                                       String prefijo) throws IOException {

        ClassPathResource resource = new ClassPathResource("static/plantillas/cat_usuario_ente_publico.xlsx");
        XSSFWorkbook plantilla = new XSSFWorkbook(resource.getInputStream());

        XSSFSheet xssfSheet = plantilla.getSheetAt(0);
        XSSFRow filaFecha = xssfSheet.getRow(2);
        if (filaFecha == null) filaFecha = xssfSheet.createRow(2);
        XSSFCell fechaCell = filaFecha.getCell(24);
        if (fechaCell == null) fechaCell = filaFecha.createCell(24);
        fechaCell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        SXSSFWorkbook workbook = new SXSSFWorkbook(plantilla, 500);
        workbook.setCompressTempFiles(true);
        SXSSFSheet sheet = (SXSSFSheet) workbook.getSheetAt(0);

        int i = 4;
        for (CatUsuarioEntePublico u : lista) {
            Row row = sheet.createRow(i++);
            CatUsuario cu = u.getCatUsuario();
            row.createCell(0).setCellValue(u.getEntePublico() != null ? u.getEntePublico().getCveEntePublico() : "");
            row.createCell(1).setCellValue(u.getEntePublico() != null ? u.getEntePublico().getNombreEntePublico() : "");
            row.createCell(2).setCellValue(cu.getCurp()                    != null ? cu.getCurp()                    : "");
            row.createCell(3).setCellValue(cu.getRfc()                     != null ? cu.getRfc()                     : "");
            row.createCell(4).setCellValue(cu.getNombre()                  != null ? cu.getNombre()                  : "");
            row.createCell(5).setCellValue(cu.getSegundoNombre()           != null ? cu.getSegundoNombre()           : "");
            row.createCell(6).setCellValue(cu.getApellidoPaterno()         != null ? cu.getApellidoPaterno()         : "");
            row.createCell(7).setCellValue(cu.getApellidoMaterno()         != null ? cu.getApellidoMaterno()         : "");
            row.createCell(8).setCellValue(cu.getCveSexo()                 != null ? cu.getCveSexo()                 : "");
            row.createCell(9).setCellValue(cu.getFechaNacimientoText()     != null ? cu.getFechaNacimientoText()     : "");
            row.createCell(10).setCellValue(cu.getNumCelular()             != null ? cu.getNumCelular()              : "");
            row.createCell(11).setCellValue(cu.getNumTelefonoPersonal()    != null ? cu.getNumTelefonoPersonal()     : "");
            row.createCell(12).setCellValue(cu.getCorreoPersonal()         != null ? cu.getCorreoPersonal()          : "");
            row.createCell(13).setCellValue(u.getCorreoInstitucional()     != null ? u.getCorreoInstitucional()      : "");
            row.createCell(14).setCellValue(u.getNumTelefonoInstitucional()!= null ? u.getNumTelefonoInstitucional() : "");
            row.createCell(15).setCellValue(u.getNumeroExtensionInstitucional() != null ? u.getNumeroExtensionInstitucional() : "");
            row.createCell(16).setCellValue(u.getPuesto()                  != null ? u.getPuesto()                  : "");
            row.createCell(17).setCellValue(u.getCategoria() != null && u.getCategoria().getNombreCategoriaUsuario() != null
                    ? u.getCategoria().getNombreCategoriaUsuario() : "");
            row.createCell(18).setCellValue(
                    u.getUbicacionUnidadAdmin() != null
                            ? formatDomicilio(u.getUbicacionUnidadAdmin().getDomicilioEntePublico())
                            : "");
            row.createCell(19).setCellValue(u.getUbicacionUnidadAdmin() != null && u.getUbicacionUnidadAdmin().getNombreUbicacion() != null
                    ? u.getUbicacionUnidadAdmin().getNombreUbicacion() : "");
            row.createCell(20).setCellValue(u.getUnidadAdministrativa() != null && u.getUnidadAdministrativa().getCveUnidadAdministrativa() != null
                    ? u.getUnidadAdministrativa().getCveUnidadAdministrativa() : "");
            row.createCell(21).setCellValue(u.getUnidadAdministrativa() != null && u.getUnidadAdministrativa().getNombreUnidadAdministrativa() != null
                    ? u.getUnidadAdministrativa().getNombreUnidadAdministrativa() : "");
            row.createCell(22).setCellValue(cu.getCveEstatus()             != null ? cu.getCveEstatus()             : "");
            row.createCell(23).setCellValue(u.getCveEstatus()              != null ? u.getCveEstatus()              : "");
        }

        escribirRespuesta(workbook, response, prefijo);
    }
    private String formatDomicilio(DomicilioEntePublico d) {
        if (d == null) return "";

        // Si ya existe una versión abreviada armada, úsala
        if (d.getNombreDomicilioAbrev() != null && !d.getNombreDomicilioAbrev().isEmpty()) {
            return d.getNombreDomicilioAbrev();
        }

        StringBuilder sb = new StringBuilder();
        if (d.getCalle() != null) sb.append(d.getCalle());
        if (d.getNumExterior() != null) sb.append(" No. ").append(d.getNumExterior());
        if (d.getNumInterior() != null) sb.append(" Int. ").append(d.getNumInterior());
        if (d.getColonia() != null) sb.append(", Col. ").append(d.getColonia());
        if (d.getReferenciaDomicilio() != null) sb.append(" (").append(d.getReferenciaDomicilio()).append(")");

        return sb.toString();
    }

    private void escribirRespuesta(SXSSFWorkbook workbook,
                                   HttpServletResponse response,
                                   String prefijo) throws IOException {
        String filename = prefijo + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        workbook.write(response.getOutputStream());
        workbook.dispose();
        workbook.close();
    }
}
