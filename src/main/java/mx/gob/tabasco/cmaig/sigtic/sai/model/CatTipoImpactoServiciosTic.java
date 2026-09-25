package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
@Data
@Entity
@Table(name = "CAT_TIPO_IMPACTO_SERVICIOS_TIC")
public class CatTipoImpactoServiciosTic implements Serializable {

    @Id
    /*@GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "SEQ_TICPO_SERVICIOS_TIC")
    @SequenceGenerator(sequenceName = "SEQ_TICPO_SERVICIOS_TIC", allocationSize = 1, name = "SEQ_TICPO_SERVICIOS_TIC")*/
    @Column (name = "CVE_TIPO_IMPACTO_SERVICIO_TIC")
    private String cveTipoImpactoServicioTic;

    @Column (name = "DESC_TIPO_IMPACTO_SERVICIO_TIC")
    private String descTipoImpactoServicioTic;

    @Column (name = "DESC_TIPO_IMPACTO_SERV_TIC_ABR")
    private String descTipoImpactoServTicAbr;

    @Column (name = "DESC_ALCANCE_TIPO_IMPACTO_STIC")
    private String descAlcanceTipoImpactoStic;

    @Column (name = "NUM_ORDEN_TIPO_IMPACTO_SERVTIC")
    private Long numOrdenTipoImpactoService;

    @Column(name = "ID_COLOR_TIPO_IMPACTO_SERV_TIC")
    private Long idColorTipoImpactoServTic;

    @Column (name = "ID_IMAGEN_TIPO_IMPACTO_SERVTIC")
    private Long idImagenTipoImpactoServtic;

    @Column (name = "IP_PC_MTTO")
    private String  ipPcMtto;

    @Column (name = "NOMBRE_PC_MTTO")
    private String nombrePcMtto;

    @Column (name = "CVE_USUARIO_SO_MTTO")
    private String cveUsuarioSoMtto;

    @Column (name = "CVE_USUARIO_MTTO")
    private String cveUsuarioMtto;

    @Column(name = "NOMBRE_APLICACION_MTTO")
    private String nombreAplicacionMtto;

    @Column (name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column (name = "FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

    @Column (name = "CVE_ESTATUS")
    private String cveEstatus;
}
