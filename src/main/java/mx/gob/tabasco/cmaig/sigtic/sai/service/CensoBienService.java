package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.Bien;
import mx.gob.tabasco.cmaig.sigtic.sai.model.UnidadAdministrativa;
import net.minidev.json.JSONObject;

import java.util.List;

public interface CensoBienService {

    JSONObject getConcentradoBienesPorEntePublico();
    JSONObject getConcentradoBienesPorUnidadAdministrativa(Long idEntePublico);

    List<Bien> getListaBienesPorUnidad(Long idUnidad);

}
