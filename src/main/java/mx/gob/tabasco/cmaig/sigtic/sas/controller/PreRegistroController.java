package mx.gob.tabasco.cmaig.sigtic.sas.controller;


import mx.gob.tabasco.cmaig.sigtic.Utileria.TokenUtils;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenu;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilPrivilegio;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuarioTemporal;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilPrivilegioService;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegUsuarioService;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegUsuarioTemporalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Optional;

@Controller
public class PreRegistroController {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private SegUsuarioTemporalService segUsuarioTemporalService;

    @Autowired
    private SegPerfilPrivilegioService segPerfilPrivilegioService;

    @Autowired
    private SegUsuarioService segUsuarioService;

    @Autowired
    private Environment env;

    @GetMapping("/pre-registro")
    public String preRegistroUsuario(Model model, @RequestParam(name="token") String token) {
        model.addAttribute("contextSigtic",servletContext.getContextPath());
        String correo=TokenUtils.getCorreoToken(token);
        model.addAttribute("tokenParam",token);

        model.addAttribute("generaToken", env.getProperty("sigtic.generaToken"));
        model.addAttribute("usuarioToken", env.getProperty("sigtic.usuarioToken"));
        model.addAttribute("secretToken", env.getProperty("sigtic.secretToken"));
        model.addAttribute("creaCodigo", env.getProperty("sigtic.creaCodigo"));
        model.addAttribute("validaCodigo", env.getProperty("sigtic.validaCodigo"));
        model.addAttribute("enviaSms", env.getProperty("sigtic.enviaSms"));
        model.addAttribute("enviaCorreo", env.getProperty("sigtic.enviaCorreo"));

        if (correo==null)
            model.addAttribute("error","El token no es valido");
        else
        if (correo.equals("Expired")){
            model.addAttribute("error","El token ya expiroó");
        }
        else{
            Optional<SegUsuarioTemporal> segUsuarioTemporalOp = segUsuarioTemporalService.getEstatusCorreo(correo);
            model.addAttribute("tokenParam",token);

            if (segUsuarioTemporalOp.isPresent()) {
                if (segUsuarioTemporalOp.get().getIdEntePublico()!=null)
                    model.addAttribute("verValCodigo", "SI");

                model.addAttribute("bloquear", 2);

           /* if (correo.toLowerCase().contains("gmail") || correo.toUpperCase().contains("hotmail")) {
                model.addAttribute("bloquear", 1);
            }
            else {
                model.addAttribute("bloquear", 2);
            }*/

                model.addAttribute("usuarioTemporal",segUsuarioTemporalOp.get());
            }else
                model.addAttribute("error","El correo ya fue validado");
        }


       /* List<SegUsuarioTemporal> lista=segUsuarioTemporalService.listaUsuariosTemporal("PENDIENTE");

        for (SegUsuarioTemporal segUsuarioTemporal:lista){

            if (segUsuarioTemporal.getCorreoInstitucional()!=null){
                System .out.println(segUsuarioTemporal.getCorreoInstitucional());
                System .out.println("http://10.2.8.121:8080/sigtic/pre-registro?token="+TokenUtils.createToken(segUsuarioTemporal.getCorreoInstitucional()));
                System .out.println("-----------------------------------------------------------------------------");
            }
            else{
                System .out.println(segUsuarioTemporal.getCorreoPersonal());
                System .out.println("http://10.2.8.121:8080/sigtic/pre-registro?token="+TokenUtils.createToken(segUsuarioTemporal.getCorreoPersonal()));
                System .out.println("-----------------------------------------------------------------------------");
            }
        }
*/


       //System .out.println(TokenUtils.createToken("jorgegomez@tabasco.gob.mx"));

        //System .out.println(TokenUtils.getCorreoToken(token));

        //System .out.println(TokenUtils.createToken("jose.narvaez@cobatab.edu.mx"));
        /*System .out.println("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb3JnZWdvbWV6QHRhYmFzY28uZ29iLm14IiwiaWF0IjoxNzE1Nzk4ODA0LCJleHAiOjE3MTYyMzA4MDQsImNvcnJlbyI6ImpvcmdlZ29tZXpAdGFiYXNjby5nb2IubXgifQ.OHI6GvAK_ZG_TaBFU594v80J635BK4HAevl2SVCNsDA".length());*/

        return "pre-registro/pre-registro-usuario";
    }

    @GetMapping("/pre-registro-admin")
    public String preRegistroAdmin(Model model) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SegMenu segMenu=authUser.getListaMenu().stream().filter(p->p.getCodigo().equals("PRE-REGISTRO")).findFirst().get();
        SegPerfilPrivilegio segPerfilPrivilegio = segPerfilPrivilegioService.getPerfilPrivilegioMenu("SIGTIC",authUser.getUsername(), segMenu.getIdMenu());

        model.addAttribute("perfil",segUsuarioService.getPerfil());
        model.addAttribute("listaMenu",authUser.getListaMenuBean());
        model.addAttribute("activarMenuPadre","GESTION_ACCESO");
        model.addAttribute("activarMenu","PRE-REGISTRO");
        model.addAttribute("titlePage","Administrar Pre-Registros");
        model.addAttribute("activeAgregar",segPerfilPrivilegio.getAgregar());
        model.addAttribute("activeConsultar",segPerfilPrivilegio.getConsultar());
        model.addAttribute("contextSigtic",servletContext.getContextPath());

        model.addAttribute("generaToken", env.getProperty("sigtic.generaToken"));
        model.addAttribute("usuarioToken", env.getProperty("sigtic.usuarioToken"));
        model.addAttribute("secretToken", env.getProperty("sigtic.secretToken"));
        model.addAttribute("creaCodigo", env.getProperty("sigtic.creaCodigo"));
        model.addAttribute("validaCodigo", env.getProperty("sigtic.validaCodigo"));
        model.addAttribute("enviaSms", env.getProperty("sigtic.enviaSms"));
        model.addAttribute("enviaCorreo", env.getProperty("sigtic.enviaCorreo"));

        return "seguridad/pre-registro-admin";
    }
}
