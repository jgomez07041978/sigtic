package mx.gob.tabasco.cmaig.sigtic.sac.repository;


import mx.gob.tabasco.cmaig.sigtic.sac.model.RhGobEmpleado;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RhGobEmpleadoRepository extends CrudRepository<RhGobEmpleado, Long> {

    @Transactional(value = "catalogosTransactionManager") // Permite que la operación se ejecute en una transacción
    @Modifying    // Indica que la consulta modifica la base de datos (no es un SELECT)
    @Query(value = "TRUNCATE TABLE RH_GOB_EMPLEADO", nativeQuery = true)
    void truncarTabla();
}
