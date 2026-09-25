package mx.gob.tabasco.cmaig.sigtic.captcha;


import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
public class RecaptchaService {

    @Data
    private static class RecaptchaResponse {

        private Boolean success;
        private Date timestamp;
        private String hostname;
        @JsonProperty("error-codes")
        private List<String> errorCodes;
    }

    @Value("${google.recaptcha.server.verify}")
    private String recaptchaUrl;

    @Value("${google.recaptcha.key.secret}")
    private String recaptchaSecretKey;

    public boolean isResponseValid(String response) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("secret", recaptchaSecretKey);
        requestMap.add("response", response);

        RecaptchaResponse apiResponse = restTemplate.postForObject(recaptchaUrl, requestMap, RecaptchaResponse.class);
        if(apiResponse == null){
            return false;
        }

        Boolean resultado = Boolean.TRUE.equals(apiResponse.getSuccess());

        return resultado;

    }
}
