package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoSistema;

import java.util.List;

public interface CatTipoSistemaService {

    List<CatTipoSistema> getListaTipoSistema();

    CatTipoSistema guardarTipoSistemas(CatTipoSistema catTipoSistema);

    CatTipoSistema getTipoSistemaCve(String cveTipoSistema);

    CatTipoSistema deleteTipoSistema(CatTipoSistema catTipoSistema);

}
