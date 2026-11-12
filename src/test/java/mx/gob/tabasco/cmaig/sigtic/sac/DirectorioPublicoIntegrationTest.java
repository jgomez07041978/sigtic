package mx.gob.tabasco.cmaig.sigtic.sac;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.gob.tabasco.cmaig.sigtic.captcha.RepCaptchaV3Handler;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.pojo.DirectorioFiltro;
import mx.gob.tabasco.cmaig.sigtic.sac.service.DirectorioPublicoService;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.SegMenuUsuarioBean;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import org.junit.jupiter.api.Test;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import javax.persistence.EntityManagerFactory;
import java.io.ByteArrayInputStream;
import java.text.Collator;
import java.net.URI;
import java.net.URLEncoder;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@EnabledIfSystemProperty(named = "sigtic.local.integration", matches = "true")
class DirectorioPublicoIntegrationTest {
    @Autowired private MockMvc mvc;
    @Autowired @Qualifier("catalogoDatasource") private DataSource datasource;
    @Autowired @Qualifier("catalogosEntityManagerFactory") private EntityManagerFactory catalogos;
    @Autowired private DirectorioPublicoService directorio;
    @MockBean private RepCaptchaV3Handler verifier;
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @Transactional(value = "catalogosTransactionManager", readOnly = true)
    void paginatedDirectoryUsesOnlyTwoScalarQueriesWithoutLoadingEntityGraphs() {
        Statistics statistics = catalogos.unwrap(SessionFactory.class).getStatistics();
        boolean enabled = statistics.isStatisticsEnabled();
        statistics.setStatisticsEnabled(true);
        statistics.clear();
        try {
            DirectorioFiltro filtro = new DirectorioFiltro();
            filtro.setIdEntePublico(900001L);
            filtro.setSize(2);
            org.springframework.data.domain.Page<?> page = directorio.buscar(filtro);
            assertEquals(2, page.getNumberOfElements());
            assertTrue(page.getTotalElements() > 2);
            assertEquals(2, statistics.getPrepareStatementCount());
            assertEquals(0, statistics.getEntityLoadCount());
            assertEquals(0, statistics.getEntityFetchCount());
            assertEquals(0, statistics.getCollectionFetchCount());
        } finally {
            statistics.setStatisticsEnabled(enabled);
        }
    }

    @Test
    @Transactional(value = "catalogosTransactionManager", readOnly = true)
    void directoryOptionsUseTwoScalarQueriesWithoutLoadingCatalogEntityGraphs() {
        Statistics statistics = catalogos.unwrap(SessionFactory.class).getStatistics();
        boolean enabled = statistics.isStatisticsEnabled();
        statistics.setStatisticsEnabled(true);
        statistics.clear();
        try {
            Map<String, Object> options = directorio.opciones();
            assertFalse(((List<?>) options.get("entes")).isEmpty());
            assertFalse(((List<?>) options.get("unidades")).isEmpty());
            assertEquals(2, statistics.getPrepareStatementCount());
            assertEquals(0, statistics.getEntityLoadCount());
            assertEquals(0, statistics.getEntityFetchCount());
        } finally {
            statistics.setStatisticsEnabled(enabled);
        }
    }

    private JsonNode buscar(String query) throws Exception {
        return mapper.readTree(mvc.perform(get(URI.create("/sigtic/api/directorio/contactos" + query))
                .contextPath("/sigtic").servletPath("/api/directorio/contactos"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray());
    }

    private JsonNode buscarNombre(String nombre) throws Exception {
        return buscar("?nombre=" + URLEncoder.encode(nombre, "UTF-8").replace("+", "%20"));
    }

    private Workbook descargarCatalogo(String ente) throws Exception {
        byte[] bytes = mvc.perform(get("/sigtic/api/directorio/unidades.xlsx")
                .contextPath("/sigtic").servletPath("/api/directorio/unidades.xlsx").param("idEntePublico", ente))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray();
        return new XSSFWorkbook(new ByteArrayInputStream(bytes));
    }

    private Workbook descargarUsuarios(String ente) throws Exception {
        byte[] bytes = mvc.perform(get("/sigtic/api/directorio/usuarios.xlsx")
                .contextPath("/sigtic").servletPath("/api/directorio/usuarios.xlsx").param("idEntePublico", ente))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("usuarios_" + ente + "_")))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andReturn().getResponse().getContentAsByteArray();
        return new XSSFWorkbook(new ByteArrayInputStream(bytes));
    }

