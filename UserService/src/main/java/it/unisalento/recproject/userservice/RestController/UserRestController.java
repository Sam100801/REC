package it.unisalento.recproject.userservice.RestController;



import it.unisalento.recproject.userservice.domain.User;
import it.unisalento.recproject.userservice.dto.UserDTO;
import it.unisalento.recproject.userservice.dto.UsersListDTO;
import it.unisalento.recproject.userservice.exceptions.CreditiInsufficientiException;
import it.unisalento.recproject.userservice.exceptions.UserNotFoundException;
import it.unisalento.recproject.userservice.repositories.UserRepository;
import it.unisalento.recproject.userservice.security.AuthorizationAPI;
import it.unisalento.recproject.userservice.security.JwtUtilities;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController //response body ritorna un body, ovvero un tsto, una risorsa. Esso rappresenta un unione
@RequestMapping("/api/users") //indirizzo di base per le richieste
public class UserRestController { //gestisce le richieste REST relative agli utenti, come ad esempio POST, GET, PUT, DELETE

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtilities jwtUtilities;

    @RequestMapping(value = "/registrazione", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrazione(@RequestBody UserDTO userDTO){

            User user = new User();
            user.setNome(userDTO.getNome());
            user.setCognome(userDTO.getCognome());
            user.setEmail(userDTO.getEmail());
            user.setIban(userDTO.getIban());
            user.setCrediti(0);

            user = userRepository.save(user);

            userDTO.setId(user.getId());

            return ResponseEntity.ok(userDTO);
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
            userDTO.setIban(user.getIban());
            userDTO.setCrediti(user.getCrediti());
            list.add(userDTO);
        }

        return usersList;
    }



    // Metterla anche in admin poichè anche lui può eliminare un utente
    @RequestMapping(value = "delete/{email}", method = RequestMethod.DELETE)
    public String delete(@PathVariable String email, HttpServletRequest request) {

        AuthorizationAPI authorizationAPI = new AuthorizationAPI();
        authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()){
            return "L'utente con id " + email + " non esiste!";
        }

        userRepository.deleteByEmail(email);

        return "L'utente con id " + email + " è stato eliminato";
    }

    //restituisce i crediti di un utente
    @RequestMapping(value = "/crediti/{email}", method = RequestMethod.GET)
    public float getCrediti(@PathVariable String email, HttpServletRequest request) throws UserNotFoundException {



        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()){
            throw new UserNotFoundException(email);
        }

        return user.get().getCrediti();
    }

    // Cambia i crediti di un utente
    @RequestMapping(value = "/addCrediti/{email}", method = RequestMethod.PUT)
    public void putCrediti(@PathVariable String email, @RequestParam float crediti) throws UserNotFoundException {


        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()){
            throw new UserNotFoundException(email);
        }

        user.get().setCrediti((user.get().getCrediti() + crediti));
        userRepository.save(user.get());
    }

    @RequestMapping(value = "/togliCrediti/{email}", method = RequestMethod.PUT)
    public void togliCrediti(@PathVariable String email, @RequestParam float crediti) throws UserNotFoundException, CreditiInsufficientiException {


        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()){
            throw new UserNotFoundException(email);
        }

        if ((user.get().getCrediti() - crediti) < 0){
            throw new CreditiInsufficientiException();
        }

        user.get().setCrediti((user.get().getCrediti() - crediti));
        userRepository.save(user.get());
    }

    @RequestMapping(value = "/searchUser/{email}", method = RequestMethod.GET)
    public UserDTO search(@PathVariable String email){

        Optional<User> user = userRepository.findByEmail(email);
        UserDTO userDTO = new UserDTO();

        if (user.isPresent()){
            userDTO.setId(user.get().getId());
            userDTO.setNome(user.get().getNome());
            userDTO.setCognome(user.get().getCognome());
            userDTO.setEmail(user.get().getEmail());
            userDTO.setIban(user.get().getIban());
            userDTO.setCrediti(user.get().getCrediti());
        }
        return userDTO;
    }
}
