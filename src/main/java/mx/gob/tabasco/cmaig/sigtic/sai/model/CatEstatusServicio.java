package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "CAT_ESTATUS_SERVICIO")
public class CatEstatusServicio implements Serializable {
    @Id
    @Column (name = "ID_ESTATUS_SERVICIO")
    private Integer idEstatusServicio;

    @Column  (name = "ESTATUS_SERVICIO")
    private String estatusServicio;
}