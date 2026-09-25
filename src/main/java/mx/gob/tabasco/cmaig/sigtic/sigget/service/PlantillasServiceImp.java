package mx.gob.tabasco.cmaig.sigtic.sigget.service;

import mx.gob.tabasco.cmaig.sigtic.Utileria.Utileria;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sac.service.UnidadesAdministrativaService;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Mensage;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Plantilla;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.PlantillaUploadBean;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Separador;
import mx.gob.tabasco.cmaig.sigtic.sigget.repository.CamposRepository;
import mx.gob.tabasco.cmaig.sigtic.sigget.repository.PlantillaRepository;
import mx.gob.tabasco.cmaig.sigtic.sigget.repository.SeparadorRepository;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(value = "siggetTransactionManager")
public class PlantillasServiceImp implements PlantillasService{

    @Autowired
    private PlantillaRepository plantillaRepository;

    @Autowired
    private SeparadorRepository separadorRepository;

    @Autowired
    private CamposRepository camposRepository;

    @Autowired
    private UnidadesAdministrativaService unidadesAdministrativaService;

    @Override
    public List<Plantilla> getPlantillas(String usuario) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return plantillaRepository.getPlantillas(usuario, authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico().longValue());
    }

    @Override
    public Plantilla getPlantillaId(Long idPlantilla) {

        Plantilla plantilla =plantillaRepository.findById(idPlantilla).get();

        if (plantilla.getIdUnidadAdmtva()!=null) {
            UnidadAdministrativa unidadAdministrativa = unidadesAdministrativaService.getUnidadAdministrativaId(plantilla.getIdUnidadAdmtva());
            plantilla.setNombreUnidadAdmtva(unidadAdministrativa.getNombreUnidadAdministrativa());
        }


        return plantilla;
    }

    @Override
    public List<Separador> getSeparador(String estatus) {
        return separadorRepository.findSeparadorByEstatus(estatus);
    }

    @Override
    public Plantilla savePlantilla(Plantilla plantilla) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        plantilla.setFechaRegistro(new Timestamp(new Date().getTime()));
        plantilla.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        plantilla.setCveUsuarioMtto(authUser.getUsername());
        return plantillaRepository.save(plantilla);
    }

    @Override
    public Plantilla updatePlantilla(Plantilla plantilla) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Plantilla plantillaActual = plantillaRepository.findById(plantilla.getIdPlantilla()).get();

        Integer numCampos = camposRepository.countByPlantilla(plantillaActual);

        if (plantilla.getNumCampos()<numCampos)
            throw new NoSuchElementException("El número de campos no puede ser menor al total de campos capturados");
        else {
            plantillaActual.setCveUsuarioMtto(authUser.getUsername());
            plantillaActual.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            plantillaActual.setNombrePlantilla(plantilla.getNombrePlantilla());
            plantillaActual.setNombrePlantillaAbrev(plantilla.getNombrePlantillaAbrev());
            plantillaActual.setBaseDatos(plantilla.getBaseDatos());
            plantillaActual.setNumLinea(plantilla.getNumLinea());
            plantillaActual.setNumCampos(plantilla.getNumCampos());
            plantillaActual.setNombreArchivo(plantilla.getNombreArchivo());
            plantillaActual.setIdSistema(plantilla.getIdSistema());
            plantillaActual.setIdUnidadAdmtva(plantilla.getIdUnidadAdmtva());
            plantillaActual.setSqlSelect(plantilla.getSqlSelect());
            plantillaActual.setSqlSelect2(plantilla.getSqlSelect2());
            plantillaActual.setSqlSelect3(plantilla.getSqlSelect3());
            plantillaActual.setSqlSelect4(plantilla.getSqlSelect4());
            plantillaActual.setSqlSelect5(plantilla.getSqlSelect5());
            plantillaActual.setSqlInsert(plantilla.getSqlInsert());
            plantillaActual.setSqlInsert2(plantilla.getSqlInsert2());
            plantillaActual.setSqlInsert3(plantilla.getSqlInsert3());
            plantillaActual.setSqlInsert4(plantilla.getSqlInsert4());
            plantillaActual.setSqlInsert5(plantilla.getSqlInsert5());
            plantillaActual.setSqlUpdate(plantilla.getSqlUpdate());
            plantillaActual.setSqlUpdate2(plantilla.getSqlUpdate2());
            plantillaActual.setSqlUpdate3(plantilla.getSqlUpdate3());
            plantillaActual.setSqlUpdate4(plantilla.getSqlUpdate4());
            plantillaActual.setSqlUpdate5(plantilla.getSqlUpdate5());
            plantillaActual.setSqlDelete(plantilla.getSqlDelete());
            plantillaActual.setSqlDelete2(plantilla.getSqlDelete2());
            plantillaActual.setSqlDelete3(plantilla.getSqlDelete3());
            plantillaActual.setSqlDelete4(plantilla.getSqlDelete4());
            plantillaActual.setSqlDelete5(plantilla.getSqlDelete5());
            plantillaActual.setSqlMerge(plantilla.getSqlMerge());
            plantillaActual.setSqlMerge2(plantilla.getSqlMerge2());
            plantillaActual.setSqlMerge3(plantilla.getSqlMerge3());
            plantillaActual.setSqlMerge4(plantilla.getSqlMerge4());
            plantillaActual.setSqlMerge5(plantilla.getSqlMerge5());
            plantillaActual.setSqlValidacion(plantilla.getSqlValidacion());
            plantillaActual.setSqlValidacion2(plantilla.getSqlValidacion2());
            plantillaActual.setSqlValidacion3(plantilla.getSqlValidacion3());
            plantillaActual.setSqlValidacion4(plantilla.getSqlValidacion4());
            plantillaActual.setSqlValidacion5(plantilla.getSqlValidacion5());
            plantillaActual.setCveEstatus(plantilla.getCveEstatus());
            plantillaActual.setIdSeparador(plantilla.getIdSeparador());
            plantillaActual.setEsquema(plantilla.getEsquema());

            return  plantillaRepository.save(plantillaActual);
        }
    }

    @Override
    public List<Plantilla> getPlantillasAll() {
        //List<Plantilla> lista=plantillaRepository.findAll(Sort.by("nombrePlantilla").ascending());
        List<Plantilla> lista=plantillaRepository.findAllByCveEstatusOrderByNombrePlantilla("VIG");

        for (Plantilla pl:lista)
            pl.setTotalCamposCapturados(camposRepository.countByPlantilla(pl));

        return lista;
    }

    @Override
    public Plantilla deletePlantilla(Plantilla plantilla) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Plantilla plantillaActual = plantillaRepository.findById(plantilla.getIdPlantilla()).get();

        plantillaActual.setCveEstatus("NOV");
        plantillaActual.setCveUsuarioMtto(authUser.getUsername());
        plantillaActual.setFechaUltimoCambio(new Timestamp(new Date().getTime()));

        return  plantillaRepository.save(plantillaActual);

    }

    @Override
    public Mensage uploadExpediente(PlantillaUploadBean plantillaUploadBean) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Plantilla plantilla = plantillaRepository.findById(plantillaUploadBean.getIdPlantilla()).get();

        if (!plantilla.getCveEstatus().equals("VIG"))
            throw new RuntimeException("Error: No se pueden adjuntar el archivo");

        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int anio = calendar.get(Calendar.YEAR);

        plantilla.setFileExtension(Utileria.getFileExtension(plantillaUploadBean.getNombre()));
        plantilla.setFileNameOriginal(plantillaUploadBean.getNombre());
        plantilla.setFileUserLoad(authUser.getUsername());

        String nombreArchivo = date.getTime() + "-" + plantilla.getIdPlantilla() + "." + plantilla.getFileExtension();

        plantilla.setFileName(nombreArchivo);

        String path="";
        try {

        File folders = new File("/plantillas_sigtic/"+anio);

            if (!folders.exists())
                folders.mkdirs();

            path = folders.getPath();
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Error: al crear la carpeta "+e);
        }

        plantilla.setFileRoute(path.replace("\\","/") + "/" + nombreArchivo);

        try {
            Utileria.writeFile(Base64.getDecoder().decode(plantillaUploadBean.getContenido()), nombreArchivo, path);
        }catch (IOException e){
            System.out.println(e.getMessage());
            System.out.println(e);
            throw new RuntimeException("Error: No se pueden adjuntar el archivo");
        }

        Mensage resp = new Mensage();
        plantillaRepository.save(plantilla);
        resp.setMessage("Archivo adjuntado correctamente");
        return resp;
    }

    @Override
    public void exportPlantilla(HttpServletResponse response, Long id) {

        try {
            Plantilla plantilla = plantillaRepository.findById(id).get();
            File file = ResourceUtils.getFile(plantilla.getFileRoute());
            InputStream in = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(in);
            in.close();

            response.setContentType("multipart/form-data");
            response.setHeader("Content-disposition", "attachment;filename=" + plantilla.getFileNameOriginal());
            response.setHeader("Pragma", "No-cache");
            response.setCharacterEncoding("utf-8");
            ServletOutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}