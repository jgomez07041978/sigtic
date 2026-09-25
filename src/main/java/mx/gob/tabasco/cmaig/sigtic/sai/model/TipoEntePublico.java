package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS")
public class TipoEntePublico implements Serializable {

    @Id
    @Column(name = "CVE_TIPO_ENTE_PUBLICO")
    private Integer cveTipoEntePublico;

    @Column(name = "DESC_TIPO_ENTE_PUBLICO_ABREV")
    private String nombreTipoEntePublicoAbrev;

    @Column(name = "DESC_TIPO_ENTE_PUBLICO")
    private String nombreTipoEntePublico;

    @Column(name = "DESC_GRUPO_ENTE_PUBLICO")
    private String nombreGrupoTipoEntePublico;

    @Column(name = "NUM_ORDEN_TIPO_ENTE_PUBLICO")
    private Integer numeroOrdenTipoEntePublico;

}
