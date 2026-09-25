package mx.gob.tabasco.cmaig.sigtic.sas.restController;

import mx.gob.tabasco.cmaig.sigtic.Utileria.TokenUtils;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegUsuarioTemporal;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegUsuarioTemporalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/preRegistro")
public class PreRegistroRestController {

    @Autowired
    private SegUsuarioTemporalService segUsuarioTemporalService;

    @GetMapping("/searchUser/{curp}/{correo}")
    public SegUsuarioTemporal searchUser(@PathVariable("curp") String curp,@PathVariable("correo") String correo){
        String correoOriginal= TokenUtils.getCorreoToken(correo);

        if (correoOriginal==null)
            throw new NoSuchElementException("El registro no es valido");
        if (correoOriginal.equals("Expired"))
            throw new NoSuchElementException("El registro ya espiró");

        return segUsuarioTemporalService.searchUsuario(curp, correoOriginal);
    }

    @PostMapping("/guardarUsuario")
    public SegUsuarioTemporal guardarCatUsuario(@RequestBody SegUsuarioTemporal segUsuarioTemporal){

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.from(dateTimeFormatter.parse(segUsuarioTemporal.getFechaNacimientoText()+" 00:00:00"));
        //localDateTime= localDateTime.plusDays(1);
        segUsuarioTemporal.setFechaNacimiento(Timestamp.valueOf(localDateTime));

        return  segUsuarioTemporalService.guardarUsuario(segUsuarioTemporal);
    }

    @PostMapping("/actualizarUsuario")
    public SegUsuarioTemporal actualizarCatUsuario(@RequestBody SegUsuarioTemporal segUsuarioTemporal){

        return  segUsuarioTemporalService.actualizarUsuario(segUsuarioTemporal);
    }

    @PostMapping("/generandoUsuario")
    public SegUsuarioTemporal generandoUsuario(@RequestBody SegUsuarioTemporal segUsuarioTemporal){

        return  segUsuarioTemporalService.generandoUsuarios(segUsuarioTemporal.getIdUsuarioTemporal());
    }


}
