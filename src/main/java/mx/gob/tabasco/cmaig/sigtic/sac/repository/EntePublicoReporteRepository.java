package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublicoReporte;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EntePublicoReporteRepository extends CrudRepository<EntePublicoReporte, Long> {

    Optional<EntePublicoReporte> findEntePublicoReporteByEntePublicoAndCveEstatus(EntePublico entePublico, String estatus);
    List<EntePublicoReporte> findAllByEntePublicoOrderByIdEntePublicoReporteDesc(EntePublico entePublico);

}
