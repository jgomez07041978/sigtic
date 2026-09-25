package mx.gob.tabasco.cmaig.sigtic.sac.repository;


import mx.gob.tabasco.cmaig.sigtic.sac.model.Municipio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MunicipioRepository extends CrudRepository<Municipio, Integer> {

    List<Municipio> findMunicipioByIdEntidadFederativaOrderByNumeroOrdenMunipio(Integer idEntidadFederativa);
}
