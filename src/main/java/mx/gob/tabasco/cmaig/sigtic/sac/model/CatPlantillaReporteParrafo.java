package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="CAT_PLANTILLA_REPORTE_PARRAFO")
public class CatPlantillaReporteParrafo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PLANTILLA_REPORTE_PARRAFO_APP")
    @SequenceGenerator(sequenceName = "SEQ_PLANTILLA_REPORTE_PARRAFO", allocationSize = 1, name = "SEQ_PLANTILLA_REPORTE_PARRAFO_APP")
    @Column(name = "ID_PLANTILLA_REPORTE_PARRAFO")
    private Long idPlantillaReporteParrafo;

    @Column(name = "ID_PLANTILLA_REPORTE")
    private Long idPlantillaReporte;

    @Column(name = "PARRAFO")
    private String parrafo;

    @Column(name = "ORDEN")
    private Integer numeroOrden;

    @Column(name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column(name = "ID_USUARIO_REGISTRO")
    private Long idUsuarioRegistro;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

    @Column(name = "FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

    @Column(name = "IP_PC_MTTO", nullable = true)
    private String ipPC;

    @Column(name = "NOMBRE_PC_MTTO", nullable = true)
    private String nombrePC;

    @Column(name = "CVE_USUARIO_MTTO", nullable = true)
    private String cveUsuarioMtto;


}
