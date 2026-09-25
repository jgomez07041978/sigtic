package mx.gob.tabasco.cmaig.sigtic.sas.restController;

import com.google.auth.oauth2.GoogleCredentials;
import mx.gob.tabasco.cmaig.sigtic.sas.model.NotificacionBean;
import mx.gob.tabasco.cmaig.sigtic.sas.model.TokenMovil;
import mx.gob.tabasco.cmaig.sigtic.sas.service.NotificacionMovilService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notificacionMovil")
public class NotificacionMovilRestController {

    private final NotificacionMovilService notificacionMovilService;

    private static final String FCM_SERVER_KEY = "AIzaSyBzsL_O-yHrFIHO4w34R3ai0ZYT8OLLu1M";
    //private static final String FCM_API_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String FCM_API_URL = "https://fcm.googleapis.com/v1/projects/appnotificaciones-c61a1/messages:send";

    public NotificacionMovilRestController(NotificacionMovilService notificacionMovilService) {
        this.notificacionMovilService = notificacionMovilService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TokenMovil>> getTokenAll(){
        return  ResponseEntity.ok(notificacionMovilService.getTokenList());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody TokenMovil tokenMovil){
        return  ResponseEntity.ok(notificacionMovilService.save(tokenMovil));
    }


    @PostMapping("/sendMessage")
    public ResponseEntity<String> enviarNotificacion(@RequestBody NotificacionBean notificacionBean) {
        try {
            ClassPathResource resource = new ClassPathResource("firebase/serviceAccountKey.json");


            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(resource.getInputStream())
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));

            googleCredentials.refreshIfExpired();
            String accessToken = googleCredentials.getAccessToken().getTokenValue();


            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> notification = new HashMap<>();
            notification.put("title", notificacionBean.getTitulo());
            notification.put("body", notificacionBean.getMensaje());

            Map<String, Object> message = new HashMap<>();
            message.put("token", notificacionBean.getToken());
            message.put("notification", notification);

            Map<String, Object> jsonBody = new HashMap<>();
            jsonBody.put("message", message);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(jsonBody, headers);

            return restTemplate.postForEntity(FCM_API_URL, request, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar la notificación: " + e.getMessage());
        }
    }
}
