package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.ServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.UsuarioServicio;
import mx.gob.tabasco.cmaig.sigtic.sai.model.UsuarioServicioBean;

import java.util.List;

public interface MisServicios {

    List<UsuarioServicio> getMisServicios();

    List<UsuarioServicioBean> getMisServiciosBean();

    ServiciosTic enviarServicio(ServiciosTic serviciosTic);

    List<UsuarioServicio> getServiciosSeguimiento();

    ServiciosTic rechazarServicio(ServiciosTic serviciosTic);

    ServiciosTic autorizarServicio(ServiciosTic serviciosTic);

}
