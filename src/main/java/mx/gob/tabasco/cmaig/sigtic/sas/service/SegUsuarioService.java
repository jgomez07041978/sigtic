package mx.gob.tabasco.cmaig.sigtic.sas.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sas.model.RecuperarPassword;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuarioLista;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuarioPk;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.Perfil;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface SegUsuarioService {

    Perfil getPerfil();

    List<SegUsuarioLista> getTablaUsuarios(String rfc, Long idEntePublico, boolean roleAdminSigtic);

   // List<CatUsuarioEntePublico> getCatUsuariosPorUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa);

    List<CatUsuarioEntePublico> getCatUsuariosPorUnidadAdministrativaAll(UnidadAdministrativa unidadAdministrativa);

    List<CatUsuarioEntePublico> getCatUsuariosPorEntePublicoAll(EntePublico entePublico);

    List<CatUsuarioEntePublico> getCatUsuariosPorEntePublicoRfcAll(EntePublico entePublico, String rfc);

    SegUsuario getUsuarioId(Long idUsuario);

    SegUsuario saveUsuario(SegUsuario segUsuario);

    JSONObject deleteUsuario(SegUsuario segUsuario);

    SegUsuario actualizarPassword(SegUsuario segUsuario);

    EntePublico getEntePublico(Long idEntePublico);

    Optional<SegUsuario> getUsuarioOptional(Long idUsuarioEntePublico);

    SegUsuario saveUsuarioTemporal(SegUsuario segUsuario);

    SegUsuario actualizarEntePublicoActual(SegUsuario segUsuario);
    SegUsuario recuperarAcceso(RecuperarPassword recuperarPassword);

    RecuperarPassword validarDatosRecuperacion(RecuperarPassword recuperarPassword);

    Optional<SegUsuario> getUsuarioOptional(String usuario);

    SegUsuario recuperarPassword(SegUsuario segUsuario);

}
