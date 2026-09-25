package mx.gob.tabasco.cmaig.sigtic.sai.model;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@NamedNativeQuery(name = "CatCorreoElectronicoBean.getCorreosElectronicoInstitucion",
        query = "SELECT " +
                "  CO.ID_CORREO_ELECTRONICO as idCorreoElectronico," +
                "  CO.ID_TITULAR_CORREO_ELECTRONICO as idUsuario," +
                " CO.ID_ENTE_PUBLICO_TITULAR_C_EP as idEntePublico, "+
                "  US.RFC_USUARIO as rfcUsuario," +
                "  US.CURP_USUARIO as curpUsuario," +
                "  US.NOMBRE_USUARIO AS nombreUsuario, " +
                "  US.APELLIDO_PATERNO_USUARIO as apellidoPaterno, " +
                "  US.APELLIDO_MATERNO_USUARIO as apellidoMaterno, " +
                "  TC.DESC_TIPO_CORREO_ELECTRONICO as nombreTipoCorreoElectronico, " +
                "  CO.CORREO_ELECTRONICO AS correoElectronico, " +
                " SC.DESC_SERVIDOR_CORREO_ELECT AS nombreServidorCorreo, " +
                " CEC.DESC_ESTATUS_CORREOELECTRONICO AS cveEstatus, " +
                " CO.ESTATUS_VALIDACION AS estatusValidacion, " +
                " TO_CHAR(CO.FECHA_ULTIMO_ACCESO_CORREO,'DD/MM/YYYY') AS fechaUltimoAcceso," +
                " CO.CONTRASENIA AS passwordCorreo " +
                "FROM CORREOS_ELECTRONICOS CO " +
                "INNER JOIN CAT_SERVIDOR_CORREOELECTRONICO SC ON SC.ID_SERVIDOR_CORREO_ELECTRONICO=CO.ID_SERVIDOR_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_TIPO_CORREO_ELECTRONICO TC ON TC.CVE_TIPO_CORREO_ELECTRONICO=CO.CVE_TIPO_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_ESTATUS_CORREO_ELECTRONICO CEC ON CEC.CVE_ESTATUS_CORREO_ELECTRONICO=CO.CVE_ESTATUS_CORREO_ELECTRONICO " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIOS US ON CO.ID_TITULAR_CORREO_ELECTRONICO=US.ID_USUARIO " +
                "WHERE CO.ID_ENTE_PUBLICO_TITULAR_C_EP=?1 AND CO.CVE_ESTATUS='VIG' " +
                " ORDER BY US.NOMBRE_USUARIO", resultClass = CatCorreoElectronicoBean.class)

