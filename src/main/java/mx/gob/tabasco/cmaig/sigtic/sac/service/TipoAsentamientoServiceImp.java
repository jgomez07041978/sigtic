package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.TipoAsentamiento;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.TipoAsentamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoAsentamientoServiceImp implements TipoAsentamientoService{
    @Autowired
    private TipoAsentamientoRepository tipoAsentamientoRepository;

    @Override
    public List<TipoAsentamiento> getTiposAsentamiento() {
        return tipoAsentamientoRepository.findAll(Sort.by("numeroOrdenTipoAsentamiento"));
    }
}
