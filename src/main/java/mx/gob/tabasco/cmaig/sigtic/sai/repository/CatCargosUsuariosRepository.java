package mx.gob.tabasco.cmaig.sigtic.sai.repository;


import mx.gob.tabasco.cmaig.sigtic.sai.model.CatCargosUsuarios;
import mx.gob.tabasco.cmaig.sigtic.sai.model.EntePublico;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatCargosUsuariosRepository extends CrudRepository<CatCargosUsuarios, Long> {

    List<CatCargosUsuarios> findByEntePublicoAndCveEstatusOrderByNumeroOrdenCargo(EntePublico entePublico, String estatus);
}
