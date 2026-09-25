package mx.gob.tabasco.cmaig.sigtic.sai.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.io.Serializable;


@NamedNativeQuery(name = "CorreoTableroBean.getCorreosPorUnidadAdmin",
        query = "SELECT nvl(UA.ID_UNIDAD_ADMINISTRATIVA,-1) AS idTabla, nvl(UA.CVE_UNIDAD_ADMINISTRATIVA,'')||' - '||nvl(UA.DESC_UNIDAD_ADMINISTRATIVA,'') as nombreAbrev,  " +
                "nvl(UA.CVE_UNIDAD_ADMINISTRATIVA,'')||' - '||nvl(UA.DESC_UNIDAD_ADMINISTRATIVA,'') as nombre,  " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='ACT' THEN 1 ELSE 0 END) AS totalActivo,     " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='BLO' THEN 1 ELSE 0 END) AS totalBloqueado,     " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='BXS' THEN 1 ELSE 0 END) AS totalBloqueadoServidor,   " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='MAN' THEN 1 ELSE 0 END) AS totalMantenimiento,   " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='NOA' THEN 1 ELSE 0 END) AS totalNoActivo,   " +
                "null parent1, null as idTipoCorreo   " +
                "FROM  CORREOS_ELECTRONICOS CE  " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_ENTE_PUBLICO=CE.ID_ENTE_PUBLICO_TITULAR_C_EP AND UA.ID_UNIDAD_ADMINISTRATIVA=CE.ID_UNIDAD_ADMTVA_TITULAR_C_EP  " +
                "WHERE CE.ID_ENTE_PUBLICO_TITULAR_C_EP=?1 AND CE.CVE_ESTATUS='VIG'  " +
                "GROUP BY UA.ID_UNIDAD_ADMINISTRATIVA,UA.CVE_UNIDAD_ADMINISTRATIVA, UA.DESC_UNIDAD_ADMINISTRATIVA, UA.NUM_ORDEN_UNIDADADMINISTRATIVA  " +
                "ORDER BY NVL(UA.NUM_ORDEN_UNIDADADMINISTRATIVA,0) ", resultClass = CorreoTableroBean.class)

@NamedNativeQuery(name = "CorreoTableroBean.getCorreosPorUnidadAdminPorNivel",
        query = "SELECT nvl(UA.ID_UNIDAD_ADMINISTRATIVA,-1) AS idTabla, nvl(UA.CVE_UNIDAD_ADMINISTRATIVA,'')||' - '||nvl(UA.DESC_UNIDAD_ADMINISTRATIVA,'') as nombreAbrev,  " +
                "nvl(UA.CVE_UNIDAD_ADMINISTRATIVA,'')||' - '||nvl(UA.DESC_UNIDAD_ADMINISTRATIVA,'') as nombre,  " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='ACT' THEN 1 ELSE 0 END) AS totalActivo,     " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='BLO' THEN 1 ELSE 0 END) AS totalBloqueado,     " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='BXS' THEN 1 ELSE 0 END) AS totalBloqueadoServidor,   " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='MAN' THEN 1 ELSE 0 END) AS totalMantenimiento,   " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='NOA' THEN 1 ELSE 0 END) AS totalNoActivo,   " +
                "null parent1, null as idTipoCorreo   " +
                "FROM  CORREOS_ELECTRONICOS CE  " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_ENTE_PUBLICO=CE.ID_ENTE_PUBLICO_TITULAR_C_EP AND UA.ID_UNIDAD_ADMINISTRATIVA=CE.ID_UNIDAD_ADMTVA_TITULAR_C_EP  " +
                "WHERE CE.ID_ENTE_PUBLICO_TITULAR_C_EP=?1 AND CE.ID_UNIDAD_ADMTVA_TITULAR_C_EP IN ?2 AND CE.CVE_ESTATUS='VIG'  " +
                "GROUP BY UA.ID_UNIDAD_ADMINISTRATIVA,UA.CVE_UNIDAD_ADMINISTRATIVA, UA.DESC_UNIDAD_ADMINISTRATIVA, UA.NUM_ORDEN_UNIDADADMINISTRATIVA  " +
                "ORDER BY NVL(UA.NUM_ORDEN_UNIDADADMINISTRATIVA,0) ", resultClass = CorreoTableroBean.class)

