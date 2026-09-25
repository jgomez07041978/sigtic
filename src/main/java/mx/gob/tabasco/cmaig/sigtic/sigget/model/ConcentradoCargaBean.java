package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.io.Serializable;


@NamedNativeQuery(name = "ConcentradoCargaBean.getConcentradoBienesEstatus",
        query = "SELECT" +
                "     rownum as id,TB.ESTATUS as nombre, TB.total " +
                "     FROM ( " +
                "SELECT  " +
                "HD.ESTATUS, count(1) as total " +
                "from CARGA_DATOS_X_PLANTILLA_HEAD HD " +
                "INNER JOIN CAT_PLANTILLAS_CARGA_DATOS PD ON PD.ID_PLANTILLA_CARGA_DATOS=HD.ID_PLANTILLA " +
                "WHERE PD.DESC_PLANTILLA_CARGA_DATOS ='BIENES' " +
                "GROUP BY HD.ESTATUS) TB ", resultClass = ConcentradoCargaBean.class)

@NamedNativeQuery(name = "ConcentradoCargaBean.getConcentradoBienesEstatusId",
        query = "SELECT" +
                "     rownum as id,TB.ESTATUS as nombre, TB.total " +
                "     FROM ( " +
                "SELECT  " +
                "HD.ESTATUS, count(1) as total " +
                "from CARGA_DATOS_X_PLANTILLA_HEAD HD " +
                "INNER JOIN CAT_PLANTILLAS_CARGA_DATOS PD ON PD.ID_PLANTILLA_CARGA_DATOS=HD.ID_PLANTILLA " +
                "WHERE PD.DESC_PLANTILLA_CARGA_DATOS ='BIENES' AND HD.ID_ENTE_PUBLICO=?1 "+
                "GROUP BY HD.ESTATUS) TB ", resultClass = ConcentradoCargaBean.class)
@Data
@Entity
@Table(name="CONCENTRADO_CARGA_BEAN")
public class ConcentradoCargaBean implements Serializable {

    @Id
    private Integer id;
    private String nombre;
    private int total;
}
