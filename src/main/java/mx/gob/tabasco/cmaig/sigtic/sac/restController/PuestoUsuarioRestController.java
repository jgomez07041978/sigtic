package mx.gob.tabasco.cmaig.sigtic.sac.restController;

import mx.gob.tabasco.cmaig.sigtic.sac.model.PuestoUsuario;
import mx.gob.tabasco.cmaig.sigtic.sac.service.PuestoUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo/puesto-usuario")
public class PuestoUsuarioRestController {

    @Autowired
    private PuestoUsuarioService puestoUsuarioService;

    @GetMapping(path = "/all/{idEntePublico}")
    public List<PuestoUsuario> getPuestoUsuarioAll(@PathVariable(value = "idEntePublico") Long idEntePublico){
        if (idEntePublico!=null)
            return puestoUsuarioService.listaPuestosPorEntePublico(idEntePublico);
        else
            return new ArrayList<>();
    }
}
