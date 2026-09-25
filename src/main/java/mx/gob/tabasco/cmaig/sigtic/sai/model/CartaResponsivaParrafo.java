package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


@Data
@Entity
@Table(name="CARTA_RESPONSIVA_PARRAFO")
public class CartaResponsivaParrafo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CARTA-RESPO_PARRAFO_APP")
    @SequenceGenerator(sequenceName = "SEQ_CARTA_RESPONSIVA_PARRAFO", allocationSize = 1, name = "SEQ_CARTA-RESPO_PARRAFO_APP")
    @Column(name = "ID_CARTA_RESPONSIVA_PARRAFO")
    private Long idCartaResponsivaParrafo;

    @Column(name = "ID_CARTA_RESPONSIVA")
    private Long idCartaResponsiva;

    @Column(name = "PARRAFO")
    private String parrafo;

    @Column(name = "ORDEN")
    private Integer orden;

    @Column(name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column(name = "CURP_ACEPTO")
    private String curpAcepto;

    @Column(name = "FECHA_ACEPTO")
    private Timestamp fechaAcepto;

    @Column(name = "IP_ACEPTO")
    private String ipAcepto;

    @Column(name = "HOST_ACEPTO")
    private String hostAcepto;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

}
