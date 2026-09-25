package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CartaResponsiva;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CartaResponsivaDetalle;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServiciosTic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartaResponsivaDetalleRepository extends CrudRepository<CartaResponsivaDetalle, Long> {

    List<CartaResponsivaDetalle> findAllByCartaResponsiva(CartaResponsiva cartaResponsiva);
    List<CartaResponsivaDetalle> findAllByCartaResponsivaOrderByNombreServicio(CartaResponsiva cartaResponsiva);

    int countByCartaResponsivaAndCveEstatus(CartaResponsiva cartaResponsiva, String estatus);

    List<CartaResponsivaDetalle> findAllByServiciosTicAndCveEstatus(ServiciosTic serviciosTic, String estatus);

    Integer countByCartaResponsiva(CartaResponsiva cartaResponsiva);
    Integer countByCartaResponsivaAndServiciosTic(CartaResponsiva cartaResponsiva, ServiciosTic serviciosTic);


}
