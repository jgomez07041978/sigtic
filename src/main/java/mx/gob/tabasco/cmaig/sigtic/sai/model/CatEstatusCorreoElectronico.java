package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="CAT_ESTATUS_CORREO_ELECTRONICO")
public class CatEstatusCorreoElectronico implements Serializable {

    @Id
    @Column(name = "CVE_ESTATUS_CORREO_ELECTRONICO")
    private String cveEstatusCorreoElectronico;


    @Column(name = "DESC_ESTATUS_CORREOELECTRONICO")
    private String  nombreEstatusCorreoElectronico;

    @Column(name="ALCANCE_ESTATUS_CORREO_ELECT")
    private String  alcanceEstatusCorreoElectronico;

    @Column(name = "NUM_ORDEN_ESTATUS_CORREO_ELECT")
    private Integer numeroOrden;

    @Column(name = "CVE_ESTATUS")
    private String  cveEstatus;

}
