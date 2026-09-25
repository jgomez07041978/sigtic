package mx.gob.tabasco.cmaig.sigtic.sigget.repository;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.GrupoManual;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Manual;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ManualRepository extends CrudRepository<Manual, Long> {
    @Query("SELECT ma FROM Manual ma WHERE ma.cveEstatus='VIG' AND ma.tipoManual=?1 ORDER BY ma.grupoManual.numeroOrden,ma.numeroOrden")
    List<Manual> getManualesPorTipo(String tipo);

    @Query("SELECT ma FROM Manual ma WHERE ma.cveEstatus='VIG' ORDER BY ma.numeroOrden")
    List<Manual> getManuales();

    Long countByGrupoManualAndCveEstatus(GrupoManual grupoManual,String estatus);

    List<Manual> findAllByGrupoManualAndCveEstatusOrderByNumeroOrden(GrupoManual grupoManual,String estatus);

}
