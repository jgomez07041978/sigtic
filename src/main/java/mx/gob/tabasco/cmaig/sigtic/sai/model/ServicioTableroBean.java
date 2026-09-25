package mx.gob.tabasco.cmaig.sigtic.sai.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.io.Serializable;

@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoTipoEnte",
        query = "SELECT  " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,  " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin, " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin, " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera, " +
                "    SUM(TB.historicoOpera) AS historicoOpera, " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico " +
                "FROM (SELECT TE.CVE_TIPO_ENTE_PUBLICO as idTabla, null nombreAbrev, TE.DESC_TIPO_ENTE_PUBLICO as nombre,   " +
                "                                TE.DESC_GRUPO_ENTE_PUBLICO as grupo,  " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,   " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,   " +
                "                                0 as vigenteOpera, " +
                "                                0 as historicoOpera, " +
                "                               null parent1, null idTipoServicio, null idSubTipoServicio, null idEntePublico, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')   " +
                "                                GROUP BY TE.CVE_TIPO_ENTE_PUBLICO, TE.DESC_TIPO_ENTE_PUBLICO, TE.DESC_GRUPO_ENTE_PUBLICO,TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "union all   " +
                "SELECT TE.CVE_TIPO_ENTE_PUBLICO as idTabla, null nombreAbrev, TE.DESC_TIPO_ENTE_PUBLICO as nombre,   " +
                "                                TE.DESC_GRUPO_ENTE_PUBLICO as grupo,  " +
                "                                0 as vigenteAdmin, " +
                "                                0 as historicoAdmin, " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,   " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,   " +
                "                               null parent1, null idTipoServicio, null idSubTipoServicio, null idEntePublico, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')   " +
                "                                GROUP BY TE.CVE_TIPO_ENTE_PUBLICO, TE.DESC_TIPO_ENTE_PUBLICO, TE.DESC_GRUPO_ENTE_PUBLICO,TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "                                ) TB " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_TIPO_ENTE_PUBLICO " +
                "                                ORDER BY TB.NUM_ORDEN_TIPO_ENTE_PUBLICO", resultClass = ServicioTableroBean.class)

@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoTipoEntePorEnte",
        query = "SELECT  " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,  " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin, " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin, " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera, " +
                "    SUM(TB.historicoOpera) AS historicoOpera, " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico " +
                "FROM (SELECT TE.CVE_TIPO_ENTE_PUBLICO as idTabla, null nombreAbrev, TE.DESC_TIPO_ENTE_PUBLICO as nombre,   " +
                "                                TE.DESC_GRUPO_ENTE_PUBLICO as grupo,  " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,   " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,   " +
                "                                0 as vigenteOpera, " +
                "                                0 as historicoOpera, " +
                "                               null parent1, null idTipoServicio, null idSubTipoServicio, null idEntePublico, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
                "                                WHERE ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=?1 AND ST.CVE_ESTATUS IN ('VIG','HIS')   " +
                "                                GROUP BY TE.CVE_TIPO_ENTE_PUBLICO, TE.DESC_TIPO_ENTE_PUBLICO, TE.DESC_GRUPO_ENTE_PUBLICO,TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "union all   " +
                "SELECT TE.CVE_TIPO_ENTE_PUBLICO as idTabla, null nombreAbrev, TE.DESC_TIPO_ENTE_PUBLICO as nombre,   " +
                "                                TE.DESC_GRUPO_ENTE_PUBLICO as grupo,  " +
                "                                0 as vigenteAdmin, " +
                "                                0 as historicoAdmin, " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,   " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,   " +
                "                               null parent1, null idTipoServicio, null idSubTipoServicio, null idEntePublico, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
                "                                WHERE ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=?1 AND ST.CVE_ESTATUS IN ('VIG','HIS')   " +
                "                                GROUP BY TE.CVE_TIPO_ENTE_PUBLICO, TE.DESC_TIPO_ENTE_PUBLICO, TE.DESC_GRUPO_ENTE_PUBLICO,TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "                                ) TB " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_TIPO_ENTE_PUBLICO " +
                "                                ORDER BY TB.NUM_ORDEN_TIPO_ENTE_PUBLICO", resultClass = ServicioTableroBean.class)

@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoTipoEntePorEnteUnidad",
        query = "SELECT  " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,  " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin, " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin, " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera, " +
                "    SUM(TB.historicoOpera) AS historicoOpera, " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico " +
                "FROM (SELECT TE.CVE_TIPO_ENTE_PUBLICO as idTabla, null nombreAbrev, TE.DESC_TIPO_ENTE_PUBLICO as nombre,   " +
                "                                TE.DESC_GRUPO_ENTE_PUBLICO as grupo,  " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,   " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,   " +
                "                                0 as vigenteOpera, " +
                "                                0 as historicoOpera, " +
                "                               null parent1, null idTipoServicio, null idSubTipoServicio, null idEntePublico, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
                "                                WHERE ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=?1 AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN " +
                "                                                   (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
                "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "AND ST.CVE_ESTATUS IN ('VIG','HIS')   " +
                "                                GROUP BY TE.CVE_TIPO_ENTE_PUBLICO, TE.DESC_TIPO_ENTE_PUBLICO, TE.DESC_GRUPO_ENTE_PUBLICO,TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "union all   " +
                "SELECT TE.CVE_TIPO_ENTE_PUBLICO as idTabla, null nombreAbrev, TE.DESC_TIPO_ENTE_PUBLICO as nombre,   " +
                "                                TE.DESC_GRUPO_ENTE_PUBLICO as grupo,  " +
                "                                0 as vigenteAdmin, " +
                "                                0 as historicoAdmin, " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,   " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,   " +
                "                               null parent1, null idTipoServicio, null idSubTipoServicio, null idEntePublico, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
                "                                WHERE ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=?1 AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN " +
                "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
                "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "                               AND ST.CVE_ESTATUS IN ('VIG','HIS')   " +
                "                                GROUP BY TE.CVE_TIPO_ENTE_PUBLICO, TE.DESC_TIPO_ENTE_PUBLICO, TE.DESC_GRUPO_ENTE_PUBLICO,TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "                                ) TB " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_TIPO_ENTE_PUBLICO " +
                "                                ORDER BY TB.NUM_ORDEN_TIPO_ENTE_PUBLICO", resultClass = ServicioTableroBean.class)


