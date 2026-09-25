package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ServiciosTicService {

/*    List<ServicioTicBean> getListaServicioAll(String rfc, Long idEntePublico, boolean roleAdminSigtic);
    List<ServicioTicBean> getListaServicioAdmin(String rfc, Long idEntePublico, boolean roleAdminSigtic);
    List<ServicioTicBean> getListaSistemasOpera(String rfc, Long idEntePublico, boolean roleAdminSigtic);*/

    JSONObject getListaServiciosTablero(UnidadAdministrativa unidadAdministrativa);

    List<ServicioGroupBean> getListaServicioGroup(String curp, Long idEntePublico, boolean roleAdminSigtic);

    List<ServiciosTic> getListaServicioPorEntePorTipoAndSubtipo(Long idEntePublico, Long idUnidadAdministrativa, Long idTipoServicio, Long idSubtipoServicio, Integer tipo, String estatus);


    List<ServiciosTic> getListaServicioAll(String rfc, Long idEntePublico, boolean roleAdminSigtic);

    List<ServiciosTic> getListaServicioAdmin(String rfc, Long idEntePublico, boolean roleAdminSigtic);

    List<ServiciosTic> getListaSistemasOpera(String rfc, Long idEntePublico, boolean roleAdminSigtic);

    ServiciosTic guardarServicios(ServiciosTic serviciosTic);

    ServiciosTic guardarMiServicio(ServiciosTic serviciosTic);

    ServiciosTic getServiciosTic(Long idServicioTic);

    ServiciosTic deleteServiciosTic(ServiciosTic serviciosTic);

    List<ServiciosTic> getServiciosSearch(Long idServicio, String nombreServicio, String clasificacion, boolean sinFiltrar);

    List<ServiciosTicRelacionados> getServiciosTicRelacionados(ServiciosTic serviciosTicParam);

    ServiciosTicRelacionados addServicio(ServiciosTicRelacionados serviciosTicRelacionados);

    ServiciosTicRelacionados deleteServicioRelacionado(ServiciosTicRelacionados serviciosTicRelacionados);

    UsuarioServicio guardarUsuarioServicios(UsuarioServicio usuarioServicio);

    List<UsuarioServicio> getUsuariosServicios(Long idServicios);

    UsuarioServicio deleteUsuarioServicio(Long idUsuarioServicio);

    Page<ServiciosTic> getServiciosSearch(ServiciosTic serviciosTic);

    Page<UsuarioServicio> getUsuarioServiciosSearch(ServiciosTic serviciosTic);

    List<UsuarioServicio> getUsuariosServiciosPorServicios(List<ServiciosTic> serviciosTics);

    void escribirExcelServiciosAdministra(List<ServiciosTic> lista, Integer tipo, String tipoEntePublicoText,
                                          String entePublicoText, String tipoServicioText, String subTipoServicioText,
                                          HttpServletResponse response) throws IOException;

    void escribirExcelServiciosOpera(List<ServiciosTic> lista, String tipoEntePublicoText, String entePublicoText, String tipoServicioText, String subTipoServicioText,
            HttpServletResponse response) throws IOException;
}