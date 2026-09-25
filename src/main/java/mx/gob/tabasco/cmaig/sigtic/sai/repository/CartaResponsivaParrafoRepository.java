package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CartaResponsivaParrafo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CartaResponsivaParrafoRepository extends CrudRepository<CartaResponsivaParrafo, Long> {

    List<CartaResponsivaParrafo> findAllByIdCartaResponsivaOrderByOrden(Long idCartaResponsiva);
}
