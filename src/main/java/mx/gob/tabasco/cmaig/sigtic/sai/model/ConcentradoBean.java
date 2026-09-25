package mx.gob.tabasco.cmaig.sigtic.sai.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.io.Serializable;

@NamedNativeQuery(name = "ConcentradoBean.getConcentradoTipoCorreoPorEnte",
        query = "SELECT TC.ID_TIPO_CORREO AS id, " +
                "TC.DESC_TIPO_CORREO_ELECTRONICO as nombre, " +
                "sum(totalActivo) as totalActivo, " +
                "sum(totalOtros) as totalOtros, " +
                "sum(totalActivo)+sum(totalOtros) as total " +
                "FROM ( " +
                "SELECT CO.CVE_TIPO_CORREO_ELECTRONICO, " +
                " case when CO.CVE_ESTATUS_CORREO_ELECTRONICO='ACT' THEN 1 ELSE 0 END AS totalActivo, " +
                " case when CO.CVE_ESTATUS_CORREO_ELECTRONICO!='ACT' THEN 1 ELSE 0 END AS totalOtros " +
                " FROM CORREOS_ELECTRONICOS CO   " +
                "WHERE CO.ID_ENTE_PUBLICO_TITULAR_C_EP=?1 AND CO.CVE_ESTATUS='VIG')TB " +
                "INNER JOIN CAT_TIPO_CORREO_ELECTRONICO TC ON TC.CVE_TIPO_CORREO_ELECTRONICO=TB.CVE_TIPO_CORREO_ELECTRONICO " +
                "GROUP BY TC.ID_TIPO_CORREO, TC.DESC_TIPO_CORREO_ELECTRONICO, TC.NUM_ORDEN_TIPO_CORREO_ELECTRON " +
                "ORDER BY TC.NUM_ORDEN_TIPO_CORREO_ELECTRON", resultClass = ConcentradoBean.class)

@NamedNativeQuery(name = "ConcentradoBean.getConcentradoTipoCorreoPorEnteListaUnidad",
        query = "SELECT TC.ID_TIPO_CORREO AS id, " +
                "TC.DESC_TIPO_CORREO_ELECTRONICO as nombre, " +
                "sum(totalActivo) as totalActivo, " +
                "sum(totalOtros) as totalOtros, " +
                "sum(totalActivo)+sum(totalOtros) as total " +
                "FROM ( " +
                "SELECT CO.CVE_TIPO_CORREO_ELECTRONICO, " +
                " case when CO.CVE_ESTATUS_CORREO_ELECTRONICO='ACT' THEN 1 ELSE 0 END AS totalActivo, " +
                " case when CO.CVE_ESTATUS_CORREO_ELECTRONICO!='ACT' THEN 1 ELSE 0 END AS totalOtros " +
                " FROM CORREOS_ELECTRONICOS CO   " +
                "WHERE CO.ID_ENTE_PUBLICO_TITULAR_C_EP=?1 AND CO.ID_UNIDAD_ADMTVA_TITULAR_C_EP IN ?2 AND CO.CVE_ESTATUS='VIG')TB " +
                "INNER JOIN CAT_TIPO_CORREO_ELECTRONICO TC ON TC.CVE_TIPO_CORREO_ELECTRONICO=TB.CVE_TIPO_CORREO_ELECTRONICO " +
                "GROUP BY TC.ID_TIPO_CORREO, TC.DESC_TIPO_CORREO_ELECTRONICO, TC.NUM_ORDEN_TIPO_CORREO_ELECTRON " +
                "ORDER BY TC.NUM_ORDEN_TIPO_CORREO_ELECTRON", resultClass = ConcentradoBean.class)

@NamedNativeQuery(name = "ConcentradoBean.getConcentradoTipoCorreoPorEnteUnidad",
        query = "SELECT TC.ID_TIPO_CORREO AS id, " +
                "TC.DESC_TIPO_CORREO_ELECTRONICO as nombre, " +
                "sum(totalActivo) as totalActivo, " +
                "sum(totalOtros) as totalOtros, " +
                "sum(totalActivo)+sum(totalOtros) as total " +
                "FROM ( " +
                "SELECT CO.CVE_TIPO_CORREO_ELECTRONICO, " +
                " case when CO.CVE_ESTATUS_CORREO_ELECTRONICO='ACT' THEN 1 ELSE 0 END AS totalActivo, " +
                " case when CO.CVE_ESTATUS_CORREO_ELECTRONICO!='ACT' THEN 1 ELSE 0 END AS totalOtros " +
                " FROM CORREOS_ELECTRONICOS CO   " +
                "WHERE CO.ID_ENTE_PUBLICO_TITULAR_C_EP=?1 " +
                " AND CO.ID_UNIDAD_ADMTVA_TITULAR_C_EP IN (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
                "                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) "+
                "AND CO.CVE_ESTATUS='VIG')TB " +
                "INNER JOIN CAT_TIPO_CORREO_ELECTRONICO TC ON TC.CVE_TIPO_CORREO_ELECTRONICO=TB.CVE_TIPO_CORREO_ELECTRONICO " +
                "GROUP BY TC.ID_TIPO_CORREO, TC.DESC_TIPO_CORREO_ELECTRONICO, TC.NUM_ORDEN_TIPO_CORREO_ELECTRON " +
                "ORDER BY TC.NUM_ORDEN_TIPO_CORREO_ELECTRON", resultClass = ConcentradoBean.class)


