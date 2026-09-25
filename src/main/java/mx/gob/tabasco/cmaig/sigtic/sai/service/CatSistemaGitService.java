package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSistemaGit;

import java.util.List;

public interface CatSistemaGitService {

    List<CatSistemaGit> getListaSistemaGit(Long idSistema);

    CatSistemaGit guardarGit(CatSistemaGit catSistemaGit);

    CatSistemaGit deleteSistemaGit(CatSistemaGit catSistemaGit);

    CatSistemaGit getSistemaGitById(Long idSistemaGit);

}