@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoTipoEntePorEnteSubTipo",
        query = "SELECT  " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,  " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin, " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin, " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera, " +
                "    SUM(TB.historicoOpera) AS historicoOpera, " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico " +
                "FROM (SELECT TE.CVE_TIPO_ENTE_PUBLICO as idTabla, null nombreAbrev, TE.DESC_TIPO_ENTE_PUBLICO as nombre,   " +
                "                                TE.DESC_GRUPO_ENTE_PUBLICO as grupo,  " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,   " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,   " +
                "                                0 as vigenteOpera, " +
                "                                0 as historicoOpera, " +
                "                               null parent1, null idTipoServicio, null idSubTipoServicio, null idEntePublico, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
                "                                INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC=ST.ID_SUBTIPO_SERVICIO_TIC  " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')   " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1  AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN ?3 " +
                "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?2 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)" +
                "                                GROUP BY TE.CVE_TIPO_ENTE_PUBLICO, TE.DESC_TIPO_ENTE_PUBLICO, TE.DESC_GRUPO_ENTE_PUBLICO,TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "union all   " +
                "SELECT TE.CVE_TIPO_ENTE_PUBLICO as idTabla, null nombreAbrev, TE.DESC_TIPO_ENTE_PUBLICO as nombre,   " +
                "                                TE.DESC_GRUPO_ENTE_PUBLICO as grupo,  " +
                "                                0 as vigenteAdmin, " +
                "                                0 as historicoAdmin, " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,   " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,   " +
                "                               null parent1, null idTipoServicio, null idSubTipoServicio, null idEntePublico, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
                "                                INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC=ST.ID_SUBTIPO_SERVICIO_TIC " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')   " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN ?3  " +
                "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?2 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)" +
                "                                GROUP BY TE.CVE_TIPO_ENTE_PUBLICO, TE.DESC_TIPO_ENTE_PUBLICO, TE.DESC_GRUPO_ENTE_PUBLICO,TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "                                ) TB " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_TIPO_ENTE_PUBLICO " +
                "                                ORDER BY TB.NUM_ORDEN_TIPO_ENTE_PUBLICO", resultClass = ServicioTableroBean.class)

@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoTipoEntePorUnidadSubTipo",
        query = "SELECT  " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,  " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin, " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin, " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera, " +
                "    SUM(TB.historicoOpera) AS historicoOpera, " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico " +
                "FROM (SELECT TE.CVE_TIPO_ENTE_PUBLICO as idTabla, null nombreAbrev, TE.DESC_TIPO_ENTE_PUBLICO as nombre,   " +
                "                                TE.DESC_GRUPO_ENTE_PUBLICO as grupo,  " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,   " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,   " +
                "                                0 as vigenteOpera, " +
                "                                0 as historicoOpera, " +
                "                               null parent1, null idTipoServicio, null idSubTipoServicio, null idEntePublico, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
                "                                INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC=ST.ID_SUBTIPO_SERVICIO_TIC  " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')   " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1  AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN " +
                "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?3 " +
                "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?3 " +
                "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?2 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)" +
                "                                GROUP BY TE.CVE_TIPO_ENTE_PUBLICO, TE.DESC_TIPO_ENTE_PUBLICO, TE.DESC_GRUPO_ENTE_PUBLICO,TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "union all   " +
                "SELECT TE.CVE_TIPO_ENTE_PUBLICO as idTabla, null nombreAbrev, TE.DESC_TIPO_ENTE_PUBLICO as nombre,   " +
                "                                TE.DESC_GRUPO_ENTE_PUBLICO as grupo,  " +
                "                                0 as vigenteAdmin, " +
                "                                0 as historicoAdmin, " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,   " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,   " +
                "                               null parent1, null idTipoServicio, null idSubTipoServicio, null idEntePublico, TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
                "                                INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC=ST.ID_SUBTIPO_SERVICIO_TIC " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')   " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN " +
                "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?3 " +
                "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?3 " +
                "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?2 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)" +
                "                                GROUP BY TE.CVE_TIPO_ENTE_PUBLICO, TE.DESC_TIPO_ENTE_PUBLICO, TE.DESC_GRUPO_ENTE_PUBLICO,TE.NUM_ORDEN_TIPO_ENTE_PUBLICO  " +
                "                                ) TB " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_TIPO_ENTE_PUBLICO " +
                "                                ORDER BY TB.NUM_ORDEN_TIPO_ENTE_PUBLICO", resultClass = ServicioTableroBean.class)


/*==============================================================================================*/

@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoAllEnte",
        query = "SELECT   " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,   " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin,  " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin,  " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera,  " +
                "    SUM(TB.historicoOpera) AS historicoOpera,  " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico  " +
                "FROM (SELECT CE.ID_ENTE_PUBLICO as idTabla,  NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO_ABREVIADA as nombreAbrev,     " +
                "                        NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO as nombre, null  as grupo,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,    " +
                "                                0 as vigenteOpera,  " +
                "                                0 as historicoOpera,    " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO as parent1,    " +
                "                               null idTipoServicio, null idSubTipoServicio, null idEntePublico,  " +
                "                               CE.NUM_ORDEN_ENTE_PUBLICO   " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE      " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO     " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO     " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')     " +
                "                                GROUP BY CE.ID_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO_ABREVIADA, CE.CVE_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO, CE.NUM_ORDEN_ENTE_PUBLICO,   " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO                             " +
                "union all  " +
                "SELECT CE.ID_ENTE_PUBLICO as idTabla,  NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO_ABREVIADA as nombreAbrev,     " +
                "                         NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO as nombre, null  as grupo,    " +
                "                                0 as vigenteAdmin,  " +
                "                                0 as historicoAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,    " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO as parent1,    " +
                "                               null idTipoServicio, null idSubTipoServicio, null idEntePublico,  " +
                "                               CE.NUM_ORDEN_ENTE_PUBLICO   " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE      " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO     " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO     " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')     " +
                "                                GROUP BY CE.ID_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO_ABREVIADA, CE.CVE_ENTE_PUBLICO,CE.DESC_ENTE_PUBLICO, CE.NUM_ORDEN_ENTE_PUBLICO,   " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO  ) TB  " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_ENTE_PUBLICO  " +
                "                                ORDER BY TB.NUM_ORDEN_ENTE_PUBLICO  " +
                "                     ", resultClass = ServicioTableroBean.class)

