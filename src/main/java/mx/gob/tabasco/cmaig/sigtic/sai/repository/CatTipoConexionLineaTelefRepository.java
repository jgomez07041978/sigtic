package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoConexionLineaTelef;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatTipoConexionLineaTelefRepository extends CrudRepository<CatTipoConexionLineaTelef, String> {

    @Query("SELECT t FROM CatTipoConexionLineaTelef t WHERE t.cveEstatus = 'VIG' ORDER BY t.numeroOrden, t.nombreTipoConexionLineaTelef")
    List<CatTipoConexionLineaTelef> findAllVigentes();
}
