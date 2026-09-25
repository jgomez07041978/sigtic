package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.OrganigramaBean;
import mx.gob.tabasco.cmaig.sigtic.sac.model.OrganigramaSql;
import mx.gob.tabasco.cmaig.sigtic.sac.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.OrganigramaSqlRepository;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.UnidadAdministrativaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Service
public class OrganigramaServiceImp implements OrganigramaService {

/*    @Autowired
    private OrganigramaUnidadRepository organigramaUnidadRepository;*/

    @Autowired
    private OrganigramaSqlRepository organigramaSqlRepository;

    @Autowired
    private UnidadAdministrativaRepository unidadAdministrativaRepository;

    @Override
    public  OrganigramaBean getOrganigramaEntePublico(Long idEntePublico) {

        List<OrganigramaSql> listaOrg = organigramaSqlRepository.getOrganigramaId(idEntePublico);

        Stack<OrganigramaBean> pila = new Stack<>();

        listaOrg.forEach(p->{
            OrganigramaBean orgBean = new OrganigramaBean();
            orgBean.setName(p.getText());
            orgBean.setTitle("aaa");
            orgBean.setId(Long.parseLong(p.getId()));
            if (p.getParent().equals("#"))
                orgBean.setIdPadre(Long.parseLong("0"));
            else
                orgBean.setIdPadre(Long.parseLong(p.getParent()));
            pila.push(orgBean);
        });

       /* Integer maxNivel=listaOrg.stream().map(p->p.getNivel()).mapToInt(p->p).max().getAsInt();

        for (int i=0;i<maxNivel+1; i++) {
            Integer nivel = i + 1;
            List<OrganigramaUnidad> listaOrgInt = listaOrg.stream().filter(p -> p.getNivel() == nivel).collect(Collectors.toList());

            listaOrgInt.forEach(p -> {

                OrganigramaBean orgBean = new OrganigramaBean();
                orgBean.setName(p.getText());
                orgBean.setTitle("aaa");
                orgBean.setId(p.getId());
                if (p.getParent().equals("#"))
                    orgBean.setIdPadre(Long.parseLong("0"));
                else
                    orgBean.setIdPadre(Long.parseLong(p.getParent()));

                if (pila.empty())
                    pila.push(orgBean);
                else {
                    boolean encontre = false;
                    List<OrganigramaBean> listaAux = new ArrayList<>();

                    do {
                        OrganigramaBean orgPila = pila.pop();
                        if (orgPila.getId() == orgBean.getIdPadre()) {
                            encontre = true;
                            pila.push(orgPila);
                        }
                        else
                            listaAux.add(orgPila);
                    } while (!pila.empty() && !encontre);

                    pila.push(orgBean);

                    for (int j=0;j<listaAux.size();j++){
                        pila.push(listaAux.get(j));
                    }
                }

            });
        }*/

        boolean termine = false;

        do{
            List<OrganigramaBean> listaAux = new ArrayList<>();
            OrganigramaBean organigramaBean=pila.pop();

            boolean encontre=false;
            while (!pila.empty() && !encontre){
                OrganigramaBean org = pila.pop();
                if (org.getId().longValue()==organigramaBean.getIdPadre().longValue()){
                    encontre=true;
                    if (org.getChildren()!=null)
                        org.getChildren().add(organigramaBean);
                    else{
                        List<OrganigramaBean> listaTemporal= new ArrayList<>();
                        listaTemporal.add(organigramaBean);
                        org.setChildren(listaTemporal);
                    }
                    pila.push(org);
                }else
                    listaAux.add(org);
            }

            for (int j=listaAux.size()-1;j>=0;j--){
                pila.push(listaAux.get(j));
            }

            if (pila.size()==1)
                termine=true;

        }while(!termine);


        return pila.pop();
    }


    @Override
    public List<OrganigramaSql> getOrganigramaId(Long idEntePublico) {

        List<OrganigramaSql> listaOrgTodo=organigramaSqlRepository.getOrganigramaId(idEntePublico);

        List<OrganigramaSql> listaOrg=organigramaSqlRepository.getOrganigramaIdNivel1(idEntePublico);

        for(OrganigramaSql org : listaOrg){
            if (org.getNivel()==1 || org.getNivel()==2)
                org.setTieneHijo(listaOrgTodo.stream().anyMatch(p->p.getParent().equals(org.getId())));
        }

        return listaOrg;
    }

    @Override
    public List<OrganigramaSql> getOrganigramaIdNivel2(Long idEntePublico, Long idSelecNivel2) {
        List<OrganigramaSql> listaOrgTodo=organigramaSqlRepository.getOrganigramaId(idEntePublico);

        List<OrganigramaSql> listaOrg=organigramaSqlRepository.getOrganigramIdNivel2(idEntePublico, idSelecNivel2);

        for(OrganigramaSql org : listaOrg){
            if (org.getNivel()==1 || org.getNivel()==2 || org.getNivel()==3)
                org.setTieneHijo(listaOrgTodo.stream().anyMatch(p->p.getParent().equals(org.getId())));
        }

        return listaOrg;
    }

    @Override
    public List<OrganigramaSql> getOrganigramaIdNivel3(Long idEntePublico, Long idSelecNivel2 , Long idSelecNivel3) {

        List<OrganigramaSql> listaOrgTodo=organigramaSqlRepository.getOrganigramaId(idEntePublico);

        List<OrganigramaSql> listaOrg=organigramaSqlRepository.getOrganigramIdNivel3(idEntePublico, idSelecNivel2, idSelecNivel3);

        for(OrganigramaSql org : listaOrg){
            if (org.getNivel()==1 || org.getNivel()==2 || org.getNivel()==3)
                org.setTieneHijo(listaOrgTodo.stream().anyMatch(p->p.getParent().equals(org.getId())));
        }

        return  listaOrg;
    }

    @Override
    public List<OrganigramaSql> getOrganigramaIdNivel4(Long idEntePublico, Long idSeleccionado) {
        return null;
    }

    @Override
    public UnidadAdministrativa guardarUnidadAdministrativaOrg(UnidadAdministrativa unidadAdministrativa) {

        UnidadAdministrativa unidadAdministrativa1 = unidadAdministrativaRepository.findById(unidadAdministrativa.getIdUnidadAdministrativa()).get();

        unidadAdministrativa1.setRfcTitular(unidadAdministrativa.getRfcTitular());
        unidadAdministrativa1.setCveUnidadAdministrativa(unidadAdministrativa.getCveUnidadAdministrativa());
        unidadAdministrativa1.setNombreUnidadAdministrativaAbreviada(unidadAdministrativa.getNombreUnidadAdministrativaAbreviada());
        unidadAdministrativa1.setNombreUnidadAdministrativa(unidadAdministrativa.getNombreUnidadAdministrativa());
        unidadAdministrativa1.setNombreCargoUnidad(unidadAdministrativa.getNombreCargoUnidad());
        unidadAdministrativa1.setNombreCategoriaUnidad(unidadAdministrativa.getNombreCategoriaUnidad());
        unidadAdministrativa1.setCorreoUnidadAdministrativa(unidadAdministrativa.getCorreoUnidadAdministrativa());
        unidadAdministrativa1.setTelefono(unidadAdministrativa.getTelefono());
        unidadAdministrativa1.setNumeroExtension(unidadAdministrativa.getNumeroExtension());
        unidadAdministrativa1.setNumeroOrden(unidadAdministrativa.getNumeroOrden());
        unidadAdministrativa1.setReglamentoInterior(unidadAdministrativa.getReglamentoInterior());

        return unidadAdministrativaRepository.save(unidadAdministrativa1);
    }
}
