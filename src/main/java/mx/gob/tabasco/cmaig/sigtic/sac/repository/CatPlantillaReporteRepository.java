package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatPlantillaReporte;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CatPlantillaReporteRepository extends CrudRepository<CatPlantillaReporte, Long> {

    @Query(value="SELECT PL.* FROM CAT_PLANTILLA_REPORTE PL " +
            "WHERE  PL.ID_ENTE_PUBLICO=?1 " +
            "AND PL.ID_SUBTIPO_SERVICIO=?2 " +
            "AND PL.CVE_ESTATUS='VIG' ", nativeQuery=true)
    List<CatPlantillaReporte> getPlantillaReportePorEnteSubtipoServicio(Long idEnte, Long idSubtipoServicio);

}
