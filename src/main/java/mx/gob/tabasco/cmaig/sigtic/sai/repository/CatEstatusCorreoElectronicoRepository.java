package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatEstatusCorreoElectronico;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatEstatusCorreoElectronicoRepository extends CrudRepository<CatEstatusCorreoElectronico,String>
{
    List<CatEstatusCorreoElectronico> findAllByOrderByNumeroOrden();

}
