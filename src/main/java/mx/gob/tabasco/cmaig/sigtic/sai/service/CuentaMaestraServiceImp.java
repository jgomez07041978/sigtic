package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sac.service.EntePublicoService;
import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.*;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CuentaMaestraServiceImp implements CuentaMaestraService {

    @Autowired
    private NumTelefonoCuentaMaestraRepository numTelefonoCuentaMaestraRepository;

    @Autowired
    private CatTipoConexionLineaTelefRepository catTipoConexionLineaTelefRepository;

    @Autowired
    private CatConceptoCuentaMaestraRepository catConceptoCuentaMaestraRepository;

    @Autowired
    private CatCuentaMaestraRepository catCuentaMaestraRepository;

    @Autowired
    private EntePublicoService entePublicoService;

    @Autowired
    @Qualifier("unidadAdministrativaRepositorySai")
    private UnidadAdministrativaRepository unidadAdministrativaRepository;

    @Autowired
    @Qualifier("entePublicoRepositorySai")
    private EntePublicoRepository entePublicoRepositorySai;

    @Autowired
    private UbicacionUnidadAdminSaiRepository ubicacionUnidadAdminSaiRepository;

    @Autowired
    @Qualifier("catUsuarioEntePublicoRepositorySai")
    private CatUsuarioEntePublicoRepository catUsuarioEntePublicoRepository;

    private boolean tieneRolAdminSigtic(UserDetailsSesion authUser) {
        if (authUser.getListaPerfil() == null)
            return false;
        return authUser.getListaPerfil().stream()
                .anyMatch(p -> "ADMIN_SIGTIC".equalsIgnoreCase(p.getNombrePerfil()));
    }

    private String descEstatus(String cveEstatus) {
        if (cveEstatus == null)
            return "";
        switch (cveEstatus) {
            case "VIG":
                return "Vigente";
            case "NOV":
                return "No Vigente";
            default:
                return cveEstatus;
        }
    }

    private String descEstatusValidacion(String estatusValidacion) {
        if (estatusValidacion == null)
            return "";
        switch (estatusValidacion) {
            case "C":
                return "Captura";
            case "E":
                return "Enviado";
            case "V":
                return "Validado";
            case "R":
                return "Rechazado";
            default:
                return estatusValidacion;
        }
    }

    private List<String> resolverListaEstatus(CuentaMaestraFiltro filtro) {
        List<String> listaEstatus = new ArrayList<>();
        if (Boolean.TRUE.equals(filtro.getIsVigente()))
            listaEstatus.add("VIG");
        if (Boolean.TRUE.equals(filtro.getIsHistorico()))
            listaEstatus.add("NOV");

        if (listaEstatus.isEmpty()) {
            listaEstatus.add("VIG");
            listaEstatus.add("NOV");
        }
        return listaEstatus;
    }

    @Override
    public List<NumTelefonoCuentaMaestra> obtenerTelefonos(CuentaMaestraFiltro filtro) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> listaEstatus = resolverListaEstatus(filtro);

        if (authUser.isAdministradorSigtic()) {
            return numTelefonoCuentaMaestraRepository.getTelefonosFiltrados(
                    filtro.getIdEntePublico(),
                    filtro.getIdUnidadAdministrativa(),
                    filtro.getCveTipoConexion(),
                    listaEstatus);
        } else {
            List<mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico> listaEntePublico = entePublicoService.getEntesPublicos(authUser.getUsername());
            List<Long> listaIdEntePublico = listaEntePublico.stream()
                    .map(mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico::getIdEntePublico)
                    .collect(Collectors.toList());

            if (listaIdEntePublico.isEmpty())
                return new ArrayList<>();

            if (filtro.getIdEntePublico() != null && listaIdEntePublico.contains(filtro.getIdEntePublico()))
                listaIdEntePublico = Collections.singletonList(filtro.getIdEntePublico());

            return numTelefonoCuentaMaestraRepository.getTelefonosFiltradosPorListaEnte(
                    listaIdEntePublico,
                    filtro.getIdUnidadAdministrativa(),
                    filtro.getCveTipoConexion(),
                    listaEstatus);
        }
    }

    @Override
    public NumTelefonoCuentaMaestra getTelefono(String numTelefono, String numCuentaMaestra) {
        NumTelefonoCuentaMaestraId id = new NumTelefonoCuentaMaestraId(numTelefono, numCuentaMaestra);
        return numTelefonoCuentaMaestraRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("El registro de la línea telefónica no existe"));
    }

    @Override
    public NumTelefonoCuentaMaestra guardarTelefono(NumTelefonoCuentaMaestra telefono, boolean esNuevo, String numTelefonoOriginal, String numCuentaMaestraOriginal) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (telefono.getNumTelefono() == null || telefono.getNumTelefono().trim().isEmpty())
            throw new NoSuchElementException("El campo Número de Teléfono es obligatorio");
        if (telefono.getEntePublico() == null || telefono.getEntePublico().getIdEntePublico() == null)
            throw new NoSuchElementException("El campo Dependencia es obligatorio");
        if (telefono.getUnidadAdministrativa() == null || telefono.getUnidadAdministrativa().getIdUnidadAdministrativa() == null)
            throw new NoSuchElementException("El campo Unidad Administrativa es obligatorio");
        if (telefono.getCatTipoConexionLineaTelef() == null || telefono.getCatTipoConexionLineaTelef().getCveTipoConexionLineaTelef() == null || telefono.getCatTipoConexionLineaTelef().getCveTipoConexionLineaTelef().trim().isEmpty())
            throw new NoSuchElementException("El campo Tipo de Conexión es obligatorio");
        if (telefono.getCatCuentaMaestra() == null || telefono.getCatCuentaMaestra().getNumCuentaMaestra() == null || telefono.getCatCuentaMaestra().getNumCuentaMaestra().trim().isEmpty())
            throw new NoSuchElementException("El campo Num Cuenta Maestra es obligatorio");
        if (telefono.getUbicacionUnidadAdmin() == null || telefono.getUbicacionUnidadAdmin().getIdUbicacionAdmtva() == null)
            throw new NoSuchElementException("El campo Ubicación es obligatorio");
        if (telefono.getCurpUsuarioContacto() == null || telefono.getCurpUsuarioContacto().trim().isEmpty())
            throw new NoSuchElementException("El campo Usuario de Contacto es obligatorio");

        EntePublico entePublico = entePublicoRepositorySai
                .findById(telefono.getEntePublico().getIdEntePublico())
                .orElseThrow(() -> new NoSuchElementException("El Ente Público no existe"));

        UnidadAdministrativa unidadAdministrativa = unidadAdministrativaRepository
                .findById(telefono.getUnidadAdministrativa().getIdUnidadAdministrativa())
                .orElseThrow(() -> new NoSuchElementException("La Unidad Administrativa no existe"));

        UbicacionUnidadAdmin ubicacionUnidadAdmin = ubicacionUnidadAdminSaiRepository
                .findById(telefono.getUbicacionUnidadAdmin().getIdUbicacionAdmtva())
                .orElseThrow(() -> new NoSuchElementException("La Ubicación no existe"));

        CatTipoConexionLineaTelef catTipoConexionLineaTelef = catTipoConexionLineaTelefRepository
                .findById(telefono.getCatTipoConexionLineaTelef().getCveTipoConexionLineaTelef())
                .orElseThrow(() -> new NoSuchElementException("El Tipo de Conexión no existe"));

        CatCuentaMaestra catCuentaMaestra = catCuentaMaestraRepository
                .findById(telefono.getCatCuentaMaestra().getNumCuentaMaestra())
                .orElseThrow(() -> new NoSuchElementException("La Cuenta Maestra no existe"));

        NumTelefonoCuentaMaestra registro;

        if (!esNuevo) {

            NumTelefonoCuentaMaestraId idOriginal = new NumTelefonoCuentaMaestraId(numTelefonoOriginal, numCuentaMaestraOriginal);
            registro = numTelefonoCuentaMaestraRepository.findById(idOriginal)
                    .orElseThrow(() -> new NoSuchElementException("El registro de la línea telefónica no existe"));

            boolean esCapturaORechazado = "C".equals(registro.getEstatusValidacion()) || "R".equals(registro.getEstatusValidacion());
            if (!esCapturaORechazado && !tieneRolAdminSigtic(authUser))
                throw new NoSuchElementException("Solo el Administrador SIGTIC puede editar registros en estatus Validado o Enviado");
        } else {
            NumTelefonoCuentaMaestraId idNuevo = new NumTelefonoCuentaMaestraId(telefono.getNumTelefono(), catCuentaMaestra.getNumCuentaMaestra());

            if (numTelefonoCuentaMaestraRepository.findById(idNuevo).isPresent())
                throw new NoSuchElementException("Ya existe una línea con ese Número de Teléfono para la Cuenta Maestra seleccionada");

            registro = new NumTelefonoCuentaMaestra();
            registro.setId(idNuevo);
            registro.setCatCuentaMaestra(catCuentaMaestra);
            registro.setFechaRegistro(new Timestamp(new Date().getTime()));
            registro.setFechaAlta(new Timestamp(new Date().getTime()));
            registro.setEstatusValidacion("C");
            if (telefono.getCveEstatus() == null || telefono.getCveEstatus().isEmpty())
                telefono.setCveEstatus("VIG");
        }

        registro.setEntePublico(entePublico);
        registro.setUnidadAdministrativa(unidadAdministrativa);
        registro.setUbicacionUnidadAdmin(ubicacionUnidadAdmin);
        registro.setCatTipoConexionLineaTelef(catTipoConexionLineaTelef);
        registro.setCurpUsuarioContacto(telefono.getCurpUsuarioContacto());
        registro.setAnchoBandaInternetMb(telefono.getAnchoBandaInternetMb());
        registro.setFechaBaja(telefono.getFechaBaja());
        registro.setObservaciones(telefono.getObservaciones());

        if (telefono.getDescValidacionTelefono() != null && !telefono.getDescValidacionTelefono().isEmpty()) {
            registro.setDescValidacionTelefono(telefono.getDescValidacionTelefono());
            registro.setCurpUsuarioValidaTelefono(authUser.getRfc());
        }

        if (telefono.getCveEstatus() != null && !telefono.getCveEstatus().isEmpty())
            registro.setCveEstatus(telefono.getCveEstatus());

        registro.setCveUsuarioMtto(authUser.getUsername());
        registro.setNombreAplicacion("SIGTIC");
        registro.setFechaUltimoCambio(new Timestamp(new Date().getTime()));

        return numTelefonoCuentaMaestraRepository.save(registro);
    }

    @Override
    public NumTelefonoCuentaMaestra eliminarTelefono(String numTelefono, String numCuentaMaestra) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        NumTelefonoCuentaMaestraId id = new NumTelefonoCuentaMaestraId(numTelefono, numCuentaMaestra);
        NumTelefonoCuentaMaestra registro = numTelefonoCuentaMaestraRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("El registro de la línea telefónica no existe"));

        registro.setCveEstatus("NOV");
        registro.setCveUsuarioMtto(authUser.getUsername());
        registro.setFechaUltimoCambio(new Timestamp(new Date().getTime()));

        return numTelefonoCuentaMaestraRepository.save(registro);
    }

    @Override
    public NumTelefonoCuentaMaestra enviarTelefono(String numTelefono, String numCuentaMaestra) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        NumTelefonoCuentaMaestraId id = new NumTelefonoCuentaMaestraId(numTelefono, numCuentaMaestra);
        NumTelefonoCuentaMaestra registro = numTelefonoCuentaMaestraRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("El registro de la línea telefónica no existe"));

        if (!"C".equals(registro.getEstatusValidacion()) && !"R".equals(registro.getEstatusValidacion()))
            throw new NoSuchElementException("Solo se pueden enviar registros en estatus Captura o Rechazado");

        registro.setEstatusValidacion("E");
        registro.setCveUsuarioMtto(authUser.getUsername());
        registro.setFechaUltimoCambio(new Timestamp(new Date().getTime()));

        return numTelefonoCuentaMaestraRepository.save(registro);
    }

    @Override
    public NumTelefonoCuentaMaestra validarTelefono(String numTelefono, String numCuentaMaestra, String descValidacionTelefono) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        NumTelefonoCuentaMaestraId id = new NumTelefonoCuentaMaestraId(numTelefono, numCuentaMaestra);
        NumTelefonoCuentaMaestra registro = numTelefonoCuentaMaestraRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("El registro de la línea telefónica no existe"));

        if (!"E".equals(registro.getEstatusValidacion()))
            throw new NoSuchElementException("Solo se pueden validar registros en estatus Enviado");

        registro.setDescValidacionTelefono(descValidacionTelefono);
        registro.setCurpUsuarioValidaTelefono(authUser.getRfc());
        registro.setEstatusValidacion("V");
        registro.setCveUsuarioMtto(authUser.getUsername());
        registro.setFechaUltimoCambio(new Timestamp(new Date().getTime()));

        return numTelefonoCuentaMaestraRepository.save(registro);
    }

    @Override
    public NumTelefonoCuentaMaestra rechazarTelefono(String numTelefono, String numCuentaMaestra) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        NumTelefonoCuentaMaestraId id = new NumTelefonoCuentaMaestraId(numTelefono, numCuentaMaestra);
        NumTelefonoCuentaMaestra registro = numTelefonoCuentaMaestraRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("El registro de la línea telefónica no existe"));

        if (!"E".equals(registro.getEstatusValidacion()))
            throw new NoSuchElementException("Solo se pueden rechazar registros en estatus Enviado");

        registro.setEstatusValidacion("R");
        registro.setCveUsuarioMtto(authUser.getUsername());
        registro.setFechaUltimoCambio(new Timestamp(new Date().getTime()));

        return numTelefonoCuentaMaestraRepository.save(registro);
    }

    @Override
    public void generarReporteExcel(CuentaMaestraFiltro filtro, HttpServletResponse response) {

        List<NumTelefonoCuentaMaestra> lista = obtenerTelefonos(filtro);

        try {
            SXSSFWorkbook workbook = new SXSSFWorkbook(100);
            SXSSFSheet sheet = workbook.createSheet("Cuenta Maestra");

            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            String[] encabezados = {
                    "Dependencia", "Unidad Administrativa", "Número Teléfono",
                    "Num Cuenta Maestra", "Tipo de Conexión", "Ubicación",
                    "Concepto Cuenta Maestra", "Usuario de Contacto (CURP)",
                    "Ancho de Banda (Mb)", "Fecha de Alta", "Fecha de Baja",
                    "Estatus", "Estado de Validación", "Descripción de Validación",
                    "Usuario que Validó (RFC)", "Observaciones"
            };

            Row headerRow = sheet.createRow(0);
            for (int c = 0; c < encabezados.length; c++) {
                Cell cell = headerRow.createCell(c);
                cell.setCellValue(encabezados[c]);
                cell.setCellStyle(headerStyle);
            }

            int i = 1;
            for (NumTelefonoCuentaMaestra reg : lista) {
                Row row = sheet.createRow(i++);

                row.createCell(0).setCellValue(
                        reg.getEntePublico() != null ? reg.getEntePublico().getNombreEntePublico() : "");
                row.createCell(1).setCellValue(
                        reg.getUnidadAdministrativa() != null ? reg.getUnidadAdministrativa().getNombreUnidadAdministrativa() : "");
                row.createCell(2).setCellValue(
                        reg.getNumTelefono() != null ? reg.getNumTelefono() : "");
                row.createCell(3).setCellValue(
                        reg.getCatCuentaMaestra() != null ? reg.getCatCuentaMaestra().getNumCuentaMaestra() : "");
                row.createCell(4).setCellValue(
                        reg.getCatTipoConexionLineaTelef() != null ? reg.getCatTipoConexionLineaTelef().getNombreTipoConexionLineaTelef() : "");
                row.createCell(5).setCellValue(
                        reg.getUbicacionUnidadAdmin() != null ? reg.getUbicacionUnidadAdmin().getDescUbicacion() : "");
                row.createCell(6).setCellValue(
                        reg.getCatConceptoCuentaMaestra() != null ? reg.getCatConceptoCuentaMaestra().getNombreConceptoCuentaMaestra() : "");
                row.createCell(7).setCellValue(
                        reg.getCurpUsuarioContacto() != null ? reg.getCurpUsuarioContacto() : "");
                row.createCell(8).setCellValue(
                        reg.getAnchoBandaInternetMb() != null ? reg.getAnchoBandaInternetMb() : 0);
                row.createCell(9).setCellValue(reg.getFechaAltaText() != null ? reg.getFechaAltaText() : "");
                row.createCell(10).setCellValue(reg.getFechaBajaText() != null ? reg.getFechaBajaText() : "");
                row.createCell(11).setCellValue(descEstatus(reg.getCveEstatus()));
                row.createCell(12).setCellValue(descEstatusValidacion(reg.getEstatusValidacion()));
                row.createCell(13).setCellValue(
                        reg.getDescValidacionTelefono() != null ? reg.getDescValidacionTelefono() : "");
                row.createCell(14).setCellValue(
                        reg.getCurpUsuarioValidaTelefono() != null ? reg.getCurpUsuarioValidaTelefono() : "");
                row.createCell(15).setCellValue(
                        reg.getObservaciones() != null ? reg.getObservaciones() : "");
            }

            String filename = "cuenta_maestra_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.setHeader("Pragma", "No-cache");
            workbook.write(response.getOutputStream());
            workbook.dispose();
            workbook.close();

        } catch (IOException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    @Override
    public List<CatTipoConexionLineaTelef> getTiposConexion() {
        return catTipoConexionLineaTelefRepository.findAllVigentes();
    }

    @Override
    public List<CatConceptoCuentaMaestra> getConceptosCuentaMaestra() {
        return catConceptoCuentaMaestraRepository.findAllVigentes();
    }

    @Override
    public List<CatCuentaMaestra> getCuentasMaestras(Long idEntePublico) {
        return catCuentaMaestraRepository.findAllVigentesPorEnte(idEntePublico);
    }

    @Override
    public List<UsuarioContactoProjection> getUsuariosContactoPorEnte(Long idEntePublico) {
        return catUsuarioEntePublicoRepository.findUsuariosContactoPorEnte(idEntePublico, "VIG");
    }
}
