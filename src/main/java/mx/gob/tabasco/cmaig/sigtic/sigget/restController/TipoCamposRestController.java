package mx.gob.tabasco.cmaig.sigtic.sigget.restController;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.TipoCampo;
import mx.gob.tabasco.cmaig.sigtic.sigget.service.TipoCamposService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tipo_campos")
public class TipoCamposRestController {

    @Autowired
    private TipoCamposService tipoCamposService;

    @GetMapping("/vigentes")
    public List<TipoCampo> getTipoCamposVigentes(){
        return tipoCamposService.getTipoCamposVigentes();
    }


}
