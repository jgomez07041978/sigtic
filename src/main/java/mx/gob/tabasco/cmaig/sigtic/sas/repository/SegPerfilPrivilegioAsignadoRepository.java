package mx.gob.tabasco.cmaig.sigtic.sas.repository;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegioAsignado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegPerfilPrivilegioAsignadoRepository extends JpaRepository<SegPerfilPrivilegioAsignado, Integer> {

    List<SegPerfilPrivilegioAsignado> findPerfilPrivilegioByIdPerfil(Integer idRol);
}
