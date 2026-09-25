package mx.gob.tabasco.cmaig.sigtic.sai.restController;

import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import mx.gob.tabasco.cmaig.sigtic.sai.service.CartaResponsivaService;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Mensage;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cartaResponsiva")
public class CartaResponsivaRestController {

    @Autowired
    private CartaResponsivaService cartaResponsivaService;

    @Autowired
    private ServletContext servletContext;


    @PostMapping("/getCartaResponsiva")
    public JSONObject getCartaResponsiva(@RequestBody CartaResponsiva cartaResponsivaParam){


        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean existeRolEnlaceCartaResponsiva = authUser.getListaPerfil().stream().filter(p-> p.getNombrePerfil().equals("ENLACE-TIC-CARTA-RESPONSIVA")).findFirst().isPresent();
        boolean adminCr = authUser.getListaPerfil().stream().filter(p-> p.getNombrePerfil().equals("ADMINISTRA_CR")).findFirst().isPresent();
        boolean administradorSigtic = authUser.isAdministradorSigtic();

        List<CartaResponsiva> lista=null;

        if (administradorSigtic){
            lista = cartaResponsivaService.getCartaResponsivaAdminSigtic(cartaResponsivaParam.getEntePublico().getIdEntePublico(), cartaResponsivaParam.getIsEstatusVig(), cartaResponsivaParam.getIsEstatusHis());
        }else
        if (adminCr)
            lista = cartaResponsivaService.getCartaResponsivaAdminCr(cartaResponsivaParam.getEntePublico().getIdEntePublico(), cartaResponsivaParam.getIsEstatusVig(), cartaResponsivaParam.getIsEstatusHis());
        else
            if (existeRolEnlaceCartaResponsiva)
                lista = cartaResponsivaService.getCartaResponsivaEnlaceCr(cartaResponsivaParam.getEntePublico().getIdEntePublico(), cartaResponsivaParam.getIsEstatusVig(), cartaResponsivaParam.getIsEstatusHis());
            else
                lista=new ArrayList<>();

        String pwdTemporal="";
        String fechaPwdTemporal="";

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        String colorCircle="";
        String texto="";
            for (CartaResponsiva cartaResponsiva : lista){
                JSONArray json = new JSONArray();

                texto="";
                if (cartaResponsiva.getFirmada().equals("S"))
                    texto="Requiere Firma Autografa";

                if (cartaResponsiva.getAcepto().equals("SI") && !texto.isEmpty() && cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva()==9)
                    colorCircle = "text-warning";
                else
                if (cartaResponsiva.getAcepto().equals("SI"))
                    colorCircle="text-success";
                else
                    colorCircle="text-danger";

                json.add("<i class='bi bi-check-circle-fill h5 "+colorCircle+"' data-bs-toggle='tooltip' data-bs-html='true' data-bs-placement='top' data-bs-title='Aceptada = "+cartaResponsiva.getAcepto()+"<br/>"+texto+"'></i> "+cartaResponsiva.getFolioCr());
                json.add(cartaResponsiva.getCatSubtipoServiciosTic().getCatTipoServiciosTic().getNombreTipoServicioTic());
                json.add(cartaResponsiva.getCatSubtipoServiciosTic().getNombreSubtipoServicioTic());
                json.add("<span class='badge rounded-pill text-bg-primary' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='"+cartaResponsiva.getEntePublicoResponsable().getNombreEntePublico()+"'>"+cartaResponsiva.getEntePublicoResponsable().getCveEntePublico()+"</span> "+cartaResponsiva.getUsuario().getNombreCompleto());
                json.add(cartaResponsiva.getFechaRegistroText());
                int registros = cartaResponsivaService.countDetalleCr(cartaResponsiva);

                json.add(registros);
                json.add(cartaResponsiva.getEstatusCartaResponsiva().getNombreEstatusCartaResponsiva());
                json.add(cartaResponsiva.getCveEstatus());


                String accion="<div class='btn-group'>" +
                        "<a href=#' class='btn btn-outline-primary btn-sm' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Detalles' onclick='obtieneCartasResponsivaDetalle("+cartaResponsiva.getIdCartaResponsiva() +",\""+cartaResponsiva.getFolioCr()+"\","+cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva()+")'><i class='bi bi-building-add h5'></i></a>";

                accion+="<a href=#' class='btn btn-outline-dark btn-sm' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Seguimiento' onclick='modalSeguimientoCr("+cartaResponsiva.getIdCartaResponsiva() +",\""+cartaResponsiva.getFolioCr()+"\")'><i class='bi bi-list-ol h5'></i></a>";


                if (cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva().intValue()==9 || cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva().intValue()==11){
                    accion += "<a class='btn btn-success btn-sm' href='#' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Carta Responsiva Aceptada' onclick='cartaResponsivaAceptada(" + cartaResponsiva.getIdCartaResponsiva() + ")'><i class=\"fa-solid fa-envelope-circle-check\"></i></a>";
                    accion += "<a class='btn btn-success btn-sm' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Carta responsiva Aceptada' href='" + servletContext.getContextPath() + "/api/cartaResponsiva/cartaResponsivaReporte/" + cartaResponsiva.getIdCartaResponsiva() + "' target='_blank'><i class='bi bi-file-pdf-fill h5'></i></a>";
                }

                if (cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva().intValue()==11 && cartaResponsiva.getFileNameOriginal()!=null) {
                    accion +="<a class='btn btn-outline-success btn-sm' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Carta responsiva Con firma' href='"+servletContext.getContextPath()+"/api/cartaResponsiva/exportCartaResponsiva/"+cartaResponsiva.getIdCartaResponsiva()+"' target='_blank' ><i class='bi bi-file-earmark-pdf-fill h5'></i></a>";
                }

                if (registros>0 && (cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva()== 1 || cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva()== 10 && (administradorSigtic || adminCr))){
                    accion += "<a class='btn btn-outline-success btn-sm' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Enviar para Aceptar' href='#' onclick='enviarCartaResponsiva("+cartaResponsiva.getIdCartaResponsiva()+")'><i class='fa-solid fa-share-from-square'></i></a>";
                }

                if (existeRolEnlaceCartaResponsiva || administradorSigtic || adminCr)
                if (cartaResponsiva.getAcepto().equals("NO") && cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva()==8) {
                    if (cartaResponsiva.getTokenUrl() != null)
                        accion += "<a href=#' class='btn btn-outline-primary btn-sm' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Enviar URL' onclick='enviarMail(\"" + cartaResponsiva.getTokenUrl() + "\",\"" + cartaResponsiva.getCorreoInstitucional() + "\",\"" + cartaResponsiva.getUsuario().getNombreCompleto() + "\",\"" + cartaResponsiva.getEntePublicoResponsable().getNombreEntePublico() + "\")'><i class='far fa-envelope'></i></a>";
                    accion += "<a href=#' class='btn btn-outline-primary btn-sm' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Generar Token para el correo "+cartaResponsiva.getCorreoInstitucional()+"' onclick='generarToken(" + cartaResponsiva.getIdCartaResponsiva() + ")'><i class='fas fa-user-cog'></i></a>";
                }


                if (cartaResponsiva.getUsuario().getPwdTemporal()==null)
                    pwdTemporal="";
                else pwdTemporal=cartaResponsiva.getUsuario().getPwdTemporal();

                if (cartaResponsiva.getUsuario().getFechaPwdTemporal()==null)
                    fechaPwdTemporal="";
                else fechaPwdTemporal=cartaResponsiva.getUsuario().getFechaPwdTemporalText();

                if (!pwdTemporal.equals(""))
                    accion += "<a class='btn btn-outline-primary btn-sm' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Ver Contraseña Temporal' href='#' onclick='verPassword(\""+pwdTemporal+"\",\""+fechaPwdTemporal+"\")'><i class='bi bi-key-fill h5'></i></a>";


                accion+="</div>";

                json.add(accion);

                array.add(json);
            }

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/getMisCartaResponsiva")
    public JSONObject getMisCartaResponsiva(){
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<CartaResponsiva> lista = cartaResponsivaService.getMisCartaResponsiva();

        String pwdTemporal="";
        String fechaPwdTemporal="";

        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        String colorCircle="";
        String texto="";
        for (CartaResponsiva cartaResponsiva : lista){
            JSONArray json = new JSONArray();

            texto="";
            if (cartaResponsiva.getFirmada().equals("S"))
                texto="Requiere Firma Autografa";

            if (cartaResponsiva.getAcepto().equals("SI") && !texto.isEmpty() && cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva()==9)
                colorCircle = "text-warning";
            else
            if (cartaResponsiva.getAcepto().equals("SI"))
                colorCircle="text-success";
            else
                colorCircle="text-danger";

            json.add("<i class='bi bi-check-circle-fill h5 "+colorCircle+"' data-bs-toggle='tooltip' data-bs-html='true' data-bs-placement='top' data-bs-title='Aceptada = "+cartaResponsiva.getAcepto()+"<br/>"+texto+"'></i> "+cartaResponsiva.getFolioCr());
            json.add(cartaResponsiva.getCatSubtipoServiciosTic().getCatTipoServiciosTic().getNombreTipoServicioTic());
            json.add(cartaResponsiva.getCatSubtipoServiciosTic().getNombreSubtipoServicioTic());
            json.add("<span class='badge rounded-pill text-bg-primary' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='"+cartaResponsiva.getEntePublicoResponsable().getNombreEntePublico()+"'>"+cartaResponsiva.getEntePublicoResponsable().getCveEntePublico()+"</span> "+cartaResponsiva.getUsuario().getNombreCompleto());
            json.add(cartaResponsiva.getFechaRegistroText());
            int registros = cartaResponsivaService.countDetalleCr(cartaResponsiva);

            json.add(registros);
            json.add(cartaResponsiva.getEstatusCartaResponsiva().getNombreEstatusCartaResponsiva());
            json.add(cartaResponsiva.getCveEstatus());


            String accion="<div class='btn-group'>" +
                    "<a href=#' class='btn btn-outline-primary btn-sm' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Detalles' onclick='obtieneCartasResponsivaDetalle("+cartaResponsiva.getIdCartaResponsiva() +",\""+cartaResponsiva.getFolioCr()+"\","+cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva()+")'><i class='bi bi-building-add h5'></i></a>";

            if (registros > 0) {
                if (cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva().intValue()==8)
                    accion += "<a class='btn btn-success btn-sm' href='#' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Aceptar Carta Responsiva' onclick='aceptarCartaResponsiva(" + cartaResponsiva.getIdCartaResponsiva() + ")'><i class=\"fa-solid fa-square-envelope\"></i></a>";
            }

            if (cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva().intValue()==9 || cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva().intValue()==11){
                accion += "<a class='btn btn-primary btn-sm' href='#' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Carta Responsiva Aceptada' onclick='cartaResponsivaAceptada(" + cartaResponsiva.getIdCartaResponsiva() + ")'><i class=\"fa-solid fa-envelope-circle-check\"></i></a>";
                accion += "<a class='btn btn-success btn-sm' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Carta responsiva Aceptada' href='" + servletContext.getContextPath() + "/api/cartaResponsiva/cartaResponsivaReporte/" + cartaResponsiva.getIdCartaResponsiva() + "' target='_blank'><i class='bi bi-file-pdf-fill h5'></i></a>";
            }

            if (cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva().intValue()==11 && cartaResponsiva.getFileNameOriginal()!=null) {
                accion +="<a class='btn btn-outline-success btn-sm' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Carta responsiva Con firma' href='"+servletContext.getContextPath()+"/api/cartaResponsiva/exportCartaResponsiva/"+cartaResponsiva.getIdCartaResponsiva()+"' target='_blank' ><i class='bi bi-file-earmark-pdf-fill h5'></i></a>";
            }

            if (cartaResponsiva.getFirmada().equals("S") && cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva().intValue()==9){
                    accion +=" <a class='btn btn-outline-primary btn-sm' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Subir Carta Firmada' href='#' onclick='openModalUploadCartaResponsiva("+ cartaResponsiva.getIdCartaResponsiva() +")'><i class='bi bi-paperclip h5'></i></a>";
            }

            if (cartaResponsiva.getUsuario().getPwdTemporal()==null)
                pwdTemporal="";
            else pwdTemporal=cartaResponsiva.getUsuario().getPwdTemporal();

            if (cartaResponsiva.getUsuario().getFechaPwdTemporal()==null)
                fechaPwdTemporal="";
            else fechaPwdTemporal=cartaResponsiva.getUsuario().getFechaPwdTemporalText();


            if (!pwdTemporal.equals(""))
                accion += "<a class='btn btn-outline-primary btn-sm' role='button' data-bs-toggle='tooltip' data-bs-placement='top' data-bs-title='Ver Contraseña Temporal' href='#' onclick='verPassword(\""+pwdTemporal+"\",\""+fechaPwdTemporal+"\")'><i class='bi bi-key-fill h5'></i></a>";


            accion+="</div>";

            json.add(accion);

            array.add(json);
        }

        jsonObject.put("aaData", array);
        return jsonObject;
    }


    @GetMapping("/getCartaResponsiva/{id}")
    public CartaResponsiva getCartaResponsivaId(@PathVariable("id") Long idCartaResponsiva){

        return cartaResponsivaService.getCartaResponsivaId(idCartaResponsiva);
    }

    @GetMapping("/asignarPlantillaCartaResponsivaId/{id}")
    public CartaResponsiva asignarPlantillaCartaResponsivaId(@PathVariable("id") Long idCartaResponsiva){

        return cartaResponsivaService.asignarPlantillaCartaResponsivaId(idCartaResponsiva);
    }

    @PostMapping("/guardarCartaResponsiva")
    public CartaResponsiva guardarCartaResponsiva(@RequestBody CartaResponsiva cartaResponsiva){
        return cartaResponsivaService.generarCartaResponsiva(cartaResponsiva);

    }

    @PostMapping("/getCartaResponsivaDetalle")
    public JSONObject getCartaResponsivaDetalle(@RequestBody CartaResponsiva cartaResponsiva){


        List<CartaResponsivaDetalle> lista = cartaResponsivaService.getServiciosAdd(cartaResponsiva);
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (CartaResponsivaDetalle cartaResponsivaDetalle : lista){
            JSONArray json = new JSONArray();
/*            json.add(cartaResponsivaDetalle.getServiciosTic().getTipoServicio().getNombreTipoServicioTic());
            json.add(cartaResponsivaDetalle.getServiciosTic().getSubTipoServicio().getNombreSubtipoServicioTic());*/
            json.add(cartaResponsivaDetalle.getServiciosTic().getCveServicioTic());
            json.add(cartaResponsivaDetalle.getServiciosTic().getNombreServicioTic());
            json.add(cartaResponsivaDetalle.getServiciosTic().getEstadosServicioTic().getDescEstadoServicioTic());
            json.add(cartaResponsivaDetalle.getServiciosTic().getEstatusServicioTic().getDescEstatusServicioTic());
            json.add(cartaResponsivaDetalle.getCveEstatus());
            if (cartaResponsivaDetalle.getCartaResponsiva().getEstatusCartaResponsiva().getIdEstatusCartaResponsiva()==1 || cartaResponsivaDetalle.getCartaResponsiva().getEstatusCartaResponsiva().getIdEstatusCartaResponsiva()==3)
                json.add("<a  href='#' class='btn btn-outline-danger mr-1' data-toggle='tooltip' data-placement='top' title='Eliminar Servicio Agregado' onclick='eliminarServicioCartaResponsiva("+ cartaResponsivaDetalle.getIdCartaResponsivaDetalle() +")'><i class='fa-solid fa-trash'></i></a>");
            else
                json.add("");

            array.add(json);
        }

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/getCartaResponsivaDetalleMiCr")
    public JSONObject getCartaResponsivaDetalleMiCr(@RequestBody CartaResponsiva cartaResponsiva){


        List<CartaResponsivaDetalle> lista = cartaResponsivaService.getServiciosAdd(cartaResponsiva);
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (CartaResponsivaDetalle cartaResponsivaDetalle : lista){
            JSONArray json = new JSONArray();
            json.add(cartaResponsivaDetalle.getServiciosTic().getCveServicioTic());
            json.add(cartaResponsivaDetalle.getServiciosTic().getNombreServicioTic());
            json.add(cartaResponsivaDetalle.getServiciosTic().getEstadosServicioTic().getDescEstadoServicioTic());
            json.add(cartaResponsivaDetalle.getServiciosTic().getEstatusServicioTic().getDescEstatusServicioTic());
            json.add(cartaResponsivaDetalle.getCveEstatus());

            array.add(json);
        }

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/getCartaResponsivaLotes")
    public JSONObject getCartaResponsivaLotes(@RequestBody CartaResponsiva cartaResponsiva){


        List<CartaResponsivaDetalle> lista = cartaResponsivaService.getServiciosAdd(cartaResponsiva);
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (CartaResponsivaDetalle cartaResponsivaDetalle : lista){
            JSONArray json = new JSONArray();
            json.add(cartaResponsivaDetalle.getServiciosTic().getCveServicioTic());
            json.add(cartaResponsivaDetalle.getServiciosTic().getNombreServicioTic());
            json.add(cartaResponsivaDetalle.getServiciosTic().getEstadosServicioTic().getDescEstadoServicioTic());
            json.add(cartaResponsivaDetalle.getServiciosTic().getEstatusServicioTic().getDescEstatusServicioTic());
            json.add(cartaResponsivaDetalle.getCveEstatus());
            array.add(json);
        }

        jsonObject.put("aaData", array);
        return jsonObject;
    }


    @PostMapping("/getServiciosDisponibles")
    public JSONObject getServiciosDisponibles(@RequestBody CartaResponsiva cartaResponsiva){


        List<ServiciosTic> lista = cartaResponsivaService.getServiciosDisponibles(cartaResponsiva);
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (ServiciosTic serviciosTic : lista){
            JSONArray json = new JSONArray();

            json.add(serviciosTic.getCveServicioTic());

            json.add(serviciosTic.getNombreServicioTic());
            if (serviciosTic.getEstadosServicioTic()!=null)
                json.add(serviciosTic.getEstadosServicioTic().getDescEstadoServicioTic());
            else
                json.add("");

            if (serviciosTic.getEstatusServicioTic()!=null)
                json.add(serviciosTic.getEstatusServicioTic().getDescEstatusServicioTic());
            else
                json.add("");
            json.add(serviciosTic.getCveEstatus());
            json.add("<a  href='#' class='btn btn-primary mr-1' data-toggle='tooltip' data-placement='top' title='Agregar Servicio' onclick='addServicioCr("+ serviciosTic.getIdServicioTic() +","+cartaResponsiva.getIdCartaResponsiva()+")'><i class='fa-solid fa-circle-plus'></i></a>");

            array.add(json);
        }

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/addServicioCartaResponsiva")
    public CartaResponsivaDetalle addServicioCartaResponsiva(@RequestBody CartaResponsivaDetalle cartaResponsivaDetalle){
        return cartaResponsivaService.addServicioCartaResponsiva(cartaResponsivaDetalle);

    }

    @DeleteMapping("/deleteServicioCartaResponsiva")
    public JSONObject deleteServicioCartaResponsiva(@RequestBody CartaResponsivaDetalle cartaResponsivaDetalle){
        return cartaResponsivaService.deleteServicioCartaResponsiva(cartaResponsivaDetalle.getIdCartaResponsivaDetalle());
    }

    @GetMapping("/cartaResponsivaReporte/{id}")
    public ResponseEntity<byte[]> getReporte(@PathVariable("id") Long id) throws JRException, IOException {
        //return  cartaResponsivaService.getReporteCr(id);
        return  cartaResponsivaService.getReporteCrAceptada(id);
    }

    @PutMapping("/aceptarCartaResponsiva")
    public CartaResponsiva aceptarCartaResponsiva(@RequestBody CartaResponsiva cartaResponsiva){
        return cartaResponsivaService.aceptarCartaResponsiva(cartaResponsiva);

    }

    @PutMapping("/aceptarCartaResponsivaUser")
    public CartaResponsiva aceptarCartaResponsivaUser(@RequestBody CartaResponsiva cartaResponsiva){
        return cartaResponsivaService.aceptarCartaResponsivaUser(cartaResponsiva);

    }

    @PutMapping("/actualizarCartaResponsiva")
    public CartaResponsivaSeguimiento actualizarCartaResponsiva(@RequestBody CartaResponsiva cartaResponsiva){
        return cartaResponsivaService.actualizarCartaResponsiva(cartaResponsiva);
    }

    @PostMapping("/uploadCartaResponsivaJpa")
    public Mensage uploadCartaResponsivaJpa(@RequestBody FileUploadBean fileUploadBean){
        return cartaResponsivaService.uploadCartaResponsiva(fileUploadBean);
    }

    @GetMapping({"/exportCartaResponsiva/{id}"})
    public void exportCartaResponsiva(HttpServletResponse response, @PathVariable Long id){
        cartaResponsivaService.exportCartaResponsiva(response, id);
    }

    @PostMapping("/descarga-archivo")
    public ResponseEntity<?> descargaDocumento(@RequestBody CartaResponsiva cartaResponsiva) throws FileNotFoundException {
        File fileDocumento = new File(cartaResponsiva.getFileRoute());

        InputStreamResource respon =  new InputStreamResource(new FileInputStream(fileDocumento));
        // println("ruta "+respon.toString())
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileDocumento.getName() )
                .header("nombre", cartaResponsiva.getFileNameOriginal())
                .body(respon);
    }

    @PutMapping("/usuarioAutorizaCartaResponsiva")
    public CartaResponsiva usuarioAutorizaCartaResponsiva(@RequestBody CartaResponsiva cartaResponsiva){
        return cartaResponsivaService.usuarioAutorizaCartaResponsiva(cartaResponsiva);
    }

    @PostMapping("/getSeguimientoCartaResponsiva")
    public JSONObject getListaSeguimientoCartaResponsiva(@RequestBody CartaResponsiva cartaResponsiva){

        List<CartaResponsivaSeguimiento> lista = cartaResponsivaService.getListaSeguimientoCartaResponsiva(cartaResponsiva);
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (CartaResponsivaSeguimiento reg : lista){
            JSONArray json = new JSONArray();

            json.add(reg.getNota());
            json.add(reg.getEstatusCartaResponsiva().getNombreEstatusCartaResponsiva());
            json.add(reg.getFechaRegistroText());
            array.add(json);
        }

        jsonObject.put("aaData", array);
        return jsonObject;
    }

    @PostMapping("/generarToken")
    public CartaResponsiva generarToken(@RequestBody CartaResponsiva cartaResponsiva){
        CartaResponsiva cartaResponsiva1 = cartaResponsivaService.generarToken(cartaResponsiva.getIdCartaResponsiva());
        return cartaResponsiva1;
    }

    @PutMapping("/aceptarCr")
    public CartaResponsiva aceptarCr(@RequestBody CartaResponsiva cartaResponsiva){
        return cartaResponsivaService.aceptarCrId(cartaResponsiva.getIdCartaResponsiva());

    }

    @PutMapping("/rechazarCr")
    public CartaResponsiva rechazarCr(@RequestBody CartaResponsiva cartaResponsiva){
        return cartaResponsivaService.rechazarCrId(cartaResponsiva.getIdCartaResponsiva());

    }

    @PutMapping("/aceptarCrFree")
    public CartaResponsiva aceptarCrFree(@RequestBody CartaResponsiva cartaResponsiva){
        return cartaResponsivaService.aceptarCrFreeId(cartaResponsiva.getIdCartaResponsiva());

    }

    @PutMapping("/rechazarCrFree")
    public CartaResponsiva rechazarCrFree(@RequestBody CartaResponsiva cartaResponsiva){
        return cartaResponsivaService.rechazarCrFreeId(cartaResponsiva.getIdCartaResponsiva());

    }

    @GetMapping("/cartaResponsivaAceptada/{id}")
    public CartaResponsiva cartaResponsivaAceptada(@PathVariable("id") Long idCartaResponsiva){

        return cartaResponsivaService.cartaResponsivaAceptada(idCartaResponsiva);
    }



}
