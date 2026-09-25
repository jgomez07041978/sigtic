package mx.gob.tabasco.cmaig.sigtic.sai.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name = "NUM_TELEFONOS_CUENTA_MAESTRA")
public class NumTelefonoCuentaMaestra implements Serializable {

    @EmbeddedId
    private NumTelefonoCuentaMaestraId id;

    @ManyToOne
    @MapsId("numCuentaMaestra")
    @JoinColumn(name = "NUM_CUENTA_MAESTRA")
    private CatCuentaMaestra catCuentaMaestra;

    @ManyToOne
    @JoinColumn(name = "ID_ENTE_PUBLICO_LINEA_TELEFONO")
    private EntePublico entePublico;

    @ManyToOne
    @JoinColumn(name = "ID_UNIDAD_ADMTVA_LINEATELEFONO")
    private UnidadAdministrativa unidadAdministrativa;

    @ManyToOne
    @JoinColumn(name = "ID_UBICACION_UA_LINEA_TELEFONO", nullable = true)
    private UbicacionUnidadAdmin ubicacionUnidadAdmin;

    @Column(name = "CURP_USUARIO_CONTACTO_TELEFONO")
    private String curpUsuarioContacto;

    @ManyToOne
    @JoinColumn(name = "CVE_TIPO_CONEXION_LINEA_TELEF")
    private CatTipoConexionLineaTelef catTipoConexionLineaTelef;

    @ManyToOne
    @JoinColumn(name = "ID_CONCEPTO_CUENTA_MAESTRA", insertable = false, updatable = false)
    private CatConceptoCuentaMaestra catConceptoCuentaMaestra;

    @Column(name = "ID_CONCEPTO_CUENTA_MAESTRA")
    private Long idConceptoCuentaMaestra;

    @Column(name = "ANCHO_BANDA_INTERNET_EN_MB")
    private Integer anchoBandaInternetMb;

    @Column(name = "FECHA_ALTA_LINEA_TELEFONO")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp fechaAlta;

    @Transient
    private String fechaAltaText;

    public String getFechaAltaText() {
        if (this.fechaAlta != null) {
            Date date = new Date(this.fechaAlta.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaAltaText = dateFormat.format(date);
            return this.fechaAltaText;
        } else
            return null;
    }

    @Column(name = "FECHA_BAJA_LINEA_TELEFONO")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp fechaBaja;

    @Transient
    private String fechaBajaText;

    public String getFechaBajaText() {
        if (this.fechaBaja != null) {
            Date date = new Date(this.fechaBaja.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaBajaText = dateFormat.format(date);
            return this.fechaBajaText;
        } else
            return null;
    }

    @Column(name = "OBSERVACIONES_LINEA_TELEFONO")
    private String observaciones;

    @Column(name = "ULTIMO_PERIODO_FACTURACION")
    private Integer ultimoPeriodoFacturacion;

    @Column(name = "DESC_DOMICILIO_TELMEX")
    private String descDomicilioTelmex;

    @Column(name = "FECHA_CORTE")
    private Timestamp fechaCorte;

    @Column(name = "ID_CARGA_DATOS_X_PLANTILLA")
    private Long idCargaDatosXPlantilla;

    @Column(name = "CURP_USUARIO_VALIDA_TELEFONO")
    private String curpUsuarioValidaTelefono;

    @Column(name = "DESC_VALIDACION_TELEFONO")
    private String descValidacionTelefono;

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

    @Column(name = "CVE_ESTATUS", nullable = true)
    private String cveEstatus;

    @Column(name = "ESTATUS_VALIDACION", nullable = true)
    private String estatusValidacion;

    @Transient
    public String getNumTelefono() {
        return this.id != null ? this.id.getNumTelefono() : null;
    }

    public void setNumTelefono(String numTelefono) {
        if (this.id == null) this.id = new NumTelefonoCuentaMaestraId();
        this.id.setNumTelefono(numTelefono);
    }
}
