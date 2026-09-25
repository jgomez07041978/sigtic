package mx.gob.tabasco.cmaig.sigtic.sac.bean;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class DomiciliosBean {
    private Integer idDomicilio;
    private String domicilio;
    private String cveEstatus;
    private String cveEstatusDom;
    private Integer start;
    private Integer length;
    private Integer total;
    private Integer totalUbicaciones;
    private Integer draw;
    private String domicilioBuscar;
    private Integer idEntePublico;
    private Integer idEnteAsignadoEdit;
    private Integer idEnteAsignadoEditUbi;
    private Integer idEntePublicoEditarDom;
    private Integer idUnidadAdmtva;
    private String idDependiente;
    private String descripcion;
    private String mensaje;
    private String descEntePublico;

    private Integer idTipoVialidadEntePublico;
    private String calleEntePublico;
    private String numExteriorEntePublico;
    private String numInteriorEntePublico;
    private Integer idTipoAsentamientoEntePub;
    private String descDomicilioEntePublico;
    private Integer idCodigoPostalLocalidadEP;
    private String codigoPostalLocalidadEP;
    private Integer idLocalidadEntePublico;
    private Integer idMunicipioEntePublico;
    private Integer idEntidadFederativaEntePub;
    private String referenciaEntreCalle;
    private String referenciaCallePosterior;
    private String referenciaDomicilioEntePub;
    private String numTelefonoEntePublico;
    private String numExtensionEntePublico;
    private String descDomicilioEntePublicoAbreviado;
    private Integer numOrdenDomicilio;
    private Integer idColorDomicilio;
    private Integer idImagenDomicilio;
    private BigDecimal latitudDomicilio;
    private BigDecimal longitudDomicilio;
    private String coloniaEntePublico;
    private String descEnteAbreviada;
}
