package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileUploadBean implements Serializable {

    private Long id;
    private String nombre;
    private String contenido;
    private String rutaDumento;

}
