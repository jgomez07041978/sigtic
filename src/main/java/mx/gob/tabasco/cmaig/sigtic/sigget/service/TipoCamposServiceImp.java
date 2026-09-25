package mx.gob.tabasco.cmaig.sigtic.sigget.service;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.TipoCampo;
import mx.gob.tabasco.cmaig.sigtic.sigget.repository.TipoCamposRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoCamposServiceImp implements TipoCamposService{

    @Autowired
    private TipoCamposRepository tipoCamposRepository;

    @Override
    public List<TipoCampo> getTipoCamposVigentes() {
        return tipoCamposRepository.findByCveEstatus("VIG");
    }

    @Override
    public TipoCampo findTipoCamposById(String cveTipoCampo) {
        return tipoCamposRepository.findById(cveTipoCampo).get();
    }
}
