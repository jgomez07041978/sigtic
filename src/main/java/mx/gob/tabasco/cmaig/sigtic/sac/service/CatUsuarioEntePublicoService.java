package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioSql;

import java.util.List;
import java.util.Optional;

public interface CatUsuarioEntePublicoService {

    CatUsuarioEntePublico actualizarPerfil(CatUsuarioEntePublico catUsuarioEntePublico);

    CatUsuarioEntePublico validarUsusario(CatUsuarioEntePublico catUsuarioEntePublicoService);

    List<CatUsuario> getCatUsuarioPorIdUnidad(Long idUnidad);

    CatUsuarioEntePublico guardarUsuario(CatUsuarioEntePublico catUsuarioEntePublico);

    CatUsuarioEntePublico guardarUsuarioEntePublico(CatUsuarioEntePublico catUsuarioEntePublico);

    CatUsuarioEntePublico getUsuario(Long idUsuarioEntepublico);

    CatUsuarioEntePublico deleteUsuario(CatUsuarioEntePublico catUsuarioEntePublico);

    List<CatUsuarioSql> getUsuariosPorEnte(CatUsuarioEntePublico catUsuarioEntePublico);

    List<CatUsuarioEntePublico> getEntesPublicoPorUsuario(Long idUsuario);

    CatUsuarioEntePublico getUsuarioEntePublico(Long idUsuario, Long idEntePublico);

    Optional<CatUsuarioEntePublico> getUsuarioEntePublicoOptional(Long idUsuario, Long idEntePublico);

    CatUsuarioEntePublico saveUsuario(CatUsuarioEntePublico catUsuarioEntePublico);
    List<CatUsuarioEntePublico> saveUsuarioAll(List<CatUsuarioEntePublico> catUsuarioEntePublicoList);

    CatUsuarioEntePublico getEntesPublicoVigentePorIdEnte(Long idUsuario, Long idEntePublico);

    mx.gob.tabasco.cmaig.sigtic.sai.model.CatUsuarioEntePublico getEntesPublicoVigentePorIdEnteSai(Long idUsuario, Long idEntePublico);

    List<CatUsuarioEntePublico> getListaCompletaUsuarios();


    List<CatUsuarioEntePublico> getUsuariosParaExportar(CatUsuarioEntePublico filtro);


}
