package mx.gob.tabasco.cmaig.sigtic.sas.repository;

import mx.gob.tabasco.cmaig.sigtic.sas.model.Captcha;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CaptchaRepository extends CrudRepository<Captcha, Integer> {

    Optional<Captcha> findBySistema(String sistema);

}
