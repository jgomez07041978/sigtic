package mx.gob.tabasco.cmaig.sigtic.sigget.service;

import mx.gob.tabasco.cmaig.sigtic.Utileria.Utileria;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CartaResponsiva;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.GrupoManual;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Manual;
import mx.gob.tabasco.cmaig.sigtic.sigget.repository.GrupoManualRepository;
import mx.gob.tabasco.cmaig.sigtic.sigget.repository.ManualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class ManualServiceImp implements ManualService {


    @Autowired
    private ManualRepository manualRepository;

    @Autowired
    private GrupoManualRepository grupoManualRepository;

    @Override
    public List<Manual> getManualesTipo(String tipo) {
        return manualRepository.getManualesPorTipo(tipo);
    }

    @Override
    public List<Manual> getManuales() {
        return manualRepository.getManuales();
    }

    @Override
    public List<GrupoManual> getGrupos() {
        return grupoManualRepository.findAllByCveEstatusOrderByNumeroOrden("VIG");
    }

    @Override
    public GrupoManual guardarGrupo(GrupoManual grupoManual) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (grupoManual.getIdGrupoManual()!=null){
            GrupoManual grupoManualEdit= grupoManualRepository.findById(grupoManual.getIdGrupoManual()).get();
            grupoManualEdit.setNombreGrupo(grupoManual.getNombreGrupo());
            grupoManualEdit.setNumeroOrden(grupoManual.getNumeroOrden());
            grupoManualEdit.setIdUsuarioMtto(authUser.getIdUsuario());
            grupoManualEdit.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            return grupoManualRepository.save(grupoManualEdit);
        }else{
            GrupoManual grupoManualNew= new GrupoManual();
            grupoManualNew.setNombreGrupo(grupoManual.getNombreGrupo());
            grupoManualNew.setNumeroOrden(grupoManual.getNumeroOrden());
            grupoManualNew.setCveEstatus("VIG");
            grupoManualNew.setFechaRegistro(new Timestamp(new Date().getTime()));
            grupoManualNew.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            grupoManualNew.setIdUsuarioMtto(authUser.getIdUsuario());

            return grupoManualRepository.save(grupoManualNew);
        }

    }

    @Override
    public GrupoManual deleteGrupo(GrupoManual grupoManual) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (grupoManual.getIdGrupoManual()!=null) {
            GrupoManual grupoManualEdit = grupoManualRepository.findById(grupoManual.getIdGrupoManual()).get();

            if (manualRepository.countByGrupoManualAndCveEstatus(grupoManualEdit,"VIG")>0)
                throw new NoSuchElementException("El grupo tienes manuales vigentes");

            grupoManualEdit.setCveEstatus("NOV");
            grupoManualEdit.setIdUsuarioMtto(authUser.getIdUsuario());
            grupoManualEdit.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            return grupoManualRepository.save(grupoManualEdit);
        }
        else
            throw new NoSuchElementException("El grupo no existe");
    }

    @Override
    public GrupoManual getGrupoId(Integer idGrupoManual) {
        return grupoManualRepository.findById(idGrupoManual).get();
    }

    @Override
    public List<Manual> getManualesGrupo(Integer  idGrupoManual) {

        GrupoManual grupoManual =  grupoManualRepository.findById(idGrupoManual).get();
        return manualRepository.findAllByGrupoManualAndCveEstatusOrderByNumeroOrden(grupoManual,"VIG");
    }

    @Override
    public Manual guardarManual(Manual manual) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Date date = new Date();
        String path="";
        try {

            File folders = new File("/archivos_sigtic/manuales/");

            if (!folders.exists())
                folders.mkdirs();

            path = folders.getPath();
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Error: al crear la carpeta "+e);
        }

        if (manual.getIdManual()!=null){
            Manual manualEdit=manualRepository.findById(manual.getIdManual()).get();
            manualEdit.setTipoManual(manual.getTipoManual());
            manualEdit.setNombreManual(manual.getNombreManual());
            manualEdit.setDescripcionManual(manual.getDescripcionManual());
            manualEdit.setNumeroOrden(manual.getNumeroOrden());
            manualEdit.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            manualEdit.setIdUsuarioMtto(authUser.getIdUsuario());

            if (!manual.getNombreArchivo().equals("")){
                manualEdit.setFileExtension(Utileria.getFileExtension(manual.getNombreArchivo()));
                manualEdit.setNombreArchivoOriginal(manual.getNombreArchivo());

                String nombreArchivo = date.getTime() + "-" + manualEdit.getIdManual() + "." + manualEdit.getFileExtension();
                manualEdit.setRutaArchivo(path.replace("\\","/") + "/" + nombreArchivo);
                manualEdit.setNombreArchivo(nombreArchivo);

                try {
                    Utileria.writeFile(Base64.getDecoder().decode(manual.getContenido()), nombreArchivo, path);
                }catch (IOException e){
                    System.out.println(e.getMessage());
                    System.out.println(e);
                    throw new RuntimeException("Error: No se pueden adjuntar el archivo");
                }
            }


            return manualRepository.save(manualEdit);
        }else{
            Manual manualNew = new Manual();
            GrupoManual grupoManual=grupoManualRepository.findById(manual.getGrupoManual().getIdGrupoManual()).get();
            manualNew.setGrupoManual(grupoManual);
            manualNew.setTipoManual(manual.getTipoManual());
            manualNew.setNombreManual(manual.getNombreManual());
            manualNew.setDescripcionManual(manual.getDescripcionManual());
            manualNew.setFechaRegistro(new Timestamp(new Date().getTime()));
            manualNew.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            manualNew.setNumeroOrden(manual.getNumeroOrden());
            manualNew.setCveEstatus("VIG");
            manualNew.setIdUsuarioMtto(authUser.getIdUsuario());
            manualNew=manualRepository.save(manualNew);

            if (manual.getNombreArchivo()!=null){
                manualNew.setFileExtension(Utileria.getFileExtension(manual.getNombreArchivo()));
                manualNew.setNombreArchivoOriginal(manual.getNombreArchivo());

                String nombreArchivo = date.getTime() + "-" + manualNew.getIdManual() + "." + manualNew.getFileExtension();
                manualNew.setRutaArchivo(path.replace("\\","/") + "/" + nombreArchivo);
                manualNew.setNombreArchivo(nombreArchivo);

                try {
                    Utileria.writeFile(Base64.getDecoder().decode(manual.getContenido()), nombreArchivo, path);
                }catch (IOException e){
                    System.out.println(e.getMessage());
                    System.out.println(e);
                    throw new RuntimeException("Error: No se pueden adjuntar el archivo");
                }
            }else
                throw new NoSuchElementException("Seleccione el manual");

            return manualRepository.save(manualNew);
        }
    }

    @Override
    public Manual deleteManual(Manual manual) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Manual manualEdit=manualRepository.findById(manual.getIdManual()).get();
        manualEdit.setCveEstatus("NOV");
        manualEdit.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        manualEdit.setIdUsuarioMtto(authUser.getIdUsuario());

        return manualRepository.save(manualEdit);
    }

    @Override
    public Manual getManuald(Long idManual) {
        return manualRepository.findById(idManual).get();
    }

    @Override
    public void exportManual(HttpServletResponse response, Long id) {

        try {
            Manual manual = manualRepository.findById(id).get();
            File file = ResourceUtils.getFile(manual.getRutaArchivo());
            InputStream in = new FileInputStream(file);
            response.setContentType("multipart/form-data");
            response.setHeader("Content-disposition", "attachment;filename=" + manual.getNombreArchivoOriginal());
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
}
