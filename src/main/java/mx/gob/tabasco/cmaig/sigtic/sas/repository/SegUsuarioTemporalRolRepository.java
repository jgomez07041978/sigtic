package mx.gob.tabasco.cmaig.sigtic.sas.repository;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuarioTemporal;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuarioTemporalRol;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;


@Repository
public interface SegUsuarioTemporalRolRepository extends CrudRepository<SegUsuarioTemporalRol, Long> {
    Integer countBySegUsuarioTemporal(SegUsuarioTemporal segUsuarioTemporal);

    List<SegUsuarioTemporalRol> findAllBySegUsuarioTemporal(SegUsuarioTemporal segUsuarioTemporal);

    Optional<SegUsuarioTemporalRol> findBySegUsuarioTemporalAndSegPerfil(SegUsuarioTemporal segUsuarioTemporal, SegPerfil segPerfil);

}
