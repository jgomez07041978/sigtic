package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoSistema;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatTipoSistemaRepository extends CrudRepository<CatTipoSistema, String> {

    List<CatTipoSistema> findAllByCveEstatusOrderByNumeroOrdenTipoSistemaAsc(String estatus);

}
