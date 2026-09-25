package mx.gob.tabasco.cmaig.sigtic.sigget.service;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.Campo;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Campos;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.CamposPK;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Plantilla;

import java.util.List;

public interface CamposService {
    List<Campos> getCamposPorPlantilla(Plantilla plantilla, String estatus);

    Campos getCamposId(CamposPK campo);

    Campos saveCampos(Campos campo);

    Campos updateCampos(Campos campo);

    void deleteCampo(CamposPK camposPK);

    List<Campo> getCamposDisponibles(List<String> listaAsignada);
}
