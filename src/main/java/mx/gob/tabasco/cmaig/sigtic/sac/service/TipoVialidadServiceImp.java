package mx.gob.tabasco.cmaig.sigtic.sac.service;


import mx.gob.tabasco.cmaig.sigtic.sac.model.TipoVialidad;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.TipoVialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoVialidadServiceImp implements TipoVialidadService{

    @Autowired
    private TipoVialidadRepository tipoVialidadRepository;

    @Override
    public List<TipoVialidad> getTipoVialidad() {
        return tipoVialidadRepository.findAll(Sort.by("numeroOrdenVialidad"));
    }
}
