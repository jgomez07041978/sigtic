package mx.gob.tabasco.cmaig.sigtic.sai.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ServicioEspecialReporteDTO {

    private String dependencia;
    private String unidadAdministrativa;
    private String cuentaMaestra;
    private Integer lote;
    private String periodo;
    private String partida;
    private String articulo;
    private Integer cantidad;
    private BigDecimal precioConIva;
    private BigDecimal total;
    private String estatus;
}