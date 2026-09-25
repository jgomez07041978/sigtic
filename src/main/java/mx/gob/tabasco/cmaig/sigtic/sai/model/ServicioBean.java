package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.io.Serializable;


@NamedNativeQuery(name = "ServicioBean.getAllServicioAdmin",
        query = "SELECT ST.ID_SERVICIO_TIC AS idServicio,TE.CVE_TIPO_ENTE_PUBLICO as cveTipoEntePublico, TE.DESC_TIPO_ENTE_PUBLICO as nombreTipoEntePublico, " +
                "TE.DESC_GRUPO_ENTE_PUBLICO as nombreGrupoEntePublico, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO as numeroOrdenTipoServicio, " +
                "CE.ID_ENTE_PUBLICO as idEntePublico, CE.DESC_ENTE_PUBLICO_ABREVIADA as nombreEntePublicoAbrev, CE.DESC_ENTE_PUBLICO as nombreEntePublico, " +
                "CT.ID_TIPO_SERVICIO_TIC as idTipoServicio, CT.DESC_TIPO_SERVICIO_TIC as nombreTipoServicio,  " +
                "SUB.ID_SUBTIPO_SERVICIO_TIC as idSubTipoServicio ,SUB.DESC_SUBTIPO_SERVICIO_TIC as nombreSubTipoServicio, " +
                "CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END AS vigente, " +
                "CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END AS historico " +
                "FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE  " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO " +
                "INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO " +
                "INNER JOIN CAT_TIPO_SERVICIOS_TIC CT ON CT.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC " +
                "INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC " +
                "WHERE ST.CVE_ESTATUS IN ('VIG','HIS') " +
                "--AND CE.ID_ENTE_PUBLICO=16 " +
                "ORDER BY  ST.ID_SERVICIO_TIC, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO, CE.NUM_ORDEN_ENTE_PUBLICO,  " +
                "CT.NUM_ORDEN_TIPO_SERVICIO_TIC, SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC", resultClass = ServicioBean.class)

@NamedNativeQuery(name = "ServicioBean.getAllServicioOpera",
        query = "SELECT ST.ID_SERVICIO_TIC AS idServicio,TE.CVE_TIPO_ENTE_PUBLICO as cveTipoEntePublico, TE.DESC_TIPO_ENTE_PUBLICO as nombreTipoEntePublico, " +
                "TE.DESC_GRUPO_ENTE_PUBLICO as nombreGrupoEntePublico, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO as numeroOrdenTipoServicio, " +
                "CE.ID_ENTE_PUBLICO as idEntePublico, CE.DESC_ENTE_PUBLICO_ABREVIADA as nombreEntePublicoAbrev, CE.DESC_ENTE_PUBLICO as nombreEntePublico, " +
                "CT.ID_TIPO_SERVICIO_TIC as idTipoServicio, CT.DESC_TIPO_SERVICIO_TIC as nombreTipoServicio,  " +
                "SUB.ID_SUBTIPO_SERVICIO_TIC as idSubTipoServicio ,SUB.DESC_SUBTIPO_SERVICIO_TIC as nombreSubTipoServicio, " +
                "CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END AS vigente, " +
                "CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END AS historico " +
                "FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE  " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO " +
                "INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO " +
                "INNER JOIN CAT_TIPO_SERVICIOS_TIC CT ON CT.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC " +
                "INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC " +
                "WHERE ST.CVE_ESTATUS IN ('VIG','HIS') " +
                "--AND CE.ID_ENTE_PUBLICO=16 " +
                "ORDER BY  ST.ID_SERVICIO_TIC, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO, CE.NUM_ORDEN_ENTE_PUBLICO,  " +
                "CT.NUM_ORDEN_TIPO_SERVICIO_TIC, SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC", resultClass = ServicioBean.class)

