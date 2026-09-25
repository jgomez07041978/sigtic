package mx.gob.tabasco.cmaig.sigtic.sigget.repository;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.TipoCampo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoCamposRepository extends JpaRepository<TipoCampo, String> {
    List<TipoCampo> findByCveEstatus(String estatus);
}
