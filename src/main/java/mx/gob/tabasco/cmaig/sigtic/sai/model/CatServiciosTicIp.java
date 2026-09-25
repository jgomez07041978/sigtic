package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table (name="SERVICIOS_TIC_IP")
public class CatServiciosTicIp implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "SEQ_SERVICIOS_TIC_IP")
    @SequenceGenerator(sequenceName = "SEQ_SERVICIOS_TIC_IP", allocationSize = 1, name  = "SEQ_SERVICIOS_TIC_IP")
    @Column (name = "ID_IP_SERVICIO_TIC")
    private Long idIpServicioTic;

    @Column (name = "IP_SERVICIO_TIC")
    private String ipServicio;

    @ManyToOne
    @JoinColumn (name = "ID_SERVICIO_TIC")
    private ServiciosTic serviciosTic;

    @Column (name = "NUM_ORDEN_IP_SERVICIO_TIC")
    private Long numOrdenIpServicioTic;

/*
    @Column(name = "IP_PC_MTTO")
    private String ipPcMtto;

    @Column (name = "NOMBRE_PC_MTTO")
    private String nombrePcMtto;

    @Column (name = "CVE_USUARIO_SO_MTTO")
    private String cveUsuarioSoMtto;

    @Column (name = "CVE_USUARIO_MTTO")
    private String cveUsuarioMtto;

    @Column (name = "NOMBRE_APLICACION_MTTO")
    private String nombreAplicacionMtto;

    @Column (name = "FECHA_REGISTRO")
    private String fechaRegistro;
*/

    @Column (name = "FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

    @Column (name = "CVE_ESTATUS")
    private String cveEstatus;
}