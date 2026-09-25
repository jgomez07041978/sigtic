package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoImpactoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatTipoImpactoServiciosTicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CatTipoImpactoServiciosTicImp implements CatTipoImpactoServiciosTicService{
    @Autowired
    private CatTipoImpactoServiciosTicRepository catTipoImpactoServiciosTicRepository;

    @Override
    public List<CatTipoImpactoServiciosTic> getCatTipoImpactoServiciosTic(){
        return catTipoImpactoServiciosTicRepository.findAllByCveEstatus("VIG");
    }
}
