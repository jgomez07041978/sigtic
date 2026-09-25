package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@NamedNativeQuery(name = "SegUsuarioLista.getUsuarioPorSistemaEnteAdminSigtic",
        query = "SELECT SU.ID_USUARIO_ENTE_PUBLICO as idUsuarioEntePublico, " +
                "        SU.ID_USUARIO as idUsuario, " +
                "        SU.SEG_USUARIO AS usuario, " +
                "        CU.CURP_USUARIO AS curp, " +
                "        CU.NOMBRE_USUARIO AS nombre, " +
                "        CU.SEGUNDO_NOMBRE_USUARIO AS segundoNombre, " +
                "        CU.APELLIDO_PATERNO_USUARIO as apellidoPaterno, " +
                "        CU.APELLIDO_MATERNO_USUARIO as apellidoMaterno, " +
                "        UA.DESC_UNIDAD_ADMINISTRATIVA AS nombreUnidadAdministrativa, " +
                "        (SELECT COUNT(1) FROM SEG_PERFILES_USUARIO SP WHERE SP.SEG_USUARIO=SU.SEG_USUARIO AND SP.SISTEMA='SIGTIC') AS numRol, " +
                "        CE.ID_ENTE_PUBLICO as idEntePublico, SU.CVE_ESTATUS as cveEstatus, UE.ID_USUARIO_ENTE_PUBLICO as idUsuarioEntePublico2 " +
                " FROM SEG_USUARIOS SU  " +
                "            INNER JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIO_ENTE_PUBLICO UE ON UE.ID_USUARIO=SU.ID_USUARIO AND UE.CVE_ESTATUS='VIG' " +
                "            INNER JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIOS CU ON CU.ID_USUARIO=UE.ID_USUARIO  " +
                "            INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.ID_ENTE_PUBLICO=UE.ID_ENTE_PUBLICO  " +
                "            LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_UNIDAD_ADMINISTRATIVA=UE.ID_UNIDAD_ADMINISTRATIVA  " +
                "            WHERE SU.SISTEMA = ?1 " +
                "            AND CE.ID_ENTE_PUBLICO = ?2  " +
                "            ORDER BY CE.DESC_ENTE_PUBLICO, UA.DESC_UNIDAD_ADMINISTRATIVA ", resultClass = SegUsuarioLista.class)

@NamedNativeQuery(name = "SegUsuarioLista.getUsuarioPorSistemaUnidades",
        query = "SELECT SU.ID_USUARIO_ENTE_PUBLICO as idUsuarioEntePublico, " +
                "        SU.ID_USUARIO as idUsuario, " +
                "        SU.SEG_USUARIO AS usuario, " +
                "        CU.CURP_USUARIO AS curp, " +
                "        CU.NOMBRE_USUARIO AS nombre, " +
                "        CU.SEGUNDO_NOMBRE_USUARIO AS segundoNombre, " +
                "        CU.APELLIDO_PATERNO_USUARIO as apellidoPaterno, " +
                "        CU.APELLIDO_MATERNO_USUARIO as apellidoMaterno, " +
                "        UA.DESC_UNIDAD_ADMINISTRATIVA AS nombreUnidadAdministrativa, " +
                "        (SELECT COUNT(1) FROM SEG_PERFILES_USUARIO SP WHERE SP.SEG_USUARIO=SU.SEG_USUARIO AND SP.SISTEMA='SIGTIC') AS numRol, " +
                "        CE.ID_ENTE_PUBLICO as idEntePublico, SU.CVE_ESTATUS as cveEstatus, UE.ID_USUARIO_ENTE_PUBLICO as idUsuarioEntePublico2 " +
                " FROM SEG_USUARIOS SU  " +
                "            INNER JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIO_ENTE_PUBLICO UE ON UE.ID_USUARIO=SU.ID_USUARIO  AND UE.CVE_ESTATUS='VIG' " +
                "            INNER JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIOS CU ON CU.ID_USUARIO=UE.ID_USUARIO  " +
                "            INNER JOIN SIGGET_SAC_CATALOGOS.CAT_ENTES_PUBLICOS CE ON CE.ID_ENTE_PUBLICO=UE.ID_ENTE_PUBLICO  " +
                "            LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UA ON UA.ID_UNIDAD_ADMINISTRATIVA=UE.ID_UNIDAD_ADMINISTRATIVA  " +
                "            WHERE SU.SISTEMA = ?1 " +
                "            AND UA.ID_UNIDAD_ADMINISTRATIVA IN (?2)  " +
                "            ORDER BY CE.DESC_ENTE_PUBLICO, UA.DESC_UNIDAD_ADMINISTRATIVA ", resultClass = SegUsuarioLista.class)

@Data
@Entity
@Table(name="SEG_USUARIO_LISTA")
public class SegUsuarioLista implements Serializable {
    @Id
    private Integer idUsuarioEntePublico;
    private Long idUsuario;
    private String usuario;
    private String curp;
    private String nombre;
    private String segundoNombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombreUnidadAdministrativa;
    private Integer numRol;
    private Long idEntePublico;
    private String cveEstatus;
    private Integer idUsuarioEntePublico2;

    @Transient
    private String nombreCompleto;

    public String getNombreCompleto() {
        nombreCompleto="";

        if (this.nombre!=null)
            nombreCompleto=nombreCompleto+" "+this.nombre;

        if (this.segundoNombre!=null)
            nombreCompleto=nombreCompleto+" "+this.segundoNombre;

        if (this.apellidoPaterno!=null)
            nombreCompleto=nombreCompleto+" "+this.apellidoPaterno;

        if (this.apellidoMaterno!=null)
            nombreCompleto=nombreCompleto+" "+this.apellidoMaterno;

        return nombreCompleto;
    }

}
