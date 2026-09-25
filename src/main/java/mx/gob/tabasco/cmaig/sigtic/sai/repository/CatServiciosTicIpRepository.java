package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatServiciosTicIp;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServiciosTic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatServiciosTicIpRepository extends CrudRepository<CatServiciosTicIp, Long> {
    List<CatServiciosTicIp> findAllByServiciosTicAndCveEstatusOrderByNumOrdenIpServicioTicAsc(ServiciosTic serviciosTic, String estatus);
}
