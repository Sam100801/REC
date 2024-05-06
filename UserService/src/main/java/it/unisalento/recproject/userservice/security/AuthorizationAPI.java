package it.unisalento.recproject.userservice.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

public class AuthorizationAPI {

    public void checkAuthorizationRole(HttpServletRequest request, JwtUtilities jwtUtilities) {
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token JWT mancante");
        }

        jwtToken = jwtToken.substring(7);

       try {
           Date expirationDate = jwtUtilities.extractExpiration(jwtToken);
       }catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token JWT scaduto");
       }

        String role = jwtUtilities.extractClaim(jwtToken, claims -> claims.get("ruolo", String.class));

        if (!"USER".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo l'admin può usufruire di questa funzionalità");
        }
    }
}
