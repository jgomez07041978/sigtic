package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificacionBean {
    private String token;     // Token FCM del dispositivo
    private String titulo;
    private String mensaje;
}

