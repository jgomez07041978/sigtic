package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosFirma;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosTic;

import java.util.List;

public interface CatSubtipoServiciosFirmaService {

    List<CatSubtipoServiciosFirma> getSubtipoServicioFirmas(CatSubtipoServiciosTic catSubtipoServiciosTic);

    CatSubtipoServiciosFirma guardarSubtipoServicioFirma(CatSubtipoServiciosFirma catSubtipoServiciosFirma);

    void eliminarSubtipoServicioFirma(CatSubtipoServiciosFirma catSubtipoServiciosFirma);

    CatSubtipoServiciosFirma getSubtipoServicioFirmaId(Long idSubtipoServicioFirma);

    CatSubtipoServiciosFirma getSubtipoServicioFirmaEnteId(Long idEnte,Long idSubtipoServicio);

}
