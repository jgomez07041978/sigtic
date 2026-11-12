package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sac.pojo.DirectorioUsuarioExcel;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.CatUsuarioEntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.EntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.UnidadAdministrativaRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Collator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional(value = "catalogosTransactionManager", readOnly = true)
public class DirectorioUsuariosExcelService {
    private final EntePublicoRepository entes;
    private final CatUsuarioEntePublicoRepository usuarios;
    private final UnidadAdministrativaRepository unidades;

    public DirectorioUsuariosExcelService(@Qualifier("entePublicoRepositorySac") EntePublicoRepository entes,
            CatUsuarioEntePublicoRepository usuarios,
            @Qualifier("unidadAdministrativaRepositorySac") UnidadAdministrativaRepository unidades) {
        this.entes = entes;
        this.usuarios = usuarios;
        this.unidades = unidades;
    }

    public byte[] exportar(Long idEntePublico) throws IOException {
        EntePublico ente = buscarEnte(idEntePublico);
        return generar(ente, null, usuarios.findUsuariosDirectorioPorEnte(idEntePublico));
    }

    public byte[] exportarUnidad(Long idEntePublico, Long idUnidadAdministrativa) throws IOException {
        if (idUnidadAdministrativa == null || idUnidadAdministrativa <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seleccione una unidad administrativa válida.");
        EntePublico ente = buscarEnte(idEntePublico);
        UnidadAdministrativa unidad = unidades.findById(idUnidadAdministrativa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unidad administrativa no encontrada."));
        if (!idEntePublico.equals(unidad.getIdEntePublico()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La unidad administrativa no pertenece al ente público seleccionado.");
        return generar(ente, unidad, usuarios.findUsuariosDirectorioPorUnidad(idEntePublico, idUnidadAdministrativa));
    }

    private EntePublico buscarEnte(Long idEntePublico) {
        if (idEntePublico == null || idEntePublico <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seleccione un ente público válido.");
        return entes.findById(idEntePublico)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ente público no encontrado."));
    }

    private byte[] generar(EntePublico ente, UnidadAdministrativa unidad, List<DirectorioUsuarioExcel> usuarios) throws IOException {
        List<DirectorioUsuarioExcel> contactos = new ArrayList<>(usuarios);
        Collator collator = Collator.getInstance(new Locale("es", "MX"));
        collator.setStrength(Collator.PRIMARY);
        contactos.sort(Comparator.comparing((DirectorioUsuarioExcel contacto) -> contacto.getNombreCompleto().isEmpty())
                .thenComparing(DirectorioUsuarioExcel::getNombreCompleto, collator)
                .thenComparing(DirectorioUsuarioExcel::getId, Comparator.nullsLast(Comparator.naturalOrder())));

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            SXSSFWorkbook workbook = new SXSSFWorkbook(200);
            workbook.setCompressTempFiles(true);
            try {
                Sheet sheet = workbook.createSheet("Usuarios");
                Font headingFont = workbook.createFont();
                headingFont.setBold(true);
                headingFont.setColor(IndexedColors.WHITE.getIndex());
                XSSFCellStyle heading = (XSSFCellStyle) workbook.createCellStyle();
                heading.setFillForegroundColor(new XSSFColor(new byte[]{0x6a, 0x1b, 0x38}, null));
                heading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                heading.setFont(headingFont);
                heading.setWrapText(true);
                heading.setVerticalAlignment(VerticalAlignment.CENTER);
                CellStyle text = workbook.createCellStyle();
                text.setDataFormat(workbook.createDataFormat().getFormat("@"));
                text.setWrapText(true);
                text.setVerticalAlignment(VerticalAlignment.TOP);
                banner(sheet, 0, unidad == null ? "Directorio institucional · Usuarios del ente público"
                        : "Directorio institucional · Usuarios de la unidad administrativa", heading);
                banner(sheet, 1, ente.getNombreEntePublico() + (unidad == null ? ""
                        : " | Unidad administrativa: " + unidad.getNombreUnidadAdministrativa()), text);
                banner(sheet, 2, "Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                        + " | Total de registros: " + contactos.size()
                        + " | Incluye registros vigentes e históricos. Una fila por adscripción.", text);
                String[] headers = {"No.", "Nombre completo", "Puesto", "Ente público", "Unidad administrativa",
                        "Teléfono institucional", "Extensión", "Correo institucional"};
                Row header = sheet.createRow(3);
                header.setHeightInPoints(32);
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = header.createCell(i, CellType.STRING);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(heading);
                }
                int rowNumber = 4;
                for (DirectorioUsuarioExcel contacto : contactos) {
                    Row row = sheet.createRow(rowNumber++);
                    row.setHeightInPoints(32);
                    row.createCell(0, CellType.NUMERIC).setCellValue(rowNumber - 4);
                    String[] values = {contacto.getNombreCompleto(), contacto.getPuesto(), contacto.getEntePublico(),
                            contacto.getUnidadAdministrativa(), contacto.getTelefono(), contacto.getExtension(), contacto.getCorreo()};
                    for (int i = 0; i < values.length; i++) {
                        Cell cell = row.createCell(i + 1, CellType.STRING);
                        cell.setCellStyle(text);
                        cell.setCellValue(values[i] == null ? "" : values[i]);
                    }
                }
                int[] widths = {9, 42, 40, 48, 52, 24, 16, 42};
                for (int i = 0; i < widths.length; i++) sheet.setColumnWidth(i, widths[i] * 256);
                sheet.createFreezePane(0, 4);
                sheet.setAutoFilter(new CellRangeAddress(3, Math.max(3, rowNumber - 1), 0, 7));
                sheet.getPrintSetup().setLandscape(true);
                sheet.getPrintSetup().setFitWidth((short) 1);
                sheet.getPrintSetup().setFitHeight((short) 0);
                sheet.setFitToPage(true);
                workbook.setPrintArea(0, 0, 7, 0, Math.max(3, rowNumber - 1));
                workbook.write(output);
            } finally {
                try {
                    workbook.close();
                } finally {
                    workbook.dispose();
                }
            }
            return output.toByteArray();
        }
    }

    private static void banner(Sheet sheet, int index, String value, CellStyle style) {
        Row row = sheet.createRow(index);
        row.setHeightInPoints(32);
        for (int i = 0; i < 8; i++) row.createCell(i, CellType.STRING).setCellStyle(style);
        row.getCell(0).setCellValue(value == null ? "" : value);
        sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 7));
    }
}
