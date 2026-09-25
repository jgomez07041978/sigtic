package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatSubtipoServiciosTicRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatTipoServiciosTicRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CatTipoServiciosTicServiceImp implements CatTipoServiciosTicService {

    @Autowired
    private CatTipoServiciosTicRepository catTipoServiciosTicRepository;

    @Override
    public CatTipoServiciosTic getCatTipoServiciosTic(Long idTipoServicioTic){
        return catTipoServiciosTicRepository.findById(idTipoServicioTic).get();
    }

    @Override
    public List<CatTipoServiciosTic> getCatTipoServiciosTicAll(){
        return catTipoServiciosTicRepository.findAllByCveEstatusOrCveEstatusOrderByNumOrdenTipoServicioTic("VIG", "HIS");
    }

    @Override
    public List<CatTipoServiciosTic> getCatTipoServiciosTicAllCr(){
        return catTipoServiciosTicRepository.getTiposServiciosCr();
    }

    @Override
    public CatTipoServiciosTic guardarTipoServicio(CatTipoServiciosTic catTipoServiciosTic) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CatTipoServiciosTic catTipoServiciosTicSave = null;


        if (catTipoServiciosTic.getIdTipoServicioTic()!=null){
            catTipoServiciosTicSave = catTipoServiciosTicRepository.findById(catTipoServiciosTic.getIdTipoServicioTic()).get();
            catTipoServiciosTicSave.setNombreTipoServicioTic(catTipoServiciosTic.getNombreTipoServicioTic());
            catTipoServiciosTicSave.setNombreTipoServicioTicAbrev(catTipoServiciosTic.getNombreTipoServicioTicAbrev());
            catTipoServiciosTicSave.setNumOrdenTipoServicioTic(catTipoServiciosTic.getNumOrdenTipoServicioTic());
            catTipoServiciosTicSave.setCveEstatus(catTipoServiciosTic.getCveEstatus());

        }
        else
        {
            catTipoServiciosTicSave = catTipoServiciosTic;
            catTipoServiciosTicSave.setFechaRegistro(new Timestamp(new Date().getTime()));
        }

        catTipoServiciosTicSave.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        catTipoServiciosTicSave.setCveUsuarioMtto(authUser.getUsername());

        return catTipoServiciosTicRepository.save(catTipoServiciosTicSave);
    }

    @Override
    public CatTipoServiciosTic deleteTipoServicio(CatTipoServiciosTic catTipoServiciosTic) {

        Optional<CatTipoServiciosTic> catTipoServiciosTicOp = catTipoServiciosTicRepository.findById(catTipoServiciosTic.getIdTipoServicioTic());

        if (!catTipoServiciosTicOp.isPresent())
            throw new NoSuchElementException("El tipo de servicio no existe");

        catTipoServiciosTicOp.get().setCveEstatus("NOV");
        catTipoServiciosTicOp.get().setFechaUltimoCambio(new Timestamp(new Date().getTime()));

        return catTipoServiciosTicRepository.save( catTipoServiciosTicOp.get());
    }

}
