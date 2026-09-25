package mx.gob.tabasco.cmaig.sigtic.sas.service;

import mx.gob.tabasco.cmaig.sigtic.sas.model.SegMenuArbol;
import mx.gob.tabasco.cmaig.sigtic.sas.model.SegPerfil;
import mx.gob.tabasco.cmaig.sigtic.sas.pojo.SegMenuUsuarioBean;

import java.util.List;

public interface SegMenuService {

    List<SegMenuUsuarioBean> getMenuUsuarioSesion(String usuario);

    List<SegMenuArbol> getMenuArbol(SegPerfil perfil);

}
