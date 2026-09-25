package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="CAT_DOMICILIOS_ENTES_PUBLICOS")
public class DomicilioEntePublico  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DOMICILIO_ENTE_APP")
    @SequenceGenerator(sequenceName = "SEQ_ID_DOMICILIO_ENTE_PUBLICO", allocationSize = 1, name = "SEQ_DOMICILIO_ENTE_APP")
    @Column(name = "ID_DOMICILIO_ENTE_PUBLICO")
    private Long idDomicilioEntePublico;

    @Column(name = "ID_ENTIDAD_FEDERATIVA_ENTE_PUB")
    private Integer idEntidadFederativa;

    @Column(name = "ID_MUNICIPIO_ENTE_PUBLICO")
    private Integer idMunicipio;

    @Column(name = "ID_LOCALIDAD_ENTE_PUBLICO")
    private Integer idLocalidad;

    @Column(name = "ID_CODIGO_POSTAL_LOCALIDAD_E_P")
    private Integer idCodigoPostal;

    @Column(name = "ID_TIPO_VIALIDAD_ENTE_PUBLICO")
    private Integer idTipoVialidad;

    @Column(name = "ID_TIPO_ASENTAMIENTO_ENTE_PUB")
    private Integer idTipoAsentamiento;

    @Column(name = "DESC_DOMICILIO_ENTE_PUBLICO", nullable = true)
    private String nombreDomicilio;

    @Column(name = "DESC_DOMICILIO_ENTE_PUBLICO_A", nullable = true)
    private String nombreDomicilioAbrev;

    @Column(name = "CALLE_ENTE_PUBLICO", nullable = true)
    private String calle;

    @Column(name = "NUM_EXTERIOR_ENTE_PUBLICO", nullable = true)
    private String numExterior;

    @Column(name = "NUM_INTERIOR_ENTE_PUBLICO", nullable = true)
    private String numInterior;

    @Column(name = "COLONIA_ENTE_PUBLICO", nullable = true)
    private String colonia;

    @Column(name = "NUM_TELEFONO_ENTE_PUBLICO", nullable = true)
    private String numTelefono;

    @Column(name = "NUM_EXTENSION_ENTE_PUBLICO", nullable = true)
    private String numExtension;

    @Column(name = "REFERENCIA_DOMICILIO_ENTE_PUB", nullable = true)
    private String referenciaDomicilio;

    @Column(name = "NUM_ORDEN_DOMICILIO_ENTE_PUB", nullable = true)
    private Integer numOrden;

    @Column(name = "CVE_USUARIO_MTTO", nullable = true)
    private String cveUsuarioMtto;

    @Column(name = "FECHA_ULTIMO_CAMBIO", nullable = true)
    private Timestamp fechaUltimoCambio;

    @Column(name = "CVE_ESTATUS", nullable = true)
    private String cveEstatus;

    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO", nullable = true, insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private EntePublico entePublico;

    @Column(name = "ID_ENTE_PUBLICO", nullable = true)
    private Long idEntePublicoRaw;

    @Column(name = "REFERENCIA_ENTRE_CALLE", nullable = true)
    private String referenciaEntreCalle;

    @Column(name = "REFERENCIA_CALLE_POSTERIOR", nullable = true)
    private String referenciaCallePosterior;

    @Column(name = "CODIGO_POSTAL_LOCALIDAD_E_P", nullable = true)
    private String codigoPostalLocalidadEp;

    @Column(name = "LATITUD_DECIMAL", nullable = true)
    private BigDecimal latitud;

    @Column(name = "LONGITUD_DECIMAL", nullable = true)
    private BigDecimal longitud;
}
