package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.Logo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LogoRepository extends CrudRepository<Logo, Integer> {


    List<Logo> findAllByCveEstatusOrderByIdLogoDesc(String estatus);

    Optional<Logo> findByNombreLogoAndCveEstatus(String nombreLogo, String estatus);

}
