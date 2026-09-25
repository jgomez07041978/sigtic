package mx.gob.tabasco.cmaig.sigtic.sac.restController;


import mx.gob.tabasco.cmaig.sigtic.sac.model.SacCatUsuarioEntePublicoView;
import mx.gob.tabasco.cmaig.sigtic.sac.service.SacCatUsuarioEntePublicoViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/directorio")
public class SacCatUsuarioEntePublicoViewRestController {


    @Autowired
    private SacCatUsuarioEntePublicoViewService sacCatUsuarioEntePublicoViewService;

    @PostMapping("/filtrar")
    public ResponseEntity<?> getDirectoryUsers(@RequestBody SacCatUsuarioEntePublicoView sacCatUsuarioEntePublicoView){
        return ResponseEntity.ok(sacCatUsuarioEntePublicoViewService.getSearchDirectorio(sacCatUsuarioEntePublicoView));
    }

    @PostMapping("/filtrarUser")
    public ResponseEntity<?> getFiltrarUsers(@RequestBody SacCatUsuarioEntePublicoView sacCatUsuarioEntePublicoView){
        return ResponseEntity.ok(sacCatUsuarioEntePublicoViewService.getSearchUser(sacCatUsuarioEntePublicoView));
    }

}