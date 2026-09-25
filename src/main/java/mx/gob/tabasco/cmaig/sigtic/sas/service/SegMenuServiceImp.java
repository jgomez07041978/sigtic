package mx.gob.tabasco.cmaig.sigtic.sas.service;

import mx.gob.tabasco.cmaig.sigtic.sas.pojo.SegMenuUsuarioBean;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.StateBean;
import mx.gob.tabasco.cmaig.sigtic.sas.model.*;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SegMenuServiceImp implements SegMenuService {

    @Autowired
    private SegMenuRepository segMenuRepository;

    @Autowired
    private SegPerfilRepository segPerfilRepository;

    @Autowired
    private SegPerfilPrivilegioAsignadoRepository segPerfilPrivilegioAsignadoRepository;

    @Autowired
    private SegMenuArbolRepository segMenuArbolRepository;

    @Autowired
    private SegPerfilPrivilegioRepository segPerfilPrivilegioRepository;

    @Override
    public List<SegMenuUsuarioBean> getMenuUsuarioSesion(String usuario) {

        List<SegMenuUsuarioBean> listaMenuUsuario = new ArrayList<>();
        Comparator<SegMenu> byOrdenAsc = (SegMenu s1, SegMenu s2)-> s1.getOrden().compareTo(s2.getOrden());
        List<SegMenu>  listaMenu = segMenuRepository.getMenuByUsuario("SIGTIC", usuario);

        List<SegMenu>  listaMenuPadre = listaMenu.stream().filter(p-> p.getIdMenuPadre()==null && p.getTipoMenu().equals("MENU")).sorted(byOrdenAsc).collect(Collectors.toList());

        listaMenuPadre.forEach(segMenu -> {
            SegMenuUsuarioBean menuUsuarioBean= new SegMenuUsuarioBean();
            menuUsuarioBean.setMenuPadre(segMenu);
            List<SegMenu> listaMenuHijoTemp= listaMenu.stream().filter(p->p.getIdMenuPadre()!=null && p.getIdMenuPadre().intValue()==segMenu.getIdMenu().intValue() && p.getTipoMenu().equals("MENU")).sorted(byOrdenAsc).collect(Collectors.toList());
            menuUsuarioBean.setMenuHijo(listaMenuHijoTemp);
            listaMenuUsuario.add(menuUsuarioBean);
        });

        return listaMenuUsuario;
    }

    @Override
    public List<SegMenuArbol> getMenuArbol(SegPerfil perfil) {
        SegPerfil segPerfil = segPerfilRepository.findById(perfil.getIdPerfil()).get();

        List<SegPerfilPrivilegioAsignado> listaAsignado= segPerfilPrivilegioAsignadoRepository.findPerfilPrivilegioByIdPerfil(perfil.getIdPerfil());
        List<SegPerfilPrivilegioAsignado> listaAsignadoAux = listaAsignado.stream().filter(p->!p.getPadre().equals("#")).collect(Collectors.toList());

        List<SegMenuArbol> listaMenu = segMenuArbolRepository.findMenuArbol();
        List<SegPerfilPrivilegio> listaPerfilPrivilegio= segPerfilPrivilegioRepository.findSegPerfilPrivilegioBySegPerfil(segPerfil);

        listaMenu.forEach(p->{
            boolean existePrivilegio = listaPerfilPrivilegio.stream().anyMatch(pv-> pv.getSegMenu().getIdMenu().intValue()==p.getId().intValue());
            boolean existeHijoAsignado = listaAsignadoAux.stream().anyMatch(pi-> Integer.parseInt(pi.getPadre())==p.getId().intValue());

            if (existePrivilegio && !existeHijoAsignado)
                p.setState(new StateBean(true));
            else
                p.setState(new StateBean(false));
        });


        return listaMenu;
    }
}
