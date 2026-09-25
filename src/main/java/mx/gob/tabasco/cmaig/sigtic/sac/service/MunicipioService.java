package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.Municipio;

import java.util.List;

public interface MunicipioService {

    List<Municipio> getMunicipios(Integer idEntidadFederativa);

}
