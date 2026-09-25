package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.ServicioBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioBeanRepository extends CrudRepository<ServicioBean, Long> {

    List<ServicioBean> getAllServicioAdmin();
    List<ServicioBean> getAllServicioOpera();
    List<ServicioBean> getServicioAdminPorEnte(Long idEntePublico);
    List<ServicioBean> getServicioOperaPorEnte(Long idEntePublico);
}