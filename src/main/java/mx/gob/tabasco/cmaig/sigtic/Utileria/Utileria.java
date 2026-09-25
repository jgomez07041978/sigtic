package mx.gob.tabasco.cmaig.sigtic.Utileria;

import org.apache.poi.hpsf.Array;
import org.apache.poi.hpsf.Decimal;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utileria {
    public static String removerAcento(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return str;
    }

    public static String getFileExtension(String pFileName) {
        String fileName = pFileName;
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else
            return "";
    }

    public static boolean validaTipoValor(String tipo,String dato, String formato){
        switch(tipo) {
            case "N":
                try
                {
                    Double.parseDouble(dato);
                    return true;
                }
                catch(NumberFormatException nfeD)
                {
                    try {
                        Integer.parseInt(dato);
                        return true;
                    } catch (NumberFormatException nfe){
                        return false;
                    }
                }
            case "I":
                try {
                    Integer.parseInt(dato);
                    return true;
                } catch (NumberFormatException nfe){
                    return false;
                }
            case "D":
                try{
                    SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
                    formatoFecha.setLenient(false);
                    formatoFecha.parse(dato);
                    return true;
                }catch(ParseException de){
                    return false;
                }

            case "V":
                return true;
        }
        return false;
    }


    public static boolean isIntOFloat(String dato){

        int decNumberInt = Integer.parseInt(dato.substring(dato.indexOf('.') + 1));

        if(decNumberInt>0)
            return true;
        else
            return false;
    }

    public static boolean isLong(BigDecimal dato){
        try{
            long valor = dato.longValue();
            return true;
        }catch(Exception de){
            return false;
        }
    }

    public static boolean isLong(String dato){
        try{
            long valor = Long.parseLong(dato);
            return true;
        }catch(Exception de){
            return false;
        }
    }

    public static boolean isBigDecimal(String dato){
        try{
            BigDecimal valor = new BigDecimal(dato);
            return true;
        }catch(Exception de){
            return false;
        }
    }

    public static  boolean validarFecha(String valor, String mascara){

        try{
            SimpleDateFormat formatoFecha = new SimpleDateFormat(mascara);
            formatoFecha.setLenient(false);
            formatoFecha.parse(valor);
            return true;
        }catch(ParseException de){
            return false;
        }
    }

    public static  boolean validarExpresionRegular(String valor, String expresionRegular){
        Pattern pattern = Pattern.compile(expresionRegular);
        Matcher matcher = pattern.matcher(valor);
        return matcher.matches();
    }


    public static  int longitudExtension(String valor){
        String extension = getFileExtension(valor);
        return extension.length();
    }

    public static void  writeFile(byte[] pFile,String nombre, String path) throws IOException {
        byte[] bytes = pFile;
        BufferedOutputStream streamKey = new BufferedOutputStream(new FileOutputStream(path+"/"+nombre));
        streamKey.write(bytes);
        streamKey.close();
    }

    public static String quitarAcentos(String texto) {
        if (texto == null) return null;

        String textoProtegido = texto.replace("ñ", "___n_min___")
                .replace("Ñ", "___N_MAY___");

        String normalized = Normalizer.normalize(textoProtegido, Normalizer.Form.NFD);
        String sinAcentos = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return sinAcentos.replace("___n_min___", "ñ")
                .replace("___N_MAY___", "Ñ");
    }

}