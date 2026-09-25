package mx.gob.tabasco.cmaig.sigtic.sigget.repository;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.Plantilla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantillaRepository extends JpaRepository<Plantilla, Long> {


    @Query(value = "select * from CAT_PLANTILLAS_CARGA_DATOS where CVE_ESTATUS='VIG'  " +
            "                        and ID_PLANTILLA_CARGA_DATOS in ( " +
            "                        SELECT DISTINCT ID_PLANTILLA_CARGA_DATOS " +
            "                        FROM SIGGET.PLANTILLAS_USUARIO_CARGA_DATOS   "+
            "                WHERE RFC_USUARIO_CARGA_DATOS = ?1 AND ID_ENTE_PUBLICO_CARGA_DATOS=?2 AND CVE_ESTATUS='VIG')", nativeQuery = true)
    List<Plantilla> getPlantillas(String usuario, Long idEntePublico);

    List<Plantilla> findByidPlantillaNotInAndCveEstatusOrderByNombrePlantilla(List<Long> lista, String estatus);
    List<Plantilla> findByCveEstatusOrderByNombrePlantilla(String estatus);
    List<Plantilla> findAllByCveEstatusOrderByNombrePlantilla(String estatus);


}
