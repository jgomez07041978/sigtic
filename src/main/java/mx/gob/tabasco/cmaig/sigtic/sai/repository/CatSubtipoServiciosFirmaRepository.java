package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosFirma;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.EntePublico;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatSubtipoServiciosFirmaRepository extends CrudRepository<CatSubtipoServiciosFirma, Long> {

    List<CatSubtipoServiciosFirma> findAllByCatSubtipoServiciosTic(CatSubtipoServiciosTic catSubtipoServiciosTic);

    List<CatSubtipoServiciosFirma> findAllByCveEstatus(String cveEstatus);

    Optional<CatSubtipoServiciosFirma> findByEntePublicoAndCatSubtipoServiciosTicAndCveEstatus(EntePublico entePublico, CatSubtipoServiciosTic catSubtipoServiciosTic, String cveEstatus);



}
