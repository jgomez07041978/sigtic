package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table (name = "CAT_ESTADOS_SERVICIOS_TIC")
public class CatEstadosServiciosTic implements Serializable {

    @Id
    @Column (name = "CVE_ESTADO_SERVICIO_TIC")
    private  String cveEstadoServicioTic;

    @Column (name = "DESC_ESTADO_SERVICIO_TIC")
    private String descEstadoServicioTic;

    @Column (name = "DESC_ESTADO_SERVICIO_TIC_ABR")
    private String descEstadoServicioTicAbr;

    @Column (name = "DESC_ALCANCE_ESTADO_SERV_TIC")
    private String descAlcanceEstadoServTic;

    @Column (name = "NUM_ORDEN_ESTADO_SERVICIO_TIC")
    private Integer numOrdenEstadoServicioTic;

    @Column (name = "ID_COLOR_ESTADO_SERVICIO_TIC")
    private Integer idColorEstadoServicioTic;

    @Column (name = "ID_IMAGEN_ESTADO_SERVICIO_TIC")
    private Integer idImagenEstadoServicioTic;

    @Column (name = "IP_PC_MTTO")
    private String ipPcMtto;

    @Column (name = "NOMBRE_PC_MTTO")
    private String nombrePcMtto;

    @Column (name = "CVE_USUARIO_SO_MTTO")
    private String cveUsuarioSoMtto;

    @Column (name = "CVE_USUARIO_MTTO")
    private String cveUsuarioMtto;

    @Column (name = "NOMBRE_APLICACION_MTTO")
    private String nombreAplicacionMtto;

    @Column (name = "FECHA_REGISTRO")
    private String fechaRegistro;

    @Column (name = "FECHA_ULTIMO_CAMBIO")
    private String fechaUltimoCambio;

    @Column (name = "CVE_ESTATUS")
    private String cveEstatus;
}
