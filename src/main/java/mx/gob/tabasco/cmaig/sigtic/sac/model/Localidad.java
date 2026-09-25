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
@Table(name="CAT_LOCALIDADES")
public class Localidad {

    @Id
    @Column(name = "ID_LOCALIDAD")
    private Integer idLocalidad;

    @Column(name="ID_MUNICIPIO_LOCALIDAD")
    private Integer idMunicipio;

    @Column(name="ID_ENTIDADFEDERATIVA_LOCALIDAD")
    private Integer idEntidadFederativa;

    @Column(name = "DESC_LOCALIDAD_ABREVIADO")
    private String descripcionLocalidadAbrev;

    @Column(name = "DESC_LOCALIDAD")
    private String descripcionLocalidad;

    @Column(name = "CODIGO_LOCALIDAD")
    private String codigoLocalidad;

    @Column(name = "NUM_ORDEN_LOCALIDAD")
    private Integer numeroOrdenLocalidad;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

}
