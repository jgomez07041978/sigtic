package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "SEG_PERFIL_PRIVILEGIOS")
public class SegPerfilPrivilegio implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PERFIL_PRIVILEGIO")
    @SequenceGenerator(sequenceName = "SEQ_SEG_PERFIL_PRIVILEGIO", allocationSize = 1, name = "SEQ_PERFIL_PRIVILEGIO")
    @Column(name = "ID_SEG_PERFIL_PRIVILEGIO")
    private Long idPerfilPrivilegio;

    @ManyToOne
    @JoinColumn(name="ID_SEG_MENU")
    private SegMenu segMenu;

    @ManyToOne
    @JoinColumn(name="ID_SEG_PERFIL")
    private SegPerfil segPerfil;

    @Column(name = "EDICION", nullable = true)
    private Boolean editar;

    @Column(name = "ELIMINAR", nullable = true)
    private Boolean eliminar;

    @Column(name = "AGREGAR", nullable = true)
    private Boolean agregar;

    @Column(name = "CONSULTAR", nullable = true)
    private Boolean consultar;

    @Column(name = "VALIDAR", nullable = true)
    private Boolean validar;

}
