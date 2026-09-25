package mx.gob.tabasco.cmaig.sigtic.sigget.model;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table
@Entity(name = "SEPARADOR_PLANTILLAS")
public class Separador implements Serializable {

    @Id
    @Column(name="ID_SEPARADOR")
    private Integer idSeparador;

    @Column(name="DES_SEPARADOR")
    private String nombreSeparador;

    @Column(name="CODIG_ASK")
    private String codigo;

    @Column(name="ESTATUS")
    private String estatus;
}
