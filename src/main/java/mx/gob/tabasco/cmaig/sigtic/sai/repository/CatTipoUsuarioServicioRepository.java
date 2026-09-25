package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoUsuarioServicio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CatTipoUsuarioServicioRepository extends CrudRepository<CatTipoUsuarioServicio, Integer> {
    List<CatTipoUsuarioServicio> findAllByCveEstatusOrderByNumeroOrdenTipoServicio(String cveEstatus);
}
