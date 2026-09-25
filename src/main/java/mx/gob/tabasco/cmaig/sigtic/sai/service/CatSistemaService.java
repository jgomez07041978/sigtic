package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatSistema;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CatSistemaService {

    List<CatSistema> getListaSistemasAll(String rfc, Long idEntePublico, Long idUnidadAdministrativa);

    List<CatSistema> getListaSistemasAdmin(String rfc, Long idEntePublico, boolean roleAdminSigtic);

    List<CatSistema> getListaSistemasOpera(String rfc, Long idEntePublico, boolean roleAdminSigtic);

    List<CatSistema> getListaSistemaPadre(Long idEntePublico);

    CatSistema guardarSistema(CatSistema catSistema);

    JSONObject deleteSistema(CatSistema catSistema);

    CatSistema getSistema(Long idSistema);

    List<CatSistema> getListaSistemasHijos(Long idSistema);

    ResponseEntity<byte[]> getReporte(Long id);

    List<CatSistema> getListaSistemas(String estatus);


}