@NamedNativeQuery(name = "CatCorreoElectronicoBean.getCorreosElectronicoInstitucionPorUnidad",
        query = "SELECT " +
                "  CO.ID_CORREO_ELECTRONICO as idCorreoElectronico," +
                "  CO.ID_TITULAR_CORREO_ELECTRONICO as idUsuario," +
                " CO.ID_ENTE_PUBLICO_TITULAR_C_EP as idEntePublico, "+
                "  US.RFC_USUARIO as rfcUsuario," +
                "  US.CURP_USUARIO as curpUsuario," +
                "  US.NOMBRE_USUARIO AS nombreUsuario, " +
                "  US.APELLIDO_PATERNO_USUARIO as apellidoPaterno, " +
                "  US.APELLIDO_MATERNO_USUARIO as apellidoMaterno, " +
                "  TC.DESC_TIPO_CORREO_ELECTRONICO as nombreTipoCorreoElectronico, " +
                "  CO.CORREO_ELECTRONICO AS correoElectronico, " +
                " SC.DESC_SERVIDOR_CORREO_ELECT AS nombreServidorCorreo, " +
                " CEC.DESC_ESTATUS_CORREOELECTRONICO AS cveEstatus, " +
                " CO.ESTATUS_VALIDACION AS estatusValidacion, " +
                " TO_CHAR(CO.FECHA_ULTIMO_ACCESO_CORREO,'DD/MM/YYYY') AS fechaUltimoAcceso," +
                " CO.CONTRASENIA AS passwordCorreo " +
                "FROM CORREOS_ELECTRONICOS CO " +
                "INNER JOIN CAT_SERVIDOR_CORREOELECTRONICO SC ON SC.ID_SERVIDOR_CORREO_ELECTRONICO=CO.ID_SERVIDOR_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_TIPO_CORREO_ELECTRONICO TC ON TC.CVE_TIPO_CORREO_ELECTRONICO=CO.CVE_TIPO_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_ESTATUS_CORREO_ELECTRONICO CEC ON CEC.CVE_ESTATUS_CORREO_ELECTRONICO=CO.CVE_ESTATUS_CORREO_ELECTRONICO " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIOS US ON CO.ID_TITULAR_CORREO_ELECTRONICO=US.ID_USUARIO " +
                "WHERE CO.ID_ENTE_PUBLICO_TITULAR_C_EP=?1 AND CO.ID_UNIDAD_ADMTVA_TITULAR_C_EP IN (SELECT DISTINCT UAD.ID_UNIDAD_ADMINISTRATIVA " +
                "                                FROM SIGGET_SAC_CATALOGOS.CAT_UNIDADES_ADMINISTRATIVAS UAD " +
                "                                START WITH    UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE =?2 " +
                "                                OR UAD.ID_UNIDAD_ADMINISTRATIVA = ?2 " +
                "                                CONNECT BY PRIOR UAD.ID_UNIDAD_ADMINISTRATIVA = UAD.ID_UNIDAD_ADMINISTRATIVA_PADRE) AND CO.CVE_ESTATUS='VIG' " +
                " ORDER BY US.NOMBRE_USUARIO", resultClass = CatCorreoElectronicoBean.class)

@NamedNativeQuery(name = "CatCorreoElectronicoBean.getCorreosElectronicoInstitucionListaUnidad",
        query = "SELECT " +
                "  CO.ID_CORREO_ELECTRONICO as idCorreoElectronico," +
                "  CO.ID_TITULAR_CORREO_ELECTRONICO as idUsuario," +
                " CO.ID_ENTE_PUBLICO_TITULAR_C_EP as idEntePublico, "+
                "  US.RFC_USUARIO as rfcUsuario," +
                "  US.CURP_USUARIO as curpUsuario," +
                "  US.NOMBRE_USUARIO AS nombreUsuario, " +
                "  US.APELLIDO_PATERNO_USUARIO as apellidoPaterno, " +
                "  US.APELLIDO_MATERNO_USUARIO as apellidoMaterno, " +
                "  TC.DESC_TIPO_CORREO_ELECTRONICO as nombreTipoCorreoElectronico, " +
                "  CO.CORREO_ELECTRONICO AS correoElectronico, " +
                " SC.DESC_SERVIDOR_CORREO_ELECT AS nombreServidorCorreo, " +
                " CEC.DESC_ESTATUS_CORREOELECTRONICO AS cveEstatus, " +
                " CO.ESTATUS_VALIDACION AS estatusValidacion, " +
                " TO_CHAR(CO.FECHA_ULTIMO_ACCESO_CORREO,'DD/MM/YYYY') AS fechaUltimoAcceso," +
                " CO.CONTRASENIA AS passwordCorreo " +
                "FROM CORREOS_ELECTRONICOS CO " +
                "INNER JOIN CAT_SERVIDOR_CORREOELECTRONICO SC ON SC.ID_SERVIDOR_CORREO_ELECTRONICO=CO.ID_SERVIDOR_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_TIPO_CORREO_ELECTRONICO TC ON TC.CVE_TIPO_CORREO_ELECTRONICO=CO.CVE_TIPO_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_ESTATUS_CORREO_ELECTRONICO CEC ON CEC.CVE_ESTATUS_CORREO_ELECTRONICO=CO.CVE_ESTATUS_CORREO_ELECTRONICO " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIOS US ON CO.ID_TITULAR_CORREO_ELECTRONICO=US.ID_USUARIO " +
                "WHERE CO.ID_ENTE_PUBLICO_TITULAR_C_EP=?1 AND CO.ID_UNIDAD_ADMTVA_TITULAR_C_EP IN ?2 AND CO.CVE_ESTATUS='VIG' " +
                " ORDER BY US.NOMBRE_USUARIO", resultClass = CatCorreoElectronicoBean.class)

