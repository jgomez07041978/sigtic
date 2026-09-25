package mx.gob.tabasco.cmaig.sigtic.sai.model;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="CAT_SUBTIPO_SERVICIOS_FIRMA")
public class CatSubtipoServiciosFirma implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="SEQ_SUBTIPO_SERVICIO_FIRMA_APP")
    @SequenceGenerator(sequenceName = "SEQ_SUBTIPO_SERVICIO_FIRMA", allocationSize = 1, name = "SEQ_SUBTIPO_SERVICIO_FIRMA_APP")
    @Column (name = "ID_SUBTIPO_SERVICIO_FIRMA")
    private Long idSubtipoServicioFirma;

    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO")
    private EntePublico entePublico;

    @ManyToOne
    @JoinColumn(name="ID_SUBTIPO_SERVICIO_TIC")
    private CatSubtipoServiciosTic catSubtipoServiciosTic;

    @Column (name = "NUMERO_FIRMAS")
    private Integer numeroFirmas;

    @Column (name = "CVE_ESTATUS")
    private String cveEstatus;

    @Column(name = "ID_USUARIO_REGISTRO")
    private Long idUsuarioRegistro;

    @Column(name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column(name = "ID_USUARIO_ACTUALIZO")
    private Long idUsuarioActualizo;

    @Column(name = "FECHA_ACTUALIZO")
    private Timestamp fechaActualizo;
}
