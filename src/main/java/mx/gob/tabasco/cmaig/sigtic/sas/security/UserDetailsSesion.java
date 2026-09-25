package mx.gob.tabasco.cmaig.sigtic.sas.security;

import lombok.Data;
import lombok.Getter;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.SegMenuUsuarioBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class UserDetailsSesion implements UserDetails {

    private Collection<? extends GrantedAuthority> authorities;
    private Long idUsuario;
    private String username;
    private String password;
    private String rfc;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    /*Propias*/
    //private SegUsuario segUsuario;
    private String sistema;
    private List<SegMenu> listaMenu;
    private List<SegPerfil> listaPerfil;
    private String userNameShort;
    private CatUsuarioEntePublico catUsuarioEntePublico;

    private List<SegMenuUsuarioBean> listaMenuBean;

    private Long idEntePublicoAsignado;
    private String cveEntePublicoAsignado;
    private String nombreEntePublicoAbrevAsignado;
    private String nombreEntePublicoAsignado;
    private Long idUnidadAdministrativaAsignado;
    private String cveUnidadAdministrativaAsignado;
    private String nombreUnidadAdministrativaAsignado;

    private boolean administradorSigtic;

    private String ip;
    private String hosName;


    public UserDetailsSesion(Collection<? extends GrantedAuthority> authorities, String username, String password, String rfc, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, SegUsuario segUsuario,
                             List<SegMenu> listaMenu ,List<SegPerfil> listaPerfil, CatUsuarioEntePublico catUsuarioEntePublico, String userNameShort, List<SegMenuUsuarioBean> listaMenuBean,
                             Long idEntePublicoAsignado, String cveEntePublicoAsignado, String nombreEntePublicoAbrevAsignado, String nombreEntePublicoAsignado, Long idUnidadAdministrativaAsignado, String cveUnidadAdaministrativaAsignado,
                             String nombreUnidadAdministrativaAsignado, boolean administradorSigtic) {
        this.authorities = authorities;
        this.username = username;
        this.password = password;
        this.idUsuario=segUsuario.getIdUsuario();
        this.rfc = rfc;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.sistema=segUsuario.getSegUsuarioPk().getSistema();
        this.listaMenu = listaMenu;
        this.listaPerfil=listaPerfil;
        this.catUsuarioEntePublico=catUsuarioEntePublico;
        this.userNameShort=userNameShort;
        this.listaMenuBean=listaMenuBean;

        this.idEntePublicoAsignado = idEntePublicoAsignado;
        this.cveEntePublicoAsignado = cveEntePublicoAsignado;
        this.nombreEntePublicoAbrevAsignado = nombreEntePublicoAbrevAsignado;
        this.nombreEntePublicoAsignado = nombreEntePublicoAsignado;
        this.idUnidadAdministrativaAsignado = idUnidadAdministrativaAsignado;
        this.cveUnidadAdministrativaAsignado = cveUnidadAdaministrativaAsignado;
        this.nombreUnidadAdministrativaAsignado = nombreUnidadAdministrativaAsignado;
        this.administradorSigtic = administradorSigtic;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /*Propios*/

}
