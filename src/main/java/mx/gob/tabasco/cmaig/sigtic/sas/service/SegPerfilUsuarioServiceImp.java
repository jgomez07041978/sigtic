package mx.gob.tabasco.cmaig.sigtic.sas.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.SegPerfilUsuarioCount;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.SegPerfilRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.SegPerfilUsuarioRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.service.CatUsuarioService;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.SegUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SegPerfilUsuarioServiceImp implements SegPerfilUsuarioService {

    @Autowired
    private SegPerfilUsuarioRepository segPerfilUsuarioRepository;

    @Autowired
    private SegPerfilRepository segPerfilRepository;
    @Autowired
    private CatUsuarioService catUsuarioService;

    @Autowired
    private SegUsuarioRepository segUsuarioRepository;

    @Override
    public List<SegPerfilUsuarioCount> getNumUsuarioByPerfil() {
        return segPerfilUsuarioRepository.getNumUsuarioByPerfil();
    }

    @Override
    public List<CatUsuarioEntePublico> getUsuariosRol(SegPerfil segPerfil) {
        SegPerfil perfil=segPerfilRepository.findById(segPerfil.getIdPerfil()).get();
        List<String> listaRfcUsuario= segPerfilUsuarioRepository.getListaRfcUsuarioPerfilBySistema(perfil,"SIGTIC");
        return catUsuarioService.listaUsuariosPorRfc(listaRfcUsuario);
    }


    @Override
    public List<SegPerfilUsuario> getRolesUsuarioAsignados(String rfc) {

        return segPerfilUsuarioRepository.findBySegUsuarioAndSistemaOrderBySegPerfil(rfc,"SIGTIC");
    }

    @Override
    public List<SegPerfil> getRolesDisponibles(String rfcUsuario, String rfcUsuarioLogueado, boolean roleAdminSigtic) {

        List<Integer> listaPerfilesAsignados = segPerfilRepository.findByPerfilesAsignados(rfcUsuario, "SIGTIC");

        if (listaPerfilesAsignados.size() == 0) {
            if (roleAdminSigtic)
                return segPerfilRepository.findPerfilesDisponibles("SIGTIC", "VIG");
            else
                return segPerfilRepository.getPerfilesUsuario(rfcUsuarioLogueado);

        }else{
            if (roleAdminSigtic)
                return segPerfilRepository.findPerfilesDisponiblesNoAsignados("SIGTIC", "VIG", listaPerfilesAsignados);
            else
                return segPerfilRepository.getPerfilesUsuarioAsignado(rfcUsuarioLogueado, listaPerfilesAsignados);
        }

    }

/*    @Override
    public List<SegPerfil> getRolesAsignados(String rfcUsuario) {
        List<SegPerfilUsuario> listaPerfilUsuarioAsignados = segPerfilUsuarioRepository.findBySegUsuarioAndSistemaOrderBySegPerfil(rfcUsuario, "SIGTIC");
        List<SegPerfil> listaPerfilAsignado= listaPerfilUsuarioAsignados.stream().map(SegPerfilUsuario::getSegPerfil).collect(Collectors.toList());

        return listaPerfilAsignado;
    }*/


    @Transactional(value = "seguridadTransactionManager")
    @Override
    public SegPerfilUsuario asignarRolUsuario(SegUsuario segUsuario) {
        SegPerfilUsuario segPerfilUsuario = segPerfilUsuarioRepository.findByPerfilPorUsuario(segUsuario.getSegUsuarioPk().getUsuario(), segUsuario.getIdPerfil(), "SIGTIC");

        if (segPerfilUsuario!=null)
            throw new RuntimeException("El rol ya esta asignado");

        SegPerfil perfil = segPerfilRepository.findById(segUsuario.getIdPerfil()).get();

        SegPerfilUsuario perfilUsuario = new SegPerfilUsuario();
        perfilUsuario.setSegUsuario(segUsuario.getSegUsuarioPk().getUsuario());
        perfilUsuario.setSegPerfil(perfil);
        perfilUsuario.setSistema("SIGTIC");

        return segPerfilUsuarioRepository.save(perfilUsuario);
    }

    @Transactional(value = "seguridadTransactionManager")
    @Override
    public SegPerfilUsuario asignarRolUsuarioTemporal(SegUsuario segUsuario) {
        SegPerfilUsuario segPerfilUsuario = segPerfilUsuarioRepository.findByPerfilPorUsuario(segUsuario.getSegUsuarioPk().getUsuario(), segUsuario.getIdPerfil(), "SIGTIC");

        if (segPerfilUsuario==null) {
            SegPerfil perfil = segPerfilRepository.findById(segUsuario.getIdPerfil()).get();

            SegPerfilUsuario perfilUsuario = new SegPerfilUsuario();
            perfilUsuario.setSegUsuario(segUsuario.getSegUsuarioPk().getUsuario());
            perfilUsuario.setSegPerfil(perfil);
            perfilUsuario.setSistema("SIGTIC");

            return segPerfilUsuarioRepository.save(perfilUsuario);
        }else
            return segPerfilUsuario;

    }

    @Transactional(value = "seguridadTransactionManager")
    @Override
    public void removerRolUsuario(SegPerfilUsuario segPerfilUsuario) {
        Optional<SegPerfilUsuario> perfilUsuario = segPerfilUsuarioRepository.findById(segPerfilUsuario.getIdSegPerfilUsuario());

        if (!perfilUsuario.isPresent())
            throw new RuntimeException("El rol ya no existe");

        segPerfilUsuarioRepository.delete(perfilUsuario.get());
    }


}