@NamedNativeQuery(name = "CorreoTableroBean.getCorreosPorTipoCorreo",
        query = "SELECT TC.ID_TIPO_CORREO||nvl(CE.ID_UNIDAD_ADMTVA_TITULAR_C_EP,0) AS idTabla, TC.CVE_TIPO_CORREO_ELECTRONICO as nombreAbrev, " +
                "nvl(TC.DESC_TIPO_CORREO_ELECTRONICO,'') as nombre,  " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='ACT' THEN 1 ELSE 0 END) AS totalActivo,    " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='BLO' THEN 1 ELSE 0 END) AS totalBloqueado,    " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='BXS' THEN 1 ELSE 0 END) AS totalBloqueadoServidor,  " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='MAN' THEN 1 ELSE 0 END) AS totalMantenimiento,  " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='NOA' THEN 1 ELSE 0 END) AS totalNoActivo,  " +
                "CE.ID_UNIDAD_ADMTVA_TITULAR_C_EP parent1, TC.ID_TIPO_CORREO  as idTipoCorreo " +
                "FROM  CORREOS_ELECTRONICOS CE " +
                "inner join CAT_TIPO_CORREO_ELECTRONICO TC ON CE.CVE_TIPO_CORREO_ELECTRONICO=TC.CVE_TIPO_CORREO_ELECTRONICO " +
                "WHERE CE.ID_ENTE_PUBLICO_TITULAR_C_EP = ?1 AND CE.CVE_ESTATUS='VIG' " +
                "GROUP BY TC.ID_TIPO_CORREO, CE.ID_UNIDAD_ADMTVA_TITULAR_C_EP, TC.CVE_TIPO_CORREO_ELECTRONICO, TC.DESC_TIPO_CORREO_ELECTRONICO, TC.NUM_ORDEN_TIPO_CORREO_ELECTRON " +
                "ORDER BY TC.NUM_ORDEN_TIPO_CORREO_ELECTRON  ", resultClass = CorreoTableroBean.class)

@NamedNativeQuery(name = "CorreoTableroBean.getCorreosPorTipoCorreoPorNivel",
        query = "SELECT TC.ID_TIPO_CORREO||nvl(CE.ID_UNIDAD_ADMTVA_TITULAR_C_EP,0) AS idTabla, TC.CVE_TIPO_CORREO_ELECTRONICO as nombreAbrev, " +
                "nvl(TC.DESC_TIPO_CORREO_ELECTRONICO,'') as nombre,  " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='ACT' THEN 1 ELSE 0 END) AS totalActivo,    " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='BLO' THEN 1 ELSE 0 END) AS totalBloqueado,    " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='BXS' THEN 1 ELSE 0 END) AS totalBloqueadoServidor,  " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='MAN' THEN 1 ELSE 0 END) AS totalMantenimiento,  " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='NOA' THEN 1 ELSE 0 END) AS totalNoActivo,  " +
                "CE.ID_UNIDAD_ADMTVA_TITULAR_C_EP parent1, TC.ID_TIPO_CORREO  as idTipoCorreo " +
                "FROM  CORREOS_ELECTRONICOS CE " +
                "inner join CAT_TIPO_CORREO_ELECTRONICO TC ON CE.CVE_TIPO_CORREO_ELECTRONICO=TC.CVE_TIPO_CORREO_ELECTRONICO " +
                "WHERE CE.ID_ENTE_PUBLICO_TITULAR_C_EP = ?1 AND CE.ID_UNIDAD_ADMTVA_TITULAR_C_EP IN ?2 AND CE.CVE_ESTATUS='VIG' " +
                "GROUP BY TC.ID_TIPO_CORREO, CE.ID_UNIDAD_ADMTVA_TITULAR_C_EP, TC.CVE_TIPO_CORREO_ELECTRONICO, TC.DESC_TIPO_CORREO_ELECTRONICO, TC.NUM_ORDEN_TIPO_CORREO_ELECTRON " +
                "ORDER BY TC.NUM_ORDEN_TIPO_CORREO_ELECTRON  ", resultClass = CorreoTableroBean.class)

