package it.unisalento.recproject.abitazioneservice.RestControllers;

import it.unisalento.recproject.abitazioneservice.domain.VenditaEnergia;
import it.unisalento.recproject.abitazioneservice.dto.*;
import it.unisalento.recproject.abitazioneservice.exceptions.AbitazioneNotFoundException;
import it.unisalento.recproject.abitazioneservice.exceptions.VenditeNotFoudExceptions;
import it.unisalento.recproject.abitazioneservice.repositories.VenditaEnergiaRepository;
import it.unisalento.recproject.abitazioneservice.service.AbitazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/venditaEnergia")
public class VenditaEnergiaRestController {

    @Autowired
    VenditaEnergiaRepository venditaEnergiaRepository;

    @Autowired
    AbitazioneService abitazioneService;

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public VenditaEnergiaDTO acquisto(@RequestBody EnergiaDTO energiaDTO) throws AbitazioneNotFoundException {

         return abitazioneService.scambioEnergia(energiaDTO);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public VenditaEnergiaListDTO getAll() {
        ArrayList<VenditaEnergiaDTO> list = new ArrayList<>();
        VenditaEnergiaListDTO venditaEnergiaList = new VenditaEnergiaListDTO();

        List<VenditaEnergia> venditeEnergia = venditaEnergiaRepository.findAll();

        for (VenditaEnergia venditaEnergia : venditeEnergia){
            VenditaEnergiaDTO venditaEnergiaDTO = new VenditaEnergiaDTO();
            venditaEnergiaDTO.setId(venditaEnergia.getId());
            venditaEnergiaDTO.setViaVenditore(venditaEnergia.getViaVenditore());
            venditaEnergiaDTO.setEmailVenditore(venditaEnergia.getEmailVenditore());
            venditaEnergiaDTO.setViaAcquirente(venditaEnergia.getViaAcquirente());
            venditaEnergiaDTO.setEmailAcquirente(venditaEnergia.getEmailAcquirente());
            venditaEnergiaDTO.setDataAcquisto1(venditaEnergia.getDataAcquisto());
            venditaEnergiaDTO.setEnergiaAcquistata(venditaEnergia.getEnergiaAcquistata());
            venditaEnergiaDTO.setCostoEnergiaAcquistata(venditaEnergia.getCostoEnergiaAcquistata());

            list.add(venditaEnergiaDTO);
        }

        // aggiorniamo la lista e la restituiamo.
        venditaEnergiaList.setList(list);

        return venditaEnergiaList;
    }

    // Ritorna tutti gli acquisti che ha fatto l'utente nel mese corrente.
    @RequestMapping(value = "/allByDateAndEmail/{email}", method = RequestMethod.GET)
    public VenditaEnergiaListDTO getAllByDateAndEmail(@PathVariable String email) throws VenditeNotFoudExceptions {
        ArrayList<VenditaEnergiaDTO> list = new ArrayList<>();
        VenditaEnergiaListDTO venditaEnergiaList = new VenditaEnergiaListDTO();

        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        List<VenditaEnergia> venditeEnergia = venditaEnergiaRepository.findByDataAcquistoIsAfterAndAndEmailAcquirente(startOfMonth, email);

        if(venditeEnergia == null){
            throw new VenditeNotFoudExceptions(email);
        }

        for (VenditaEnergia venditaEnergia : venditeEnergia){
            VenditaEnergiaDTO venditaEnergiaDTO = new VenditaEnergiaDTO();
            venditaEnergiaDTO.setId(venditaEnergia.getId());
            venditaEnergiaDTO.setViaVenditore(venditaEnergia.getViaVenditore());
            venditaEnergiaDTO.setEmailVenditore(venditaEnergia.getEmailVenditore());
            venditaEnergiaDTO.setViaAcquirente(venditaEnergia.getViaAcquirente());
            venditaEnergiaDTO.setEmailAcquirente(venditaEnergia.getEmailAcquirente());
            venditaEnergiaDTO.setDataAcquisto1(venditaEnergia.getDataAcquisto());
            venditaEnergiaDTO.setEnergiaAcquistata(venditaEnergia.getEnergiaAcquistata());
            venditaEnergiaDTO.setCostoEnergiaAcquistata(venditaEnergia.getCostoEnergiaAcquistata());

            list.add(venditaEnergiaDTO);
        }
        // aggiorniamo la lista e la restituiamo.
        venditaEnergiaList.setList(list);

        return venditaEnergiaList;
    }

}


