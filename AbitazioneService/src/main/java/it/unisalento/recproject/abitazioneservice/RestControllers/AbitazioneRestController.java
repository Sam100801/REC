package it.unisalento.recproject.abitazioneservice.RestControllers;

import it.unisalento.recproject.abitazioneservice.domain.Abitazione;
import it.unisalento.recproject.abitazioneservice.dto.AbitazioneDTO;
import it.unisalento.recproject.abitazioneservice.dto.AbitazioneListDTO;
import it.unisalento.recproject.abitazioneservice.dto.CapacitaCalcoloDTO;
import it.unisalento.recproject.abitazioneservice.exceptions.AbitazioneNotFoundException;
import it.unisalento.recproject.abitazioneservice.repositories.AbitazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/abitazione")
public class AbitazioneRestController {

    @Autowired
    AbitazioneRepository abitazioneRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public AbitazioneDTO get(@PathVariable String  id) throws AbitazioneNotFoundException {

        Optional<Abitazione> abitazione = abitazioneRepository.findById(id);

        if (abitazione.isEmpty()){
            throw new AbitazioneNotFoundException();
        }

        AbitazioneDTO abitazioneDTO = new AbitazioneDTO();

        abitazioneDTO.setId(abitazione.get().getId());
        abitazioneDTO.setProprietario(abitazione.get().getProprietario());
        abitazioneDTO.setViaENumeroCivico(abitazione.get().getViaENumeroCivico());
        abitazioneDTO.setKw(abitazione.get().getKw());
        abitazioneDTO.setOre(abitazione.get().getOre());
        abitazioneDTO.setEnergiaGreen(abitazione.get().getEnergiaGreen());
        abitazioneDTO.setCaratteristiche(abitazione.get().getCaratteristiche());
        abitazioneDTO.setEnergiaVenduta(abitazione.get().getEnergiaVenduta());

        return abitazioneDTO;
    }

