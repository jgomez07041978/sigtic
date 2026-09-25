package mx.gob.tabasco.cmaig.sigtic.sas.service;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegioAsignado;
import net.minidev.json.JSONArray;

import java.util.List;

public interface SegPerfilPrivilegioService {

    SegPerfilPrivilegio getPerfilPrivilegioMenu(String sistema, String usuario, Integer idMenu);

    List<SegPerfilPrivilegioAsignado> getPrivilegiosPorRol(Integer idRol);

    JSONArray savePervilegioPorPerfil(SegPerfil perfil);

    SegPerfilPrivilegio actualizarPerfilPrivilegio(Long idPerfilPrivilegio, String tipo, Boolean valor);

/*    boolean existeRolAsignado(String rol);*/
}
