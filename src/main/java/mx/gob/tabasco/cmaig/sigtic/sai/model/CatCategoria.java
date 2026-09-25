package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="SIGGET_SAC_CATALOGOS.CAT_CATEGORIAS_USUARIOS")
public class CatCategoria implements Serializable {

    @Id
    @Column(name = "ID_CATEGORIA")
    private Long idCategoriaUsuario;


    @Column(name = "DESC_CATEGORIA_USUARIO")
    private String  nombreCategoriaUsuario;

    @ManyToOne
    @JoinColumn(name="ID_ENTE_PUBLICO_CATEGORIA_USUA")
    private EntePublico entePublico;


    @Column(name = "NUM_ORDEN_CATEGORIA_USUARIO")
    private Integer numeroOrdenCategoria;

    @Column(name = "CVE_ESTATUS")
    private String  cveEstatus;

}
