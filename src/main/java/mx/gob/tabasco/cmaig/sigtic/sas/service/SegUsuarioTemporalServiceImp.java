package mx.gob.tabasco.cmaig.sigtic.sas.service;

import mx.gob.tabasco.cmaig.sigtic.Utileria.TokenUtils;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.service.CatUsuarioEntePublicoService;
import mx.gob.tabasco.cmaig.sigtic.sac.service.CatUsuarioService;
import mx.gob.tabasco.cmaig.sigtic.sac.service.EntePublicoService;
import mx.gob.tabasco.cmaig.sigtic.sas.model.*;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.*;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class SegUsuarioTemporalServiceImp implements SegUsuarioTemporalService{

    @Autowired
    private SegUsuarioTemporalRepository segUsuarioTemporalRepository;

    @Autowired
    private SegUsuarioRepository segUsuarioRepository;

    @Autowired
    private CatUsuarioService catUsuarioService;

    @Autowired
    private CatUsuarioEntePublicoService catUsuarioEntePublicoService;

    @Autowired
    private EntePublicoService entePublicoService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SegPerfilRepository segPerfilRepository;

    @Autowired
    private SegPerfilUsuarioRepository segPerfilUsuarioRepository;

    @Autowired
    private SegUsuarioTemporalRolRepository segUsuarioTemporalRolRepository;

    @Autowired
    private SegAccesoUsuarioRepository segAccesoUsuarioRepository;

    @Override
    public SegUsuarioTemporal guardarUsuario(SegUsuarioTemporal segUsuarioTemporal){

        SegUsuarioTemporal segUsuarioTemporal1= segUsuarioTemporalRepository.findById(segUsuarioTemporal.getIdUsuarioTemporal()).get();

        segUsuarioTemporal1.setCurp(segUsuarioTemporal.getCurp());
        segUsuarioTemporal1.setRfc(segUsuarioTemporal.getRfc());
        segUsuarioTemporal1.setIdEntePublico(segUsuarioTemporal.getIdEntePublico());
        segUsuarioTemporal1.setNombre(segUsuarioTemporal.getNombre());
        segUsuarioTemporal1.setSegundoNombre(segUsuarioTemporal.getSegundoNombre());
        segUsuarioTemporal1.setApellidoPaterno(segUsuarioTemporal.getApellidoPaterno());
        segUsuarioTemporal1.setApellidoMaterno(segUsuarioTemporal.getApellidoMaterno());
        segUsuarioTemporal1.setFechaNacimiento(segUsuarioTemporal.getFechaNacimiento());
        segUsuarioTemporal1.setSexo(segUsuarioTemporal.getSexo());
        segUsuarioTemporal1.setCelular(segUsuarioTemporal.getCelular());
        segUsuarioTemporal1.setCorreoInstitucional(segUsuarioTemporal.getCorreoInstitucional());
        segUsuarioTemporal1.setCorreoPersonal(segUsuarioTemporal.getCorreoPersonal());
        segUsuarioTemporal1.setTelefonoInstitucional(segUsuarioTemporal.getTelefonoInstitucional());
        segUsuarioTemporal1.setExtensionInstitucional(segUsuarioTemporal.getExtensionInstitucional());
        segUsuarioTemporal1.setPassword(segUsuarioTemporal.getPassword());

        return segUsuarioTemporalRepository.save(segUsuarioTemporal1);
    }

    @Override
    public SegUsuarioTemporal actualizarUsuario(SegUsuarioTemporal segUsuarioTemporal) {
        SegUsuarioTemporal segUsuarioTemporalUpdate = segUsuarioTemporalRepository.findById(segUsuarioTemporal.getIdUsuarioTemporal()).get();
        segUsuarioTemporalUpdate.setUuid(segUsuarioTemporal.getUuid());
        segUsuarioTemporalUpdate.setCodigoValidacion(segUsuarioTemporal.getCodigoValidacion());
        segUsuarioTemporalUpdate.setMetodoValidacion(segUsuarioTemporal.getMetodoValidacion());
        segUsuarioTemporalUpdate.setAccessToken(segUsuarioTemporal.getAccessToken());

        return segUsuarioTemporalRepository.save(segUsuarioTemporalUpdate);
    }

    @Override
    public Optional<SegUsuarioTemporal> getEstatusCorreo(String correo) {

/*        if (correo.toLowerCase().contains("gmail") || correo.toLowerCase().contains("hotmail"))
            return segUsuarioTemporalRepository.findByCorreoPersonalAndEstatus(correo,"PENDIENTE");
        else*/
            return segUsuarioTemporalRepository.findByCorreoInstitucionalAndEstatus(correo,"PENDIENTE");
    }

    @Override
    public SegUsuarioTemporal searchUsuario(String curp, String correo) {

        Optional<SegUsuarioTemporal> segUsuarioTemporalOp=getEstatusCorreo(correo);

        if (!segUsuarioTemporalOp.isPresent())
            throw new NoSuchElementException("El registro ya expiró, o ya fue validado");

        if (segUsuarioTemporalOp.get().getCurp()!=null && segUsuarioTemporalOp.get().getCurp().equals(curp))
            return segUsuarioTemporalOp.get();

        Optional<CatUsuario> catUsuarioOp = catUsuarioService.getUsuarioCurp(curp);

        if (catUsuarioOp.isPresent()){

            List<CatUsuarioEntePublico> lista = catUsuarioService.getListaUsuarioPorCurp(curp);

            segUsuarioTemporalOp.get().setCurp(catUsuarioOp.get().getCurp());
            segUsuarioTemporalOp.get().setRfc(catUsuarioOp.get().getRfc());

            if (lista.size()==1){
                segUsuarioTemporalOp.get().setIdEntePublico(lista.get(0).getEntePublico().getIdEntePublico());
                segUsuarioTemporalOp.get().setCorreoInstitucional(lista.get(0).getCorreoInstitucional());
                segUsuarioTemporalOp.get().setTelefonoInstitucional(lista.get(0).getNumTelefonoInstitucional());
                segUsuarioTemporalOp.get().setExtensionInstitucional(lista.get(0).getNumeroExtensionInstitucional());
            }else{
                List<CatUsuarioEntePublico> listaFilter = lista.stream().filter(p->p.getCveEstatus().equals("VIG")).collect(Collectors.toList());
                if (listaFilter.size()==1){
                    segUsuarioTemporalOp.get().setIdEntePublico(listaFilter.get(0).getEntePublico().getIdEntePublico());
                    segUsuarioTemporalOp.get().setCorreoInstitucional(listaFilter.get(0).getCorreoInstitucional());
                    segUsuarioTemporalOp.get().setTelefonoInstitucional(listaFilter.get(0).getNumTelefonoInstitucional());
                    segUsuarioTemporalOp.get().setExtensionInstitucional(listaFilter.get(0).getNumeroExtensionInstitucional());
                }
            }

            segUsuarioTemporalOp.get().setNombre(catUsuarioOp.get().getNombre());
            segUsuarioTemporalOp.get().setSegundoNombre(catUsuarioOp.get().getSegundoNombre());
            segUsuarioTemporalOp.get().setApellidoPaterno(catUsuarioOp.get().getApellidoPaterno());
            segUsuarioTemporalOp.get().setApellidoMaterno(catUsuarioOp.get().getApellidoMaterno());
            segUsuarioTemporalOp.get().setSexo(catUsuarioOp.get().getCveSexo());
            segUsuarioTemporalOp.get().setFechaNacimiento(catUsuarioOp.get().getFechaNacimiento());
            segUsuarioTemporalOp.get().setCelular(catUsuarioOp.get().getNumCelular());
            segUsuarioTemporalOp.get().setCorreoPersonal(catUsuarioOp.get().getCorreoPersonal());

        }

            return segUsuarioTemporalOp.get();
    }


    @Override
    public SegUsuarioTemporal generandoUsuarios(Long idUsuarioTemporal){
        SegUsuarioTemporal segUsuarioTemporal= segUsuarioTemporalRepository.findById(idUsuarioTemporal).get();

        if (segUsuarioTemporal.getEstatus().equals("VALIDADO"))
            throw new NoSuchElementException("El usuario ya esta validado");

        Optional<CatUsuario> catUsuarioOp = catUsuarioService.getUsuarioCurp(segUsuarioTemporal.getCurp());
        Optional<CatUsuario> catUsuarioRfcOp = catUsuarioService.getUsuarioRfc(segUsuarioTemporal.getRfc());

        if (catUsuarioOp.isPresent()){
            if (catUsuarioRfcOp.isPresent() && catUsuarioRfcOp.get().getIdUsuario().longValue()!=catUsuarioOp.get().getIdUsuario().longValue())
                throw new NoSuchElementException("El RFC existe en otro usuario");

            catUsuarioOp.get().setRfc(segUsuarioTemporal.getRfc());
            catUsuarioOp.get().setNombre(segUsuarioTemporal.getNombre());
            catUsuarioOp.get().setSegundoNombre(segUsuarioTemporal.getSegundoNombre());
            catUsuarioOp.get().setApellidoPaterno(segUsuarioTemporal.getApellidoPaterno());
            catUsuarioOp.get().setApellidoMaterno(segUsuarioTemporal.getApellidoMaterno());
            catUsuarioOp.get().setCveSexo(segUsuarioTemporal.getSexo());
            catUsuarioOp.get().setFechaNacimiento(segUsuarioTemporal.getFechaNacimiento());
            catUsuarioOp.get().setNumCelular(segUsuarioTemporal.getCelular());
            catUsuarioOp.get().setCorreoPersonal(segUsuarioTemporal.getCorreoPersonal());
            catUsuarioOp.get().setCveEstatus("VIG");

            CatUsuario catUsuario = catUsuarioService.saveCatUsuario(catUsuarioOp.get());

            List<CatUsuarioEntePublico> listaCatUsuarioEntePublico = catUsuarioEntePublicoService. getEntesPublicoPorUsuario(catUsuario.getIdUsuario());
            Boolean encontre=false;

            long idUsuarioEntePublico=0;

            for (CatUsuarioEntePublico catUsuarioEntePublico: listaCatUsuarioEntePublico){
                if (catUsuarioEntePublico.getEntePublico().getIdEntePublico().longValue()!=segUsuarioTemporal.getIdEntePublico().longValue()) {
                    catUsuarioEntePublico.setCveEstatus("NOV");
                }else{
                    encontre=true;
                    catUsuarioEntePublico.setNumTelefonoInstitucional(segUsuarioTemporal.getTelefonoInstitucional());
                    catUsuarioEntePublico.setNumeroExtensionInstitucional(segUsuarioTemporal.getExtensionInstitucional());
                    catUsuarioEntePublico.setCorreoInstitucional(segUsuarioTemporal.getCorreoInstitucional());
                    catUsuarioEntePublico.setCveEstatus("VIG");
                    catUsuarioEntePublico.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
                    idUsuarioEntePublico=catUsuarioEntePublico.getIdUsuarioEntePublico().longValue();
                }
            }

            catUsuarioEntePublicoService.saveUsuarioAll(listaCatUsuarioEntePublico);

            if (!encontre){

                EntePublico entePublico = entePublicoService.getEntePublicoId(segUsuarioTemporal.getIdEntePublico().longValue());

                CatUsuarioEntePublico catUsuarioEntePublico = new CatUsuarioEntePublico();
                catUsuarioEntePublico.setCatUsuario(catUsuario);
                catUsuarioEntePublico.setEntePublico(entePublico);
                catUsuarioEntePublico.setNumTelefonoInstitucional(segUsuarioTemporal.getTelefonoInstitucional());
                catUsuarioEntePublico.setNumeroExtensionInstitucional(segUsuarioTemporal.getExtensionInstitucional());
                catUsuarioEntePublico.setCorreoInstitucional(segUsuarioTemporal.getCorreoInstitucional());
                catUsuarioEntePublico.setCveEstatus("VIG");
                catUsuarioEntePublico.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
                catUsuarioEntePublico.setUnidadAdministrativa(null);
                catUsuarioEntePublico.setUbicacionUnidadAdmin(null);
                catUsuarioEntePublico.setPuesto(null);
                catUsuarioEntePublico.setCatUsuarioResguardante(null);
                catUsuarioEntePublico.setUnidadAdministrativaPptal(null);
                catUsuarioEntePublico.setEntePublicoResguardante(null);
                catUsuarioEntePublico.setUnidadAdministrativaResguardante(null);
                CatUsuarioEntePublico catUsuarioEntePublicoNew = catUsuarioEntePublicoService.saveUsuario(catUsuarioEntePublico);
                idUsuarioEntePublico=catUsuarioEntePublicoNew.getIdUsuarioEntePublico().longValue();
            }

            Optional<SegUsuario> usuTemp = segUsuarioRepository.findByIdUsuarioAndSistemaOptional(catUsuarioOp.get().getIdUsuario(),"SIGTIC");
            if (usuTemp.isPresent()){
                usuTemp.get().setContrasenia(bCryptPasswordEncoder.encode(segUsuarioTemporal.getPassword()));
                usuTemp.get().setIdUsuarioEntePublico(idUsuarioEntePublico);
                usuTemp.get().setCveEstatus("VIG");
                usuTemp.get().setBloqueado(0);
                usuTemp.get().setContraseniaReset(0);
                segUsuarioRepository.save(usuTemp.get());
            }else{

                SegUsuario segUsuario= new SegUsuario();
                SegUsuarioPk segUsuarioPk= new SegUsuarioPk();
                segUsuarioPk.setUsuario(catUsuario.getCurp());
                segUsuarioPk.setSistema("SIGTIC");
                segUsuario.setSegUsuarioPk(segUsuarioPk);
                segUsuario.setBloqueado(0);
                segUsuario.setContraseniaReset(0);
                segUsuario.setContrasenia(bCryptPasswordEncoder.encode(segUsuarioTemporal.getPassword()));
                segUsuario.setCveEstatus("VIG");
                segUsuario.setIdUsuarioEntePublico(idUsuarioEntePublico);
                segUsuario.setIdUsuario(catUsuario.getIdUsuario());
                segUsuarioRepository.save(segUsuario);

            }

        }else{
            if (catUsuarioRfcOp.isPresent())
                throw new NoSuchElementException("El RFC existe en otro usuario");

            CatUsuario catUsuario = new CatUsuario();
            catUsuario.setCurp(segUsuarioTemporal.getCurp());
            catUsuario.setRfc(segUsuarioTemporal.getRfc());
            catUsuario.setNombre(segUsuarioTemporal.getNombre());
            catUsuario.setSegundoNombre(segUsuarioTemporal.getSegundoNombre());
            catUsuario.setApellidoPaterno(segUsuarioTemporal.getApellidoPaterno());
            catUsuario.setApellidoMaterno(segUsuarioTemporal.getApellidoMaterno());
            catUsuario.setCveSexo(segUsuarioTemporal.getSexo());
            catUsuario.setFechaNacimiento(segUsuarioTemporal.getFechaNacimiento());
            catUsuario.setNumCelular(segUsuarioTemporal.getCelular());
            catUsuario.setCorreoPersonal(segUsuarioTemporal.getCorreoPersonal());
            catUsuario.setCveEstatus("VIG");
            catUsuarioService.saveCatUsuario(catUsuario);

            EntePublico entePublico = entePublicoService.getEntePublicoId(segUsuarioTemporal.getIdEntePublico().longValue());

            CatUsuarioEntePublico catUsuarioEntePublico = new CatUsuarioEntePublico();
            catUsuarioEntePublico.setCatUsuario(catUsuario);
            catUsuarioEntePublico.setEntePublico(entePublico);
            catUsuarioEntePublico.setNumTelefonoInstitucional(segUsuarioTemporal.getTelefonoInstitucional());
            catUsuarioEntePublico.setNumeroExtensionInstitucional(segUsuarioTemporal.getExtensionInstitucional());
            catUsuarioEntePublico.setCorreoInstitucional(segUsuarioTemporal.getCorreoInstitucional());
            catUsuarioEntePublico.setCveEstatus("VIG");
            catUsuarioEntePublico.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            catUsuarioEntePublico.setUnidadAdministrativa(null);
            catUsuarioEntePublico.setUbicacionUnidadAdmin(null);
            catUsuarioEntePublico.setPuesto(null);
            catUsuarioEntePublico.setCatUsuarioResguardante(null);
            catUsuarioEntePublico.setUnidadAdministrativaPptal(null);
            catUsuarioEntePublico.setEntePublicoResguardante(null);
            catUsuarioEntePublico.setUnidadAdministrativaResguardante(null);
            CatUsuarioEntePublico catUsuarioEntePublicoNew = catUsuarioEntePublicoService.saveUsuario(catUsuarioEntePublico);

            SegUsuario segUsuario= new SegUsuario();
            SegUsuarioPk segUsuarioPk= new SegUsuarioPk();
            segUsuarioPk.setUsuario(catUsuario.getCurp());
            segUsuarioPk.setSistema("SIGTIC");
            segUsuario.setSegUsuarioPk(segUsuarioPk);
            segUsuario.setBloqueado(0);
            segUsuario.setContraseniaReset(0);
            segUsuario.setContrasenia(bCryptPasswordEncoder.encode(segUsuarioTemporal.getPassword()));
            segUsuario.setCveEstatus("VIG");
            segUsuario.setIdUsuarioEntePublico(catUsuarioEntePublicoNew.getIdUsuarioEntePublico().longValue());
            segUsuario.setIdUsuario(catUsuario.getIdUsuario());
            segUsuarioRepository.save(segUsuario);
        }

        List<SegAccesoUsuario> listaAcceso = segAccesoUsuarioRepository.findAllByRfcUsuarioAndIdSistemaAndIdEntePublicoAndIdUnidadAdministrativaAndCveEstatus(segUsuarioTemporal.getCurp(), "SIGTIC", segUsuarioTemporal.getIdEntePublico(), segUsuarioTemporal.getIdEntePublico(), "VIG");

        if (listaAcceso.size()==0){
            SegAccesoUsuario segAccesoUsuario = new SegAccesoUsuario();
            segAccesoUsuario.setIdEntePublico(segUsuarioTemporal.getIdEntePublico());
            segAccesoUsuario.setIdUnidadAdministrativa(segUsuarioTemporal.getIdEntePublico());
            segAccesoUsuario.setRfcUsuario(segUsuarioTemporal.getCurp());
            segAccesoUsuario.setIdSistema("SIGTIC");
            segAccesoUsuario.setCveEstatus("VIG");
            segAccesoUsuarioRepository.save(segAccesoUsuario);
        }

        List<SegUsuarioTemporalRol>  listaRol= segUsuarioTemporalRolRepository.findAllBySegUsuarioTemporal(segUsuarioTemporal);
        List<SegPerfilUsuario> listaPerfilUsuario = new ArrayList<>();

        for (SegUsuarioTemporalRol reg:listaRol){
            Optional<SegPerfilUsuario> segPerfilUsuarioOp = segPerfilUsuarioRepository.findBySegUsuarioAndSistemaAndSegPerfil(segUsuarioTemporal.getCurp(),"SIGTIC", reg.getSegPerfil());

            if (!segPerfilUsuarioOp.isPresent()){
                SegPerfilUsuario segPerfilUsuario = new SegPerfilUsuario();
                segPerfilUsuario.setSegPerfil(reg.getSegPerfil());
                segPerfilUsuario.setSegUsuario(segUsuarioTemporal.getCurp());
                segPerfilUsuario.setSistema("SIGTIC");
                listaPerfilUsuario.add(segPerfilUsuario);
            }
        }
        segPerfilUsuarioRepository.saveAll(listaPerfilUsuario);

        segUsuarioTemporal.setEstatus("VALIDADO");

        return segUsuarioTemporalRepository.save(segUsuarioTemporal);
    }

    @Override
    public List<SegUsuarioTemporal> listaUsuariosTemporal(String estatus) {
        return segUsuarioTemporalRepository.findAllByEstatus(estatus);
    }

    @Override
    public List<SegUsuarioTemporal> listaUsuariosTemporalAll() {
        return (List<SegUsuarioTemporal>) segUsuarioTemporalRepository.findAll();
    }

    @Override
    public Integer totalRoles(SegUsuarioTemporal segUsuarioTemporal) {
        return segUsuarioTemporalRolRepository.countBySegUsuarioTemporal(segUsuarioTemporal);
    }

    @Override
    public SegUsuarioTemporal savePreRegistro(SegUsuarioTemporal segUsuarioTemporal){

        Optional<SegUsuarioTemporal> segUsuarioTemporalOp = segUsuarioTemporalRepository.findByCorreoInstitucionalAndEstatus(segUsuarioTemporal.getCorreoInstitucional(),"PENDIENTE");

        if (segUsuarioTemporalOp.isPresent() && segUsuarioTemporal.getIdUsuarioTemporal()==null)
            throw new NoSuchElementException("El correo ya esta agregado con estatus PENDIENTE");

        if (segUsuarioTemporalOp.isPresent() && segUsuarioTemporal!=null && segUsuarioTemporalOp.get().getIdUsuarioTemporal()!=segUsuarioTemporal.getIdUsuarioTemporal())
            throw new NoSuchElementException("El correo ya esta agregado con estatus PENDIENTE y no es el que esta editando");

        if (segUsuarioTemporal.getIdUsuarioTemporal()!=null){
            SegUsuarioTemporal segUsuarioTemporalEditar = segUsuarioTemporalRepository.findById(segUsuarioTemporal.getIdUsuarioTemporal()).get();
            segUsuarioTemporalEditar.setCorreoInstitucional(segUsuarioTemporal.getCorreoInstitucional());
            segUsuarioTemporalEditar.setCurp(segUsuarioTemporal.getCurp());
            segUsuarioTemporalEditar.setRfc(segUsuarioTemporal.getRfc());
            segUsuarioTemporalEditar.setNombre(segUsuarioTemporal.getNombre());
            segUsuarioTemporalEditar.setSegundoNombre(segUsuarioTemporal.getSegundoNombre());
            segUsuarioTemporalEditar.setApellidoPaterno(segUsuarioTemporal.getApellidoPaterno());
            segUsuarioTemporalEditar.setApellidoMaterno(segUsuarioTemporal.getApellidoMaterno());
            segUsuarioTemporalEditar.setCelular(segUsuarioTemporal.getCelular());
            segUsuarioTemporalEditar.setCorreoPersonal(segUsuarioTemporal.getCorreoPersonal());
            segUsuarioTemporalEditar.setTelefonoInstitucional(segUsuarioTemporal.getTelefonoInstitucional());
            segUsuarioTemporalEditar.setExtensionInstitucional(segUsuarioTemporal.getExtensionInstitucional());
            segUsuarioTemporalEditar.setOficio(segUsuarioTemporal.getOficio());
            segUsuarioTemporalEditar.setDependencia(segUsuarioTemporal.getDependencia());
            return segUsuarioTemporalRepository.save(segUsuarioTemporalEditar);
        }else{
            segUsuarioTemporal.setEstatus("PENDIENTE");
            return segUsuarioTemporalRepository.save(segUsuarioTemporal);
        }

    }

    @Override
    public JSONObject deletePreRegistro(SegUsuarioTemporal segUsuarioTemporal) {

        SegUsuarioTemporal preRegistro = segUsuarioTemporalRepository.findById(segUsuarioTemporal.getIdUsuarioTemporal()).get();

        if (!preRegistro.getEstatus().equals("PENDIENTE"))
            throw new NoSuchElementException("El pre registro ya no esta en estatus de PENDIENTE.");

        List<SegUsuarioTemporalRol> listaRolTempo = segUsuarioTemporalRolRepository.findAllBySegUsuarioTemporal(preRegistro);

        segUsuarioTemporalRolRepository.deleteAll(listaRolTempo);
        segUsuarioTemporalRepository.delete(preRegistro);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message","Pre Registro Eliminado Correctamente");

        return jsonObject;

    }

    @Override
    public SegUsuarioTemporal getUsuarioTemporalId(Long idUsuarioTemporal) {
        return segUsuarioTemporalRepository.findById(idUsuarioTemporal).get();
    }

    @Override
    public List<SegUsuarioTemporalRol> listaSegUsuarioTemporalRol(SegUsuarioTemporal segUsuarioTemporal) {
        return segUsuarioTemporalRolRepository.findAllBySegUsuarioTemporal(segUsuarioTemporal);
    }

    @Override
    public SegUsuarioTemporalRol addRolTemporal(SegUsuarioTemporalRol segUsuarioTemporalRol) {

        SegUsuarioTemporal segUsuarioTemporal = segUsuarioTemporalRepository.findById(segUsuarioTemporalRol.getSegUsuarioTemporal().getIdUsuarioTemporal()).get();
        SegPerfil segPerfil = segPerfilRepository.findById(segUsuarioTemporalRol.getSegPerfil().getIdPerfil()).get();

        Optional<SegUsuarioTemporalRol> segUsuarioTemporalRolOp = segUsuarioTemporalRolRepository.findBySegUsuarioTemporalAndSegPerfil(segUsuarioTemporal, segPerfil);

        if (segUsuarioTemporalRolOp.isPresent())
            throw new NoSuchElementException("El rol ya esta asignado");

        segUsuarioTemporalRol.setSegUsuarioTemporal(segUsuarioTemporal);
        segUsuarioTemporalRol.setSegPerfil(segPerfil);
        SegUsuarioTemporalRol segUsuarioTemporalRolResult = segUsuarioTemporalRolRepository.save(segUsuarioTemporalRol);

        /*Si el usuario esta validado y se lea grega un rol se asigna en automatico al usuario*/
        if (segUsuarioTemporal.getEstatus().equals("VALIDADO")){
            Optional<SegPerfilUsuario> segPerfilUsuarioOp = segPerfilUsuarioRepository.findBySegUsuarioAndSistemaAndSegPerfil(segUsuarioTemporal.getCurp(),"SIGTIC", segPerfil);

            if (!segPerfilUsuarioOp.isPresent()){
                SegPerfilUsuario segPerfilUsuario = new SegPerfilUsuario();
                segPerfilUsuario.setSegPerfil(segPerfil);
                segPerfilUsuario.setSegUsuario(segUsuarioTemporal.getCurp());
                segPerfilUsuario.setSistema("SIGTIC");
                segPerfilUsuarioRepository.save(segPerfilUsuario);
            }
        }

        return segUsuarioTemporalRolResult;
    }

    @Override
    public JSONObject deleteRolPreRegistro(SegUsuarioTemporalRol segUsuarioTemporalRol) {

        SegUsuarioTemporalRol segUsuarioTemporalRol1 = segUsuarioTemporalRolRepository.findById(segUsuarioTemporalRol.getIdUsuarioTemporalRol()).get();

       /* if (!segUsuarioTemporalRol1.getSegUsuarioTemporal().getEstatus().equals("PENDIENTE"))
            throw new NoSuchElementException("El pre registro ya no esta en estatus de PENDIENTE.");*/

        segUsuarioTemporalRolRepository.delete(segUsuarioTemporalRol1);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message","Rol de Pre Registro Eliminado Correctamente");

        return jsonObject;

    }

    @Override
    public SegUsuarioTemporal regresarPreRegistro(SegUsuarioTemporal segUsuarioTemporal){

        SegUsuarioTemporal segUsuarioTemporalActual = segUsuarioTemporalRepository.findById(segUsuarioTemporal.getIdUsuarioTemporal()).get();

        if (!segUsuarioTemporalActual.getEstatus().equals("VALIDADO"))
            throw new NoSuchElementException("El pre-registro ya no esta VALIDADO");

        Optional<SegUsuarioTemporal> segUsuarioTemporalOp = segUsuarioTemporalRepository.findByCorreoInstitucionalAndEstatus(segUsuarioTemporalActual.getCorreoInstitucional(),"PENDIENTE");

        if (segUsuarioTemporalOp.isPresent())
            throw new NoSuchElementException("Ya hay un pre-registro en PENDIENTE para este correo");

        segUsuarioTemporalActual.setEstatus("PENDIENTE");

        return segUsuarioTemporalRepository.save(segUsuarioTemporalActual);

    }

    @Override
    public SegUsuarioTemporal generarToken(SegUsuarioTemporal segUsuarioTemporal) {
        SegUsuarioTemporal segUsuarioTemporalActual = segUsuarioTemporalRepository.findById(segUsuarioTemporal.getIdUsuarioTemporal()).get();

        String token=TokenUtils.createToken(segUsuarioTemporalActual.getCorreoInstitucional());
        segUsuarioTemporalActual.setTokenUrl(token);
        return segUsuarioTemporalRepository.save(segUsuarioTemporalActual);
    }

}
