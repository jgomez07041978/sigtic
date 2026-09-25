package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.OrganigramaSql;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganigramaSqlRepository extends CrudRepository<OrganigramaSql, String> {

    List<OrganigramaSql> getOrganigramaId(Long idEntePublico);

    List<OrganigramaSql> getOrganigramaIdNivel1(Long idEntePublico);

    List<OrganigramaSql> getOrganigramIdNivel2(Long idEntePublico, Long idSeleccionado);

    List<OrganigramaSql> getOrganigramIdNivel3(Long idEntePublico, Long idSeleccionado2, Long idSeleccionado3);
}
