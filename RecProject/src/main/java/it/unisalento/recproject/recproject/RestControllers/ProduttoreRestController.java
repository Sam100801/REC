package it.unisalento.recproject.recproject.RestControllers;

import it.unisalento.recproject.recproject.domain.Produttore;
import it.unisalento.recproject.recproject.dto.*;
import it.unisalento.recproject.recproject.exceptions.ProduttoreNotFoundException;
import it.unisalento.recproject.recproject.repositories.ProduttoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produttore")
public class ProduttoreRestController {

    @Autowired
    ProduttoreRepository produttoreRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ProduttoreListDTO getAll(){

        ArrayList<ProduttoreDTO> list = new ArrayList<>();
        ProduttoreListDTO produttoreListDTO = new ProduttoreListDTO();
        produttoreListDTO.setList(list);

        List<Produttore> produttori = produttoreRepository.findAll();

        for (Produttore produttore : produttori){
            ProduttoreDTO produttoreDTO = new ProduttoreDTO();

            produttoreDTO.setNome(produttore.getNome());
            produttoreDTO.setCognome(produttore.getCognome());
            produttoreDTO.setId(produttore.getId());
            produttoreDTO.setUsername(produttore.getUsername());
            produttoreDTO.setEmail(produttore.getEmail());
            produttoreDTO.setPassword(produttore.getPassword());

            list.add(produttoreDTO);
        }
        return produttoreListDTO;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ProduttoreDTO getById(@PathVariable String id) throws ProduttoreNotFoundException {

        Optional<Produttore> produttore = produttoreRepository.findById(id);

        if (produttore.isEmpty()){
            throw new ProduttoreNotFoundException();
        }

        ProduttoreDTO produttoreDTO = new ProduttoreDTO();

        produttoreDTO.setUsername(produttore.get().getUsername());
        produttoreDTO.setEmail(produttore.get().getEmail());
        produttoreDTO.setId(produttore.get().getId());
        produttoreDTO.setNome(produttore.get().getNome());
        produttoreDTO.setCognome(produttore.get().getCognome());
        produttoreDTO.setPassword(produttore.get().getPassword());

        return produttoreDTO;
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String update(@RequestBody ProduttoreDTO produttoreDTO, @PathVariable String id) throws ProduttoreNotFoundException {

        Optional<Produttore> produttore = produttoreRepository.findById(id);

        if (produttore.isEmpty()){
            throw new ProduttoreNotFoundException();
        }

        produttore.get().setEmail(produttoreDTO.getEmail());
        produttore.get().setUsername(produttoreDTO.getUsername());

        produttoreRepository.save(produttore.get());
        return "L'utente con id " + id + " è stato aggiornato!";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable String id) throws ProduttoreNotFoundException {

        Optional<Produttore> produttore = produttoreRepository.findById(id);

        if (produttore.isEmpty()){
            throw new ProduttoreNotFoundException();
        }

        produttoreRepository.deleteById(id);

        return "Il produttore con id: " + id + " è stato eliminato!";
    }
}
