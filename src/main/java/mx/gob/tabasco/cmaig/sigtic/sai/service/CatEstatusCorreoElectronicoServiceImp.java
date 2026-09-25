package mx.gob.tabasco.cmaig.sigtic.sai.service;


import mx.gob.tabasco.cmaig.sigtic.sai.model.CatEstatusCorreoElectronico;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatEstatusCorreoElectronicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatEstatusCorreoElectronicoServiceImp implements CatEstatusCorreoElectronicoService{

    @Autowired
    private CatEstatusCorreoElectronicoRepository catEstatusCorreoElectronicoRepository;

    @Override
    public List<CatEstatusCorreoElectronico> getEstatusCorreoElectronico() {
        return catEstatusCorreoElectronicoRepository.findAllByOrderByNumeroOrden();
    }
}
