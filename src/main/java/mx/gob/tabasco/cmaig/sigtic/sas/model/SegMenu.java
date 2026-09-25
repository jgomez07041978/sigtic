package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "SEG_MENUS")
public class SegMenu implements Serializable {

    @Id
    @Column(name = "ID_SEG_MENU")
    private Integer idMenu;

    @Column(name = "ID_SEG_MENU_PADRE", nullable = true)
    private Integer idMenuPadre;

    @Column(name = "CODIGO_SEG_MENU", nullable = true)
    private String codigo;

    @Column(name = "ID_SEG_SISTEMA", nullable = true)
    private String idSistema;

    @Column(name = "NOMBRE_SEG_MENU", nullable = true)
    private String nombreMenu;

    @Column(name = "URL_SEG_MENU", nullable = true)
    private String urlMenu;

    @Column(name = "ICONO_SEG_MENU", nullable = true)
    private String iconoMenu;

    @Column(name = "DESCRIPCION_SEG_MENU", nullable = true)
    private String descMenu;

    @Column(name = "TEXTO_AYUDA", nullable = true)
    private String textoAyuda;

    @Column(name = "STYLE", nullable = true)
    private String style;

    @Column(name = "TIPO_SEG_MENU", nullable = true)
    private String tipoMenu;

    @Column(name = "ORDEN", nullable = true)
    private Integer orden;

    @Column(name = "CVE_ESTATUS", nullable = true)
    private String cveEstatus;


}
