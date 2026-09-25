package mx.gob.tabasco.cmaig.sigtic.sac.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name="CAT_ENTIDADES_FEDERATIVAS")
public class EntidadFederativa {

    @Id
    @Column(name = "ID_ENTIDAD_FEDERATIVA")
    private Integer idEntidadFederatia;

    @Column(name = "DESC_ENTIDAD_FEDERATIVA_ABREV")
    private String descripcionEntidadFederativaAbrev;

    @Column(name = "DESC_ENTIDAD_FEDERATIVA")
    private String descripcionEntidadFederativa;

    @Column(name = "NUM_ORDEN_ENTIDAD_FEDERATIVA")
    private Integer numeroOrdenEntidadFederativa;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

}
