package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatServidorCorreoElectronico;
import mx.gob.tabasco.cmaig.sigtic.sai.model.EntePublico;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatServidorCorreoElectronicoRepository extends CrudRepository<CatServidorCorreoElectronico, Long> {

    List<CatServidorCorreoElectronico> findAllByEntePublicoAndCveEstatusOrderByNumeroOrdenServidorCorreoElectronico(EntePublico entePublico, String estatus);

    List<CatServidorCorreoElectronico> findAllByCveEstatusOrderByNumeroOrdenServidorCorreoElectronico(String estatus);

}
