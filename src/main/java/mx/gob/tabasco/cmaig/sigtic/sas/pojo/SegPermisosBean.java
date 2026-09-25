package mx.gob.tabasco.cmaig.sigtic.sas.pojo;

import lombok.Data;

import java.io.Serializable;


@Data
public class SegPermisosBean implements Serializable {

    private Long id;
    private Long idEntePublico;
    private Long iduUnidadAdministrativa;
    private String idSeguridadSistema;
    private String rfc;
    private String curp;

}
