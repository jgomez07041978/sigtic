package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="CAT_SERVIDOR_CORREOELECTRONICO")
public class CatServidorCorreoElectronico implements Serializable {

    @Id
    @Column(name = "ID_SERVIDOR_CORREO_ELECTRONICO")
    private Long idServidorCorreoElectronico;

    @Column(name = "DESC_SERVIDOR_CORREO_ELECT")
    private String nombreServidorCorreoElectronico;

    @Column(name = "DESC_SERVIDOR_CORREO_ELECT_ABR")
    private String nombreServidorCorreoElectronicoAbreviado;

    @Column(name = "CVE_NIVEL_CORREO_ELECTRONICO")
    private String cveNivelCorreoElectronico;

    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO_SERVIDORCORREO")
    private EntePublico entePublico;

    @Column(name = "NUM_ORDEN_SERVIDOR_CORREOELECT")
    private String numeroOrdenServidorCorreoElectronico;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;
}
