package mx.gob.tabasco.cmaig.sigtic.sac.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name="CAT_IMAGENES")
public class Logo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LOGOS_APP")
    @SequenceGenerator(sequenceName = "SEQ_LOGOS", allocationSize = 1, name = "SEQ_LOGOS_APP")
    @Column(name = "ID_IMAGEN")
    private Integer idLogo;

    @Column(name="NOMBRE_IMAGEN")
    private String nombreLogo;

    @Column(name = "NOMBRE_INTERNO")
    private String nombreLogoInterno;

    @Column(name = "RUTA_IMAGEN")
    private String rutaArchivo;

    @Transient
    private String contenido;

    @Column(name = "CVE_USUARIO_MTTO")
    private String cveUsuarioMtto;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

    @Column(name="FECHA_REGISTRO")
    private Timestamp fechaRegistro;

    @Column(name="FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

    @Column(name="ANCHO")
    private Integer ancho;

    @Column(name="ALTO")
    private Integer alto;

    @Transient
    private String nombreEntePublico;

    @Transient
    private String nombreUnidadAdministrativa;

}
