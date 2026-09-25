package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.DomicilioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.DomicilioEntePublicoBean;
import mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UbicacionUnidadAdmin;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.DomicilioEntePublicoBeanRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.DomicilioEntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.EntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.UbicacionUnidadAdminRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DomicilioEntePublicoServiceImp implements DomicilioEntePublicoService {

    @Autowired
    private DomicilioEntePublicoRepository domicilioEntePublicoRepository;

    @Autowired
    private EntePublicoRepository entePublicoRepository;

    @Autowired
    private UbicacionUnidadAdminRepository ubicacionUnidadAdminRepository;

    @Autowired
    @Qualifier("catalogoDatasource")
    private DataSource catalogoDatasource;

    @Autowired
    private DomicilioEntePublicoBeanRepository domicilioEntePublicoBeanRepository;

    @Override
    public List<DomicilioEntePublico> listaDomicilioEntePublico(Long idEntePublico) {

        EntePublico entePublico = entePublicoRepository.findById(idEntePublico).get();

        return domicilioEntePublicoRepository.findAllByEntePublicoAndCveEstatusOrderByNombreDomicilio(entePublico, "VIG");
    }

    @Override
    public List<DomicilioEntePublicoBean> listaDomicilioEntePublicoBean(Long idEntePublico) {
        EntePublico ente = entePublicoRepository.findById(idEntePublico).orElse(null);
        if (ente == null) return domicilioEntePublicoBeanRepository.getDomiciliosEntePublico(idEntePublico);

        List<DomicilioEntePublico> ordenados = domicilioEntePublicoRepository
                .findAllByEntePublicoAndCveEstatusOrderByNumOrden(ente, "VIG");
        List<Long> idsOrdenados = ordenados.stream()
                .map(DomicilioEntePublico::getIdDomicilioEntePublico)
                .collect(java.util.stream.Collectors.toList());

        List<DomicilioEntePublicoBean> beans = domicilioEntePublicoBeanRepository.getDomiciliosEntePublico(idEntePublico);
        beans.sort(java.util.Comparator.comparingInt(b -> {
            int idx = idsOrdenados.indexOf(b.getIdDomicilio());
            return idx == -1 ? Integer.MAX_VALUE : idx;
        }));
        return beans;
    }

    @Override
    public DomicilioEntePublico deleteDomicilio(DomicilioEntePublico domicilioEntePublico){
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<DomicilioEntePublico> domicilioEntePublicoOptional =  domicilioEntePublicoRepository.findById(domicilioEntePublico.getIdDomicilioEntePublico());

        if (domicilioEntePublicoOptional.isPresent()){
            if (ubicacionUnidadAdminRepository.existsAllByDomicilioEntePublicoAndCveEstatus(domicilioEntePublicoOptional.get(),"VIG"))
                throw new NoSuchElementException("El domiciilio no se puede eliminar porque tiene ubicaciones relacionadas");
            else{
                domicilioEntePublicoOptional.get().setCveEstatus("NOV");
                domicilioEntePublicoOptional.get().setCveUsuarioMtto(authUser.getUsername());
                domicilioEntePublicoOptional.get().setFechaUltimoCambio(new Timestamp(new Date().getTime()));
                return domicilioEntePublicoRepository.save(domicilioEntePublicoOptional.get());
            }


        }else
            throw new NoSuchElementException("Se domicilio no existe.");
    }

    @Override
    public DomicilioEntePublico saveDomicilio(DomicilioEntePublico domicilioEntePublico){
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (domicilioEntePublico.getIdDomicilioEntePublico()!=null){
            DomicilioEntePublico domicilioEntePublicoUpdate=domicilioEntePublicoRepository.findById(domicilioEntePublico.getIdDomicilioEntePublico()).get();
            domicilioEntePublicoUpdate.setIdEntidadFederativa(domicilioEntePublico.getIdEntidadFederativa());
            domicilioEntePublicoUpdate.setIdMunicipio(domicilioEntePublico.getIdMunicipio());
            domicilioEntePublicoUpdate.setIdLocalidad(domicilioEntePublico.getIdLocalidad());
            domicilioEntePublicoUpdate.setIdCodigoPostal(domicilioEntePublico.getIdCodigoPostal());
            domicilioEntePublicoUpdate.setIdTipoVialidad(domicilioEntePublico.getIdTipoVialidad());
            domicilioEntePublicoUpdate.setIdTipoAsentamiento(domicilioEntePublico.getIdTipoAsentamiento());
            domicilioEntePublicoUpdate.setNombreDomicilio(domicilioEntePublico.getNombreDomicilio());
            domicilioEntePublicoUpdate.setNombreDomicilioAbrev(domicilioEntePublico.getNombreDomicilioAbrev());
            domicilioEntePublicoUpdate.setCalle(domicilioEntePublico.getCalle());
            domicilioEntePublicoUpdate.setNumExterior(domicilioEntePublico.getNumExterior());
            domicilioEntePublicoUpdate.setNumInterior(domicilioEntePublico.getNumInterior());
            domicilioEntePublicoUpdate.setColonia(domicilioEntePublico.getColonia());
            domicilioEntePublicoUpdate.setNumTelefono(domicilioEntePublico.getNumTelefono());
            domicilioEntePublicoUpdate.setNumExtension(domicilioEntePublico.getNumExtension());
            domicilioEntePublicoUpdate.setReferenciaDomicilio(domicilioEntePublico.getReferenciaDomicilio());
            domicilioEntePublicoUpdate.setNumOrden(domicilioEntePublico.getNumOrden());
            domicilioEntePublicoUpdate.setCveUsuarioMtto(authUser.getUsername());
            domicilioEntePublicoUpdate.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            domicilioEntePublicoUpdate.setCveEstatus(domicilioEntePublico.getCveEstatus());
            EntePublico entePublico = entePublicoRepository.findById(domicilioEntePublico.getEntePublico().getIdEntePublico()).get();
            domicilioEntePublicoUpdate.setEntePublico(entePublico);
            domicilioEntePublicoUpdate.setIdEntePublicoRaw(entePublico.getIdEntePublico()); // ← Se agrego este linea
            domicilioEntePublicoUpdate.setReferenciaEntreCalle(domicilioEntePublico.getReferenciaEntreCalle());
            domicilioEntePublicoUpdate.setReferenciaCallePosterior(domicilioEntePublico.getReferenciaCallePosterior());
            domicilioEntePublicoUpdate.setCodigoPostalLocalidadEp(domicilioEntePublico.getCodigoPostalLocalidadEp());
            domicilioEntePublicoUpdate.setLatitud(domicilioEntePublico.getLatitud());
            domicilioEntePublicoUpdate.setLongitud(domicilioEntePublico.getLongitud());

            return domicilioEntePublicoRepository.save(domicilioEntePublicoUpdate);

        }else{
            DomicilioEntePublico domicilioEntePublicoNew = new DomicilioEntePublico();
            domicilioEntePublicoNew.setIdEntidadFederativa(domicilioEntePublico.getIdEntidadFederativa());
            domicilioEntePublicoNew.setIdMunicipio(domicilioEntePublico.getIdMunicipio());
            domicilioEntePublicoNew.setIdLocalidad(domicilioEntePublico.getIdLocalidad());
            domicilioEntePublicoNew.setIdCodigoPostal(domicilioEntePublico.getIdCodigoPostal());
            domicilioEntePublicoNew.setIdTipoVialidad(domicilioEntePublico.getIdTipoVialidad());
            domicilioEntePublicoNew.setIdTipoAsentamiento(domicilioEntePublico.getIdTipoAsentamiento());
            domicilioEntePublicoNew.setNombreDomicilio(domicilioEntePublico.getNombreDomicilio());
            domicilioEntePublicoNew.setNombreDomicilioAbrev(domicilioEntePublico.getNombreDomicilioAbrev());
            domicilioEntePublicoNew.setCalle(domicilioEntePublico.getCalle());
            domicilioEntePublicoNew.setNumExterior(domicilioEntePublico.getNumExterior());
            domicilioEntePublicoNew.setNumInterior(domicilioEntePublico.getNumInterior());
            domicilioEntePublicoNew.setColonia(domicilioEntePublico.getColonia());
            domicilioEntePublicoNew.setNumTelefono(domicilioEntePublico.getNumTelefono());
            domicilioEntePublicoNew.setNumExtension(domicilioEntePublico.getNumExtension());
            domicilioEntePublicoNew.setReferenciaDomicilio(domicilioEntePublico.getReferenciaDomicilio());
            domicilioEntePublicoNew.setNumOrden(domicilioEntePublico.getNumOrden());
            domicilioEntePublicoNew.setCveUsuarioMtto(authUser.getUsername());
            domicilioEntePublicoNew.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            domicilioEntePublicoNew.setCveEstatus(domicilioEntePublico.getCveEstatus());
            EntePublico entePublico = entePublicoRepository.findById(domicilioEntePublico.getEntePublico().getIdEntePublico()).get();
            domicilioEntePublicoNew.setEntePublico(entePublico);
            domicilioEntePublicoNew.setIdEntePublicoRaw(entePublico.getIdEntePublico()); // ← Se agrego este linea
            domicilioEntePublicoNew.setReferenciaEntreCalle(domicilioEntePublico.getReferenciaEntreCalle());
            domicilioEntePublicoNew.setReferenciaCallePosterior(domicilioEntePublico.getReferenciaCallePosterior());
            domicilioEntePublicoNew.setCodigoPostalLocalidadEp(domicilioEntePublico.getCodigoPostalLocalidadEp());
            domicilioEntePublicoNew.setLatitud(domicilioEntePublico.getLatitud());
            domicilioEntePublicoNew.setLongitud(domicilioEntePublico.getLongitud());

            return domicilioEntePublicoRepository.save(domicilioEntePublicoNew);
        }

    }

    @Override
    public DomicilioEntePublico getDomicilioId(Long idDomicilio) {
        return domicilioEntePublicoRepository.findById(idDomicilio).get();
    }





    private String construirDomicilioCompleto(DomicilioEntePublico d) {
        if (d == null) return "";
        StringBuilder sb = new StringBuilder();
        if (d.getCalle() != null && !d.getCalle().isEmpty()) sb.append(d.getCalle());
        if (d.getNumExterior() != null && !d.getNumExterior().isEmpty()) sb.append(" #").append(d.getNumExterior());
        if (d.getNumInterior() != null && !d.getNumInterior().isEmpty()) sb.append(" Int. ").append(d.getNumInterior());
        if (d.getColonia() != null && !d.getColonia().isEmpty()) sb.append(", Col. ").append(d.getColonia());
        if (d.getCodigoPostalLocalidadEp() != null && !d.getCodigoPostalLocalidadEp().isEmpty()) sb.append(", C.P. ").append(d.getCodigoPostalLocalidadEp());
        if (sb.length() == 0 && d.getNombreDomicilio() != null) sb.append(d.getNombreDomicilio());
        return sb.toString();
    }

    private void escribirEncabezadoFecha(Sheet sheet, Workbook workbook) {
        Row filaFecha = sheet.getRow(2);
        if (filaFecha == null) filaFecha = sheet.createRow(2);
        Cell labelCell = filaFecha.getCell(0);
        if (labelCell == null) labelCell = filaFecha.createCell(0);
        labelCell.setCellValue("Fecha de Descarga:");
        int lastCol = sheet.getRow(3).getLastCellNum() - 1;
        Cell fechaCell = filaFecha.getCell(lastCol);
        if (fechaCell == null) fechaCell = filaFecha.createCell(lastCol);
        fechaCell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    @Override
    public void exportarDomiciliosPorEnte(Long idEntePublico, HttpServletResponse response) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(catalogoDatasource);
        String sql = "SELECT V.ID_DOMICILIO_ENTE_PUBLICO, V.CVE_ENTE_PUBLICO, V.DESC_ENTE_PUBLICO, V.DESC_DOMICILIO_COMPLETO_EP, " +
                     "D.LATITUD_DECIMAL, D.LONGITUD_DECIMAL, V.NUM_ORDEN_DOMICILIO_ENTE_PUB " +
                     "FROM SAC_CAT_DOMICILIO_ENTE_PUBLICO V " +
                     "LEFT JOIN CAT_DOMICILIOS_ENTES_PUBLICOS D ON D.ID_DOMICILIO_ENTE_PUBLICO = V.ID_DOMICILIO_ENTE_PUBLICO " +
                     "WHERE V.ID_ENTE_PUBLICO = ? AND V.CVE_ESTATUS = 'VIG' " +
                     "ORDER BY V.DESC_ENTE_PUBLICO, V.NUM_ORDEN_DOMICILIO_ENTE_PUB";
        List<Map<String, Object>> lista = jdbcTemplate.queryForList(sql, idEntePublico);
        try {
            ClassPathResource resource = new ClassPathResource("static/plantillas/domicilios_ente.xlsx");
            Workbook workbook = new XSSFWorkbook(resource.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            escribirEncabezadoFecha(sheet, workbook);
            int i = 4;
            for (Map<String, Object> d : lista) {
                Row fila = sheet.createRow(i++);
                fila.createCell(0).setCellValue(d.get("ID_DOMICILIO_ENTE_PUBLICO")   != null ? d.get("ID_DOMICILIO_ENTE_PUBLICO").toString()   : "");
                fila.createCell(1).setCellValue(d.get("CVE_ENTE_PUBLICO")           != null ? d.get("CVE_ENTE_PUBLICO").toString()           : "");
                fila.createCell(2).setCellValue(d.get("DESC_ENTE_PUBLICO")          != null ? d.get("DESC_ENTE_PUBLICO").toString()          : "");
                fila.createCell(3).setCellValue(d.get("DESC_DOMICILIO_COMPLETO_EP") != null ? d.get("DESC_DOMICILIO_COMPLETO_EP").toString() : "");
                fila.createCell(4).setCellValue(d.get("LATITUD_DECIMAL")            != null ? new java.math.BigDecimal(d.get("LATITUD_DECIMAL").toString()).doubleValue() : 0);
                fila.createCell(5).setCellValue(d.get("LONGITUD_DECIMAL")           != null ? new java.math.BigDecimal(d.get("LONGITUD_DECIMAL").toString()).doubleValue() : 0);
                fila.createCell(6).setCellValue(d.get("NUM_ORDEN_DOMICILIO_ENTE_PUB") != null ? d.get("NUM_ORDEN_DOMICILIO_ENTE_PUB").toString() : "");
            }
            for (int col = 0; col < 7; col++) sheet.autoSizeColumn(col);
            String filename = "domicilios_ente_" + idEntePublico + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.setHeader("Pragma", "No-cache");
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    @Override
    public void exportarUbicacionesPorEnte(Long idEntePublico, HttpServletResponse response) {
        EntePublico ente = entePublicoRepository.findById(idEntePublico).orElseThrow(NoSuchElementException::new);
        List<DomicilioEntePublico> domicilios = domicilioEntePublicoRepository.findAllByEntePublicoAndCveEstatusOrderByNombreDomicilio(ente, "VIG");
        try {
            ClassPathResource resource = new ClassPathResource("static/plantillas/ubicaciones_ente.xlsx");
            Workbook workbook = new XSSFWorkbook(resource.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            escribirEncabezadoFecha(sheet, workbook);
            int i = 4;
            for (DomicilioEntePublico d : domicilios) {
                List<UbicacionUnidadAdmin> ubicaciones = (List<UbicacionUnidadAdmin>) ubicacionUnidadAdminRepository
                        .findAllByDomicilioEntePublicoAndCveEstatusOrderByNumOrden(d, "VIG");
                for (UbicacionUnidadAdmin u : ubicaciones) {
                    Row fila = sheet.createRow(i++);
                    fila.createCell(0).setCellValue(u.getIdUbicacionAdaministrativa() != null ? u.getIdUbicacionAdaministrativa() : 0L);
                    fila.createCell(1).setCellValue(ente.getCveEntePublico()    != null ? ente.getCveEntePublico()    : "");
                    fila.createCell(2).setCellValue(ente.getNombreEntePublico() != null ? ente.getNombreEntePublico() : "");
                    fila.createCell(3).setCellValue(construirDomicilioCompleto(d));
                    fila.createCell(4).setCellValue(u.getNombreUbicacion()      != null ? u.getNombreUbicacion()      : "");
                    fila.createCell(5).setCellValue(u.getNumOrden()             != null ? u.getNumOrden().toString()  : "");
                }
            }
            for (int col = 0; col < 6; col++) sheet.autoSizeColumn(col);
            String filename = "ubicaciones_ente_" + idEntePublico + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.setHeader("Pragma", "No-cache");
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    @Override
    public void exportarTodasUbicaciones(HttpServletResponse response) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(catalogoDatasource);

        String sqlUbicaciones = "SELECT ID_UBICACION_UNIDAD_ADMTVA, ID_DOMICILIO_UNIDAD_ADMTVA, DESC_UBICACION_UNIDAD_ADMTVA, NUM_ORDEN_UBICACION_UNIDAD_ADM " +
                     "FROM CAT_UBICACIONES_UNIDAD_ADMTVA WHERE CVE_ESTATUS = ? " +
                     "ORDER BY ID_DOMICILIO_UNIDAD_ADMTVA, NUM_ORDEN_UBICACION_UNIDAD_ADM";
        List<Map<String, Object>> filas = jdbcTemplate.queryForList(sqlUbicaciones, "VIG");

        String sqlDomicilios = "SELECT ID_DOMICILIO_ENTE_PUBLICO, ID_ENTE_PUBLICO, CVE_ENTE_PUBLICO, DESC_ENTE_PUBLICO, " +
                     "DESC_DOMICILIO_COMPLETO_EP " +
                     "FROM SAC_CAT_DOMICILIO_ENTE_PUBLICO";
        List<Map<String, Object>> domiciliosRaw = jdbcTemplate.queryForList(sqlDomicilios);

        Map<Long, Map<String, Object>> mapaDomicilios = new HashMap<>();
        for (Map<String, Object> d : domiciliosRaw) {
            Object id = d.get("ID_DOMICILIO_ENTE_PUBLICO");
            if (id != null) mapaDomicilios.put(Long.valueOf(id.toString()), d);
        }

        try {
            ClassPathResource resource = new ClassPathResource("static/plantillas/ubicaciones_todas.xlsx");
            Workbook workbook = new XSSFWorkbook(resource.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            escribirEncabezadoFecha(sheet, workbook);
            int i = 4;

            for (Map<String, Object> fila : filas) {
                Object idUbicacionObj = fila.get("ID_UBICACION_UNIDAD_ADMTVA");
                Object idDomObj = fila.get("ID_DOMICILIO_UNIDAD_ADMTVA");
                Long idDomicilio = idDomObj != null ? Long.valueOf(idDomObj.toString()) : null;
                String nombreUbicacion = (String) fila.get("DESC_UBICACION_UNIDAD_ADMTVA");
                Object ordenObj = fila.get("NUM_ORDEN_UBICACION_UNIDAD_ADM");
                Integer numOrden = ordenObj != null ? Integer.valueOf(ordenObj.toString()) : null;

                String cveEnte = "";
                String nombreEnte = "";
                String domicilioCompleto = "";

                if (idDomicilio != null && idDomicilio != 0L) {
                    Map<String, Object> d = mapaDomicilios.get(idDomicilio);
                    if (d != null) {
                        domicilioCompleto = d.get("DESC_DOMICILIO_COMPLETO_EP") != null ? d.get("DESC_DOMICILIO_COMPLETO_EP").toString() : "";
                        cveEnte    = d.get("CVE_ENTE_PUBLICO")  != null ? d.get("CVE_ENTE_PUBLICO").toString()  : "";
                        nombreEnte = d.get("DESC_ENTE_PUBLICO") != null ? d.get("DESC_ENTE_PUBLICO").toString() : "";
                    }
                }

                Row r = sheet.createRow(i++);
                r.createCell(0).setCellValue(idUbicacionObj != null ? idUbicacionObj.toString() : "");
                r.createCell(1).setCellValue(cveEnte);
                r.createCell(2).setCellValue(nombreEnte);
                r.createCell(3).setCellValue(domicilioCompleto);
                r.createCell(4).setCellValue(nombreUbicacion != null ? nombreUbicacion : "");
                r.createCell(5).setCellValue(numOrden        != null ? numOrden.toString() : "");
            }
            for (int col = 0; col < 6; col++) sheet.autoSizeColumn(col);
            String filename = "ubicaciones_todas_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.setHeader("Pragma", "No-cache");
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    private String construirDomicilioCompletoRaw(Map<String, Object> d) {
        StringBuilder sb = new StringBuilder();
        Object calle = d.get("CALLE_ENTE_PUBLICO");
        Object numExt = d.get("NUM_EXTERIOR_ENTE_PUBLICO");
        Object numInt = d.get("NUM_INTERIOR_ENTE_PUBLICO");
        Object colonia = d.get("COLONIA_ENTE_PUBLICO");
        Object cp = d.get("CODIGO_POSTAL_LOCALIDAD_E_P");
        Object nombreDom = d.get("DESC_DOMICILIO_ENTE_PUBLICO");

        if (calle != null && !calle.toString().isEmpty()) sb.append(calle);
        if (numExt != null && !numExt.toString().isEmpty()) sb.append(" #").append(numExt);
        if (numInt != null && !numInt.toString().isEmpty()) sb.append(" Int. ").append(numInt);
        if (colonia != null && !colonia.toString().isEmpty()) sb.append(", Col. ").append(colonia);
        if (cp != null && !cp.toString().isEmpty()) sb.append(", C.P. ").append(cp);
        if (sb.length() == 0 && nombreDom != null) sb.append(nombreDom);
        return sb.toString();
    }


    @Override
    public void exportarTodosDomicilios(HttpServletResponse response) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(catalogoDatasource);
        String sql = "SELECT V.ID_DOMICILIO_ENTE_PUBLICO, V.CVE_ENTE_PUBLICO, V.DESC_ENTE_PUBLICO, V.DESC_DOMICILIO_COMPLETO_EP, " +
                     "D.LATITUD_DECIMAL, D.LONGITUD_DECIMAL, V.NUM_ORDEN_DOMICILIO_ENTE_PUB " +
                     "FROM SAC_CAT_DOMICILIO_ENTE_PUBLICO V " +
                     "LEFT JOIN CAT_DOMICILIOS_ENTES_PUBLICOS D ON D.ID_DOMICILIO_ENTE_PUBLICO = V.ID_DOMICILIO_ENTE_PUBLICO " +
                     "WHERE V.CVE_ESTATUS = 'VIG' " +
                     "ORDER BY V.DESC_ENTE_PUBLICO, V.NUM_ORDEN_DOMICILIO_ENTE_PUB";
        List<Map<String, Object>> lista = jdbcTemplate.queryForList(sql);
        try {
            ClassPathResource resource = new ClassPathResource("static/plantillas/domicilios_ente.xlsx");
            Workbook workbook = new XSSFWorkbook(resource.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            escribirEncabezadoFecha(sheet, workbook);
            int i = 4;
            for (Map<String, Object> d : lista) {
                Row fila = sheet.createRow(i++);
                fila.createCell(0).setCellValue(d.get("ID_DOMICILIO_ENTE_PUBLICO")   != null ? d.get("ID_DOMICILIO_ENTE_PUBLICO").toString()   : "");
                fila.createCell(1).setCellValue(d.get("CVE_ENTE_PUBLICO")           != null ? d.get("CVE_ENTE_PUBLICO").toString()           : "");
                fila.createCell(2).setCellValue(d.get("DESC_ENTE_PUBLICO")          != null ? d.get("DESC_ENTE_PUBLICO").toString()          : "");
                fila.createCell(3).setCellValue(d.get("DESC_DOMICILIO_COMPLETO_EP") != null ? d.get("DESC_DOMICILIO_COMPLETO_EP").toString() : "");
                fila.createCell(4).setCellValue(d.get("LATITUD_DECIMAL")            != null ? new BigDecimal(d.get("LATITUD_DECIMAL").toString()).doubleValue() : 0);
                fila.createCell(5).setCellValue(d.get("LONGITUD_DECIMAL")           != null ? new BigDecimal(d.get("LONGITUD_DECIMAL").toString()).doubleValue() : 0);
                fila.createCell(6).setCellValue(d.get("NUM_ORDEN_DOMICILIO_ENTE_PUB") != null ? d.get("NUM_ORDEN_DOMICILIO_ENTE_PUB").toString() : "");
            }
            for (int col = 0; col < 7; col++) sheet.autoSizeColumn(col);
            String filename = "todos_domicilios_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.setHeader("Pragma", "No-cache");
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) { throw new NoSuchElementException(e.getMessage()); }
    }

}