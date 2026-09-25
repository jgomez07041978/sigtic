package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.UbicacionUnidadAdmin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UbicacionUnidadAdminSaiRepository extends CrudRepository<UbicacionUnidadAdmin, Long> {
}
