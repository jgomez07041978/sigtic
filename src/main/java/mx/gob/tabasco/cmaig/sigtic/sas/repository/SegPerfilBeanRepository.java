package mx.gob.tabasco.cmaig.sigtic.sas.repository;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegPerfilBeanRepository extends CrudRepository<SegPerfilBean, Long> {

      List<SegPerfilBean> findBySistemaAndCveEstatusSql(String sistema, String estatus);
}
