package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "CAT_TIPO_ENTES_PUBLICOS")
public class TipoEntePublico implements Serializable {

    @Id
    @Column(name = "CVE_TIPO_ENTE_PUBLICO")
    private Integer cveTipoEntePublico;

    @Column(name = "DESC_TIPO_ENTE_PUBLICO_ABREV")
    private String nombreTipoEntePublicoAbrev;

    @Column(name = "DESC_TIPO_ENTE_PUBLICO")
    private String nombreTipoEntePublico;

}
