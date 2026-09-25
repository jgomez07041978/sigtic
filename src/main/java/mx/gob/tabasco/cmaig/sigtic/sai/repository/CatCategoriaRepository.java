package mx.gob.tabasco.cmaig.sigtic.sai.repository;


import mx.gob.tabasco.cmaig.sigtic.sai.model.CatCategoria;
import mx.gob.tabasco.cmaig.sigtic.sai.model.EntePublico;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatCategoriaRepository extends CrudRepository<CatCategoria, Long> {

    List<CatCategoria> findByEntePublicoAndCveEstatusOrderByNumeroOrdenCategoria(EntePublico entePublico, String estatus);
}
