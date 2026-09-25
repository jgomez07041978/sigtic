package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="CAT_TIPO_SERVICIOS_TIC")
public class CatTipoServiciosTic implements Serializable {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "SEQ_TIPO_SERVICIOS_TIC")
    @SequenceGenerator(sequenceName = "SEQ_TIPO_SERVICIOS_TIC", allocationSize = 1, name = "SEQ_TIPO_SERVICIOS_TIC")
    @Column (name = "ID_TIPO_SERVICIO_TIC")
    private Long idTipoServicioTic;

    @Column (name = "DESC_TIPO_SERVICIO_TIC")
    private String nombreTipoServicioTic;

    @Column (name = "DESC_TIPO_SERVICIO_TIC_ABREV")
    private String nombreTipoServicioTicAbrev;

    @Column (name = "ALCANCE_TIPO_SERVICIO_TIC")
    private String alcamceTipoServicioTic;

    @Column (name = "NUM_ORDEN_TIPO_SERVICIO_TIC")
    private Long numOrdenTipoServicioTic;

    /*
    @Column (name = "ID_COLOR_TIPO_SERVICIO_TIC")
    private Long idColorTipoServicioTic;

    @Column (name = "ID_IMAGEN_TIPO_SERVICIO_TIC")
    private Long idImagenTipoSerivicioTic;

    @Column (name = "IP_PC_MTTO")
    private String ipPcMtto;

    @Column (name = "NOMBRE_PC_MTTO")
    private String nombrePcMtto;

    @Column (name = "CVE_USUARIO_SO_MTTO")
    private String cveUsuarioSoMtto;*/

    @Column (name = "CVE_USUARIO_MTTO")
    private String cveUsuarioMtto;

/*
    @Column (name = "NOMBRE_APLICACION_MTTO")
    private String nombreAplicacionMtto;*/

    @Column (name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column (name = "FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

    @Column (name = "CVE_ESTATUS")
    private String cveEstatus;
}
