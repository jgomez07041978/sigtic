package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatEstatusSistema;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatEstatusSistemaRepository extends CrudRepository<CatEstatusSistema, String> {

    List<CatEstatusSistema> findAllByCveEstatus(String estatus);
}
