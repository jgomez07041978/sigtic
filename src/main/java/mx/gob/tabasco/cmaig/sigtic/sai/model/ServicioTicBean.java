package mx.gob.tabasco.cmaig.sigtic.sai.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.io.Serializable;

@NamedNativeQuery(name = "ServicioTicBean.getServiciosAllPorEntePublico",
        query = "SELECT " +
                "    ST.ID_SERVICIO_TIC AS idServicioTic," +
                "    CTS.DESC_TIPO_SERVICIO_TIC AS tipoServicio," +
                "    CSTS.DESC_SUBTIPO_SERVICIO_TIC AS subTipoServicio," +
                "    ST.DESC_SERVICIO_TIC AS nombreServicio, " +
                "    EP.DESC_ENTE_PUBLICO_ABREVIADA AS nombreEntePublicoAdminAbrev," +
                "    EP.DESC_ENTE_PUBLICO AS nombreEntePublicoAdmin," +
                "    UA.DESC_UNIDAD_ADMINISTRATIVA_ABR AS nombreUnidadAdminAbrev," +
                "    UA.DESC_UNIDAD_ADMINISTRATIVA AS nombreUnidadAdmin," +
                "    EPO.DESC_ENTE_PUBLICO_ABREVIADA AS nombreEntePublicoOperaAbrev," +
                "    EPO.DESC_ENTE_PUBLICO AS nombreEntePublicoOpera," +
                "    UAO.DESC_UNIDAD_ADMINISTRATIVA_ABR AS nombreUnidadOperaAbrev," +
                "    UAO.DESC_UNIDAD_ADMINISTRATIVA AS nombreUnidadOpera," +
                "    CE.DESC_ESTADO_SERVICIO_TIC AS nombreEstadoServicio," +
                "    ST.CVE_ESTATUS AS cveEstatus " +
                "FROM SERVICIOS_TIC ST " +
                "INNER JOIN CAT_TIPO_SERVICIOS_TIC CTS ON CTS.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC " +
                "INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC CSTS ON CSTS.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EP ON EP.ID_ENTE_PUBLICO=ST.ID_ENTE_PUBLICO_ADM_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EPO ON EPO.ID_ENTE_PUBLICO=ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_UNIDAD_ADMINISTRATIVA=ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAO ON UAO.ID_UNIDAD_ADMINISTRATIVA=ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC " +
                "INNER JOIN CAT_ESTADOS_SERVICIOS_TIC CE ON CE.CVE_ESTADO_SERVICIO_TIC = ST.CVE_ESTADO_SERVICIO_TIC " +
                "WHERE (ST.ID_ENTE_PUBLICO_ADM_SERV_TIC =?1 OR ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC= ?1) AND ST.CVE_ESTATUS IN ('VIG','HIS') " +
                "ORDER BY ST.NUM_ORDEN_SERVICIO_TIC", resultClass = ServicioTicBean.class)

