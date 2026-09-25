package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.Bien;
import mx.gob.tabasco.cmaig.sigtic.sai.model.UnidadAdministrativa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BienRepository extends CrudRepository<Bien, Long> {

    List<Bien> findAllByUnidadAdministrativaAndCveEstatusOrderByNumeroInventario(UnidadAdministrativa unidadAdministrativa, String estatus);

}
