package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.SacCatUsuarioEntePublicoView;
import org.springframework.data.domain.Page;

public interface SacCatUsuarioEntePublicoViewService {

    Page<SacCatUsuarioEntePublicoView> getSearchDirectorio(SacCatUsuarioEntePublicoView sacUsu);

    Page<CatUsuarioEntePublico> getSearchUser(SacCatUsuarioEntePublicoView sacUsu);

}