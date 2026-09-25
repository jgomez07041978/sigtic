package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.*;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.*;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.SegUsuarioRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

@Service
public class UnidadesAdministrativaServiceImp implements UnidadesAdministrativaService {


    private final ArbolRepository arbolRepository;
    private final SegUsuarioRepository segUsuarioRepository;
    private final UnidadAdministrativaRepository unidadAdministrativaRepository;
    private final CatUsuarioRepository catUsuarioRepository;
    private final UbicacionUnidadAdminRepository ubicacionUnidadAdminRepository;
    private final NivelUnidadAdministrativaRepository nivelUnidadAdministrativaRepository;
    private final SacCatUnidadAdministrativaViewRepository sacCatUnidadAdministrativaViewRepository;

    public UnidadesAdministrativaServiceImp(ArbolRepository arbolRepository, SegUsuarioRepository segUsuarioRepository,  @Qualifier("unidadAdministrativaRepositorySac") UnidadAdministrativaRepository unidadAdministrativaRepository, CatUsuarioRepository catUsuarioRepository, UbicacionUnidadAdminRepository ubicacionUnidadAdminRepository, NivelUnidadAdministrativaRepository nivelUnidadAdministrativaRepository, SacCatUnidadAdministrativaViewRepository sacCatUnidadAdministrativaViewRepository) {
        this.arbolRepository = arbolRepository;
        this.segUsuarioRepository = segUsuarioRepository;
        this.unidadAdministrativaRepository = unidadAdministrativaRepository;
        this.catUsuarioRepository = catUsuarioRepository;
        this.ubicacionUnidadAdminRepository = ubicacionUnidadAdminRepository;
        this.nivelUnidadAdministrativaRepository = nivelUnidadAdministrativaRepository;
        this.sacCatUnidadAdministrativaViewRepository = sacCatUnidadAdministrativaViewRepository;
    }

