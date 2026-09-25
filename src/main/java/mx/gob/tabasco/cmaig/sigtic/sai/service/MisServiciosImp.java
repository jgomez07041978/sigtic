package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.*;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegAccesoUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MisServiciosImp implements MisServicios{

    @Autowired
    private ServiciosTicRepository serviciosTicRepository;

    @Autowired
    private SegAccesoUsuarioService segAccesoUsuarioService;

    @Autowired
    private UnidadAdministrativaRepository unidadAdministrativaRepository;

    @Autowired
    private EntePublicoRepository entePublicoRepository;

    @Autowired
    @Qualifier("catUsuarioEntePublicoRepositorySai")
    private CatUsuarioEntePublicoRepository catUsuarioEntePublicoRepository;

    @Autowired
    private CatEstatusServicioRepository catEstatusServicioRepository;

    @Autowired
    private BitacoraMovimientoService bitacoraMovimientoService;

    @Autowired
    private CartaResponsivaService cartaResponsivaService;

    @Autowired
    private UsuarioServicioRepository usuarioServicioRepository;

    @Autowired
    private UsuarioServicioBeanRepository usuarioServicioBeanRepository;


    @Override
    public List<UsuarioServicio> getMisServicios() {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<String> listaEstatus = Arrays.asList("VIG","HIS");

        CatUsuarioEntePublico catUsuarioEntePublico = catUsuarioEntePublicoRepository.findById(authUser.getCatUsuarioEntePublico().getIdUsuarioEntePublico()).get();

        List<UsuarioServicio> listaServicios= usuarioServicioRepository.findAllByCurpUsuarioServicioAndCatUsuarioEntePublicoAndCveEstatusInOrderByIdUsuarioServicio(authUser.getUsername(),catUsuarioEntePublico, listaEstatus);

        return listaServicios;
        
    }

    @Override
    public List<UsuarioServicioBean> getMisServiciosBean() {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<UsuarioServicioBean> listaServicios= usuarioServicioBeanRepository.getMisServicios(authUser.getCatUsuarioEntePublico().getIdUsuarioEntePublico());

        return listaServicios;

    }

    @Override
    public ServiciosTic enviarServicio(ServiciosTic serviciosTic) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ServiciosTic serviciosTic1 = serviciosTicRepository.findById(serviciosTic.getIdServicioTic()).get();

       /* if (serviciosTic1.getCatEstatusSeguimiento().getIdEstatusSeguimiento().intValue()!=1 && serviciosTic1.getCatEstatusSeguimiento().getIdEstatusSeguimiento().intValue()!=4)
            throw new NoSuchElementException("El servicio ya no esta en captura o rechazado");

        CatEstatusServicio catEstatusSeguimiento = catEstatusSeguimientoRepository.findById(2).get();

        serviciosTic1.setCatEstatusSeguimiento(catEstatusSeguimiento);
        serviciosTic1.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        serviciosTic1.setCveUsuarioMtto(authUser.getUsername());

        bitacoraMovimientoService.guardarBitacoraMovimiento(serviciosTic1.getIdServicioTic(), "SERVICIOS_TIC", "Estatus de seguimiento Enviado");

        return serviciosTicRepository.save(serviciosTic1);*/
        return serviciosTic1;
    }

    @Override
    public List<UsuarioServicio> getServiciosSeguimiento() {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean existeRolAdminSeguimiento = authUser.getListaPerfil().stream().filter(p-> p.getNombrePerfil().equals("ADMIN_SEGUIMIENTO_SERVICIO")).findFirst().isPresent();

        List<String> listaEstatus = Arrays.asList("VIG","HIS");
        List<CatEstatusServicio> listaEstatusSeguimiento = new ArrayList<>();
        CatEstatusServicio catEstatusServicioEnviado =catEstatusServicioRepository.findById(2).get();
        CatEstatusServicio catEstatusServicioAtendido =catEstatusServicioRepository.findById(5).get();
        listaEstatusSeguimiento.add(catEstatusServicioEnviado);
        listaEstatusSeguimiento.add(catEstatusServicioAtendido);

        /*EntePublico entePublico = entePublicoRepository.findById(authUser.getIdEntePublicoAsignado()).get();

        if (existeRolAdminSeguimiento) {
            List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(authUser.getUsername(), authUser.getIdEntePublicoAsignado());
            if (listaUnidAcceso.size() > 0) {
                List<UnidadAdministrativa> listaUnidad = (List<UnidadAdministrativa>) unidadAdministrativaRepository.findAllById(listaUnidAcceso);

                List<ServiciosTic> listaServicios = serviciosTicRepository.findAllByEntePublicoOperaAndUnidadAdministrativaOperaInAndCveEstatusInAndCatEstatusSeguimientoInOrderByIdServicioTicDesc(entePublico, listaUnidad, listaEstatus, listaEstatusSeguimiento);

                List<Long> listaUser = listaServicios.stream().map(ServiciosTic::getIdUsuarioEntePublico).distinct().collect(Collectors.toList());
                List<CatUsuarioEntePublico> listaUsuarioEntePublico = (List<CatUsuarioEntePublico>) catUsuarioEntePublicoRepository.findAllById(listaUser);

                listaServicios.forEach(p -> {

                    Optional<CatUsuarioEntePublico> catUsuarioEntePublicoOp = listaUsuarioEntePublico.stream().filter(q -> q.getIdUsuarioEntePublico().longValue() == p.getIdUsuarioEntePublico().longValue()).findFirst();
                    if (catUsuarioEntePublicoOp.isPresent())
                        p.setCatUsuarioEntePublico(catUsuarioEntePublicoOp.get());
                });

                return listaServicios;
            }else
                throw new NoSuchElementException("El usuario no tiene asignado unidades de acceso");

        }else
            throw new NoSuchElementException("Este usuario no tiene el rol de seguimiento de servicios");*/

        return new ArrayList<>();
    }

    @Override
    public ServiciosTic rechazarServicio(ServiciosTic serviciosTic) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ServiciosTic serviciosTic1 = serviciosTicRepository.findById(serviciosTic.getIdServicioTic()).get();

        /*if (serviciosTic1.getCatEstatusSeguimiento().getIdEstatusSeguimiento().intValue()!=2)
            throw new NoSuchElementException("El servicio ya no esta en estatus de seguimiento de enviado");

        CatEstatusServicio catEstatusSeguimiento = catEstatusSeguimientoRepository.findById(4).get();

        serviciosTic1.setCatEstatusSeguimiento(catEstatusSeguimiento);
        serviciosTic1.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        serviciosTic1.setCveUsuarioMtto(authUser.getUsername());
        serviciosTic1.setNotaRechazo(serviciosTic.getNotaRechazo());

        bitacoraMovimientoService.guardarBitacoraMovimiento(serviciosTic1.getIdServicioTic(), "SERVICIOS_TIC", "Estatus de seguimiento de rechazado con nota:"+serviciosTic.getNotaRechazo());

        return serviciosTicRepository.save(serviciosTic1);*/

        return serviciosTic1;
    }

    @Transactional(value = "saiSigticTransactionManager")
    @Override
    public ServiciosTic autorizarServicio(ServiciosTic serviciosTic) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ServiciosTic serviciosTic1 = serviciosTicRepository.findById(serviciosTic.getIdServicioTic()).get();

        /*if (serviciosTic1.getCatEstatusSeguimiento().getIdEstatusSeguimiento().intValue()!=2)
            throw new NoSuchElementException("El servicio ya no esta en estatus de seguimiento de enviado");

        if (serviciosTic1.getNombreServicioTic()==null || serviciosTic1.getNombreServicioTic().equals(""))
            throw new NoSuchElementException("Capture los datos faltantes del servicio");

        CatEstatusServicio catEstatusSeguimiento = catEstatusSeguimientoRepository.findById(3).get();

        serviciosTic1.setCatEstatusSeguimiento(catEstatusSeguimiento);
        serviciosTic1.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        serviciosTic1.setCveUsuarioMtto(authUser.getUsername());

        bitacoraMovimientoService.guardarBitacoraMovimiento(serviciosTic1.getIdServicioTic(), "SERVICIOS_TIC", "Estatus de seguimiento de autorizado");

        ServiciosTic serviciosTic2= serviciosTicRepository.save(serviciosTic1);
        cartaResponsivaService.generarCartaResponsivaServicio(serviciosTic2);

        return serviciosTic2;*/

        return serviciosTic1;
    }

}