@NamedNativeQuery(name = "ServicioTicBean.getServiciosAdministradosPorEntePublico",
        query = "SELECT " +
                "    ST.ID_SERVICIO_TIC AS idServicioTic," +
                "    CTS.DESC_TIPO_SERVICIO_TIC AS tipoServicio," +
                "    CSTS.DESC_SUBTIPO_SERVICIO_TIC AS subTipoServicio," +
                "    ST.DESC_SERVICIO_TIC AS nombreServicio, " +
                "    EP.DESC_ENTE_PUBLICO_ABREVIADA AS nombreEntePublicoAdminAbrev," +
                "    EP.DESC_ENTE_PUBLICO AS nombreEntePublicoAdmin," +
                "    UA.DESC_UNIDAD_ADMINISTRATIVA_ABR AS nombreUnidadAdminAbrev," +
                "    UA.DESC_UNIDAD_ADMINISTRATIVA AS nombreUnidadAdmin," +
                "    EPO.DESC_ENTE_PUBLICO_ABREVIADA AS nombreEntePublicoOperaAbrev," +
                "    EPO.DESC_ENTE_PUBLICO AS nombreEntePublicoOpera," +
                "    UAO.DESC_UNIDAD_ADMINISTRATIVA_ABR AS nombreUnidadOperaAbrev," +
                "    UAO.DESC_UNIDAD_ADMINISTRATIVA AS nombreUnidadOpera," +
                "    CE.DESC_ESTADO_SERVICIO_TIC AS nombreEstadoServicio," +
                "    ST.CVE_ESTATUS AS cveEstatus " +
                "FROM SERVICIOS_TIC ST " +
                "INNER JOIN CAT_TIPO_SERVICIOS_TIC CTS ON CTS.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC " +
                "INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC CSTS ON CSTS.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EP ON EP.ID_ENTE_PUBLICO=ST.ID_ENTE_PUBLICO_ADM_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EPO ON EPO.ID_ENTE_PUBLICO=ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_UNIDAD_ADMINISTRATIVA=ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAO ON UAO.ID_UNIDAD_ADMINISTRATIVA=ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC " +
                "INNER JOIN CAT_ESTADOS_SERVICIOS_TIC CE ON CE.CVE_ESTADO_SERVICIO_TIC = ST.CVE_ESTADO_SERVICIO_TIC " +
                "WHERE ST.ID_ENTE_PUBLICO_ADM_SERV_TIC =?1 AND ST.CVE_ESTATUS IN ('VIG','HIS') "+
                "ORDER BY ST.NUM_ORDEN_SERVICIO_TIC", resultClass = ServicioTicBean.class)

@NamedNativeQuery(name = "ServicioTicBean.getServiciosOperadosPorEntePublico",
        query = "SELECT " +
                "    ST.ID_SERVICIO_TIC AS idServicioTic," +
                "    CTS.DESC_TIPO_SERVICIO_TIC AS tipoServicio," +
                "    CSTS.DESC_SUBTIPO_SERVICIO_TIC AS subTipoServicio," +
                "    ST.DESC_SERVICIO_TIC AS nombreServicio, " +
                "    EP.DESC_ENTE_PUBLICO_ABREVIADA AS nombreEntePublicoAdminAbrev," +
                "    EP.DESC_ENTE_PUBLICO AS nombreEntePublicoAdmin," +
                "    UA.DESC_UNIDAD_ADMINISTRATIVA_ABR AS nombreUnidadAdminAbrev," +
                "    UA.DESC_UNIDAD_ADMINISTRATIVA AS nombreUnidadAdmin," +
                "    EPO.DESC_ENTE_PUBLICO_ABREVIADA AS nombreEntePublicoOperaAbrev," +
                "    EPO.DESC_ENTE_PUBLICO AS nombreEntePublicoOpera," +
                "    UAO.DESC_UNIDAD_ADMINISTRATIVA_ABR AS nombreUnidadOperaAbrev," +
                "    UAO.DESC_UNIDAD_ADMINISTRATIVA AS nombreUnidadOpera," +
                "    CE.DESC_ESTADO_SERVICIO_TIC AS nombreEstadoServicio," +
                "    ST.CVE_ESTATUS AS cveEstatus " +
                "FROM SERVICIOS_TIC ST " +
                "INNER JOIN CAT_TIPO_SERVICIOS_TIC CTS ON CTS.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC " +
                "INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC CSTS ON CSTS.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EP ON EP.ID_ENTE_PUBLICO=ST.ID_ENTE_PUBLICO_ADM_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EPO ON EPO.ID_ENTE_PUBLICO=ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_UNIDAD_ADMINISTRATIVA=ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAO ON UAO.ID_UNIDAD_ADMINISTRATIVA=ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC " +
                "INNER JOIN CAT_ESTADOS_SERVICIOS_TIC CE ON CE.CVE_ESTADO_SERVICIO_TIC = ST.CVE_ESTADO_SERVICIO_TIC " +
                "WHERE ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC= ?1 AND ST.CVE_ESTATUS IN ('VIG','HIS') "+
                "ORDER BY ST.NUM_ORDEN_SERVICIO_TIC", resultClass = ServicioTicBean.class)

