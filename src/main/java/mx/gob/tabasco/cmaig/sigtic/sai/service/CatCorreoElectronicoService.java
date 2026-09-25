package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatCorreoElectronico;
import mx.gob.tabasco.cmaig.sigtic.sai.model.CatCorreoElectronicoBean;
import mx.gob.tabasco.cmaig.sigtic.sai.model.UnidadAdministrativa;
import mx.gob.tabasco.cmaig.sigtic.sai.model.Usuario;
import net.minidev.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CatCorreoElectronicoService {

    List<Usuario> getCatUsuariosPorUnidadAdministrativaCorreo(UnidadAdministrativa unidadAdministrativa);

    List<CatCorreoElectronicoBean> obtenerCorreosElectronicos(Long idEntePublico, Long idUnidadAdministrativa);

    List<CatCorreoElectronicoBean> obtenerCorreosElectronicosModal(Long idEntePublico, Long idUnidadAdministrativa, Integer idTipoCorreo, String estatusCorreo);

    List<CatCorreoElectronicoBean> obtenerCorreosElectronicosEnteNulo();

    List<CatCorreoElectronicoBean> obtenerCorreosElectronicosSearch(String correo, Long idEntePublico);

    List<CatCorreoElectronicoBean> obtenerCorreosElectronicosSearchAdmin(String correo);

    CatCorreoElectronico getCorreoElectronico(Long idCorreoElectronico);

    CatCorreoElectronico saveCorreoElectronico(CatCorreoElectronico catCorreoElectronico);

    JSONObject deleteCorreoElectronico(CatCorreoElectronico catCorreoElectronico);

    JSONObject getConcentradoTipoCorreo(Long idEntePublico);

    JSONObject getConcentradoTipoCorreoAll();

    CatCorreoElectronico vigCorreoElectronico(CatCorreoElectronico catCorreoElectronico);

    CatCorreoElectronico novCorreoElectronico(CatCorreoElectronico catCorreoElectronico);

    void generaReporteCorreos(Long idEntePublico, Long idUnidadAdministrativa, HttpServletResponse response);

    JSONObject getListaCorreoGroupTablero(Long idEntePublico, Long idUnidadAdministrativa);

}
