package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServiciosTic;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioTicSpecification implements Specification<ServiciosTic> {

    private Long idEntePublico;
    private String cveServicio;
    private String nombreServicio;


    public ServicioTicSpecification(Long idEntePublico, String cveServicio, String nombreServicio) {
        this.idEntePublico = idEntePublico;
        if (cveServicio!=null)
        this.cveServicio = cveServicio.trim().toUpperCase();
        if (nombreServicio!=null)
        this.nombreServicio = nombreServicio.trim().toUpperCase();
    }
    @Override
    public Predicate toPredicate(Root<ServiciosTic> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        Join<ServiciosTic, EntePublico> entePublico =
                root.join("entePublicoAdmin", JoinType.LEFT);

        if (this.idEntePublico != null) {
            predicates.add(
                    criteriaBuilder.equal(
                            entePublico.get("idEntePublico"),
                            this.idEntePublico
                    )
            );
        }

        /*Join<ServiciosTic, EntePublico> entePublico= root.join("entePublicoAdmin");

        if (this.idEntePublico != null ) {
            predicates.add(criteriaBuilder.equal(entePublico.get("idEntePublico").as(Long.class),this.idEntePublico.longValue()));
        }*/

        if (this.cveServicio != null && !this.cveServicio.equals("")) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(root.<String> get("cveServicioTic")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.cveServicio + "%"));
        }

        if (this.nombreServicio != null && !this.nombreServicio.equals("")) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(root.<String> get("nombreServicioTic")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.nombreServicio + "%"));
        }

            List<String> statusList = new ArrayList<>();
            statusList.add("VIG");
            statusList.add("HIS");


        predicates.add(criteriaBuilder.in(root.get("cveEstatus")).value(statusList));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
