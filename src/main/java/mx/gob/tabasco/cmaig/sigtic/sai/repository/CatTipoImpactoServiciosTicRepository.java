package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoImpactoServiciosTic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatTipoImpactoServiciosTicRepository extends CrudRepository<CatTipoImpactoServiciosTic, String> {
    List<CatTipoImpactoServiciosTic> findAllByCveEstatus(String estatus);
}
