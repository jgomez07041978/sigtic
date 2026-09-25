package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.PuestoUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.EntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.PuestoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PuestoUsuarioServiceImp implements PuestoUsuarioService{

    @Autowired
    private PuestoUsuarioRepository puestoUsuarioRepository;

    @Autowired
    private EntePublicoRepository entePublicoRepository;

    @Override
    public List<PuestoUsuario> listaPuestosPorEntePublico(Long idEntePublico) {

        EntePublico entePublico = entePublicoRepository.findById(idEntePublico).get();

        return puestoUsuarioRepository.findAllByEntePublicoOrderByNombrePuestoUsuario(entePublico);
    }
}
