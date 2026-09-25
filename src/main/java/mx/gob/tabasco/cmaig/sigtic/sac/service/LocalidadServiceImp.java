package mx.gob.tabasco.cmaig.sigtic.sac.service;


import mx.gob.tabasco.cmaig.sigtic.sac.model.Localidad;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalidadServiceImp implements LocalidadService{

    @Autowired
    private LocalidadRepository localidadRepository;

    @Override
    public List<Localidad> getLocalidades(Integer idEntidadFEderativa, Integer idMunicipio) {
        return localidadRepository.findLocalidadByIdEntidadFederativaAndIdMunicipioOrderByNumeroOrdenLocalidad(idEntidadFEderativa,idMunicipio);
    }

}
