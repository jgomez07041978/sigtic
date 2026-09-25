package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSistema;
import mx.gob.tabasco.cmaig.sigtic.sai.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sai.model.UnidadAdministrativa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CatSistemaRepository extends CrudRepository<CatSistema, Long> {

    @Query(value="SELECT CS.* FROM CAT_SISTEMAS CS WHERE CS.ID_ENTE_PUBLICO_ADMIN_SISTEMA=?1 OR CS.ID_ENTE_PUBLICO_OPERA_SISTEMA=?1 ORDER BY CS.NUM_ORDEN_SISTEMA ", nativeQuery=true)
    List<CatSistema> getAllSistemasEntePublico(Long idEntePublico);

    @Query(value="SELECT CS.* FROM CAT_SISTEMAS CS WHERE (CS.ID_ENTE_PUBLICO_ADMIN_SISTEMA=?1 AND CS.ID_UNIDAD_ADMTVA_ADMIN_SISTEMA IN ?2) OR (CS.ID_ENTE_PUBLICO_OPERA_SISTEMA=?1 AND CS.ID_UNIDAD_ADMTVA_OPERA_SISTEMA IN ?2) ORDER BY CS.NUM_ORDEN_SISTEMA ", nativeQuery=true)
    List<CatSistema> getAllSistemasEntePublicoPorNivel(Long idEntePublico, List<Long> listaUnidades);


    @Query(value="SELECT CS.* FROM CAT_SISTEMAS CS WHERE CS.ID_UNIDAD_ADMTVA_ADMIN_SISTEMA in " +
            " (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
            "                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
            "                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?1 " +
            "                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?1 " +
            "                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) "+
            " OR CS.ID_UNIDAD_ADMTVA_OPERA_SISTEMA in" +
            " (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
            "                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
            "                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?1 " +
            "                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?1 " +
            "                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) "+
            " ORDER BY CS.NUM_ORDEN_SISTEMA ", nativeQuery=true)
    List<CatSistema> getAllSistemasUnidad(Long idUnidadAdministrativa);

    List<CatSistema> findAllByEntePublicoAdminOrderByNumeroOrdenAsc(EntePublico entePublico);

    List<CatSistema> findAllByEntePublicoOperaOrderByNumeroOrdenAsc(EntePublico entePublico);

    List<CatSistema> findAllByUnidadAdministrativaAdminInOrderByNumeroOrdenAsc(List<UnidadAdministrativa> listaUnidad);

    List<CatSistema> findAllByUnidadAdministrativaOperaInOrderByNumeroOrdenAsc(List<UnidadAdministrativa> listaUnidad);

    List<CatSistema> findAllByEntePublicoAdminOrderByNombreSistemaAsc(EntePublico entePublico);

    List<CatSistema> findAllByCatSistemaPadre(CatSistema catSistema);

    List<CatSistema> findAllByCveEstatus(String estatus);

    Optional<CatSistema> findCatSistemaByCodigoSistema(String codigo);
}
