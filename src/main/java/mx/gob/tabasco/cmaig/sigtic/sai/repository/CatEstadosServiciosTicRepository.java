package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatEstadosServiciosTic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatEstadosServiciosTicRepository extends CrudRepository<CatEstadosServiciosTic, String> {
    List<CatEstadosServiciosTic> findAllByCveEstatus(String estatus);
}
