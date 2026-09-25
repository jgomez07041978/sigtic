package mx.gob.tabasco.cmaig.sigtic.sac.rhGob;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rh-gob")
public class RhGobRestController {

    @Autowired
    private RhGobService rhGobService;

    @GetMapping("/getToken")
    public RhGobToken obtenCargoCorreo() {

        RhGobToken rhGobToken = rhGobService.obtenerRhGobToken();

        return rhGobToken;
    }

    @GetMapping("/getEmpleados")
    public RhGobDatos1 obtenEmpleados() {

        RhGobDatos1 empleados = rhGobService.obtenerDatosEmpleados();

        return empleados;
    }

}
