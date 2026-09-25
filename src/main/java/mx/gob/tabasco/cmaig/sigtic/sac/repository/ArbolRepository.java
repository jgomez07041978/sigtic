package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.Arbol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArbolRepository extends CrudRepository<Arbol, String> {

    List<Arbol> getArbolConParent(Long idParent1);

    List<Arbol> getArbolConParentporInstitucion(Long idParent1);

    List<Arbol> getArbolUnidadAdminPresupuestal(Long idParent1);

    List<Arbol> getArbolAdminSigtic();

    List<Arbol> getArbolSinParentConUsuario(String rfcUsuario);


    List<Arbol> getArbolSinParentConUsuarioEnte(String rfcUsuario, Long idEntePublico);

}
