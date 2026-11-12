package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sac.pojo.DirectorioResponsableUnidad;
import mx.gob.tabasco.cmaig.sigtic.sac.pojo.DirectorioUsuarioExcel;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public interface CatUsuarioEntePublicoRepository extends CrudRepository<CatUsuarioEntePublico, Long>  , JpaSpecificationExecutor<CatUsuarioEntePublico> {

    @RestResource(exported = false)
    @Query("SELECT new mx.gob.tabasco.cmaig.sigtic.sac.pojo.DirectorioUsuarioExcel(" +
            "c.idUsuarioEntePublico, p.nombre, p.segundoNombre, p.apellidoPaterno, p.apellidoMaterno, " +
            "c.puesto, e.nombreEntePublico, u.nombreUnidadAdministrativa, " +
            "c.numTelefonoInstitucional, c.numeroExtensionInstitucional, c.correoInstitucional) " +
            "FROM CatUsuarioEntePublico c JOIN c.catUsuario p JOIN c.entePublico e " +
            "LEFT JOIN c.unidadAdministrativa u " +
            "WHERE e.idEntePublico = :idEntePublico AND c.cveEstatus IN ('VIG', 'HIS')")
    List<DirectorioUsuarioExcel> findUsuariosDirectorioPorEnte(@Param("idEntePublico") Long idEntePublico);

    @RestResource(exported = false)
    @Query("SELECT new mx.gob.tabasco.cmaig.sigtic.sac.pojo.DirectorioUsuarioExcel(" +
            "c.idUsuarioEntePublico, p.nombre, p.segundoNombre, p.apellidoPaterno, p.apellidoMaterno, " +
            "c.puesto, e.nombreEntePublico, u.nombreUnidadAdministrativa, " +
            "c.numTelefonoInstitucional, c.numeroExtensionInstitucional, c.correoInstitucional) " +
            "FROM CatUsuarioEntePublico c JOIN c.catUsuario p JOIN c.entePublico e " +
            "JOIN c.unidadAdministrativa u " +
            "WHERE e.idEntePublico = :idEntePublico AND u.idUnidadAdministrativa = :idUnidadAdministrativa " +
            "AND u.idEntePublico = :idEntePublico AND c.cveEstatus IN ('VIG', 'HIS')")
    List<DirectorioUsuarioExcel> findUsuariosDirectorioPorUnidad(@Param("idEntePublico") Long idEntePublico,
            @Param("idUnidadAdministrativa") Long idUnidadAdministrativa);

    @RestResource(exported = false)
    @Query("SELECT new mx.gob.tabasco.cmaig.sigtic.sac.pojo.DirectorioResponsableUnidad(" +
            "ua.idUnidadAdministrativa, p.nombre, p.segundoNombre, p.apellidoPaterno, p.apellidoMaterno, " +
            "c.puesto, c.correoInstitucional, c.numTelefonoInstitucional, c.numeroExtensionInstitucional) " +
            "FROM UnidadAdministrativa ua " +
            "JOIN CatUsuario p ON UPPER(TRIM(BOTH ' ' FROM p.rfc)) = UPPER(TRIM(BOTH ' ' FROM ua.rfcTitular)) " +
            "AND p.cveEstatus = 'VIG' " +
            "LEFT JOIN CatUsuarioEntePublico c ON c.catUsuario = p AND c.unidadAdministrativa = ua " +
            "AND c.entePublico.idEntePublico = ua.idEntePublico AND c.cveEstatus = 'VIG' " +
            "WHERE ua.idEntePublico = :idEntePublico AND ua.estatus = 'VIG' " +
            "ORDER BY ua.idUnidadAdministrativa, c.fechaUltimoCambio DESC NULLS LAST, " +
            "c.idUsuarioEntePublico DESC NULLS LAST, p.idUsuario")
    List<DirectorioResponsableUnidad> findResponsablesDirectorioPorEnte(@Param("idEntePublico") Long idEntePublico);

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
