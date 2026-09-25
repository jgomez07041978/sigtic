package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.NivelUnidadAdministrativa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NivelUnidadAdministrativaRepository extends CrudRepository<NivelUnidadAdministrativa,Integer> {

    List<NivelUnidadAdministrativa> findAllByCveEstatusOrderByNumeroNivelUnidadAdministrativaAscNumeroOrdenUnidadAdministrativaAsc(String cveEstatus);

}
