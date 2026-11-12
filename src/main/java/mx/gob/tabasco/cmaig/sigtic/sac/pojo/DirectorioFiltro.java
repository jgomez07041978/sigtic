package mx.gob.tabasco.cmaig.sigtic.sac.pojo;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class DirectorioFiltro {
    @Size(max = 150)
    private String nombre;
    @Positive
    private Long idEntePublico;
    @Positive
    private Long idUnidadAdministrativa;
    @Size(max = 40)
    private String telefonoExtension;
    @Size(max = 40)
    private String telefono;
    @Size(max = 20)
    private String extension;
    @Pattern(regexp = "VIG|HIS")
    private String estado;
    @Min(0)
    @Max(100000)
    private int pagina = 0;
    @Min(1)
    @Max(100)
    private int size = 20;
}
