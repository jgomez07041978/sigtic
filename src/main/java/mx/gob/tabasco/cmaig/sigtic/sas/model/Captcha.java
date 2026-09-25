package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "CAPTCHA")
public class Captcha implements Serializable{

    @Id
    @Column(name = "ID_CAPTCHA")
    private Integer idCaptcha;

    @Column(name = "VALIDAR_CAPTCHA")
    private Integer validarCaptcha;

    @Column(name = "SISTEMA")
    private String sistema;
}