@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradPorEnte",
        query = "SELECT   " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,   " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin,  " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin,  " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera,  " +
                "    SUM(TB.historicoOpera) AS historicoOpera,  " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico  " +
                "FROM (SELECT CE.ID_ENTE_PUBLICO as idTabla,  NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO_ABREVIADA as nombreAbrev,     " +
                "                        NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO as nombre, null  as grupo,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,    " +
                "                                0 as vigenteOpera,  " +
                "                                0 as historicoOpera,    " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO as parent1,    " +
                "                               null idTipoServicio, null idSubTipoServicio, null idEntePublico,  " +
                "                               CE.NUM_ORDEN_ENTE_PUBLICO   " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE      " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO     " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO     " +
                "                                WHERE  CE.ID_ENTE_PUBLICO=?1 AND ST.CVE_ESTATUS IN ('VIG','HIS')     " +
                "                                GROUP BY CE.ID_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO_ABREVIADA, CE.CVE_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO, CE.NUM_ORDEN_ENTE_PUBLICO,   " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO                             " +
                "union all  " +
                "SELECT CE.ID_ENTE_PUBLICO as idTabla,  NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO_ABREVIADA as nombreAbrev,     " +
                "                         NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO as nombre, null  as grupo,    " +
                "                                0 as vigenteAdmin,  " +
                "                                0 as historicoAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,    " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO as parent1,    " +
                "                               null idTipoServicio, null idSubTipoServicio, null idEntePublico,  " +
                "                               CE.NUM_ORDEN_ENTE_PUBLICO   " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE      " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO     " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO     " +
                "                                WHERE CE.ID_ENTE_PUBLICO=?1 AND ST.CVE_ESTATUS IN ('VIG','HIS')     " +
                "                                GROUP BY CE.ID_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO_ABREVIADA, CE.CVE_ENTE_PUBLICO,CE.DESC_ENTE_PUBLICO, CE.NUM_ORDEN_ENTE_PUBLICO,   " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO  ) TB  " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_ENTE_PUBLICO  " +
                "                                ORDER BY TB.NUM_ORDEN_ENTE_PUBLICO  " +
                "                     ", resultClass = ServicioTableroBean.class)

@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradPorEnteUnidad",
        query = "SELECT   " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,   " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin,  " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin,  " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera,  " +
                "    SUM(TB.historicoOpera) AS historicoOpera,  " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico  " +
                "FROM (SELECT CE.ID_ENTE_PUBLICO as idTabla,  NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO_ABREVIADA as nombreAbrev,     " +
                "                        NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO as nombre, null  as grupo,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,    " +
                "                                0 as vigenteOpera,  " +
                "                                0 as historicoOpera,    " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO as parent1,    " +
                "                               null idTipoServicio, null idSubTipoServicio, null idEntePublico,  " +
                "                               CE.NUM_ORDEN_ENTE_PUBLICO   " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE      " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO     " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO     " +
                "                                WHERE  CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN " +
                "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
                "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "                                   AND ST.CVE_ESTATUS IN ('VIG','HIS')     " +
                "                                GROUP BY CE.ID_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO_ABREVIADA, CE.CVE_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO, CE.NUM_ORDEN_ENTE_PUBLICO,   " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO                             " +
                "union all  " +
                "SELECT CE.ID_ENTE_PUBLICO as idTabla,  NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO_ABREVIADA as nombreAbrev,     " +
                "                         NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO as nombre, null  as grupo,    " +
                "                                0 as vigenteAdmin,  " +
                "                                0 as historicoAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,    " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO as parent1,    " +
                "                               null idTipoServicio, null idSubTipoServicio, null idEntePublico,  " +
                "                               CE.NUM_ORDEN_ENTE_PUBLICO   " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE      " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO     " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO     " +
                "                                WHERE  CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN " +
                "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
                "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "                                   AND ST.CVE_ESTATUS IN ('VIG','HIS')     " +
                "                                GROUP BY CE.ID_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO_ABREVIADA, CE.CVE_ENTE_PUBLICO,CE.DESC_ENTE_PUBLICO, CE.NUM_ORDEN_ENTE_PUBLICO,   " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO  ) TB  " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_ENTE_PUBLICO  " +
                "                                ORDER BY TB.NUM_ORDEN_ENTE_PUBLICO  " +
                "                     ", resultClass = ServicioTableroBean.class)


@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoAllService",
        query = "SELECT    " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,    " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin,   " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin,   " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera,   " +
                "    SUM(TB.historicoOpera) AS historicoOpera,   " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico   " +
                "FROM (SELECT CT.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, CT.DESC_TIPO_SERVICIO_TIC_ABREV as nombreAbrev,       " +
                "                        CT.DESC_TIPO_SERVICIO_TIC as nombre, null  as grupo,      " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,       " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,     " +
                "                                 0 as vigenteOpera,   " +
                "                                0 as historicoOpera,      " +
                "                                CE.ID_ENTE_PUBLICO as parent1,      " +
                "                               CT.ID_TIPO_SERVICIO_TIC idTipoServicio, null idSubTipoServicio, CE.ID_ENTE_PUBLICO idEntePublico,   " +
                "                               CT.NUM_ORDEN_TIPO_SERVICIO_TIC    " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE        " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO       " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO       " +
                "                                INNER JOIN CAT_TIPO_SERVICIOS_TIC CT ON CT.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC       " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')       " +
                "                                GROUP BY CT.ID_TIPO_SERVICIO_TIC, CT.DESC_TIPO_SERVICIO_TIC_ABREV, CT.DESC_TIPO_SERVICIO_TIC,      " +
                "                                CT.NUM_ORDEN_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO   " +
                "                                union all   " +
                "                                SELECT CT.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, CT.DESC_TIPO_SERVICIO_TIC_ABREV as nombreAbrev,       " +
                "                        CT.DESC_TIPO_SERVICIO_TIC as nombre, null  as grupo,      " +
                "                                0 as vigenteAdmin,   " +
                "                                0 as historicoAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,       " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,       " +
                "                                CE.ID_ENTE_PUBLICO as parent1,      " +
                "                               CT.ID_TIPO_SERVICIO_TIC idTipoServicio, null idSubTipoServicio, CE.ID_ENTE_PUBLICO idEntePublico,   " +
                "                               CT.NUM_ORDEN_TIPO_SERVICIO_TIC    " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE        " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO       " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO       " +
                "                                INNER JOIN CAT_TIPO_SERVICIOS_TIC CT ON CT.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC       " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')       " +
                "                                GROUP BY CT.ID_TIPO_SERVICIO_TIC, CT.DESC_TIPO_SERVICIO_TIC_ABREV, CT.DESC_TIPO_SERVICIO_TIC,      " +
                "                                CT.NUM_ORDEN_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO ) TB   " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_TIPO_SERVICIO_TIC   " +
                "                                ORDER BY TB.NUM_ORDEN_TIPO_SERVICIO_TIC    ", resultClass = ServicioTableroBean.class)

