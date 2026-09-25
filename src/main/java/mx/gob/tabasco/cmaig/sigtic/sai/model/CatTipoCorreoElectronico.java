package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="CAT_TIPO_CORREO_ELECTRONICO")
public class CatTipoCorreoElectronico implements Serializable {

    @Id
    @Column(name = "CVE_TIPO_CORREO_ELECTRONICO")
    private String cveTipoCorreoElectronico;

    @Column(name = "DESC_TIPO_CORREO_ELECTRONICO")
    private String nombreTipoCorreoElectronico;

    @Column(name = "DESC_TIPO_CORREO_ELECTRONICO_A")
    private String nombreTipoCorreoElectronicoA;

    @Column(name = "ALCANCE_TIPO_CORREOELECTRONICO")
    private String alcanceTipoCorreoElectronico;

    @Column(name = "NUM_ORDEN_TIPO_CORREO_ELECTRON")
    private Integer numeroOrden;

    @Column (name="CVE_USUARIO_MTTO")
    private String usuarioMtto;

    @Column (name="FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column (name = "FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

    @Column(name = "ID_TIPO_CORREO")
    private Long idTipoCorreo;

    @Column(name = "ID_SUBTIPO_SERVICIO")
    private Long idSubtipoServicio;

    @Transient
    private String opcion;

}
