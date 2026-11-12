package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.*;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.*;
import mx.gob.tabasco.cmaig.sigtic.sac.pojo.DirectorioResponsableUnidad;
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

class DirectorioUnidadesExcelServiceTest {
    private EntePublicoRepository entes;
    private SacCatUnidadAdministrativaViewRepository unidades;
    private UbicacionUnidadAdminRepository ubicaciones;
    private CatUsuarioEntePublicoRepository usuarios;
    private DirectorioUnidadesExcelService service;

    @BeforeEach
    void setup() {
        entes = mock(EntePublicoRepository.class);
        unidades = mock(SacCatUnidadAdministrativaViewRepository.class);
        ubicaciones = mock(UbicacionUnidadAdminRepository.class);
        usuarios = mock(CatUsuarioEntePublicoRepository.class);
        service = new DirectorioUnidadesExcelService(entes, unidades, ubicaciones, usuarios);
        EntePublico ente = new EntePublico();
        ente.setIdEntePublico(21L);
        ente.setCveEntePublico("0021");
        ente.setNombreEntePublico("Ente de prueba");
        ente.setRfcTitular("DATO_PRIVADO_NO_EXPORTAR");
        when(entes.findById(21L)).thenReturn(Optional.of(ente));
        when(unidades.findAllByIdEntePublicoAndEstatus(21L, "VIG")).thenReturn(Collections.emptyList());
    }

