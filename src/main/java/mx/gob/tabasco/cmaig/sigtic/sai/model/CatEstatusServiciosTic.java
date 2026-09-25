package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "CAT_ESTATUS_SERVICIOS_TIC")
public class CatEstatusServiciosTic implements Serializable {
    @Id
    @Column (name = "CVE_ESTATUS_SERVICIO_TIC")
    private String cveEstatusServicioTic;

    @Column  (name = "DESC_ESTATUS_SERVICIO_TIC")
    private String descEstatusServicioTic;

    @Column (name = "DESC_ESTATUS_SERVICIO_TIC_ABR")
    private String descEstatusServicioTicAbr;

    @Column (name = "DESC_ALCANCE_ESTADO_SERV_TIC")
    private String descAlcanceEstadoServTic;

    @Column (name = "NUM_ORDEN_ESTATUS_SERV_TIC")
    private Integer numOrdenEstatusServTic;

    @Column (name = "ID_COLOR_ESTATUS_SERV_TIC")
    private Integer idColorEstatusServTic;

    @Column (name = "ID_IMAGEN_ESTATUS_SERV_TIC")
    private Integer idImagenEstatusServTic;

    @Column (name = "CVE_ESTATUS")
    private String cveEstatus;
}