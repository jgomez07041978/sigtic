package mx.gob.tabasco.cmaig.sigtic.sac.model;


import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="SAC_CAT_USUARIO_ENTE_PUBLICO")
public class SacCatUsuarioEntePublicoView {

    @Id
    @Column(name = "ID_USUARIO_ENTE_PUBLICO")
    private Long idUsuarioEntePublico;

    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @Column(name = "CVE_USUARIO")
    private String cveUsuario;

    @Column(name = "CURP_USUARIO")
    private String curpUsuario;

    @Column(name = "RFC_USUARIO")
    private String rfcUsuario;

    @Column(name = "NOMBRE_USUARIO")
    private String nombre;

    @Column(name = "SEGUNDO_NOMBRE_USUARIO")
    private String segundoNombre;

    @Column(name = "APELLIDO_PATERNO_USUARIO")
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO_USUARIO")
    private String apellidoMaterno;

    @Column(name = "NOMBRE_COMPLETO")
    private String nombreCompleto;

    @Column(name = "NOMBRE_COMPLETO_FMT_MAY")
    private String nombreCompletoMayusculas;

    @Column(name = "NOMBRE_COMPLETO_APAMN")
    private String nombreCompletoPaternoMinuscula;

    @Column(name = "NOMBRE_COMPLETO_APAMN_FMT_MAY")
    private String nombreCompletoPaternoMayuscula;

    @Column(name = "CORREO_INSTITUCIONAL")
    private String correoInstitucional;

    @Column(name = "NUM_TELEFONO_INSTITUCIONAL")
    private String telefonoInstitucinal;

    @Column(name = "NUM_EXTENSION_INSTITUCIONAL")
    private String numeroExtension;

    @Column(name = "ID_ENTE_PUBLICO")
    private Long idEntePublico;

    @Column(name = "CVE_ENTE_PUBLICO")
    private String cveEntePublico;

    @Column(name = "CVE_ENTE_PUBLICO2")
    private String cveEntePublico2;

    @Column(name = "DESC_ENTE_PUBLICO")
    private String nombreEntePublico;

    @Column(name = "DESC_ENTE_PUBLICO_ABREVIADA")
    private String descEntePublicoAbreviada;

    @Column(name = "DESC_ENTE_PUBLICO_ABREVIADA2")
    private String descEntePublicoAbreviada2;

   /* @Column(name = "CVE_TIPO_ENTE_PUBLICO")
    private Integer cveTipoEntePublico;*/

    @Column(name = "DESC_TIPO_ENTE_PUBLICO")
    private String nombreTipoEntePublico;

    @Column(name = "DESC_GRUPO_ENTE_PUBLICO")
    private String nombreGrupoEntePublico;

    @Column(name = "DESC_GRUPO_ENTE_PUBLICO_ABREV2")
    private String nombreGrupoEntePublicoAbreviada;

    @Column(name = "CVE_CATEGORIA_USUARIO")
    private String cveCategoria;

    @Column(name = "DESC_CATEGORIA_USUARIO")
    private String nombreCategoriaUsuario;

    @Column(name = "DESC_CATEGORIA_USUARIO_ABREV")
    private String nombreCategoriaUsuarioAbreviada;

    @Column(name = "ID_UNIDAD_ADMINISTRATIVA")
    private Long idUnidadAdministrativa;

    @Column(name = "CVE_UNIDAD_ADMINISTRATIVA")
    private String cveUnidadAdministrativa;

    @Column(name = "DESC_UNIDAD_ADMINISTRATIVA")
    private String nombreUnidadAdministrativa;

    @Column(name = "DESC_UNIDAD_ADMINISTRATIVA_ABR")
    private String nombreUnidadAdministrativaAbreviada;

    @Column(name = "DESC_UNIDAD_ADMINISTRATIVA_PTO")
    private String nombreUnidadAdministrativaPresupuestal;

    @Column(name = "RUTA_DESC_UNIDAD_ADMTVA")
    private String rutaUnidadAdministrativa;

    @Column(name = "RUTA_DESC_UNIDAD_ADMTVA_ABR")
    private String rutaUnidadAdministrativaAbreviada;

    @Column(name = "DESC_UNIDAD_ADMINISTRATIVA_XN")
    private String rutaUnidadAdministrativaXn;

    @Column(name = "DESC_UBICACION_UNIDAD_ADMTVA")
    private String nombreUbicacionUnidadAdministrativa;

    @Column(name = "DESC_UBICACION_UNIDAD_ADMTVA_A")
    private String nombreUbicacionUnidadAdministrativaAbreviada;

    @Column(name = "FECHA_ALTA")
    private Timestamp fechaAlta;

    @Column(name = "FECHA_BAJA")
    private Timestamp fechaBaja;

    @Column(name = "ID_UNIDAD_ADMINISTRATIVA_PPTAL")
    private Long idUnidadAdministrativaPresupuestal;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

    @Column(name = "CVE_ESTATUS_ENTE_PUBLICO")
    private String cveEstatusEntePublico;

    @Transient
    private Integer pagina;

    @Transient
    private Integer size;
}
