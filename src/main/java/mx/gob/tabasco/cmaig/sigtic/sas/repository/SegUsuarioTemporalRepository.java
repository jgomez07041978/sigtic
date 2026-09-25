package mx.gob.tabasco.cmaig.sigtic.sas.repository;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuarioTemporal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SegUsuarioTemporalRepository extends CrudRepository<SegUsuarioTemporal, Long> {

    Optional<SegUsuarioTemporal>  findByCorreoInstitucionalAndEstatus(String correo, String estatus);
    Optional<SegUsuarioTemporal>  findByCorreoPersonalAndEstatus(String correo, String estatus);

    Optional<SegUsuarioTemporal>  findByCurpAndEstatus(String curp, String estatus);

    List<SegUsuarioTemporal> findAllByEstatus(String estatus);

}
