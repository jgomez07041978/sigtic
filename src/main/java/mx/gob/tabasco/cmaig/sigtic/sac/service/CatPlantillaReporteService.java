package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatPlantillaReporte;

import java.util.List;

public interface CatPlantillaReporteService {

    List<CatPlantillaReporte> getListaPlantillaEnteSubtipoServicio(Long idEntePublico, Long idSubtipoServicio);

    List<CatPlantillaReporte> getListaPlantillaEnteSubtipoServicio(Long idCartaResponsiva);

    CatPlantillaReporte getPlantillaReporteId(Long idPlantillaReporte, Long idCartaResponsiva);
}
