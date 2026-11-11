package mx.gob.tabasco.cmaig.sigtic.sai.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class ServicioEspecialFiltro {

    private Long idEntePublico;
    private Long idUnidadAdministrativa;
    private String numCuentaMaestra;
    private String cveTipoConexion;
    private String cvePartida;
    private String codigoEbs;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodoInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodoFin;

    private Boolean isVigente;
    private Boolean isHistorico;

    public ServicioEspecialFiltro() {
    }

    public Long getIdEntePublico() {
        return this.idEntePublico;
    }

    public Long getIdUnidadAdministrativa() {
        return this.idUnidadAdministrativa;
    }

    public String getNumCuentaMaestra() {
        return this.numCuentaMaestra;
    }

    public String getCveTipoConexion() {
        return this.cveTipoConexion;
    }

    public String getCvePartida() {
        return this.cvePartida;
    }

    public String getCodigoEbs() {
        return this.codigoEbs;
    }

    public LocalDate getPeriodoInicio() {
        return this.periodoInicio;
    }

    public LocalDate getPeriodoFin() {
        return this.periodoFin;
    }

    public Boolean getIsVigente() {
        return this.isVigente;
    }

    public Boolean getIsHistorico() {
        return this.isHistorico;
    }

    public void setIdEntePublico(Long idEntePublico) {
        this.idEntePublico = idEntePublico;
    }

    public void setIdUnidadAdministrativa(Long idUnidadAdministrativa) {
        this.idUnidadAdministrativa = idUnidadAdministrativa;
    }

    public void setNumCuentaMaestra(String numCuentaMaestra) {
        this.numCuentaMaestra = numCuentaMaestra;
    }

    public void setCveTipoConexion(String cveTipoConexion) {
        this.cveTipoConexion = cveTipoConexion;
    }

    public void setCvePartida(String cvePartida) {
        this.cvePartida = cvePartida;
    }

    public void setCodigoEbs(String codigoEbs) {
        this.codigoEbs = codigoEbs;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public void setPeriodoInicio(LocalDate periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public void setPeriodoFin(LocalDate periodoFin) {
        this.periodoFin = periodoFin;
    }

    public void setIsVigente(Boolean isVigente) {
        this.isVigente = isVigente;
    }

    public void setIsHistorico(Boolean isHistorico) {
        this.isHistorico = isHistorico;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ServicioEspecialFiltro)) return false;
        final ServicioEspecialFiltro other = (ServicioEspecialFiltro) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$idEntePublico = this.getIdEntePublico();
        final Object other$idEntePublico = other.getIdEntePublico();
        if (this$idEntePublico == null ? other$idEntePublico != null : !this$idEntePublico.equals(other$idEntePublico))
            return false;
        final Object this$idUnidadAdministrativa = this.getIdUnidadAdministrativa();
        final Object other$idUnidadAdministrativa = other.getIdUnidadAdministrativa();
        if (this$idUnidadAdministrativa == null ? other$idUnidadAdministrativa != null : !this$idUnidadAdministrativa.equals(other$idUnidadAdministrativa))
            return false;
        final Object this$numCuentaMaestra = this.getNumCuentaMaestra();
        final Object other$numCuentaMaestra = other.getNumCuentaMaestra();
        if (this$numCuentaMaestra == null ? other$numCuentaMaestra != null : !this$numCuentaMaestra.equals(other$numCuentaMaestra))
            return false;
        final Object this$cveTipoConexion = this.getCveTipoConexion();
        final Object other$cveTipoConexion = other.getCveTipoConexion();
        if (this$cveTipoConexion == null ? other$cveTipoConexion != null : !this$cveTipoConexion.equals(other$cveTipoConexion))
            return false;
        final Object this$cvePartida = this.getCvePartida();
        final Object other$cvePartida = other.getCvePartida();
        if (this$cvePartida == null ? other$cvePartida != null : !this$cvePartida.equals(other$cvePartida))
            return false;
        final Object this$codigoEbs = this.getCodigoEbs();
        final Object other$codigoEbs = other.getCodigoEbs();
        if (this$codigoEbs == null ? other$codigoEbs != null : !this$codigoEbs.equals(other$codigoEbs)) return false;
        final Object this$periodoInicio = this.getPeriodoInicio();
        final Object other$periodoInicio = other.getPeriodoInicio();
        if (this$periodoInicio == null ? other$periodoInicio != null : !this$periodoInicio.equals(other$periodoInicio))
            return false;
        final Object this$periodoFin = this.getPeriodoFin();
        final Object other$periodoFin = other.getPeriodoFin();
        if (this$periodoFin == null ? other$periodoFin != null : !this$periodoFin.equals(other$periodoFin))
            return false;
        final Object this$isVigente = this.getIsVigente();
        final Object other$isVigente = other.getIsVigente();
        if (this$isVigente == null ? other$isVigente != null : !this$isVigente.equals(other$isVigente)) return false;
        final Object this$isHistorico = this.getIsHistorico();
        final Object other$isHistorico = other.getIsHistorico();
        if (this$isHistorico == null ? other$isHistorico != null : !this$isHistorico.equals(other$isHistorico))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ServicioEspecialFiltro;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $idEntePublico = this.getIdEntePublico();
        result = result * PRIME + ($idEntePublico == null ? 43 : $idEntePublico.hashCode());
        final Object $idUnidadAdministrativa = this.getIdUnidadAdministrativa();
        result = result * PRIME + ($idUnidadAdministrativa == null ? 43 : $idUnidadAdministrativa.hashCode());
        final Object $numCuentaMaestra = this.getNumCuentaMaestra();
        result = result * PRIME + ($numCuentaMaestra == null ? 43 : $numCuentaMaestra.hashCode());
        final Object $cveTipoConexion = this.getCveTipoConexion();
        result = result * PRIME + ($cveTipoConexion == null ? 43 : $cveTipoConexion.hashCode());
        final Object $cvePartida = this.getCvePartida();
        result = result * PRIME + ($cvePartida == null ? 43 : $cvePartida.hashCode());
        final Object $codigoEbs = this.getCodigoEbs();
        result = result * PRIME + ($codigoEbs == null ? 43 : $codigoEbs.hashCode());
        final Object $periodoInicio = this.getPeriodoInicio();
        result = result * PRIME + ($periodoInicio == null ? 43 : $periodoInicio.hashCode());
        final Object $periodoFin = this.getPeriodoFin();
        result = result * PRIME + ($periodoFin == null ? 43 : $periodoFin.hashCode());
        final Object $isVigente = this.getIsVigente();
        result = result * PRIME + ($isVigente == null ? 43 : $isVigente.hashCode());
        final Object $isHistorico = this.getIsHistorico();
        result = result * PRIME + ($isHistorico == null ? 43 : $isHistorico.hashCode());
        return result;
    }

    public String toString() {
        return "ServicioEspecialFiltro(idEntePublico=" + this.getIdEntePublico() + ", idUnidadAdministrativa=" + this.getIdUnidadAdministrativa() + ", numCuentaMaestra=" + this.getNumCuentaMaestra() + ", cveTipoConexion=" + this.getCveTipoConexion() + ", cvePartida=" + this.getCvePartida() + ", codigoEbs=" + this.getCodigoEbs() + ", periodoInicio=" + this.getPeriodoInicio() + ", periodoFin=" + this.getPeriodoFin() + ", isVigente=" + this.getIsVigente() + ", isHistorico=" + this.getIsHistorico() + ")";
    }
}