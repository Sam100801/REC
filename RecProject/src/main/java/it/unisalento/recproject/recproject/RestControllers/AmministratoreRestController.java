package it.unisalento.recproject.recproject.RestControllers;

import it.unisalento.recproject.recproject.domain.Amministratore;
import it.unisalento.recproject.recproject.domain.Reward;
import it.unisalento.recproject.recproject.domain.User;
import it.unisalento.recproject.recproject.dto.*;
import it.unisalento.recproject.recproject.exceptions.AmministratoreNotFoundException;
import it.unisalento.recproject.recproject.repositories.AmministratoreRepository;
import it.unisalento.recproject.recproject.repositories.RewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import static it.unisalento.recproject.recproject.configuration.SecurityConfig.passwordEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/amministratore")
public class AmministratoreRestController {

    @Autowired
    AmministratoreRepository amministratoreRepository;
    @Autowired
    RewardRepository rewardRepository;

    @RequestMapping(value = "/incasso/{id}", method = RequestMethod.GET)
    public float getIncasso(@PathVariable String id) throws AmministratoreNotFoundException {

        Optional<Amministratore> amministratore = amministratoreRepository.findById(id);

        if(amministratore.isEmpty()){
            throw new AmministratoreNotFoundException();
        }

        return amministratore.get().getIncasso();
    }

    @RequestMapping(value = "/reward", method = RequestMethod.GET)
    public RewardListDTO getAll(){
        ArrayList<RewardDTO> list = new ArrayList<>();
        RewardListDTO rewardList = new RewardListDTO();
        rewardList.setRewards(list);

        List<Reward> rewards = rewardRepository.findAll();

        for (Reward reward : rewards){
            // Se isAssigned == True allora sono i prodotti per l'amministratore altrimenti sono per l'utente
            if (reward.isAssigned() == true){
                RewardDTO rewardDTO = new RewardDTO();
                rewardDTO.setId(reward.getId());
                rewardDTO.setNome(reward.getNome());
                rewardDTO.setDescrizione(reward.getDescrizione());
                list.add(rewardDTO);
            }
        }

        return rewardList;
    }

    @RequestMapping(value = "/saldo/{id}", method = RequestMethod.GET)
    public float getSaldo(@PathVariable String id) throws AmministratoreNotFoundException {
        Optional<Amministratore> amministratore = amministratoreRepository.findById(id);

        if(amministratore.isEmpty()){
            throw new AmministratoreNotFoundException();
        }

        return amministratore.get().getSaldo();
    }

    @RequestMapping(value = "/performance_REC", method = RequestMethod.GET)
    public float getPerformance() {
        /**Implementare la logica per calcolare la performance
         **/
        float performance = 100;
        return performance;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public AmministratoreListDTO getAllAdmin(){
        ArrayList<AmministratoreDTO> list = new ArrayList<>();
        AmministratoreListDTO amministratoreListDTO = new AmministratoreListDTO();
        amministratoreListDTO.setList(list);

        List<Amministratore> amministratori = amministratoreRepository.findAll();

        for (Amministratore amministratore : amministratori){
            AmministratoreDTO amministratoreDTO = new AmministratoreDTO();

            amministratoreDTO.setNome(amministratore.getNome());
            amministratoreDTO.setCognome(amministratore.getCognome());
            amministratoreDTO.setId(amministratore.getId());
            amministratoreDTO.setUsername(amministratore.getUsername());
            amministratoreDTO.setEmail(amministratore.getEmail());
            amministratoreDTO.setPassword(amministratore.getPassword());

            list.add(amministratoreDTO);
        }
        return amministratoreListDTO;
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String update(@RequestBody AmministratoreDTO amministratoreDTO, @PathVariable String id) throws AmministratoreNotFoundException {
        Optional<Amministratore> amministratore = amministratoreRepository.findById(id);

        if (amministratore.isEmpty()){
            throw new AmministratoreNotFoundException();
        }

        amministratore.get().setEmail(amministratoreDTO.getEmail());
        amministratore.get().setUsername(amministratoreDTO.getUsername());

        amministratoreRepository.save(amministratore.get());

        return "L'amministratore con id: " + id + " è stato aggiornato!";
    }
}