@NamedNativeQuery(name = "CatCorreoElectronicoBean.getCorreosElectronicoInstitucionNulo",
        query = "SELECT " +
                "  CO.ID_CORREO_ELECTRONICO as idCorreoElectronico," +
                "  CO.ID_TITULAR_CORREO_ELECTRONICO as idUsuario," +
                " CO.ID_ENTE_PUBLICO_TITULAR_C_EP as idEntePublico, "+
                "  US.RFC_USUARIO as rfcUsuario," +
                "  US.CURP_USUARIO as curpUsuario," +
                "  US.NOMBRE_USUARIO AS nombreUsuario, " +
                "  US.APELLIDO_PATERNO_USUARIO as apellidoPaterno, " +
                "  US.APELLIDO_MATERNO_USUARIO as apellidoMaterno, " +
                "  TC.DESC_TIPO_CORREO_ELECTRONICO as nombreTipoCorreoElectronico, " +
                "  CO.CORREO_ELECTRONICO AS correoElectronico, " +
                " SC.DESC_SERVIDOR_CORREO_ELECT AS nombreServidorCorreo, " +
                " CEC.DESC_ESTATUS_CORREOELECTRONICO AS cveEstatus, " +
                " CO.ESTATUS_VALIDACION AS estatusValidacion, " +
                    " TO_CHAR(CO.FECHA_ULTIMO_ACCESO_CORREO,'DD/MM/YYYY') AS fechaUltimoAcceso," +
                " CO.CONTRASENIA AS passwordCorreo " +
                "FROM CORREOS_ELECTRONICOS CO " +
                "INNER JOIN CAT_SERVIDOR_CORREOELECTRONICO SC ON SC.ID_SERVIDOR_CORREO_ELECTRONICO=CO.ID_SERVIDOR_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_TIPO_CORREO_ELECTRONICO TC ON TC.CVE_TIPO_CORREO_ELECTRONICO=CO.CVE_TIPO_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_ESTATUS_CORREO_ELECTRONICO CEC ON CEC.CVE_ESTATUS_CORREO_ELECTRONICO=CO.CVE_ESTATUS_CORREO_ELECTRONICO " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIOS US ON CO.ID_TITULAR_CORREO_ELECTRONICO=US.ID_USUARIO " +
                "WHERE CO.ID_ENTE_PUBLICO_TITULAR_C_EP is null " +
                " AND CO.CVE_ESTATUS ='VIG' ORDER BY US.NOMBRE_USUARIO", resultClass = CatCorreoElectronicoBean.class)

