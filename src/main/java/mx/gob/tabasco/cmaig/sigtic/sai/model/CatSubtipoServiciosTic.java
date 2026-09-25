package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="CAT_SUBTIPO_SERVICIOS_TIC")
public class CatSubtipoServiciosTic implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="SEQ_SUBTIPO_SERVICIO_TIC")
    @SequenceGenerator(sequenceName = "SEQ_PK_CAT_SUBTIPO_SERVICI_TIC", allocationSize = 1, name = "SEQ_SUBTIPO_SERVICIO_TIC")
    @Column (name = "ID_SUBTIPO_SERVICIO_TIC")
    private Long idSubtipoServicioTic;

    @ManyToOne
    @JoinColumn (name = "ID_TIPO_SERVICIO_TIC")
    private CatTipoServiciosTic catTipoServiciosTic;

    @Column (name = "CVE_SUBTIPO_SERVICIO_TIC")
    private String cveSubtipoServicioTic;

    @Column (name = "DESC_SUBTIPO_SERVICIO_TIC")
    private String nombreSubtipoServicioTic;

    @Column (name = "DESC_SUBTIPO_SERVICIO_TIC_ABR")
    private String nombreSubtipServicioTicAbr;

/*    @Column (name = "ALCANCE_SUBTIPO_SERVICIO_TIC")
    private String alcanceSubtipoServicioTic;*/

    @Column (name = "NUM_ORDEN_SUBTIPO_SERVICIO_TIC")
    private Integer numeroOrdenSubtipoServicioTic;

/*    @Column (name = "ID_COLOR_SUBTIPO_SERVICIO_TIC")
    private Long idColorSubtipo_ServicioTic;

    @Column (name = "ID_IMAGEN_SUBTIPO_SERVICIO_TIC")
    private Long idImagenSubtipoServicioTic;*/

    /*@Column (name = "IP_PC_MTTO")
    private String iPPcMtto;

    @Column (name = "NOMBRE_PC_MTTO")
    private String nombrePcMtto;

    @Column (name = "CVE_USUARIO_SO_MTTO")
    private String cveUsuarioSoMtto;*/

    @Column (name = "CVE_USUARIO_MTTO")
    private String cveUsuarioMtto;

   /* @Column (name = "NOMBRE_APLICACION_MTTO")
    private String nombreAplicacionMtto;*/

    @Column (name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column (name = "FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;
/*
    /*@Column (name = "ID_TIPO_SERVICIO_TIC")
    private Long idTipoServicioTic;*/

    @Column (name = "CVE_ESTATUS")
    private String cveEstatus;

    @Column (name = "CONTROL_INVENTARIO")
    private String controlInventario;

    @Column (name = "CONTROL_IP")
    private String controlIp;

    @Column (name = "CONTROL_PUERTO")
    private String controlPuerto;

    @Column (name = "CONTROL_USUARIO")
    private String controlUsuarios;

    @Column (name = "GENERA_CR")
    private String generaCr;

    @Column (name = "CR_FIRMADA")
    private String crFirmada;



}

