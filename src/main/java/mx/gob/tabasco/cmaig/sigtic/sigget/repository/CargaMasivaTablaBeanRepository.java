package mx.gob.tabasco.cmaig.sigtic.sigget.repository;

import mx.gob.tabasco.cmaig.sigtic.sigget.model.CargaDatosPlantillaError;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.CargaMasivaTablaBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.sql.ResultSet;
import java.util.List;

@Transactional(value = "siggetTransactionManager", readOnly = true)
@Repository
public class CargaMasivaTablaBeanRepository {

    @Autowired
    @PersistenceContext(unitName = "siggetEntityManagerFactory")
    private EntityManager em;

    public  List<CargaMasivaTablaBean> getCargaMasivaBean(Long idPlantilla,
                                                    String rfc){

        StoredProcedureQuery p = em.createStoredProcedureQuery("PKG_CARGAS_MASIVA.PR_VISUALIZA_CARGA_BEAN", CargaMasivaTablaBean.class);
        p.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        p.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        p.registerStoredProcedureParameter(3, ResultSet.class, ParameterMode.REF_CURSOR);
        p.setParameter(1,idPlantilla);
        p.setParameter(2,rfc);
        List<CargaMasivaTablaBean> lista = p.getResultList();

        return lista;

    }

    public  String ejecutaSql(Long  idCargaDatos, String userName, Long idEntePublcio){


        StoredProcedureQuery p = em.createStoredProcedureQuery("PKG_CARGAS_MASIVA.EJECUTA_SQL", CargaMasivaTablaBean.class);

        p.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        p.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        p.registerStoredProcedureParameter(3, Long.class, ParameterMode.IN);
        p.registerStoredProcedureParameter(4, String.class, ParameterMode.OUT);
        p.setParameter(1,idCargaDatos);
        p.setParameter(2,userName);
        p.setParameter(3,idEntePublcio);

        String resultado = (String) p.getOutputParameterValue(4);

        return resultado;

    }

}