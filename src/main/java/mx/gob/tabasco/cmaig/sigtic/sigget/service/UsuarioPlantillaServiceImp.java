package mx.gob.tabasco.cmaig.sigtic.sigget.service;


import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Plantilla;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.PlantillasUsuario;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.UsuarioPlantilla;
import mx.gob.tabasco.cmaig.sigtic.sigget.repository.PlantillaRepository;
import mx.gob.tabasco.cmaig.sigtic.sigget.repository.PlantillasUsuarioRepository;
import mx.gob.tabasco.cmaig.sigtic.sigget.repository.UsuarioPlantillaFilterSpecification;
import mx.gob.tabasco.cmaig.sigtic.sigget.repository.UsuarioPlantillaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UsuarioPlantillaServiceImp implements UsuarioPlantillaService{

    @Autowired
    private UsuarioPlantillaRepository usuarioPlantillaRepository;

    @Autowired
    private PlantillasUsuarioRepository plantillasUsuarioRepository;

    @Autowired
    private PlantillaRepository plantillaRepository;
    @Override
    public Page<UsuarioPlantilla> getUsuariosPlantillas(UsuarioPlantilla usuarioPlantilla) {
        UsuarioPlantillaFilterSpecification usuarioPlantillaFilterSpecification = new UsuarioPlantillaFilterSpecification(usuarioPlantilla);

        Pageable pageable = PageRequest.of(usuarioPlantilla.getPagina(), usuarioPlantilla.getSize());

        return usuarioPlantillaRepository.findAll(usuarioPlantillaFilterSpecification, pageable);
    }

    @Override
    public List<PlantillasUsuario> getPlantillasAsignadas(Long idEntePublico, String rfcUsuario) {
        return plantillasUsuarioRepository.findPlantillasUsuarioByIdEntePublicoAndUsuarioAndCveEstatusOrderByPlantilla(idEntePublico, rfcUsuario, "VIG");
    }

    @Override
    public List<Plantilla> getPlantillasDisponibles(List<Long> lista) {

        List<Plantilla> listaDisponible=null;

        if (lista.size()>0)
            listaDisponible = plantillaRepository.findByidPlantillaNotInAndCveEstatusOrderByNombrePlantilla(lista, "VIG");
        else
            listaDisponible = plantillaRepository.findByCveEstatusOrderByNombrePlantilla("VIG");

        return listaDisponible;
    }

    @Override
    public List<Long> getPlantillasIds(Long idEntePublico, String rfcUsuario) {
        return plantillasUsuarioRepository.findAllPlantillas(idEntePublico, rfcUsuario);
    }

    @Override
    public PlantillasUsuario addPlantilla(PlantillasUsuario plantillasUsuario){
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Plantilla plantilla = plantillaRepository.findById(plantillasUsuario.getPlantilla().getIdPlantilla()).get();

        Optional<PlantillasUsuario> plantillasUsuarioOp=plantillasUsuarioRepository.findByPlantillaAndUsuarioAndIdEntePublico(plantilla, plantillasUsuario.getUsuario(), authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico().longValue());

        if (plantillasUsuarioOp.isPresent())
            throw new NoSuchElementException("La plantilla ya esta agregada");

        plantillasUsuario.setPlantilla(plantilla);
        plantillasUsuario.setFechaRegistro(new Timestamp(new Date().getTime()));
        plantillasUsuario.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        plantillasUsuario.setCveEstatus("VIG");
        return plantillasUsuarioRepository.save(plantillasUsuario);
    }

    @Override
    public void removePlantilla(PlantillasUsuario plantillasUsuario){
        Optional<PlantillasUsuario> plantillasUsuarioOp=plantillasUsuarioRepository.findById(plantillasUsuario.getIdPlantillaUsuario());

        if (!plantillasUsuarioOp.isPresent())
            throw new NoSuchElementException("La plantilla ya no esta agregada");

           plantillasUsuarioRepository.delete(plantillasUsuarioOp.get());
    }


}
