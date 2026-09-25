package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.Localidad;

import java.util.List;

public interface LocalidadService {

    List<Localidad> getLocalidades(Integer idEntidadFEderativa, Integer idMunicipio);
}
