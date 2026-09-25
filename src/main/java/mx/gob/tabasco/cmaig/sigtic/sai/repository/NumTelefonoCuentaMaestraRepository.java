package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.NumTelefonoCuentaMaestra;
import mx.gob.tabasco.cmaig.sigtic.sai.model.NumTelefonoCuentaMaestraId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NumTelefonoCuentaMaestraRepository extends CrudRepository<NumTelefonoCuentaMaestra, NumTelefonoCuentaMaestraId> {

    @Query("SELECT t FROM NumTelefonoCuentaMaestra t " +
            "LEFT JOIN FETCH t.entePublico ep " +
            "LEFT JOIN FETCH t.unidadAdministrativa ua " +
            "LEFT JOIN FETCH t.catTipoConexionLineaTelef tc " +
            "LEFT JOIN FETCH t.catCuentaMaestra cm " +
            "LEFT JOIN FETCH t.catConceptoCuentaMaestra cc " +
            "LEFT JOIN FETCH t.ubicacionUnidadAdmin ub " +
            "WHERE (:idEntePublico IS NULL OR ep.idEntePublico = :idEntePublico) " +
            "AND (:idUnidadAdministrativa IS NULL OR ua.idUnidadAdministrativa = :idUnidadAdministrativa) " +
            "AND (:cveTipoConexion IS NULL OR tc.cveTipoConexionLineaTelef = :cveTipoConexion) " +
            "AND t.cveEstatus IN :listaEstatus " +
            "ORDER BY ep.nombreEntePublico, ua.nombreUnidadAdministrativa, t.id.numTelefono")
    List<NumTelefonoCuentaMaestra> getTelefonosFiltrados(
            @Param("idEntePublico") Long idEntePublico,
            @Param("idUnidadAdministrativa") Long idUnidadAdministrativa,
            @Param("cveTipoConexion") String cveTipoConexion,
            @Param("listaEstatus") List<String> listaEstatus);

    @Query("SELECT t FROM NumTelefonoCuentaMaestra t " +
            "LEFT JOIN FETCH t.entePublico ep " +
            "LEFT JOIN FETCH t.unidadAdministrativa ua " +
            "LEFT JOIN FETCH t.catTipoConexionLineaTelef tc " +
            "LEFT JOIN FETCH t.catCuentaMaestra cm " +
            "LEFT JOIN FETCH t.catConceptoCuentaMaestra cc " +
            "LEFT JOIN FETCH t.ubicacionUnidadAdmin ub " +
            "WHERE ep.idEntePublico IN :listaEntePublico " +
            "AND (:idUnidadAdministrativa IS NULL OR ua.idUnidadAdministrativa = :idUnidadAdministrativa) " +
            "AND (:cveTipoConexion IS NULL OR tc.cveTipoConexionLineaTelef = :cveTipoConexion) " +
            "AND t.cveEstatus IN :listaEstatus " +
            "ORDER BY ep.nombreEntePublico, ua.nombreUnidadAdministrativa, t.id.numTelefono")
    List<NumTelefonoCuentaMaestra> getTelefonosFiltradosPorListaEnte(
            @Param("listaEntePublico") List<Long> listaEntePublico,
            @Param("idUnidadAdministrativa") Long idUnidadAdministrativa,
            @Param("cveTipoConexion") String cveTipoConexion,
            @Param("listaEstatus") List<String> listaEstatus);
}
