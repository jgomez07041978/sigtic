package mx.gob.tabasco.cmaig.sigtic.sac.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@NamedNativeQuery(name = "LogoBean.getLogosAll",
        query = "SELECT CI.ID_IMAGEN AS idLogo,  " +
                "CI.NOMBRE_IMAGEN AS nombreLogo, " +
                "CI.NOMBRE_INTERNO AS nombreLogoInterno, " +
                "CI.RUTA_IMAGEN AS rutaArchivo, " +
                "CI.ANCHO AS ancho, " +
                "CI.ALTO AS alto, " +
                "EP.DESC_ENTE_PUBLICO AS nombreEntePublico, " +
                "UA.DESC_UNIDAD_ADMINISTRATIVA AS nombreUnidadAdministrativa " +
                "FROM CAT_IMAGENES CI  " +
                "LEFT JOIN CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_LOGO=CI.ID_IMAGEN " +
                "LEFT JOIN CAT_ENTES_PUBLICOS EP ON EP.ID_ENTE_PUBLICO = UA.ID_ENTE_PUBLICO " +
                "WHERE CI.CVE_ESTATUS='VIG' " +
                "ORDER BY EP.NUM_ORDEN_ENTE_PUBLICO, UA.NUM_ORDEN_UNIDADADMINISTRATIVA", resultClass = LogoBean.class)

@NamedNativeQuery(name = "LogoBean.getLogos",
        query = "SELECT CI.ID_IMAGEN AS idLogo,  " +
                "CI.NOMBRE_IMAGEN AS nombreLogo, " +
                "CI.NOMBRE_INTERNO AS nombreLogoInterno, " +
                "CI.RUTA_IMAGEN AS rutaArchivo, " +
                "CI.ANCHO AS ancho, " +
                "CI.ALTO AS alto, " +
                "EP.DESC_ENTE_PUBLICO AS nombreEntePublico, " +
                "UA.DESC_UNIDAD_ADMINISTRATIVA AS nombreUnidadAdministrativa " +
                "FROM CAT_IMAGENES CI  " +
                "LEFT JOIN CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_LOGO=CI.ID_IMAGEN " +
                "LEFT JOIN CAT_ENTES_PUBLICOS EP ON EP.ID_ENTE_PUBLICO = UA.ID_ENTE_PUBLICO " +
                "WHERE ep.ID_ENTE_PUBLICO=?1 AND CI.CVE_ESTATUS='VIG' " +
                "ORDER BY EP.NUM_ORDEN_ENTE_PUBLICO, UA.NUM_ORDEN_UNIDADADMINISTRATIVA", resultClass = LogoBean.class)

@Getter
@Setter
@Entity
@Table(name="LOGO_TB")
public class LogoBean {
    @Id
    private Integer idLogo;
    private String nombreLogo;
    private String nombreLogoInterno;
    private String rutaArchivo;
    private Integer ancho;
    private Integer alto;
    private String nombreEntePublico;
    private String nombreUnidadAdministrativa;

}
