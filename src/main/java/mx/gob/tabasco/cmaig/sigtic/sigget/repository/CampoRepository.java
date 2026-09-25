package mx.gob.tabasco.cmaig.sigtic.sigget.repository;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.Campo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampoRepository extends CrudRepository<Campo, String> {

    List<Campo> findAllByOrderByOrden();
    List<Campo> findAllByNombreCampoNotInOrderByOrden(List<String> listaAsignada);


}
