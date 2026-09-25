package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.OrganigramaBean;
import mx.gob.tabasco.cmaig.sigtic.sac.model.OrganigramaSql;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;

import java.util.List;
import java.util.Stack;

public interface OrganigramaService {

    OrganigramaBean getOrganigramaEntePublico(Long idEntePublico);

    List<OrganigramaSql> getOrganigramaId(Long idEntePublico);

    List<OrganigramaSql> getOrganigramaIdNivel2(Long idEntePublico, Long idSelecNivel2);

    List<OrganigramaSql> getOrganigramaIdNivel3(Long idEntePublico, Long idSelecNivel2 , Long idSelecNivel3);

    List<OrganigramaSql> getOrganigramaIdNivel4(Long idEntePublico, Long idSeleccionado);

    UnidadAdministrativa guardarUnidadAdministrativaOrg(UnidadAdministrativa unidadAdministrativa);

}
