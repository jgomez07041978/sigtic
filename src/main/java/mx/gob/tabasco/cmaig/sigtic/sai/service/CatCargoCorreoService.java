package mx.gob.tabasco.cmaig.sigtic.sai.service;


import mx.gob.tabasco.cmaig.sigtic.sai.model.CatCargosUsuarios;
import mx.gob.tabasco.cmaig.sigtic.sai.model.EntePublico;

import java.util.List;

public interface CatCargoCorreoService {

    List<CatCargosUsuarios> obtenCargoCorreo(Long idEnte);

}
