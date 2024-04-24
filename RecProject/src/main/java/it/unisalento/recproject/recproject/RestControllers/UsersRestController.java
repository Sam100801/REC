package it.unisalento.recproject.recproject.RestControllers;

import it.unisalento.recproject.recproject.di.IRenewableSource;
import it.unisalento.recproject.recproject.domain.Abitazione;
import it.unisalento.recproject.recproject.domain.User;
import it.unisalento.recproject.recproject.dto.*;
import it.unisalento.recproject.recproject.exceptions.UserNotFoundException;
import it.unisalento.recproject.recproject.repositories.AbitazioneRepository;
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
    @Autowired
    AbitazioneRepository abitazioneRepository;

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

    @RequestMapping(value = "/searchByCpu", method = RequestMethod.GET)
    public AbitazioniListDTO searchByCpu(@RequestParam float cpu){
        AbitazioniListDTO abitazioniList = new AbitazioniListDTO();

        ArrayList<AbitazioneDTO> abitazioni = new ArrayList<>();
        abitazioniList.setList(abitazioni);

        List<Abitazione> list = abitazioneRepository.findByCaratteristicheCpu(cpu);

        for (Abitazione abitazione : list){
            CapacitaCalcoloDTO caratteristiche = abitazione.getCaratteristiche();
            if (caratteristiche != null && caratteristiche.getCpu() == cpu) {
                AbitazioneDTO abitazioneDTO = new AbitazioneDTO();
                abitazioneDTO.setId(abitazione.getId());
                abitazioneDTO.setProprietario(abitazione.getProprietario());
                abitazioneDTO.setKwh(abitazione.getKwh());
                abitazioneDTO.setOre(abitazione.getOre());
                abitazioneDTO.setEnergiaGreen(abitazione.getEnergiaGreen());
                abitazioneDTO.setCaratteristiche(abitazione.getCaratteristiche());
                abitazioneDTO.setEnergiaVenduta(abitazione.getEnergiaVenduta());
                abitazioni.add(abitazioneDTO);
            }
        }
        return abitazioniList;
    }

    @RequestMapping(value = "/searchBySistemaOperativo", method = RequestMethod.GET)
    public AbitazioniListDTO searchBySistemaOperativo(@RequestParam String sistemaOperativo){
        AbitazioniListDTO abitazioniList = new AbitazioniListDTO();

        ArrayList<AbitazioneDTO> abitazioni = new ArrayList<>();
        abitazioniList.setList(abitazioni);

        List<Abitazione> list = abitazioneRepository.findByCaratteristicheSistemaOperativo(sistemaOperativo);

        for (Abitazione abitazione : list){
            CapacitaCalcoloDTO caratteristiche = abitazione.getCaratteristiche();
            if (caratteristiche != null && caratteristiche.getSistemaOperativo().equals(sistemaOperativo)) {
                AbitazioneDTO abitazioneDTO = new AbitazioneDTO();
                abitazioneDTO.setId(abitazione.getId());
                abitazioneDTO.setProprietario(abitazione.getProprietario());
                abitazioneDTO.setKwh(abitazione.getKwh());
                abitazioneDTO.setOre(abitazione.getOre());
                abitazioneDTO.setEnergiaGreen(abitazione.getEnergiaGreen());
                abitazioneDTO.setCaratteristiche(abitazione.getCaratteristiche());
                abitazioneDTO.setEnergiaVenduta(abitazione.getEnergiaVenduta());
                abitazioni.add(abitazioneDTO);
            }
        }
        return abitazioniList;
    }

    @RequestMapping(value = "/searchByRam", method = RequestMethod.GET)
    public AbitazioniListDTO searchByRam(@RequestParam int ram){
        AbitazioniListDTO abitazioniList = new AbitazioniListDTO();

        ArrayList<AbitazioneDTO> abitazioni = new ArrayList<>();
        abitazioniList.setList(abitazioni);

        List<Abitazione> list = abitazioneRepository.findByCaratteristicheRam(ram);

        for (Abitazione abitazione : list){
            CapacitaCalcoloDTO caratteristiche = abitazione.getCaratteristiche(); // Ottieni le caratteristiche dall'abitazione
            if (caratteristiche != null && caratteristiche.getRam() == ram) { // Controlla se le caratteristiche non sono nulle e confronta la RAM
                AbitazioneDTO abitazioneDTO = new AbitazioneDTO();
                abitazioneDTO.setId(abitazione.getId());
                abitazioneDTO.setProprietario(abitazione.getProprietario());
                abitazioneDTO.setKwh(abitazione.getKwh());
                abitazioneDTO.setOre(abitazione.getOre());
                abitazioneDTO.setEnergiaGreen(abitazione.getEnergiaGreen());
                abitazioneDTO.setCaratteristiche(caratteristiche); // Imposta le caratteristiche dell'abitazione
                abitazioneDTO.setEnergiaVenduta(abitazione.getEnergiaVenduta());
                abitazioni.add(abitazioneDTO);
            }
        }
        return abitazioniList;
    }

}