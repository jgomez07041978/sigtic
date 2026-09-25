package mx.gob.tabasco.cmaig.sigtic.sigget.repository;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.ConcentradoCargaBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ConcentradoCargaBeanRepository extends CrudRepository<ConcentradoCargaBean, Integer> {

    List<ConcentradoCargaBean> getConcentradoBienesEstatus ();
    List<ConcentradoCargaBean> getConcentradoBienesEstatusId (Long idEntePublico);
}
