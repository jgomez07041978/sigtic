package mx.gob.tabasco.cmaig.sigtic.sigget.service;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.TipoCampoMascara;

import java.util.List;

public interface TipoCampoMascaraService {

    List<TipoCampoMascara> getTipoCamposMascaraPorTipoCampo(String cveTipoCampo);

}
