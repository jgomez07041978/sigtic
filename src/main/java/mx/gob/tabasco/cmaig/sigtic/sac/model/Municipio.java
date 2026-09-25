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
@Table(name="CAT_MUNICIPIOS")
public class Municipio {
    @Id
    @Column(name = "ID_MUNICIPIO")
    private Integer idMunicipio;

    @Column(name="ID_ENTIDADFEDERATIVA_MUNICIPIO")
    private Integer idEntidadFederativa;

    @Column(name = "DESC_MUNICIPIO_ABREVIADO")
    private String descripcionMunicipioAbrev;

    @Column(name = "DESC_MUNICIPIO")
    private String descripcionMunicipio;

    @Column(name = "NUM_ORDEN_MUNICIPIO")
    private Integer numeroOrdenMunipio;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

}
