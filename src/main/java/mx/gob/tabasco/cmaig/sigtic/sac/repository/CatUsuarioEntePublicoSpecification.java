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

public class CatUsuarioEntePublicoSpecification implements Specification<CatUsuarioEntePublico>{

    private Long idEntePublico;
    private String rfc;
    private String curp;
    private String nombre;
    private String segundoNombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Boolean isVigente;
    private Boolean isHistorico;
    private Boolean isNoVigente;

    public CatUsuarioEntePublicoSpecification(Long idEntePublico, String rfc, String curp, String nombre, String segundoNombre, String apellidoPaterno, String apellidoMaterno, Boolean isVigente, Boolean isHistorico, Boolean isNoVigente) {
        this.idEntePublico = idEntePublico;
        this.rfc = rfc;
        this.curp = curp;
        if (nombre!=null)
            this.nombre = Utileria.quitarAcentos(nombre.toUpperCase());
        if (segundoNombre!=null)
            this.segundoNombre = Utileria.quitarAcentos(segundoNombre.toUpperCase());
        if (apellidoPaterno!=null)
            this.apellidoPaterno = Utileria.quitarAcentos(apellidoPaterno.toUpperCase());
        if (apellidoMaterno!=null)
            this.apellidoMaterno = Utileria.quitarAcentos(apellidoMaterno.toUpperCase());
        this.isVigente = isVigente;
        this.isHistorico = isHistorico;
        this.isNoVigente = isNoVigente;
    }

    @Override
    public Predicate toPredicate(Root<CatUsuarioEntePublico> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        Join<CatUsuarioEntePublico, EntePublico> entePublico= root.join("entePublico");
        Join<CatUsuarioEntePublico,   CatUsuario> catUsuarioJoin = root.join("catUsuario");

        if (this.idEntePublico != null && !this.idEntePublico.equals("")) {
            predicates.add(criteriaBuilder.equal(entePublico.get("idEntePublico").as(Long.class),this.idEntePublico));
        }

        if (this.rfc != null && !this.rfc.equals("")) {
            predicates.add(criteriaBuilder.like(catUsuarioJoin.get("rfc").as(String.class),"%"+this.rfc.toUpperCase()+"%"));
        }

        if (this.curp != null && !this.curp.equals("")) {
            predicates.add(criteriaBuilder.like(catUsuarioJoin.get("curp").as(String.class),"%"+this.curp.toUpperCase()+"%"));
        }

        if (this.nombre != null && !this.nombre.equals("")) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(root.<String> get("nombre")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.nombre + "%"));
        }

        if (this.segundoNombre != null && !this.segundoNombre.equals("")) {
            Expression<String> func = criteriaBuilder.function("translate", String.class, criteriaBuilder.upper(root.<String> get("segundoNombre")),
                    criteriaBuilder.literal("áàâäÁÀÂÄéèêëÉÈÊËíìïîÍÌÏÎóòöőôÓÒÖŐÔúùûüűÚÙÛÜŰ"), criteriaBuilder.literal("aaaaAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuuUUUUU"));
            predicates.add(criteriaBuilder.like(func, "%" + this.segundoNombre + "%"));
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

        List<String> statusList = new ArrayList<>();

        if (this.isVigente)
            statusList.add("VIG");


        predicates.add(criteriaBuilder.in(catUsuarioJoin.get("cveEstatus")).value(statusList));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
