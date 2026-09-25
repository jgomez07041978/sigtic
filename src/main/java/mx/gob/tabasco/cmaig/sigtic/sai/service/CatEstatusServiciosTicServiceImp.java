package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatEstatusServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatEstatusServiciosTicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatEstatusServiciosTicServiceImp implements CatEstatusServiciosTicService{

    @Autowired
    private CatEstatusServiciosTicRepository catEstatusServiciosTicRepository;

    @Override
    public List<CatEstatusServiciosTic> getEstatusServiciosTic(){
        return catEstatusServiciosTicRepository.findAllByCveEstatus("VIG");
    }
}
