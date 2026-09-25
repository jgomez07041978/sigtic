package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoServiciosTic;
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
public class CatSubtipoServiciosTicServiceImp implements CatSubtipoServiciosTicService{

    @Autowired
    private CatSubtipoServiciosTicRepository catSubtipoServiciosTicRepository;

    @Autowired
    private CatTipoServiciosTicRepository catTipoServiciosTicRepository;

    @Override
    public List<CatSubtipoServiciosTic> getCatSubtipoServiciosTic(Long idTipoServicio){
        CatTipoServiciosTic catTipoServiciosTic = catTipoServiciosTicRepository.findById(idTipoServicio).get();
        return catSubtipoServiciosTicRepository.findAllByCatTipoServiciosTicAndCveEstatusOrderByNombreSubtipoServicioTic(catTipoServiciosTic, "VIG");
    }

    @Override
    public List<CatSubtipoServiciosTic> getCatSubtipoServiciosTicCr(Long idTipoServicio) {
        CatTipoServiciosTic catTipoServiciosTic = catTipoServiciosTicRepository.findById(idTipoServicio).get();
        return catSubtipoServiciosTicRepository.findAllByCatTipoServiciosTicAndCveEstatusAndGeneraCrOrderByNombreSubtipoServicioTic(catTipoServiciosTic, "VIG","S");
    }

    @Override
    public  List<CatSubtipoServiciosTic> getCatSubtipoServiciosTicAll(){
        //return catSubtipoServiciosTicRepository.findAllByCveEstatusOrderByCatTipoServiciosTicAscNumeroOrdenSubtipoServicioTicAsc("VIG");
        return catSubtipoServiciosTicRepository.getAllSubtipoServicio();
    }

    @Override
    public  CatSubtipoServiciosTic guardarSubtipoServiciosTic(CatSubtipoServiciosTic catSubtipoServiciosTic){
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CatSubtipoServiciosTic catSubtipoServiciosTicBd = null;

        if(catSubtipoServiciosTic.getCatTipoServiciosTic().getIdTipoServicioTic()==null)
            throw new NoSuchElementException("Se requiere el tipo de servicio");

        Optional<CatTipoServiciosTic> catTipoServiciosTicOp = catTipoServiciosTicRepository.findById(catSubtipoServiciosTic.getCatTipoServiciosTic().getIdTipoServicioTic());

        if (!catTipoServiciosTicOp.isPresent())
            throw new   NoSuchElementException("El tipo de servicio no existe");

        if(catSubtipoServiciosTic.getIdSubtipoServicioTic()!=null){
            catSubtipoServiciosTicBd = catSubtipoServiciosTicRepository.findById(catSubtipoServiciosTic.getIdSubtipoServicioTic()).get();
            catSubtipoServiciosTicBd.setCveSubtipoServicioTic(catSubtipoServiciosTic.getCveSubtipoServicioTic());
            catSubtipoServiciosTicBd.setNombreSubtipoServicioTic(catSubtipoServiciosTic.getNombreSubtipoServicioTic());
            catSubtipoServiciosTicBd.setNombreSubtipServicioTicAbr(catSubtipoServiciosTic.getNombreSubtipServicioTicAbr());
            catSubtipoServiciosTicBd.setNumeroOrdenSubtipoServicioTic(catSubtipoServiciosTic.getNumeroOrdenSubtipoServicioTic());
            catSubtipoServiciosTicBd.setCveEstatus(catSubtipoServiciosTic.getCveEstatus());
            catSubtipoServiciosTicBd.setControlInventario(catSubtipoServiciosTic.getControlInventario());
            catSubtipoServiciosTicBd.setControlIp(catSubtipoServiciosTic.getControlIp());
            catSubtipoServiciosTicBd.setControlPuerto(catSubtipoServiciosTic.getControlPuerto());
            catSubtipoServiciosTicBd.setControlUsuarios(catSubtipoServiciosTic.getControlUsuarios());
            catSubtipoServiciosTicBd.setGeneraCr(catSubtipoServiciosTic.getGeneraCr());
            catSubtipoServiciosTicBd.setCrFirmada(catSubtipoServiciosTic.getCrFirmada());
        } else{
            catSubtipoServiciosTicBd = catSubtipoServiciosTic;
            catSubtipoServiciosTicBd.setFechaRegistro(new Timestamp(new Date().getTime()));
        }

        catSubtipoServiciosTicBd.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        catSubtipoServiciosTicBd.setCveUsuarioMtto(authUser.getUsername());
        catSubtipoServiciosTicBd.setCatTipoServiciosTic(catTipoServiciosTicOp.get());

        return catSubtipoServiciosTicRepository.save(catSubtipoServiciosTicBd);
    }
    @Override
    public  CatSubtipoServiciosTic getSubtipoServiciosTic(Long idSubtipoServicioTic){
        Optional <CatSubtipoServiciosTic> catSubtipoServiciosTicOp = catSubtipoServiciosTicRepository.findById(idSubtipoServicioTic);

        if(!catSubtipoServiciosTicOp.isPresent()) throw new NoSuchElementException("El Subtipo no existe");
        return catSubtipoServiciosTicOp.get();
    }

    @Override
    public CatSubtipoServiciosTic deleteSubTipoServicio(CatSubtipoServiciosTic catSubtipoServiciosTic) {

        Optional<CatSubtipoServiciosTic> catSubTipoServiciosTicOp = catSubtipoServiciosTicRepository.findById(catSubtipoServiciosTic.getIdSubtipoServicioTic());

        if (!catSubTipoServiciosTicOp.isPresent())
            throw new NoSuchElementException("El subtipo de servicio no existe");

        catSubTipoServiciosTicOp.get().setCveEstatus("NOV");
        catSubTipoServiciosTicOp.get().setFechaUltimoCambio(new Timestamp(new Date().getTime()));

        return catSubtipoServiciosTicRepository.save( catSubTipoServiciosTicOp.get());
    }

}
