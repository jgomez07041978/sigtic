package mx.gob.tabasco.cmaig.sigtic.sai.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.io.Serializable;

@NamedNativeQuery(name = "ServicioGroupBean.getGroupServicioAll",
        query = "SELECT ROWNUM as idServicioGroup, TBG.* FROM ( " +
                "SELECT ST.ID_TIPO_SERVICIO_TIC as idTipoServicio, CTS.DESC_TIPO_SERVICIO_TIC as nombreServicio, " +
                "ST.ID_SUBTIPO_SERVICIO_TIC AS idSubTipoServicio, STS.DESC_SUBTIPO_SERVICIO_TIC AS nombreSubServicio, COUNT(1) AS total  " +
                "FROM SERVICIOS_TIC ST " +
                "INNER JOIN CAT_TIPO_SERVICIOS_TIC CTS ON CTS.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC " +
                "INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC STS ON STS.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC " +
                "WHERE ST.CVE_ESTATUS IN ('VIG','HIS') "+
                "GROUP BY ST.ID_TIPO_SERVICIO_TIC, CTS.DESC_TIPO_SERVICIO_TIC,ST.ID_SUBTIPO_SERVICIO_TIC, STS.DESC_SUBTIPO_SERVICIO_TIC,CTS.NUM_ORDEN_TIPO_SERVICIO_TIC, STS.NUM_ORDEN_SUBTIPO_SERVICIO_TIC " +
                "ORDER BY CTS.NUM_ORDEN_TIPO_SERVICIO_TIC, STS.NUM_ORDEN_SUBTIPO_SERVICIO_TIC) TBG", resultClass = ServicioGroupBean.class)

@NamedNativeQuery(name = "ServicioGroupBean.getGroupServicioPorUnidad",
        query = "SELECT ROWNUM as idServicioGroup, TBG.* FROM ( " +
                "SELECT ST.ID_TIPO_SERVICIO_TIC as idTipoServicio, CTS.DESC_TIPO_SERVICIO_TIC as nombreServicio, " +
                "ST.ID_SUBTIPO_SERVICIO_TIC AS idSubTipoServicio, STS.DESC_SUBTIPO_SERVICIO_TIC AS nombreSubServicio, COUNT(1) AS total  " +
                "FROM SERVICIOS_TIC ST " +
                "INNER JOIN CAT_TIPO_SERVICIOS_TIC CTS ON CTS.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC " +
                "INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC STS ON STS.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC " +
                "WHERE (ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC in (?1) OR ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC in (?1)) AND ST.CVE_ESTATUS IN ('VIG','HIS') "+
                "GROUP BY ST.ID_TIPO_SERVICIO_TIC, CTS.DESC_TIPO_SERVICIO_TIC,ST.ID_SUBTIPO_SERVICIO_TIC, STS.DESC_SUBTIPO_SERVICIO_TIC,CTS.NUM_ORDEN_TIPO_SERVICIO_TIC, STS.NUM_ORDEN_SUBTIPO_SERVICIO_TIC " +
                "ORDER BY CTS.NUM_ORDEN_TIPO_SERVICIO_TIC, STS.NUM_ORDEN_SUBTIPO_SERVICIO_TIC) TBG", resultClass = ServicioGroupBean.class)

@Data
@Entity
@Table(name="SERVICIO_GROUP_BEAN")
public class ServicioGroupBean implements Serializable {

    @Id
    private Integer idServicioGroup;
    private Long idTipoServicio;
    private String nombreServicio;
    private Long idSubTipoServicio;
    private String nombreSubServicio;
    private int total;
}
