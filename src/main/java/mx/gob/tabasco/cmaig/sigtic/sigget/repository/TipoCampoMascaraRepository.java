package mx.gob.tabasco.cmaig.sigtic.sigget.repository;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.TipoCampoMascara;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TipoCampoMascaraRepository extends CrudRepository<TipoCampoMascara, Integer> {

    List<TipoCampoMascara> findAllByCveTipoCampoAndCveEstatus(String cveTipoCampo, String estatus);

}
