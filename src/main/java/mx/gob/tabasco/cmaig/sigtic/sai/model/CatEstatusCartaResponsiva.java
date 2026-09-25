package mx.gob.tabasco.cmaig.sigtic.sai.model;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name="CAT_ESTATUS_CARTA_RESPONSIVA")
public class CatEstatusCartaResponsiva implements Serializable {

    @Id
    @Column(name="ID_ESTATUS_CR")
    private Integer idEstatusCartaResponsiva;

    @Column(name="NOMBRE_ESTATUS_CR")
    private String nombreEstatusCartaResponsiva;

}
