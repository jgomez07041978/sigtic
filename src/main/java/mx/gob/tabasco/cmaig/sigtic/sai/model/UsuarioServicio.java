package mx.gob.tabasco.cmaig.sigtic.sai.model;


import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name="USUARIOS_SERVICIOS_TIC")
public class UsuarioServicio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO_SERVICIO_TIC")
    @SequenceGenerator(sequenceName = "SEQ_USUARIOS_SERVICIOS_TIC", allocationSize = 1, name = "SEQ_USUARIO_SERVICIO_TIC")
    @Column (name = "ID_USUARIO_SERVICIO_TIC")
    private Long idUsuarioServicio;

    @ManyToOne
    @JoinColumn(name="ID_TIPO_USUARIO_SERVICIO_TIC")
    private CatTipoUsuarioServicio catTipoUsuarioServicio;

    @ManyToOne
    @JoinColumn(name="ID_SERVICIO_TIC")
    private ServiciosTic serviciosTic;

    @Column (name = "CURP_USUARIO_SERVICIO_TIC")
    private String curpUsuarioServicio;

    @Column (name = "NUM_FOLIO_SOLICITUD_SIMA", nullable = true)
    private Long folioSima;

    @Column (name = "FECHA_SOLICITUD_SIMA", nullable = true)
    private Timestamp fechaSolicitudSima;

    @Column (name = "CURP_TECNICO_SERVICIO_TIC")
    private String curpTenicoServicio;

    @Column (name = "FECHA_ALTA_SERVICIO_TIC", nullable = true)
    private Timestamp fechaAltaServicio;

    @Column (name = "FECHA_BAJA_SERVICIO_TIC", nullable = true)
    private Timestamp fechaBajaServicio;

    @Column (name = "FECHA_ULTIMOCAMBIO_SERVICIOTIC", nullable = true)
    private Timestamp fechaUltimoCambioServicio;

    @Column (name = "ALCANCE_SERVICIO_TIC", nullable = true)
    private String alcanceServicio;

    @Column (name = "OBS_SERVICIO_TIC", nullable = true)
    private String observacionServicio;

    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO_USUARIO_S_TIC")
    private EntePublico entePublicoUsuarioServicio;

    @ManyToOne
    @JoinColumn(name="ID_UNIDAD_ADMTVA_USUARIO_S_TIC", nullable = true)
    private UnidadAdministrativa unidadAdministrativaUsuarioServicio;

    @ManyToOne
    @JoinColumn(name="ID_UBICACION_USUARIO_SERV_TIC", nullable = true)
    private UbicacionUnidadAdmin ubicacionUnidadAdmin;

    @Column (name = "CVE_ESTATUS_USUARIO_SERV_TIC")
    private String cveEstatusUsuarioServicio;

    @Column (name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column (name = "FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

    @Column(name = "IP_PC_MTTO", nullable = true)
    private String ipPC;

    @Column(name = "NOMBRE_PC_MTTO", nullable = true)
    private String nombrePC;

    @Column(name = "CVE_USUARIO_MTTO", nullable = true)
    private String cveUsuarioMtto;

    @Column(name = "NOMBRE_APLICACION_MTTO", nullable = true)
    private String nombreAppMtto;

    @Column (name = "CVE_ESTATUS")
    private String cveEstatus;

    @ManyToOne
    @JoinColumn(name="ID_ESTATUS_SERVICIO", nullable = true)
    private CatEstatusServicio catEstatusServicio;

    /*@Column (name="ID_USUARIO_ENTE_PUBLICO")
    private Long idUsuarioEntePublico;*/

    @ManyToOne
    @JoinColumn(name="ID_USUARIO_ENTE_PUBLICO", nullable = true)
    private CatUsuarioEntePublico catUsuarioEntePublico;


   @Column(name = "NOTA_RECHAZO", nullable = true)
    private String notaRechazo;

   @Column(name = "ID_CARTA_RESPONSIVA_DETALLE", nullable = true)
    private Long idCartaRespondivaDetalle;

    @Column (name = "CORREO_INSTITUCIONAL_USUARIO", nullable = true)
    private String correoInstitucionalUsuario;

    @Column (name = "CORREO_PERSONAL_USUARIO", nullable = true)
    private String correoPersonalUsuario;

    @Transient
    private String fechaAltaServicioText;

    @Transient
    private String fechaBajaServicioText;

    public String getFechaAltaServicioText() {
        if (this.fechaAltaServicio!=null) {
            Date date = new Date(this.fechaAltaServicio.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaAltaServicioText = dateFormat.format(date);
            return this.fechaAltaServicioText;
        }
        else
            return null;
    }

    public String getFechaBajaServicioText() {
        if (this.fechaBajaServicio!=null) {
            Date date = new Date(this.fechaBajaServicio.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaBajaServicioText = dateFormat.format(date);
            return this.fechaBajaServicioText;
        }
        else
            return null;
    }

    @Transient
    private String fechaSolicitudSimaText;

    public String getFechaSolicitudSimaText() {
        if (this.fechaSolicitudSima != null) {
            Date date = new Date(this.fechaSolicitudSima.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaSolicitudSimaText = dateFormat.format(date);
            return this.fechaSolicitudSimaText;
        }
        else
            return null;
    }

    @Transient
    private String nombreCompletoFallback;

    @Transient
    private String rfcFallback;



}
