package mx.gob.tabasco.cmaig.sigtic.sas.service;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilBean;
import net.minidev.json.JSONObject;

import java.util.List;

public interface SegRolService {

/*
    List<SegPerfil> getRolesByUsuarioAndEstatus(String rfc, String estatus);
*/
    SegPerfil getPerfilId(Integer idRol);

    List<SegPerfil> getRolesBySistemaAndEstatus(String sistema, String estatus);

    SegPerfil saveRol(SegPerfil segPerfil);

    JSONObject deleteRol(SegPerfil segPerfil);

    List<SegPerfilBean> getRolesBySistemaAndEstatusSql(String sistema, String estatus);

}
