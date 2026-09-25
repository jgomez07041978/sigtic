package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Data;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "SEG_USUARIOS")
public class SegUsuario implements Serializable {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "usuario", column = @Column(name = "SEG_USUARIO")),
            @AttributeOverride(name = "sistema", column = @Column(name = "SISTEMA"))
    })
    private SegUsuarioPk segUsuarioPk;

    @Column(name = "CONTRASENIA", nullable = false)
    private String contrasenia;

    /*Solo se usa para mandar la clave, debido a que en la pagina principal ya hay un campo con este nombre*/
    @Transient
    private String contraseniaSistema;

    @Transient
    private String contraseniaActual;

    @Transient
    private String contraseniaNueva;

    @Transient
    private String repetirContraseniaNueva;

    @Column(name = "FECHA_INICIO_VIGENCIA")
    private Timestamp fechaIniciaVigencia;

    @Column(name = "FECHA_FINAL_VIGENCIA")
    private Timestamp fechaFinalVigencia;

    @Column(name = "BLOQUEADO", nullable = false)
    private Integer bloqueado;

    @Column(name = "CONTRASENIA_RESET", nullable = false)
    private Integer contraseniaReset;

    @Column(name = "ID_NIVEL_USUARIO", nullable = true)
    private Integer idNivelUsuario;

    @Column(name = "RUTA_FOTO", nullable = true)
    private String foto;

    @Column(name = "UUIDUSUARIO", nullable = true)
    private String uidUsuario;

    @Column(name = "CVE_ESTATUS", nullable = true)
    private String cveEstatus;

    @Column(name = "ID_USUARIO", nullable = false)
    private Long idUsuario;


    @Column(name = "ID_USUARIO_ENTE_PUBLICO", nullable = false)
    private Long idUsuarioEntePublico;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "TOKEN_ACTIVO")
    private String tokenActivo;

    /*@Transient
    private List<SegPerfil> listaSegPerfil;*/

    /*@Transient
    private CatUsuario catUsuario;*/

    @Transient
    private String opcion;

    @Transient
    private String recursoAcceso;

    @Transient
    private Integer idPerfil;

    @ManyToOne
    @JoinColumn(name="ID_TIPO_USUARIO")
    private SegTipoUsuario segTipoUsuario;

    @Transient
    private Integer idTipoUsuario;

    @Transient
    private CatUsuarioEntePublico catUsuarioEntePublico;

    @Transient
    private String correo;

}
