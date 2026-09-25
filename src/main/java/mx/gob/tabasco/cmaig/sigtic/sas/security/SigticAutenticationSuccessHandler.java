package mx.gob.tabasco.cmaig.sigtic.sas.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.gob.tabasco.cmaig.sigtic.Utileria.ClienteInfo;
import mx.gob.tabasco.cmaig.sigtic.captcha.RepCaptchaV3Handler;
import mx.gob.tabasco.cmaig.sigtic.exception.ReCaptchaInvalidException;
import mx.gob.tabasco.cmaig.sigtic.sas.model.Captcha;
import mx.gob.tabasco.cmaig.sigtic.sas.model.RecaptchaResponse;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.CaptchaRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Transactional(value = "seguridadTransactionManager", readOnly = true)
@Component
public class SigticAutenticationSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    private final CaptchaRepository captchaRepository;

    private final Environment env;

/*    @Autowired
    private ServletContext servletContext;*/

/*    @Autowired
    private RecaptchaService recaptchaService;*/

    private final RequestService requestService;

    public SigticAutenticationSuccessHandler(CaptchaRepository captchaRepository, Environment env, RequestService requestService) {
        this.captchaRepository = captchaRepository;
        this.env = env;
        this.requestService = requestService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String ip=ClienteInfo.getClientIpAddr(request);
        String hosName=ClienteInfo.getHostPC(request);

        authUser.setIp(ip);
        authUser.setHosName(hosName);

        Captcha captcha=captchaRepository.findBySistema("SIGTIC").get();

        Map<String, Object> data = new HashMap<>();

        if (captcha.getValidarCaptcha().intValue()==1) {

          /*  if (recaptchaService.isResponseValid(request.getParameter("g-recaptcha-response")))
                throw new ReCaptchaInvalidException("Error: reCaptcha no fue validado.");*/

            String recaptchaFormResponse = request.getParameter("g-recaptcha-response");
            RepCaptchaV3Handler handler = new RepCaptchaV3Handler();
                String recaptchaSecret = env.getProperty("google.recaptcha.key.secret");
            String recaptchaServerVerify = env.getProperty("google.recaptcha.server.verify");
            float threshold = Float.parseFloat(env.getProperty("google.recaptcha.key.threshold"));

            RecaptchaResponse responseVer = handler.verify(recaptchaFormResponse, recaptchaSecret, recaptchaServerVerify);

            if (!responseVer.isSuccess()) {
                throw new ReCaptchaInvalidException("Error: reCaptcha invalido.");
            }

            if (responseVer.getScore() < threshold) {
                //httpServletResponse.sendError(401)
                throw new ReCaptchaInvalidException("Error: reCaptcha no fue validado.");
            }

            data.put("redirect",responseVer.getAction());
        }
        else
            data.put("redirect","/inicio");

        response.setStatus(HttpServletResponse.SC_OK);
        data.put("code", 200);
        data.put("status", "success");

        response.getOutputStream().println(objectMapper.writeValueAsString(data));
    }

}
