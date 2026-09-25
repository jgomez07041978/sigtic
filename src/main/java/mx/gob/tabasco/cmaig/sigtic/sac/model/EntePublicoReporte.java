package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Data;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoServiciosTic;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="CAT_ENTES_PUBLICO_REPORTE")
public class EntePublicoReporte implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ENTE_PUBLICO_REPORTE_APP")
    @SequenceGenerator(sequenceName = "SEQ_ENTE_PUBLICO_REPORTE", allocationSize = 1, name = "SEQ_ENTE_PUBLICO_REPORTE_APP")
    @Column(name = "ID_ENTE_PUBLICO_REPORTE")
    private Long idEntePublicoReporte;

    @Column(name = "NOMBRE_REPORTE")
    private String reporte;

    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO")
    private EntePublico entePublico;

    @Column(name = "HEAD1")
    private String head1;

    @Column(name = "HEAD2")
    private String head2;

    @Column(name = "HEAD3")
    private String head3;

    @Column(name = "BODY1")
    private String body1;

    @Column(name = "BODY2")
    private String body2;

    @Column(name = "PIE1")
    private String pie1;

    @Column(name = "PIE2")
    private String pie2;

    @Column(name = "LOGO")
    private String logo;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

/*    @Column(name = "ID_USUARIO_REGISTRO")
    private Long idUsuarioRegistro;*/

/*    @Column(name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;*/

/*    @Column(name = "ID_USUARIO_ACTUALIZO")
    private Long idUsuarioActualizo;

    @Column(name = "FECHA_ACTUALIZO")
    private Timestamp fechaActualizo;*/

    @ManyToOne
    @JoinColumn(name="ID_UNIDAD_ADMINISTRATIVA", nullable = true)
    private UnidadAdministrativa unidadAdministrativa;

    @Column(name = "ID_TIPO_SERVICIO")
    private Long idTipoServicio;

    @Column(name = "ID_SUBTIPO_SERVICIO")
    private Long idSubTipoServicio;

    @Column(name="FOLIO")
    private String folio;

    @Column(name="CONSECUTIVO")
    private Integer consecutivo;

    @Column(name="ANIO")
    private Integer anio;

    @Transient
    private CatTipoServiciosTic catTipoServiciosTic;

    @Transient
    private CatSubtipoServiciosTic catSubtipoServiciosTic;

}
