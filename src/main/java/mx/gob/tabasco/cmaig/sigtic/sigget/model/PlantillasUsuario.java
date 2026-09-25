package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Data
@Entity
@Table(name="PLANTILLAS_USUARIO_CARGA_DATOS")
public class PlantillasUsuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PLANTILLA_USUARIO_APP")
    @SequenceGenerator(sequenceName = "SEQ_PLANTILLAS_USUARIO_CARGA_D", allocationSize = 1, name = "SEQ_PLANTILLA_USUARIO_APP")
    @Column(name= "ID_PLANTILLA_USUARIO_CARGADATO")
    private Long idPlantillaUsuario;

    @ManyToOne
    @JoinColumn(name = "ID_PLANTILLA_CARGA_DATOS")
    private Plantilla plantilla;

    @Column(name="RFC_USUARIO_CARGA_DATOS")
    private String usuario;

    @Column(name="ID_ENTE_PUBLICO_CARGA_DATOS")
    private Long idEntePublico;

    @Column(name="ID_UNIDAD_ADMTVA_CARGA_DATOS")
    private Long idUnidadAdministrativa;

    @Column(name="FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column(name="FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

    @Column(name="CVE_ESTATUS")
    private String cveEstatus;

}
