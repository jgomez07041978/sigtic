package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ConcentradoBean;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;

@NamedStoredProcedureQuery(
        name = "getCargaMasivaBean",
        procedureName = "PKG_CARGAS_MASIVA.PR_VISUALIZA_CARGA_BEAN",
        resultClasses = CargaMasivaTablaBean.class,
        parameters = {
        @StoredProcedureParameter(name="P_ID_PLANTILLA_CARGA_DATOS",  mode= ParameterMode.IN, type=Long.class),
        @StoredProcedureParameter(name="P_RFC_RESGUARDO", mode=ParameterMode.IN, type=String.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, type = ResultSet.class)
        })
@Data
@Entity
@Table(name="CARGA_MASIVA_BEAN")
public class CargaMasivaTablaBean implements Serializable {

    @Id
    @Column(name = "idCargaDatos")
    private Long idCargaDatos;

    @Column(name = "fechaCorte")
    private String fechaCorte;

    @Column(name = "idPlantilla")
    private Long idPlantilla;

    @Column(name = "nombrePlantilla")
    private String nombrePlantilla;

    @Column(name = "fechaRegistro")
    private String fechaRegistro;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "total")
    private Integer total;

    @Column(name = "totalFilas")
    private Integer totalFilas;

    @Column(name = "totalError")
    private Integer totalError;


}