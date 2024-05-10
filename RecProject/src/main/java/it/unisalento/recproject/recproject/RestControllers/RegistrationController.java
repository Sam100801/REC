package it.unisalento.recproject.recproject.RestControllers;

import it.unisalento.recproject.recproject.domain.Produttore;
import it.unisalento.recproject.recproject.domain.User;
import it.unisalento.recproject.recproject.dto.AmministratoreDTO;
import it.unisalento.recproject.recproject.dto.ProduttoreDTO;
import it.unisalento.recproject.recproject.dto.UserDTO;
import it.unisalento.recproject.recproject.dto.VerificaDTO;
import it.unisalento.recproject.recproject.repositories.AmministratoreRepository;
import it.unisalento.recproject.recproject.repositories.ProduttoreRepository;
import it.unisalento.recproject.recproject.repositories.UserRepository;
import it.unisalento.recproject.recproject.service.CustomDetailsService;
import it.unisalento.recproject.recproject.util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController //response body ritorna un body, ovvero un tsto, una risorsa. Esso rappresenta un unione
@RequestMapping("/api/registrazione") //indirizzo di base per le richieste
public class RegistrationController { //gestisce le richieste REST relative agli utenti, come ad esempio POST, GET, PUT, DELETE

    @Autowired
    UserRepository userRepository;

    @Autowired
    AmministratoreRepository amministratoreRepository;

    @Autowired
    ProduttoreRepository produttoreRepository;

    @Autowired
    CustomDetailsService customService;

    @Autowired
    OtpUtil otpUtil;

    @RequestMapping(value = "/user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrazione(@RequestBody UserDTO userDTO){
        return customService.registrazioneUser(userDTO);
    }

     @RequestMapping(value = "/amministratore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrazione(@RequestBody AmministratoreDTO amministratoreDTO){
        return customService.registrazioneAmministratore(amministratoreDTO);
    }

    @RequestMapping(value = "/produttore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrazione(@RequestBody ProduttoreDTO produttoreDTO){
        return customService.registrazioneProduttore(produttoreDTO);
    }

    @RequestMapping(value = "/verify-account", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> verifyAccount(@RequestBody VerificaDTO verificaDTO) {
        return new ResponseEntity<>(customService.verifyAccount(verificaDTO.getEmail(), verificaDTO.getOtp()), HttpStatus.OK);
    }
    @RequestMapping(value = "/regenerate-otp", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> regenerateOtp(@RequestBody VerificaDTO verificaDTO) {
        return new ResponseEntity<>(customService.regenerateOtp(verificaDTO.getEmail()), HttpStatus.OK);
    }

    @RequestMapping(value = "/is_active", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean isActive(@RequestBody VerificaDTO verificaDTO){
        User user = userRepository.findByEmail(verificaDTO.getEmail());
        Produttore produttore = produttoreRepository.findByEmail(verificaDTO.getEmail());

        if (user != null){
             return user.isActive();
        }
        else if (produttore != null){
            return produttore.isActive();
        }
        return false;
    }
}