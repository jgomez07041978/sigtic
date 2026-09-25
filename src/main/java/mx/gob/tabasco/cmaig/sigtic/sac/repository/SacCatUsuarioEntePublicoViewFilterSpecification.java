package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.Utileria.Utileria;
import mx.gob.tabasco.cmaig.sigtic.sac.model.SacCatUsuarioEntePublicoView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class SacCatUsuarioEntePublicoViewFilterSpecification implements Specification<SacCatUsuarioEntePublicoView> {

    private String rfc;
    private String curp;
    private String nombre;
    private String segundoNombre;
    private String apellidoPaterno;
    private String apellidoMaterno;


    public SacCatUsuarioEntePublicoViewFilterSpecification(String rfc, String curp, String nombre, String segundoNombre, String apellidoPaterno, String apellidoMaterno) {

        if (rfc!=null && !rfc.equals(""))
            this.rfc = Utileria.quitarAcentos(rfc.toUpperCase());

        if (curp!=null && !curp.equals(""))
            this.curp = Utileria.quitarAcentos(curp.toUpperCase());

        if (nombre!=null && !nombre.equals(""))
            this.nombre = Utileria.quitarAcentos(nombre.toUpperCase());

        if (segundoNombre!=null && !segundoNombre.equals(""))
            this.segundoNombre = Utileria.quitarAcentos(segundoNombre.toUpperCase());

        if (apellidoPaterno!=null && !apellidoPaterno.equals(""))
            this.apellidoPaterno = Utileria.quitarAcentos(apellidoPaterno.toUpperCase());

        if (apellidoMaterno!=null && !apellidoMaterno.equals(""))
            this.apellidoMaterno = Utileria.quitarAcentos(apellidoMaterno.toUpperCase());

    }

    @Override
    public Predicate toPredicate(Root<SacCatUsuarioEntePublicoView> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        if (this.rfc != null) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(root.<String> get("rfcUsuario")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.rfc + "%"));
        }

        if (this.curp != null) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(root.<String> get("curpUsuario")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.curp + "%"));
        }

        if (this.nombre != null) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(root.<String> get("nombreCompletoMayusculas")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.nombre + "%"));
        }

        if (this.segundoNombre != null) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(root.<String> get("segundoNombre")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.nombre + "%"));
        }

        if (this.apellidoPaterno != null) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(root.<String> get("apellidoPaterno")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.apellidoPaterno + "%"));
        }

        if (this.apellidoMaterno != null) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(root.<String> get("apellidoMaterno")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.apellidoMaterno + "%"));
        }

        predicates.add(criteriaBuilder.equal(root.get("cveEstatus"),"VIG"));
        predicates.add(criteriaBuilder.equal(root.get("cveEstatusEntePublico"),"VIG"));


        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