@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoAllServicePorEnte",
        query = "SELECT    " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,    " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin,   " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin,   " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera,   " +
                "    SUM(TB.historicoOpera) AS historicoOpera,   " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico   " +
                "FROM (SELECT CT.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, CT.DESC_TIPO_SERVICIO_TIC_ABREV as nombreAbrev,       " +
                "                        CT.DESC_TIPO_SERVICIO_TIC as nombre, null  as grupo,      " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,       " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,     " +
                "                                 0 as vigenteOpera,   " +
                "                                0 as historicoOpera,      " +
                "                                CE.ID_ENTE_PUBLICO as parent1,      " +
                "                               CT.ID_TIPO_SERVICIO_TIC idTipoServicio, null idSubTipoServicio, CE.ID_ENTE_PUBLICO idEntePublico,   " +
                "                               CT.NUM_ORDEN_TIPO_SERVICIO_TIC    " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE        " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO       " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO       " +
                "                                INNER JOIN CAT_TIPO_SERVICIOS_TIC CT ON CT.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC       " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')       " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1  " +
                "                                GROUP BY CT.ID_TIPO_SERVICIO_TIC, CT.DESC_TIPO_SERVICIO_TIC_ABREV, CT.DESC_TIPO_SERVICIO_TIC,      " +
                "                                CT.NUM_ORDEN_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO   " +
                "                                union all   " +
                "                                SELECT CT.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, CT.DESC_TIPO_SERVICIO_TIC_ABREV as nombreAbrev,       " +
                "                        CT.DESC_TIPO_SERVICIO_TIC as nombre, null  as grupo,      " +
                "                                0 as vigenteAdmin,   " +
                "                                0 as historicoAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,       " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,       " +
                "                                CE.ID_ENTE_PUBLICO as parent1,      " +
                "                               CT.ID_TIPO_SERVICIO_TIC idTipoServicio, null idSubTipoServicio, CE.ID_ENTE_PUBLICO idEntePublico,   " +
                "                               CT.NUM_ORDEN_TIPO_SERVICIO_TIC    " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE        " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO       " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO       " +
                "                                INNER JOIN CAT_TIPO_SERVICIOS_TIC CT ON CT.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC       " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')       " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1  " +
                "                                GROUP BY CT.ID_TIPO_SERVICIO_TIC, CT.DESC_TIPO_SERVICIO_TIC_ABREV, CT.DESC_TIPO_SERVICIO_TIC,      " +
                "                                CT.NUM_ORDEN_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO ) TB   " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_TIPO_SERVICIO_TIC   " +
                "                                ORDER BY TB.NUM_ORDEN_TIPO_SERVICIO_TIC    ", resultClass = ServicioTableroBean.class)

@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoAllServicePorEnteUnidad",
        query = "SELECT    " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,    " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin,   " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin,   " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera,   " +
                "    SUM(TB.historicoOpera) AS historicoOpera,   " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico   " +
                "FROM (SELECT CT.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, CT.DESC_TIPO_SERVICIO_TIC_ABREV as nombreAbrev,       " +
                "                        CT.DESC_TIPO_SERVICIO_TIC as nombre, null  as grupo,      " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,       " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,     " +
                "                                 0 as vigenteOpera,   " +
                "                                0 as historicoOpera,      " +
                "                                CE.ID_ENTE_PUBLICO as parent1,      " +
                "                               CT.ID_TIPO_SERVICIO_TIC idTipoServicio, null idSubTipoServicio, CE.ID_ENTE_PUBLICO idEntePublico,   " +
                "                               CT.NUM_ORDEN_TIPO_SERVICIO_TIC    " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE        " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO       " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO       " +
                "                                INNER JOIN CAT_TIPO_SERVICIOS_TIC CT ON CT.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC       " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')       " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN " +
                "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
                "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "                                GROUP BY CT.ID_TIPO_SERVICIO_TIC, CT.DESC_TIPO_SERVICIO_TIC_ABREV, CT.DESC_TIPO_SERVICIO_TIC,      " +
                "                                CT.NUM_ORDEN_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO   " +
                "                                union all   " +
                "                                SELECT CT.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, CT.DESC_TIPO_SERVICIO_TIC_ABREV as nombreAbrev,       " +
                "                        CT.DESC_TIPO_SERVICIO_TIC as nombre, null  as grupo,      " +
                "                                0 as vigenteAdmin,   " +
                "                                0 as historicoAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,       " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,       " +
                "                                CE.ID_ENTE_PUBLICO as parent1,      " +
                "                               CT.ID_TIPO_SERVICIO_TIC idTipoServicio, null idSubTipoServicio, CE.ID_ENTE_PUBLICO idEntePublico,   " +
                "                               CT.NUM_ORDEN_TIPO_SERVICIO_TIC    " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE        " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO       " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO       " +
                "                                INNER JOIN CAT_TIPO_SERVICIOS_TIC CT ON CT.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC       " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')       " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN " +
                "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
                "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "                                GROUP BY CT.ID_TIPO_SERVICIO_TIC, CT.DESC_TIPO_SERVICIO_TIC_ABREV, CT.DESC_TIPO_SERVICIO_TIC,      " +
                "                                CT.NUM_ORDEN_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO ) TB   " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_TIPO_SERVICIO_TIC   " +
                "                                ORDER BY TB.NUM_ORDEN_TIPO_SERVICIO_TIC    ", resultClass = ServicioTableroBean.class)



