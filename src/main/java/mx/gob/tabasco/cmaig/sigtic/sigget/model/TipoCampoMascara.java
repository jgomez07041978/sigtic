package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table
@Entity(name="CAT_TIPO_CAMPO_MASCARA")
public class TipoCampoMascara implements Serializable {
    @Id
    @Column(name = "ID_TIPO_CAMPO_MASCARA")
    private Integer idTipoCampoMascara;

    @Column(name = "CVE_TIPO_CAMPO_PLANTILLA")
    private String cveTipoCampo;

    @Column(name = "NOMBRE_MASCARA")
    private String nombreMascara;

    @Column(name = "MASCARA")
    private String mascara;

    @Column(name="CVE_ESTATUS")
    private String cveEstatus;

    @Column(name="USUARIO_REGISTRO")
    private String usuarioRegistro;

}
