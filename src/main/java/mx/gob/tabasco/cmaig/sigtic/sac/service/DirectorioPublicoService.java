package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.Utileria.Utileria;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.pojo.DirectorioContacto;
import mx.gob.tabasco.cmaig.sigtic.sac.pojo.DirectorioFiltro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.text.Collator;
import java.util.*;

@Service
@Transactional(value = "catalogosTransactionManager", readOnly = true)
public class DirectorioPublicoService {
    @PersistenceContext(unitName = "catalogo")
    private EntityManager entityManager;

    public Page<DirectorioContacto> buscar(DirectorioFiltro filtro) {
        Specification<CatUsuarioEntePublico> specification = (root, query, cb) -> {
            boolean contentQuery = query.getResultType() != Long.class && query.getResultType() != long.class;
            Join<Object, Object> usuario = root.join("catUsuario", JoinType.INNER);
            Join<Object, Object> ente = contentQuery || filtro.getIdEntePublico() != null
                    ? root.join("entePublico", JoinType.LEFT) : null;
            Join<Object, Object> unidad = contentQuery || filtro.getIdUnidadAdministrativa() != null
                    ? root.join("unidadAdministrativa", JoinType.LEFT) : null;
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(root.get("cveEstatus").in("VIG", "HIS"));
            if (filtro.getEstado() != null) predicates.add(cb.equal(root.get("cveEstatus"), filtro.getEstado()));
            if (filtro.getIdEntePublico() != null)
                predicates.add(cb.equal(ente.get("idEntePublico"), filtro.getIdEntePublico()));
            if (filtro.getIdUnidadAdministrativa() != null)
                predicates.add(cb.equal(unidad.get("idUnidadAdministrativa"), filtro.getIdUnidadAdministrativa()));
            Expression<String> nombre = cb.trim(cb.concat(cb.concat(cb.concat(
                    cb.coalesce(usuario.get("nombre"), ""), cb.concat(" ", cb.coalesce(usuario.get("segundoNombre"), ""))),
                    cb.concat(" ", cb.coalesce(usuario.get("apellidoPaterno"), ""))),
                    cb.concat(" ", cb.coalesce(usuario.get("apellidoMaterno"), ""))));
            nombre = cb.function("REGEXP_REPLACE", String.class, nombre, cb.literal("[[:space:]]+"), cb.literal(" "));
            if (StringUtils.hasText(filtro.getNombre())) {
                Expression<String> normalized = cb.function("TRANSLATE", String.class, cb.upper(nombre),
                        cb.literal("ÁÀÂÄÉÈÊËÍÌÎÏÓÒÔÖÚÙÛÜÑ"), cb.literal("AAAAEEEEIIIIOOOOUUUUN"));
                String input = Utileria.quitarAcentos(filtro.getNombre().trim().toUpperCase(Locale.ROOT))
                        .replace('Ñ', 'N');
                // Match all words, even when second names intervene or surnames come first.
                for (String word : input.split("(?U)\\s+")) {
                    if (!word.isEmpty()) predicates.add(cb.like(normalized, pattern(word), '\\'));
                }
            }
            if (StringUtils.hasText(filtro.getTelefonoExtension())) {
                String input = pattern(filtro.getTelefonoExtension().trim());
                predicates.add(cb.or(
                        cb.like(root.get("numTelefonoInstitucional"), input, '\\'),
                        cb.like(root.get("numeroExtensionInstitucional"), input, '\\')));
            }
            if (StringUtils.hasText(filtro.getTelefono()))
                predicates.add(cb.like(root.get("numTelefonoInstitucional"), pattern(filtro.getTelefono().trim()), '\\'));
            if (StringUtils.hasText(filtro.getExtension()))
                predicates.add(cb.like(root.get("numeroExtensionInstitucional"), pattern(filtro.getExtension().trim()), '\\'));
            if (contentQuery) {
                // Select public scalars directly, without hydrating the eager entity graph.
                query.multiselect(root.get("idUsuarioEntePublico"), usuario.get("nombre"),
                        usuario.get("segundoNombre"), usuario.get("apellidoPaterno"),
                        usuario.get("apellidoMaterno"), root.get("puesto"),
                        ente.get("idEntePublico"), ente.get("nombreEntePublico"),
                        unidad.get("idUnidadAdministrativa"), unidad.get("nombreUnidadAdministrativa"),
                        root.get("numTelefonoInstitucional"), root.get("numeroExtensionInstitucional"),
                        root.get("correoInstitucional"), root.get("cveEstatus"));
                query.orderBy(cb.asc(cb.function("NLSSORT", String.class, nombre, cb.literal("NLS_SORT=SPANISH_AI"))),
                        cb.asc(root.get("idUsuarioEntePublico")));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        PageRequest pageable = PageRequest.of(filtro.getPagina(), filtro.getSize());
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DirectorioContacto> query = cb.createQuery(DirectorioContacto.class);
        Root<CatUsuarioEntePublico> root = query.from(CatUsuarioEntePublico.class);
        query.where(specification.toPredicate(root, query, cb));
        List<DirectorioContacto> content = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return PageableExecutionUtils.getPage(content, pageable, () -> {
            CriteriaQuery<Long> count = cb.createQuery(Long.class);
            Root<CatUsuarioEntePublico> countRoot = count.from(CatUsuarioEntePublico.class);
            count.select(cb.count(countRoot));
            count.where(specification.toPredicate(countRoot, count, cb));
            return entityManager.createQuery(count).getSingleResult();
        });
    }

    private static String pattern(String value) {
        return "%" + value.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%";
    }

    public Map<String, Object> opciones() {
        List<Map<String, Object>> entesPublicos = new ArrayList<>();
        entityManager.createQuery("SELECT e.idEntePublico, e.nombreEntePublico FROM EntePublico e", Object[].class)
                .setHint("org.hibernate.fetchSize", 500).getResultList()
                .forEach(ente -> entesPublicos.add(opcion((Long) ente[0], (String) ente[1], null)));
        List<Map<String, Object>> unidadesAdministrativas = new ArrayList<>();
        entityManager.createQuery("SELECT u.idUnidadAdministrativa, u.nombreUnidadAdministrativa, "
                + "u.idEntePublico, u.idUnidadAdministrativaPadre, u.cveUnidadAdministrativa "
                + "FROM UnidadAdministrativa u", Object[].class)
                .setHint("org.hibernate.fetchSize", 500).getResultList().forEach(unidad -> {
            Map<String, Object> item = opcion((Long) unidad[0], (String) unidad[1], (Long) unidad[2]);
            item.put("idPadre", unidad[3]);
            item.put("clave", unidad[4] == null ? "" : unidad[4]);
            unidadesAdministrativas.add(item);
        });
        Collator collator = Collator.getInstance(new Locale("es", "MX"));
        Comparator<Map<String, Object>> order = (a, b) -> collator.compare(a.get("nombre"), b.get("nombre"));
        entesPublicos.sort(order);
        unidadesAdministrativas.sort(order);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("entes", entesPublicos);
        response.put("unidades", unidadesAdministrativas);
        return response;
    }

    private static Map<String, Object> opcion(Long id, String nombre, Long ente) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", id);
        result.put("nombre", nombre == null ? "" : nombre);
        if (ente != null) result.put("idEntePublico", ente);
        return result;
    }
}
