package mx.gob.tabasco.cmaig.sigtic.sai.service;


import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoUsuarioServicio;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatTipoUsuarioServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatTipoUsuarioServicioServiceImp implements CatTipoUsuarioServicioService{

    @Autowired
    private CatTipoUsuarioServicioRepository catTipoUsuarioServicioRepository;

    @Override
    public List<CatTipoUsuarioServicio> getTipoUsuarioServicio() {
        return catTipoUsuarioServicioRepository.findAllByCveEstatusOrderByNumeroOrdenTipoServicio("VIG");
    }
}
