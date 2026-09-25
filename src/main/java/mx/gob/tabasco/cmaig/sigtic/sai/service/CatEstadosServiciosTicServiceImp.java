package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatEstadosServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatEstadosServiciosTicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatEstadosServiciosTicServiceImp implements CatEstadosServiciosTicService{

    @Autowired
    private CatEstadosServiciosTicRepository catEstadosServiciosTicRepository;

    @Override
    public List<CatEstadosServiciosTic> getEstadosServiciosTic(){
        return catEstadosServiciosTicRepository.findAllByCveEstatus("VIG");
    }
}
