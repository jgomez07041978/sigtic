package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

@Data
public class CorreoTablero {

    private Long idUnidadAdministrativa;
    private Long idEntePublico;
    private Integer idTipoCorreo;
    private String nombre;
    private String tooltip;
    private int totalActivo;
    private int totalBloqueado;
    private int totalBloqueadoServidor;
    private int totalMantenimiento;
    private int totalNoActivo;
    private int total;
    private Integer nodo;
    private Integer nodoPadre;
    private String clase;
    private String estilo;
    private String clasePadre;
}
