package mx.gob.tabasco.cmaig.sigtic.sigget.repository;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.Campos;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.CamposPK;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Plantilla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CamposRepository extends JpaRepository<Campos, CamposPK> {
    Integer countByPlantilla(Plantilla plantilla);

    List<Campos> findCamposByPlantillaAndCveEstatusOrderByNumOrdenCampo(Plantilla plantilla, String estatus);
    List<Campos> findCamposByPlantillaAndCveEstatusOrderByCamposPKAsc(Plantilla plantilla, String estatus);

    List<Campos> findCamposByPlantilla(Plantilla plantilla);
    //List<Campos> findCamposByUbicacionColumna(String ubicacion);
    List<Campos> findAllByPlantillaAndUbicacionColumna(Plantilla plantilla, String ubicacion);
    Optional<Campos> findAllByCamposPK(CamposPK camposPK);

}
