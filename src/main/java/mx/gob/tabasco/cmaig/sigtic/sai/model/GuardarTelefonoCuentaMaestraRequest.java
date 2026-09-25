package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

@Data
public class GuardarTelefonoCuentaMaestraRequest {

    private NumTelefonoCuentaMaestra telefono;
    private String opcion;
    private String numTelefonoOriginal;
    private String numCuentaMaestraOriginal;
}
