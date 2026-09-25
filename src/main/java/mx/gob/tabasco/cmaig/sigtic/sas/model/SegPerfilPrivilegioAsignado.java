package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import java.io.Serializable;


@NamedNativeQuery(name = "SegPerfilPrivilegioAsignado.findPerfilPrivilegioByIdPerfil",
        query = "SELECT PP.ID_SEG_PERFIL_PRIVILEGIO as idSegPerfilPrivilegio, SGP.ID_SEG_MENU as idSegMenu, LEVEL as nivel "+
       ", (CASE WHEN SGP.ID_SEG_MENU_PADRE IS NULL  THEN '#' ELSE TO_CHAR(SGP.ID_SEG_MENU_PADRE ) END) padre "+
        ", LPAD(SGP.NOMBRE_SEG_MENU,(LENGTH(SGP.NOMBRE_SEG_MENU)-1)+LEVEL,' ') nombreMenu, "+
        "PP.EDICION as editar,PP.ELIMINAR as eliminar,PP.AGREGAR as agregar,PP.CONSULTAR as consultar, PP.VALIDAR as validar "+
        "FROM SEG_MENUS SGP "+
        "INNER JOIN SEG_PERFIL_PRIVILEGIOS PP ON PP.ID_SEG_MENU=SGP.ID_SEG_MENU AND PP.ID_SEG_PERFIL=?1 "+
        "WHERE SGP.CVE_ESTATUS = 'VIG' "+
        "and SGP.ID_SEG_SISTEMA='SIGTIC' "+
        "AND (SGP.TIPO_SEG_MENU = 'MENU' "+
        "OR SGP.ID_SEG_MENU_PADRE IS NULL) "+
        "START WITH SGP.ID_SEG_MENU_PADRE IS NULL CONNECT BY PRIOR SGP.ID_SEG_MENU = SGP.ID_SEG_MENU_PADRE "+
        "ORDER SIBLINGS BY SGP.ORDEN", resultClass = SegPerfilPrivilegioAsignado.class)

@Data
@Entity
@Table(name="SEG_PRIVILEGIO_ASIGNADO")
public class SegPerfilPrivilegioAsignado implements Serializable {

    @Id
    private Integer idSegPerfilPrivilegio;
    private Integer idSegMenu;
    private Integer nivel;
    private String padre;
    private String nombreMenu;
    private Boolean editar;
    private Boolean eliminar;
    private Boolean agregar;
    private Boolean consultar;
    private Boolean validar;
}
