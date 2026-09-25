package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.PuestoUsuario;

import java.util.List;

public interface PuestoUsuarioService {

    List<PuestoUsuario> listaPuestosPorEntePublico(Long idEntePublico);

}
