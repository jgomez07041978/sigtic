package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "SEG_PERFILES_USUARIO")
public class SegPerfilUsuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PERFIL_USUARIO_APP")
    @SequenceGenerator(sequenceName = "SEQ_SEG_PERFIL_USUARIO", allocationSize = 1, name = "SEQ_PERFIL_USUARIO_APP")
    @Column(name="ID_SEG_PERFIL_USUARIO")
    private Long idSegPerfilUsuario;

    @Column(name="SEG_USUARIO")
    private String segUsuario;

    @Column(name="FECHA_INICIO_VIGENCIA")
    private Timestamp fechaInicioVigencia;

    @Column(name="FECHA_FINAL_VIGENCIA")
    private Timestamp fechaFinVigencia;

    @Column(name="SISTEMA")
    private String sistema;

    @ManyToOne
    @JoinColumn(name="ID_SEG_PERFIL")
    private SegPerfil segPerfil;

}
