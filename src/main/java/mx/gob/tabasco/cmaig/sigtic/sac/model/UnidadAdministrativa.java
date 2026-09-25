package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "CAT_UNIDADES_ADMINISTRATIVAS")
public class UnidadAdministrativa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_UNIDAD_ADMIN_APP")
    @SequenceGenerator(sequenceName = "SEG_CAT_UNI_ADMIN", allocationSize = 1, name = "SEQ_UNIDAD_ADMIN_APP")
    @Column(name = "ID_UNIDAD_ADMINISTRATIVA")
    private Long idUnidadAdministrativa;

    @Column(name = "ID_UNIDAD_ADMINISTRATIVA_PADRE", nullable = true)
    private Long idUnidadAdministrativaPadre;

    @Column(name = "ID_ENTE_PUBLICO", nullable = false)
    private Long idEntePublico;

    @Column(name = "CVE_UNIDAD_ADMINISTRATIVA", nullable = true)
    private String cveUnidadAdministrativa;

    @Column(name = "CVE_UNIDAD_ADMINISTRATIVAPADRE", nullable = true)
    private String cveUnidadAdministrativaPadre;

    @Column(name = "CVE_UNIDAD_ADMINISTRATIVA2", nullable = true)
    private String cveUnidadAdministrativa2;

    @Column(name = "CVE_UNIDAD_ADMINISTRATIVA3", nullable = true)
    private String cveUnidadAdministrativa3;

    @Column(name = "DESC_UNIDAD_ADMINISTRATIVA", nullable = false)
    private String nombreUnidadAdministrativa;

    @Column(name = "DESC_UNIDAD_ADMINISTRATIVA_ABR", nullable = false)
    private String nombreUnidadAdministrativaAbreviada;

    @Column(name = "DESC_UNIDAD_ADMINISTRATIVA_AB2", nullable = false)
    private String nombreUnidadAdministrativaAbreviada2;

    @Column(name = "CVE_TIPO_UNIDAD_ADMINISTRATIVA", nullable = true)
    private Integer tipoUnidadAdministrativa;

/*    @Column(name = "ID_NIVEL_UNIDADADMINISTRATIVA", nullable = true)
    private Integer nivelUnidadAdministrativa;*/

    @ManyToOne
    @JoinColumn(name="ID_NIVEL_UNIDADADMINISTRATIVA", nullable = true)
    private NivelUnidadAdministrativa nivelUnidadAdministrativa;


    @Column(name = "ID_UBICACION_UNIDAD_ADMTRATIVA", nullable = true)
    private Long idUbicacionUnidadAdministrativa;

    @Column(name = "NUM_NIVEL_ORGANIGRAMA", nullable = true)
    private Integer nivelOrganigrama;

    @Column(name = "NUM_ORDEN_ORGANIGRAMA", nullable = true)
    private Integer ordenOrganigrama;

    @Column(name = "DESC_CARGO_UNIDAD_ADMTVA_ORG", nullable = true)
    private String nombreCargoUnidad;

    @Column(name = "DESC_CATEGORIA_UNIDAD_ADMTVA_O", nullable = true)
    private String nombreCategoriaUnidad;

    @Column(name = "NUM_TELEFONO_UNIDAD_ADMTVA", nullable = true)
    private String telefono;

    @Column(name = "NUM_EXTENSION_UNIDAD_ADMTVA", nullable = true)
    private String numeroExtension;

    @Column(name = "CORREO_UNIDAD_ADMTVA", nullable = true)
    private String correoUnidadAdministrativa;

    @Column(name = "RFC_TITULAR_UNIDAD_ADMTVA", nullable = true)
    private String rfcTitular;

    @Column(name = "NUM_ORDEN_UNIDADADMINISTRATIVA", nullable = true)
    private String numeroOrden;

    @Column(name = "ID_COLOR_UNIDAD_ADMINISTRATIVA", nullable = true)
    private String idColor;

    @Column(name = "ID_IMAGEN_UNIDADADMINISTRATIVA", nullable = true)
    private String idImagen;

    @Column(name = "IP_PC_MTTO", nullable = true)
    private String ipPc;

    @Column(name = "NOMBRE_PC_MTTO", nullable = true)
    private String nombrePc;

    @Column(name = "CVE_USUARIO_SO_MTTO", nullable = true)
    private String usuarioSo;

    @Column(name = "CVE_USUARIO_MTTO", nullable = true)
    private String usuarioMtto;

    @Column(name = "NOMBRE_APLICACION_MTTO", nullable = true)
    private String aplicacion;

    @Column(name = "FECHA_REGISTRO", nullable = true)
    private Timestamp fechaRegistro;

    @Column(name = "FECHA_ULTIMO_CAMBIO", nullable = true)
    private Timestamp fechaUltimo;

    @Column(name = "CVE_ESTATUS", nullable = true)
    private String estatus;

    @Column(name = "ES_UNIDAD_ADMINISTRATIVA_PPTAL", nullable = true)
    private String unidadPresupuestal;

    @Column(name = "REGLAMENTO_INTERIOR", nullable = true)
    private String reglamentoInterior;

    @Column(name = "ID_LOGO", nullable = true)
    private Integer idLogo;

    @Transient
    private Long idDomicilioEntePublico;

    @Transient
    private String nombreTitular;

    @Transient
    private String nombreUnidadAdministrativaPadre;

    @Transient
    private String correoSearch;

    @Transient
    private Integer idTipoCorreo;

    @Transient
    private String estatusCorreo;

    @Transient
    private Boolean isAdministra;

    @Transient
    private Boolean isOpera;

}
