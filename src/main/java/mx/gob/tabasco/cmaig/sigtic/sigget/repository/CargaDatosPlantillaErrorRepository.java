package mx.gob.tabasco.cmaig.sigtic.sigget.repository;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.CargaDatosPlantillaError;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargaDatosPlantillaErrorRepository extends CrudRepository<CargaDatosPlantillaError, Long> {

    List<CargaDatosPlantillaError> findAllByIdCargaDatosPlantillaOrderByNumLineaPlantilla(Long idCargaDatos);

    void deleteAllByIdCargaDatosPlantilla(Long idCargaDatos);

}
