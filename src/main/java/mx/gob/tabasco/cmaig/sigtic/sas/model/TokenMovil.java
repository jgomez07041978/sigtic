package mx.gob.tabasco.cmaig.sigtic.sas.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "TOKEN_MOVIL")
public class TokenMovil {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TOKEN_MOVIL_APP")
    @SequenceGenerator(sequenceName = "TOKEN_MOVIL_SEQ", allocationSize = 1, name = "SEQ_TOKEN_MOVIL_APP")
    @Column(name = "ID_TOKEN_MOVIL")
    private Long idTokenMovil;

    @Column(name = "TOKEN_MOVIL", nullable = false)
    private String tokenMovil;

    @Column(name = "DISPOSITIVO", nullable = true)
    private String dispositivo;

    @Column(name = "FECHA_REGISTRO", nullable = true)
    private Timestamp fechaRegistro;

}
