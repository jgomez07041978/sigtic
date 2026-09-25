package mx.gob.tabasco.cmaig.sigtic.sigget.model;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table
@Entity(name = "CAMPO")
public class Campo implements Serializable {

    @Id
    @Column(name="NOMBRE_CAMPO")
    private String nombreCampo;

    @Column(name="ORDEN")
    private Integer orden;
}
