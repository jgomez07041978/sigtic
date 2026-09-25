package mx.gob.tabasco.cmaig.sigtic.sas.service;

import mx.gob.tabasco.cmaig.sigtic.sas.repository.SegMenuRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.SegPerfilPrivilegioAsignadoRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.SegPerfilPrivilegioRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.SegPerfilRepository;

import mx.gob.tabasco.cmaig.sigtic.sas.model.*;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SegPerfilPrivilegioServiceImp implements SegPerfilPrivilegioService{

    @Autowired
    private SegPerfilPrivilegioRepository segPerfilPrivilegioRepository;

    @Autowired
    private SegPerfilPrivilegioAsignadoRepository segPerfilPrivilegioAsignadoRepository;

    @Autowired
    private SegPerfilRepository segPerfilRepository;

    @Autowired
    private SegMenuRepository segMenuRepository;

    @Override
    public SegPerfilPrivilegio getPerfilPrivilegioMenu(String sistema, String usuario, Integer idMenu) {

        SegPerfilPrivilegio segPerfilPrivilegio =null;

        List<SegPerfilPrivilegio> lista = segPerfilPrivilegioRepository.getPerfilPrivilegioMenu(sistema,usuario, idMenu);

        if (lista.size()>0) {
            segPerfilPrivilegio=lista.stream().findFirst().get();
            segPerfilPrivilegio.setAgregar(lista.stream().anyMatch(p -> p.getAgregar()!=null && p.getAgregar() == true));
            segPerfilPrivilegio.setConsultar(lista.stream().anyMatch(p -> p.getConsultar()!=null && p.getConsultar() == true));
            segPerfilPrivilegio.setEditar(lista.stream().anyMatch(p -> p.getEditar()!=null && p.getEditar() == true));
            segPerfilPrivilegio.setEliminar(lista.stream().anyMatch(p -> p.getEliminar()!=null && p.getEliminar() == true));
            segPerfilPrivilegio.setValidar(lista.stream().anyMatch(p -> p.getValidar()!=null && p.getValidar() == true));
        }

        return segPerfilPrivilegio;
    }

    @Override
    public List<SegPerfilPrivilegioAsignado> getPrivilegiosPorRol(Integer idRol) {
        return segPerfilPrivilegioAsignadoRepository.findPerfilPrivilegioByIdPerfil(idRol);
    }

    @Transactional(value = "seguridadTransactionManager")
    @Override
    public JSONArray savePervilegioPorPerfil(SegPerfil perfil) {
        /*Se almacenan en un map los privilegios que se pretenden guardar del arbol*/
        JSONArray array = new JSONArray();

        List<Integer> listaMenuIdAdd = new ArrayList<>();
        Map<Integer, SegMenu> mapMenuAdd = new HashMap<>();

        perfil.getMenu().forEach(menuArbol->{
            if (menuArbol.getParent().equals("#")){
                mapMenuAdd.put(menuArbol.getId(),null);
                listaMenuIdAdd.add(menuArbol.getId());
            }
            else
            {
                if (!mapMenuAdd.containsKey(Integer.parseInt(menuArbol.getParent()))){
                    mapMenuAdd.put(Integer.parseInt(menuArbol.getParent()), null);
                    listaMenuIdAdd.add(Integer.parseInt(menuArbol.getParent()));
                }
                mapMenuAdd.put(menuArbol.getId(),null);
                listaMenuIdAdd.add(menuArbol.getId());
            }

        });

        SegPerfil perfilSave = segPerfilRepository.findById(perfil.getIdPerfil()).get();
        List<SegMenu> listaMenuAdd = segMenuRepository.findByIdMenuIn(listaMenuIdAdd);

        /*Llenamos los map agregar con su objeto menú*/
        listaMenuAdd.forEach(menu->{
            if (mapMenuAdd.containsKey(menu.getIdMenu()))
                mapMenuAdd.put(menu.getIdMenu(), menu);
        });


        if (listaMenuAdd.size()==0){
            //Borramos todos los perfiles. Ya que la lista viene vacía
            segPerfilPrivilegioRepository.deleteBySegPerfil(perfilSave);

            array.add("Guardado exitosamente");
            return array;
        }else {

            /*Lista de los privilegios de ese perfil que estan en la lista que se desea agregar*/
            List<SegPerfilPrivilegio> listaPrivilegiosExisten = segPerfilPrivilegioRepository.findBySegPerfilAndSegMenuIn(perfilSave, listaMenuAdd);

            /*Lista de los privilegios cuyo menu no viene en la lista y antes estaban agregados para este perfil*/
            List<SegPerfilPrivilegio> listaPrivilegiosBorrar =  segPerfilPrivilegioRepository.findPorPerfilAndListaMenuNotInPorTipo(perfilSave, listaMenuAdd, "MENU");


            /*Se remueven del Map los menus que ya están guardados en la base de datos*/
            listaPrivilegiosExisten.forEach(privilegio-> mapMenuAdd.remove(privilegio.getSegMenu().getIdMenu()));


            /*Se forman los privilegios que se van agregar*/
            List<SegPerfilPrivilegio> listaPerfilPrivilegioAdd = new ArrayList<>();
            mapMenuAdd.forEach((k,v)->{
                SegPerfilPrivilegio privilegio = new SegPerfilPrivilegio();
                privilegio.setSegMenu(v);
                privilegio.setSegPerfil(perfilSave);
                privilegio.setEditar(true);
                privilegio.setEliminar(true);
                privilegio.setAgregar(true);
                privilegio.setConsultar(true);
                privilegio.setValidar(true);

                listaPerfilPrivilegioAdd.add(privilegio);

            });

            /*Elementos de Menu de Aplicación que  se van  borrar si es que se encuentran. En los nuevos perfiles ya no se agregan
              No son necesarios*/
            List<Integer> listaMenuAppBorrar = listaPrivilegiosBorrar.stream().map(p->p.getSegMenu().getIdMenu()).collect(Collectors.toList());

            List<SegMenu> listaMenuAplicacionDelete = segMenuRepository.findByidMenuPadreInAndTipoMenu(listaMenuAppBorrar, "APLICACION");

            if (listaMenuAplicacionDelete.size() > 0) {
                List<SegPerfilPrivilegio> listaPrivilegiosMenuAppBorrar = segPerfilPrivilegioRepository.findPorPerfilAndListaMenuInPorTipo(perfilSave, listaMenuAplicacionDelete, "APLICACION");
                segPerfilPrivilegioRepository.deleteAll(listaPrivilegiosMenuAppBorrar);
            }

            segPerfilPrivilegioRepository.deleteAll(listaPrivilegiosBorrar);
            segPerfilPrivilegioRepository.saveAll(listaPerfilPrivilegioAdd);

            array.add("Guardado exitosamente");
            return array;
        }
    }

    @Override
    public SegPerfilPrivilegio actualizarPerfilPrivilegio(Long idPerfilPrivilegio, String tipo, Boolean valor) {

        SegPerfilPrivilegio perfilPrivilegio = segPerfilPrivilegioRepository.findById(idPerfilPrivilegio).get();

        if (tipo.equals("Ed_"))
            perfilPrivilegio.setEditar(valor);

        if (tipo.equals("El_"))
            perfilPrivilegio.setEliminar(valor);

        if (tipo.equals("Ag_"))
            perfilPrivilegio.setAgregar(valor);

        if (tipo.equals("Co_"))
            perfilPrivilegio.setConsultar(valor);

        if (tipo.equals("Va_"))
            perfilPrivilegio.setValidar(valor);

        return segPerfilPrivilegioRepository.save(perfilPrivilegio);

    }

/*    @Override
    public boolean existeRolAsignado(String rol) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authUser.getAuthorities().stream().anyMatch(p-> ((GrantedAuthority) p).getAuthority().equals(rol));
    }*/

}