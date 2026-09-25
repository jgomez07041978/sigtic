package mx.gob.tabasco.cmaig.sigtic.sac.service;


import mx.gob.tabasco.cmaig.sigtic.sac.model.Municipio;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.MunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MunicipioServiceImp implements MunicipioService{

    @Autowired
    private MunicipioRepository municipioRepository;

    @Override
    public List<Municipio> getMunicipios(Integer idEntidadFederativa) {
        return municipioRepository.findMunicipioByIdEntidadFederativaOrderByNumeroOrdenMunipio(idEntidadFederativa);
    }
}
