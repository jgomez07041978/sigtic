package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.DomicilioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.DomicilioEntePublicoBean;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DomicilioEntePublicoService {

    List<DomicilioEntePublico> listaDomicilioEntePublico(Long idEntePublico);
    List<DomicilioEntePublicoBean> listaDomicilioEntePublicoBean(Long idEntePublico);

    DomicilioEntePublico deleteDomicilio(DomicilioEntePublico domicilioEntePublico);

    DomicilioEntePublico saveDomicilio(DomicilioEntePublico domicilioEntePublico);

    DomicilioEntePublico getDomicilioId(Long idDomicilio);

    void exportarDomiciliosPorEnte(Long idEntePublico, HttpServletResponse response);

    void exportarUbicacionesPorEnte(Long idEntePublico, HttpServletResponse response);

    void exportarTodasUbicaciones(HttpServletResponse response);

    void exportarTodosDomicilios(HttpServletResponse response);

}
