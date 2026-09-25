package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.DomicilioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DomicilioEntePublicoRepository extends CrudRepository<DomicilioEntePublico, Long> {

    List<DomicilioEntePublico> findAllByEntePublicoAndCveEstatusOrderByNombreDomicilio(EntePublico entePublico, String estatus);

    List<DomicilioEntePublico> findAllByEntePublicoAndCveEstatusOrderByNumOrden(EntePublico entePublico, String estatus);
}
