package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.SacCatUnidadAdministrativaView;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SacCatUnidadAdministrativaViewRepository extends CrudRepository<SacCatUnidadAdministrativaView, Long> {

    List<SacCatUnidadAdministrativaView> findAllByIdEntePublicoAndEstatus(Long idEntePublico, String estatus);

}
