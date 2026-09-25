package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.Logo;
import mx.gob.tabasco.cmaig.sigtic.sac.model.LogoBean;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface LogoService {

    List<LogoBean> getLogos();

    List<LogoBean> getLogosEnte();

    Logo importarLogo(Logo logo);

    void exportLogo(HttpServletResponse response, Integer id);

    void exportLogoCr(HttpServletResponse response, Long id);

    Logo deleteLogo(Integer idLogo);

    UnidadAdministrativa asignarLogo(UnidadAdministrativa unidadAdministrativa);

}
