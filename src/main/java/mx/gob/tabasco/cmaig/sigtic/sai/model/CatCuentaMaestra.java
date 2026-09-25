package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "CAT_CUENTAS_MAESTRAS")
public class CatCuentaMaestra implements Serializable {

    @Id
    @Column(name = "NUM_CUENTA_MAESTRA")
    private String numCuentaMaestra;

    @Column(name = "CVE_CLIENTE_CUENTA_MAESTRA")
    private String cveClienteCuentaMaestra;

    @Column(name = "NOMBRE_CLIENTE_CUENTA_MAESTRA")
    private String nombreClienteCuentaMaestra;

    @Column(name = "ATENCION_CLIENTE_CUENTAMAESTRA")
    private String atencionClienteCuentaMaestra;

    @Column(name = "AREA_CLIENTE_CTA_MAESTRA")
    private String areaClienteCuentaMaestra;

    @Column(name = "DOMICILIO_CLIENTE_CTA_MAESTRA")
    private String domicilioClienteCuentaMaestra;

    @Column(name = "RFC_CLIENTE_CUENTA_MAESTRA")
    private String rfcClienteCuentaMaestra;

    @Column(name = "CUENTA_MAESTRA_PRINCIPAL")
    private String cuentaMaestraPrincipal;

    @Column(name = "ES_CUENTA_MAESTRA_PRINCIPAL")
    private String esCuentaMaestraPrincipal;

    @ManyToOne
    @JoinColumn(name = "ID_ENTE_PUBLICO_CUENTA_MAESTRA", nullable = true)
    private EntePublico entePublico;

    @ManyToOne
    @JoinColumn(name = "ID_UNIDAD_ADMTVA_CUENTAMAESTRA", nullable = true)
    private UnidadAdministrativa unidadAdministrativa;

    @ManyToOne
    @JoinColumn(name = "ID_UBICACION_UA_CUENTA_MAESTRA", nullable = true)
    private UbicacionUnidadAdmin ubicacionUnidadAdmin;

    @Column(name = "DESC_CUENTA_MAESTRA")
    private String descCuentaMaestra;

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
