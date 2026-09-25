package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Data;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CartaResponsiva;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoServiciosTic;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name="CAT_PLANTILLA_REPORTE")
public class CatPlantillaReporte implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PLANTILLA_REPORTE_APP")
    @SequenceGenerator(sequenceName = "SEQ_PLANTILLA_REPORTE", allocationSize = 1, name = "SEQ_PLANTILLA_REPORTE_APP")
    @Column(name = "ID_PLANTILLA_REPORTE")
    private Long idPlantillaReporte;

    @Column(name = "NOMBRE_REPORTE")
    private String nombreReporte;

    @Column(name = "TITULO_REPORTE")
    private String tituloReporte;

/*    @Column(name="CUERPO")
    @Lob
    private String cuerpo;*/

/*    @Column(name = "FIRMA1")
    private String firma1;

    @Column(name = "CARGO1")
    private String cargo1;*/

    @Column(name = "DESC_FIRMA1")
    private String decFirma1;

    @Column(name = "FIRMA2")
    private String firma2;

    @Column(name = "CARGO2")
    private String cargo2;

    @Column(name = "DESC_FIRMA2")
    private String decFirma2;

    @Column(name = "FIRMA3")
    private String firma3;

    @Column(name = "CARGO3")
    private String cargo3;

    @Column(name = "DESC_FIRMA3")
    private String decFirma3;

    @Column(name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column(name = "ID_USUARIO_REGISTRO")
    private Long idUsuarioRegistro;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO")
    private EntePublico entePublico;

    @Column(name = "ID_SUBTIPO_SERVICIO")
    private Long idSubtipoServicio;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO_ENTE_PUBLICO_ENT")
    private CatUsuarioEntePublico catUsuarioEntePublicoEntrega;

    @Column(name = "CURP_ENTREGA")
    private String curpEntrega;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO_ENTE_PUBLICO_AUT")
    private CatUsuarioEntePublico catUsuarioEntePublicoAutoriza;

    @Column(name = "CURP_AUTORIZA")
    private String curpAutoriza;

    @ManyToOne
    @JoinColumn(name = "ID_LOGO", nullable = true)
    private Logo logo;

    @Column(name = "FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

    @Column(name = "IP_PC_MTTO", nullable = true)
    private String ipPC;

    @Column(name = "NOMBRE_PC_MTTO", nullable = true)
    private String nombrePC;

    @Column(name = "CVE_USUARIO_MTTO", nullable = true)
    private String cveUsuarioMtto;

    @Transient
    private CatSubtipoServiciosTic catSubtipoServiciosTic;

    @Transient
    private String rutaLogo;

}
