package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecuperarPassword {

    private String curp;
    private int opcion;
    private String dato;
    private String error;
    private String msgExito;


}
