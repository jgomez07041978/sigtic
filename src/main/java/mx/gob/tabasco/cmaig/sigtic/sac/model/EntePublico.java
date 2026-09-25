package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="CAT_ENTES_PUBLICOS")
public class EntePublico implements Serializable {

    @Id
    @Column(name = "ID_ENTE_PUBLICO")
    private Long idEntePublico;

    @Column(name = "ID_ENTE_PUBLICO_PADRE", nullable = true)
    private Integer idEntePublicoPadre;

    @Column(name = "CVE_ENTE_PUBLICO", nullable = true)
    private String cveEntePublico;

    @Column(name = "CVE_ENTE_PUBLICO2", nullable = true)
    private String cveEntePublico2;

    @Column(name = "CVE_ENTE_PUBLICO3", nullable = true)
    private String cveEntePublico3;

    @Column(name = "DESC_ENTE_PUBLICO")
    private String nombreEntePublico;

    @Transient
    private String nombreEntePublicoConCve;

    @Column(name = "DESC_ENTE_PUBLICO_ABREVIADA", nullable = true)
    private String nombreEntePublicoAbreviada;

    @Column(name = "DESC_ENTE_PUBLICO_ABREVIADA2", nullable = true)
    private String nombreEntePublicoAbreviada2;

    @OneToOne
    @JoinColumn(name = "CVE_TIPO_ENTE_PUBLICO")
    private TipoEntePublico tipoEntePublico;


    @Column(name = "ID_DOMICILIO_ENTE_PUBLICO", nullable = true)
    private Integer idDomicilioEntePublico;

    @Column(name = "RFC_TITULAR_ENTE_PUBLICO", nullable = true)
    private String rfcTitular;

    @Column(name = "DESC_CARGO_TITULAR_ENTEPUBLICO", nullable = true)
    private String nombreCargoTitular;

    @Column(name = "DESC_CATEGORIA_TITULAR_ENTEPUB", nullable = true)
    private String nombreCategoriaTitular;

    @Column(name = "NUM_TELEFONO_ENTE_PUBLICO", nullable = true)
    private String numTelefonoEntePublico;

    @Column(name = "NUM_EXTENSION_ENTE_PUBLICO", nullable = true)
    private String numExtensionEntePublico;

    @Column(name = "CORREO_ENTE_PUBLICO", nullable = true)
    private String correoEntePublico;

    @Column(name = "NUM_ORDEN_ENTE_PUBLICO", nullable = true)
    private Integer numOrdenEntePublico;

    @Column(name = "ID_COLOR_ENTE_PUBLICO", nullable = true)
    private Integer idColorEntePublico;

    @Column(name = "ID_IMAGEN_ENTE_PUBLICO", nullable = true)
    private Integer idImagenEntePublico;

    @Column(name = "IP_PC_MTTO", nullable = true)
    private String ipPC;

    @Column(name = "NOMBRE_PC_MTTO", nullable = true)
    private String nombrePC;

    @Column(name = "CVE_USUARIO_SO_MTTO", nullable = true)
    private String usuarioSO;

    @Column(name = "CVE_USUARIO_MTTO", nullable = true)
    private String cveUsuarioMtto;

    @Column(name = "NOMBRE_APLICACION_MTTO", nullable = true)
    private String nombreAplicacion;

    @Column(name = "FECHA_REGISTRO", nullable = true)
    private Timestamp fechaRegistro;

    @Column(name = "FECHA_ULTIMO_CAMBIO", nullable = true)
    private Timestamp fechaCambio;

    @Column(name = "CVE_ESTATUS", nullable = true)
    private String cveEstatus;

    public String getNombreEntePublicoConCve() {
        if (this.nombreEntePublico!=null)
            return this.tipoEntePublico.getNombreTipoEntePublico()+" - "+this.nombreEntePublico;
        else
            return "";
    }

    @Transient
    private String term;


}