    @Test
    void writesOriginalColumnsPlusInstitutionalResponsibleAndPreservesText() throws Exception {
        SacCatUnidadAdministrativaView unidad = new SacCatUnidadAdministrativaView();
        unidad.setIdUnidadAdministrativa(21170101L);
        unidad.setIdEntePublico(21L);
        unidad.setCveUnidadAdministrativa("001");
        unidad.setCveUnidadAdministrativaPadre("000");
        unidad.setNombreUnidadAdministrativaAbreviada("OG");
        unidad.setDescripcionUnidadAdministrativaXn("=1+1");
        unidad.setUnidadPresupuestal("S");
        unidad.setTelefono("009933103300");
        unidad.setNumeroExtension("0007");
        unidad.setNumeroOrden("0");
        unidad.setIdUbicacionUnidadAdministrativa(7L);
        NivelUnidadAdministrativa nivel = new NivelUnidadAdministrativa();
        nivel.setNombreNivelUnidadAdministrativa("Dirección");
        unidad.setNivelUnidadAdministrativa(nivel);
        when(unidades.findAllByIdEntePublicoAndEstatus(21L, "VIG"))
                .thenReturn(Collections.singletonList(unidad));
        DomicilioEntePublico domicilio = new DomicilioEntePublico();
        domicilio.setNombreDomicilio("Domicilio institucional");
        UbicacionUnidadAdmin location = new UbicacionUnidadAdmin();
        location.setIdUbicacionAdaministrativa(7L);
        location.setDomicilioEntePublico(domicilio);
        location.setNombreUbicacion("Edificio A");
        location.setUbicacionTitularEntePublico("Oficina institucional");
        location.setCorreoUnidad("unidad@example.invalid");
        when(ubicaciones.findAllById(any())).thenReturn(Collections.singletonList(location));
        when(usuarios.findResponsablesDirectorioPorEnte(21L)).thenReturn(Collections.singletonList(
                new DirectorioResponsableUnidad(21170101L, " María ", "José", "Pérez", "Núñez",
                        "=Cargo institucional", "responsable@example.invalid", "009931234567", "0008")));

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(service.exportar(21L)))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(22, sheet.getRow(3).getLastCellNum());
            assertEquals("ID Unidad Adm.", sheet.getRow(3).getCell(0).getStringCellValue());
            assertEquals("Num. Orden", sheet.getRow(3).getCell(16).getStringCellValue());
            String[] expected = {"21170101", "21", "0021", "Ente de prueba", "001", "000", "OG",
                    "=1+1", "Dirección", "S", "Domicilio institucional", "Edificio A",
                    "Oficina institucional", "unidad@example.invalid", "009933103300", "0007", "0",
                    "María José Pérez Núñez", "=Cargo institucional", "responsable@example.invalid",
                    "009931234567", "0008"};
            for (int i = 0; i < expected.length; i++) {
                Cell cell = sheet.getRow(4).getCell(i);
                assertEquals(CellType.STRING, cell.getCellType());
                assertEquals(expected[i], cell.getStringCellValue());
                assertFalse(cell.getStringCellValue().contains("DATO_PRIVADO"));
            }
            assertEquals(4, sheet.getPaneInformation().getHorizontalSplitPosition());
            assertEquals("Responsable de la unidad", sheet.getRow(3).getCell(17).getStringCellValue());
            assertEquals("Extensión responsable", sheet.getRow(3).getCell(21).getStringCellValue());
            assertEquals(sheet.getRow(3).getCell(0).getCellStyle().getIndex(),
                    sheet.getRow(3).getCell(21).getCellStyle().getIndex());
            assertTrue(workbook.getPrintArea(0).contains("$V$"));
            assertFalse(sheet.getRow(2).getCell(21).getStringCellValue().isEmpty());
        }
        verify(unidades).findAllByIdEntePublicoAndEstatus(21L, "VIG");
        verify(ubicaciones).findAllById(Collections.singletonList(7L));
        verify(usuarios).findResponsablesDirectorioPorEnte(21L);
    }

    @Test
    void acceptsEmptyCatalogWithoutInventingRows() throws Exception {
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(service.exportar(21L)))) {
            assertEquals(3, workbook.getSheetAt(0).getLastRowNum());
            assertEquals(22, workbook.getSheetAt(0).getRow(3).getLastCellNum());
        }
        verifyNoInteractions(ubicaciones, usuarios);
    }

    @Test
    void rejectsMissingInvalidAndUnknownEnteBeforeQueryingUnits() {
        for (Long id : Arrays.asList(null, 0L, -1L))
            assertEquals(HttpStatus.BAD_REQUEST,
                    assertThrows(ResponseStatusException.class, () -> service.exportar(id)).getStatus());
        assertEquals(HttpStatus.NOT_FOUND,
                assertThrows(ResponseStatusException.class, () -> service.exportar(99L)).getStatus());
        verifyNoInteractions(unidades, ubicaciones, usuarios);
    }

    @Test
    void keepsLatestResponsibleContactTogetherAndDoesNotBorrowAnotherUnitsResponsible() throws Exception {
        SacCatUnidadAdministrativaView first = new SacCatUnidadAdministrativaView();
        first.setIdUnidadAdministrativa(10L);
        SacCatUnidadAdministrativaView second = new SacCatUnidadAdministrativaView();
        second.setIdUnidadAdministrativa(11L);
        when(unidades.findAllByIdEntePublicoAndEstatus(21L, "VIG")).thenReturn(Arrays.asList(first, second));
        when(usuarios.findResponsablesDirectorioPorEnte(21L)).thenReturn(Arrays.asList(
                new DirectorioResponsableUnidad(10L, "Titular", null, "Actual", null,
                        "Cargo actual", null, "000123", "0004"),
                new DirectorioResponsableUnidad(10L, "Titular", null, "Actual", null,
                        "Cargo anterior", "anterior@example.invalid", "999", "9"),
                new DirectorioResponsableUnidad(99L, "Otra", null, "Unidad", null,
                        "Otro cargo", "otro@example.invalid", "888", "8")));
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(service.exportar(21L)))) {
            Row actual = workbook.getSheetAt(0).getRow(4);
            assertEquals("Titular Actual", actual.getCell(17).getStringCellValue());
            assertEquals("Cargo actual", actual.getCell(18).getStringCellValue());
            assertEquals("", actual.getCell(19).getStringCellValue());
            assertEquals("000123", actual.getCell(20).getStringCellValue());
            Row unassigned = workbook.getSheetAt(0).getRow(5);
            assertEquals("Sin responsable vigente registrado", unassigned.getCell(17).getStringCellValue());
            for (int i = 18; i < 22; i++) assertEquals("", unassigned.getCell(i).getStringCellValue());
        }
    }

    @Test
    void batchesLocationsBelowOracleInLimitAndHandlesNullMetadata() throws Exception {
        List<SacCatUnidadAdministrativaView> list = new ArrayList<>();
        for (long i = 1; i <= 1001; i++) {
            SacCatUnidadAdministrativaView unit = new SacCatUnidadAdministrativaView();
            unit.setIdUnidadAdministrativa(i);
            unit.setIdUbicacionUnidadAdministrativa(i);
            list.add(unit);
        }
        when(unidades.findAllByIdEntePublicoAndEstatus(21L, "VIG")).thenReturn(list);
        List<Integer> sizes = new ArrayList<>();
        when(ubicaciones.findAllById(any())).thenAnswer(invocation -> {
            sizes.add(((List<?>) invocation.getArgument(0)).size());
            return Collections.emptyList();
        });
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(service.exportar(21L)))) {
            assertEquals(1004, workbook.getSheetAt(0).getLastRowNum());
            assertEquals("", workbook.getSheetAt(0).getRow(4).getCell(10).getStringCellValue());
        }
        assertEquals(Arrays.asList(900, 101), sizes);
    }
}
