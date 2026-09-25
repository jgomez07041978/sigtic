package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublicoReporte;

import java.util.List;
import java.util.Optional;

public interface EntePublicoService {

    List<EntePublico> getEntesPublicos();

    List<EntePublico> getEntesPublicos(String curp);

    EntePublico getEntePublicoId(Long idEntePublico);

    List<EntePublico> getEntesPublicoSaig();

    Optional<EntePublicoReporte> getEntePublicoReporte(Long idEntePublico, String estatus);
    Optional<EntePublicoReporte> getEntePublicoReporteId(Long idEntePublicoReporte);
    List<EntePublico> getEntePublicoUsuario(String rfc,String sistema, String estatus);

    void deleteLogoEntePublico(Integer idLogo);

    Optional<EntePublico> getEntePublicoLogo(Integer idLogo);

    void deleteLogoUnidadAdministrativa(Integer idLogo);

}
