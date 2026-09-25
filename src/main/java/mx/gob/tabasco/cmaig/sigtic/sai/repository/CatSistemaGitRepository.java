package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSistemaGit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CatSistemaGitRepository extends CrudRepository<CatSistemaGit,Long>{

    List<CatSistemaGit> findAllByCatSistema_idSistemaAndCveEstatus(Long idSistema,String cveEstatus);

    Optional<CatSistemaGit> findByCatSistema_idSistemaAndCodigoGit(Long idSistema, String codigo);


}