@NamedNativeQuery(name = "CatCorreoElectronicoBean.getCorreosElectronicoSearch",
        query = "SELECT " +
                "  CO.ID_CORREO_ELECTRONICO as idCorreoElectronico," +
                "  CO.ID_TITULAR_CORREO_ELECTRONICO as idUsuario," +
                " CO.ID_ENTE_PUBLICO_TITULAR_C_EP as idEntePublico, "+
                "  US.RFC_USUARIO as rfcUsuario," +
                "  US.CURP_USUARIO as curpUsuario," +
                "  US.NOMBRE_USUARIO AS nombreUsuario, " +
                "  US.APELLIDO_PATERNO_USUARIO as apellidoPaterno, " +
                "  US.APELLIDO_MATERNO_USUARIO as apellidoMaterno, " +
                "  TC.DESC_TIPO_CORREO_ELECTRONICO as nombreTipoCorreoElectronico, " +
                "  CO.CORREO_ELECTRONICO AS correoElectronico, " +
                " SC.DESC_SERVIDOR_CORREO_ELECT AS nombreServidorCorreo, " +
                " CEC.DESC_ESTATUS_CORREOELECTRONICO AS cveEstatus, " +
                " CO.ESTATUS_VALIDACION AS estatusValidacion, " +
                " TO_CHAR(CO.FECHA_ULTIMO_ACCESO_CORREO,'DD/MM/YYYY') AS fechaUltimoAcceso," +
                " CO.CONTRASENIA AS passwordCorreo " +
                "FROM CORREOS_ELECTRONICOS CO " +
                "INNER JOIN CAT_SERVIDOR_CORREOELECTRONICO SC ON SC.ID_SERVIDOR_CORREO_ELECTRONICO=CO.ID_SERVIDOR_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_TIPO_CORREO_ELECTRONICO TC ON TC.CVE_TIPO_CORREO_ELECTRONICO=CO.CVE_TIPO_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_ESTATUS_CORREO_ELECTRONICO CEC ON CEC.CVE_ESTATUS_CORREO_ELECTRONICO=CO.CVE_ESTATUS_CORREO_ELECTRONICO " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIOS US ON CO.ID_TITULAR_CORREO_ELECTRONICO=US.ID_USUARIO " +
                "WHERE CO.CORREO_ELECTRONICO like ?1 AND CO.ID_UNIDAD_ADMTVA_TITULAR_C_EP IN ?2 AND CO.CVE_ESTATUS IN ('VIG', 'HIS') " +
                " ORDER BY US.NOMBRE_USUARIO", resultClass = CatCorreoElectronicoBean.class)
               /* " /*AND CO.CVE_ESTATUS 'VIG' ORDER BY US.NOMBRE_USUARIO", resultClass = CatCorreoElectronicoBean.class)*/

@NamedNativeQuery(name = "CatCorreoElectronicoBean.getCorreosElectronicoSearchAdmin",
        query = "SELECT " +
                "  CO.ID_CORREO_ELECTRONICO as idCorreoElectronico," +
                "  CO.ID_TITULAR_CORREO_ELECTRONICO as idUsuario," +
                " CO.ID_ENTE_PUBLICO_TITULAR_C_EP as idEntePublico, "+
                "  US.RFC_USUARIO as rfcUsuario," +
                "  US.CURP_USUARIO as curpUsuario," +
                "  US.NOMBRE_USUARIO AS nombreUsuario, " +
                "  US.APELLIDO_PATERNO_USUARIO as apellidoPaterno, " +
                "  US.APELLIDO_MATERNO_USUARIO as apellidoMaterno, " +
                "  TC.DESC_TIPO_CORREO_ELECTRONICO as nombreTipoCorreoElectronico, " +
                "  CO.CORREO_ELECTRONICO AS correoElectronico, " +
                " SC.DESC_SERVIDOR_CORREO_ELECT AS nombreServidorCorreo, " +
                " CEC.DESC_ESTATUS_CORREOELECTRONICO AS cveEstatus, " +
                " CO.ESTATUS_VALIDACION AS estatusValidacion, " +
                " TO_CHAR(CO.FECHA_ULTIMO_ACCESO_CORREO,'DD/MM/YYYY') AS fechaUltimoAcceso," +
                " CO.CONTRASENIA AS passwordCorreo " +
                "FROM CORREOS_ELECTRONICOS CO " +
                "INNER JOIN CAT_SERVIDOR_CORREOELECTRONICO SC ON SC.ID_SERVIDOR_CORREO_ELECTRONICO=CO.ID_SERVIDOR_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_TIPO_CORREO_ELECTRONICO TC ON TC.CVE_TIPO_CORREO_ELECTRONICO=CO.CVE_TIPO_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_ESTATUS_CORREO_ELECTRONICO CEC ON CEC.CVE_ESTATUS_CORREO_ELECTRONICO=CO.CVE_ESTATUS_CORREO_ELECTRONICO " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIOS US ON CO.ID_TITULAR_CORREO_ELECTRONICO=US.ID_USUARIO " +
                "WHERE CO.CORREO_ELECTRONICO like ?1 AND CO.CVE_ESTATUS IN ('VIG', 'HIS')" +
                " ORDER BY US.NOMBRE_USUARIO", resultClass = CatCorreoElectronicoBean.class)

