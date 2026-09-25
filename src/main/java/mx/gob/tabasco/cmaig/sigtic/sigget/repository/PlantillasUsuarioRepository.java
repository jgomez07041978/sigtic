package mx.gob.tabasco.cmaig.sigtic.sigget.repository;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.Plantilla;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.PlantillasUsuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantillasUsuarioRepository  extends CrudRepository<PlantillasUsuario, Long> {

    List<PlantillasUsuario> findPlantillasUsuarioByIdEntePublicoAndUsuarioAndCveEstatusOrderByPlantilla(Long idEntePublico, String rfcUsuario, String estataus);

    @Query("select p.plantilla.idPlantilla from PlantillasUsuario p where p.idEntePublico=?1 and p.usuario=?2 and p.plantilla <>null ")
    List<Long> findAllPlantillas(Long idEntePublico, String rfcUsuario);

    Optional<PlantillasUsuario> findByPlantillaAndUsuarioAndIdEntePublico(Plantilla plantilla, String usuario, Long idEntePublico);

}