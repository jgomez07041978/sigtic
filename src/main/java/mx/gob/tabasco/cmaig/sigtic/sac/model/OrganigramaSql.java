package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@NamedNativeQuery(name = "OrganigramaSql.getOrganigramaId",
        query = "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "                                         ,(CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NULL  THEN '#' ELSE '#' END) parent " +
                "                                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA_ABR text2 " +
                "                                         , 'false' children " +
                "                                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA descEntePublico, " +
                " US.NOMBRE_USUARIO nombre, US.APELLIDO_PATERNO_USUARIO apellidoPaterno, APELLIDO_MATERNO_USUARIO as apellidoMaterno, 1 nivel  " +
                "                                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA " +
                " LEFT JOIN CAT_USUARIOS US ON US.RFC_USUARIO=CUA.RFC_TITULAR_UNIDAD_ADMTVA AND US.CVE_ESTATUS='VIG'" +
                "                                    WHERE CUA.CVE_ESTATUS = 'VIG' " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA = ?1 " +
                "UNION ALL " +
                "select * from ( " +
                "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "                                         , (CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA = ?1  THEN '#' ELSE TO_CHAR(CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE) END) parent " +
                "                                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA_ABR text2 " +
                "                                         , 'false' children " +
                "                                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                                         , EPU.DESC_ENTE_PUBLICO descEntePublico, " +
                " US.NOMBRE_USUARIO nombre, US.APELLIDO_PATERNO_USUARIO apellidoPaterno, APELLIDO_MATERNO_USUARIO as apellidoMaterno , LEVEL nivel " +
                "                                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA " +
                " LEFT JOIN CAT_USUARIOS US ON US.RFC_USUARIO=CUA.RFC_TITULAR_UNIDAD_ADMTVA AND US.CVE_ESTATUS='VIG'" +
                " LEFT JOIN CAT_ENTES_PUBLICOS EPU ON CUA.ID_ENTE_PUBLICO = EPU.ID_ENTE_PUBLICO" +
                "                                    WHERE CUA.CVE_ESTATUS = 'VIG' " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NOT NULL " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA <> ?1 " +
                "                                    START WITH CUA.ID_UNIDAD_ADMINISTRATIVA = ?1 " +
                "                               CONNECT BY PRIOR CUA.ID_UNIDAD_ADMINISTRATIVA = CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE " +
                "                               ORDER SIBLINGS BY CUA.DESC_UNIDAD_ADMINISTRATIVA)", resultClass = OrganigramaSql.class)

@NamedNativeQuery(name = "OrganigramaSql.getOrganigramaIdNivel1",
        query = "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "                                         ,(CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NULL  THEN '#' ELSE '#' END) parent " +
                "                                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA_ABR text2 " +
                "                                         , 'false' children " +
                "                                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA descEntePublico, " +
                " US.NOMBRE_USUARIO nombre, US.APELLIDO_PATERNO_USUARIO apellidoPaterno, APELLIDO_MATERNO_USUARIO as apellidoMaterno, 1 nivel  " +
                "                                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA " +
                " LEFT JOIN CAT_USUARIOS US ON US.RFC_USUARIO=CUA.RFC_TITULAR_UNIDAD_ADMTVA AND US.CVE_ESTATUS='VIG'" +
                "                                    WHERE CUA.CVE_ESTATUS = 'VIG' " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA = ?1 " +
                "UNION ALL " +
                "select * from ( " +
                "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "                                         , (CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA = ?1  THEN '#' ELSE TO_CHAR(CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE) END) parent " +
                "                                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA_ABR text2 " +
                "                                         , 'false' children " +
                "                                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                                         , EPU.DESC_ENTE_PUBLICO descEntePublico, " +
                " US.NOMBRE_USUARIO nombre, US.APELLIDO_PATERNO_USUARIO apellidoPaterno, APELLIDO_MATERNO_USUARIO as apellidoMaterno , LEVEL nivel " +
                "                                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA " +
                " LEFT JOIN CAT_USUARIOS US ON US.RFC_USUARIO=CUA.RFC_TITULAR_UNIDAD_ADMTVA AND US.CVE_ESTATUS='VIG'" +
                " LEFT JOIN CAT_ENTES_PUBLICOS EPU ON CUA.ID_ENTE_PUBLICO = EPU.ID_ENTE_PUBLICO" +
                "                                    WHERE CUA.CVE_ESTATUS = 'VIG' " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NOT NULL " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA <> ?1 " +
                "                                    START WITH CUA.ID_UNIDAD_ADMINISTRATIVA = ?1 " +
                "                               CONNECT BY PRIOR CUA.ID_UNIDAD_ADMINISTRATIVA = CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE " +
                "                               ORDER SIBLINGS BY CUA.DESC_UNIDAD_ADMINISTRATIVA) TB WHERE TB.nivel=2", resultClass = OrganigramaSql.class)


