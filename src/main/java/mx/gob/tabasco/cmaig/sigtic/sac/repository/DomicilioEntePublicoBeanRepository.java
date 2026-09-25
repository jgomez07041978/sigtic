package mx.gob.tabasco.cmaig.sigtic.sac.repository;


import mx.gob.tabasco.cmaig.sigtic.sac.model.DomicilioEntePublicoBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DomicilioEntePublicoBeanRepository extends CrudRepository<DomicilioEntePublicoBean, Long> {

    List<DomicilioEntePublicoBean> getDomiciliosEntePublico(Long idEntePublico);

    DomicilioEntePublicoBean getDomicilioId(Long idDomicilio);

}
