package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="SUB_SERVICIOS_TIC_USUARIOS")
public class SubServicioUsuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SUB_SERVICIO_USU_APP")
    @SequenceGenerator(sequenceName = "SEQ_SUB_SERVICIO_USUARIO", allocationSize = 1, name = "SEQ_SUB_SERVICIO_USU_APP")
    @Column(name = "ID_SUB_SERVICIO_USUARIO")
    private Long idSubtipoServicioUsuario;

    @ManyToOne
    @JoinColumn(name="ID_USUARIO")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name="ID_SUBTIPO_SERVICIO_TIC")
    private CatSubtipoServiciosTic catSubtipoServiciosTic;

    @Column(name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column(name = "CVE_USUARIO_MTTO")
    private String cveUsuarioMtto;

}
