package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.io.Serializable;


@NamedNativeQuery(name = "OrganigramaUnidad.getUnidadesAdministrativas",
        query = "SELECT CUA.ID_UNIDAD_ADMINISTRATIVA id " +
                "                                         ,(CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NULL  THEN '#' ELSE '#' END) parent " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                                         , 'false' children " +
                "                                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA descEntePublico, 1 nivel " +
                "                                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA " +
                "                                    WHERE CUA.CVE_ESTATUS = 'VIG' " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA = ?1 " +
                "UNION ALL " +
                "select * from ( " +
                "SELECT CUA.ID_UNIDAD_ADMINISTRATIVA id " +
                "                                         , (CASE WHEN CUA.ID_UNIDAD_ADMINISTRATIVA = ?1  THEN '#' ELSE TO_CHAR(CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE) END) parent " +
                "                                         , CUA.DESC_UNIDAD_ADMINISTRATIVA text " +
                "                                         , 'false' children " +
                "                                         , CUA.ID_ENTE_PUBLICO idEntePublico " +
                "                                         , EPU.DESC_ENTE_PUBLICO descEntePublico," +
                " level as nivel " +
                "                                     FROM CAT_UNIDADES_ADMINISTRATIVAS CUA, " +
                "                                          CAT_ENTES_PUBLICOS EPU " +
                "                                    WHERE CUA.ID_ENTE_PUBLICO = EPU.ID_ENTE_PUBLICO(+) " +
                "                                      AND CUA.CVE_ESTATUS = 'VIG' " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE IS NOT NULL " +
                "                                      AND CUA.ID_UNIDAD_ADMINISTRATIVA <> ?1 " +
                "                                    START WITH CUA.ID_UNIDAD_ADMINISTRATIVA = ?1 " +
                "                               CONNECT BY PRIOR CUA.ID_UNIDAD_ADMINISTRATIVA = CUA.ID_UNIDAD_ADMINISTRATIVA_PADRE " +
                "                               ORDER SIBLINGS BY CUA.DESC_UNIDAD_ADMINISTRATIVA)", resultClass = OrganigramaUnidad.class)

@Data
@Entity
@Table(name="ORGANIGRAMA_UNIDAD")
public class OrganigramaUnidad implements Serializable {
    @Id
    private Long id;
    private String parent;
    private String text;
    private boolean children;
    private Integer idEntePublico;
    private String descEntePublico;
    private Integer nivel;

/*    private Long id;
    private Long parent;
    private String unidad;
    private String rfc;
    private Integer nivel;*/
}
