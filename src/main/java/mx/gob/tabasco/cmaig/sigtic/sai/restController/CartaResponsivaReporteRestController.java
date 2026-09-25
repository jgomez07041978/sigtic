package mx.gob.tabasco.cmaig.sigtic.sai.restController;


import mx.gob.tabasco.cmaig.sigtic.sai.service.CartaResponsivaService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping
public class CartaResponsivaReporteRestController {

    @Autowired
    private CartaResponsivaService cartaResponsivaService;

    @GetMapping("/getCrPdf/{id}")
    public ResponseEntity<byte[]> getReporte(@PathVariable("id") Long id) throws JRException, IOException {
        return  cartaResponsivaService.getReporteCrAceptada(id);
    }


}
