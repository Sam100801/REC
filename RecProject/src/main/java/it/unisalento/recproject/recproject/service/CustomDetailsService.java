package it.unisalento.recproject.recproject.service;

import it.unisalento.recproject.recproject.domain.Amministratore;
import it.unisalento.recproject.recproject.domain.Produttore;
import it.unisalento.recproject.recproject.domain.User;
import it.unisalento.recproject.recproject.dto.AmministratoreDTO;
import it.unisalento.recproject.recproject.dto.ProduttoreDTO;
import it.unisalento.recproject.recproject.dto.UserDTO;
import it.unisalento.recproject.recproject.repositories.AmministratoreRepository;
import it.unisalento.recproject.recproject.repositories.ProduttoreRepository;
import it.unisalento.recproject.recproject.repositories.UserRepository;
import it.unisalento.recproject.recproject.util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;

import static it.unisalento.recproject.recproject.configuration.SecurityConfig.passwordEncoder;

//questa classe fornisce i dettagli di un certo utente
@Service
public class CustomDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AmministratoreRepository amministratoreRepository;

    @Autowired
    ProduttoreRepository produttoreRepository;

    @Autowired
    OtpUtil otpUtil;

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {

        // Si va a cercare per email o username l'utente o l'amministratore o il produttore.
        User user = userRepository.findByEmail(emailOrUsername);

        Amministratore amministratore = amministratoreRepository.findByEmail(emailOrUsername);

        Produttore produttore = produttoreRepository.findByEmail(emailOrUsername);

        UserDetails userDetails;

        //Si gestisce la logica per il login a seconda se è utente aministratore o produttore

        if (user != null) {
            userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .roles("USER")
                    .build();
            return userDetails;
        }
        else if (amministratore != null){
            userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(amministratore.getEmail())
                    .password(amministratore.getPassword())
                    .roles("ADMIN")
                    .build();
            return userDetails;
        }
        else if (produttore != null){
            userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(produttore.getEmail())
                    .password(produttore.getPassword())
                    .roles("PRODUTTORE")
                    .build();
            return userDetails;
        }
        else return null;

    }

    public String verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email);
        Produttore produttore = produttoreRepository.findByEmail(email);

        if (user == null && produttore == null){
            throw new RuntimeException("User not found with this email: " + email);
        }
        if(user != null) {
            if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                    LocalDateTime.now()).getSeconds() < (1 * 60)) {
                user.setActive(true);
                userRepository.save(user);
                return "OTP verified you can login";
            }
        } else if (produttore != null) {
            if (produttore.getOtp().equals(otp) && Duration.between(produttore.getOtpGeneratedTime(),
                    LocalDateTime.now()).getSeconds() < (1 * 60)) {
                produttore.setActive(true);
                produttoreRepository.save(produttore);
                return "OTP verified you can login";
            }
        }
        return "Please regenerate otp and try again";
    }

    public String regenerateOtp(String email) {
        User user = userRepository.findByEmail(email);
        Produttore produttore = produttoreRepository.findByEmail(email);

        if (user == null && produttore == null){
            throw new RuntimeException("User not found with this email: " + email);
        }

        String otp = otpUtil.generateOtp();
        String url = "http://host.docker.internal:8084/api/email/opt";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", email);
        map.add("opt", otp);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Email contenente otp mandata con successo.");
        } else {
            System.out.println("Errore durante l'invio della mail contenente l'otp: " + response.getStatusCodeValue());
        }

        if (user != null){
            user.setOtp(otp);
            user.setOtpGeneratedTime(LocalDateTime.now());
            userRepository.save(user);
        }
        else if(produttore != null){
            produttore.setOtp(otp);
            produttore.setOtpGeneratedTime(LocalDateTime.now());
            produttoreRepository.save(produttore);
        }
        return "Email sent... please verify account within 1 minute";
    }

    public ResponseEntity<?> registrazioneUser(UserDTO userDTO){
        if (!amministratoreRepository.existsByEmail(userDTO.getEmail()) &&
                !produttoreRepository.existsByEmail(userDTO.getEmail()) &&
                !userRepository.existsByEmail(userDTO.getEmail()) ) {


            String otp = otpUtil.generateOtp();
            String url = "http://host.docker.internal:8084/api/email/opt";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("email", userDTO.getEmail());
            map.add("opt", otp);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Email contenente otp mandata con successo.");
            } else {
                System.out.println("Errore durante l'invio della mail contenente l'otp: " + response.getStatusCodeValue());
            }


            User user = new User();
            user.setNome(userDTO.getNome());
            user.setCognome(userDTO.getCognome());
            user.setEmail(userDTO.getEmail());
            user.setIban(userDTO.getIban());
            user.setOtp(otp);
            user.setActive(false);
            user.setOtpGeneratedTime(LocalDateTime.now());
            user.setPassword(passwordEncoder().encode(userDTO.getPassword()));

            user = userRepository.save(user); //faccio questa assegnazione per avere l'id dell'utente

            userDTO.setId(user.getId()); //assegno l'id dell'utente al DTO
            userDTO.setPassword(null);

            url = "http://host.docker.internal:8088/api/users/registrazione";
            restTemplate = new RestTemplate();
            headers = new HttpHeaders();
            HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO, headers);
            ResponseEntity<AmministratoreDTO> response1 = restTemplate.postForEntity(url, entity, AmministratoreDTO.class);

            return ResponseEntity.ok(userDTO);
        }
        else return new ResponseEntity<>("La registrazione non è andata a buon fine!", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> registrazioneAmministratore(AmministratoreDTO amministratoreDTO){

        if (!amministratoreRepository.existsByEmail(amministratoreDTO.getEmail()) &&
                !produttoreRepository.existsByEmail(amministratoreDTO.getEmail()) &&
                !userRepository.existsByEmail(amministratoreDTO.getEmail()) ) {
            Amministratore amministratore = new Amministratore();

            amministratore.setNome(amministratoreDTO.getNome());
            amministratore.setCognome(amministratoreDTO.getCognome());
            amministratore.setEmail(amministratoreDTO.getEmail());
            amministratore.setIban(amministratoreDTO.getIban());
            amministratore.setPassword(passwordEncoder().encode(amministratoreDTO.getPassword()));

            amministratore = amministratoreRepository.save(amministratore);

            amministratoreDTO.setId(amministratore.getId());
            amministratoreDTO.setPassword(null);

            String url = "http://host.docker.internal:8089/api/amministratore/registrazione";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<AmministratoreDTO> entity = new HttpEntity<>(amministratoreDTO, headers);
            ResponseEntity<AmministratoreDTO> response = restTemplate.postForEntity(url, entity, AmministratoreDTO.class);



            return ResponseEntity.ok(amministratoreDTO);
        }
        else return new ResponseEntity<>("La registrazione non è andata a buon fine!", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> registrazioneProduttore(ProduttoreDTO produttoreDTO){
        if (!amministratoreRepository.existsByEmail(produttoreDTO.getEmail()) &&
                !produttoreRepository.existsByEmail(produttoreDTO.getEmail()) &&
                !userRepository.existsByEmail(produttoreDTO.getEmail()) ) {

            String otp = otpUtil.generateOtp();
            String url = "http://host.docker.internal:8084/api/email/opt";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("email", produttoreDTO.getEmail());
            map.add("opt", otp);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Email contenente otp mandata con successo.");
            } else {
                System.out.println("Errore durante l'invio della mail contenente l'otp: " + response.getStatusCodeValue());
            }

            Produttore produttore = new Produttore();

            produttore.setNome(produttoreDTO.getNome());
            produttore.setCognome(produttoreDTO.getCognome());
            produttore.setEmail(produttoreDTO.getEmail());
            produttore.setIban(produttoreDTO.getIban());
            produttore.setOtp(otp);
            produttore.setActive(false);
            produttore.setOtpGeneratedTime(LocalDateTime.now());
            produttore.setPassword(passwordEncoder().encode(produttoreDTO.getPassword()));


            produttore = produttoreRepository.save(produttore);

            produttoreDTO.setId(produttore.getId());
            produttoreDTO.setPassword(null);

            url = "http://host.docker.internal:8086/api/produttore/registrazione";
            restTemplate = new RestTemplate();
            headers = new HttpHeaders();
            HttpEntity<ProduttoreDTO> entity = new HttpEntity<>(produttoreDTO, headers);
            ResponseEntity<ProduttoreDTO> response1 = restTemplate.postForEntity(url, entity, ProduttoreDTO.class);


            return ResponseEntity.ok(produttoreDTO);
        }
        else return new ResponseEntity<>("La registrazione non è andata a buon fine!", HttpStatus.BAD_REQUEST);
    }
}