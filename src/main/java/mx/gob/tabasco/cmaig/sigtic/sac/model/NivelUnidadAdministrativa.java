package mx.gob.tabasco.cmaig.sigtic.sac.model;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="CAT_NIVELES_UNIDADES_ADMTVAS")
public class NivelUnidadAdministrativa implements Serializable {

    @Id
    @Column(name = "ID_NIVEL_UNIDAD_ADMINISTRATIVA")
    private Integer idNivelUnidadAdministrativa;

    @Column(name = "NUM_NIVEL_UNIDAD_ADMTVA")
    private Short numeroNivelUnidadAdministrativa;

    @Column(name = "DESC_NIVEL_UNIDAD_ADMTVA_ABREV")
    private String nombreNivelUnidadAdministrativaAbrev;

    @Column(name = "DESC_NIVEL_UNIDAD_ADMTVA")
    private String nombreNivelUnidadAdministrativa;

    @Column(name = "NUM_ORDEN_NIVEL_UNIDAD_ADMTVA")
    private Short numeroOrdenUnidadAdministrativa;

    @Column(name = "CVE_ESTATUS")
    private String cveEstatus;

    @Column (name = "CVE_USUARIO_MTTO")
    private String cveUsuarioMtto;

    @Column (name = "FECHA_ULTIMO_CAMBIO")
    private Timestamp fechaUltimoCambio;

}
