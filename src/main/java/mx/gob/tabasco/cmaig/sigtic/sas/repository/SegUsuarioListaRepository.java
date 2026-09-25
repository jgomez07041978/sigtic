package mx.gob.tabasco.cmaig.sigtic.sas.repository;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuarioLista;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegUsuarioListaRepository extends CrudRepository<SegUsuarioLista, Long> {

    List<SegUsuarioLista> getUsuarioPorSistemaEnteAdminSigtic(String sistema, Long idEnte);
    List<SegUsuarioLista> getUsuarioPorSistemaUnidades(String sistema, List<Long> listaUnidad);


}
