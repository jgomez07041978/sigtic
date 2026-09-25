package mx.gob.tabasco.cmaig.sigtic.sigget.service;

import mx.gob.tabasco.cmaig.sigtic.Utileria.Utileria;
import mx.gob.tabasco.cmaig.sigtic.sai.model.ConcentradoBean;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import mx.gob.tabasco.cmaig.sigtic.sigget.model.*;
import mx.gob.tabasco.cmaig.sigtic.sigget.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CargaMasivaServiceImp implements CargaMasivaService {

    @Autowired
    private CargaDatosPlantillaRepository cargaDatosPlantillaRepository;

    @Autowired
    private PlantillaRepository plantillaRepository;

    @Autowired
    private CargaMasivaTablaBeanRepository cargaMasivaTablaBeanRepository;

    @Autowired
    private CamposService camposService;

    @Autowired
    private EquivalenciaRepository equivalenciaRepository;

    @Autowired
    private SeparadorRepository separadorRepository;

    @Autowired
    private CargaDatosPlantillaHeadRepository cargaDatosPlantillaHeadRepository;

    @Autowired
    private CargaDatosPlantillaErrorRepository cargaDatosPlantillaErrorRepository;

    @Autowired
    private CampoRepository campoRepository;

    @Autowired
    private ConcentradoCargaBeanRepository concentradoCargaBeanRepository;

    @Autowired
    private ConcentradoCargaEnteBeanRepository concentradoCargaEnteBeanRepository;

    @Override
    public String procesarCargaMasiva(CargaMasivaBean cargaMasiva) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cargaMasiva.setIdEntePublico(authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico());
        cargaMasiva.setUsuario(authUser.getRfc());

        String columnas = "";

        Optional<Plantilla> plantilla = plantillaRepository.findById(cargaMasiva.getIdPlantilla());

        if (!plantilla.isPresent())
            throw new NoSuchElementException("La plantilla no existe");

        if (plantilla.get().getNumLinea()<=0)
            throw new NoSuchElementException("El encabezado de la plantilla no tiene configurado un valor > 0");

        List<Campos> campos = camposService.getCamposPorPlantilla(plantilla.get(), "VIG");

        if (campos.size() == 0)
            throw new NoSuchElementException("La plantilla no tiene configurado campos");

        List<Equivalencia> equivalencia = (List) equivalenciaRepository.findAll();

       List<Object> datos = new ArrayList<Object>();

        File folders = new File(System.getProperty("user.home") + "/repositorio/cargar_masiva/" + authUser.getUsername());

        if (!folders.exists())
            folders.mkdirs();

        if (!cargaMasiva.getExtension().equals(Utileria.getFileExtension(plantilla.get().getNombreArchivo()).toLowerCase()))
            throw new NoSuchElementException("El formato no coincide con el de la plantilla");

        cargaMasiva.setPath(folders.getPath());

        if (!cargaMasiva.getExtension().equals("xlsx") && !cargaMasiva.getExtension().equals("txt"))
            throw new NoSuchElementException("Formato incorrecto");

        try {
            File fileCarga = new File(cargaMasiva.getPath() + "/" + cargaMasiva.getNombreArchivo() + "." + cargaMasiva.getExtension());
            if (fileCarga.exists())
                fileCarga.delete();

            FileOutputStream fosCar = new FileOutputStream(fileCarga);
            byte[] decoderCer = Base64.getDecoder().decode(cargaMasiva.getArchivo());
            fosCar.write(decoderCer);
            fosCar.close();
        } catch (Exception e) {
            if (e.getMessage().contains("Input byte array has wrong 4-byte ending unit"))
                throw new RuntimeException("Datos requeridos: \n * El archivo .txt no es valido.");
            else
                throw new RuntimeException("Error al intentar guardar el archivo .txt");
        }

        File archivoCarga = new File(folders.getPath() + "/" + cargaMasiva.getNombreArchivo() + "." + cargaMasiva.getExtension());

        if (cargaMasiva.getExtension().equals("xlsx")) {

            List<Campo> listaCampo = campoRepository.findAllByOrderByOrden();
            Map<Integer, String> campoIndexMap = listaCampo.stream().collect(Collectors.toMap(Campo::getOrden, Campo::getNombreCampo));

            Map<String, Integer> campoBd =new HashMap<>();
            for (int i=0; i<campos.size();i++){
                campoBd.put(campos.get(i).getUbicacionColumna(), i);
            }

            try {
                FileInputStream file = new FileInputStream(archivoCarga);
                XSSFWorkbook wb = new XSSFWorkbook(file);
                XSSFSheet sheet = wb.getSheetAt(0);
                int rows = sheet.getLastRowNum();

                DataFormatter objDefaultFormat = new DataFormatter();
                FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) wb);

                if (rows == 0)
                    throw new IOException("El archivo esta vacio");

/*                Row encabezado = sheet.getRow(plantilla.get().getNumLinea()-1);

                if (encabezado.getLastCellNum() != plantilla.get().getNumCampos())
                    throw new IOException("Numero de campos incorrectos en el archivo de carga");*/

                for (int i = plantilla.get().getNumLinea(); i <= rows; ++i) {

                    Row row = sheet.getRow(i);
                    if (row!=null){
                        Map<String, Object> fila = new HashMap<String, Object>();

                        Iterator<Cell> cellIterator = row.cellIterator();
                        Cell celda;
                        //int j = 0;

                        boolean entre = false;

                        while (cellIterator.hasNext()) {
                            celda = cellIterator.next();

                            int idx = celda.getColumnIndex();
                            String nomCampo = campoIndexMap.get(idx + 1);

                            if (campoBd.containsKey(nomCampo)) {
                                int j = campoBd.get(nomCampo).intValue();

                                if (celda != null && celda.getCellType() != CellType.BLANK) {
                                    entre = true;
                                    switch (celda.getCellType()) {
                                        case NUMERIC:
                                            if (DateUtil.isCellDateFormatted(celda)) {

                                                if (campos.get(j).getTipoCampo().getCveTipoCampo().equals("D") && campos.get(j).getMascara() == null)
                                                    throw new IOException("Configure la máscara para la fecha");

                                                String fecha = "";
                                                if (campos.get(j).getTipoCampo().getCveTipoCampo().equals("D")) {

                                                    SimpleDateFormat formatoddmmyy = new SimpleDateFormat(campos.get(j).getMascara());
                                                    fecha = formatoddmmyy.format(celda.getDateCellValue());
                                                }
                                                //SimpleDateFormat formatoddmmyy = new SimpleDateFormat("dd-MM-yyyy");

                                                //System.out.println(fecha)
                                       /* if (campos.get(j).getMascara() != null && campos.get(j).getTipoCampo().getCveTipoCampo().equals("D") && !Utileria.validarFecha(fecha, campos.get(j).getMascara())) {
                                            throw new IOException("Valor campo " + campos.get(j).getEtiquetaColumna() + " incorrecto");
                                        }*/

                                                fila.put(campos.get(j).getCampoBaseDatos(), fecha);
                                            } else {
                                                String d = "";
                                                BigDecimal valorDecimal = new BigDecimal(celda.toString());

                                                if (Utileria.isLong(valorDecimal)) {
                                                    Long valEntero = valorDecimal.longValue();
                                                    d = String.valueOf(valEntero);
                                                } else if (campos.get(j).getTipoCampo().getCveTipoCampo().equals("N")) {
                                                    d = String.valueOf(valorDecimal);
                                                } else if (campos.get(j).getTipoCampo().getCveTipoCampo().equals("I") && Utileria.isLong(valorDecimal)) {
                                                    d = String.valueOf(valorDecimal);
                                                } else
                                                    d = String.valueOf(valorDecimal);

                                                fila.put(campos.get(j).getCampoBaseDatos(), d);
                                            }
                                            break;
                                        case STRING:
                                            String text = celda.getStringCellValue();
                                            for (Equivalencia equivalencia1 : equivalencia) {
                                                //System.out.println(resultado)
                                                text = text.replaceAll(equivalencia1.getExpresion(), " ");
                                                //System.out.println(resultado)
                                            }

                                            fila.put(campos.get(j).getCampoBaseDatos(), text);
                                            break;
                                        case BOOLEAN:
                                            fila.put(campos.get(j).getCampoBaseDatos(), celda.getBooleanCellValue());
                                            break;
                                    }

                                }
                            }
                        }


                        if (entre) {
                            datos.add(fila);
                        }
                    }
                    //System.out.println("LLegamos a enviar")
                }


            } catch (Exception e) {
                System.out.println(datos);
                throw new NoSuchElementException(e.getMessage());
            }

        } else if (cargaMasiva.getExtension().equals("txt")) {

            Separador separador = null;

            if (plantilla.get().getIdSeparador() != null)
                separador = separadorRepository.findById(plantilla.get().getIdSeparador()).get();

            try {
                String texto;
                FileReader lector=new FileReader(archivoCarga);
                BufferedReader contenido=new BufferedReader(lector);
                String encabezado;
                int i=0;
                while((texto=contenido.readLine())!=null)
                {
                    String resultado= texto;
                    String[] split;

                    if(i>=(plantilla.get().getNumLinea()-1)){

                        for(Equivalencia equi: equivalencia)
                            resultado = resultado.replaceAll(equi.getExpresion() ," ");

                        Map<String, Object> fila = new HashMap<String, Object>();

                        if(plantilla.get().getIdSeparador() != null)
                            split= resultado.split(separador.getCodigo());
                        else
                            split= resultado.split("~~");

                        if(split.length!=campos.size())
                            throw new IOException("Numero de campos incorrectos en el archivo de carga");

                        for(int z=0; z<split.length; ++z){
                            if(!Utileria.validaTipoValor(campos.get(z).getTipoCampo().getCveTipoCampo(), split[z],campos.get(z).getMascara())){
                                throw new IOException("Valor campo "+campos.get(z).getEtiquetaColumna()+" incorrecto");
                            }
                            fila.put(campos.get(z).getCampoBaseDatos(), split[z]);

                        }

                        datos.add(fila);
                    }
                    i++;
                }

                lector.close();
                if(i==0){
                    throw new IOException("El archivo esta vacio");
                }

            } catch (Exception e) {
                throw new NoSuchElementException(e.getMessage());
            }
        }

        for(Campos campos1:campos){
            if (columnas == "")
                columnas = campos1.getCampoBaseDatos();
            else
                columnas += "," + campos1.getCampoBaseDatos();
        }

        cargaMasiva(datos, cargaMasiva, columnas);

        return "Correcto";
    }


    @Transactional(value = "siggetTransactionManager")
    @Override
    public void cargaMasiva(List<Object> datos, CargaMasivaBean cargaMasivaBean, String columnas) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        BigDecimal sequencia = null;

        if (cargaMasivaBean.getIdCargaDatos()!=null){
            List<CargaDatosPlantillaError> listaError=cargaDatosPlantillaErrorRepository.findAllByIdCargaDatosPlantillaOrderByNumLineaPlantilla(cargaMasivaBean.getIdCargaDatos().longValue());
            cargaDatosPlantillaErrorRepository.deleteAll(listaError);
            List<CargaDatosPlantilla> listaDelete = cargaDatosPlantillaRepository.listaCargaDatos(cargaMasivaBean.getIdCargaDatos().longValue());
            cargaDatosPlantillaRepository.deleteAll(listaDelete);
            sequencia =new BigDecimal(cargaMasivaBean.getIdCargaDatos());
        }
        else
            sequencia = cargaDatosPlantillaRepository.getNextValMySequence();

        Timestamp fechaRegistro = new Timestamp(new Date().getTime());

        LocalDateTime localDateTime = LocalDateTime.from(dateTimeFormatter.parse(cargaMasivaBean.getFechaCorteText() + " 00:00:00"));
        //localDateTime = localDateTime.plusDays(1);

        List<CargaDatosPlantilla> lista = new ArrayList<>();

        Plantilla plantilla = plantillaRepository.findById(cargaMasivaBean.getIdPlantilla()).get();

        for (int i = 0; i < datos.size(); i++) {
            CargaDatosPlantillaPk cargaDatosPlantillaPk = new CargaDatosPlantillaPk();
            cargaDatosPlantillaPk.setIdCargaDatosPlantilla(sequencia);
            cargaDatosPlantillaPk.setNumLineaCargaDatos(Integer.valueOf(plantilla.getNumLinea()+(i + 1)));

            CargaDatosPlantilla cargaDatosPlantilla = new CargaDatosPlantilla();
            cargaDatosPlantilla.setCargaDatosPlantillaPk(cargaDatosPlantillaPk);
            cargaDatosPlantilla.setPlantilla(plantilla);
            cargaDatosPlantilla.setIdEntePublico(cargaMasivaBean.getIdEntePublico());
            cargaDatosPlantilla.setRfcUsuarioCargaDatos(cargaMasivaBean.getUsuario());

            cargaDatosPlantilla.setFechaCorteCargaDatos(Timestamp.valueOf(localDateTime));
            cargaDatosPlantilla.setCveEstatus("VIG");
            cargaDatosPlantilla.setFechaUltimoCambio(new Timestamp(new Date().getTime()));

            String column[] = columnas.split(",");
            //HashMap<String, Object> map = datos.get(i);

            Map<String, Object> fila = (Map<String, Object>) datos.get(i);

            for (String col : column) {

                if (col.equals("COLUMNA_A"))
                    cargaDatosPlantilla.setColumnaA((String) fila.get(col));

                if (col.equals("COLUMNA_B"))
                    cargaDatosPlantilla.setColumnaB((String) fila.get(col));

                if (col.equals("COLUMNA_C"))
                    cargaDatosPlantilla.setColumnaC((String) fila.get(col));

                if (col.equals("COLUMNA_D"))
                    cargaDatosPlantilla.setColumnaD((String) fila.get(col));

                if (col.equals("COLUMNA_E"))
                    cargaDatosPlantilla.setColumnaE((String) fila.get(col));

                if (col.equals("COLUMNA_F"))
                    cargaDatosPlantilla.setColumnaF((String) fila.get(col));

                if (col.equals("COLUMNA_G"))
                    cargaDatosPlantilla.setColumnaG((String) fila.get(col));

                if (col.equals("COLUMNA_H"))
                    cargaDatosPlantilla.setColumnaH((String) fila.get(col));

                if (col.equals("COLUMNA_I"))
                    cargaDatosPlantilla.setColumnaI((String) fila.get(col));

                if (col.equals("COLUMNA_J"))
                    cargaDatosPlantilla.setColumnaJ((String) fila.get(col));

                if (col.equals("COLUMNA_K"))
                    cargaDatosPlantilla.setColumnaK((String) fila.get(col));

                if (col.equals("COLUMNA_L"))
                    cargaDatosPlantilla.setColumnaL((String) fila.get(col));

                if (col.equals("COLUMNA_M"))
                    cargaDatosPlantilla.setColumnaM((String) fila.get(col));

                if (col.equals("COLUMNA_N"))
                    cargaDatosPlantilla.setColumnaN((String) fila.get(col));

                if (col.equals("COLUMNA_O"))
                    cargaDatosPlantilla.setColumnaO((String) fila.get(col));

                if (col.equals("COLUMNA_P"))
                    cargaDatosPlantilla.setColumnaP((String) fila.get(col));

                if (col.equals("COLUMNA_Q"))
                    cargaDatosPlantilla.setColumnaQ((String) fila.get(col));

                if (col.equals("COLUMNA_R"))
                    cargaDatosPlantilla.setColumnaR((String) fila.get(col));

                if (col.equals("COLUMNA_S"))
                    cargaDatosPlantilla.setColumnaS((String) fila.get(col));

                if (col.equals("COLUMNA_T"))
                    cargaDatosPlantilla.setColumnaT((String) fila.get(col));

                if (col.equals("COLUMNA_U"))
                    cargaDatosPlantilla.setColumnaU((String) fila.get(col));

                if (col.equals("COLUMNA_V"))
                    cargaDatosPlantilla.setColumnaV((String) fila.get(col));

                if (col.equals("COLUMNA_W"))
                    cargaDatosPlantilla.setColumnaW((String) fila.get(col));

                if (col.equals("COLUMNA_X"))
                    cargaDatosPlantilla.setColumnaX((String) fila.get(col));

                if (col.equals("COLUMNA_Y"))
                    cargaDatosPlantilla.setColumnaY((String) fila.get(col));

                if (col.equals("COLUMNA_Z"))
                    cargaDatosPlantilla.setColumnaZ((String) fila.get(col));


                /*-------------------------------*/
                if (col.equals("COLUMNA_AA"))
                    cargaDatosPlantilla.setColumnaAA((String) fila.get(col));

                if (col.equals("COLUMNA_AB"))
                    cargaDatosPlantilla.setColumnaAB((String) fila.get(col));

                if (col.equals("COLUMNA_AC"))
                    cargaDatosPlantilla.setColumnaAC((String) fila.get(col));

                if (col.equals("COLUMNA_AD"))
                    cargaDatosPlantilla.setColumnaAD((String) fila.get(col));

                if (col.equals("COLUMNA_AE"))
                    cargaDatosPlantilla.setColumnaAE((String) fila.get(col));

                if (col.equals("COLUMNA_AF"))
                    cargaDatosPlantilla.setColumnaAF((String) fila.get(col));

                if (col.equals("COLUMNA_AG"))
                    cargaDatosPlantilla.setColumnaAG((String) fila.get(col));

                if (col.equals("COLUMNA_AH"))
                    cargaDatosPlantilla.setColumnaAH((String) fila.get(col));

                if (col.equals("COLUMNA_AI"))
                    cargaDatosPlantilla.setColumnaAI((String) fila.get(col));

                if (col.equals("COLUMNA_AJ"))
                    cargaDatosPlantilla.setColumnaAJ((String) fila.get(col));

                if (col.equals("COLUMNA_AK"))
                    cargaDatosPlantilla.setColumnaAK((String) fila.get(col));

                if (col.equals("COLUMNA_AL"))
                    cargaDatosPlantilla.setColumnaAL((String) fila.get(col));

                if (col.equals("COLUMNA_AM"))
                    cargaDatosPlantilla.setColumnaAM((String) fila.get(col));

                if (col.equals("COLUMNA_AN"))
                    cargaDatosPlantilla.setColumnaAN((String) fila.get(col));

                if (col.equals("COLUMNA_AO"))
                    cargaDatosPlantilla.setColumnaAO((String) fila.get(col));

                if (col.equals("COLUMNA_AP"))
                    cargaDatosPlantilla.setColumnaAP((String) fila.get(col));

                if (col.equals("COLUMNA_AQ"))
                    cargaDatosPlantilla.setColumnaAQ((String) fila.get(col));

                if (col.equals("COLUMNA_AR"))
                    cargaDatosPlantilla.setColumnaAR((String) fila.get(col));

                if (col.equals("COLUMNA_AS"))
                    cargaDatosPlantilla.setColumnaAS((String) fila.get(col));

                if (col.equals("COLUMNA_AT"))
                    cargaDatosPlantilla.setColumnaAT((String) fila.get(col));

                if (col.equals("COLUMNA_AU"))
                    cargaDatosPlantilla.setColumnaAU((String) fila.get(col));

                if (col.equals("COLUMNA_AV"))
                    cargaDatosPlantilla.setColumnaAV((String) fila.get(col));

                if (col.equals("COLUMNA_AW"))
                    cargaDatosPlantilla.setColumnaAW((String) fila.get(col));

                if (col.equals("COLUMNA_AX"))
                    cargaDatosPlantilla.setColumnaAX((String) fila.get(col));

                if (col.equals("COLUMNA_AY"))
                    cargaDatosPlantilla.setColumnaAY((String) fila.get(col));

                if (col.equals("COLUMNA_AZ"))
                    cargaDatosPlantilla.setColumnaAZ((String) fila.get(col));

                /*-------------------------------*/
                if (col.equals("COLUMNA_BA"))
                    cargaDatosPlantilla.setColumnaBA((String) fila.get(col));

                if (col.equals("COLUMNA_BB"))
                    cargaDatosPlantilla.setColumnaBB((String) fila.get(col));

                if (col.equals("COLUMNA_BC"))
                    cargaDatosPlantilla.setColumnaBC((String) fila.get(col));

                if (col.equals("COLUMNA_BD"))
                    cargaDatosPlantilla.setColumnaBD((String) fila.get(col));

                if (col.equals("COLUMNA_BE"))
                    cargaDatosPlantilla.setColumnaBE((String) fila.get(col));

                if (col.equals("COLUMNA_BF"))
                    cargaDatosPlantilla.setColumnaBF((String) fila.get(col));

                if (col.equals("COLUMNA_BG"))
                    cargaDatosPlantilla.setColumnaBG((String) fila.get(col));

                if (col.equals("COLUMNA_BH"))
                    cargaDatosPlantilla.setColumnaBH((String) fila.get(col));

                if (col.equals("COLUMNA_BI"))
                    cargaDatosPlantilla.setColumnaBI((String) fila.get(col));

                if (col.equals("COLUMNA_BJ"))
                    cargaDatosPlantilla.setColumnaBJ((String) fila.get(col));

                if (col.equals("COLUMNA_BK"))
                    cargaDatosPlantilla.setColumnaBK((String) fila.get(col));

                if (col.equals("COLUMNA_BL"))
                    cargaDatosPlantilla.setColumnaBL((String) fila.get(col));

                if (col.equals("COLUMNA_BM"))
                    cargaDatosPlantilla.setColumnaBM((String) fila.get(col));

                if (col.equals("COLUMNA_BN"))
                    cargaDatosPlantilla.setColumnaBN((String) fila.get(col));

                if (col.equals("COLUMNA_BO"))
                    cargaDatosPlantilla.setColumnaBO((String) fila.get(col));

                if (col.equals("COLUMNA_BP"))
                    cargaDatosPlantilla.setColumnaBP((String) fila.get(col));

                if (col.equals("COLUMNA_BQ"))
                    cargaDatosPlantilla.setColumnaBQ((String) fila.get(col));

                if (col.equals("COLUMNA_BR"))
                    cargaDatosPlantilla.setColumnaBR((String) fila.get(col));

                if (col.equals("COLUMNA_BS"))
                    cargaDatosPlantilla.setColumnaBS((String) fila.get(col));

                if (col.equals("COLUMNA_BT"))
                    cargaDatosPlantilla.setColumnaBT((String) fila.get(col));

                if (col.equals("COLUMNA_BU"))
                    cargaDatosPlantilla.setColumnaBU((String) fila.get(col));

                if (col.equals("COLUMNA_BV"))
                    cargaDatosPlantilla.setColumnaBV((String) fila.get(col));

                if (col.equals("COLUMNA_BW"))
                    cargaDatosPlantilla.setColumnaBW((String) fila.get(col));

                if (col.equals("COLUMNA_BX"))
                    cargaDatosPlantilla.setColumnaBX((String) fila.get(col));

                if (col.equals("COLUMNA_BY"))
                    cargaDatosPlantilla.setColumnaBY((String) fila.get(col));

                if (col.equals("COLUMNA_BZ"))
                    cargaDatosPlantilla.setColumnaBZ((String) fila.get(col));

                /*------------------------------------------*/
                if (col.equals("COLUMNA_CA"))
                    cargaDatosPlantilla.setColumnaCA((String) fila.get(col));

                if (col.equals("COLUMNA_CB"))
                    cargaDatosPlantilla.setColumnaCB((String) fila.get(col));

                if (col.equals("COLUMNA_CC"))
                    cargaDatosPlantilla.setColumnaCC((String) fila.get(col));

                if (col.equals("COLUMNA_CD"))
                    cargaDatosPlantilla.setColumnaCD((String) fila.get(col));

                if (col.equals("COLUMNA_CE"))
                    cargaDatosPlantilla.setColumnaCE((String) fila.get(col));

                if (col.equals("COLUMNA_CF"))
                    cargaDatosPlantilla.setColumnaCF((String) fila.get(col));

                if (col.equals("COLUMNA_CG"))
                    cargaDatosPlantilla.setColumnaCG((String) fila.get(col));

                if (col.equals("COLUMNA_CH"))
                    cargaDatosPlantilla.setColumnaCH((String) fila.get(col));

                if (col.equals("COLUMNA_CI"))
                    cargaDatosPlantilla.setColumnaCI((String) fila.get(col));

                if (col.equals("COLUMNA_CJ"))
                    cargaDatosPlantilla.setColumnaCJ((String) fila.get(col));

                if (col.equals("COLUMNA_CK"))
                    cargaDatosPlantilla.setColumnaCK((String) fila.get(col));

                if (col.equals("COLUMNA_CL"))
                    cargaDatosPlantilla.setColumnaCL((String) fila.get(col));

                if (col.equals("COLUMNA_CM"))
                    cargaDatosPlantilla.setColumnaCM((String) fila.get(col));

                if (col.equals("COLUMNA_CN"))
                    cargaDatosPlantilla.setColumnaCN((String) fila.get(col));

                if (col.equals("COLUMNA_CO"))
                    cargaDatosPlantilla.setColumnaCO((String) fila.get(col));

                if (col.equals("COLUMNA_CP"))
                    cargaDatosPlantilla.setColumnaCP((String) fila.get(col));

                if (col.equals("COLUMNA_CQ"))
                    cargaDatosPlantilla.setColumnaCQ((String) fila.get(col));

                if (col.equals("COLUMNA_CR"))
                    cargaDatosPlantilla.setColumnaCR((String) fila.get(col));

                if (col.equals("COLUMNA_CS"))
                    cargaDatosPlantilla.setColumnaCS((String) fila.get(col));

                if (col.equals("COLUMNA_CT"))
                    cargaDatosPlantilla.setColumnaCT((String) fila.get(col));

                if (col.equals("COLUMNA_CU"))
                    cargaDatosPlantilla.setColumnaCU((String) fila.get(col));

                if (col.equals("COLUMNA_CV"))
                    cargaDatosPlantilla.setColumnaCV((String) fila.get(col));

                if (col.equals("COLUMNA_CW"))
                    cargaDatosPlantilla.setColumnaCW((String) fila.get(col));

                if (col.equals("COLUMNA_CX"))
                    cargaDatosPlantilla.setColumnaCX((String) fila.get(col));

                if (col.equals("COLUMNA_CY"))
                    cargaDatosPlantilla.setColumnaCY((String) fila.get(col));

                if (col.equals("COLUMNA_CZ"))
                    cargaDatosPlantilla.setColumnaCZ((String) fila.get(col));
            }

            lista.add(cargaDatosPlantilla);
        }

        cargaDatosPlantillaRepository.saveAll(lista);


        boolean existe=false;
        if (cargaMasivaBean.getIdCargaDatos()!=null){
            Optional<CargaDatosPlantillaHead> cargaDatosPlantillaHeadOp = cargaDatosPlantillaHeadRepository.findById(cargaMasivaBean.getIdCargaDatos());
            if (cargaDatosPlantillaHeadOp.isPresent()){
                existe=true;
                cargaDatosPlantillaHeadOp.get().setPlantilla(plantilla);
                cargaDatosPlantillaHeadOp.get().setIdEntePublico(cargaMasivaBean.getIdEntePublico());
                cargaDatosPlantillaHeadOp.get().setUsuarioCargaDato(cargaMasivaBean.getUsuario());
                cargaDatosPlantillaHeadOp.get().setFechaRegistro(fechaRegistro);
                cargaDatosPlantillaHeadOp.get().setFechaUltimoCambio(fechaRegistro);
                cargaDatosPlantillaHeadOp.get().setFechaCorte(Timestamp.valueOf(localDateTime));
                cargaDatosPlantillaHeadOp.get().setEstatus("PE");
                cargaDatosPlantillaHeadOp.get().setTotalRegistros(lista.size());
                cargaDatosPlantillaHeadOp.get().setTotalError(0);
                cargaDatosPlantillaHeadRepository.save(cargaDatosPlantillaHeadOp.get());
            }
        }

        if (!existe){
            CargaDatosPlantillaHead cargaDatosPlantillaHead = new CargaDatosPlantillaHead();
            cargaDatosPlantillaHead.setIdCargaDatosPlantilla(sequencia.longValue());
            cargaDatosPlantillaHead.setPlantilla(plantilla);
            cargaDatosPlantillaHead.setIdEntePublico(cargaMasivaBean.getIdEntePublico());
            cargaDatosPlantillaHead.setUsuarioCargaDato(cargaMasivaBean.getUsuario());
            cargaDatosPlantillaHead.setFechaRegistro(fechaRegistro);
            cargaDatosPlantillaHead.setFechaUltimoCambio(fechaRegistro);
            cargaDatosPlantillaHead.setFechaCorte(Timestamp.valueOf(localDateTime));
            cargaDatosPlantillaHead.setEstatus("PE");
            cargaDatosPlantillaHead.setTotalRegistros(lista.size());
            cargaDatosPlantillaHead.setTotalError(0);
            cargaDatosPlantillaHeadRepository.save(cargaDatosPlantillaHead);
        }

    }

    @Override
    public Optional<Plantilla> getPlantillaId(Long idPlantilla) {
        return plantillaRepository.findById(idPlantilla);
    }

    @Override
    public List<CargaMasivaTablaBean> getTablaCargaMasiva(CargaMasivaTablaBean cargaMasivaTablaBean) {
        List<CargaMasivaTablaBean> datos = cargaMasivaTablaBeanRepository.getCargaMasivaBean(cargaMasivaTablaBean.getIdPlantilla(), cargaMasivaTablaBean.getUsuario());

        return datos;
    }

    @Override
    public List<CargaDatosPlantillaHead> getCargaDatosPlantillaHead(CargaDatosPlantillaHead cargaDatosPlantillaHead) {
        Plantilla plantilla = plantillaRepository.findById(cargaDatosPlantillaHead.getPlantilla().getIdPlantilla()).get();
        return cargaDatosPlantillaHeadRepository.findAllByPlantillaAndUsuarioCargaDatoOrderByFechaCorteDesc(plantilla, cargaDatosPlantillaHead.getUsuarioCargaDato());
    }

    public void generaPlantilla(Long idPlantilla, HttpServletResponse response) {

        List<Campo> listaCampo = campoRepository.findAllByOrderByOrden();

        Map<String, Integer> campoMap = listaCampo.stream().collect(Collectors.toMap(Campo::getNombreCampo, Campo::getOrden));

        Optional<Plantilla> plantillaOp = plantillaRepository.findById(idPlantilla);

        if (!plantillaOp.isPresent())
            throw new NoSuchElementException("La plantilla no existe");

        List<Campos> campos = camposService.getCamposPorPlantilla(plantillaOp.get(), "VIG");

        if (plantillaOp.get().getNumLinea()<=0)
            throw new NoSuchElementException("El encabezado de la plantilla no tiene configurado un valor > 0");

        if (campos.size() == 0)
            throw new NoSuchElementException("La plantilla no tiene configurado campos");

        Separador separador = null;

        if (plantillaOp.get().getIdSeparador() != null)
            separador = separadorRepository.findById(plantillaOp.get().getIdSeparador()).get();

        String ext = Utileria.getFileExtension(plantillaOp.get().getNombreArchivo()).toLowerCase();
        File folders = new File(System.getProperty("user.home") + "/repositorio/carga_masiva/plantilla/");

        if (ext.equals("xlsx")) {
            try {
                XSSFWorkbook  workbook = new XSSFWorkbook();
                XSSFSheet sheet = workbook.createSheet();
                workbook.setSheetName(0, "Hoja excel");

                    CellStyle headerStyle = workbook.createCellStyle();
                    Font font = workbook.createFont();
                    font.setBold(true);
                    headerStyle.setFont(font);


/*                    CellStyle style = workbook.createCellStyle();
                    style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);*/


                    //HSSFRow headerRow = sheet.createRow(0)
                    Row headerRow = sheet.createRow(plantillaOp.get().getNumLinea()-1);
                    for (Campos campo:campos){
                        int index=campoMap.get(campo.getUbicacionColumna()).intValue()-1;
                        Cell cell = headerRow.createCell(index);
                        cell.setCellStyle(headerStyle);
                        cell.setCellValue(campo.getEtiquetaColumna());
                        sheet.autoSizeColumn(index);
                    }

                if (!folders.exists())
                    folders.mkdirs();

                File verifica = new File(folders.getPath() + "/" + plantillaOp.get().getNombreArchivo());
                File archivo;
                if (verifica.exists()) {
                    verifica.delete();
                    archivo = new File(folders.getPath() + "/" + plantillaOp.get().getNombreArchivo());
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
                File file = new File(folders.getPath() + "/" + plantillaOp.get().getNombreArchivo());
                InputStream inputStream = new FileInputStream(file);
                XSSFWorkbook wb = new XSSFWorkbook(inputStream);
                inputStream.close();

                response.setContentType("multipart/form-data");
                response.setHeader("Content-disposition", "attachment;filename=" + plantillaOp.get().getNombreArchivo());
                response.setHeader("Pragma", "No-cache");
                response.setCharacterEncoding("utf-8");
                ServletOutputStream out = response.getOutputStream();
                wb.write(out);
                out.flush();
                out.close();

            } catch (Exception e) {
                throw new NoSuchElementException(e.getMessage());
            }
        }else
        if(ext.equals("txt")){
            String concatenar="";
            List<String> encabezados = new ArrayList<>();

            for (Campos campo : campos) {
                encabezados.add(campo.getEtiquetaColumna());
            }

            for (int i = 0; i < encabezados.size(); ++i) {
                if(concatenar.equals("")){
                    concatenar = encabezados.get(i);
                }else{
                    if(plantillaOp.get().getIdSeparador() !=null){
                        concatenar += " "+separador.getCodigo()+" "+encabezados.get(i);
                    }else{
                        concatenar += " ~~ "+encabezados.get(i);
                    }

                }
            }

            File archivo=new File(folders.getPath()+"/"+plantillaOp.get().getNombreArchivo());
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(archivo, "UTF-8");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("No soporta la codificación UTF-8");
            }


            if(plantillaOp.get().getNumLinea()>1){
                writer.println(concatenar);
            }
            writer.close();

            try
            {
                File file = new File(folders.getPath() + "/" + plantillaOp.get().getNombreArchivo());
                InputStream inputStream = new FileInputStream(file);
                int ARBITARY_SIZE = 1048;

                response.setContentType("text/plain");
                response.setHeader("Content-disposition", "attachment;filename=" + plantillaOp.get().getNombreArchivo());
                response.setHeader("Pragma", "No-cache");
                response.setCharacterEncoding("utf-8");
                ServletOutputStream out = response.getOutputStream();

                byte[] buffer = new byte[ARBITARY_SIZE];
                int numBytesRead;
                while ((numBytesRead = inputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, numBytesRead);
                }
                inputStream.close();


                out.flush();
                out.close();

            } catch (Exception e) {
                throw new NoSuchElementException(e.getMessage());
            }

        }
    }

    @Transactional(value = "siggetTransactionManager")
    @Override
    public void deleteCarga(CargaDatosPlantilla cargaDatosPlantilla) {
        cargaDatosPlantillaErrorRepository.deleteAllByIdCargaDatosPlantilla(cargaDatosPlantilla.getCargaDatosPlantillaPk().getIdCargaDatosPlantilla().longValue());
        Optional<CargaDatosPlantillaHead> cargaDatosPlantillaHeadOp = cargaDatosPlantillaHeadRepository.findById(cargaDatosPlantilla.getCargaDatosPlantillaPk().getIdCargaDatosPlantilla().longValue());
        if (cargaDatosPlantillaHeadOp.isPresent())
            cargaDatosPlantillaHeadRepository.delete(cargaDatosPlantillaHeadOp.get());

        cargaDatosPlantillaRepository.deleteCargaMasiva(cargaDatosPlantilla.getCargaDatosPlantillaPk().getIdCargaDatosPlantilla(), cargaDatosPlantilla.getPlantilla().getIdPlantilla());
    }

    @Override
    public JSONObject getCargaMasivaDetalle(Long idCargaDatos, Long idPlantilla){
        JSONObject datosJson= new JSONObject();

        Optional<Plantilla> plantillaOp = plantillaRepository.findById(idPlantilla);

        if (!plantillaOp.isPresent())
            throw new NoSuchElementException("La plantilla no existe");

        List<Campos> campos = camposService.getCamposPorPlantilla(plantillaOp.get(), "VIG");

        List<CargaDatosPlantilla> lista = cargaDatosPlantillaRepository.listaCargaDatos(idCargaDatos);

        List<TitleBean> listaTitle = new ArrayList<>();
        List<ColumnaBean> listaColumna = new ArrayList<>();
        TitleBean titleBeanLinea = new TitleBean();
        titleBeanLinea.setTitle("NÚM LÍNEA");
        titleBeanLinea.setTarget(0);
        listaTitle.add(titleBeanLinea);
        ColumnaBean columnaBeanLinea= new ColumnaBean();
        columnaBeanLinea.setData("cargaDatosPlantillaPk.numLineaCargaDatos");
        listaColumna.add(columnaBeanLinea);

        int i=1;
        for(Campos campo:campos){
            TitleBean titleBean = new TitleBean();
            titleBean.setTitle(campo.getEtiquetaColumna());
            titleBean.setTarget(i);
            listaTitle.add(titleBean);
            ColumnaBean columnaBean= new ColumnaBean();
            String[] columna = campo.getCampoBaseDatos().split("_");
            columnaBean.setData(columna[0].toLowerCase()+columna[1]);
            listaColumna.add(columnaBean);
            i++;
        }

        datosJson.put("nombrePlantilla",plantillaOp.get().getNombrePlantilla());
        datosJson.put("titulos",listaTitle);
        datosJson.put("columnas",listaColumna);
        datosJson.put("datos",lista);

        return datosJson;
    }

    private CargaDatosPlantillaError crearCeldaError(Long idCargaDatoPlantilla, Integer numLineaPlantilla,String columna, String error){
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CargaDatosPlantillaError celdaError = new CargaDatosPlantillaError();
        celdaError.setIdCargaDatosPlantilla(idCargaDatoPlantilla);
        celdaError.setNumLineaPlantilla(numLineaPlantilla);
        celdaError.setColumna(columna);
        celdaError.setInfoError(error);
        celdaError.setUsuarioRegistro(authUser.getUsername());
        celdaError.setFechaRegistro(new Timestamp(new Date().getTime()));
        return celdaError;
    }

    @Transactional(value = "siggetTransactionManager")
    @Override
    public JSONObject validarCargaMasiva(Long idCargaDatos, Long idPlantilla){

        cargaDatosPlantillaErrorRepository.deleteAllByIdCargaDatosPlantilla(idCargaDatos.longValue());

        CargaDatosPlantillaHead cargaDatosPlantillaHead = cargaDatosPlantillaHeadRepository.findById(idCargaDatos.longValue()).get();

        if (cargaDatosPlantillaHead.getEstatus().equals("CA"))
            throw new NoSuchElementException("La plantilla ya esta cargada");

        cargaDatosPlantillaHead.setTotalError(0);
        cargaDatosPlantillaHead.setFechaUltimoCambio(new Timestamp(new Date().getTime()));


        JSONObject datosJson= new JSONObject();

        Optional<Plantilla> plantillaOp = plantillaRepository.findById(idPlantilla);

        if (!plantillaOp.isPresent())
            throw new NoSuchElementException("La plantilla no existe");

        List<Campos> campos = camposService.getCamposPorPlantilla(plantillaOp.get(), "VIG");

        List<CargaDatosPlantilla> lista = cargaDatosPlantillaRepository.listaCargaDatos(idCargaDatos);

        List<CargaDatosPlantillaError> listaError = new ArrayList<>();

        for (CargaDatosPlantilla datos : lista) {
            Integer linea= datos.getCargaDatosPlantillaPk().getNumLineaCargaDatos();

            for (Campos campo:campos) {
                String valorOriginal="";
                if (campo.getCampoBaseDatos().equals("COLUMNA_A"))
                    valorOriginal=datos.getColumnaA();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_B"))
                    valorOriginal=datos.getColumnaB();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_C"))
                    valorOriginal=datos.getColumnaC();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_D"))
                    valorOriginal=datos.getColumnaD();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_E"))
                    valorOriginal=datos.getColumnaE();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_F"))
                    valorOriginal=datos.getColumnaF();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_G"))
                    valorOriginal=datos.getColumnaG();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_H"))
                    valorOriginal=datos.getColumnaH();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_I"))
                    valorOriginal=datos.getColumnaI();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_J"))
                    valorOriginal=datos.getColumnaJ();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_K"))
                    valorOriginal=datos.getColumnaK();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_L"))
                    valorOriginal=datos.getColumnaL();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_M"))
                    valorOriginal=datos.getColumnaM();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_N"))
                    valorOriginal=datos.getColumnaN();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_O"))
                    valorOriginal=datos.getColumnaO();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_P"))
                    valorOriginal=datos.getColumnaP();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_Q"))
                    valorOriginal=datos.getColumnaQ();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_R"))
                    valorOriginal=datos.getColumnaR();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_S"))
                    valorOriginal=datos.getColumnaS();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_T"))
                    valorOriginal=datos.getColumnaT();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_U"))
                    valorOriginal=datos.getColumnaU();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_V"))
                    valorOriginal=datos.getColumnaV();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_W"))
                    valorOriginal=datos.getColumnaW();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_X"))
                    valorOriginal=datos.getColumnaX();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_Y"))
                    valorOriginal=datos.getColumnaY();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_Z"))
                    valorOriginal=datos.getColumnaZ();
                //--------------------------------------------------
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AA"))
                    valorOriginal=datos.getColumnaAA();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AB"))
                    valorOriginal=datos.getColumnaAB();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AC"))
                    valorOriginal=datos.getColumnaAC();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AD"))
                    valorOriginal=datos.getColumnaAD();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AE"))
                    valorOriginal=datos.getColumnaAE();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AF"))
                    valorOriginal=datos.getColumnaAF();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AG"))
                    valorOriginal=datos.getColumnaAG();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AH"))
                    valorOriginal=datos.getColumnaAH();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AI"))
                    valorOriginal=datos.getColumnaAI();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AJ"))
                    valorOriginal=datos.getColumnaAJ();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AK"))
                    valorOriginal=datos.getColumnaAK();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AL"))
                    valorOriginal=datos.getColumnaAL();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AM"))
                    valorOriginal=datos.getColumnaAM();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AN"))
                    valorOriginal=datos.getColumnaAN();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AO"))
                    valorOriginal=datos.getColumnaAO();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AP"))
                    valorOriginal=datos.getColumnaAP();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AQ"))
                    valorOriginal=datos.getColumnaAQ();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AR"))
                    valorOriginal=datos.getColumnaAR();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AS"))
                    valorOriginal=datos.getColumnaAS();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AT"))
                    valorOriginal=datos.getColumnaAT();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AU"))
                    valorOriginal=datos.getColumnaAU();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AV"))
                    valorOriginal=datos.getColumnaAV();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AW"))
                    valorOriginal=datos.getColumnaAW();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AX"))
                    valorOriginal=datos.getColumnaAX();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AY"))
                    valorOriginal=datos.getColumnaAY();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_AZ"))
                    valorOriginal=datos.getColumnaAZ();
                //--------------------------------------------------
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BA"))
                    valorOriginal=datos.getColumnaBA();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BB"))
                    valorOriginal=datos.getColumnaBB();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BC"))
                    valorOriginal=datos.getColumnaBC();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BD"))
                    valorOriginal=datos.getColumnaBD();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BE"))
                    valorOriginal=datos.getColumnaBE();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BF"))
                    valorOriginal=datos.getColumnaBF();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BG"))
                    valorOriginal=datos.getColumnaBG();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BH"))
                    valorOriginal=datos.getColumnaBH();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BI"))
                    valorOriginal=datos.getColumnaBI();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BJ"))
                    valorOriginal=datos.getColumnaBJ();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BK"))
                    valorOriginal=datos.getColumnaBK();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BL"))
                    valorOriginal=datos.getColumnaBL();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BM"))
                    valorOriginal=datos.getColumnaBM();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BN"))
                    valorOriginal=datos.getColumnaBN();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BO"))
                    valorOriginal=datos.getColumnaBO();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BP"))
                    valorOriginal=datos.getColumnaBP();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BQ"))
                    valorOriginal=datos.getColumnaBQ();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BR"))
                    valorOriginal=datos.getColumnaBR();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BS"))
                    valorOriginal=datos.getColumnaBS();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BT"))
                    valorOriginal=datos.getColumnaBT();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BU"))
                    valorOriginal=datos.getColumnaBU();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BV"))
                    valorOriginal=datos.getColumnaBV();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BW"))
                    valorOriginal=datos.getColumnaBW();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BX"))
                    valorOriginal=datos.getColumnaBX();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BY"))
                    valorOriginal=datos.getColumnaBY();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_BZ"))
                    valorOriginal=datos.getColumnaBZ();
                //--------------------------------------------------
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CA"))
                    valorOriginal=datos.getColumnaCA();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CB"))
                    valorOriginal=datos.getColumnaCB();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CC"))
                    valorOriginal=datos.getColumnaCC();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CD"))
                    valorOriginal=datos.getColumnaCD();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CE"))
                    valorOriginal=datos.getColumnaCE();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CF"))
                    valorOriginal=datos.getColumnaCF();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CG"))
                    valorOriginal=datos.getColumnaCG();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CH"))
                    valorOriginal=datos.getColumnaCH();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CI"))
                    valorOriginal=datos.getColumnaCI();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CJ"))
                    valorOriginal=datos.getColumnaCJ();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CK"))
                    valorOriginal=datos.getColumnaCK();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CL"))
                    valorOriginal=datos.getColumnaCL();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CM"))
                    valorOriginal=datos.getColumnaCM();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CN"))
                    valorOriginal=datos.getColumnaCN();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CO"))
                    valorOriginal=datos.getColumnaCO();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CP"))
                    valorOriginal=datos.getColumnaCP();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CQ"))
                    valorOriginal=datos.getColumnaCQ();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CR"))
                    valorOriginal=datos.getColumnaCR();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CS"))
                    valorOriginal=datos.getColumnaCS();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CT"))
                    valorOriginal=datos.getColumnaCT();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CU"))
                    valorOriginal=datos.getColumnaCU();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CV"))
                    valorOriginal=datos.getColumnaCV();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CW"))
                    valorOriginal=datos.getColumnaCW();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CX"))
                    valorOriginal=datos.getColumnaCX();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CY"))
                    valorOriginal=datos.getColumnaCY();
                else
                if (campo.getCampoBaseDatos().equals("COLUMNA_CZ"))
                    valorOriginal=datos.getColumnaCZ();

                //--------------------------------------------------


                boolean sinError = true;

                if (campo.getObligatorio().equals("S")) {
                    if (valorOriginal == null || valorOriginal.equals("")) {
                        listaError.add(crearCeldaError(idCargaDatos, linea, campo.getCampoBaseDatos(), "El campo es obligatorio, con etiqueta " + campo.getEtiquetaColumna()));
                        sinError=false;
                    }
                }

                if (sinError && valorOriginal != null && !valorOriginal.equals("")) {

                    String valor=valorOriginal;

                    if (campo.getOmitirTextValiacion()!= null && !campo.getOmitirTextValiacion().trim().equals("")) {
                        String[] omitirTexto = campo.getOmitirTextValiacion().trim().split("|");
                        for (String texto : omitirTexto) {
                            valor = valor.replaceAll(texto.trim(), "");
                        }
                    }

                    valor=valor.trim();
                    //Validando campos date
                    if (campo.getTipoCampo().getCveTipoCampo().equals("D") && (campo.getMascara() == null || campo.getMascara().trim().equals("")))
                        listaError.add(crearCeldaError(idCargaDatos, linea, campo.getEtiquetaColumna(), "El campo requiere una máscara"));
                    else
                        if (valor!=null && campo.getTipoCampo().getCveTipoCampo().equals("D") && !Utileria.validarFecha(valor, campo.getMascara().trim()))
                            listaError.add(crearCeldaError(idCargaDatos, linea, campo.getEtiquetaColumna(), "El campo no es una fecha valida"));
                    else
                        if (campo.getTipoCampo().getCveTipoCampo().equals("N") && !Utileria.isBigDecimal(valor))
                            listaError.add(crearCeldaError(idCargaDatos, linea, campo.getEtiquetaColumna(), "El campo no es una númerico"));
                    //Validando campos Integer
                    else
                        if (campo.getTipoCampo().getCveTipoCampo().equals("I") && !Utileria.isLong(valor))
                            listaError.add(crearCeldaError(idCargaDatos, linea, campo.getEtiquetaColumna(), "El campo no es un entero"));
                    else
                        if (campo.getTipoCampo().getCveTipoCampo().equals("I") && (campo.getMascara() != null && !campo.getMascara().trim().equals("") &&  !Utileria.validarExpresionRegular(valor, campo.getMascara())))
                            listaError.add(crearCeldaError(idCargaDatos, linea, campo.getEtiquetaColumna(), "El campo no pasa la validación de la expresión regular"));
                    else
                        if (campo.getTipoCampo().getCveTipoCampo().equals("I") && campo.getLongitud()!=null && valor.length()>campo.getLongitud().intValue())
                            listaError.add(crearCeldaError(idCargaDatos, linea, campo.getEtiquetaColumna(), "La cantidad de carácteres debe ser menor a la longitud configurada"));
                    //Validando campos Númericos
                    else
                        if (campo.getTipoCampo().getCveTipoCampo().equals("N") && !Utileria.isBigDecimal(valor))
                            listaError.add(crearCeldaError(idCargaDatos, linea, campo.getEtiquetaColumna(), "El campo no es un numerico"));
                    else
                        if (campo.getTipoCampo().getCveTipoCampo().equals("N") && (campo.getMascara() != null && !campo.getMascara().trim().equals("") &&  !Utileria.validarExpresionRegular(valor, campo.getMascara())))
                            listaError.add(crearCeldaError(idCargaDatos, linea, campo.getEtiquetaColumna(), "El campo no pasa la validación de la expresión regular"));
                    else
                        if (campo.getTipoCampo().getCveTipoCampo().equals("N") && campo.getLongitud()!=null && valor.length()>campo.getLongitud().intValue())
                            listaError.add(crearCeldaError(idCargaDatos, linea, campo.getEtiquetaColumna(), "La cantidad de carácteres debe ser menor a la longitud configurada"));
                    else
                        if (campo.getTipoCampo().getCveTipoCampo().equals("N") && campo.getLongitudDecimal()!=null && Utileria.longitudExtension(valor)>campo.getLongitudDecimal().intValue())
                            listaError.add(crearCeldaError(idCargaDatos, linea, campo.getEtiquetaColumna(), "La cantidad de carácteres decimales debe ser menor a la longitud decimal configurada"));
                    //Validando campos VARCHAR
                    else
                        if (campo.getTipoCampo().getCveTipoCampo().equals("V") && (campo.getMascara() != null && !campo.getMascara().trim().equals("") &&  !Utileria.validarExpresionRegular(valor, campo.getMascara())))
                            listaError.add(crearCeldaError(idCargaDatos, linea, campo.getEtiquetaColumna(), "El campo no pasa la validación de la expresión regular"));
                    else
                        if (campo.getTipoCampo().getCveTipoCampo().equals("V") && campo.getLongitud()!=null && valor.length()>campo.getLongitud().intValue())
                            listaError.add(crearCeldaError(idCargaDatos, linea, campo.getEtiquetaColumna(), "La cantidad de carácteres debe ser menor a la longitud configurada"));
                }

            }

        }

        if (listaError.size()>0){
            cargaDatosPlantillaErrorRepository.saveAll(listaError);
            cargaDatosPlantillaHead.setTotalError(listaError.size());
            cargaDatosPlantillaHead.setEstatus("VE");
            datosJson.put("msg","La carga se ha validado con "+listaError.size()+" error(es)");
        }else{
            cargaDatosPlantillaHead.setEstatus("VA");
            datosJson.put("msg","La carga se ha validado con exito");
        }

            cargaDatosPlantillaHeadRepository.save(cargaDatosPlantillaHead);

        return datosJson;

    }


    @Override
    public List<CargaDatosPlantillaError> getErroresPlantilla(Long idCargaDatos){

        return cargaDatosPlantillaErrorRepository.findAllByIdCargaDatosPlantillaOrderByNumLineaPlantilla(idCargaDatos);

    }

    @Override
    public String ejecutarSql(Long idCargaDatos) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return cargaMasivaTablaBeanRepository.ejecutaSql(idCargaDatos, authUser.getRfc(), authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico());
        //return cargaDatosPlantillaRepository.ejecutaSql(idCargaDatos, authUser.getUsername());
    }


    @Override
    public JSONObject getConcentradoBienesEstatus() {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ConcentradoCargaBean> listaConcentrado = null;

        if (authUser.isAdministradorSigtic())
            listaConcentrado = concentradoCargaBeanRepository.getConcentradoBienesEstatus();
        else
            listaConcentrado = concentradoCargaBeanRepository.getConcentradoBienesEstatusId(authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("listaConcentrado", listaConcentrado);

        return jsonObject;
    }

    @Override
    public List<ConcentradoCargaEnteBean> getConcentradoBienesEntePublico(String estatus) {
        UserDetailsSesion authUser = (UserDetailsSesion) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ConcentradoCargaEnteBean> listaConcentrado = null;
        if (authUser.isAdministradorSigtic())
            listaConcentrado = concentradoCargaEnteBeanRepository.getConcentradoBienesEntePublico(estatus);
        else
            listaConcentrado = concentradoCargaEnteBeanRepository.getConcentradoBienesEntePublicoId(authUser.getCatUsuarioEntePublico().getEntePublico().getIdEntePublico(), estatus);

        return listaConcentrado;
    }


}
