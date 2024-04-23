package it.unisalento.recproject.recproject.RestControllers;

import it.unisalento.recproject.recproject.domain.Amministratore;
import it.unisalento.recproject.recproject.domain.Produttore;
import it.unisalento.recproject.recproject.domain.User;
import it.unisalento.recproject.recproject.dto.AuthenticationResponseDTO;
import it.unisalento.recproject.recproject.dto.LoginDTO;
import it.unisalento.recproject.recproject.repositories.AmministratoreRepository;
import it.unisalento.recproject.recproject.repositories.ProduttoreRepository;
import it.unisalento.recproject.recproject.repositories.UserRepository;
import it.unisalento.recproject.recproject.security.JwtUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtilities jwtUtilities;

    @Autowired
    private AmministratoreRepository amministratoreRepository;

    @Autowired
    private ProduttoreRepository produttoreRepository;

    @RequestMapping(value="/", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationTokenUser(@RequestBody LoginDTO loginDTO) {
        Authentication authentication;
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()
                    )
            );

            User user = userRepository.findByEmail(authentication.getName());
            Amministratore amministratore = amministratoreRepository.findByEmail(authentication.getName());
            Produttore produttore = produttoreRepository.findByEmail(authentication.getName());

            if (user == null && amministratore == null && produttore == null) {
                throw new UsernameNotFoundException(loginDTO.getEmail());

            }

            String role = (user != null) ? "USER" : (amministratore != null) ? "ADMIN" : "PRODUTTORE";

            SecurityContextHolder.getContext().setAuthentication(authentication);

            final String jwt = jwtUtilities.generateToken(loginDTO.getEmail(), role);

            return ResponseEntity.ok(new AuthenticationResponseDTO(jwt));
        }
}
