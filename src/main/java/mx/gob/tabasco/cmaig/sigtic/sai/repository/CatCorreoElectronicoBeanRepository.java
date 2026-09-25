package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatCorreoElectronicoBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatCorreoElectronicoBeanRepository extends CrudRepository<CatCorreoElectronicoBean, Long> {

    List<CatCorreoElectronicoBean> getCorreosElectronicoInstitucion(Long idEntePublico);

    List<CatCorreoElectronicoBean> getCorreosElectronicoInstitucionPorUnidad(Long idEntePublico, Long idUnidadAdministrativa);

    List<CatCorreoElectronicoBean> getCorreosElectronicoInstitucionListaUnidad(Long idEntePublico, List<Long> lista);

    List<CatCorreoElectronicoBean> getCorreosElectronicoInstitucionNulo();
    List<CatCorreoElectronicoBean> getCorreosElectronicoSearch(String correo, List<Long> lista);
    List<CatCorreoElectronicoBean> getCorreosElectronicoSearchAdmin(String correo);
    List<CatCorreoElectronicoBean> getCorreosElectronicoModal(Long idEntePublico, Long idUnidadAdministrativa, Integer idTipoCorreo, String estatusCorreo);
    List<CatCorreoElectronicoBean> getCorreosElectronicoModalSinUnidad(Long idEntePublico, Integer idTipoCorreo, String estatusCorreo);

    List<CatCorreoElectronicoBean> getCorreosElectronicoModalTodo(Long idEntePublico, Long idUnidadAdministrativa, Integer idTipoCorreo);

    List<CatCorreoElectronicoBean> getCorreosElectronicoModalSinUnidadTodo(Long idEntePublico, Integer idTipoCorreo);

}
