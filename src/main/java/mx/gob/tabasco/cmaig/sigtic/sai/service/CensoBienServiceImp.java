package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.Bien;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ConcentradoBean;
import mx.gob.tabasco.cmaig.sigtic.sai.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.BienRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.ConcentradoBeanRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.UnidadAdministrativaRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CensoBienServiceImp implements CensoBienService{

    @Autowired
    private ConcentradoBeanRepository concentradoBeanRepository;

    @Autowired
    private BienRepository bienRepository;

    @Autowired
    private UnidadAdministrativaRepository unidadAdministrativaRepository;
    @Override
    public JSONObject getConcentradoBienesPorEntePublico() {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ConcentradoBean> listaConcentrado = null;

        if (authUser.isAdministradorSigtic())
            listaConcentrado = concentradoBeanRepository.getConcentradoBienesEntePublico();
        else
            listaConcentrado = concentradoBeanRepository.getConcentradoBienesEntePublicoId(authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("listaConcentrado", listaConcentrado);

        return jsonObject;
    }

    @Override
    public JSONObject getConcentradoBienesPorUnidadAdministrativa(Long idEntePublico) {
        List<ConcentradoBean> listaConcentrado = concentradoBeanRepository.getConcentradoBienesUnidadAdministrativa(idEntePublico);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("listaConcentrado", listaConcentrado);

        return jsonObject;
    }

    @Override
    public List<Bien> getListaBienesPorUnidad(Long idUnidad) {
        UnidadAdministrativa unidadAdministrativa = unidadAdministrativaRepository.findById(idUnidad).get();

        List<Bien> lista = bienRepository.findAllByUnidadAdministrativaAndCveEstatusOrderByNumeroInventario(unidadAdministrativa,"VIG");

        return lista;
    }
}
