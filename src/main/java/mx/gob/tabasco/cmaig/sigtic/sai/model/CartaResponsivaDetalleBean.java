package mx.gob.tabasco.cmaig.sigtic.sai.model;


import lombok.Data;

import java.io.Serializable;

@Data
public class CartaResponsivaDetalleBean implements Serializable {

    private Long idDetalle;
    private String cveServicio;
    private String nombreServicio;
    private int fila;

}
