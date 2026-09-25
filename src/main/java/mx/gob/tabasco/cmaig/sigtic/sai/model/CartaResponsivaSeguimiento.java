package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name="CARTA_RESPONSIVA_SEGUIMIENTO")
public class CartaResponsivaSeguimiento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CARTA_RESPONSIVA_SEGUI_APP")
    @SequenceGenerator(sequenceName = "SEQ_CARTA_RESPONSIVA_SEGUI", allocationSize = 1, name = "SEQ_CARTA_RESPONSIVA_SEGUI_APP")
    @Column(name = "ID_CARTA_RESPONSIVA_SEGUI")
    private Long idCartaResponsivaSeguimiento;

    @ManyToOne
    @JoinColumn(name="ID_ESTATUS_CR")
    private CatEstatusCartaResponsiva estatusCartaResponsiva;

    @ManyToOne
    @JoinColumn(name="ID_CARTA_RESPONSIVA")
    private CartaResponsiva cartaResponsiva;

    @Column(name = "NOTA")
    private String nota;

    @Column(name = "ID_USUARIO_REGISTRO")
    private Long idUsuarioRegistro;

    @Column(name = "IP_REGISTRO")
    private String ipRegistro;

    @Column(name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Transient
    private String fechaRegistroText;

    public String getFechaRegistroText() {

        if (this.fechaRegistro!=null) {
            Date date = new Date(this.fechaRegistro.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            this.fechaRegistroText = dateFormat.format(date);
            return this.fechaRegistroText;
        }
        else
            return null;
    }

}
