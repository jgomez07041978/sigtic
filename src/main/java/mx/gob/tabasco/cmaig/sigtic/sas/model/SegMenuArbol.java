package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Data;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.StateBean;

import javax.persistence.*;
import java.io.Serializable;

@NamedNativeQuery(name = "SegMenuArbol.findMenuArbol",
        query = "SELECT SGP.ID_SEG_MENU as id, (CASE WHEN SGP.ID_SEG_MENU_PADRE IS NULL  THEN '#' ELSE TO_CHAR(SGP.ID_SEG_MENU_PADRE ) END) parent "+
        ", SGP.NOMBRE_SEG_MENU text "+
        "FROM SEG_MENUS SGP "+
        "WHERE SGP.CVE_ESTATUS = 'VIG' "+
        "and SGP.ID_SEG_SISTEMA='SIGTIC' "+
        "AND (SGP.TIPO_SEG_MENU = 'MENU' "+
        "OR SGP.ID_SEG_MENU_PADRE IS NULL) "+
        "START WITH SGP.ID_SEG_MENU_PADRE IS NULL CONNECT BY PRIOR SGP.ID_SEG_MENU = SGP.ID_SEG_MENU_PADRE "+
        "ORDER SIBLINGS BY SGP.ORDEN", resultClass = SegMenuArbol.class)

@Data
@Entity
@Table(name="SEG_MENU_ARBOL")
public class SegMenuArbol implements Serializable {
    @Id
    private Integer id;
    private String parent;
    private String text;

    @Transient
    private StateBean state;
}
