package mx.gob.tabasco.cmaig.sigtic.sas.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class SigticAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    private final ServletContext servletContext;

    public SigticAccessDeniedHandler(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if(request.getServletPath().contains("/api")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            Map<String, Object> data = new HashMap<>();
            data.put("status", 403);
            data.put("error", "Forbidden");
            data.put("timeStamp", Calendar.getInstance().getTime());
            data.put("message", accessDeniedException.getMessage());
            data.put("path", request.getServletPath());
            data.put("trace", null);
            response.getOutputStream().println(objectMapper.writeValueAsString(data));
        }else {
            request.getSession().invalidate();
            response.sendRedirect(servletContext.getContextPath()+"/login?denied");
            }
    }
}
