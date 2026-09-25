package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class CargaMasivaBean implements Serializable {
    private Long idCargaDatos;
    private String nombreArchivo;
    private String extension;
    private String archivo;
    private Timestamp fechaCorte;
    private String path;
    private String usuario;
    private Long idPlantilla;
    private String accion;
    private Long idEntePublico;
    private String fechaCorteText;

    public String getFechaCorteText() {
        if (this.fechaCorte!=null) {
            Date date = new Date(this.fechaCorte.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaCorteText = dateFormat.format(date);
            return this.fechaCorteText;
        }
        else
            return null;
    }

}
