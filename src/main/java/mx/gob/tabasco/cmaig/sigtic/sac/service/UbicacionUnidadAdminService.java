package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.UbicacionUnidadAdmin;

import java.util.List;

public interface UbicacionUnidadAdminService {

    List<UbicacionUnidadAdmin> listaUbicacionesUnidadPorDomicilio(Long idDomicilioEntePublico);
    List<UbicacionUnidadAdmin> listaUbicacionesUnidadPorDomicilioVigentes(Long idDomicilioEntePublico);

    UbicacionUnidadAdmin obtenerUbicacionId(Long idUbicacion);

    UbicacionUnidadAdmin saveUbicacion(UbicacionUnidadAdmin ubicacionUnidadAdmin);

    UbicacionUnidadAdmin deleteUbicacion(UbicacionUnidadAdmin ubicacionUnidadAdmin);

}
