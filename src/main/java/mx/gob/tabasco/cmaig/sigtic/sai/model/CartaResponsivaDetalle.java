package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="CARTA_RESPONSIVA_DETALLE")
public class CartaResponsivaDetalle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CARTA-RESPO_DETA_APP")
    @SequenceGenerator(sequenceName = "SEQ_CARTA_RESPONSIVA_DETALLE", allocationSize = 1, name = "SEQ_CARTA-RESPO_DETA_APP")
    @Column(name = "ID_CARTA_RESPONSIVA_DETALLE")
    private Long idCartaResponsivaDetalle;

    @ManyToOne
    @JoinColumn(name="ID_CARTA_RESPONSIVA")
    private CartaResponsiva cartaResponsiva;

    @ManyToOne
    @JoinColumn(name="ID_SERVICIO_TIC")
    private ServiciosTic serviciosTic;

    @Column(name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

    @Column(name = "ID_USUARIO_REGISTRO")
    private Long idUsuarioRegistro;

    @Column(name = "IP_REGISTRO")
    private String ipRegistro;

    @Column(name = "HOST_REGISTRO")
    private String hostNameRegistro;

    @Column(name = "CVE_SERVICIO_TIC")
    private String cveServicio;

    @Column(name = "TIPO_SERVICIO")
    private String tipoServicio;

    @Column(name = "SUBTIPO_SERVICIO")
    private String subtipoServicio;

    @Column(name = "NOMBRE_SERVICIO_TIC")
    private String nombreServicio;

    @Column(name = "IP_PC_MTTO", nullable = true)
    private String ipPc;

    @Column(name = "NOMBRE_PC_MTTO", nullable = true)
    private String nombrePc;

    @Column(name = "CVE_USUARIO_SO_MTTO", nullable = true)
    private String usuarioSo;

    @Column(name = "CVE_USUARIO_MTTO", nullable = true)
    private String usuarioMtto;

    @Column(name = "NOMBRE_APLICACION_MTTO", nullable = true)
    private String aplicacion;

    @Column(name = "FECHA_ULTIMO_CAMBIO", nullable = true)
    private Timestamp fechaUltimo;


}
