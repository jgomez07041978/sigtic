package mx.gob.tabasco.cmaig.sigtic.sigget.repository;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.CargaDatosPlantillaHead;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Plantilla;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargaDatosPlantillaHeadRepository extends CrudRepository<CargaDatosPlantillaHead, Long> {

    List<CargaDatosPlantillaHead> findAllByPlantillaAndUsuarioCargaDatoOrderByFechaCorteDesc(Plantilla plantilla, String usuario);

}
