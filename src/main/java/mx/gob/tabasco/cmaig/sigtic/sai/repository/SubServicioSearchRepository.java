package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.SubServicioSearch;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubServicioSearchRepository extends CrudRepository<SubServicioSearch, Long> {

    List<SubServicioSearch> findAllByClasificacion(String clasificacion);

}
