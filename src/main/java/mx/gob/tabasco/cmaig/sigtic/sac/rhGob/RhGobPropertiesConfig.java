package mx.gob.tabasco.cmaig.sigtic.sac.rhGob;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rh-gob")
public class RhGobPropertiesConfig {

    private  String email;
    private String password;
    private String urlToken;
    private String urlEmpleadoEstatus;
}
