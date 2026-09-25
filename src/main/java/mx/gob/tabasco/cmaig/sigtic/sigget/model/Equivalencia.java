package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name="EQUIVALENCIA")
public class Equivalencia implements Serializable {
    @Id
    @Column(name="ID_EQUIVALENCIA")
    private Long idEquivalencia;

    @Column(name="DES_EQUIVALENCIA")
    private String desEquivalencia;

    @Column(name="EXPRESION")
    private String expresion;
}
