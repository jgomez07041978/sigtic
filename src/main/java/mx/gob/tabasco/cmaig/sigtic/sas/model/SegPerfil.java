package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "SEG_PERFILES")
public class SegPerfil  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PERFIL_APP")
    @SequenceGenerator(sequenceName = "SEQ_SEG_PERFIL", allocationSize = 1, name = "SEQ_PERFIL_APP")
    @Column(name = "ID_SEG_PERFIL")
    private Integer idPerfil;

    @Column(name = "NOMBRE_SEG_PERFIL", nullable = true)
    private String nombrePerfil;

    @Column(name = "DESCRIPCION_SEG_PERFIL", nullable = true)
    private String descPerfil;

    @Column(name = "CVE_ESTATUS", nullable = true)
    private  String cveEstatus;

    @Column(name = "DEFECTO", nullable = true)
    private String defecto;

    @Column(name = "SISTEMA", nullable = true)
    private String sistema;

    @Transient
    private String tipoMenu;

    @Transient
    private Integer numPerfil;

    @Transient
    private List<SegMenuArbol> menu;

}
