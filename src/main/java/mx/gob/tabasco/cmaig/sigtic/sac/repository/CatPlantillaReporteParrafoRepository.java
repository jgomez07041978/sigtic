package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatPlantillaReporteParrafo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatPlantillaReporteParrafoRepository extends CrudRepository<CatPlantillaReporteParrafo, Long> {

    List<CatPlantillaReporteParrafo> findAllByIdPlantillaReporteAndCveEstatusOrderByNumeroOrdenAsc(Long idPlantillaReporte,String estatus);
}