    @Test
    void publicUsersExcelContainsAllDirectoryRecordsOnlyFromSelectedEnteAndRejectsInvalidRequests() throws Exception {
        JsonNode page = buscar("?idEntePublico=900001&size=100");
        assertTrue(page.path("totalElements").asInt() > 0);
        assertTrue(page.path("totalElements").asInt() <= 100);
        List<List<String>> expected = new ArrayList<>();
        for (JsonNode contact : page.path("content"))
            expected.add(Arrays.asList(contact.path("nombreCompleto").asText(""), contact.path("puesto").asText(""),
                    contact.path("entePublico").asText(""), contact.path("unidadAdministrativa").asText(""),
                    contact.path("telefono").asText(""), contact.path("extension").asText(""), contact.path("correo").asText("")));
        try (Workbook workbook = descargarUsuarios("900001")) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(8, sheet.getRow(3).getLastCellNum());
            assertEquals(expected.size(), sheet.getLastRowNum() - 3);
            List<List<String>> actual = new ArrayList<>();
            for (int i = 4; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                assertEquals(i - 3, row.getCell(0).getNumericCellValue());
                List<String> values = new ArrayList<>();
                for (int col = 1; col < 8; col++) {
                    assertEquals(CellType.STRING, row.getCell(col).getCellType());
                    values.add(row.getCell(col).getStringCellValue());
                }
                actual.add(values);
            }
            expected.sort(Comparator.comparing(Object::toString));
            actual.sort(Comparator.comparing(Object::toString));
            assertEquals(expected, actual);
        }
        for (String invalid : Arrays.asList("0", "-1", "texto"))
            mvc.perform(get("/sigtic/api/directorio/usuarios.xlsx").contextPath("/sigtic")
                    .servletPath("/api/directorio/usuarios.xlsx").param("idEntePublico", invalid))
                    .andExpect(status().isBadRequest());
        mvc.perform(get("/sigtic/api/directorio/usuarios.xlsx").contextPath("/sigtic")
                .servletPath("/api/directorio/usuarios.xlsx")).andExpect(status().isBadRequest());
        mvc.perform(get("/sigtic/api/directorio/usuarios.xlsx").contextPath("/sigtic")
                .servletPath("/api/directorio/usuarios.xlsx").param("idEntePublico", Long.toString(Long.MAX_VALUE)))
                .andExpect(status().isNotFound());
        mvc.perform(post("/sigtic/api/directorio/usuarios.xlsx").contextPath("/sigtic")
                .servletPath("/api/directorio/usuarios.xlsx").param("idEntePublico", "900001"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional("catalogosTransactionManager")
    void usersExcelIncludesHistoricalAndUnassignedContactsButNeverPrivateDataOrFormulas() throws Exception {
        JdbcTemplate jdbc = new JdbcTemplate(datasource);
        assertEquals(1, jdbc.queryForObject("SELECT COUNT(*) FROM CAT_USUARIO_ENTE_PUBLICO c JOIN CAT_USUARIOS p ON p.ID_USUARIO=c.ID_USUARIO WHERE c.ID_USUARIO_ENTE_PUBLICO=900001 AND c.ID_ENTE_PUBLICO=900001 AND p.ID_USUARIO=900001 AND p.RFC_USUARIO LIKE 'DEMO%'", Integer.class));
        jdbc.update("UPDATE CAT_USUARIOS SET NOMBRE_USUARIO='=NombreExcel',SEGUNDO_NOMBRE_USUARIO=NULL,"
                + "APELLIDO_PATERNO_USUARIO=NULL,APELLIDO_MATERNO_USUARIO=NULL,"
                + "CORREO_PERSONAL_USUARIO='privado-usuarios@example.invalid',CURP_USUARIO='CURP_PRIVADO_XLSX',"
                + "NUM_CELULAR_PERSONAL_USUARIO='PRIVADO_XLSX' WHERE ID_USUARIO=900001");
        jdbc.update("UPDATE CAT_USUARIO_ENTE_PUBLICO SET CVE_ESTATUS='HIS',ID_UNIDAD_ADMINISTRATIVA=NULL,"
                + "DESC_CARGO='=1+1',CORREO_INSTITUCIONAL='institucional-usuarios@example.invalid',"
                + "NUM_TELEFONO_INSTITUCIONAL='009931234567',NUM_EXTENSION_INSTITUCIONAL='0009' WHERE ID_USUARIO_ENTE_PUBLICO=900001");
        String rfc = jdbc.queryForObject("SELECT RFC_USUARIO FROM CAT_USUARIOS WHERE ID_USUARIO=900001", String.class);
        try (Workbook workbook = descargarUsuarios("900001")) {
            Sheet sheet = workbook.getSheetAt(0);
            Row exported = null;
            for (int i = 4; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if ("=NombreExcel".equals(row.getCell(1).getStringCellValue())) exported = row;
            }
            assertNotNull(exported);
            assertEquals("=1+1", exported.getCell(2).getStringCellValue());
            assertEquals("", exported.getCell(4).getStringCellValue());
            assertEquals("009931234567", exported.getCell(5).getStringCellValue());
            assertEquals("0009", exported.getCell(6).getStringCellValue());
            assertEquals("institucional-usuarios@example.invalid", exported.getCell(7).getStringCellValue());
            for (Row row : sheet) for (Cell cell : row) {
                assertNotEquals(CellType.FORMULA, cell.getCellType());
                if (cell.getCellType() == CellType.STRING) {
                    String value = cell.getStringCellValue();
                    for (String forbidden : Arrays.asList(rfc, "privado-usuarios@example.invalid", "CURP_PRIVADO_XLSX", "PRIVADO_XLSX"))
                        assertFalse(value.contains(forbidden));
                }
            }
        }
    }

    private Row filaUnidad(Sheet sheet, String id) {
        for (int i = 4; i <= sheet.getLastRowNum(); i++)
            if (id.equals(sheet.getRow(i).getCell(0).getStringCellValue())) return sheet.getRow(i);
        throw new AssertionError("Unidad ficticia no encontrada en el catálogo");
    }

    private JdbcTemplate prepararResponsableFicticio() {
        JdbcTemplate jdbc = new JdbcTemplate(datasource);
        assertEquals(1, jdbc.queryForObject("SELECT COUNT(*) FROM CAT_USUARIOS WHERE ID_USUARIO=900001 AND RFC_USUARIO LIKE 'DEMO%'", Integer.class));
        assertEquals(2, jdbc.queryForObject("SELECT COUNT(*) FROM CAT_UNIDADES_ADMINISTRATIVAS WHERE ID_UNIDAD_ADMINISTRATIVA IN (900001,900002) AND ID_ENTE_PUBLICO=900001", Integer.class));
        assertEquals(3, jdbc.queryForObject("SELECT COUNT(*) FROM CAT_USUARIO_ENTE_PUBLICO c JOIN CAT_USUARIOS p ON p.ID_USUARIO=c.ID_USUARIO WHERE c.ID_USUARIO_ENTE_PUBLICO IN (900001,900002,900003) AND p.RFC_USUARIO LIKE 'DEMO%'", Integer.class));
        jdbc.update("UPDATE CAT_USUARIOS SET NOMBRE_USUARIO='María',SEGUNDO_NOMBRE_USUARIO='José',"
                + "APELLIDO_PATERNO_USUARIO='Núñez',APELLIDO_MATERNO_USUARIO='Pérez',CVE_ESTATUS='VIG',"
                + "CORREO_PERSONAL_USUARIO='privado@example.invalid',NUM_CELULAR_PERSONAL_USUARIO='PRIVADO_NO_EXPORTAR' WHERE ID_USUARIO=900001");
        jdbc.update("UPDATE CAT_UNIDADES_ADMINISTRATIVAS SET RFC_TITULAR_UNIDAD_ADMTVA="
                + "(SELECT ' ' || LOWER(RFC_USUARIO) || ' ' FROM CAT_USUARIOS WHERE ID_USUARIO=900001) WHERE ID_UNIDAD_ADMINISTRATIVA=900001");
        jdbc.update("UPDATE CAT_UNIDADES_ADMINISTRATIVAS SET RFC_TITULAR_UNIDAD_ADMTVA=NULL WHERE ID_UNIDAD_ADMINISTRATIVA=900002");
        jdbc.update("UPDATE CAT_USUARIO_ENTE_PUBLICO SET ID_USUARIO=900001,ID_ENTE_PUBLICO=900001,"
                + "ID_UNIDAD_ADMINISTRATIVA=900001,CVE_ESTATUS='VIG',DESC_CARGO='Titular de prueba',"
                + "CORREO_INSTITUCIONAL='responsable@example.invalid',NUM_TELEFONO_INSTITUCIONAL='009931234567',"
                + "NUM_EXTENSION_INSTITUCIONAL='0007',FECHA_ULTIMO_CAMBIO=TIMESTAMP '2026-01-01 00:00:00' WHERE ID_USUARIO_ENTE_PUBLICO=900001");
        // More recent assignments from a different ente or unit must not supply the titular's contact details.
        jdbc.update("UPDATE CAT_USUARIO_ENTE_PUBLICO SET ID_USUARIO=900001,ID_ENTE_PUBLICO=900002,"
                + "ID_UNIDAD_ADMINISTRATIVA=900001,CVE_ESTATUS='VIG',CORREO_INSTITUCIONAL='otroente@example.invalid',"
                + "FECHA_ULTIMO_CAMBIO=TIMESTAMP '2099-01-01 00:00:00' WHERE ID_USUARIO_ENTE_PUBLICO=900002");
        jdbc.update("UPDATE CAT_USUARIO_ENTE_PUBLICO SET ID_USUARIO=900001,ID_ENTE_PUBLICO=900001,"
                + "ID_UNIDAD_ADMINISTRATIVA=900002,CVE_ESTATUS='VIG',CORREO_INSTITUCIONAL='otraunidad@example.invalid',"
                + "FECHA_ULTIMO_CAMBIO=TIMESTAMP '2099-01-01 00:00:00' WHERE ID_USUARIO_ENTE_PUBLICO=900003");
        return jdbc;
    }

    @Test
    @Transactional("catalogosTransactionManager")
    void excelUsesRegisteredTitularAndOnlyInstitutionalDetailsFromSameEnteAndUnit() throws Exception {
        JdbcTemplate jdbc = prepararResponsableFicticio();
        String rfc = jdbc.queryForObject("SELECT RFC_USUARIO FROM CAT_USUARIOS WHERE ID_USUARIO=900001", String.class);
        try (Workbook workbook = descargarCatalogo("900001")) {
            Sheet sheet = workbook.getSheetAt(0);
            Row responsible = filaUnidad(sheet, "900001");
            String[] expected = {"María José Núñez Pérez", "Titular de prueba", "responsable@example.invalid", "009931234567", "0007"};
            for (int i = 0; i < expected.length; i++) {
                assertEquals(expected[i], responsible.getCell(17 + i).getStringCellValue());
                assertEquals(CellType.STRING, responsible.getCell(17 + i).getCellType());
            }
            assertEquals("Sin responsable vigente registrado", filaUnidad(sheet, "900002").getCell(17).getStringCellValue());
            for (Row row : sheet) for (Cell cell : row) {
                assertNotEquals(CellType.FORMULA, cell.getCellType());
                if (cell.getCellType() == CellType.STRING) {
                    String value = cell.getStringCellValue();
                    assertFalse(value.contains(rfc));
                    assertFalse(value.contains("privado@example.invalid"));
                    assertFalse(value.contains("PRIVADO_NO_EXPORTAR"));
                    assertFalse(value.contains("otroente@example.invalid"));
                    assertFalse(value.contains("otraunidad@example.invalid"));
                }
            }
        }
    }

    @Test
    @Transactional("catalogosTransactionManager")
    void excelDoesNotBorrowOtherUnitsContactOrUseHistoricalAssignmentsOrInactiveTitular() throws Exception {
        JdbcTemplate jdbc = prepararResponsableFicticio();
        jdbc.update("UPDATE CAT_USUARIO_ENTE_PUBLICO SET CVE_ESTATUS='HIS' WHERE ID_USUARIO_ENTE_PUBLICO=900001");
        try (Workbook workbook = descargarCatalogo("900001")) {
            Row responsible = filaUnidad(workbook.getSheetAt(0), "900001");
            assertEquals("María José Núñez Pérez", responsible.getCell(17).getStringCellValue());
            for (int i = 18; i < 22; i++) assertEquals("", responsible.getCell(i).getStringCellValue());
        }
        jdbc.update("UPDATE CAT_USUARIOS SET CVE_ESTATUS='HIS' WHERE ID_USUARIO=900001");
        try (Workbook workbook = descargarCatalogo("900001")) {
            Row responsible = filaUnidad(workbook.getSheetAt(0), "900001");
            assertEquals("Sin responsable vigente registrado", responsible.getCell(17).getStringCellValue());
            for (int i = 18; i < 22; i++) assertEquals("", responsible.getCell(i).getStringCellValue());
        }
    }

    @Test
    void publicExcelExportsOnlySelectedEnteAndRejectsInvalidRequests() throws Exception {
        JsonNode options = mapper.readTree(mvc.perform(get("/sigtic/api/directorio/opciones")
                .contextPath("/sigtic").servletPath("/api/directorio/opciones"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        String ente = options.path("unidades").get(0).path("idEntePublico").asText();
        byte[] bytes = mvc.perform(get("/sigtic/api/directorio/unidades.xlsx")
                .contextPath("/sigtic").servletPath("/api/directorio/unidades.xlsx")
                .param("idEntePublico", ente))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("attachment;")))
                .andReturn().getResponse().getContentAsByteArray();
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(bytes))) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(22, sheet.getRow(3).getLastCellNum());
            assertTrue(sheet.getLastRowNum() >= 4);
            for (int i = 4; i <= sheet.getLastRowNum(); i++) {
                assertEquals(ente, sheet.getRow(i).getCell(1).getStringCellValue());
                assertFalse(sheet.getRow(i).getCell(3).getStringCellValue().isEmpty());
                for (Cell cell : sheet.getRow(i)) assertNotEquals(CellType.FORMULA, cell.getCellType());
            }
        }
        for (String invalid : Arrays.asList("0", "-1", "texto"))
            mvc.perform(get("/sigtic/api/directorio/unidades.xlsx").contextPath("/sigtic")
                    .servletPath("/api/directorio/unidades.xlsx").param("idEntePublico", invalid))
                    .andExpect(status().isBadRequest());
        mvc.perform(get("/sigtic/api/directorio/unidades.xlsx").contextPath("/sigtic")
                .servletPath("/api/directorio/unidades.xlsx")).andExpect(status().isBadRequest());
        mvc.perform(get("/sigtic/api/directorio/unidades.xlsx").contextPath("/sigtic")
                .servletPath("/api/directorio/unidades.xlsx").param("idEntePublico", Long.toString(Long.MAX_VALUE)))
                .andExpect(status().isNotFound());
        mvc.perform(post("/sigtic/api/directorio/unidades.xlsx").contextPath("/sigtic")
                .servletPath("/api/directorio/unidades.xlsx").param("idEntePublico", ente))
                .andExpect(status().isForbidden());
    }