@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoAllSubService",
        query = "SELECT     " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,     " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin,    " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin,    " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera,    " +
                "    SUM(TB.historicoOpera) AS historicoOpera,    " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico    " +
                "FROM (SELECT SUB.ID_SUBTIPO_SERVICIO_TIC||SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR as nombreAbrev,            " +
                "                                        SUB.DESC_SUBTIPO_SERVICIO_TIC as nombre, null  as grupo,           " +
                "                                        sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,        " +
                "                                        sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,    " +
                "                                                0 as vigenteOpera,    " +
                "                                                0 as historicoOpera,        " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as parent1,        " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC as idTipoServicio, SUB.ID_SUBTIPO_SERVICIO_TIC AS idSubTipoServicio, CE.ID_ENTE_PUBLICO AS idEntePublico,    " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC        " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE             " +
                "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO            " +
                "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO            " +
                "                                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC         " +
                "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')         " +
                "                                                GROUP BY SUB.ID_SUBTIPO_SERVICIO_TIC, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR, SUB.DESC_SUBTIPO_SERVICIO_TIC,           " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC, SUB.ID_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO      " +
                "                                                union all    " +
                "                                                SELECT SUB.ID_SUBTIPO_SERVICIO_TIC||SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR as nombreAbrev,            " +
                "                                        SUB.DESC_SUBTIPO_SERVICIO_TIC as nombre, null  as grupo,           " +
                "                                0 as vigenteAdmin,    " +
                "                                0 as historicoAdmin,     " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,        " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,           " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as parent1,        " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC as idTipoServicio, SUB.ID_SUBTIPO_SERVICIO_TIC AS idSubTipoServicio, CE.ID_ENTE_PUBLICO AS idEntePublico,    " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC        " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE             " +
                "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO            " +
                "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO            " +
                "                                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC         " +
                "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')         " +
                "                                                GROUP BY SUB.ID_SUBTIPO_SERVICIO_TIC, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR, SUB.DESC_SUBTIPO_SERVICIO_TIC,           " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC, SUB.ID_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO) TB    " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC    " +
                "                                ORDER BY TB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC    ", resultClass = ServicioTableroBean.class)

@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoAllSubServicePorEnte",
        query = "SELECT     " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,     " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin,    " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin,    " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera,    " +
                "    SUM(TB.historicoOpera) AS historicoOpera,    " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico    " +
                "FROM (SELECT SUB.ID_SUBTIPO_SERVICIO_TIC||SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR as nombreAbrev,            " +
                "                                        SUB.DESC_SUBTIPO_SERVICIO_TIC as nombre, null  as grupo,           " +
                "                                        sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,        " +
                "                                        sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,    " +
                "                                                0 as vigenteOpera,    " +
                "                                                0 as historicoOpera,        " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as parent1,        " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC as idTipoServicio, SUB.ID_SUBTIPO_SERVICIO_TIC AS idSubTipoServicio, CE.ID_ENTE_PUBLICO AS idEntePublico,    " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC        " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE             " +
                "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO            " +
                "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO            " +
                "                                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC         " +
                "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')         " +
                "                                                   AND CE.ID_ENTE_PUBLICO=?1  " +
                "                                                GROUP BY SUB.ID_SUBTIPO_SERVICIO_TIC, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR, SUB.DESC_SUBTIPO_SERVICIO_TIC,           " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC, SUB.ID_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO      " +
                "                                                union all    " +
                "                                                SELECT SUB.ID_SUBTIPO_SERVICIO_TIC||SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR as nombreAbrev,            " +
                "                                        SUB.DESC_SUBTIPO_SERVICIO_TIC as nombre, null  as grupo,           " +
                "                                0 as vigenteAdmin,    " +
                "                                0 as historicoAdmin,     " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,        " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,           " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as parent1,        " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC as idTipoServicio, SUB.ID_SUBTIPO_SERVICIO_TIC AS idSubTipoServicio, CE.ID_ENTE_PUBLICO AS idEntePublico,    " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC        " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE             " +
                "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO            " +
                "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO            " +
                "                                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC         " +
                "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')         " +
                "                                                   AND CE.ID_ENTE_PUBLICO=?1  " +
                "                                                GROUP BY SUB.ID_SUBTIPO_SERVICIO_TIC, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR, SUB.DESC_SUBTIPO_SERVICIO_TIC,           " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC, SUB.ID_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO) TB    " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC    " +
                "                                ORDER BY TB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC    ", resultClass = ServicioTableroBean.class)

@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoAllSubServicePorEnteUnidad",
        query = "SELECT     " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,     " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin,    " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin,    " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera,    " +
                "    SUM(TB.historicoOpera) AS historicoOpera,    " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico    " +
                "FROM (SELECT SUB.ID_SUBTIPO_SERVICIO_TIC||SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR as nombreAbrev,            " +
                "                                        SUB.DESC_SUBTIPO_SERVICIO_TIC as nombre, null  as grupo,           " +
                "                                        sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,        " +
                "                                        sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,    " +
                "                                                0 as vigenteOpera,    " +
                "                                                0 as historicoOpera,        " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as parent1,        " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC as idTipoServicio, SUB.ID_SUBTIPO_SERVICIO_TIC AS idSubTipoServicio, CE.ID_ENTE_PUBLICO AS idEntePublico,    " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC        " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE             " +
                "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO            " +
                "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO            " +
                "                                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC         " +
                "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')         " +
                "                                                   AND CE.ID_ENTE_PUBLICO=?1  AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN " +
                "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
                "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "                                                GROUP BY SUB.ID_SUBTIPO_SERVICIO_TIC, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR, SUB.DESC_SUBTIPO_SERVICIO_TIC,           " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC, SUB.ID_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO      " +
                "                                                union all    " +
                "                                                SELECT SUB.ID_SUBTIPO_SERVICIO_TIC||SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR as nombreAbrev,            " +
                "                                        SUB.DESC_SUBTIPO_SERVICIO_TIC as nombre, null  as grupo,           " +
                "                                0 as vigenteAdmin,    " +
                "                                0 as historicoAdmin,     " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,        " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,           " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as parent1,        " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC as idTipoServicio, SUB.ID_SUBTIPO_SERVICIO_TIC AS idSubTipoServicio, CE.ID_ENTE_PUBLICO AS idEntePublico,    " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC        " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE             " +
                "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO            " +
                "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO            " +
                "                                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC         " +
                "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')         " +
                "                                                   AND CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN " +
                "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
                "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "                                                GROUP BY SUB.ID_SUBTIPO_SERVICIO_TIC, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR, SUB.DESC_SUBTIPO_SERVICIO_TIC,           " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC, SUB.ID_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO) TB    " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC    " +
                "                                ORDER BY TB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC    ", resultClass = ServicioTableroBean.class)


