package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatConceptoCuentaMaestra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatConceptoCuentaMaestraRepository extends CrudRepository<CatConceptoCuentaMaestra, Long> {

    @Query("SELECT c FROM CatConceptoCuentaMaestra c WHERE c.cveEstatus = 'VIG' ORDER BY c.numeroOrden, c.nombreConceptoCuentaMaestra")
    List<CatConceptoCuentaMaestra> findAllVigentes();
}
