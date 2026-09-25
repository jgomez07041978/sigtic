package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoCorreoElectronico;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatTipoCorreoRepository extends CrudRepository<CatTipoCorreoElectronico, String> {

    List<CatTipoCorreoElectronico> findAllByCveEstatusOrderByNumeroOrdenAsc(String estatus);

    @Query(value = "SELECT SIGGET_SAI_SIGTICS.SEQ_CAT_TIPO_CORREO.NEXTVAL FROM DUAL", nativeQuery = true)
    Long getNextIdTipoCorreo();

}