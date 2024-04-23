package it.unisalento.recproject.recproject.RestControllers;

import it.unisalento.recproject.recproject.domain.Amministratore;
import it.unisalento.recproject.recproject.domain.Produttore;
import it.unisalento.recproject.recproject.domain.User;
import it.unisalento.recproject.recproject.dto.AmministratoreDTO;
import it.unisalento.recproject.recproject.dto.ProduttoreDTO;
import it.unisalento.recproject.recproject.dto.UserDTO;
import it.unisalento.recproject.recproject.repositories.AmministratoreRepository;
import it.unisalento.recproject.recproject.repositories.ProduttoreRepository;
import it.unisalento.recproject.recproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static it.unisalento.recproject.recproject.configuration.SecurityConfig.passwordEncoder;


@RestController //response body ritorna un body, ovvero un tsto, una risorsa. Esso rappresenta un unione
@RequestMapping("/api/registrazione") //indirizzo di base per le richieste
public class RegistrationController { //gestisce le richieste REST relative agli utenti, come ad esempio POST, GET, PUT, DELETE

    @Autowired
    UserRepository userRepository;

    @Autowired
    AmministratoreRepository amministratoreRepository;

    @Autowired
    ProduttoreRepository produttoreRepository;

    @RequestMapping(value = "/user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrazione(@RequestBody UserDTO userDTO){
        if (!amministratoreRepository.existsByEmail(userDTO.getEmail()) &&
                !produttoreRepository.existsByEmail(userDTO.getEmail()) &&
                !userRepository.existsByEmail(userDTO.getEmail()) ) {

            User user = new User();
            user.setNome(userDTO.getNome());
            user.setCognome(userDTO.getCognome());
            user.setEmail(userDTO.getEmail());
            user.setUsername(userDTO.getUsername());
            user.setPassword(passwordEncoder().encode(userDTO.getPassword()));

            user = userRepository.save(user); //faccio questa assegnazione per avere l'id dell'utente

            userDTO.setId(user.getId()); //assegno l'id dell'utente al DTO
            userDTO.setPassword(null);
            return ResponseEntity.ok(userDTO);
        }
        else return new ResponseEntity<>("La registrazione non è andata a buon fine!", HttpStatus.BAD_REQUEST);
    }

     @RequestMapping(value = "/amministratore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrazione(@RequestBody AmministratoreDTO amministratoreDTO){

        if (!amministratoreRepository.existsByEmail(amministratoreDTO.getEmail()) &&
                !produttoreRepository.existsByEmail(amministratoreDTO.getEmail()) &&
                !userRepository.existsByEmail(amministratoreDTO.getEmail()) ) {
            Amministratore amministratore = new Amministratore();

            amministratore.setNome(amministratoreDTO.getNome());
            amministratore.setCognome(amministratoreDTO.getCognome());
            amministratore.setEmail(amministratoreDTO.getEmail());
            amministratore.setPassword(passwordEncoder().encode(amministratoreDTO.getPassword()));
            amministratore.setSaldo(0);
            amministratore.setIncasso(0);

            amministratore = amministratoreRepository.save(amministratore);

            amministratoreDTO.setId(amministratore.getId());
            amministratoreDTO.setPassword(null);
            return ResponseEntity.ok(amministratoreDTO);
        }
        else return new ResponseEntity<>("La registrazione non è andata a buon fine!", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/produttore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrazione(@RequestBody ProduttoreDTO produttoreDTO){
        if (!amministratoreRepository.existsByEmail(produttoreDTO.getEmail()) &&
                !produttoreRepository.existsByEmail(produttoreDTO.getEmail()) &&
                !userRepository.existsByEmail(produttoreDTO.getEmail()) ) {
            Produttore produttore = new Produttore();

            produttore.setNome(produttoreDTO.getNome());
            produttore.setCognome(produttoreDTO.getCognome());
            produttore.setEmail(produttoreDTO.getEmail());
            produttore.setUsername(produttoreDTO.getUsername());
            produttore.setPassword(passwordEncoder().encode(produttoreDTO.getPassword()));


            produttore = produttoreRepository.save(produttore);

            produttoreDTO.setId(produttore.getId());
            produttoreDTO.setPassword(null);
            return ResponseEntity.ok(produttoreDTO);
        }
        else return new ResponseEntity<>("La registrazione non è andata a buon fine!", HttpStatus.BAD_REQUEST);
    }
}