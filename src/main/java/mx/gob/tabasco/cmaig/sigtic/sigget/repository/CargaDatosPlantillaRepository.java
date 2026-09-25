package mx.gob.tabasco.cmaig.sigtic.sigget.repository;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.CargaDatosPlantilla;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.CargaDatosPlantillaPk;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CargaDatosPlantillaRepository extends CrudRepository<CargaDatosPlantilla, CargaDatosPlantillaPk> {

    @Query(value = "SELECT SEQ_CAT_PLANTILLAS_CARGA_DATOS.nextval FROM dual", nativeQuery = true)
    BigDecimal getNextValMySequence();

    @Modifying
    @Query("delete from CargaDatosPlantilla b where b.cargaDatosPlantillaPk.idCargaDatosPlantilla=?1 AND b.plantilla.idPlantilla=?2")
    void deleteCargaMasiva(BigDecimal idCargaDatosPlantilla, Long idPlantilla);

    @Query(value="SELECT DP.* FROM CARGA_DATOS_X_PLANTILLA DP WHERE DP.ID_CARGA_DATOS_X_PLANTILLA=?1 ORDER BY DP.NUM_LINEA_CARGADATOSXPLANTILLA", nativeQuery=true)
    List<CargaDatosPlantilla> listaCargaDatos(Long idCargaDatos);

    @Query(value="SELECT SIGGET.PKG_CARGAS_MASIVA.EJECUTA_SQL(?1, ?2) FROM dual", nativeQuery=true)
    String ejecutaSql(Long  idCargaDatos, String userName);
}
