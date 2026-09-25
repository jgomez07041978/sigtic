package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name="CAT_ESTATUS_SISTEMAS")
public class CatEstatusSistema implements Serializable {
    @Id
    @Column(name="CVE_ESTATUS_SISTEMA")
    private String cveEstatusSistema;

    @Column(name="DESC_ESTATUS_SISTEMA")
    private String nombreEstatusSistema;

    @Column(name="DESC_ESTATUS_SISTEMA_ABREVIADO")
    private String nombreEstatusSistemaAbreviado;

    @Column(name="CVE_ESTATUS")
    private String cveEstatus;


}
