package mx.gob.tabasco.cmaig.sigtic.sas.repository;


import mx.gob.tabasco.cmaig.sigtic.sas.model.TokenMovil;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TokenMovilRepository extends CrudRepository<TokenMovil, Long> {

    Optional<TokenMovil> findByTokenMovil(String token);

}
