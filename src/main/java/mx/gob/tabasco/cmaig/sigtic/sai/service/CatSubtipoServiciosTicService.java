package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServiciosTic;

import java.util.List;

public interface CatSubtipoServiciosTicService {
    List<CatSubtipoServiciosTic> getCatSubtipoServiciosTic(Long idTipoServicio);

    List<CatSubtipoServiciosTic> getCatSubtipoServiciosTicCr(Long idTipoServicio);

    /*List<CatSubtipoServiciosTic> getCatSubtipoServiciosTics();*/

    List<CatSubtipoServiciosTic> getCatSubtipoServiciosTicAll();

    CatSubtipoServiciosTic guardarSubtipoServiciosTic(CatSubtipoServiciosTic catSubtipoServiciosTic);

    CatSubtipoServiciosTic getSubtipoServiciosTic(Long idSubtipoServicioTic);

    CatSubtipoServiciosTic deleteSubTipoServicio(CatSubtipoServiciosTic catSubtipoServiciosTic);

}
