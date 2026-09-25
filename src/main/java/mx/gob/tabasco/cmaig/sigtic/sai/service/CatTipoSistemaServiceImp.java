package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoSistema;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatTipoSistemaRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import net.minidev.json.JSONObject;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CatTipoSistemaServiceImp implements CatTipoSistemaService {

    @Autowired
    private CatTipoSistemaRepository catTipoSistemaRepository;

    @Override
    public List<CatTipoSistema> getListaTipoSistema() {
        return catTipoSistemaRepository.findAllByCveEstatusOrderByNumeroOrdenTipoSistemaAsc("VIG");
    }

    /*@Override
    public  List<CatTipoSistema> getListaTipoSistemaAll(){
        return catTipoSistemaRepository.findAllByCveEstatusOrderByNumeroOrdenTipoSistema("VIG");
    }*/

    @Override
    public CatTipoSistema guardarTipoSistemas(CatTipoSistema catTipoSistema){
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (catTipoSistema.getOpcion().equals("Nuevo")){
            Optional<CatTipoSistema> catTipoSistemaOp = catTipoSistemaRepository.findById(catTipoSistema.getCveTipoSistema());
            if (catTipoSistemaOp.isPresent())
                throw new NoSuchElementException("El tipo de sistema ya existe.");
            else{
                CatTipoSistema catTipoSistemaNew=new CatTipoSistema();
                catTipoSistemaNew.setCveTipoSistema(catTipoSistema.getCveTipoSistema());
                catTipoSistemaNew.setNombreTipoSistema(catTipoSistema.getNombreTipoSistema());
                catTipoSistemaNew.setNombreTipoSistemaAbreviado(catTipoSistema.getNombreTipoSistemaAbreviado());
                catTipoSistemaNew.setAlcanceSistema(catTipoSistema.getAlcanceSistema());
                catTipoSistemaNew.setNumeroOrdenTipoSistema(catTipoSistema.getNumeroOrdenTipoSistema());
                catTipoSistemaNew.setCveEstatus(catTipoSistema.getCveEstatus());
                catTipoSistemaNew.setUsuarioMtto(authUser.getUsername());
                catTipoSistemaNew.setFechaRegistro(new Timestamp(new Date().getTime()));
                catTipoSistemaNew.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
                catTipoSistemaNew.setRequiereSistemaPadre(catTipoSistema.getRequiereSistemaPadre());

                return catTipoSistemaRepository.save(catTipoSistemaNew);
            }

        }else{
            Optional<CatTipoSistema> catTipoSistemaOp = catTipoSistemaRepository.findById(catTipoSistema.getCveTipoSistema());
            if (catTipoSistemaOp.isPresent()){
                catTipoSistemaOp.get().setNombreTipoSistema(catTipoSistema.getNombreTipoSistema());
                catTipoSistemaOp.get().setNombreTipoSistemaAbreviado(catTipoSistema.getNombreTipoSistemaAbreviado());
                catTipoSistemaOp.get().setAlcanceSistema(catTipoSistema.getAlcanceSistema());
                catTipoSistemaOp.get().setNumeroOrdenTipoSistema(catTipoSistema.getNumeroOrdenTipoSistema());
                catTipoSistemaOp.get().setCveEstatus(catTipoSistema.getCveEstatus());
                catTipoSistemaOp.get().setUsuarioMtto(authUser.getUsername());
                catTipoSistemaOp.get().setFechaUltimoCambio(new Timestamp(new Date().getTime()));
                catTipoSistemaOp.get().setRequiereSistemaPadre(catTipoSistema.getRequiereSistemaPadre());
                return catTipoSistemaRepository.save(catTipoSistemaOp.get());

            }else
                throw new NoSuchElementException("El tipo de sistema no existe");


        }

    }

    @Override
    public CatTipoSistema getTipoSistemaCve(String cveTipoSistema) {
        return catTipoSistemaRepository.findById(cveTipoSistema).get();
    }

    @Override
    public CatTipoSistema deleteTipoSistema(CatTipoSistema catTipoSistema){
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Optional<CatTipoSistema> catTipoSistemaOp = catTipoSistemaRepository.findById(catTipoSistema.getCveTipoSistema());
            if (catTipoSistemaOp.isPresent()){
                catTipoSistemaOp.get().setCveEstatus("NOV");
                catTipoSistemaOp.get().setUsuarioMtto(authUser.getUsername());
                catTipoSistemaOp.get().setFechaUltimoCambio(new Timestamp(new Date().getTime()));
                return catTipoSistemaRepository.save(catTipoSistemaOp.get());

            }else
                throw new NoSuchElementException("El tipo de sistema no existe");


    }

}
