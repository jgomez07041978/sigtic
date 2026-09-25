package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatPlantillaReporte;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


@Data
@Entity
@Table(name="CARTA_RESPONSIVA")
public class CartaResponsiva implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CARTA-RESPO_APP")
    @SequenceGenerator(sequenceName = "SEQ_CARTA_RESPONSIVA", allocationSize = 1, name = "SEQ_CARTA-RESPO_APP")
    @Column(name = "ID_CARTA_RESPONSIVA")
    private Long idCartaResponsiva;

    @Column(name = "FOLIO_CARTA_RESPONSIVA")
    private Integer folioCartaResponsiva;

    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO")
    private EntePublico entePublico;

    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO_RESPONSABLE")
    private EntePublico entePublicoResponsable;

    @ManyToOne
    @JoinColumn(name="ID_USUARIO")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name="ID_USUARIO_ENTE_PUBLICO", nullable = true)
    private CatUsuarioEntePublico catUsuarioEntePublico;

/*    @Column(name = "ID_USUARIO_ENTE_PUBLICO")
    private Long idUsuarioEntePublico;

    @Transient
    private CatUsuarioEntePublico catUsuarioEntePublico;*/

    @ManyToOne
    @JoinColumn(name="ID_USUARIO_MTTO")
    private Usuario usuarioMtto;

    @Column(name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column(name = "FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

    @Column(name = "ANIO")
    private Integer anio;

    @Column(name = "FOLIO_CR")
    private String folioCr;

    @Column(name = "TOKEN_URL")
    private String tokenUrl;

    @Column(name = "IP_REGISTRO")
    private String ipRegistro;

    @Column(name = "HOST_REGISTRO")
    private String hostRegistro;

    @ManyToOne
    @JoinColumn(name = "ID_ESTATUS_CARTA_RESPONSIVA")
    private CatEstatusCartaResponsiva estatusCartaResponsiva;

    @Column(name = "NOMBRE_ARCHIVO")
    private String fileName;

    @Column(name = "NOMBRE_ORGINAL_ARCHIVO")
    private String fileNameOriginal;

    @Column(name = "EXTENSION_ARCHIVO")
    private String fileExtension;

    @Column(name = "RUTA_ARCHIVO")
    private String fileRoute;

    @Column(name = "ID_USUARIO_CARGO_ARCHIVO")
    private Long idUserLoadFile;

    /*@ManyToOne
    @JoinColumn(name="ID_USUARIO_AUTORIZA")
    private Usuario usuarioAutoriza;

    @Column(name = "CARGO_USARIO_AUTORIZA")
    private String cargoUsuarioAutoriza;
*/
    @Column(name = "CARGO_USUARIO_RESPONSABLE")
    private String cargoUsuarioResponsable;

    @ManyToOne
    @JoinColumn(name = "ID_SUBTIPO_SERVICIO")
    private CatSubtipoServiciosTic catSubtipoServiciosTic;

    @Column(name = "ID_ENTE_PUBLICO_REPORTE")
    private Long idEntePublicoReporte;

    @Column(name = "ID_ENTE_PUBLICO_REPORTE_RESP")
    private Long idEntePublicoReporteResponsable;

    @Column(name = "CORREO_INSTITUCIONAL")
    private String correoInstitucional;

    @Column(name = "CORREO_PERSONAL")
    private String correoPersonal;

    @Column(name = "PWD_TEMPORAL")
    private String pwdTemporal;

    @Transient
    private String fechaRegistroText;

    public String getFechaRegistroText() {

        if (this.fechaRegistro!=null) {
            Date date = new Date(this.fechaRegistro.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            this.fechaRegistroText = dateFormat.format(date);
            return this.fechaRegistroText;
        }
        else
            return null;
    }

    @Transient
    private String nota;

    @Transient
    private Long idTipoServicio;

    @Transient
    private Boolean isEstatusVig;

    @Transient
    private Boolean isEstatusHis;

    @Column(name = "NOTIFICACION_ENTREGA_SERVICIO")
    private String notificacionEntregaServicio;


    /*Campos de Aceptación*/

    @Column(name = "ACEPTO")
    private String acepto;

    @Column(name = "FECHA_ACEPTO")
    private Timestamp fechaAcepto;

    @Transient
    private String fechaAceptoText;

    public String getFechaAceptoText() {

        if (this.fechaAcepto!=null) {
            Date date = new Date(this.fechaAcepto.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            this.fechaAceptoText = dateFormat.format(date);
            return this.fechaAceptoText;
        }
        else
            return null;
    }

    @Column(name = "IP_ACEPTO")
    private String ipAcepto;

    @Column(name = "HOST_ACEPTO")
    private String hostAcepto;

    @Column(name="CARTA_RESPONSIVA_DOC")
    @Lob
    private String cartaResponsivaDoc;

    @Column(name = "NOMBRE_REPORTE")
    private String nombreReporte;

    @Column(name = "TITULO_REPORTE")
    private String tituloReporte;

    @Column(name = "FIRMA1")
    private String firma1;

    @Column(name = "CARGO1")
    private String cargo1;

    @Column(name = "DESC_FIRMA1")
    private String descripcionFirma1;

    @Column(name = "FIRMA2")
    private String firma2;

    @Column(name = "CARGO2")
    private String cargo2;

    @Column(name = "DESC_FIRMA2")
    private String descripcionFirma2;

    @Column(name = "FIRMA3")
    private String firma3;

    @Column(name = "CARGO3")
    private String cargo3;

    @Column(name = "DESC_FIRMA3")
    private String descripcionFirma3;

    @Column(name = "ID_PLANTILLA_REPORTE")
    private Long idPlantillaReporte;

    @Transient
    private CatPlantillaReporte catPlantillaReporte;

    @Transient
    private String plantillaReporteText;

/*    @Column(name = "FECHA_REPORTE_ACEPTO")
    private String fechaReporteAcepto;*/

    @Column(name = "UBICACION_RESPONSABLE")
    private String ubicacionResponsable;

    @Column(name = "CURP_ACEPTO")
    private String curpAcepto;

    @Column(name = "NOMBRE_COMPLETO_ACEPTO")
    private String nombreCompletoAcepto;

    @Column(name = "CURP_ENTREGA")
    private String curpEntrega;

    @Column(name = "NOMBRE_COMPLETO_ENTREGA")
    private String nombreCompletoEntrega;

    @Column(name = "CURP_AUTORIZA")
    private String curpAutoriza;

    @Column(name = "NOMBRE_COMPLETO_AUTORIZA")
    private String nombreCompletoAutoriza;

    @Column(name = "NOTA_RECHAZO")
    private String notaRechazo;

    @Column(name = "CVE_DEPENDENCIA")
    private String cveDependencia;

    @Column(name = "DESC_DEPENDENCIA")
    private String nombreDependencia;

    @Column(name = "CVE_DEPENDENCIA_RESPONSABLE")
    private String cveDependenciaResponsable;

    @Column(name = "DESC_DEPENDENCIA_RESPONSABLE")
    private String nombreDependenciaResponsable;

    @Transient
    private String fechaAceptadaCortaText;

    public String getFechaAceptadaCortaText() {
        if (this.fechaAcepto!=null) {
            Date date = new Date(this.fechaAcepto.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaAceptadaCortaText = dateFormat.format(date);
            return this.fechaAceptadaCortaText;
        }
        else
            return null;
    }

    @Transient
    private String fechaAceptadaText;

    public String getFechaAceptadaText() {
        if (this.fechaAcepto != null) {
            Date date = new Date(this.fechaAcepto.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMM");
            SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy");
            this.fechaAceptadaText = dateFormat.format(date) + " de " + dateFormat2.format(date) + " del " + dateFormat3.format(date);
            return this.fechaAceptadaText;
        } else
            return null;
    }

    @Transient
    private String fechaActualText;

    public String getFechaActualText() {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMM");
            SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy");
            this.fechaActualText = dateFormat.format(date) + " de " + dateFormat2.format(date) + " del " + dateFormat3.format(date);
            return this.fechaActualText;
    }

    @Column(name = "FECHA_RECHAZO")
    private Timestamp fechaRechazo;

    @Column(name = "FECHA_ACEPTADA_FIJA")
    private String fechaAceptadaFija;

    @Column(name = "TIPO_SERVICIO")
    private String tipoServicio;

    @Column(name = "SUBTIPO_SERVICIO")
    private String subtipoServicio;

    @Column(name = "FIRMADA")
    private String firmada;

    @Column(name = "CELULAR")
    private String celular;

    @Column(name = "ID_LOGO")
    private Long idLogo;

    @Column(name = "NOMBRE_IMAGEN")
    private String nombreLogo;

    @Column(name = "RUTA_IMAGEN")
    private String rutaLogo;

}
