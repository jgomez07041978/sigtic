package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.ServicioTicBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioTicBeanRepository extends CrudRepository<ServicioTicBean, Long> {

    List<ServicioTicBean> getServiciosAllPorEntePublico(Long idEntePublico);
    List<ServicioTicBean> getServiciosAdministradosPorEntePublico(Long idEntePublico);
    List<ServicioTicBean> getServiciosOperadosPorEntePublico(Long idEntePublico);

    List<ServicioTicBean> getServiciosAllPorUnidad(List<Long> listaUnidad);

    List<ServicioTicBean> getServiciosAdministradosPorUnidad(List<Long> listaUnidad);

    List<ServicioTicBean> getServiciosOperadosPorUnidad(List<Long> listaUnidad);
}
