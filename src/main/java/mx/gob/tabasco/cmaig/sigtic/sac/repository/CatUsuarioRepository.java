package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatUsuarioRepository extends CrudRepository<CatUsuario, Long> , JpaSpecificationExecutor<CatUsuario> {

    //Optional<CatUsuario> findByIdUsuario(Long idUsuario);

    Optional<CatUsuario> findByRfc(String rfc);

    Optional<CatUsuario> findByCurp(String curp);

    Optional<CatUsuario> findByRfcAndCveEstatus(String rfc,String estatus);

    @Query("SELECT cu FROM CatUsuario cu WHERE cu.idUsuario not in (SELECT cu.catUsuario.idUsuario FROM CatUsuarioEntePublico cu)")
    List<CatUsuario> getUsuariosSinDependencia();

/*    @Query("SELECT cu FROM CatUsuario cu " +
            "inner join CatUsuarioEntePublico ue on ue.catUsuario.idUsuario=cu.idUsuario " +
            "WHERE ue.entePublico.idEntePublico=?1 ")
    List<CatUsuario> getUsuariosPorDependencia(Long idEntePublico, Specification<CatUsuario> usu);*/

    List<CatUsuario> findAllByCveEstatusInOrderByNombreAscSegundoNombreAscApellidoPaternoAscApellidoMaternoAscCurpAsc(List<String> listaEstatus);
    //List<CatUsuario> findAllByNombreCompletoLikeAndCveEstatusInOrderByNombreAscSegundoNombreAscApellidoPaternoAscApellidoMaternoAsc(String nombre, List<String> listaEstatus);

}
