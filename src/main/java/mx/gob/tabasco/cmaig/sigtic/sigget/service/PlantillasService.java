package mx.gob.tabasco.cmaig.sigtic.sigget.service;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.Mensage;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Plantilla;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.PlantillaUploadBean;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Separador;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PlantillasService {
    List<Plantilla> getPlantillas(String usuario);

    Plantilla getPlantillaId(Long idPlantilla);

    List<Separador> getSeparador(String estatus);

    Plantilla savePlantilla(Plantilla plantilla);

    Plantilla updatePlantilla(Plantilla plantilla);
    List<Plantilla> getPlantillasAll();

    Plantilla deletePlantilla(Plantilla plantilla);

    Mensage uploadExpediente(PlantillaUploadBean plantillaUploadBean);

    void exportPlantilla(HttpServletResponse response, Long id);

}
