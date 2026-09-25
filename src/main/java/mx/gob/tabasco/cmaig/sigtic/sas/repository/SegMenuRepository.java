package mx.gob.tabasco.cmaig.sigtic.sas.repository;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegMenuRepository extends CrudRepository<SegMenu, Integer> {

    List<SegMenu> findByIdSistemaAndCveEstatus(String sistema, String estatus);

    @Query(value="  SELECT *" +
            "    FROM SEG_MENUS SM" +
            "   WHERE     SM.CVE_ESTATUS = 'VIG'" +
            "         AND SM.ID_SEG_SISTEMA =?1" +
            "         AND SM.ID_SEG_MENU IN " +
            "                 (SELECT spp.id_seg_menu" +
            "                    FROM seg_perfil_privilegios spp" +
            "                         INNER JOIN seg_perfiles_usuario pu" +
            "                             ON pu.ID_SEG_PERFIL = spp.ID_SEG_PERFIL" +
            "                         INNER JOIN seg_perfiles sp" +
            "                             ON sp.ID_SEG_PERFIL = pu.ID_SEG_PERFIL" +
            "                   WHERE  pu.SISTEMA = ?1" +
            "                         AND sp.CVE_ESTATUS = 'VIG'" +
            "                         AND sp.SISTEMA = pu.SISTEMA" +
            "                         AND SEG_USUARIO = ?2)" +
            "ORDER BY 1", nativeQuery=true)
    List<SegMenu> getMenuByUsuario(String sistema1, String usuario);

    List<SegMenu> findByIdMenuIn(List<Integer> listaMenu);

    List<SegMenu> findByidMenuPadreInAndTipoMenu(List<Integer> listaMenu, String tipoMenu);
}
