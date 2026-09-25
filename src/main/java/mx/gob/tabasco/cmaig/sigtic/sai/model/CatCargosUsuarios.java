package mx.gob.tabasco.cmaig.sigtic.sai.model;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="SIGGET_SAC_CATALOGOS.CAT_CARGOS_USUARIOS")
public class CatCargosUsuarios  implements Serializable {


    @Id
    @Column(name = "ID_CARGO")
    private Long idCargoUsuario;


    @Column(name = "DESC_CARGO_USUARIO")
    private String  nombreCargoUsuario;

    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO_CARGO_USUARIO")
    private EntePublico entePublico;


    @Column(name = "NUM_ORDEN_CARGO_USUARIO")
    private Integer numeroOrdenCargo;

    @Column(name = "CVE_ESTATUS")
    private String  cveEstatus;

}
