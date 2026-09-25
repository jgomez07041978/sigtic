package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatServidorCorreoElectronico;
import mx.gob.tabasco.cmaig.sigtic.sai.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatServidorCorreoElectronicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.EntePublicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatServidorCorreoElectronicoServiceImp implements CatServidorCorreoElectronicoService {


    @Autowired
    private CatServidorCorreoElectronicoRepository catServidorCorreoElectronicoRepository;

    @Autowired
    EntePublicoRepository entePublicoRepository;

    @Override
    public List<CatServidorCorreoElectronico> obtenServidorCorreoElectronico(Long idEntePublico) {

        EntePublico entePublico1= entePublicoRepository.findById(idEntePublico).get();

        List<CatServidorCorreoElectronico> lista = catServidorCorreoElectronicoRepository.findAllByEntePublicoAndCveEstatusOrderByNumeroOrdenServidorCorreoElectronico(entePublico1,"VIG");
        return lista;
    }

    @Override
    public List<CatServidorCorreoElectronico> obtenServidorCorreoElectronicoAll() {
        List<CatServidorCorreoElectronico> lista = catServidorCorreoElectronicoRepository.findAllByCveEstatusOrderByNumeroOrdenServidorCorreoElectronico("VIG");

        return lista;
    }
}
