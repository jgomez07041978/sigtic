package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "SEG_TIPO_USUARIO")
public class SegTipoUsuario implements Serializable {

    @Id
    @Column(name = "ID_TIPO_USUARIO")
    private Integer idTipoUsuario;

    @Column(name="CVE_TIPO_USUARIO")
    private String claveTipoUsuario;

    @Column(name="NOMBRE_TIPO_USUARIO")
    private String nombreTipoUsuario;

}