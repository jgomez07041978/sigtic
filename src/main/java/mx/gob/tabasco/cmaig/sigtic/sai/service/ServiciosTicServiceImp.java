package mx.gob.tabasco.cmaig.sigtic.sai.service;


import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.*;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegAccesoUsuarioService;
import net.minidev.json.JSONObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiciosTicServiceImp implements ServiciosTicService {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UsuarioServicioRepository usuarioRepository;

    @Autowired
    private CatTipoServiciosTicRepository catTipoServiciosTicRepository;

    @Autowired
    private  CatSubtipoServiciosTicRepository catSubtipoServiciosTicRepository;

    @Autowired
    private ServiciosTicRepository serviciosTicRepository;

    @Autowired
    private EntePublicoRepository entePublicoRepository;

    @Autowired
    private UnidadAdministrativaRepository unidadAdministrativaRepository;

    @Autowired
    private CatEstatusServiciosTicRepository catEstatusServiciosTicRepository;

    @Autowired
    private CatTipoImpactoServiciosTicRepository catTipoImpactoServiciosTicRepository;

    @Autowired
    private CatEstadosServiciosTicRepository catEstadosServiciosTicRepository;

    @Autowired
    private SegAccesoUsuarioService segAccesoUsuarioService;

    @Autowired
    private ServicioGroupBeanRepository servicioGroupBeanRepository;

    @Autowired
    private ServicioTableroBeanRepository servicioTableroBeanRepository;

    @Autowired
    private SubServicioSearchRepository subServicioSearchRepository;

    @Autowired
    private ServiciosTicRelacionadosRepository serviciosTicRelacionadosRepository;

    @Autowired
    @Qualifier("catUsuarioEntePublicoRepositorySai")
    private mx.gob.tabasco.cmaig.sigtic.sai.repository.CatUsuarioEntePublicoRepository catUsuarioEntePublicoRepositorySai;

    @Autowired
    private UsuarioServicioRepository usuarioServicioRepository;

    @Autowired
    private CatTipoUsuarioServicioRepository catTipoUsuarioServicioRepository;

    @Autowired
    private CatEstatusServicioRepository catEstatusServicioRepository;

    @Autowired
    private UbicacionUnidadAdminSaiRepository ubicacionUnidadAdminSaiRepository;

   @Autowired
    private  ServiciosTicService serviciosTicService;

    @Autowired
    private mx.gob.tabasco.cmaig.sigtic.sac.service.CatUsuarioService catUsuarioService;

    @Override
    public JSONObject getListaServiciosTablero(UnidadAdministrativa unidadAdministrativa){

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Properties props = new Properties();
        props.put("titulo", "Servicioas Administra");
        ArrayList<String> listEtiquetas = new ArrayList<String>();
        ArrayList<Integer> listValores = new ArrayList<Integer>();

        Properties props2 = new Properties();
        props2.put("titulo", "Servicios Opera");
        ArrayList<String> listEtiquetas2 = new ArrayList<String>();
        ArrayList<Integer> listValores2 = new ArrayList<Integer>();

        List<ServicioTableroBean> lista = null;

        if (authUser.isAdministradorSigtic()){

           if (unidadAdministrativa.getIdEntePublico()==null)
               lista = servicioTableroBeanRepository.getAllConcentradoTipoEnte();
           else
           if (unidadAdministrativa.getIdEntePublico()!=null && unidadAdministrativa.getIdUnidadAdministrativa()==null)
               lista = servicioTableroBeanRepository.getAllConcentradoTipoEntePorEnte(unidadAdministrativa.getIdEntePublico());
           else
           if (unidadAdministrativa.getIdEntePublico()!=null && unidadAdministrativa.getIdUnidadAdministrativa()!=null)
               lista = servicioTableroBeanRepository.getAllConcentradoTipoEntePorEnteUnidad(unidadAdministrativa.getIdEntePublico(), unidadAdministrativa.getIdUnidadAdministrativa());
           else
               lista=new ArrayList<>();
        }else{
            if (unidadAdministrativa.getIdEntePublico()==null)
                lista=new ArrayList<>();
            else {
                if (unidadAdministrativa.getIdEntePublico() != null && unidadAdministrativa.getIdUnidadAdministrativa() == null) {
                    List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(authUser.getUsername(),unidadAdministrativa.getIdEntePublico());
                    if (listaUnidAcceso.size()>0)
                        lista = servicioTableroBeanRepository.getAllConcentradoTipoEntePorEnteSubTipo(unidadAdministrativa.getIdEntePublico(), authUser.getIdUsuario(), listaUnidAcceso);
                    else
                        lista=new ArrayList<>();
                } else if (unidadAdministrativa.getIdEntePublico() != null && unidadAdministrativa.getIdUnidadAdministrativa() != null)
                    lista = servicioTableroBeanRepository.getAllConcentradoTipoEntePorUnidadSubTipo(unidadAdministrativa.getIdEntePublico(), authUser.getIdUsuario(),  unidadAdministrativa.getIdUnidadAdministrativa());
                else
                    lista=new ArrayList<>();
            }
        }

        int totalVigenteAdmin = lista.stream().mapToInt(ServicioTableroBean::getVigenteAdmin).sum();
        int totalHistoricoAdmin = lista.stream().mapToInt(ServicioTableroBean::getHistoricoAdmin).sum();
        int totalVigenteOpera = lista.stream().mapToInt(ServicioTableroBean::getVigenteOpera).sum();
        int totalHistoricoOpera = lista.stream().mapToInt(ServicioTableroBean::getHistoricoOpera).sum();

        int totalEncendidoAdmin = 0;
        int totalApagadoAdmin = 0;
        int totalEncendidoOpera = 0;
        int totalApagadoOpera = 0;

        List<ServicioTableroBean> listaEnte = null;
        List<ServicioTableroBean> listaService = null;
        List<ServicioTableroBean> listaSubService = null;

        if (authUser.isAdministradorSigtic()){

            if (unidadAdministrativa.getIdEntePublico()==null){
                listaEnte = servicioTableroBeanRepository.getAllConcentradoAllEnte();
                listaService = servicioTableroBeanRepository.getAllConcentradoAllService();
                listaSubService = servicioTableroBeanRepository.getAllConcentradoAllSubService();
                totalEncendidoAdmin = serviciosTicRepository.getCountAdminEstatusServicioTicAndCveEstatus("E");
                totalApagadoAdmin = serviciosTicRepository.getCountAdminEstatusServicioTicAndCveEstatus("A");
                totalEncendidoOpera = serviciosTicRepository.getCountOperaEstatusServicioTicAndCveEstatus("E");
                totalApagadoOpera = serviciosTicRepository.getCountOperaEstatusServicioTicAndCveEstatus("A");
            }else
            if (unidadAdministrativa.getIdEntePublico()!=null && unidadAdministrativa.getIdUnidadAdministrativa()==null){
                listaEnte = servicioTableroBeanRepository.getAllConcentradPorEnte(unidadAdministrativa.getIdEntePublico());
                listaService = servicioTableroBeanRepository.getAllConcentradoAllServicePorEnte(unidadAdministrativa.getIdEntePublico());
                listaSubService = servicioTableroBeanRepository.getAllConcentradoAllSubServicePorEnte(unidadAdministrativa.getIdEntePublico());
                totalEncendidoAdmin = serviciosTicRepository.getCountAdminEstatusServicioTicAndCveEstatusPorEnte(unidadAdministrativa.getIdEntePublico(), "E");
                totalApagadoAdmin = serviciosTicRepository.getCountAdminEstatusServicioTicAndCveEstatusPorEnte(unidadAdministrativa.getIdEntePublico(),"A");
                totalEncendidoOpera = serviciosTicRepository.getCountOperaEstatusServicioTicAndCveEstatusPorEnte(unidadAdministrativa.getIdEntePublico(),"E");
                totalApagadoOpera = serviciosTicRepository.getCountOperaEstatusServicioTicAndCveEstatusPorEnte(unidadAdministrativa.getIdEntePublico(),"A");
            }else
            if (unidadAdministrativa.getIdEntePublico()!=null && unidadAdministrativa.getIdUnidadAdministrativa()!=null){
                listaEnte = servicioTableroBeanRepository.getAllConcentradPorEnteUnidad(unidadAdministrativa.getIdEntePublico(), unidadAdministrativa.getIdUnidadAdministrativa());
                listaService = servicioTableroBeanRepository.getAllConcentradoAllServicePorEnteUnidad(unidadAdministrativa.getIdEntePublico(), unidadAdministrativa.getIdUnidadAdministrativa());
                listaSubService = servicioTableroBeanRepository.getAllConcentradoAllSubServicePorEnteUnidad(unidadAdministrativa.getIdEntePublico(), unidadAdministrativa.getIdUnidadAdministrativa());
                totalEncendidoAdmin = serviciosTicRepository.getCountAdminEstatusServicioTicAndCveEstatusPorEnteUnidad(unidadAdministrativa.getIdEntePublico(),unidadAdministrativa.getIdUnidadAdministrativa(),"E");
                totalApagadoAdmin = serviciosTicRepository.getCountAdminEstatusServicioTicAndCveEstatusPorEnteUnidad(unidadAdministrativa.getIdEntePublico(),unidadAdministrativa.getIdUnidadAdministrativa(),"A");
                totalEncendidoOpera = serviciosTicRepository.getCountOperaEstatusServicioTicAndCveEstatusPorEnteUnidad(unidadAdministrativa.getIdEntePublico(),unidadAdministrativa.getIdUnidadAdministrativa(),"A");
                totalApagadoOpera = serviciosTicRepository.getCountOperaEstatusServicioTicAndCveEstatusPorEnteUnidad(unidadAdministrativa.getIdEntePublico(),unidadAdministrativa.getIdUnidadAdministrativa(),"A");
            }else{
                listaEnte=new ArrayList<>();
                listaService=new ArrayList<>();
                listaSubService=new ArrayList<>();
            }

        }
        else{
            if (unidadAdministrativa.getIdEntePublico()==null){
                listaEnte=new ArrayList<>();
                listaService=new ArrayList<>();
                listaSubService=new ArrayList<>();
            }else
            if (unidadAdministrativa.getIdEntePublico()!=null && unidadAdministrativa.getIdUnidadAdministrativa()==null){
                List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(authUser.getUsername(),unidadAdministrativa.getIdEntePublico());
                if (listaUnidAcceso.size()>0) {
                    listaEnte = servicioTableroBeanRepository.getAllConcentradoAllEntePorEnteSubTipo(unidadAdministrativa.getIdEntePublico(), authUser.getIdUsuario(), listaUnidAcceso);
                    listaService = servicioTableroBeanRepository.getAllConcentradoAllServicePorEnteSubTipo(unidadAdministrativa.getIdEntePublico(), authUser.getIdUsuario(), listaUnidAcceso);
                    listaSubService = servicioTableroBeanRepository.getAllConcentradoAllSubServicePorEnteSubTipo(unidadAdministrativa.getIdEntePublico(), authUser.getIdUsuario(), listaUnidAcceso);
                    totalEncendidoAdmin = serviciosTicRepository.getCountAdminEstatusServicioTicAndCveEstatusPorEnteSubTipo(unidadAdministrativa.getIdEntePublico(), "E", authUser.getIdUsuario(),listaUnidAcceso);
                    totalApagadoAdmin = serviciosTicRepository.getCountAdminEstatusServicioTicAndCveEstatusPorEnteSubTipo(unidadAdministrativa.getIdEntePublico(), "A", authUser.getIdUsuario(),listaUnidAcceso);
                    totalEncendidoOpera = serviciosTicRepository.getCountOperaEstatusServicioTicAndCveEstatusPorEnteSubTipo(unidadAdministrativa.getIdEntePublico(), "E", authUser.getIdUsuario(),listaUnidAcceso);
                    totalApagadoOpera = serviciosTicRepository.getCountOperaEstatusServicioTicAndCveEstatusPorEnteSubTipo(unidadAdministrativa.getIdEntePublico(), "A", authUser.getIdUsuario(),listaUnidAcceso);
                }else{
                    listaEnte=new ArrayList<>();
                    listaService=new ArrayList<>();
                    listaSubService=new ArrayList<>();
                }
            }else
            if (unidadAdministrativa.getIdEntePublico()!=null && unidadAdministrativa.getIdUnidadAdministrativa()!=null){
                listaEnte = servicioTableroBeanRepository.getAllConcentradoAllEntePorEnteSubTipoUnidad(unidadAdministrativa.getIdEntePublico(), authUser.getIdUsuario(), unidadAdministrativa.getIdUnidadAdministrativa());
                listaService = servicioTableroBeanRepository.getAllConcentradoAllServicePorEnteSubTipoUnidad(unidadAdministrativa.getIdEntePublico(), authUser.getIdUsuario(),unidadAdministrativa.getIdUnidadAdministrativa());
                listaSubService = servicioTableroBeanRepository.getAllConcentradoAllSubServicePorEnteSubTipoUnidad(unidadAdministrativa.getIdEntePublico(), authUser.getIdUsuario(),unidadAdministrativa.getIdUnidadAdministrativa());
                totalEncendidoAdmin = serviciosTicRepository.getCountAdminEstatusServicioTicAndCveEstatusPorEnteSubTipoUnidad(unidadAdministrativa.getIdEntePublico(), "E", authUser.getIdUsuario(),unidadAdministrativa.getIdUnidadAdministrativa());
                totalApagadoAdmin = serviciosTicRepository.getCountAdminEstatusServicioTicAndCveEstatusPorEnteSubTipoUnidad(unidadAdministrativa.getIdEntePublico(), "A", authUser.getIdUsuario(),unidadAdministrativa.getIdUnidadAdministrativa());
                totalEncendidoOpera = serviciosTicRepository.getCountOperaEstatusServicioTicAndCveEstatusPorEnteSubTipoUnidad(unidadAdministrativa.getIdEntePublico(), "E", authUser.getIdUsuario(),unidadAdministrativa.getIdUnidadAdministrativa());
                totalApagadoOpera = serviciosTicRepository.getCountOperaEstatusServicioTicAndCveEstatusPorEnteSubTipoUnidad(unidadAdministrativa.getIdEntePublico(), "A", authUser.getIdUsuario(),unidadAdministrativa.getIdUnidadAdministrativa());
            }

        }


        List<ServicioTablero> listaTablero= new ArrayList<>();
        List<ServicioTablero> listaTableroHijos= new ArrayList<>();
        //List<String> listaTr= new ArrayList<>();
        int contador=0;

        List<ServicioTableroBean> listaTipoEntePe = lista.stream().filter(p->p.getGrupo().equals("Poder Ejecutivo")).collect(Collectors.toList());
        List<ServicioTableroBean> listaTipoEnteNoPe = lista.stream().filter(p->!p.getGrupo().equals("Poder Ejecutivo")).collect(Collectors.toList());

        int parent=0;
        String clasePadre="";
        DecimalFormat formatea = new DecimalFormat("###,###,###");

        if (listaTipoEntePe.size()>0){

            int totalEntePoderEjecutivo=0;
            for(ServicioTableroBean servicioTableroBean:listaTipoEntePe) {
                totalEntePoderEjecutivo += listaEnte.stream().filter(p -> p.getParent1().longValue() == servicioTableroBean.getIdTabla().longValue()).collect(Collectors.toList()).size();
            }

            int totalVigenteA = lista.stream().filter(p->p.getGrupo().equals("Poder Ejecutivo")).mapToInt(ServicioTableroBean::getVigenteAdmin).sum();
            int totalHistoricoA = lista.stream().filter(p->p.getGrupo().equals("Poder Ejecutivo")).mapToInt(ServicioTableroBean::getHistoricoAdmin).sum();
            int totalVigenteO = lista.stream().filter(p->p.getGrupo().equals("Poder Ejecutivo")).mapToInt(ServicioTableroBean::getVigenteOpera).sum();
            int totalHistoricoO = lista.stream().filter(p->p.getGrupo().equals("Poder Ejecutivo")).mapToInt(ServicioTableroBean::getHistoricoOpera).sum();
            ServicioTablero servicioTablero= new ServicioTablero();
            servicioTablero.setNombreServicio("Poder Ejecutivo"+" ("+totalEntePoderEjecutivo+")");
            servicioTablero.setVigenteAdmin(totalVigenteA);
            servicioTablero.setHistoricoAdmin(totalHistoricoA);
            servicioTablero.setTotalAdmin(totalVigenteA+totalHistoricoA);
            servicioTablero.setVigenteOpera(totalVigenteO);
            servicioTablero.setHistoricoOpera(totalHistoricoO);
            servicioTablero.setTotalOpera(totalVigenteO+totalHistoricoO);
            contador++;
            parent=contador;
            servicioTablero.setNodo(contador);
            clasePadre="treegrid-"+contador;
            servicioTablero.setClase("treegrid-"+contador);
            servicioTablero.setEstilo("style='border-color:#198754'");
            servicioTablero.setClasePadre("tree");
            listaTablero.add(servicioTablero);

            listEtiquetas.add("Poder Ejecutivo");
            listValores.add(totalVigenteA+totalHistoricoA);
            listEtiquetas2.add("Poder Ejecutivo");
            listValores2.add(totalVigenteO+totalHistoricoO);

        }

        for(ServicioTableroBean servicioTableroBean:listaTipoEntePe){

            List<ServicioTableroBean> listaEnteTemp = listaEnte.stream().filter(p->p.getParent1().longValue()==servicioTableroBean.getIdTabla().longValue()).collect(Collectors.toList());

            ServicioTablero servicioTablero= new ServicioTablero();
            servicioTablero.setNombreServicio(servicioTableroBean.getNombre()+" ("+listaEnteTemp.size()+")");
            servicioTablero.setVigenteAdmin(servicioTableroBean.getVigenteAdmin());
            servicioTablero.setHistoricoAdmin(servicioTableroBean.getHistoricoAdmin());
            servicioTablero.setTotalAdmin(servicioTableroBean.getVigenteAdmin()+servicioTableroBean.getHistoricoAdmin());
            servicioTablero.setVigenteOpera(servicioTableroBean.getVigenteOpera());
            servicioTablero.setHistoricoOpera(servicioTableroBean.getHistoricoOpera());
            servicioTablero.setTotalOpera(servicioTableroBean.getVigenteOpera()+servicioTableroBean.getHistoricoOpera());
            contador++;
            servicioTablero.setNodo(contador);
            servicioTablero.setClase("treegrid-"+contador+" treegrid-parent-"+parent);
            servicioTablero.setClasePadre(clasePadre);
            servicioTablero.setEstilo("");
            listaTablero.add(servicioTablero);

            int parent2=contador;
            String clasePadre2="treegrid-"+contador;

            for(ServicioTableroBean servicioTableroBeanEnte:listaEnteTemp){
                ServicioTablero servicioTableroEnte= new ServicioTablero();
                servicioTableroEnte.setNombreServicio(servicioTableroBeanEnte.getNombreAbrev());
                servicioTableroEnte.setTooltip(servicioTableroBeanEnte.getNombre());
                servicioTableroEnte.setVigenteAdmin(servicioTableroBeanEnte.getVigenteAdmin());
                servicioTableroEnte.setHistoricoAdmin(servicioTableroBeanEnte.getHistoricoAdmin());
                servicioTableroEnte.setTotalAdmin(servicioTableroBeanEnte.getVigenteAdmin()+servicioTableroBeanEnte.getHistoricoAdmin());
                servicioTableroEnte.setVigenteOpera(servicioTableroBeanEnte.getVigenteOpera());
                servicioTableroEnte.setHistoricoOpera(servicioTableroBeanEnte.getHistoricoOpera());
                servicioTableroEnte.setTotalOpera(servicioTableroBeanEnte.getVigenteOpera()+servicioTableroBeanEnte.getHistoricoOpera());
                contador++;
                servicioTableroEnte.setNodo(contador);
                servicioTableroEnte.setClase("treegrid-"+contador+" treegrid-parent-"+parent2);
                servicioTableroEnte.setClasePadre(clasePadre2);
                servicioTableroEnte.setEstilo("style='border-color:#6c757d'");
                listaTablero.add(servicioTableroEnte);

                int parent3=contador;
                String clasePadre3="treegrid-"+contador;


                List<ServicioTableroBean> listaServiceTemp = listaService.stream().filter(p->p.getParent1().longValue()==servicioTableroBeanEnte.getIdTabla().longValue()).collect(Collectors.toList());
                for(ServicioTableroBean servicioTableroBeanService:listaServiceTemp){
                    ServicioTablero servicioTableroService= new ServicioTablero();
                    servicioTableroService.setNombreServicio(servicioTableroBeanService.getNombre());
                    servicioTableroService.setVigenteAdmin(servicioTableroBeanService.getVigenteAdmin());
                    servicioTableroService.setHistoricoAdmin(servicioTableroBeanService.getHistoricoAdmin());
                    servicioTableroService.setTotalAdmin(servicioTableroBeanService.getVigenteAdmin()+servicioTableroBeanService.getHistoricoAdmin());
                    servicioTableroService.setVigenteOpera(servicioTableroBeanService.getVigenteOpera());
                    servicioTableroService.setHistoricoOpera(servicioTableroBeanService.getHistoricoOpera());
                    servicioTableroService.setTotalOpera(servicioTableroBeanService.getVigenteOpera()+servicioTableroBeanService.getHistoricoOpera());
                    contador++;
                    servicioTableroService.setNodo(contador);
                    servicioTableroService.setClase("treegrid-"+contador+" treegrid-parent-"+parent3);
                    servicioTableroService.setClasePadre(clasePadre3);
                    servicioTableroService.setEstilo("");
                    listaTablero.add(servicioTableroService);

                    int parent4=contador;
                    String clasePadre4="treegrid-"+contador;

                    List<ServicioTableroBean> listaSubServiceTemp = listaSubService.stream().filter(p->p.getParent1().longValue()==servicioTableroBeanService.getIdTabla().longValue()).collect(Collectors.toList());
                    for(ServicioTableroBean servicioTableroBeanSubService:listaSubServiceTemp){
                        ServicioTablero servicioTableroSubService= new ServicioTablero();
                        servicioTableroSubService.setNombreServicio(servicioTableroBeanSubService.getNombre());
                        servicioTableroSubService.setVigenteAdmin(servicioTableroBeanSubService.getVigenteAdmin());
                        servicioTableroSubService.setHistoricoAdmin(servicioTableroBeanSubService.getHistoricoAdmin());
                        servicioTableroSubService.setTotalAdmin(servicioTableroBeanSubService.getVigenteAdmin()+servicioTableroBeanSubService.getHistoricoAdmin());
                        servicioTableroSubService.setVigenteOpera(servicioTableroBeanSubService.getVigenteOpera());
                        servicioTableroSubService.setHistoricoOpera(servicioTableroBeanSubService.getHistoricoOpera());
                        servicioTableroSubService.setTotalOpera(servicioTableroBeanSubService.getVigenteOpera()+servicioTableroBeanSubService.getHistoricoOpera());
                        servicioTableroSubService.setIdTipoServicio(servicioTableroBeanSubService.getIdTipoServicio());
                        servicioTableroSubService.setIdSubTipoServicio(servicioTableroBeanSubService.getIdSubTipoServicio());
                        servicioTableroSubService.setIdEntePublico(servicioTableroBeanSubService.getIdEntePublico());
                        contador++;
                        servicioTableroSubService.setNodo(contador);
                        servicioTableroSubService.setClase("treegrid-"+contador+" treegrid-parent-"+parent4);
                        servicioTableroSubService.setClasePadre(clasePadre4);
                        servicioTableroSubService.setEstilo("");
                        listaTableroHijos.add(servicioTableroSubService);

                    }
                }


            }
        }

        for(ServicioTableroBean servicioTableroBean:listaTipoEnteNoPe){

            List<ServicioTableroBean> listaEnteTemp = listaEnte.stream().filter(p->p.getParent1().longValue()==servicioTableroBean.getIdTabla().longValue()).collect(Collectors.toList());

            ServicioTablero servicioTablero= new ServicioTablero();
            servicioTablero.setNombreServicio(servicioTableroBean.getNombre()+" ("+listaEnteTemp.size()+")");
            servicioTablero.setVigenteAdmin(servicioTableroBean.getVigenteAdmin());
            servicioTablero.setHistoricoAdmin(servicioTableroBean.getHistoricoAdmin());
            servicioTablero.setTotalAdmin(servicioTableroBean.getVigenteAdmin()+servicioTableroBean.getHistoricoAdmin());
            servicioTablero.setVigenteOpera(servicioTableroBean.getVigenteOpera());
            servicioTablero.setHistoricoOpera(servicioTableroBean.getHistoricoOpera());
            servicioTablero.setTotalOpera(servicioTableroBean.getVigenteOpera()+servicioTableroBean.getHistoricoOpera());
            contador++;
            servicioTablero.setNodo(contador);
            servicioTablero.setClase("treegrid-"+contador);
            servicioTablero.setEstilo("style='border-color:#198754'");
            servicioTablero.setClasePadre("tree");
            listaTablero.add(servicioTablero);

            listEtiquetas.add(servicioTableroBean.getNombre());
            listValores.add(servicioTableroBean.getVigenteAdmin()+servicioTableroBean.getHistoricoAdmin());
            listEtiquetas2.add(servicioTableroBean.getNombre());
            listValores2.add(servicioTableroBean.getVigenteOpera()+servicioTableroBean.getHistoricoOpera());

            int parent2=contador;
            String clasePadre2="treegrid-"+contador;


            for(ServicioTableroBean servicioTableroBeanEnte:listaEnteTemp){
                ServicioTablero servicioTableroEnte= new ServicioTablero();
                servicioTableroEnte.setNombreServicio(servicioTableroBeanEnte.getNombreAbrev());
                servicioTableroEnte.setTooltip(servicioTableroBeanEnte.getNombre());
                servicioTableroEnte.setVigenteAdmin(servicioTableroBeanEnte.getVigenteAdmin());
                servicioTableroEnte.setHistoricoAdmin(servicioTableroBeanEnte.getHistoricoAdmin());
                servicioTableroEnte.setTotalAdmin(servicioTableroBeanEnte.getVigenteAdmin()+servicioTableroBeanEnte.getHistoricoAdmin());
                servicioTableroEnte.setVigenteOpera(servicioTableroBeanEnte.getVigenteOpera());
                servicioTableroEnte.setHistoricoOpera(servicioTableroBeanEnte.getHistoricoOpera());
                servicioTableroEnte.setTotalOpera(servicioTableroBeanEnte.getVigenteOpera()+servicioTableroBeanEnte.getHistoricoOpera());
                contador++;
                servicioTableroEnte.setNodo(contador);
                servicioTableroEnte.setClase("treegrid-"+contador+" treegrid-parent-"+parent2);
                servicioTableroEnte.setClasePadre(clasePadre2);
                servicioTableroEnte.setEstilo("style='border-color:#6c757d'");
                listaTablero.add(servicioTableroEnte);

                int parent3=contador;
                String clasePadre3="treegrid-"+contador;
                List<ServicioTableroBean> listaServiceTemp = listaService.stream().filter(p->p.getParent1().longValue()==servicioTableroBeanEnte.getIdTabla().longValue()).collect(Collectors.toList());
                for(ServicioTableroBean servicioTableroBeanService:listaServiceTemp){
                    ServicioTablero servicioTableroService= new ServicioTablero();
                    servicioTableroService.setNombreServicio(servicioTableroBeanService.getNombre());
                    servicioTableroService.setVigenteAdmin(servicioTableroBeanService.getVigenteAdmin());
                    servicioTableroService.setHistoricoAdmin(servicioTableroBeanService.getHistoricoAdmin());
                    servicioTableroService.setTotalAdmin(servicioTableroBeanService.getVigenteAdmin()+servicioTableroBeanService.getHistoricoAdmin());
                    servicioTableroService.setVigenteOpera(servicioTableroBeanService.getVigenteOpera());
                    servicioTableroService.setHistoricoOpera(servicioTableroBeanService.getHistoricoOpera());
                    servicioTableroService.setTotalOpera(servicioTableroBeanService.getVigenteOpera()+servicioTableroBeanService.getHistoricoOpera());
                    contador++;
                    servicioTableroService.setNodo(contador);
                    servicioTableroService.setClase("treegrid-"+contador+" treegrid-parent-"+parent3);
                    servicioTableroService.setClasePadre(clasePadre3);
                    servicioTableroService.setEstilo("");
                    listaTablero.add(servicioTableroService);

                    int parent4=contador;
                    String clasePadre4="treegrid-"+contador;
                    List<ServicioTableroBean> listaSubServiceTemp = listaSubService.stream().filter(p->p.getParent1().longValue()==servicioTableroBeanService.getIdTabla().longValue()).collect(Collectors.toList());
                    for(ServicioTableroBean servicioTableroBeanSubService:listaSubServiceTemp){
                        ServicioTablero servicioTableroSubService= new ServicioTablero();
                        servicioTableroSubService.setNombreServicio(servicioTableroBeanSubService.getNombre());
                        servicioTableroSubService.setVigenteAdmin(servicioTableroBeanSubService.getVigenteAdmin());
                        servicioTableroSubService.setHistoricoAdmin(servicioTableroBeanSubService.getHistoricoAdmin());
                        servicioTableroSubService.setTotalAdmin(servicioTableroBeanSubService.getVigenteAdmin()+servicioTableroBeanSubService.getHistoricoAdmin());
                        servicioTableroSubService.setVigenteOpera(servicioTableroBeanSubService.getVigenteOpera());
                        servicioTableroSubService.setHistoricoOpera(servicioTableroBeanSubService.getHistoricoOpera());
                        servicioTableroSubService.setTotalOpera(servicioTableroBeanSubService.getVigenteOpera()+servicioTableroBeanSubService.getHistoricoOpera());
                        servicioTableroSubService.setIdTipoServicio(servicioTableroBeanSubService.getIdTipoServicio());
                        servicioTableroSubService.setIdSubTipoServicio(servicioTableroBeanSubService.getIdSubTipoServicio());
                        servicioTableroSubService.setIdEntePublico(servicioTableroBeanSubService.getIdEntePublico());
                        contador++;
                        servicioTableroSubService.setNodo(contador);
                        servicioTableroSubService.setClase("treegrid-"+contador+" treegrid-parent-"+parent4);
                        servicioTableroSubService.setClasePadre(clasePadre4);
                        servicioTableroSubService.setEstilo("");
                        listaTableroHijos.add(servicioTableroSubService);
                    }
                }
            }

        }
        props.put("etiquetas", listEtiquetas);
        props.put("valores", listValores);
        props2.put("etiquetas", listEtiquetas2);
        props2.put("valores", listValores2);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("listaTableroAdmin", listaTablero);
        jsonObject.put("listaTableroAdminHijos", listaTableroHijos);
        //jsonObject.put("listaTr", listaTr);
        jsonObject.put("totalAdministraVigente", totalVigenteAdmin);
        jsonObject.put("totalAdministraHistorico", totalHistoricoAdmin);
        jsonObject.put("totalAdministra", totalVigenteAdmin+totalHistoricoAdmin);
        jsonObject.put("totalOperaVigente", totalVigenteOpera);
        jsonObject.put("totalOperaHistorico", totalHistoricoOpera);
        jsonObject.put("totalOpera", totalVigenteOpera+totalHistoricoOpera);
        jsonObject.put("totalEncendidoAdmin", totalEncendidoAdmin);
        jsonObject.put("totalApagadoAdmin", totalApagadoAdmin);
        jsonObject.put("totalEncendidoOpera", totalEncendidoOpera);
        jsonObject.put("totalApagadoOpera", totalApagadoOpera);
        jsonObject.put("graficaAdministra", props);
        jsonObject.put("graficaOpera", props2);

        return jsonObject;
    }

    @Override
    public List<ServicioGroupBean> getListaServicioGroup(String curp, Long idEntePublico, boolean roleAdminSigtic) {

        List<ServicioGroupBean> lista = null;

        if (roleAdminSigtic)
            lista = servicioGroupBeanRepository.getGroupServicioAll();
        else{
            List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(curp,idEntePublico);
            if (listaUnidAcceso.size()>0)
                lista =  servicioGroupBeanRepository.getGroupServicioPorUnidad(listaUnidAcceso);
            else
                lista= new ArrayList<>();
        }

        return lista;
    }


    @Override
    public List<ServiciosTic> getListaServicioPorEntePorTipoAndSubtipo(Long idEntePublico, Long idUnidadAdministrativa, Long idTipoServicio, Long idSubtipoServicio, Integer tipo, String estatus) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ServiciosTic> lista = null;

        List<String> listaEstatus = Arrays.asList("VIG","HIS");
        CatTipoServiciosTic catTipoServiciosTic = catTipoServiciosTicRepository.findById(idTipoServicio).get();
        CatSubtipoServiciosTic catSubtipoServiciosTic = catSubtipoServiciosTicRepository.findById(idSubtipoServicio).get();
        EntePublico entePublico = entePublicoRepository.findById(idEntePublico).get();


            if (idUnidadAdministrativa==null){
                if (authUser.isAdministradorSigtic()) {
                    if (tipo == 1 && estatus == null)
                        lista = serviciosTicRepository.findAllByEntePublicoAdminAndTipoServicioAndSubTipoServicioAndCveEstatusInOrderByNumOrdenServicioTic(entePublico, catTipoServiciosTic, catSubtipoServiciosTic, listaEstatus);
                    else if (tipo == 1 && estatus != null)
                        lista = serviciosTicRepository.findAllByEntePublicoAdminAndTipoServicioAndSubTipoServicioAndCveEstatusOrderByNumOrdenServicioTic(entePublico, catTipoServiciosTic, catSubtipoServiciosTic, estatus);
                    else if (tipo == 2 && estatus == null)
                        lista = serviciosTicRepository.findAllByEntePublicoOperaAndTipoServicioAndSubTipoServicioAndCveEstatusInOrderByNumOrdenServicioTic(entePublico, catTipoServiciosTic, catSubtipoServiciosTic, listaEstatus);
                    else if (tipo == 2 && estatus != null)
                        lista = serviciosTicRepository.findAllByEntePublicoOperaAndTipoServicioAndSubTipoServicioAndCveEstatusOrderByNumOrdenServicioTic(entePublico, catTipoServiciosTic, catSubtipoServiciosTic, estatus);
                }else{
                    List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(authUser.getUsername(),idEntePublico);
                    if (listaUnidAcceso.size()>0){
                        List<UnidadAdministrativa> listaUnidades = (List) unidadAdministrativaRepository.findAllById(listaUnidAcceso);

                        if (tipo==1 && estatus==null)
                            lista=serviciosTicRepository.findAllByEntePublicoAdminAndUnidadAdministrativaAdminInAndTipoServicioAndSubTipoServicioAndCveEstatusInOrderByNumOrdenServicioTic(entePublico, listaUnidades, catTipoServiciosTic, catSubtipoServiciosTic, listaEstatus);
                        else
                        if (tipo==1 && estatus!=null)
                            lista=serviciosTicRepository.findAllByEntePublicoAdminAndUnidadAdministrativaAdminInAndTipoServicioAndSubTipoServicioAndCveEstatusOrderByNumOrdenServicioTic(entePublico, listaUnidades, catTipoServiciosTic, catSubtipoServiciosTic, estatus);
                        else
                        if (tipo==2 && estatus==null)
                            lista=serviciosTicRepository.findAllByEntePublicoOperaAndUnidadAdministrativaOperaInAndTipoServicioAndSubTipoServicioAndCveEstatusInOrderByNumOrdenServicioTic(entePublico,listaUnidades, catTipoServiciosTic, catSubtipoServiciosTic, listaEstatus);
                        else
                        if (tipo==2 && estatus!=null)
                            lista=serviciosTicRepository.findAllByEntePublicoOperaAndUnidadAdministrativaOperaInAndTipoServicioAndSubTipoServicioAndCveEstatusOrderByNumOrdenServicioTic(entePublico, listaUnidades, catTipoServiciosTic, catSubtipoServiciosTic, estatus);
                    }

                }

            }else{
                //UnidadAdministrativa unidadAdministrativa=unidadAdministrativaRepository.findById(idUnidadAdministrativa).get();

                if (tipo==1 && estatus==null)
                    lista=serviciosTicRepository.getServiciosAdministradosPorUnidadAndEstatusIn(entePublico.getIdEntePublico(), idUnidadAdministrativa, catTipoServiciosTic.getIdTipoServicioTic(), catSubtipoServiciosTic.getIdSubtipoServicioTic(), listaEstatus);
                else
                if (tipo==1 && estatus!=null)
                    lista=serviciosTicRepository.getServiciosAdministradosPorUnidad(entePublico.getIdEntePublico(), idUnidadAdministrativa, catTipoServiciosTic.getIdTipoServicioTic(), catSubtipoServiciosTic.getIdSubtipoServicioTic(), estatus);
                else
                if (tipo==2 && estatus==null)
                    lista=serviciosTicRepository.getServiciosOperadosPorUnidadAndEstatusIn(entePublico.getIdEntePublico(), idUnidadAdministrativa, catTipoServiciosTic.getIdTipoServicioTic(), catSubtipoServiciosTic.getIdSubtipoServicioTic(), listaEstatus);
                else
                if (tipo==2 && estatus!=null)
                    lista=serviciosTicRepository.getServiciosOperadosPorUnidad(entePublico.getIdEntePublico(), idUnidadAdministrativa, catTipoServiciosTic.getIdTipoServicioTic(), catSubtipoServiciosTic.getIdSubtipoServicioTic(), estatus);
            }

        if (lista==null)
            lista= new ArrayList<>();

        return lista;
    }

    @Override
    public List<ServiciosTic> getListaServicioAll(String rfc, Long idEntePublico, boolean roleAdminSigtic) {

        List<ServiciosTic> lista = null;

        if (roleAdminSigtic)
            lista = serviciosTicRepository.getAllServiciosEntePublico(idEntePublico);
        else{
            List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(rfc,idEntePublico);
            if (listaUnidAcceso.size()>0)
                lista =  serviciosTicRepository.getAllServiciosUnidad(listaUnidAcceso);
            else
                lista= new ArrayList<>();
        }

        return lista;
    }

    @Override
    public List<ServiciosTic> getListaServicioAdmin(String rfc, Long idEntePublico, boolean roleAdminSigtic) {
        List<ServiciosTic> lista = null;

        EntePublico entePublico =entePublicoRepository.findById(idEntePublico).get();
        List<String> listaEstatus = Arrays.asList("VIG","HIS");

        if (roleAdminSigtic)
            lista = serviciosTicRepository.findAllByEntePublicoAdminAndCveEstatusInOrderByNumOrdenServicioTic(entePublico, listaEstatus);
        else{
            List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(rfc,idEntePublico);
            if (listaUnidAcceso.size()>0){
                List<UnidadAdministrativa> listaUnidad = ( List<UnidadAdministrativa>) unidadAdministrativaRepository.findAllById(listaUnidAcceso);

                lista =  serviciosTicRepository.findAllByUnidadAdministrativaAdminInAndCveEstatusInOrderByNumOrdenServicioTic(listaUnidad, listaEstatus);
            }
            else
                lista= new ArrayList<>();
        }

        return lista;
    }

    @Override
    public List<ServiciosTic> getListaSistemasOpera(String rfc, Long idEntePublico, boolean roleAdminSigtic) {
        List<ServiciosTic> lista = null;

        EntePublico entePublico =entePublicoRepository.findById(idEntePublico).get();
        List<String> listaEstatus = Arrays.asList("VIG","HIS");

        if (roleAdminSigtic)
            lista = serviciosTicRepository.findAllByEntePublicoOperaAndCveEstatusInOrderByNumOrdenServicioTic(entePublico, listaEstatus);
        else{
            List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(rfc,idEntePublico);
            if (listaUnidAcceso.size()>0){
                List<UnidadAdministrativa> listaUnidad = ( List<UnidadAdministrativa>) unidadAdministrativaRepository.findAllById(listaUnidAcceso);

                lista =  serviciosTicRepository.findAllByUnidadAdministrativaOperaInAndCveEstatusInOrderByNumOrdenServicioTic(listaUnidad, listaEstatus);
            }
            else
                lista= new ArrayList<>();
        }
        return lista;
    }
    /*
    @Override
    public List<ServiciosTic> getListaServicioAll(String rfc, Long idEntePublico, boolean roleAdminSigtic) {

        List<ServicioTicBean> lista = null;

        if (roleAdminSigtic)
            lista = servicioTicBeanRepository.getServiciosAllPorEntePublico(idEntePublico);
        else{
            List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(rfc,idEntePublico);
            if (listaUnidAcceso.size()>0)
                lista =  servicioTicBeanRepository.getServiciosAllPorUnidad(listaUnidAcceso);
            else
                lista= new ArrayList<>();
        }

        return lista;
    }

    @Override
    public List<ServicioTicBean> getListaServicioAdmin(String rfc, Long idEntePublico, boolean roleAdminSigtic) {
        List<ServicioTicBean> lista = null;

        if (roleAdminSigtic)
            lista = servicioTicBeanRepository.getServiciosAdministradosPorEntePublico(idEntePublico);
        else{
            List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(rfc,idEntePublico);
            if (listaUnidAcceso.size()>0)
                lista =  servicioTicBeanRepository.getServiciosAdministradosPorUnidad(listaUnidAcceso);
            else
                lista= new ArrayList<>();
        }

        return lista;
    }

    @Override
    public List<ServicioTicBean> getListaSistemasOpera(String rfc, Long idEntePublico, boolean roleAdminSigtic) {
        List<ServicioTicBean> lista = null;

        if (roleAdminSigtic)
            lista = servicioTicBeanRepository.getServiciosOperadosPorEntePublico(idEntePublico);
        else{
            List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(rfc,idEntePublico);
            if (listaUnidAcceso.size()>0)
                lista =  servicioTicBeanRepository.getServiciosOperadosPorUnidad(listaUnidAcceso);
            else
                lista= new ArrayList<>();
        }

        return lista;
    }
    */


    @Override
    public ServiciosTic guardarServicios(ServiciosTic serviciosTic) {
        ServiciosTic serviciosTicBd = null;

        Optional<CatTipoServiciosTic> tipoServiciosTicOp = catTipoServiciosTicRepository.findById(serviciosTic.getTipoServicio().getIdTipoServicioTic());
        Optional<CatSubtipoServiciosTic> subTipoServiciosTicOp = catSubtipoServiciosTicRepository.findById(serviciosTic.getSubTipoServicio().getIdSubtipoServicioTic());
        Optional<CatEstatusServiciosTic> estatusServiciosTicOp = catEstatusServiciosTicRepository.findById(serviciosTic.getEstatusServicioTic().getCveEstatusServicioTic());
        Optional<CatEstadosServiciosTic> estadosServiciosTicOp = catEstadosServiciosTicRepository.findById(serviciosTic.getEstadosServicioTic().getCveEstadoServicioTic());
        Optional<CatTipoImpactoServiciosTic> tipoImpactoServiciosTicOp = catTipoImpactoServiciosTicRepository.findById(serviciosTic.getTipoImpactoServicioTic().getCveTipoImpactoServicioTic());
        Optional<EntePublico> entePublicoAdminOp = entePublicoRepository.findById(serviciosTic.getEntePublicoAdmin().getIdEntePublico());
        Optional<UnidadAdministrativa> unidadAdministrativaAdminOp = unidadAdministrativaRepository.findById(serviciosTic.getUnidadAdministrativaAdmin().getIdUnidadAdministrativa());
        Optional<EntePublico> entePublicoOperaOp = entePublicoRepository.findById(serviciosTic.getEntePublicoOpera().getIdEntePublico());
        Optional<UnidadAdministrativa> unidadAdministrativaAdminOperaOp = unidadAdministrativaRepository.findById(serviciosTic.getUnidadAdministrativaOpera().getIdUnidadAdministrativa());
        Optional<UbicacionUnidadAdmin> ubicacionUnidadAdminOp = ubicacionUnidadAdminSaiRepository.findById(serviciosTic.getUbicacionUnidadAdmin().getIdUbicacionAdmtva());
        Optional<UbicacionUnidadAdmin> ubicacionUnidadOperaOp = ubicacionUnidadAdminSaiRepository.findById(serviciosTic.getUbicacionUnidadOpera().getIdUbicacionAdmtva());

        if (!tipoServiciosTicOp.isPresent())
            throw new NoSuchElementException("Falta el tipo del servicio");

        if (!subTipoServiciosTicOp.isPresent())
            throw new NoSuchElementException("Falta el sub-tipo del servicio");

        if (!estatusServiciosTicOp.isPresent())
            throw new NoSuchElementException("Falta el estatus del servicio");

        if (!estadosServiciosTicOp.isPresent())
            throw new NoSuchElementException("Falta el estado del servicio");

        if (!tipoImpactoServiciosTicOp.isPresent())
            throw new NoSuchElementException("Falta el tipo de impacto");

        if(!entePublicoAdminOp.isPresent())
            throw new NoSuchElementException("Falta ente público que administra");

        if(!unidadAdministrativaAdminOp.isPresent())
            throw new NoSuchElementException("Falta unidad administrativa que administra");

        if(!entePublicoOperaOp.isPresent())
            throw new NoSuchElementException("Falta ente público que opera");

        if(!unidadAdministrativaAdminOperaOp.isPresent())
            throw new NoSuchElementException("Falta unidad administrativa que opera");

        if(!ubicacionUnidadAdminOp.isPresent())
            throw new NoSuchElementException("Falta la ubicación ente administra");

        if(!ubicacionUnidadAdminOp.isPresent())
            throw new NoSuchElementException("Falta la ubicación ente administra");

        if(!ubicacionUnidadOperaOp.isPresent())
            throw new NoSuchElementException("Falta la ubicación ente opera");

        /*UnidadAdministrativa unidadAdministrativa=null;

        if (serviciosTic.getUnidadAdministrativaOpera()!=null) {
            Optional<UnidadAdministrativa> unidadAdministrativaOperaOp = unidadAdministrativaRepository.findById(serviciosTic.getUnidadAdministrativaOpera().getIdUnidadAdministrativa());
            if(!unidadAdministrativaOperaOp.isPresent())
                throw new NoSuchElementException("Falta unidad administrativa que opera");
            else
                unidadAdministrativa=unidadAdministrativaOperaOp.get();
        }*/



        if(serviciosTic.getIdServicioTic() != null){
            serviciosTicBd = serviciosTicRepository.findById(serviciosTic.getIdServicioTic()).get();
        }else
            serviciosTicBd = serviciosTic;

        serviciosTicBd.setTipoServicio(tipoServiciosTicOp.get());
        serviciosTicBd.setSubTipoServicio(subTipoServiciosTicOp.get());
        serviciosTicBd.setCveServicioTic(serviciosTic.getCveServicioTic());
        serviciosTicBd.setNombreServicioTic(serviciosTic.getNombreServicioTic());
        serviciosTicBd.setServicioTicAbreviadoDescripcion(serviciosTic.getServicioTicAbreviadoDescripcion());
        serviciosTicBd.setServicioTicDescripcion(serviciosTic.getServicioTicDescripcion());
        serviciosTicBd.setEstatusServicioTic(estatusServiciosTicOp.get());
        serviciosTicBd.setEstadosServicioTic(estadosServiciosTicOp.get());
        serviciosTicBd.setTipoImpactoServicioTic(tipoImpactoServiciosTicOp.get());
        serviciosTicBd.setCveEstatus(serviciosTic.getCveEstatus());

        serviciosTicBd.setNumOrdenServicioTic(serviciosTic.getNumOrdenServicioTic());
        serviciosTicBd.setAlcanceServicioTic(serviciosTic.getAlcanceServicioTic());
        serviciosTicBd.setObsServicioTic(serviciosTic.getObsServicioTic());

        serviciosTicBd.setNumInventarioGet(serviciosTic.getNumInventarioGet());
        serviciosTicBd.setNumInventarioCi(serviciosTic.getNumInventarioCi());

        serviciosTicBd.setEntePublicoAdmin(entePublicoAdminOp.get());
        serviciosTicBd.setUbicacionUnidadAdmin(ubicacionUnidadAdminOp.get());
        serviciosTicBd.setUnidadAdministrativaAdmin(unidadAdministrativaAdminOp.get());
        serviciosTicBd.setEntePublicoOpera(entePublicoOperaOp.get());
        serviciosTicBd.setUnidadAdministrativaOpera(unidadAdministrativaAdminOperaOp.get());
        serviciosTicBd.setUbicacionUnidadOpera(ubicacionUnidadOperaOp.get());
        /*if (unidadAdministrativa!=null){
            serviciosTicBd.setUnidadAdministrativaOpera(unidadAdministrativa);
            serviciosTicBd.setUnidadAdministrativaOpera(unidadAdministrativa);
        }*/
        serviciosTicBd.setFechaUltimoCambio(new Timestamp(new Date().getTime()));

        return serviciosTicRepository.save(serviciosTicBd);
    }

    @Override
    public ServiciosTic guardarMiServicio(ServiciosTic serviciosTic) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean existeRolEnlaceTicServicio = authUser.getListaPerfil().stream().filter(p-> p.getNombrePerfil().equals("ENLACE_TIC_SERVICIO")).findFirst().isPresent();

        Optional<CatTipoServiciosTic> tipoServiciosTicOp = catTipoServiciosTicRepository.findById(serviciosTic.getTipoServicio().getIdTipoServicioTic());
        Optional<CatSubtipoServiciosTic> subTipoServiciosTicOp = catSubtipoServiciosTicRepository.findById(serviciosTic.getSubTipoServicio().getIdSubtipoServicioTic());
        Optional<EntePublico> entePublicoDirige = entePublicoRepository.findById(serviciosTic.getEntePublicoAdmin().getIdEntePublico());
        Optional<UnidadAdministrativa> unidadAdministrativaDirige = unidadAdministrativaRepository.findById(serviciosTic.getUnidadAdministrativaAdmin().getIdUnidadAdministrativa());

        if (!tipoServiciosTicOp.isPresent())
            throw new NoSuchElementException("Falta el tipo del servicio");

        if (!subTipoServiciosTicOp.isPresent())
            throw new NoSuchElementException("Falta el sub-tipo del servicio");

        String curp=serviciosTic.getCurp();
        Long idUsuarioEntePublico=null;

        EntePublico entePublicoOpera=null;
        UnidadAdministrativa unidadAdminOpera=null;

        /*if (existeRolEnlaceTicServicio){

            if (serviciosTic.getIdUsuarioEntePublico()==null)
                throw new NoSuchElementException("No se ha seleccionado el usuario");

            CatUsuarioEntePublico catUsuarioEntePublico = catUsuarioEntePublicoRepository.findById(serviciosTic.getIdUsuarioEntePublico().longValue()).get();

            entePublicoOpera = entePublicoRepository.findById(catUsuarioEntePublico.getEntePublico().getIdEntePublico()).get();

            curp= catUsuarioEntePublico.getCatUsuario().getCurp();
            idUsuarioEntePublico= catUsuarioEntePublico.getIdUsuarioEntePublico();

            if (catUsuarioEntePublico.getUnidadAdministrativa()!=null)
                unidadAdminOpera=unidadAdministrativaRepository.findById(catUsuarioEntePublico.getUnidadAdministrativa().getIdUnidadAdministrativa()).get();

        }else{


            entePublicoOpera = entePublicoRepository.findById(authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico()).get();

            curp= authUser.getCatUsuarioEntePublico().getCatUsuario().getCurp();
            idUsuarioEntePublico=authUser.getCatUsuarioEntePublico().getIdUsuarioEntePublico();

            if (authUser.getCatUsuarioEntePublico().getUnidadAdministrativa()!=null)
                unidadAdminOpera=unidadAdministrativaRepository.findById(authUser.getCatUsuarioEntePublico().getUnidadAdministrativa().getIdUnidadAdministrativa()).get();

        }

        if (serviciosTic.getIdServicioTic()!=null){
            ServiciosTic serviciosTicUpdate = serviciosTicRepository.findById(serviciosTic.getIdServicioTic()).get();
            serviciosTicUpdate.setTipoServicio(tipoServiciosTicOp.get());
            serviciosTicUpdate.setSubTipoServicio(subTipoServiciosTicOp.get());
            serviciosTicUpdate.setServicioTicAbreviadoDescripcion(serviciosTic.getServicioTicAbreviadoDescripcion());
            serviciosTicUpdate.setServicioTicDescripcion(serviciosTic.getServicioTicDescripcion());

            serviciosTicUpdate.setEntePublicoAdmin(entePublicoDirige.get());
            serviciosTicUpdate.setUnidadAdministrativaAdmin(unidadAdministrativaDirige.get());
            serviciosTicUpdate.setEntePublicoOpera(entePublicoOpera);

            if (unidadAdminOpera!=null){
                serviciosTicUpdate.setUnidadAdministrativaOpera(unidadAdminOpera);
            }
            serviciosTicUpdate.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            serviciosTicUpdate.setCurp(curp);
            serviciosTicUpdate.setIdUsuarioEntePublico(idUsuarioEntePublico);
            serviciosTicUpdate.setIpPC(ClienteInfo.getClientIpAddr(request));
            serviciosTicUpdate.setNombrePC(ClienteInfo.getHostPC(request));
            serviciosTicUpdate.setCveUsuarioMtto(authUser.getUsername());


            bitacoraMovimientoService.guardarBitacoraMovimiento(serviciosTicUpdate.getIdServicioTic(),"SERVICIOS_TIC", "Se actualiza el servicio");

            return serviciosTicRepository.save(serviciosTicUpdate);
        }else{
            ServiciosTic serviciosTicNew =new ServiciosTic();

            serviciosTicNew.setTipoServicio(tipoServiciosTicOp.get());
            serviciosTicNew.setSubTipoServicio(subTipoServiciosTicOp.get());
            serviciosTicNew.setServicioTicAbreviadoDescripcion(serviciosTic.getServicioTicAbreviadoDescripcion());
            serviciosTicNew.setServicioTicDescripcion(serviciosTic.getServicioTicDescripcion());

            serviciosTicNew.setEntePublicoAdmin(entePublicoDirige.get());
            serviciosTicNew.setUnidadAdministrativaAdmin(unidadAdministrativaDirige.get());
            serviciosTicNew.setEntePublicoOpera(entePublicoOpera);

            if (unidadAdminOpera!=null){
                serviciosTicNew.setUnidadAdministrativaOpera(unidadAdminOpera);
            }
            serviciosTicNew.setFechaRegistro(new Timestamp(new Date().getTime()));
            serviciosTicNew.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            serviciosTicNew.setCurp(curp);
            serviciosTicNew.setIdUsuarioEntePublico(idUsuarioEntePublico);
            serviciosTicNew.setIdUsuarioCapturo(authUser.getIdUsuario());
            serviciosTicNew.setIpPC(ClienteInfo.getClientIpAddr(request));
            serviciosTicNew.setNombrePC(ClienteInfo.getHostPC(request));
            serviciosTicNew.setCveUsuarioMtto(authUser.getUsername());
            serviciosTicNew.setCveEstatus("VIG");

            CatEstatusServicio catEstatusSeguimiento = catEstatusSeguimientoRepository.findById(1).get();
            serviciosTicNew.setCatEstatusSeguimiento(catEstatusSeguimiento);

            ServiciosTic serviciosTic1 = serviciosTicRepository.save(serviciosTicNew);
            bitacoraMovimientoService.guardarBitacoraMovimiento(serviciosTic1.getIdServicioTic(),"SERVICIOS_TIC", "Nuevo servicio");

            return serviciosTic1;
        }

         */

        return new ServiciosTic();

    }

    @Override
    public ServiciosTic getServiciosTic(Long idServicioTic){
        Optional <ServiciosTic>  serviciosTicOptional = serviciosTicRepository.findById(idServicioTic);

        if(!serviciosTicOptional.isPresent())
            throw new NoSuchElementException("El servicio no existe");

       /* if (serviciosTicOptional.get().getIdUsuarioEntePublico()!=null){
            CatUsuarioEntePublico catUsuarioEntePublico= catUsuarioEntePublicoRepository.findById(serviciosTicOptional.get().getIdUsuarioEntePublico()).get();
            serviciosTicOptional.get().setCatUsuarioEntePublico(catUsuarioEntePublico);
        }*/

        return serviciosTicOptional.get();
    }

    @Override
    public ServiciosTic deleteServiciosTic(ServiciosTic serviciosTic) {
        Optional<ServiciosTic> serviciosTicOp = serviciosTicRepository.findById(serviciosTic.getIdServicioTic());

        if (!serviciosTicOp.isPresent())
            throw new NoSuchElementException("El servicio no existe");

        serviciosTicOp.get().setCveEstatus("NOV");
        serviciosTicOp.get().setFechaUltimoCambio(new Timestamp(new Date().getTime()));

        return serviciosTicRepository.save(serviciosTicOp.get());
    }

    @Override
    public List<ServiciosTic> getServiciosSearch(Long idServicio, String nombreServicio, String clasificacion, boolean sinFiltrar) {

        ServiciosTic serviciosTic = serviciosTicRepository.findById(idServicio).get();
        List<SubServicioSearch> listaSearch = subServicioSearchRepository.findAllByClasificacion(clasificacion);
        List<CatSubtipoServiciosTic> listaSubtipoServicio =  listaSearch.stream().map(SubServicioSearch::getCatSubtipoServiciosTic).collect(Collectors.toList());

        List<ServiciosTic> lista=null;

        if (sinFiltrar)
            lista= serviciosTicRepository.getServiciosSearch(nombreServicio, serviciosTic.getEntePublicoAdmin(), serviciosTic.getEntePublicoOpera(), listaSubtipoServicio);
        else
            lista= serviciosTicRepository.getServiciosSearch(nombreServicio, serviciosTic.getEntePublicoAdmin(), serviciosTic.getEntePublicoOpera(), listaSubtipoServicio);

        return lista;
    }

    @Override
    public List<ServiciosTicRelacionados> getServiciosTicRelacionados(ServiciosTic serviciosTicParam) {

        List<SubServicioSearch> listaSearch = subServicioSearchRepository.findAllByClasificacion(serviciosTicParam.getClasificacion());
        List<CatSubtipoServiciosTic> listaSubtipoServicio =  listaSearch.stream().map(SubServicioSearch::getCatSubtipoServiciosTic).collect(Collectors.toList());

        ServiciosTic serviciosTic = serviciosTicRepository.findById(serviciosTicParam.getIdServicioTic()).get();

        List<ServiciosTicRelacionados> lista = serviciosTicRelacionadosRepository.getServiciosRelacionados(serviciosTic, listaSubtipoServicio);

        return lista;
    }

    @Override
    public ServiciosTicRelacionados addServicio(ServiciosTicRelacionados serviciosTicRelacionados) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ServiciosTic serviciosTicPadre = serviciosTicRepository.findById(serviciosTicRelacionados.getServiciosTicPadre().getIdServicioTic()).get();
        ServiciosTic serviciosTic = serviciosTicRepository.findById(serviciosTicRelacionados.getServiciosTic().getIdServicioTic()).get();

        Optional<ServiciosTicRelacionados> serviciosTicRelacionadosOp = serviciosTicRelacionadosRepository.findByServiciosTicPadreAndServiciosTic(serviciosTicPadre, serviciosTic);

        if (serviciosTicRelacionadosOp.isPresent()) {
            if (serviciosTicRelacionadosOp.get().getCveEstatus().equals("VIG"))
                throw new NoSuchElementException("El servicio ya esta agregado");
            else{
                serviciosTicRelacionadosOp.get().setCveEstatus("VIG");
                serviciosTicRelacionadosOp.get().setFechaUltimoCambio(new Timestamp(new Date().getTime()));
                serviciosTicRelacionadosOp.get().setCveUsuarioMtto(authUser.getUsername());
                serviciosTicRelacionadosOp.get().setDescripcionServicioTic(serviciosTicRelacionados.getDescripcionServicioTic());
                serviciosTicRelacionadosOp.get().setNotaServicioTicRelacionado(serviciosTicRelacionados.getNotaServicioTicRelacionado());
                serviciosTicRelacionadosOp.get().setNumeroOrdenServicio(serviciosTicRelacionados.getNumeroOrdenServicio());

                return serviciosTicRelacionadosRepository.save(serviciosTicRelacionadosOp.get());
            }
        }else{

        ServiciosTicRelacionados serviciosTicRelacionados1 = new ServiciosTicRelacionados();
        serviciosTicRelacionados1.setServiciosTicPadre(serviciosTicPadre);
        serviciosTicRelacionados1.setServiciosTic(serviciosTic);
        serviciosTicRelacionados1.setDescripcionServicioTic(serviciosTicRelacionados.getDescripcionServicioTic());
        serviciosTicRelacionados1.setNotaServicioTicRelacionado(serviciosTicRelacionados.getNotaServicioTicRelacionado());
        serviciosTicRelacionados1.setNumeroOrdenServicio(serviciosTicRelacionados.getNumeroOrdenServicio());
        serviciosTicRelacionados1.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        serviciosTicRelacionados1.setCveUsuarioMtto(authUser.getUsername());
        serviciosTicRelacionados1.setCveEstatus("VIG");

        return serviciosTicRelacionadosRepository.save(serviciosTicRelacionados1);
        }
    }

    @Override
    public ServiciosTicRelacionados deleteServicioRelacionado(ServiciosTicRelacionados serviciosTicRelacionados) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ServiciosTicRelacionados> serviciosTicRelacionadosOp= serviciosTicRelacionadosRepository.findById(serviciosTicRelacionados.getIdServicioTicRelacionado());

        if (serviciosTicRelacionadosOp.isPresent()){
            if (serviciosTicRelacionadosOp.get().getCveEstatus().equals("VIG")){
                serviciosTicRelacionadosOp.get().setCveEstatus("NOV");
                serviciosTicRelacionadosOp.get().setFechaUltimoCambio(new Timestamp(new Date().getTime()));
                serviciosTicRelacionadosOp.get().setCveUsuarioMtto(authUser.getUsername());
                return  serviciosTicRelacionadosRepository.save(serviciosTicRelacionadosOp.get());
            }else
                throw new NoSuchElementException("La relación ya esta eliminada");
        }else
            throw new NoSuchElementException("El servicio no existe");

    }

    @Override
    public UsuarioServicio guardarUsuarioServicios(UsuarioServicio usuarioServicio) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (usuarioServicio.getIdUsuarioServicio() != null) {
            UsuarioServicio existente = usuarioServicioRepository.findById(usuarioServicio.getIdUsuarioServicio())
                    .orElseThrow(() -> new NoSuchElementException("Usuario servicio no encontrado"));

            CatTipoUsuarioServicio catTipoUsuarioServicio = catTipoUsuarioServicioRepository
                    .findById(usuarioServicio.getCatTipoUsuarioServicio().getIdTipoUsuario()).get();

            existente.setCatTipoUsuarioServicio(catTipoUsuarioServicio);
            existente.setFolioSima(usuarioServicio.getFolioSima());
            existente.setFechaSolicitudSima(usuarioServicio.getFechaSolicitudSima());
            existente.setFechaAltaServicio(usuarioServicio.getFechaAltaServicio());
            existente.setFechaBajaServicio(usuarioServicio.getFechaBajaServicio());
            existente.setAlcanceServicio(usuarioServicio.getAlcanceServicio());
            existente.setObservacionServicio(usuarioServicio.getObservacionServicio());
            existente.setCorreoInstitucionalUsuario(usuarioServicio.getCorreoInstitucionalUsuario());
            existente.setCorreoPersonalUsuario(usuarioServicio.getCorreoPersonalUsuario());
            existente.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            existente.setIpPC(authUser.getIp());
            existente.setNombrePC(authUser.getHosName());
            existente.setCveUsuarioMtto(authUser.getUsername());
            existente.setNombreAppMtto("SIGTIC");

            return usuarioServicioRepository.save(existente);
        }

        ServiciosTic serviciosTic= serviciosTicRepository.findById(usuarioServicio.getServiciosTic().getIdServicioTic()).get();
        CatUsuarioEntePublico catUsuarioEntePublico = catUsuarioEntePublicoRepositorySai.findById(usuarioServicio.getCatUsuarioEntePublico().getIdUsuarioEntePublico()).get();

        Optional<UsuarioServicio> usuarioServicioOpt = usuarioServicioRepository.findByServiciosTicAndCatUsuarioEntePublico(serviciosTic, catUsuarioEntePublico);

        CatTipoUsuarioServicio catTipoUsuarioServicio = catTipoUsuarioServicioRepository.findById(usuarioServicio.getCatTipoUsuarioServicio().getIdTipoUsuario()).get();

        CatEstatusServicio catEstatusServicio =  catEstatusServicioRepository.findById(1).get();

        if (usuarioServicioOpt.isPresent() && (usuarioServicioOpt.get().getCveEstatus().equals("VIG"))){
            throw new NoSuchElementException("El usuario ya esta agregado");
        }else
        if (usuarioServicioOpt.isPresent() && usuarioServicioOpt.get().getCveEstatus().equals("NOV")){

            usuarioServicioOpt.get().setCatTipoUsuarioServicio(catTipoUsuarioServicio);
            usuarioServicioOpt.get().setFolioSima(usuarioServicio.getFolioSima());
            usuarioServicioOpt.get().setFechaSolicitudSima(usuarioServicio.getFechaSolicitudSima());
            usuarioServicioOpt.get().setCurpTenicoServicio(authUser.getCatUsuarioEntePublico().getCatUsuario().getCurp());
            usuarioServicioOpt.get().setFechaAltaServicio(usuarioServicio.getFechaAltaServicio());
            usuarioServicioOpt.get().setFechaBajaServicio(usuarioServicio.getFechaBajaServicio());
            usuarioServicioOpt.get().setAlcanceServicio(usuarioServicio.getAlcanceServicio());
            usuarioServicioOpt.get().setObservacionServicio(usuarioServicio.getObservacionServicio());
            usuarioServicioOpt.get().setCorreoInstitucionalUsuario(usuarioServicio.getCorreoInstitucionalUsuario());
            usuarioServicioOpt.get().setCorreoPersonalUsuario(usuarioServicio.getCorreoPersonalUsuario());

            if (catUsuarioEntePublico.getUnidadAdministrativa()!=null)
                usuarioServicioOpt.get().setUnidadAdministrativaUsuarioServicio(catUsuarioEntePublico.getUnidadAdministrativa());
            if (catUsuarioEntePublico.getUbicacionUnidadAdmin()!=null)
                usuarioServicioOpt.get().setUbicacionUnidadAdmin(catUsuarioEntePublico.getUbicacionUnidadAdmin());

            usuarioServicioOpt.get().setCveEstatusUsuarioServicio(catUsuarioEntePublico.getCveEstatus());
            usuarioServicioOpt.get().setCveEstatus("VIG");
            usuarioServicioOpt.get().setFechaUltimoCambioServicio(serviciosTic.getFechaUltimoCambio());
            usuarioServicioOpt.get().setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            usuarioServicioOpt.get().setIpPC(authUser.getIp());
            usuarioServicioOpt.get().setNombrePC(authUser.getHosName());
            usuarioServicioOpt.get().setCveUsuarioMtto(authUser.getUsername());
            usuarioServicioOpt.get().setNombreAppMtto("SIGTIC");
            usuarioServicioOpt.get().setCatEstatusServicio(catEstatusServicio);

            return usuarioServicioRepository.save(usuarioServicioOpt.get());
        }else{

            UsuarioServicio usuarioServicioNew = new UsuarioServicio();
            usuarioServicioNew.setCatTipoUsuarioServicio(catTipoUsuarioServicio);
            usuarioServicioNew.setServiciosTic(serviciosTic);
            usuarioServicioNew.setCatUsuarioEntePublico(catUsuarioEntePublico);
            usuarioServicioNew.setCurpUsuarioServicio(usuarioServicio.getCurpUsuarioServicio());
            usuarioServicioNew.setFolioSima(usuarioServicio.getFolioSima());
            usuarioServicioNew.setFechaSolicitudSima(usuarioServicio.getFechaSolicitudSima());
            usuarioServicioNew.setCurpTenicoServicio(authUser.getCatUsuarioEntePublico().getCatUsuario().getCurp());
            usuarioServicioNew.setFechaAltaServicio(usuarioServicio.getFechaAltaServicio());
            usuarioServicioNew.setFechaBajaServicio(usuarioServicio.getFechaBajaServicio());
            usuarioServicioNew.setAlcanceServicio(usuarioServicio.getAlcanceServicio());
            usuarioServicioNew.setObservacionServicio(usuarioServicio.getObservacionServicio());
            usuarioServicioNew.setEntePublicoUsuarioServicio(catUsuarioEntePublico.getEntePublico());
            usuarioServicioNew.setCorreoInstitucionalUsuario(usuarioServicio.getCorreoInstitucionalUsuario());
            usuarioServicioNew.setCorreoPersonalUsuario(usuarioServicio.getCorreoPersonalUsuario());

            if (catUsuarioEntePublico.getUnidadAdministrativa()!=null)
                usuarioServicioNew.setUnidadAdministrativaUsuarioServicio(catUsuarioEntePublico.getUnidadAdministrativa());
            if (catUsuarioEntePublico.getUbicacionUnidadAdmin()!=null)
                usuarioServicioNew.setUbicacionUnidadAdmin(catUsuarioEntePublico.getUbicacionUnidadAdmin());

            usuarioServicioNew.setCveEstatusUsuarioServicio(catUsuarioEntePublico.getCveEstatus());
            usuarioServicioNew.setCveEstatus("VIG");
            usuarioServicioNew.setFechaRegistro(new Timestamp(new Date().getTime()));
            usuarioServicioNew.setFechaUltimoCambioServicio(serviciosTic.getFechaUltimoCambio());
            usuarioServicioNew.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            usuarioServicioNew.setIpPC(authUser.getIp());
            usuarioServicioNew.setNombrePC(authUser.getHosName());
            usuarioServicioNew.setCveUsuarioMtto(authUser.getUsername());
            usuarioServicioNew.setNombreAppMtto("SIGTIC");
            usuarioServicioNew.setCatEstatusServicio(catEstatusServicio);

            return usuarioServicioRepository.save(usuarioServicioNew);
        }
    }

    @Override
    public List<UsuarioServicio> getUsuariosServicios(Long idServicios) {

        ServiciosTic serviciosTic =  serviciosTicRepository.findById(idServicios).get();

        List<String> listaEstatus = Arrays.asList("VIG","HIS");

        List<UsuarioServicio> lista= usuarioServicioRepository.findAllByServiciosTicAndCveEstatusInOrderByIdUsuarioServicio(serviciosTic, listaEstatus);
        return lista;
    }

    @Override
    public UsuarioServicio deleteUsuarioServicio(Long idUsuarioServicio) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsuarioServicio usuarioServicio =  usuarioServicioRepository.findById(idUsuarioServicio).get();
        usuarioServicio.setCveEstatus("NOV");
        usuarioServicio.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
        usuarioServicio.setCveUsuarioMtto(authUser.getUsername());

        return usuarioServicioRepository.save(usuarioServicio);
    }


    @Override
    public Page<ServiciosTic> getServiciosSearch(ServiciosTic serviciosTic) {

        ServicioTicSpecification servicioTicSpecification= new ServicioTicSpecification(serviciosTic.getEntePublicoAdmin().getIdEntePublico(),serviciosTic.getCveServicioTic(),serviciosTic.getNombreServicioTic());
        Pageable pageable = PageRequest.of(serviciosTic.getPagina(), serviciosTic.getSize(), Sort.Direction.ASC, "numOrdenServicioTic");

        Page<ServiciosTic> lista = serviciosTicRepository.findAll(servicioTicSpecification,  pageable);

        return lista;
    }

    @Override
    public Page<UsuarioServicio> getUsuarioServiciosSearch(ServiciosTic serviciosTic) {

        UsuarioServicioSpecification usuarioServicioSpecification = new UsuarioServicioSpecification(serviciosTic.getEntePublicoAdmin().getIdEntePublico(),serviciosTic.getCveServicioTic(),serviciosTic.getNombreServicioTic(), serviciosTic.getCurp());
        Pageable pageable = PageRequest.of(serviciosTic.getPagina(), serviciosTic.getSize());

        Page<UsuarioServicio> lista = usuarioServicioRepository.findAll(usuarioServicioSpecification,  pageable);

        return lista;
    }

    @Override
    public List<UsuarioServicio> getUsuariosServiciosPorServicios(List<ServiciosTic> serviciosTics) {
        if (serviciosTics == null || serviciosTics.isEmpty()) return new ArrayList<>();

        List<String> listaEstatus = Arrays.asList("VIG", "HIS");
        List<UsuarioServicio> resultado = new ArrayList<>();

        int tamanoLote = 900; //
        for (int inicio = 0; inicio < serviciosTics.size(); inicio += tamanoLote) {
            int fin = Math.min(inicio + tamanoLote, serviciosTics.size());
            List<ServiciosTic> lote = serviciosTics.subList(inicio, fin);
            resultado.addAll(usuarioServicioRepository.findAllByServiciosTicInAndCveEstatusInFetch(lote, listaEstatus));
        }

        return resultado;
    }

    @Override
    public void escribirExcelServiciosAdministra(List<ServiciosTic> lista, Integer tipo, String tipoEntePublicoText, String entePublicoText, String tipoServicioText, String subTipoServicioText,
                                                 HttpServletResponse response) throws IOException {

        ClassPathResource resource =
                new ClassPathResource("static/plantillas/serviciosTicAdministra.xlsx");

        try (InputStream inputStream = resource.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            escribirCeldaTexto(sheet, 0, 0, "Tipo Ente Público: " + strVal(tipoEntePublicoText));
            escribirCeldaTexto(sheet, 0, 1, strVal(entePublicoText));
            escribirCeldaTexto(sheet, 1, 0, "Tipo Servicio: " + strVal(tipoServicioText));
            escribirCeldaTexto(sheet, 2, 0, "SubTipo Servicio: " + strVal(subTipoServicioText));

            int i = 4;

            List<UsuarioServicio> todosUsuarios = serviciosTicService.getUsuariosServiciosPorServicios(lista);

            Map<Long, List<UsuarioServicio>> usuariosPorServicio = todosUsuarios.stream()
                    .collect(Collectors.groupingBy(us -> us.getServiciosTic().getIdServicioTic()));


            for (ServiciosTic s : lista) {

                List<UsuarioServicio> usuariosRelacionados = usuariosPorServicio.getOrDefault(s.getIdServicioTic(), Collections.emptyList());

                if (usuariosRelacionados.isEmpty()) {
                    escribirFilaServicioConUsuario(sheet, i++, s, tipo, null);
                } else {
                    for (UsuarioServicio us : usuariosRelacionados) {
                        escribirFilaServicioConUsuario(sheet, i++, s, tipo, us);
                    }
                }
            }

            for (int col = 0; col < 26; col++) {
                sheet.autoSizeColumn(col);
            }

            String filename = "servicios_detalle_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.setHeader("Pragma", "No-cache");

            workbook.write(response.getOutputStream());
        }
    }

    private void escribirFilaServicioConUsuario(Sheet sheet, int filaIdx, ServiciosTic s, Integer tipo, UsuarioServicio us) {

        Row row = sheet.createRow(filaIdx);

        // ---- columnas 0-14: datos del servicio (igual que antes) ----
        row.createCell(0).setCellValue(s.getEntePublicoAdmin() != null ? strVal(s.getEntePublicoAdmin().getNombreEntePublico()) : "");
        row.createCell(1).setCellValue(strVal(s.getCveServicioTic()));

        if (tipo != null && tipo == 1) {
            row.createCell(2).setCellValue(strVal(s.getNombreServicioTic()));
        } else if (tipo != null && tipo == 2) {
            row.createCell(2).setCellValue(s.getEntePublicoOpera() != null ? strVal(s.getEntePublicoOpera().getNombreEntePublicoAbreviada()) : "");
        } else {
            row.createCell(2).setCellValue("");
        }

        String entePublicoUnidadColumna = "";
        if (tipo != null && tipo == 2) {
            if (s.getEntePublicoAdmin() != null) {
                entePublicoUnidadColumna = strVal(s.getEntePublicoAdmin().getNombreEntePublicoAbreviada());
                if (s.getUnidadAdministrativaAdmin() != null)
                    entePublicoUnidadColumna += "/" + strVal(s.getUnidadAdministrativaAdmin().getNombreUnidadAdministrativaAbreviada());
            }
        } else if (tipo != null && tipo == 1) {
            if (s.getEntePublicoOpera() != null) {
                entePublicoUnidadColumna = strVal(s.getEntePublicoOpera().getNombreEntePublicoAbreviada());
                if (s.getUnidadAdministrativaOpera() != null)
                    entePublicoUnidadColumna += "/" + strVal(s.getUnidadAdministrativaOpera().getNombreUnidadAdministrativaAbreviada());
            }
        }

        row.createCell(3).setCellValue(s.getEntePublicoOpera() != null ? strVal(s.getEntePublicoOpera().getNombreEntePublico()) : "");
        row.createCell(4).setCellValue(s.getUnidadAdministrativaOpera() != null ? strVal(s.getUnidadAdministrativaOpera().getNombreUnidadAdministrativa()) : "");
        row.createCell(5).setCellValue(entePublicoUnidadColumna);
        row.createCell(6).setCellValue(s.getEstadosServicioTic() != null ? strVal(s.getEstadosServicioTic().getDescEstadoServicioTic()) : "");
        row.createCell(7).setCellValue(s.getEstatusServicioTic() != null ? strVal(s.getEstatusServicioTic().getDescEstatusServicioTic()) : "");
        row.createCell(8).setCellValue(strVal(s.getCveEstatus()));
        row.createCell(9).setCellValue(s.getObsServicioTic() != null ? strVal(s.getObsServicioTic()) : "");
        row.createCell(10).setCellValue(s.getServicioTicAbreviadoDescripcion() != null ? strVal(s.getServicioTicAbreviadoDescripcion()) : "");
        row.createCell(11).setCellValue(s.getTipoImpactoServicioTic() != null ? strVal(s.getTipoImpactoServicioTic().getDescTipoImpactoServicioTic()) : "");
        row.createCell(12).setCellValue(s.getNumOrdenServicioTic());
        row.createCell(13).setCellValue(s.getCurp());
        row.createCell(14).setCellValue(s.getNumInventarioCi());

        // se hizo un respaldo por si, el caso el catUsarioEnte publico viene null se agarra de catUsuario y completa los datos
        // ---- columnas 15+: datos del usuario relacionado (en blanco si no hay usuario) ----
        if (us != null) {
            CatUsuarioEntePublico cuep = us.getCatUsuarioEntePublico();
            Usuario usuario = (cuep != null) ? cuep.getCatUsuario() : null;

            mx.gob.tabasco.cmaig.sigtic.sac.model.CatUsuario catUsuarioFallback = null;
            if (usuario == null && us.getCurpUsuarioServicio() != null) {
                catUsuarioFallback = catUsuarioService.getUsuarioCurp(us.getCurpUsuarioServicio()).orElse(null);
            }

            row.createCell(15).setCellValue(us.getCatTipoUsuarioServicio() != null ? strVal(us.getCatTipoUsuarioServicio().getTipoUsuarioServicio()) : "");
            row.createCell(16).setCellValue(strVal(us.getCurpUsuarioServicio()));

            if (usuario != null) {
                row.createCell(17).setCellValue(strVal(usuario.getNombre()));
                row.createCell(18).setCellValue(strVal(usuario.getApellidoPaterno()));
                row.createCell(19).setCellValue(strVal(usuario.getApellidoMaterno()));
            } else {
                row.createCell(17).setCellValue(catUsuarioFallback != null ? strVal(catUsuarioFallback.getNombre()) : "");
                row.createCell(18).setCellValue(catUsuarioFallback != null ? strVal(catUsuarioFallback.getApellidoPaterno()) : "");
                row.createCell(19).setCellValue(catUsuarioFallback != null ? strVal(catUsuarioFallback.getApellidoMaterno()) : "");
            }

            row.createCell(20).setCellValue(usuario != null ? strVal(usuario.getNumCelular()) : "");
            row.createCell(21).setCellValue(cuep != null ? strVal(cuep.getNumTelefonoInstitucional()) : "");
            row.createCell(22).setCellValue(cuep != null ? strVal(cuep.getNumeroExtensionInstitucional()) : "");
            row.createCell(23).setCellValue(us.getFechaAltaServicio() != null ? us.getFechaAltaServicioText() : "");
            row.createCell(24).setCellValue(us.getFechaBajaServicio() != null ? us.getFechaBajaServicioText() : "");
            row.createCell(25).setCellValue(us.getFolioSima() != null ? String.valueOf(us.getFolioSima()) : "");

        } else {
            for (int col = 15; col <= 25; col++) {
                row.createCell(col).setCellValue("");
            }
        }
    }

    private void escribirCeldaTexto(Sheet sheet, int filaIdx, int colIdx, String valor) {
       Row row = sheet.getRow(filaIdx) != null ? sheet.getRow(filaIdx) : sheet.createRow(filaIdx);
       Cell cell = row.getCell(colIdx) != null ? row.getCell(colIdx) : row.createCell(colIdx);
       cell.setCellValue(valor);

    }

    private String strVal(String val) {
        return val != null ? val : "";
    }


    @Override
    public void escribirExcelServiciosOpera(List<ServiciosTic> lista, String tipoEntePublicoText, String entePublicoText, String tipoServicioText, String subTipoServicioText,
            HttpServletResponse response) throws IOException {

        ClassPathResource resource =
                new ClassPathResource("static/plantillas/serviciosTicOpera.xlsx");

        try (InputStream inputStream = resource.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            escribirCeldaTexto(sheet, 0, 0, "Tipo Ente Público: " + strVal(tipoEntePublicoText));
            escribirCeldaTexto(sheet, 0, 1, strVal(entePublicoText));
            escribirCeldaTexto(sheet, 1, 0, "Tipo Servicio: " + strVal(tipoServicioText));
            escribirCeldaTexto(sheet, 2, 0, "SubTipo Servicio: " + strVal(subTipoServicioText));

            int i = 4;

            for (ServiciosTic s : lista) {

                Row row = sheet.createRow(i++);
                row.createCell(0).setCellValue(s.getEntePublicoOpera() !=null ? strVal(s.getEntePublicoOpera().getNombreEntePublico()): "");
//                row.createCell(0).setCellValue(s.getUnidadAdministrativaOpera() != null ? strVal(s.getUnidadAdministrativaOpera().getNombreUnidadAdministrativa()) : "");
                row.createCell(1).setCellValue(strVal(s.getCveServicioTic()));
                row.createCell(2).setCellValue(strVal(s.getNombreServicioTic()));
//                row.createCell(3).setCellValue(s.getEntePublicoOpera() != null ? strVal(s.getEntePublicoOpera().getNombreEntePublicoAbreviada()) : "");
                String entePublicoUnidadAdmin = "";
                if (s.getEntePublicoAdmin() != null) {
                    entePublicoUnidadAdmin = strVal(s.getEntePublicoAdmin().getNombreEntePublicoAbreviada());
                    if (s.getUnidadAdministrativaAdmin() != null)
                        entePublicoUnidadAdmin += "/" + strVal(s.getUnidadAdministrativaAdmin().getNombreUnidadAdministrativaAbreviada());
                }
                row.createCell(5).setCellValue(entePublicoUnidadAdmin);
                row.createCell(3).setCellValue(strVal(s.getEntePublicoAdmin() != null ? strVal(s.getEntePublicoAdmin().getNombreEntePublico()) : ""));
                row.createCell(4).setCellValue(s.getUnidadAdministrativaAdmin() != null ? strVal(s.getUnidadAdministrativaAdmin().getNombreUnidadAdministrativa()) : "");
//                row.createCell(3).setCellValue(s.getEntePublicoAdmin() != null ? strVal(s.getEntePublicoAdmin().getNombreEntePublicoAbreviada()) : "");
                row.createCell(6).setCellValue(s.getEstadosServicioTic() != null ? strVal(s.getEstadosServicioTic().getDescEstadoServicioTic()) : "");
                row.createCell(7).setCellValue(s.getEstatusServicioTic() != null ? strVal(s.getEstatusServicioTic().getDescEstatusServicioTic()) : "");
                row.createCell(8).setCellValue(strVal(s.getCveEstatus()));
                row.createCell(9).setCellValue(strVal(s.getObsServicioTic()));
                row.createCell(10).setCellValue(strVal(s.getServicioTicAbreviadoDescripcion()));
                row.createCell(11).setCellValue(s.getTipoImpactoServicioTic() != null ? strVal(s.getTipoImpactoServicioTic().getDescTipoImpactoServicioTic()) : "");
                row.createCell(12).setCellValue(s.getNumOrdenServicioTic());
                row.createCell(13).setCellValue(strVal(s.getCurp()));
                row.createCell(14).setCellValue(s.getNumInventarioCi());
            }

            for (int col = 0; col < 15; col++) {
                sheet.autoSizeColumn(col);
            }

            String filename = "servicios_detalle_opera_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename
            );
            response.setHeader("Pragma", "No-cache");
            workbook.write(response.getOutputStream());
        }
    }

}