package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosFirma;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSubtipoServiciosTic;
import mx.gob.tabasco.cmaig.sigtic.sai.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatSubtipoServiciosFirmaRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatSubtipoServiciosTicRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.EntePublicoRepository;
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
public class CatSubtipoServiciosFirmaServiceImp implements CatSubtipoServiciosFirmaService{


    @Autowired
    private CatSubtipoServiciosFirmaRepository catSubtipoServiciosFirmaRepository;

    @Autowired
    private CatSubtipoServiciosTicRepository catSubtipoServiciosTicRepository;

    @Autowired
    private EntePublicoRepository entePublicoRepository;

    @Override
    public List<CatSubtipoServiciosFirma> getSubtipoServicioFirmas(CatSubtipoServiciosTic catSubtipoServiciosTic) {
        CatSubtipoServiciosTic catSubtipoServiciosTic1= catSubtipoServiciosTicRepository.findById(catSubtipoServiciosTic.getIdSubtipoServicioTic()).get();

        return catSubtipoServiciosFirmaRepository.findAllByCatSubtipoServiciosTic(catSubtipoServiciosTic1);
    }

    @Override
    public CatSubtipoServiciosFirma guardarSubtipoServicioFirma(CatSubtipoServiciosFirma catSubtipoServiciosFirma) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (catSubtipoServiciosFirma.getNumeroFirmas()!=1 && catSubtipoServiciosFirma.getNumeroFirmas()!=2)
            throw new NoSuchElementException("El número de firmas soportados es 1 o 2");


        if (catSubtipoServiciosFirma.getIdSubtipoServicioFirma()!=null){
            Optional<CatSubtipoServiciosFirma> catSubtipoServiciosFirma1 = catSubtipoServiciosFirmaRepository.findById(catSubtipoServiciosFirma.getIdSubtipoServicioFirma());

            if (catSubtipoServiciosFirma1.isPresent()){
                catSubtipoServiciosFirma1.get().setNumeroFirmas(catSubtipoServiciosFirma.getNumeroFirmas());
                catSubtipoServiciosFirma1.get().setFechaActualizo(new Timestamp(new Date().getTime()));
                catSubtipoServiciosFirma1.get().setIdUsuarioActualizo(authUser.getIdUsuario());
                return catSubtipoServiciosFirmaRepository.save(catSubtipoServiciosFirma1.get());
            }
            else{
                throw new NoSuchElementException("La configuración de firma no existe");
            }
        }else{
            CatSubtipoServiciosTic catSubtipoServiciosTic = catSubtipoServiciosTicRepository.findById(catSubtipoServiciosFirma.getCatSubtipoServiciosTic().getIdSubtipoServicioTic()).get();
            EntePublico entePublico = entePublicoRepository.findById(catSubtipoServiciosFirma.getEntePublico().getIdEntePublico()).get();
            CatSubtipoServiciosFirma catSubtipoServiciosFirma1= new CatSubtipoServiciosFirma();
            catSubtipoServiciosFirma1.setNumeroFirmas(catSubtipoServiciosFirma.getNumeroFirmas());
            catSubtipoServiciosFirma1.setCatSubtipoServiciosTic(catSubtipoServiciosTic);
            catSubtipoServiciosFirma1.setEntePublico(entePublico);
            catSubtipoServiciosFirma1.setFechaRegistro(new Timestamp(new Date().getTime()));
            catSubtipoServiciosFirma1.setIdUsuarioRegistro(authUser.getIdUsuario());
            catSubtipoServiciosFirma1.setCveEstatus("VIG");

            return catSubtipoServiciosFirmaRepository.save(catSubtipoServiciosFirma1);
        }
    }

    @Override
    public void eliminarSubtipoServicioFirma(CatSubtipoServiciosFirma catSubtipoServiciosFirma) {
        Optional<CatSubtipoServiciosFirma> catSubtipoServiciosFirma1 = catSubtipoServiciosFirmaRepository.findById(catSubtipoServiciosFirma.getIdSubtipoServicioFirma());

        if (catSubtipoServiciosFirma1.isPresent())
            catSubtipoServiciosFirmaRepository.delete(catSubtipoServiciosFirma1.get());
        else
            throw new NoSuchElementException("La configuración de firma no existe");
    }

    @Override
    public CatSubtipoServiciosFirma getSubtipoServicioFirmaId(Long idSubtipoServicioFirma) {
        return catSubtipoServiciosFirmaRepository.findById(idSubtipoServicioFirma).get();
    }
    @Override
    public CatSubtipoServiciosFirma getSubtipoServicioFirmaEnteId(Long idEnte,Long idSubtipoServicio) {
        EntePublico entePublico =entePublicoRepository.findById(idEnte).get();
        CatSubtipoServiciosTic catSubtipoServiciosTic =  catSubtipoServiciosTicRepository.findById(idSubtipoServicio).get();
        Optional<CatSubtipoServiciosFirma> catSubtipoServiciosFirma=catSubtipoServiciosFirmaRepository.findByEntePublicoAndCatSubtipoServiciosTicAndCveEstatus(entePublico, catSubtipoServiciosTic, "VIG");

        if (catSubtipoServiciosFirma.isPresent())
            return catSubtipoServiciosFirma.get();
        else
            return new CatSubtipoServiciosFirma();
    }


}
