package mx.gob.tabasco.cmaig.sigtic.sai.repository;


import mx.gob.tabasco.cmaig.sigtic.sai.model.CatEstatusServicio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatEstatusServicioRepository extends CrudRepository<CatEstatusServicio, Integer> {
}
