package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "CAT_CONCEPTOS_CUENTA_MAESTRA")
public class CatConceptoCuentaMaestra implements Serializable {

    @Id
    @Column(name = "ID_CONCEPTO_CUENTA_MAESTRA")
    private Long idConceptoCuentaMaestra;

    @Column(name = "CVE_CONCEPTO_CUENTA_MAESTRA")
    private String cveConceptoCuentaMaestra;

    @Column(name = "NUM_GRUPO_CONCEPTO_CTA_MAESTRA")
    private Integer numeroGrupoConcepto;

    @Column(name = "DESC_GRUPO_CONCEPTO_CTAMAESTRA")
    private String nombreGrupoConcepto;

    @Column(name = "DESC_SUBGRUPO_CONCEPTO_CTAMTRA")
    private String nombreSubgrupoConcepto;

    @Column(name = "DESC_CONCEPTO_CUENTA_MAESTRA")
    private String nombreConceptoCuentaMaestra;

    @Column(name = "DESC_CONCEPTO_CUENTA_MAESTRA_A")
    private String nombreConceptoCuentaMaestraAbr;

    @Column(name = "DESC_UNIDAD_MEDIDA_CONCEPTO_CM")
    private String descUnidadMedidaConcepto;

    @Column(name = "CVE_IVA_CONCEPTO_CTA_MAESTRA")
    private Integer cveIvaConcepto;

    @Column(name = "CVE_AJUSTE_CONCEPTO_CTAMAESTRA")
    private String cveAjusteConcepto;

    @Column(name = "CVE_REGISTRO_CUENTA_MAESTRA")
    private Integer cveRegistroCuentaMaestra;

    @Column(name = "TIPO_RED_CONCEPTO_CTA_MAESTRA")
    private String tipoRedConcepto;

    @Column(name = "ES_CONCEPTO_CARGO_O_REMBOLSO")
    private String esConceptoCargoOReembolso;

    @Column(name = "CVE_USO_LINEA_TELEFONO")
    private String cveUsoLineaTelefono;

    @Column(name = "NUM_ORDEN_CONCEPTO_CTA_MAESTRA")
    private Integer numeroOrden;

    @Column(name = "ID_COLOR_CONCEPTO_CTA_MAESTRA")
    private Integer idColor;

    @Column(name = "ID_IMAGEN_CONCEPTO_CTA_MAESTRA")
    private Integer idImagen;

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
}
