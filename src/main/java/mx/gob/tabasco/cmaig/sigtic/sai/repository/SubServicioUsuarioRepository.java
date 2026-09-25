package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.SubServicioUsuario;
import mx.gob.tabasco.cmaig.sigtic.sai.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubServicioUsuarioRepository extends JpaRepository<SubServicioUsuario, Long> {

    List<SubServicioUsuario> findAllByUsuario(Usuario usuario);

    Optional<SubServicioUsuario> findByCatSubtipoServiciosTicAndUsuario(CatSubtipoServiciosTic catSubtipoServiciosTic, Usuario usuario);



}
