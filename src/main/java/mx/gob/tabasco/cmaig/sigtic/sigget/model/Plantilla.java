package mx.gob.tabasco.cmaig.sigtic.sigget.model;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="CAT_PLANTILLAS_CARGA_DATOS")
public class Plantilla implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PLANTILLA_APP")
    @SequenceGenerator(sequenceName = "SEQ_CAT_PLANTILLAS_CARGA_DATOS", allocationSize = 1, name = "SEQ_PLANTILLA_APP")
    @Column(name = "ID_PLANTILLA_CARGA_DATOS")
    private Long idPlantilla;

    @Column(name = "DESC_PLANTILLA_CARGA_DATOS", nullable = false)
    private String nombrePlantilla;

    @Column(name = "DESC_PLANTILLA_CARGA_DATOS_ABR", nullable = false)
    private String nombrePlantillaAbrev;

    @Column(name = "BASE_DATOS", nullable = false)
    private String baseDatos;

    @Column(name = "NUM_LINEA_NOMBRE_CAMPO", nullable = true)
    private Integer numLinea;

    @Column(name = "NUM_CAMPOS_PLANTILLA", nullable = true)
    private Integer numCampos;

    @Column(name = "NOMBRE_ARCHIVO_PLANTILLA", nullable = true)
    private String nombreArchivo;

    @Column(name = "ID_SISTEMA_PLANTILLA", nullable = false)
    private String idSistema;

    @Column(name = "ID_UNIDAD_ADMTVA_PLANTILLA", nullable = true)
    private Long idUnidadAdmtva;

    @Transient
    private String nombreUnidadAdmtva;

    @Column(name = "SQL_SELECT_01")
    private String sqlSelect;

    @Column(name = "SQL_SELECT_02")
    private String sqlSelect2;

    @Column(name = "SQL_SELECT_03")
    private String sqlSelect3;

    @Column(name = "SQL_SELECT_04")
    private String sqlSelect4;

    @Column(name = "SQL_SELECT_05")
    private String sqlSelect5;

    @Column(name = "SQL_INSERT_01")
    private String sqlInsert;

    @Column(name = "SQL_INSERT_02")
    private String sqlInsert2;

    @Column(name = "SQL_INSERT_03")
    private String sqlInsert3;

    @Column(name = "SQL_INSERT_04")
    private String sqlInsert4;

    @Column(name = "SQL_INSERT_05")
    private String sqlInsert5;

    @Column(name = "SQL_UPDATE_01")
    private String sqlUpdate;

    @Column(name = "SQL_UPDATE_02")
    private String sqlUpdate2;

    @Column(name = "SQL_UPDATE_03")
    private String sqlUpdate3;

    @Column(name = "SQL_UPDATE_04")
    private String sqlUpdate4;

    @Column(name = "SQL_UPDATE_05")
    private String sqlUpdate5;

    @Column(name = "SQL_DELETE_01")
    private String sqlDelete;

    @Column(name = "SQL_DELETE_02")
    private String sqlDelete2;

    @Column(name = "SQL_DELETE_03")
    private String sqlDelete3;

    @Column(name = "SQL_DELETE_04")
    private String sqlDelete4;

    @Column(name = "SQL_DELETE_05")
    private String sqlDelete5;

    @Column(name = "SQL_MERGE_01")
    private String sqlMerge;

    @Column(name = "SQL_MERGE_02")
    private String sqlMerge2;

    @Column(name = "SQL_MERGE_03")
    private String sqlMerge3;

    @Column(name = "SQL_MERGE_04")
    private String sqlMerge4;

    @Column(name = "SQL_MERGE_05")
    private String sqlMerge5;

    @Column(name = "SQL_VALIDACION_01")
    private String sqlValidacion;

    @Column(name = "SQL_VALIDACION_02")
    private String sqlValidacion2;

    @Column(name = "SQL_VALIDACION_03")
    private String sqlValidacion3;

    @Column(name = "SQL_VALIDACION_04")
    private String sqlValidacion4;

    @Column(name = "SQL_VALIDACION_05")
    private String sqlValidacion5;

    @Column(name = "FECHA_REGISTRO", nullable = false)
    private Timestamp fechaRegistro;

    @Column(name = "FECHA_ULTIMO_CAMBIO", nullable = false)
    Timestamp fechaUltimoCambio;

    @Column(name = "CVE_ESTATUS", nullable = false)
    private String cveEstatus;

    @Column(name = "ID_CARACTER_SEPARADOR_CAMPO", nullable = true)
    private Integer idSeparador;

    @Column(name = "ESQUEMA_TABLA_DEFINITIVA", nullable = true)
    private String esquema;

    @Transient
    private Integer totalCamposCapturados;

    @Column(name = "CVE_USUARIO_MTTO", nullable = false)
    private String cveUsuarioMtto;

    @Column(name = "NOMBRE_ARCHIVO")
    private String fileName;

    @Column(name = "NOMBRE_ORGINAL_ARCHIVO")
    private String fileNameOriginal;

    @Column(name = "EXTENSION_ARCHIVO")
    private String fileExtension;

    @Column(name = "RUTA_ARCHIVO")
    private String fileRoute;

    @Column(name = "USUARIO_CARGO_ARCHIVO")
    private String fileUserLoad;

}
