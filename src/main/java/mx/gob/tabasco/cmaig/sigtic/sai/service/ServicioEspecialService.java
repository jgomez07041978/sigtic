package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.ServicioEspecial;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServicioEspecialFiltro;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServicioEspecialReporteDTO;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.ServicioEspecialRepository.CatalogoItem;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ServicioEspecialService {

    ServicioEspecial getServicioEspecial(String numCuentaMaestra, Integer noLote);

    ServicioEspecial guardarServicioEspecial(ServicioEspecial servicioEspecial, boolean esNuevo,
                                             Integer noLoteOriginal, String numCuentaMaestraOriginal);

    List<CatalogoItem> getPartidas();

    List<CatalogoItem> getArticulosPorPartida(String cvePartida);

    List<CatalogoItem> getUnidadesMedida();

    List<CatalogoItem> getTodosArticulos();

    List<ServicioEspecial> obtenerServicios(ServicioEspecialFiltro filtro);

    List<ServicioEspecialReporteDTO> getFilasReporte(ServicioEspecialFiltro filtro);

    void generarReporteExcel(ServicioEspecialFiltro filtro, HttpServletResponse response);
}