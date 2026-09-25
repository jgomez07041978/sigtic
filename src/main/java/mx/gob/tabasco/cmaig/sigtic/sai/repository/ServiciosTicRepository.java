package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiciosTicRepository  extends JpaRepository<ServiciosTic, Long>, JpaSpecificationExecutor<ServiciosTic> {

    @Query(value="SELECT count(ST.ID_SERVICIO_TIC) " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
            "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
            "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
            "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS') " +
            "                                                AND CE.ID_ENTE_PUBLICO=?1  " +
            "                                                and ST.CVE_ESTATUS_SERVICIO_TIC=?2", nativeQuery=true)
    Integer getCountAdminEstatusServicioTicAndCveEstatusPorEnte(Long idEntePublico, String estatus);

    @Query(value="SELECT count(ST.ID_SERVICIO_TIC) " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
            "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
            "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
            "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS') " +
            "                                                AND CE.ID_ENTE_PUBLICO=?1  " +
            "                                                and ST.CVE_ESTATUS_SERVICIO_TIC=?2", nativeQuery=true)
    Integer getCountOperaEstatusServicioTicAndCveEstatusPorEnte(Long idEntePublico, String estatus);
    @Query(value="SELECT count(ST.ID_SERVICIO_TIC) " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
            "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
            "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
            "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS') " +
            "                                                and ST.CVE_ESTATUS_SERVICIO_TIC=?1", nativeQuery=true)
    Integer getCountAdminEstatusServicioTicAndCveEstatus(String estatus);

    @Query(value="SELECT count(ST.ID_SERVICIO_TIC) " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
            "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
            "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
            "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS') " +
            "                                                AND ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=?1 AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN " +
            "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
            "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
            "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
            "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) AND ST.CVE_ESTATUS_SERVICIO_TIC=?3", nativeQuery=true)
    Integer getCountAdminEstatusServicioTicAndCveEstatusPorEnteUnidad(Long idEntePublico,Long idUnidadAdministrativa,String estatus);

    @Query(value="SELECT count(ST.ID_SERVICIO_TIC) " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
            "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
            "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
            "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS') " +
            "                                                AND ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=?1 AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN " +
            "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
            "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
            "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
            "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) AND ST.CVE_ESTATUS_SERVICIO_TIC=?3", nativeQuery=true)
    Integer getCountOperaEstatusServicioTicAndCveEstatusPorEnteUnidad(Long idEntePublico,Long idUnidadAdministrativa,String estatus);

    @Query(value="SELECT count(ST.ID_SERVICIO_TIC) " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
            "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
            "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
            "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS') " +
            "                                                and ST.CVE_ESTATUS_SERVICIO_TIC=?1", nativeQuery=true)
    Integer getCountOperaEstatusServicioTicAndCveEstatus(String estatus);

    List<ServiciosTic> findAllByEntePublicoAdminAndTipoServicioAndSubTipoServicioAndCveEstatusInOrderByNumOrdenServicioTic(EntePublico entePublico, CatTipoServiciosTic catTipoServiciosTic, CatSubtipoServiciosTic catSubtipoServiciosTic, List<String> estatus);

    List<ServiciosTic> findAllByEntePublicoAdminAndTipoServicioAndSubTipoServicioAndCveEstatusOrderByNumOrdenServicioTic(EntePublico entePublico, CatTipoServiciosTic catTipoServiciosTic, CatSubtipoServiciosTic catSubtipoServiciosTic, String estatus);

    List<ServiciosTic> findAllByEntePublicoOperaAndTipoServicioAndSubTipoServicioAndCveEstatusInOrderByNumOrdenServicioTic(EntePublico entePublico, CatTipoServiciosTic catTipoServiciosTic, CatSubtipoServiciosTic catSubtipoServiciosTic, List<String> estatus);
    List<ServiciosTic> findAllByEntePublicoOperaAndTipoServicioAndSubTipoServicioAndCveEstatusOrderByNumOrdenServicioTic(EntePublico entePublico, CatTipoServiciosTic catTipoServiciosTic, CatSubtipoServiciosTic catSubtipoServiciosTic, String estatus);


    List<ServiciosTic> findAllByCurpAndCveEstatusInOrderByIdServicioTicDesc(String Curp, List<String> estatus);

    List<ServiciosTic> findAllByEntePublicoOperaAndUnidadAdministrativaOperaInAndCveEstatusInOrderByIdServicioTicDesc(EntePublico entePublico, List<UnidadAdministrativa> listaUnidadAdmin, List<String> estatus);
    //List<ServiciosTic> findAllByEntePublicoOperaAndUnidadAdministrativaOperaInAndCveEstatusInAndIdUsuarioEntePublicoIsNotNullOrderByIdServicioTicDesc(EntePublico entePublico, List<UnidadAdministrativa> listaUnidadAdmin, List<String> estatus);

    //List<ServiciosTic> findAllByEntePublicoOperaAndUnidadAdministrativaOperaInAndCveEstatusInAndCatEstatusSeguimientoInOrderByIdServicioTicDesc(EntePublico entePublico, List<UnidadAdministrativa> listaUnidadAdmin, List<String> estatus, List<CatEstatusServicio> lista);


/*
    INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
            "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
            "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS') " +
            "                                                and ST.CVE_ESTATUS_SERVICIO_TIC=?1*/

    @Query(value="SELECT ST.* FROM SERVICIOS_TIC ST  WHERE ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=?1 AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN " +
            "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
            "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
            "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
            "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
            " AND ST.ID_TIPO_SERVICIO_TIC=?3 AND ST.ID_SUBTIPO_SERVICIO_TIC=?4 AND CVE_ESTATUS IN ?5" , nativeQuery=true)
    List<ServiciosTic> getServiciosAdministradosPorUnidadAndEstatusIn(Long idEntePublico, Long idUnidadAdministrativa, Long idTipoServicio, Long idSubtipoServicio, List<String> estatus);

    @Query(value="SELECT ST.* FROM SERVICIOS_TIC ST  WHERE ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=?1 AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN " +
            "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
            "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
            "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
            "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
            " AND ST.ID_TIPO_SERVICIO_TIC=?3 AND ST.ID_SUBTIPO_SERVICIO_TIC=?4 AND CVE_ESTATUS=?5" , nativeQuery=true)
    List<ServiciosTic> getServiciosAdministradosPorUnidad(Long idEntePublico, Long idUnidadAdministrativa, Long idTipoServicio, Long idSubtipoServicio, String estatus);

    @Query(value="SELECT ST.* FROM SERVICIOS_TIC ST  WHERE ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=?1 AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN " +
            "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
            "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
            "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
            "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
            " AND ST.ID_TIPO_SERVICIO_TIC=?3 AND ST.ID_SUBTIPO_SERVICIO_TIC=?4 AND CVE_ESTATUS IN ?5" , nativeQuery=true)
    List<ServiciosTic> getServiciosOperadosPorUnidadAndEstatusIn(Long idEntePublico, Long idUnidadAdministrativa, Long idTipoServicio, Long idSubtipoServicio, List<String> estatus);

    @Query(value="SELECT ST.* FROM SERVICIOS_TIC ST  WHERE ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=?1 AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN " +
            "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
            "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
            "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
            "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
            " AND ST.ID_TIPO_SERVICIO_TIC=?3 AND ST.ID_SUBTIPO_SERVICIO_TIC=?4 AND CVE_ESTATUS = ?5" , nativeQuery=true)
    List<ServiciosTic> getServiciosOperadosPorUnidad(Long idEntePublico, Long idUnidadAdministrativa, Long idTipoServicio, Long idSubtipoServicio, String estatus);

    //List<ServiciosTic> findAllByEntePublicoAdminAndUnidadAdministrativaAdminAndTipoServicioAndSubTipoServicioAndCveEstatusInOrderByNumOrdenServicioTic(EntePublico entePublico,UnidadAdministrativa unidadAdministrativa, CatTipoServiciosTic catTipoServiciosTic, CatSubtipoServiciosTic catSubtipoServiciosTic, List<String> estatus);

    //List<ServiciosTic> findAllByEntePublicoAdminAndUnidadAdministrativaAdminAndTipoServicioAndSubTipoServicioAndCveEstatusOrderByNumOrdenServicioTic(EntePublico entePublico,UnidadAdministrativa unidadAdministrativa, CatTipoServiciosTic catTipoServiciosTic, CatSubtipoServiciosTic catSubtipoServiciosTic, String estatus);

    //List<ServiciosTic> findAllByEntePublicoOperaAndUnidadAdministrativaOperaAndTipoServicioAndSubTipoServicioAndCveEstatusInOrderByNumOrdenServicioTic(EntePublico entePublico, UnidadAdministrativa unidadAdministrativa, CatTipoServiciosTic catTipoServiciosTic, CatSubtipoServiciosTic catSubtipoServiciosTic, List<String> estatus);
    //List<ServiciosTic> findAllByEntePublicoOperaAndUnidadAdministrativaOperaAndTipoServicioAndSubTipoServicioAndCveEstatusOrderByNumOrdenServicioTic(EntePublico entePublico, UnidadAdministrativa unidadAdministrativa, CatTipoServiciosTic catTipoServiciosTic, CatSubtipoServiciosTic catSubtipoServiciosTic, String estatus);


    List<ServiciosTic> findAllByEntePublicoAdminAndUnidadAdministrativaAdminInAndTipoServicioAndSubTipoServicioAndCveEstatusInOrderByNumOrdenServicioTic(EntePublico entePublico,List<UnidadAdministrativa> listaUnidades, CatTipoServiciosTic catTipoServiciosTic, CatSubtipoServiciosTic catSubtipoServiciosTic, List<String> estatus);

    List<ServiciosTic> findAllByEntePublicoAdminAndUnidadAdministrativaAdminInAndTipoServicioAndSubTipoServicioAndCveEstatusOrderByNumOrdenServicioTic(EntePublico entePublico,List<UnidadAdministrativa> listaUnidades, CatTipoServiciosTic catTipoServiciosTic, CatSubtipoServiciosTic catSubtipoServiciosTic, String estatus);

    List<ServiciosTic> findAllByEntePublicoOperaAndUnidadAdministrativaOperaInAndTipoServicioAndSubTipoServicioAndCveEstatusInOrderByNumOrdenServicioTic(EntePublico entePublico, List<UnidadAdministrativa> listaUnidades, CatTipoServiciosTic catTipoServiciosTic, CatSubtipoServiciosTic catSubtipoServiciosTic, List<String> estatus);
    List<ServiciosTic> findAllByEntePublicoOperaAndUnidadAdministrativaOperaInAndTipoServicioAndSubTipoServicioAndCveEstatusOrderByNumOrdenServicioTic(EntePublico entePublico, List<UnidadAdministrativa> listaUnidades, CatTipoServiciosTic catTipoServiciosTic, CatSubtipoServiciosTic catSubtipoServiciosTic, String estatus);





    List<ServiciosTic> findAllByTipoServicioAndSubTipoServicioAndCveEstatusInOrderByNumOrdenServicioTic(CatTipoServiciosTic catTipoServiciosTic, CatSubtipoServiciosTic catSubtipoServiciosTic, List<String> estatus);
    List<ServiciosTic> findAllByUnidadAdministrativaAdminInAndUnidadAdministrativaOperaInAndTipoServicioAndSubTipoServicioAndCveEstatusInOrderByNumOrdenServicioTic(List<UnidadAdministrativa> listaAdmin, List<UnidadAdministrativa> listaOpera, CatTipoServiciosTic catTipoServiciosTic, CatSubtipoServiciosTic catSubtipoServiciosTic, List<String> estatus);
    @Query(value="SELECT CS.* FROM SERVICIOS_TIC CS WHERE CS.ID_ENTE_PUBLICO_ADM_SERV_TIC=?1 OR CS.ID_ENTE_PUBLICO_OPERA_SERV_TIC=?1 AND CVE_ESTATUS IN ('VIG','HIS') ORDER BY CS.NUM_ORDEN_SERVICIO_TIC ", nativeQuery=true)
    List<ServiciosTic> getAllServiciosEntePublico(Long idEntePublico);

    @Query(value="SELECT CS.* FROM SERVICIOS_TIC CS WHERE CS.ID_UNIDAD_ADMTVA_ADM_SERV_TIC in (?1) OR CS.ID_UNIDAD_ADMTVA_OPERA_SERVTIC in (?1) AND CVE_ESTATUS IN ('VIG','HIS') ORDER BY CS.NUM_ORDEN_SERVICIO_TIC ", nativeQuery=true)
    List<ServiciosTic> getAllServiciosUnidad(List<Long> listaUnidad);

    List<ServiciosTic> findAllByEntePublicoAdminAndCveEstatusInOrderByNumOrdenServicioTic(EntePublico entePublico, List<String> estatus);
    List<ServiciosTic> findAllByEntePublicoOperaAndCveEstatusInOrderByNumOrdenServicioTic(EntePublico entePublico, List<String> estatus);
    List<ServiciosTic> findAllByUnidadAdministrativaAdminInAndCveEstatusInOrderByNumOrdenServicioTic(List<UnidadAdministrativa> lista, List<String> estatus);
    List<ServiciosTic> findAllByUnidadAdministrativaOperaInAndCveEstatusInOrderByNumOrdenServicioTic(List<UnidadAdministrativa> lista, List<String> estatus);

    /*@Query(value="SELECT ST.* FROM SERVICIOS_TIC ST WHERE ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=?1 AND ST.CVE_ESTATUS='VIG' AND ST.ID_SERVICIO_TIC NOT IN (SELECT CRD.ID_SERVICIO_TIC FROM" +
            " CARTA_RESPONSIVA_DETALLE CRD WHERE CRD.ID_CARTA_RESPONSIVA=?2) AND ST.ID_TIPO_SERVICIO_TIC=?3 AND ST.ID_SUBTIPO_SERVICIO_TIC=?4", nativeQuery=true)
    List<ServiciosTic> getServiciosDisponibles(Long idEntePublico, Long idCartaResponsiva, Long idTipoServicio, Long idSubtipoServicio);    */

    @Query(value="SELECT ST.* FROM SERVICIOS_TIC ST  " +
            "INNER JOIN USUARIOS_SERVICIOS_TIC US ON US.ID_SERVICIO_TIC=ST.ID_SERVICIO_TIC " +
            "WHERE US.ID_USUARIO_ENTE_PUBLICO=?1  " +
            "AND ST.CVE_ESTATUS='VIG'  " +
            "AND ST.ID_SERVICIO_TIC NOT IN (SELECT CRD.ID_SERVICIO_TIC FROM CARTA_RESPONSIVA_DETALLE CRD " +
            "INNER JOIN CARTA_RESPONSIVA CR ON CR.ID_CARTA_RESPONSIVA = CRD.ID_CARTA_RESPONSIVA " +
            "WHERE CR.ID_USUARIO_ENTE_PUBLICO=?1 AND CRD.CVE_ESTATUS='VIG' " +
            "AND CR.CVE_ESTATUS=CRD.CVE_ESTATUS)  " +
            "AND ST.ID_TIPO_SERVICIO_TIC=?2 AND ST.ID_SUBTIPO_SERVICIO_TIC=?3", nativeQuery=true)
    List<ServiciosTic> getServiciosDisponibles(Long idUsuarioEntePublico, Long idTipoServicio, Long idSubtipoServicio);

    @Query("SELECT st FROM ServiciosTic st WHERE (st.cveServicioTic like %?1% or st.nombreServicioTic like %?1%) and (st.entePublicoAdmin=?2 or st.entePublicoOpera=?3) and st.subTipoServicio in (?4)")
    List<ServiciosTic> getServiciosSearch(String nombreServicio, EntePublico entePublicoAdmin, EntePublico entePublicoOpera, List<CatSubtipoServiciosTic> lista);

    @Query("SELECT st FROM ServiciosTic st WHERE (st.cveServicioTic=?1 or st.nombreServicioTic=?1) and (st.entePublicoAdmin=?2 or st.entePublicoOpera=?3) and st.subTipoServicio in (?4)")
    List<ServiciosTic> getServiciosSearchSinFiltrar(String nombreServicio, EntePublico entePublicoAdmin, EntePublico entePublicoOpera, List<CatSubtipoServiciosTic> lista);


    /*---------------------------------------------------------------*/
    @Query(value="SELECT count(ST.ID_SERVICIO_TIC) " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
            "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
            "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
            "                                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC         " +
            "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS') " +
            "                                                AND CE.ID_ENTE_PUBLICO=?1  AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN ?4 " +
            "                                                and ST.CVE_ESTATUS_SERVICIO_TIC=?2" +
            "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?3 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)"
            , nativeQuery=true)
    Integer getCountAdminEstatusServicioTicAndCveEstatusPorEnteSubTipo(Long idEntePublico, String estatus, Long idUsuario, List<Long> listaAcceso);

    @Query(value="SELECT count(ST.ID_SERVICIO_TIC) " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
            "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
            "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
            "                                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC         " +
            "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS') " +
            "                                                AND CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN ?4 " +
            "                                                and ST.CVE_ESTATUS_SERVICIO_TIC=?2" +
            "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?3 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)"
            , nativeQuery=true)
    Integer getCountOperaEstatusServicioTicAndCveEstatusPorEnteSubTipo(Long idEntePublico, String estatus, Long idUsuario, List<Long> listaAcceso);

    @Query(value="SELECT count(ST.ID_SERVICIO_TIC) " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
            "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
            "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_ADM_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
            "                                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC         " +
            "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS') " +
            "                                                AND CE.ID_ENTE_PUBLICO=?1  AND ST.ID_UNIDAD_ADMTVA_ADM_SERV_TIC IN " +
            "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
            "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?4 " +
            "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?4 " +
            "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
            "                                                and ST.CVE_ESTATUS_SERVICIO_TIC=?2" +
            "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?3 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)"
            , nativeQuery=true)
    Integer getCountAdminEstatusServicioTicAndCveEstatusPorEnteSubTipoUnidad(Long idEntePublico, String estatus, Long idUsuario, Long idUnidadAdministrativa);

    @Query(value="SELECT count(ST.ID_SERVICIO_TIC) " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_TIPO_ENTES_PUBLICOS TE    " +
            "                                                INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.CVE_TIPO_ENTE_PUBLICO = TE.CVE_TIPO_ENTE_PUBLICO   " +
            "                                                INNER JOIN SERVICIOS_TIC ST ON ST.ID_ENTE_PUBLICO_OPERA_SERV_TIC=CE.ID_ENTE_PUBLICO   " +
            "                                               INNER JOIN CAT_SUBTIPO_SERVICIOS_TIC SUB ON SUB.ID_SUBTIPO_SERVICIO_TIC = ST.ID_SUBTIPO_SERVICIO_TIC         " +
            "                                                WHERE ST.CVE_ESTATUS IN ('VIG','HIS') " +
            "                                                AND CE.ID_ENTE_PUBLICO=?1 AND ST.ID_UNIDAD_ADMTVA_OPERA_SERVTIC IN " +
            "                                               (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
            "                                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
            "                                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?4 " +
            "                                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?4 " +
            "                                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) " +
            "                                                and ST.CVE_ESTATUS_SERVICIO_TIC=?2" +
            "                                   AND exists (SELECT 1 FROM SUB_SERVICIOS_TIC_USUARIOS SUBI WHERE SUBI.ID_USUARIO=?3 AND SUBI.ID_SUBTIPO_SERVICIO_TIC=SUB.ID_SUBTIPO_SERVICIO_TIC)"
            , nativeQuery=true)
    Integer getCountOperaEstatusServicioTicAndCveEstatusPorEnteSubTipoUnidad(Long idEntePublico, String estatus, Long idUsuario, Long idUnidadAdministrativa);

    List<ServiciosTic> findAllByCurpOrderByIdServicioTicDesc(String rfc);
}