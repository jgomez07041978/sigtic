package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="CAT_TIPO_SISTEMAS")
public class CatTipoSistema implements Serializable {

    @Id
    @Column(name="CVE_TIPO_SISTEMA")
    private String cveTipoSistema;

    @Column(name="DESC_TIPO_SISTEMA")
    private String nombreTipoSistema;

    @Column(name="DESC_TIPO_SISTEMA_ABREVIADO")
    private String nombreTipoSistemaAbreviado;

    @Column(name="DESC_ALCANCE_TIPO_SISTEMA")
    private String alcanceSistema;

    @Column(name="REQUIERE_SISTEMA_PADRE")
    private String requiereSistemaPadre;

    @Column(name="NUM_ORDEN_TIPO_SISTEMA")
    private Integer numeroOrdenTipoSistema;

    @Column (name="CVE_USUARIO_MTTO")
    private String usuarioMtto;

    @Column (name="FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column (name="FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

    @Column(name="CVE_ESTATUS")
    private String cveEstatus;

    @Transient
    private String opcion;

}