package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.ServicioEspecial;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServicioEspecialId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServicioEspecialRepository extends JpaRepository<ServicioEspecial, ServicioEspecialId>,
        JpaSpecificationExecutor<ServicioEspecial> {

    interface CatalogoItem {
        String getClave();

        String getDescripcion();
    }

    @Query("SELECT COALESCE(MAX(s.id.noLote), 0) FROM ServicioEspecial s " +
            "WHERE s.id.numCuentaMaestra = :numCuentaMaestra")
    Integer maxNoLote(@Param("numCuentaMaestra") String numCuentaMaestra);


    @Query(value = "SELECT CVE_PARTIDA AS clave, " +
            "CVE_PARTIDA || ' - ' || NOMBRE_PARTIDA AS descripcion " +
            "FROM CAT_PARTIDA_SERV_ESP WHERE CVE_ESTATUS = 'VIG' ORDER BY CVE_PARTIDA",
            nativeQuery = true)
    List<CatalogoItem> findPartidas();

    @Query(value = "SELECT CODIGO_ARTICULO_EBS AS clave, " +
            "CODIGO_ARTICULO_EBS || ' - ' || NOMBRE_ARTICULO AS descripcion " +
            "FROM CAT_ARTICULO_EBS_SERV_ESP " +
            "WHERE CVE_PARTIDA = :cvePartida AND CVE_ESTATUS = 'VIG' " +
            "ORDER BY CODIGO_ARTICULO_EBS",
            nativeQuery = true)
    List<CatalogoItem> findArticulosPorPartida(@Param("cvePartida") String cvePartida);

    @Query(value = "SELECT CVE_UNIDAD_MEDIDA AS clave, NOMBRE_UNIDAD_MEDIDA AS descripcion " +
            "FROM CAT_UNIDAD_MEDIDA_SERV_ESP WHERE CVE_ESTATUS = 'VIG' ORDER BY NOMBRE_UNIDAD_MEDIDA",
            nativeQuery = true)
    List<CatalogoItem> findUnidadesMedida();

    @Query(value = "SELECT COUNT(*) FROM CAT_ARTICULO_EBS_SERV_ESP " +
            "WHERE CODIGO_ARTICULO_EBS = :codigoEbs AND CVE_PARTIDA = :cvePartida AND CVE_ESTATUS = 'VIG'",
            nativeQuery = true)
    int contarArticuloEnPartida(@Param("codigoEbs") String codigoEbs, @Param("cvePartida") String cvePartida);

    @Query(value = "SELECT CODIGO_ARTICULO_EBS AS clave, " +
            "CODIGO_ARTICULO_EBS || ' - ' || NOMBRE_ARTICULO AS descripcion " +
            "FROM CAT_ARTICULO_EBS_SERV_ESP ORDER BY CODIGO_ARTICULO_EBS",
            nativeQuery = true)
    List<CatalogoItem> findTodosArticulos();
}