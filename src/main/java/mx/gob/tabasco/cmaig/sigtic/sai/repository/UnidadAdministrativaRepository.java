package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.UnidadAdministrativa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("unidadAdministrativaRepositorySai")
public interface UnidadAdministrativaRepository extends CrudRepository<UnidadAdministrativa, Long> {
}
