package mx.gob.tabasco.cmaig.sigtic.sac.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.io.Serializable;


/*USUARIOS ADMINISTRADORES SIGTIC*/
/*-----------------------------------------------------------------*/

@NamedNativeQuery(name = "CatUsuarioSql.getUsuarioAdminEntidad",
        query = "SELECT UE.ID_USUARIO_ENTE_PUBLICO idUsuarioEntePublico, CU.ID_USUARIO idUsuario, " +
                "                  CU.RFC_USUARIO rfc, " +
                "                   CU.CURP_USUARIO curp, " +
                "                  UPPER (CU.NOMBRE_USUARIO) " +
                "                          || (CASE " +
                "                  WHEN CU.SEGUNDO_NOMBRE_USUARIO IS NOT NULL " +
                "                  THEN " +
                "                          (' ' || UPPER (CU.SEGUNDO_NOMBRE_USUARIO)) " +
                "                  ELSE " +
                "                 '' " +
                "                 END) " +
                "                  || ' ' " +
                "                  || UPPER (CU.APELLIDO_PATERNO_USUARIO) " +
                "                  || ' ' " +
                "                  || UPPER (CU.APELLIDO_MATERNO_USUARIO)    nombreUsuario, " +
                "                  EP.DESC_ENTE_PUBLICO            descEntePublico, " +
                "                  UA.DESC_UNIDAD_ADMINISTRATIVA             descUnidadAdmtva, " +
                "                  EST.DESC_ESTATUS descEstatus " +
                "                  FROM CAT_USUARIOS CU " +
                "                 INNER JOIN CAT_USUARIO_ENTE_PUBLICO UE ON CU.ID_USUARIO = UE.ID_USUARIO " +
                "                 INNER JOIN CAT_ENTES_PUBLICOS EP ON UE.ID_ENTE_PUBLICO = EP.ID_ENTE_PUBLICO  " +
                "                 LEFT JOIN CAT_UNIDADES_ADMINISTRATIVAS UA ON UE.ID_UNIDAD_ADMINISTRATIVA = UA.ID_UNIDAD_ADMINISTRATIVA  " +
                "                 INNER JOIN CAT_ESTATUS  EST ON UE.CVE_ESTATUS = EST.CVE_ESTATUS " +
                "                 WHERE UE.ID_ENTE_PUBLICO =?1 " +
                "                   AND UE.CVE_ESTATUS IN (?2) " +
                "                 ORDER BY EP.DESC_ENTE_PUBLICO, UA.DESC_UNIDAD_ADMINISTRATIVA, " +
                "                 CU.NOMBRE_USUARIO, CU.SEGUNDO_NOMBRE_USUARIO, CU.APELLIDO_PATERNO_USUARIO, " +
                "                 CU.APELLIDO_MATERNO_USUARIO " , resultClass = CatUsuarioSql.class)

