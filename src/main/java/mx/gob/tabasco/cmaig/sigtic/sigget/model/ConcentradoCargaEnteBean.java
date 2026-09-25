package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.io.Serializable;


@NamedNativeQuery(name = "ConcentradoCargaEnteBean.getConcentradoBienesEntePublico",
        query = "SELECT  HD.ID_CARGA_DATOS_X_PLANTILLA AS ID,EP.DESC_ENTE_PUBLICO AS entePublico, TO_CHAR(HD.FECHA_CORTE,'DD/MM/YYYY') as fechaCorte, HD.USUARIO_CARGA_DATOS AS RFC, " +
                "CU.NOMBRE_USUARIO AS NOMBRE, " +
                "CU.SEGUNDO_NOMBRE_USUARIO AS segundoNombre, " +
                "CU.APELLIDO_PATERNO_USUARIO AS apellidoPa, " +
                "CU.APELLIDO_MATERNO_USUARIO AS apellidoMa, " +
                "CASE WHEN HD.ESTATUS='PE' THEN 'PENDIENTE' " +
                " WHEN HD.ESTATUS='VE' THEN 'CON ERROR' " +
                " WHEN HD.ESTATUS='CE' THEN 'ERROR AL CARGAR' " +
                " WHEN HD.ESTATUS='VA' THEN 'VALIDADO' " +
                " WHEN HD.ESTATUS='CA' THEN 'CARGADO' " +
                "END AS ESTATUS, HD.TOTAL_REGISTRO as  totalRegistro " +
                "from CARGA_DATOS_X_PLANTILLA_HEAD HD " +
                "INNER JOIN CAT_PLANTILLAS_CARGA_DATOS PD ON PD.ID_PLANTILLA_CARGA_DATOS=HD.ID_PLANTILLA " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EP ON EP.ID_ENTE_PUBLICO = HD.ID_ENTE_PUBLICO " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIOS CU ON CU.RFC_USUARIO=HD.USUARIO_CARGA_DATOS " +
                "WHERE PD.DESC_PLANTILLA_CARGA_DATOS IN ('BIENES') AND HD.ESTATUS = ?1 " +
                "ORDER BY HD.ID_CARGA_DATOS_X_PLANTILLA ", resultClass = ConcentradoCargaEnteBean.class)

@NamedNativeQuery(name = "ConcentradoCargaEnteBean.getConcentradoBienesEntePublicoId",
        query = "SELECT  HD.ID_CARGA_DATOS_X_PLANTILLA AS ID,EP.DESC_ENTE_PUBLICO AS entePublico, TO_CHAR(HD.FECHA_CORTE,'DD/MM/YYYY') as fechaCorte, HD.USUARIO_CARGA_DATOS AS RFC, " +
                "CU.NOMBRE_USUARIO AS NOMBRE, " +
                "CU.SEGUNDO_NOMBRE_USUARIO AS segundoNombre, " +
                "CU.APELLIDO_PATERNO_USUARIO AS apellidoPa, " +
                "CU.APELLIDO_MATERNO_USUARIO AS apellidoMa, " +
                "CASE WHEN HD.ESTATUS='PE' THEN 'PENDIENTE' " +
                " WHEN HD.ESTATUS='VE' THEN 'CON ERROR' " +
                " WHEN HD.ESTATUS='CE' THEN 'ERROR AL CARGAR' " +
                " WHEN HD.ESTATUS='VA' THEN 'VALIDADO' " +
                " WHEN HD.ESTATUS='CA' THEN 'CARGADO' " +
                "END AS ESTATUS, HD.TOTAL_REGISTRO as  totalRegistro " +
                "from CARGA_DATOS_X_PLANTILLA_HEAD HD " +
                "INNER JOIN CAT_PLANTILLAS_CARGA_DATOS PD ON PD.ID_PLANTILLA_CARGA_DATOS=HD.ID_PLANTILLA " +
                "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS EP ON EP.ID_ENTE_PUBLICO = HD.ID_ENTE_PUBLICO " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIOS CU ON CU.RFC_USUARIO=HD.USUARIO_CARGA_DATOS " +
                "WHERE PD.DESC_PLANTILLA_CARGA_DATOS IN ('BIENES') AND HD.ID_ENTE_PUBLICO = ?1 AND HD.ESTATUS = ?2 " +
                "ORDER BY HD.ID_CARGA_DATOS_X_PLANTILLA ", resultClass = ConcentradoCargaEnteBean.class)
@Data
@Entity
@Table(name="CONCENTRADO_CARGA_ENTE_BEAN")
public class ConcentradoCargaEnteBean implements Serializable {

    @Id
    private Integer id;
    private String entePublico;
    private String fechaCorte;
    private String rfc;
    private String nombre;
    private String segundoNombre;
    private String apellidoPa;
    private String apellidoMa;
    private String estatus;
    private Integer totalRegistro;

}
