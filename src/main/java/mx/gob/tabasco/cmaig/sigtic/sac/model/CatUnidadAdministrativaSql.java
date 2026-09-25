package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.io.Serializable;

@NamedNativeQuery(name = "CatUnidadAdministrativaSql.getUnidadesAdministrativasArbol",
        query = "SELECT UA.ID_UNIDAD_ADMINISTRATIVA AS idUnidadAdministrativa, "+
        "UA.ID_UNIDAD_ADMINISTRATIVA_PADRE as idUnidadAdministrativaPadre, "+
        "UA.ID_ENTE_PUBLICO as idEntePublico, "+
        "UA.CVE_UNIDAD_ADMINISTRATIVA as cveUnidadAdministrativa, "+
        "UA.DESC_UNIDAD_ADMINISTRATIVA as nombreUnidadAdmin, "+
        "UA.DESC_UNIDAD_ADMINISTRATIVA_ABR nombreUnidadAdminAbrev, "+
        "UA.CVE_ESTATUS as cveEstatus "+
        "FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA "+
        "WHERE  UA.ID_ENTE_PUBLICO IN (SELECT DISTINCT ID_ENTE_PUBLICO_ACCESO "+
        "FROM SIGGET_SAS_SEGURIDAD.SEG_ACCESO_USUARIO "+
        "WHERE RFC_ACCESO_USUARIO = ?1 "+
        "AND CVE_ESTATUS = 'VIG' "+
        "AND ID_SEG_SISTEMA = 'SIGTIC') "+
        "AND UA.ID_UNIDAD_ADMINISTRATIVA IN (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA "+
        "FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD "+
        "START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 "+
        "OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 "+
        "CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) "+
        "AND UA.ID_UNIDAD_ADMINISTRATIVA IN (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA "+
        "FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD "+
         "START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE IN "+
         "(SELECT DISTINCT "+
         "ID_UNIDAD_ADMTVA_ACCESO "+
         "FROM SIGGET_SAS_SEGURIDAD.SEG_ACCESO_USUARIO "+
         "WHERE     RFC_ACCESO_USUARIO = ?1 "+
         "AND CVE_ESTATUS = 'VIG' "+
         "AND ID_SEG_SISTEMA = 'SIGTIC') "+
         "OR UAD.ID_UNIDAD_ADMINISTRATIVA IN "+
         "(SELECT DISTINCT ID_UNIDAD_ADMTVA_ACCESO "+
         "FROM SIGGET_SAS_SEGURIDAD.SEG_ACCESO_USUARIO "+
         "WHERE     RFC_ACCESO_USUARIO = ?1 "+
         "AND CVE_ESTATUS = 'VIG' "+
         "AND ID_SEG_SISTEMA ='SIGTIC') "+
         "CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE)", resultClass = CatUnidadAdministrativaSql.class)

@NamedNativeQuery(name = "CatUnidadAdministrativaSql.getUnidadesAdministrativasArbolSigtic",
        query = "SELECT UA.ID_UNIDAD_ADMINISTRATIVA AS idUnidadAdministrativa, " +
                "                UA.ID_UNIDAD_ADMINISTRATIVA_PADRE as idUnidadAdministrativaPadre, " +
                "                UA.ID_ENTE_PUBLICO as idEntePublico, " +
                "                UA.CVE_UNIDAD_ADMINISTRATIVA as cveUnidadAdministrativa, " +
                "                UA.DESC_UNIDAD_ADMINISTRATIVA as nombreUnidadAdmin, " +
                "                UA.DESC_UNIDAD_ADMINISTRATIVA_ABR nombreUnidadAdminAbrev, " +
                "                UA.CVE_ESTATUS as cveEstatus " +
                "                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA " +
                "                WHERE  UA.ID_UNIDAD_ADMINISTRATIVA IN (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?1 " +
                "                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?1 " +
                "                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) ", resultClass = CatUnidadAdministrativaSql.class)

@Data
@Entity
@Table(name="CAT_UNIDAD_ADMINISTRATIVA_SQL")
public class CatUnidadAdministrativaSql implements Serializable {
    @Id
   private Long idUnidadAdministrativa;
   private Long idUnidadAdministrativaPadre;
   private Long idEntePublico;
   private String cveUnidadAdministrativa;
   private String nombreUnidadAdmin;
   private String nombreUnidadAdminAbrev;
   private String cveEstatus;

}