@NamedNativeQuery(name = "ConcentradoBean.getConcentradoTipoCorreo",
        query = "SELECT TC.ID_TIPO_CORREO AS id,  " +
                "TC.DESC_TIPO_CORREO_ELECTRONICO as nombre,  " +
                "sum(totalActivo) as totalActivo,  " +
                "sum(totalOtros) as totalOtros,  " +
                "sum(totalActivo)+sum(totalOtros) as total  " +
                "FROM (  " +
                "SELECT CO.CVE_TIPO_CORREO_ELECTRONICO,  " +
                " case when CO.CVE_ESTATUS_CORREO_ELECTRONICO='ACT' THEN 1 ELSE 0 END AS totalActivo,  " +
                " case when CO.CVE_ESTATUS_CORREO_ELECTRONICO!='ACT' THEN 1 ELSE 0 END AS totalOtros  " +
                " FROM CORREOS_ELECTRONICOS CO    " +
                "WHERE CO.CVE_ESTATUS='VIG')TB  " +
                "INNER JOIN CAT_TIPO_CORREO_ELECTRONICO TC ON TC.CVE_TIPO_CORREO_ELECTRONICO=TB.CVE_TIPO_CORREO_ELECTRONICO  " +
                "GROUP BY TC.ID_TIPO_CORREO, TC.DESC_TIPO_CORREO_ELECTRONICO, TC.NUM_ORDEN_TIPO_CORREO_ELECTRON  " +
                "ORDER BY TC.NUM_ORDEN_TIPO_CORREO_ELECTRON", resultClass = ConcentradoBean.class)

@NamedNativeQuery(name = "ConcentradoBean.getConcentradoBienesEntePublico",
        query = "SELECT EP.ID_ENTE_PUBLICO as id,EP.DESC_ENTE_PUBLICO as nombre, 0 totalActivo, 0 totalOtros, COUNT(BI.ID_BIEN) AS total FROM BIENES BI " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_UNIDAD_ADMINISTRATIVA = BI.ID_UNIDAD_ADMINISTRATIVA " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EP ON EP.ID_ENTE_PUBLICO = UA.ID_ENTE_PUBLICO " +
                "where BI.CVE_ESTATUS='VIG' " +
                "GROUP BY EP.ID_ENTE_PUBLICO,EP.DESC_ENTE_PUBLICO, EP.NUM_ORDEN_ENTE_PUBLICO " +
                "ORDER BY EP.NUM_ORDEN_ENTE_PUBLICO ", resultClass = ConcentradoBean.class)

@NamedNativeQuery(name = "ConcentradoBean.getConcentradoBienesUnidadAdministrativa",
        query = "SELECT UA.ID_UNIDAD_ADMINISTRATIVA as id,UA.DESC_UNIDAD_ADMINISTRATIVA as nombre, 0 totalActivo, 0 totalOtros, COUNT(BI.ID_BIEN) AS total FROM BIENES BI " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_UNIDAD_ADMINISTRATIVA = BI.ID_UNIDAD_ADMINISTRATIVA " +
                "where UA.ID_ENTE_PUBLICO=?1 AND BI.CVE_ESTATUS='VIG' " +
                "GROUP BY UA.ID_UNIDAD_ADMINISTRATIVA,UA.DESC_UNIDAD_ADMINISTRATIVA, UA.NUM_ORDEN_UNIDADADMINISTRATIVA " +
                "ORDER BY UA.NUM_ORDEN_UNIDADADMINISTRATIVA ", resultClass = ConcentradoBean.class)

@NamedNativeQuery(name = "ConcentradoBean.getConcentradoBienesEntePublicoId",
        query = "SELECT EP.ID_ENTE_PUBLICO as id,EP.DESC_ENTE_PUBLICO as nombre, 0 totalActivo, 0 totalOtros, COUNT(BI.ID_BIEN) AS total FROM BIENES BI " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_UNIDAD_ADMINISTRATIVA = BI.ID_UNIDAD_ADMINISTRATIVA " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EP ON EP.ID_ENTE_PUBLICO = UA.ID_ENTE_PUBLICO " +
                "where BI.CVE_ESTATUS='VIG'  AND  EP.ID_ENTE_PUBLICO=?1 " +
                "GROUP BY EP.ID_ENTE_PUBLICO,EP.DESC_ENTE_PUBLICO, EP.NUM_ORDEN_ENTE_PUBLICO " +
                "ORDER BY EP.NUM_ORDEN_ENTE_PUBLICO ", resultClass = ConcentradoBean.class)

@Data
@Entity
@Table(name="CONCENTRADO_BEAN")
public class ConcentradoBean implements Serializable {

    @Id
    private Integer id;
    private String nombre;
    private int totalActivo;
    private int totalOtros;
    private int total;


}