@NamedNativeQuery(name = "CatUsuarioSql.getUsuarioAdminEntidadNombre",
        query = "SELECT UE.ID_USUARIO_ENTE_PUBLICO idUsuarioEntePublico, CU.ID_USUARIO idUsuario, " +
                "                  CU.RFC_USUARIO rfc, " +
                "                   CU.CURP_USUARIO curp, " +
                "                  UPPER (CU.NOMBRE_USUARIO) " +
                "                          || (CASE " +
                "                  WHEN CU.SEGUNDO_NOMBRE_USUARIO IS NOT NULL " +
                "                  THEN " +
                "                          (' ' || UPPER (CU.SEGUNDO_NOMBRE_USUARIO)) " +
                "                  ELSE " +
                "                 '' " +
                "                 END) " +
                "                  || ' ' " +
                "                  || UPPER (CU.APELLIDO_PATERNO_USUARIO) " +
                "                  || ' ' " +
                "                  || UPPER (CU.APELLIDO_MATERNO_USUARIO)    nombreUsuario, " +
                "                  EP.DESC_ENTE_PUBLICO            descEntePublico, " +
                "                  UA.DESC_UNIDAD_ADMINISTRATIVA             descUnidadAdmtva, " +
                "                  EST.DESC_ESTATUS descEstatus " +
                "                  FROM CAT_USUARIOS CU " +
                "                 INNER JOIN CAT_USUARIO_ENTE_PUBLICO UE ON CU.ID_USUARIO = UE.ID_USUARIO " +
                "                 INNER JOIN CAT_ENTES_PUBLICOS EP ON UE.ID_ENTE_PUBLICO = EP.ID_ENTE_PUBLICO  " +
                "                 LEFT JOIN CAT_UNIDADES_ADMINISTRATIVAS UA ON UE.ID_UNIDAD_ADMINISTRATIVA = UA.ID_UNIDAD_ADMINISTRATIVA  " +
                "                 INNER JOIN CAT_ESTATUS  EST ON UE.CVE_ESTATUS = EST.CVE_ESTATUS " +
                "                 WHERE UE.ID_ENTE_PUBLICO =?1 " +
                " AND ( ( UPPER(TRIM(CU.NOMBRE_USUARIO)) || (CASE WHEN CU.SEGUNDO_NOMBRE_USUARIO IS NOT NULL THEN (' ' || UPPER(TRIM(CU.SEGUNDO_NOMBRE_USUARIO))  ) "+
                "    ELSE '' END ) || ' ' || UPPER(TRIM(CU.APELLIDO_PATERNO_USUARIO)) || ' ' || UPPER(TRIM(CU.APELLIDO_MATERNO_USUARIO))) "+
                "    LIKE UPPER(TRIM('%'||?2||'%')) )" +
                "   AND UE.CVE_ESTATUS IN (?3) " +
                "                 ORDER BY EP.DESC_ENTE_PUBLICO, UA.DESC_UNIDAD_ADMINISTRATIVA, " +
                "                 CU.NOMBRE_USUARIO, CU.SEGUNDO_NOMBRE_USUARIO, CU.APELLIDO_PATERNO_USUARIO, " +
                "                 CU.APELLIDO_MATERNO_USUARIO ", resultClass = CatUsuarioSql.class)


@NamedNativeQuery(name = "CatUsuarioSql.getUsuarioAdminUnidad",
        query = "SELECT UE.ID_USUARIO_ENTE_PUBLICO idUsuarioEntePublico, CU.ID_USUARIO idUsuario, " +
                "                  CU.RFC_USUARIO rfc, " +
                "                   CU.CURP_USUARIO curp, " +
                "                  UPPER (CU.NOMBRE_USUARIO) " +
                "                          || (CASE " +
                "                  WHEN CU.SEGUNDO_NOMBRE_USUARIO IS NOT NULL " +
                "                  THEN " +
                "                          (' ' || UPPER (CU.SEGUNDO_NOMBRE_USUARIO)) " +
                "                  ELSE " +
                "                 '' " +
                "                 END) " +
                "                  || ' ' " +
                "                  || UPPER (CU.APELLIDO_PATERNO_USUARIO) " +
                "                  || ' ' " +
                "                  || UPPER (CU.APELLIDO_MATERNO_USUARIO)    nombreUsuario, " +
                "                  EP.DESC_ENTE_PUBLICO            descEntePublico, " +
                "                  UA.DESC_UNIDAD_ADMINISTRATIVA             descUnidadAdmtva, " +
                "                  EST.DESC_ESTATUS descEstatus " +
                "                  FROM CAT_USUARIOS CU " +
                "                 INNER JOIN CAT_USUARIO_ENTE_PUBLICO UE ON CU.ID_USUARIO = UE.ID_USUARIO " +
                "                 INNER JOIN CAT_ENTES_PUBLICOS EP ON UE.ID_ENTE_PUBLICO = EP.ID_ENTE_PUBLICO  " +
                "                 LEFT JOIN CAT_UNIDADES_ADMINISTRATIVAS UA ON UE.ID_UNIDAD_ADMINISTRATIVA = UA.ID_UNIDAD_ADMINISTRATIVA  " +
                "                 INNER JOIN CAT_ESTATUS  EST ON UE.CVE_ESTATUS = EST.CVE_ESTATUS " +
                "                 WHERE UE.ID_ENTE_PUBLICO =?1 " +
                "                                  AND UA.ID_UNIDAD_ADMINISTRATIVA IN (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                   FROM CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                   START WITH UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE = ?2 " +
                "                                                   OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                                                   CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE)" +
                "                   AND UE.CVE_ESTATUS IN (?3) " +
                "                 ORDER BY EP.DESC_ENTE_PUBLICO, UA.DESC_UNIDAD_ADMINISTRATIVA, " +
                "                 CU.NOMBRE_USUARIO, CU.SEGUNDO_NOMBRE_USUARIO, CU.APELLIDO_PATERNO_USUARIO, " +
                "                 CU.APELLIDO_MATERNO_USUARIO " +
                "                ", resultClass = CatUsuarioSql.class)

