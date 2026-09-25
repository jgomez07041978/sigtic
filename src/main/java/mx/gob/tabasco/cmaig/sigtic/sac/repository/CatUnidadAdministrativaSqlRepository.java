package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUnidadAdministrativaSql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatUnidadAdministrativaSqlRepository extends JpaRepository<CatUnidadAdministrativaSql, Long> {

    List<CatUnidadAdministrativaSql> getUnidadesAdministrativasArbol(String rfc, Long idUnidadAdministrativa);

    List<CatUnidadAdministrativaSql> getUnidadesAdministrativasArbolSigtic(Long idUnidadAdministrativa);

}
