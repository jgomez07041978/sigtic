package mx.gob.tabasco.cmaig.sigtic.sac.service;


import mx.gob.tabasco.cmaig.sigtic.sac.model.EntidadFederativa;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.EntidadFederativaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntidadFederativaServiceImp implements EntidadFederativaService{

    @Autowired
    private EntidadFederativaRepository entidadFederativaRepository;

    @Override
    public List<EntidadFederativa> getEntdadesFederativas() {
        return entidadFederativaRepository.findAll(Sort.by("numeroOrdenEntidadFederativa"));
    }
}
