package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.SubServicioUsuario;
import mx.gob.tabasco.cmaig.sigtic.sai.model.Usuario;

import java.util.List;

public interface SubservicioUsuarioService {

    List<CatSubtipoServiciosTic> getSubserviciosAsignarUsuario(Long idUsuario);

    List<SubServicioUsuario> getSubServiciosAddUsuario(Long idUsuario);

    SubServicioUsuario asignarSubTipoServicioUsuario(SubServicioUsuario subServicioUsuario);
    void removerSubTipoServicioUsuario(SubServicioUsuario subServicioUsuario);

}
