package mx.gob.tabasco.cmaig.sigtic.sac.repository;


import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("entePublicoRepositorySac")
public interface EntePublicoRepository extends CrudRepository<EntePublico, Long> {

    List<EntePublico> findAllByCveEstatusOrderByTipoEntePublicoAscNombreEntePublicoAsc(String estatus);
    List<EntePublico> findAllByCveEstatusOrderByNumOrdenEntePublicoAsc(String estatus);
    List<EntePublico> findAllByIdEntePublicoInAndCveEstatusOrderByTipoEntePublicoAscNombreEntePublicoAsc(List<Long> lista, String estatus);
    List<EntePublico> findAllByCveEntePublico(String cveEntePublico);

    @Query("SELECT p FROM EntePublico p where p.idEntePublico in (?1) order by p.tipoEntePublico.cveTipoEntePublico, p.nombreEntePublico asc ")
    List<EntePublico> getEntesPublicoByList(List<Long> lista);

    List<EntePublico> findAllByIdImagenEntePublico(Integer idLogo);

}
