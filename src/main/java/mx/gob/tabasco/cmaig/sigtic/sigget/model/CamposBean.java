package mx.gob.tabasco.cmaig.sigtic.sigget.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CamposBean implements Serializable {

    private Long idPlantilla;
    private Integer numCampo;
    private String desCampo;
    private String desCampoAbr;
    private String ubicacionColumna;
    private String etiquetaColumna;
    private String campoBaseDatos;
    private String cveTipoCampo;
    private String obligatorio;
    private Integer longitud;
    private Integer longitudDecimal;
    private String mascara;
    private Integer pkNumPosicionCampo;
    private String pkFkTabla;
    private String pkFkColumna;
    private String omitirTextValiacion;
    private Integer fkNumPosicionColumna;
    private String fkTabla;
    private String fkColumna;
    private Integer numOrdenCampo;
    private String ipPc;
    private String nombrePc;
    private String cveUsuarioSo;
    private String cveUsuarioMtto;
    private String nombreAplicacion;
    private Timestamp fechaRegistro;
    private Timestamp fechaCambio;
    private String cveEstatus;
}
