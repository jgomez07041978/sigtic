package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatEstatusSistema;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatEstatusSistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatEstatusSistemaServiceImp implements CatEstatusSistemaService {

    @Autowired
    private CatEstatusSistemaRepository catEstatusSistemaRepository;

    @Override
    public List<CatEstatusSistema> getEstatusSistema() {
        return catEstatusSistemaRepository.findAllByCveEstatus("VIG");
    }
}