@NamedNativeQuery(name = "CatUsuarioSql.getUsuarioAdminUnidadNombre",
        query = "SELECT UE.ID_USUARIO_ENTE_PUBLICO idUsuarioEntePublico, CU.ID_USUARIO idUsuario, " +
                "                  CU.RFC_USUARIO rfc, " +
                "                   CU.CURP_USUARIO curp, " +
                "                  UPPER (CU.NOMBRE_USUARIO) " +
                "                          || (CASE " +
                "                  WHEN CU.SEGUNDO_NOMBRE_USUARIO IS NOT NULL " +
                "                  THEN " +
                "                          (' ' || UPPER (CU.SEGUNDO_NOMBRE_USUARIO)) " +
                "                  ELSE " +
                "                 '' " +
                "                 END) " +
                "                  || ' ' " +
                "                  || UPPER (CU.APELLIDO_PATERNO_USUARIO) " +
                "                  || ' ' " +
                "                  || UPPER (CU.APELLIDO_MATERNO_USUARIO)    nombreUsuario, " +
                "                  EP.DESC_ENTE_PUBLICO            descEntePublico, " +
                "                  UA.DESC_UNIDAD_ADMINISTRATIVA             descUnidadAdmtva, " +
                "                  EST.DESC_ESTATUS descEstatus " +
                "                  FROM CAT_USUARIOS CU " +
                "                 INNER JOIN CAT_USUARIO_ENTE_PUBLICO UE ON CU.ID_USUARIO = UE.ID_USUARIO " +
                "                 INNER JOIN CAT_ENTES_PUBLICOS EP ON UE.ID_ENTE_PUBLICO = EP.ID_ENTE_PUBLICO  " +
                "                 LEFT JOIN CAT_UNIDADES_ADMINISTRATIVAS UA ON UE.ID_UNIDAD_ADMINISTRATIVA = UA.ID_UNIDAD_ADMINISTRATIVA  " +
                "                 INNER JOIN CAT_ESTATUS  EST ON UE.CVE_ESTATUS = EST.CVE_ESTATUS " +
                "                 WHERE UE.ID_ENTE_PUBLICO =?1 " +
                "                                  AND UA.ID_UNIDAD_ADMINISTRATIVA IN (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                                   FROM CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                                   START WITH UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE = ?2 " +
                "                                                   OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                                                   CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE)" +
                " AND ( ( UPPER(TRIM(CU.NOMBRE_USUARIO)) || (CASE WHEN CU.SEGUNDO_NOMBRE_USUARIO IS NOT NULL THEN (' ' || UPPER(TRIM(CU.SEGUNDO_NOMBRE_USUARIO))  ) "+
                "    ELSE '' END ) || ' ' || UPPER(TRIM(CU.APELLIDO_PATERNO_USUARIO)) || ' ' || UPPER(TRIM(CU.APELLIDO_MATERNO_USUARIO))) "+
                "    LIKE UPPER(TRIM('%'||?3||'%')) )" +
                "   AND UE.CVE_ESTATUS IN (?4) " +
                "                 ORDER BY EP.DESC_ENTE_PUBLICO, UA.DESC_UNIDAD_ADMINISTRATIVA, " +
                "                 CU.NOMBRE_USUARIO, CU.SEGUNDO_NOMBRE_USUARIO, CU.APELLIDO_PATERNO_USUARIO, " +
                "                 CU.APELLIDO_MATERNO_USUARIO ", resultClass = CatUsuarioSql.class)


