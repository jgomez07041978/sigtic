package mx.gob.tabasco.cmaig.sigtic.sai.service;


import mx.gob.tabasco.cmaig.sigtic.Utileria.TokenUtils;
import mx.gob.tabasco.cmaig.sigtic.Utileria.Utileria;
import mx.gob.tabasco.cmaig.sigtic.Utileria.UtileriaReporte;
import mx.gob.tabasco.cmaig.sigtic.sac.model.*;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.*;
import mx.gob.tabasco.cmaig.sigtic.sac.service.CatUsuarioEntePublicoService;
import mx.gob.tabasco.cmaig.sigtic.sac.service.EntePublicoService;
import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sai.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.*;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatUsuarioEntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.EntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfilUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuarioPk;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.RequestService;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegPerfilUsuarioService;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegUsuarioService;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Mensage;
import net.minidev.json.JSONObject;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
public class CartaResponsivaServiceImp implements CartaResponsivaService{

    @Autowired
    private CartaResponsivaRepository cartaResponsivaRepository;

    @Autowired
    private EntePublicoRepository entePublicoRepository;

    @Autowired
    private mx.gob.tabasco.cmaig.sigtic.sac.repository.EntePublicoRepository entePublicoSacRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CartaResponsivaDetalleRepository cartaResponsivaDetalleRepository;

    @Autowired
    private ServiciosTicRepository serviciosTicRepository;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private CatEstatusCartaResponsivaRepository catEstatusCartaResponsivaRepository;

    @Autowired
    private RequestService requestService;

    @Autowired
    private Environment environment;

    @Autowired
    private CartaResponsivaSeguimientoRepository cartaResponsivaSeguimientoRepository;

    @Autowired
    private CatSubtipoServiciosFirmaRepository catSubtipoServiciosFirmaRepository;

    @Autowired
    private CatSubtipoServiciosTicRepository catSubtipoServiciosTicRepository;

    @Autowired
    private CatUsuarioEntePublicoService catUsuarioEntePublicoService;

    @Autowired
    @Qualifier("catUsuarioEntePublicoRepositorySai")
    private CatUsuarioEntePublicoRepository catUsuarioEntePublicoRepository;

    @Autowired
    private EntePublicoService entePublicoService;

    @Autowired
    private CatTipoServiciosTicRepository catTipoServiciosTicRepository;

    @Autowired
    private SegUsuarioService segUsuarioService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SegPerfilUsuarioService segPerfilUsuarioService;

    @Autowired
    private BitacoraMovimientoService bitacoraMovimientoService;

    @Autowired
    private CatUsuarioRepository catUsuarioRepository;

    @Autowired
    private CatEstatusServicioRepository catEstatusServicioRepository;

    @Autowired
    private CatPlantillaReporteRepository catPlantillaReporteRepository;

    @Autowired
    private CatPlantillaReporteParrafoRepository catPlantillaReporteParrafoRepository;

    @Autowired
    private CartaResponsivaParrafoRepository cartaResponsivaParrafoRepository;

    @Autowired
    private UsuarioServicioRepository usuarioServicioRepository;

    @Autowired
    private SubServicioUsuarioRepository subServicioUsuarioRepository;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public List<CartaResponsiva> getCartaResponsivaAdminSigtic(Long idEntePublico, boolean isEstatusVig, boolean isEstatusHis) {
        List<String> listaEstatus=new ArrayList<>();
        if (isEstatusVig)
            listaEstatus.add("VIG");

        if (isEstatusHis)
            listaEstatus.add("HIS");

        EntePublico entePublico = entePublicoRepository.findById(idEntePublico.longValue()).get();

        return cartaResponsivaRepository.getCartaResponsivaAdminSigtic(entePublico, listaEstatus);
    }

    @Override
    public List<CartaResponsiva> getCartaResponsivaAdminCr(Long idEntePublico, boolean isEstatusVig, boolean isEstatusHis) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<String> listaEstatus=new ArrayList<>();
        if (isEstatusVig)
            listaEstatus.add("VIG");

        if (isEstatusHis)
            listaEstatus.add("HIS");

        Usuario usuario = usuarioRepository.findById(authUser.getIdUsuario()).get();
        List<SubServicioUsuario> listaSubServicioUsuario = subServicioUsuarioRepository.findAllByUsuario(usuario);
        List<CatSubtipoServiciosTic> lista = listaSubServicioUsuario.stream().map(SubServicioUsuario::getCatSubtipoServiciosTic).collect(Collectors.toList());
        EntePublico entePublico = entePublicoRepository.findById(idEntePublico.longValue()).get();

