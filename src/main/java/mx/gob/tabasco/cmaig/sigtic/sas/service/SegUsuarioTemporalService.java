package mx.gob.tabasco.cmaig.sigtic.sas.service;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuarioTemporal;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuarioTemporalRol;
import net.minidev.json.JSONObject;

import java.util.List;
import java.util.Optional;

public interface SegUsuarioTemporalService {

    SegUsuarioTemporal guardarUsuario(SegUsuarioTemporal segUsuarioTemporal);

    SegUsuarioTemporal actualizarUsuario(SegUsuarioTemporal segUsuarioTemporal);

    Optional<SegUsuarioTemporal> getEstatusCorreo(String correo);

    SegUsuarioTemporal searchUsuario(String curp, String correo);

    SegUsuarioTemporal generandoUsuarios(Long idUsuarioTemporal);

    List<SegUsuarioTemporal> listaUsuariosTemporal(String estatus);

    List<SegUsuarioTemporal> listaUsuariosTemporalAll();

    Integer totalRoles(SegUsuarioTemporal segUsuarioTemporal);

    SegUsuarioTemporal savePreRegistro(SegUsuarioTemporal segUsuarioTemporal);

    JSONObject deletePreRegistro(SegUsuarioTemporal segUsuarioTemporal);

    SegUsuarioTemporal getUsuarioTemporalId(Long idUsuarioTemporal);

    List<SegUsuarioTemporalRol> listaSegUsuarioTemporalRol(SegUsuarioTemporal segUsuarioTemporal);

    SegUsuarioTemporalRol addRolTemporal(SegUsuarioTemporalRol segUsuarioTemporalRol);

    JSONObject deleteRolPreRegistro(SegUsuarioTemporalRol segUsuarioTemporalRol);

    SegUsuarioTemporal regresarPreRegistro(SegUsuarioTemporal segUsuarioTemporal);

    SegUsuarioTemporal generarToken(SegUsuarioTemporal segUsuarioTemporal);
}
