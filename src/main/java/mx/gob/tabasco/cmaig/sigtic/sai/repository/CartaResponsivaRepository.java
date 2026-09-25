package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CartaResponsiva;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sai.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CartaResponsivaRepository extends CrudRepository<CartaResponsiva, Long> {

    List<CartaResponsiva> findAllByEntePublicoOrderByIdCartaResponsivaDesc(EntePublico entePublico);
    List<CartaResponsiva> findAllByUsuarioAndCveEstatusInOrderByIdCartaResponsivaDesc(Usuario usuario, List<String> listaEstatus);

    @Query("SELECT cr FROM CartaResponsiva cr where cr.usuario=?1 and  cr.cveEstatus in ?2 and cr.estatusCartaResponsiva.idEstatusCartaResponsiva in (8,9,11) order by cr.idCartaResponsiva desc")
    List<CartaResponsiva> getMisCartasResponsivas(Usuario usuario, List<String> listaEstatus);


    @Query("SELECT cr FROM CartaResponsiva cr where cr.entePublicoResponsable=?1 and cr.catSubtipoServiciosTic in ?2 and cr.cveEstatus in ?3 and cr.estatusCartaResponsiva.idEstatusCartaResponsiva>1 order by cr.idCartaResponsiva desc")
    List<CartaResponsiva> getCartaResponsivaEnlaceCr(EntePublico entePublico, List<CatSubtipoServiciosTic> lista, List<String> listaEstatus);

    @Query("SELECT cr FROM CartaResponsiva cr where cr.entePublico=?1 and cr.catSubtipoServiciosTic in ?2 and cr.cveEstatus in ?3 order by cr.idCartaResponsiva desc")
    List<CartaResponsiva> getCartaResponsivaAdminCr(EntePublico entePublico, List<CatSubtipoServiciosTic> lista, List<String> listaEstatus);

    @Query("SELECT cr FROM CartaResponsiva cr where cr.entePublico=?1 and cr.cveEstatus in ?2 order by cr.idCartaResponsiva desc")
    List<CartaResponsiva> getCartaResponsivaAdminSigtic(EntePublico entePublico, List<String> listaEstatus);

    @Query("SELECT cr FROM CartaResponsiva cr where (cr.entePublico=?1 or cr.entePublicoResponsable=?2) and cr.cveEstatus in ?3 order by cr.idCartaResponsiva desc")
    List<CartaResponsiva> getCartaResponsivaAdmin(EntePublico entePublico, EntePublico entePublicoResponsable, List<String> listaEstatus);
    //List<CartaResponsiva> findAllByEntePublicoOrEntePublicoResponsableOrderByIdCartaResponsivaDesc(EntePublico entePublico, EntePublico entePublicoResponsable);

    @Query("SELECT cr FROM CartaResponsiva cr where (cr.entePublico=?1 or cr.entePublicoResponsable=?2) and cr.catSubtipoServiciosTic in ?3 and cr.cveEstatus in ?4 order by cr.idCartaResponsiva desc")
    List<CartaResponsiva> getCartaResponsivaAdminSubtipoIn(EntePublico entePublico, EntePublico entePublicoResponsable, List<CatSubtipoServiciosTic> lista, List<String> listaEstatus);

    @Query("SELECT cr FROM CartaResponsiva cr where (cr.entePublico=?1 or cr.entePublicoResponsable=?2) and cr.catSubtipoServiciosTic = ?3  and cr.cveEstatus in ?4 order by cr.idCartaResponsiva desc")
    List<CartaResponsiva> getCartaResponsivaAdminSubtipo(EntePublico entePublico, EntePublico entePublicoResponsable, CatSubtipoServiciosTic catSubtipoServiciosTic, List<String> listaEstatus);

    @Query("SELECT cr FROM CartaResponsiva cr where cr.entePublicoResponsable=?1 and cr.cveEstatus in ?2 order by cr.idCartaResponsiva desc")
    List<CartaResponsiva> getCartaResponsivaEnlace(EntePublico entePublicoResponsable, List<String> listaEstatus);
    //List<CartaResponsiva> findAllByEntePublicoResponsableOrderByIdCartaResponsivaDesc(EntePublico entePublicoResponsable);

    @Query("SELECT cr FROM CartaResponsiva cr where cr.entePublicoResponsable=?1 and cr.catSubtipoServiciosTic in ?2 and cr.cveEstatus in ?3  order by cr.idCartaResponsiva desc")
    List<CartaResponsiva> getCartaResponsivaEnlaceSubtipoIn(EntePublico entePublicoResponsable, List<CatSubtipoServiciosTic> lista, List<String> listaEstatus);

    @Query("SELECT cr FROM CartaResponsiva cr where cr.entePublicoResponsable=?1 and cr.catSubtipoServiciosTic = ?2 and cr.cveEstatus in ?3 order by cr.idCartaResponsiva desc")
    List<CartaResponsiva> getCartaResponsivaEnlaceSubtipo(EntePublico entePublicoResponsable, CatSubtipoServiciosTic catSubtipoServiciosTic, List<String> listaEstatus);
    //List<CartaResponsiva> findAllByEntePublicoAndUsuarioOrderByIdCartaResponsivaDesc(EntePublico entePublico, Usuario usuario);


    @Query("SELECT cr FROM CartaResponsiva cr where cr.entePublicoResponsable=?1 and cr.usuario=?2 and cr.cveEstatus in ?3 order by cr.idCartaResponsiva desc")
    List<CartaResponsiva> getCartaResponsivaResponsable(EntePublico entePublicoResponsable, Usuario usuario, List<String> listaEstatus);
    //List<CartaResponsiva> findAllByEntePublicoResponsableAndUsuarioOrderByIdCartaResponsivaDesc(EntePublico entePublicoResponsable, Usuario usuario);

    @Query("SELECT cr FROM CartaResponsiva cr where cr.entePublicoResponsable=?1 and cr.usuario=?2 and cr.catSubtipoServiciosTic in ?3 and cr.cveEstatus in ?4 order by cr.idCartaResponsiva desc")
    List<CartaResponsiva> getCartaResponsivaSubtipoIn(EntePublico entePublicoResponsable, Usuario usuario, List<CatSubtipoServiciosTic> lista, List<String> listaEstatus);

    @Query("SELECT cr FROM CartaResponsiva cr where cr.entePublicoResponsable=?1 and cr.usuario=?2 and cr.catSubtipoServiciosTic = ?3 and cr.cveEstatus in ?4 order by cr.idCartaResponsiva desc")
    List<CartaResponsiva> getCartaResponsivaSubtipo(EntePublico entePublicoResponsable, Usuario usuario, CatSubtipoServiciosTic catSubtipoServiciosTic, List<String> listaEstatus);
    Optional<CartaResponsiva> findTopByEntePublicoAndAnioOrderByFolioCartaResponsivaDesc(EntePublico entePublico, Integer anio);

}
