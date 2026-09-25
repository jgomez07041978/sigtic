package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "CAT_TIPO_CONEXION_LINEA_TELEF")
public class CatTipoConexionLineaTelef implements Serializable {

    @Id
    @Column(name = "CVE_TIPO_CONEXION_LINEA_TELEF")
    private String cveTipoConexionLineaTelef;

    @Column(name = "DESC_TIPO_CONEXION_LINEA_TELEF")
    private String nombreTipoConexionLineaTelef;

    @Column(name = "DESC_TIPO_CONEXION_LINEA_TEL_A")
    private String nombreTipoConexionLineaTelefAbr;

    @Column(name = "ALCANCE_TIPO_CONEXION_LINEATEL")
    private String alcanceTipoConexionLineaTelef;

    @Column(name = "NUM_ORDEN_TIPO_CONEXION_LINEAT")
    private Integer numeroOrden;

    @Column(name = "ID_COLOR_TIPO_CONEXION_LINEA_T")
    private Integer idColor;

    @Column(name = "ID_IMAGEN_TIPO_CONEXION_LINEAT")
    private Integer idImagen;

    @Column(name = "IP_PC_MTTO", nullable = true)
    private String ipPC;

    @Column(name = "NOMBRE_PC_MTTO", nullable = true)
    private String nombrePC;

    @Column(name = "CVE_USUARIO_SO_MTTO", nullable = true)
    private String usuarioSO;

    @Column(name = "CVE_USUARIO_MTTO", nullable = true)
    private String cveUsuarioMtto;

    @Column(name = "NOMBRE_APLICACION_MTTO", nullable = true)
    private String nombreAplicacion;

    @Column(name = "FECHA_REGISTRO", nullable = true)
    private Timestamp fechaRegistro;

    @Column(name = "FECHA_ULTIMO_CAMBIO", nullable = true)
    private Timestamp fechaUltimoCambio;

    @Column(name = "CVE_ESTATUS", nullable = true)
    private String cveEstatus;
}
