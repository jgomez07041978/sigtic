package mx.gob.tabasco.cmaig.sigtic.sai.restController;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kml")
public class CulturaRestController {

    @GetMapping(value="/{filename}", produces = "application/vnd.google-earth.kml+xml")
    public ResponseEntity<Resource> descargarKml(@PathVariable String filename) {
        try {
        Resource resource = new ClassPathResource("static/kml/" + filename);

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }


            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.google-earth.kml+xml"))
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

}
