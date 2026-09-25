package mx.gob.tabasco.cmaig.sigtic.sigget.repository;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.GrupoManual;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrupoManualRepository extends CrudRepository<GrupoManual, Integer> {

    List<GrupoManual> findAllByCveEstatusOrderByNumeroOrden(String cveEstatus);
}
