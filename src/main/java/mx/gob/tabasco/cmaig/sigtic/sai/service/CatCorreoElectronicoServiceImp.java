package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.Utileria.Utileria;
import mx.gob.tabasco.cmaig.sigtic.sac.model.CatUnidadAdministrativaSql;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.CatUnidadAdministrativaSqlRepository;
import mx.gob.tabasco.cmaig.sigtic.sai.model.*;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.*;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sas.service.SegAccesoUsuarioService;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Campo;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Campos;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Plantilla;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.Separador;
import net.minidev.json.JSONObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CatCorreoElectronicoServiceImp implements CatCorreoElectronicoService {


    @Autowired
    private CatCorreoElectronicoBeanRepository catCorreoElectronicoBeanRepository;

    @Autowired
    private CatCorreoElectronicoRepository catCorreoElectronicoRepository;

    @Autowired
    private CatUnidadAdministrativaSqlRepository catUnidadAdministrativaSqlRepository;

    @Autowired
    private CatTipoCorreoRepository catTipoCorreoRepository;

    @Autowired
    private CatEstatusCorreoElectronicoRepository catEstatusCorreoElectronicoRepository;

    @Autowired
    private CatServidorCorreoElectronicoRepository catServidorCorreoElectronicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntePublicoRepository entePublicoRepository;

    @Autowired
    private CatCargosUsuariosRepository catCargosUsuariosRepository;

    @Autowired
    private CatCategoriaRepository catCategoriaRepository;

    @Autowired
    @Qualifier("unidadAdministrativaRepositorySai")
    private UnidadAdministrativaRepository unidadAdministrativaRepository;

    @Autowired
    private ConcentradoBeanRepository concentradoBeanRepository;

    @Autowired
    private CorreoTableroBeanRepository correoTableroBeanRepository;

    @Autowired
    private SegAccesoUsuarioService segAccesoUsuarioService;

    @Override
    public List<Usuario> getCatUsuariosPorUnidadAdministrativaCorreo(UnidadAdministrativa unidadAdministrativa){

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<CatUnidadAdministrativaSql> listaUnidadesAdministrativas=null;

        if (authUser.isAdministradorSigtic())
            listaUnidadesAdministrativas= catUnidadAdministrativaSqlRepository.getUnidadesAdministrativasArbolSigtic(unidadAdministrativa.getIdUnidadAdministrativa());
        else
            listaUnidadesAdministrativas= catUnidadAdministrativaSqlRepository.getUnidadesAdministrativasArbol(authUser.getUsername(),unidadAdministrativa.getIdUnidadAdministrativa());


        List<Long> listaIdUnidad = listaUnidadesAdministrativas.stream().map(CatUnidadAdministrativaSql::getIdUnidadAdministrativa).collect(Collectors.toList());

        List<UnidadAdministrativa> listaUnidad=(List<UnidadAdministrativa>) unidadAdministrativaRepository.findAllById(listaIdUnidad);

        List<Usuario> listaUsuario=null;

        if (listaUnidad.size()>0){
            listaUsuario = usuarioRepository.getUsuariosPorUnidadAdministrativaNoExistaCorreo(listaUnidad, unidadAdministrativa.getIdEntePublico(), "VIG");

            return listaUsuario;
        }else{
            listaUsuario=new ArrayList<>();

            return listaUsuario;
        }

    }

    @Override
    public List<CatCorreoElectronicoBean> obtenerCorreosElectronicos(Long idEntePublico, Long idUnidadAdministrativa) {
        List<CatCorreoElectronicoBean> lista=null;
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authUser.isAdministradorSigtic() && idUnidadAdministrativa==null)
            lista= catCorreoElectronicoBeanRepository.getCorreosElectronicoInstitucion(idEntePublico);
        else
        if (!authUser.isAdministradorSigtic() && idUnidadAdministrativa==null){
            List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(authUser.getUsername(),idEntePublico);
            lista= catCorreoElectronicoBeanRepository.getCorreosElectronicoInstitucionListaUnidad(idEntePublico, listaUnidAcceso);
        }
        else
            lista= catCorreoElectronicoBeanRepository.getCorreosElectronicoInstitucionPorUnidad(idEntePublico, idUnidadAdministrativa);

        return lista;
    }

    @Override
    public List<CatCorreoElectronicoBean> obtenerCorreosElectronicosModal(Long idEntePublico, Long idUnidadAdministrativa, Integer idTipoCorreo, String estatusCorreo) {

        List<CatCorreoElectronicoBean> lista = null;

        if (idUnidadAdministrativa==null && !estatusCorreo.equals("TODO"))
            lista = catCorreoElectronicoBeanRepository.getCorreosElectronicoModalSinUnidad(idEntePublico, idTipoCorreo, estatusCorreo);
        else
        if (idUnidadAdministrativa==null && estatusCorreo.equals("TODO"))
            lista = catCorreoElectronicoBeanRepository.getCorreosElectronicoModalSinUnidadTodo(idEntePublico, idTipoCorreo);
        else
        if (estatusCorreo.equals("TODO"))
            lista = catCorreoElectronicoBeanRepository.getCorreosElectronicoModalTodo(idEntePublico, idUnidadAdministrativa, idTipoCorreo);
        else
            lista = catCorreoElectronicoBeanRepository.getCorreosElectronicoModal(idEntePublico, idUnidadAdministrativa, idTipoCorreo, estatusCorreo);

        return lista;
    }

    @Override
    public List<CatCorreoElectronicoBean> obtenerCorreosElectronicosEnteNulo() {
        List<CatCorreoElectronicoBean> lista= catCorreoElectronicoBeanRepository.getCorreosElectronicoInstitucionNulo();
        return lista;
    }

    @Override
    public List<CatCorreoElectronicoBean> obtenerCorreosElectronicosSearch(String correo, Long idEntePublico) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(authUser.getUsername(),idEntePublico);

        List<CatCorreoElectronicoBean> lista= catCorreoElectronicoBeanRepository.getCorreosElectronicoSearch("%"+correo+"%", listaUnidAcceso);
        return lista;
    }

    @Override
    public List<CatCorreoElectronicoBean> obtenerCorreosElectronicosSearchAdmin(String correo) {

        List<CatCorreoElectronicoBean> lista= catCorreoElectronicoBeanRepository.getCorreosElectronicoSearchAdmin("%"+correo+"%");
        return lista;
    }

    @Override
    public CatCorreoElectronico saveCorreoElectronico(CatCorreoElectronico catCorreoElectronico) {

        CatCorreoElectronico catCorreoElectronico1 = null;
        EntePublico entePublico = entePublicoRepository.findById(catCorreoElectronico.getEntePublico().getIdEntePublico()).get();
        UnidadAdministrativa unidadAdministrativa = unidadAdministrativaRepository.findById(catCorreoElectronico.getUnidadAdministrativa().getIdUnidadAdministrativa()).get();
        CatTipoCorreoElectronico catTipoCorreoElectronico = catTipoCorreoRepository.findById(catCorreoElectronico.getCatTipoCorreoElectronico().getCveTipoCorreoElectronico()).get();
        CatEstatusCorreoElectronico catEstatusCorreoElectronico = catEstatusCorreoElectronicoRepository.findById(catCorreoElectronico.getCatEstatusCorreoElectronico().getCveEstatusCorreoElectronico()).get();
        Long idUsuario=catCorreoElectronico.getCatUsuario().getIdUsuario();
        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        CatServidorCorreoElectronico catServidorCorreoElectronico = catServidorCorreoElectronicoRepository.findById(catCorreoElectronico.getCatServidorCorreoElectronico().getIdServidorCorreoElectronico()).get();
        CatCategoria catCategoria = catCategoriaRepository.findById(catCorreoElectronico.getCatCategoria().getIdCategoriaUsuario()).get();

        Optional<CatCorreoElectronico> catCorreoElectronicoOp= catCorreoElectronicoRepository.findByAndCorreoElectronico(catCorreoElectronico.getCorreoElectronico());

        if (catCorreoElectronico.getIdCorreoElectronico()!=null) {
            catCorreoElectronico1 = catCorreoElectronicoRepository.findById(catCorreoElectronico.getIdCorreoElectronico()).get();

            if (catCorreoElectronicoOp.isPresent()){
                if (!catCorreoElectronicoOp.get().getCorreoElectronico().equals(catCorreoElectronico.getCorreoElectronico()))
                    throw new NoSuchElementException("El correo electrónico ya esta en uso en la dependencia "+catCorreoElectronicoOp.get().getEntePublico().getNombreEntePublico());
            }

            if (catCorreoElectronico1.getEstatusValidacion()==null || catCorreoElectronico1.getEstatusValidacion().equals(""))
                catCorreoElectronico1.setEstatusValidacion("NOV");
        }
        else {
            catCorreoElectronico1 = catCorreoElectronico;
            catCorreoElectronico1.setEstatusValidacion("NOV");

            if (catCorreoElectronicoOp.isPresent())
                throw new NoSuchElementException("El correo electrónico ya esta en uso en la dependencia "+catCorreoElectronicoOp.get().getEntePublico().getNombreEntePublico());
        }

        catCorreoElectronico1.setCatServidorCorreoElectronico(catServidorCorreoElectronico);
        catCorreoElectronico1.setCatTipoCorreoElectronico(catTipoCorreoElectronico);
        catCorreoElectronico1.setCorreoElectronico(catCorreoElectronico.getCorreoElectronico());
        catCorreoElectronico1.setCorreoElectronicoEnvio(catCorreoElectronico.getCorreoElectronicoEnvio());
        catCorreoElectronico1.setCorreoElectronicoAlias(catCorreoElectronico.getCorreoElectronicoAlias());
        catCorreoElectronico1.setCatUsuario(usuario);
        catCorreoElectronico1.setRfcTitular(usuario.getRfc());
        catCorreoElectronico1.setCurpTitular(usuario.getCurp());
        catCorreoElectronico1.setEntePublico(entePublico);
        catCorreoElectronico1.setUnidadAdministrativa(unidadAdministrativa);
        catCorreoElectronico1.setTitularCorreoElecntronicoEntePublico(usuario.getNombreCompleto());
        catCorreoElectronico1.setCargoTitularCorreo(catCorreoElectronico.getCargoTitularCorreo());
        catCorreoElectronico1.setCatCategoria(catCategoria);
        catCorreoElectronico1.setNombreCategoriaTitular(catCategoria.getNombreCategoriaUsuario());
        catCorreoElectronico1.setFechaAltaCorreoElectronico(catCorreoElectronico.getFechaAltaCorreoElectronico());
        catCorreoElectronico1.setFechaBajaCorreoElectronico(catCorreoElectronico.getFechaBajaCorreoElectronico());
        catCorreoElectronico1.setCatEstatusCorreoElectronico(catEstatusCorreoElectronico);
        catCorreoElectronico1.setCveEstatus(catCorreoElectronico.getCveEstatus());
        catCorreoElectronico1.setFechaUltimoCambio(new Timestamp(new Date().getTime()));

        catCorreoElectronico1=catCorreoElectronicoRepository.save(catCorreoElectronico1);
        return catCorreoElectronico1;
    }
    @Override
    public  CatCorreoElectronico getCorreoElectronico(Long idCorreoElectronico){
        Optional <CatCorreoElectronico> catCorreoElectronicoOp = catCorreoElectronicoRepository.findById(idCorreoElectronico);
        if(!catCorreoElectronicoOp.isPresent())
            throw  new NoSuchElementException("El sistema no existe");

        return catCorreoElectronicoOp.get();
    }

    @Override
    public JSONObject deleteCorreoElectronico(CatCorreoElectronico catCorreoElectronico) {

        Optional<CatCorreoElectronico> catCorreoElectronicoOp = catCorreoElectronicoRepository.findById(catCorreoElectronico.getIdCorreoElectronico());

        if (!catCorreoElectronicoOp.isPresent())
            throw new NoSuchElementException("El correo electrónico no existe");

        catCorreoElectronicoOp.get().setCveEstatus("NOV");
        //catCorreoElectronicoRepository.delete(catCorreoElectronicoOp.get());
        catCorreoElectronicoRepository.save(catCorreoElectronicoOp.get());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message","Correo Electrónico eliminado correctamente");

        return jsonObject;
    }

    @Override
    public JSONObject getConcentradoTipoCorreo(Long idEntePublico) {

        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ConcentradoBean> listaConcentrado = null;

        if (authUser.isAdministradorSigtic())
            listaConcentrado = concentradoBeanRepository.getConcentradoTipoCorreoPorEnte(idEntePublico);
        else{
            List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(authUser.getUsername(),idEntePublico);

            listaConcentrado = concentradoBeanRepository.getConcentradoTipoCorreoPorEnteListaUnidad(idEntePublico, listaUnidAcceso);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("listaConcentrado", listaConcentrado);

        return jsonObject;
    }

    @Override
    public JSONObject getConcentradoTipoCorreoAll() {
        List<ConcentradoBean> listaConcentrado = concentradoBeanRepository.getConcentradoTipoCorreo();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("listaConcentrado", listaConcentrado);

        return jsonObject;
    }

    @Override
    public CatCorreoElectronico vigCorreoElectronico(CatCorreoElectronico catCorreoElectronico) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<CatCorreoElectronico> catCorreoElectronicoOp = catCorreoElectronicoRepository.findById(catCorreoElectronico.getIdCorreoElectronico());

        if (!catCorreoElectronicoOp.isPresent())
            throw new NoSuchElementException("El correo electrónico no existe");

        catCorreoElectronicoOp.get().setEstatusValidacion("VIG");
        catCorreoElectronicoOp.get().setUsuariovalidoCorreoElectronico(authUser.getUsername());
        catCorreoElectronicoOp.get().setFechaUltimoCambio(new Timestamp(new Date().getTime()));

        return catCorreoElectronicoRepository.save(catCorreoElectronicoOp.get());
    }

    @Override
    public CatCorreoElectronico novCorreoElectronico(CatCorreoElectronico catCorreoElectronico) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<CatCorreoElectronico> catCorreoElectronicoOp = catCorreoElectronicoRepository.findById(catCorreoElectronico.getIdCorreoElectronico());

        if (!catCorreoElectronicoOp.isPresent())
            throw new NoSuchElementException("El correo electrónico no existe");

        catCorreoElectronicoOp.get().setEstatusValidacion("NOV");
        catCorreoElectronicoOp.get().setUsuariovalidoCorreoElectronico(authUser.getUsername());
        catCorreoElectronicoOp.get().setFechaUltimoCambio(new Timestamp(new Date().getTime()));

        return catCorreoElectronicoRepository.save(catCorreoElectronicoOp.get());
    }

    public void generaReporteCorreos(Long idEntePublico, Long idUnidadAdministrativa, HttpServletResponse response) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean existeRolVerPassword = authUser.getListaPerfil().stream().filter(p-> p.getNombrePerfil().equals("VER-PASSWORD-CORREO") ).findFirst().isPresent();

        List<CatCorreoElectronicoBean> listaCorreo =obtenerCorreosElectronicos(idEntePublico,idUnidadAdministrativa);

        File folders = new File(System.getProperty("user.home") + "/repositorio/correos_electronico/");

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(0, "Hoja excel");

            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            //HSSFRow headerRow = sheet.createRow(0)
            Row headerRow = sheet.createRow(0);
            Cell cell = headerRow.createCell(0);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("CURP");
            sheet.autoSizeColumn(0);

            Cell cell1 = headerRow.createCell(1);
            cell1.setCellStyle(headerStyle);
            cell1.setCellValue("Títular Correo");
            sheet.autoSizeColumn(1);

            Cell cell2 = headerRow.createCell(2);
            cell2.setCellStyle(headerStyle);
            cell2.setCellValue("Tipo Correo");
            sheet.autoSizeColumn(2);

            Cell cell3 = headerRow.createCell(3);
            cell3.setCellStyle(headerStyle);
            cell3.setCellValue("Correo");
            sheet.autoSizeColumn(3);

            Cell cell4 = headerRow.createCell(4);
            cell4.setCellStyle(headerStyle);
            cell4.setCellValue("Estatus Correo");
            sheet.autoSizeColumn(4);

            Cell cell5 = headerRow.createCell(5);
            cell5.setCellStyle(headerStyle);
            cell5.setCellValue("Contraseña Temporal");
            sheet.autoSizeColumn(5);

            Cell cell6 = headerRow.createCell(6);
            cell6.setCellStyle(headerStyle);
            cell6.setCellValue("Estatus Validación");
            sheet.autoSizeColumn(6);

            int i=1;

            for (CatCorreoElectronicoBean reg:listaCorreo){
                Row headerRowReg = sheet.createRow(i);

                if (reg.getCurpUsuario()!=null) {
                    Cell cellx = headerRowReg.createCell(0);
                    cellx.setCellValue(reg.getCurpUsuario());
                }

                if (reg.getNombreCompleto()!=null) {
                    Cell cellx1 = headerRowReg.createCell(1);
                    cellx1.setCellValue(reg.getNombreCompleto());
                }

                if (reg.getNombreTipoCorreoElectronico()!=null) {
                    Cell cellx2 = headerRowReg.createCell(2);
                    cellx2.setCellValue(reg.getNombreTipoCorreoElectronico());
                }

                Cell cellx3 = headerRowReg.createCell(3);
                cellx3.setCellValue(reg.getCorreoElectronico());

                Cell cellx4 = headerRowReg.createCell(4);
                cellx4.setCellValue(reg.getCveEstatus());

                Cell cellx5 = headerRowReg.createCell(5);

                if (reg.getPasswordCorreo()==null || existeRolVerPassword==false)
                    cellx5.setCellValue("");
                else
                    cellx5.setCellValue(reg.getPasswordCorreo());

                Cell cellx6 = headerRowReg.createCell(6);
                cellx6.setCellValue(reg.getEstatusValidacion());
                i++;

            }


            if (!folders.exists())
                folders.mkdirs();

            File verifica = new File(folders.getPath() + "/correos_ente_"+idEntePublico+".xlsx");
            File archivo;
            if (verifica.exists()) {
                verifica.delete();
                archivo = new File(folders.getPath() + "/correos_ente_"+idEntePublico+".xlsx");
            } else {
                archivo = verifica;
            }
            FileOutputStream file = new FileOutputStream(archivo);
            workbook.write(file);
            file.close();

        } catch (Exception e) {
            throw new NoSuchElementException(e.getMessage());
        }

            try {
                File file = new File(folders.getPath() +"/correos_ente_"+idEntePublico+".xlsx");
                InputStream inputStream = new FileInputStream(file);
                XSSFWorkbook wb = new XSSFWorkbook(inputStream);
                inputStream.close();

                response.setContentType("multipart/form-data");
                response.setHeader("Content-disposition", "attachment;filename=" + "correos_ente_"+idEntePublico+".xlsx");
                response.setHeader("Pragma", "No-cache");
                response.setCharacterEncoding("utf-8");
                ServletOutputStream out = response.getOutputStream();
                wb.write(out);
                out.flush();
                out.close();

            } catch (Exception e) {
                throw new NoSuchElementException(e.getMessage());
            }

    }

    @Override
    public JSONObject getListaCorreoGroupTablero(Long idEntePublico, Long idUnidadAdministrativa) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Properties props = new Properties();
        props.put("titulo", "Correos Por Estatus");
        ArrayList<String> listEtiquetas = new ArrayList<String>();
        ArrayList<Integer> listValores = new ArrayList<Integer>();

        Properties props2 = new Properties();
        props2.put("titulo", "Correos Por Tipo de Correo");
        ArrayList<String> listEtiquetas2 = new ArrayList<String>();
        ArrayList<Integer> listValores2 = new ArrayList<Integer>();

        List<CorreoTableroBean> lista = null;
        List<CorreoTableroBean> listaTipoCorreo = null;


        if (authUser.isAdministradorSigtic()){
            if (idUnidadAdministrativa==null){
                lista = correoTableroBeanRepository.getCorreosPorUnidadAdmin(idEntePublico);
                listaTipoCorreo = correoTableroBeanRepository.getCorreosPorTipoCorreo(idEntePublico);
            }else{
                lista = correoTableroBeanRepository.getCorreosPorIdUnidadAdmin(idEntePublico, idUnidadAdministrativa);
                listaTipoCorreo = correoTableroBeanRepository.getCorreosPorTipoCorreoPorIdUnidad(idEntePublico, idUnidadAdministrativa);
            }
        }else{
            if (idUnidadAdministrativa==null){
                List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(authUser.getUsername(),idEntePublico);
                lista = correoTableroBeanRepository.getCorreosPorUnidadAdminPorNivel(idEntePublico, listaUnidAcceso);
                listaTipoCorreo = correoTableroBeanRepository.getCorreosPorTipoCorreoPorNivel(idEntePublico, listaUnidAcceso);
            }else{
                lista = correoTableroBeanRepository.getCorreosPorIdUnidadAdmin(idEntePublico, idUnidadAdministrativa);
                listaTipoCorreo = correoTableroBeanRepository.getCorreosPorTipoCorreoPorIdUnidad(idEntePublico, idUnidadAdministrativa);
            }

        }

        List<CorreoTableroBean> listaTipoCorreoNotNull = listaTipoCorreo.stream().filter(p->p.getParent1()!=null).collect(Collectors.toList());
        List<CorreoTableroBean> listaTipoCorreoNull = listaTipoCorreo.stream().filter(p->p.getParent1()==null).collect(Collectors.toList());

        List<CorreoTablero> listaTablero= new ArrayList<>();
        int contador=0;
        int parent=0;
        String clasePadre="";
        //DecimalFormat formatea = new DecimalFormat("###,###,###");

        int totalActivo=0;
        int totalBloqueado=0;
        int totalBloqueadoServidor=0;
        int totalMantenimiento=0;
        int totalNoActivo=0;

        for(CorreoTableroBean correoTableroBean:lista){

            List<CorreoTableroBean> listaTipoCorreoTemp = null;

            if (correoTableroBean.getIdTabla()!=null && correoTableroBean.getIdTabla()!=-1)
                listaTipoCorreoTemp=   listaTipoCorreoNotNull.stream().filter(p->p.getParent1()!=null).filter(p->p.getParent1().longValue()==correoTableroBean.getIdTabla().longValue()).collect(Collectors.toList());

            else
                listaTipoCorreoTemp=   listaTipoCorreoNull;

            if (listaTipoCorreoTemp==null)
                listaTipoCorreoTemp = new ArrayList<>();

            totalActivo+=correoTableroBean.getTotalActivo();
            totalBloqueado+=correoTableroBean.getTotalBloqueado();
            totalBloqueadoServidor+=correoTableroBean.getTotalBloqueadoServidor();
            totalMantenimiento+=correoTableroBean.getTotalMantenimiento();
            totalNoActivo+=correoTableroBean.getTotalNoActivo();

            CorreoTablero correoTablero= new CorreoTablero();
            correoTablero.setNombre(correoTableroBean.getNombre()+" ("+listaTipoCorreoTemp.size()+")");
            correoTablero.setTotalActivo(correoTableroBean.getTotalActivo());
            correoTablero.setTotalBloqueado(correoTableroBean.getTotalBloqueado());
            correoTablero.setTotalBloqueadoServidor(correoTableroBean.getTotalBloqueadoServidor());
            correoTablero.setTotalMantenimiento(correoTableroBean.getTotalMantenimiento());
            correoTablero.setTotalNoActivo(correoTableroBean.getTotalNoActivo());
            correoTablero.setTotal(correoTableroBean.getTotalActivo()+correoTableroBean.getTotalBloqueado()+correoTableroBean.getTotalBloqueadoServidor()+
                    correoTableroBean.getTotalMantenimiento()+correoTableroBean.getTotalNoActivo());
            contador++;
            correoTablero.setNodo(contador);
            correoTablero.setClase("treegrid-"+contador+" treegrid-parent-"+parent);
            correoTablero.setClasePadre("tree");
            correoTablero.setEstilo("");
            listaTablero.add(correoTablero);

            int parent2=contador;
            String clasePadre2="treegrid-"+contador;

            for(CorreoTableroBean correoTableroBeanTemp:listaTipoCorreoTemp){
                CorreoTablero correoTableroTemp= new CorreoTablero();
                correoTableroTemp.setIdTipoCorreo(correoTableroBeanTemp.getIdTipoCorreo());
                correoTableroTemp.setIdEntePublico(idEntePublico);
                correoTableroTemp.setNombre(correoTableroBeanTemp.getNombre());
                correoTableroTemp.setTooltip(correoTableroBeanTemp.getNombre());
                correoTableroTemp.setTotalActivo(correoTableroBeanTemp.getTotalActivo());
                correoTableroTemp.setTotalBloqueado(correoTableroBeanTemp.getTotalBloqueado());
                correoTableroTemp.setTotalBloqueadoServidor(correoTableroBeanTemp.getTotalBloqueadoServidor());
                correoTableroTemp.setTotalMantenimiento(correoTableroBeanTemp.getTotalMantenimiento());
                correoTableroTemp.setTotalNoActivo(correoTableroBeanTemp.getTotalNoActivo());
                correoTableroTemp.setTotal(correoTableroBeanTemp.getTotalActivo()+correoTableroBeanTemp.getTotalBloqueado()+correoTableroBeanTemp.getTotalBloqueadoServidor()+
                        correoTableroBeanTemp.getTotalMantenimiento()+correoTableroBeanTemp.getTotalNoActivo());
                contador++;
                correoTableroTemp.setNodo(contador);
                correoTableroTemp.setClase("treegrid-"+contador+" treegrid-parent-"+parent2);
                correoTableroTemp.setClasePadre(clasePadre2);
                correoTableroTemp.setEstilo("style='border-color:#6c757d'");
                correoTableroTemp.setIdUnidadAdministrativa(correoTableroBeanTemp.getParent1());
                listaTablero.add(correoTableroTemp);
            }
        }
        listEtiquetas.add("Activo");
        listValores.add(totalActivo);
        listEtiquetas.add("Bloqueado");
        listValores.add(totalBloqueado);
        listEtiquetas.add("Bloqueo por Servidor");
        listValores.add(totalBloqueadoServidor);
        listEtiquetas.add("Mantenimiento");
        listValores.add(totalMantenimiento);
        listEtiquetas.add("No Activo");
        listValores.add(totalNoActivo);

        props.put("etiquetas", listEtiquetas);
        props.put("valores", listValores);

        List<ConcentradoBean> listaConcentrado = null;

        if (idUnidadAdministrativa==null){
            if (authUser.isAdministradorSigtic())
                listaConcentrado = concentradoBeanRepository.getConcentradoTipoCorreoPorEnte(idEntePublico);
            else {
                List<Long> listaUnidAcceso = segAccesoUsuarioService.getUnidadesAdministrativaAcceso(authUser.getUsername(),idEntePublico);
                listaConcentrado = concentradoBeanRepository.getConcentradoTipoCorreoPorEnteListaUnidad(idEntePublico, listaUnidAcceso);
            }
        }else
            listaConcentrado = concentradoBeanRepository.getConcentradoTipoCorreoPorEnteUnidad(idEntePublico, idUnidadAdministrativa);


        int totalTipo=0;
        for (ConcentradoBean concentradoBean:listaConcentrado){
            listEtiquetas2.add(concentradoBean.getNombre());
            listValores2.add(concentradoBean.getTotal());
            totalTipo+=concentradoBean.getTotal();
        }

        props2.put("etiquetas", listEtiquetas2);
        props2.put("valores", listValores2);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalActivo", totalActivo);
        jsonObject.put("totalBloqueado", totalBloqueado);
        jsonObject.put("totalBloqueadoServidor", totalBloqueadoServidor);
        jsonObject.put("totalMantenimiento", totalMantenimiento);
        jsonObject.put("totalNoActivo", totalNoActivo);
        jsonObject.put("total", totalActivo+totalBloqueado+totalBloqueadoServidor+totalMantenimiento+totalNoActivo);
        jsonObject.put("totalTipo", totalTipo);
        jsonObject.put("listaTablero", listaTablero);
        jsonObject.put("graficaCorreoEstatus", props);
        jsonObject.put("graficaCorreoPorTipo", props2);

        return jsonObject;
    }


}
