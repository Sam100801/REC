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
        abitazioneDTO.setKwh(abitazione.get().getKwh());
        abitazioneDTO.setOre(abitazione.get().getOre());
        abitazioneDTO.setEnergiaGreen(abitazione.get().getEnergiaGreen());
        abitazioneDTO.setCaratteristiche(abitazione.get().getCaratteristiche());
        abitazioneDTO.setEnergiaVenduta(abitazione.get().getEnergiaVenduta());

        return abitazioneDTO;
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
            abitazioneDTO.setKwh(abitazione.getKwh());
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
    public float put(@RequestBody AbitazioneDTO abitazioneDTO ) {
        Abitazione abitazione = new Abitazione();
        abitazione.setId(abitazioneDTO.getId());
        abitazione.setOre(abitazioneDTO.getOre());
        abitazione.setKwh(abitazioneDTO.getKwh());

        if (!abitazione.getEnergiaGreen().isEmpty())
            abitazione.setEnergiaGreen(abitazioneDTO.getEnergiaGreen());

        abitazione = abitazioneRepository.save(abitazione);

        return abitazioneDTO.getKwh();
    }

    @RequestMapping(value = "/crea_abitazione", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AbitazioneDTO post(@RequestBody AbitazioneDTO abitazioneDTO ) {
        Abitazione abitazione = new Abitazione();

        abitazione.setOre(abitazioneDTO.getOre());
        abitazione.setKwh(abitazioneDTO.getKwh());
        abitazione.setCaratteristiche(abitazioneDTO.getCaratteristiche());
        abitazione.setProprietario(abitazioneDTO.getProprietario());
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
            kwhTotali = kwhTotali + abitazione.getKwh();
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

    @RequestMapping(value = "/energia_disponibile_totale", method = RequestMethod.GET)
    public float getEnergiaDisponibileTotale(){
        float energiaDisponibileTotale = 0.0F;

        List<Abitazione> abitazioni = abitazioneRepository.findAll();

        for (Abitazione abitazione : abitazioni){
            energiaDisponibileTotale = energiaDisponibileTotale + (abitazione.getOre() * abitazione.getKwh());
        }

        return energiaDisponibileTotale;
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

    @RequestMapping(value = "/searchByCpu", method = RequestMethod.GET)
    public AbitazioneListDTO searchByCpu(@RequestParam float cpu){
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
    public AbitazioneListDTO searchBySistemaOperativo(@RequestParam String sistemaOperativo){
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
    public AbitazioneListDTO searchByRam(@RequestParam int ram){
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

    // Bisogna fare una get per cercare le caratteristiche minime.
}
