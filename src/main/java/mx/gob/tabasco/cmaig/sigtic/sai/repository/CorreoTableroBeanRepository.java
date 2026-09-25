package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CorreoTableroBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorreoTableroBeanRepository  extends CrudRepository<CorreoTableroBean, Long> {
    List<CorreoTableroBean> getCorreosPorUnidadAdmin(Long idEntePublico);
    List<CorreoTableroBean> getCorreosPorUnidadAdminPorNivel(Long idEntePublico, List<Long> idUnidadAdministrativa);

    List<CorreoTableroBean> getCorreosPorTipoCorreo(Long idEntePublico);
    List<CorreoTableroBean> getCorreosPorTipoCorreoPorNivel(Long idEntePublico, List<Long> idUnidadAdministrativa);

    List<CorreoTableroBean> getCorreosPorIdUnidadAdmin(Long idEntePublico, Long idUnidadAdministrativa);

    List<CorreoTableroBean> getCorreosPorTipoCorreoPorIdUnidad(Long idEntePublico, Long idUnidadAdministrativa);

}
