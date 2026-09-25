package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="CAT_UBICACIONES_UNIDAD_ADMTVA")
public class UbicacionUnidadAdmin implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_UBICACION_ADMTVA_APP")
    @SequenceGenerator(sequenceName = "SEQ_UBICACION_UNIDAD_ADMTVA", allocationSize = 1, name = "SEQ_UBICACION_ADMTVA_APP")
    @Column(name = "ID_UBICACION_UNIDAD_ADMTVA")
    private Long idUbicacionAdaministrativa;

    @Column(name = "DESC_UBICACION_UNIDAD_ADMTVA")
    private String nombreUbicacion;

    @Column(name = "DESC_UBICACION_UNIDAD_ADMTVA_A")
    private String nombreUbicacionAbreviada;

    @Column(name = "NUM_TELEFONO_UNIDAD_ADMTVA")
    private String numTelefono;

    @Column(name = "NUM_EXTENSION_UNIDAD_ADMTVA")
    private String extension;

    @Column(name = "REFERENCIA_UBICACION_UNIDADADM")
    private String referencia;

    @ManyToOne
    @JoinColumn(name="ID_DOMICILIO_UNIDAD_ADMTVA", nullable = true)
    private DomicilioEntePublico domicilioEntePublico;

    @Column(name = "CORREO_UNIDAD_ADMINISTRATIVA")
    private String correoUnidad;

    @Column(name = "NUM_ORDEN_UBICACION_UNIDAD_ADM")
    private Integer numOrden;

    @Column(name = "CVE_USUARIO_MTTO", nullable = true)
    private String cveUsuarioMtto;

    @Column(name = "NOMBRE_APLICACION_MTTO", nullable = true)
    private String nombreAplicacion;

    @Column(name = "FECHA_REGISTRO", nullable = true)
    private Timestamp fechaRegistro;

    @Column(name = "FECHA_ULTIMO_CAMBIO", nullable = true)
    private Timestamp fechaUltimoCambio;

    @Column(name = "UBICACION_TITULAR_ENTE_PUBLICO", nullable = true)
    private String ubicacionTitularEntePublico;

    @Column(name = "CVE_ESTATUS", nullable = true)
    private String cveEstatus;

}
