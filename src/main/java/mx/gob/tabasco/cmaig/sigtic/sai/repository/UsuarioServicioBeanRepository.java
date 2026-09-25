package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.UsuarioServicioBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UsuarioServicioBeanRepository extends CrudRepository<UsuarioServicioBean, Long> {

    List<UsuarioServicioBean> getMisServicios(Long idUsuarioEntePublico);

}
