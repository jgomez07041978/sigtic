package mx.gob.tabasco.cmaig.sigtic.sas.repository;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegPerfilRepository extends CrudRepository<SegPerfil, Integer> {

    @Query("select sp from SegPerfil sp inner join SegPerfilUsuario pu on pu.segPerfil=sp and pu.segUsuario=?1 and sp.sistema='SIGTIC' AND sp.cveEstatus='VIG'")
    List<SegPerfil> getPerfilesUsuario(String usuario);


    @Query("SELECT sp FROM SegPerfil sp where sp.sistema=?1 and sp.cveEstatus=?2 order by sp.descPerfil")
    List<SegPerfil> findPerfilesDisponibles(String sistema, String estatus);

    @Query("SELECT sp FROM SegPerfil sp where sp.sistema=?1 and sp.cveEstatus=?2 and sp.idPerfil not in (?3) order by sp.descPerfil")
    List<SegPerfil> findPerfilesDisponiblesNoAsignados(String sistema, String estatus, List<Integer> listaPerfilAsignado);


    @Query(value="SELECT SP.* "+
            "FROM SEG_PERFILES SP "+
            "INNER JOIN SEG_PERFILES_USUARIO SU ON SU.ID_SEG_PERFIL = SP.ID_SEG_PERFIL "+
            "WHERE SU.SEG_USUARIO = ?1 "+
            "and SP.SISTEMA = 'SIGTIC' "+
            "AND SP.CVE_ESTATUS = 'VIG' AND SP.ID_SEG_PERFIL NOT IN (?2)", nativeQuery =true)
    List<SegPerfil> getPerfilesUsuarioAsignado(String usuarioLogueado, List<Integer> listaPerfilAsignado);

    @Query("SELECT pu.segPerfil.idPerfil FROM SegPerfilUsuario pu WHERE pu.segUsuario=?1 AND pu.sistema=?2")
    List<Integer> findByPerfilesAsignados(String usuario, String sistema);

    @Query("select sp from SegPerfil sp inner join SegPerfilUsuario pu on pu.segPerfil=sp and pu.segUsuario=?1 and sp.sistema='SIGTIC' AND sp.cveEstatus=?2")
    List<SegPerfil> getRolesByUsuarioAndEstatus(String usuario, String estatus);

    List<SegPerfil> findBySistemaAndCveEstatus(String sistema, String estatus);

    SegPerfil findByNombrePerfilAndSistemaAndCveEstatus(String nombrePerfil, String sistema, String estatus);

}