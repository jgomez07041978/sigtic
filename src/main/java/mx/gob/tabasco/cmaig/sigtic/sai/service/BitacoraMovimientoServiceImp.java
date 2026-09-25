package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.Utileria.ClienteInfo;
import mx.gob.tabasco.cmaig.sigtic.sai.model.BitacoraMovimiento;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatUsuarioEntePublico;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.BitacoraMovimientoRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatUsuarioEntePublicoRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BitacoraMovimientoServiceImp implements BitacoraMovimientoService{

    @Autowired
    private BitacoraMovimientoRepository bitacoraMovimientoRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    @Qualifier("catUsuarioEntePublicoRepositorySai")
    private CatUsuarioEntePublicoRepository catUsuarioEntePublicoRepository;

    @Override
    public void guardarBitacoraMovimiento(Long idMovimiento, String tablaMovimiento, String descripcionMovimiento) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authUser != null && authUser.getIdUsuario()!=null){
            BitacoraMovimiento movimiento = new BitacoraMovimiento();
            movimiento.setIdMovimiento(idMovimiento);
            movimiento.setTablaMovimiento(tablaMovimiento);
            movimiento.setDescripcionMovimiento(descripcionMovimiento);
            movimiento.setUsuarioRealizo(authUser.getCatUsuarioEntePublico().getCatUsuario().getCurp());
            movimiento.setIdUsuario(authUser.getIdUsuario());
            movimiento.setFechaRegistro(new Timestamp(new Date().getTime()));
            movimiento.setIpRegistro(ClienteInfo.getClientIpAddr(request));
            bitacoraMovimientoRepository.save(movimiento);
        }else
            throw new NoSuchElementException("El usuario no esta logueado o su sesión ha finalizado por inactividad");
    }

    @Override
    public void guardarBitacoraMovimiento(Long idMovimiento, String tablaMovimiento, String descripcionMovimiento, Long idUsuarioEntePublico) {
        Optional<CatUsuarioEntePublico> catUsuarioEntePublicoOp = catUsuarioEntePublicoRepository.findById(idUsuarioEntePublico.longValue());

        if (catUsuarioEntePublicoOp.isPresent()){
            BitacoraMovimiento movimiento = new BitacoraMovimiento();
            movimiento.setIdMovimiento(idMovimiento);
            movimiento.setTablaMovimiento(tablaMovimiento);
            movimiento.setDescripcionMovimiento(descripcionMovimiento);
            movimiento.setUsuarioRealizo(catUsuarioEntePublicoOp.get().getCatUsuario().getCurp());
            movimiento.setIdUsuario(catUsuarioEntePublicoOp.get().getCatUsuario().getIdUsuario());
            movimiento.setFechaRegistro(new Timestamp(new Date().getTime()));
            movimiento.setIpRegistro(ClienteInfo.getClientIpAddr(request));
            bitacoraMovimientoRepository.save(movimiento);
        }else
            throw new NoSuchElementException("El usuario no existe o esta inactivo");
    }
}
