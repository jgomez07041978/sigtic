package mx.gob.tabasco.cmaig.sigtic.sas.service;

import mx.gob.tabasco.cmaig.sigtic.sas.model.TokenMovil;

import java.util.List;

public interface NotificacionMovilService {

    List<TokenMovil> getTokenList();

    String getToken(String token);

    String save(TokenMovil token);
}
