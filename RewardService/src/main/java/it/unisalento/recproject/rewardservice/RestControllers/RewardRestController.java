package it.unisalento.recproject.rewardservice.RestControllers;

import it.unisalento.recproject.rewardservice.domain.RewardAdmin;
import it.unisalento.recproject.rewardservice.dto.RewardAdminDTO;
import it.unisalento.recproject.rewardservice.dto.RewardAdminListDTO;
import it.unisalento.recproject.rewardservice.repositories.RewardRepository;
import it.unisalento.recproject.rewardservice.security.AuthorizationAPI;
import it.unisalento.recproject.rewardservice.security.JwtUtilities;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/reward")
public class RewardRestController {

    @Autowired
    RewardRepository rewardRepository;
    @Autowired
    JwtUtilities jwtUtilities;

    @RequestMapping(value = "/crea_reward", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@RequestBody RewardAdminDTO rewardDTO, HttpServletRequest request) {

        AuthorizationAPI authorizationAPI = new AuthorizationAPI();
        authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

        RewardAdmin reward = new RewardAdmin();
        reward.setImageUrl(rewardDTO.getImageUrl());
        reward.setNome(rewardDTO.getNome());
        reward.setDescrizione(rewardDTO.getDescrizione());

        reward = rewardRepository.save(reward);

        rewardDTO.setId(reward.getId());

        return ResponseEntity.ok("Reward creato con successo");
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> update(@RequestBody RewardAdminDTO rewardDTO, @PathVariable String id, HttpServletRequest request) {

        AuthorizationAPI authorizationAPI = new AuthorizationAPI();
        authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

        Optional<RewardAdmin> reward = rewardRepository.findById(id);

        if (reward.isEmpty()) {
            return ResponseEntity.badRequest().body("Reward non trovato");
        }

        reward.get().setImageUrl(rewardDTO.getImageUrl());
        reward.get().setNome(rewardDTO.getNome());
        reward.get().setDescrizione(rewardDTO.getDescrizione());

        rewardRepository.save(reward.get());

        return ResponseEntity.ok("Reward aggiornato con successo");
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable String id, HttpServletRequest request) {

        AuthorizationAPI authorizationAPI = new AuthorizationAPI();
        authorizationAPI.checkAuthorizationRole(request, jwtUtilities);

        Optional<RewardAdmin> reward = rewardRepository.findById(id);

        if (reward.isEmpty()) {
            return ResponseEntity.badRequest().body("Reward non trovato");
        }

        rewardRepository.delete(reward.get());

        return ResponseEntity.ok("Reward eliminato con successo");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public RewardAdminListDTO getAll() {

        ArrayList<RewardAdminDTO> list = new ArrayList<>();
        RewardAdminListDTO rewardList = new RewardAdminListDTO();

        List<RewardAdmin> rewards = rewardRepository.findAll();

        for (RewardAdmin reward : rewards) {
            RewardAdminDTO rewardDTO = new RewardAdminDTO();
            rewardDTO.setId(reward.getId());
            rewardDTO.setNome(reward.getNome());
            rewardDTO.setDescrizione(reward.getDescrizione());
            rewardDTO.setImageUrl(reward.getImageUrl());
            list.add(rewardDTO);
        }
        rewardList.setRewards(list);

        return rewardList;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public RewardAdminDTO getById(@PathVariable String id) {

        Optional<RewardAdmin> reward = rewardRepository.findById(id);

        if (reward.isEmpty()) {
            return null;
        }

        RewardAdminDTO rewardDTO = new RewardAdminDTO();

        rewardDTO.setId(reward.get().getId());
        rewardDTO.setNome(reward.get().getNome());
        rewardDTO.setDescrizione(reward.get().getDescrizione());
        rewardDTO.setImageUrl(reward.get().getImageUrl());

        return rewardDTO;
    }
}