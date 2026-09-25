package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.DomicilioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UbicacionUnidadAdmin;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.DomicilioEntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.UbicacionUnidadAdminRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class UbicacionUnidadAdminServiceImp implements UbicacionUnidadAdminService {

    @Autowired
    private UbicacionUnidadAdminRepository ubicacionUnidadAdminRepository;

    @Autowired
    private DomicilioEntePublicoRepository domicilioEntePublicoRepository;

    @Override
    public List<UbicacionUnidadAdmin> listaUbicacionesUnidadPorDomicilio(Long idDomicilioEntePublico) {

        DomicilioEntePublico domicilioEntePublico = domicilioEntePublicoRepository.findById(idDomicilioEntePublico).get();

        return ubicacionUnidadAdminRepository.findAllByDomicilioEntePublicoOrderByNumOrden(domicilioEntePublico);
    }

    @Override
    public List<UbicacionUnidadAdmin> listaUbicacionesUnidadPorDomicilioVigentes(Long idDomicilioEntePublico) {
        DomicilioEntePublico domicilioEntePublico = domicilioEntePublicoRepository.findById(idDomicilioEntePublico).get();
        return ubicacionUnidadAdminRepository.findAllByDomicilioEntePublicoAndCveEstatusOrderByNumOrden(domicilioEntePublico, "VIG");
    }

    @Override
    public UbicacionUnidadAdmin obtenerUbicacionId(Long idUbicacion) {
        return ubicacionUnidadAdminRepository.findById(idUbicacion).get();
    }

    @Override
    public UbicacionUnidadAdmin saveUbicacion(UbicacionUnidadAdmin ubicacionUnidadAdmin) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (ubicacionUnidadAdmin.getIdUbicacionAdaministrativa()!=null){
            UbicacionUnidadAdmin ubicacionUnidadAdminUpate= ubicacionUnidadAdminRepository.findById(ubicacionUnidadAdmin.getIdUbicacionAdaministrativa()).get();
            ubicacionUnidadAdminUpate.setNombreUbicacion(ubicacionUnidadAdmin.getNombreUbicacion());
            ubicacionUnidadAdminUpate.setNombreUbicacionAbreviada(ubicacionUnidadAdmin.getNombreUbicacionAbreviada());
            ubicacionUnidadAdminUpate.setNumTelefono(ubicacionUnidadAdmin.getNumTelefono());
            ubicacionUnidadAdminUpate.setExtension(ubicacionUnidadAdmin.getExtension());
            ubicacionUnidadAdminUpate.setReferencia(ubicacionUnidadAdmin.getReferencia());
            ubicacionUnidadAdminUpate.setCorreoUnidad(ubicacionUnidadAdmin.getCorreoUnidad());
            ubicacionUnidadAdminUpate.setNumOrden(ubicacionUnidadAdmin.getNumOrden());
            ubicacionUnidadAdminUpate.setCveUsuarioMtto(authUser.getUsername());
            ubicacionUnidadAdminUpate.setNombreAplicacion("SIGTIC");
            ubicacionUnidadAdminUpate.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            ubicacionUnidadAdminUpate.setUbicacionTitularEntePublico(ubicacionUnidadAdmin.getUbicacionTitularEntePublico());
            ubicacionUnidadAdminUpate.setCveEstatus(ubicacionUnidadAdmin.getCveEstatus());

            return ubicacionUnidadAdminRepository.save(ubicacionUnidadAdminUpate);
        }else{
            UbicacionUnidadAdmin ubicacionUnidadAdminNew= new UbicacionUnidadAdmin();
            ubicacionUnidadAdminNew.setNombreUbicacion(ubicacionUnidadAdmin.getNombreUbicacion());
            ubicacionUnidadAdminNew.setNombreUbicacionAbreviada(ubicacionUnidadAdmin.getNombreUbicacionAbreviada());
            ubicacionUnidadAdminNew.setNumTelefono(ubicacionUnidadAdmin.getNumTelefono());
            ubicacionUnidadAdminNew.setExtension(ubicacionUnidadAdmin.getExtension());
            ubicacionUnidadAdminNew.setReferencia(ubicacionUnidadAdmin.getReferencia());
            ubicacionUnidadAdminNew.setCorreoUnidad(ubicacionUnidadAdmin.getCorreoUnidad());
            ubicacionUnidadAdminNew.setNumOrden(ubicacionUnidadAdmin.getNumOrden());
            ubicacionUnidadAdminNew.setCveUsuarioMtto(authUser.getUsername());
            ubicacionUnidadAdminNew.setNombreAplicacion("SIGTIC");
            ubicacionUnidadAdminNew.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            ubicacionUnidadAdminNew.setFechaRegistro(new Timestamp(new Date().getTime()));
            ubicacionUnidadAdminNew.setUbicacionTitularEntePublico(ubicacionUnidadAdmin.getUbicacionTitularEntePublico());
            ubicacionUnidadAdminNew.setCveEstatus(ubicacionUnidadAdmin.getCveEstatus());
            DomicilioEntePublico domicilioEntePublico =domicilioEntePublicoRepository.findById(ubicacionUnidadAdmin.getDomicilioEntePublico().getIdDomicilioEntePublico()).get();
            ubicacionUnidadAdminNew.setDomicilioEntePublico(domicilioEntePublico);

            return ubicacionUnidadAdminRepository.save(ubicacionUnidadAdminNew);
        }

    }

    @Override
    public UbicacionUnidadAdmin deleteUbicacion(UbicacionUnidadAdmin ubicacionUnidadAdmin) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UbicacionUnidadAdmin ubicacionUnidadAdmin1= ubicacionUnidadAdminRepository.findById(ubicacionUnidadAdmin.getIdUbicacionAdaministrativa()).get();

        ubicacionUnidadAdmin1.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        ubicacionUnidadAdmin1.setCveUsuarioMtto(authUser.getUsername());
        ubicacionUnidadAdmin1.setCveEstatus("NOV");

        return ubicacionUnidadAdminRepository.save(ubicacionUnidadAdmin1);
    }
}
