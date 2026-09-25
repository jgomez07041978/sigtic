package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatServiciosTicIp;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatServiciosTicIpRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.ServiciosTicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CatServiciosTicIpServiceImp implements CatServiciosTicIpService{

    @Autowired
    private CatServiciosTicIpRepository catServiciosTicIpRepository;

    @Autowired
    private ServiciosTicRepository serviciosTicRepository;
    @Override
    public List<CatServiciosTicIp> getListaIpPorServicio(Long idServicio) {
        ServiciosTic serviciosTic = serviciosTicRepository.findById(idServicio).get();
        return catServiciosTicIpRepository.findAllByServiciosTicAndCveEstatusOrderByNumOrdenIpServicioTicAsc(serviciosTic,"VIG");
    }

    @Override
    public CatServiciosTicIp guardarServiciosTicIp(CatServiciosTicIp catServiciosTicIp){
        CatServiciosTicIp catServiciosTicIpBd = null;

        if(catServiciosTicIp.getServiciosTic() == null)
            throw new NoSuchElementException("Falta el servicio");

        ServiciosTic serviciosTic = serviciosTicRepository.findById(catServiciosTicIp.getServiciosTic().getIdServicioTic()).get();


        if(catServiciosTicIp.getIdIpServicioTic() != null){
            catServiciosTicIpBd = catServiciosTicIpRepository.findById(catServiciosTicIp.getIdIpServicioTic()).get();
        }else
            catServiciosTicIpBd = catServiciosTicIp;

        catServiciosTicIpBd.setServiciosTic(serviciosTic);
        catServiciosTicIpBd.setIpServicio(catServiciosTicIp.getIpServicio());
        catServiciosTicIpBd.setNumOrdenIpServicioTic(catServiciosTicIp.getNumOrdenIpServicioTic());
        catServiciosTicIpBd.setCveEstatus("VIG");
        catServiciosTicIpBd.setFechaUltimoCambio(new Timestamp(new Date().getTime()));

        return catServiciosTicIpRepository.save(catServiciosTicIpBd);
    }

}
