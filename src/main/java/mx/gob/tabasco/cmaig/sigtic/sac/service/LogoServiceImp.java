package mx.gob.tabasco.cmaig.sigtic.sac.service;


import mx.gob.tabasco.cmaig.sigtic.Utileria.Utileria;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.Logo;
import mx.gob.tabasco.cmaig.sigtic.sac.model.LogoBean;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.EntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.LogoBeanRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.LogoRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.UnidadAdministrativaRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CartaResponsiva;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CartaResponsivaRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StreamUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LogoServiceImp implements LogoService {

    @Value("${sigtic.archivos.ruta}")
    private String rutaBase;

    @Autowired
    private LogoRepository logosRepository;

    @Autowired
    private CartaResponsivaRepository cartaResponsivaRepository;

    @Autowired
    private EntePublicoService entePublicoService;

    @Autowired
    private LogoBeanRepository logoBeanRepository;

    @Autowired
    private UnidadAdministrativaRepository unidadAdministrativaRepository;

    @Override
    public List<LogoBean> getLogos() {
        List<LogoBean> lista = logoBeanRepository.getLogosAll();
        return lista;
    }

    @Override
    public List<LogoBean> getLogosEnte() {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<LogoBean> lista = logoBeanRepository.getLogos(authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico().longValue());
        return lista;
    }

    public Logo importarLogo(Logo logo) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Date date = new Date();
        String path="";

        try {
            File folders = new File(rutaBase, "logos");

            if (!folders.exists() && !folders.mkdirs()) {
                throw new IOException("No se pudo crear la carpeta de logos");
            }

            path = folders.getAbsolutePath();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new NoSuchElementException("Error al crear la carpeta: " + e.getMessage());
        }

        Optional<Logo> logoOp = logosRepository.findByNombreLogoAndCveEstatus(logo.getNombreLogo(),"VIG");

        if (!logoOp.isPresent()){
            Logo logoNew = new Logo();
            logoNew.setFechaRegistro(new Timestamp(new Date().getTime()));
            logoNew.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            logoNew.setCveEstatus("VIG");
            logoNew.setCveUsuarioMtto(authUser.getUsername());
            logoNew.setNombreLogo(logo.getNombreLogo());
            String nombreArchivo = date.getTime() + "." + Utileria.getFileExtension(logo.getNombreLogo());
            logoNew.setNombreLogoInterno(nombreArchivo);
            logoNew.setRutaArchivo(path.replace("\\","/") + "/" + nombreArchivo);


            try {
                Utileria.writeFile(Base64.getDecoder().decode(logo.getContenido()), nombreArchivo, path);
                BufferedImage bimg = ImageIO.read(new File(path.replace("\\","/") + "/" + nombreArchivo));
                logoNew.setAncho(bimg.getWidth());
                logoNew.setAlto(bimg.getHeight());

            }catch (IOException e){
                throw new NoSuchElementException("Error: No se pueden adjuntar el archivo");
            }

            return logosRepository.save(logoNew);
        }else
            throw new NoSuchElementException("Error: El logo ya esta cargado");


    }

    @Override
    public void exportLogo(HttpServletResponse response, Integer id) {

        try {
            Optional<Logo> logoOp = logosRepository.findById(id);

            if (!logoOp.isPresent()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Logo logo = logoOp.get();

            Path archivo = Paths.get(
                    rutaBase,
                    "logos",
                    logo.getNombreLogoInterno()
            );

            if (!Files.isRegularFile(archivo)) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            String nombre = logo.getNombreLogoInterno().toLowerCase();

            if (nombre.endsWith(".png")) {
                response.setContentType("image/png");
            } else if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg")) {
                response.setContentType("image/jpeg");
            } else if (nombre.endsWith(".gif")) {
                response.setContentType("image/gif");
            }

            try (InputStream in = Files.newInputStream(archivo)) {
                StreamUtils.copy(in, response.getOutputStream());
            }

        } catch (IOException e) {
            e.printStackTrace();

            if (!response.isCommitted()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public void exportLogoCr(HttpServletResponse response, Long id) {

        try {
            CartaResponsiva cartaResponsiva = cartaResponsivaRepository.findById(id).get();
            if (cartaResponsiva.getNombreLogo().toUpperCase().endsWith(".PNG"))
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
            else
            if (cartaResponsiva.getNombreLogo().toUpperCase().endsWith(".GIF"))
                response.setContentType(MediaType.IMAGE_GIF_VALUE);
            else
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ServletOutputStream out;
            out = response.getOutputStream();
            FileInputStream fin = new FileInputStream(ResourceUtils.getFile(cartaResponsiva.getRutaLogo()));

            BufferedInputStream bin = new BufferedInputStream(fin);
            BufferedOutputStream bout = new BufferedOutputStream(out);
            int ch =0; ;
            while((ch=bin.read())!=-1)
            {
                bout.write(ch);
            }

            bin.close();
            fin.close();
            bout.close();
            out.close();


        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Logo deleteLogo(Integer idLogo) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Logo logo = logosRepository.findById(idLogo).get();
        entePublicoService.deleteLogoEntePublico(idLogo);
        logo.setCveEstatus("NOV");
        logo.setCveUsuarioMtto(authUser.getUsername());
        logo.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        return logosRepository.save(logo);
    }

    @Override
    public UnidadAdministrativa asignarLogo(UnidadAdministrativa unidadAdministrativa) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Logo logo = logosRepository.findById(unidadAdministrativa.getIdLogo()).get();
        UnidadAdministrativa unidadAdministrativaUpdate = unidadAdministrativaRepository.findById(unidadAdministrativa.getIdUnidadAdministrativa()).get();
        entePublicoService.deleteLogoUnidadAdministrativa(unidadAdministrativa.getIdLogo());
        unidadAdministrativaUpdate.setIdLogo(logo.getIdLogo());
        unidadAdministrativaUpdate.setUsuarioMtto(authUser.getUsername());
        unidadAdministrativaUpdate.setFechaUltimo(new Timestamp(new Date().getTime()));
        return unidadAdministrativaRepository.save(unidadAdministrativaUpdate);
    }


}