@NamedNativeQuery(name = "CatCorreoElectronicoBean.getCorreosElectronicoModal",
        query = "SELECT " +
                "  CO.ID_CORREO_ELECTRONICO as idCorreoElectronico," +
                "  CO.ID_TITULAR_CORREO_ELECTRONICO as idUsuario," +
                " CO.ID_ENTE_PUBLICO_TITULAR_C_EP as idEntePublico, "+
                "  US.RFC_USUARIO as rfcUsuario," +
                "  US.CURP_USUARIO as curpUsuario," +
                "  US.NOMBRE_USUARIO AS nombreUsuario, " +
                "  US.APELLIDO_PATERNO_USUARIO as apellidoPaterno, " +
                "  US.APELLIDO_MATERNO_USUARIO as apellidoMaterno, " +
                "  TC.DESC_TIPO_CORREO_ELECTRONICO as nombreTipoCorreoElectronico, " +
                "  CO.CORREO_ELECTRONICO AS correoElectronico, " +
                " SC.DESC_SERVIDOR_CORREO_ELECT AS nombreServidorCorreo, " +
                " CEC.DESC_ESTATUS_CORREOELECTRONICO AS cveEstatus, " +
                " CO.ESTATUS_VALIDACION AS estatusValidacion, " +
                " TO_CHAR(CO.FECHA_ULTIMO_ACCESO_CORREO,'DD/MM/YYYY') AS fechaUltimoAcceso," +
                " CO.CONTRASENIA AS passwordCorreo " +
                "FROM CORREOS_ELECTRONICOS CO " +
                "INNER JOIN CAT_SERVIDOR_CORREOELECTRONICO SC ON SC.ID_SERVIDOR_CORREO_ELECTRONICO=CO.ID_SERVIDOR_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_TIPO_CORREO_ELECTRONICO TC ON TC.CVE_TIPO_CORREO_ELECTRONICO=CO.CVE_TIPO_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_ESTATUS_CORREO_ELECTRONICO CEC ON CEC.CVE_ESTATUS_CORREO_ELECTRONICO=CO.CVE_ESTATUS_CORREO_ELECTRONICO " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIOS US ON CO.ID_TITULAR_CORREO_ELECTRONICO=US.ID_USUARIO " +
                "WHERE CO.ID_ENTE_PUBLICO_TITULAR_C_EP=?1 AND CO.ID_UNIDAD_ADMTVA_TITULAR_C_EP=?2 AND TC.ID_TIPO_CORREO=?3 AND CEC.CVE_ESTATUS_CORREO_ELECTRONICO=?4 AND CO.CVE_ESTATUS='VIG' " +
                " ORDER BY US.NOMBRE_USUARIO", resultClass = CatCorreoElectronicoBean.class)

