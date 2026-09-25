package mx.gob.tabasco.cmaig.sigtic.sigget.model;


import lombok.Data;
import mx.gob.tabasco.cmaig.sigtic.sai.model.Usuario;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="MANUAL")
public class Manual implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MANUAL_APP")
    @SequenceGenerator(sequenceName = "SEQ_MANUAL", allocationSize = 1, name = "SEQ_MANUAL_APP")
    @Column(name = "ID_MANUAL")
    private Long idManual;

    @Column(name = "NOMBRE_MANUAL", nullable = false)
    private String nombreManual;

    @Column(name = "DESCRIPCION", nullable = false)
    private String descripcionManual;

    @Column(name = "NOMBRE_ARCHIVO")
    private String nombreArchivo;

    @Column(name = "NOMBRE_ORIGINAL_ARCHIVO")
    private String nombreArchivoOriginal;

    @Column(name = "RUTA_ARCHIVO")
    private String rutaArchivo;

    @Column(name = "EXTENSION_ARCHIVO")
    private String fileExtension;


    @Transient
    private String contenido;

    @Column(name = "ID_USUARIO_MTTO", nullable = false)
    private Long idUsuarioMtto;

    @Column(name = "FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column(name = "FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

    @Column(name = "CVE_ESTATUS", nullable = false)
    private String cveEstatus;

    @Column(name = "NUMERO_ORDEN", nullable = false)
    private Long numeroOrden;

    @ManyToOne
    @JoinColumn(name = "ID_GRUPO")
    private GrupoManual grupoManual;

    @Column(name = "TIPO_MANUAL", nullable = false)
    private String tipoManual;

    @Transient
    private String tipoManualText;

    public String getTipoManualText() {

        if (this.tipoManual.equals("U"))
            return "Usuario";
        else
            return "Técnico";
    }

}
