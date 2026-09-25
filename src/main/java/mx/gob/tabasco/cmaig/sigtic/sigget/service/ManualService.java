package mx.gob.tabasco.cmaig.sigtic.sigget.service;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.GrupoManual;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Manual;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ManualService {

    List<Manual> getManualesTipo(String tipo);

    List<Manual> getManuales();

    List<GrupoManual> getGrupos();

    GrupoManual guardarGrupo(GrupoManual grupoManual);

    GrupoManual deleteGrupo(GrupoManual grupoManual);

    GrupoManual getGrupoId(Integer idGrupoManual);

    List<Manual> getManualesGrupo(Integer  idGrupoManual);

    Manual guardarManual(Manual manual);

    Manual deleteManual(Manual manual);

    Manual getManuald(Long idManual);

    void exportManual(HttpServletResponse response, Long id);
}
