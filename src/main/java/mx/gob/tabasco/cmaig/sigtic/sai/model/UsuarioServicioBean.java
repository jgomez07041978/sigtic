package mx.gob.tabasco.cmaig.sigtic.sai.model;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@NamedNativeQuery(name = "UsuarioServicioBean.getMisServicios",
        query = "SELECT US.ID_USUARIO_SERVICIO_TIC as idUsuarioServicio, " +
                "US.CURP_USUARIO_SERVICIO_TIC as curp, " +
                "USUA.NOMBRE_COMPLETO AS nombreCompleto, " +
                "EP.DESC_ENTE_PUBLICO AS nombreEntePublicoOpera, " +
                "UA.DESC_UNIDAD_ADMINISTRATIVA AS unidadAdministrativaOpera, " +
                "TS.DESC_TIPO_SERVICIO_TIC AS tipoServicio, " +
                "SB.DESC_SUBTIPO_SERVICIO_TIC AS subtipoServicio, " +
                "ST.DESC_SERVICIO_TIC AS descripcionServicio, " +
                "ES.ESTATUS_SERVICIO AS estatusSeguimiento, " +
                "US.CVE_ESTATUS AS cveEstatus, " +
                "SB.GENERA_CR AS requiereCr, " +
                "CRE.NOMBRE_ESTATUS_CR AS estatusCr, " +
                "CRE.ID_ESTATUS_CR as idEstatusCr," +
                "CR.FOLIO_CR AS folioCr, " +
                "CR.ID_CARTA_RESPONSIVA AS idCartaResponsiva, " +
                "CR.acepto, " +
                "SB.CR_FIRMADA as requiereFirma, " +
                "CR.NOMBRE_ORGINAL_ARCHIVO AS fileNameOriginal " +
                "FROM USUARIOS_SERVICIOS_TIC US " +
                "INNER JOIN SERVICIOS_TIC ST ON ST.ID_SERVICIO_TIC=US.ID_SERVICIO_TIC " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIO_ENTE_PUBLICO SU ON SU.ID_USUARIO_ENTE_PUBLICO = US.ID_USUARIO_ENTE_PUBLICO " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.SAC_CAT_USUARIOS USUA ON USUA.ID_USUARIO=SU.ID_USUARIO " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EP ON EP.ID_ENTE_PUBLICO=US.ID_ENTE_PUBLICO_USUARIO_S_TIC " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_UNIDAD_ADMINISTRATIVA=US.ID_UNIDAD_ADMTVA_USUARIO_S_TIC " +
                "INNER JOIN CAT_TIPO_SERVICIOS_TIC TS ON TS.ID_TIPO_SERVICIO_TIC = ST.ID_TIPO_SERVICIO_TIC " +
                "INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SB ON SB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC " +
                "INNER JOIN CAT_ESTATUS_SERVICIO ES ON ES.ID_ESTATUS_SERVICIO = US.ID_ESTATUS_SERVICIO " +
                "LEFT JOIN CARTA_RESPONSIVA_DETALLE CRD ON CRD.ID_CARTA_RESPONSIVA_DETALLE=US.ID_CARTA_RESPONSIVA_DETALLE " +
                "LEFT JOIN CARTA_RESPONSIVA CR ON CR.ID_CARTA_RESPONSIVA = CRD.ID_CARTA_RESPONSIVA " +
                "LEFT JOIN CAT_ESTATUS_CARTA_RESPONSIVA CRE ON  CRE.ID_ESTATUS_CR=CR.ID_ESTATUS_CARTA_RESPONSIVA " +
                "WHERE US.ID_USUARIO_ENTE_PUBLICO = ?1 AND US.CVE_ESTATUS IN ('VIG','HIS') " +
                "ORDER BY ST.NUM_ORDEN_SERVICIO_TIC ", resultClass = UsuarioServicioBean.class)


@Data
@Entity
@Table(name="USUARIO_SERVICIO_BEAN")
public class UsuarioServicioBean implements Serializable {
    @Id
    private Long idUsuarioServicio;

    private String curp;

    private String nombreCompleto;

    private String nombreEntePublicoOpera;

    private String unidadAdministrativaOpera;

    private String tipoServicio;

    private String subtipoServicio;

    private String descripcionServicio;

    private String estatusSeguimiento;

    private String cveEstatus;

    private String requiereCr;

    private String estatusCr;

    private Long idEstatusCr;

    private String folioCr;

    private Long idCartaResponsiva;

    private String  acepto;

    private String  requiereFirma;

    private String fileNameOriginal;

}
