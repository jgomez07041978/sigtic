package mx.gob.tabasco.cmaig.sigtic.sac.rhGob;


import mx.gob.tabasco.cmaig.sigtic.sac.model.RhGobEmpleado;
import mx.gob.tabasco.cmaig.sigtic.sac.repository.RhGobEmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class RhGobServiceImp implements RhGobService{

    @Autowired
    private RhGobPropertiesConfig rhGobPropertiesConfig;


    @Autowired
    private RhGobEmpleadoRepository rhGobEmpleadoRepository;

    @Override
    public RhGobToken obtenerRhGobToken(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(rhGobPropertiesConfig.getUrlToken())
                .queryParam("email", "{email}")
                .queryParam("password", "{password}")
                .encode()
                .toUriString();

        Map<String, String> map = new HashMap<>();
        map.put("email",rhGobPropertiesConfig.getEmail());
        map.put("password",rhGobPropertiesConfig.getPassword());



        RestTemplate restTemplate = new RestTemplate();

        try {

            ResponseEntity<RhGobToken> response = restTemplate.exchange(urlTemplate, HttpMethod.GET, httpEntity, RhGobToken.class, map);

            //return response;

            if (response.getStatusCode() == HttpStatus.OK)
                return response.getBody();
            else{
                RhGobToken rhGobToken =new RhGobToken();
                rhGobToken.setError("Error al obtener el token");

                return rhGobToken;
            }

        }catch (Exception e){

            RhGobToken rhGobToken =new RhGobToken();

            if (e.getMessage().contains("401 UNAUTHORIZED")){
                    rhGobToken.setError("Petición no autorizada");
            }else
            if (e.getMessage().contains("404 NOT FOUND")){
                rhGobToken.setError("API no encontrada");
            }
            else
                rhGobToken.setError("Error al obtener el token");
            return rhGobToken;

        }


       /*
       Está también funciona

       MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("email", rhGobPropertiesConfig.getEmail());
        requestMap.add("password", rhGobPropertiesConfig.getPassword());

        RhGobToken response = restTemplate.postForObject(rhGobPropertiesConfig.getUrlToken(), requestMap, RhGobToken.class);

        return response;
        */

    }

    @Transactional(value = "catalogosTransactionManager")
    @Override
    public RhGobDatos1 obtenerDatosEmpleados() {

        rhGobEmpleadoRepository.truncarTabla();

        RhGobToken rhGobToken = obtenerRhGobToken();

        if (rhGobToken.getError()==null || rhGobToken.getError().isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + rhGobToken.getToken());
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);


            String urlTemplate = UriComponentsBuilder.fromHttpUrl(rhGobPropertiesConfig.getUrlEmpleadoEstatus())
                    .encode()
                    .toUriString();


            RestTemplate restTemplate = new RestTemplate();

            try {

                ResponseEntity<RhGobDatos1> response = restTemplate.exchange(urlTemplate, HttpMethod.GET, httpEntity, RhGobDatos1.class);

                //return response;

                if (response.getStatusCode() == HttpStatus.OK){
                    RhGobDatos1 empleados=response.getBody();

                    List<RhGobEmpleado> lista= new ArrayList<>();

                    for(RhGobEmpleadoBean datos:empleados.datos){
                        RhGobEmpleado empleado=new RhGobEmpleado();
                        empleado.setRfc(datos.getRfc());
                        empleado.setExpedienteEmpleado(datos.getExpediente_empleado());
                        empleado.setNombreCompleto(datos.getNombre_completo());
                        empleado.setNombre(datos.getNombre());
                        empleado.setApellidoPaterno(datos.getApellido_paterno());
                        empleado.setApellidoMaterno(datos.getApellido_materno());
                        empleado.setEstatusEmpleado(datos.getEstatus_empleado());
                        empleado.setClaveEntePublico(datos.getClave_ente());
                        empleado.setNombreEntePublico(datos.getNombre_ente());
                        empleado.setClaveUnidadAdministrativa(datos.getClave_unidad_administrativa());
                        empleado.setNombreUnidadAdministrativa(datos.getNombre_unidad_administrativa());
                        lista.add(empleado);
                    }

                    rhGobEmpleadoRepository.saveAll(lista);
                    return empleados;
                }
                else
                    throw new NoSuchElementException("Error al obtener los empleados");

            }catch (Exception e) {

                if (e.getMessage().contains("401 UNAUTHORIZED")) {
                    throw new NoSuchElementException("Petición no autorizada");
                } else if (e.getMessage().contains("404 NOT FOUND")) {
                    throw new NoSuchElementException("API no encontrada");
                } else
                    throw new NoSuchElementException("Error al obtener los empleados");

            }
        }else
            throw new NoSuchElementException("Error al obtener los empleados");

    }

}
