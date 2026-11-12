package mx.gob.tabasco.cmaig.sigtic.sac.controller;

import mx.gob.tabasco.cmaig.sigtic.sac.restController.DirectorioPublicoRestController;
import mx.gob.tabasco.cmaig.sigtic.sac.service.DirectorioPublicoService;
import mx.gob.tabasco.cmaig.sigtic.sac.service.DirectorioUnidadesExcelService;
import mx.gob.tabasco.cmaig.sigtic.sac.service.DirectorioUsuariosExcelService;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UrlFilterInvocationSecurityMetadataSource;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DirectorioUsuariosUnidadRestControllerTest {
    private DirectorioUsuariosExcelService usuarios;
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        usuarios = mock(DirectorioUsuariosExcelService.class);
        DirectorioPublicoRestController controller = new DirectorioPublicoRestController(
                mock(DirectorioPublicoService.class), mock(DirectorioUnidadesExcelService.class), usuarios);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(new ByteArrayHttpMessageConverter(), new MappingJackson2HttpMessageConverter())
                .build();
    }

    @AfterEach
    void clearSecurity() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void downloadsSelectedUnitWithPublicAndAuthenticatedRequestsWithoutApplyingOtherFilters() throws Exception {
        byte[] archivo = {1, 2, 3};
        when(usuarios.exportarUnidad(21L, 11L)).thenReturn(archivo);
        for (boolean authenticated : new boolean[]{false, true}) {
            MockHttpServletRequestBuilder request = get("/api/directorio/usuarios-unidad.xlsx")
                    .param("idEntePublico", "21").param("idUnidadAdministrativa", "11")
                    .param("nombre", "No debe filtrar").param("pagina", "5").param("size", "10");
            if (authenticated) request.principal(() -> "usuario-prueba");
            mvc.perform(request)
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .andExpect(content().bytes(archivo))
                    .andExpect(header().string("Content-Disposition", containsString("usuarios_unidad_11_21_")))
                    .andExpect(header().string("Cache-Control", "no-store"));
        }
        verify(usuarios, times(2)).exportarUnidad(21L, 11L);
        verify(usuarios, never()).exportar(anyLong());
    }

    @Test
    void rejectsMissingOrNonNumericParametersAndDoesNotAcceptPost() throws Exception {
        mvc.perform(get("/api/directorio/usuarios-unidad.xlsx"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/directorio/usuarios-unidad.xlsx").param("idEntePublico", "21"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/directorio/usuarios-unidad.xlsx").param("idUnidadAdministrativa", "11"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/directorio/usuarios-unidad.xlsx")
                .param("idEntePublico", "21").param("idUnidadAdministrativa", "texto"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/directorio/usuarios-unidad.xlsx")
                .param("idEntePublico", "21").param("idUnidadAdministrativa", "11"))
                .andExpect(status().isMethodNotAllowed());
        verifyNoInteractions(usuarios);
    }

    @Test
    void returnsSelectionErrorsWithoutReturningAFile() throws Exception {
        when(usuarios.exportarUnidad(21L, 22L)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "La unidad administrativa no pertenece al ente público seleccionado."));
        mvc.perform(get("/api/directorio/usuarios-unidad.xlsx")
                .param("idEntePublico", "21").param("idUnidadAdministrativa", "22"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("La unidad administrativa no pertenece al ente público seleccionado."));
        when(usuarios.exportarUnidad(21L, 99L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Unidad administrativa no encontrada."));
        mvc.perform(get("/api/directorio/usuarios-unidad.xlsx")
                .param("idEntePublico", "21").param("idUnidadAdministrativa", "99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void securityMetadataAllowsGetForBothVisitorsAndUsersWithoutGrantingOtherMethods() {
        SegPerfilPrivilegioService privilegios = mock(SegPerfilPrivilegioService.class);
        UrlFilterInvocationSecurityMetadataSource metadata = new UrlFilterInvocationSecurityMetadataSource(privilegios);
        for (boolean authenticated : new boolean[]{false, true}) {
            SecurityContextHolder.clearContext();
            if (authenticated)
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken("usuario-prueba", null));
            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/directorio/usuarios-unidad.xlsx");
            request.setServletPath("/api/directorio/usuarios-unidad.xlsx");
            assertNull(metadata.getAttributes(new FilterInvocation(request, new MockHttpServletResponse(),
                    (req, response) -> {})));
        }
        SecurityContextHolder.clearContext();
        MockHttpServletRequest post = new MockHttpServletRequest("POST", "/api/directorio/usuarios-unidad.xlsx");
        post.setServletPath("/api/directorio/usuarios-unidad.xlsx");
        assertNotNull(metadata.getAttributes(new FilterInvocation(post, new MockHttpServletResponse(),
                (req, response) -> {})));
        verifyNoInteractions(privilegios);
    }
}
