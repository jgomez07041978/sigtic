package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatPlantillaReporte;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatPlantillaReporteBean;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatPlantillaReporteParrafo;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.util.List;

public interface ConfigReporteService {

    List<CatPlantillaReporteBean> getDatosReportes();
    CatPlantillaReporte guardarConfiguracionReporte(CatPlantillaReporte catPlantillaReporte);
    CatPlantillaReporte editarConfiguracionReporte(Long idPlantillaReporte);
    CatPlantillaReporte deleteConfiguracionReporte(Long idPlantillaReporte);

    CatPlantillaReporteParrafo addParrafoPlantilla(CatPlantillaReporteParrafo catPlantillaReporteParrafo);

    List<CatPlantillaReporteParrafo> getListaPlantillaParrafo(CatPlantillaReporteParrafo catPlantillaReporteParrafo);

    CatPlantillaReporteParrafo editarParrafo(Long idPlantillaReporteParrafo);
    CatPlantillaReporteParrafo deleteParrafo(Long idPlantillaReporteParrafo);

    ResponseEntity<byte[]> getReportePrueba(Long idPlantillaReporte) throws FileNotFoundException, JRException;


}
