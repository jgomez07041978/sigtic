package mx.gob.tabasco.cmaig.sigtic.sac.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.io.Serializable;

@NamedNativeQuery(name = "DomicilioEntePublicoBean.getDomiciliosEntePublico",
        query = "select DOM.ID_DOMICILIO_ENTE_PUBLICO as idDomicilio, " +
                "DOM.DESC_DOMICILIO_COMPLETO_EP as nombreDomicilio,   " +
                "DOM.CVE_ESTATUS as cveEstatus,   " +
                "DOM.ID_ENTE_PUBLICO as idEntePublico,   " +
                "nvl(ubs.TOTAL_UBICACIONES,0) as totalUbicaciones " +
                "from SAC_CAT_DOMICILIO_ENTE_PUBLICO DOM, " +
                "(SELECT id_domicilio_unidad_admtva, count(*) total_ubicaciones " +
                "                     FROM cat_ubicaciones_unidad_admtva WHERE CVE_ESTATUS='VIG' " +
                "                     GROUP BY id_domicilio_unidad_admtva) ubs  " +
                "WHERE DOM.ID_ENTE_PUBLICO=?1 " +
                "AND DOM.CVE_ESTATUS='VIG' " +
                "AND DOM.id_domicilio_ente_publico = UBS.id_domicilio_unidad_admtva(+) " +
                "order by DOM.DESC_DOMICILIO_COMPLETO_EP", resultClass = DomicilioEntePublicoBean.class)

@NamedNativeQuery(name = "DomicilioEntePublicoBean.getDomicilioId",
        query = "select DOM.ID_DOMICILIO_ENTE_PUBLICO as idDomicilio, " +
                "DOM.DESC_DOMICILIO_COMPLETO_EP as nombreDomicilio,   " +
                "DOM.CVE_ESTATUS as cveEstatus,   " +
                "DOM.ID_ENTE_PUBLICO as idEntePublico,   " +
                "0 as totalUbicaciones " +
                "from SAC_CAT_DOMICILIO_ENTE_PUBLICO DOM " +
                "WHERE DOM.ID_DOMICILIO_ENTE_PUBLICO=?1 ", resultClass = DomicilioEntePublicoBean.class)

@Data
@Entity
@Table(name="DOMICILIO_ENTE_PUBLICO_BEAN")
public class DomicilioEntePublicoBean  implements Serializable {

    @Id
    private Long idDomicilio;
    private String nombreDomicilio;
    private String cveEstatus;
    private Long idEntePublico;
    private int totalUbicaciones;
}
