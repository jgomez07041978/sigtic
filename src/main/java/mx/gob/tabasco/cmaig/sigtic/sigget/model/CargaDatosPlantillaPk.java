package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CargaDatosPlantillaPk implements Serializable {
    private BigDecimal idCargaDatosPlantilla;
    private Integer numLineaCargaDatos;
}
