package mx.gob.tabasco.cmaig.sigtic.sai.service;


import mx.gob.tabasco.cmaig.sigtic.sai.model.CatCategoria;
import mx.gob.tabasco.cmaig.sigtic.sai.model.EntePublico;

import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatCargosUsuariosRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatCategoriaRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.EntePublicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CatCategoriaCorreoServicelmp implements CatCategoriaCorreoService {


    @Autowired
    private CatCategoriaRepository catCategoriaRepository;

    @Autowired
    private EntePublicoRepository entePublicoRepository;

    @Override
    public List<CatCategoria> obtenCategoriaCorreo(Long idEnte) {

        EntePublico entePublico = entePublicoRepository.findById(idEnte).get();

        List<CatCategoria> lista = catCategoriaRepository.findByEntePublicoAndCveEstatusOrderByNumeroOrdenCategoria(entePublico,"VIG");
        return lista;
    }

}
