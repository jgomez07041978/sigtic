package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioSql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatUsuarioSqlRepository extends JpaRepository<CatUsuarioSql,Long> {


    List<CatUsuarioSql> getUsuarioAdminEntidad(Long idEntePublico, List<String> listaEstatus);

    List<CatUsuarioSql> getUsuarioAdminEntidadNombre(Long idEntePublico, String nombre, List<String> listaEstatus);

    List<CatUsuarioSql> getUsuarioAdminUnidad(Long idEntePublico, Long idUnidadAdministrativa, List<String> listaEstatus);

    List<CatUsuarioSql> getUsuarioAdminUnidadNombre(Long idEntePublico, Long idUnidadAdministrativa, String nombre, List<String> listaEstatus);

    List<CatUsuarioSql> getUsuarioEntidad(Long idEntePublico, Long idUnidadAdministrativa, String rfcUsuario, List<String> listaEstatus);

    List<CatUsuarioSql> getUsuarioEntidadNombre(Long idEntePublico, Long idUnidadAdministrativa, String rfcUsuario, String nombre, List<String> listaEstatus);


/*
    List<CatUsuarioSql> getUsuarioPorUnidadAdministrativa(String rfcUsuario, Long idEntePublico, Long idUnidadAdministrativa, List<String> listaEstatus);

    List<CatUsuarioSql> getUsuarioPorUnidadAdministrativaNombre(String rfcUsuario, Long idEntePublico, Long idUnidadAdministrativa, String nombre, List<String> listaEstatus);

    List<CatUsuarioSql> getUsuarioTodo(String rfcUsuario, List<String> listaEstatus);

    List<CatUsuarioSql> getUsuarioTodoNombre(String rfcUsuario, String nombre, List<String> listaEstatus);

    List<CatUsuarioSql> getUsuarioAdminTodo(List<String> listaEstatus);

    List<CatUsuarioSql> getUsuarioAdminTodoNombre(String nombre, List<String> listaEstatus);

    List<CatUsuarioSql> getUsuarioNotDependencia(List<String> listaEstatus);

    List<CatUsuarioSql> getUsuarioNotDependenciaNombre(String nombre, List<String> listaEstatus);

    List<CatUsuarioSql> getUsuarioPorEntidad(String rfcUsuario, Long idEntePublico, Long idUnidadAdministrativa, List<String> listaEstatus);

    List<CatUsuarioSql> getUsuarioPorEntidadNombre(String rfcUsuario, Long idEntePublico, Long idUnidadAdministrativa, String nombre, List<String> listaEstatus);
    */
}
