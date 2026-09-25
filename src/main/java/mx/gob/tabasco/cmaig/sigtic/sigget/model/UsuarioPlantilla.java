package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Table
@Entity(name = "VIEW_USUARIOS_PLANTILLAS")
public class UsuarioPlantilla implements Serializable {

    @Id
    @Column(name="CURP_USUARIO")
    private String curpUsuario;

    @Column(name="RFC_USUARIO")
    private String rfcUsuario;

    @Column(name="NOMBRE_USUARIO")
    private String nombreUsuario;

    @Column(name="APELLIDO_PATERNO_USUARIO")
    private String apellidoPaterno;

    @Column(name="APELLIDO_MATERNO_USUARIO")
    private String apellidoMaterno;

    @Column(name="ID_ENTE_PUBLICO")
    private Long idEntePublico;

    @Column(name="CVE_ENTE_PUBLICO")
    private String cveEntePublico;

    @Column(name="DESC_ENTE_PUBLICO")
    private String descEntePublico;

    @Column(name="ID_UNIDAD_ADMINISTRATIVA")
    private Long idUnidadAdministrativa;

    @Column(name="CVE_UNIDAD_ADMINISTRATIVA")
    private String cveUnidadAdministrativa;

    @Column(name="DESC_UNIDAD_ADMINISTRATIVA")
    private String descUnidadAdministrativa;

    @Column(name="NUM_PLANTILLA")
    private Integer numeroPlantilla;

    @Column(name="CVE_ESTATUS")
    private String cveEstatus;

    @Transient
    private Integer pagina;

    @Transient
    private Integer size;
}
