package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name="CARGA_DATOS_X_PLANTILLA_ERR")
public class CargaDatosPlantillaError implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PLANTILLA_ERROR_APP")
    @SequenceGenerator(sequenceName = "SEQ_PLANTILLA_ERROR", allocationSize = 1, name = "SEQ_PLANTILLA_ERROR_APP")
    @Column(name = "ID_ERROR")
    private Long idError;

    @Column(name = "ID_CARGA_DATOS_X_PLANTILLA", nullable = false)
    private Long idCargaDatosPlantilla;

    @Column(name = "NUM_LINEA_CARGADATOSXPLANTILLA", nullable = false)
    private Integer numLineaPlantilla;

    @Column(name = "COLUMNA", nullable = false)
    private String columna;

    @Column(name = "INFO_ERROR", nullable = false)
    private String infoError;

    @Column(name = "USUARIO_REGISTRO", nullable = false)
    private String usuarioRegistro;

    @Column(name = "FECHA_REGISTRO", nullable = false)
    private Timestamp fechaRegistro;

    @Transient
    private String fechaRegistroText;

    public String getFechaRegistroText() {

        if (this.fechaRegistro!=null) {
            Date date = new Date(this.fechaRegistro.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            this.fechaRegistroText = dateFormat.format(date);
            return this.fechaRegistroText;
        } else
            return null;
    }


}
