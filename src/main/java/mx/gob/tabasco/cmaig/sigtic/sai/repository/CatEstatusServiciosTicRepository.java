package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatEstatusServiciosTic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatEstatusServiciosTicRepository extends CrudRepository<CatEstatusServiciosTic, String> {
    List<CatEstatusServiciosTic> findAllByCveEstatus(String estatus);
}
