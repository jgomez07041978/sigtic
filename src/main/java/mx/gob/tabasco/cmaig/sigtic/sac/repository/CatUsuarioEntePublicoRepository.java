package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public interface CatUsuarioEntePublicoRepository extends CrudRepository<CatUsuarioEntePublico, Long>  , JpaSpecificationExecutor<CatUsuarioEntePublico> {

    @Query("SELECT cu FROM CatUsuarioEntePublico cu WHERE cu.catUsuario.curp in (?1)")
    List<CatUsuarioEntePublico> getListaUsuariosPorCurp(List<String> lista);

    @Query("SELECT cu FROM CatUsuarioEntePublico cu WHERE cu.catUsuario.curp = ?1")
    List<CatUsuarioEntePublico> getListaUsuariosPorCurp(String curp);
    @Query("SELECT cu FROM CatUsuarioEntePublico cu WHERE cu.catUsuario.rfc in (?1)")
    List<CatUsuarioEntePublico> getListaUsuariosPorRfc(List<String> lista);

    @Query("SELECT cu FROM CatUsuarioEntePublico cu WHERE cu.unidadAdministrativa in (?1) and cu.cveEstatus=?2 order by cu.catUsuario.nombre, cu.catUsuario.segundoNombre, cu.catUsuario.apellidoPaterno, cu.catUsuario.apellidoMaterno, cu.catUsuario.curp")
    List<CatUsuarioEntePublico> getListaUsuariosPorUnidadAdmin(List<UnidadAdministrativa> listaUnidad, String estatus);

    Optional<CatUsuarioEntePublico> findByEntePublicoAndCatUsuario(EntePublico entePublico, CatUsuario catUsuario);

    List<CatUsuarioEntePublico> findByCatUsuarioOrderByEntePublico(CatUsuario catUsuario);
    Optional<CatUsuarioEntePublico> findByCatUsuarioAndEntePublico(CatUsuario catUsuario,EntePublico entePublico);
    @Query("SELECT COUNT(cu.catUsuario.idUsuario) FROM CatUsuarioEntePublico cu WHERE cu.catUsuario.idUsuario in (?1)")
    Integer getCountUsuario(Long idUsuario);

    @Query("SELECT cu FROM CatUsuarioEntePublico cu WHERE cu.entePublico=?1 and cu.cveEstatus='VIG' and cu.catUsuario.cveEstatus='VIG' order by cu.catUsuario.curp")
    List<CatUsuarioEntePublico> getCatUsuarioEntePublico(EntePublico entePublico);

    @Query("SELECT cu FROM CatUsuarioEntePublico cu WHERE cu.entePublico=?1 and cu.cveEstatus='VIG'")
    List<CatUsuarioEntePublico> getCatUsuarioEntePublicoList(EntePublico entePublico);

    @Query("SELECT cu FROM CatUsuarioEntePublico cu WHERE cu.entePublico=?1 and cu.cveEstatus='VIG' and cu.catUsuario.rfc like ?2% and cu.catUsuario.cveEstatus='VIG' order by cu.catUsuario.curp")
    List<CatUsuarioEntePublico> getCatUsuarioEntePublicoAndRfc(EntePublico entePublico, String rfc);

    @Query("SELECT u FROM CatUsuarioEntePublico u " +
            "LEFT JOIN FETCH u.entePublico ep " +
            "LEFT JOIN FETCH u.catUsuario " +
            "LEFT JOIN FETCH u.unidadAdministrativa " +
            "LEFT JOIN FETCH u.ubicacionUnidadAdmin ubi " +
            "ORDER BY ep.nombreEntePublico, u.catUsuario.apellidoPaterno, u.catUsuario.apellidoMaterno, u.catUsuario.nombre")
    List<CatUsuarioEntePublico> findAllOrderByEntePublicoAndApellidos();


    @Query("SELECT u FROM CatUsuarioEntePublico u " +
            "LEFT JOIN u.entePublico ep " +
            "LEFT JOIN u.unidadAdministrativa ua " +
            "WHERE (:idEntePublico IS NULL OR ep.idEntePublico = :idEntePublico) " +
            "AND (:idUnidadAdministrativa IS NULL OR ua.idUnidadAdministrativa = :idUnidadAdministrativa) " +
            "AND u.cveEstatus IN :listaEstatus " +
            "ORDER BY ep.nombreEntePublico, u.catUsuario.apellidoPaterno, u.catUsuario.apellidoMaterno, u.catUsuario.nombre")
    List<CatUsuarioEntePublico> getUsuariosFiltrados(
            @Param("idEntePublico") Long idEntePublico,
            @Param("idUnidadAdministrativa") Long idUnidadAdministrativa,
            @Param("listaEstatus") List<String> listaEstatus);


}
