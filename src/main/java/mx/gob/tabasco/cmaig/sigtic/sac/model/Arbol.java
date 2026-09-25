package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.io.Serializable;


@NamedNativeQuery(name = "Arbol.getArbolConParent",
        query = "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
        "                         , (CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA = ?1  THEN '#' ELSE TO_CHAR(CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE) END) parent " +
                "                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                         , 'false' children " +
                "                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                         , EPU.DESC_ENTE_PUBLICO descEntePublico " +
                "                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA, " +
                "                          CAT_ENTES_PUBLICOS EPU " +
                "                    WHERE CUA.ID_ENTE_PUBLICO = EPU.ID_ENTE_PUBLICO(+) " +
                "                      AND CUA.CVE_ESTATUS = 'VIG' " +
                "                      AND CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NOT NULL " +
                "                      AND CUA.ID_UNIDAD_ADMINISTRATIVA <> ?1 " +
                "                    START WITH CUA.ID_UNIDAD_ADMINISTRATIVA = ?1 " +
                "               CONNECT BY PRIOR CUA.ID_UNIDAD_ADMINISTRATIVA = CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE " +
                "               ORDER SIBLINGS BY  CUA.DESC_UNIDAD_ADMINISTRATIVA", resultClass = Arbol.class)

//TO_NUMBER(CUA.CVE_UNIDAD_ADMINISTRATIVA),
@NamedNativeQuery(name = "Arbol.getArbolConParentporInstitucion",
        query = "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "                                         ,(CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NULL  THEN '#' ELSE '#' END) parent " +
                "                                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                                         , 'false' children " +
                "                                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA descEntePublico " +
                "                                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA " +
                "                                    WHERE CUA.CVE_ESTATUS = 'VIG' " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA = ?1 " +
                "UNION ALL " +
                "select * from ( " +
                "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "                                         , (CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA = ?1  THEN '#' ELSE TO_CHAR(CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE) END) parent " +
                "                                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                                         , 'false' children " +
                "                                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                                         , EPU.DESC_ENTE_PUBLICO descEntePublico " +
                "                                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA, " +
                "                                          CAT_ENTES_PUBLICOS EPU " +
                "                                    WHERE CUA.ID_ENTE_PUBLICO = EPU.ID_ENTE_PUBLICO(+) " +
                "                                      AND CUA.CVE_ESTATUS = 'VIG' " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NOT NULL " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA <> ?1 " +
                "                                    START WITH CUA.ID_UNIDAD_ADMINISTRATIVA = ?1 " +
                "                               CONNECT BY PRIOR CUA.ID_UNIDAD_ADMINISTRATIVA = CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE " +
                "                               ORDER SIBLINGS BY CUA.DESC_UNIDAD_ADMINISTRATIVA)", resultClass = Arbol.class)

@NamedNativeQuery(name = "Arbol.getArbolUnidadAdminPresupuestal",
        query = "SELECT  " +
                "        to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "        , (CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA = ?1  THEN '#' ELSE TO_CHAR(CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE) END) parent " +
                "        , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "        , 'false' children " +
                "        , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "        , CUA.DESC_ENTE_PUBLICO descEntePublico " +
                /*"        , CUA.NUM_NIVEL_UNIDAD_ADMTVA " +
                "        , CUA.RUTA_DESC_UNIDAD_ADMTVA_ABR " +
                "        , CUA.DESC_UNIDAD_ADMINISTRATIVA_XN " +*/
                "FROM SAC_CAT_UNIDAD_ADMINISTRATIVA CUA " +
                "WHERE (CUA.ID_ENTE_PUBLICO = ?1 " +
                "AND   CUA.ES_UNIDAD_ADMINISTRATIVA_PPTAL='S' " +
                "AND   CUA.CVE_ESTATUS = 'VIG' " +
                "OR CUA.ID_UNIDAD_ADMINISTRATIVA = ?1 )", resultClass = Arbol.class)

//TO_NUMBER(CUA.CVE_UNIDAD_ADMINISTRATIVA),

@NamedNativeQuery(name = "Arbol.getArbolAdminSigtic",
       query = "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "                         , (CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NULL  THEN '#' ELSE '#' END) parent " +
                "                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                         , 'true' children " +
                "                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                         , CUA.DESC_UNIDAD_ADMINISTRATIVA descEntePublico " +
                "                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA " +
                "                    WHERE CUA.CVE_ESTATUS = 'VIG' " +
                "                    AND CUA.ID_ENTE_PUBLICO IN (SELECT DISTINCT ID_ENTE_PUBLICO FROM CAT_ENTES_PUBLICOS WHERE ID_ENTE_PUBLICO_PADRE IS NULL) " +
                " AND CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NULL AND CUA.ID_UNIDAD_ADMINISTRATIVA!=0 " +
                " START WITH CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NULL " +
                "                CONNECT BY PRIOR CUA.ID_UNIDAD_ADMINISTRATIVA = CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE" +
                " ", resultClass = Arbol.class)


@NamedNativeQuery(name = "Arbol.getArbolSinParentConUsuario",
        query = "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "                         , (CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NULL  THEN '#' ELSE '#' END) parent " +
                "                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                         , 'true' children " +
                "                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                         , CUA.DESC_UNIDAD_ADMINISTRATIVA descEntePublico " +
                "                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA " +
                "                    WHERE CUA.CVE_ESTATUS = 'VIG' " +
                "                     AND CUA.ID_UNIDAD_ADMINISTRATIVA IN (SELECT DISTINCT ID_UNIDAD_ADMTVA_ACCESO " +
                "                                    FROM SIGGET_SAS_SEGURIDAD.SEG_ACCESO_USUARIO " +
                "                                    WHERE RFC_ACCESO_USUARIO = ?1 " +
                "                                    AND CVE_ESTATUS = 'VIG' " +
                "                                    AND ID_SEG_SISTEMA = 'SIGTIC')                     " +
                "                                    START WITH CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NULL " +
                "                CONNECT BY PRIOR CUA.ID_UNIDAD_ADMINISTRATIVA = CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE", resultClass = Arbol.class)

@NamedNativeQuery(name = "Arbol.getArbolSinParentConUsuarioEnte",
        query = "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "                         , (CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NULL  THEN '#' ELSE '#' END) parent " +
                "                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                         , 'true' children " +
                "                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                         , CUA.DESC_UNIDAD_ADMINISTRATIVA descEntePublico " +
                "                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA " +
                "                    WHERE CUA.CVE_ESTATUS = 'VIG' " +
                "                     AND CUA.ID_UNIDAD_ADMINISTRATIVA IN (SELECT DISTINCT ID_UNIDAD_ADMTVA_ACCESO " +
                "                                    FROM SIGGET_SAS_SEGURIDAD.SEG_ACCESO_USUARIO " +
                "                                    WHERE RFC_ACCESO_USUARIO = ?1 " +
                "                                    AND CVE_ESTATUS = 'VIG' " +
                "                                    AND ID_SEG_SISTEMA = 'SIGTIC' AND ID_ENTE_PUBLICO_ACCESO = ?2)                     " +
                "                                    START WITH CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NULL " +
                "                CONNECT BY PRIOR CUA.ID_UNIDAD_ADMINISTRATIVA = CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE", resultClass = Arbol.class)

@Data
@Entity
@Table(name="ARBOL")
public class Arbol implements Serializable {
    @Id
    private String id;
    private String parent;
    private String text;
    private boolean children;
    private Integer idEntePublico;
    private String descEntePublico;
    //private Integer idSegUsuario;
}
