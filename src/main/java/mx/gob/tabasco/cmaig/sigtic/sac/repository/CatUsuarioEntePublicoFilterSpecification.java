package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.Utileria.Utileria;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class CatUsuarioEntePublicoFilterSpecification implements Specification<CatUsuarioEntePublico>{

    private String rfc;
    private String curp;
    private String nombre;
    private String segundoNombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Boolean isVigente;

    public CatUsuarioEntePublicoFilterSpecification(String rfc, String curp, String nombre, String segundoNombre, String apellidoPaterno, String apellidoMaterno) {
        if (rfc!=null && !rfc.equals(""))
            this.rfc = Utileria.quitarAcentos(rfc.toUpperCase());

        if (curp!=null && !curp.equals(""))
            this.curp = Utileria.quitarAcentos(curp.toUpperCase());

        if (nombre!=null)
            this.nombre = Utileria.quitarAcentos(nombre.toUpperCase());

        if (segundoNombre!=null)
            this.segundoNombre = Utileria.quitarAcentos(segundoNombre.toUpperCase());

        if (apellidoPaterno!=null)
            this.apellidoPaterno = Utileria.quitarAcentos(apellidoPaterno.toUpperCase());

        if (apellidoMaterno!=null)
            this.apellidoMaterno = Utileria.quitarAcentos(apellidoMaterno.toUpperCase());

        this.isVigente = isVigente;
    }

    @Override
    public Predicate toPredicate(Root<CatUsuarioEntePublico> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        Join<CatUsuarioEntePublico, EntePublico> entePublico= root.join("entePublico");
        Join<CatUsuarioEntePublico,   CatUsuario> catUsuarioJoin = root.join("catUsuario");

        if (this.rfc != null && !this.rfc.equals("")) {
            predicates.add(criteriaBuilder.like(catUsuarioJoin.get("rfc").as(String.class),"%"+this.rfc.toUpperCase()+"%"));
        }

        if (this.curp != null && !this.curp.equals("")) {
            predicates.add(criteriaBuilder.like(catUsuarioJoin.get("curp").as(String.class),"%"+this.curp.toUpperCase()+"%"));
        }

        if (this.nombre != null && !this.nombre.equals("")) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(catUsuarioJoin.<String> get("nombre")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.nombre + "%"));
        }

        if (this.segundoNombre != null && !this.segundoNombre.equals("")) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(catUsuarioJoin.<String> get("segundoNombre")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.segundoNombre + "%"));
        }


        if (this.apellidoPaterno != null && !this.apellidoPaterno.equals("")) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(catUsuarioJoin.<String> get("apellidoPaterno")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.apellidoPaterno + "%"));
        }

        if (this.apellidoMaterno != null && !this.apellidoMaterno.equals("")) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(catUsuarioJoin.<String> get("apellidoMaterno")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.apellidoMaterno + "%"));
        }

        predicates.add(criteriaBuilder.equal(entePublico.get("cveEstatus"),"VIG"));
        predicates.add(criteriaBuilder.equal(catUsuarioJoin.get("cveEstatus"),"VIG"));
        predicates.add(criteriaBuilder.equal(root.get("cveEstatus"),"VIG"));

       /* List<Order> orderList = new ArrayList();
        orderList.add(criteriaBuilder.asc(catUsuarioJoin.get("nombre")));
        orderList.add(criteriaBuilder.asc(catUsuarioJoin.get("segundoNombre")));
        orderList.add(criteriaBuilder.asc(catUsuarioJoin.get("apellidoPaterno")));
        orderList.add(criteriaBuilder.asc(catUsuarioJoin.get("apellidoPaterno")));

        query.orderBy(orderList);*/
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
