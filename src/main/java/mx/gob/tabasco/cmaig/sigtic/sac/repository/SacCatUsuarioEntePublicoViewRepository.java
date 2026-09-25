package mx.gob.tabasco.cmaig.sigtic.sac.repository;

import mx.gob.tabasco.cmaig.sigtic.sac.model.SacCatUsuarioEntePublicoView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SacCatUsuarioEntePublicoViewRepository extends PagingAndSortingRepository<SacCatUsuarioEntePublicoView, Long>, JpaSpecificationExecutor<SacCatUsuarioEntePublicoView> {

}
