package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublicoReporte;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.EntePublicoReporteRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.EntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.UnidadAdministrativaRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegAccesoUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.SegAccesoUsuarioRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntePublicoServiceImp implements EntePublicoService {

    @Autowired
    @Qualifier("entePublicoRepositorySac")
    private EntePublicoRepository entePublicoRepository;

    @Autowired
    private SegAccesoUsuarioRepository segAccesoUsuarioRepository;

    @Autowired
    private EntePublicoReporteRepository entePublicoReporteRepository;

    @Autowired
    private UnidadAdministrativaRepository unidadAdministrativaRepository;


    @Override
    public List<EntePublico> getEntesPublicos() {
        List<EntePublico> lista=entePublicoRepository.findAllByCveEstatusOrderByNumOrdenEntePublicoAsc("VIG");

        List<EntePublico> listaFiler = lista.stream().filter(entePublico -> entePublico.getIdEntePublico()!=0).collect(Collectors.toList());

        return listaFiler;
    }

    @Override
    public List<EntePublico> getEntesPublicos(String curp) {
            List<SegAccesoUsuario> lista=segAccesoUsuarioRepository.findAllByRfcUsuarioAndIdSistema(curp,"SIGTIC");

            if (lista.size()>0) {
                List<Long> listaId = lista.stream().map(SegAccesoUsuario::getIdEntePublico).collect(Collectors.toList());
                return entePublicoRepository.findAllByIdEntePublicoInAndCveEstatusOrderByTipoEntePublicoAscNombreEntePublicoAsc(listaId,"VIG");
            }else{
                UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                List<EntePublico> listaEnte= new ArrayList<>();
                listaEnte.add(entePublicoRepository.findById(authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico()).get());
                return listaEnte;
            }
    }

    @Override
    public EntePublico getEntePublicoId(Long idEntePublico) {
        return entePublicoRepository.findById(idEntePublico).get();
    }

    @Override
    public List<EntePublico> getEntesPublicoSaig() {
        return entePublicoRepository.findAllByCveEntePublico("21");
    }

    @Override
    public Optional<EntePublicoReporte> getEntePublicoReporte(Long idEntePublico, String estatus) {
        EntePublico entePublico= entePublicoRepository.findById(idEntePublico).get();

        return entePublicoReporteRepository.findEntePublicoReporteByEntePublicoAndCveEstatus(entePublico,estatus);
    }

    @Override
    public Optional<EntePublicoReporte> getEntePublicoReporteId(Long idEntePublicoReporte) {
        return entePublicoReporteRepository.findById(idEntePublicoReporte);
    }

    @Override
    public List<EntePublico> getEntePublicoUsuario(String rfc,String sistema, String estatus){

        List<Long> lista = segAccesoUsuarioRepository.getIdEntePublicoUnico(rfc, sistema, estatus);
        List<EntePublico> listaEnte = entePublicoRepository.getEntesPublicoByList(lista);

        return listaEnte;
    }

    @Override
    public void deleteLogoEntePublico(Integer idLogo) {

        List<EntePublico> lista = entePublicoRepository.findAllByIdImagenEntePublico(idLogo);

        for (EntePublico entePublico :  lista){
            entePublico.setIdImagenEntePublico(null);
        }

        entePublicoRepository.saveAll(lista);
    }

    @Override
    public Optional<EntePublico> getEntePublicoLogo(Integer idLogo) {

        List<EntePublico> lista = entePublicoRepository.findAllByIdImagenEntePublico(idLogo);

        return  lista.stream().findFirst();
    }

    @Override
    public void deleteLogoUnidadAdministrativa(Integer idLogo) {

        List<UnidadAdministrativa> lista = unidadAdministrativaRepository.findAllByIdLogo(idLogo);

        for (UnidadAdministrativa unidadAdministrativa :  lista){
            unidadAdministrativa.setIdLogo(null);
        }

        unidadAdministrativaRepository.saveAll(lista);
    }


}
