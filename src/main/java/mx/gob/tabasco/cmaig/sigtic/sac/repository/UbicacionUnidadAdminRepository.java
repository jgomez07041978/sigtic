package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.DomicilioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UbicacionUnidadAdmin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbicacionUnidadAdminRepository extends CrudRepository<UbicacionUnidadAdmin, Long> {

    List<UbicacionUnidadAdmin> findAllByDomicilioEntePublicoOrderByNombreUbicacion(DomicilioEntePublico domicilioEntePublico);
    List<UbicacionUnidadAdmin> findAllByDomicilioEntePublicoOrderByNumOrden(DomicilioEntePublico domicilioEntePublico);

    List<UbicacionUnidadAdmin> findAllByDomicilioEntePublicoAndCveEstatusOrderByNombreUbicacion(DomicilioEntePublico domicilioEntePublico, String estatus);
    List<UbicacionUnidadAdmin> findAllByDomicilioEntePublicoAndCveEstatusOrderByNumOrden(DomicilioEntePublico domicilioEntePublico, String estatus);

    boolean existsAllByDomicilioEntePublicoAndCveEstatus(DomicilioEntePublico domicilioEntePublico, String estatus);

    List<UbicacionUnidadAdmin> findAllByCveEstatus(String estatus);

}
