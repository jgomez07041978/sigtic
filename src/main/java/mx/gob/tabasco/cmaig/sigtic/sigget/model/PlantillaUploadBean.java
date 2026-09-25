package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PlantillaUploadBean implements Serializable {

    private Long idPlantilla;
    private String nombre;
    private String contenido;
    private String rutaDumento;

}