@NamedNativeQuery(name = "ServicioBean.getServicioAdminPorEnte",
        query = "SELECT ST.ID_SERVICIO_TIC AS idServicio,TE.CVE_TIPO_ENTE_PUBLICO as cveTipoEntePublico, TE.DESC_TIPO_ENTE_PUBLICO as nombreTipoEntePublico, " +
                "TE.DESC_GRUPO_ENTE_PUBLICO as nombreGrupoEntePublico, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO as numeroOrdenTipoServicio, " +
                "CE.ID_ENTE_PUBLICO as idEntePublico, CE.DESC_ENTE_PUBLICO_ABREVIADA as nombreEntePublicoAbrev, CE.DESC_ENTE_PUBLICO as nombreEntePublico, " +
                "CT.ID_TIPO_SERVICIO_TIC as idTipoServicio, CT.DESC_TIPO_SERVICIO_TIC as nombreTipoServicio,  " +
                "SUB.ID_SUBTIPO_SERVICIO_TIC as idSubTipoServicio ,SUB.DESC_SUBTIPO_SERVICIO_TIC as nombreSubTipoServicio, " +
                "CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END AS vigente, " +
                "CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END AS historico " +
                "FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE  " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO " +
                "INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO " +
                "INNER JOIN CAT_TIPO_SERVICIOS_TIC CT ON CT.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC " +
                "INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC " +
                "WHERE ST.CVE_ESTATUS IN ('VIG','HIS') " +
                "AND CE.ID_ENTE_PUBLICO=?1 " +
                "ORDER BY  ST.ID_SERVICIO_TIC, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO, CE.NUM_ORDEN_ENTE_PUBLICO,  " +
                "CT.NUM_ORDEN_TIPO_SERVICIO_TIC, SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC", resultClass = ServicioBean.class)

@NamedNativeQuery(name = "ServicioBean.getServicioOperaPorEnte",
        query = "SELECT ST.ID_SERVICIO_TIC AS idServicio,TE.CVE_TIPO_ENTE_PUBLICO as cveTipoEntePublico, TE.DESC_TIPO_ENTE_PUBLICO as nombreTipoEntePublico, " +
                "TE.DESC_GRUPO_ENTE_PUBLICO as nombreGrupoEntePublico, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO as numeroOrdenTipoServicio, " +
                "CE.ID_ENTE_PUBLICO as idEntePublico, CE.DESC_ENTE_PUBLICO_ABREVIADA as nombreEntePublicoAbrev, CE.DESC_ENTE_PUBLICO as nombreEntePublico, " +
                "CT.ID_TIPO_SERVICIO_TIC as idTipoServicio, CT.DESC_TIPO_SERVICIO_TIC as nombreTipoServicio,  " +
                "SUB.ID_SUBTIPO_SERVICIO_TIC as idSubTipoServicio ,SUB.DESC_SUBTIPO_SERVICIO_TIC as nombreSubTipoServicio, " +
                "CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END AS vigente, " +
                "CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END AS historico " +
                "FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE  " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO " +
                "INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO " +
                "INNER JOIN CAT_TIPO_SERVICIOS_TIC CT ON CT.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC " +
                "INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC " +
                "WHERE ST.CVE_ESTATUS IN ('VIG','HIS') " +
                "AND CE.ID_ENTE_PUBLICO=?1 " +
                "ORDER BY  ST.ID_SERVICIO_TIC, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO, CE.NUM_ORDEN_ENTE_PUBLICO,  " +
                "CT.NUM_ORDEN_TIPO_SERVICIO_TIC, SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC", resultClass = ServicioBean.class)


@Data
@Entity
@Table(name="SERVICIO_BEAN")
public class ServicioBean  implements Serializable {

    @Id
    private Long idServicio;

    private Integer cveTipoEntePublico;

    private String nombreTipoEntePublico;

    private String nombreGrupoEntePublico;

    private int numeroOrdenTipoServicio;

    private Long idEntePublico;

    private String nombreEntePublicoAbrev;

    private String nombreEntePublico;

    private Long idTipoServicio;

    private String nombreTipoServicio;

    private Long idSubTipoServicio;

    private String nombreSubTipoServicio;

    private int vigente;

    private int historico;

}
