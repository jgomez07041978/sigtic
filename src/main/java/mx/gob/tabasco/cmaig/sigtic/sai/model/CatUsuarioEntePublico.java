package mx.gob.tabasco.cmaig.sigtic.sai.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name="SIGGET_SAC_CATALOGOS.CAT_USUARIO_ENTE_PUBLICO")
public class CatUsuarioEntePublico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO_ENTE_PUBLICO_APP")
    @SequenceGenerator(sequenceName = "SEQ_USUARIO_ENTE_PUBLICO", allocationSize = 1, name = "SEQ_USUARIO_ENTE_PUBLICO_APP")
    @Column(name = "ID_USUARIO_ENTE_PUBLICO")
    private Long idUsuarioEntePublico;

    @ManyToOne
    @JoinColumn(name="ID_USUARIO")
    private Usuario catUsuario;

    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO", nullable = true)
    private EntePublico entePublico;

    @Column(name = "CORREO_INSTITUCIONAL", nullable = true)
    private String correoInstitucional;

    @Column(name = "NUM_TELEFONO_INSTITUCIONAL", nullable = true)
    private String numTelefonoInstitucional;

    @Column(name = "NUM_EXTENSION_INSTITUCIONAL", nullable = true)
    private String numeroExtensionInstitucional;

    @Column(name = "DESC_CARGO", nullable = true)
    private String puesto;

    @ManyToOne
    @JoinColumn(name="ID_UNIDAD_ADMINISTRATIVA", nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private UnidadAdministrativa unidadAdministrativa;

    @ManyToOne
    @JoinColumn(name="ID_UBICACION_UNIDAD_ADMTVA", nullable = true)
    private UbicacionUnidadAdmin ubicacionUnidadAdmin;

    @Column(name="CVE_ESTATUS")
    private String cveEstatus;

    @Column(name="FECHA_ULTIMO_CAMBIO")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Timestamp fechaUltimoCambio;

    @Column(name="ID_USUARIO_REGISTRO")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Long idUsuarioRegistro;

    @ManyToOne
    @JoinColumn(name="ID_UNIDAD_ADMINISTRATIVA_PPTAL", nullable = true)
    private UnidadAdministrativa unidadAdministrativaPptal;

    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO_RESGUARDANTE", nullable = true)
    private EntePublico entePublicoResguardante;

    @ManyToOne
    @JoinColumn(name="ID_UNIDAD_RESGUARDANTE", nullable = true)
    private UnidadAdministrativa unidadAdministrativaResguardante;

    @ManyToOne
    @JoinColumn(name="ID_USUARIO_RESGUARDANTE", nullable = true)
    private Usuario catUsuarioResguardante;

    @Transient
    private Boolean isNotEntePublico;

    @Transient
    private Boolean isVigente;

    @Transient
    private Boolean isHistorico;

    @Transient
    private Boolean isNoVigente;

}
