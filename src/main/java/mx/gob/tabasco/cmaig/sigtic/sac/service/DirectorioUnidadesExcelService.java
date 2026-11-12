package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.*;
import mx.gob.tabasco.cmaig.sigtic.sac.pojo.DirectorioResponsableUnidad;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.CatUsuarioEntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.EntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.SacCatUnidadAdministrativaViewRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.UbicacionUnidadAdminRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(value = "catalogosTransactionManager", readOnly = true)
public class DirectorioUnidadesExcelService {
    private final EntePublicoRepository entes;
    private final SacCatUnidadAdministrativaViewRepository unidades;
    private final UbicacionUnidadAdminRepository ubicaciones;
    private final CatUsuarioEntePublicoRepository usuarios;

    public DirectorioUnidadesExcelService(
            @Qualifier("entePublicoRepositorySac") EntePublicoRepository entes,
            SacCatUnidadAdministrativaViewRepository unidades, UbicacionUnidadAdminRepository ubicaciones,
            CatUsuarioEntePublicoRepository usuarios) {
        this.entes = entes;
        this.unidades = unidades;
        this.ubicaciones = ubicaciones;
        this.usuarios = usuarios;
    }

    public byte[] exportar(Long idEntePublico) throws IOException {
        if (idEntePublico == null || idEntePublico <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seleccione un ente público válido.");
        EntePublico ente = entes.findById(idEntePublico)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ente público no encontrado."));
        List<SacCatUnidadAdministrativaView> lista = new ArrayList<>(
                unidades.findAllByIdEntePublicoAndEstatus(idEntePublico, "VIG"));
        lista.sort(Comparator.comparing(SacCatUnidadAdministrativaView::getOrdenOrganigrama,
                Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(SacCatUnidadAdministrativaView::getIdUnidadAdministrativa));
        List<Long> ids = lista.stream().map(SacCatUnidadAdministrativaView::getIdUbicacionUnidadAdministrativa)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, UbicacionUnidadAdmin> locations = new HashMap<>();
        // Oracle limits an IN expression to 1000 values.
        for (int i = 0; i < ids.size(); i += 900)
            ubicaciones.findAllById(ids.subList(i, Math.min(i + 900, ids.size())))
                    .forEach(location -> locations.put(location.getIdUbicacionAdaministrativa(), location));
        Map<Long, DirectorioResponsableUnidad> responsables = new HashMap<>();
        if (!lista.isEmpty()) {
            // The query orders matching assignments newest first; never mix contact fields between rows.
            usuarios.findResponsablesDirectorioPorEnte(idEntePublico)
                    .forEach(responsable -> responsables.putIfAbsent(responsable.getIdUnidadAdministrativa(), responsable));
        }

        ClassPathResource template = new ClassPathResource("static/plantillas/unidades_administrativas.xlsx");
        try (InputStream input = template.getInputStream(); Workbook workbook = new XSSFWorkbook(input);
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.getSheetAt(0);
            workbook.setSheetName(0, "Unidades administrativas");
            String[] headers = {"Responsable de la unidad", "Cargo responsable", "Correo institucional responsable",
                    "Teléfono institucional responsable", "Extensión responsable"};
            Row header = sheet.getRow(3);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.cloneStyleFrom(header.getCell(16).getCellStyle());
            headerStyle.setWrapText(true);
            for (int col = 0; col < headers.length; col++) header.createCell(17 + col).setCellValue(headers[col]);
            for (Cell cell : header) cell.setCellStyle(headerStyle);
            header.setHeightInPoints(32);
            for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
                CellRangeAddress region = sheet.getMergedRegion(i);
                if (region.getFirstRow() == 0 && region.getLastRow() == 0 && region.getFirstColumn() == 0) {
                    sheet.removeMergedRegion(i);
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 21));
                    break;
                }
            }
            sheet.getRow(2).createCell(0).setCellValue("Fecha de descarga:");
            sheet.getRow(2).createCell(21).setCellValue(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));
            textStyle.setWrapText(true);
            textStyle.setVerticalAlignment(VerticalAlignment.TOP);
            int rowNumber = 4;
            for (SacCatUnidadAdministrativaView u : lista) {
                UbicacionUnidadAdmin location = locations.get(u.getIdUbicacionUnidadAdministrativa());
                DomicilioEntePublico domicilio = location == null ? null : location.getDomicilioEntePublico();
                DirectorioResponsableUnidad responsable = responsables.get(u.getIdUnidadAdministrativa());
                Object[] values = {
                        u.getIdUnidadAdministrativa(), ente.getIdEntePublico(), ente.getCveEntePublico(),
                        ente.getNombreEntePublico(), u.getCveUnidadAdministrativa(), u.getCveUnidadAdministrativaPadre(),
                        u.getNombreUnidadAdministrativaAbreviada(),
                        u.getDescripcionUnidadAdministrativaXn() == null
                                ? u.getNombreUnidadAdministrativa() : u.getDescripcionUnidadAdministrativaXn(),
                        u.getNivelUnidadAdministrativa() == null ? null
                                : u.getNivelUnidadAdministrativa().getNombreNivelUnidadAdministrativa(),
                        u.getUnidadPresupuestal(), domicilio == null ? null : domicilio.getNombreDomicilio(),
                        location == null ? null : location.getNombreUbicacion(),
                        location == null ? null : location.getUbicacionTitularEntePublico(),
                        location == null ? null : location.getCorreoUnidad(),
                        u.getTelefono(), u.getNumeroExtension(), u.getNumeroOrden(),
                        responsable == null ? "Sin responsable vigente registrado" : responsable.getNombreCompleto(),
                        responsable == null ? null : responsable.getPuesto(),
                        responsable == null ? null : responsable.getCorreo(),
                        responsable == null ? null : responsable.getTelefono(),
                        responsable == null ? null : responsable.getExtension()
                };
                Row row = sheet.createRow(rowNumber++);
                row.setHeightInPoints(30);
                for (int col = 0; col < values.length; col++) {
                    Cell cell = row.createCell(col, CellType.STRING);
                    cell.setCellStyle(textStyle);
                    cell.setCellValue(values[col] == null ? "" : String.valueOf(values[col]));
                }
            }
            int[] widths = {18, 18, 20, 45, 23, 26, 22, 70, 24, 14, 50, 35, 35, 38, 20, 16, 16,
                    42, 34, 42, 28, 22};
            for (int col = 0; col < widths.length; col++) sheet.setColumnWidth(col, widths[col] * 256);
            sheet.createFreezePane(0, 4);
            sheet.setAutoFilter(new CellRangeAddress(3, Math.max(3, rowNumber - 1), 0, widths.length - 1));
            workbook.setPrintArea(0, 0, widths.length - 1, 0, Math.max(3, rowNumber - 1));
            workbook.write(output);
            return output.toByteArray();
        }
    }
}
