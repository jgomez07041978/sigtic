package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

@Data
public class CuentaMaestraFiltro {

    private Long idEntePublico;
    private Long idUnidadAdministrativa;
    private String cveTipoConexion;

    private Boolean isVigente = true;
    private Boolean isHistorico = false;
}
