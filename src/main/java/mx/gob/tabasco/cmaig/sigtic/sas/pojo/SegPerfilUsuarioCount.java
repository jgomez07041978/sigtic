package mx.gob.tabasco.cmaig.sigtic.sas.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SegPerfilUsuarioCount implements Serializable {

    private Integer idPerfil;
    private Long numUsuarios;

    public SegPerfilUsuarioCount(Integer idPerfil, Long numUsuarios) {
        this.idPerfil = idPerfil;
        this.numUsuarios = numUsuarios;
    }
}
