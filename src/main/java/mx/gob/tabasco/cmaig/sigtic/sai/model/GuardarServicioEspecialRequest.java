package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

@Data
public class GuardarServicioEspecialRequest {

    private String opcion;
    private Integer noLoteOriginal;
    private String numCuentaMaestraOriginal;
    private ServicioEspecial servicioEspecial;
}