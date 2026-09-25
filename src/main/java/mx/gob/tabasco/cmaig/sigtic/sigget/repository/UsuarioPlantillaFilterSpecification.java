package mx.gob.tabasco.cmaig.sigtic.sigget.repository;

import mx.gob.tabasco.cmaig.sigtic.Utileria.Utileria;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.UsuarioPlantilla;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioPlantillaFilterSpecification implements Specification<UsuarioPlantilla> {

    private Long idEntePublico;
    private String rfc;
    private String curp;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;

    public UsuarioPlantillaFilterSpecification(UsuarioPlantilla usuarioPlantilla) {
        this.idEntePublico = usuarioPlantilla.getIdEntePublico();

        if (usuarioPlantilla.getRfcUsuario()!=null)
            this.rfc = usuarioPlantilla.getRfcUsuario().toUpperCase().trim();

        if (usuarioPlantilla.getCurpUsuario()!=null)
            this.curp = usuarioPlantilla.getCurpUsuario().toUpperCase().trim();

        if (usuarioPlantilla.getNombreUsuario()!=null)
            this.nombre = Utileria.quitarAcentos(usuarioPlantilla.getNombreUsuario().toUpperCase());
        if (usuarioPlantilla.getApellidoPaterno()!=null)
            this.apellidoPaterno = Utileria.quitarAcentos(usuarioPlantilla.getApellidoPaterno().toUpperCase());
        if (usuarioPlantilla.getApellidoMaterno()!=null)
            this.apellidoMaterno = Utileria.quitarAcentos(usuarioPlantilla.getApellidoMaterno().toUpperCase());
    }

    @Override
    public Predicate toPredicate(Root<UsuarioPlantilla> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (this.idEntePublico != null && !this.idEntePublico.equals("")) {
            predicates.add(criteriaBuilder.equal(root.get("idEntePublico").as(Long.class),this.idEntePublico));
        }

        if (this.rfc != null && !this.rfc.equals("")) {
            predicates.add(criteriaBuilder.like(root.get("rfcUsuario").as(String.class),"%"+this.rfc.toUpperCase()+"%"));
        }

        if (this.curp != null && !this.curp.equals("")) {
            predicates.add(criteriaBuilder.like(root.get("curpUsuario").as(String.class),"%"+this.curp.toUpperCase()+"%"));
        }

        if (this.nombre != null && !this.nombre.equals("")) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(root.<String> get("nombreUsuario")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.nombre + "%"));
        }

        if (this.apellidoPaterno != null && !this.apellidoPaterno.equals("")) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(root.<String> get("apellidoPaterno")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.apellidoPaterno + "%"));
        }

        if (this.apellidoMaterno != null && !this.apellidoMaterno.equals("")) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(root.<String> get("apellidoMaterno")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.apellidoMaterno + "%"));
        }


        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
