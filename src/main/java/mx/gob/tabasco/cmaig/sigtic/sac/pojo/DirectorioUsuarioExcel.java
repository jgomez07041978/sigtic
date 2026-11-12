package mx.gob.tabasco.cmaig.sigtic.sac.pojo;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/** Projection restricted to institutional directory data. */
@Getter
public class DirectorioUsuarioExcel {
    private final Long id;
    private final String nombreCompleto;
    private final String puesto;
    private final String entePublico;
    private final String unidadAdministrativa;
    private final String telefono;
    private final String extension;
    private final String correo;

    public DirectorioUsuarioExcel(Long id, String nombre, String segundoNombre, String apellidoPaterno,
            String apellidoMaterno, String puesto, String entePublico, String unidadAdministrativa,
            String telefono, String extension, String correo) {
        this.id = id;
        this.nombreCompleto = Arrays.asList(nombre, segundoNombre, apellidoPaterno, apellidoMaterno).stream()
                .filter(Objects::nonNull).map(String::trim).filter(value -> !value.isEmpty())
                .collect(Collectors.joining(" ")).replaceAll("\\s+", " ");
        this.puesto = puesto;
        this.entePublico = entePublico;
        this.unidadAdministrativa = unidadAdministrativa;
        this.telefono = telefono;
        this.extension = extension;
        this.correo = correo;
    }
}