/*------------------------------------------------------------------------------*/
@NamedNativeQuery(name = "ServicioTicBean.getServiciosAllPorUnidad",
        query = "SELECT " +
                "    ST.ID_SERVICIO_TIC AS idServicioTic," +
                "    CTS.DESC_TIPO_SERVICIO_TIC AS tipoServicio," +
                "    CSTS.DESC_SUBTIPO_SERVICIO_TIC AS subTipoServicio," +
                "    ST.DESC_SERVICIO_TIC AS nombreServicio, " +
                "    EP.DESC_ENTE_PUBLICO_ABREVIADA AS nombreEntePublicoAdminAbrev," +
                "    EP.DESC_ENTE_PUBLICO AS nombreEntePublicoAdmin," +
                "    UA.DESC_UNIDAD_ADMINISTRATIVA_ABR AS nombreUnidadAdminAbrev," +
                "    UA.DESC_UNIDAD_ADMINISTRATIVA AS nombreUnidadAdmin," +
                "    EPO.DESC_ENTE_PUBLICO_ABREVIADA AS nombreEntePublicoOperaAbrev," +
                "    EPO.DESC_ENTE_PUBLICO AS nombreEntePublicoOpera," +
                "    UAO.DESC_UNIDAD_ADMINISTRATIVA_ABR AS nombreUnidadOperaAbrev," +
                "    UAO.DESC_UNIDAD_ADMINISTRATIVA AS nombreUnidadOpera," +
                "    CE.DESC_ESTADO_SERVICIO_TIC AS nombreEstadoServicio," +
                "    ST.CVE_ESTATUS AS cveEstatus " +
                "FROM SERVICIOS_TIC ST " +
                "INNER JOIN CAT_TIPO_SERVICIOS_TIC CTS ON CTS.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC " +
                "INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC CSTS ON CSTS.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EP ON EP.ID_ENTE_PUBLICO=ST.ID_ENTE_PUBLICO_ADM_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EPO ON EPO.ID_ENTE_PUBLICO=ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_UNIDAD_ADMINISTRATIVA=ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAO ON UAO.ID_UNIDAD_ADMINISTRATIVA=ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC " +
                "INNER JOIN CAT_ESTADOS_SERVICIOS_TIC CE ON CE.CVE_ESTADO_SERVICIO_TIC = ST.CVE_ESTADO_SERVICIO_TIC " +
                "WHERE (ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC in (?1) OR ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC in (?1)) AND ST.CVE_ESTATUS IN ('VIG','HIS') "+
                "ORDER BY ST.NUM_ORDEN_SERVICIO_TIC", resultClass = ServicioTicBean.class)

@NamedNativeQuery(name = "ServicioTicBean.getServiciosAdministradosPorUnidad",
        query = "SELECT " +
                "    ST.ID_SERVICIO_TIC AS idServicioTic," +
                "    CTS.DESC_TIPO_SERVICIO_TIC AS tipoServicio," +
                "    CSTS.DESC_SUBTIPO_SERVICIO_TIC AS subTipoServicio," +
                "    ST.DESC_SERVICIO_TIC AS nombreServicio, " +
                "    EP.DESC_ENTE_PUBLICO_ABREVIADA AS nombreEntePublicoAdminAbrev," +
                "    EP.DESC_ENTE_PUBLICO AS nombreEntePublicoAdmin," +
                "    UA.DESC_UNIDAD_ADMINISTRATIVA_ABR AS nombreUnidadAdminAbrev," +
                "    UA.DESC_UNIDAD_ADMINISTRATIVA AS nombreUnidadAdmin," +
                "    EPO.DESC_ENTE_PUBLICO_ABREVIADA AS nombreEntePublicoOperaAbrev," +
                "    EPO.DESC_ENTE_PUBLICO AS nombreEntePublicoOpera," +
                "    UAO.DESC_UNIDAD_ADMINISTRATIVA_ABR AS nombreUnidadOperaAbrev," +
                "    UAO.DESC_UNIDAD_ADMINISTRATIVA AS nombreUnidadOpera," +
                "    CE.DESC_ESTADO_SERVICIO_TIC AS nombreEstadoServicio," +
                "    ST.CVE_ESTATUS AS cveEstatus " +
                "FROM SERVICIOS_TIC ST " +
                "INNER JOIN CAT_TIPO_SERVICIOS_TIC CTS ON CTS.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC " +
                "INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC CSTS ON CSTS.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EP ON EP.ID_ENTE_PUBLICO=ST.ID_ENTE_PUBLICO_ADM_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EPO ON EPO.ID_ENTE_PUBLICO=ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_UNIDAD_ADMINISTRATIVA=ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAO ON UAO.ID_UNIDAD_ADMINISTRATIVA=ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC " +
                "INNER JOIN CAT_ESTADOS_SERVICIOS_TIC CE ON CE.CVE_ESTADO_SERVICIO_TIC = ST.CVE_ESTADO_SERVICIO_TIC " +
                "WHERE ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC in (?1) AND ST.CVE_ESTATUS IN ('VIG','HIS') "+
                "ORDER BY ST.NUM_ORDEN_SERVICIO_TIC", resultClass = ServicioTicBean.class)

