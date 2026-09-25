package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="CAT_CARGOS_USUARIOS")
public class CargoUsuario  implements Serializable {

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

    @Column(name = "TOTAL_ENTE_PUBLICO")
    private Integer totalEntePublico;

    @Column(name = "CVE_ESTATUS")
    private String  cveEstatus;

}
