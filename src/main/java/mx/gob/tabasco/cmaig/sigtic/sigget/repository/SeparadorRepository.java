package mx.gob.tabasco.cmaig.sigtic.sigget.repository;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.Separador;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeparadorRepository extends CrudRepository<Separador, Integer> {

    List<Separador> findSeparadorByEstatus(String estatus);
}