@NamedNativeQuery(name = "ServicioTicBean.getServiciosOperadosPorUnidad",
        query = "SELECT " +
                "    ST.ID_SERVICIO_TIC AS idServicioTic," +
                "    CTS.DESC_TIPO_SERVICIO_TIC AS tipoServicio," +
                "    CSTS.DESC_SUBTIPO_SERVICIO_TIC AS subTipoServicio," +
                "    ST.DESC_SERVICIO_TIC AS nombreServicio, " +
                "    EP.DESC_ENTE_PUBLICO_ABREVIADA AS nombreEntePublicoAdminAbrev," +
                "    EP.DESC_ENTE_PUBLICO AS nombreEntePublicoAdmin," +
                "    UA.DESC_UNIDAD_ADMINISTRATIVA_ABR AS nombreUnidadAdminAbrev," +
                "    UA.DESC_UNIDAD_ADMINISTRATIVA AS nombreUnidadAdmin," +
                "    EPO.DESC_ENTE_PUBLICO_ABREVIADA AS nombreEntePublicoOperaAbrev," +
                "    EPO.DESC_ENTE_PUBLICO AS nombreEntePublicoOpera," +
                "    UAO.DESC_UNIDAD_ADMINISTRATIVA_ABR AS nombreUnidadOperaAbrev," +
                "    UAO.DESC_UNIDAD_ADMINISTRATIVA AS nombreUnidadOpera," +
                "    CE.DESC_ESTADO_SERVICIO_TIC AS nombreEstadoServicio," +
                "    ST.CVE_ESTATUS AS cveEstatus " +
                "FROM SERVICIOS_TIC ST " +
                "INNER JOIN CAT_TIPO_SERVICIOS_TIC CTS ON CTS.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC " +
                "INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC CSTS ON CSTS.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EP ON EP.ID_ENTE_PUBLICO=ST.ID_ENTE_PUBLICO_ADM_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EPO ON EPO.ID_ENTE_PUBLICO=ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_UNIDAD_ADMINISTRATIVA=ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAO ON UAO.ID_UNIDAD_ADMINISTRATIVA=ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC " +
                "INNER JOIN CAT_ESTADOS_SERVICIOS_TIC CE ON CE.CVE_ESTADO_SERVICIO_TIC = ST.CVE_ESTADO_SERVICIO_TIC " +
                "WHERE ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC in (?1) AND ST.CVE_ESTATUS IN ('VIG','HIS') "+
                "ORDER BY ST.NUM_ORDEN_SERVICIO_TIC", resultClass = ServicioTicBean.class)


@Data
@Entity
@Table(name="SERVICIOS_TIC_BEAN")

public class ServicioTicBean  implements Serializable {

    @Id
    private Long idServicioTic;

    private String tipoServicio;

    private String subTipoServicio;

    private String nombreServicio;

    private String nombreEntePublicoAdminAbrev;

    private String nombreEntePublicoAdmin;

    private String nombreUnidadAdminAbrev;

    private String nombreUnidadAdmin;

    private String nombreEntePublicoOperaAbrev;

    private String nombreEntePublicoOpera;

    private String nombreUnidadOperaAbrev;

    private String nombreUnidadOpera;

    private String nombreEstadoServicio;

    private String cveEstatus;

}
