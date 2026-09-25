package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name="CARGA_DATOS_X_PLANTILLA_HEAD")
public class CargaDatosPlantillaHead implements Serializable {

    @Id
    @Column(name = "ID_CARGA_DATOS_X_PLANTILLA")
    private Long idCargaDatosPlantilla;

    @Column(name = "FECHA_CORTE")
    private Timestamp fechaCorte;

    @ManyToOne
    @JoinColumn(name = "ID_PLANTILLA")
    private Plantilla plantilla;

    @Column(name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column(name = "FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

    @Column(name = "USUARIO_CARGA_DATOS", nullable = false)
    private String usuarioCargaDato;

    @Column(name = "TOTAL_REGISTRO", nullable = false)
    private Integer totalRegistros;

    @Column(name = "TOTAL_ERROR", nullable = false)
    private Integer totalError;

    @Column(name = "ID_ENTE_PUBLICO")
    private Long idEntePublico;

    @Column(name = "ESTATUS", nullable = false)
    private String estatus;

    @Transient
    private String estatusText;
    @Transient
    private String fechaCorteText;

    @Transient
    private String fechaRegistroText;


    public String getEstatusText() {
        String estatusText="";
        if (estatus.equals("PE"))
            estatusText="PENDIENTE";
        else
        if (estatus.equals("VE"))
            estatusText="CON ERROR";
        else
        if (estatus.equals("VA"))
            estatusText="VALIDADO";
        else
        if (estatus.equals("CE"))
            estatusText="ERROR AL CARGAR";
        else
        if (estatus.equals("CA"))
            estatusText="CARGADO";

        return estatusText;
    }

    public String getFechaCorteText() {
        if (this.fechaCorte!=null) {
            Date date = new Date(this.fechaCorte.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaCorteText = dateFormat.format(date);
            return this.fechaCorteText;
        } else
            return null;
    }

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
