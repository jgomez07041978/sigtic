package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioSql;

import java.util.List;
import java.util.Optional;

public interface CatUsuarioService {

    List<CatUsuario> getUsuariosFiltro(CatUsuario catUsuario);

    List<CatUsuarioEntePublico> getUsuariosFiltroPorDependencia(CatUsuarioEntePublico catUsuarioEntePublico);


    CatUsuario getUsuario(Long idUsuario);

    List<CatUsuario> getUsuariosSinDependencia();

    List<CatUsuarioEntePublico> listaUsuariosPorRfc(List<String> lista);

    List<CatUsuario> getUsuarios(CatUsuario catUsuario);

    CatUsuario saveCatUsuario(CatUsuario catUsuario);

    CatUsuario deleteUsuario(CatUsuario catUsuario);

    Optional<CatUsuario> getUsuarioCurp(String curp);

    Optional<CatUsuario> getUsuarioRfc(String rfc);

    List<CatUsuarioEntePublico> getListaUsuarioPorCurp(String curp);
}
