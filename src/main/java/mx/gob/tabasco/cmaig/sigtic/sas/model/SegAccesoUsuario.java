package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "SEG_ACCESO_USUARIO")
public class SegAccesoUsuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ACCESO_APP")
    @SequenceGenerator(sequenceName = "SEQ_ID_SEG_ACCESO_USUARIO", allocationSize = 1, name = "SEQ_ACCESO_APP")
    @Column(name = "ID_SEG_ACCESO_USUARIO", insertable=false, updatable=false)
    private Long idAccesoUsuario;

    @Column(name = "RFC_ACCESO_USUARIO")
    private String rfcUsuario;

    @Column(name = "ID_ENTE_PUBLICO_ACCESO")
    private Long idEntePublico;

    @Column(name = "ID_UNIDAD_ADMTVA_ACCESO")
    private Long idUnidadAdministrativa;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

    @Column(name = "ID_SEG_SISTEMA")
    private String idSistema;

    @Column(name = "FECHA_ASIGNACION_ACCESO")
    @CreationTimestamp
    private LocalDateTime fechaAccesoAsignacion;

}
