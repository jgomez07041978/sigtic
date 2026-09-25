package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sai.model.Usuario;
import mx.gob.tabasco.cmaig.sigtic.sai.model.UsuarioContactoProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("catUsuarioEntePublicoRepositorySai")
public interface CatUsuarioEntePublicoRepository extends CrudRepository<CatUsuarioEntePublico, Long> {

    Optional<CatUsuarioEntePublico> findByCatUsuarioAndEntePublico(Usuario usuario, EntePublico entePublico);

    @Query("SELECT u.curp AS curp, " +
            "CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(COALESCE(u.nombre, ''), ' '), COALESCE(u.segundoNombre, '')), ' '), COALESCE(u.apellidoPaterno, '')), CONCAT(' ', COALESCE(u.apellidoMaterno, ''))) AS nombreCompleto " +
            "FROM CatUsuarioEntePublico cuep JOIN cuep.catUsuario u " +
            "WHERE cuep.entePublico.idEntePublico = :idEntePublico " +
            "AND cuep.cveEstatus = :cveEstatus " +
            "AND u.curp IS NOT NULL " +
            "ORDER BY u.nombre ASC, u.apellidoPaterno ASC")
    List<UsuarioContactoProjection> findUsuariosContactoPorEnte(@Param("idEntePublico") Long idEntePublico, @Param("cveEstatus") String cveEstatus);

}