@NamedNativeQuery(name = "CatUsuarioSql.getUsuarioEntidad",
        query = "SELECT UE.ID_USUARIO_ENTE_PUBLICO idUsuarioEntePublico, CU.ID_USUARIO idUsuario, " +
                "                  CU.RFC_USUARIO rfc, " +
                "                   CU.CURP_USUARIO curp, " +
                "                  UPPER (CU.NOMBRE_USUARIO) " +
                "                          || (CASE " +
                "                  WHEN CU.SEGUNDO_NOMBRE_USUARIO IS NOT NULL " +
                "                  THEN " +
                "                          (' ' || UPPER (CU.SEGUNDO_NOMBRE_USUARIO)) " +
                "                  ELSE " +
                "                 '' " +
                "                 END) " +
                "                  || ' ' " +
                "                  || UPPER (CU.APELLIDO_PATERNO_USUARIO) " +
                "                  || ' ' " +
                "                  || UPPER (CU.APELLIDO_MATERNO_USUARIO)    nombreUsuario, " +
                "                  EP.DESC_ENTE_PUBLICO            descEntePublico, " +
                "                  UA.DESC_UNIDAD_ADMINISTRATIVA             descUnidadAdmtva, " +
                "                  EST.DESC_ESTATUS descEstatus " +
                "                  FROM CAT_USUARIOS CU " +
                "                 INNER JOIN CAT_USUARIO_ENTE_PUBLICO UE ON CU.ID_USUARIO = UE.ID_USUARIO " +
                "                 INNER JOIN CAT_ENTES_PUBLICOS EP ON UE.ID_ENTE_PUBLICO = EP.ID_ENTE_PUBLICO  " +
                "                 LEFT JOIN CAT_UNIDADES_ADMINISTRATIVAS UA ON UE.ID_UNIDAD_ADMINISTRATIVA = UA.ID_UNIDAD_ADMINISTRATIVA  " +
                "                 INNER JOIN CAT_ESTATUS  EST ON UE.CVE_ESTATUS = EST.CVE_ESTATUS " +
                "                 WHERE UE.ID_ENTE_PUBLICO =?1 " +

                " AND UA.ID_UNIDAD_ADMINISTRATIVA IN (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                FROM CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                START WITH UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE = ?2 " +
                "                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE)" +

                "                       AND UE.ID_UNIDAD_ADMINISTRATIVA IN (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                       FROM CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                       START WITH    " +
                "                                UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE IN " +
                "                                (SELECT DISTINCT ID_UNIDAD_ADMTVA_ACCESO " +
                "                                   FROM SIGGET_SAS_SEGURIDAD.SEG_ACCESO_USUARIO " +
                "                                    WHERE RFC_ACCESO_USUARIO = ?3 " +
                "                                    AND CVE_ESTATUS = 'VIG' AND ID_SEG_SISTEMA ='SIGTIC') " +
                "                               OR UAD.ID_UNIDAD_ADMINISTRATIVA IN " +
                "                                (SELECT DISTINCT ID_UNIDAD_ADMTVA_ACCESO " +
                "                                FROM SIGGET_SAS_SEGURIDAD.SEG_ACCESO_USUARIO " +
                "                                WHERE     RFC_ACCESO_USUARIO = ?3 " +
                "                       AND CVE_ESTATUS = 'VIG' AND ID_SEG_SISTEMA ='SIGTIC')" +
                "                       CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
                "                   AND UE.CVE_ESTATUS IN (?4) " +
                "                 ORDER BY EP.DESC_ENTE_PUBLICO, UA.DESC_UNIDAD_ADMINISTRATIVA, " +
                "                 CU.NOMBRE_USUARIO, CU.SEGUNDO_NOMBRE_USUARIO, CU.APELLIDO_PATERNO_USUARIO, " +
                "                 CU.APELLIDO_MATERNO_USUARIO " , resultClass = CatUsuarioSql.class)

