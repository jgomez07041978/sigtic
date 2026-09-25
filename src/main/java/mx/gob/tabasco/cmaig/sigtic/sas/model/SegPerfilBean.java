package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Data;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.StateBean;

import javax.persistence.*;
import java.io.Serializable;

@NamedNativeQuery(name = "SegPerfilBean.findBySistemaAndCveEstatusSql",
        query = " SELECT SP.ID_SEG_PERFIL as idPerfil, SP.NOMBRE_SEG_PERFIL as nombrePerfil, " +
                " SP.DESCRIPCION_SEG_PERFIL AS descPerfil, SP.CVE_ESTATUS AS cveEstatus," +
                " SP.DEFECTO AS defecto, SP.SISTEMA AS sistema, " +
                "             (SELECT   COUNT(1)      " +
                "                      FROM SEG_MENUS SGP      " +
                "                      ,SEG_PERFIL_PRIVILEGIOS PP     " +
                "                      WHERE   " +
                "                      PP.ID_SEG_MENU=SGP.ID_SEG_MENU   " +
                "                      AND PP.ID_SEG_PERFIL=SP.ID_SEG_PERFIL   " +
                "                      AND SGP.CVE_ESTATUS = 'VIG'      " +
                "                      and SGP.ID_SEG_SISTEMA='SIGTIC'                       " +
                "                      AND (SGP.TIPO_SEG_MENU = 'MENU'      " +
                "                      OR SGP.ID_SEG_MENU_PADRE IS NULL)) AS numPerfil   " +
                "             FROM SEG_PERFILES SP    " +
                "             WHERE SP.SISTEMA = ?1   " +
                "             AND SP.CVE_ESTATUS = ?2 ", resultClass = SegPerfilBean.class)

@Data
@Entity
@Table(name="SEG_PERFIL_BEAN")
public class SegPerfilBean implements Serializable {
    @Id
    private Integer idPerfil;
    private String nombrePerfil;
    private String descPerfil;
    private  String cveEstatus;
    private String defecto;
    private String sistema;
    private Integer numPerfil;
}