@NamedNativeQuery(name = "OrganigramaSql.getOrganigramIdNivel2",
        query = "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "                                         ,(CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NULL  THEN '#' ELSE '#' END) parent " +
                "                                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA_ABR text2 " +
                "                                         , 'false' children " +
                "                                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA descEntePublico, " +
                " US.NOMBRE_USUARIO nombre, US.APELLIDO_PATERNO_USUARIO apellidoPaterno, APELLIDO_MATERNO_USUARIO as apellidoMaterno, 1 nivel  " +
                "                                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA " +
                " LEFT JOIN CAT_USUARIOS US ON US.RFC_USUARIO=CUA.RFC_TITULAR_UNIDAD_ADMTVA AND US.CVE_ESTATUS='VIG'" +
                "                                    WHERE CUA.CVE_ESTATUS = 'VIG' " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA = ?1 " +
                "UNION ALL " +
                "select * from ( " +
                "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "                                         , (CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA = ?1  THEN '#' ELSE TO_CHAR(CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE) END) parent " +
                "                                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA_ABR text2 " +
                "                                         , 'false' children " +
                "                                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                                         , EPU.DESC_ENTE_PUBLICO descEntePublico, " +
                " US.NOMBRE_USUARIO nombre, US.APELLIDO_PATERNO_USUARIO apellidoPaterno, APELLIDO_MATERNO_USUARIO as apellidoMaterno , LEVEL nivel " +
                "                                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA " +
                " LEFT JOIN CAT_USUARIOS US ON US.RFC_USUARIO=CUA.RFC_TITULAR_UNIDAD_ADMTVA AND US.CVE_ESTATUS='VIG'" +
                " LEFT JOIN CAT_ENTES_PUBLICOS EPU ON CUA.ID_ENTE_PUBLICO = EPU.ID_ENTE_PUBLICO" +
                "                                    WHERE CUA.CVE_ESTATUS = 'VIG' " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NOT NULL " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA <> ?1 " +
                "                                    START WITH CUA.ID_UNIDAD_ADMINISTRATIVA = ?1 " +
                "                               CONNECT BY PRIOR CUA.ID_UNIDAD_ADMINISTRATIVA = CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE " +
                "                               ORDER SIBLINGS BY CUA.DESC_UNIDAD_ADMINISTRATIVA) TB WHERE TB.nivel=2" +
                "UNION ALL " +
                "select * from ( " +
                "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "                                         , (CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA = ?2  THEN '#' ELSE TO_CHAR(CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE) END) parent " +
                "                                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA_ABR text2 " +
                "                                         , 'false' children " +
                "                                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                                         , EPU.DESC_ENTE_PUBLICO descEntePublico, " +
                " US.NOMBRE_USUARIO nombre, US.APELLIDO_PATERNO_USUARIO apellidoPaterno, APELLIDO_MATERNO_USUARIO as apellidoMaterno , LEVEL+1 nivel " +
                "                                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA " +
                " LEFT JOIN CAT_USUARIOS US ON US.RFC_USUARIO=CUA.RFC_TITULAR_UNIDAD_ADMTVA AND US.CVE_ESTATUS='VIG'" +
                " LEFT JOIN CAT_ENTES_PUBLICOS EPU ON CUA.ID_ENTE_PUBLICO = EPU.ID_ENTE_PUBLICO" +
                "                                    WHERE CUA.CVE_ESTATUS = 'VIG' " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NOT NULL " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA <> ?2 " +
                "                                    START WITH CUA.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                               CONNECT BY PRIOR CUA.ID_UNIDAD_ADMINISTRATIVA = CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE " +
                "                               ORDER SIBLINGS BY CUA.DESC_UNIDAD_ADMINISTRATIVA) TB2 WHERE TB2.nivel=3", resultClass = OrganigramaSql.class)

