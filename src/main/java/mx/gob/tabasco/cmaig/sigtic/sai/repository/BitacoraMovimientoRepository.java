package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.BitacoraMovimiento;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BitacoraMovimientoRepository extends CrudRepository<BitacoraMovimiento, Long> {
}
