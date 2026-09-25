package mx.gob.tabasco.cmaig.sigtic.sas.repository;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenuArbol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegMenuArbolRepository extends CrudRepository<SegMenuArbol, Integer>  {

    List<SegMenuArbol> findMenuArbol();
}
