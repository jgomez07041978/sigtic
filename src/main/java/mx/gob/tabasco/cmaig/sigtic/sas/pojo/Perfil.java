package mx.gob.tabasco.cmaig.sigtic.sas.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Perfil implements Serializable {

   private String userNameShort;
   private String userNameFull;
   private String nombre;
   private String segundoNombre;
   private String apellidoPaterno;
   private String apellidoMaterno;
   private String rfc;
   private String curp;
   private String correo;
   private String cargo;
   private Long idInstitucion;
   private String nombreInstitucion;
   private Long idUnidadAdministrativa;
   private String nombreUnidadAdministrativa;
   private String numeroTelefono;
   private String numeroExtension;
   private String correoAlterno;
   private String celular;
}
