package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatCorreoElectronico;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CatCorreoElectronicoRepository extends CrudRepository<CatCorreoElectronico, Long> {

    Optional<CatCorreoElectronico> findByAndCorreoElectronico(String correoElectronico);

    //List<CatCorreoElectronico> findAllByIdEntePublico(Long idEntePublico);

/*    @Query("SELECT cp,us FROM CatCorreoElectronico cp " +
            " inner join Usuario us on us.idUsuario=cp.idTitularCorreo" +
            " where cp.idEntePublico=?1")
    List<CatCorreoElectronico> getCorreosElectronico(Long idEntePublico);*/

    /*@Query("SELECT cp,us FROM CatCorreoElectronico cp " +
            " inner join Usuario us on us.idUsuario=cp.idTitularCorreo" +
            " where cp.idEntePublico=?1")
    List<CatCorreoElectronico> getCorreosElectronico(Long idEntePublico);*/

   /* @Query(value="SELECT CO.*, US.NOMBRE_USUARIO AS nombreUsuario, US.APELLIDO_PATERNO_USUARIO as apellidoPaterno, " +
            "    US.APELLIDO_MATERNO_USUARIO as apellidoMaterno,US.RFC_USUARIO as rfcUsuario FROM CORREOS_ELECTRONICOS CO " +
            "    LEFT JOIN SIGGET_SAC_CATALOGOS.CAT_USUARIOS US ON CO.ID_TITULAR_CORREO_ELECTRONICO=US.ID_USUARIO " +
            "    WHERE CO.ID_ENTE_PUBLICO_TITULAR_C_EP=?1", nativeQuery=true)
    List<CatCorreoElectronico> getCorreosElectronico(Long idEntePublico);*/



}