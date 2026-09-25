package mx.gob.tabasco.cmaig.sigtic.sigget.model;


import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="CARGA_DATOS_X_PLANTILLA")
public class CargaDatosPlantilla implements Serializable {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "idCargaDatosPlantilla", column = @Column(name = "ID_CARGA_DATOS_X_PLANTILLA")),
            @AttributeOverride(name = "numLineaCargaDatos", column = @Column(name = "NUM_LINEA_CARGADATOSXPLANTILLA"))
    })
    private CargaDatosPlantillaPk cargaDatosPlantillaPk;

    @ManyToOne
    @JoinColumn(name = "ID_PLANTILLA_CARGA_DATOS")
    private Plantilla plantilla;

    @Column(name = "COLUMNA_A", nullable = true)
    private String columnaA;

    @Column(name = "COLUMNA_B", nullable = true)
    private String columnaB;

    @Column(name = "COLUMNA_C", nullable = true)
    private String columnaC;

    @Column(name = "COLUMNA_D", nullable = true)
    private String columnaD;

    @Column(name = "COLUMNA_E", nullable = true)
    private String columnaE;

    @Column(name = "COLUMNA_F", nullable = true)
    private String columnaF;

    @Column(name = "COLUMNA_G", nullable = true)
    private String columnaG;

    @Column(name = "COLUMNA_H", nullable = true)
    private String columnaH;

    @Column(name = "COLUMNA_I", nullable = true)
    private String columnaI;

    @Column(name = "COLUMNA_J", nullable = true)
    private String columnaJ;

    @Column(name = "COLUMNA_K", nullable = true)
    private String columnaK;

    @Column(name = "COLUMNA_L", nullable = true)
    private String columnaL;

    @Column(name = "COLUMNA_M", nullable = true)
    private String columnaM;

    @Column(name = "COLUMNA_N", nullable = true)
    private String columnaN;

    @Column(name = "COLUMNA_O", nullable = true)
    private String columnaO;

    @Column(name = "COLUMNA_P", nullable = true)
    private String columnaP;

    @Column(name = "COLUMNA_Q", nullable = true)
    private String columnaQ;

    @Column(name = "COLUMNA_R", nullable = true)
    private String columnaR;

    @Column(name = "COLUMNA_S", nullable = true)
    private String columnaS;

    @Column(name = "COLUMNA_T", nullable = true)
    private String columnaT;

    @Column(name = "COLUMNA_U", nullable = true)
    private String columnaU;

    @Column(name = "COLUMNA_V", nullable = true)
    private String columnaV;

    @Column(name = "COLUMNA_W", nullable = true)
    private String columnaW;

    @Column(name = "COLUMNA_X", nullable = true)
    private String columnaX;

    @Column(name = "COLUMNA_Y", nullable = true)
    private String columnaY;

    @Column(name = "COLUMNA_Z", nullable = true)
    private String columnaZ;

    @Column(name = "COLUMNA_AA", nullable = true)
    private String columnaAA;

    @Column(name = "COLUMNA_AB", nullable = true)
    private String columnaAB;

    @Column(name = "COLUMNA_AC", nullable = true)
    private String columnaAC;

    @Column(name = "COLUMNA_AD", nullable = true)
    private String columnaAD;

    @Column(name = "COLUMNA_AE", nullable = true)
    private String columnaAE;

    @Column(name = "COLUMNA_AF", nullable = true)
    private String columnaAF;

    @Column(name = "COLUMNA_AG", nullable = true)
    private String columnaAG;

    @Column(name = "COLUMNA_AH", nullable = true)
    private String columnaAH;

    @Column(name = "COLUMNA_AI", nullable = true)
    private String columnaAI;

    @Column(name = "COLUMNA_AJ", nullable = true)
    private String columnaAJ;

    @Column(name = "COLUMNA_AK", nullable = true)
    private String columnaAK;

    @Column(name = "COLUMNA_AL", nullable = true)
    private String columnaAL;

    @Column(name = "COLUMNA_AM", nullable = true)
    private String columnaAM;

    @Column(name = "COLUMNA_AN", nullable = true)
    private String columnaAN;

    @Column(name = "COLUMNA_AO", nullable = true)
    private String columnaAO;

    @Column(name = "COLUMNA_AP", nullable = true)
    private String columnaAP;

    @Column(name = "COLUMNA_AQ", nullable = true)
    private String columnaAQ;

    @Column(name = "COLUMNA_AR", nullable = true)
    private String columnaAR;

    @Column(name = "COLUMNA_AS", nullable = true)
    private String columnaAS;

    @Column(name = "COLUMNA_AT", nullable = true)
    private String columnaAT;

    @Column(name = "COLUMNA_AU", nullable = true)
    private String columnaAU;

    @Column(name = "COLUMNA_AV", nullable = true)
    private String columnaAV;

    @Column(name = "COLUMNA_AW", nullable = true)
    private String columnaAW;

    @Column(name = "COLUMNA_AX", nullable = true)
    private String columnaAX;

    @Column(name = "COLUMNA_AY", nullable = true)
    private String columnaAY;

    @Column(name = "COLUMNA_AZ", nullable = true)
    private String columnaAZ;

    @Column(name = "COLUMNA_BA", nullable = true)
    private String columnaBA;

    @Column(name = "COLUMNA_BB", nullable = true)
    private String columnaBB;

    @Column(name = "COLUMNA_BC", nullable = true)
    private String columnaBC;

    @Column(name = "COLUMNA_BD", nullable = true)
    private String columnaBD;

    @Column(name = "COLUMNA_BE", nullable = true)
    private String columnaBE;

    @Column(name = "COLUMNA_BF", nullable = true)
    private String columnaBF;

    @Column(name = "COLUMNA_BG", nullable = true)
    private String columnaBG;

    @Column(name = "COLUMNA_BH", nullable = true)
    private String columnaBH;

    @Column(name = "COLUMNA_BI", nullable = true)
    private String columnaBI;

    @Column(name = "COLUMNA_BJ", nullable = true)
    private String columnaBJ;

    @Column(name = "COLUMNA_BK", nullable = true)
    private String columnaBK;

    @Column(name = "COLUMNA_BL", nullable = true)
    private String columnaBL;

    @Column(name = "COLUMNA_BM", nullable = true)
    private String columnaBM;

    @Column(name = "COLUMNA_BN", nullable = true)
    private String columnaBN;

    @Column(name = "COLUMNA_BO", nullable = true)
    private String columnaBO;

    @Column(name = "COLUMNA_BP", nullable = true)
    private String columnaBP;

    @Column(name = "COLUMNA_BQ", nullable = true)
    private String columnaBQ;

    @Column(name = "COLUMNA_BR", nullable = true)
    private String columnaBR;

    @Column(name = "COLUMNA_BS", nullable = true)
    private String columnaBS;

    @Column(name = "COLUMNA_BT", nullable = true)
    private String columnaBT;

    @Column(name = "COLUMNA_BU", nullable = true)
    private String columnaBU;

    @Column(name = "COLUMNA_BV", nullable = true)
    private String columnaBV;

    @Column(name = "COLUMNA_BW", nullable = true)
    private String columnaBW;

    @Column(name = "COLUMNA_BX", nullable = true)
    private String columnaBX;

    @Column(name = "COLUMNA_BY", nullable = true)
    private String columnaBY;

    @Column(name = "COLUMNA_BZ", nullable = true)
    private String columnaBZ;

    @Column(name = "COLUMNA_CA", nullable = true)
    private String columnaCA;

    @Column(name = "COLUMNA_CB", nullable = true)
    private String columnaCB;

    @Column(name = "COLUMNA_CC", nullable = true)
    private String columnaCC;

    @Column(name = "COLUMNA_CD", nullable = true)
    private String columnaCD;

    @Column(name = "COLUMNA_CE", nullable = true)
    private String columnaCE;

    @Column(name = "COLUMNA_CF", nullable = true)
    private String columnaCF;

    @Column(name = "COLUMNA_CG", nullable = true)
    private String columnaCG;

    @Column(name = "COLUMNA_CH", nullable = true)
    private String columnaCH;

    @Column(name = "COLUMNA_CI", nullable = true)
    private String columnaCI;

    @Column(name = "COLUMNA_CJ", nullable = true)
    private String columnaCJ;

    @Column(name = "COLUMNA_CK", nullable = true)
    private String columnaCK;

    @Column(name = "COLUMNA_CL", nullable = true)
    private String columnaCL;

    @Column(name = "COLUMNA_CM", nullable = true)
    private String columnaCM;

    @Column(name = "COLUMNA_CN", nullable = true)
    private String columnaCN;

    @Column(name = "COLUMNA_CO", nullable = true)
    private String columnaCO;

    @Column(name = "COLUMNA_CP", nullable = true)
    private String columnaCP;

    @Column(name = "COLUMNA_CQ", nullable = true)
    private String columnaCQ;

    @Column(name = "COLUMNA_CR", nullable = true)
    private String columnaCR;

    @Column(name = "COLUMNA_CS", nullable = true)
    private String columnaCS;

    @Column(name = "COLUMNA_CT", nullable = true)
    private String columnaCT;

    @Column(name = "COLUMNA_CU", nullable = true)
    private String columnaCU;

    @Column(name = "COLUMNA_CV", nullable = true)
    private String columnaCV;

    @Column(name = "COLUMNA_CW", nullable = true)
    private String columnaCW;

    @Column(name = "COLUMNA_CX", nullable = true)
    private String columnaCX;

    @Column(name = "COLUMNA_CY", nullable = true)
    private String columnaCY;

    @Column(name = "COLUMNA_CZ", nullable = true)
    private String columnaCZ;

    @Column(name = "NUM_PARAMETRO_01", nullable = true)
    private String numParametro01;

    @Column(name = "NUM_PARAMETRO_02", nullable = true)
    private String numParametro02;

    @Column(name = "NUM_PARAMETRO_03", nullable = true)
    private String numParametro03;

    @Column(name = "NUM_PARAMETRO_04", nullable = true)
    private String numParametro04;

    @Column(name = "NUM_PARAMETRO_05", nullable = true)
    private String numParametro05;

    @Column(name = "NUM_PARAMETRO_06", nullable = true)
    private String numParametro06;

    @Column(name = "NUM_PARAMETRO_07", nullable = true)
    private String numParametro07;

    @Column(name = "NUM_PARAMETRO_08", nullable = true)
    private String numParametro08;

    @Column(name = "NUM_PARAMETRO_09", nullable = true)
    private String numParametro09;

    @Column(name = "NUM_PARAMETRO_10", nullable = true)
    private String numParametro10;

    @Column(name = "ACCION_CARGA_DATOS", nullable = true)
    private String accionCargarDatos;

    @Column(name = "RFC_USUARIO_CARGA_DATOS", nullable = false)
    private String rfcUsuarioCargaDatos;

    @Column(name = "FECHA_CORTE_CARGA_DATOS", nullable = false)
    private Timestamp fechaCorteCargaDatos;

    @Column(name = "ID_ENTE_PUBLICO_CARGA_DATOS", nullable = true)
    private Long idEntePublicoCargaDatos;

    @Column(name = "ID_UNIDAD_ADMTVA_CARGA_DATOS", nullable = true)
    private Long idUnidadAdministrativaCargaDatos;

    @Column(name = "QUERY_GENERADO", nullable = true)
    private String queryGenerado;

    @Column(name = "NUM_ERRORES_VALIDACION", nullable = true)
    private Integer numErrorValidacion;

    @Column(name = "DESC_ERRORES_VALIDACION", nullable = true)
    private String infoErrorValidacion;

    @Column(name = "FECHA_ULTIMO_CAMBIO", nullable = false)
    private Timestamp fechaUltimoCambio;

    @Column(name = "CVE_ESTATUS", nullable = false)
    private String cveEstatus;

    @Column(name = "NUM_ERRORES_ACTUALIZACION", nullable = true)
    private Integer numErrorActualizacion;

    @Column(name = "ID_ENTE_PUBLICO", nullable = true)
    private Long idEntePublico;


}
