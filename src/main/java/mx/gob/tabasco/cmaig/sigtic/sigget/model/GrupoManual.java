package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Data
@Entity
@Table(name="GRUPO_MANUAL")
public class GrupoManual implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GRUPO_MANUAL_APP")
    @SequenceGenerator(sequenceName = "SEQ_GRUPO_MANUAL", allocationSize = 1, name = "SEQ_GRUPO_MANUAL_APP")
    @Column(name = "ID_GRUPO")
    private Integer idGrupoManual;

    @Column(name = "NOMBRE_GRUPO", nullable = false)
    private String nombreGrupo;

    @Column(name = "ID_USUARIO_MTTO", nullable = false)
    private Long idUsuarioMtto;

    @Column(name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column(name = "FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

    @Column(name = "CVE_ESTATUS", nullable = false)
    private String cveEstatus;

    @Column(name = "NUMERO_ORDEN", nullable = false)
    private Long numeroOrden;
}