@NamedNativeQuery(name = "CorreoTableroBean.getCorreosPorIdUnidadAdmin",
        query = "SELECT nvl(UA.ID_UNIDAD_ADMINISTRATIVA,-1) AS idTabla, nvl(UA.CVE_UNIDAD_ADMINISTRATIVA,'')||' - '||nvl(UA.DESC_UNIDAD_ADMINISTRATIVA,'') as nombreAbrev,  " +
                "nvl(UA.CVE_UNIDAD_ADMINISTRATIVA,'')||' - '||nvl(UA.DESC_UNIDAD_ADMINISTRATIVA,'') as nombre,  " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='ACT' THEN 1 ELSE 0 END) AS totalActivo,     " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='BLO' THEN 1 ELSE 0 END) AS totalBloqueado,     " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='BXS' THEN 1 ELSE 0 END) AS totalBloqueadoServidor,   " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='MAN' THEN 1 ELSE 0 END) AS totalMantenimiento,   " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='NOA' THEN 1 ELSE 0 END) AS totalNoActivo,   " +
                "null parent1, null as idTipoCorreo   " +
                "FROM  CORREOS_ELECTRONICOS CE  " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_ENTE_PUBLICO=CE.ID_ENTE_PUBLICO_TITULAR_C_EP AND UA.ID_UNIDAD_ADMINISTRATIVA=CE.ID_UNIDAD_ADMTVA_TITULAR_C_EP  " +
                "WHERE CE.ID_ENTE_PUBLICO_TITULAR_C_EP=?1 AND CE.ID_UNIDAD_ADMTVA_TITULAR_C_EP IN (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
                "                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) AND CE.CVE_ESTATUS='VIG'  " +
                "GROUP BY UA.ID_UNIDAD_ADMINISTRATIVA,UA.CVE_UNIDAD_ADMINISTRATIVA, UA.DESC_UNIDAD_ADMINISTRATIVA, UA.NUM_ORDEN_UNIDADADMINISTRATIVA  " +
                "ORDER BY NVL(UA.NUM_ORDEN_UNIDADADMINISTRATIVA,0) ", resultClass = CorreoTableroBean.class)

@NamedNativeQuery(name = "CorreoTableroBean.getCorreosPorTipoCorreoPorIdUnidad",
        query = "SELECT TC.ID_TIPO_CORREO||nvl(CE.ID_UNIDAD_ADMTVA_TITULAR_C_EP,0) AS idTabla, TC.CVE_TIPO_CORREO_ELECTRONICO as nombreAbrev, " +
                "nvl(TC.DESC_TIPO_CORREO_ELECTRONICO,'') as nombre,  " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='ACT' THEN 1 ELSE 0 END) AS totalActivo,    " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='BLO' THEN 1 ELSE 0 END) AS totalBloqueado,    " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='BXS' THEN 1 ELSE 0 END) AS totalBloqueadoServidor,  " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='MAN' THEN 1 ELSE 0 END) AS totalMantenimiento,  " +
                "sum(CASE WHEN CE.CVE_ESTATUS_CORREO_ELECTRONICO='NOA' THEN 1 ELSE 0 END) AS totalNoActivo,  " +
                "CE.ID_UNIDAD_ADMTVA_TITULAR_C_EP parent1, TC.ID_TIPO_CORREO  as idTipoCorreo " +
                "FROM  CORREOS_ELECTRONICOS CE " +
                "inner join CAT_TIPO_CORREO_ELECTRONICO TC ON CE.CVE_TIPO_CORREO_ELECTRONICO=TC.CVE_TIPO_CORREO_ELECTRONICO " +
                "WHERE CE.ID_ENTE_PUBLICO_TITULAR_C_EP = ?1 AND CE.ID_UNIDAD_ADMTVA_TITULAR_C_EP IN (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
                "                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) AND CE.CVE_ESTATUS='VIG'  " +
                "GROUP BY TC.ID_TIPO_CORREO, CE.ID_UNIDAD_ADMTVA_TITULAR_C_EP, TC.CVE_TIPO_CORREO_ELECTRONICO, TC.DESC_TIPO_CORREO_ELECTRONICO, TC.NUM_ORDEN_TIPO_CORREO_ELECTRON " +
                "ORDER BY TC.NUM_ORDEN_TIPO_CORREO_ELECTRON  ", resultClass = CorreoTableroBean.class)



@Data
@Entity
@Table(name="CORREO_TABLERO_BEAN")
public class CorreoTableroBean implements Serializable {

    @Id
    private Long idTabla;
    private String nombreAbrev;
    private String nombre;
    private int totalActivo;
    private int totalBloqueado;
    private int totalBloqueadoServidor;
    private int totalMantenimiento;
    private int totalNoActivo;
    private Long parent1;
    private Integer idTipoCorreo;
}
