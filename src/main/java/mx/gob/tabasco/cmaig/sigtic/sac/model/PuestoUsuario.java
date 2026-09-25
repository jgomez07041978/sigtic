package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "CAT_PUESTOS_USUARIOS")
public class PuestoUsuario implements Serializable {

    @Id
    @Column(name = "ID_PUESTO_USUARIO")
    private Long idPuestoUsuario;

    @Column(name = "DESC_PUESTO_USUARIO", nullable = true)
    private String nombrePuestoUsuario;

    @Column(name = "DESC_PUESTO_USUARIO_ABREV", nullable = true)
    private String nombrePuestoUsuarioAbreviada;


    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO_PUESTO_USUARIO", nullable = true)
    private EntePublico entePublico;

    @Column(name = "NUM_ORDEN_PUESTO_USUARIO", nullable = true)
    private Integer numOrden;

    @Column(name = "ID_COLOR_PUESTO_USUARIO", nullable = true)
    private Integer idColor;

    @Column(name = "ID_IMAGEN_PUESTO_USUARIO", nullable = true)
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
    private Timestamp fechaCambio;

    @Column(name = "CVE_ESTATUS", nullable = true)
    private String cveEstatus;

    @Column(name = "CVE_PUESTO_USUARIO", nullable = true)
    private String cvePuestoUsuario;

}
