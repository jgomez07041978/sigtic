package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.ServicioGroupBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioGroupBeanRepository extends CrudRepository<ServicioGroupBean, Integer> {

    List<ServicioGroupBean> getGroupServicioAll();

    List<ServicioGroupBean> getGroupServicioPorUnidad(List<Long> listaUnidad);
}
