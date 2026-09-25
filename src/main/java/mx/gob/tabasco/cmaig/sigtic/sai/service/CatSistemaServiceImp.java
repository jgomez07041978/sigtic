package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.Utileria.UtileriaReporte;
import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.*;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegAccesoUsuarioService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CatSistemaServiceImp implements CatSistemaService {

    @Autowired
    private CatSistemaRepository catSistemaRepository;

    @Autowired
    private EntePublicoRepository entePublicoRepository;

    @Autowired
    private SegAccesoUsuarioService segAccesoUsuarioService;

    @Autowired
    private UnidadAdministrativaRepository unidadAdministrativaRepository;

    @Autowired
    private CatTipoSistemaRepository catTipoSistemaRepository;

    @Autowired
    private CatEstatusSistemaRepository catEstatusSistemaRepository;

    @Override
    public List<CatSistema> getListaSistemasAll(String rfc, Long idEntePublico, Long idUnidadAdministrativa) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CatSistema> lista = null;

        List<CatSistema> listaAll = (List<CatSistema>) catSistemaRepository.findAll();

        if (idUnidadAdministrativa==null){

            if (authUser.isAdministradorSigtic())
                lista = catSistemaRepository.getAllSistemasEntePublico(idEntePublico);
            else{
                List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(authUser.getUsername(),idEntePublico);
                if (listaUnidAcceso.size()>0){
                    lista = catSistemaRepository.getAllSistemasEntePublicoPorNivel(idEntePublico, listaUnidAcceso);
                }
                else
                    lista= new ArrayList<>();
            }
        }else
                lista =  catSistemaRepository.getAllSistemasUnidad(idUnidadAdministrativa);

        lista.forEach(p->{
            List<CatSistema> listaTemp = listaAll.stream().filter(f->f.getCatSistemaPadre()!=null && f.getCatSistemaPadre().getIdSistema()==p.getIdSistema()).collect(Collectors.toList());
            p.setTotalSistemaHijos(listaTemp.size());
        });

        return lista;
    }

    @Override
    public List<CatSistema> getListaSistemasAdmin(String rfc, Long idEntePublico, boolean roleAdminSigtic) {

        List<CatSistema> lista = null;
        EntePublico entePublico =entePublicoRepository.findById(idEntePublico).get();

        List<CatSistema> listaAll = (List<CatSistema>) catSistemaRepository.findAll();

        if (roleAdminSigtic)
            lista = catSistemaRepository.findAllByEntePublicoAdminOrderByNumeroOrdenAsc(entePublico);
        else{
            List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(rfc,idEntePublico);
            if (listaUnidAcceso.size()>0){
                List<UnidadAdministrativa> listaUnidad = ( List<UnidadAdministrativa>) unidadAdministrativaRepository.findAllById(listaUnidAcceso);

                lista =  catSistemaRepository.findAllByUnidadAdministrativaAdminInOrderByNumeroOrdenAsc(listaUnidad);
            }
            else
                lista= new ArrayList<>();
        }

        lista.forEach(p->{
            List<CatSistema> listaTemp = listaAll.stream().filter(f->f.getCatSistemaPadre()!=null && f.getCatSistemaPadre().getIdSistema()==p.getIdSistema()).collect(Collectors.toList());
            p.setTotalSistemaHijos(listaTemp.size());
        });

        return lista;
    }

    @Override
    public List<CatSistema> getListaSistemasOpera(String rfc, Long idEntePublico, boolean roleAdminSigtic) {

        List<CatSistema> lista = null;
        EntePublico entePublico =entePublicoRepository.findById(idEntePublico).get();

        List<CatSistema> listaAll = (List<CatSistema>) catSistemaRepository.findAll();

        if (roleAdminSigtic)
            lista = catSistemaRepository.findAllByEntePublicoOperaOrderByNumeroOrdenAsc(entePublico);
        else{
            List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(rfc,idEntePublico);
            if (listaUnidAcceso.size()>0){
                List<UnidadAdministrativa> listaUnidad = ( List<UnidadAdministrativa>) unidadAdministrativaRepository.findAllById(listaUnidAcceso);

                lista =  catSistemaRepository.findAllByUnidadAdministrativaOperaInOrderByNumeroOrdenAsc(listaUnidad);
            }
            else
                lista= new ArrayList<>();
        }

        lista.forEach(p->{
            List<CatSistema> listaTemp = listaAll.stream().filter(f->f.getCatSistemaPadre()!=null && f.getCatSistemaPadre().getIdSistema()==p.getIdSistema()).collect(Collectors.toList());
            p.setTotalSistemaHijos(listaTemp.size());
        });

        return lista;
    }

    @Override
    public List<CatSistema> getListaSistemaPadre(Long idEntePublico) {
        EntePublico entePublico =entePublicoRepository.findById(idEntePublico).get();
        List<CatSistema> lista = catSistemaRepository.findAllByEntePublicoAdminOrderByNombreSistemaAsc(entePublico);

        return lista;
    }

    @Override
    public CatSistema guardarSistema(CatSistema catSistema) {

        CatSistema catSistemaBd = null;

        if (catSistema.getIdSistema()!=null){
            catSistemaBd = catSistemaRepository.findById(catSistema.getIdSistema()).get();
            catSistemaBd.setCodigoSistema(catSistema.getCodigoSistema());
            catSistemaBd.setCveMejoraRegulatoria(catSistema.getCveMejoraRegulatoria());
            catSistemaBd.setNombreSistemaAbreviado(catSistema.getNombreSistemaAbreviado());
            catSistemaBd.setNombreSistema(catSistema.getNombreSistema());
            catSistemaBd.setAlcanceSistema(catSistema.getAlcanceSistema());
            catSistemaBd.setNumeroInventarioGet(catSistema.getNumeroInventarioGet());
            catSistemaBd.setNumeroInventarioEntePublico(catSistema.getNumeroInventarioEntePublico());
            catSistemaBd.setNumeroOrden(catSistema.getNumeroOrden());
            catSistemaBd.setCveEstatus(catSistema.getCveEstatus());
        }
        else {
            catSistemaBd = catSistema;

            Optional<CatSistema> sistemaExiste=catSistemaRepository.findCatSistemaByCodigoSistema(catSistema.getCodigoSistema());

            if (sistemaExiste.isPresent())
                throw new NoSuchElementException("El código de sistema ya se esta utilizando en "+sistemaExiste.get().getEntePublicoAdmin().getNombreEntePublico());
        }

        if (catSistema.getCatSistemaPadre()!=null && catSistema.getCatSistemaPadre().getIdSistema()!=null){
            CatSistema catSistemaPadre = catSistemaRepository.findById(catSistema.getCatSistemaPadre().getIdSistema()).get();

            if (catSistemaBd.getIdSistema()!=null && catSistemaPadre.getIdSistema().longValue()==catSistemaBd.getIdSistema().longValue())
                throw new NoSuchElementException("El padre no puede ser el mismo sistema.");

            catSistemaBd.setCatSistemaPadre(catSistemaPadre);
        }else
            catSistemaBd.setCatSistemaPadre(null);


        if (catSistema.getCatTipoSistema().getCveTipoSistema()==null)
            throw new NoSuchElementException("Se requiere el tipo de sistema.");

        Optional<CatTipoSistema> catTipoSistemaOp = catTipoSistemaRepository.findById(catSistema.getCatTipoSistema().getCveTipoSistema());

        if (!catTipoSistemaOp.isPresent())
            throw new NoSuchElementException("El tipo de sistema no existe.");

        catSistemaBd.setCatTipoSistema(catTipoSistemaOp.get());

        if (catSistema.getEntePublicoAdmin()!=null && catSistema.getEntePublicoAdmin().getIdEntePublico()==null)
            throw new NoSuchElementException("Se requiere el ente publico que administra.");

        EntePublico entePublicoAdmin = entePublicoRepository.findById(catSistema.getEntePublicoAdmin().getIdEntePublico()).get();

        catSistemaBd.setEntePublicoAdmin(entePublicoAdmin);

        if (catSistema.getUnidadAdministrativaAdmin()!=null && catSistema.getUnidadAdministrativaAdmin().getIdUnidadAdministrativa()==null)
            throw new NoSuchElementException("Se requiere la unidad administrativa que administra.");

        UnidadAdministrativa unidadAdministrativa = unidadAdministrativaRepository.findById(catSistema.getUnidadAdministrativaAdmin().getIdUnidadAdministrativa()).get();

        catSistemaBd.setUnidadAdministrativaAdmin(unidadAdministrativa);

        if (catSistema.getEntePublicoOpera()!=null && catSistema.getEntePublicoOpera().getIdEntePublico()!=null){
            EntePublico entePublicoOpera = entePublicoRepository.findById(catSistema.getEntePublicoOpera().getIdEntePublico()).get();
            catSistemaBd.setEntePublicoOpera(entePublicoOpera);
        }
        else
            catSistemaBd.setEntePublicoOpera(null);

        if (catSistema.getUnidadAdministrativaOpera()!=null && catSistema.getUnidadAdministrativaOpera().getIdUnidadAdministrativa()!=null){
            UnidadAdministrativa unidadAdministrativaOpera = unidadAdministrativaRepository.findById(catSistema.getUnidadAdministrativaOpera().getIdUnidadAdministrativa()).get();
            catSistemaBd.setUnidadAdministrativaOpera(unidadAdministrativaOpera);
        }else
            catSistemaBd.setUnidadAdministrativaOpera(null);

        if (catSistema.getCatEstatusSistema()!=null && catSistema.getCatEstatusSistema().getCveEstatusSistema()==null)
            throw new NoSuchElementException("Se requiere el estatus del sistema");

        CatEstatusSistema catEstatusSistema = catEstatusSistemaRepository.findById(catSistema.getCatEstatusSistema().getCveEstatusSistema()).get();

        catSistemaBd.setCatEstatusSistema(catEstatusSistema);

        return catSistemaRepository.save(catSistemaBd);
    }

    @Override
    public JSONObject deleteSistema(CatSistema catSistema) {

        Optional<CatSistema> catSistemaOp = catSistemaRepository.findById(catSistema.getIdSistema());

        if (!catSistemaOp.isPresent())
            throw new NoSuchElementException("El sistema no existe");

        List<CatSistema> listaSistemaPadre = catSistemaRepository.findAllByCatSistemaPadre(catSistemaOp.get());

        if (listaSistemaPadre.size()>0)
            throw new NoSuchElementException("El sistema tiene hijos asociados");

        catSistemaRepository.delete(catSistemaOp.get());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message","Sistema eliminado correctamente");

        return jsonObject;
    }

    @Override
    public CatSistema getSistema(Long idSistema) {

        Optional<CatSistema> catSistemaOp = catSistemaRepository.findById(idSistema);

        if (!catSistemaOp.isPresent())
            throw new NoSuchElementException("El sistema no existe");

        return catSistemaOp.get();
    }

    @Override
    public List<CatSistema> getListaSistemasHijos(Long idSistema) {
        CatSistema catSistema = catSistemaRepository.findById(idSistema).get();
        return catSistemaRepository.findAllByCatSistemaPadre(catSistema);
    }

    @Override
    public ResponseEntity<byte[]> getReporte(Long id) {

        Map<String, Object> parametrosReporte = new HashMap<String, Object>();

        CatSistema catSistema= catSistemaRepository.findById(id).get();

        List<CatSistema> lista= new ArrayList<>();
        lista.add(catSistema);


        String titulo="Java es un lenguaje de programación multiplataforma orientado a objetos que se ejecuta en miles de millones de dispositivos de " +
                "todo el mundo. Sustenta aplicaciones, sistemas operativos de smartphones, <b>"+catSistema.getNombreSistema()+"</b> software empresarial y muchos programas conocidos. A pesar de " +
                "haberse inventado hace más de 20 años, Java es actualmente el lenguaje de programación más popular para los desarrolladores de aplicaciones";

        String titulo2="Java es un lenguaje de programación multiplataforma orientado a objetos que se ejecuta en miles de millones de dispositivos de " +
                "todo el mundo. Sustenta aplicaciones, sistemas operativos de smartphones, <b>"+catSistema.getNombreSistema()+" Otra cosa</b> software empresarial y muchos programas conocidos. A pesar de " +
                "haberse inventado hace más de 20 años, Java es actualmente el lenguaje de programación más popular para los desarrolladores de aplicaciones";


        parametrosReporte.put("nombreDependencia","Secretaría de Administración");
        parametrosReporte.put("titulo1",titulo);
        parametrosReporte.put("titulo2",titulo2);


        return UtileriaReporte.jasperCreatePdf("static/jasper/ejemplo.jrxml", parametrosReporte, lista, "reporte_ejmplo.pdf");
    }

    @Override
    public List<CatSistema> getListaSistemas(String estatus) {
        return catSistemaRepository.findAllByCveEstatus(estatus);
    }


}