@NamedNativeQuery(name = "CatCorreoElectronicoBean.getCorreosElectronicoModalSinUnidad",
        query = "SELECT " +
                "  CO.ID_CORREO_ELECTRONICO as idCorreoElectronico," +
                "  CO.ID_TITULAR_CORREO_ELECTRONICO as idUsuario," +
                " CO.ID_ENTE_PUBLICO_TITULAR_C_EP as idEntePublico, "+
                "  US.RFC_USUARIO as rfcUsuario," +
                "  US.CURP_USUARIO as curpUsuario," +
                "  US.NOMBRE_USUARIO AS nombreUsuario, " +
                "  US.APELLIDO_PATERNO_USUARIO as apellidoPaterno, " +
                "  US.APELLIDO_MATERNO_USUARIO as apellidoMaterno, " +
                "  TC.DESC_TIPO_CORREO_ELECTRONICO as nombreTipoCorreoElectronico, " +
                "  CO.CORREO_ELECTRONICO AS correoElectronico, " +
                " SC.DESC_SERVIDOR_CORREO_ELECT AS nombreServidorCorreo, " +
                " CEC.DESC_ESTATUS_CORREOELECTRONICO AS cveEstatus, " +
                " CO.ESTATUS_VALIDACION AS estatusValidacion, " +
                " TO_CHAR(CO.FECHA_ULTIMO_ACCESO_CORREO,'DD/MM/YYYY') AS fechaUltimoAcceso," +
                " CO.CONTRASENIA AS passwordCorreo " +
                "FROM CORREOS_ELECTRONICOS CO " +
                "INNER JOIN CAT_SERVIDOR_CORREOELECTRONICO SC ON SC.ID_SERVIDOR_CORREO_ELECTRONICO=CO.ID_SERVIDOR_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_TIPO_CORREO_ELECTRONICO TC ON TC.CVE_TIPO_CORREO_ELECTRONICO=CO.CVE_TIPO_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_ESTATUS_CORREO_ELECTRONICO CEC ON CEC.CVE_ESTATUS_CORREO_ELECTRONICO=CO.CVE_ESTATUS_CORREO_ELECTRONICO " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIOS US ON CO.ID_TITULAR_CORREO_ELECTRONICO=US.ID_USUARIO " +
                "WHERE CO.ID_ENTE_PUBLICO_TITULAR_C_EP=?1 AND CO.ID_UNIDAD_ADMTVA_TITULAR_C_EP is null AND TC.ID_TIPO_CORREO=?2 AND CEC.CVE_ESTATUS_CORREO_ELECTRONICO=?3 AND CO.CVE_ESTATUS='VIG' " +
                " ORDER BY US.NOMBRE_USUARIO", resultClass = CatCorreoElectronicoBean.class)


@NamedNativeQuery(name = "CatCorreoElectronicoBean.getCorreosElectronicoModalTodo",
        query = "SELECT " +
                "  CO.ID_CORREO_ELECTRONICO as idCorreoElectronico," +
                "  CO.ID_TITULAR_CORREO_ELECTRONICO as idUsuario," +
                " CO.ID_ENTE_PUBLICO_TITULAR_C_EP as idEntePublico, "+
                "  US.RFC_USUARIO as rfcUsuario," +
                "  US.CURP_USUARIO as curpUsuario," +
                "  US.NOMBRE_USUARIO AS nombreUsuario, " +
                "  US.APELLIDO_PATERNO_USUARIO as apellidoPaterno, " +
                "  US.APELLIDO_MATERNO_USUARIO as apellidoMaterno, " +
                "  TC.DESC_TIPO_CORREO_ELECTRONICO as nombreTipoCorreoElectronico, " +
                "  CO.CORREO_ELECTRONICO AS correoElectronico, " +
                " SC.DESC_SERVIDOR_CORREO_ELECT AS nombreServidorCorreo, " +
                " CEC.DESC_ESTATUS_CORREOELECTRONICO AS cveEstatus, " +
                " CO.ESTATUS_VALIDACION AS estatusValidacion, " +
                " TO_CHAR(CO.FECHA_ULTIMO_ACCESO_CORREO,'DD/MM/YYYY') AS fechaUltimoAcceso," +
                " CO.CONTRASENIA AS passwordCorreo " +
                "FROM CORREOS_ELECTRONICOS CO " +
                "INNER JOIN CAT_SERVIDOR_CORREOELECTRONICO SC ON SC.ID_SERVIDOR_CORREO_ELECTRONICO=CO.ID_SERVIDOR_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_TIPO_CORREO_ELECTRONICO TC ON TC.CVE_TIPO_CORREO_ELECTRONICO=CO.CVE_TIPO_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_ESTATUS_CORREO_ELECTRONICO CEC ON CEC.CVE_ESTATUS_CORREO_ELECTRONICO=CO.CVE_ESTATUS_CORREO_ELECTRONICO " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIOS US ON CO.ID_TITULAR_CORREO_ELECTRONICO=US.ID_USUARIO " +
                "WHERE CO.ID_ENTE_PUBLICO_TITULAR_C_EP=?1 AND CO.ID_UNIDAD_ADMTVA_TITULAR_C_EP=?2 AND TC.ID_TIPO_CORREO=?3 AND CO.CVE_ESTATUS='VIG' " +
                " ORDER BY US.NOMBRE_USUARIO", resultClass = CatCorreoElectronicoBean.class)

