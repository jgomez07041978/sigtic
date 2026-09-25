package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.Localidad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LocalidadRepository extends CrudRepository<Localidad, Integer> {
    List<Localidad> findLocalidadByIdEntidadFederativaAndIdMunicipioOrderByNumeroOrdenLocalidad(Integer idEntidadFEderativa, Integer idMunicipio);

}
