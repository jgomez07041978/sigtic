package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatServidorCorreoElectronico;
import mx.gob.tabasco.cmaig.sigtic.sai.model.EntePublico;

import java.util.List;

public interface CatServidorCorreoElectronicoService {

    List<CatServidorCorreoElectronico> obtenServidorCorreoElectronico(Long idEentePublico);

    List<CatServidorCorreoElectronico> obtenServidorCorreoElectronicoAll();
}
