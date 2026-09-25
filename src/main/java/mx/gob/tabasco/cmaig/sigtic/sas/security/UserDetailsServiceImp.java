package mx.gob.tabasco.cmaig.sigtic.sas.security;


import mx.gob.tabasco.cmaig.sigtic.Utileria.ClienteInfo;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.CatUsuarioEntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.EntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.UnidadAdministrativaRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.service.CatUsuarioService;
import mx.gob.tabasco.cmaig.sigtic.sas.model.*;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.SegMenuUsuarioBean;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.*;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final SegUsuarioRepository segUsuarioRepository;

    private final SegPerfilRepository segPerfilRepository;

    private final SegMenuRepository segMenuRepository;

    private final SegMenuService segMenuService;

    private final SegAccesoUsuarioRepository segAccesoUsuarioRepository;

    private final CatUsuarioEntePublicoRepository catUsuarioEntePublicoRepository;

    private final EntePublicoRepository entePublicoRepository;

    private final UnidadAdministrativaRepository unidadAdministrativaRepository;

    private final CatUsuarioService catUsuarioService;

    private final HttpServletRequest httpServletRequest;

    public UserDetailsServiceImp(SegUsuarioRepository segUsuarioRepository, SegPerfilRepository segPerfilRepository, SegMenuRepository segMenuRepository, SegMenuService segMenuService, SegAccesoUsuarioRepository segAccesoUsuarioRepository, CatUsuarioEntePublicoRepository catUsuarioEntePublicoRepository, @Qualifier("entePublicoRepositorySac") EntePublicoRepository entePublicoRepository,  @Qualifier("unidadAdministrativaRepositorySac") UnidadAdministrativaRepository unidadAdministrativaRepository, CatUsuarioService catUsuarioService, HttpServletRequest httpServletRequest) {
        this.segUsuarioRepository = segUsuarioRepository;
        this.segPerfilRepository = segPerfilRepository;
        this.segMenuRepository = segMenuRepository;
        this.segMenuService = segMenuService;
        this.segAccesoUsuarioRepository = segAccesoUsuarioRepository;
        this.catUsuarioEntePublicoRepository = catUsuarioEntePublicoRepository;
        this.entePublicoRepository = entePublicoRepository;
        this.unidadAdministrativaRepository = unidadAdministrativaRepository;
        this.catUsuarioService = catUsuarioService;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

         /*  String ip="";
            String hosName="";

        ip= ClienteInfo.getClientIpAddr(httpServletRequest);
        hosName=ClienteInfo.getHostPC(httpServletRequest);*/

        SegUsuarioPk segUsuarioPk =  new SegUsuarioPk();
        segUsuarioPk.setSistema("SIGTIC");
        segUsuarioPk.setUsuario(username);
        Optional<SegUsuario> segUsuario = segUsuarioRepository.findById(segUsuarioPk);

        if (!segUsuario.isPresent()){
            throw new NoSuchElementException("El usuario no esta registrado.");}

        if (segUsuario.get().getBloqueado()==1)
            throw new NoSuchElementException("El usuario esta bloqueado.");

        CatUsuario catUsuario =catUsuarioService.getUsuario(segUsuario.get().getIdUsuario());

/*        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(9);
        String password2=bCryptPasswordEncoder.encode("s123");
        System.out.println(password2);*/

        List<SegPerfil> listaPerfil = segPerfilRepository.getRolesByUsuarioAndEstatus(username,"VIG");


        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_RECURSOS"));

        boolean isAdminSigtic = false;

        for (SegPerfil segPerfil:listaPerfil){
            authorities.add(new SimpleGrantedAuthority("ROLE_"+segPerfil.getNombrePerfil()));
            if (segPerfil.getNombrePerfil().equals("ADMIN_SIGTIC"))
                isAdminSigtic=true;
        }

        List<SegMenu> listaMenu = segMenuRepository.getMenuByUsuario("SIGTIC", username);

        List<SegMenuUsuarioBean> listaMenuBean = segMenuService.getMenuUsuarioSesion(username);

        Optional<CatUsuarioEntePublico> catUsuarioEntePublico= catUsuarioEntePublicoRepository.findById(segUsuario.get().getIdUsuarioEntePublico());

        String userNameShort="";
        if (catUsuarioEntePublico.isPresent()){
            if (!catUsuarioEntePublico.get().getCveEstatus().equals("VIG"))
                throw new NoSuchElementException("El usuario que esta vigente en el sistema, ya no esta esta vigente en el ente público: "+ catUsuarioEntePublico.get().getEntePublico().getNombreEntePublico());

            userNameShort=catUsuarioEntePublico.get().getCatUsuario().getNombre().charAt(0)+". ";
            if (catUsuarioEntePublico.get().getCatUsuario().getApellidoPaterno()!=null && !catUsuarioEntePublico.get().getCatUsuario().getApellidoPaterno().equals(""))
                userNameShort+=catUsuarioEntePublico.get().getCatUsuario().getApellidoPaterno();
            if ((catUsuarioEntePublico.get().getCatUsuario().getApellidoPaterno()==null && catUsuarioEntePublico.get().getCatUsuario().getApellidoMaterno()!=null &&
                    !catUsuarioEntePublico.get().getCatUsuario().getApellidoMaterno().equals("")))
                userNameShort+=catUsuarioEntePublico.get().getCatUsuario().getApellidoMaterno();


        }else
            throw new NoSuchElementException("El usuario no esta asignado a un ente público.");


        List<SegAccesoUsuario> lista = segAccesoUsuarioRepository.findAllByRfcUsuarioAndIdSistemaAndCveEstatus(username.trim().toUpperCase(), "SIGTIC", "VIG");
        EntePublico entePublico=null;
        UnidadAdministrativa unidadAdministrativa=null;

        if (lista.size()==1) {
            if (lista.get(0).getIdEntePublico()!=null)
                entePublico = entePublicoRepository.findById(lista.get(0).getIdEntePublico()).get();

            if (lista.get(0).getIdUnidadAdministrativa()!=null)
                unidadAdministrativa = unidadAdministrativaRepository.findById(lista.get(0).getIdUnidadAdministrativa()).get();
        }

       UserDetailsSesion userDetailsSesion=new UserDetailsSesion(
               authorities,
               segUsuario.get().getSegUsuarioPk().getUsuario(),
               segUsuario.get().getContrasenia(),
               catUsuario.getRfc(),
               true,
               true,
               true,
               segUsuario.get().getCveEstatus().equals("VIG"),
               segUsuario.get(),
               listaMenu,
               listaPerfil,
               catUsuarioEntePublico.get(),
               userNameShort,
               listaMenuBean,
               entePublico!=null ? entePublico.getIdEntePublico(): null,
               entePublico!=null ? entePublico.getCveEntePublico(): null,
               entePublico!=null ? entePublico.getNombreEntePublicoAbreviada() : null,
               entePublico!=null ? entePublico.getNombreEntePublico(): null,
               unidadAdministrativa!=null ? unidadAdministrativa.getIdUnidadAdministrativa(): null,
               unidadAdministrativa!=null ? unidadAdministrativa.getCveUnidadAdministrativa(): null,
               unidadAdministrativa!=null ? unidadAdministrativa.getNombreUnidadAdministrativa(): null,
               isAdminSigtic
       );
        return userDetailsSesion;
    }
}
