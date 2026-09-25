package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="SERVICIOS_TIC")
public class ServiciosTic {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "SEQ_SERVICIO_TIC")
    @SequenceGenerator(sequenceName = "SEQ_SERVICIOS_TIC", allocationSize = 1, name = "SEQ_SERVICIO_TIC")
    @Column (name = "ID_SERVICIO_TIC")
    private Long idServicioTic;

    @Column (name="CVE_SERVICIO_TIC")
    private String cveServicioTic;

    @Column (name = "NOMBRE_SERVICIO_TIC")
    private String nombreServicioTic;

    @Column (name="DESC_SERVICIO_TIC")
    private String servicioTicDescripcion;

    @Column (name="DESC_SERVICIO_TIC_ABREVIADO")
    private String servicioTicAbreviadoDescripcion;

    @Column (name="ALCANCE_SERVICIO_TIC")
    private String alcanceServicioTic;

    @Column (name="OBS_SERVICIO_TIC")
    private String obsServicioTic;

    @ManyToOne
    @JoinColumn (name="CVE_ESTATUS_SERVICIO_TIC")
    private CatEstatusServiciosTic estatusServicioTic;

    @Column (name="NUM_ORDEN_SERVICIO_TIC")
    private Long numOrdenServicioTic;

    @ManyToOne
    @JoinColumn (name="ID_TIPO_SERVICIO_TIC")
    private CatTipoServiciosTic tipoServicio;

    @ManyToOne
    @JoinColumn (name="ID_SUBTIPO_SERVICIO_TIC")
    private CatSubtipoServiciosTic subTipoServicio;

    @Column (name="CVE_ESTATUS")
    private String cveEstatus;

    @ManyToOne
    @JoinColumn (name="CVE_ESTADO_SERVICIO_TIC")
    private CatEstadosServiciosTic estadosServicioTic;

    @ManyToOne
    @JoinColumn (name = "CVE_TIPO_IMPACTO_SERVICIO_TIC")
    private CatTipoImpactoServiciosTic tipoImpactoServicioTic;

    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO_ADM_SERV_TIC",  nullable=true)
    private EntePublico entePublicoAdmin;

    @ManyToOne
    @JoinColumn(name="ID_UNIDAD_ADMTVA_ADM_SERV_TIC", nullable = true)
    private UnidadAdministrativa unidadAdministrativaAdmin;

    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO_OPERA_SERV_TIC",  nullable=true)
    private EntePublico entePublicoOpera;

    @ManyToOne
    @JoinColumn(name="ID_UNIDAD_ADMTVA_OPERA_SERVTIC", nullable = true)
    private UnidadAdministrativa unidadAdministrativaOpera;

    @ManyToOne
    @JoinColumn(name="ID_UBICACION_UA_ADM_SERV_TIC", nullable = true)
    private UbicacionUnidadAdmin ubicacionUnidadAdmin;

    @ManyToOne
    @JoinColumn(name="ID_UBICACION_UA_OPERA_SERV_TIC", nullable = true)
    private UbicacionUnidadAdmin ubicacionUnidadOpera;

    @Column(name = "FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;


    @Column(name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Transient
    private Integer tipo;

    @Column (name="NUM_INVENTARIO_GET")
    private String numInventarioGet;

    @Column (name="NUM_INVENTARIO_CI")
    private String numInventarioCi;

    @Transient
    private String clasificacion;

    @Transient
    private boolean sinFiltrar;

    @Column (name="CURP")
    private String curp;

    @Column (name = "ID_USUARIO_CAPTURO")
    private Long idUsuarioCapturo;

    @Transient
    private Usuario usuarioCapturo;

    @Column(name = "IP_PC_MTTO", nullable = true)
    private String ipPC;

    @Column(name = "NOMBRE_PC_MTTO", nullable = true)
    private String nombrePC;

    @Column(name = "CVE_USUARIO_MTTO", nullable = true)
    private String cveUsuarioMtto;

    @Transient
    private Integer pagina;

    @Transient
    private Integer size;
}