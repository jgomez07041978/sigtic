package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="SUB_SERVICIOS_SEARCH")
public class SubServicioSearch implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SERVICIO_SEARCH_TIC")
    @SequenceGenerator(sequenceName = "SEQ_SERVICIOS_SEARCH_TIC", allocationSize = 1, name = "SEQ_SERVICIO_SEARCH_TIC")
    @Column (name = "ID_SUB_SERVICIO_SEARCH")
    private Long idSubServicioSearch;

    @Column (name="CLASIFICACION")
    private String clasificacion;

    @ManyToOne
    @JoinColumn (name = "ID_SUBTIPO_SERVICIO_TIC")
    private CatSubtipoServiciosTic catSubtipoServiciosTic;

}
