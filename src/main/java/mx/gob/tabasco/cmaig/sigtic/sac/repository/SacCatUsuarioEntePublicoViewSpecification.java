package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.Utileria.Utileria;
import mx.gob.tabasco.cmaig.sigtic.sac.model.SacCatUsuarioEntePublicoView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class SacCatUsuarioEntePublicoViewSpecification implements Specification<SacCatUsuarioEntePublicoView> {

    private Long idEntePublico;
    private List<Long> listaUnidadAdminId;

    private String nombre;

    private String telefonoInstitucinal;

    private String extension;

    public SacCatUsuarioEntePublicoViewSpecification(Long idEntePublico, List<Long> listaUnidadAdminId, String nombre, String telefonoInstitucinal, String extension) {
        this.idEntePublico = idEntePublico;
        if (listaUnidadAdminId!=null)
            this.listaUnidadAdminId = listaUnidadAdminId;
        if (nombre!=null)
            this.nombre = Utileria.quitarAcentos(nombre.toUpperCase());
        this.telefonoInstitucinal = telefonoInstitucinal;
        this.extension = extension;
    }

    @Override
    public Predicate toPredicate(Root<SacCatUsuarioEntePublicoView> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        if (this.idEntePublico != null) {
            predicates.add(criteriaBuilder.equal(root.get("idEntePublico").as(Long.class),this.idEntePublico));
        }

        if (this.listaUnidadAdminId != null) {
            Expression<Long> parentExpression = root.get("idUnidadAdministrativa");
            Predicate parentPredicate = parentExpression.in(listaUnidadAdminId);
            predicates.add(parentPredicate);
            //predicates.add(criteriaBuilder.equal(root.get("idUnidadAdministrativa").as(Long.class),this.idUnidadAdministrativa));
        }


        if (this.nombre != null && !this.nombre.equals("")) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, root.<String> get("nombreCompletoMayusculas"),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.nombre + "%"));
        }

        if (this.telefonoInstitucinal != null && !this.telefonoInstitucinal.equals("")) {
            predicates.add(criteriaBuilder.like(root.get("telefonoInstitucinal"),"%"+ this.telefonoInstitucinal+"%"));
        }

        if (this.extension != null && !this.extension.equals("")) {
            predicates.add(criteriaBuilder.like(root.get("numeroExtension"),"%"+ this.extension+"%"));
        }

        //predicates.add(criteriaBuilder.equal(root.get("cveEstatus"),"VIG"));
        //predicates.add(criteriaBuilder.equal(root.get("cveEstatusEntePublico"),"VIG"));


        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
