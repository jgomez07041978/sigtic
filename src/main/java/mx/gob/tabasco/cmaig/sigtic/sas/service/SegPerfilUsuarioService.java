package mx.gob.tabasco.cmaig.sigtic.sas.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.SegPerfilUsuarioCount;

import java.util.List;

public interface SegPerfilUsuarioService {

    List<SegPerfilUsuarioCount> getNumUsuarioByPerfil();

    List<CatUsuarioEntePublico> getUsuariosRol(SegPerfil segPerfil);

    List<SegPerfilUsuario> getRolesUsuarioAsignados(String rfc);

    List<SegPerfil> getRolesDisponibles(String rfcUsuario, String rfcUsuarioLogueado, boolean roleAdminSigtic);

    //List<SegPerfil> getRolesAsignados(String rfcUsuario);

    SegPerfilUsuario asignarRolUsuario(SegUsuario segUsuario);

    SegPerfilUsuario asignarRolUsuarioTemporal(SegUsuario segUsuario);

    void removerRolUsuario(SegPerfilUsuario segPerfilUsuario);

}
