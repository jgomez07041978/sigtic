package mx.gob.tabasco.cmaig.sigtic.sigget.service;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.*;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public interface CargaMasivaService {


    String procesarCargaMasiva(CargaMasivaBean cargaMasiva);

    void cargaMasiva(List<Object> datos, CargaMasivaBean cargaMasivaBean, String columnas);

    Optional<Plantilla> getPlantillaId(Long idPlantilla);

    List<CargaMasivaTablaBean> getTablaCargaMasiva(CargaMasivaTablaBean cargaMasivaTablaBean);

    List<CargaDatosPlantillaHead> getCargaDatosPlantillaHead(CargaDatosPlantillaHead cargaDatosPlantillaHead);

    void generaPlantilla(Long idPlantilla, HttpServletResponse response);

    void deleteCarga(CargaDatosPlantilla cargaDatosPlantilla);

    JSONObject getCargaMasivaDetalle(Long idCargaDatos, Long idPlantilla);

    JSONObject validarCargaMasiva(Long idCargaDatos, Long idPlantilla);

    List<CargaDatosPlantillaError> getErroresPlantilla(Long idCargaDatos);

    String ejecutarSql(Long idCargaDatos);

    JSONObject getConcentradoBienesEstatus();

    List<ConcentradoCargaEnteBean> getConcentradoBienesEntePublico(String estatus);

}