    @RequestMapping(value = "/get/{proprietario}", method = RequestMethod.GET)
    public AbitazioneDTO getByProprietario(@PathVariable String proprietario){

        AbitazioneDTO abitazioneDTO = abitazioneRepository.findByProprietario(proprietario);

        if (abitazioneDTO == null){
            throw new RuntimeException("Abitazione non trovata con proprietario: " + proprietario);
        }

        AbitazioneDTO abitazione = new AbitazioneDTO();
        abitazione.setId(abitazioneDTO.getId());
        abitazione.setProprietario(abitazioneDTO.getProprietario());
        abitazione.setViaENumeroCivico(abitazioneDTO.getViaENumeroCivico());
        abitazione.setKw(abitazioneDTO.getKw());
        abitazione.setOre(abitazioneDTO.getOre());
        abitazione.setEnergiaGreen(abitazioneDTO.getEnergiaGreen());
        abitazione.setCaratteristiche(abitazioneDTO.getCaratteristiche());
        abitazione.setEnergiaVenduta(abitazioneDTO.getEnergiaVenduta());

        return abitazione;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public AbitazioneListDTO getAll() {
        ArrayList<AbitazioneDTO> list = new ArrayList<>();
        AbitazioneListDTO abitazioniList = new AbitazioneListDTO();

        List<Abitazione> abitazioni = abitazioneRepository.findAll();

        for (Abitazione abitazione : abitazioni){
            AbitazioneDTO abitazioneDTO = new AbitazioneDTO();
            abitazioneDTO.setId(abitazione.getId());
            abitazioneDTO.setProprietario(abitazione.getProprietario());
            abitazioneDTO.setViaENumeroCivico(abitazione.getViaENumeroCivico());
            abitazioneDTO.setKw(abitazione.getKw());
            abitazioneDTO.setOre(abitazione.getOre());
            abitazioneDTO.setEnergiaGreen(abitazione.getEnergiaGreen());
            abitazioneDTO.setCaratteristiche(abitazione.getCaratteristiche());
            abitazioneDTO.setEnergiaVenduta(abitazione.getEnergiaVenduta());
            list.add(abitazioneDTO);
        }

        // aggiorniamo la lista e la restituiamo.
        abitazioniList.setList(list);

        return abitazioniList;
    }

    // E' un update che per l'abitazione va a cambiare le ore di energia disponibile, kwh e se è cambiato anche il tipo di energia green.
    @RequestMapping(value = "/energia_disponibile", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public float put(@RequestBody AbitazioneDTO abitazioneDTO) {
        Optional<Abitazione> abitazioneOptional = abitazioneRepository.findById(abitazioneDTO.getId());

        if (abitazioneOptional.isEmpty()) {
            throw new RuntimeException("Abitazione non trovata con ID: " + abitazioneDTO.getId());
        }

        Abitazione abitazione = abitazioneOptional.get();
        abitazione.setOre(abitazioneDTO.getOre());
        abitazione.setKw(abitazioneDTO.getKw());

        if (abitazioneDTO.getEnergiaGreen() != null) {
            abitazione.setEnergiaGreen(abitazioneDTO.getEnergiaGreen());
        }

        abitazione = abitazioneRepository.save(abitazione);


        return abitazione.getKw();
    }


    @RequestMapping(value = "/crea_abitazione", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AbitazioneDTO post(@RequestBody AbitazioneDTO abitazioneDTO ) {
        Abitazione abitazione = new Abitazione();

        abitazione.setOre(abitazioneDTO.getOre());
        abitazione.setKw(abitazioneDTO.getKw());
        abitazione.setCaratteristiche(abitazioneDTO.getCaratteristiche());
        abitazione.setProprietario(abitazioneDTO.getProprietario());
        abitazione.setViaENumeroCivico(abitazioneDTO.getViaENumeroCivico());
        abitazione.setEnergiaGreen(abitazioneDTO.getEnergiaGreen());
        abitazione.setEnergiaVenduta(abitazioneDTO.getEnergiaVenduta());

        abitazione = abitazioneRepository.save(abitazione);
        abitazioneDTO.setId(abitazione.getId());
        return abitazioneDTO;
    }

    // Ritorna l'energia disponibile totale all'interno della REC.
    @RequestMapping(value = "/energia_disponibile_totale", method = RequestMethod.GET)
    public float getEnergiaDisponibile(){

        float kwhTotali = 0.0F;

        List<Abitazione> abitazioni = abitazioneRepository.findAll();

        for (Abitazione abitazione : abitazioni){
            kwhTotali = kwhTotali + (abitazione.getKw() * abitazione.getOre());
        }

        return kwhTotali;
    }

    //  VEDERE BENE CHE LOGICA USARE
    @RequestMapping(value = "/energia_venduta/{id}", method = RequestMethod.GET)
    public float getEnergiaVenduta(@PathVariable String id) throws AbitazioneNotFoundException{

        Optional<Abitazione> abitazione = abitazioneRepository.findById(id);

        if (abitazione.isEmpty()){
            throw new AbitazioneNotFoundException();
        }

        AbitazioneDTO abitazioneDTO = new AbitazioneDTO();
        abitazioneDTO.setId(abitazione.get().getId());
        abitazioneDTO.setEnergiaVenduta(abitazione.get().getEnergiaVenduta());

        return abitazioneDTO.getEnergiaVenduta();
    }



    @RequestMapping(value = "/energia_venduta_totale", method = RequestMethod.GET)
    public float getEnergiaVendutaTotale(){
        float energiaVendutaTotale = 0.0F;

        List<Abitazione> abitazioni = abitazioneRepository.findAll();

        for (Abitazione abitazione : abitazioni){
            energiaVendutaTotale = energiaVendutaTotale + abitazione.getEnergiaVenduta();
        }

        return energiaVendutaTotale;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public String  delete(@PathVariable String id) throws AbitazioneNotFoundException{

        Optional<Abitazione> abitazione = abitazioneRepository.findById(id);

        if (abitazione.isEmpty()){
            throw new AbitazioneNotFoundException();
        }

        abitazioneRepository.deleteById(id);
        return "L'utente con id " + id + " è stato eliminato";
    }

    @RequestMapping(value = "/searchByCpu/{cpu}", method = RequestMethod.GET)
    public AbitazioneListDTO searchByCpu(@PathVariable float cpu){
        AbitazioneListDTO abitazioniList = new AbitazioneListDTO();

        ArrayList<AbitazioneDTO> abitazioni = new ArrayList<>();
        abitazioniList.setList(abitazioni);

        List<Abitazione> list = abitazioneRepository.findByCaratteristicheCpu(cpu);

        for (Abitazione abitazione : list){
            CapacitaCalcoloDTO caratteristiche = abitazione.getCaratteristiche();
            if (caratteristiche != null && caratteristiche.getCpu() == cpu) {
                AbitazioneDTO abitazioneDTO = new AbitazioneDTO();
                abitazioneDTO.setId(abitazione.getId());
                abitazioneDTO.setProprietario(abitazione.getProprietario());
                abitazioneDTO.setViaENumeroCivico(abitazione.getViaENumeroCivico());
                abitazioneDTO.setKw(abitazione.getKw());
                abitazioneDTO.setOre(abitazione.getOre());
                abitazioneDTO.setEnergiaGreen(abitazione.getEnergiaGreen());
                abitazioneDTO.setCaratteristiche(abitazione.getCaratteristiche());
                abitazioneDTO.setEnergiaVenduta(abitazione.getEnergiaVenduta());
                abitazioni.add(abitazioneDTO);
            }
        }
        return abitazioniList;
    }

    @RequestMapping(value = "/searchBySistemaOperativo/{sistemaOperativo}", method = RequestMethod.GET)
    public AbitazioneListDTO searchBySistemaOperativo(@PathVariable String sistemaOperativo){
        AbitazioneListDTO abitazioniList = new AbitazioneListDTO();

        ArrayList<AbitazioneDTO> abitazioni = new ArrayList<>();
        abitazioniList.setList(abitazioni);

        List<Abitazione> list = abitazioneRepository.findByCaratteristicheSistemaOperativo(sistemaOperativo);

        for (Abitazione abitazione : list){
            CapacitaCalcoloDTO caratteristiche = abitazione.getCaratteristiche();
            if (caratteristiche != null && caratteristiche.getSistemaOperativo().equals(sistemaOperativo)) {
                AbitazioneDTO abitazioneDTO = new AbitazioneDTO();
                abitazioneDTO.setId(abitazione.getId());
                abitazioneDTO.setProprietario(abitazione.getProprietario());
                abitazioneDTO.setViaENumeroCivico(abitazione.getViaENumeroCivico());
                abitazioneDTO.setKw(abitazione.getKw());
                abitazioneDTO.setOre(abitazione.getOre());
                abitazioneDTO.setEnergiaGreen(abitazione.getEnergiaGreen());
                abitazioneDTO.setCaratteristiche(abitazione.getCaratteristiche());
                abitazioneDTO.setEnergiaVenduta(abitazione.getEnergiaVenduta());
                abitazioni.add(abitazioneDTO);
            }
        }
        return abitazioniList;
    }

    @RequestMapping(value = "/searchByRam/{ram}", method = RequestMethod.GET)
    public AbitazioneListDTO searchByRam(@PathVariable int ram){
        AbitazioneListDTO abitazioniList = new AbitazioneListDTO();

        ArrayList<AbitazioneDTO> abitazioni = new ArrayList<>();
        abitazioniList.setList(abitazioni);

        List<Abitazione> list = abitazioneRepository.findByCaratteristicheRam(ram);

        for (Abitazione abitazione : list){
            CapacitaCalcoloDTO caratteristiche = abitazione.getCaratteristiche(); // Ottieni le caratteristiche dall'abitazione
            if (caratteristiche != null && caratteristiche.getRam() == ram) { // Controlla se le caratteristiche non sono nulle e confronta la RAM
                AbitazioneDTO abitazioneDTO = new AbitazioneDTO();
                abitazioneDTO.setId(abitazione.getId());
                abitazioneDTO.setProprietario(abitazione.getProprietario());
                abitazioneDTO.setViaENumeroCivico(abitazione.getViaENumeroCivico());
                abitazioneDTO.setKw(abitazione.getKw());
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
