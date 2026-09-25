package mx.gob.tabasco.cmaig.sigtic.sigget.repository;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.ConcentradoCargaEnteBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcentradoCargaEnteBeanRepository extends CrudRepository<ConcentradoCargaEnteBean, Integer> {

    List<ConcentradoCargaEnteBean> getConcentradoBienesEntePublico (String estatus);
    List<ConcentradoCargaEnteBean> getConcentradoBienesEntePublicoId (Long idEntePublico, String estatus);
}
