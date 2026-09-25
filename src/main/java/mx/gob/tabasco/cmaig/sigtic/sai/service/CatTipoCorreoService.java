package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoCorreoElectronico;

import java.util.List;

public interface CatTipoCorreoService {


    List<CatTipoCorreoElectronico> getListaTipoCorreo();

    CatTipoCorreoElectronico getCatTipoCorreoElectronicoCve(String cveTipoCorreoElectronico);

    CatTipoCorreoElectronico guardarTipoCorreo(CatTipoCorreoElectronico catTipoCorreoElectronico);

    CatTipoCorreoElectronico deleteTipoCorreoElectronico(CatTipoCorreoElectronico catTipoCorreoElectronico);
}