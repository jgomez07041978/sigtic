package mx.gob.tabasco.cmaig.sigtic.sai.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class NumTelefonoCuentaMaestraId implements Serializable {

    @Column(name = "NUM_TELEFONO")
    private String numTelefono;

    @Column(name = "NUM_CUENTA_MAESTRA")
    private String numCuentaMaestra;

    public NumTelefonoCuentaMaestraId() {
    }

    public NumTelefonoCuentaMaestraId(String numTelefono, String numCuentaMaestra) {
        this.numTelefono = numTelefono;
        this.numCuentaMaestra = numCuentaMaestra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NumTelefonoCuentaMaestraId)) return false;
        NumTelefonoCuentaMaestraId that = (NumTelefonoCuentaMaestraId) o;
        return Objects.equals(numTelefono, that.numTelefono)
                && Objects.equals(numCuentaMaestra, that.numCuentaMaestra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numTelefono, numCuentaMaestra);
    }
}
