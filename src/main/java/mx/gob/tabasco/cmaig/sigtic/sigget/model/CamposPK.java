package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
public class CamposPK implements Serializable {

    private Long idPlantilla;
    private Integer numCampo;

/*    public CamposPK(Long idPlantilla, Integer numCampo) {
        this.idPlantilla = idPlantilla;
        this.numCampo = numCampo;
    }*/
}