/*----------------------------------------------------------------------------------------*/
/*
@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoAllEntePorEnte",
        query = "SELECT   " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,   " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin,  " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin,  " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera,  " +
                "    SUM(TB.historicoOpera) AS historicoOpera,  " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico  " +
                "FROM (SELECT CE.ID_ENTE_PUBLICO as idTabla, CE.DESC_ENTE_PUBLICO_ABREVIADA as nombreAbrev,     " +
                "                        CE.DESC_ENTE_PUBLICO as nombre, null  as grupo,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,    " +
                "                                0 as vigenteOpera,  " +
                "                                0 as historicoOpera,    " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO as parent1,    " +
                "                               null idTipoServicio, null idSubTipoServicio, null idEntePublico,  " +
                "                               CE.NUM_ORDEN_ENTE_PUBLICO   " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE      " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO     " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO     " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')     " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1  " +
                "                                GROUP BY CE.ID_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO_ABREVIADA, CE.DESC_ENTE_PUBLICO, CE.NUM_ORDEN_ENTE_PUBLICO,   " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO                             " +
                "union all  " +
                "SELECT CE.ID_ENTE_PUBLICO as idTabla, CE.DESC_ENTE_PUBLICO_ABREVIADA as nombreAbrev,     " +
                "                        CE.DESC_ENTE_PUBLICO as nombre, null  as grupo,    " +
                "                                0 as vigenteAdmin,  " +
                "                                0 as historicoAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,    " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO as parent1,    " +
                "                               null idTipoServicio, null idSubTipoServicio, null idEntePublico,  " +
                "                               CE.NUM_ORDEN_ENTE_PUBLICO   " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE      " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO     " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO     " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')     " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1  " +
                "                                GROUP BY CE.ID_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO_ABREVIADA, CE.DESC_ENTE_PUBLICO, CE.NUM_ORDEN_ENTE_PUBLICO,   " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO  ) TB  " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_ENTE_PUBLICO  " +
                "                                ORDER BY TB.NUM_ORDEN_ENTE_PUBLICO  " +
                "                     ", resultClass = ServicioTableroBean.class)

*/

/*----------------------------------------------------------------*/

@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoAllEntePorEnteSubTipo",
        query = "SELECT   " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,   " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin,  " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin,  " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera,  " +
                "    SUM(TB.historicoOpera) AS historicoOpera,  " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico  " +
                "FROM (SELECT CE.ID_ENTE_PUBLICO as idTabla,  NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO_ABREVIADA as nombreAbrev,     " +
                "                        NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO as nombre, null  as grupo,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,    " +
                "                                0 as vigenteOpera,  " +
                "                                0 as historicoOpera,    " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO as parent1,    " +
                "                               null idTipoServicio, null idSubTipoServicio, null idEntePublico,  " +
                "                               CE.NUM_ORDEN_ENTE_PUBLICO   " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE      " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO     " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO     " +
                "                                INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC=ST.ID_SUBTIPO_SERVICIO_TIC " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')     " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN ?3  " +
                "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?2 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)" +
                "                                GROUP BY CE.ID_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO_ABREVIADA, CE.CVE_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO, CE.NUM_ORDEN_ENTE_PUBLICO,   " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO                             " +
                "union all  " +
                "SELECT CE.ID_ENTE_PUBLICO as idTabla,  NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO_ABREVIADA as nombreAbrev,     " +
                "                         NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO as nombre, null  as grupo,    " +
                "                                0 as vigenteAdmin,  " +
                "                                0 as historicoAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,    " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO as parent1,    " +
                "                               null idTipoServicio, null idSubTipoServicio, null idEntePublico,  " +
                "                               CE.NUM_ORDEN_ENTE_PUBLICO   " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE      " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO     " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO     " +
                "                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC=ST.ID_SUBTIPO_SERVICIO_TIC " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')     " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1  AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN ?3 " +
                "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?2 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)" +
                "                                GROUP BY CE.ID_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO_ABREVIADA, CE.CVE_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO, CE.NUM_ORDEN_ENTE_PUBLICO,   " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO  ) TB  " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_ENTE_PUBLICO  " +
                "                                ORDER BY TB.NUM_ORDEN_ENTE_PUBLICO  " +
                "                     ", resultClass = ServicioTableroBean.class)

@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoAllEntePorEnteSubTipoUnidad",
        query = "SELECT   " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,   " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin,  " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin,  " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera,  " +
                "    SUM(TB.historicoOpera) AS historicoOpera,  " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico  " +
                "FROM (SELECT CE.ID_ENTE_PUBLICO as idTabla,  NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO_ABREVIADA as nombreAbrev,     " +
                "                        NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO as nombre, null  as grupo,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,    " +
                "                                0 as vigenteOpera,  " +
                "                                0 as historicoOpera,    " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO as parent1,    " +
                "                               null idTipoServicio, null idSubTipoServicio, null idEntePublico,  " +
                "                               CE.NUM_ORDEN_ENTE_PUBLICO   " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE      " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO     " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO     " +
                "                                INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC=ST.ID_SUBTIPO_SERVICIO_TIC " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')     " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN " +
                "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?3 " +
                "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?3 " +
                "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?2 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)" +
                "                                GROUP BY CE.ID_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO_ABREVIADA, CE.CVE_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO, CE.NUM_ORDEN_ENTE_PUBLICO,   " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO                             " +
                "union all  " +
                "SELECT CE.ID_ENTE_PUBLICO as idTabla,  NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO_ABREVIADA as nombreAbrev,     " +
                "                         NVL(CE.CVE_ENTE_PUBLICO,'')||' - '||CE.DESC_ENTE_PUBLICO as nombre, null  as grupo,    " +
                "                                0 as vigenteAdmin,  " +
                "                                0 as historicoAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,    " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO as parent1,    " +
                "                               null idTipoServicio, null idSubTipoServicio, null idEntePublico,  " +
                "                               CE.NUM_ORDEN_ENTE_PUBLICO   " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE      " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO     " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO     " +
                "                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC=ST.ID_SUBTIPO_SERVICIO_TIC " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')     " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1  AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN " +
                "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?3 " +
                "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?3 " +
                "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?2 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)" +
                "                                GROUP BY CE.ID_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO_ABREVIADA, CE.CVE_ENTE_PUBLICO, CE.DESC_ENTE_PUBLICO, CE.NUM_ORDEN_ENTE_PUBLICO,   " +
                "                                TE.CVE_TIPO_ENTE_PUBLICO  ) TB  " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_ENTE_PUBLICO  " +
                "                                ORDER BY TB.NUM_ORDEN_ENTE_PUBLICO  " +
                "                     ", resultClass = ServicioTableroBean.class)

