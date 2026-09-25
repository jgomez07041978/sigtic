package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.SubServicioUsuario;
import mx.gob.tabasco.cmaig.sigtic.sai.model.Usuario;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatSubtipoServiciosTicRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.SubServicioUsuarioRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.UsuarioRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubservicioUsuarioServiceImp implements SubservicioUsuarioService{

    @Autowired
    private CatSubtipoServiciosTicRepository catSubtipoServiciosTicRepository;

    @Autowired
    private SubServicioUsuarioRepository subServicioUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Override
    public List<CatSubtipoServiciosTic> getSubserviciosAsignarUsuario(Long idUsuario) {
        return catSubtipoServiciosTicRepository.getAllSubtipoServicioNoAddUsuario(idUsuario);
    }

    @Override
    public List<SubServicioUsuario> getSubServiciosAddUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        return subServicioUsuarioRepository.findAllByUsuario(usuario);
    }

    @Transactional(value = "saiSigticTransactionManager")
    @Override
    public SubServicioUsuario asignarSubTipoServicioUsuario(SubServicioUsuario subServicioUsuario) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CatSubtipoServiciosTic catSubtipoServiciosTic= catSubtipoServiciosTicRepository.findById(subServicioUsuario.getCatSubtipoServiciosTic().getIdSubtipoServicioTic()).get();
        Usuario usuario = usuarioRepository.findById(subServicioUsuario.getUsuario().getIdUsuario()).get();

        Optional<SubServicioUsuario> subServicioUsuarioOp = subServicioUsuarioRepository.findByCatSubtipoServiciosTicAndUsuario(catSubtipoServiciosTic, usuario);

        if (subServicioUsuarioOp.isPresent())
            throw new RuntimeException("El sub-servicio ya esta asignado");

        SubServicioUsuario subServicioUsuario1 = new SubServicioUsuario();
        subServicioUsuario1.setCatSubtipoServiciosTic(catSubtipoServiciosTic);
        subServicioUsuario1.setUsuario(usuario);
        subServicioUsuario1.setCveUsuarioMtto(authUser.getUsername());
        subServicioUsuario1.setFechaRegistro(new Timestamp(new Date().getTime()));

        return subServicioUsuarioRepository.save(subServicioUsuario1);
    }

    @Transactional(value = "saiSigticTransactionManager")
    @Override
    public void removerSubTipoServicioUsuario(SubServicioUsuario subServicioUsuario) {
        Optional<SubServicioUsuario> subServicioUsuarioOp = subServicioUsuarioRepository.findById(subServicioUsuario.getIdSubtipoServicioUsuario());

        if (!subServicioUsuarioOp.isPresent())
            throw new RuntimeException("El sub-servicio ya no existe");

        subServicioUsuarioRepository.delete(subServicioUsuarioOp.get());
    }
}
