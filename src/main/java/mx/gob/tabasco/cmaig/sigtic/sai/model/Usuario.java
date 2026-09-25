package mx.gob.tabasco.cmaig.sigtic.sai.model;

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
@Table(name="SIGGET_SAC_CATALOGOS.CAT_USUARIOS")
public class Usuario implements Serializable {

    @Id
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @Column(name="RFC_USUARIO")
    private String rfc;

    /*@Column(name="CVE_USUARIO")
    private String cveUsuario;

    @Column(name="NUM_EMPLEADO_USUARIO")
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

    /*@ManyToOne
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

    @Column(name = "CONTRASENIA_TEMPORAL")
    private String pwdTemporal;

    @Column(name = "FECHA_CONTRASENIA_TEMPORAL")
    private Timestamp fechaPwdTemporal;

    @Transient
    private String fechaPwdTemporalText;
    public String getFechaPwdTemporalText() {

        if (this.fechaPwdTemporal!=null) {
            Date date = new Date(this.fechaPwdTemporal.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaPwdTemporalText = dateFormat.format(date);
            return this.fechaPwdTemporalText;
        }
        else
            return null;
    }

    /*
    @Column(name = "CORREO_INSTITUCIONAL_USUARIO", nullable = true)
    private String correoInstitucional;

    @Column(name = "RFC_TITULAR_UNIDAD_ADMTVA_USU", nullable = true)
    private String rfcTitularUnidad;
*/
    public String getNombreCompleto() {
        nombreCompleto="";

        if (this.nombre!=null)
            nombreCompleto=this.nombre;

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

}
