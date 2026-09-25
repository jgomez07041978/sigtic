package mx.gob.tabasco.cmaig.sigtic.sac.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

@NamedNativeQuery(name = "CatPlantillaReporteBean.getPlantillaReporte",
        query = "SELECT   " +
                "    CR.ID_PLANTILLA_REPORTE AS idPlantillaReporte,  " +
                " CR.NOMBRE_REPORTE as nombreReporte,  " +
                " CR.TITULO_REPORTE as tituloReporte,  " +
                " CR.CVE_ESTATUS AS cveEstatus,  " +
                " TS.DESC_TIPO_SERVICIO_TIC as tipoServicio,  " +
                " SB.DESC_SUBTIPO_SERVICIO_TIC as subTipoServicio  " +
                "FROM CAT_PLANTILLA_REPORTE CR   " +
                "INNER JOIN SIGGET_SAI_SIGTIC.CAT_SUBTIPO_SERVICIOS_TIC SB ON SB.ID_SUBTIPO_SERVICIO_TIC=CR.ID_SUBTIPO_SERVICIO  " +
                "INNER JOIN SIGGET_SAI_SIGTIC.CAT_TIPO_SERVICIOS_TIC TS ON TS.ID_TIPO_SERVICIO_TIC=SB.ID_TIPO_SERVICIO_TIC  " +
                "WHERE CR.ID_ENTE_PUBLICO=?1 AND CR.CVE_ESTATUS=?2  " +
                "ORDER BY TS.DESC_TIPO_SERVICIO_TIC, SB.DESC_SUBTIPO_SERVICIO_TIC", resultClass = CatPlantillaReporteBean.class)

@Getter
@Setter
@Entity
@Table(name="PLANTILLA_REPORTE_TB")
public class CatPlantillaReporteBean {
    @Id
    private Long idPlantillaReporte;
    private String nombreReporte;
    private String tituloReporte;
    private String cveEstatus;
    private String  tipoServicio;
    private String  subTipoServicio;
}
