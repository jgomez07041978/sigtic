package mx.gob.tabasco.cmaig.sigtic.sai.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ServicioEspecialId implements Serializable {

    @Column(name = "NUM_CUENTA_MAESTRA")
    private String numCuentaMaestra;

    @Column(name = "NO_LOTE")
    private Integer noLote;

    //  Constructor vacío
    public ServicioEspecialId() {}

    // argumentos (Service para buscar)
    public ServicioEspecialId(String numCuentaMaestra, Integer noLote) {
        this.numCuentaMaestra = numCuentaMaestra;
        this.noLote = noLote;
    }

    public String getNumCuentaMaestra() {
        return numCuentaMaestra;
    }

    public void setNumCuentaMaestra(String numCuentaMaestra) {
        this.numCuentaMaestra = numCuentaMaestra;
    }

    public Integer getNoLote() {
        return noLote;
    }

    public void setNoLote(Integer noLote) {
        this.noLote = noLote;
    }
}