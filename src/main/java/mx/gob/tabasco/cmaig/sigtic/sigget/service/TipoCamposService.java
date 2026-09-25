package mx.gob.tabasco.cmaig.sigtic.sigget.service;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.TipoCampo;

import java.util.List;

public interface TipoCamposService {
    List<TipoCampo> getTipoCamposVigentes();

    TipoCampo findTipoCamposById(String cveTipoCampo);


}
