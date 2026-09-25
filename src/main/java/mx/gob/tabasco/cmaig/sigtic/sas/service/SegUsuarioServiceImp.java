package mx.gob.tabasco.cmaig.sigtic.sas.service;

import mx.gob.tabasco.cmaig.sigtic.Utileria.TokenUtils;
import mx.gob.tabasco.cmaig.sigtic.sac.model.*;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.*;
import mx.gob.tabasco.cmaig.sigtic.sas.model.*;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.Perfil;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.*;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SegUsuarioServiceImp implements SegUsuarioService {

    @Autowired
    private SegUsuarioRepository segUsuarioRepository;

    @Autowired
    private CatUsuarioRepository catUsuarioRepository;

    @Autowired
    private SegPerfilUsuarioRepository segPerfilUsuarioRepository;

    @Autowired
    private SegPerfilRepository segPerfilRepository;

    @Autowired
    private CatUnidadAdministrativaSqlRepository catUnidadAdministrativaSqlRepository;

    @Autowired
    private UnidadAdministrativaRepository unidadAdministrativaRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SegAccesoUsuarioRepository segAccesoUsuarioRepository;

    @Autowired
    private SegAccesoUsuarioService segAccesoUsuarioService;

    @Autowired
    private CatUsuarioEntePublicoRepository catUsuarioEntePublicoRepository;

    @Autowired
    private EntePublicoRepository entePublicoRepository;

    @Autowired
    private SegUsuarioListaRepository segUsuarioListaRepository;

    @Override
    public Perfil getPerfil() {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Perfil perfil=new Perfil();
        perfil.setUserNameShort(authUser.getUserNameShort());
        perfil.setUserNameFull(authUser.getCatUsuarioEntePublico().getCatUsuario().getNombreCompleto());
        perfil.setCorreo(authUser.getCatUsuarioEntePublico().getCorreoInstitucional());
        perfil.setNombre(authUser.getCatUsuarioEntePublico().getCatUsuario().getNombre());
        perfil.setSegundoNombre(authUser.getCatUsuarioEntePublico().getCatUsuario().getSegundoNombre());
        perfil.setApellidoPaterno(authUser.getCatUsuarioEntePublico().getCatUsuario().getApellidoPaterno());
        perfil.setApellidoMaterno(authUser.getCatUsuarioEntePublico().getCatUsuario().getApellidoMaterno());
        perfil.setRfc(authUser.getCatUsuarioEntePublico().getCatUsuario().getRfc());
        perfil.setCurp(authUser.getCatUsuarioEntePublico().getCatUsuario().getCurp());

        if (authUser.getCatUsuarioEntePublico().getEntePublico()!=null) {
            perfil.setIdInstitucion(authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico());
            perfil.setNombreInstitucion(authUser.getCatUsuarioEntePublico().getEntePublico().getNombreEntePublico());
        }

        if (authUser.getCatUsuarioEntePublico().getUnidadAdministrativa()!=null) {
            perfil.setIdUnidadAdministrativa(authUser.getCatUsuarioEntePublico().getUnidadAdministrativa().getIdUnidadAdministrativa());
            perfil.setNombreUnidadAdministrativa(authUser.getCatUsuarioEntePublico().getUnidadAdministrativa().getNombreUnidadAdministrativa());
        }

        if (authUser.getCatUsuarioEntePublico().getPuesto()!=null) {
            perfil.setCargo(authUser.getCatUsuarioEntePublico().getPuesto());
        }

        perfil.setNumeroTelefono(authUser.getCatUsuarioEntePublico().getNumTelefonoInstitucional());
        perfil.setNumeroExtension(authUser.getCatUsuarioEntePublico().getNumeroExtensionInstitucional());

        perfil.setCorreoAlterno(authUser.getCatUsuarioEntePublico().getCatUsuario().getCorreoPersonal());
        perfil.setCelular(authUser.getCatUsuarioEntePublico().getCatUsuario().getNumCelular());

        return perfil;
    }

    @Override
    public List<SegUsuarioLista> getTablaUsuarios(String rfc, Long idEntePublico, boolean roleAdminSigtic) {

        List<SegUsuarioLista> listaUsuario = null;

        if (roleAdminSigtic)
            listaUsuario = segUsuarioListaRepository.getUsuarioPorSistemaEnteAdminSigtic("SIGTIC", idEntePublico);
        else {
            List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(rfc,idEntePublico);

            if (listaUnidAcceso.size()>0)
                listaUsuario = segUsuarioListaRepository.getUsuarioPorSistemaUnidades( "SIGTIC", listaUnidAcceso);
            else
                listaUsuario = new ArrayList<>();
        }

        //List<Long> listaIdUsuarioEntePublico = listaUsuario.stream().map(SegUsuario::getIdUsuarioEntePublico).collect(Collectors.toList());

        //List<CatUsuarioEntePublico> allUsers = (List<CatUsuarioEntePublico>) catUsuarioEntePublicoRepository.findAllById(listaIdUsuarioEntePublico);

        //EntePublico entePublico = entePublicoRepository.findById(idEntePublico).get();

        //List<CatUsuarioEntePublico> allUsers = catUsuarioEntePublicoRepository.getCatUsuarioEntePublicoList(entePublico);

       // Map<Long, CatUsuarioEntePublico> mapUsers = allUsers.stream().collect(Collectors.toMap(CatUsuarioEntePublico::getIdUsuarioEntePublico, Function.identity()));

        //List<SegPerfilUsuario> listaSegPerfilUsuario=segPerfilUsuarioRepository.findBySistemaOrderBySegPerfil("SIGTIC");

        /*listaUsuario.forEach(user->{
           if (mapUsers.containsKey(user.getIdUsuarioEntePublico()))
                user.setCatUsuarioEntePublico(mapUsers.get(user.getIdUsuarioEntePublico()));
            List<SegPerfil> listaPerfil = listaSegPerfilUsuario.stream().filter(p->p.getSegUsuario().equals(user.getSegUsuarioPk().getUsuario())).map(SegPerfilUsuario::getSegPerfil).collect(Collectors.toList());
            user.setListaSegPerfil(listaPerfil);
        });*/

        return listaUsuario;
    }

    /*
    @Override
    public List<CatUsuarioEntePublico> getCatUsuariosPorUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa){

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<CatUnidadAdministrativaSql> listaUnidadesAdministrativas=null;

        if (authUser.isAdministradorSigtic())
            listaUnidadesAdministrativas= catUnidadAdministrativaSqlRepository.getUnidadesAdministrativasArbolSigtic(unidadAdministrativa.getIdUnidadAdministrativa());
        else
            listaUnidadesAdministrativas= catUnidadAdministrativaSqlRepository.getUnidadesAdministrativasArbol(authUser.getUsername(),unidadAdministrativa.getIdUnidadAdministrativa());


        List<Long> listaIdUnidad = listaUnidadesAdministrativas.stream().map(CatUnidadAdministrativaSql::getIdUnidadAdministrativa).collect(Collectors.toList());

        List<UnidadAdministrativa> listaUnidad=(List<UnidadAdministrativa>) unidadAdministrativaRepository.findAllById(listaIdUnidad);

        List<CatUsuarioEntePublico> listaCatUsuarios=null;

        if (listaUnidad.size()>0){
            List<Long>listaIdUsuario = segUsuarioRepository.findIdUsuarioBySistemaAndCveEstatus("SIGTIC","VIG", listaIdUnidad);

            if (listaIdUsuario.size()>0)
                listaCatUsuarios = catUsuarioRepository.findByUnidadAdministrativaInAndCveEstatusAndIdUsuarioNotInOrderByUnidadAdministrativaAscNombreAsc(listaUnidad,"VIG", listaIdUsuario);
            else
                listaCatUsuarios = catUsuarioEntePublicoRepository.getListaUsuariosPorUnidadAdmin(listaUnidad,"VIG");

            return listaCatUsuarios;
        }else{
            listaCatUsuarios=new ArrayList<>();

            return listaCatUsuarios;
        }

    }*/


    @Override
    public List<CatUsuarioEntePublico> getCatUsuariosPorUnidadAdministrativaAll(UnidadAdministrativa unidadAdministrativa){

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<CatUnidadAdministrativaSql> listaUnidadesAdministrativas=null;

        if (authUser.isAdministradorSigtic())
            listaUnidadesAdministrativas= catUnidadAdministrativaSqlRepository.getUnidadesAdministrativasArbolSigtic(unidadAdministrativa.getIdUnidadAdministrativa());
        else
            listaUnidadesAdministrativas= catUnidadAdministrativaSqlRepository.getUnidadesAdministrativasArbol(authUser.getUsername(),unidadAdministrativa.getIdUnidadAdministrativa());


        List<Long> listaIdUnidad = listaUnidadesAdministrativas.stream().map(CatUnidadAdministrativaSql::getIdUnidadAdministrativa).collect(Collectors.toList());

        List<UnidadAdministrativa> listaUnidad=(List<UnidadAdministrativa>) unidadAdministrativaRepository.findAllById(listaIdUnidad);

        List<CatUsuarioEntePublico> listaCatUsuarioEntePublico=null;

        if (listaUnidad.size()>0){
            listaCatUsuarioEntePublico = catUsuarioEntePublicoRepository.getListaUsuariosPorUnidadAdmin(listaUnidad,"VIG");

            return listaCatUsuarioEntePublico;
        }else{
            listaCatUsuarioEntePublico=new ArrayList<>();

            return listaCatUsuarioEntePublico;
        }

    }

    @Override
    public List<CatUsuarioEntePublico> getCatUsuariosPorEntePublicoAll(EntePublico entePublico) {
        EntePublico entePublico1 = entePublicoRepository.findById(entePublico.getIdEntePublico()).get();

        return catUsuarioEntePublicoRepository.getCatUsuarioEntePublico(entePublico1);
    }

    @Override
    public List<CatUsuarioEntePublico> getCatUsuariosPorEntePublicoRfcAll(EntePublico entePublico, String rfc) {

        EntePublico entePublico1 = entePublicoRepository.findById(entePublico.getIdEntePublico()).get();

        return catUsuarioEntePublicoRepository.getCatUsuarioEntePublicoAndRfc(entePublico1, rfc);
    }


    @Override
    public SegUsuario getUsuarioId(Long idUsuarioEntePublico) {

        SegUsuario usuario = segUsuarioRepository.findByIdUsuarioEntePublicoAndSistema(idUsuarioEntePublico, "SIGTIC");

        Optional<CatUsuarioEntePublico> catUsuarioEntePublicoOp=catUsuarioEntePublicoRepository.findById(usuario.getIdUsuarioEntePublico());
        catUsuarioEntePublicoOp.ifPresent(usuario::setCatUsuarioEntePublico);

        return usuario;
    }

    @Override
    public SegUsuario saveUsuario(SegUsuario segUsuario){

        segUsuario.setContrasenia(segUsuario.getContraseniaSistema());

        Optional<CatUsuario> catUsuario = catUsuarioRepository.findById(segUsuario.getIdUsuario());

        if (catUsuario.isPresent()) {
            SegUsuarioPk segUsuarioPk= new SegUsuarioPk();
            segUsuarioPk.setUsuario(catUsuario.get().getCurp());
            segUsuarioPk.setSistema("SIGTIC");
            segUsuario.setSegUsuarioPk(segUsuarioPk);
            segUsuario.setBloqueado(0);
            segUsuario.setContraseniaReset(0);

            if (segUsuario.getOpcion().equals("Nuevo")){
                segUsuario.setUidUsuario(String.valueOf(UUID.randomUUID()));
                if (segUsuario.getContrasenia().equals(""))
                    throw new RuntimeException("Falta la contraseña");

                segUsuario.setContrasenia(bCryptPasswordEncoder.encode(segUsuario.getContrasenia()));

                segUsuarioRepository.save(segUsuario);
            }else
            if (segUsuario.getOpcion().equals("Editar")){
                SegUsuario usuTemp = segUsuarioRepository.findByIdUsuarioAndSistema(segUsuario.getIdUsuario(),"SIGTIC");

                if (!segUsuario.getContrasenia().equals(""))
                    usuTemp.setContrasenia(bCryptPasswordEncoder.encode(segUsuario.getContrasenia()));

                usuTemp.setCveEstatus(segUsuario.getCveEstatus());

                segUsuarioRepository.save(usuTemp);

            }
        }
        else
            throw new RuntimeException("El usuario no existe en el catálogo de usuarios");

        return segUsuario;
    }

    @Transactional(value = "seguridadTransactionManager")
    @Override
    public JSONObject deleteUsuario(SegUsuario segUsuario) {
        SegUsuario userDelete = segUsuarioRepository.findByIdUsuarioEntePublicoAndSistema(segUsuario.getCatUsuarioEntePublico().getIdUsuarioEntePublico(),"SIGTIC");

        if (userDelete==null)
            throw new NoSuchElementException("El usuario no existe");

        List<SegPerfilUsuario> listSegPerfilUsuario = segPerfilUsuarioRepository.findBySegUsuarioAndSistemaOrderBySegPerfil(userDelete.getSegUsuarioPk().getUsuario(), "SIGTIC");

        if (listSegPerfilUsuario.size()>0)
            throw new NoSuchElementException("El usuario tiene roles asignados");

        List<SegAccesoUsuario> listAccesoUsuario= segAccesoUsuarioRepository.findSegAccesoUsuarioByRfcUsuarioAndIdSistema(userDelete.getSegUsuarioPk().getUsuario(), "SIGTIC");

        if (listAccesoUsuario.size()>0)
            throw new NoSuchElementException("El usuario tiene unidades administrativas asignadas");

       // segUsuarioRepository.delete(userDelete);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message","Usuario eliminado correctamente");

        return jsonObject;
    }

    @Override
    public SegUsuario actualizarPassword(SegUsuario segUsuario) {

        SegUsuario seg = segUsuarioRepository.findById(segUsuario.getSegUsuarioPk()).get();

        if (!bCryptPasswordEncoder.matches(segUsuario.getContraseniaActual(), seg.getContrasenia()))
            throw new NoSuchElementException("La contraseña actual no coincide");

        seg.setContrasenia(bCryptPasswordEncoder.encode(segUsuario.getContraseniaNueva()));

        return segUsuarioRepository.save(seg);

    }

    @Override
    public EntePublico getEntePublico(Long idEntePublico) {
        return entePublicoRepository.findById(idEntePublico).get();
    }

    @Override
    public Optional<SegUsuario> getUsuarioOptional(Long idUsuario) {
        return segUsuarioRepository.findByIdUsuarioAndSistemaOptional(idUsuario,"SIGTIC");
    }

    @Override
    public SegUsuario saveUsuarioTemporal(SegUsuario segUsuario) {
        return segUsuarioRepository.save(segUsuario);
    }

    @Transactional(value = "seguridadTransactionManager")
    @Override
    public SegUsuario actualizarEntePublicoActual(SegUsuario segUsuario) {
        SegUsuario segUsuarioUpdate = segUsuarioRepository.findByIdUsuarioEntePublicoAndSistema(segUsuario.getIdUsuarioEntePublico(),"SIGTIC");

        CatUsuarioEntePublico catUsuarioEntePublicoNew = catUsuarioEntePublicoRepository.findById(segUsuario.getCatUsuarioEntePublico().getIdUsuarioEntePublico()).get();
        segUsuarioUpdate.setIdUsuarioEntePublico(catUsuarioEntePublicoNew.getIdUsuarioEntePublico());



        return segUsuarioRepository.save(segUsuarioUpdate);
    }

    @Transactional(value = "seguridadTransactionManager")
    @Override
    public SegUsuario recuperarAcceso(RecuperarPassword recuperarPassword) {

        SegUsuarioPk segUsuarioPk=new SegUsuarioPk();
        segUsuarioPk.setSistema("SIGTIC");
        segUsuarioPk.setUsuario(recuperarPassword.getCurp().toUpperCase());

        Optional<SegUsuario> segUsuarioOp = segUsuarioRepository.findSegUsuarioBySegUsuarioPk(segUsuarioPk);

        if (!segUsuarioOp.isPresent())
            throw new NoSuchElementException("El usuario no tiene registros de acceso al sigtic");

        if (!segUsuarioOp.get().getCveEstatus().equals("VIG"))
            throw new NoSuchElementException("El usuario actualmente no esta vigente. Solicitar acceso a la Dirección de Tecnologías de la CGMAIG");

        Optional<CatUsuarioEntePublico> catUsuarioEntePublico = catUsuarioEntePublicoRepository.findById(segUsuarioOp.get().getIdUsuarioEntePublico());

        CatUsuarioEntePublico catUsuarioEntePublicoActual=null;

        if (!catUsuarioEntePublico.isPresent())
            throw new NoSuchElementException("El usuario no existe");

        /*Si el ente publico no esta vigente, buscamos un ente vigente*/
        if (!catUsuarioEntePublico.get().getEntePublico().getCveEstatus().equals("VIG")){
            List<CatUsuarioEntePublico> listaEntesPublicos=catUsuarioEntePublicoRepository.getListaUsuariosPorCurp(recuperarPassword.getCurp());
            List<CatUsuarioEntePublico> listaEnteVigente=listaEntesPublicos.stream().filter(p->p.getCveEstatus().equals("VIG")).collect(Collectors.toList());
            if  (listaEnteVigente.size()>1)
                throw new NoSuchElementException("Tiene mas de un ente público vigente asignado");
            else
            if  (listaEnteVigente.size()==0)
                throw new NoSuchElementException("No tiene un ente publico vigente asignado");
            else
            if  (listaEnteVigente.size()==1)
                catUsuarioEntePublicoActual=listaEnteVigente.get(0);

        }else
            catUsuarioEntePublicoActual=catUsuarioEntePublico.get();


        String dato="";

        if (recuperarPassword.getOpcion()==1 && catUsuarioEntePublicoActual.getCorreoInstitucional()!=null && !catUsuarioEntePublicoActual.getCorreoInstitucional().equals(""))
            dato = catUsuarioEntePublicoActual.getCorreoInstitucional();

        if (recuperarPassword.getOpcion()==2 && catUsuarioEntePublicoActual.getCatUsuario()!=null && catUsuarioEntePublicoActual.getCatUsuario().getCorreoPersonal()!=null  && !catUsuarioEntePublicoActual.getCatUsuario().getCorreoPersonal().equals(""))
            dato = catUsuarioEntePublicoActual.getCatUsuario().getCorreoPersonal();

        if (recuperarPassword.getOpcion()==3 && catUsuarioEntePublicoActual.getCatUsuario()!=null && catUsuarioEntePublicoActual.getCatUsuario().getNumCelular()!=null  && !catUsuarioEntePublicoActual.getCatUsuario().getNumCelular().equals(""))
            dato = catUsuarioEntePublicoActual.getCatUsuario().getNumCelular();

        segUsuarioOp.get().setCorreo(dato);
        segUsuarioOp.get().setCatUsuarioEntePublico(catUsuarioEntePublicoActual);

        String token= TokenUtils.createTokenRecuperar(segUsuarioOp.get().getCorreo(), segUsuarioOp.get().getSegUsuarioPk().getUsuario(),recuperarPassword.getOpcion());

        segUsuarioOp.get().setToken(token);
        segUsuarioOp.get().setTokenActivo("S");

        return segUsuarioRepository.save(segUsuarioOp.get());
    }

    @Override
    public RecuperarPassword validarDatosRecuperacion(RecuperarPassword recuperarPassword) {

        SegUsuarioPk segUsuarioPk=new SegUsuarioPk();
        segUsuarioPk.setSistema("SIGTIC");
        segUsuarioPk.setUsuario(recuperarPassword.getCurp().toUpperCase());

        Optional<SegUsuario> segUsuarioOp = segUsuarioRepository.findSegUsuarioBySegUsuarioPk(segUsuarioPk);

        if (!segUsuarioOp.isPresent())
            throw new NoSuchElementException("El usuario no tiene registros de acceso al sigtic");

        if (!segUsuarioOp.get().getCveEstatus().equals("VIG"))
            throw new NoSuchElementException("El usuario actualmente no esta vigente. Solicitar acceso a la Dirección de Tecnologías de la CGMAIG");

        Optional<CatUsuarioEntePublico> catUsuarioEntePublico = catUsuarioEntePublicoRepository.findById(segUsuarioOp.get().getIdUsuarioEntePublico());

        CatUsuarioEntePublico catUsuarioEntePublicoActual=null;

        if (!catUsuarioEntePublico.isPresent())
            throw new NoSuchElementException("El usuario no existe");

        /*Si el ente publico no esta vigente, buscamos un ente vigente*/
        if (!catUsuarioEntePublico.get().getEntePublico().getCveEstatus().equals("VIG")){
            List<CatUsuarioEntePublico> listaEntesPublicos=catUsuarioEntePublicoRepository.getListaUsuariosPorCurp(recuperarPassword.getCurp());
            List<CatUsuarioEntePublico> listaEnteVigente=listaEntesPublicos.stream().filter(p->p.getCveEstatus().equals("VIG")).collect(Collectors.toList());
            if  (listaEnteVigente.size()>1)
                throw new NoSuchElementException("Tiene mas de un ente público vigente asignado");
            else
            if  (listaEnteVigente.size()==0)
                throw new NoSuchElementException("No tiene un ente publico vigente asignado");
            else
            if  (listaEnteVigente.size()==1)
                catUsuarioEntePublicoActual=listaEnteVigente.get(0);

        }else
            catUsuarioEntePublicoActual=catUsuarioEntePublico.get();

        String dato="";

        if (recuperarPassword.getOpcion()==1 && catUsuarioEntePublicoActual.getCorreoInstitucional()!=null && !catUsuarioEntePublicoActual.getCorreoInstitucional().equals(""))
            dato = catUsuarioEntePublicoActual.getCorreoInstitucional();

        if (recuperarPassword.getOpcion()==2 && catUsuarioEntePublicoActual.getCatUsuario()!=null && catUsuarioEntePublicoActual.getCatUsuario().getCorreoPersonal()!=null  && !catUsuarioEntePublicoActual.getCatUsuario().getCorreoPersonal().equals(""))
            dato = catUsuarioEntePublicoActual.getCatUsuario().getCorreoPersonal();

        if (recuperarPassword.getOpcion()==3 && catUsuarioEntePublicoActual.getCatUsuario()!=null && catUsuarioEntePublicoActual.getCatUsuario().getNumCelular()!=null  && !catUsuarioEntePublicoActual.getCatUsuario().getNumCelular().equals(""))
            dato = catUsuarioEntePublicoActual.getCatUsuario().getNumCelular();


        if (recuperarPassword.getOpcion()==1 && dato.isEmpty())
            recuperarPassword.setError("El usuario no cuenta con correo institucional en sus registro");
        else
        if (recuperarPassword.getOpcion()==2 && dato.isEmpty())
            recuperarPassword.setError("El usuario no cuenta con correo personal en sus registro");
        else
        if (recuperarPassword.getOpcion()==3 && dato.isEmpty())
            recuperarPassword.setError("El usuario no cuenta con número de celular en sus registro");
        else{
            if (recuperarPassword.getOpcion()==1 || recuperarPassword.getOpcion()==2){
                String[] datoSplit = dato.split("@");
                if (datoSplit.length==2){
                    if (datoSplit[0].length()>4)
                        recuperarPassword.setDato(datoSplit[0].substring(datoSplit[0].length()-4)+"@"+datoSplit[1]);
                    else
                        recuperarPassword.setDato(dato);

                    recuperarPassword.setMsgExito("Valide que los ultimos caracteres corresponden con su correo");
                }else
                    recuperarPassword.setError("Hay un error en el correo");
            }else
            if (recuperarPassword.getOpcion()==3){
                if (dato.length()==10) {
                    recuperarPassword.setDato(dato.substring(dato.length() - 4));
                    recuperarPassword.setMsgExito("Valide que los últimos 4 dígitcos corresponden con su celular");
                }
                else
                    recuperarPassword.setError("El número de celular es incorrecto");
            }
        }

        return recuperarPassword;
    }

    @Override
    public Optional<SegUsuario> getUsuarioOptional(String usuario) {
        SegUsuarioPk segUsuarioPk= new SegUsuarioPk();
        segUsuarioPk.setUsuario(usuario);
        segUsuarioPk.setSistema("SIGTIC");

        Optional<SegUsuario> segUsuarioOp = segUsuarioRepository.findSegUsuarioBySegUsuarioPk(segUsuarioPk);

        Optional<CatUsuarioEntePublico> catUsuarioEntePublico = catUsuarioEntePublicoRepository.findById(segUsuarioOp.get().getIdUsuarioEntePublico());
        if (catUsuarioEntePublico.isPresent())
            segUsuarioOp.get().setCatUsuarioEntePublico(catUsuarioEntePublico.get());

        return segUsuarioOp;
    }

    @Override
    public SegUsuario recuperarPassword(SegUsuario segUsuario) {

        SegUsuario seg = segUsuarioRepository.findById(segUsuario.getSegUsuarioPk()).get();
        seg.setContrasenia(bCryptPasswordEncoder.encode(segUsuario.getContraseniaNueva()));
        seg.setTokenActivo("N");


        SegPerfil segPerfilCr = segPerfilRepository.findByNombrePerfilAndSistemaAndCveEstatus("MI_CARTA_RESPONSIVA","SIGTIC","VIG");
        SegPerfil segPerfilServi = segPerfilRepository.findByNombrePerfilAndSistemaAndCveEstatus("MIS_SERVICIOS","SIGTIC","VIG");

        Optional<SegPerfilUsuario> perfilUsuarioCr = segPerfilUsuarioRepository.findBySegUsuarioAndSistemaAndSegPerfil(seg.getSegUsuarioPk().getUsuario(),"SIGTIC", segPerfilCr);
        Optional<SegPerfilUsuario> perfilUsuarioSv = segPerfilUsuarioRepository.findBySegUsuarioAndSistemaAndSegPerfil(seg.getSegUsuarioPk().getUsuario(),"SIGTIC", segPerfilServi);

        List<SegPerfilUsuario> listaPerfilUsuario = new ArrayList<>();
        if (!perfilUsuarioCr.isPresent()){
            SegPerfilUsuario segPerfilUsuario = new SegPerfilUsuario();
            segPerfilUsuario.setSegPerfil(segPerfilCr);
            segPerfilUsuario.setSegUsuario(seg.getSegUsuarioPk().getUsuario());
            segPerfilUsuario.setSistema("SIGTIC");
            listaPerfilUsuario.add(segPerfilUsuario);
        }

        if (!perfilUsuarioSv.isPresent()){
            SegPerfilUsuario segPerfilUsuario = new SegPerfilUsuario();
            segPerfilUsuario.setSegPerfil(segPerfilServi);
            segPerfilUsuario.setSegUsuario(seg.getSegUsuarioPk().getUsuario());
            segPerfilUsuario.setSistema("SIGTIC");
            listaPerfilUsuario.add(segPerfilUsuario);
        }
        segPerfilUsuarioRepository.saveAll(listaPerfilUsuario);



        return segUsuarioRepository.save(seg);
    }

}
