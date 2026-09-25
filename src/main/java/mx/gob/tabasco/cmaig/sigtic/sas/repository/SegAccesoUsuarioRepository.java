package mx.gob.tabasco.cmaig.sigtic.sas.repository;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegAccesoUsuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SegAccesoUsuarioRepository extends CrudRepository<SegAccesoUsuario, Long> {

    List<SegAccesoUsuario> findAllByRfcUsuarioAndIdSistemaAndCveEstatus(String rfc, String sistema, String estatus);

    List<SegAccesoUsuario> findAllByRfcUsuarioAndIdSistemaAndIdEntePublicoAndCveEstatus(String rfc, String sistema, Long idEntePublico, String estatus);

    List<SegAccesoUsuario> findAllByRfcUsuarioAndIdSistemaAndIdEntePublicoAndIdUnidadAdministrativaAndCveEstatus(String rfc, String sistema, Long idEntePublico, Long idUnidadAdministrativa, String estatus);


    List<SegAccesoUsuario> findAllByRfcUsuarioAndIdSistema(String rfc, String sistema);

    @Query("SELECT distinct su.idEntePublico FROM SegAccesoUsuario su WHERE su.rfcUsuario = ?1 AND su.idSistema = ?2 AND su.cveEstatus = ?3")
    List<Long> getIdEntePublicoUnico(String rfc, String sistema, String estatus);

    List<SegAccesoUsuario> findSegAccesoUsuarioByRfcUsuarioAndIdSistema(String rfc, String sistema);

    Optional<SegAccesoUsuario> findByRfcUsuarioAndIdEntePublicoAndIdUnidadAdministrativaAndIdSistema(String rfc, Long idEntePublico, Long idUnidadAdministrativa, String sistema);
    Optional<SegAccesoUsuario> findByRfcUsuarioAndIdUnidadAdministrativaAndIdSistema(String rfc, Long idUnidadAdministrativa, String sistema);

}
