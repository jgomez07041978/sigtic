package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="CAT_SISTEMAS")
public class CatSistema implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SISTEMA_APP")
    @SequenceGenerator(sequenceName = "SEQ_CAT_SISTEMAS", allocationSize = 1, name = "SEQ_SISTEMA_APP")
    @Column(name = "ID_SISTEMA")
    private Long idSistema;

    @ManyToOne(optional = true)
    @JoinColumn(name="ID_SISTEMA_PADRE", nullable = true)
    private CatSistema catSistemaPadre;

    @Column(name = "CODIGO_SISTEMA")
    private String codigoSistema;

    @Column(name = "DESC_SISTEMA")
    private String nombreSistema;

    @Column(name = "DESC_SISTEMA_ABREVIADO")
    private String nombreSistemaAbreviado;

    @Column(name = "ALCANCE_SISTEMA")
    private String alcanceSistema;

    @Column(name = "CVE_MEJORA_REGULATORIA")
    private String cveMejoraRegulatoria;

    @ManyToOne
    @JoinColumn(name = "CVE_TIPO_SISTEMA")
    private CatTipoSistema catTipoSistema;

    @Column(name = "NUM_INVENTARIO_GET")
    private String numeroInventarioGet;

    @Column(name = "NUM_INVENTARIO_ENTE_PUBLICO")
    private String numeroInventarioEntePublico;

    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO_ADMIN_SISTEMA", nullable = false)
    private EntePublico entePublicoAdmin;

    @ManyToOne
    @JoinColumn(name="ID_UNIDAD_ADMTVA_ADMIN_SISTEMA", nullable = false)
    private UnidadAdministrativa unidadAdministrativaAdmin;

    @ManyToOne(optional = true)
    @JoinColumn(name="ID_ENTE_PUBLICO_OPERA_SISTEMA",  nullable=true)
    private EntePublico entePublicoOpera;

    @ManyToOne(optional = true)
    @JoinColumn(name="ID_UNIDAD_ADMTVA_OPERA_SISTEMA", nullable=true)
    private UnidadAdministrativa unidadAdministrativaOpera;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

    @ManyToOne
    @JoinColumn(name = "CVE_ESTATUS_SISTEMA")
    private CatEstatusSistema catEstatusSistema;

    @Column(name = "NUM_ORDEN_SISTEMA")
    private Integer numeroOrden;

    @Transient
    private String codigoNombreSistema;

    @Transient
    private Integer totalSistemaHijos;

    public String getCodigoNombreSistema() {
        return this.codigoSistema+") "+ this.nombreSistema;
    }
}
