package mx.gob.tabasco.cmaig.sigtic.sac.restController;


import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.service.CatUsuarioEntePublicoService;
import mx.gob.tabasco.cmaig.sigtic.sac.service.CatUsuarioService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/catalogoCatUsuario")
public class CatUsuarioRestController {

    @Autowired
    private CatUsuarioService catUsuarioService;

    @Autowired
    private CatUsuarioEntePublicoService catUsuarioEntePublicoService;

    @PostMapping("/tablaUsuario")
    public JSONObject getUsuarios(@RequestBody CatUsuario catUsuario) {

        //List<CatUsuario> usuarios = catUsuarioService.getUsuarios(catUsuario);
        List<CatUsuario> usuarios = catUsuarioService.getUsuariosFiltro(catUsuario);
        //String total = 0
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        if (usuarios.size() > 0) {
            for (CatUsuario usuarioR : usuarios) {
                JSONArray json = new JSONArray();
                json.add(usuarioR.getCurp());
                json.add(usuarioR.getRfc());
                json.add(usuarioR.getNombreCompleto());
                if (usuarioR.getTotalEntePublico() > 0)
                    json.add("<a href='#' data-toggle='tooltip' data-placement='top' title='Lista de entes' onclick='obtieneEntesPublicosPorUsuarios(\"" + usuarioR.getIdUsuario() + "\")'>" + usuarioR.getTotalEntePublico() + "</a>");
                else
                    json.add(usuarioR.getTotalEntePublico());

                json.add(usuarioR.getCveEstatus());
                json.add("<a href='#' class='btn btn-outline-success mr-1' data-toggle='tooltip' data-placement='top' title='Editar Usuario' onclick='editarUsuario(" + usuarioR.getIdUsuario() + ")'><i class='fas fa-edit'></i></a>"
                        + "<a href='javascript:void(0);' class='btn btn-outline-secondary' data-toggle='tooltip' data-placement='top' title='Eliminar Usuario' onclick='eliminarUsuario(" + usuarioR.getIdUsuario() + ")'><i class='fas fa-trash'></i></a>");

                array.add(json);
            }
        }
        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @GetMapping("/getUsuario/{idUsuario}")
    public CatUsuario getUsuario(@PathVariable("idUsuario") Long idUsuario) {
        return catUsuarioService.getUsuario(idUsuario);
    }

    @PostMapping("/guardarCatUsuario")
    public CatUsuario guardarCatUsuario(@RequestBody CatUsuario catUsuario) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.from(dateTimeFormatter.parse(catUsuario.getFechaNacimientoText() + " 00:00:00"));
        //localDateTime= localDateTime.plusDays(1);
        catUsuario.setFechaNacimiento(Timestamp.valueOf(localDateTime));

        return catUsuarioService.saveCatUsuario(catUsuario);
    }

    @DeleteMapping("/deleteUsuario")
    public CatUsuario deleteUsuario(@RequestBody CatUsuario catUsuario) {
        return catUsuarioService.deleteUsuario(catUsuario);
    }

    @PostMapping("/tablaUsuarioSearch")
    public JSONObject getUsuariosSearch(@RequestBody CatUsuario catUsuario) {

        List<CatUsuario> usuarios = catUsuarioService.getUsuariosFiltro(catUsuario);
        //String total = 0
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        if (usuarios.size() > 0) {
            for (CatUsuario usuario : usuarios) {
                JSONArray json = new JSONArray();
                json.add(usuario.getRfc());
                json.add(usuario.getCurp());
                json.add(usuario.getNombreCompleto());
                json.add(usuario.getCveEstatus());
                json.add("<a href='#' class='btn btn-outline-success mr-1' data-toggle='tooltip' data-placement='top' title='Seleccionar Usuario' onclick='seleccionarUsuario(\"" + usuario.getIdUsuario() + "\")'><i class='bi bi-plus-circle'></i></a>");
                array.add(json);
            }
        }
        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }


   /* @PostMapping("/tablaUsuarioEntePublicoSearch")
    public JSONObject getUsuarioEntePublicoSearch(@RequestBody CatUsuario catUsuario)
    {

        List<CatUsuarioEntePublico> usuarios = catUsuarioService.getUsuariosFiltro(catUsuario);
        //String total = 0
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
            for (CatUsuarioEntePublico usuario : usuarios)
            {   JSONArray json = new JSONArray();
                json.add(usuario.getEntePublico().getCveEntePublico()+") "+usuario.getEntePublico().getNombreEntePublico());
                json.add(usuario.getCatUsuario().getCurp());
                json.add(usuario.getCatUsuario().getNombreCompleto());
                json.add(usuario.getCveEstatus());
                json.add("<a href='#' class='btn btn-outline-success mr-1' data-toggle='tooltip' data-placement='top' title='Seleccionar Usuario' onclick='seleccionarUsuario(\""+ usuario.getIdUsuarioEntePublico() +"\")'><i class='bi bi-plus-circle'></i></a>");
                array.add(json);
            }
        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }*/

