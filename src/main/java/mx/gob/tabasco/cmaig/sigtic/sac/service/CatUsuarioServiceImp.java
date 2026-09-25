package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class CatUsuarioServiceImp implements CatUsuarioService {

    @Autowired
    private CatUsuarioRepository catUsuarioRepository;

    @Autowired
    private CatUsuarioEntePublicoRepository catUsuarioEntePublicoRepository;

    @Override
    public List<CatUsuario> getUsuariosFiltro(CatUsuario catUsuario) {
        CatUsuarioSpecification specUSuario = new CatUsuarioSpecification(catUsuario.getRfc(),catUsuario.getCurp(),catUsuario.getNombre(),catUsuario.getSegundoNombre(),
                catUsuario.getApellidoPaterno(),catUsuario.getApellidoMaterno(), catUsuario.getIsVigente(), catUsuario.getIsHistorico(), catUsuario.getIsNoVigente());

        return catUsuarioRepository.findAll(specUSuario, Sort.by("nombre","segundoNombre","apellidoPaterno","apellidoMaterno","curp"));
    }

    @Override
    public List<CatUsuarioEntePublico> getUsuariosFiltroPorDependencia(CatUsuarioEntePublico catUsuarioEntePublico) {

        CatUsuarioEntePublicoSpecification specUSuarioEntePublico = new CatUsuarioEntePublicoSpecification(catUsuarioEntePublico.getEntePublico().getIdEntePublico(),catUsuarioEntePublico.getCatUsuario().getRfc(),catUsuarioEntePublico.getCatUsuario().getCurp(),catUsuarioEntePublico.getCatUsuario().getNombre(),catUsuarioEntePublico.getCatUsuario().getSegundoNombre(),
                catUsuarioEntePublico.getCatUsuario().getApellidoPaterno(),catUsuarioEntePublico.getCatUsuario().getApellidoMaterno(), catUsuarioEntePublico.getCatUsuario().getIsVigente(), catUsuarioEntePublico.getCatUsuario().getIsHistorico(), catUsuarioEntePublico.getCatUsuario().getIsNoVigente());

        return catUsuarioEntePublicoRepository.findAll(specUSuarioEntePublico, Sort.by("catUsuario.nombre","catUsuario.segundoNombre","catUsuario.apellidoPaterno","catUsuario.apellidoMaterno","catUsuario.curp"));
    }

    @Override
    public CatUsuario getUsuario(Long idUsuario) {
        return catUsuarioRepository.findById(idUsuario).get();
    }

    @Override
    public List<CatUsuario> getUsuariosSinDependencia() {
        return catUsuarioRepository.getUsuariosSinDependencia();
    }

    @Override
    public List<CatUsuarioEntePublico> listaUsuariosPorRfc(List<String> lista) {
        return catUsuarioEntePublicoRepository.getListaUsuariosPorCurp(lista);
    }

    @Override
    public List<CatUsuario> getUsuarios(CatUsuario catUsuario) {

        List<String> listaEstatus=new ArrayList<>();

        if (catUsuario.getIsVigente())
            listaEstatus.add("VIG");

        if (catUsuario.getIsNoVigente())
            listaEstatus.add("NOV");

        if (catUsuario.getIsHistorico())
            listaEstatus.add("HIS");

            return catUsuarioRepository.findAllByCveEstatusInOrderByNombreAscSegundoNombreAscApellidoPaternoAscApellidoMaternoAscCurpAsc(listaEstatus);
    }


    @Override
    public CatUsuario saveCatUsuario(CatUsuario catUsuario) {


        catUsuario.setRfc(catUsuario.getRfc().trim().toUpperCase());
        catUsuario.setCurp(catUsuario.getCurp().trim().toUpperCase());

        if (catUsuario.getRfc().length()!=10 && catUsuario.getRfc().length()!=13)
            throw new NoSuchElementException("El RFC debe ser de 10 caracteres si no cuenta con homoclave y de 13 caracteres si cuenta con homoclave");

        if (catUsuario.getCurp().length()!=18)
            throw new NoSuchElementException("La CURP debe ser de 18 caracteres");

        Optional<CatUsuario> catUsuarioRfcOp = catUsuarioRepository.findByRfc(catUsuario.getRfc());
        Optional<CatUsuario> catUsuarioCurpOp = catUsuarioRepository.findByCurp(catUsuario.getCurp());

        if (catUsuario.getIdUsuario()==null){
            if (catUsuarioRfcOp.isPresent())
                throw new NoSuchElementException("El RFC esta registrado con otro usuario");

            if (catUsuarioCurpOp.isPresent())
                throw new NoSuchElementException("La CURP esta registrada con otro usuario");

            catUsuario.setTotalEntePublico(0);

            catUsuario.setCveEstatus(catUsuario.getCveEstatus());

            return catUsuarioRepository.save(catUsuario);
        }else{
            Optional<CatUsuario> catUsuarioOP= catUsuarioRepository.findById(catUsuario.getIdUsuario());

            if (!catUsuarioOP.isPresent())
                throw new NoSuchElementException("El usuario no existe");

            if (catUsuarioOP.get().getRfc()!=null && !catUsuarioOP.get().getRfc().equals(catUsuario.getRfc()) && catUsuarioRfcOp.isPresent())
                throw new NoSuchElementException("El RFC esta registrado con otro usuario");

            if (catUsuarioOP.get().getCurp()!=null && !catUsuarioOP.get().getCurp().equals(catUsuario.getCurp()) && catUsuarioCurpOp.isPresent())
                throw new NoSuchElementException("La CURP esta registrada con otro usuario");

            LocalDateTime currentTime = LocalDateTime.now();
            System.out.println("Current time (UTC): " + currentTime);

            catUsuarioOP.get().setRfc(catUsuario.getRfc());
            catUsuarioOP.get().setCurp(catUsuario.getCurp());
            catUsuarioOP.get().setNombre(catUsuario.getNombre());
            catUsuarioOP.get().setSegundoNombre(catUsuario.getSegundoNombre());
            catUsuarioOP.get().setApellidoPaterno(catUsuario.getApellidoPaterno());
            catUsuarioOP.get().setApellidoMaterno(catUsuario.getApellidoMaterno());
            catUsuarioOP.get().setFechaNacimiento(catUsuario.getFechaNacimiento());
            catUsuarioOP.get().setCveSexo(catUsuario.getCveSexo());
            catUsuarioOP.get().setCveEstatus(catUsuario.getCveEstatus());
            catUsuarioOP.get().setNumCelular(catUsuario.getNumCelular());
            catUsuarioOP.get().setNumTelefonoPersonal(catUsuario.getNumTelefonoPersonal());
            catUsuarioOP.get().setCorreoPersonal(catUsuario.getCorreoPersonal());

            return catUsuarioRepository.save(catUsuarioOP.get());
        }
    }

    @Override
    public CatUsuario deleteUsuario(CatUsuario catUsuario) {
        Optional<CatUsuario> catUsuarioOp= catUsuarioRepository.findById(catUsuario.getIdUsuario());

        if (!catUsuarioOp.isPresent())
            throw new NoSuchElementException("El registro no existe");

        if (catUsuarioOp.get().getCveEstatus().equals("NOV"))
            throw new NoSuchElementException("El usuario esta eliminado");

        catUsuarioOp.get().setCveEstatus("NOV");

        return catUsuarioRepository.save(catUsuarioOp.get());
    }

    @Override
    public Optional<CatUsuario> getUsuarioCurp(String curp) {
        return catUsuarioRepository.findByCurp(curp);
    }

    @Override
    public Optional<CatUsuario> getUsuarioRfc(String rfc) {
        return catUsuarioRepository.findByRfc(rfc);
    }

    @Override
    public  List<CatUsuarioEntePublico> getListaUsuarioPorCurp(String curp) {
        return catUsuarioEntePublicoRepository.getListaUsuariosPorCurp(curp);
    }



}
