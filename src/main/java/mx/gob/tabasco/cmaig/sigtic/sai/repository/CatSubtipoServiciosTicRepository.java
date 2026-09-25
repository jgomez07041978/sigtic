package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoServiciosTic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatSubtipoServiciosTicRepository extends CrudRepository<CatSubtipoServiciosTic, Long> {
    List<CatSubtipoServiciosTic> findAllByCveEstatus(String estatus);

    List<CatSubtipoServiciosTic> findAllByCatTipoServiciosTicAndCveEstatusOrderByNombreSubtipoServicioTic(CatTipoServiciosTic catTipoServiciosTic, String estatus);
    List<CatSubtipoServiciosTic> findAllByCatTipoServiciosTicAndCveEstatusAndGeneraCrOrderByNombreSubtipoServicioTic(CatTipoServiciosTic catTipoServiciosTic, String estatus, String generaCr);

    /*@Query("select p from CatSubtipoServiciosTic p where p.cveEstatus=?1 order by p.catTipoServiciosTic.numOrdenTipoServicioTic asc, p.numeroOrdenSubtipoServicioTic asc")*/
    @Query(value="SELECT SS.* " +
            " FROM CAT_SUBTIPO_SERVICIOS_TIC SS    " +
            " INNER JOIN CAT_TIPO_SERVICIOS_TIC TS ON TS.ID_TIPO_SERVICIO_TIC=SS.ID_TIPO_SERVICIO_TIC"+
            " WHERE SS.CVE_ESTATUS IN ('VIG','HIS') " +
            " ORDER BY TS.NUM_ORDEN_TIPO_SERVICIO_TIC,TS.ID_TIPO_SERVICIO_TIC, SS.NUM_ORDEN_SUBTIPO_SERVICIO_TIC", nativeQuery=true)
    List<CatSubtipoServiciosTic> getAllSubtipoServicio();

    List<CatSubtipoServiciosTic> findAllByCveEstatusOrderByCatTipoServiciosTicAscNumeroOrdenSubtipoServicioTicAsc(String estatus);

    @Query(value="SELECT SS.* " +
            " FROM CAT_SUBTIPO_SERVICIOS_TIC SS    " +
            " INNER JOIN CAT_TIPO_SERVICIOS_TIC TS ON TS.ID_TIPO_SERVICIO_TIC=SS.ID_TIPO_SERVICIO_TIC"+
            " WHERE SS.CVE_ESTATUS IN ('VIG','HIS') " +
            " AND SS.ID_SUBTIPO_SERVICIO_TIC NOT IN (SELECT ID_SUBTIPO_SERVICIO_TIC FROM SUB_SERVICIOS_TIC_USUARIOS WHERE ID_USUARIO=?1)"+
            " ORDER BY TS.NUM_ORDEN_TIPO_SERVICIO_TIC,TS.ID_TIPO_SERVICIO_TIC, SS.NUM_ORDEN_SUBTIPO_SERVICIO_TIC", nativeQuery=true)
    List<CatSubtipoServiciosTic> getAllSubtipoServicioNoAddUsuario(Long idUsuario);
}