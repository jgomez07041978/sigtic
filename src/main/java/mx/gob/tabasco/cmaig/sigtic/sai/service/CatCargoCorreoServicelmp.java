package mx.gob.tabasco.cmaig.sigtic.sai.service;


import mx.gob.tabasco.cmaig.sigtic.sai.model.CatCargosUsuarios;
import mx.gob.tabasco.cmaig.sigtic.sai.model.EntePublico;

import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatCargosUsuariosRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.EntePublicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatCargoCorreoServicelmp implements CatCargoCorreoService {


    @Autowired
    private CatCargosUsuariosRepository catCargosUsuariosRepository;

    @Autowired
    private EntePublicoRepository entePublicoRepository;

    @Override
    public List<CatCargosUsuarios> obtenCargoCorreo(Long idEnte) {

        EntePublico entePublico = entePublicoRepository.findById(idEnte).get();

        List<CatCargosUsuarios> lista = catCargosUsuariosRepository.findByEntePublicoAndCveEstatusOrderByNumeroOrdenCargo(entePublico,"VIG");
        return lista;
    }

}
