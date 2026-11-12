package mx.gob.tabasco.cmaig.sigtic.sac.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.pojo.DirectorioContacto;
import mx.gob.tabasco.cmaig.sigtic.sac.pojo.DirectorioFiltro;
import mx.gob.tabasco.cmaig.sigtic.sac.service.DirectorioPublicoService;
import mx.gob.tabasco.cmaig.sigtic.sac.service.DirectorioUnidadesExcelService;
import mx.gob.tabasco.cmaig.sigtic.sac.service.DirectorioUsuariosExcelService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Collections;

@RestController
@RequestMapping("/api/directorio")
public class DirectorioPublicoRestController {
    private final DirectorioPublicoService service;
    private final DirectorioUnidadesExcelService excel;
    private final DirectorioUsuariosExcelService usuariosExcel;

    public DirectorioPublicoRestController(DirectorioPublicoService service, DirectorioUnidadesExcelService excel,
            DirectorioUsuariosExcelService usuariosExcel) {
        this.service = service;
        this.excel = excel;
        this.usuariosExcel = usuariosExcel;
    }

    @GetMapping("/contactos")
    public Page<DirectorioContacto> contactos(@Valid @ModelAttribute DirectorioFiltro filtro) {
        return service.buscar(filtro);
    }

    @GetMapping("/opciones")
    public Map<String, Object> opciones() {
        return service.opciones();
    }

    @GetMapping("/unidades.xlsx")
    public ResponseEntity<byte[]> unidades(@RequestParam Long idEntePublico) throws IOException {
        return descargar(excel.exportar(idEntePublico), "unidades_administrativas", idEntePublico);
    }

    @GetMapping("/usuarios.xlsx")
    public ResponseEntity<byte[]> usuarios(@RequestParam Long idEntePublico) throws IOException {
        return descargar(usuariosExcel.exportar(idEntePublico), "usuarios", idEntePublico);
    }

    @GetMapping("/usuarios-unidad.xlsx")
    public ResponseEntity<byte[]> usuariosUnidad(@RequestParam Long idEntePublico,
            @RequestParam Long idUnidadAdministrativa) throws IOException {
        return descargar(usuariosExcel.exportarUnidad(idEntePublico, idUnidadAdministrativa),
                "usuarios_unidad_" + idUnidadAdministrativa, idEntePublico);
    }

    private ResponseEntity<byte[]> descargar(byte[] archivo, String prefijo, Long idEntePublico) {
        String filename = prefijo + "_" + idEntePublico + "_"
                + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".xlsx";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(archivo);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> errorDeConsulta(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(Collections.singletonMap("message", exception.getReason()));
    }
}