@NamedNativeQuery(name = "CatCorreoElectronicoBean.getCorreosElectronicoModalSinUnidadTodo",
        query = "SELECT " +
                "  CO.ID_CORREO_ELECTRONICO as idCorreoElectronico," +
                "  CO.ID_TITULAR_CORREO_ELECTRONICO as idUsuario," +
                " CO.ID_ENTE_PUBLICO_TITULAR_C_EP as idEntePublico, "+
                "  US.RFC_USUARIO as rfcUsuario," +
                "  US.CURP_USUARIO as curpUsuario," +
                "  US.NOMBRE_USUARIO AS nombreUsuario, " +
                "  US.APELLIDO_PATERNO_USUARIO as apellidoPaterno, " +
                "  US.APELLIDO_MATERNO_USUARIO as apellidoMaterno, " +
                "  TC.DESC_TIPO_CORREO_ELECTRONICO as nombreTipoCorreoElectronico, " +
                "  CO.CORREO_ELECTRONICO AS correoElectronico, " +
                " SC.DESC_SERVIDOR_CORREO_ELECT AS nombreServidorCorreo, " +
                " CEC.DESC_ESTATUS_CORREOELECTRONICO AS cveEstatus, " +
                " CO.ESTATUS_VALIDACION AS estatusValidacion, " +
                " TO_CHAR(CO.FECHA_ULTIMO_ACCESO_CORREO,'DD/MM/YYYY') AS fechaUltimoAcceso," +
                " CO.CONTRASENIA AS passwordCorreo " +
                "FROM CORREOS_ELECTRONICOS CO " +
                "INNER JOIN CAT_SERVIDOR_CORREOELECTRONICO SC ON SC.ID_SERVIDOR_CORREO_ELECTRONICO=CO.ID_SERVIDOR_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_TIPO_CORREO_ELECTRONICO TC ON TC.CVE_TIPO_CORREO_ELECTRONICO=CO.CVE_TIPO_CORREO_ELECTRONICO " +
                "INNER JOIN CAT_ESTATUS_CORREO_ELECTRONICO CEC ON CEC.CVE_ESTATUS_CORREO_ELECTRONICO=CO.CVE_ESTATUS_CORREO_ELECTRONICO " +
                "LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIOS US ON CO.ID_TITULAR_CORREO_ELECTRONICO=US.ID_USUARIO " +
                "WHERE CO.ID_ENTE_PUBLICO_TITULAR_C_EP=?1 AND CO.ID_UNIDAD_ADMTVA_TITULAR_C_EP is null AND TC.ID_TIPO_CORREO=?2 AND CO.CVE_ESTATUS='VIG' " +
                " ORDER BY US.NOMBRE_USUARIO", resultClass = CatCorreoElectronicoBean.class)

@Data
@Entity
@Table(name="CORREO_ELECTRONICO_BEAN")
public class CatCorreoElectronicoBean implements Serializable {
    @Id
    private Long idCorreoElectronico;

    private Long idUsuario;

    private Long idEntePublico;

    private String rfcUsuario;

    private String curpUsuario;

    private String nombreUsuario;

    private String apellidoPaterno;

    private String apellidoMaterno;

    private String nombreServidorCorreo;

    private String nombreTipoCorreoElectronico;

    private String correoElectronico;

    private String cveEstatus;

    private String estatusValidacion;

    private String fechaUltimoAcceso;

    private String passwordCorreo;

    @Transient
    private String nombreCompleto;

    public String getNombreCompleto() {
        nombreCompleto="";

        if (this.nombreUsuario!=null)
            nombreCompleto=nombreCompleto+" "+this.nombreUsuario;

        if (this.apellidoPaterno!=null)
            nombreCompleto=nombreCompleto+" "+this.apellidoPaterno;

        if (this.apellidoMaterno!=null)
            nombreCompleto=nombreCompleto+" "+this.apellidoMaterno;

        return nombreCompleto;
    }

}
