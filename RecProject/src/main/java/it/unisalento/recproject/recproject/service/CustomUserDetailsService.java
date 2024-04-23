package it.unisalento.recproject.recproject.service;

import it.unisalento.recproject.recproject.domain.Amministratore;
import it.unisalento.recproject.recproject.domain.Produttore;
import it.unisalento.recproject.recproject.domain.User;
import it.unisalento.recproject.recproject.repositories.AmministratoreRepository;
import it.unisalento.recproject.recproject.repositories.ProduttoreRepository;
import it.unisalento.recproject.recproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//questa classe fornisce i dettagli di un certo utente
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AmministratoreRepository amministratoreRepository;

    @Autowired
    ProduttoreRepository produttoreRepository;

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

}