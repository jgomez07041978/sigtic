package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.LogoBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogoBeanRepository extends CrudRepository<LogoBean, Integer> {

    List<LogoBean> getLogosAll();

    List<LogoBean> getLogos(Long idEntePublico);


}
