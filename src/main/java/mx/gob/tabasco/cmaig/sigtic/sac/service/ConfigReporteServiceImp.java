package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.Utileria.UtileriaReporte;
import mx.gob.tabasco.cmaig.sigtic.sac.model.*;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.*;
import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatSubtipoServiciosTicRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConfigReporteServiceImp implements ConfigReporteService{


    @Autowired
    private CatUsuarioEntePublicoRepository catUsuarioEntePublicoRepository;

    @Autowired
    private CatPlantillaReporteBeanRepository catPlantillaReporteBeanRepository;

    @Autowired
    private LogoRepository logoRepository;

    @Autowired
    private CatPlantillaReporteRepository catPlantillaReporteRepository;

    @Autowired
    private CatSubtipoServiciosTicRepository catSubtipoServiciosTicRepository;

    @Autowired
    private ServletContext servletContext;


    @Autowired
    private CatPlantillaReporteParrafoRepository catPlantillaReporteParrafoRepository;



    @Override
    public List<CatPlantillaReporteBean> getDatosReportes() {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CatPlantillaReporteBean> lista=catPlantillaReporteBeanRepository.getPlantillaReporte(authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico().longValue(),"VIG");

        return lista;
    }

    @Override
    public CatPlantillaReporte guardarConfiguracionReporte(CatPlantillaReporte catPlantillaReporte) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<CatUsuarioEntePublico> catUsuarioEntePublicoEntrega = catUsuarioEntePublicoRepository.findById(catPlantillaReporte.getCatUsuarioEntePublicoEntrega().getIdUsuarioEntePublico());
        Optional<CatUsuarioEntePublico> catUsuarioEntePublicoAutoriza = catUsuarioEntePublicoRepository.findById(catPlantillaReporte.getCatUsuarioEntePublicoAutoriza().getIdUsuarioEntePublico());
        Optional<Logo> logoOp = logoRepository.findById(catPlantillaReporte.getLogo().getIdLogo());
        Optional<CatSubtipoServiciosTic> catSubtipoServiciosTicOp = catSubtipoServiciosTicRepository.findById(catPlantillaReporte.getIdSubtipoServicio());

        if (!catUsuarioEntePublicoEntrega.isPresent())
            throw new NoSuchElementException("El usuario que entrega no existe");

        if (!catUsuarioEntePublicoEntrega.get().getCveEstatus().equals("VIG"))
            throw new NoSuchElementException("El usuario que entrega no esta vigente");

        if (catUsuarioEntePublicoEntrega.get().getPuesto()== null)
            throw new NoSuchElementException("El usuario que entrega no tiene cargo");

        if (!catUsuarioEntePublicoAutoriza.isPresent())
            throw new NoSuchElementException("El usuario que autoriza no existe");

        if (!catUsuarioEntePublicoAutoriza.get().getCveEstatus().equals("VIG"))
            throw new NoSuchElementException("El usuario que autoriza no esta vigente");

        if (catUsuarioEntePublicoAutoriza.get().getPuesto()== null)
            throw new NoSuchElementException("El usuario que autoriza no tiene cargo");

        if (!logoOp.isPresent())
            throw new NoSuchElementException("El logo no existe");

        if (!logoOp.get().getCveEstatus().equals("VIG"))
            throw new NoSuchElementException("El logo no existe");

        if (!catSubtipoServiciosTicOp.isPresent())
            throw new NoSuchElementException("El subtipo de servicio no existe");



        if (catPlantillaReporte.getIdPlantillaReporte()!=null){
            CatPlantillaReporte catPlantillaReporteUpdate = catPlantillaReporteRepository.findById(catPlantillaReporte.getIdPlantillaReporte()).get();
            catPlantillaReporteUpdate.setNombreReporte(catPlantillaReporte.getNombreReporte());
            catPlantillaReporteUpdate.setTituloReporte(catPlantillaReporte.getTituloReporte());
            catPlantillaReporteUpdate.setDecFirma1(catPlantillaReporte.getDecFirma1());
            catPlantillaReporteUpdate.setFirma2(catUsuarioEntePublicoEntrega.get().getCatUsuario().getNombreCompleto());
            catPlantillaReporteUpdate.setCargo2(catUsuarioEntePublicoEntrega.get().getPuesto());
            catPlantillaReporteUpdate.setDecFirma2(catPlantillaReporte.getDecFirma2());
            catPlantillaReporteUpdate.setFirma3(catUsuarioEntePublicoAutoriza.get().getCatUsuario().getNombreCompleto());
            catPlantillaReporteUpdate.setCargo3(catUsuarioEntePublicoAutoriza.get().getPuesto());
            catPlantillaReporteUpdate.setDecFirma3(catPlantillaReporte.getDecFirma3());
            catPlantillaReporteUpdate.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            catPlantillaReporteUpdate.setIpPC(authUser.getIp());
            catPlantillaReporteUpdate.setNombrePC(authUser.getHosName());
            catPlantillaReporteUpdate.setCveUsuarioMtto(authUser.getUsername());
            catPlantillaReporteUpdate.setEntePublico(authUser.getCatUsuarioEntePublico().getEntePublico());
            catPlantillaReporteUpdate.setIdSubtipoServicio(catPlantillaReporte.getIdSubtipoServicio());
            catPlantillaReporteUpdate.setCatUsuarioEntePublicoEntrega(catUsuarioEntePublicoEntrega.get());
            catPlantillaReporteUpdate.setCurpEntrega(catUsuarioEntePublicoEntrega.get().getCatUsuario().getCurp());
            catPlantillaReporteUpdate.setCatUsuarioEntePublicoAutoriza(catUsuarioEntePublicoAutoriza.get());
            catPlantillaReporteUpdate.setCurpAutoriza(catUsuarioEntePublicoAutoriza.get().getCatUsuario().getCurp());
            catPlantillaReporteUpdate.setLogo(logoOp.get());

            return catPlantillaReporteRepository.save(catPlantillaReporteUpdate);
        }else{
            CatPlantillaReporte  catPlantillaReporteNew = new CatPlantillaReporte();
            catPlantillaReporteNew.setNombreReporte(catPlantillaReporte.getNombreReporte());
            catPlantillaReporteNew.setTituloReporte(catPlantillaReporte.getTituloReporte());
            catPlantillaReporteNew.setDecFirma1(catPlantillaReporte.getDecFirma1());
            catPlantillaReporteNew.setFirma2(catUsuarioEntePublicoEntrega.get().getCatUsuario().getNombreCompleto());
            catPlantillaReporteNew.setCargo2(catUsuarioEntePublicoEntrega.get().getPuesto());
            catPlantillaReporteNew.setDecFirma2(catPlantillaReporte.getDecFirma2());
            catPlantillaReporteNew.setFirma3(catUsuarioEntePublicoAutoriza.get().getCatUsuario().getNombreCompleto());
            catPlantillaReporteNew.setCargo3(catUsuarioEntePublicoAutoriza.get().getPuesto());
            catPlantillaReporteNew.setDecFirma3(catPlantillaReporte.getDecFirma3());
            catPlantillaReporteNew.setFechaRegistro(new Timestamp(new Date().getTime()));
            catPlantillaReporteNew.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            catPlantillaReporteNew.setIpPC(authUser.getIp());
            catPlantillaReporteNew.setNombrePC(authUser.getHosName());
            catPlantillaReporteNew.setCveUsuarioMtto(authUser.getUsername());
            catPlantillaReporteNew.setIdUsuarioRegistro(authUser.getIdUsuario());
            catPlantillaReporteNew.setCveEstatus("VIG");
            catPlantillaReporteNew.setEntePublico(authUser.getCatUsuarioEntePublico().getEntePublico());
            catPlantillaReporteNew.setIdSubtipoServicio(catPlantillaReporte.getIdSubtipoServicio());
            catPlantillaReporteNew.setCatUsuarioEntePublicoEntrega(catUsuarioEntePublicoEntrega.get());
            catPlantillaReporteNew.setCurpEntrega(catUsuarioEntePublicoEntrega.get().getCatUsuario().getCurp());
            catPlantillaReporteNew.setCatUsuarioEntePublicoAutoriza(catUsuarioEntePublicoAutoriza.get());
            catPlantillaReporteNew.setCurpAutoriza(catUsuarioEntePublicoAutoriza.get().getCatUsuario().getCurp());
            catPlantillaReporteNew.setLogo(logoOp.get());

            return catPlantillaReporteRepository.save(catPlantillaReporteNew);

        }
    }

    @Override
    public CatPlantillaReporte editarConfiguracionReporte(Long idPlantillaReporte) {

        CatPlantillaReporte  plantillaReporte =  catPlantillaReporteRepository.findById(idPlantillaReporte.longValue()).get();
        CatSubtipoServiciosTic catSubtipoServiciosTic = catSubtipoServiciosTicRepository.findById(plantillaReporte.getIdSubtipoServicio()).get();
        plantillaReporte.setRutaLogo(servletContext.getContextPath()+"/api/logo/exportLogo/"+plantillaReporte.getLogo().getIdLogo());
        plantillaReporte.setCatSubtipoServiciosTic(catSubtipoServiciosTic);

        return plantillaReporte;
    }

    @Override
    public CatPlantillaReporte deleteConfiguracionReporte(Long idPlantillaReporte) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CatPlantillaReporte  plantillaReporte =  catPlantillaReporteRepository.findById(idPlantillaReporte.longValue()).get();
        plantillaReporte.setCveEstatus("NOV");
        plantillaReporte.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        plantillaReporte.setIpPC(authUser.getIp());
        plantillaReporte.setNombrePC(authUser.getHosName());
        plantillaReporte.setCveUsuarioMtto(authUser.getUsername());
        plantillaReporte.setIdUsuarioRegistro(authUser.getIdUsuario());

        return catPlantillaReporteRepository.save(plantillaReporte);
    }


    @Override
    public CatPlantillaReporteParrafo addParrafoPlantilla(CatPlantillaReporteParrafo catPlantillaReporteParrafo) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (catPlantillaReporteParrafo.getIdPlantillaReporteParrafo()!=null){

            List<CatPlantillaReporteParrafo> lista = catPlantillaReporteParrafoRepository.findAllByIdPlantillaReporteAndCveEstatusOrderByNumeroOrdenAsc(catPlantillaReporteParrafo.getIdPlantillaReporte().longValue(),"VIG");

            if (lista.size()>0) {
                int numero=catPlantillaReporteParrafo.getNumeroOrden();
                for (CatPlantillaReporteParrafo pf : lista) {

                    if (pf.getNumeroOrden() >= catPlantillaReporteParrafo.getNumeroOrden()) {
                        if (pf.getIdPlantillaReporteParrafo().longValue()!=catPlantillaReporteParrafo.getIdPlantillaReporteParrafo().longValue())
                            numero++;
                        pf.setNumeroOrden(numero);
                        pf.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
                        pf.setIpPC(authUser.getIp());
                        pf.setNombrePC(authUser.getHosName());
                        pf.setCveUsuarioMtto(authUser.getUsername());
                    }
                }
                catPlantillaReporteParrafoRepository.saveAll(lista);
            }

            CatPlantillaReporteParrafo catPlantillaReporteParrafoUpdate = catPlantillaReporteParrafoRepository.findById(catPlantillaReporteParrafo.getIdPlantillaReporteParrafo()).get();
            catPlantillaReporteParrafoUpdate.setParrafo(catPlantillaReporteParrafo.getParrafo());
            catPlantillaReporteParrafoUpdate.setNumeroOrden(catPlantillaReporteParrafo.getNumeroOrden());
            catPlantillaReporteParrafoUpdate.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            catPlantillaReporteParrafoUpdate.setIpPC(authUser.getIp());
            catPlantillaReporteParrafoUpdate.setNombrePC(authUser.getHosName());
            catPlantillaReporteParrafoUpdate.setCveUsuarioMtto(authUser.getUsername());

            return catPlantillaReporteParrafoRepository.save(catPlantillaReporteParrafoUpdate);
        }else{
            List<CatPlantillaReporteParrafo> lista = catPlantillaReporteParrafoRepository.findAllByIdPlantillaReporteAndCveEstatusOrderByNumeroOrdenAsc(catPlantillaReporteParrafo.getIdPlantillaReporte().longValue(),"VIG");

            if (lista.size()>0) {
                int numero=catPlantillaReporteParrafo.getNumeroOrden();
                for (CatPlantillaReporteParrafo pf : lista) {
                    if (pf.getNumeroOrden() >= catPlantillaReporteParrafo.getNumeroOrden()) {
                            numero++;
                        pf.setNumeroOrden(numero);
                        pf.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
                        pf.setIpPC(authUser.getIp());
                        pf.setNombrePC(authUser.getHosName());
                        pf.setCveUsuarioMtto(authUser.getUsername());

                    }
                }
                catPlantillaReporteParrafoRepository.saveAll(lista);
            }

            CatPlantillaReporteParrafo catPlantillaReporteParrafoNew = new CatPlantillaReporteParrafo();
            catPlantillaReporteParrafoNew.setParrafo(catPlantillaReporteParrafo.getParrafo());
            catPlantillaReporteParrafoNew.setNumeroOrden(catPlantillaReporteParrafo.getNumeroOrden());
            catPlantillaReporteParrafoNew.setIdPlantillaReporte(catPlantillaReporteParrafo.getIdPlantillaReporte());
            catPlantillaReporteParrafoNew.setCveEstatus("VIG");
            catPlantillaReporteParrafoNew.setFechaRegistro(new Timestamp(new Date().getTime()));
            catPlantillaReporteParrafoNew.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            catPlantillaReporteParrafoNew.setIpPC(authUser.getIp());
            catPlantillaReporteParrafoNew.setNombrePC(authUser.getHosName());
            catPlantillaReporteParrafoNew.setCveUsuarioMtto(authUser.getUsername());
            catPlantillaReporteParrafoNew.setIdUsuarioRegistro(authUser.getIdUsuario());

            return catPlantillaReporteParrafoRepository.save(catPlantillaReporteParrafoNew);
        }
    }

    @Override
    public List<CatPlantillaReporteParrafo> getListaPlantillaParrafo(CatPlantillaReporteParrafo catPlantillaReporteParrafo){
        List<CatPlantillaReporteParrafo> lista = catPlantillaReporteParrafoRepository.findAllByIdPlantillaReporteAndCveEstatusOrderByNumeroOrdenAsc(catPlantillaReporteParrafo.getIdPlantillaReporte().longValue(),"VIG");

        return lista;
    }

    @Override
    public CatPlantillaReporteParrafo editarParrafo(Long idPlantillaReporteParrafo) {

        CatPlantillaReporteParrafo  plantillaReporteParrafo =  catPlantillaReporteParrafoRepository.findById(idPlantillaReporteParrafo.longValue()).get();

        return plantillaReporteParrafo;
    }

    @Override
    public CatPlantillaReporteParrafo deleteParrafo(Long idPlantillaReporteParrafo) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CatPlantillaReporteParrafo  plantillaReporteParrafo =  catPlantillaReporteParrafoRepository.findById(idPlantillaReporteParrafo.longValue()).get();
        plantillaReporteParrafo.setCveEstatus("NOV");
        plantillaReporteParrafo.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        plantillaReporteParrafo.setIpPC(authUser.getIp());
        plantillaReporteParrafo.setNombrePC(authUser.getHosName());
        plantillaReporteParrafo.setCveUsuarioMtto(authUser.getUsername());
        plantillaReporteParrafo.setIdUsuarioRegistro(authUser.getIdUsuario());

        return catPlantillaReporteParrafoRepository.save(plantillaReporteParrafo);
    }


    @Override
    public ResponseEntity<byte[]> getReportePrueba(Long idPlantillaReporte) throws FileNotFoundException, JRException {

        CatPlantillaReporte catPlantillaReporte = catPlantillaReporteRepository.findById(idPlantillaReporte).get();


        List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
        Map<String, Object> parametrosReporte = new HashMap<String, Object>();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMM");
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy");
        String fechaActualText = dateFormat.format(date) + " de " + dateFormat2.format(date) + " del " + dateFormat3.format(date);


        parametrosReporte.put("nombre_reporte", catPlantillaReporte.getNombreReporte());
        parametrosReporte.put("titulo", catPlantillaReporte.getTituloReporte());
        parametrosReporte.put("fecha", "Villahermosa, Tabasco a "+fechaActualText);
        parametrosReporte.put("folio", "<b>Folio: </b> 21-000001-25" );
        parametrosReporte.put("ip_acepto", "<b>IP Acepto: </b> -");
        parametrosReporte.put("host_acepto", "<b>Host Acepto: </b> -");
        parametrosReporte.put("fecha_acepto", "<b>Fecha Acepto: </b> -");
        parametrosReporte.put("aceptado","NO");
        parametrosReporte.put("preview", "static/include/imagen/preview.png");


        String url_Cr = "https://sigtic.tabasco.gob.mx";
        parametrosReporte.put("qr", url_Cr);

        String nombreReporte = "carta_responsiva_aceptada.jasper";


        List<CatPlantillaReporteParrafo> listaParrafo = catPlantillaReporteParrafoRepository.findAllByIdPlantillaReporteAndCveEstatusOrderByNumeroOrdenAsc(catPlantillaReporte.getIdPlantillaReporte().longValue(),"VIG");



        /*Inicio Detalles*/
        if (listaParrafo.size() == 0)
            throw new NoSuchElementException("No hay parrafos agregados");


        if (catPlantillaReporte.getLogo().getIdLogo() != null) {
            parametrosReporte.put("logo", catPlantillaReporte.getLogo().getRutaArchivo());
        }

        /*Fin Detalles*/

        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/jasper/" + nombreReporte);
        InputStream in = new FileInputStream(file);

        JasperPrint jasperPrintPrincipal = JasperFillManager.fillReport(in, parametrosReporte, new JRBeanCollectionDataSource(listaParrafo));
        jasperPrintList.add(jasperPrintPrincipal);


        File file2 = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/jasper/carta_responsiva_anexo.jasper");
        InputStream in2 = new FileInputStream(file2);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        exporter.setConfiguration(configuration);
        exporter.exportReport();

        return UtileriaReporte.jasperCreatePdf(baos.toByteArray(), "ejemplo_carta_responsiva.pdf");

    }

}
