package mx.gob.tabasco.cmaig.sigtic.sac.controller;

import mx.gob.tabasco.cmaig.sigtic.sas.pojo.SegMenuUsuarioBean;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.Perfil;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegUsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectorioControllerTest {
    private DirectorioController controller;
    private SegUsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        controller = new DirectorioController();
        MockServletContext context = new MockServletContext();
        context.setContextPath("/sigtic");
        ReflectionTestUtils.setField(controller, "servletContext", context);
        usuarioService = mock(SegUsuarioService.class);
        ReflectionTestUtils.setField(controller, "segUsuarioService", usuarioService);
    }

    private void assertReturn(Authentication authentication, String url, String label) {
        ExtendedModelMap model = new ExtendedModelMap();
        assertEquals("directorio/directorio", controller.usuarios(model, authentication));
        assertEquals("/sigtic", model.get("contextSigtic"));
        assertEquals(url, model.get("directorioReturnUrl"));
        assertEquals(label, model.get("directorioReturnLabel"));
        assertEquals("/inicio".equals(url), model.get("directorioAuthenticated"));
        if ("/login".equals(url)) {
            assertFalse(model.containsAttribute("listaMenu"));
            assertFalse(model.containsAttribute("perfil"));
            verifyNoInteractions(usuarioService);
        }
    }

    @Test
    void visitorReturnsToLogin() {
        assertReturn(null, "/login", "Volver al login");
    }

    @Test
    void anonymousAuthenticationStillReturnsToLogin() {
        Authentication anonymous = new AnonymousAuthenticationToken("test", "anonymousUser",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
        assertReturn(anonymous, "/login", "Volver al login");
    }

    @Test
    void authenticatedVisitorReturnsToHomeWithoutLoggingOut() {
        Authentication user = new UsernamePasswordAuthenticationToken("test-user", null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        assertReturn(user, "/inicio", "Volver al inicio");
    }

    @Test
    void authenticatedDirectoryReusesSessionMenusWithoutAddingPermissions() {
        UserDetailsSesion principal = mock(UserDetailsSesion.class);
        java.util.List<SegMenuUsuarioBean> menus = Collections.singletonList(new SegMenuUsuarioBean());
        when(principal.getListaMenuBean()).thenReturn(menus);
        Perfil perfil = new Perfil();
        when(usuarioService.getPerfil()).thenReturn(perfil);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        ExtendedModelMap model = new ExtendedModelMap();
        controller.usuarios(model, authentication);
        assertEquals(true, model.get("directorioAuthenticated"));
        assertSame(menus, model.get("listaMenu"));
        assertEquals("DIRECTORIO", model.get("activarMenu"));
        assertSame(perfil, model.get("perfil"));
        verify(usuarioService).getPerfil();
    }
}
