package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatServiciosTicIp;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServiciosTic;

import java.util.List;

public interface CatServiciosTicIpService {

    List<CatServiciosTicIp> getListaIpPorServicio(Long idServicio);
    CatServiciosTicIp guardarServiciosTicIp(CatServiciosTicIp catServiciosTicIp);
}
