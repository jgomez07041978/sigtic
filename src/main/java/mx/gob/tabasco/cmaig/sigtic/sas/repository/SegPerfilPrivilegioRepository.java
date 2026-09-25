package mx.gob.tabasco.cmaig.sigtic.sas.repository;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegPerfilPrivilegioRepository extends CrudRepository<SegPerfilPrivilegio, Long> {

    List<SegPerfilPrivilegio> findSegPerfilPrivilegioBySegPerfil(SegPerfil segPerfil);

    @Query(value="SELECT spp.*" +
            "                    FROM seg_perfil_privilegios spp" +
            "                         INNER JOIN seg_perfiles_usuario pu" +
            "                             ON pu.ID_SEG_PERFIL = spp.ID_SEG_PERFIL" +
            "                         INNER JOIN seg_perfiles sp" +
            "                             ON sp.ID_SEG_PERFIL = pu.ID_SEG_PERFIL" +
            "                   WHERE  pu.SISTEMA = ?1" +
            "                         AND sp.CVE_ESTATUS = 'VIG'" +
            "                         AND sp.SISTEMA = pu.SISTEMA" +
            "                         AND SEG_USUARIO = ?2" +
            "                         and SPP.ID_SEG_MENU=?3", nativeQuery =true)
    List<SegPerfilPrivilegio> getPerfilPrivilegioMenu(String sistema, String usuario, Integer idMenu);

    @Query("SELECT COUNT(pp.idPerfilPrivilegio) FROM SegPerfilPrivilegio pp WHERE pp.segPerfil=?1")
    Integer existePrivilegiosPorRol(SegPerfil segPerfil);

    void deleteBySegPerfil(SegPerfil segperfil);

    List<SegPerfilPrivilegio> findBySegPerfilAndSegMenuIn(SegPerfil segPerfil, List<SegMenu> lista);

    @Query("select sp from SegPerfilPrivilegio sp where sp.segPerfil = ?1 and sp.segMenu not in (?2) and sp.segMenu.tipoMenu = ?3")
    List<SegPerfilPrivilegio> findPorPerfilAndListaMenuNotInPorTipo(SegPerfil segPerfil, List<SegMenu> lista, String tipo);

    @Query("select sp from SegPerfilPrivilegio sp where sp.segPerfil = ?1 and sp.segMenu in (?2) and sp.segMenu.tipoMenu = ?3")
    List<SegPerfilPrivilegio> findPorPerfilAndListaMenuInPorTipo(SegPerfil segPerfil, List<SegMenu> lista, String tipo);
}
