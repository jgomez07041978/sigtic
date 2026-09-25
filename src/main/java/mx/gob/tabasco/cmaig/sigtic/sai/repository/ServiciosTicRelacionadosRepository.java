package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServiciosTicRelacionados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ServiciosTicRelacionadosRepository extends JpaRepository<ServiciosTicRelacionados, Long> {

    List<ServiciosTicRelacionados> findAllByServiciosTicPadre(ServiciosTic serviciosTic);

    @Query("SELECT st FROM ServiciosTicRelacionados st WHERE st.serviciosTicPadre=?1 and st.serviciosTic.subTipoServicio in (?2) and st.cveEstatus='VIG' order by st.numeroOrdenServicio")
    List<ServiciosTicRelacionados> getServiciosRelacionados(ServiciosTic serviciosTic, List<CatSubtipoServiciosTic> lista);

    Optional<ServiciosTicRelacionados> findByServiciosTicPadreAndServiciosTic(ServiciosTic serviciosTicPadre, ServiciosTic serviciosTic);
}
