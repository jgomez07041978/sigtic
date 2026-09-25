package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

@Data
public class ServicioTablero {

    private Long idEntePublico;
    private Long idTipoServicio;
    private Long idSubTipoServicio;
    private String nombreServicio;
    private String tooltip;
    private int vigenteAdmin;
    private int historicoAdmin;
    private int totalAdmin;
    private int vigenteOpera;
    private int historicoOpera;
    private int totalOpera;
    private Integer nodo;
    private Integer nodoPadre;
    private String clase;
    private String estilo;
    private String clasePadre;
}
