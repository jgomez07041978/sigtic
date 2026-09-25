package mx.gob.tabasco.cmaig.sigtic.sai.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name="BIENES")
public class Bien implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BIEN_APP")
    @SequenceGenerator(sequenceName = "SEQ_BIENES", allocationSize = 1, name = "SEQ_BIEN_APP")
    @Column(name = "ID_BIEN")
    private Long idBien;

    @Column(name = "NUM_INVENTARIO")
    private String numeroInventario;

    @Column(name = "DESCRIPCION_BIEN")
    private String descripcionBien;

    @Column(name = "FECHA_ALTA")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp fechaAlta;

    @Transient
    private String fechaAltaText;

    public String getFechaAltaText() {
        if (this.fechaAlta!=null) {
            Date date = new Date(this.fechaAlta.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaAltaText = dateFormat.format(date);
            return this.fechaAltaText;
        }
        else
            return null;
    }

    @ManyToOne
    @JoinColumn(name="ID_UNIDAD_ADMINISTRATIVA", nullable = false)
    private UnidadAdministrativa unidadAdministrativa;

    @Column(name = "COSTO")
    private BigDecimal costo;

    @Column(name = "NOTA")
    private String nota;


    @Column(name = "FECHA_CORTE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp fechaCorte;

    @Transient
    private String fechaCorteText;

    public String getFechaCorteText() {
        if (this.fechaCorte!=null) {
            Date date = new Date(this.fechaCorte.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaCorteText = dateFormat.format(date);
            return this.fechaCorteText;
        }
        else
            return null;
    }

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

}
