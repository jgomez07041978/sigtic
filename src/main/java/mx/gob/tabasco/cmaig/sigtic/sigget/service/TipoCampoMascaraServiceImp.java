package mx.gob.tabasco.cmaig.sigtic.sigget.service;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.TipoCampoMascara;
import mx.gob.tabasco.cmaig.sigtic.sigget.repository.TipoCampoMascaraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoCampoMascaraServiceImp implements TipoCampoMascaraService{

    @Autowired
    private TipoCampoMascaraRepository tipoCampoMascaraRepository;

    @Override
    public List<TipoCampoMascara> getTipoCamposMascaraPorTipoCampo(String cveTipoCampo) {
        return tipoCampoMascaraRepository.findAllByCveTipoCampoAndCveEstatus(cveTipoCampo, "VIG");
    }
}
