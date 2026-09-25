package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Table
@Entity(name = "PLANTILLAS_CAMPOS_CARGA_DATOS")
public class Campos implements Serializable {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "idPlantilla", column = @Column(name = "ID_PLANTILLA_CARGA_DATOS")),
            @AttributeOverride(name = "numCampo", column = @Column(name = "NUM_CAMPO_PLANTILLA"))
    })
    private CamposPK camposPK;

    @ManyToOne
    @JoinColumn(name = "ID_PLANTILLA_CARGA_DATOS" , insertable = false, updatable = false)
    private Plantilla plantilla;

    @Column(name="DESC_CAMPO_PLANTILLA")
    private String nombreCampo;

    @Column(name="DESC_CAMPO_PLANTILLA_ABREV")
    private String nombreCampoAbr;

    @Column(name="UBICACION_COLUMNA")
    private String ubicacionColumna;

    @Column(name="ETIQUETA_COLUMNA")
    private String etiquetaColumna;

    @Column(name="NOMBRE_CAMPO_BASE_DATOS")
    private String campoBaseDatos;

    @ManyToOne
    @JoinColumn(name = "CVE_TIPO_CAMPO_PLANTILLA")
    private TipoCampo tipoCampo;

    @Column(name="ES_CAMPO_OBLIGATORIO")
    private String obligatorio;

    @Column(name="LONGITUD_CAMPO_PLANTILLA")
    private Integer longitud;

    @Column(name="LONGITUD_DECIMAL_CAMPO")
    private Integer longitudDecimal;

    @Column(name="ID_TIPO_CAMPO_MASCARA")
    private Integer idTipoCampoMascara;

    @Column(name="FORMATO_MASCARA_CAMPO")
    private String mascara;

    @Column(name="PK_NUM_POSICION_CAMPO")
    private Integer pkNumPosicionCampo;

    @Column(name="PK_FK_TABLA")
    private String pkFkTabla;

    @Column(name="PK_FK_COLUMNA")
    private String pkFkColumna;

    @Column(name="OMITIR_TEXTO_EN_VALIDACION")
    private String omitirTextValiacion;

    @Column(name="FK_NUM_POSICION_COLUMNA")
    private Integer fkNumPosicionColumna;

    @Column(name="FK_TABLA")
    private String fkTabla;

    @Column(name="FK_COLUMNA")
    private String fkColumna;

    @Column(name="NUM_ORDEN_CAMPO_PLANTILLA")
    private Integer numOrdenCampo;

    @Column(name="FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column(name="FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaCambio;

    @Column(name="CVE_ESTATUS")
    private String cveEstatus;

    @Transient
    private Long idPlantillaCampoTemp;

    @Transient
    private Integer numCampo;

    @Transient
    private String cveTipoCampo;

}
