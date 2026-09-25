package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("unidadAdministrativaRepositorySac")
public interface UnidadAdministrativaRepository extends JpaRepository<UnidadAdministrativa, Long> {

     //"LEFT JOIN NivelUnidadAdministrativa nu on ua.nivelUnidadAdministrativa=nu.idNivelUnidadAdministrativa " +
    //"LEFT JOIN NivelUnidadAdministrativa nu on ua.nivelUnidadAdministrativa.idNivelUnidadAdministrativa=nu.idNivelUnidadAdministrativa " +
/*    @Query("SELECT ua FROM UnidadAdministrativa ua " +
            "WHERE ua.idEntePublico=?1 and ua.estatus=?2"
    )
    List<UnidadAdministrativa> getUnidadesAdministrativaPorEnteId(Long idEnte, String estatus);*/

    List<UnidadAdministrativa> findAllByIdEntePublicoAndEstatus(Long idEnte, String estatus, Sort sort);

    List<UnidadAdministrativa> findAllByCveUnidadAdministrativaAndEstatus(String cveUnidadAdministrativa, String estatus);

    List<UnidadAdministrativa> findAllByIdUnidadAdministrativaPadreAndEstatus(Long id, String estatus);
    List<UnidadAdministrativa> findAllByIdLogo(Integer idLogo);

    List<UnidadAdministrativa> findAllByEstatus(String estatus);

}
