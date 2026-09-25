package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="SERVICIOS_TIC_RELACIONADOS")
public class ServiciosTicRelacionados implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SERVICIO_TIC_REL")
    @SequenceGenerator(sequenceName = "SEQ_SERVICIOS_TIC_RELACIONADO", allocationSize = 1, name = "SEQ_SERVICIO_TIC_REL")
    @Column (name = "ID_SERVICIO_TIC_RELACIONADO")
    private Long idServicioTicRelacionado;

    @ManyToOne
    @JoinColumn (name="ID_SERVICIO_TIC_PADRE")
    private ServiciosTic serviciosTicPadre;

    @ManyToOne
    @JoinColumn (name="ID_SERVICIO_TIC")
    private ServiciosTic serviciosTic;

    @Column (name="DESC_SERVICIO_TIC_RELACIONADO")
    private String descripcionServicioTic;

    @Column (name="NOTA_SERVICIO_TIC_RELACIONADO")
    private String notaServicioTicRelacionado;

    @Column (name="NUM_ORDEN_SERVICIOTIC_RELACION")
    private Integer numeroOrdenServicio;

    @Column(name = "FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

    @Column(name = "CVE_USUARIO_MTTO", nullable = true)
    private String cveUsuarioMtto;

    @Column (name="CVE_ESTATUS")
    private String cveEstatus;

}