@NamedNativeQuery(name = "OrganigramaSql.getOrganigramIdNivel3",
        query = "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "                                         ,(CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NULL  THEN '#' ELSE '#' END) parent " +
                "                                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA_ABR text2 " +
                "                                         , 'false' children " +
                "                                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA descEntePublico, " +
                " US.NOMBRE_USUARIO nombre, US.APELLIDO_PATERNO_USUARIO apellidoPaterno, APELLIDO_MATERNO_USUARIO as apellidoMaterno, 1 nivel  " +
                "                                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA " +
                " LEFT JOIN CAT_USUARIOS US ON US.RFC_USUARIO=CUA.RFC_TITULAR_UNIDAD_ADMTVA AND US.CVE_ESTATUS='VIG'" +
                "                                    WHERE CUA.CVE_ESTATUS = 'VIG' " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA = ?1 " +
                "UNION ALL " +
                "select * from ( " +
                "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "                                         , (CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA = ?1  THEN '#' ELSE TO_CHAR(CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE) END) parent " +
                "                                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA_ABR text2 " +
                "                                         , 'false' children " +
                "                                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                                         , EPU.DESC_ENTE_PUBLICO descEntePublico, " +
                " US.NOMBRE_USUARIO nombre, US.APELLIDO_PATERNO_USUARIO apellidoPaterno, APELLIDO_MATERNO_USUARIO as apellidoMaterno , LEVEL nivel " +
                "                                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA " +
                " LEFT JOIN CAT_USUARIOS US ON US.RFC_USUARIO=CUA.RFC_TITULAR_UNIDAD_ADMTVA AND US.CVE_ESTATUS='VIG'" +
                " LEFT JOIN CAT_ENTES_PUBLICOS EPU ON CUA.ID_ENTE_PUBLICO = EPU.ID_ENTE_PUBLICO" +
                "                                    WHERE CUA.CVE_ESTATUS = 'VIG' " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NOT NULL " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA <> ?1 " +
                "                                    START WITH CUA.ID_UNIDAD_ADMINISTRATIVA = ?1 " +
                "                               CONNECT BY PRIOR CUA.ID_UNIDAD_ADMINISTRATIVA = CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE " +
                "                               ORDER SIBLINGS BY CUA.DESC_UNIDAD_ADMINISTRATIVA) TB WHERE TB.nivel=2" +
                "UNION ALL " +
                "select * from ( " +
                "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "                                         , (CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA = ?2  THEN '#' ELSE TO_CHAR(CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE) END) parent " +
                "                                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA_ABR text2 " +
                "                                         , 'false' children " +
                "                                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                                         , EPU.DESC_ENTE_PUBLICO descEntePublico, " +
                " US.NOMBRE_USUARIO nombre, US.APELLIDO_PATERNO_USUARIO apellidoPaterno, APELLIDO_MATERNO_USUARIO as apellidoMaterno , LEVEL+1 nivel " +
                "                                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA " +
                " LEFT JOIN CAT_USUARIOS US ON US.RFC_USUARIO=CUA.RFC_TITULAR_UNIDAD_ADMTVA AND US.CVE_ESTATUS='VIG'" +
                " LEFT JOIN CAT_ENTES_PUBLICOS EPU ON CUA.ID_ENTE_PUBLICO = EPU.ID_ENTE_PUBLICO" +
                "                                    WHERE CUA.CVE_ESTATUS = 'VIG' " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NOT NULL " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA <> ?2 " +
                "                                    START WITH CUA.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                               CONNECT BY PRIOR CUA.ID_UNIDAD_ADMINISTRATIVA = CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE " +
                "                               ORDER SIBLINGS BY CUA.DESC_UNIDAD_ADMINISTRATIVA) TB2 WHERE TB2.nivel=3" +
                "UNION ALL " +
                "select * from ( " +
                "SELECT to_char(CUA.ID_UNIDAD_ADMINISTRATIVA) id " +
                "                                         , (CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA = ?3  THEN '#' ELSE TO_CHAR(CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE) END) parent " +
                "                                         , '( ' || CVE_UNIDAD_ADMINISTRATIVA || ' ) ' || CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA_ABR text2 " +
                "                                         , 'false' children " +
                "                                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                                         , EPU.DESC_ENTE_PUBLICO descEntePublico, " +
                " US.NOMBRE_USUARIO nombre, US.APELLIDO_PATERNO_USUARIO apellidoPaterno, APELLIDO_MATERNO_USUARIO as apellidoMaterno , LEVEL+2 nivel " +
                "                                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA " +
                " LEFT JOIN CAT_USUARIOS US ON US.RFC_USUARIO=CUA.RFC_TITULAR_UNIDAD_ADMTVA AND US.CVE_ESTATUS='VIG'" +
                " LEFT JOIN CAT_ENTES_PUBLICOS EPU ON CUA.ID_ENTE_PUBLICO = EPU.ID_ENTE_PUBLICO" +
                "                                    WHERE CUA.CVE_ESTATUS = 'VIG' " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NOT NULL " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA <> ?3 " +
                "                                    START WITH CUA.ID_UNIDAD_ADMINISTRATIVA = ?3 " +
                "                               CONNECT BY PRIOR CUA.ID_UNIDAD_ADMINISTRATIVA = CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE " +
                "                               ORDER SIBLINGS BY CUA.DESC_UNIDAD_ADMINISTRATIVA) TB3", resultClass = OrganigramaSql.class)


@Data
@Entity
@Table(name="ORGANIGRAMA_SQL")
public class OrganigramaSql implements Serializable {
    @Id
    private String id;
    private String parent;
    private String text;
    private String text2;
    private boolean children;
    private Integer idEntePublico;
    private String descEntePublico;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Integer nivel;

    @Transient
    private Boolean tieneHijo;

    @Transient
    private String nombreCompleto;


    public String getNombreCompleto() {

        this.nombreCompleto=this.nombre;

        if (this.apellidoPaterno!=null && !this.apellidoPaterno.equals(""))
            this.nombreCompleto=this.nombreCompleto+" "+this.apellidoPaterno;


        if (this.apellidoMaterno!=null && !this.apellidoMaterno.equals(""))
            this.nombreCompleto=this.nombreCompleto+" "+this.apellidoMaterno;

        return nombreCompleto;
    }
}
