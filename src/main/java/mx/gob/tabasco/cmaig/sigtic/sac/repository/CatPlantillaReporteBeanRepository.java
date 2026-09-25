package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatPlantillaReporteBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatPlantillaReporteBeanRepository extends CrudRepository<CatPlantillaReporteBean, Long> {

    List<CatPlantillaReporteBean> getPlantillaReporte(Long idEntePublico, String estatus);

}
