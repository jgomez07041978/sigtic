package mx.gob.tabasco.cmaig.sigtic.captcha;

import mx.gob.tabasco.cmaig.sigtic.sas.model.RecaptchaResponse;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.io.Serializable;

public class RepCaptchaV3Handler implements Serializable {

    public RecaptchaResponse verify(String recaptchaFormResponse, String recaptchaSecret, String recaptchaServerVerify){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("secret",recaptchaSecret);
        map.add("response",recaptchaFormResponse);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        /* RecaptchaResponse response = restTemplate.postForObject(recaptchaServerVerify, request, RecaptchaResponse.class)*/

        ResponseEntity<RecaptchaResponse> response = restTemplate.exchange(recaptchaServerVerify,
                HttpMethod.POST,
                request,
                RecaptchaResponse.class);

        return response.getBody();
    }

}
