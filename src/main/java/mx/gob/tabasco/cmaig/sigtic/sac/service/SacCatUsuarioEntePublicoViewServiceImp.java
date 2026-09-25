package mx.gob.tabasco.cmaig.sigtic.sac.service;


import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUnidadAdministrativaSql;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.SacCatUsuarioEntePublicoView;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SacCatUsuarioEntePublicoViewServiceImp implements SacCatUsuarioEntePublicoViewService{

    @Autowired
    private SacCatUsuarioEntePublicoViewRepository sacCatUsuarioEntePublicoViewRepository;

    @Autowired
    private CatUnidadAdministrativaSqlRepository catUnidadAdministrativaSqlRepository;

    @Autowired
    private CatUsuarioEntePublicoRepository catUsuarioEntePublicoRepository;

    @Override
    public Page<SacCatUsuarioEntePublicoView> getSearchDirectorio(SacCatUsuarioEntePublicoView sacUsu) {

        List<CatUnidadAdministrativaSql> listaUnidadesAdministrativas=null;


        if (sacUsu.getIdUnidadAdministrativa()!=null)
            listaUnidadesAdministrativas= catUnidadAdministrativaSqlRepository.getUnidadesAdministrativasArbolSigtic(sacUsu.getIdUnidadAdministrativa());

        List<Long> listaIdUnidad = null;

        if (listaUnidadesAdministrativas!=null)
            listaIdUnidad=listaUnidadesAdministrativas.stream().map(CatUnidadAdministrativaSql::getIdUnidadAdministrativa).collect(Collectors.toList());



        SacCatUsuarioEntePublicoViewSpecification specSacUSuario = new SacCatUsuarioEntePublicoViewSpecification(sacUsu.getIdEntePublico(), listaIdUnidad, sacUsu.getNombre(), sacUsu.getTelefonoInstitucinal(), sacUsu.getNumeroExtension());
        Pageable pageable = PageRequest.of(sacUsu.getPagina(), sacUsu.getSize(), Sort.Direction.ASC, "nombreCompletoMayusculas");
        return sacCatUsuarioEntePublicoViewRepository.findAll(specSacUSuario, pageable);

        //return sacCatUsuarioEntePublicoViewRepository.findAll(pageable);
    }

    @Override
    public Page<CatUsuarioEntePublico> getSearchUser(SacCatUsuarioEntePublicoView sacUsu) {

        CatUsuarioEntePublicoFilterSpecification specSacUSuario = new CatUsuarioEntePublicoFilterSpecification(sacUsu.getRfcUsuario(),sacUsu.getCurpUsuario(), sacUsu.getNombre(), sacUsu.getSegundoNombre(), sacUsu.getApellidoPaterno(), sacUsu.getApellidoMaterno());
        Sort sort = Sort.by(Sort.Order.asc("catUsuario.nombre").ignoreCase(),
                Sort.Order.asc("catUsuario.segundoNombre").ignoreCase().nullsFirst(),
                Sort.Order.asc("catUsuario.apellidoPaterno").ignoreCase(),
                Sort.Order.asc("catUsuario.apellidoMaterno").ignoreCase());

        Pageable pageable = PageRequest.of(sacUsu.getPagina(), sacUsu.getSize(), sort);

        return catUsuarioEntePublicoRepository.findAll(specSacUSuario, pageable);
    }
}
