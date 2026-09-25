package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "SEG_USUARIO_TEMPORAL_ROL")
public class SegUsuarioTemporalRol implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO_TEMPORAL_ROL_APP")
    @SequenceGenerator(sequenceName = "SEQ_USUARIO_TEMPORAL_ROL", allocationSize = 1, name = "SEQ_USUARIO_TEMPORAL_ROL_APP")
    @Column(name = "ID_USUARIO_TEMPORAL_ROL")
    private Long idUsuarioTemporalRol;


    @ManyToOne
    @JoinColumn(name="ID_USUARIO_TEMPORAL")
    private SegUsuarioTemporal segUsuarioTemporal;

    @ManyToOne
    @JoinColumn(name="ID_SEG_PERFIL")
    private SegPerfil segPerfil;

}