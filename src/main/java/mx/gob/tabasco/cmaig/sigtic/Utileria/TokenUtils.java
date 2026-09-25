package mx.gob.tabasco.cmaig.sigtic.Utileria;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils implements Serializable {

    //cgmaig.t4b4sc0.gob.mx_2024 codificado base 64
    private final static String ACCESS_TOKEN_SECRET = "Y2dtYWlnLnQ0YjRzYzAuZ29iLm14XzIwMjQ=";

    /*Crea token valido por 5 días*/
    private final static Long ACCESS_TOKEN_VALIDITY_SECONS = 432_000L;

    /*Genera ACCESS_TOKEN_SECRET*/
    public static String encode64(String word){

        return Base64.getEncoder().encodeToString(word.getBytes());
    }

    public static String createToken(String correo){
        long expirationTime = ACCESS_TOKEN_VALIDITY_SECONS* 1_000;
        //long expirationTime = 240000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        Map<String, Object> extra = new HashMap<>();
        extra.put("correo",correo);

        return Jwts.builder()
                .setSubject(correo)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();
    }

    public static String getCorreoToken(String token){
        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String correo = claims.getSubject();

            return correo;
        }catch (ExpiredJwtException ex){
            return "Expired";
        }
        catch (JwtException e){
            return null;
        }
    }

    public static String createTokenCr(Long idCartaResponsiva){
        long expirationTime = ACCESS_TOKEN_VALIDITY_SECONS* 1_000;
        //long expirationTime = 240000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);
        System.out.println(expirationDate);

        Map<String, Object> extra = new HashMap<>();
        extra.put("idCr",String.valueOf(idCartaResponsiva));

        return Jwts.builder()
                .setSubject(String.valueOf(idCartaResponsiva))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();
    }

    public static String getTokenCr(String token){
        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String idCr = claims.getSubject();

            return idCr;
        }catch (ExpiredJwtException ex){
            return "Expired";
        }
        catch (JwtException e){
            return null;
        }
    }

    public static String createTokenRecuperar(String dato, String usuario, Integer opcion) {
        //15 minutos
        long expirationTime = 900000;
        //long expirationTime = 240000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);
        System.out.println(expirationDate);

        Map<String, Object> extra = new HashMap<>();
        if (opcion==1 || opcion==2)
            extra.put("correo", dato);
        else
            extra.put("celular", dato);
        extra.put("usuario", usuario);
        extra.put("opcion", opcion);

        return Jwts.builder()
                .setSubject(usuario)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();
    }

}
