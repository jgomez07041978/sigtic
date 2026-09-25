package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table
@Entity(name="CAT_TIPO_CAMPO_PLANTILLAS")
public class TipoCampo implements Serializable {

    @Id
    @Column(name = "CVE_TIPO_CAMPO_PLANTILLA")
    private String cveTipoCampo;

    @Column(name = "DESC_TIPO_CAMPO_PLANTILLA_ABR")
    private String nombreTipoCampoAbrev;

    @Column(name = "DESC_TIPO_CAMPO_PLANTILLA")
    private String nombreTipoCampo;

    @Column(name = "NUM_ORDEN_TIPO_CAMPO_PLANTILLA")
    private Integer numOrden;

    @Column(name="CVE_ESTATUS")
    private String cveEstatus;
}
