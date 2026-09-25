package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SegUsuarioPk implements Serializable {
    private String usuario;
    private String sistema;
}
