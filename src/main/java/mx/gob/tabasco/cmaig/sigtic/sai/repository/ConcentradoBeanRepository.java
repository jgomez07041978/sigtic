package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.ConcentradoBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcentradoBeanRepository extends CrudRepository<ConcentradoBean, Integer> {

    List<ConcentradoBean> getConcentradoTipoCorreoPorEnte (Long idEntePublico);
    List<ConcentradoBean> getConcentradoTipoCorreoPorEnteListaUnidad (Long idEntePublico, List<Long> lista);
    List<ConcentradoBean> getConcentradoTipoCorreoPorEnteUnidad (Long idEntePublico, Long idUnidadAdministrativa);
    List<ConcentradoBean> getConcentradoTipoCorreo ();
    List<ConcentradoBean> getConcentradoBienesEntePublico ();
    List<ConcentradoBean> getConcentradoBienesEntePublicoId (Long idEntePublico);
    List<ConcentradoBean> getConcentradoBienesUnidadAdministrativa (Long idEntePublico);

}
