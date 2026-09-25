package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="CAT_SISTEMA_GIT")
public class CatSistemaGit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SISTEMA_GIT_APP")
    @SequenceGenerator(sequenceName = "SEQ_SISTEMA_GIT", allocationSize = 1, name = "SEQ_SISTEMA_GIT_APP")
    @Column(name = "ID_SISTEMA_GIT")
    private Long idSistemaGit;

    @ManyToOne
    @JoinColumn(name="ID_SISTEMA", nullable = false)
    private CatSistema catSistema;

    @Column(name = "CODIGO_GIT")
    private String codigoGit;

    @Column(name = "descripcion")
    private String descripcion;

    @Column (name="FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;


}
