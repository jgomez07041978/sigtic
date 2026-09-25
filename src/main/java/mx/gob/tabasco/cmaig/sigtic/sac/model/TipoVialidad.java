package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Getter
@Setter
@Entity
@Table(name="CAT_TIPO_VIALIDADES")
public class TipoVialidad implements Serializable {

    @Id
    @Column(name = "ID_TIPO_VIALIDAD")
    private Integer idTipoVialidad;

    @Column(name = "DESC_TIPO_VIALIDAD")
    private String nombreTipoVialidad;

    @Column(name = "DESC_TIPO_VIALIDAD_ABREV")
    private String nombreTipoVialidadAbrev;

    @Column(name = "NUM_ORDEN_TIPO_VIALIDAD")
    private Integer numeroOrdenVialidad;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

}