@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoAllServicePorEnteSubTipo",
        query = "SELECT    " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,    " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin,   " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin,   " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera,   " +
                "    SUM(TB.historicoOpera) AS historicoOpera,   " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico   " +
                "FROM (SELECT CT.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, CT.DESC_TIPO_SERVICIO_TIC_ABREV as nombreAbrev,       " +
                "                        CT.DESC_TIPO_SERVICIO_TIC as nombre, null  as grupo,      " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,       " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,     " +
                "                                 0 as vigenteOpera,   " +
                "                                0 as historicoOpera,      " +
                "                                CE.ID_ENTE_PUBLICO as parent1,      " +
                "                               CT.ID_TIPO_SERVICIO_TIC idTipoServicio, null idSubTipoServicio, CE.ID_ENTE_PUBLICO idEntePublico,   " +
                "                               CT.NUM_ORDEN_TIPO_SERVICIO_TIC    " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE        " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO       " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO       " +
                "                                INNER JOIN CAT_TIPO_SERVICIOS_TIC CT ON CT.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC       " +
                "                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC=ST.ID_SUBTIPO_SERVICIO_TIC " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')       " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1  AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN ?3 " +
                "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?2 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)" +
                "                                GROUP BY CT.ID_TIPO_SERVICIO_TIC, CT.DESC_TIPO_SERVICIO_TIC_ABREV, CT.DESC_TIPO_SERVICIO_TIC,      " +
                "                                CT.NUM_ORDEN_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO   " +
                "                                union all   " +
                "                                SELECT CT.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, CT.DESC_TIPO_SERVICIO_TIC_ABREV as nombreAbrev,       " +
                "                        CT.DESC_TIPO_SERVICIO_TIC as nombre, null  as grupo,      " +
                "                                0 as vigenteAdmin,   " +
                "                                0 as historicoAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,       " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,       " +
                "                                CE.ID_ENTE_PUBLICO as parent1,      " +
                "                               CT.ID_TIPO_SERVICIO_TIC idTipoServicio, null idSubTipoServicio, CE.ID_ENTE_PUBLICO idEntePublico,   " +
                "                               CT.NUM_ORDEN_TIPO_SERVICIO_TIC    " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE        " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO       " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO       " +
                "                                INNER JOIN CAT_TIPO_SERVICIOS_TIC CT ON CT.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC       " +
                "                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC=ST.ID_SUBTIPO_SERVICIO_TIC " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')       " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN ?3 " +
                "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?2 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)" +
                "                                GROUP BY CT.ID_TIPO_SERVICIO_TIC, CT.DESC_TIPO_SERVICIO_TIC_ABREV, CT.DESC_TIPO_SERVICIO_TIC,      " +
                "                                CT.NUM_ORDEN_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO ) TB   " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_TIPO_SERVICIO_TIC   " +
                "                                ORDER BY TB.NUM_ORDEN_TIPO_SERVICIO_TIC    ", resultClass = ServicioTableroBean.class)

@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoAllServicePorEnteSubTipoUnidad",
        query = "SELECT    " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,    " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin,   " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin,   " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera,   " +
                "    SUM(TB.historicoOpera) AS historicoOpera,   " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico   " +
                "FROM (SELECT CT.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, CT.DESC_TIPO_SERVICIO_TIC_ABREV as nombreAbrev,       " +
                "                        CT.DESC_TIPO_SERVICIO_TIC as nombre, null  as grupo,      " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,       " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,     " +
                "                                 0 as vigenteOpera,   " +
                "                                0 as historicoOpera,      " +
                "                                CE.ID_ENTE_PUBLICO as parent1,      " +
                "                               CT.ID_TIPO_SERVICIO_TIC idTipoServicio, null idSubTipoServicio, CE.ID_ENTE_PUBLICO idEntePublico,   " +
                "                               CT.NUM_ORDEN_TIPO_SERVICIO_TIC    " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE        " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO       " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO       " +
                "                                INNER JOIN CAT_TIPO_SERVICIOS_TIC CT ON CT.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC       " +
                "                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC=ST.ID_SUBTIPO_SERVICIO_TIC " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')       " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1  AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN " +
                "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?3 " +
                "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?3 " +
                "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?2 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)" +
                "                                GROUP BY CT.ID_TIPO_SERVICIO_TIC, CT.DESC_TIPO_SERVICIO_TIC_ABREV, CT.DESC_TIPO_SERVICIO_TIC,      " +
                "                                CT.NUM_ORDEN_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO   " +
                "                                union all   " +
                "                                SELECT CT.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, CT.DESC_TIPO_SERVICIO_TIC_ABREV as nombreAbrev,       " +
                "                        CT.DESC_TIPO_SERVICIO_TIC as nombre, null  as grupo,      " +
                "                                0 as vigenteAdmin,   " +
                "                                0 as historicoAdmin,    " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,       " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,       " +
                "                                CE.ID_ENTE_PUBLICO as parent1,      " +
                "                               CT.ID_TIPO_SERVICIO_TIC idTipoServicio, null idSubTipoServicio, CE.ID_ENTE_PUBLICO idEntePublico,   " +
                "                               CT.NUM_ORDEN_TIPO_SERVICIO_TIC    " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE        " +
                "                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO       " +
                "                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO       " +
                "                                INNER JOIN CAT_TIPO_SERVICIOS_TIC CT ON CT.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC       " +
                "                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC=ST.ID_SUBTIPO_SERVICIO_TIC " +
                "                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')       " +
                "                                   AND CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN " +
                "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?3 " +
                "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?3 " +
                "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?2 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)" +
                "                                GROUP BY CT.ID_TIPO_SERVICIO_TIC, CT.DESC_TIPO_SERVICIO_TIC_ABREV, CT.DESC_TIPO_SERVICIO_TIC,      " +
                "                                CT.NUM_ORDEN_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO ) TB   " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_TIPO_SERVICIO_TIC   " +
                "                                ORDER BY TB.NUM_ORDEN_TIPO_SERVICIO_TIC    ", resultClass = ServicioTableroBean.class)


