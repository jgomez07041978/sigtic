package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.OrganigramaUnidad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganigramaUnidadRepository extends CrudRepository<OrganigramaUnidad, String> {

    List<OrganigramaUnidad> getUnidadesAdministrativas(Long idEntePublico);
}
