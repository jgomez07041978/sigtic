package mx.gob.tabasco.cmaig.sigtic.sac.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name="CAT_USUARIOS")
public class CatUsuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO_APP")
    @SequenceGenerator(sequenceName = "SEQ_CAT_USUARIOS", allocationSize = 1, name = "SEQ_USUARIO_APP")
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @Column(name="RFC_USUARIO")
    private String rfc;

/*    @Column(name="CVE_USUARIO")
    private String cveUsuario;*/

   /* @Column(name="NUM_EMPLEADO_USUARIO")
    private String numeroEmpleadoUsuario;*/

    @Column(name="CURP_USUARIO")
    private String curp;

    @Column(name="NOMBRE_USUARIO")
    private String nombre;

    @Column(name="SEGUNDO_NOMBRE_USUARIO")
    private String segundoNombre;

    @Column(name="APELLIDO_PATERNO_USUARIO")
    private String apellidoPaterno;

    @Column(name="APELLIDO_MATERNO_USUARIO")
    private String apellidoMaterno;

    @Transient
    private String nombreCompleto;

    @Column(name="FECHA_NACIMIENTO_USUARIO")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp fechaNacimiento;

    @Column(name="CVE_SEXO_USUARIO")
    private String cveSexo;

    /*@Column(name = "ID_CATEGORIA_USUARIO", nullable = true)
    private Integer idCategoria;

    @Column(name="DESC_CATEGORIA_USUARIO_EP")
    private String categoriaEP;

    @Column(name = "ID_CARGO_USUARIO", nullable = true)
    private Integer idCargo;

    @Column(name="DESC_CARGO_USUARIO_EP")
    private String cargoEP;

    @ManyToOne
    @JoinColumn(name="ID_PUESTO_USUARIO", nullable = true)
    private PuestoUsuario puestoUsuario;*/


    @Column(name="CVE_ESTATUS")
    private String cveEstatus;

   /* @ManyToOne
    @JoinColumn(name="ID_UBICACION_UNIDAD_ADMTVA_USU", nullable = true)
    private UbicacionUnidadAdmin ubicacionUnidadAdmin;


    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO_USUARIO", nullable = true)
    private EntePublico entePublico;

    @ManyToOne
    @JoinColumn(name="ID_UNIDAD_ADMTVA_USUARIO", nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private UnidadAdministrativa unidadAdministrativa;
    */


    @Column(name = "NUM_CELULAR_PERSONAL_USUARIO", nullable = true)
    private String numCelular;

    @Column(name = "NUM_TELEFONO_PERSONAL_USUARIO", nullable = true)
    private String numTelefonoPersonal;

    /*@Column(name = "NUM_TELEFONO_INSTITUC_USUARIO", nullable = true)
    private String numTelefonoInstitucional;

    @Column(name = "NUM_EXTENSION_INSTITUC_USUARIO", nullable = true)
    private String numeroExtensionInstitucional;*/

    @Column(name = "CORREO_PERSONAL_USUARIO", nullable = true)
    private String correoPersonal;

    /*@Column(name = "CORREO_INSTITUCIONAL_USUARIO", nullable = true)
    private String correoInstitucional;

    @Column(name = "RFC_TITULAR_UNIDAD_ADMTVA_USU", nullable = true)
    private String rfcTitularUnidad;
    */

/*    @ManyToOne
    @JoinColumn(name="ID_USUARIO_TITULAR_UNIDAD", nullable = true)
    private CatUsuario catUsuarioTitular;*/


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

    @Transient
    private String mensaje;

    @Transient
    private String fechaNacimientoText;

    public String getFechaNacimientoText() {
        if (this.fechaNacimiento!=null) {
            Date date = new Date(this.fechaNacimiento.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaNacimientoText = dateFormat.format(date);
            return this.fechaNacimientoText;
        }
        else
            return null;
    }

    @Transient
    private Boolean isVigente;

    @Transient
    private Boolean isHistorico;

    @Transient
    private Boolean isNoVigente;

    @Column(name = "TOTAL_ENTE_PUBLICO")
    private Integer totalEntePublico;
}
