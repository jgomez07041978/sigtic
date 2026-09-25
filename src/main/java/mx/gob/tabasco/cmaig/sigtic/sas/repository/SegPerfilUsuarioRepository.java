package mx.gob.tabasco.cmaig.sigtic.sas.repository;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.SegPerfilUsuarioCount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SegPerfilUsuarioRepository extends CrudRepository<SegPerfilUsuario, Long> {

/*    List<SegPerfilUsuario> findBySegUsuarioAndSistema(String usuario,String sistema);*/

    @Query("SELECT new mx.gob.tabasco.cmaig.sigtic.sas.pojo.SegPerfilUsuarioCount(pp.segPerfil.idPerfil,COUNT(pp.segUsuario)) "+
    "FROM SegPerfilUsuario pp where pp.segPerfil.sistema='SIGTIC' AND pp.segPerfil.cveEstatus='VIG' group by pp.segPerfil.idPerfil")
    List<SegPerfilUsuarioCount> getNumUsuarioByPerfil();

    @Query("SELECT COUNT(pp.segUsuario) FROM SegPerfilUsuario pp WHERE pp.segPerfil=?1 AND  pp.sistema='SIGTIC'")
    Integer existeUsuarioPorRol(SegPerfil segPerfil);

    @Query("SELECT pp.segUsuario FROM SegPerfilUsuario pp WHERE pp.segPerfil=?1 and pp.sistema=?2")
    List<String> getListaRfcUsuarioPerfilBySistema(SegPerfil segPerfil, String sistema);

    List<SegPerfilUsuario> findBySegUsuarioAndSistemaOrderBySegPerfil(String usuario, String sistema);

    List<SegPerfilUsuario> findBySistemaOrderBySegPerfil(String sistema);

    @Query("SELECT sp FROM SegPerfilUsuario sp where sp.segUsuario=?1 and sp.segPerfil.idPerfil=?2 and sp.sistema=?3")
    SegPerfilUsuario findByPerfilPorUsuario(String rfcUsuario, Integer idPerfil, String sistema);

    Optional<SegPerfilUsuario> findBySegUsuarioAndSistemaAndSegPerfil(String segUsuario, String sistema, SegPerfil segPerfil);

}