    @PostMapping("/tablaUsuarioPorDependenciaSearch")
    public JSONObject getUsuariosPorDependenciaSearch(@RequestBody CatUsuarioEntePublico catUsuarioEntePublico)
    {

        List<CatUsuarioEntePublico> usuarios = catUsuarioService.getUsuariosFiltroPorDependencia(catUsuarioEntePublico);
        //String total = 0
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (CatUsuarioEntePublico usuario : usuarios) {
            JSONArray json = new JSONArray();
            json.add(usuario.getCatUsuario().getRfc());
            json.add(usuario.getCatUsuario().getCurp());
            json.add(usuario.getCatUsuario().getNombreCompleto());
            json.add(usuario.getCatUsuario().getCveEstatus());
            json.add("<a href='#' class='btn btn-outline-success mr-1' data-toggle='tooltip' data-placement='top' title='Seleccionar Usuario' onclick='seleccionarUsuario(" + usuario.getIdUsuarioEntePublico() + ",\"" + usuario.getCatUsuario().getRfc() + "\",\"" + usuario.getCatUsuario().getCurp() + "\",\"" + usuario.getCatUsuario().getNombreCompleto() + "\")'><i class='bi bi-plus-circle'></i></a>");
            array.add(json);
        }
        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/tablaUsuarioEntePublico")
    public JSONObject getUsuariosEntePublico(@RequestBody CatUsuario catUsuario) {

        List<CatUsuarioEntePublico> usuariosEnte = catUsuarioEntePublicoService.getEntesPublicoPorUsuario(catUsuario.getIdUsuario());
        //String total = 0
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        if (usuariosEnte.size() > 0) {
            for (CatUsuarioEntePublico usuarioEnte : usuariosEnte) {
                JSONArray json = new JSONArray();
                json.add(usuarioEnte.getEntePublico().getCveEntePublico());
                json.add(usuarioEnte.getEntePublico().getNombreEntePublico());
                json.add(usuarioEnte.getCveEstatus());
                array.add(json);
            }
        }
        jsonObject.put("iTotalDisplayRecords", array.size());
        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/exportarUsuarios")
    public void exportarUsuarios(@RequestBody CatUsuario filtro,
                                 HttpServletResponse response) throws IOException {

        List<CatUsuario> lista = catUsuarioService.getUsuariosFiltro(filtro);

        ClassPathResource resource = new ClassPathResource("static/plantillas/cat_usuarios.xlsx");
        XSSFWorkbook plantilla = new XSSFWorkbook(resource.getInputStream());


        XSSFSheet xssfSheet = plantilla.getSheetAt(0);

        XSSFRow filaFecha = xssfSheet.getRow(2);
        if (filaFecha == null) filaFecha = xssfSheet.createRow(2);
        XSSFCell fechaCell = filaFecha.getCell(11);
        if (fechaCell == null) fechaCell = filaFecha.createCell(11);
        fechaCell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));


        SXSSFWorkbook workbook = new SXSSFWorkbook(plantilla, 500);
        workbook.setCompressTempFiles(true);
        SXSSFSheet sheet = (SXSSFSheet) workbook.getSheetAt(0);

//       Datos desde fila 5 (índice 4)
        int i = 4;
        for (CatUsuario u : lista) {
            Row row = sheet.createRow(i++);
            row.createCell(0).setCellValue(u.getCurp() != null ? u.getCurp() : "");
            row.createCell(1).setCellValue(u.getRfc() != null ? u.getRfc() : "");
            row.createCell(2).setCellValue(u.getNombre() != null ? u.getNombre() : "");
            row.createCell(3).setCellValue(u.getSegundoNombre() != null ? u.getSegundoNombre() : "");
            row.createCell(4).setCellValue(u.getApellidoPaterno() != null ? u.getApellidoPaterno() : "");
            row.createCell(5).setCellValue(u.getApellidoMaterno() != null ? u.getApellidoMaterno() : "");
            row.createCell(6).setCellValue(u.getCveSexo() != null ? u.getCveSexo() : "");
            row.createCell(7).setCellValue(u.getFechaNacimientoText() != null ? u.getFechaNacimientoText() : "");
            row.createCell(8).setCellValue(u.getNumCelular() != null ? u.getNumCelular() : "");
            row.createCell(9).setCellValue(u.getNumTelefonoPersonal() != null ? u.getNumTelefonoPersonal() : "");
            row.createCell(10).setCellValue(u.getCorreoPersonal() != null ? u.getCorreoPersonal() : "");
            row.createCell(11).setCellValue(u.getCveEstatus() != null ? u.getCveEstatus() : "");
        }

        String filename = "cat_usuarios_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        workbook.write(response.getOutputStream());
        workbook.dispose();
        workbook.close();
    }
}
