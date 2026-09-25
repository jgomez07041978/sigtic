package mx.gob.tabasco.cmaig.sigtic.sai.repository;


import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioServicioRepository extends CrudRepository<UsuarioServicio, Long> , JpaSpecificationExecutor<UsuarioServicio> {

    //List<UsuarioServicio> findAllByCurpUsuarioServicioAndIdUsuarioEntePublicoAndCveEstatusInOrderByIdUsuarioServicio(String curp, Long idUSuarioEntePublico, List<String> lista);
    List<UsuarioServicio> findAllByCurpUsuarioServicioAndCatUsuarioEntePublicoAndCveEstatusInOrderByIdUsuarioServicio(String curp, CatUsuarioEntePublico catUsuarioEntePublico, List<String> lista);

    //List<ServiciosTic> findAllByEntePublicoOperaAndUnidadAdministrativaOperaInAndCveEstatusInAndCatEstatusSeguimientoInOrderByIdServicioTicDesc(EntePublico entePublico, List<UnidadAdministrativa> listaUnidadAdmin, List<String> estatus, List<CatEstatusServicio> lista);

    Optional<UsuarioServicio> findByServiciosTicAndCatUsuarioEntePublico(ServiciosTic serviciosTic, CatUsuarioEntePublico catUsuarioEntePublico);

    List<UsuarioServicio> findAllByServiciosTicAndCveEstatusInOrderByIdUsuarioServicio(ServiciosTic serviciosTic, List<String> listaEstatus);

    @Query("SELECT us FROM UsuarioServicio us " +
            "LEFT JOIN FETCH us.catUsuarioEntePublico cuep " +
            "LEFT JOIN FETCH cuep.catUsuario " +
            "LEFT JOIN FETCH us.catTipoUsuarioServicio " +
            "WHERE us.serviciosTic IN :serviciosTics AND us.cveEstatus IN :listaEstatus " +
            "ORDER BY us.idUsuarioServicio")
    List<UsuarioServicio> findAllByServiciosTicInAndCveEstatusInFetch(
            @Param("serviciosTics") List<ServiciosTic> serviciosTics,
            @Param("listaEstatus") List<String> listaEstatus);

}
