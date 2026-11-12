package mx.gob.tabasco.cmaig.sigtic.sac.pojo;

import lombok.Getter;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;

/** Solo datos institucionales autorizados para la consulta pública. */
@Getter
public class DirectorioContacto {
    private final Long id;
    private final String nombreCompleto;
    private final String puesto;
    private final Long idEntePublico;
    private final String entePublico;
    private final Long idUnidadAdministrativa;
    private final String unidadAdministrativa;
    private final String telefono;
    private final String extension;
    private final String correo;
    private final String estado;

    public DirectorioContacto(Long id, String nombre, String segundoNombre, String apellidoPaterno,
            String apellidoMaterno, String puesto, Long idEntePublico,
            String entePublico, Long idUnidadAdministrativa, String unidadAdministrativa,
            String telefono, String extension, String correo, String estado) {
        this.id = id;
        StringBuilder fullName = new StringBuilder();
        for (String part : new String[] {nombre, segundoNombre, apellidoPaterno, apellidoMaterno})
            if (part != null) fullName.append(' ').append(part);
        this.nombreCompleto = fullName.toString().trim().replaceAll("\\s+", " ");
        this.puesto = puesto;
        this.idEntePublico = idEntePublico;
        this.entePublico = entePublico;
        this.idUnidadAdministrativa = idUnidadAdministrativa;
        this.unidadAdministrativa = unidadAdministrativa;
        this.telefono = telefono;
        this.extension = extension;
        this.correo = correo;
        this.estado = estado;
    }

    public DirectorioContacto(CatUsuarioEntePublico contacto) {
        id = contacto.getIdUsuarioEntePublico();
        nombreCompleto = contacto.getCatUsuario() == null ? "" :
                contacto.getCatUsuario().getNombreCompleto().trim().replaceAll("\\s+", " ");
        puesto = contacto.getPuesto();
        idEntePublico = contacto.getEntePublico() == null ? null : contacto.getEntePublico().getIdEntePublico();
        entePublico = contacto.getEntePublico() == null ? null : contacto.getEntePublico().getNombreEntePublico();
        idUnidadAdministrativa = contacto.getUnidadAdministrativa() == null ? null :
                contacto.getUnidadAdministrativa().getIdUnidadAdministrativa();
        unidadAdministrativa = contacto.getUnidadAdministrativa() == null ? null :
                contacto.getUnidadAdministrativa().getNombreUnidadAdministrativa();
        telefono = contacto.getNumTelefonoInstitucional();
        extension = contacto.getNumeroExtensionInstitucional();
        correo = contacto.getCorreoInstitucional();
        estado = contacto.getCveEstatus();
    }
}