    @Override
    public List<Arbol> getUnidadAdministrativaAll(Long id) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (id==0 && !authUser.isAdministradorSigtic())
            return arbolRepository.getArbolSinParentConUsuario(authUser.getUsername());
        else
        if (id==0 && authUser.isAdministradorSigtic())
            return arbolRepository.getArbolAdminSigtic();
        else
            return arbolRepository.getArbolConParent(id);

    }

    @Override
    public List<Arbol> getUnidadAdministrativaAllFree(Long id) {
            return arbolRepository.getArbolConParent(id);

    }

    @Override
    public List<Arbol> getUnidadAdministrativaAllPorInstitucionFree(Long id) {
            return arbolRepository.getArbolConParentporInstitucion(id);
    }

    @Override
    public List<Arbol> getUnidadAdministrativaAllPorInstitucion(Long id) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (authUser.isAdministradorSigtic())
            return arbolRepository.getArbolConParentporInstitucion(id);
        else
            return arbolRepository.getArbolSinParentConUsuarioEnte(authUser.getUsername(), id);
    }

    @Override
    public List<Arbol> getUnidadAdministrativaPresupuestal(Long id) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return arbolRepository.getArbolUnidadAdminPresupuestal(id);
    }


    @Override
    public List<Arbol> getUnidadAdministrativaAllPorUsuario(Long id) {
        SegUsuario segUsuario = segUsuarioRepository.findByIdUsuarioAndSistema(id,"SIGTIC");
        List<Arbol> lista=arbolRepository.getArbolSinParentConUsuario(segUsuario.getSegUsuarioPk().getUsuario());

        return lista;
    }

    @Override
    public UnidadAdministrativa getUnidadAdministrativaId(Long id) {

        UnidadAdministrativa unidadAdministrativa=unidadAdministrativaRepository.findById(id).get();

        if (unidadAdministrativa.getRfcTitular()!=null){
            Optional<CatUsuario> catUsuarioOp=catUsuarioRepository.findByRfcAndCveEstatus(unidadAdministrativa.getRfcTitular(),"VIG");

            if (catUsuarioOp.isPresent())
                unidadAdministrativa.setNombreTitular(catUsuarioOp.get().getNombreCompleto());
        }

        if (unidadAdministrativa.getIdUnidadAdministrativaPadre()!= null) {
            Optional<UnidadAdministrativa> unidadAdministrativaPadreOp= unidadAdministrativaRepository.findById(unidadAdministrativa.getIdUnidadAdministrativaPadre());
            if (unidadAdministrativaPadreOp.isPresent())
                unidadAdministrativa.setNombreUnidadAdministrativaPadre("( "+unidadAdministrativaPadreOp.get().getCveUnidadAdministrativa()+" ) "+unidadAdministrativaPadreOp.get().getNombreUnidadAdministrativa());
        }

        if (unidadAdministrativa.getIdUbicacionUnidadAdministrativa()!=null){
            Optional<UbicacionUnidadAdmin> ubicacionUnidadAdminOp=ubicacionUnidadAdminRepository.findById(unidadAdministrativa.getIdUbicacionUnidadAdministrativa());
            if (ubicacionUnidadAdminOp.isPresent())
                unidadAdministrativa.setIdDomicilioEntePublico(ubicacionUnidadAdminOp.get().getDomicilioEntePublico().getIdDomicilioEntePublico());

        }

        return unidadAdministrativa;
    }

    @Override
    public List<UnidadAdministrativa> getUnidadAdministrativaPorEnteId(Long id) {

        return unidadAdministrativaRepository.findAllByIdEntePublicoAndEstatus(id, "VIG", Sort.by(Sort.Direction.ASC,"NivelUnidadAdministrativa.NumeroNivelUnidadAdministrativa").and(Sort.by(Sort.Direction.ASC,"NumeroOrden")));
        //return unidadAdministrativaRepository.findAllByIdEntePublicoAndEstatus(id, "VIG", Sort.by(Sort.Direction.ASC,"NivelUnidadAdministrativa.NumeroNivelUnidadAdministrativa").and(Sort.by(Sort.Direction.ASC,"NumeroOrden")));
    }

    @Override
    public List<NivelUnidadAdministrativa> getAllNivelUnidadAdministrativa(String cveEstatus) {
        return nivelUnidadAdministrativaRepository.findAllByCveEstatusOrderByNumeroNivelUnidadAdministrativaAscNumeroOrdenUnidadAdministrativaAsc(cveEstatus);
    }


    @Override
    public UnidadAdministrativa guardarUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (unidadAdministrativa.getIdUnidadAdministrativa()==null){

            UnidadAdministrativa unidadAdministrativaNew = new UnidadAdministrativa();

            if (unidadAdministrativa.getCveUnidadAdministrativa()!=null && !unidadAdministrativa.getCveUnidadAdministrativa().trim().equals("")){
                List<UnidadAdministrativa> unidadAdministrativaList= unidadAdministrativaRepository.findAllByCveUnidadAdministrativaAndEstatus(unidadAdministrativa.getCveUnidadAdministrativa(),"VIG");
                if (unidadAdministrativaList.size()>0)
                    throw new NoSuchElementException("La clave de la unidad esta en uso");
            }

            if (unidadAdministrativa.getIdUnidadAdministrativaPadre()!=null){
                Optional<UnidadAdministrativa> unidadAdministrativaPadreOp= unidadAdministrativaRepository.findById(unidadAdministrativa.getIdUnidadAdministrativaPadre());
                if (unidadAdministrativaPadreOp.isPresent()){
                    unidadAdministrativaNew.setIdUnidadAdministrativaPadre(unidadAdministrativa.getIdUnidadAdministrativaPadre());
                    unidadAdministrativaNew.setCveUnidadAdministrativaPadre(unidadAdministrativaPadreOp.get().getCveUnidadAdministrativa());
                }
            }

            unidadAdministrativaNew.setIdEntePublico(unidadAdministrativa.getIdEntePublico());
            unidadAdministrativaNew.setCveUnidadAdministrativa(unidadAdministrativa.getCveUnidadAdministrativa());
            unidadAdministrativaNew.setNombreUnidadAdministrativaAbreviada(unidadAdministrativa.getNombreUnidadAdministrativaAbreviada());
            unidadAdministrativaNew.setNombreUnidadAdministrativaAbreviada2(unidadAdministrativa.getNombreUnidadAdministrativaAbreviada());
            unidadAdministrativaNew.setNombreUnidadAdministrativa(unidadAdministrativa.getNombreUnidadAdministrativa());
            unidadAdministrativaNew.setTipoUnidadAdministrativa(1);

            NivelUnidadAdministrativa nivelUnidadAdministrativa = nivelUnidadAdministrativaRepository.findById(unidadAdministrativa.getNivelUnidadAdministrativa().getIdNivelUnidadAdministrativa()).get();
            unidadAdministrativaNew.setNivelUnidadAdministrativa(nivelUnidadAdministrativa);
            unidadAdministrativaNew.setIdUbicacionUnidadAdministrativa(unidadAdministrativa.getIdUbicacionUnidadAdministrativa());
            unidadAdministrativaNew.setNombreCargoUnidad(unidadAdministrativa.getNombreCargoUnidad());
            unidadAdministrativaNew.setNombreCategoriaUnidad(unidadAdministrativa.getNombreCategoriaUnidad());
            unidadAdministrativaNew.setReglamentoInterior(unidadAdministrativa.getReglamentoInterior());
            unidadAdministrativaNew.setTelefono(unidadAdministrativa.getTelefono());
            unidadAdministrativaNew.setNumeroExtension(unidadAdministrativa.getNumeroExtension());
            unidadAdministrativaNew.setCorreoUnidadAdministrativa(unidadAdministrativa.getCorreoUnidadAdministrativa());
            unidadAdministrativaNew.setRfcTitular(unidadAdministrativa.getRfcTitular());
            unidadAdministrativaNew.setNumeroOrden(unidadAdministrativa.getNumeroOrden());
            unidadAdministrativaNew.setUsuarioMtto(authUser.getUsername());
            unidadAdministrativaNew.setFechaRegistro(new Timestamp(new Date().getTime()));
            unidadAdministrativaNew.setFechaUltimo(new Timestamp(new Date().getTime()));
            unidadAdministrativaNew.setEstatus("VIG");
            unidadAdministrativaNew.setUnidadPresupuestal(unidadAdministrativa.getUnidadPresupuestal());
            return unidadAdministrativaRepository.save(unidadAdministrativaNew);
        }else {

            UnidadAdministrativa unidadAdministrativaEdit = unidadAdministrativaRepository.findById(unidadAdministrativa.getIdUnidadAdministrativa()).get();

            if (unidadAdministrativa.getCveUnidadAdministrativa()!=null && !unidadAdministrativa.getCveUnidadAdministrativa().trim().equals("")){

                List<UnidadAdministrativa> unidadAdministrativaList= unidadAdministrativaRepository.findAllByCveUnidadAdministrativaAndEstatus(unidadAdministrativa.getCveUnidadAdministrativa(),"VIG");

                if (unidadAdministrativaList.size()==1){
                   if (unidadAdministrativaEdit.getIdUnidadAdministrativa().longValue()!=unidadAdministrativaList.get(0).getIdUnidadAdministrativa().longValue())
                       throw new NoSuchElementException("La clave de la unidad esta en uso");
                }else
                if (unidadAdministrativaList.size()>0)
                    throw new NoSuchElementException("La clave de la unidad esta en uso");
            }

            if (unidadAdministrativa.getIdUnidadAdministrativaPadre()!=null){
                Optional<UnidadAdministrativa> unidadAdministrativaPadreOp= unidadAdministrativaRepository.findById(unidadAdministrativa.getIdUnidadAdministrativaPadre());
                if (unidadAdministrativaPadreOp.isPresent()){
                    unidadAdministrativaEdit.setIdUnidadAdministrativaPadre(unidadAdministrativa.getIdUnidadAdministrativaPadre());
                    unidadAdministrativaEdit.setCveUnidadAdministrativaPadre(unidadAdministrativaPadreOp.get().getCveUnidadAdministrativa());
                }
            }

            unidadAdministrativaEdit.setIdEntePublico(unidadAdministrativa.getIdEntePublico());
            unidadAdministrativaEdit.setCveUnidadAdministrativa(unidadAdministrativa.getCveUnidadAdministrativa());
            unidadAdministrativaEdit.setNombreUnidadAdministrativaAbreviada(unidadAdministrativa.getNombreUnidadAdministrativaAbreviada());
            unidadAdministrativaEdit.setNombreUnidadAdministrativa(unidadAdministrativa.getNombreUnidadAdministrativa());
            unidadAdministrativaEdit.setTipoUnidadAdministrativa(1);

            NivelUnidadAdministrativa nivelUnidadAdministrativa = nivelUnidadAdministrativaRepository.findById(unidadAdministrativa.getNivelUnidadAdministrativa().getIdNivelUnidadAdministrativa()).get();
            unidadAdministrativaEdit.setNivelUnidadAdministrativa(nivelUnidadAdministrativa);
            unidadAdministrativaEdit.setIdUbicacionUnidadAdministrativa(unidadAdministrativa.getIdUbicacionUnidadAdministrativa());
            unidadAdministrativaEdit.setNombreCargoUnidad(unidadAdministrativa.getNombreCargoUnidad());
            unidadAdministrativaEdit.setNombreCategoriaUnidad(unidadAdministrativa.getNombreCategoriaUnidad());
            unidadAdministrativaEdit.setReglamentoInterior(unidadAdministrativa.getReglamentoInterior());
            unidadAdministrativaEdit.setTelefono(unidadAdministrativa.getTelefono());
            unidadAdministrativaEdit.setNumeroExtension(unidadAdministrativa.getNumeroExtension());
            unidadAdministrativaEdit.setCorreoUnidadAdministrativa(unidadAdministrativa.getCorreoUnidadAdministrativa());
            unidadAdministrativaEdit.setRfcTitular(unidadAdministrativa.getRfcTitular());
            unidadAdministrativaEdit.setNumeroOrden(unidadAdministrativa.getNumeroOrden());
            unidadAdministrativaEdit.setUsuarioMtto(authUser.getUsername());
            unidadAdministrativaEdit.setFechaUltimo(new Timestamp(new Date().getTime()));
            unidadAdministrativaEdit.setEstatus("VIG");
            unidadAdministrativaEdit.setUnidadPresupuestal(unidadAdministrativa.getUnidadPresupuestal());

            return unidadAdministrativaRepository.save(unidadAdministrativaEdit);
        }

    }

    @Override
    public UnidadAdministrativa deleteUnidadAdministrativa(Long id) {

        List<UnidadAdministrativa> lista = unidadAdministrativaRepository.findAllByIdUnidadAdministrativaPadreAndEstatus(id,"VIG");

        if (lista.size()>0)
            throw new NoSuchElementException("La unidad administrativa tiene unidades relacionadas");

        UnidadAdministrativa unidadAdministrativa = unidadAdministrativaRepository.findById(id).get();

        unidadAdministrativa.setEstatus("NOV");

        return unidadAdministrativaRepository.save(unidadAdministrativa);
    }

    @Override
    public List<SacCatUnidadAdministrativaView> getSacUnidadAdministrativaPorEnteId(Long id) {

        return sacCatUnidadAdministrativaViewRepository.findAllByIdEntePublicoAndEstatus(id,"VIG");
    }


    @Override
    public void generaReporteUnidadesAdministrativas(Long idEntePublico, HttpServletResponse response) {
        Map<Integer, String> nivel = new HashMap<>();
        nivel.put(1,"");
        nivel.put(2,"... ");
        nivel.put(3,"...... ");
        nivel.put(4,"......... ");
        nivel.put(5,"............ ");
        nivel.put(6,"............... ");
        nivel.put(7,".................. ");
        nivel.put(8,"..................... ");
        nivel.put(9,"........................ ");


        List<SacCatUnidadAdministrativaView> listaUnidades =sacCatUnidadAdministrativaViewRepository.findAllByIdEntePublicoAndEstatus(idEntePublico,"VIG");

        File folders = new File(System.getProperty("user.home") + "/repositorio/unidades_administrativas/");

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(0, "Hoja excel");

            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            //HSSFRow headerRow = sheet.createRow(0)
            Row headerRow = sheet.createRow(0);
            Cell cell = headerRow.createCell(0);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("ID_UNIDAD_ADMINISTRATIVA");
            sheet.autoSizeColumn(0);

            Cell cell1 = headerRow.createCell(1);
            cell1.setCellStyle(headerStyle);
            cell1.setCellValue("CVE_UNIDAD_ADMINISTRATIVA");
            sheet.autoSizeColumn(1);

            Cell cell2 = headerRow.createCell(2);
            cell2.setCellStyle(headerStyle);
            cell2.setCellValue("CVE_UNIDAD_ADMINISTRATIVA_PADRE");
            sheet.autoSizeColumn(2);

            Cell cell3 = headerRow.createCell(3);
            cell3.setCellStyle(headerStyle);
            cell3.setCellValue("UNIDAD ADMINISTRATIVA ABREVIADO");
            sheet.autoSizeColumn(3);

            Cell cell4 = headerRow.createCell(4);
            cell4.setCellStyle(headerStyle);
            cell4.setCellValue("UNIDAD ADMINISTRATIVA");
            sheet.autoSizeColumn(4);

            Cell cell5 = headerRow.createCell(5);
            cell5.setCellStyle(headerStyle);
            cell5.setCellValue("NIVEL DE LA UNIDAD ADMINISTRATIVA");
            sheet.autoSizeColumn(5);

            Cell cell6 = headerRow.createCell(6);
            cell6.setCellStyle(headerStyle);
            cell6.setCellValue("ES PRESUPUESTAL");
            sheet.autoSizeColumn(6);

            Cell cell7 = headerRow.createCell(7);
            cell7.setCellStyle(headerStyle);
            cell7.setCellValue("NÚMERO DE ORDEN");
            sheet.autoSizeColumn(7);

            int i=1;

            for (SacCatUnidadAdministrativaView reg:listaUnidades){
                Row headerRowReg = sheet.createRow(i);

                if (reg.getIdUnidadAdministrativa()!=null) {
                    Cell cellx = headerRowReg.createCell(0);
                    cellx.setCellValue(reg.getIdUnidadAdministrativa());
                }

                if (reg.getCveUnidadAdministrativaPadre()!=null) {
                    Cell cellx1 = headerRowReg.createCell(1);
                    cellx1.setCellValue(reg.getCveUnidadAdministrativa());
                }

                if (reg.getCveUnidadAdministrativa()!=null) {
                    Cell cellx2 = headerRowReg.createCell(2);
                    cellx2.setCellValue(reg.getCveUnidadAdministrativaPadre());
                }

                if (reg.getNombreUnidadAdministrativaAbreviada()!=null) {
                    Cell cellx3 = headerRowReg.createCell(3);
                    cellx3.setCellValue(reg.getNombreUnidadAdministrativaAbreviada());
                }

                if (reg.getNombreUnidadAdministrativa()!=null) {
                    Cell cellx4 = headerRowReg.createCell(4);
                    cellx4.setCellValue(nivel.get(reg.getNumeroNivelUnidadAdministrativa())+reg.getNombreUnidadAdministrativa());
                }

                if (reg.getNivelUnidadAdministrativa()!=null) {
                    Cell cellx5 = headerRowReg.createCell(5);
                    cellx5.setCellValue(reg.getNivelUnidadAdministrativa().getNombreNivelUnidadAdministrativa());
                }

                if (reg.getUnidadPresupuestal()!=null) {
                    Cell cellx6 = headerRowReg.createCell(6);
                    cellx6.setCellValue(reg.getUnidadPresupuestal());
                }

                if (reg.getNumeroOrden()!=null) {
                    Cell cellx7 = headerRowReg.createCell(7);
                    cellx7.setCellValue(reg.getNumeroOrden());
                }

                i++;

            }


            if (!folders.exists())
                folders.mkdirs();

            File verifica = new File(folders.getPath() + "/Unidad_administrativa_"+idEntePublico+".xlsx");
            File archivo;
            if (verifica.exists()) {
                verifica.delete();
                archivo = new File(folders.getPath() + "/Unidad_administrativa_"+idEntePublico+".xlsx");
            } else {
                archivo = verifica;
            }
            FileOutputStream file = new FileOutputStream(archivo);
            workbook.write(file);
            file.close();

        } catch (Exception e) {
            throw new NoSuchElementException(e.getMessage());
        }

        try {
            File file = new File(folders.getPath() +"/Unidad_administrativa_"+idEntePublico+".xlsx");
            InputStream inputStream = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            inputStream.close();

            response.setContentType("multipart/form-data");
            response.setHeader("Content-disposition", "attachment;filename=" + "Unidad_administrativa_"+idEntePublico+".xlsx");
            response.setHeader("Pragma", "No-cache");
            response.setCharacterEncoding("utf-8");
            ServletOutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();

        } catch (Exception e) {
            throw new NoSuchElementException(e.getMessage());
        }

    }


    @Override
    @org.springframework.transaction.annotation.Transactional(transactionManager = "catalogosTransactionManager", readOnly = true)
    public void exportarCatalogoUnidadesAdministrativas(Long idEntePublico, HttpServletResponse response) {
        List<SacCatUnidadAdministrativaView> listaUnidades =
                sacCatUnidadAdministrativaViewRepository.findAllByIdEntePublicoAndEstatus(idEntePublico, "VIG");

        try {
            ClassPathResource resource = new ClassPathResource("static/plantillas/unidades_administrativas.xlsx");
            Workbook workbook = new XSSFWorkbook(resource.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            Row filaFecha = sheet.getRow(2);
            if (filaFecha == null) filaFecha = sheet.createRow(2);
            Cell labelCell = filaFecha.getCell(0);
            if (labelCell == null) labelCell = filaFecha.createCell(0);
            labelCell.setCellValue("Fecha de Descarga:");
            int lastCol = sheet.getRow(3).getLastCellNum() - 1;
            Cell fechaCell = filaFecha.getCell(lastCol);
            if (fechaCell == null) fechaCell = filaFecha.createCell(lastCol);
            fechaCell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

            int i = 4;
            for (SacCatUnidadAdministrativaView u : listaUnidades) {

                String domicilio        = "";
                String ubicacionUnidad  = "";
                String ubicacionTitular = "";
                String correo           = "";
                String telefono         = "";
                String extension        = "";
                String numOrden         = "";

                if (u.getIdUbicacionUnidadAdministrativa() != null) {
                    Optional<UbicacionUnidadAdmin> ubicOp = ubicacionUnidadAdminRepository.findById(u.getIdUbicacionUnidadAdministrativa());
                    if (ubicOp.isPresent()) {
                        UbicacionUnidadAdmin ubic = ubicOp.get();
                        if (ubic.getDomicilioEntePublico() != null && ubic.getDomicilioEntePublico().getNombreDomicilio() != null)
                            domicilio = ubic.getDomicilioEntePublico().getNombreDomicilio();
                        ubicacionUnidad  = ubic.getNombreUbicacion()             != null ? ubic.getNombreUbicacion()             : "";
                        ubicacionTitular = ubic.getUbicacionTitularEntePublico() != null ? ubic.getUbicacionTitularEntePublico() : "";
                        correo           = ubic.getCorreoUnidad()                != null ? ubic.getCorreoUnidad()                : "";
                        telefono         = ubic.getNumTelefono()                 != null ? ubic.getNumTelefono()                 : "";
                        extension        = ubic.getExtension()                   != null ? ubic.getExtension()                   : "";
                        numOrden         = ubic.getNumOrden()                    != null ? ubic.getNumOrden().toString()          : "";
                    }
                }

                Row row = sheet.createRow(i++);
                row.createCell(0) .setCellValue(u.getIdUnidadAdministrativa()             != null ? u.getIdUnidadAdministrativa()                                                 : 0L);
                row.createCell(1) .setCellValue(u.getIdEntePublico()                      != null ? u.getIdEntePublico()                                                          : 0L);
                row.createCell(2) .setCellValue("");
                row.createCell(3) .setCellValue("");
                row.createCell(4) .setCellValue(u.getCveUnidadAdministrativa()            != null ? u.getCveUnidadAdministrativa()                                                : "");
                row.createCell(5) .setCellValue(u.getCveUnidadAdministrativaPadre()       != null ? u.getCveUnidadAdministrativaPadre()                                           : "");
                row.createCell(6) .setCellValue(u.getNombreUnidadAdministrativaAbreviada()!= null ? u.getNombreUnidadAdministrativaAbreviada()                                    : "");
                row.createCell(7) .setCellValue(u.getDescripcionUnidadAdministrativaXn()  != null ? u.getDescripcionUnidadAdministrativaXn()                                      : "");
                row.createCell(8) .setCellValue(u.getNivelUnidadAdministrativa()          != null ? u.getNivelUnidadAdministrativa().getNombreNivelUnidadAdministrativa()          : "");
                row.createCell(9) .setCellValue(u.getUnidadPresupuestal()                 != null ? u.getUnidadPresupuestal()                                                     : "");
                row.createCell(10).setCellValue(domicilio);
                row.createCell(11).setCellValue(ubicacionUnidad);
                row.createCell(12).setCellValue(ubicacionTitular);
                row.createCell(13).setCellValue(correo);
                row.createCell(14).setCellValue(u.getTelefono()        != null ? u.getTelefono()        : "");
                row.createCell(15).setCellValue(u.getNumeroExtension() != null ? u.getNumeroExtension() : "");
                row.createCell(16).setCellValue(u.getNumeroOrden()     != null ? u.getNumeroOrden()     : "");
            }

            for (int col = 0; col < 17; col++) {
                if (col != 7 && col != 10 && col != 11 && col != 12) sheet.autoSizeColumn(col);
            }

            String filename = "unidades_administrativas_" + idEntePublico + "_"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.setHeader("Pragma", "No-cache");
            workbook.write(response.getOutputStream());
            workbook.close();

        } catch (IOException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

}