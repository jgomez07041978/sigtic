package mx.gob.tabasco.cmaig.sigtic.sai.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.UsuarioServicio;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioServicioSpecification implements Specification<UsuarioServicio> {

    private Long idEntePublico;
    private String cveServicio;
    private String nombreServicio;
    private String curp;


    public UsuarioServicioSpecification(Long idEntePublico, String cveServicio, String nombreServicio, String curp) {
        this.idEntePublico = idEntePublico;
        if (cveServicio!=null)
        this.cveServicio = cveServicio.trim().toUpperCase();
        if (nombreServicio!=null)
        this.nombreServicio = nombreServicio.trim().toUpperCase();
        if (curp!=null)
            this.curp = curp;
    }
    @Override
    public Predicate toPredicate(Root<UsuarioServicio> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (this.curp != null && !this.curp.equals("")) {
            predicates.add(criteriaBuilder.equal(root.get("curpUsuarioServicio").as(String.class), this.curp));
        }

        Join<UsuarioServicio, ServiciosTic> servicio= root.join("serviciosTic");


        Join<ServiciosTic, EntePublico> entePublico= servicio.join("entePublicoAdmin");

        if (this.idEntePublico != null && !this.idEntePublico.equals("")) {
            predicates.add(criteriaBuilder.equal(entePublico.get("idEntePublico").as(Long.class),this.idEntePublico.longValue()));
        }

        if (this.cveServicio != null && !this.cveServicio.equals("")) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(servicio.<String> get("cveServicioTic")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.cveServicio + "%"));
        }

        if (this.nombreServicio != null && !this.nombreServicio.equals("")) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(servicio.<String> get("nombreServicioTic")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.nombreServicio + "%"));
        }

            List<String> statusList = new ArrayList<>();
            statusList.add("VIG");
            statusList.add("HIS");

        predicates.add(criteriaBuilder.in(servicio.get("cveEstatus")).value(statusList));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
