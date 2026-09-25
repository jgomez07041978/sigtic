package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "CAT_TIPO_USUARIO_SERVICIOS_TIC")
public class CatTipoUsuarioServicio implements Serializable {
    @Id
    @Column (name = "ID_TIPO_USUARIO_SERVICIO_TIC")
    private Integer idTipoUsuario;

    @Column  (name = "DESC_TIPO_USUARIO_SERVICIO_TIC")
    private String tipoUsuarioServicio;

    @Column  (name = "NUM_ORDEN_TIPO_USUARIO_SERVTIC")
    private Integer numeroOrdenTipoServicio;

    @Column  (name = "CVE_ESTATUS")
    private String cveEstatus;
}