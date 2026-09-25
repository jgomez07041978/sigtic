package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.EntePublico;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("entePublicoRepositorySai")
public interface EntePublicoRepository extends CrudRepository<EntePublico, Long> {

   // Object findAll(Long idEntePublico);
}
