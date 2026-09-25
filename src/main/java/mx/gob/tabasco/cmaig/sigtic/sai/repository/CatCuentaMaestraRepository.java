package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatCuentaMaestra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatCuentaMaestraRepository extends CrudRepository<CatCuentaMaestra, String> {

    @Query("SELECT c FROM CatCuentaMaestra c WHERE c.cveEstatus = 'VIG' ORDER BY c.numCuentaMaestra")
    List<CatCuentaMaestra> findAllVigentes();

    @Query("SELECT c FROM CatCuentaMaestra c WHERE c.cveEstatus = 'VIG' " +
            "AND (:idEntePublico IS NULL OR c.entePublico.idEntePublico = :idEntePublico) " +
            "ORDER BY c.numCuentaMaestra")
    List<CatCuentaMaestra> findAllVigentesPorEnte(@Param("idEntePublico") Long idEntePublico);
}
