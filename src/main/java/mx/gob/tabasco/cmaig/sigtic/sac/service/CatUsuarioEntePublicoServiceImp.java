package mx.gob.tabasco.cmaig.sigtic.sac.service;

import mx.gob.tabasco.cmaig.sigtic.sac.model.*;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.*;
import mx.gob.tabasco.cmaig.sigtic.sai.model.Usuario;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.UsuarioRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CatUsuarioEntePublicoServiceImp implements CatUsuarioEntePublicoService {

    @Autowired
    private CatUsuarioRepository catUsuarioRepository;

    @Autowired
    private CatUsuarioEntePublicoRepository catUsuarioEntePublicoRepository;

    @Autowired
    @Qualifier("catUsuarioEntePublicoRepositorySai")
    private mx.gob.tabasco.cmaig.sigtic.sai.repository.CatUsuarioEntePublicoRepository catUsuarioEntePublicoRepositorySai;

    @Autowired
    @Qualifier("entePublicoRepositorySac")
    private EntePublicoRepository entePublicoRepository;

    @Autowired
    @Qualifier("entePublicoRepositorySai")
    private mx.gob.tabasco.cmaig.sigtic.sai.repository.EntePublicoRepository entePublicoRepositorySai;

    @Autowired
    private UnidadAdministrativaRepository unidadAdministrativaRepository;

    @Autowired
    private CatUnidadAdministrativaSqlRepository catUnidadAdministrativaSqlRepository;

    @Autowired
    private PuestoUsuarioRepository puestoUsuarioRepository;

    @Autowired
    private UbicacionUnidadAdminRepository ubicacionUnidadAdminRepository;

    @Autowired
    private CatUsuarioSqlRepository catUsuarioSqlRepository;

    @Autowired
    private CategoriaRepository CategoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public CatUsuarioEntePublico actualizarPerfil(CatUsuarioEntePublico catUsuarioEntePublico) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CatUsuario catUsuario= catUsuarioRepository.findById(authUser.getCatUsuarioEntePublico().getCatUsuario().getIdUsuario()).get();
        catUsuario.setNombre(catUsuarioEntePublico.getCatUsuario().getNombre());
        catUsuario.setSegundoNombre(catUsuarioEntePublico.getCatUsuario().getSegundoNombre());
        catUsuario.setApellidoPaterno(catUsuarioEntePublico.getCatUsuario().getApellidoPaterno());
        catUsuario.setApellidoMaterno(catUsuarioEntePublico.getCatUsuario().getApellidoMaterno());
        catUsuario.setCurp(catUsuarioEntePublico.getCatUsuario().getCurp());
        catUsuario.setRfc(catUsuarioEntePublico.getCatUsuario().getRfc());
        catUsuario.setCorreoPersonal(catUsuarioEntePublico.getCatUsuario().getCorreoPersonal());
        catUsuario.setNumCelular(catUsuarioEntePublico.getCatUsuario().getNumCelular());
        catUsuarioRepository.save(catUsuario);

        CatUsuarioEntePublico catUsuarioEntePublicoActual = catUsuarioEntePublicoRepository.findById(authUser.getCatUsuarioEntePublico().getIdUsuarioEntePublico()).get();

        catUsuarioEntePublicoActual.setCorreoInstitucional(catUsuarioEntePublico.getCorreoInstitucional());
        catUsuarioEntePublicoActual.setNumTelefonoInstitucional(catUsuarioEntePublico.getNumTelefonoInstitucional());
        catUsuarioEntePublicoActual.setNumeroExtensionInstitucional(catUsuarioEntePublico.getNumeroExtensionInstitucional());
        catUsuarioEntePublicoActual=catUsuarioEntePublicoRepository.save(catUsuarioEntePublicoActual);

        authUser.setCatUsuarioEntePublico(catUsuarioEntePublicoActual);

        return catUsuarioEntePublicoActual;
    }

    @Override
    public CatUsuarioEntePublico validarUsusario(CatUsuarioEntePublico catUsuarioEntePublico) {

        Optional<CatUsuario> catUsuarioRfcOp = catUsuarioRepository.findByRfc(catUsuarioEntePublico.getCatUsuario().getRfc());
        EntePublico entePublico = entePublicoRepository.findById(catUsuarioEntePublico.getEntePublico().getIdEntePublico()).get();

        if (catUsuarioRfcOp.isPresent()){
            Optional<CatUsuarioEntePublico>  catUsuarioEntePublicoExiste = catUsuarioEntePublicoRepository.findByEntePublicoAndCatUsuario(entePublico, catUsuarioRfcOp.get());

            if (catUsuarioEntePublicoExiste.isPresent()) {
                    return catUsuarioEntePublicoExiste.get();
            }
            else{
                catUsuarioEntePublico.setCatUsuario(catUsuarioRfcOp.get());
                return catUsuarioEntePublico;
            }

        }
        else
            return catUsuarioEntePublico;

    }

    @Override
    public List<CatUsuario> getCatUsuarioPorIdUnidad(Long idUnidad) {

        List<CatUnidadAdministrativaSql> listaUnidadesAdministrativas= catUnidadAdministrativaSqlRepository.getUnidadesAdministrativasArbolSigtic(idUnidad);

        List<Long> listaIdUnidad = listaUnidadesAdministrativas.stream().map(CatUnidadAdministrativaSql::getIdUnidadAdministrativa).collect(Collectors.toList());

        if (listaIdUnidad.size()>0){
            List<UnidadAdministrativa> listaUnidad=(List<UnidadAdministrativa>) unidadAdministrativaRepository.findAllById(listaIdUnidad);

            List<CatUsuarioEntePublico> listaCatUsuarioEnte = catUsuarioEntePublicoRepository.getListaUsuariosPorUnidadAdmin(listaUnidad,"VIG");

            List<CatUsuario> listaCatUsuario = listaCatUsuarioEnte.stream().map(CatUsuarioEntePublico::getCatUsuario).collect(Collectors.toList());

            return listaCatUsuario;
        }else
            return new ArrayList<>();
    }

    @Override
    public CatUsuarioEntePublico guardarUsuario(CatUsuarioEntePublico catUsuarioEntePublico) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        catUsuarioEntePublico.getCatUsuario().setRfc(catUsuarioEntePublico.getCatUsuario().getRfc().toUpperCase().trim());
        catUsuarioEntePublico.getCatUsuario().setCurp(catUsuarioEntePublico.getCatUsuario().getCurp().toUpperCase().trim());

        if (catUsuarioEntePublico.getCatUsuario().getRfc().length()!=10 && catUsuarioEntePublico.getCatUsuario().getRfc().length()!=13)
            throw new NoSuchElementException("El RFC debe ser de 10 caracteres si no cuenta con homoclave y de 13 caracteres si cuenta con homoclave");

        if (catUsuarioEntePublico.getCatUsuario().getCurp().length()!=18)
            throw new NoSuchElementException("La CURP debe ser de 18 caracteres");


        Optional<CatUsuario> catUsuarioRfcOp = catUsuarioRepository.findByRfc(catUsuarioEntePublico.getCatUsuario().getRfc());
        Optional<CatUsuario> catUsuarioCurpOp = catUsuarioRepository.findByCurp(catUsuarioEntePublico.getCatUsuario().getCurp());
        EntePublico entePublico = entePublicoRepository.findById(catUsuarioEntePublico.getEntePublico().getIdEntePublico()).get();


        if (catUsuarioEntePublico.getIdUsuarioEntePublico()==null){
            /* Usuario Nuevo */
            /*No deberia suceder pero se ealiza la validación*/
            if (catUsuarioRfcOp.isPresent()){
                Optional<CatUsuarioEntePublico>  catUsuarioEntePublicoRfcExiste = catUsuarioEntePublicoRepository.findByEntePublicoAndCatUsuario(entePublico, catUsuarioRfcOp.get());
                if (catUsuarioEntePublicoRfcExiste.isPresent())
                    throw new NoSuchElementException("El RFC esta registrado con otro usuario");

            }

            /*No deberia suceder pero se ealiza la validación*/
            if (catUsuarioCurpOp.isPresent()){
                Optional<CatUsuarioEntePublico>  catUsuarioEntePublicoCurpExiste = catUsuarioEntePublicoRepository.findByEntePublicoAndCatUsuario(entePublico, catUsuarioCurpOp.get());
                if (catUsuarioEntePublicoCurpExiste.isPresent())
                    throw new NoSuchElementException("La CURP esta registrada con otro usuario");
            }

            if (catUsuarioRfcOp.isPresent()){
                catUsuarioRfcOp.get().setCurp(catUsuarioEntePublico.getCatUsuario().getCurp());
                catUsuarioRfcOp.get().setNombre(catUsuarioEntePublico.getCatUsuario().getNombre());
                catUsuarioRfcOp.get().setSegundoNombre(catUsuarioEntePublico.getCatUsuario().getSegundoNombre());
                catUsuarioRfcOp.get().setApellidoPaterno(catUsuarioEntePublico.getCatUsuario().getApellidoPaterno());
                catUsuarioRfcOp.get().setApellidoMaterno(catUsuarioEntePublico.getCatUsuario().getApellidoMaterno());
                catUsuarioRfcOp.get().setFechaNacimiento(catUsuarioEntePublico.getCatUsuario().getFechaNacimiento());
                catUsuarioRfcOp.get().setCveSexo(catUsuarioEntePublico.getCatUsuario().getCveSexo());
                catUsuarioRfcOp.get().setNumCelular(catUsuarioEntePublico.getCatUsuario().getNumCelular());
                catUsuarioRfcOp.get().setNumTelefonoPersonal(catUsuarioEntePublico.getCatUsuario().getNumTelefonoPersonal());
                catUsuarioRfcOp.get().setCorreoPersonal(catUsuarioEntePublico.getCatUsuario().getCorreoPersonal());

                CatUsuario catUsuarioSave = catUsuarioRepository.save(catUsuarioRfcOp.get());
                catUsuarioEntePublico.setCatUsuario(catUsuarioSave);
            }
            else{
                CatUsuario catUsuarioSave = catUsuarioRepository.save(catUsuarioEntePublico.getCatUsuario());
                catUsuarioEntePublico.setCatUsuario(catUsuarioSave);
            }

            if (catUsuarioEntePublico.getUnidadAdministrativa()!=null && catUsuarioEntePublico.getUnidadAdministrativa().getIdUnidadAdministrativa()!=null){
                UnidadAdministrativa unidadAdministrativa = unidadAdministrativaRepository.findById(catUsuarioEntePublico.getUnidadAdministrativa().getIdUnidadAdministrativa()).get();
                catUsuarioEntePublico.setUnidadAdministrativa(unidadAdministrativa);
            }

            catUsuarioEntePublico.setPuesto(catUsuarioEntePublico.getPuesto());

            catUsuarioEntePublico.setUbicacionUnidadAdmin(null);
            if (catUsuarioEntePublico.getUbicacionUnidadAdmin()!=null && catUsuarioEntePublico.getUbicacionUnidadAdmin().getIdUbicacionAdaministrativa()!=null){
                UbicacionUnidadAdmin ubicacionUnidadAdmin = ubicacionUnidadAdminRepository.findById(catUsuarioEntePublico.getUbicacionUnidadAdmin().getIdUbicacionAdaministrativa()).get();
                catUsuarioEntePublico.setUbicacionUnidadAdmin(ubicacionUnidadAdmin);
            }

            catUsuarioEntePublico.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            catUsuarioEntePublico.setIdUsuarioRegistro(authUser.getCatUsuarioEntePublico().getCatUsuario().getIdUsuario());

            catUsuarioEntePublico.setUnidadAdministrativaPptal(null);
            if (catUsuarioEntePublico.getUnidadAdministrativaPptal()!=null && catUsuarioEntePublico.getUnidadAdministrativaPptal().getIdUnidadAdministrativa()!=null){
                UnidadAdministrativa unidadAdministrativaPptal = unidadAdministrativaRepository.findById(catUsuarioEntePublico.getUnidadAdministrativaPptal().getIdUnidadAdministrativa()).get();
                catUsuarioEntePublico.setUnidadAdministrativaPptal(unidadAdministrativaPptal);
            }

            catUsuarioEntePublico.setEntePublicoResguardante(null);
            if (catUsuarioEntePublico.getEntePublicoResguardante()!=null && catUsuarioEntePublico.getEntePublicoResguardante().getIdEntePublico()!=null){
                EntePublico entePublicoResg = entePublicoRepository.findById(catUsuarioEntePublico.getEntePublicoResguardante().getIdEntePublico()).get();
                catUsuarioEntePublico.setEntePublicoResguardante(entePublicoResg);
            }

            catUsuarioEntePublico.setUnidadAdministrativaResguardante(null);
            if (catUsuarioEntePublico.getUnidadAdministrativaResguardante()!=null && catUsuarioEntePublico.getUnidadAdministrativaResguardante().getIdUnidadAdministrativa()!=null){
                UnidadAdministrativa unidadAdministrativaResg = unidadAdministrativaRepository.findById(catUsuarioEntePublico.getUnidadAdministrativaResguardante().getIdUnidadAdministrativa()).get();
                catUsuarioEntePublico.setUnidadAdministrativaResguardante(unidadAdministrativaResg);
            }

            catUsuarioEntePublico.setCatUsuarioResguardante(null);
            if (catUsuarioEntePublico.getCatUsuarioResguardante()!=null && catUsuarioEntePublico.getCatUsuarioResguardante().getIdUsuario()!=null){
                CatUsuario catUsuarioResg = catUsuarioRepository.findById(catUsuarioEntePublico.getCatUsuarioResguardante().getIdUsuario()).get();
                catUsuarioEntePublico.setCatUsuarioResguardante(catUsuarioResg);
            }


            return catUsuarioEntePublicoRepository.save(catUsuarioEntePublico);
        }
        else{
            /* Actualizando Usuario*/
            CatUsuarioEntePublico  catUsuarioEntePublicoUpdate = catUsuarioEntePublicoRepository.findById(catUsuarioEntePublico.getIdUsuarioEntePublico()).get();

            if (catUsuarioCurpOp.isPresent() && catUsuarioCurpOp.get().getIdUsuario().longValue()!=catUsuarioRfcOp.get().getIdUsuario().longValue()){
                throw new NoSuchElementException("La CURP esta registrada con otro usuario");
            }else{

                CatUsuario catUsuario = catUsuarioRepository.findById(catUsuarioRfcOp.get().getIdUsuario()).get();

                catUsuario.setCurp(catUsuarioEntePublico.getCatUsuario().getCurp());
                catUsuario.setNombre(catUsuarioEntePublico.getCatUsuario().getNombre());
                catUsuario.setSegundoNombre(catUsuarioEntePublico.getCatUsuario().getSegundoNombre());
                catUsuario.setApellidoPaterno(catUsuarioEntePublico.getCatUsuario().getApellidoPaterno());
                catUsuario.setApellidoMaterno(catUsuarioEntePublico.getCatUsuario().getApellidoMaterno());
                catUsuario.setFechaNacimiento(catUsuarioEntePublico.getCatUsuario().getFechaNacimiento());
                catUsuario.setCveSexo(catUsuarioEntePublico.getCatUsuario().getCveSexo());
                catUsuario.setNumCelular(catUsuarioEntePublico.getCatUsuario().getNumCelular());
                catUsuario.setNumTelefonoPersonal(catUsuarioEntePublico.getCatUsuario().getNumTelefonoPersonal());
                catUsuario.setCorreoPersonal(catUsuarioEntePublico.getCatUsuario().getCorreoPersonal());

                catUsuarioEntePublicoUpdate.setNumTelefonoInstitucional(catUsuarioEntePublico.getNumTelefonoInstitucional());
                catUsuarioEntePublicoUpdate.setNumeroExtensionInstitucional(catUsuarioEntePublico.getNumeroExtensionInstitucional());
                catUsuarioEntePublicoUpdate.setCorreoInstitucional(catUsuarioEntePublico.getCorreoInstitucional());

                CatUsuario catUsuarioSave = catUsuarioRepository.save(catUsuario);
                catUsuarioEntePublicoUpdate.setCatUsuario(catUsuarioSave);

                if (catUsuarioEntePublico.getUnidadAdministrativa()!=null && catUsuarioEntePublico.getUnidadAdministrativa().getIdUnidadAdministrativa()!=null){
                    UnidadAdministrativa unidadAdministrativa = unidadAdministrativaRepository.findById(catUsuarioEntePublico.getUnidadAdministrativa().getIdUnidadAdministrativa()).get();
                    catUsuarioEntePublicoUpdate.setUnidadAdministrativa(unidadAdministrativa);
                }

                catUsuarioEntePublicoUpdate.setPuesto(catUsuarioEntePublico.getPuesto());

                catUsuarioEntePublicoUpdate.setUbicacionUnidadAdmin(null);
                if (catUsuarioEntePublico.getUbicacionUnidadAdmin()!=null && catUsuarioEntePublico.getUbicacionUnidadAdmin().getIdUbicacionAdaministrativa()!=null){
                    UbicacionUnidadAdmin ubicacionUnidadAdmin = ubicacionUnidadAdminRepository.findById(catUsuarioEntePublico.getUbicacionUnidadAdmin().getIdUbicacionAdaministrativa()).get();
                    catUsuarioEntePublicoUpdate.setUbicacionUnidadAdmin(ubicacionUnidadAdmin);
                }

                catUsuarioEntePublicoUpdate.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
                catUsuarioEntePublicoUpdate.setIdUsuarioRegistro(authUser.getCatUsuarioEntePublico().getCatUsuario().getIdUsuario());

                catUsuarioEntePublicoUpdate.setUnidadAdministrativaPptal(null);
                if (catUsuarioEntePublico.getUnidadAdministrativaPptal()!=null && catUsuarioEntePublico.getUnidadAdministrativaPptal().getIdUnidadAdministrativa()!=null){
                    UnidadAdministrativa unidadAdministrativaPptal = unidadAdministrativaRepository.findById(catUsuarioEntePublico.getUnidadAdministrativaPptal().getIdUnidadAdministrativa()).get();
                    catUsuarioEntePublicoUpdate.setUnidadAdministrativaPptal(unidadAdministrativaPptal);
                }

                catUsuarioEntePublicoUpdate.setEntePublicoResguardante(null);
                if (catUsuarioEntePublico.getEntePublicoResguardante()!=null && catUsuarioEntePublico.getEntePublicoResguardante().getIdEntePublico()!=null){
                    EntePublico entePublicoResg = entePublicoRepository.findById(catUsuarioEntePublico.getEntePublicoResguardante().getIdEntePublico()).get();
                    catUsuarioEntePublicoUpdate.setEntePublicoResguardante(entePublicoResg);
                }

                catUsuarioEntePublicoUpdate.setUnidadAdministrativaResguardante(null);
                if (catUsuarioEntePublico.getUnidadAdministrativaResguardante()!=null && catUsuarioEntePublico.getUnidadAdministrativaResguardante().getIdUnidadAdministrativa()!=null){
                    UnidadAdministrativa unidadAdministrativaResg = unidadAdministrativaRepository.findById(catUsuarioEntePublico.getUnidadAdministrativaResguardante().getIdUnidadAdministrativa()).get();
                    catUsuarioEntePublicoUpdate.setUnidadAdministrativaResguardante(unidadAdministrativaResg);
                }

                catUsuarioEntePublicoUpdate.setCatUsuarioResguardante(null);
                if (catUsuarioEntePublico.getCatUsuarioResguardante()!=null && catUsuarioEntePublico.getCatUsuarioResguardante().getIdUsuario()!=null){
                    CatUsuario catUsuarioResg = catUsuarioRepository.findById(catUsuarioEntePublico.getCatUsuarioResguardante().getIdUsuario()).get();
                    catUsuarioEntePublicoUpdate.setCatUsuarioResguardante(catUsuarioResg);
                }
                catUsuarioEntePublicoUpdate.setCveEstatus(catUsuarioEntePublico.getCveEstatus());

                return catUsuarioEntePublicoRepository.save(catUsuarioEntePublicoUpdate);
            }

        }

    }

    @Transactional(value = "catalogosTransactionManager")
    @Override
    public CatUsuarioEntePublico guardarUsuarioEntePublico(CatUsuarioEntePublico catUsuarioEntePublico) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CatUsuario catUsuario=catUsuarioRepository.findById(catUsuarioEntePublico.getCatUsuario().getIdUsuario()).get();
        catUsuario.setNombre(catUsuarioEntePublico.getCatUsuario().getNombre());
        catUsuario.setSegundoNombre(catUsuarioEntePublico.getCatUsuario().getSegundoNombre());
        catUsuario.setApellidoPaterno(catUsuarioEntePublico.getCatUsuario().getApellidoPaterno());
        catUsuario.setApellidoMaterno(catUsuarioEntePublico.getCatUsuario().getApellidoMaterno());
        catUsuario.setFechaNacimiento(catUsuarioEntePublico.getCatUsuario().getFechaNacimiento());
        catUsuario.setCveSexo(catUsuarioEntePublico.getCatUsuario().getCveSexo());
        catUsuario.setNumCelular(catUsuarioEntePublico.getCatUsuario().getNumCelular());
        catUsuario.setNumTelefonoPersonal(catUsuarioEntePublico.getCatUsuario().getNumTelefonoPersonal());
        catUsuario.setCorreoPersonal(catUsuarioEntePublico.getCatUsuario().getCorreoPersonal());
        catUsuario.setCveEstatus(catUsuarioEntePublico.getCatUsuario().getCveEstatus());

        CatUsuario catUsuarioSave = catUsuarioRepository.save(catUsuario);

        EntePublico entePublico = entePublicoRepository.findById(catUsuarioEntePublico.getEntePublico().getIdEntePublico()).get();

        if (catUsuarioEntePublico.getIdUsuarioEntePublico()==null){
            /* Usuario Nuevo */
            /*No deberia suceder pero se ealiza la validación*/

                Optional<CatUsuarioEntePublico>  catUsuarioEntePublicoRfcExiste = catUsuarioEntePublicoRepository.findByEntePublicoAndCatUsuario(entePublico, catUsuario);
                if (catUsuarioEntePublicoRfcExiste.isPresent())
                    throw new NoSuchElementException("El RFC esta registrado con otro usuario");

                catUsuarioEntePublico.setCatUsuario(catUsuarioSave);

            if (catUsuarioEntePublico.getCategoria()!=null && catUsuarioEntePublico.getCategoria().getIdCategoriaUsuario()!=null){
                Categoria categoria = CategoriaRepository.findById(catUsuarioEntePublico.getCategoria().getIdCategoriaUsuario()).get();
                catUsuarioEntePublico.setCategoria(categoria);
            }

            //catUsuarioEntePublico.setUnidadAdministrativa(null);
            if (catUsuarioEntePublico.getUnidadAdministrativa()!=null && catUsuarioEntePublico.getUnidadAdministrativa().getIdUnidadAdministrativa()!=null){
                UnidadAdministrativa unidadAdministrativa = unidadAdministrativaRepository.findById(catUsuarioEntePublico.getUnidadAdministrativa().getIdUnidadAdministrativa()).get();
                catUsuarioEntePublico.setUnidadAdministrativa(unidadAdministrativa);
            }


            catUsuarioEntePublico.setUbicacionUnidadAdmin(null);
            if (catUsuarioEntePublico.getUbicacionUnidadAdmin()!=null && catUsuarioEntePublico.getUbicacionUnidadAdmin().getIdUbicacionAdaministrativa()!=null){
                UbicacionUnidadAdmin ubicacionUnidadAdmin = ubicacionUnidadAdminRepository.findById(catUsuarioEntePublico.getUbicacionUnidadAdmin().getIdUbicacionAdaministrativa()).get();
                catUsuarioEntePublico.setUbicacionUnidadAdmin(ubicacionUnidadAdmin);
            }

            catUsuarioEntePublico.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            catUsuarioEntePublico.setIdUsuarioRegistro(authUser.getCatUsuarioEntePublico().getCatUsuario().getIdUsuario());

            catUsuarioEntePublico.setUnidadAdministrativaPptal(null);
            if (catUsuarioEntePublico.getUnidadAdministrativaPptal()!=null && catUsuarioEntePublico.getUnidadAdministrativaPptal().getIdUnidadAdministrativa()!=null){
                UnidadAdministrativa unidadAdministrativaPptal = unidadAdministrativaRepository.findById(catUsuarioEntePublico.getUnidadAdministrativaPptal().getIdUnidadAdministrativa()).get();
                catUsuarioEntePublico.setUnidadAdministrativaPptal(unidadAdministrativaPptal);
            }

            CatUsuarioEntePublico catUsuarioEntePublicoSave = catUsuarioEntePublicoRepository.save(catUsuarioEntePublico);

            int totalUser = catUsuarioEntePublicoRepository.getCountUsuario(catUsuarioEntePublico.getCatUsuario().getIdUsuario());
            CatUsuario catUsuario1= catUsuarioRepository.findById(catUsuarioEntePublico.getCatUsuario().getIdUsuario()).get();
            catUsuario1.setTotalEntePublico(totalUser);
            catUsuarioRepository.save(catUsuario1);

            return catUsuarioEntePublicoSave;
        }
        else{
            /* Actualizando Usuario*/
            CatUsuarioEntePublico  catUsuarioEntePublicoUpdate = catUsuarioEntePublicoRepository.findById(catUsuarioEntePublico.getIdUsuarioEntePublico()).get();

                catUsuarioEntePublicoUpdate.setCatUsuario(catUsuarioSave);

                catUsuarioEntePublicoUpdate.setNumTelefonoInstitucional(catUsuarioEntePublico.getNumTelefonoInstitucional());
                catUsuarioEntePublicoUpdate.setNumeroExtensionInstitucional(catUsuarioEntePublico.getNumeroExtensionInstitucional());
                catUsuarioEntePublicoUpdate.setCorreoInstitucional(catUsuarioEntePublico.getCorreoInstitucional());

            if (catUsuarioEntePublico.getCategoria()!=null && catUsuarioEntePublico.getCategoria().getIdCategoriaUsuario()!=null){
                Categoria categoria = CategoriaRepository.findById(catUsuarioEntePublico.getCategoria().getIdCategoriaUsuario()).get();
                catUsuarioEntePublicoUpdate.setCategoria(categoria);
            }

                if (catUsuarioEntePublico.getUnidadAdministrativa()!=null && catUsuarioEntePublico.getUnidadAdministrativa().getIdUnidadAdministrativa()!=null){
                    UnidadAdministrativa unidadAdministrativa = unidadAdministrativaRepository.findById(catUsuarioEntePublico.getUnidadAdministrativa().getIdUnidadAdministrativa()).get();
                    catUsuarioEntePublicoUpdate.setUnidadAdministrativa(unidadAdministrativa);
                }

                catUsuarioEntePublicoUpdate.setPuesto(catUsuarioEntePublico.getPuesto());

                catUsuarioEntePublicoUpdate.setUbicacionUnidadAdmin(null);
                if (catUsuarioEntePublico.getUbicacionUnidadAdmin()!=null && catUsuarioEntePublico.getUbicacionUnidadAdmin().getIdUbicacionAdaministrativa()!=null){
                    UbicacionUnidadAdmin ubicacionUnidadAdmin = ubicacionUnidadAdminRepository.findById(catUsuarioEntePublico.getUbicacionUnidadAdmin().getIdUbicacionAdaministrativa()).get();
                    catUsuarioEntePublicoUpdate.setUbicacionUnidadAdmin(ubicacionUnidadAdmin);
                }

                catUsuarioEntePublicoUpdate.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
                catUsuarioEntePublicoUpdate.setIdUsuarioRegistro(authUser.getCatUsuarioEntePublico().getCatUsuario().getIdUsuario());

                catUsuarioEntePublicoUpdate.setUnidadAdministrativaPptal(null);
                if (catUsuarioEntePublico.getUnidadAdministrativaPptal()!=null && catUsuarioEntePublico.getUnidadAdministrativaPptal().getIdUnidadAdministrativa()!=null){
                    UnidadAdministrativa unidadAdministrativaPptal = unidadAdministrativaRepository.findById(catUsuarioEntePublico.getUnidadAdministrativaPptal().getIdUnidadAdministrativa()).get();
                    catUsuarioEntePublicoUpdate.setUnidadAdministrativaPptal(unidadAdministrativaPptal);
                }

                catUsuarioEntePublicoUpdate.setCveEstatus(catUsuarioEntePublico.getCveEstatus());

                catUsuarioEntePublicoUpdate = catUsuarioEntePublicoRepository.save(catUsuarioEntePublicoUpdate);

            int totalUser = catUsuarioEntePublicoRepository.getCountUsuario(catUsuarioEntePublicoUpdate.getCatUsuario().getIdUsuario());
            CatUsuario catUsuario1= catUsuarioRepository.findById(catUsuarioEntePublicoUpdate.getCatUsuario().getIdUsuario()).get();
            catUsuario1.setTotalEntePublico(totalUser);
            catUsuarioRepository.save(catUsuario1);

                return catUsuarioEntePublicoUpdate;


        }
    }

    @Override
    public CatUsuarioEntePublico getUsuario(Long idUsuarioEntepublico) {

        Optional<CatUsuarioEntePublico> catUsuarioEntePublicoOp= catUsuarioEntePublicoRepository.findById(idUsuarioEntepublico);

        if (!catUsuarioEntePublicoOp.isPresent())
            throw new NoSuchElementException("El registro no existe");

        return catUsuarioEntePublicoOp.get();
    }

    @Override
    public CatUsuarioEntePublico deleteUsuario(CatUsuarioEntePublico catUsuarioEntePublico) {
        Optional<CatUsuarioEntePublico> catUsuarioEntePublicoOp= catUsuarioEntePublicoRepository.findById(catUsuarioEntePublico.getIdUsuarioEntePublico());

        if (!catUsuarioEntePublicoOp.isPresent())
            throw new NoSuchElementException("El registro no existe");

        if (catUsuarioEntePublicoOp.get().getCveEstatus().equals("NOV"))
            throw new NoSuchElementException("El usuario esta eliminado");

        catUsuarioEntePublicoOp.get().setCveEstatus("NOV");

        return catUsuarioEntePublicoRepository.save(catUsuarioEntePublicoOp.get());
    }

    @Override
    public List<CatUsuarioSql> getUsuariosPorEnte(CatUsuarioEntePublico catUsuarioEntePublico) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<String> listaEstatus=new ArrayList<>();

        if (catUsuarioEntePublico.getIsVigente())
            listaEstatus.add("VIG");

        if (catUsuarioEntePublico.getIsNoVigente())
            listaEstatus.add("NOV");

        if (catUsuarioEntePublico.getIsHistorico())
            listaEstatus.add("HIS");

        List<CatUsuarioSql> lista=null;

        if (authUser.isAdministradorSigtic()){
            if (catUsuarioEntePublico.getEntePublico()!=null && catUsuarioEntePublico.getUnidadAdministrativa()==null) {
                if (catUsuarioEntePublico.getCatUsuario().getNombre().equals(""))
                    lista = catUsuarioSqlRepository.getUsuarioAdminEntidad(catUsuarioEntePublico.getEntePublico().getIdEntePublico(), listaEstatus);
                else
                    lista = catUsuarioSqlRepository.getUsuarioAdminEntidadNombre(catUsuarioEntePublico.getEntePublico().getIdEntePublico(), catUsuarioEntePublico.getCatUsuario().getNombre(), listaEstatus);
            }
            else
            if (catUsuarioEntePublico.getEntePublico()!=null && catUsuarioEntePublico.getUnidadAdministrativa()!=null){
                if (catUsuarioEntePublico.getCatUsuario().getNombre().equals(""))
                    lista = catUsuarioSqlRepository.getUsuarioAdminUnidad(catUsuarioEntePublico.getEntePublico().getIdEntePublico(), catUsuarioEntePublico.getUnidadAdministrativa().getIdUnidadAdministrativa(), listaEstatus);
                else
                    lista = catUsuarioSqlRepository.getUsuarioAdminUnidadNombre(catUsuarioEntePublico.getEntePublico().getIdEntePublico(), catUsuarioEntePublico.getUnidadAdministrativa().getIdUnidadAdministrativa(), catUsuarioEntePublico.getCatUsuario().getNombre(), listaEstatus);
            }
        }else{
            if (catUsuarioEntePublico.getEntePublico()!=null && catUsuarioEntePublico.getUnidadAdministrativa()==null) {
                if (catUsuarioEntePublico.getCatUsuario().getNombre().equals(""))
                    lista = catUsuarioSqlRepository.getUsuarioEntidad(catUsuarioEntePublico.getEntePublico().getIdEntePublico(), catUsuarioEntePublico.getEntePublico().getIdEntePublico(), authUser.getUsername(), listaEstatus);
                else
                    lista = catUsuarioSqlRepository.getUsuarioEntidadNombre(catUsuarioEntePublico.getEntePublico().getIdEntePublico(), catUsuarioEntePublico.getEntePublico().getIdEntePublico(), authUser.getUsername(),catUsuarioEntePublico.getCatUsuario().getNombre(), listaEstatus);
            }
            else
            if (catUsuarioEntePublico.getEntePublico()!=null && catUsuarioEntePublico.getUnidadAdministrativa()!=null){
                if (catUsuarioEntePublico.getCatUsuario().getNombre().equals(""))
                    lista = catUsuarioSqlRepository.getUsuarioEntidad(catUsuarioEntePublico.getEntePublico().getIdEntePublico(), catUsuarioEntePublico.getUnidadAdministrativa().getIdUnidadAdministrativa(), authUser.getUsername(), listaEstatus);
                else
                    lista = catUsuarioSqlRepository.getUsuarioEntidadNombre(catUsuarioEntePublico.getEntePublico().getIdEntePublico(), catUsuarioEntePublico.getUnidadAdministrativa().getIdUnidadAdministrativa(), authUser.getUsername(),catUsuarioEntePublico.getCatUsuario().getNombre(), listaEstatus);

            }
        }



        if (lista==null)
            lista= new ArrayList<>();

        return lista;
    }

    @Override
    public List<CatUsuarioEntePublico> getEntesPublicoPorUsuario(Long idUsuario) {
        CatUsuario catUsuario= catUsuarioRepository.findById(idUsuario).get();

        return catUsuarioEntePublicoRepository.findByCatUsuarioOrderByEntePublico(catUsuario);
    }

    @Override
    public CatUsuarioEntePublico getUsuarioEntePublico(Long idUsuario, Long idEntePublico) {

        Optional<CatUsuario> catUsuarioOp = catUsuarioRepository.findById(idUsuario);
        Optional<EntePublico> entePublicoOp = entePublicoRepository.findById(idEntePublico);

        if (catUsuarioOp.isPresent() && entePublicoOp.isPresent()){
            Optional<CatUsuarioEntePublico> catUsuarioEntePublicoOp = catUsuarioEntePublicoRepository.findByEntePublicoAndCatUsuario(entePublicoOp.get(),catUsuarioOp.get());

            if (catUsuarioEntePublicoOp.isPresent())
                return catUsuarioEntePublicoOp.get();
        }

        CatUsuarioEntePublico catUsuarioEntePublico = new CatUsuarioEntePublico();
        catUsuarioEntePublico.setCatUsuario(catUsuarioOp.get());

        return catUsuarioEntePublico;
    }

    @Override
    public Optional<CatUsuarioEntePublico> getUsuarioEntePublicoOptional(Long idUsuario, Long idEntePublico) {

        Optional<CatUsuario> catUsuarioOp = catUsuarioRepository.findById(idUsuario);
        Optional<EntePublico> entePublicoOp = entePublicoRepository.findById(idEntePublico);

        if (catUsuarioOp.isPresent() && entePublicoOp.isPresent()){
            Optional<CatUsuarioEntePublico> catUsuarioEntePublicoOp = catUsuarioEntePublicoRepository.findByEntePublicoAndCatUsuario(entePublicoOp.get(),catUsuarioOp.get());

                return catUsuarioEntePublicoOp;
        }

        return Optional.empty();
    }

    @Override
    public CatUsuarioEntePublico saveUsuario(CatUsuarioEntePublico catUsuarioEntePublico) {
        return catUsuarioEntePublicoRepository.save(catUsuarioEntePublico);
    }

    @Override
    public List<CatUsuarioEntePublico> saveUsuarioAll(List<CatUsuarioEntePublico> catUsuarioEntePublicoList) {
        return (List<CatUsuarioEntePublico>) catUsuarioEntePublicoRepository.saveAll(catUsuarioEntePublicoList);
    }

    @Override
    public CatUsuarioEntePublico getEntesPublicoVigentePorIdEnte(Long idUsuario, Long idEntePublico) {
        CatUsuario catUsuario= catUsuarioRepository.findById(idUsuario).get();
        EntePublico entePublico= entePublicoRepository.findById(idEntePublico).get();

        Optional<CatUsuarioEntePublico>  catUsuarioEntePublico = catUsuarioEntePublicoRepository.findByCatUsuarioAndEntePublico(catUsuario,entePublico);

        if (catUsuarioEntePublico.isPresent())
            return catUsuarioEntePublico.get();
        else
            return null;
    }

    @Override
    public mx.gob.tabasco.cmaig.sigtic.sai.model.CatUsuarioEntePublico getEntesPublicoVigentePorIdEnteSai(Long idUsuario, Long idEntePublico) {
        Usuario catUsuario= usuarioRepository.findById(idUsuario).get();
        mx.gob.tabasco.cmaig.sigtic.sai.model.EntePublico entePublico= entePublicoRepositorySai.findById(idEntePublico).get();

        Optional<mx.gob.tabasco.cmaig.sigtic.sai.model.CatUsuarioEntePublico>  catUsuarioEntePublico = catUsuarioEntePublicoRepositorySai.findByCatUsuarioAndEntePublico(catUsuario,entePublico);

        if (catUsuarioEntePublico.isPresent())
            return catUsuarioEntePublico.get();
        else
            return null;
    }

    @Override
    public List<CatUsuarioEntePublico> getListaCompletaUsuarios() {
        return catUsuarioEntePublicoRepository.findAllOrderByEntePublicoAndApellidos();
    }

    @Override
    public List<CatUsuarioEntePublico> getUsuariosParaExportar(CatUsuarioEntePublico filtro) {
        List<String> listaEstatus = new ArrayList<>();
        if (filtro.getIsVigente())   listaEstatus.add("VIG");
        if (filtro.getIsNoVigente()) listaEstatus.add("NOV");
        if (filtro.getIsHistorico()) listaEstatus.add("HIS");

        if (listaEstatus.isEmpty()) listaEstatus.add("VIG");

        Long idEntePublico = filtro.getEntePublico() != null ? filtro.getEntePublico().getIdEntePublico() : null;
        Long idUnidadAdministrativa = filtro.getUnidadAdministrativa() != null
                ? filtro.getUnidadAdministrativa().getIdUnidadAdministrativa() : null;

        return catUsuarioEntePublicoRepository.getUsuariosFiltrados(idEntePublico, idUnidadAdministrativa, listaEstatus);
    }

}
