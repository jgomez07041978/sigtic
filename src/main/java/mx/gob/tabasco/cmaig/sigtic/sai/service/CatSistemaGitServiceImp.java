package mx.gob.tabasco.cmaig.sigtic.sai.service;


import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatSistemaGitRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatSistemaRepository;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class CatSistemaGitServiceImp implements CatSistemaGitService{

    private final CatSistemaRepository catSistemaRepository;
    private final CatSistemaGitRepository catSistemaGitRepository;
    private final BitacoraMovimientoService bitacoraMovimientoService;

    public CatSistemaGitServiceImp(CatSistemaRepository catSistemaRepository, CatSistemaGitRepository catSistemaGitRepository, BitacoraMovimientoService bitacoraMovimientoService) {
        this.catSistemaRepository = catSistemaRepository;
        this.catSistemaGitRepository = catSistemaGitRepository;
        this.bitacoraMovimientoService = bitacoraMovimientoService;
    }

    @Override
    public List<CatSistemaGit> getListaSistemaGit(Long idSistema) {

        return catSistemaGitRepository.findAllByCatSistema_idSistemaAndCveEstatus(idSistema,"VIG");
    }

    @Override
    public CatSistemaGit guardarGit(CatSistemaGit catSistemaGit) {

        CatSistemaGit catSistemaGitBd = null;

        if (catSistemaGit.getIdSistemaGit()!=null){
            catSistemaGitBd = catSistemaGitRepository.findById(catSistemaGit.getIdSistemaGit()).get();
            catSistemaGitBd.setCodigoGit(catSistemaGit.getCodigoGit());
            catSistemaGitBd.setDescripcion(catSistemaGit.getDescripcion());

            bitacoraMovimientoService.guardarBitacoraMovimiento(catSistemaGitBd.getIdSistemaGit(), "CAT_SISTEMA_GIT", "Se actualiza repository Git");

            return catSistemaGitRepository.save(catSistemaGitBd);
        }
        else {
            catSistemaGitBd = catSistemaGit;

            Optional<CatSistemaGit> sistemaGitExiste = catSistemaGitRepository.findByCatSistema_idSistemaAndCodigoGit(catSistemaGit.getCatSistema().getIdSistema(), catSistemaGit.getCodigoGit().trim());

            if (sistemaGitExiste.isPresent()) {
                throw new NoSuchElementException("El código git ya existe en el sistema");
            }

            CatSistema catSistema = catSistemaRepository.findById(catSistemaGit.getCatSistema().getIdSistema()).orElseThrow(() -> new NoSuchElementException("El sistema no existe"));

            catSistemaGitBd.setCatSistema(catSistema);
            catSistemaGitBd.setFechaRegistro(new Timestamp(new Date().getTime()));
            catSistemaGitBd.setCveEstatus("VIG");
            CatSistemaGit catSistemaGitNew = catSistemaGitRepository.save(catSistemaGitBd);

            bitacoraMovimientoService.guardarBitacoraMovimiento(catSistemaGitNew.getIdSistemaGit(), "CAT_SISTEMA_GIT", "Nuevo repository Git");

        return catSistemaGitNew;
        }

    }


    @Override
    public CatSistemaGit deleteSistemaGit(CatSistemaGit catSistemaGit) {

        CatSistemaGit catSistemaGitDelete = catSistemaGitRepository.findById(catSistemaGit.getIdSistemaGit()).orElseThrow(() -> new NoSuchElementException("Lo datos del repositorio Git, no existen"));

        catSistemaGitDelete.setCveEstatus("NOV");
        catSistemaGitDelete=catSistemaGitRepository.save(catSistemaGitDelete);

        bitacoraMovimientoService.guardarBitacoraMovimiento(catSistemaGitDelete.getIdSistemaGit(), "CAT_SISTEMA_GIT", "Borrado lógico");
        return catSistemaGitDelete;
    }

    @Override
    public CatSistemaGit getSistemaGitById(Long idSistemaGit) {

        CatSistemaGit catSistemaGit = catSistemaGitRepository.findById(idSistemaGit).orElseThrow(() -> new NoSuchElementException("Lo datos del repositorio Git, no existen"));

        return catSistemaGit;
    }



}
