package mx.gob.tabasco.cmaig.sigtic.sac.restController;


import mx.gob.tabasco.cmaig.sigtic.sac.model.Logo;
import mx.gob.tabasco.cmaig.sigtic.sac.model.LogoBean;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sac.service.LogoService;
import net.minidev.json.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/logo")
public class LogoRestController {

    @Autowired
    private LogoService logoService;

    @Autowired
    private ServletContext servletContext;


    @GetMapping("/listaLogos")
    public JSONObject getLogos(){

        List<LogoBean> listaLogo = logoService.getLogos();

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (LogoBean logo : listaLogo)
        {
            JSONArray json = new JSONArray();
            json.add(logo.getNombreLogo());
            json.add(logo.getNombreLogoInterno());
            json.add(logo.getNombreEntePublico()!=null?logo.getNombreEntePublico():"");
            json.add(logo.getNombreUnidadAdministrativa()!=null?logo.getNombreUnidadAdministrativa():"");
            json.add(logo.getAncho()+"x"+logo.getAlto());
            json.add("<img src="+servletContext.getContextPath()+"/api/logo/exportLogo/"+logo.getIdLogo()+" width=\"400px\" height=\"100\" />");
            json.add("<div class=\"btn-group\" role=\"group\">" +
                    "<a href='javascript:void(0);' class='btn btn-success' role='button' data-toggle='tooltip' data-placement='top' title='Asignar Logo' onclick='asignarLogoModal(" + logo.getIdLogo() + ")'><i class=\"fa-solid fa-images\"></i></a>"+
                    "<a href='javascript:void(0);' class='btn btn-danger' role='button' data-toggle='tooltip' data-placement='top' title='Eliminar Logo' onclick='deleteLogo(" + logo.getIdLogo() + ")'><i class='bi bi-trash3'></i></a>" +
                    "</div>");

            array.add(json);
        }


        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @GetMapping("/listaLogosEnte")
    public JSONObject getLogosEnte(){

        List<LogoBean> listaLogo = logoService.getLogosEnte();

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (LogoBean logo : listaLogo)
        {
            JSONArray json = new JSONArray();
            json.add(logo.getNombreLogo());
            json.add(logo.getNombreLogoInterno());
            json.add(logo.getNombreUnidadAdministrativa()!=null?logo.getNombreUnidadAdministrativa():"");
            json.add("<img src="+servletContext.getContextPath()+"/api/logo/exportLogo/"+logo.getIdLogo()+" width=\"400px\" height=\"100\" />");
            json.add("<div class=\"btn-group\" role=\"group\">" +
                    "<a href='javascript:void(0);' class='btn btn-success' role='button' data-toggle='tooltip' data-placement='top' title='Seleccionar Logo' onclick='seleccionarLogo(" + logo.getIdLogo() + ",\""+servletContext.getContextPath()+"/api/logo/exportLogo/"+logo.getIdLogo()+"\")'><i class=\"fa-solid fa-circle-plus\"></i></a>"+
                    "</div>");
            array.add(json);
        }

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/importarLogo")
    public Logo importarLogo(@RequestBody Logo logo){
        return logoService.importarLogo(logo);

    }

    @GetMapping({"/exportLogo/{id}"})
    public void exportLogo(HttpServletResponse response, @PathVariable Integer id){
        logoService.exportLogo(response, id);
    }

    @GetMapping({"/exportLogoCr/{id}"})
    public void exportLogoCr(HttpServletResponse response, @PathVariable Long id){
        logoService.exportLogoCr(response, id);
    }

    @DeleteMapping({"/deleteLogo"})
    public Logo deleteLogo(@RequestBody Logo logo){
        return  logoService.deleteLogo(logo.getIdLogo());
    }

    @PostMapping({"/asignarLogo"})
    public UnidadAdministrativa asignarLogo(@RequestBody UnidadAdministrativa unidadAdministrativa){
        return  logoService.asignarLogo(unidadAdministrativa);
    }
}
