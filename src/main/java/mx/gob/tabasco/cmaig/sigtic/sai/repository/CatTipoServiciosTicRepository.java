package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoServiciosTic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatTipoServiciosTicRepository extends CrudRepository<CatTipoServiciosTic, Long> {
    List<CatTipoServiciosTic> findAllByCveEstatus(String estatus);

    List<CatTipoServiciosTic> findAllByCveEstatusOrderByNombreTipoServicioTic(String estatus);
    List<CatTipoServiciosTic> findAllByCveEstatusOrCveEstatusOrderByNumOrdenTipoServicioTic(String estatus1,String estatus2);

    @Query("SELECT ts "+
            "FROM CatTipoServiciosTic ts where ts.cveEstatus='VIG' " +
            "AND ts in (select st.catTipoServiciosTic from CatSubtipoServiciosTic st where st.cveEstatus='VIG' AND st.generaCr='S')")
    List<CatTipoServiciosTic> getTiposServiciosCr();
}