@NamedNativeQuery(name = "CatUsuarioSql.getUsuarioEntidadNombre",
        query = "SELECT UE.ID_USUARIO_ENTE_PUBLICO idUsuarioEntePublico, CU.ID_USUARIO idUsuario, " +
                "                  CU.RFC_USUARIO rfc, " +
                "                   CU.CURP_USUARIO curp, " +
                "                  UPPER (CU.NOMBRE_USUARIO) " +
                "                          || (CASE " +
                "                  WHEN CU.SEGUNDO_NOMBRE_USUARIO IS NOT NULL " +
                "                  THEN " +
                "                          (' ' || UPPER (CU.SEGUNDO_NOMBRE_USUARIO)) " +
                "                  ELSE " +
                "                 '' " +
                "                 END) " +
                "                  || ' ' " +
                "                  || UPPER (CU.APELLIDO_PATERNO_USUARIO) " +
                "                  || ' ' " +
                "                  || UPPER (CU.APELLIDO_MATERNO_USUARIO)    nombreUsuario, " +
                "                  EP.DESC_ENTE_PUBLICO            descEntePublico, " +
                "                  UA.DESC_UNIDAD_ADMINISTRATIVA             descUnidadAdmtva, " +
                "                  EST.DESC_ESTATUS descEstatus " +
                "                  FROM CAT_USUARIOS CU " +
                "                 INNER JOIN CAT_USUARIO_ENTE_PUBLICO UE ON CU.ID_USUARIO = UE.ID_USUARIO " +
                "                 INNER JOIN CAT_ENTES_PUBLICOS EP ON UE.ID_ENTE_PUBLICO = EP.ID_ENTE_PUBLICO  " +
                "                 LEFT JOIN CAT_UNIDADES_ADMINISTRATIVAS UA ON UE.ID_UNIDAD_ADMINISTRATIVA = UA.ID_UNIDAD_ADMINISTRATIVA  " +
                "                 INNER JOIN CAT_ESTATUS  EST ON UE.CVE_ESTATUS = EST.CVE_ESTATUS " +
                "                 WHERE UE.ID_ENTE_PUBLICO =?1 " +

                "                                 AND UA.ID_UNIDAD_ADMINISTRATIVA IN (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                FROM CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                START WITH UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE = ?2 " +
                "                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE)" +

                "                       AND UE.ID_UNIDAD_ADMINISTRATIVA IN (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                       FROM CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                       START WITH    " +
                "                                UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE IN " +
                "                                (SELECT DISTINCT ID_UNIDAD_ADMTVA_ACCESO " +
                "                                   FROM SIGGET_SAS_SEGURIDAD.SEG_ACCESO_USUARIO " +
                "                                    WHERE RFC_ACCESO_USUARIO = ?3 " +
                "                                    AND CVE_ESTATUS = 'VIG' AND ID_SEG_SISTEMA ='SIGTIC') " +
                "                               OR UAD.ID_UNIDAD_ADMINISTRATIVA IN " +
                "                                (SELECT DISTINCT ID_UNIDAD_ADMTVA_ACCESO " +
                "                                FROM SIGGET_SAS_SEGURIDAD.SEG_ACCESO_USUARIO " +
                "                                WHERE     RFC_ACCESO_USUARIO = ?3 " +
                "                       AND CVE_ESTATUS = 'VIG' AND ID_SEG_SISTEMA ='SIGTIC')" +
                "                       CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +

                " AND ( ( UPPER(TRIM(CU.NOMBRE_USUARIO)) || (CASE WHEN CU.SEGUNDO_NOMBRE_USUARIO IS NOT NULL THEN (' ' || UPPER(TRIM(CU.SEGUNDO_NOMBRE_USUARIO))  ) "+
                "    ELSE '' END ) || ' ' || UPPER(TRIM(CU.APELLIDO_PATERNO_USUARIO)) || ' ' || UPPER(TRIM(CU.APELLIDO_MATERNO_USUARIO))) "+
                "    LIKE UPPER(TRIM('%'||?4||'%')) )" +
                "   AND UE.CVE_ESTATUS IN (?5) " +
                "                 ORDER BY EP.DESC_ENTE_PUBLICO, UA.DESC_UNIDAD_ADMINISTRATIVA, " +
                "                 CU.NOMBRE_USUARIO, CU.SEGUNDO_NOMBRE_USUARIO, CU.APELLIDO_PATERNO_USUARIO, " +
                "                 CU.APELLIDO_MATERNO_USUARIO ", resultClass = CatUsuarioSql.class)


@Data
@Entity
@Table(name="CAT_USUARIO_SQL")
public class CatUsuarioSql implements Serializable {

    @Id
    Long idUsuarioEntePublico;
    Long idUsuario;
    String rfc;
    String curp;
    String nombreUsuario;
    String descEntePublico;
    String descUnidadAdmtva;
    String descEstatus;
}
