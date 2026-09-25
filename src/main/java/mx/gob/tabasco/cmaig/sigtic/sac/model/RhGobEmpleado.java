package mx.gob.tabasco.cmaig.sigtic.sac.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="RH_GOB_EMPLEADO")
public class RhGobEmpleado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RH_GOB_EMPLEADO_APP")
    @SequenceGenerator(sequenceName = "SEQ_RH_GOB_EMPLEADO", allocationSize = 1, name = "RH_GOB_EMPLEADO_APP")
    @Column(name = "ID_EMPLEADO")
    private Long idEmpleado;

    @Column(name="EXPEDIENTE")
    private String expedienteEmpleado;

    @Column(name="RFC")
    private String rfc;

    @Column(name="NOMBRE_COMPLETO")
    private String nombreCompleto;

    @Column(name="NOMBRE")
    private String nombre;

    @Column(name="APELLIDO_PATERNO")
    private String apellidoPaterno;

    @Column(name="APELLIDO_MATERNO")
    private String apellidoMaterno;

    @Column(name="ESTATUS_EMPLEADO")
    private String estatusEmpleado;

    @Column(name="CLAVE_ENTE_PUBLICO")
    private String claveEntePublico;

    @Column(name="NOMBRE_ENTE_PUBLICO")
    private String nombreEntePublico;

    @Column(name="CLAVE_UNIDAD_ADMINISTRATIVA")
    private String claveUnidadAdministrativa;

    @Column(name="NOMBRE_UNIDAD_ADMINISTRATIVA")
    private String nombreUnidadAdministrativa;
}