        if (lista.size()>0)
            return cartaResponsivaRepository.getCartaResponsivaAdminCr(entePublico,lista, listaEstatus);
        else
            return new ArrayList<>();
    }

    @Override
    public List<CartaResponsiva> getCartaResponsivaEnlaceCr(Long idEntePublico, boolean isEstatusVig, boolean isEstatusHis) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<String> listaEstatus=new ArrayList<>();
        if (isEstatusVig)
            listaEstatus.add("VIG");

        if (isEstatusHis)
            listaEstatus.add("HIS");

        Usuario usuario = usuarioRepository.findById(authUser.getIdUsuario()).get();
        List<SubServicioUsuario> listaSubServicioUsuario = subServicioUsuarioRepository.findAllByUsuario(usuario);
        List<CatSubtipoServiciosTic> lista = listaSubServicioUsuario.stream().map(SubServicioUsuario::getCatSubtipoServiciosTic).collect(Collectors.toList());
        EntePublico entePublico = entePublicoRepository.findById(idEntePublico.longValue()).get();
        if (lista.size()>0)
            return cartaResponsivaRepository.getCartaResponsivaEnlaceCr(entePublico,lista, listaEstatus);
        else
            return new ArrayList<>();
    }

    @Override
    public List<CartaResponsiva> getCartaResponsivaPorEnte(CartaResponsiva cartaResponsiva, boolean existeRolEnlaceCartaResponsiva, boolean existeRolUsuarioCartaResponsiva, boolean isEstatusVig, boolean isEstatusHis) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        EntePublico entePublicoResponsable = entePublicoRepository.findById(authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico()).get();

        List<CartaResponsiva> lista=null;

        List<String> listaEstatus=new ArrayList<>();
        if (isEstatusVig)
            listaEstatus.add("VIG");

        if (isEstatusHis)
            listaEstatus.add("HIS");

        if (authUser.isAdministradorSigtic() && cartaResponsiva.getEntePublico()!=null && cartaResponsiva.getEntePublico().getIdEntePublico()!=null){

            if (listaEstatus.size()==0)
                lista=new ArrayList<>();
            else {
                EntePublico entePublico1 = entePublicoRepository.findById(cartaResponsiva.getEntePublico().getIdEntePublico()).get();

                if (cartaResponsiva.getCatSubtipoServiciosTic().getIdSubtipoServicioTic() == null && cartaResponsiva.getIdTipoServicio() == null) {
                    lista = cartaResponsivaRepository.getCartaResponsivaAdmin(entePublico1, entePublicoResponsable, listaEstatus);
                }
                else if (cartaResponsiva.getCatSubtipoServiciosTic().getIdSubtipoServicioTic() == null && cartaResponsiva.getIdTipoServicio() != null) {
                    CatTipoServiciosTic catTipoServiciosTic = catTipoServiciosTicRepository.findById(cartaResponsiva.getIdTipoServicio()).get();
                    List<CatSubtipoServiciosTic> listaSubtipo = catSubtipoServiciosTicRepository.findAllByCatTipoServiciosTicAndCveEstatusOrderByNombreSubtipoServicioTic(catTipoServiciosTic, "VIG");
                    lista = cartaResponsivaRepository.getCartaResponsivaAdminSubtipoIn(entePublico1, entePublicoResponsable, listaSubtipo, listaEstatus);
                } else if (cartaResponsiva.getCatSubtipoServiciosTic().getIdSubtipoServicioTic() != null && cartaResponsiva.getIdTipoServicio() != null) {
                    CatSubtipoServiciosTic catSubtipoServiciosTic = catSubtipoServiciosTicRepository.findById(cartaResponsiva.getCatSubtipoServiciosTic().getIdSubtipoServicioTic()).get();
                    lista = cartaResponsivaRepository.getCartaResponsivaAdminSubtipo(entePublico1, entePublicoResponsable, catSubtipoServiciosTic, listaEstatus);
                }
            }
        }else
        if (existeRolEnlaceCartaResponsiva){
            if (cartaResponsiva.getCatSubtipoServiciosTic().getIdSubtipoServicioTic()==null && cartaResponsiva.getIdTipoServicio()==null)
                lista=cartaResponsivaRepository.getCartaResponsivaEnlace(entePublicoResponsable, listaEstatus);
            else
            if (cartaResponsiva.getCatSubtipoServiciosTic().getIdSubtipoServicioTic()==null && cartaResponsiva.getIdTipoServicio()!=null) {
                CatTipoServiciosTic catTipoServiciosTic = catTipoServiciosTicRepository.findById(cartaResponsiva.getIdTipoServicio()).get();
                List<CatSubtipoServiciosTic> listaSubtipo = catSubtipoServiciosTicRepository.findAllByCatTipoServiciosTicAndCveEstatusOrderByNombreSubtipoServicioTic(catTipoServiciosTic, "VIG");
                lista=cartaResponsivaRepository.getCartaResponsivaEnlaceSubtipoIn(entePublicoResponsable,listaSubtipo, listaEstatus);
            }else
            if (cartaResponsiva.getCatSubtipoServiciosTic().getIdSubtipoServicioTic()!=null && cartaResponsiva.getIdTipoServicio()!=null) {
                CatSubtipoServiciosTic catSubtipoServiciosTic = catSubtipoServiciosTicRepository.findById(cartaResponsiva.getCatSubtipoServiciosTic().getIdSubtipoServicioTic()).get();
                lista=cartaResponsivaRepository.getCartaResponsivaEnlaceSubtipo(entePublicoResponsable,catSubtipoServiciosTic,listaEstatus);
            }
        }
        else{
            if (existeRolUsuarioCartaResponsiva){
                Usuario usuario = usuarioRepository.findById(authUser.getIdUsuario()).get();
                if (cartaResponsiva.getCatSubtipoServiciosTic().getIdSubtipoServicioTic()==null && cartaResponsiva.getIdTipoServicio()==null)
                    lista=cartaResponsivaRepository.getCartaResponsivaResponsable(entePublicoResponsable, usuario, listaEstatus);
                else
                if (cartaResponsiva.getCatSubtipoServiciosTic().getIdSubtipoServicioTic()==null && cartaResponsiva.getIdTipoServicio()!=null) {
                    CatTipoServiciosTic catTipoServiciosTic = catTipoServiciosTicRepository.findById(cartaResponsiva.getIdTipoServicio()).get();
                    List<CatSubtipoServiciosTic> listaSubtipo = catSubtipoServiciosTicRepository.findAllByCatTipoServiciosTicAndCveEstatusOrderByNombreSubtipoServicioTic(catTipoServiciosTic, "VIG");
                    lista=cartaResponsivaRepository.getCartaResponsivaSubtipoIn(entePublicoResponsable, usuario, listaSubtipo, listaEstatus);
                }else
                if (cartaResponsiva.getCatSubtipoServiciosTic().getIdSubtipoServicioTic()!=null && cartaResponsiva.getIdTipoServicio()!=null) {
                    CatSubtipoServiciosTic catSubtipoServiciosTic = catSubtipoServiciosTicRepository.findById(cartaResponsiva.getCatSubtipoServiciosTic().getIdSubtipoServicioTic()).get();
                    lista=cartaResponsivaRepository.getCartaResponsivaSubtipo(entePublicoResponsable, usuario, catSubtipoServiciosTic, listaEstatus);
                }
            }
        }
        if (lista==null)
            lista=new ArrayList<>();

        return lista;
    }

    @Override
    public List<CartaResponsiva> getMisCartaResponsiva() {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Usuario usuario = usuarioRepository.findById(authUser.getIdUsuario()).get();

        List<String> listaEstatus = Arrays.asList("VIG","HIS");

        List<CartaResponsiva> listaCr =  cartaResponsivaRepository.getMisCartasResponsivas(usuario, listaEstatus);
        return listaCr;
    }

    @Override
    public CartaResponsiva generarCartaResponsiva(CartaResponsiva cartaResponsiva) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Calendar cal= Calendar.getInstance();
        String yearFull=String.valueOf(cal.get(Calendar.YEAR));
        int year= cal.get(Calendar.YEAR);

        if (yearFull.length()==4)
            year=Integer.parseInt(yearFull.substring(2));

        boolean existeRolEnlaceCartaResponsiva = authUser.getListaPerfil().stream().filter(p->p.getNombrePerfil().equals("ENLACE-TIC-CARTA-RESPONSIVA") ).findFirst().isPresent();


        EntePublico entePublico = entePublicoRepository.findById(cartaResponsiva.getEntePublico().getIdEntePublico()).get();
        EntePublico entePublicoResponsable = null;

        if (cartaResponsiva.getEntePublicoResponsable().getIdEntePublico()!=null)
            entePublicoResponsable = entePublicoRepository.findById(cartaResponsiva.getEntePublicoResponsable().getIdEntePublico()).get();
        else
            entePublicoResponsable = entePublicoRepository.findById(authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico()).get();

        Usuario usuario = null;

        if (!authUser.isAdministradorSigtic() && !existeRolEnlaceCartaResponsiva)
            usuario= usuarioRepository.findById(authUser.getIdUsuario()).get();
        else
        if (cartaResponsiva.getUsuario().getIdUsuario()!=null)
            usuario= usuarioRepository.findById(cartaResponsiva.getUsuario().getIdUsuario()).get();
        else
            throw new NoSuchElementException("El usuario no existe");

        CatUsuarioEntePublico catUsuarioEntePublico=catUsuarioEntePublicoService.getEntesPublicoVigentePorIdEnteSai(usuario.getIdUsuario(), entePublicoResponsable.getIdEntePublico());


        Optional<CartaResponsiva>  cartaResponsivaOp = cartaResponsivaRepository.findTopByEntePublicoAndAnioOrderByFolioCartaResponsivaDesc(entePublico, year);

        int folio = 1;

        if (cartaResponsivaOp.isPresent())
            folio=cartaResponsivaOp.get().getFolioCartaResponsiva().intValue()+1;

        Usuario usuarioMtto= usuarioRepository.findById(authUser.getIdUsuario()).get();


        CatEstatusCartaResponsiva catEstatusCartaResponsiva = catEstatusCartaResponsivaRepository.findById(1).get();
        
        CatSubtipoServiciosTic catSubtipoServiciosTic=catSubtipoServiciosTicRepository.findById(cartaResponsiva.getCatSubtipoServiciosTic().getIdSubtipoServicioTic()).get();

        CartaResponsiva cartaResponsivaNew = new CartaResponsiva();
        cartaResponsivaNew.setFolioCartaResponsiva(folio);
        cartaResponsivaNew.setEntePublico(entePublico);
        cartaResponsivaNew.setEntePublicoResponsable(entePublicoResponsable);
        cartaResponsivaNew.setNotificacionEntregaServicio(cartaResponsiva.getNotificacionEntregaServicio());
        cartaResponsivaNew.setCveDependencia(entePublico.getCveEntePublico());
        cartaResponsivaNew.setNombreDependencia(entePublico.getNombreEntePublico());
        cartaResponsivaNew.setCveDependenciaResponsable(entePublicoResponsable.getCveEntePublico());
        cartaResponsivaNew.setNombreDependenciaResponsable(entePublicoResponsable.getNombreEntePublico());

        if (catUsuarioEntePublico!=null){
                cartaResponsivaNew.setCatUsuarioEntePublico(catUsuarioEntePublico);
                cartaResponsivaNew.setCargoUsuarioResponsable(catUsuarioEntePublico.getPuesto());
            if (catUsuarioEntePublico.getCorreoInstitucional()!=null)
                cartaResponsivaNew.setCorreoInstitucional(catUsuarioEntePublico.getCorreoInstitucional());

            if (catUsuarioEntePublico.getCatUsuario().getCorreoPersonal()!=null)
                cartaResponsivaNew.setCorreoPersonal(catUsuarioEntePublico.getCatUsuario().getCorreoPersonal());

            if (catUsuarioEntePublico.getCatUsuario().getNumCelular()!=null && catUsuarioEntePublico.getCatUsuario().getNumCelular().trim().length()==10)
                cartaResponsivaNew.setCelular(catUsuarioEntePublico.getCatUsuario().getNumCelular().trim());
        }

        cartaResponsivaNew.setFechaRegistro(new Timestamp(new Date().getTime()));
        cartaResponsivaNew.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        cartaResponsivaNew.setUsuario(usuario);
        cartaResponsivaNew.setCveEstatus("VIG");
        cartaResponsivaNew.setAcepto("NO");
        cartaResponsivaNew.setAnio(year);
        cartaResponsivaNew.setEstatusCartaResponsiva(catEstatusCartaResponsiva);
        cartaResponsivaNew.setIpRegistro(authUser.getIp());
        cartaResponsivaNew.setHostRegistro(authUser.getHosName());
        cartaResponsivaNew.setUsuarioMtto(usuarioMtto);
        cartaResponsivaNew.setCatSubtipoServiciosTic(catSubtipoServiciosTic);
        cartaResponsivaNew.setTipoServicio(catSubtipoServiciosTic.getCatTipoServiciosTic().getNombreTipoServicioTic());
        cartaResponsivaNew.setSubtipoServicio(catSubtipoServiciosTic.getNombreSubtipoServicioTic());
        String firmada = cartaResponsiva.getFirmada();
        if (firmada == null || !(firmada.equals("S") || firmada.equals("N")))
            throw new NoSuchElementException("Seleccione el tipo de firma (autógrafa o e.firma)");
        cartaResponsivaNew.setFirmada(firmada);

        String folioCr="";

        if (entePublico!=null)
            folioCr=entePublico.getCveEntePublico();

        folioCr=folioCr+"-"+String.format("%06d",folio)+"-"+year;

        cartaResponsivaNew.setFolioCr(folioCr);

        CartaResponsiva cr=cartaResponsivaRepository.save(cartaResponsivaNew);

        CartaResponsivaSeguimiento cartaResponsivaSeguimiento = new CartaResponsivaSeguimiento();
        cartaResponsivaSeguimiento.setEstatusCartaResponsiva(catEstatusCartaResponsiva);
        cartaResponsivaSeguimiento.setFechaRegistro(new Timestamp(new Date().getTime()));
        cartaResponsivaSeguimiento.setNota("Se Genera carta responsiva");
        cartaResponsivaSeguimiento.setIpRegistro(authUser.getIp());
        cartaResponsivaSeguimiento.setIdUsuarioRegistro(authUser.getIdUsuario());

        cartaResponsivaSeguimiento.setCartaResponsiva(cr);

        cartaResponsivaSeguimientoRepository.save(cartaResponsivaSeguimiento);


        bitacoraMovimientoService.guardarBitacoraMovimiento(cr.getIdCartaResponsiva(), "CARTA_RESPONSIVA", "Se genera carta responsiva");

        return cr;
    }

    @Transactional(value = "saiSigticTransactionManager")
    @Override
    public CartaResponsiva generarCartaResponsivaServicio(ServiciosTic serviciosTic) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Calendar cal= Calendar.getInstance();
        int year= cal.get(Calendar.YEAR);

        Usuario usuarioMtto= usuarioRepository.findById(authUser.getIdUsuario()).get();

        Optional<CartaResponsiva>  cartaResponsivaOp = cartaResponsivaRepository.findTopByEntePublicoAndAnioOrderByFolioCartaResponsivaDesc(serviciosTic.getEntePublicoAdmin(), year);

        int folio = 1;

        if (cartaResponsivaOp.isPresent())
            folio=cartaResponsivaOp.get().getFolioCartaResponsiva().intValue()+1;

        CatEstatusCartaResponsiva catEstatusCartaResponsiva = catEstatusCartaResponsivaRepository.findById(8).get();

        CartaResponsiva cartaResponsivaNew = new CartaResponsiva();
        cartaResponsivaNew.setFolioCartaResponsiva(folio);
        cartaResponsivaNew.setEntePublico(serviciosTic.getEntePublicoAdmin());
        cartaResponsivaNew.setEntePublicoResponsable(serviciosTic.getEntePublicoOpera());

        cartaResponsivaNew.setFechaRegistro(new Timestamp(new Date().getTime()));
        cartaResponsivaNew.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        cartaResponsivaNew.setCveEstatus("VIG");
        cartaResponsivaNew.setAcepto("NO");
        cartaResponsivaNew.setAnio(year);
        cartaResponsivaNew.setEstatusCartaResponsiva(catEstatusCartaResponsiva);
        cartaResponsivaNew.setIpRegistro(authUser.getIp());
        cartaResponsivaNew.setHostRegistro(authUser.getHosName());
        cartaResponsivaNew.setUsuarioMtto(usuarioMtto);
        cartaResponsivaNew.setCatSubtipoServiciosTic(serviciosTic.getSubTipoServicio());
        cartaResponsivaNew.setTipoServicio(serviciosTic.getSubTipoServicio().getCatTipoServiciosTic().getNombreTipoServicioTic());
        cartaResponsivaNew.setSubtipoServicio(serviciosTic.getSubTipoServicio().getNombreSubtipoServicioTic());

        String folioCr="";
        folioCr=serviciosTic.getEntePublicoAdmin().getCveEntePublico();

        folioCr=folioCr+"-"+String.format("%06d",folio)+"-"+year;

        cartaResponsivaNew.setFolioCr(folioCr);

        CartaResponsiva cartaResponsiva = cartaResponsivaRepository.save(cartaResponsivaNew);

        bitacoraMovimientoService.guardarBitacoraMovimiento(cartaResponsiva.getIdCartaResponsiva(), "CARTA_RESPONSIVA", "Se genera carta responsiva desde autorización de servicio");

        CartaResponsivaDetalle cartaResponsivaDetalle = new CartaResponsivaDetalle();
        cartaResponsivaDetalle.setCartaResponsiva(cartaResponsiva);
        cartaResponsivaDetalle.setServiciosTic(serviciosTic);
        cartaResponsivaDetalle.setFechaRegistro(new Timestamp(new Date().getTime()));
        cartaResponsivaDetalle.setCveEstatus("VIG");
        cartaResponsivaDetalle.setIdUsuarioRegistro(authUser.getIdUsuario());
        cartaResponsivaDetalle.setIpRegistro(authUser.getIp());
        cartaResponsivaDetalle.setHostNameRegistro(authUser.getHosName());
        CartaResponsivaDetalle cartaResponsivaDetalle1=cartaResponsivaDetalleRepository.save(cartaResponsivaDetalle);

        bitacoraMovimientoService.guardarBitacoraMovimiento(cartaResponsivaDetalle1.getIdCartaResponsivaDetalle(), "CARTA_RESPONSIVA_DETALLE", "Se genera detalles de la carta responsiva desde autorización de servicio");

        return cartaResponsiva;
    }

    @Override
    public CartaResponsiva getCartaResponsivaId(Long idCartaResponsiva) {
        CartaResponsiva cr= cartaResponsivaRepository.findById(idCartaResponsiva).get();
        return cr;
    }

    @Override
    public CartaResponsiva asignarPlantillaCartaResponsivaId(Long idCartaResponsiva) {
        CartaResponsiva cr= cartaResponsivaRepository.findById(idCartaResponsiva).get();

        if (cr.getIdPlantillaReporte()==null){
            String firmada = cr.getFirmada() != null ? cr.getFirmada() : "S";
            List<CatPlantillaReporte> listaPlantilla = catPlantillaReporteRepository.getPlantillaReportePorEnteSubtipoServicioFirmada(
                    cr.getEntePublico().getIdEntePublico(),
                    cr.getCatSubtipoServiciosTic().getIdSubtipoServicioTic(),
                    firmada);

            if (listaPlantilla.size()==0)
                throw new NoSuchElementException("No existe plantilla disponible para está carta responsiva");
            else
            if (listaPlantilla.size()>1)
                throw new NoSuchElementException("Existe más de una plantilla disponible para esta carta responsiva");
            else
            {
                cr.setIdPlantillaReporte(listaPlantilla.get(0).getIdPlantillaReporte());
                cr=cartaResponsivaRepository.save(cr);
                cr.setCatPlantillaReporte(listaPlantilla.get(0));
            }
        }else {
            CatPlantillaReporte catPlantillaReporte = catPlantillaReporteRepository.findById(cr.getIdPlantillaReporte()).get();
            cr.setCatPlantillaReporte(catPlantillaReporte);

        }


        if (cr.getIdPlantillaReporte()!=null){
            //CatUsuarioEntePublico catUsuarioEntePublico=catUsuarioEntePublicoRepository.findById(cr.getIdUsuarioEntePublico()).get();
            List<CatPlantillaReporteParrafo> catPlantillaReporteParrafoList = catPlantillaReporteParrafoRepository.findAllByIdPlantillaReporteAndCveEstatusOrderByNumeroOrdenAsc(cr.getIdPlantillaReporte(),"VIG");

            String cartaText="";
            for (CatPlantillaReporteParrafo cp:catPlantillaReporteParrafoList){
                if (cp.getParrafo().contains("##infoComplementaria##")) {
                    if (cr.getNotificacionEntregaServicio() != null)
                        cartaText += "<p>" + cp.getParrafo() + "</p><br/>";
                }
                else
                    cartaText+="<p>"+cp.getParrafo()+"</p><br/>";
            }
            /*Remplazando datos*/
            cartaText=cartaText.replace("##NombreResponsable##",cr.getUsuario().getNombreCompleto());

            cartaText=cartaText.replace("##generarTabla##", "<table border='1' cellpadding='5' cellspacing='0' width='100%'>" +
                    "<tr style='background-color:#D9EAF7;'>" +
                    "<th>Nombre</th>" +
                    "<th>Edad</th>" +
                    "<th>Departamento</th>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>Juan Pérez</td>" +
                    "<td>30</td>" +
                    "<td>Sistemas</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>María López</td>" +
                    "<td>28</td>" +
                    "<td>Administración</td>" +
                    "</tr>" +
                    "</table>");


            if (cr.getCatUsuarioEntePublico().getPuesto()!=null)
                cartaText=cartaText.replace("##CargoResponsable##",cr.getCatUsuarioEntePublico().getPuesto());
            else
                cartaText=cartaText.replace("##CargoResponsable##","");

            if (cr.getUbicacionResponsable()!=null)
                cartaText=cartaText.replace("##DatosUbicacion##",cr.getUbicacionResponsable());
            else
                cartaText=cartaText.replace("##DatosUbicacion##","");
            cartaText=cartaText.replace("##secretaria##",cr.getEntePublico().getNombreEntePublico());
            cartaText=cartaText.replace("##curp##",cr.getCatUsuarioEntePublico().getCatUsuario().getCurp());

            String cveServicio="";

            List<CartaResponsivaDetalle> cartaResponsivaDetalleList = cartaResponsivaDetalleRepository.findAllByCartaResponsiva(cr);

            for (CartaResponsivaDetalle crd:cartaResponsivaDetalleList){
                if (cveServicio.equals(""))
                    cveServicio=crd.getCveServicio();
                else
                    cveServicio+=", "+crd.getCveServicio();
            }

            cartaText=cartaText.replace("##cveServicio##", cveServicio);

            if (cr.getNotificacionEntregaServicio()!=null)
                cartaText=cartaText.replace("##infoComplementaria##",cr.getNotificacionEntregaServicio());

            /*Fin de Remplazo de datos*/
            cr.setPlantillaReporteText(cartaText);
        }


        return cr;
    }

    @Override
    public List<CartaResponsivaDetalle> getServiciosAdd(CartaResponsiva cartaResponsiva) {

        CartaResponsiva cartaResponsiva1 = cartaResponsivaRepository.findById(cartaResponsiva.getIdCartaResponsiva()).get();

        return cartaResponsivaDetalleRepository.findAllByCartaResponsiva(cartaResponsiva1);
    }

    @Override
    public List<ServiciosTic> getServiciosDisponibles(CartaResponsiva cartaResponsivaParam) {
        CartaResponsiva cartaResponsiva=cartaResponsivaRepository.findById(cartaResponsivaParam.getIdCartaResponsiva()).get();

        if (cartaResponsivaParam.getIdTipoServicio()==null || (cartaResponsivaParam.getCatSubtipoServiciosTic()==null && cartaResponsivaParam.getCatSubtipoServiciosTic().getIdSubtipoServicioTic()==null))
            return new ArrayList<>();
        else
            return serviciosTicRepository.getServiciosDisponibles(cartaResponsiva.getCatUsuarioEntePublico().getIdUsuarioEntePublico(),
                    cartaResponsivaParam.getIdTipoServicio(),
                    cartaResponsivaParam.getCatSubtipoServiciosTic().getIdSubtipoServicioTic());
    }

    @Transactional(value = "saiSigticTransactionManager")
    @Override
    public CartaResponsivaDetalle addServicioCartaResponsiva(CartaResponsivaDetalle cartaResponsivaDetalle) {
        CartaResponsiva cartaResponsiva = cartaResponsivaRepository.findById(cartaResponsivaDetalle.getCartaResponsiva().getIdCartaResponsiva()).get();
        ServiciosTic serviciosTic = serviciosTicRepository.findById(cartaResponsivaDetalle.getServiciosTic().getIdServicioTic()).get();

        //cartaResponsiva= cartaResponsivaRepository.save(cartaResponsiva);

        Integer existe= cartaResponsivaDetalleRepository.countByCartaResponsivaAndServiciosTic(cartaResponsiva,serviciosTic);

        if (existe>0)
            throw new NoSuchElementException("El bien ya se encuentra agregado");

        CatEstatusServicio catEstatusServicio = catEstatusServicioRepository.findById(5).get();

        String curp= cartaResponsiva.getUsuario().getCurp();
        serviciosTic.setCurp(curp);

        CatUsuario catUsuario = catUsuarioRepository.findById(cartaResponsiva.getUsuario().getIdUsuario()).get();
        Long idEnte=cartaResponsiva.getEntePublicoResponsable().getIdEntePublico();
        mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico entePublico = entePublicoSacRepository.findById(idEnte).get();

        serviciosTicRepository.save(serviciosTic);

        Optional<UsuarioServicio> usuarioServicio = usuarioServicioRepository.findByServiciosTicAndCatUsuarioEntePublico(serviciosTic,cartaResponsiva.getCatUsuarioEntePublico());

        if (!usuarioServicio.isPresent())
            throw new NoSuchElementException("El servicio no esta asignado al usuario");


        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CartaResponsivaDetalle cartaResponsivaDetalle1 = new CartaResponsivaDetalle();
        cartaResponsivaDetalle1.setCartaResponsiva(cartaResponsiva);
        cartaResponsivaDetalle1.setServiciosTic(serviciosTic);
        cartaResponsivaDetalle1.setFechaRegistro(new Timestamp(new Date().getTime()));
        cartaResponsivaDetalle1.setCveEstatus("VIG");
        cartaResponsivaDetalle1.setIdUsuarioRegistro(authUser.getIdUsuario());
        cartaResponsivaDetalle1.setIpRegistro(authUser.getIp());
        cartaResponsivaDetalle1.setHostNameRegistro(authUser.getHosName());
        cartaResponsivaDetalle1.setCveServicio(serviciosTic.getCveServicioTic());
        cartaResponsivaDetalle1.setTipoServicio(serviciosTic.getTipoServicio().getNombreTipoServicioTic());
        cartaResponsivaDetalle1.setSubtipoServicio(serviciosTic.getSubTipoServicio().getNombreSubtipoServicioTic());
        cartaResponsivaDetalle1.setNombreServicio(serviciosTic.getNombreServicioTic());

        CartaResponsivaDetalle cartaResponsivaDetalle2= cartaResponsivaDetalleRepository.save(cartaResponsivaDetalle1);

        usuarioServicio.get().setIdCartaRespondivaDetalle(cartaResponsivaDetalle2.getIdCartaResponsivaDetalle());
        usuarioServicio.get().setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        usuarioServicio.get().setCveUsuarioMtto(authUser.getUsername());
        usuarioServicioRepository.save(usuarioServicio.get());

        return cartaResponsivaDetalle2;
    }

    @Transactional(value = "saiSigticTransactionManager")
    @Override
    public JSONObject deleteServicioCartaResponsiva(Long idCartaResponsivaDetalle) {

        Optional<CartaResponsivaDetalle> cartaResponsivaDetalleOptional = cartaResponsivaDetalleRepository.findById(idCartaResponsivaDetalle);

        if (!cartaResponsivaDetalleOptional.isPresent())
            throw new NoSuchElementException("El servicio no esta agregado");

        if (cartaResponsivaDetalleOptional.get().getCartaResponsiva().getAcepto().equals("SI"))
            throw new NoSuchElementException("La carta responsiva ya esta aceptada");

        cartaResponsivaDetalleRepository.delete(cartaResponsivaDetalleOptional.get());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message","Servicio eliminado correctamente");

        return jsonObject;
    }

    @Override
    public ResponseEntity<byte[]> getReporteCr(Long id) throws JRException, IOException {

        List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
        Map<String, Object> parametrosReporte = new HashMap<String, Object>();
        Map<String, Object> parametrosReporte2 = new HashMap<String, Object>();
        CartaResponsiva cartaResponsiva = cartaResponsivaRepository.findById(id).get();
        List<CartaResponsivaDetalle> listaDetalle = cartaResponsivaDetalleRepository.findAllByCartaResponsiva(cartaResponsiva);

        Optional<EntePublicoReporte> entePublicoReporte = entePublicoService.getEntePublicoReporteId(cartaResponsiva.getIdEntePublicoReporte());

        if (!entePublicoReporte.isPresent())
            throw new NoSuchElementException("No existe la configuración para la dependencia "+cartaResponsiva.getEntePublico().getNombreEntePublico());


        List<CartaResponsivaDetalleBean> listaCrDetalle=new ArrayList<>();
        int contador=0;
        for (CartaResponsivaDetalle cr: listaDetalle){
            CartaResponsivaDetalleBean  crd=new CartaResponsivaDetalleBean();
            crd.setIdDetalle(cr.getIdCartaResponsivaDetalle());
            crd.setCveServicio(cr.getServiciosTic().getCveServicioTic());
            crd.setNombreServicio(cr.getServiciosTic().getNombreServicioTic());
            contador++;
            crd.setFila(contador);
            listaCrDetalle.add(crd);
        }

        List<CartaResponsivaBean> lista= new ArrayList<>();

        CartaResponsivaBean cr = new CartaResponsivaBean();
        cr.setIdCartaResponsiva(cartaResponsiva.getIdCartaResponsiva());
        lista.add(cr);

        if (listaDetalle.size()==0)
            throw new NoSuchElementException("No hay servicios agregados");

        if (cartaResponsiva.getEntePublico().getIdImagenEntePublico()!=null && cartaResponsiva.getEntePublico().getIdImagenEntePublico().intValue()>0) {
            URL url = new URL(environment.getProperty("url.sigtic") + "api/logo/exportLogo/" + cartaResponsiva.getEntePublico().getIdImagenEntePublico().intValue());
            parametrosReporte.put("logo", url);
        }

        if (cartaResponsiva.getEntePublicoResponsable().getIdImagenEntePublico()!=null && cartaResponsiva.getEntePublicoResponsable().getIdImagenEntePublico().intValue()>0){
            URL url = new URL(environment.getProperty("url.sigtic") + "api/logo/exportLogo/" + cartaResponsiva.getEntePublicoResponsable().getIdImagenEntePublico().intValue());
            parametrosReporte.put("logo2", url);
        }

        parametrosReporte.put("aceptado", cartaResponsiva.getAcepto());
        parametrosReporte.put("nombre",cartaResponsiva.getUsuario().getNombreCompleto());
        parametrosReporte.put("cargoResponsable",cartaResponsiva.getCargoUsuarioResponsable());

        parametrosReporte.put("preview", "static/include/imagen/preview.png");

        String url_Cr= environment.getProperty("url.socket.io")+cartaResponsiva.getIdCartaResponsiva();
        parametrosReporte.put("qr", url_Cr);

        parametrosReporte.put("preview", "static/include/imagen/preview.png");
        if (cartaResponsiva.getFechaAceptadaText()!=null)
            parametrosReporte.put("fecha","Villahermosa, Tabasco "+cartaResponsiva.getFechaAceptadaText());
        else
            parametrosReporte.put("fecha","Villahermosa, Tabasco xx/xx/xxxx");
        parametrosReporte.put("folio",cartaResponsiva.getFolioCr());
        parametrosReporte.put("asunto","Carta Responsiva");
        parametrosReporte.put("coordinacion",entePublicoReporte.get().getHead1());
        parametrosReporte.put("direccionGeneral", entePublicoReporte.get().getHead2());
        parametrosReporte.put("direccion",entePublicoReporte.get().getHead3());

        parametrosReporte.put("cuerpo",entePublicoReporte.get().getBody1());

        String recibo=entePublicoReporte.get().getBody2();
        if (listaCrDetalle.size()==1)
            recibo=recibo.replace("#XXX1#","el servicio");
         else
        if (listaCrDetalle.size()>1)
            recibo=recibo.replace("#XXX1#","los "+listaCrDetalle.size()+" servicios");

        parametrosReporte.put("recibo",recibo);

        parametrosReporte.put("pie1",entePublicoReporte.get().getPie1());
        parametrosReporte.put("ubicacion",entePublicoReporte.get().getPie2());



        if (cartaResponsiva.getEntePublico().getIdImagenEntePublico()!=null && cartaResponsiva.getEntePublico().getIdImagenEntePublico().intValue()>0) {
            URL url = new URL(environment.getProperty("url.sigtic") + "api/logo/exportLogo/" + cartaResponsiva.getEntePublico().getIdImagenEntePublico().intValue());
            parametrosReporte2.put("logo", url);
        }

        if (cartaResponsiva.getEntePublicoResponsable().getIdImagenEntePublico()!=null && cartaResponsiva.getEntePublicoResponsable().getIdImagenEntePublico().intValue()>0){
            URL url = new URL(environment.getProperty("url.sigtic") + "api/logo/exportLogo/" + cartaResponsiva.getEntePublicoResponsable().getIdImagenEntePublico().intValue());
            parametrosReporte2.put("logo2", url);
        }

        parametrosReporte2.put("aceptado",cartaResponsiva.getAcepto());
        parametrosReporte2.put("preview", "static/include/imagen/preview.png");
        if (cartaResponsiva.getFechaAceptadaText()!=null)
            parametrosReporte2.put("fecha","Villahermosa, Tabasco "+cartaResponsiva.getFechaAceptadaText());
        else
            parametrosReporte2.put("fecha","Villahermosa, Tabasco xx/xx/xxxx");

        parametrosReporte2.put("folio",cartaResponsiva.getFolioCr());
        parametrosReporte2.put("asunto","Carta Responsiva");
        parametrosReporte2.put("pie1",entePublicoReporte.get().getPie1());
        parametrosReporte2.put("ubicacion",entePublicoReporte.get().getPie2());
        parametrosReporte2.put("tipoServicio",cartaResponsiva.getCatSubtipoServiciosTic().getCatTipoServiciosTic().getNombreTipoServicioTic());
        parametrosReporte2.put("subTipoServicio",cartaResponsiva.getCatSubtipoServiciosTic().getNombreSubtipoServicioTic());
        parametrosReporte2.put("qr", url_Cr);

        String nombreReporte="carta_responsiva.jasper";

        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/jasper/"+nombreReporte);
        InputStream in = new FileInputStream(file);

        File file2 = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/jasper/carta_responsiva_anexo.jasper");
        InputStream in2 = new FileInputStream(file2);

        JasperPrint jasperPrintPrincipal = JasperFillManager.fillReport(in,parametrosReporte, new JRBeanCollectionDataSource(lista));
        jasperPrintList.add(jasperPrintPrincipal);

        JasperPrint jasperPrintAnexo = JasperFillManager.fillReport(in2,parametrosReporte2, new JRBeanCollectionDataSource(listaCrDetalle));
        jasperPrintList.add(jasperPrintAnexo);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        exporter.setConfiguration(configuration);
        exporter.exportReport();

        return UtileriaReporte.jasperCreatePdf(baos.toByteArray(),"carta_responsiva.pdf");
    }

    @Override
    public Integer countDetalleCr(CartaResponsiva cartaResponsiva) {

        return cartaResponsivaDetalleRepository.countByCartaResponsiva(cartaResponsiva);
    }

    @Transactional(value = "saiSigticTransactionManager")
    @Override
    public CartaResponsiva aceptarCartaResponsiva(CartaResponsiva cartaResponsiva) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CartaResponsiva cartaResponsiva1 = cartaResponsivaRepository.findById(cartaResponsiva.getIdCartaResponsiva()).get();

        if (cartaResponsiva1.getCatSubtipoServiciosTic()==null)
            throw new NoSuchElementException("La carta responsiva no tiene un subtipo de servicio asignado");

        if (cartaResponsiva1.getAcepto().equals("SI"))
            throw new NoSuchElementException("La carta responsiva ya esta aceptada");

        CatEstatusCartaResponsiva catEstatusCartaResponsiva =catEstatusCartaResponsivaRepository.findById(9).get();

        cartaResponsiva1.setAcepto("SI");
        cartaResponsiva1.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        cartaResponsiva1.setFechaAcepto(new Timestamp(new Date().getTime()));
        cartaResponsiva1.setIpAcepto(authUser.getIp());
        cartaResponsiva1.setHostAcepto(authUser.getHosName());
        cartaResponsiva1.setEstatusCartaResponsiva(catEstatusCartaResponsiva);

        bitacoraMovimientoService.guardarBitacoraMovimiento(cartaResponsiva1.getIdCartaResponsiva(),"CARTA_RESPONSIVA","Carta Responsiva aceptada");

        return cartaResponsivaRepository.save(cartaResponsiva1);
    }



    @Transactional(value = "saiSigticTransactionManager")
    @Override
    public CartaResponsiva aceptarCartaResponsivaUser(CartaResponsiva cartaResponsiva) {
        CartaResponsiva cartaResponsiva1 = cartaResponsivaRepository.findById(cartaResponsiva.getIdCartaResponsiva()).get();

        if (cartaResponsiva1.getCatSubtipoServiciosTic()==null)
            throw new NoSuchElementException("La carta responsiva no tiene un subtipo de servicio asignado");

        if (cartaResponsiva1.getAcepto().equals("SI"))
            throw new NoSuchElementException("La carta responsiva ya esta aceptada");

        String pwdTemporal="Temporal"+cartaResponsiva1.getUsuario().getIdUsuario()+"#";

        mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico catUsuarioEntePublico=catUsuarioEntePublicoService.getEntesPublicoVigentePorIdEnte(cartaResponsiva1.getUsuario().getIdUsuario(), cartaResponsiva1.getEntePublicoResponsable().getIdEntePublico());
        Optional<SegUsuario> segUsuario =segUsuarioService.getUsuarioOptional(cartaResponsiva1.getUsuario().getIdUsuario());
        if (!segUsuario.isPresent()) {
            SegUsuario segUsuarioNew= new SegUsuario();
            SegUsuarioPk segUsuarioPk= new SegUsuarioPk();
            segUsuarioPk.setUsuario(cartaResponsiva1.getUsuario().getCurp());
            segUsuarioPk.setSistema("SIGTIC");
            segUsuarioNew.setSegUsuarioPk(segUsuarioPk);
            segUsuarioNew.setBloqueado(0);
            segUsuarioNew.setContraseniaReset(0);
            segUsuarioNew.setContrasenia(bCryptPasswordEncoder.encode(pwdTemporal));
            segUsuarioNew.setCveEstatus("VIG");
            segUsuarioNew.setCatUsuarioEntePublico(catUsuarioEntePublico);
            segUsuarioNew.setIdUsuario(cartaResponsiva1.getUsuario().getIdUsuario());
            segUsuarioNew=segUsuarioService.saveUsuarioTemporal(segUsuarioNew);

            segUsuarioNew.setIdPerfil(557);
            SegPerfilUsuario segPerfilUsuario= segPerfilUsuarioService.asignarRolUsuarioTemporal(segUsuarioNew);

            cartaResponsiva1.setPwdTemporal(pwdTemporal);
        }else
        if (segUsuario.isPresent()) {
            if (!segUsuario.get().getCveEstatus().equals("VIG")){
                segUsuario.get().setCveEstatus("VIG");
                segUsuario.get().setBloqueado(0);
                segUsuario.get().setContraseniaReset(0);
                segUsuario.get().setContrasenia(bCryptPasswordEncoder.encode(pwdTemporal));
                segUsuarioService.saveUsuarioTemporal(segUsuario.get());
                cartaResponsiva1.setPwdTemporal(pwdTemporal);
            }
        }

        cartaResponsiva1.setAcepto("SI");
        cartaResponsiva1.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        cartaResponsiva1.setFechaAcepto(new Timestamp(new Date().getTime()));
        cartaResponsiva1.setIpAcepto("0.0.0.0");
        cartaResponsiva1.setHostAcepto(cartaResponsiva1.getUsuario().getCurp());

        return cartaResponsivaRepository.save(cartaResponsiva1);
    }

    @Transactional(value = "saiSigticTransactionManager")
    @Override
    public CartaResponsivaSeguimiento actualizarCartaResponsiva(CartaResponsiva cartaResponsiva) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CartaResponsiva cartaResponsiva1 = cartaResponsivaRepository.findById(cartaResponsiva.getIdCartaResponsiva()).get();

        if (cartaResponsiva1.getCatUsuarioEntePublico().getPuesto()==null){
            throw new NoSuchElementException("El responsable no cuenta con cargo. Actualizar cargo para continuar");
        }


        Usuario usuarioMtto= usuarioRepository.findById(authUser.getIdUsuario()).get();
        CatEstatusCartaResponsiva catEstatusCartaResponsiva = catEstatusCartaResponsivaRepository.findById(cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva()).get();

        cartaResponsiva1.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        cartaResponsiva1.setUsuarioMtto(usuarioMtto);
        cartaResponsiva1.setEstatusCartaResponsiva(catEstatusCartaResponsiva);


        CartaResponsivaSeguimiento cartaResponsivaSeguimiento = new CartaResponsivaSeguimiento();
        cartaResponsivaSeguimiento.setEstatusCartaResponsiva(catEstatusCartaResponsiva);
        cartaResponsivaSeguimiento.setFechaRegistro(new Timestamp(new Date().getTime()));
        if (catEstatusCartaResponsiva.getIdEstatusCartaResponsiva().intValue()==8)
            cartaResponsivaSeguimiento.setNota("Se envia para ser aceptada por el responsable del servicio o servicios");
        else
            cartaResponsivaSeguimiento.setNota(cartaResponsiva.getNota());
        cartaResponsivaSeguimiento.setIpRegistro(authUser.getIp());
        cartaResponsivaSeguimiento.setIdUsuarioRegistro(authUser.getIdUsuario());

        if (catEstatusCartaResponsiva.getIdEstatusCartaResponsiva().intValue()==3){
            cartaResponsiva1.setAcepto("NO");
            cartaResponsiva1.setIpAcepto(null);
            cartaResponsiva1.setHostAcepto(null);
            cartaResponsiva1.setFechaAcepto(null);
            cartaResponsiva1.setIdUserLoadFile(null);
            cartaResponsiva1.setFileExtension(null);
            cartaResponsiva1.setFileNameOriginal(null);
            cartaResponsiva1.setFileRoute(null);
        }

        if (catEstatusCartaResponsiva.getIdEstatusCartaResponsiva().intValue()==6){

            List<CartaResponsivaDetalle> cartaResponsivaDetalleList= cartaResponsivaDetalleRepository.findAllByCartaResponsiva(cartaResponsiva1);

            for (CartaResponsivaDetalle crd:cartaResponsivaDetalleList){
                List<CartaResponsivaDetalle> cartaResponsivaDetalleListSearch= cartaResponsivaDetalleRepository.findAllByServiciosTicAndCveEstatus(crd.getServiciosTic(),"VIG");

                for (CartaResponsivaDetalle crd2:cartaResponsivaDetalleListSearch){
                    if (crd2.getCartaResponsiva().getIdCartaResponsiva().longValue()!=cartaResponsiva1.getIdCartaResponsiva().longValue() && crd2.getCartaResponsiva().getEstatusCartaResponsiva().getIdEstatusCartaResponsiva().intValue()>=7 && crd2.getCartaResponsiva().getCveEstatus().equals("VIG")){
                        crd2.setCveEstatus("HIS");

                        int hayVigentes=cartaResponsivaDetalleRepository.countByCartaResponsivaAndCveEstatus(crd2.getCartaResponsiva(),"VIG");

                        if (hayVigentes==0){
                            crd2.getCartaResponsiva().setCveEstatus("HIS");
                            crd2.getCartaResponsiva().setFechaUltimoCambio(new Timestamp(new Date().getTime()));
                            crd2.getCartaResponsiva().setUsuarioMtto(usuarioMtto);
                        }

                    }
                }
                cartaResponsivaDetalleRepository.saveAll(cartaResponsivaDetalleListSearch);
            }
        }

        cartaResponsiva1=cartaResponsivaRepository.save(cartaResponsiva1);

        bitacoraMovimientoService.guardarBitacoraMovimiento(cartaResponsiva1.getIdCartaResponsiva(), "CARTA_RESPONSIVA", "Se enviar para ser aceptada por el responsable del servicio o servicios");
        cartaResponsivaSeguimiento.setCartaResponsiva(cartaResponsiva1);



        return cartaResponsivaSeguimientoRepository.save(cartaResponsivaSeguimiento);
    }

    @Override
    public Mensage uploadCartaResponsiva(FileUploadBean fileUploadBean) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CartaResponsiva cartaResponsiva = cartaResponsivaRepository.findById(fileUploadBean.getId()).get();

        if (cartaResponsiva.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva().intValue()!=9)
            throw new RuntimeException("Error: No se pueden adjuntar el archivo");

        Date date = new Date();

        CatEstatusCartaResponsiva estatusCrAceptaFirmada = catEstatusCartaResponsivaRepository.findById(11).get();
        Usuario usuarioMtto= usuarioRepository.findById(authUser.getIdUsuario()).get();

        cartaResponsiva.setFileExtension(Utileria.getFileExtension(fileUploadBean.getNombre()));
        cartaResponsiva.setFileNameOriginal(fileUploadBean.getNombre());
        cartaResponsiva.setIdUserLoadFile(authUser.getIdUsuario());
        cartaResponsiva.setEstatusCartaResponsiva(estatusCrAceptaFirmada);
        cartaResponsiva.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        cartaResponsiva.setUsuarioMtto(usuarioMtto);

        String nombreArchivo = date.getTime() + "-" + fileUploadBean.getId() + "." + cartaResponsiva.getFileExtension();

        cartaResponsiva.setFileName(nombreArchivo);

        String path="";
        try {

            File folders = new File("/archivos_sigtic/cartas_responsivas/"+cartaResponsiva.getAnio());

            if (!folders.exists())
                folders.mkdirs();

            path = folders.getPath();
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Error: al crear la carpeta "+e);
        }

        cartaResponsiva.setFileRoute(path.replace("\\","/") + "/" + nombreArchivo);

        try {
            Utileria.writeFile(Base64.getDecoder().decode(fileUploadBean.getContenido()), nombreArchivo, path);
        }catch (IOException e){
            throw new RuntimeException("Error: No se pueden adjuntar el archivo");
        }

        Mensage resp = new Mensage();

        bitacoraMovimientoService.guardarBitacoraMovimiento(cartaResponsiva.getIdCartaResponsiva(), "CARTA_RESPONSIVA", "Se sube carta responsiva firmada ");

        cartaResponsivaRepository.save(cartaResponsiva);
        resp.setMessage("Archivo adjuntado correctamente");
        return resp;
    }



    @Override
    public void exportCartaResponsiva(HttpServletResponse response, Long id) {

        try {
            CartaResponsiva cartaResponsiva = cartaResponsivaRepository.findById(id).get();
            File file = ResourceUtils.getFile(cartaResponsiva.getFileRoute());
            InputStream in = new FileInputStream(file);
            response.setContentType("multipart/form-data");
            response.setHeader("Content-disposition", "attachment;filename=" + cartaResponsiva.getFileNameOriginal());
            response.setHeader("Pragma", "No-cache");
            response.setCharacterEncoding("utf-8");
            ServletOutputStream out = response.getOutputStream();
            int bytes;
            while ((bytes = in.read()) != -1) {
                out.write(bytes);
            }
            out.flush();
            out.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //@Transactional(value = "saiSigticTransactionManager")
    @Override
    public CartaResponsiva usuarioAutorizaCartaResponsiva(CartaResponsiva cartaResponsiva) {

        CartaResponsiva cartaResponsiva1 = cartaResponsivaRepository.findById(cartaResponsiva.getIdCartaResponsiva()).get();

        if (cartaResponsiva1.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva()!=1 && cartaResponsiva1.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva()!=3)
            throw new NoSuchElementException("La carta responsiva ya no esta en captura, y tampoco esta en estatus de observada");

        return cartaResponsivaRepository.save(cartaResponsiva1);
    }

    @Override
    public List<CartaResponsivaSeguimiento> getListaSeguimientoCartaResponsiva(CartaResponsiva cartaResponsiva) {
        CartaResponsiva cartaResponsiva1 = cartaResponsivaRepository.findById(cartaResponsiva.getIdCartaResponsiva()).get();
        return cartaResponsivaSeguimientoRepository.findAllByCartaResponsivaOrderByIdCartaResponsivaSeguimientoDesc(cartaResponsiva1);
    }

    @Override
    public CartaResponsiva generarToken(Long idCartaResponsiva) {
        CartaResponsiva cartaResponsiva = cartaResponsivaRepository.findById(idCartaResponsiva).get();

        if (cartaResponsiva.getCorreoInstitucional()==null && cartaResponsiva.getCorreoPersonal()==null)
            throw new NoSuchElementException("El token no se puede generar ya que el usuario no cuenta con correo institucional");

         String token= TokenUtils.createTokenCr(cartaResponsiva.getIdCartaResponsiva());
         cartaResponsiva.setTokenUrl(token);
         return cartaResponsivaRepository.save(cartaResponsiva);
    }


    @Transactional(value = "saiSigticTransactionManager")
    @Override
    public CartaResponsiva aceptarCrId(Long idCartaResponsiva) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CartaResponsiva cr= cartaResponsivaRepository.findById(idCartaResponsiva).get();

        if (cr.getIdPlantillaReporte()==null){
            throw new NoSuchElementException("No existe plantilla disponible para está carta responsiva");
        }

        Optional<CatUsuarioEntePublico> catUsuarioEntePublicoOp = catUsuarioEntePublicoRepository.findById(authUser.getCatUsuarioEntePublico().getIdUsuarioEntePublico());

        if (!catUsuarioEntePublicoOp.isPresent()){
            throw new NoSuchElementException("El usuario no esta asignado a un ente público");
        }

        if (catUsuarioEntePublicoOp.get().getPuesto()==null){
            throw new NoSuchElementException("Actualice su cargo en la opción del menú de Mis datos");
        }

        CatPlantillaReporte plantillaReporte = catPlantillaReporteRepository.findById(cr.getIdPlantillaReporte()).get();
        List<CatPlantillaReporteParrafo> catPlantillaReporteParrafoList = catPlantillaReporteParrafoRepository.findAllByIdPlantillaReporteAndCveEstatusOrderByNumeroOrdenAsc(cr.getIdPlantillaReporte(),"VIG");

        CatEstatusCartaResponsiva estatusCrAceptar = catEstatusCartaResponsivaRepository.findById(9).get();

        cr.setAcepto("SI");
        cr.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        cr.setFechaAcepto(new Timestamp(new Date().getTime()));
        cr.setFechaAceptadaFija("Villahermosa, Tabasco a "+cr.getFechaAceptadaText());
        cr.setIpAcepto(authUser.getIp());
        cr.setHostAcepto(authUser.getHosName());
        cr.setEstatusCartaResponsiva(estatusCrAceptar);
        cr.setNombreReporte(plantillaReporte.getNombreReporte());
        cr.setTituloReporte(plantillaReporte.getTituloReporte());
        cr.setFirma1(authUser.getCatUsuarioEntePublico().getCatUsuario().getNombreCompleto());
        cr.setCargo1(authUser.getCatUsuarioEntePublico().getPuesto());
        cr.setDescripcionFirma1(plantillaReporte.getDecFirma1());
        cr.setFirma2(plantillaReporte.getFirma2());
        cr.setCargo2(plantillaReporte.getCargo2());
        cr.setDescripcionFirma2(plantillaReporte.getDecFirma2());
        cr.setFirma3(plantillaReporte.getFirma3());
        cr.setCargo3(plantillaReporte.getCargo3());
        cr.setDescripcionFirma3(plantillaReporte.getDecFirma3());
        cr.setCurpAcepto(authUser.getCatUsuarioEntePublico().getCatUsuario().getCurp());
        cr.setNombreCompletoAcepto(authUser.getCatUsuarioEntePublico().getCatUsuario().getNombreCompleto());

        cr.setCurpEntrega(plantillaReporte.getCurpEntrega());
        if (plantillaReporte.getCatUsuarioEntePublicoEntrega()!=null)
            cr.setNombreCompletoEntrega(plantillaReporte.getCatUsuarioEntePublicoEntrega().getCatUsuario().getNombreCompleto());

        cr.setCurpAutoriza(plantillaReporte.getCurpAutoriza());
        if (plantillaReporte.getCatUsuarioEntePublicoAutoriza()!=null)
            cr.setNombreCompletoAutoriza(plantillaReporte.getCatUsuarioEntePublicoAutoriza().getCatUsuario().getNombreCompleto());

        if (plantillaReporte.getLogo()!=null){
            cr.setIdLogo(plantillaReporte.getLogo().getIdLogo().longValue());
            cr.setNombreLogo(plantillaReporte.getLogo().getNombreLogo());
            cr.setRutaLogo(plantillaReporte.getLogo().getRutaArchivo());
        }

        List<CartaResponsivaParrafo> listaParrafo= new ArrayList<>();
        for (CatPlantillaReporteParrafo cp:catPlantillaReporteParrafoList){
            CartaResponsivaParrafo parrafoCr = new CartaResponsivaParrafo();
            parrafoCr.setIdCartaResponsiva(cr.getIdCartaResponsiva());
            parrafoCr.setOrden(cp.getNumeroOrden());
            parrafoCr.setParrafo(cp.getParrafo());

            String parrafo = "";

            if (cp.getParrafo().trim().startsWith("[[")) {
                parrafo = cp.getParrafo().trim();
            }
            else if (cp.getParrafo().contains("##infoComplementaria##")) {
                if (cr.getNotificacionEntregaServicio() != null)
                    parrafo += "<p>" + cp.getParrafo() + "</p>";
            }
            else
                parrafo+="<p>"+cp.getParrafo()+"</p>";

            parrafo=parrafo.replace("##NombreResponsable##",cr.getUsuario().getNombreCompleto());


            if (cr.getCatUsuarioEntePublico().getPuesto()!=null)
                parrafo=parrafo.replace("##CargoResponsable##",cr.getCatUsuarioEntePublico().getPuesto());
            else
                parrafo=parrafo.replace("##CargoResponsable##","");

            if (cr.getUbicacionResponsable()!=null)
                parrafo=parrafo.replace("##DatosUbicacion##",cr.getUbicacionResponsable());
            else
                parrafo=parrafo.replace("##DatosUbicacion##","");
            parrafo=parrafo.replace("##secretaria##",cr.getEntePublico().getNombreEntePublico());
            parrafo=parrafo.replace("##curp##",cr.getCatUsuarioEntePublico().getCatUsuario().getCurp());

            String cveServicio="";

            List<CartaResponsivaDetalle> cartaResponsivaDetalleList = cartaResponsivaDetalleRepository.findAllByCartaResponsiva(cr);

            for (CartaResponsivaDetalle crd:cartaResponsivaDetalleList){
                if (cveServicio.equals(""))
                    cveServicio=crd.getCveServicio();
                else
                    cveServicio+=", "+crd.getCveServicio();
            }

            parrafo=parrafo.replace("##cveServicio##",cveServicio);


            if (cr.getNotificacionEntregaServicio()!=null)
                parrafo=parrafo.replace("##infoComplementaria##",cr.getNotificacionEntregaServicio());

            parrafoCr.setParrafo(parrafo);
            parrafoCr.setFechaRegistro(new Timestamp(new Date().getTime()));
            parrafoCr.setCurpAcepto(cr.getCurpAcepto());
            parrafoCr.setFechaAcepto(cr.getFechaAcepto());
            parrafoCr.setIpAcepto(cr.getIpAcepto());
            parrafoCr.setHostAcepto(cr.getHostAcepto());
            parrafoCr.setCveEstatus("VIG");

            if (!parrafo.equals(""))
                listaParrafo.add(parrafoCr);
        }

        cartaResponsivaParrafoRepository.saveAll(listaParrafo);

        bitacoraMovimientoService.guardarBitacoraMovimiento(cr.getIdCartaResponsiva(), "CARTA_RESPONSIVA", "Se acepta la carta responsiva");

        return cartaResponsivaRepository.save(cr);
    }


    @Override
    public CartaResponsiva rechazarCrId(Long idCartaResponsiva) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CartaResponsiva cr= cartaResponsivaRepository.findById(idCartaResponsiva).get();

        if (cr.getIdPlantillaReporte()==null){
            throw new NoSuchElementException("No existe plantilla disponible para está carta responsiva");
        }

        CatEstatusCartaResponsiva estatusCrRechazar = catEstatusCartaResponsivaRepository.findById(10).get();
        cr.setAcepto("NO");
        cr.setEstatusCartaResponsiva(estatusCrRechazar);
        cr.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        cr.setFechaRechazo(new Timestamp(new Date().getTime()));
        cr.setNotaRechazo(cr.getNotaRechazo());
        bitacoraMovimientoService.guardarBitacoraMovimiento(cr.getIdCartaResponsiva(), "CARTA_RESPONSIVA", "Se rechaza la carta responsiva");

        return cartaResponsivaRepository.save(cr);
    }

    @Transactional(value = "saiSigticTransactionManager")
    @Override
    public CartaResponsiva aceptarCrFreeId(Long idCartaResponsiva) {
        CartaResponsiva cr= cartaResponsivaRepository.findById(idCartaResponsiva).get();

        CatUsuarioEntePublico catUsuarioEntePublico = catUsuarioEntePublicoRepository.findById(cr.getCatUsuarioEntePublico().getIdUsuarioEntePublico().longValue()).get();

        if (cr.getIdPlantillaReporte()==null){
            throw new NoSuchElementException("No existe plantilla disponible para está carta responsiva");
        }

        Optional<CatUsuarioEntePublico> catUsuarioEntePublicoOp = catUsuarioEntePublicoRepository.findById(cr.getCatUsuarioEntePublico().getIdUsuarioEntePublico().longValue());

        if (!catUsuarioEntePublicoOp.isPresent()){
            throw new NoSuchElementException("El usuario no esta asignado a un ente público");
        }

        if (catUsuarioEntePublicoOp.get().getPuesto()==null){
            throw new NoSuchElementException("Actualice su cargo en la opción del menú de Mis datos");
        }

        CatPlantillaReporte plantillaReporte = catPlantillaReporteRepository.findById(cr.getIdPlantillaReporte()).get();
        List<CatPlantillaReporteParrafo> catPlantillaReporteParrafoList = catPlantillaReporteParrafoRepository.findAllByIdPlantillaReporteAndCveEstatusOrderByNumeroOrdenAsc(cr.getIdPlantillaReporte(),"VIG");

        CatEstatusCartaResponsiva estatusCrAceptar = catEstatusCartaResponsivaRepository.findById(9).get();

        cr.setAcepto("SI");
        cr.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        cr.setFechaAcepto(new Timestamp(new Date().getTime()));
        cr.setFechaAceptadaFija("Villahermosa, Tabasco a "+cr.getFechaAceptadaText());
        cr.setIpAcepto(requestService.getClientIp(httpServletRequest));
        try {
            cr.setHostAcepto(requestService.getClientHost(httpServletRequest));
        } catch (UnknownHostException e) {
            throw new NoSuchElementException("Error al obtener el host");
        }

        cr.setEstatusCartaResponsiva(estatusCrAceptar);
        cr.setNombreReporte(plantillaReporte.getNombreReporte());
        cr.setTituloReporte(plantillaReporte.getTituloReporte());
        cr.setFirma1(catUsuarioEntePublico.getCatUsuario().getNombreCompleto());

        if (catUsuarioEntePublico.getPuesto()!=null)
            cr.setCargo1(catUsuarioEntePublico.getPuesto());

        cr.setDescripcionFirma1(plantillaReporte.getDecFirma1());
        cr.setFirma2(plantillaReporte.getFirma2());
        cr.setCargo2(plantillaReporte.getCargo2());
        cr.setDescripcionFirma2(plantillaReporte.getDecFirma2());
        cr.setFirma3(plantillaReporte.getFirma3());
        cr.setCargo3(plantillaReporte.getCargo3());
        cr.setDescripcionFirma3(plantillaReporte.getDecFirma3());
        cr.setCurpAcepto(catUsuarioEntePublico.getCatUsuario().getCurp());
        cr.setNombreCompletoAcepto(catUsuarioEntePublico.getCatUsuario().getNombreCompleto());

        cr.setCurpEntrega(plantillaReporte.getCurpEntrega());
        if (plantillaReporte.getCatUsuarioEntePublicoEntrega()!=null)
            cr.setNombreCompletoEntrega(plantillaReporte.getCatUsuarioEntePublicoEntrega().getCatUsuario().getNombreCompleto());

        cr.setCurpAutoriza(plantillaReporte.getCurpAutoriza());
        if (plantillaReporte.getCatUsuarioEntePublicoAutoriza()!=null)
            cr.setNombreCompletoAutoriza(plantillaReporte.getCatUsuarioEntePublicoAutoriza().getCatUsuario().getNombreCompleto());

        if (plantillaReporte.getLogo()!=null){
            cr.setIdLogo(plantillaReporte.getLogo().getIdLogo().longValue());
            cr.setNombreLogo(plantillaReporte.getLogo().getNombreLogo());
            cr.setRutaLogo(plantillaReporte.getLogo().getRutaArchivo());
        }

        List<CartaResponsivaParrafo> listaParrafo= new ArrayList<>();
        for (CatPlantillaReporteParrafo cp:catPlantillaReporteParrafoList){
            CartaResponsivaParrafo parrafoCr = new CartaResponsivaParrafo();
            parrafoCr.setIdCartaResponsiva(cr.getIdCartaResponsiva());
            parrafoCr.setOrden(cp.getNumeroOrden());
            parrafoCr.setParrafo(cp.getParrafo());

            String parrafo = "";

            if (cp.getParrafo().trim().startsWith("[[")) {
                parrafo = cp.getParrafo().trim();
            }
            else if (cp.getParrafo().contains("##infoComplementaria##")) {
                if (cr.getNotificacionEntregaServicio() != null)
                    parrafo += "<p>" + cp.getParrafo() + "</p>";
            }
            else
                parrafo+="<p>"+cp.getParrafo()+"</p>";

            parrafo=parrafo.replace("##NombreResponsable##",cr.getUsuario().getNombreCompleto());


            if (cr.getCatUsuarioEntePublico().getPuesto()!=null)
                parrafo=parrafo.replace("##CargoResponsable##",cr.getCatUsuarioEntePublico().getPuesto());
            else
                parrafo=parrafo.replace("##CargoResponsable##","");

            if (cr.getUbicacionResponsable()!=null)
                parrafo=parrafo.replace("##DatosUbicacion##",cr.getUbicacionResponsable());
            else
                parrafo=parrafo.replace("##DatosUbicacion##","");
            parrafo=parrafo.replace("##secretaria##",cr.getEntePublico().getNombreEntePublico());
            parrafo=parrafo.replace("##curp##",cr.getCatUsuarioEntePublico().getCatUsuario().getCurp());

            String cveServicio="";

            List<CartaResponsivaDetalle> cartaResponsivaDetalleList = cartaResponsivaDetalleRepository.findAllByCartaResponsiva(cr);

            for (CartaResponsivaDetalle crd:cartaResponsivaDetalleList){
                if (cveServicio.equals(""))
                    cveServicio=crd.getCveServicio();
                else
                    cveServicio+=", "+crd.getCveServicio();
            }

            parrafo=parrafo.replace("##cveServicio##",cveServicio);

            if (cr.getNotificacionEntregaServicio()!=null)
                parrafo=parrafo.replace("##infoComplementaria##",cr.getNotificacionEntregaServicio());

            parrafoCr.setParrafo(parrafo);
            parrafoCr.setFechaRegistro(new Timestamp(new Date().getTime()));
            parrafoCr.setCurpAcepto(cr.getCurpAcepto());
            parrafoCr.setFechaAcepto(cr.getFechaAcepto());
            parrafoCr.setIpAcepto(cr.getIpAcepto());
            parrafoCr.setHostAcepto(cr.getHostAcepto());
            parrafoCr.setCveEstatus("VIG");

            listaParrafo.add(parrafoCr);
        }

        cartaResponsivaParrafoRepository.saveAll(listaParrafo);

        bitacoraMovimientoService.guardarBitacoraMovimiento(cr.getIdCartaResponsiva(), "CARTA_RESPONSIVA", "Se acepta la carta responsiva",catUsuarioEntePublico.getIdUsuarioEntePublico());

        return cartaResponsivaRepository.save(cr);
    }


    @Override
    public CartaResponsiva rechazarCrFreeId(Long idCartaResponsiva) {
        CartaResponsiva cr= cartaResponsivaRepository.findById(idCartaResponsiva).get();

        CatUsuarioEntePublico catUsuarioEntePublico = catUsuarioEntePublicoRepository.findById(cr.getCatUsuarioEntePublico().getIdUsuarioEntePublico().longValue()).get();

        if (cr.getIdPlantillaReporte()==null){
            throw new NoSuchElementException("No existe plantilla disponible para está carta responsiva");
        }

        CatEstatusCartaResponsiva estatusCrRechazar = catEstatusCartaResponsivaRepository.findById(10).get();
        cr.setAcepto("NO");
        cr.setEstatusCartaResponsiva(estatusCrRechazar);
        cr.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        cr.setFechaRechazo(new Timestamp(new Date().getTime()));
        cr.setNotaRechazo(cr.getNotaRechazo());
        bitacoraMovimientoService.guardarBitacoraMovimiento(cr.getIdCartaResponsiva(), "CARTA_RESPONSIVA", "Se rechaza la carta responsiva",catUsuarioEntePublico.getIdUsuarioEntePublico());

        return cartaResponsivaRepository.save(cr);
    }


    @Override
    public CartaResponsiva cartaResponsivaAceptada(Long idCartaResponsiva) {
        CartaResponsiva cr= cartaResponsivaRepository.findById(idCartaResponsiva).get();

        if (cr.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva()== 9 || cr.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva()== 11){

            List<CartaResponsivaParrafo> listaParrafo = cartaResponsivaParrafoRepository.findAllByIdCartaResponsivaOrderByOrden(cr.getIdCartaResponsiva());

            String cartaText="";
            for(CartaResponsivaParrafo cp: listaParrafo){
                cartaText+=cp.getParrafo();
            }


            if (cr.getFirma1()!=null) {
                cartaText += "<br/><br/>";
                cartaText += "<div align='center'>" + cr.getFirma1() + "</div>";
                if (cr.getCargo1()!=null)
                    cartaText += "<div align='center'>" + cr.getCargo1() + "</div>";
                else
                    cartaText += "<div align='center'></div>";
                cartaText += "<div align='center'><b>" + cr.getDescripcionFirma1() + "</b></div>";
            }

            if (cr.getFirma2()!=null) {
                cartaText += "<br/><br/>";
                cartaText += "<div align='center'>" + cr.getFirma2() + "</div>";
                if (cr.getCargo2()!=null)
                    cartaText += "<div align='center'>" + cr.getCargo2() + "</div>";
                else
                    cartaText += "<div align='center'></div>";
                cartaText += "<div align='center'><b>" + cr.getDescripcionFirma2() + "</b></div>";
            }

            if (cr.getFirma3()!=null) {
                cartaText += "<br/><br/>";
                cartaText += "<div align='center'>" + cr.getFirma3() + "</div>";

                if (cr.getCargo3()!=null)
                    cartaText += "<div align='center'>" + cr.getCargo3() + "</div>";
                else
                    cartaText += "<div align='center'></div>";

                cartaText += "<div align='center'><b>" + cr.getDescripcionFirma3() + "</b></div>";
            }

            cartaText+="<br/><br/>";
            cartaText+="<b>Ip Acepto:</b> "+cr.getIpAcepto()+"<br/>";
            cartaText+="<b>Host Acepto:</b> "+cr.getHostAcepto()+"<br/>";
            cartaText+="<b>Fecha Acepto:</b> "+cr.getFechaAceptoText()+"<br/>";

            cartaText=cartaText.replace("##generarTabla##", "<table border='1' cellpadding='5' cellspacing='0' width='100%'>" +
                    "<tr style='background-color:#D9EAF7;'>" +
                    "<th>Nombre</th>" +
                    "<th>Edad</th>" +
                    "<th>Departamento</th>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>Juan Pérez</td>" +
                    "<td>30</td>" +
                    "<td>Sistemas</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>María López</td>" +
                    "<td>28</td>" +
                    "<td>Administración</td>" +
                    "</tr>" +
                    "</table>");

            cr.setPlantillaReporteText(cartaText);
        }else
            throw new NoSuchElementException("No se ha aceptado la carta responsiva");

        return cr;
    }

    private String limpiar(String s) {
        return s == null ? "" : s.replace("|", "-").replace("\r", " ").replace("\n", " ").trim();
    }

    private String expandirMarcador(String parrafo, CartaResponsiva cr, List<CartaResponsivaDetalle> detalle) {
        String t = parrafo.trim().replaceAll("(?is)^<p>\\s*", "").replaceAll("(?is)\\s*</p>$", "");
        if (t.startsWith("[[TABLA]]") || t.startsWith("[[FIRMAS]]") || t.startsWith("[[EFIRMA]]"))
            return t;
        return parrafo;
    }

    @Override
    public ResponseEntity<byte[]> getReporteCrAceptada(Long idCartaResponsiva) throws FileNotFoundException, JRException, MalformedURLException {

        CartaResponsiva cr= cartaResponsivaRepository.findById(idCartaResponsiva).get();
        List<CartaResponsivaDetalle> listaDetalle = cartaResponsivaDetalleRepository.findAllByCartaResponsivaOrderByNombreServicio(cr);

        List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
        Map<String, Object> parametrosReporte = new HashMap<String, Object>();
        Map<String, Object> parametrosReporte2 = new HashMap<String, Object>();

        if (cr.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva()== 9 || cr.getEstatusCartaResponsiva().getIdEstatusCartaResponsiva()== 11){

            parametrosReporte.put("nombre_reporte", cr.getNombreReporte());
            parametrosReporte.put("titulo",cr.getTituloReporte());
            parametrosReporte.put("fecha",cr.getFechaAceptadaFija());
            parametrosReporte.put("folio","<b>Folio: </b>"+cr.getFolioCr());
            parametrosReporte.put("ip_acepto","<b>IP Acepto: </b>"+cr.getIpAcepto());
            if (cr.getHostAcepto()!=null)
                parametrosReporte.put("host_acepto","<b>Host Acepto: </b>"+cr.getHostAcepto());
            else
                parametrosReporte.put("host_acepto","<b>Host Acepto: </b>");

            parametrosReporte.put("fecha_acepto","<b>Fecha Acepto: </b>"+cr.getFechaAceptoText());
            parametrosReporte.put("aceptado",cr.getAcepto());
            parametrosReporte.put("preview", "static/include/imagen/preview.png");


            int numFirmas=0;

            if (!cr.getFirma1().isEmpty()) {
                parametrosReporte.put("firma1", cr.getFirma1());
                parametrosReporte.put("cargo1", cr.getCargo1());
                parametrosReporte.put("descripcion_firma1", cr.getDescripcionFirma1());
                numFirmas++;
            }

            if (!cr.getFirma2().isEmpty()){
                parametrosReporte.put("firma2",cr.getFirma2());
                parametrosReporte.put("cargo2",cr.getCargo2());
                parametrosReporte.put("descripcion_firma2",cr.getDescripcionFirma2());
                numFirmas++;
            }

            if (!cr.getFirma3().isEmpty()) {
                parametrosReporte.put("firma3", cr.getFirma3());
                parametrosReporte.put("cargo3", cr.getCargo3());
                parametrosReporte.put("descripcion_firma3", cr.getDescripcionFirma3());
                numFirmas++;
            }

            parametrosReporte.put("num_firma",numFirmas);

            String url_Cr= environment.getProperty("url.socket.io")+cr.getIdCartaResponsiva();
            parametrosReporte.put("qr", url_Cr);

            String nombreReporte="carta_responsiva_aceptada.jasper";


            List<CartaResponsivaParrafo> listaParrafo = cartaResponsivaParrafoRepository.findAllByIdCartaResponsivaOrderByOrden(cr.getIdCartaResponsiva());


            List<CartaResponsivaParrafo> lista = new ArrayList<>();
            for (CartaResponsivaParrafo cp : listaParrafo) {
                if (cp.getParrafo() != null) {
                    CartaResponsivaParrafo nuevo = new CartaResponsivaParrafo();
                    nuevo.setParrafo(expandirMarcador(cp.getParrafo(), cr, listaDetalle));
                    lista.add(nuevo);
                }
            }

            /*Inicio Detalles*/
            if (listaDetalle.size()==0)
                throw new NoSuchElementException("No hay servicios agregados");

            List<CartaResponsivaDetalleBean> listaCrDetalle=new ArrayList<>();
            int contador=0;
            for (CartaResponsivaDetalle crd: listaDetalle){
                CartaResponsivaDetalleBean  crdBean=new CartaResponsivaDetalleBean();
                crdBean.setIdDetalle(crd.getIdCartaResponsivaDetalle());
                crdBean.setCveServicio(crd.getCveServicio());
                crdBean.setNombreServicio(crd.getNombreServicio());
                contador++;
                crdBean.setFila(contador);
                listaCrDetalle.add(crdBean);
            }

            if (cr.getIdLogo()!=null) {
                parametrosReporte.put("logo", cr.getRutaLogo());
                parametrosReporte2.put("logo", cr.getRutaLogo());
            }


            parametrosReporte2.put("aceptado",cr.getAcepto());
            parametrosReporte2.put("preview", "static/include/imagen/preview.png");
            parametrosReporte2.put("fecha",cr.getFechaAceptadaFija());

            parametrosReporte2.put("folio","<b>Folio: </b>"+cr.getFolioCr());
            parametrosReporte2.put("asunto","Carta Responsiva");
            parametrosReporte2.put("tipoServicio",cr.getTipoServicio());
            parametrosReporte2.put("subTipoServicio",cr.getSubtipoServicio());
            parametrosReporte2.put("qr", url_Cr);
            parametrosReporte2.put("ip_acepto","<b>IP Acepto: </b>"+cr.getIpAcepto());
            parametrosReporte2.put("host_acepto","<b>Host Acepto: </b>"+cr.getHostAcepto());
            parametrosReporte2.put("fecha_acepto","<b>Fecha Acepto: </b>"+cr.getFechaAceptoText());



            /*Fin Detalles*/

            File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/jasper/"+nombreReporte);
            InputStream in = new FileInputStream(file);

            JasperPrint jasperPrintPrincipal = JasperFillManager.fillReport(in,parametrosReporte, new JRBeanCollectionDataSource(lista));
            jasperPrintList.add(jasperPrintPrincipal);


            File file2 = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/jasper/carta_responsiva_anexo.jasper");
            InputStream in2 = new FileInputStream(file2);


            JasperPrint jasperPrintAnexo = JasperFillManager.fillReport(in2,parametrosReporte2, new JRBeanCollectionDataSource(listaCrDetalle));
            jasperPrintList.add(jasperPrintAnexo);



            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();

            return UtileriaReporte.jasperCreatePdf(baos.toByteArray(),"carta_responsiva_aceptada.pdf");

        }else
            throw new NoSuchElementException("No se ha aceptado la carta responsiva");


    }

}
