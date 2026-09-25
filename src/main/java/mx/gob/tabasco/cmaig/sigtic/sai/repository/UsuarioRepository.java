package mx.gob.tabasco.cmaig.sigtic.sai.repository;


import mx.gob.tabasco.cmaig.sigtic.sai.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sai.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository  extends CrudRepository<Usuario, Long> {

    //List<Usuario> findByUnidadAdministrativaInAndCveEstatusOrderByUnidadAdministrativaAscNombreAsc(List<UnidadAdministrativa> listaUnidad, String estatus);

    @Query(value="select US.* from SIGGET_SAC_CATALOGOS.CAT_USUARIOS US where US.ID_UNIDAD_ADMTVA_USUARIO IN (?1) AND " +
            "NOT EXISTS (SELECT *FROM CORREOS_ELECTRONICOS where ID_TITULAR_CORREO_ELECTRONICO=US.ID_USUARIO AND ID_ENTE_PUBLICO_TITULAR_C_EP=?2) " +
            "AND US.CVE_ESTATUS=?3 ORDER BY US.NOMBRE_USUARIO, US.SEGUNDO_NOMBRE_USUARIO, US.APELLIDO_PATERNO_USUARIO, US.APELLIDO_MATERNO_USUARIO ", nativeQuery=true)
    List<Usuario> getUsuariosPorUnidadAdministrativaNoExistaCorreo(List<UnidadAdministrativa> listaUnidad, Long idEntePublico, String estatus);
}
