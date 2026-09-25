package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.PuestoUsuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuestoUsuarioRepository extends CrudRepository<PuestoUsuario, Long> {

    List<PuestoUsuario> findAllByEntePublicoOrderByNombrePuestoUsuario(EntePublico entePublico);
}
