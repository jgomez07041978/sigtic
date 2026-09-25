package mx.gob.tabasco.cmaig.sigtic.sai.repository;


import mx.gob.tabasco.cmaig.sigtic.sai.model.ServicioTableroBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioTableroBeanRepository extends CrudRepository<ServicioTableroBean, Long> {

    List<ServicioTableroBean> getAllConcentradoTipoEnte();

    List<ServicioTableroBean> getAllConcentradoTipoEntePorEnte(Long idEntePublico);
    List<ServicioTableroBean> getAllConcentradoTipoEntePorEnteUnidad(Long idEntePublico, Long idUnidadAdministrativa);

    List<ServicioTableroBean> getAllConcentradoTipoEntePorEnteSubTipo(Long idEntePublico, Long idUsuario, List<Long> listaUnidAcceso);

    List<ServicioTableroBean> getAllConcentradoTipoEntePorUnidadSubTipo(Long idEntePublico, Long idUsuario, Long idUnidadAdministrativa);

    /*==============================================================================================*/

    List<ServicioTableroBean> getAllConcentradoAllEnte();

    List<ServicioTableroBean> getAllConcentradPorEnte(Long idEntePublico);
    List<ServicioTableroBean> getAllConcentradPorEnteUnidad(Long idEntePublico, Long idUnidadAdministrativa);

    List<ServicioTableroBean> getAllConcentradoAllService();
    List<ServicioTableroBean> getAllConcentradoAllServicePorEnte(Long idEntePublico);
    List<ServicioTableroBean> getAllConcentradoAllServicePorEnteUnidad(Long idEntePublico, Long idUnidadAdministrativa);

    List<ServicioTableroBean> getAllConcentradoAllSubService();
    List<ServicioTableroBean> getAllConcentradoAllSubServicePorEnte(Long idEntePublico);
    List<ServicioTableroBean> getAllConcentradoAllSubServicePorEnteUnidad(Long idEntePublico, Long idUnidadAdministrativa);

    //List<ServicioTableroBean> getAllConcentradoAllEntePorEnte(Long idEntePublico);



    /*--------------------------------------------------------*/

    List<ServicioTableroBean> getAllConcentradoAllEntePorEnteSubTipo(Long idEntePublico, Long idUsuario, List<Long> listaUnidAcceso);
    List<ServicioTableroBean> getAllConcentradoAllEntePorEnteSubTipoUnidad(Long idEntePublico, Long idUsuario, Long idUnidadAdministrativa);

    List<ServicioTableroBean> getAllConcentradoAllServicePorEnteSubTipo(Long idEntePublico, Long idUsuario, List<Long> listaUnidAcceso);
    List<ServicioTableroBean> getAllConcentradoAllServicePorEnteSubTipoUnidad(Long idEntePublico, Long idUsuario, Long idUnidadAdministrativa);

    List<ServicioTableroBean> getAllConcentradoAllSubServicePorEnteSubTipo(Long idEntePublico, Long idUsuario, List<Long> listaUnidAcceso);
    List<ServicioTableroBean> getAllConcentradoAllSubServicePorEnteSubTipoUnidad(Long idEntePublico, Long idUsuario, Long idUnidadAdministrativa);




}
