package it.unisalento.recproject.recproject.RestControllers;

import it.unisalento.recproject.recproject.di.IRenewableSource;
import it.unisalento.recproject.recproject.domain.User;
import it.unisalento.recproject.recproject.dto.*;
import it.unisalento.recproject.recproject.exceptions.UserNotFoundException;
import it.unisalento.recproject.recproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController //response body ritorna un body, ovvero un tsto, una risorsa. Esso rappresenta un unione
@RequestMapping("/api/users") //indirizzo di base per le richieste
public class UsersRestController { //gestisce le richieste REST relative agli utenti, come ad esempio POST, GET, PUT, DELETE

    @Autowired //riesce a capire a quale classe si riferisce grazie al nome
    IRenewableSource solarPanel;
    @Autowired //inietta la dipendenza all'interno di Rest Controller
    IRenewableSource windTurbine;
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value="/{id}", method = RequestMethod.GET) //indirizzo di base per le richieste
    public UserDTO getUserById(@PathVariable String id) throws UserNotFoundException {

        solarPanel.initialize(); //inizializza il pannello solare
        windTurbine.initialize(); //inizializza la turbina eolica

        Optional<User> user = userRepository.findById(id); //cerca l'utente con l'id specificato

        if (user.isEmpty()){
            throw new UserNotFoundException();
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.get().getId());
        userDTO.setNome(user.get().getNome());
        userDTO.setCognome(user.get().getCognome());
        userDTO.setEmail(user.get().getEmail());
        userDTO.setCrediti(user.get().getCrediti());
        userDTO.setUsername(user.get().getUsername());
        userDTO.setPassword(user.get().getPassword());

        return userDTO;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public UsersListDTO getAll(){


        ArrayList<UserDTO> list = new ArrayList<>();
        UsersListDTO usersList = new UsersListDTO();
        usersList.setList(list);

        List<User> users = userRepository.findAll();

        for (User user : users){
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setNome(user.getNome());
            userDTO.setCognome(user.getCognome());
            userDTO.setEmail(user.getEmail());
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(user.getPassword());
            list.add(userDTO);
        }

        return usersList;
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String update(@RequestBody UserDTO userDTO, @PathVariable String id) throws UserNotFoundException {

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()){
            throw new UserNotFoundException();
        }

        user.get().setEmail(userDTO.getEmail());

        userRepository.save(user.get());
        return "L'utente con id " + id + " è stato aggiornato!";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable String id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()){
            return "L'utente con id " + id + " non esiste!";
        }

        userRepository.deleteById(id);

        return "L'utente con id " + id + " è stato eliminato";
    }

    //restituisce i crediti di un utente
    @RequestMapping(value = "/crediti/{id}", method = RequestMethod.GET)
    public float getCrediti(@PathVariable String id) throws UserNotFoundException {

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()){
            throw new UserNotFoundException();
        }

        return user.get().getCrediti();
    }

    @RequestMapping(value = "/acquisto_reward", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String acquistoReward(){
        // Fare tutta la logica di scalare crediti, diminuire quantità del reward ecc.. (Forse bisogna fare un carrello di reward)
        return "prodotto acquistato";
    }

    @RequestMapping(value = "/pagamento", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String pagamentoServizio(@PathVariable float pagamento){
        return "pagamento effettuato";
    }


    @RequestMapping(value = "/searchUser", method = RequestMethod.GET)
    public UserDTO search(@RequestParam String email){

        User user = userRepository.findByEmail(email);
        UserDTO userDTO = new UserDTO();

        if (user != null){
            userDTO.setId(user.getId());
            userDTO.setNome(user.getNome());
            userDTO.setCognome(user.getCognome());
            userDTO.setEmail(user.getEmail());
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(user.getPassword());
        }
        return userDTO;
    }
}