    @Test
    void loginLinksToPublicReadOnlyDirectory() throws Exception {
        String login = mvc.perform(get("/sigtic/login").contextPath("/sigtic").servletPath("/login"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(login.contains("href=\"/sigtic/directorio\""));
        String html = mvc.perform(get("/sigtic/directorio").contextPath("/sigtic").servletPath("/directorio"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertTrue(html.contains("Directorio Institucional"));
        assertTrue(html.contains("Volver al login"));
        assertTrue(html.contains("directory-back"));
        assertTrue(html.contains("href=\"/sigtic/login\""));
        assertFalse(html.contains("Volver al inicio"));
        assertTrue(html.contains("Teléfono/Extensión"));
        assertTrue(html.contains("name=\"telefonoExtension\""));
        assertTrue(html.contains("id=\"directoryEnteField\""));
        assertTrue(html.contains("id=\"directoryUnitField\""));
        assertTrue(html.contains("id=\"directoryUnitOpen\""));
        assertTrue(html.contains("aria-describedby=\"directoryUnitHint\" readonly disabled"));
        assertTrue(html.contains("id=\"directoryUnitModal\""));
        assertTrue(html.contains("id=\"directoryUnitTree\""));
        assertTrue(html.contains("Unidades Administrativas"));
        assertTrue(html.contains("Selecciona primero un ente público."));
        assertTrue(html.contains(">Seleccione</option>"));
        assertFalse(html.contains("Histórico"));
        assertFalse(html.contains("Vigente"));
        assertFalse(html.contains("name=\"estado\""));
        assertFalse(html.contains("name=\"telefono\""));
        assertFalse(html.contains("name=\"extension\""));
        assertFalse(html.contains(">Estado<"));
        assertTrue(html.contains("Puesto"));
        assertFalse(html.contains("Agregar Contacto"));
        assertTrue(html.contains("/include/imagen/Escudo_Header.png"));
        assertTrue(html.contains("/include/imagen/SIGTIC.png"));
        assertTrue(html.contains("Sistema Integral de Gestión de Tecnología de la Información y Comunicación"));
        assertFalse(html.contains("toggle-sidebar-btn"));
        assertFalse(html.contains("id=\"sidebar\""));
        assertFalse(html.contains("id=\"directorySidebarToggle\""));
        assertFalse(html.contains("id=\"sidebarDirectorio\""));
        assertFalse(html.contains("/js/directorio/directorio-sidebar.js"));
        assertFalse(html.contains("href=\"/sigtic/assets/css/style.css\""));
        assertFalse(html.contains("id=\"directoryUserMenu\""));
        assertFalse(html.contains("id=\"idModalPerfil\""));
        assertFalse(html.contains("id=\"idModalCambiarPassword\""));
        assertFalse(html.contains("/js/utils.js"));
        assertFalse(html.contains("jquery.serializeToJSON.js"));
        mvc.perform(get("/sigtic/assets/css/directorio.css").contextPath("/sigtic")
                .servletPath("/assets/css/directorio.css"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/css"));
        for (String path : Arrays.asList("/include/js/jquery-3.7.0.min.js",
                "/include/select2-bootstrap-5-theme-1.3.0/dist/select2.full.min.js",
                "/include/select2-bootstrap-5-theme-1.3.0/dist/select2.min.css",
                "/include/select2-bootstrap-5-theme-1.3.0/dist/select2-bootstrap-5-theme.min.css",
                "/include/vakata-jstree-8c1bcc8/dist/jstree.min.js",
                "/include/vakata-jstree-8c1bcc8/dist/themes/default/style.min.css")) {
            assertTrue(html.contains(path));
            mvc.perform(get("/sigtic" + path).contextPath("/sigtic").servletPath(path))
                    .andExpect(status().isOk());
        }
    }

    private static SegMenuUsuarioBean menu(int id, String code, String name, String url) {
        SegMenu item = new SegMenu();
        item.setIdMenu(id);
        item.setCodigo(code);
        item.setNombreMenu(name);
        item.setUrlMenu(url);
        item.setIconoMenu("bi bi-house");
        SegMenuUsuarioBean bean = new SegMenuUsuarioBean();
        bean.setMenuPadre(item);
        bean.setMenuHijo(Collections.emptyList());
        return bean;
    }

    @Test
    void authenticatedDirectoryRendersSessionSidebarWithoutChangingSessionOrPublicView() throws Exception {
        SegMenuUsuarioBean catalog = menu(3, "CATALOGOS", "Catálogos", "#");
        catalog.setMenuHijo(Collections.singletonList(
                menu(4, "CAT_ENTE_PUBLICO", "Entes permitidos", "/catalogo/entePublico").getMenuPadre()));
        List<SegMenuUsuarioBean> menus = Arrays.asList(menu(1, "INICIO", "Inicio", "/inicio"),
                menu(2, "MANUALES_TECNICO", "Manuales", "/manual_tecnico"), catalog);
        UserDetailsSesion user = mock(UserDetailsSesion.class);
        when(user.getListaMenuBean()).thenReturn(menus);
        CatUsuario person = new CatUsuario();
        person.setNombre("Perfil de prueba");
        person.setRfc("RFC_SOLO_SESION_TEST");
        person.setCurp("CURP_SOLO_SESION_TEST");
        CatUsuarioEntePublico assignment = new CatUsuarioEntePublico();
        assignment.setCatUsuario(person);
        when(user.getCatUsuarioEntePublico()).thenReturn(assignment);
        when(user.getUserNameShort()).thenReturn("Perfil de prueba");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextImpl context = new SecurityContextImpl(authentication);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("SPRING_SECURITY_CONTEXT", context);
        String html = mvc.perform(get("/sigtic/directorio").contextPath("/sigtic").servletPath("/directorio")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("directorioAuthenticated", true))
                .andExpect(model().attribute("listaMenu", menus))
                .andReturn().getResponse().getContentAsString();
        assertTrue(html.contains("class=\"directory-authenticated\""));
        assertTrue(html.contains("id=\"sidebar\""));
        assertTrue(html.contains("id=\"directorySidebarToggle\""));
        assertTrue(html.contains("class=\"bi bi-list\""));
        assertTrue(html.indexOf("class=\"directory-government\"") < html.indexOf("id=\"directorySidebarToggle\""));
        assertFalse(html.contains("directory-back"));
        assertFalse(html.contains("Volver al inicio"));
        assertTrue(html.contains("/js/directorio/directorio-sidebar.js"));
        assertTrue(html.contains("href=\"/sigtic/assets/css/style.css\""));
        assertTrue(html.contains("Entes permitidos"));
        assertTrue(html.contains("id=\"directoryUserMenu\""));
        assertTrue(html.contains("/include/imagen/Icono_Usuario.png"));
        assertTrue(html.contains("Mi Perfil"));
        assertTrue(html.contains("Cambiar Contraseña"));
        assertTrue(html.contains("Cerrar Sesión"));
        assertTrue(html.contains("href=\"/sigtic/logout\""));
        assertTrue(html.contains("id=\"idModalPerfil\""));
        assertTrue(html.contains("id=\"idModalCambiarPassword\""));
        assertTrue(html.contains("RFC_SOLO_SESION_TEST"));
        assertTrue(html.contains("CURP_SOLO_SESION_TEST"));
        assertEquals(1, html.split("id=\"contextSigtic\"", -1).length - 1);
        assertEquals(1, html.split("id=\"idFormPassword\"", -1).length - 1);
        for (String path : Arrays.asList("/include/js/jquery.serializeToJSON.js",
                "/include/jquery-validation-1.19.5/dist/jquery.validate.min.js",
                "/include/sweetalert2/sweetalert2.all.min.js", "/js/utils.js",
                "/js/directorio/directorio-user-menu.js")) {
            assertTrue(html.contains(path));
            mvc.perform(get("/sigtic" + path).contextPath("/sigtic").servletPath(path))
                    .andExpect(status().isOk());
        }
        assertEquals(1, html.split("id=\"sidebarDirectorio\"", -1).length - 1);
        assertTrue(html.indexOf("href=\"/sigtic/manual_tecnico\"") < html.indexOf("id=\"sidebarDirectorio\""));
        assertTrue(html.indexOf("id=\"sidebarDirectorio\"") < html.indexOf(">Catálogos</span>"));
        assertFalse(html.contains("href=\"/sigtic/login\""));
        assertSame(context, session.getAttribute("SPRING_SECURITY_CONTEXT"));
        assertSame(user, context.getAuthentication().getPrincipal());
        mvc.perform(get("/sigtic/api/directorio/contactos?size=1").contextPath("/sigtic")
                .servletPath("/api/directorio/contactos").session(session)).andExpect(status().isOk());
        assertSame(user, context.getAuthentication().getPrincipal());
        String publicHtml = mvc.perform(get("/sigtic/directorio").contextPath("/sigtic").servletPath("/directorio"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertFalse(publicHtml.contains("id=\"sidebar\""));
        assertFalse(publicHtml.contains("Entes permitidos"));
        assertFalse(publicHtml.contains("RFC_SOLO_SESION_TEST"));
        assertFalse(publicHtml.contains("CURP_SOLO_SESION_TEST"));
        assertFalse(publicHtml.contains("id=\"directoryUserMenu\""));
        assertFalse(publicHtml.contains("id=\"idModalPerfil\""));
        assertFalse(publicHtml.contains("id=\"idModalCambiarPassword\""));
        mvc.perform(get("/sigtic/js/directorio/directorio-sidebar.js").contextPath("/sigtic")
                .servletPath("/js/directorio/directorio-sidebar.js")).andExpect(status().isOk());
        mvc.perform(get("/sigtic/logout").contextPath("/sigtic").servletPath("/logout").session(session))
                .andExpect(status().is3xxRedirection());
        assertTrue(session.isInvalid());
    }

    @Test
    void publicContactsAreAlphabeticalPaginatedAndContainNoPersonalFields() throws Exception {
        JsonNode page = buscar("?size=5");
        assertTrue(page.path("totalElements").asInt() >= 20);
        assertEquals(5, page.path("content").size());
        Set<String> allowed = new HashSet<>(Arrays.asList("id", "nombreCompleto", "puesto", "idEntePublico",
                "entePublico", "idUnidadAdministrativa", "unidadAdministrativa", "telefono", "extension", "correo", "estado"));
        Collator collator = Collator.getInstance(new Locale("es", "MX"));
        collator.setStrength(Collator.PRIMARY);
        String previous = null;
        for (JsonNode contact : page.path("content")) {
            contact.fieldNames().forEachRemaining(field -> assertTrue(allowed.contains(field), "Campo no público: " + field));
            String name = contact.path("nombreCompleto").asText();
            if (previous != null) assertTrue(collator.compare(previous, name) <= 0);
            previous = name;
        }
        JsonNode next = buscar("?size=5&pagina=1");
        assertNotEquals(page.path("content").get(0).path("id"), next.path("content").get(0).path("id"));
        assertTrue(collator.compare(previous, next.path("content").get(0).path("nombreCompleto").asText()) <= 0);
    }

    @Test
    void filtersMatchInstitutionalFieldsAndClearReturnsAllContacts() throws Exception {
        JsonNode all = buscar("?size=100");
        JsonNode contact = all.path("content").get(0);
        String url = "?idEntePublico=" + contact.path("idEntePublico").asLong()
                + "&idUnidadAdministrativa=" + contact.path("idUnidadAdministrativa").asLong();
        JsonNode filtered = buscar(url);
        assertTrue(filtered.path("totalElements").asLong() > 0);
        for (JsonNode result : filtered.path("content")) {
            assertEquals(contact.path("idEntePublico"), result.path("idEntePublico"));
            assertEquals(contact.path("idUnidadAdministrativa"), result.path("idUnidadAdministrativa"));
        }
        assertEquals(0, buscar("?nombre=CONTACTO_INEXISTENTE_XYZ").path("totalElements").asInt());
        assertEquals(0, buscar("?nombre=%25").path("totalElements").asInt());
        assertEquals(all.path("totalElements"), buscar("").path("totalElements"));
    }

    @Test
    @Transactional("catalogosTransactionManager")
    void nameAndSurnamesMatchSeparateWordsInAnyOrderAndIgnoreAccents() throws Exception {
        JdbcTemplate jdbc = new JdbcTemplate(datasource);
        assertEquals(1, jdbc.queryForObject("SELECT COUNT(*) FROM CAT_USUARIOS WHERE ID_USUARIO=900001 AND RFC_USUARIO LIKE 'DEMO%'", Integer.class));
        jdbc.update("UPDATE CAT_USUARIOS SET NOMBRE_USUARIO=?,SEGUNDO_NOMBRE_USUARIO=?,"
                + "APELLIDO_PATERNO_USUARIO=?,APELLIDO_MATERNO_USUARIO=? WHERE ID_USUARIO=900001",
                "María", "José", "Pérez", "Núñez");
        for (String search : Arrays.asList("Maria Perez", "pérez maría", "JOSE NÚÑEZ", "Núñez",
                "Pérez Núñez", "María José Pérez Núñez", "  maria   nunez  ", "Maria\tPerez",
                "María\u00a0Pérez", "Nu\u0301n\u0303ez")) {
            JsonNode results = buscarNombre(search);
            assertTrue(results.path("content").findValues("id").stream().anyMatch(id -> id.asLong() == 900001L),
                    "No encontró nombre/apellidos: " + search);
        }
        for (String search : Arrays.asList("Maria APELLIDO_INEXISTENTE_XYZ", "Perez NOMBRE_INEXISTENTE_XYZ")) {
            JsonNode results = buscarNombre(search);
            assertEquals(0, results.path("totalElements").asInt(), "Deben coincidir todas las palabras");
        }
        assertTrue(buscar("?nombre=maria%20perez&idEntePublico=900001")
                .path("content").findValues("id").stream().anyMatch(id -> id.asLong() == 900001L));
        assertFalse(buscar("?nombre=maria%20perez&idEntePublico=900002")
                .path("content").findValues("id").stream().anyMatch(id -> id.asLong() == 900001L));
        assertEquals(buscar("").path("totalElements"), buscar("?nombre=%20%20%09%20").path("totalElements"));
    }

    @Test
    @Transactional("catalogosTransactionManager")
    void nameWordSearchKeepsSqlWildcardsLiteral() throws Exception {
        JdbcTemplate jdbc = new JdbcTemplate(datasource);
        assertEquals(1, jdbc.queryForObject("SELECT COUNT(*) FROM CAT_USUARIOS WHERE ID_USUARIO=900001 AND RFC_USUARIO LIKE 'DEMO%'", Integer.class));
        jdbc.update("UPDATE CAT_USUARIOS SET NOMBRE_USUARIO=?,SEGUNDO_NOMBRE_USUARIO=NULL,"
                + "APELLIDO_PATERNO_USUARIO=?,APELLIDO_MATERNO_USUARIO=NULL WHERE ID_USUARIO=900001",
                "Prueba%_Nombre", "Apellido\\Prueba");
        JsonNode literal = buscarNombre("Apellido\\Prueba Prueba%_Nombre");
        assertTrue(literal.path("content").findValues("id").stream().anyMatch(id -> id.asLong() == 900001L));
        JsonNode nonLiteral = buscarNombre("PruebaXXNombre");
        assertFalse(nonLiteral.path("content").findValues("id").stream().anyMatch(id -> id.asLong() == 900001L));
    }

    @Test
    @Transactional("catalogosTransactionManager")
    void historicalFilterAndAccentedFullNameWorkWithoutPersistingChanges() throws Exception {
        JdbcTemplate jdbc = new JdbcTemplate(datasource);
        assertEquals(1, jdbc.queryForObject("SELECT COUNT(*) FROM CAT_USUARIOS WHERE ID_USUARIO=900001 AND RFC_USUARIO LIKE 'DEMO%'", Integer.class));
        jdbc.update("UPDATE CAT_USUARIOS SET NOMBRE_USUARIO='Álvaro',SEGUNDO_NOMBRE_USUARIO=NULL,"
                + "APELLIDO_PATERNO_USUARIO='Núñez',APELLIDO_MATERNO_USUARIO=NULL WHERE ID_USUARIO=900001");
        jdbc.update("UPDATE CAT_USUARIO_ENTE_PUBLICO SET CVE_ESTATUS='HIS',DESC_CARGO='Puesto de prueba',"
                + "NUM_TELEFONO_INSTITUCIONAL='9931234567',NUM_EXTENSION_INSTITUCIONAL='4321' WHERE ID_USUARIO_ENTE_PUBLICO=900001");
        assertTrue(buscar("?estado=HIS").path("content").findValues("id").stream().anyMatch(id -> id.asLong() == 900001L));
        JsonNode page = buscar("?nombre=alvaro%20nunez&estado=HIS&telefono=1234567&extension=4321");
        assertEquals(1, page.path("totalElements").asInt());
        assertEquals("HIS", page.path("content").get(0).path("estado").asText());
        assertEquals("Puesto de prueba", page.path("content").get(0).path("puesto").asText());
        assertEquals(0, buscar("?nombre=alvaro%20nunez&estado=VIG").path("totalElements").asInt());
    }

    @Test
    @Transactional("catalogosTransactionManager")
    void combinedPhoneFilterMatchesEitherFieldAndEscapesWildcards() throws Exception {
        JdbcTemplate jdbc = new JdbcTemplate(datasource);
        assertEquals(1, jdbc.queryForObject("SELECT COUNT(*) FROM CAT_USUARIOS WHERE ID_USUARIO=900001 AND RFC_USUARIO LIKE 'DEMO%'", Integer.class));
        jdbc.update("UPDATE CAT_USUARIO_ENTE_PUBLICO SET NUM_TELEFONO_INSTITUCIONAL='9931234567',"
                + "NUM_EXTENSION_INSTITUCIONAL='4321' WHERE ID_USUARIO_ENTE_PUBLICO=900001");
        for (String value : Arrays.asList("1234567", "4321", "%20%204321%20")) {
            JsonNode page = buscar("?idEntePublico=900001&telefonoExtension=" + value);
            assertTrue(page.path("content").findValues("id").stream().anyMatch(id -> id.asLong() == 900001L));
        }
        assertEquals(0, buscar("?telefonoExtension=CONTACTO_INEXISTENTE_XYZ").path("totalElements").asInt());
        assertEquals(0, buscar("?telefonoExtension=%25").path("totalElements").asInt());
        assertEquals(0, buscar("?telefonoExtension=%5F").path("totalElements").asInt());
        assertEquals(buscar("").path("totalElements"), buscar("?telefonoExtension=%20").path("totalElements"));
    }

    @Test
    void publicOptionsAreLimitedAndInvalidFiltersAreRejected() throws Exception {
        JsonNode options = mapper.readTree(mvc.perform(get("/sigtic/api/directorio/opciones")
                .contextPath("/sigtic").servletPath("/api/directorio/opciones"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        assertTrue(options.path("entes").size() >= 3);
        assertTrue(options.path("unidades").size() >= 6);
        Set<String> allowed = new HashSet<>(Arrays.asList("id", "nombre", "idEntePublico", "idPadre", "clave"));
        for (JsonNode unit : options.path("unidades")) {
            assertTrue(unit.has("idPadre"));
            assertTrue(unit.has("clave"));
            unit.fieldNames().forEachRemaining(field -> assertTrue(allowed.contains(field), "Campo no público: " + field));
        }
        for (String query : Arrays.asList("?pagina=-1", "?size=101", "?estado=NVG", "?idEntePublico=-1",
                "?telefonoExtension=12345678901234567890123456789012345678901")) {
            mvc.perform(get("/sigtic/api/directorio/contactos" + query)
                    .contextPath("/sigtic").servletPath("/api/directorio/contactos"))
                    .andExpect(status().isBadRequest());
        }
        mvc.perform(post("/sigtic/api/directorio/contactos").contextPath("/sigtic")
                .servletPath("/api/directorio/contactos")).andExpect(status().isForbidden());
    }
}
