package mx.gob.tabasco.cmaig.sigtic.sas.service;

import mx.gob.tabasco.cmaig.sigtic.sas.model.TokenMovil;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.TokenMovilRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacionMovilServiceImp implements NotificacionMovilService{


    private final TokenMovilRepository tokenMovilRepository;

    public NotificacionMovilServiceImp(TokenMovilRepository tokenMovilRepository) {
        this.tokenMovilRepository = tokenMovilRepository;
    }

    @Override
    public List<TokenMovil> getTokenList() {
        return (List<TokenMovil>) tokenMovilRepository.findAll();
    }

    @Override
    public String getToken(String token) {

        Optional<TokenMovil> tokenMovil = tokenMovilRepository.findByTokenMovil(token);

        if (tokenMovil.isPresent()) { return tokenMovil.get().getTokenMovil(); }
        else { return null; }

    }

    @Override
    public String save(TokenMovil token) {

        Optional<TokenMovil> tokenMovil = tokenMovilRepository.findByTokenMovil(token.getTokenMovil());

        if (tokenMovil.isPresent())
            return "El token ya existe";

        token.setFechaRegistro(new Timestamp(new Date().getTime()));

        TokenMovil tokenMovil1= tokenMovilRepository.save(token);

        if (tokenMovil1==null)
            return "Problema al registrar el token";
        else return "token registrado correctamente";
    }
}
