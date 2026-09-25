package mx.gob.tabasco.cmaig.sigtic.sas.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.Arbol;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.ArbolRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegAccesoUsuario;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.SegPermisosBean;
import mx.gob.tabasco.cmaig.sigtic.sas.repository.SegAccesoUsuarioRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SegAccesoUsuarioServiceImp implements SegAccesoUsuarioService {

    @Autowired
    private SegAccesoUsuarioRepository segAccesoUsuarioRepository;

    @Autowired
    private ArbolRepository arbolRepository;


    @Override
    public SegAccesoUsuario addPermisosTreeAll(SegPermisosBean segPermisosBean) {

        Optional<SegAccesoUsuario> segAccesoUsuarioOp = segAccesoUsuarioRepository.findByRfcUsuarioAndIdEntePublicoAndIdUnidadAdministrativaAndIdSistema(segPermisosBean.getCurp(),
                segPermisosBean.getIdEntePublico(),segPermisosBean.getIduUnidadAdministrativa(),segPermisosBean.getIdSeguridadSistema());

        if (segAccesoUsuarioOp.isPresent())
            throw new NoSuchElementException("El permiso ya esta asignado.");

        SegAccesoUsuario segAccesoUsuario = new SegAccesoUsuario();
        segAccesoUsuario.setIdEntePublico(segPermisosBean.getIdEntePublico());
        segAccesoUsuario.setIdUnidadAdministrativa(segPermisosBean.getIduUnidadAdministrativa());
        segAccesoUsuario.setRfcUsuario(segPermisosBean.getCurp());
        segAccesoUsuario.setIdSistema(segPermisosBean.getIdSeguridadSistema());
        segAccesoUsuario.setCveEstatus("VIG");
        return  segAccesoUsuarioRepository.save(segAccesoUsuario);
    }

    @Override
    public JSONObject deletePermisosTreeAll(SegPermisosBean segPermisosBean) {

        Optional<SegAccesoUsuario> segAccesoUsuarioOp = segAccesoUsuarioRepository.findByRfcUsuarioAndIdUnidadAdministrativaAndIdSistema(segPermisosBean.getCurp(), segPermisosBean.getIduUnidadAdministrativa(),segPermisosBean.getIdSeguridadSistema());

        if (segAccesoUsuarioOp.isPresent())
            segAccesoUsuarioRepository.delete(segAccesoUsuarioOp.get());
        else
            throw new NoSuchElementException("El permiso ya no existe.");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message","El permiso fue removido exitosamente");
        return jsonObject;
    }

    @Override
    public List<Long> getUnidadesAdministrativaAcceso(String rfc, Long idEntePublico) {

        List<SegAccesoUsuario> listaAcceso = segAccesoUsuarioRepository.findAllByRfcUsuarioAndIdSistemaAndIdEntePublicoAndCveEstatus(rfc,"SIGTIC", idEntePublico,"VIG");

       List<Long> listaUnidades=new ArrayList<>();

        listaAcceso.forEach(p->{

            List<Arbol> listaArbol = arbolRepository.getArbolConParent(p.getIdUnidadAdministrativa());

            listaUnidades.add(p.getIdUnidadAdministrativa());

            if (listaArbol.size()>0) {
                listaUnidades.addAll(listaArbol.stream().map(a -> a.getId()).map(a2->Long.parseLong(a2)).collect(Collectors.toList()));
            }
        });

         List<Long> listaUnidadesFinal= listaUnidades.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList());

        return listaUnidadesFinal;
    }
}
