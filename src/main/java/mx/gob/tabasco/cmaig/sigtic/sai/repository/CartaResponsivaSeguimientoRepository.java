package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CartaResponsiva;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CartaResponsivaSeguimiento;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartaResponsivaSeguimientoRepository extends CrudRepository<CartaResponsivaSeguimiento, Long> {

    List<CartaResponsivaSeguimiento> findAllByCartaResponsivaOrderByIdCartaResponsivaSeguimientoDesc(CartaResponsiva cartaResponsiva);
}
