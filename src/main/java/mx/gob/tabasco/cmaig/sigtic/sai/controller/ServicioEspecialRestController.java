package mx.gob.tabasco.cmaig.sigtic.sai.controller;

import mx.gob.tabasco.cmaig.sigtic.sai.model.GuardarServicioEspecialRequest;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServicioEspecial;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServicioEspecialFiltro;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.ServicioEspecialRepository.CatalogoItem;
import mx.gob.tabasco.cmaig.sigtic.sai.service.ServicioEspecialService;
import org.springframework.web.bind.annotation.*;
import mx.gob.tabasco.cmaig.sigtic.Utileria.UtileriaReporte;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ServicioEspecialReporteDTO;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo/tic/servicioEspecial")
public class ServicioEspecialRestController {

    private final ServicioEspecialService servicioEspecialService;

    public ServicioEspecialRestController(ServicioEspecialService servicioEspecialService) {
        this.servicioEspecialService = servicioEspecialService;
    }

    @GetMapping("/get/{numCuentaMaestra}/{noLote}")
    public ServicioEspecial getServicioEspecial(@PathVariable("numCuentaMaestra") String numCuentaMaestra,
                                                @PathVariable("noLote") Integer noLote) {
        return servicioEspecialService.getServicioEspecial(numCuentaMaestra, noLote);
    }

    @PostMapping("/guardar")
    public ServicioEspecial guardar(@RequestBody GuardarServicioEspecialRequest request) {
        boolean esNuevo = "Nuevo".equalsIgnoreCase(request.getOpcion());
        return servicioEspecialService.guardarServicioEspecial(
                request.getServicioEspecial(),
                esNuevo,
                request.getNoLoteOriginal(),
                request.getNumCuentaMaestraOriginal());
    }

    @PostMapping("/exportar")
    public void exportar(@RequestBody ServicioEspecialFiltro filtro, HttpServletResponse response) {
        servicioEspecialService.generarReporteExcel(filtro, response);
    }

    @PostMapping("/exportarPdf")
    public ResponseEntity<byte[]> exportarPdf(@RequestBody ServicioEspecialFiltro filtro) {
        List<ServicioEspecialReporteDTO> filas = servicioEspecialService.getFilasReporte(filtro);
        return UtileriaReporte.jasperCreatePdf(
                "static/jasper/ServicioEspecialReporte.jrxml",
                new HashMap<>(),
                filas,
                "servicios_especiales.pdf");
    }

    @GetMapping("/getPartidas")
    public List<CatalogoItem> getPartidas() {
        return servicioEspecialService.getPartidas();
    }

    @GetMapping("/getArticulos")
    public List<CatalogoItem> getArticulos() {
        return servicioEspecialService.getTodosArticulos();
    }

    @GetMapping("/getArticulosPorPartida/{cvePartida}")
    public List<CatalogoItem> getArticulosPorPartida(@PathVariable("cvePartida") String cvePartida) {
        return servicioEspecialService.getArticulosPorPartida(cvePartida);
    }

    @GetMapping("/getUnidadesMedida")
    public List<CatalogoItem> getUnidadesMedida() {
        return servicioEspecialService.getUnidadesMedida();
    }
}