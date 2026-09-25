package mx.gob.tabasco.cmaig.sigtic.sas.security;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Iterator;

@Component
public class SigticAccessDecisionManager implements AccessDecisionManager {

    private final HttpServletResponse response;
    private final ServletContext servletContext;

    public SigticAccessDecisionManager(HttpServletResponse response, ServletContext servletContext) {
        this.response = response;
        this.servletContext = servletContext;
    }

    @SneakyThrows
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        Iterator<ConfigAttribute> iterator = configAttributes.iterator();

        while (iterator.hasNext()) {

            if (authentication == null|| authentication instanceof AnonymousAuthenticationToken)
                throw new AccessDeniedException("No ha Iniciado Sesión");

            ConfigAttribute configAttribute = iterator.next();
            String needCode = configAttribute.getAttribute();

           /** if (needCode.equals("DENIED_404"))
                response.sendRedirect(servletContext.getContextPath()+"/error_404");*/

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (StringUtils.equals(authority.getAuthority(), "ROLE_" + needCode)) {
                    return;
                }
            }
        }

        throw new AccessDeniedException("Acceso Denegado");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
