package mx.gob.tabasco.cmaig.sigtic.sai.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;


@Getter
@Setter
@Entity
@Table(name = "BITACORA_MOVIMIENTO")
public class BitacoraMovimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BITA_MOV_BIEN")
    @SequenceGenerator(sequenceName = "SEQ_BITACORA_MOV_BIEN", allocationSize = 1, name = "SEQ_BITA_MOV_BIEN")
    @Column(name = "ID_BITACORA_MOVIMIENTO")
    private Long idBitacoraMovimiento;

    @Column(name = "TABLA_MOVIMIENTO", nullable = false)
    private String tablaMovimiento;

    @Column(name = "DESCRIPCION_MOVIMIENTO", nullable = false)
    private String descripcionMovimiento;

    @Column(name = "ID_MOVIMIENTO", nullable = false)
    private Long idMovimiento;

    @Column(name = "USUARIO_REALIZO", nullable = false)
    private String usuarioRealizo;

    @Column(name = "ID_USUARIO", nullable = false)
    private Long idUsuario;

    @Column(name = "FECHA_REGISTRO", nullable = false)
    private Timestamp fechaRegistro;

    @Column(name = "IP_EQUIPO_REGISTRO")
    private String ipRegistro;

}
