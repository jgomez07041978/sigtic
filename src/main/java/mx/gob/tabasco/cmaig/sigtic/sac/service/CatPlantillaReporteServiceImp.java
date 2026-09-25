package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatPlantillaReporte;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.CatPlantillaReporteRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CartaResponsiva;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CartaResponsivaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CatPlantillaReporteServiceImp implements CatPlantillaReporteService {


    @Autowired
    private CatPlantillaReporteRepository catPlantillaReporteRepository;

    @Autowired
    private CartaResponsivaRepository cartaResponsivaRepository;

    @Override
    public List<CatPlantillaReporte> getListaPlantillaEnteSubtipoServicio(Long idEntePublico, Long idSubtipoServicio) {


        return catPlantillaReporteRepository.getPlantillaReportePorEnteSubtipoServicio(idEntePublico.longValue(), idSubtipoServicio);
    }

    @Override
    public List<CatPlantillaReporte> getListaPlantillaEnteSubtipoServicio(Long idCartaResponsiva) {

        CartaResponsiva cartaResponsiva = cartaResponsivaRepository.findById(idCartaResponsiva).get();
        return catPlantillaReporteRepository.getPlantillaReportePorEnteSubtipoServicio(cartaResponsiva.getEntePublico().getIdEntePublico().longValue(), cartaResponsiva.getCatSubtipoServiciosTic().getIdSubtipoServicioTic());
    }

    @Override
    public CatPlantillaReporte getPlantillaReporteId(Long idPlantillaReporte,Long idCartaResponsiva) {
        CatPlantillaReporte  plantillaReporte=catPlantillaReporteRepository.findById(idPlantillaReporte).get();
       /* CartaResponsiva cartaResponsiva = cartaResponsivaRepository.findById(idCartaResponsiva).get();
        plantillaReporte.setCartaResponsiva(cartaResponsiva);*/
        return plantillaReporte;
    }
}