@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoAllSubServicePorEnteSubTipo",
        query = "SELECT     " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,     " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin,    " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin,    " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera,    " +
                "    SUM(TB.historicoOpera) AS historicoOpera,    " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico    " +
                "FROM (SELECT SUB.ID_SUBTIPO_SERVICIO_TIC||SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR as nombreAbrev,            " +
                "                                        SUB.DESC_SUBTIPO_SERVICIO_TIC as nombre, null  as grupo,           " +
                "                                        sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,        " +
                "                                        sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,    " +
                "                                                0 as vigenteOpera,    " +
                "                                                0 as historicoOpera,        " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as parent1,        " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC as idTipoServicio, SUB.ID_SUBTIPO_SERVICIO_TIC AS idSubTipoServicio, CE.ID_ENTE_PUBLICO AS idEntePublico,    " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC        " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE             " +
                "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO            " +
                "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO            " +
                "                                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC         " +
                "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')         " +
                "                                                   AND CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN ?3 " +
                "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?2 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)" +
                "                                                GROUP BY SUB.ID_SUBTIPO_SERVICIO_TIC, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR, SUB.DESC_SUBTIPO_SERVICIO_TIC,           " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC, SUB.ID_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO      " +
                "                                                union all    " +
                "                                                SELECT SUB.ID_SUBTIPO_SERVICIO_TIC||SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR as nombreAbrev,            " +
                "                                        SUB.DESC_SUBTIPO_SERVICIO_TIC as nombre, null  as grupo,           " +
                "                                0 as vigenteAdmin,    " +
                "                                0 as historicoAdmin,     " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,        " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,           " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as parent1,        " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC as idTipoServicio, SUB.ID_SUBTIPO_SERVICIO_TIC AS idSubTipoServicio, CE.ID_ENTE_PUBLICO AS idEntePublico,    " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC        " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE             " +
                "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO            " +
                "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO            " +
                "                                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC         " +
                "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')         " +
                "                                                   AND CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN ?3 " +
                "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?2 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)" +
                "                                                GROUP BY SUB.ID_SUBTIPO_SERVICIO_TIC, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR, SUB.DESC_SUBTIPO_SERVICIO_TIC,           " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC, SUB.ID_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO) TB    " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC    " +
                "                                ORDER BY TB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC    ", resultClass = ServicioTableroBean.class)

@NamedNativeQuery(name = "ServicioTableroBean.getAllConcentradoAllSubServicePorEnteSubTipoUnidad",
        query = "SELECT     " +
                "    TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo,     " +
                "    SUM(TB.vigenteAdmin) AS vigenteAdmin,    " +
                "    SUM(TB.historicoAdmin) AS historicoAdmin,    " +
                "    SUM(TB.vigenteOpera) AS vigenteOpera,    " +
                "    SUM(TB.historicoOpera) AS historicoOpera,    " +
                "    TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico    " +
                "FROM (SELECT SUB.ID_SUBTIPO_SERVICIO_TIC||SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR as nombreAbrev,            " +
                "                                        SUB.DESC_SUBTIPO_SERVICIO_TIC as nombre, null  as grupo,           " +
                "                                        sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteAdmin,        " +
                "                                        sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoAdmin,    " +
                "                                                0 as vigenteOpera,    " +
                "                                                0 as historicoOpera,        " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as parent1,        " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC as idTipoServicio, SUB.ID_SUBTIPO_SERVICIO_TIC AS idSubTipoServicio, CE.ID_ENTE_PUBLICO AS idEntePublico,    " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC        " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE             " +
                "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO            " +
                "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO            " +
                "                                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC         " +
                "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')         " +
                "                                                   AND CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN " +
                "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?3 " +
                "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?3 " +
                "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?2 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)" +
                "                                                GROUP BY SUB.ID_SUBTIPO_SERVICIO_TIC, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR, SUB.DESC_SUBTIPO_SERVICIO_TIC,           " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC, SUB.ID_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO      " +
                "                                                union all    " +
                "                                                SELECT SUB.ID_SUBTIPO_SERVICIO_TIC||SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as idTabla, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR as nombreAbrev,            " +
                "                                        SUB.DESC_SUBTIPO_SERVICIO_TIC as nombre, null  as grupo,           " +
                "                                0 as vigenteAdmin,    " +
                "                                0 as historicoAdmin,     " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='VIG' THEN 1 ELSE 0 END) AS vigenteOpera,        " +
                "                                sum(CASE WHEN ST.CVE_ESTATUS='HIS' THEN 1 ELSE 0 END) AS historicoOpera,           " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC||CE.ID_ENTE_PUBLICO as parent1,        " +
                "                                                SUB.ID_TIPO_SERVICIO_TIC as idTipoServicio, SUB.ID_SUBTIPO_SERVICIO_TIC AS idSubTipoServicio, CE.ID_ENTE_PUBLICO AS idEntePublico,    " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC        " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE             " +
                "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO            " +
                "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO            " +
                "                                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC         " +
                "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS')         " +
                "                                                   AND CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN " +
                "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?3 " +
                "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?3 " +
                "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?2 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)" +
                "                                                GROUP BY SUB.ID_SUBTIPO_SERVICIO_TIC, SUB.DESC_SUBTIPO_SERVICIO_TIC_ABR, SUB.DESC_SUBTIPO_SERVICIO_TIC,           " +
                "                                                SUB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC, SUB.ID_TIPO_SERVICIO_TIC, CE.ID_ENTE_PUBLICO) TB    " +
                "                                GROUP BY TB.idTabla,TB.nombreAbrev, TB.nombre, TB.grupo, TB.parent1, TB.idTipoServicio, TB.idSubTipoServicio, TB.idEntePublico,TB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC    " +
                "                                ORDER BY TB.NUM_ORDEN_SUBTIPO_SERVICIO_TIC    ", resultClass = ServicioTableroBean.class)

@Data
@Entity
@Table(name="SERVICIO_TABLERO_BEAN")
public class ServicioTableroBean implements Serializable {

    @Id
    private Long idTabla;
    private String nombreAbrev;
    private String nombre;
    private String grupo;
    private int vigenteAdmin;
    private int historicoAdmin;
    private int vigenteOpera;
    private int historicoOpera;
    private Long parent1;
    private Long idTipoServicio;
    private Long idSubTipoServicio;
    private Long idEntePublico;
}
