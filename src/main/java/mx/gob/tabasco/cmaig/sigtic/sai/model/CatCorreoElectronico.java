package mx.gob.tabasco.cmaig.sigtic.sai.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name="CORREOS_ELECTRONICOS")
public class CatCorreoElectronico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CORREO_APP")
    @SequenceGenerator(sequenceName = "SEQ_CORREOS_ELECTRONICOS", allocationSize = 1, name = "SEQ_CORREO_APP")
    @Column(name = "ID_CORREO_ELECTRONICO")
    private Long idCorreoElectronico;

    @ManyToOne
    @JoinColumn(name="ID_SERVIDOR_CORREO_ELECTRONICO")
    private CatServidorCorreoElectronico catServidorCorreoElectronico;

    @ManyToOne
    @JoinColumn(name="CVE_TIPO_CORREO_ELECTRONICO")
    private CatTipoCorreoElectronico catTipoCorreoElectronico;

    @Column(name = "CORREO_ELECTRONICO")
    private String correoElectronico;

    @Column(name = "CORREO_ELECTRONICO_ENVIO")
    private String correoElectronicoEnvio;

    @Column(name = "CORREO_ELECTRONICO_ALIAS")
    private String correoElectronicoAlias;

    @ManyToOne
    @JoinColumn(name="ID_TITULAR_CORREO_ELECTRONICO", nullable = true)
    private Usuario catUsuario;

    @Column(name = "CURP_TITULAR_CORREOELECTRONICO")
    private String curpTitular;

    @Column(name = "RFC_TITULAR_CORREO_ELECTRON_EP")
    private String rfcTitular;

    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO_TITULAR_C_EP", nullable = true)
    private EntePublico entePublico;

    @ManyToOne
    @JoinColumn(name="ID_UNIDAD_ADMTVA_TITULAR_C_EP", nullable = true)
    private UnidadAdministrativa unidadAdministrativa;

    @ManyToOne
    @JoinColumn(name="ID_TITULAR_CORREO_ENTE_PUBLICO", nullable = true)
    private Usuario usuarioTitularEntePublico;

    @Column(name = "TITULAR_CORREO_ELECTRONICO_EP", nullable = true)
    private String titularCorreoElecntronicoEntePublico;

    @Column(name = "CARGO_TITULAR_CORREO_EP", nullable = true)
    private String cargoTitularCorreo;

/*    @ManyToOne
    @JoinColumn(name="ID_CARGO_TITULAR_CORREO_EP", nullable = true)
    private CatCargosUsuarios cargosUsuarios;*/

    @Column(name = "DESC_CARGO_TITULAR_CORREO_PR")
    private String nombreCargoTitular;

    @ManyToOne
    @JoinColumn(name="ID_CATEGORIA_TITULAR_CORREO_EP", nullable = true)
    private CatCategoria catCategoria;

    @Column(name = "DESC_CATEGORIA_TITULARCORREOPR")
    private String nombreCategoriaTitular;

    @Column(name = "DOCTO_ALTA_CORREO_ELECTRONICO")
    private String documentoAltaCorreo;

    @Column(name = "FECHA_ALTA_CORREO_ELECTRONICO")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp fechaAltaCorreoElectronico;

    @Transient
    private String fechaAltaCorreoElectronicoText;

    public String getFechaAltaCorreoElectronicoText() {
        if (this.fechaAltaCorreoElectronico!=null) {
            Date date = new Date(this.fechaAltaCorreoElectronico.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaAltaCorreoElectronicoText = dateFormat.format(date);
            return this.fechaAltaCorreoElectronicoText;
        }
        else
            return null;
    }

    @Column(name = "DOCTO_BAJA_CORREO_ELECTRONICO")
    private String documentoBajaCorreo;

    @Column(name = "FECHA_BAJA_CORREO_ELECTRONICO")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp fechaBajaCorreoElectronico;

    @Transient
    private String fechaBajaCorreoElectronicoText;

    public String getFechaBajaCorreoElectronicoText() {
        if (this.fechaBajaCorreoElectronico!=null) {
            Date date = new Date(this.fechaBajaCorreoElectronico.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaBajaCorreoElectronicoText = dateFormat.format(date);
            return this.fechaBajaCorreoElectronicoText;
        }
        else
            return null;
    }

    @Column(name = "FECHA_ULTIMO_ACCESO_CORREO")
    private Timestamp fechaUltimoAcceso;

    @Column(name = "ULTIMO_FOLIO_MESA_DE_AYUDA")
    private Timestamp ultimoFolioMesaAyuda;

    @Column(name = "DOCTO_ULTIMO_CAMBIO_CONTRASENA")
    private Timestamp documentoUltimoCambioPassword;

    @Column(name = "FECHA_ULTIMO_CAMBIO_CONTRASENA")
    private Timestamp fechaUltimoCambioPassword;

    @Column(name = "FECHA_ULTIMA_NOTIFICA_ESPACIO")
    private Timestamp fechaUltimoNotificacionEspacio;

    @ManyToOne
    @JoinColumn(name="CVE_ESTATUS_CORREO_ELECTRONICO", nullable = true)
    private CatEstatusCorreoElectronico catEstatusCorreoElectronico;

    @Column(name = "NOTAS_CORREO_ELECTRONICO")
    private String  idNotasCorreoElectronico;

    @Column(name = "ID_CORTE_CORREO_ELECTRONICO")
    private Integer  idCorteCorreoElectronico;

    @Column(name = "NOMBRE_APLICACION_MTTO")
    private String  idNombreAplicacion;

    @Column(name = "FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

    @Transient
    private String fechaUltimoCambioText;

    public String getFechaUltimoCambioText() {
        if (this.fechaUltimoCambio!=null) {
            Date date = new Date(this.fechaUltimoCambio.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaUltimoCambioText = dateFormat.format(date);
            return this.fechaUltimoCambioText;
        }
        else
            return null;
    }

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

    @Column(name = "VAL_CORREO_ELECTRONICO")
    private String usuariovalidoCorreoElectronico;

    @Column(name = "VAL_CURP_USUARIO")
    private String validoCurpUsuario;

    @Column(name = "VAL_RFC_USUARIO")
    private String validoRfcUsuario;

    @Column(name = "VAL_CORREO_USUARIO")
    private String validoCorreoUsuario;

    @Column(name = "VAL_CURP_ARCHIVO_PDF")
    private String validoCurpArchivoPdf;

    @Column(name = "OBS_CORREO_USUARIO")
    private String observacionCorreoUsuario;

    @Column(name = "VAL_NOMBRE_COMPLETO_USUARIO")
    private String nombreCompletoUsuarioValido;

    @Column(name = "ESTATUS_VALIDACION")
    private String estatusValidacion;

    @Column(name = "CONTRASENIA")
    private String passwordCorreo;

    @Transient
    private String fechaUltimoAccesoText;

    public String getFechaUltimoAccesoText() {
        if (this.fechaUltimoAcceso!=null) {
            Date date = new Date(this.fechaUltimoAcceso.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaUltimoAccesoText = dateFormat.format(date);
            return this.fechaUltimoAccesoText;
        }
        else
            return null;
    }
}