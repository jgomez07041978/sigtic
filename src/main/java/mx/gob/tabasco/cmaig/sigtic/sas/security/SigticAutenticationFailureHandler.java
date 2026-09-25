package mx.gob.tabasco.cmaig.sigtic.sas.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class SigticAutenticationFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, Object> data = new HashMap<>();


        data.put("fecha", Calendar.getInstance().getTime());
        data.put("error", exception.getMessage());

        if (exception.getMessage().equals("Bad credentials"))
            data.put("message","Password Incorrecto");
        else
            data.put("message",exception.getMessage());

        response.getOutputStream().println(objectMapper.writeValueAsString(data));
    }
}
