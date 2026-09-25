package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoServiciosTic;

import java.util.List;

public interface CatTipoServiciosTicService {
    CatTipoServiciosTic getCatTipoServiciosTic(Long idTipoServicioTic);

    List<CatTipoServiciosTic> getCatTipoServiciosTicAll();

    List<CatTipoServiciosTic> getCatTipoServiciosTicAllCr();

    CatTipoServiciosTic guardarTipoServicio(CatTipoServiciosTic catTipoServiciosTic);

    CatTipoServiciosTic deleteTipoServicio(CatTipoServiciosTic catTipoServiciosTic);

}
