package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sac.pojo.DirectorioUsuarioExcel;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.CatUsuarioEntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.EntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.UnidadAdministrativaRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectorioUsuariosExcelServiceTest {
    private EntePublicoRepository entes;
    private CatUsuarioEntePublicoRepository usuarios;
    private UnidadAdministrativaRepository unidades;
    private DirectorioUsuariosExcelService service;

    @BeforeEach
    void setup() {
        entes = mock(EntePublicoRepository.class);
        usuarios = mock(CatUsuarioEntePublicoRepository.class);
        unidades = mock(UnidadAdministrativaRepository.class);
        service = new DirectorioUsuariosExcelService(entes, usuarios, unidades);
        EntePublico ente = new EntePublico();
        ente.setIdEntePublico(21L);
        ente.setNombreEntePublico("Ente de prueba");
        ente.setRfcTitular("RFC_PRIVADO_NO_EXPORTAR");
        when(entes.findById(21L)).thenReturn(Optional.of(ente));
        UnidadAdministrativa unidad = new UnidadAdministrativa();
        unidad.setIdUnidadAdministrativa(11L);
        unidad.setIdEntePublico(21L);
        unidad.setNombreUnidadAdministrativa("Unidad de prueba");
        when(unidades.findById(11L)).thenReturn(Optional.of(unidad));
    }

    private Workbook descargar() throws Exception {
        return new XSSFWorkbook(new ByteArrayInputStream(service.exportar(21L)));
    }

    @Test
    void writesOnlyInstitutionalColumnsAndPreservesLiteralText() throws Exception {
        when(usuarios.findUsuariosDirectorioPorEnte(21L)).thenReturn(Collections.singletonList(
                new DirectorioUsuarioExcel(9999L, " María ", " José ", "Pérez", "Núñez",
                        "=1+1", "Ente de prueba", "Unidad de prueba", "009931234567", "0007", "oficial@example.invalid")));
        try (Workbook workbook = descargar()) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals("Usuarios", sheet.getSheetName());
            assertEquals(8, sheet.getRow(3).getLastCellNum());
            assertEquals("Nombre completo", sheet.getRow(3).getCell(1).getStringCellValue());
            assertEquals("Correo institucional", sheet.getRow(3).getCell(7).getStringCellValue());
            Row row = sheet.getRow(4);
            assertEquals(CellType.NUMERIC, row.getCell(0).getCellType());
            assertEquals(1, row.getCell(0).getNumericCellValue());
            String[] expected = {"María José Pérez Núñez", "=1+1", "Ente de prueba", "Unidad de prueba",
                    "009931234567", "0007", "oficial@example.invalid"};
            for (int i = 0; i < expected.length; i++) {
                assertEquals(CellType.STRING, row.getCell(i + 1).getCellType());
                assertEquals(expected[i], row.getCell(i + 1).getStringCellValue());
                assertEquals("@", row.getCell(i + 1).getCellStyle().getDataFormatString());
            }
            assertEquals(4, sheet.getPaneInformation().getHorizontalSplitPosition());
            assertTrue(sheet.getRow(3).getCell(1).getCellStyle().getWrapText());
            assertTrue(workbook.getPrintArea(0).contains("$H$"));
            for (Row r : sheet) for (Cell cell : r) {
                assertNotEquals(CellType.FORMULA, cell.getCellType());
                if (cell.getCellType() == CellType.STRING)
                    assertFalse(cell.getStringCellValue().contains("RFC_PRIVADO"));
            }
        }
        verify(usuarios).findUsuariosDirectorioPorEnte(21L);
    }

    @Test
    void exportsEveryRecordBeyondPageAndStreamingWindowLimits() throws Exception {
        List<DirectorioUsuarioExcel> list = new ArrayList<>();
        for (long id = 205; id > 0; id--)
            list.add(new DirectorioUsuarioExcel(id, String.format("Usuario %03d", id), null, null, null,
                    null, "Ente de prueba", null, null, null, null));
        when(usuarios.findUsuariosDirectorioPorEnte(21L)).thenReturn(list);
        try (Workbook workbook = descargar()) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(208, sheet.getLastRowNum());
            assertEquals("Usuario 001", sheet.getRow(4).getCell(1).getStringCellValue());
            assertEquals("Usuario 205", sheet.getRow(208).getCell(1).getStringCellValue());
            assertEquals(205, sheet.getRow(208).getCell(0).getNumericCellValue());
            assertTrue(sheet.getRow(2).getCell(0).getStringCellValue().contains("205"));
            for (int i = 4; i <= 208; i++) assertEquals(8, sheet.getRow(i).getLastCellNum());
        }
    }

    @Test
    void sortsSpanishNamesWithStableTiesAndLeavesMissingDataEmpty() throws Exception {
        when(usuarios.findUsuariosDirectorioPorEnte(21L)).thenReturn(Arrays.asList(
                new DirectorioUsuarioExcel(4L, null, null, null, null, null, null, null, null, null, null),
                new DirectorioUsuarioExcel(3L, "Zoe", null, null, null, null, "Ente de prueba", null, null, null, null),
                new DirectorioUsuarioExcel(2L, "Ána", null, null, null, "Segundo", "Ente de prueba", null, null, null, null),
                new DirectorioUsuarioExcel(1L, "Ana", null, null, null, "Primero", "Ente de prueba", null, null, null, null)));
        try (Workbook workbook = descargar()) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals("Primero", sheet.getRow(4).getCell(2).getStringCellValue());
            assertEquals("Segundo", sheet.getRow(5).getCell(2).getStringCellValue());
            assertEquals("Zoe", sheet.getRow(6).getCell(1).getStringCellValue());
            for (int i = 1; i < 8; i++) assertEquals("", sheet.getRow(7).getCell(i).getStringCellValue());
        }
    }

    @Test
    void acceptsAnEnteWithNoUsersWithoutInventingRecords() throws Exception {
        try (Workbook workbook = descargar()) {
            assertEquals(3, workbook.getSheetAt(0).getLastRowNum());
            assertTrue(workbook.getSheetAt(0).getRow(2).getCell(0).getStringCellValue().contains("Total de registros: 0"));
        }
    }

    @Test
    void rejectsMissingInvalidAndUnknownEnteBeforeQueryingUsers() {
        for (Long id : Arrays.asList(null, 0L, -1L))
            assertEquals(HttpStatus.BAD_REQUEST,
                    assertThrows(ResponseStatusException.class, () -> service.exportar(id)).getStatus());
        assertEquals(HttpStatus.NOT_FOUND,
                assertThrows(ResponseStatusException.class, () -> service.exportar(99L)).getStatus());
        verifyNoInteractions(usuarios);
    }

    @Test
    void unitExportUsesExactEnteAndUnitAndIncludesAllRowsBeyondPagination() throws Exception {
        List<DirectorioUsuarioExcel> list = new ArrayList<>();
        for (long id = 205; id > 0; id--)
            list.add(new DirectorioUsuarioExcel(id, String.format("Usuario %03d", id), null, null, null,
                    "=Cargo", "Ente de prueba", "Unidad de prueba", "009931234567", "0007", "unidad@example.invalid"));
        when(usuarios.findUsuariosDirectorioPorUnidad(21L, 11L)).thenReturn(list);
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(service.exportarUnidad(21L, 11L)))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(208, sheet.getLastRowNum());
            assertTrue(sheet.getRow(0).getCell(0).getStringCellValue().contains("Usuarios de la unidad administrativa"));
            assertEquals("Ente de prueba | Unidad administrativa: Unidad de prueba",
                    sheet.getRow(1).getCell(0).getStringCellValue());
            assertTrue(sheet.getRow(2).getCell(0).getStringCellValue().contains("Total de registros: 205"));
            assertEquals("Usuario 001", sheet.getRow(4).getCell(1).getStringCellValue());
            assertEquals("Usuario 205", sheet.getRow(208).getCell(1).getStringCellValue());
            assertEquals(4, sheet.getPaneInformation().getHorizontalSplitPosition());
            for (int index = 4; index <= 208; index++) {
                Row row = sheet.getRow(index);
                assertEquals(8, row.getLastCellNum());
                assertEquals("Unidad de prueba", row.getCell(4).getStringCellValue());
                assertEquals("009931234567", row.getCell(5).getStringCellValue());
                assertEquals("0007", row.getCell(6).getStringCellValue());
                for (int column = 1; column < 8; column++)
                    assertEquals(CellType.STRING, row.getCell(column).getCellType());
            }
        }
        verify(usuarios).findUsuariosDirectorioPorUnidad(21L, 11L);
        verify(usuarios, never()).findUsuariosDirectorioPorEnte(anyLong());
    }

    @Test
    void unitWithNoUsersProducesAnEmptyWorkbookWithSelectedUnitAndHeaders() throws Exception {
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(service.exportarUnidad(21L, 11L)))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(3, sheet.getLastRowNum());
            assertTrue(sheet.getRow(1).getCell(0).getStringCellValue().contains("Unidad de prueba"));
            assertTrue(sheet.getRow(2).getCell(0).getStringCellValue().contains("Total de registros: 0"));
            assertEquals(8, sheet.getRow(3).getLastCellNum());
        }
    }

    @Test
    void unitExportRejectsInvalidUnknownOrMismatchedSelectionBeforeQueryingUsers() {
        for (Long id : Arrays.asList(null, 0L, -1L)) {
            assertEquals(HttpStatus.BAD_REQUEST,
                    assertThrows(ResponseStatusException.class, () -> service.exportarUnidad(21L, id)).getStatus());
            assertEquals(HttpStatus.BAD_REQUEST,
                    assertThrows(ResponseStatusException.class, () -> service.exportarUnidad(id, 11L)).getStatus());
        }
        assertEquals(HttpStatus.NOT_FOUND,
                assertThrows(ResponseStatusException.class, () -> service.exportarUnidad(99L, 11L)).getStatus());
        assertEquals(HttpStatus.NOT_FOUND,
                assertThrows(ResponseStatusException.class, () -> service.exportarUnidad(21L, 99L)).getStatus());
        UnidadAdministrativa otraUnidad = new UnidadAdministrativa();
        otraUnidad.setIdUnidadAdministrativa(22L);
        otraUnidad.setIdEntePublico(22L);
        when(unidades.findById(22L)).thenReturn(Optional.of(otraUnidad));
        assertEquals(HttpStatus.BAD_REQUEST,
                assertThrows(ResponseStatusException.class, () -> service.exportarUnidad(21L, 22L)).getStatus());
        verifyNoInteractions(usuarios);
    }
}
