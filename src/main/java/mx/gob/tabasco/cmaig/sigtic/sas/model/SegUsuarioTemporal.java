package mx.gob.tabasco.cmaig.sigtic.sas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name = "SEG_USUARIO_TEMPORAL")
public class SegUsuarioTemporal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO_TEMPORAL_APP")
    @SequenceGenerator(sequenceName = "SEQ_USUARIO_TEMPORAL", allocationSize = 1, name = "SEQ_USUARIO_TEMPORAL_APP")
    @Column(name = "ID_USUARIO_TEMPORAL")
    private Long idUsuarioTemporal;

    @Column(name = "ID_ENTE_PUBLICO")
    private Long idEntePublico;

    @Column(name="CURP")
    private String curp;

    @Column(name="RFC")
    private String rfc;

    @Column(name="NOMBRE")
    private String nombre;

    @Column(name="SEGUNDO_NOMBRE")
    private String segundoNombre;

    @Column(name="APELLIDO_PATERNO")
    private String apellidoPaterno;

    @Column(name="APELLIDO_MATERNO")
    private String apellidoMaterno;

    @Column(name="SEXO")
    private String sexo;

    @Column(name="FECHA_NACIMIENTO")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp fechaNacimiento;

    @Column(name = "CELULAR")
    private String celular;

    @Column(name = "CORREO_PERSONAL")
    private String correoPersonal;

    @Column(name = "CORREO_INSTITUCIONAL")
    private String correoInstitucional;

    @Column(name = "TELEFONO_INSTITUCIONAL")
    private String telefonoInstitucional;

    @Column(name = "EXTENSION_INSTITUCIONAL")
    private String extensionInstitucional;

    @Column(name = "CONTRASENIA")
    private String password;

    @Column(name = "METODO_VALIDACION")
    private String metodoValidacion;

    @Column(name = "CODIGO_VALIDACION")
    private String codigoValidacion;

    @Column(name = "ESTATUS")
    private String estatus;

    @Column(name = "UUID")
    private String uuid;

    @Column(name = "ACCESS_TOKEN")
    private String accessToken;

    @Column(name = "TOKEN_URL")
    private String tokenUrl;

    @Column(name = "OFICIO")
    private String oficio;

    @Column(name = "DEPENDENCIA")
    private String dependencia;

    @Transient
    private String fechaNacimientoText;

    @Transient
    private String nombreCompleto;

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

    public String getNombreCompleto() {
        nombreCompleto="";

        if (this.nombre!=null )
            nombreCompleto=this.nombre;

        if (this.segundoNombre!=null)
            nombreCompleto=nombreCompleto+" "+this.segundoNombre;

        if (this.apellidoPaterno!=null)
            nombreCompleto=nombreCompleto+" "+this.apellidoPaterno;

        if (this.apellidoMaterno!=null)
            nombreCompleto=nombreCompleto+" "+this.apellidoMaterno;

        return nombreCompleto;
    }

}