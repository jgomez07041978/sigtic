package mx.gob.tabasco.cmaig.sigtic.sac.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/** Institutional data only; the titular's RFC is used for matching, never exported. */
@Getter
@AllArgsConstructor
public class DirectorioResponsableUnidad {
    private final Long idUnidadAdministrativa;
    private final String nombre;
    private final String segundoNombre;
    private final String apellidoPaterno;
    private final String apellidoMaterno;
    private final String puesto;
    private final String correo;
    private final String telefono;
    private final String extension;

    public String getNombreCompleto() {
        return Arrays.asList(nombre, segundoNombre, apellidoPaterno, apellidoMaterno).stream()
                .filter(Objects::nonNull).map(String::trim).filter(value -> !value.isEmpty())
                .collect(Collectors.joining(" ")).replaceAll("\\s+", " ");
    }
}
