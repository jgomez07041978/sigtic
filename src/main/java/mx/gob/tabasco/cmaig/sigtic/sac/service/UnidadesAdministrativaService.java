package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.Arbol;
import mx.gob.tabasco.cmaig.sigtic.sac.model.NivelUnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sac.model.SacCatUnidadAdministrativaView;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UnidadesAdministrativaService {

    List<Arbol> getUnidadAdministrativaAll(Long id);

    List<Arbol> getUnidadAdministrativaAllFree(Long id);

    List<Arbol> getUnidadAdministrativaAllPorInstitucionFree(Long id);

    List<Arbol> getUnidadAdministrativaAllPorInstitucion(Long id);

    List<Arbol> getUnidadAdministrativaPresupuestal(Long id);

    List<Arbol> getUnidadAdministrativaAllPorUsuario(Long id);

    UnidadAdministrativa getUnidadAdministrativaId(Long id);
    List<UnidadAdministrativa> getUnidadAdministrativaPorEnteId(Long id);

    List<NivelUnidadAdministrativa> getAllNivelUnidadAdministrativa(String cveEstatus);

    UnidadAdministrativa guardarUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa);

    UnidadAdministrativa deleteUnidadAdministrativa(Long id);

    List<SacCatUnidadAdministrativaView> getSacUnidadAdministrativaPorEnteId(Long id);

    void generaReporteUnidadesAdministrativas(Long idEntePublico, HttpServletResponse response);

    void exportarCatalogoUnidadesAdministrativas(Long idEntePublico, HttpServletResponse response);
}
