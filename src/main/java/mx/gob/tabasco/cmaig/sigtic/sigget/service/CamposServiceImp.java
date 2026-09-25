package mx.gob.tabasco.cmaig.sigtic.sigget.service;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.Campo;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Campos;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.CamposPK;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Plantilla;
import mx.gob.tabasco.cmaig.sigtic.sigget.repository.CampoRepository;
import mx.gob.tabasco.cmaig.sigtic.sigget.repository.CamposRepository;
import mx.gob.tabasco.cmaig.sigtic.sigget.repository.PlantillaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(value = "siggetTransactionManager")
public class CamposServiceImp implements CamposService{

    @Autowired
    private CamposRepository camposRepository;

    @Autowired
    private CampoRepository campoRepository;

    @Autowired
    private PlantillaRepository plantillaRepository;

    @Override
    public List<Campos> getCamposPorPlantilla(Plantilla plantilla, String estatus) {
        List<Campos> lista = camposRepository.findCamposByPlantillaAndCveEstatusOrderByNumOrdenCampo(plantilla,estatus);
        return lista;
    }

    @Override
    public Campos getCamposId(CamposPK campo) {
        Optional<Campos> campos = camposRepository.findById(campo);
        if (!campos.isPresent())
            throw new NoSuchElementException("El campo no existe");

        return campos.get();
    }

    @Override
    public Campos saveCampos(Campos campo) {
        //UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Plantilla plantilla = plantillaRepository.findById(campo.getCamposPK().getIdPlantilla()).get();

        List<Campos> lista = camposRepository.findAllByPlantillaAndUbicacionColumna(plantilla, campo.getUbicacionColumna());
        if (lista.size()>1)
            throw new NoSuchElementException("La ubicación ya esta en Uso");
        else
            if (lista.size()==1)
                if (lista.get(0).getCamposPK().getNumCampo().intValue()!=campo.getCamposPK().getNumCampo())
                    throw new NoSuchElementException("La ubicación ya esta en Uso");


        campo.setFechaRegistro(new Timestamp(new Date().getTime()));
        /*campo.setCveUsuarioMtto(authUser.getUsername());
        campo.setIpPc(ClienteInfo.getClientIpAddr(request))
        campo.setNombrePc(ClienteInfo.getHostPC(request))
        campo.setNombreAplicacion(ClienteInfo.getClienteApp(request))*/

        return camposRepository.save(campo);
    }

    @Override
    public Campos updateCampos(Campos campo) {

        Plantilla plantilla = plantillaRepository.findById(campo.getCamposPK().getIdPlantilla()).get();

        List<Campos> lista = camposRepository.findAllByPlantillaAndUbicacionColumna(plantilla, campo.getUbicacionColumna());
        if (lista.size()>1)
            throw new NoSuchElementException("La ubicación ya esta en Uso");
        else
        if (lista.size()==1)
            if (lista.get(0).getCamposPK().getNumCampo().intValue()!=campo.getCamposPK().getNumCampo()){
                Optional<Campos> campos =  camposRepository.findAllByCamposPK(campo.getCamposPK());
                if (campos.isPresent())
                    throw new NoSuchElementException("La ubicación ya esta en Uso");

                camposRepository.delete(lista.get(0));
            }


        campo.setFechaCambio(new Timestamp(new Date().getTime()));
        return camposRepository.save(campo);
    }

    @Override
    public void deleteCampo(CamposPK camposPK) {
        Campos campo =  camposRepository.findById(camposPK).get();
        camposRepository.delete(campo);
    }

    @Override
    public List<Campo> getCamposDisponibles(List<String> listaAsignada) {

        List<Campo> listaCampo = null;

        if (listaAsignada.size()==0)
            listaCampo=campoRepository.findAllByOrderByOrden();
        else
            listaCampo = campoRepository.findAllByNombreCampoNotInOrderByOrden(listaAsignada);

        return listaCampo;
    }


}
