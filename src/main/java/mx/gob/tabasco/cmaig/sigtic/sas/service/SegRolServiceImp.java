package mx.gob.tabasco.cmaig.sigtic.sas.service;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilBean;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.SegPerfilBeanRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.SegPerfilPrivilegioRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.SegPerfilRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.SegPerfilUsuarioRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SegRolServiceImp implements SegRolService {

    @Autowired
    private SegPerfilRepository segPerfilRepository;

    @Autowired
    private SegPerfilUsuarioRepository segPerfilUsuarioRepository;

    @Autowired
    private SegPerfilPrivilegioRepository segPerfilPrivilegioRepository;

    @Autowired
    private SegPerfilBeanRepository segPerfilBeanRepository;

/*    @Override
    public List<SegPerfil> getRolesByUsuarioAndEstatus(String usuario, String estatus) {
        return segRolRepository.getRolesByUsuarioAndEstatus(usuario, estatus);
    }*/

    @Override
    public SegPerfil getPerfilId(Integer idRol) {
        return segPerfilRepository.findById(idRol).get();
    }

    @Override
    public List<SegPerfil> getRolesBySistemaAndEstatus(String sistema, String estatus) {
        //return segPerfilRepository.findBySistemaAndCveEstatus(sistema, estatus);
        return segPerfilRepository.findBySistemaAndCveEstatus(sistema, estatus);
    }

    @Override
    public List<SegPerfilBean> getRolesBySistemaAndEstatusSql(String sistema, String estatus) {
        //return segPerfilRepository.findBySistemaAndCveEstatus(sistema, estatus);
        return segPerfilBeanRepository.findBySistemaAndCveEstatusSql(sistema, estatus);
    }

    @Override
    public SegPerfil saveRol(SegPerfil rol) {

        rol.setCveEstatus("VIG");
        rol.setSistema("SIGTIC");
        rol.setDefecto("N");

        return segPerfilRepository.save(rol);
    }

    @Override
    public JSONObject deleteRol(SegPerfil rol) {

        SegPerfil rolTemp = segPerfilRepository.findById(rol.getIdPerfil()).get();

        int existeUsuario= segPerfilUsuarioRepository.existeUsuarioPorRol(rolTemp);

        if (existeUsuario>0)
            throw new NoSuchElementException("El rol esta asignado a usuarios del sistema.");

        int existePrivilegio = segPerfilPrivilegioRepository.existePrivilegiosPorRol(rolTemp);

        if (existePrivilegio>0)
            throw new NoSuchElementException("El rol tiene permisos asignados.");

        segPerfilRepository.delete(rolTemp);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message","Eliminado correctamente");

        return jsonObject;

    }


}