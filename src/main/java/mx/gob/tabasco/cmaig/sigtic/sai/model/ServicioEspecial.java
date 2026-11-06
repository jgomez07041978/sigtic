package mx.gob.tabasco.cmaig.sigtic.sai.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "SERV_ESPECIALES_CTA_MAESTRA")
public class ServicioEspecial implements Serializable {

    @EmbeddedId
    private ServicioEspecialId id;

    @ManyToOne
    @MapsId("numCuentaMaestra")
    @JoinColumn(name = "NUM_CUENTA_MAESTRA")
    private CatCuentaMaestra catCuentaMaestra;

    @ManyToOne
    @JoinColumn(name = "ID_ENTE_PUBLICO_SERV_ESP")
    private EntePublico entePublico;

    @ManyToOne
    @JoinColumn(name = "ID_UNIDAD_ADMTVA_SERV_ESP")
    private UnidadAdministrativa unidadAdministrativa;

    @ManyToOne
    @JoinColumn(name = "CVE_TIPO_CONEXION_LINEA_TELEF")
    private CatTipoConexionLineaTelef catTipoConexionLineaTelef;

    @Column(name = "PERIODO_SERVICIO")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodo;

    @Column(name = "CVE_PARTIDA")
    private String cvePartida;

    @Column(name = "CODIGO_ARTICULO_EBS")
    private String codigoEbs;

    @Column(name = "CANTIDAD")
    private Integer cantidad;

    @Column(name = "CVE_UNIDAD_MEDIDA")
    private String cveUnidadMedida;

    @Column(name = "PRECIO_CON_IVA")
    private BigDecimal precioConIva;

    @Column(name = "TOTAL")
    private BigDecimal total;

    @Column(name = "NUM_OFICIO")
    private String numOficio;

    @Column(name = "DESCRIPCION_ABREVIADA")
    private String descripcionAbreviada;

    @Lob
    @Column(name = "DESCRIPCION_SERVICIO")
    private String descripcion;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

    @Column(name = "ESTATUS_VALIDACION")
    private String estatusValidacion;

    // Igual a NumTelefonoCuentaMaestra
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
    private Timestamp fechaUltimoCambio;

    public Integer getNoLote() {
        return this.id != null ? this.id.getNoLote() : null;
    }

    public void setNoLote(Integer noLote) {
        if (this.id == null) this.id = new ServicioEspecialId();
        this.id.setNoLote(noLote);
    }
}