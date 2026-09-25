package mx.gob.tabasco.cmaig.sigtic.sac.repository;


import mx.gob.tabasco.cmaig.sigtic.sac.model.TipoVialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoVialidadRepository extends JpaRepository<TipoVialidad, Integer> {

}

