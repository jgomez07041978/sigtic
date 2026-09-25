package mx.gob.tabasco.cmaig.sigtic.sas.security;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Component
public class UrlFilterInvocationSecurityMetadataSource  implements FilterInvocationSecurityMetadataSource {

    private final SegPerfilPrivilegioService segPerfilPrivilegioService;

    public UrlFilterInvocationSecurityMetadataSource(SegPerfilPrivilegioService segPerfilPrivilegioService) {
        this.segPerfilPrivilegioService = segPerfilPrivilegioService;
    }
/*
    @Autowired
    private HttpSession session;
    */

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuteticated=true;

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            isAuteticated=false;

        //String requestUrl = ((FilterInvocation) object).getRequestUrl();
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        //HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());

        // Bypassing Security check for /js, /css and /images url
        if (
                new AntPathRequestMatcher("/api/v1/notificacionMovil/getAll").matches(request)
                ||new AntPathRequestMatcher("/api/v1/notificacionMovil/save").matches(request)
                ||new AntPathRequestMatcher("/api/v1/notificacionMovil/sendMessage").matches(request)
                ||new AntPathRequestMatcher("/include/**").matches(request)
                || new AntPathRequestMatcher("/assets/**").matches(request)
                || new AntPathRequestMatcher("/jasper/**").matches(request)
                || new AntPathRequestMatcher("/js/**").matches(request)
                || new AntPathRequestMatcher("/login").matches(request)
                || new AntPathRequestMatcher("/manuales").matches(request)
                || new AntPathRequestMatcher("/getCr/**").matches(request)
                || new AntPathRequestMatcher("/getCrPdf/**").matches(request)
                || new AntPathRequestMatcher("/carta-responsiva-aceptar/**").matches(request)
                || new AntPathRequestMatcher("/directorio").matches(request)
                || new AntPathRequestMatcher("/pre-registro").matches(request)
                || new AntPathRequestMatcher("/recuperar-acceso").matches(request)
                || new AntPathRequestMatcher("/api/directorio/filtrar").matches(request)
                || new AntPathRequestMatcher("/api/v1/kml/**").matches(request)
                || new AntPathRequestMatcher("/api/tree/getPorInstitucionFree/**").matches(request)
                || new AntPathRequestMatcher("/api/tree/free/**").matches(request)
                || new AntPathRequestMatcher("/api/manual/listaManualesFree").matches(request)
                || new AntPathRequestMatcher("/api/logo/exportLogo/**").matches(request)
                || new AntPathRequestMatcher("/api/logo/exportLogoCr/**").matches(request)
                || new AntPathRequestMatcher("/api/manual/exportManual/**").matches(request)
                || new AntPathRequestMatcher("/api/seguridad/validarOpcionRecuperacion").matches(request)
                || new AntPathRequestMatcher("/api/seguridad/recuperarAcceso").matches(request)
                || new AntPathRequestMatcher("/api/seguridad/recuperarPassword").matches(request)
                || new AntPathRequestMatcher("/api/cartaResponsiva/aceptarCrFree/**").matches(request)
                || new AntPathRequestMatcher("/api/cartaResponsiva/rechazarCrFree/**").matches(request)
                || new AntPathRequestMatcher("/api/cartaResponsiva/asignarPlantillaCartaResponsivaId/**").matches(request)
                || new AntPathRequestMatcher("/api/cartaResponsiva/aceptarCartaResponsivaUser").matches(request)
                || new AntPathRequestMatcher("/api/cartaResponsiva/getCartaResponsivaLotes").matches(request)
                || new AntPathRequestMatcher("/api/cartaResponsiva/cartaResponsivaReporte/**").matches(request)
                || new AntPathRequestMatcher("/api/cartaResponsiva/exportCartaResponsiva/**").matches(request)
                || new AntPathRequestMatcher("/api/preRegistro/**").matches(request)
                || new AntPathRequestMatcher("/api/entePublico/listaFree").matches(request)
                || new AntPathRequestMatcher("/logout").matches(request)
                || new AntPathRequestMatcher("/error_404").matches(request)
                || new AntPathRequestMatcher("/error").matches(request)
                || new AntPathRequestMatcher("/sigtic").matches(request)
                || new AntPathRequestMatcher("/").matches(request)
        ) {
            return null;
        }

        if (isAuteticated) {

            UserDetailsSesion userDetails = (UserDetailsSesion) authentication.getPrincipal();

            if (new AntPathRequestMatcher("/api/**").matches(request))
                return SecurityConfig.createList("RECURSOS");

            if (userDetails.getListaMenu().size()==0)
                return SecurityConfig.createList("DENIED");
            else {

            /*    if (request.getServletPath().equals("/error"))
                    return SecurityConfig.createList("DENIED_404");
                else {*/
                    String url = request.getRequestURL().toString();

                    SegMenu segMenu = null;
                    for (SegMenu sm : userDetails.getListaMenu()) {
                        if (url.contains("&")) {
                            String[] divideUrl = url.split("&");
                            if (divideUrl.length > 0)
                                if (sm.getUrlMenu().equals(divideUrl[0])) {
                                    segMenu = sm;
                                    break;
                                }
                        } else {
                            if (sm.getUrlMenu().equals(url)) {
                                segMenu = sm;
                                break;
                            }
                        }
                    }

                    if (segMenu == null) {
                        for (SegMenu sm : userDetails.getListaMenu()) {
                            if (new AntPathRequestMatcher(sm.getUrlMenu().split("\\?")[0]).matches(request)) {
                                segMenu = sm;
                                break;
                            }
                        }
                    }

                    if (segMenu == null)
                        return SecurityConfig.createList("DENIED");
                    else {

                        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC", userDetails.getUsername(), segMenu.getIdMenu());

                        if (segPerfilPrivilegio == null)
                            return SecurityConfig.createList("DENIED");
                        else
                            return SecurityConfig.createList(segPerfilPrivilegio.getSegPerfil().getNombrePerfil());
                    }
                }
          //  }

        }else
            return SecurityConfig.createList("DENIED");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
