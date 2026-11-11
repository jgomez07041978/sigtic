package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.*;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.ServicioEspecialRepository.CatalogoItem;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import mx.gob.tabasco.cmaig.sigtic.sac.service.EntePublicoService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ServicioEspecialServiceImp implements ServicioEspecialService {

    private static final int DESC_ABREVIADA_MIN = 100;
    private static final int DESC_ABREVIADA_MAX = 240;

    @Autowired
    private ServicioEspecialRepository servicioEspecialRepository;

    @Autowired
    private CatTipoConexionLineaTelefRepository catTipoConexionLineaTelefRepository;

    @Autowired
    private CatCuentaMaestraRepository catCuentaMaestraRepository;

    @Autowired
    @Qualifier("unidadAdministrativaRepositorySai")
    private UnidadAdministrativaRepository unidadAdministrativaRepository;

    @Autowired
    @Qualifier("entePublicoRepositorySai")
    private EntePublicoRepository entePublicoRepositorySai;

    @Autowired
    private EntePublicoService entePublicoService;

    @Override
    public ServicioEspecial getServicioEspecial(String numCuentaMaestra, Integer noLote) {
        ServicioEspecialId id = new ServicioEspecialId(numCuentaMaestra, noLote);
        return servicioEspecialRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("El registro del servicio especial no existe"));
    }

    @Override
    public ServicioEspecial guardarServicioEspecial(ServicioEspecial datos, boolean esNuevo,
                                                    Integer noLoteOriginal, String numCuentaMaestraOriginal) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        validar(datos);

        EntePublico entePublico = entePublicoRepositorySai
                .findById(datos.getEntePublico().getIdEntePublico())
                .orElseThrow(() -> new NoSuchElementException("El Ente Público no existe"));

        UnidadAdministrativa unidadAdministrativa = unidadAdministrativaRepository
                .findById(datos.getUnidadAdministrativa().getIdUnidadAdministrativa())
                .orElseThrow(() -> new NoSuchElementException("La Unidad Administrativa no existe"));

        CatTipoConexionLineaTelef catTipoConexionLineaTelef = catTipoConexionLineaTelefRepository
                .findById(datos.getCatTipoConexionLineaTelef().getCveTipoConexionLineaTelef())
                .orElseThrow(() -> new NoSuchElementException("El Tipo de Conexión no existe"));

        ServicioEspecial registro;

        if (!esNuevo) {
            registro = getServicioEspecial(numCuentaMaestraOriginal, noLoteOriginal);

            // La Cuenta Maestra es parte de la llave: no se puede cambiar
            if (!numCuentaMaestraOriginal.equals(datos.getCatCuentaMaestra().getNumCuentaMaestra()))
                throw new NoSuchElementException("La Cuenta Maestra no se puede modificar");
        } else {
            String numCuenta = datos.getCatCuentaMaestra().getNumCuentaMaestra();

            // La cuenta debe existir, estar vigente y pertenecer a la Dependencia elegida
            CatCuentaMaestra catCuentaMaestra = catCuentaMaestraRepository
                    .findAllVigentesPorEnte(entePublico.getIdEntePublico()).stream()
                    .filter(c -> numCuenta.equals(c.getNumCuentaMaestra()))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("La Cuenta Maestra no existe o no pertenece a la Dependencia seleccionada"));

            int siguienteLote = servicioEspecialRepository.maxNoLote(catCuentaMaestra.getNumCuentaMaestra()) + 1;

            registro = new ServicioEspecial();
            registro.setId(new ServicioEspecialId(catCuentaMaestra.getNumCuentaMaestra(), siguienteLote));
            registro.setCatCuentaMaestra(catCuentaMaestra);
            registro.setFechaRegistro(new Timestamp(new Date().getTime()));
            registro.setEstatusValidacion("C");
            registro.setCveEstatus("VIG");
        }

        registro.setEntePublico(entePublico);
        registro.setUnidadAdministrativa(unidadAdministrativa);
        registro.setCatTipoConexionLineaTelef(catTipoConexionLineaTelef);
        registro.setPeriodo(datos.getPeriodo().withDayOfMonth(1));
        registro.setCvePartida(datos.getCvePartida());
        registro.setCodigoEbs(datos.getCodigoEbs());
        registro.setCantidad(datos.getCantidad());
        registro.setCveUnidadMedida(datos.getCveUnidadMedida());
        registro.setPrecioConIva(datos.getPrecioConIva().setScale(2, RoundingMode.HALF_UP));
        registro.setTotal(registro.getPrecioConIva()
                .multiply(BigDecimal.valueOf(datos.getCantidad()))
                .setScale(2, RoundingMode.HALF_UP));
        registro.setNumOficio(vacioANull(datos.getNumOficio()));
        registro.setDescripcionAbreviada(datos.getDescripcionAbreviada().trim());
        registro.setDescripcion(vacioANull(datos.getDescripcion()));

        registro.setCveUsuarioMtto(authUser.getUsername());
        registro.setNombreAplicacion("SIGTIC");
        registro.setFechaUltimoCambio(new Timestamp(new Date().getTime()));

        return servicioEspecialRepository.save(registro);
    }

    @Override
    public List<CatalogoItem> getPartidas() {
        return servicioEspecialRepository.findPartidas();
    }

    @Override
    public List<CatalogoItem> getArticulosPorPartida(String cvePartida) {
        return servicioEspecialRepository.findArticulosPorPartida(cvePartida);
    }

    @Override
    public List<CatalogoItem> getUnidadesMedida() {
        return servicioEspecialRepository.findUnidadesMedida();
    }

    @Override
    public List<CatalogoItem> getTodosArticulos() {
        return servicioEspecialRepository.findTodosArticulos();
    }

    // Reportes
    private List<String> resolverListaEstatus(ServicioEspecialFiltro filtro) {
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
    public List<ServicioEspecial> obtenerServicios(ServicioEspecialFiltro filtro) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Long> entesPermitidos = null;
        if (!authUser.isAdministradorSigtic()) {
            entesPermitidos = entePublicoService.getEntesPublicos(authUser.getUsername()).stream()
                    .map(mx.gob.tabasco.cmaig.sigtic.sac.model.EntePublico::getIdEntePublico)
                    .collect(Collectors.toList());

            if (entesPermitidos.isEmpty())
                return new ArrayList<>();

            if (filtro.getIdEntePublico() != null) {
                if (!entesPermitidos.contains(filtro.getIdEntePublico()))
                    return new ArrayList<>();
                entesPermitidos = Collections.singletonList(filtro.getIdEntePublico());
            }
        }

        final List<Long> enteRestringido = entesPermitidos;
        final List<String> listaEstatus = resolverListaEstatus(filtro);

        // Filtros dinámicos
        Specification<ServicioEspecial> spec = (root, query, cb) -> {
            List<Predicate> predicados = new ArrayList<>();

            predicados.add(root.get("cveEstatus").in(listaEstatus));

            if (enteRestringido != null)
                predicados.add(root.get("entePublico").get("idEntePublico").in(enteRestringido));
            else if (filtro.getIdEntePublico() != null)
                predicados.add(cb.equal(root.get("entePublico").get("idEntePublico"), filtro.getIdEntePublico()));

            if (filtro.getIdUnidadAdministrativa() != null)
                predicados.add(cb.equal(root.get("unidadAdministrativa").get("idUnidadAdministrativa"), filtro.getIdUnidadAdministrativa()));

            if (vacioANull(filtro.getNumCuentaMaestra()) != null)
                predicados.add(cb.equal(root.get("id").get("numCuentaMaestra"), filtro.getNumCuentaMaestra().trim()));

            if (vacioANull(filtro.getCveTipoConexion()) != null)
                predicados.add(cb.equal(root.get("catTipoConexionLineaTelef").get("cveTipoConexionLineaTelef"), filtro.getCveTipoConexion().trim()));

            if (vacioANull(filtro.getCvePartida()) != null)
                predicados.add(cb.equal(root.get("cvePartida"), filtro.getCvePartida().trim()));

            if (vacioANull(filtro.getCodigoEbs()) != null)
                predicados.add(cb.equal(root.get("codigoEbs"), filtro.getCodigoEbs().trim()));

            if (filtro.getPeriodoInicio() != null)
                predicados.add(cb.greaterThanOrEqualTo(root.<java.time.LocalDate>get("periodo"), filtro.getPeriodoInicio().withDayOfMonth(1)));

            if (filtro.getPeriodoFin() != null)
                predicados.add(cb.lessThanOrEqualTo(root.<java.time.LocalDate>get("periodo"), filtro.getPeriodoFin().withDayOfMonth(1)));

            return cb.and(predicados.toArray(new Predicate[0]));
        };

        return servicioEspecialRepository.findAll(spec, Sort.by(
                "entePublico.nombreEntePublico",
                "unidadAdministrativa.nombreUnidadAdministrativa",
                "id.numCuentaMaestra",
                "id.noLote"));
    }

    private Map<String, String> catalogoAMapa(List<CatalogoItem> lista) {
        return lista.stream().collect(Collectors.toMap(CatalogoItem::getClave, CatalogoItem::getDescripcion, (a, b) -> a));
    }

    private String descEstatus(String cveEstatus) {
        if (cveEstatus == null) return "";
        switch (cveEstatus) {
            case "VIG":
                return "Vigente";
            case "NOV":
                return "No Vigente";
            default:
                return cveEstatus;
        }
    }
    @Override
    public List<ServicioEspecialReporteDTO> getFilasReporte(ServicioEspecialFiltro filtro) {

        List<ServicioEspecial> lista = obtenerServicios(filtro);

        Map<String, String> partidas = catalogoAMapa(servicioEspecialRepository.findPartidas());
        Map<String, String> articulos = catalogoAMapa(servicioEspecialRepository.findTodosArticulos());
        DateTimeFormatter formatoPeriodo = DateTimeFormatter.ofPattern("MM/yyyy");

        List<ServicioEspecialReporteDTO> filas = new ArrayList<>();
        for (ServicioEspecial reg : lista) {
            ServicioEspecialReporteDTO f = new ServicioEspecialReporteDTO();
            f.setDependencia(reg.getEntePublico() != null ? reg.getEntePublico().getNombreEntePublico() : "");
            f.setUnidadAdministrativa(reg.getUnidadAdministrativa() != null ? reg.getUnidadAdministrativa().getNombreUnidadAdministrativa() : "");
            f.setCuentaMaestra(reg.getId() != null ? reg.getId().getNumCuentaMaestra() : "");
            f.setLote(reg.getNoLote());
            f.setPeriodo(reg.getPeriodo() != null ? reg.getPeriodo().format(formatoPeriodo) : "");
            f.setPartida(partidas.getOrDefault(reg.getCvePartida(), reg.getCvePartida() != null ? reg.getCvePartida() : ""));
            f.setArticulo(articulos.getOrDefault(reg.getCodigoEbs(), reg.getCodigoEbs() != null ? reg.getCodigoEbs() : ""));
            f.setCantidad(reg.getCantidad());
            f.setPrecioConIva(reg.getPrecioConIva() != null ? reg.getPrecioConIva() : BigDecimal.ZERO);
            f.setTotal(reg.getTotal() != null ? reg.getTotal() : BigDecimal.ZERO);
            f.setEstatus(descEstatus(reg.getCveEstatus()));
            filas.add(f);
        }
        return filas;
    }
    @Override
    public void generarReporteExcel(ServicioEspecialFiltro filtro, HttpServletResponse response) {

        List<ServicioEspecial> lista = obtenerServicios(filtro);

        // Nombres de catálogo
        Map<String, String> partidas = catalogoAMapa(servicioEspecialRepository.findPartidas());
        Map<String, String> articulos = catalogoAMapa(servicioEspecialRepository.findTodosArticulos());
        Map<String, String> unidades = catalogoAMapa(servicioEspecialRepository.findUnidadesMedida());

        try {
            SXSSFWorkbook workbook = new SXSSFWorkbook(100);
            SXSSFSheet sheet = workbook.createSheet("Servicios Especiales");

            Font fontNegrita = workbook.createFont();
            fontNegrita.setBold(true);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(fontNegrita);

            CellStyle montoStyle = workbook.createCellStyle();
            montoStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

            CellStyle montoNegritaStyle = workbook.createCellStyle();
            montoNegritaStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
            montoNegritaStyle.setFont(fontNegrita);

            String[] encabezados = {
                    "Dependencia", "Unidad Administrativa", "Num Cuenta Maestra", "No. Lote",
                    "Tipo de Conexión", "Periodo", "Partida", "Artículo (Código EBS)",
                    "Cantidad", "Unidad de Medida", "Precio con IVA", "Total",
                    "No. de Oficio", "Descripción Abreviada", "Descripción", "Estatus"
            };

            Row headerRow = sheet.createRow(0);
            for (int c = 0; c < encabezados.length; c++) {
                Cell cell = headerRow.createCell(c);
                cell.setCellValue(encabezados[c]);
                cell.setCellStyle(headerStyle);
            }

            DateTimeFormatter formatoPeriodo = DateTimeFormatter.ofPattern("MM/yyyy");
            BigDecimal sumaTotal = BigDecimal.ZERO;
            int i = 1;

            for (ServicioEspecial reg : lista) {
                Row row = sheet.createRow(i++);

                row.createCell(0).setCellValue(
                        reg.getEntePublico() != null ? reg.getEntePublico().getNombreEntePublico() : "");
                row.createCell(1).setCellValue(
                        reg.getUnidadAdministrativa() != null ? reg.getUnidadAdministrativa().getNombreUnidadAdministrativa() : "");
                row.createCell(2).setCellValue(
                        reg.getId() != null ? reg.getId().getNumCuentaMaestra() : "");
                row.createCell(3).setCellValue(
                        reg.getNoLote() != null ? reg.getNoLote() : 0);
                row.createCell(4).setCellValue(
                        reg.getCatTipoConexionLineaTelef() != null ? reg.getCatTipoConexionLineaTelef().getNombreTipoConexionLineaTelef() : "");
                row.createCell(5).setCellValue(
                        reg.getPeriodo() != null ? reg.getPeriodo().format(formatoPeriodo) : "");
                row.createCell(6).setCellValue(
                        partidas.getOrDefault(reg.getCvePartida(), reg.getCvePartida() != null ? reg.getCvePartida() : ""));
                row.createCell(7).setCellValue(
                        articulos.getOrDefault(reg.getCodigoEbs(), reg.getCodigoEbs() != null ? reg.getCodigoEbs() : ""));
                row.createCell(8).setCellValue(
                        reg.getCantidad() != null ? reg.getCantidad() : 0);
                row.createCell(9).setCellValue(
                        unidades.getOrDefault(reg.getCveUnidadMedida(), reg.getCveUnidadMedida() != null ? reg.getCveUnidadMedida() : ""));

                Cell precio = row.createCell(10);
                precio.setCellValue(reg.getPrecioConIva() != null ? reg.getPrecioConIva().doubleValue() : 0);
                precio.setCellStyle(montoStyle);

                Cell total = row.createCell(11);
                total.setCellValue(reg.getTotal() != null ? reg.getTotal().doubleValue() : 0);
                total.setCellStyle(montoStyle);
                if (reg.getTotal() != null) sumaTotal = sumaTotal.add(reg.getTotal());

                row.createCell(12).setCellValue(reg.getNumOficio() != null ? reg.getNumOficio() : "");
                row.createCell(13).setCellValue(reg.getDescripcionAbreviada() != null ? reg.getDescripcionAbreviada() : "");
                row.createCell(14).setCellValue(reg.getDescripcion() != null ? reg.getDescripcion() : "");
                row.createCell(15).setCellValue(descEstatus(reg.getCveEstatus()));
            }

            Row filaTotal = sheet.createRow(i + 1);
            Cell etiqueta = filaTotal.createCell(10);
            etiqueta.setCellValue("Total general");
            etiqueta.setCellStyle(headerStyle);
            Cell suma = filaTotal.createCell(11);
            suma.setCellValue(sumaTotal.doubleValue());
            suma.setCellStyle(montoNegritaStyle);

            String filename = "servicios_especiales_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
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

    private void validar(ServicioEspecial s) {
        if (s == null)
            throw new NoSuchElementException("No se recibieron datos del servicio especial");
        if (s.getEntePublico() == null || s.getEntePublico().getIdEntePublico() == null)
            throw new NoSuchElementException("El campo Dependencia es obligatorio");
        if (s.getUnidadAdministrativa() == null || s.getUnidadAdministrativa().getIdUnidadAdministrativa() == null)
            throw new NoSuchElementException("El campo Unidad Administrativa es obligatorio");
        if (s.getCatCuentaMaestra() == null || vacioANull(s.getCatCuentaMaestra().getNumCuentaMaestra()) == null)
            throw new NoSuchElementException("El campo Num Cuenta Maestra es obligatorio");
        if (s.getCatTipoConexionLineaTelef() == null || vacioANull(s.getCatTipoConexionLineaTelef().getCveTipoConexionLineaTelef()) == null)
            throw new NoSuchElementException("El campo Tipo de Conexión es obligatorio");
        if (s.getPeriodo() == null)
            throw new NoSuchElementException("El campo Periodo es obligatorio");
        if (vacioANull(s.getCvePartida()) == null)
            throw new NoSuchElementException("El campo Partida es obligatorio");
        if (vacioANull(s.getCodigoEbs()) == null)
            throw new NoSuchElementException("El campo Artículo es obligatorio");
        if (servicioEspecialRepository.contarArticuloEnPartida(s.getCodigoEbs(), s.getCvePartida()) == 0)
            throw new NoSuchElementException("El Artículo no existe o no pertenece a la Partida seleccionada");
        if (vacioANull(s.getCveUnidadMedida()) == null)
            throw new NoSuchElementException("El campo Unidad de medida es obligatorio");
        if (s.getCantidad() == null || s.getCantidad() < 1)
            throw new NoSuchElementException("El campo Cantidad debe ser mayor a 0");
        if (s.getPrecioConIva() == null || s.getPrecioConIva().compareTo(BigDecimal.ZERO) <= 0)
            throw new NoSuchElementException("El campo Precio con IVA debe ser mayor a 0");

        String abreviada = vacioANull(s.getDescripcionAbreviada());
        if (abreviada == null)
            throw new NoSuchElementException("El campo Descripción Abreviada es obligatorio");
        if (abreviada.length() < DESC_ABREVIADA_MIN || abreviada.length() > DESC_ABREVIADA_MAX)
            throw new NoSuchElementException("La Descripción Abreviada debe tener entre "
                    + DESC_ABREVIADA_MIN + " y " + DESC_ABREVIADA_MAX + " caracteres");
        if (esTodoMayusculas(abreviada) || esTodoMayusculas(s.getDescripcion()))
            throw new NoSuchElementException("Los textos deben escribirse en altibajo (no todo en mayúsculas)");
    }

    private boolean esTodoMayusculas(String texto) {
        if (texto == null || texto.trim().isEmpty()) return false;
        boolean tieneLetras = texto.chars().anyMatch(Character::isLetter);
        return tieneLetras && texto.equals(texto.toUpperCase(Locale.ROOT));
    }

    private String vacioANull(String texto) {
        return (texto == null || texto.trim().isEmpty()) ? null : texto.trim();
    }
}