package mx.gob.tabasco.cmaig.sigtic.sigget.service;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.Plantilla;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.PlantillasUsuario;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.UsuarioPlantilla;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UsuarioPlantillaService {
    Page<UsuarioPlantilla> getUsuariosPlantillas(UsuarioPlantilla usuarioPlantilla);

    List<PlantillasUsuario> getPlantillasAsignadas(Long idEntePublico, String rfcUsuario);

    List<Plantilla> getPlantillasDisponibles(List<Long> lista);

    List<Long> getPlantillasIds(Long idEntePublico, String rfcUsuario);

    PlantillasUsuario addPlantilla(PlantillasUsuario plantillasUsuario);

    void removePlantilla(PlantillasUsuario plantillasUsuario);

}
