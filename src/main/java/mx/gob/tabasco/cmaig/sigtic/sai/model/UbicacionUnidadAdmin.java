package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="SAC_CAT_UBICACION_UNIDADADMTVA")
public class UbicacionUnidadAdmin implements Serializable {

    @Id
    @Column(name = "ID_UBICACION_UNIDAD_ADMTVA")
    Long idUbicacionAdmtva;

    @Column(name = "DESC_UBICACION_UNIDAD_ADMTVA")
    String descUbicacion;

    @Column(name = "DESC_UBICACION_UNIDAD_ADMTVA_A")
    String descUbicacionAbreviada;

    @Column(name = "NUM_TELEFONO_UNIDAD_ADMTVA")
    String numTelefono;

    @Column(name = "NUM_EXTENSION_UNIDAD_ADMTVA")
    String extension;

    @Column(name = "REFERENCIA_UBICACION_UNIDADADM")
    String referencia;

    @Column(name = "ID_DOMICILIO_UNIDAD_ADMTVA")
    Integer idDomicilioEnte;

    @Column(name = "CORREO_UNIDAD_ADMINISTRATIVA")
    String correoUnidad;

    @Column(name = "NUM_ORDEN_UBICACION_UNIDAD_ADM")
    Integer numOrden;

    @Column(name = "ID_COLOR_UBICACION_UNIDAD_ADM", nullable = true)
    Integer idColor;

    @Column(name = "ID_IMAGEN_UBICACION_UNIDAD_ADM", nullable = true)
    Integer idImagen;

    @Column(name = "IP_PC_MTTO", nullable = true)
    String ipPC;

    @Column(name = "NOMBRE_PC_MTTO", nullable = true)
    String nombrePC;

    @Column(name = "CVE_USUARIO_SO_MTTO", nullable = true)
    String usuarioSO;

    @Column(name = "CVE_USUARIO_MTTO", nullable = true)
    String cveUsuarioMtto;

    @Column(name = "NOMBRE_APLICACION_MTTO", nullable = true)
    String nombreAplicacion;

    @Column(name = "FECHA_REGISTRO", nullable = true)
    Timestamp fechaRegistro;

    @Column(name = "FECHA_ULTIMO_CAMBIO", nullable = true)
    Timestamp fechaCambio;

    @Column(name = "CVE_ESTATUS", nullable = true)
    String cveEstatus;

}
