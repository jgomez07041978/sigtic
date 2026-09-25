package mx.gob.tabasco.cmaig.sigtic.Utileria;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class UtileriaReporte implements Serializable {

    public static ResponseEntity<byte[]> jasperCreatePdf(String pathJrxml, Map<String, Object> parametrosReporte, List<?> lista, String nombrePdf){
        try {
           /* File file = ResourceUtils.getFile("classpath:employee-rpt.jrxml");*/
/*            File file = ResourceUtils.getFile(pathJrxml);
            InputStream input = new FileInputStream(file);*/
            File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + pathJrxml);
            InputStream in = new FileInputStream(file);


            JasperPrint qrReport =
                    JasperFillManager.fillReport(
                            JasperCompileManager.compileReport(in), parametrosReporte, new JRBeanCollectionDataSource(lista));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", nombrePdf);

            return new ResponseEntity<byte[]> (JasperExportManager.exportReportToPdf(qrReport), headers, HttpStatus.OK);

        }catch(Exception e) {

            System.out.println(e);
            return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public static ResponseEntity<byte[]> jasperCreatePdf(byte [] reporte, String nombrePdf){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", nombrePdf);

            return new ResponseEntity<byte[]> (reporte, headers, HttpStatus.OK);

        }catch(Exception e) {
            return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
