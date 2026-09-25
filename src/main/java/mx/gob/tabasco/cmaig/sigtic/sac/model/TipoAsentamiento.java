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
@Table(name="CAT_TIPO_ASENTAMIENTOS")
public class TipoAsentamiento {
    @Id
    @Column(name = "ID_TIPO_ASENTAMIENTO")
    private Integer idTipoAsentamiento;

    @Column(name = "DESC_TIPO_ASENTAMIENTO_ABREV")
    private String nombreTipoAsentamiento;;

    @Column(name = "DESC_TIPO_ASENTAMIENTO")
    private String nombreTipoAsentamientoAbrev;

    @Column(name = "NUM_ORDEN_TIPO_ASENTAMIENTO")
    private Integer numeroOrdenTipoAsentamiento;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

}
