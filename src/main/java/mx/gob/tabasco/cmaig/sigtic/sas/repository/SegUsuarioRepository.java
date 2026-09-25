package mx.gob.tabasco.cmaig.sigtic.sas.repository;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuarioPk;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SegUsuarioRepository extends CrudRepository<SegUsuario, SegUsuarioPk> {


    /*
    @Query(value="SELECT SU.* FROM SEG_USUARIOS SU "+
            "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIOS CU ON CU.ID_USUARIO=SU.ID_USUARIO "+
            "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.ID_ENTE_PUBLICO=CU.ID_ENTE_PUBLICO_USUARIO "+
            "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_UNIDAD_ADMINISTRATIVA=CU.ID_UNIDAD_ADMTVA_USUARIO "+
            "WHERE SU.SISTEMA=?1  AND EXISTS (SELECT NULL FROM SEG_ACCESO_USUARIO WHERE RFC_ACCESO_USUARIO = ?2 "+
            "AND ID_UNIDAD_ADMTVA_ACCESO = UA.ID_UNIDAD_ADMINISTRATIVA  "+
            "AND ID_SEG_SISTEMA = SU.SISTEMA  AND CVE_ESTATUS = 'VIG') "+
            "AND CE.ID_ENTE_PUBLICO = ?3 "+
            "ORDER BY CE.DESC_ENTE_PUBLICO, UA.DESC_UNIDAD_ADMINISTRATIVA, CU.NOMBRE_COMPLETO_USUARIO", nativeQuery=true)
    List<SegUsuario> findUsuarioPorSistemaEnte(String sistema, String rfc1, Long idEnte);*/

    @Query(value="SELECT SU.* FROM SEG_USUARIOS SU "+
            "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIO_ENTE_PUBLICO UE ON UE.ID_USUARIO=SU.ID_USUARIO "+
            "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.ID_ENTE_PUBLICO=UE.ID_ENTE_PUBLICO "+
            "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_UNIDAD_ADMINISTRATIVA=UE.ID_UNIDAD_ADMINISTRATIVA "+
            "WHERE SU.SISTEMA=?1 AND UE.ID_UNIDAD_ADMINISTRATIVA IN (?2)"+
            "ORDER BY CE.DESC_ENTE_PUBLICO, UA.DESC_UNIDAD_ADMINISTRATIVA", nativeQuery=true)
    List<SegUsuario> findUsuarioPorSistemaPorUnidades(String sistema, List<Long> listaUnidad);


    @Query(value="SELECT SU.* FROM SEG_USUARIOS SU "+
            "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIO_ENTE_PUBLICO UE ON UE.ID_USUARIO=SU.ID_USUARIO "+
            "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.ID_ENTE_PUBLICO=UE.ID_ENTE_PUBLICO "+
            "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_UNIDAD_ADMINISTRATIVA=UE.ID_UNIDAD_ADMINISTRATIVA "+
            "WHERE SU.SISTEMA = ?1 "+
            "AND CE.ID_ENTE_PUBLICO = ?2 "+
            "ORDER BY CE.DESC_ENTE_PUBLICO, UA.DESC_UNIDAD_ADMINISTRATIVA", nativeQuery=true)
    List<SegUsuario> findUsuarioPorSistemaEnteAdminSigtic(String sistema, Long idEnte);

    @Query(value="SELECT SU.ID_USUARIO FROM SEG_USUARIOS SU "+
            "INNER JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIO_ENTE_PUBLICO UE ON UE.ID_USUARIO=SU.ID_USUARIO "+
            "AND SU.SISTEMA=?1 AND SU.CVE_ESTATUS=?2 "+
            "AND UE.ID_UNIDAD_ADMINISTRATIVA in (?3)", nativeQuery=true)
    List<Long> findIdUsuarioBySistemaAndCveEstatus(String sistema,String estatus, List<Long> listaUnidad);

    @Query(value="SELECT SU.* FROM SEG_USUARIOS SU WHERE SU.ID_USUARIO=?1 AND SU.SISTEMA=?2", nativeQuery=true)
    SegUsuario findByIdUsuarioAndSistema(Long idUsuario, String sistema);

    @Query(value="SELECT SU.* FROM SEG_USUARIOS SU WHERE SU.ID_USUARIO=?1 AND SU.SISTEMA=?2", nativeQuery=true)
    Optional<SegUsuario> findByIdUsuarioAndSistemaOptional(Long idUsuario, String sistema);

    @Query(value="SELECT SU.* FROM SEG_USUARIOS SU WHERE SU.ID_USUARIO_ENTE_PUBLICO=?1 AND SU.SISTEMA=?2", nativeQuery=true)
    SegUsuario findByIdUsuarioEntePublicoAndSistema(Long idUsuarioEntePublico, String sistema);

    Optional<SegUsuario> findSegUsuarioBySegUsuarioPk(SegUsuarioPk segUsuarioPk);



}