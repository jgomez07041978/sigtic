package mx.gob.tabasco.cmaig.sigtic.sas.service;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegAccesoUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.SegPermisosBean;
import net.minidev.json.JSONObject;

import java.util.List;

public interface SegAccesoUsuarioService {

    SegAccesoUsuario addPermisosTreeAll(SegPermisosBean segPermisosBean);

    JSONObject deletePermisosTreeAll(SegPermisosBean segPermisosBean);

    List<Long> getUnidadesAdministrativaAcceso(String rfc, Long idEntePublico);
}
